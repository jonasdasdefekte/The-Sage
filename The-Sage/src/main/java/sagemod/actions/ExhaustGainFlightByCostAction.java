package sagemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.UIStrings;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import sagemod.powers.Flight;

public class ExhaustGainFlightByCostAction
extends AbstractGameAction {
	private static final UIStrings uiStrings =
			CardCrawlGame.languagePack.getUIString("ExhaustAction");
	public static final String[] TEXT = ExhaustGainFlightByCostAction.uiStrings.TEXT;
	private AbstractPlayer p;
	private boolean isRandom;
	private boolean anyNumber;
	private boolean canPickZero = false;
	public static int numExhausted;

	public ExhaustGainFlightByCostAction(AbstractCreature target, AbstractCreature source,
			int amount, boolean isRandom) {
		this(target, source, amount, isRandom, false, false);
	}

	public ExhaustGainFlightByCostAction(AbstractCreature target, AbstractCreature source,
			int amount, boolean isRandom, boolean anyNumber, boolean canPickZero) {
		this.anyNumber = anyNumber;
		this.canPickZero = canPickZero;
		p = (AbstractPlayer) target;
		this.isRandom = isRandom;
		this.setValues(target, source, amount);
		duration = Settings.ACTION_DUR_FAST;
		actionType = AbstractGameAction.ActionType.EXHAUST;
	}

	public ExhaustGainFlightByCostAction(AbstractCreature target, AbstractCreature source,
			int amount, boolean isRandom, boolean anyNumber) {
		this(target, source, amount, isRandom, anyNumber, false);
	}

	@Override
	public void update() {
		if (duration == Settings.ACTION_DUR_FAST) {
			if (p.hand.size() == 0) {
				isDone = true;
				return;
			}
			if (!anyNumber && p.hand.size() <= amount) {
				int flightGain = 0;
				numExhausted = amount = p.hand.size();
				int tmp = p.hand.size();
				for (int i = 0; i < tmp; ++i) {
					AbstractCard c = p.hand.getTopCard();
					flightGain += getCost(c);
					p.hand.moveToExhaustPile(c);
				}
				if (flightGain != 0) {
					AbstractDungeon.actionManager.addToBottom(
							new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
									new Flight(AbstractDungeon.player, flightGain),
									flightGain));
				}
				CardCrawlGame.dungeon.checkForPactAchievement();
				return;
			}
			if (isRandom) {
				int flightGain = 0;
				for (int i = 0; i < amount; ++i) {
					AbstractCard c = p.hand.getRandomCard(AbstractDungeon.cardRandomRng);
					flightGain += getCost(c);
					p.hand.moveToExhaustPile(c);
				}
				if (flightGain != 0) {
					AbstractDungeon.actionManager.addToBottom(
							new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
									new Flight(AbstractDungeon.player, flightGain),
									flightGain));
				}
				CardCrawlGame.dungeon.checkForPactAchievement();
			} else {
				numExhausted = amount;
				AbstractDungeon.handCardSelectScreen.open(TEXT[0], amount, anyNumber, canPickZero);
				tickDuration();
				return;
			}
		}
		if (!AbstractDungeon.handCardSelectScreen.wereCardsRetrieved) {
			int flightGain = 0;
			for (AbstractCard c : AbstractDungeon.handCardSelectScreen.selectedCards.group) {
				flightGain += getCost(c);
				p.hand.moveToExhaustPile(c);
			}
			if (flightGain != 0) {
				AbstractDungeon.actionManager.addToBottom(
						new ApplyPowerAction(AbstractDungeon.player, AbstractDungeon.player,
								new Flight(AbstractDungeon.player, flightGain), flightGain));
			}
			CardCrawlGame.dungeon.checkForPactAchievement();
			AbstractDungeon.handCardSelectScreen.wereCardsRetrieved = true;
		}
		tickDuration();
	}

	private int getCost(AbstractCard c) {
		if (c.costForTurn == -2) {
			return 0;
		} else if (c.cost == -2) {
			return 0;
		} else if (c.costForTurn == -1) {
			return EnergyPanel.totalCount;
		} else if (c.cost == -1) {
			return EnergyPanel.totalCount;
		} else {
			return c.costForTurn;
		}
	}
}