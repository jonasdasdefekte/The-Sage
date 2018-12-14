package sagemod.variables;

import com.megacrit.cardcrawl.cards.AbstractCard;

import basemod.abstracts.DynamicVariable;

public class MiscDynamicVariable extends DynamicVariable {

	@Override
	public String key() {
		return "MISC";
	}

	@Override
	public boolean isModified(AbstractCard card) {
		return false;
	}

	@Override
	public int value(AbstractCard card) {
		return card.misc;
	}

	@Override
	public int baseValue(AbstractCard card) {
		return card.misc;
	}

	@Override
	public boolean upgraded(AbstractCard card) {
		return false;
	}

}
