package sagemod.powers;

import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.RefundFields;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import sagemod.actions.ExecuteLaterAction;


public class TrixterPower extends AbstractSagePower {

	public static final String POWER_ID = "Trixter";
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	private AbstractCard lastCard;
	private int refundBefore = -1;

	public TrixterPower(AbstractCreature owner, int amount) {
		super(POWER_ID, NAME, owner, amount);
		type = AbstractPower.PowerType.BUFF;
		updateDescription();
	}

	@Override
	public void updateDescription() {
		if (amount == 1) {
			description = DESCRIPTIONS[0];
		} else {
			description = DESCRIPTIONS[1] + amount + DESCRIPTIONS[2];
		}
	}

	@Override
	public void onUseCard(AbstractCard card, UseCardAction action) {
		super.onUseCard(card, action);
		if (card != null) {
			lastCard = card;
			refundBefore = RefundFields.refund.get(card);
			// refund all
			RefundFields.refund.set(card, 1000);
			flash();
		}
	}

	@Override
	public void onAfterCardPlayed(AbstractCard usedCard) {
		super.onAfterCardPlayed(usedCard);
		if (lastCard != null) {
			final AbstractCard cardToRefund = lastCard;
			final int refBefore = refundBefore;
			AbstractDungeon.actionManager.addToBottom(
					new ExecuteLaterAction(
							() -> resetRefundAndDecrease(cardToRefund, refBefore)));
			lastCard = null;
			refundBefore = -1;
		}
	}

	private void resetRefundAndDecrease(AbstractCard c, int i) {
		RefundFields.refund.set(c, i);
		AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(owner, owner, this, 1));
	}

}
