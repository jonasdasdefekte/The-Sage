package sagemod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class Brewing extends AbstractSagePower {

	public static final String POWER_ID = "Brewing";
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	/**
	 * actual implementation in Brew
	 */
	public Brewing(AbstractCreature owner, int amount) {
		super(POWER_ID, NAME, owner, amount);
		updateDescription();
		type = AbstractPower.PowerType.BUFF;
	}

	@Override
	public void updateDescription() {
		if (amount > 1) {
			description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
		} else {
			description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[2];
		}
	}

}
