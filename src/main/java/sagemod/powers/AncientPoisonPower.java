package sagemod.powers;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class AncientPoisonPower extends AbstractSagePower {

	public static final String POWER_ID = "Ancient_Poison";
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	// Implementation in PatchesForAncientPoison
	public AncientPoisonPower(AbstractCreature owner, int amount) {
		super(POWER_ID, NAME, owner, amount);
		type = AbstractPower.PowerType.BUFF;
		amount = -1;
		updateDescription();
	}

	@Override
	public void updateDescription() {
		description = DESCRIPTIONS[0];
	}

}
