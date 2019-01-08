package sagemod.variables;

import com.megacrit.cardcrawl.cards.AbstractCard;
import basemod.abstracts.DynamicVariable;
import sagemod.cards.AbstractSageCard;

public class MiscDynamicVariable extends DynamicVariable {

	@Override
	public String key() {
		return "sagemod:MISC";
	}

	@Override
	public boolean isModified(AbstractCard card) {
		if (card instanceof AbstractSageCard) {
			return isModifiedSage((AbstractSageCard) card);
		}
		return false;
	}

	private boolean isModifiedSage(AbstractSageCard card) {
		return card.showSageMiscAsModified || card.sageMisc != card.baseSageMisc;
	}

	@Override
	public int value(AbstractCard card) {
		if (card instanceof AbstractSageCard) {
			return valueSage((AbstractSageCard) card);
		}
		return -1;
	}

	private int valueSage(AbstractSageCard card) {
		return card.sageMisc;
	}

	@Override
	public int baseValue(AbstractCard card) {
		if (card instanceof AbstractSageCard) {
			return baseValueSage((AbstractSageCard) card);
		}
		return -1;
	}

	private int baseValueSage(AbstractSageCard card) {
		return card.baseSageMisc;
	}

	@Override
	public void setIsModified(AbstractCard card, boolean v) {
		if (card instanceof AbstractSageCard) {
			((AbstractSageCard) card).showSageMiscAsModified = v;
		}
	}

	@Override
	public boolean upgraded(AbstractCard card) {
		if (card instanceof AbstractSageCard) {
			return upgradedSage((AbstractSageCard) card);
		}
		return false;
	}

	private boolean upgradedSage(AbstractSageCard card) {
		return card.upgradedSageMisc;
	}

}
