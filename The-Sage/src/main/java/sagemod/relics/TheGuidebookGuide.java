package sagemod.relics;

import com.evacipated.cardcrawl.mod.stslib.fields.cards.AbstractCard.RefundFields;
import com.evacipated.cardcrawl.mod.stslib.relics.OnAfterUseCardRelic;
import com.megacrit.cardcrawl.actions.utility.UseCardAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import sagemod.actions.ExecuteLaterAction;

public class TheGuidebookGuide extends AbstractSageRelic implements OnAfterUseCardRelic {

	public static final String ID = "sagemod:The_Guidebook_Guide";
	public static final RelicTier TIER = RelicTier.SHOP;
	public static final LandingSound SOUND = LandingSound.MAGICAL;

	private AbstractCard lastCard;
	private int refundBefore = -1;

	public TheGuidebookGuide() {
		super(ID, TIER, SOUND);
	}

	@Override
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0];
	}

	@Override
	public AbstractRelic makeCopy() {
		return new TheGuidebookGuide();
	}


	@Override
	public void onUseCard(AbstractCard card, UseCardAction useCardAction) {
		super.onUseCard(card, useCardAction);
		if (card != null && (card.cost == -1 || card.costForTurn == -1)) {
			lastCard = card;
			refundBefore = RefundFields.refund.get(card);
			// refund all
			RefundFields.refund.set(card, 1000);
			useCardAction.exhaustCard = true;
		}
	}

	@Override
	public void onAfterUseCard(AbstractCard card, UseCardAction action) {
		if (lastCard != null) {
			final AbstractCard cardToRefund = lastCard;
			final int refBefore = refundBefore;
			AbstractDungeon.actionManager.addToBottom(
					new ExecuteLaterAction(
							() -> RefundFields.refund.set(cardToRefund, refBefore)));
			lastCard = null;
			refundBefore = -1;
			flash();
			appearAbove(AbstractDungeon.player);
		}
	}

}
