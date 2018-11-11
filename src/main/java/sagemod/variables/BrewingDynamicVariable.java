package sagemod.variables;

import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.Settings;

import basemod.abstracts.DynamicVariable;
import sagemod.cards.AbstractSageCard;

public class BrewingDynamicVariable extends DynamicVariable {

	@Override
	public String key() {
		return "BRW";
	}

	@Override
	public boolean isModified(AbstractCard card) {
		if (card instanceof AbstractSageCard) {
			return ((AbstractSageCard) card).isBrewInModified;
		}
		return false;
	}

	@Override
	public int value(AbstractCard card) {
		if (card instanceof AbstractSageCard) {
			return ((AbstractSageCard) card).brewIn;
		}
		return 0;
	}

	@Override
	public int baseValue(AbstractCard card) {
		if (card instanceof AbstractSageCard) {
			return ((AbstractSageCard) card).baseBrewIn;
		}
		return 0;
	}

	@Override
	public boolean upgraded(AbstractCard card) {
		if (card instanceof AbstractSageCard) {
			return ((AbstractSageCard) card).upgradedBrewIn;
		}
		return false;
	}

	public Color getUpgradedColor() {
		return Settings.GREEN_TEXT_COLOR;
	}

	public Color getIncreasedValueColor() {
		return Settings.RED_TEXT_COLOR;
	}

	public Color getDecreasedValueColor() {
		return Settings.GREEN_TEXT_COLOR;
	}

}
