package sagemod.powers;

import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.RefundFields;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import sagemod.actions.ExecuteLaterAction;

public class BookwormPower extends AbstractSagePower {

	public static final String POWER_ID = "sagemod:Bookworm";
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	private static boolean upgradeNext;

	private int gainEnergy;
	private int refund;
	private int refundBefore = -1;
	private AbstractCard lastCard = null;

	public BookwormPower(AbstractCreature owner, int amount) {
		super(POWER_ID, NAME, owner, amount);
		if (upgradeNext) {
			gainEnergy += amount;
			upgradeNext = false;
		} else {
			refund += amount;
		}
		updateDescription();
		type = AbstractPower.PowerType.BUFF;
	}

	public static void upgradeNext() {
		upgradeNext = true;
	}

	@Override
	public void updateDescription() {
		StringBuilder sb = new StringBuilder();
		sb.append(DESCRIPTIONS[0]);
		if (refund > 0) {
			sb.append(DESCRIPTIONS[1]);
			sb.append(refund);
			sb.append(DESCRIPTIONS[2]);
			if (gainEnergy > 0) {
				sb.append(DESCRIPTIONS[3]);
			}
		}
		if (gainEnergy > 0) {
			if (refund <= 0) {
				sb.append(DESCRIPTIONS[4]);
			}
			for (int i = 0; i < gainEnergy; ++i) {
				sb.append("[E] ");
			}
		}
		sb.append(DESCRIPTIONS[5]);
		description = sb.toString();
	}

	@Override
	public void stackPower(int stackAmount) {
		if (upgradeNext) {
			upgradeNext = false;
			gainEnergy += stackAmount;
		} else {
			refund += stackAmount;
		}
		amount = gainEnergy + refund;
		updateDescription();
	}

	@Override
	public void reducePower(int reduceAmount) {
		if (refund > 0) {
			if (refund >= reduceAmount) {
				refund = 0;
				reduceAmount -= refund;
			} else {
				refund -= reduceAmount;
				reduceAmount = 0;
			}
		}
		if (reduceAmount > 0) {
			if (gainEnergy > 0) {
				if (gainEnergy >= reduceAmount) {
					gainEnergy = 0;
					reduceAmount -= gainEnergy;
				} else {
					gainEnergy -= reduceAmount;
					reduceAmount = 0;
				}
			}
		}
		amount = gainEnergy + refund;
		updateDescription();
	}

	@Override
	public void onUseCard(AbstractCard card, UseCardAction action) {
		super.onUseCard(card, action);
		if (isXCost(card) && refund > 0) {
			refundBefore = RefundFields.refund.get(card);
			if (refundBefore == -1) {
				refundBefore = 0;
			}
			RefundFields.refund.set(card, refundBefore + refund);
			lastCard = card;
			flash();
		}
	}

	@Override
	public void onAfterCardPlayed(AbstractCard usedCard) {
		if (isXCost(usedCard) && gainEnergy > 0) {
			AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(gainEnergy));
			flash();
		}
		if (lastCard != null) {
			final AbstractCard cardToRefund = lastCard;
			final int refBefore = refundBefore;
			AbstractDungeon.actionManager.addToBottom(
					new ExecuteLaterAction(
							() -> RefundFields.refund.set(cardToRefund, refBefore)));
		}
		lastCard = null;
		refundBefore = -1;
	}

	private boolean isXCost(AbstractCard c) {
		return c != null && (c.cost == -1 || c.costForTurn == -1);
	}

}
