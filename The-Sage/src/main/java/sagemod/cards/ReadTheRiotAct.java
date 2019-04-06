package sagemod.cards;

import java.util.ArrayList;
import java.util.List;
import com.megacrit.cardcrawl.actions.common.DiscardSpecificCardAction;
import com.megacrit.cardcrawl.actions.common.ExhaustSpecificCardAction;
import com.megacrit.cardcrawl.actions.utility.QueueCardAction;
import com.megacrit.cardcrawl.actions.utility.UnlimboAction;
import com.megacrit.cardcrawl.actions.utility.WaitAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ReadTheRiotAct extends AbstractSageCard {

	public static final String ID = "sagemod:Read_The_Riot_Act";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.SKILL;
	private static final CardRarity RARITY = CardRarity.RARE;
	private static final CardTarget TARGET = CardTarget.SELF;
	
	private static final int UPGRADED_COST = 0;

	public ReadTheRiotAct() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeBaseCost(UPGRADED_COST);
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new ReadTheRiotAct();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		List<AbstractCard> list = new ArrayList<>(player().drawPile.group);
		list.removeIf(c -> c.cost != -1);
		if (!list.isEmpty()) {
			AbstractCard card = list.get(AbstractDungeon.cardRandomRng.random(list.size() - 1));
			AbstractCard copy = card.makeStatEquivalentCopy();
			playCard(card, false, AbstractDungeon.getCurrRoom().monsters.getRandomMonster(true));
			playCard(copy, true, AbstractDungeon.getCurrRoom().monsters.getRandomMonster(true));
		}
	}
	
	private void playCard(AbstractCard card, boolean exhaustOnUse, AbstractMonster target) {
		player().drawPile.group.remove(card);
		AbstractDungeon.getCurrRoom().souls.remove(card);
		card.freeToPlayOnce = true;
		card.exhaustOnUseOnce = exhaustOnUse;
		player().limbo.group.add(card);
		card.current_y = -200.0f * Settings.scale;
		card.target_x = Settings.WIDTH / 2.0f + 200.0f * Settings.scale;
		card.target_y = Settings.HEIGHT / 2.0f;
		card.targetAngle = 0.0f;
		card.lighten(false);
		card.drawScale = 0.12f;
		card.targetDrawScale = 0.75f;
		if (!card.canUse(AbstractDungeon.player, target)) {
			if (exhaustOnUse) {
				AbstractDungeon.actionManager.addToTop(
						new ExhaustSpecificCardAction(card, AbstractDungeon.player.limbo));
			} else {
				AbstractDungeon.actionManager.addToTop(new UnlimboAction(card));
				AbstractDungeon.actionManager.addToTop(
						new DiscardSpecificCardAction(card, AbstractDungeon.player.limbo));
				AbstractDungeon.actionManager.addToTop(new WaitAction(0.4f));
			}
		} else {
			card.applyPowers();
			AbstractDungeon.actionManager.addToTop(new QueueCardAction(card, target));
			AbstractDungeon.actionManager.addToTop(new UnlimboAction(card));
			if (!Settings.FAST_MODE) {
				AbstractDungeon.actionManager.addToTop(new WaitAction(Settings.ACTION_DUR_MED));
			} else {
				AbstractDungeon.actionManager
						.addToTop(new WaitAction(Settings.ACTION_DUR_FASTER));
			}
		}
	}

	@Override
	public String getLoadedDescription() {
		return upgraded ? cardStrings.UPGRADE_DESCRIPTION : DESCRIPTION;
	}

}
