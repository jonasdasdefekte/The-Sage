package sagemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.EmptyDeckShuffleAction;
import com.megacrit.cardcrawl.actions.utility.QueueCardAction;
import com.megacrit.cardcrawl.actions.utility.UnlimboAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class PerpetuumMobileAction extends AbstractGameAction {

	AbstractPlayer player;
	int forCost;
	boolean upgraded;

	public PerpetuumMobileAction(AbstractCreature target, int forCost, boolean upgraded) {
		duration = Settings.ACTION_DUR_FAST;
		actionType = ActionType.WAIT;
		source = player = AbstractDungeon.player;
		this.target = target;
		this.forCost = forCost;
		this.upgraded = upgraded;
	}

	@Override
	public void update() {
		// mostly copied from Havoc's PlayTopCardAction
		if (duration == Settings.ACTION_DUR_FAST) {
			if (AbstractDungeon.player.drawPile.size()
					+ AbstractDungeon.player.discardPile.size() == 0) {
				isDone = true;
				return;
			}

			if (AbstractDungeon.player.drawPile.isEmpty()) {
				AbstractDungeon.actionManager
						.addToTop(new PerpetuumMobileAction(target, forCost, upgraded));
				AbstractDungeon.actionManager.addToTop(new EmptyDeckShuffleAction());
				isDone = true;
				return;
			} else {
				AbstractCard card = AbstractDungeon.player.drawPile.getTopCard();

				if (card.cost == forCost || (upgraded && card.cost == -1)) {
					boolean freeToPlayBefore = card.freeToPlayOnce;
					card.freeToPlayOnce = true;
					if (!card.canUse(AbstractDungeon.player, (AbstractMonster) target)) {
						card.freeToPlayOnce = freeToPlayBefore;
						AbstractDungeon.actionManager.addToTop(new DrawCardAction(player, 1));
						AbstractDungeon.actionManager.addToTop(new WaitAction(0.4F));
						isDone = true;
						return;
					}
					AbstractDungeon.player.limbo.group.add(card);
					card.current_y = (-200.0F * Settings.scale);
					card.target_x = (Settings.WIDTH / 2.0F + 200.0F * Settings.scale);
					card.target_y = (Settings.HEIGHT / 2.0F);
					card.targetAngle = 0.0F;
					card.lighten(false);
					card.drawScale = 0.12F;
					card.targetDrawScale = 0.75F;
					AbstractDungeon.player.drawPile.group.remove(card);
					// Not sure what this does
					AbstractDungeon.getCurrRoom().souls.remove(card);
					card.applyPowers();
					AbstractDungeon.actionManager.addToTop(new QueueCardAction(card, target));
					AbstractDungeon.actionManager.addToTop(new UnlimboAction(card));
					if (!Settings.FAST_MODE) {
						AbstractDungeon.actionManager
								.addToTop(new WaitAction(Settings.ACTION_DUR_MED));
					} else {
						AbstractDungeon.actionManager
								.addToTop(new WaitAction(Settings.ACTION_DUR_FASTER));
					}
				} else {
					AbstractDungeon.actionManager.addToTop(new DrawCardAction(player, 1));
					AbstractDungeon.actionManager.addToTop(new WaitAction(0.4F));
				}
			}
			isDone = true;
		}
	}
}
