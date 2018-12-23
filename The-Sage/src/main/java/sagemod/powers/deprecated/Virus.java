package sagemod.powers.deprecated;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import sagemod.powers.AbstractSagePower;

@Deprecated
public class Virus extends AbstractSagePower {

	public static final String POWER_ID = "Virus";
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;


	public Virus(AbstractCreature owner, int amount) {
		super(POWER_ID, NAME, owner, -1);
		updateDescription();
		type = AbstractPower.PowerType.DEBUFF;
	}

	@Override
	public void updateDescription() {
		description = DESCRIPTIONS[0];
	}



}
