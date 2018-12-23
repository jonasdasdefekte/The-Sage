package sagemod.powers.deprecated;

import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import sagemod.powers.AbstractSagePower;

@Deprecated
public class EndlessFearPower extends AbstractSagePower {

	public static final String POWER_ID = "Endless_Fear";
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	// actual implementation in PotionListener
	public EndlessFearPower(AbstractCreature owner, int amount) {
		super(POWER_ID, NAME, owner, -1);
		updateDescription();
	}

	@Override
	public void updateDescription() {
		description = DESCRIPTIONS[0];
	}

}
