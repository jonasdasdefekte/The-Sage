package sagemod.powers;

import com.evacipated.cardcrawl.mod.stslib.powers.interfaces.InvisiblePower;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;

public class UnderminePower extends AbstractSagePower implements InvisiblePower {

	public static final String POWER_ID = "sagemod:Undermine";
	private static final PowerStrings powerStrings =
			CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;


	public UnderminePower(AbstractCreature owner, int amount) {
		super(POWER_ID, NAME, owner, -1);
		updateDescription();
		type = AbstractPower.PowerType.BUFF;
		isTurnBased = true;
	}

	@Override
	public void updateDescription() {
		description = DESCRIPTIONS[0];
	}

	@Override
	public float atDamageFinalReceive(float damage, DamageInfo.DamageType type) {
		if (type == DamageInfo.DamageType.NORMAL && owner.hasPower(ArtifactPower.POWER_ID)) {
			int artifact = owner.getPower(ArtifactPower.POWER_ID).amount;
			return damage + artifact;
		}
		return damage;
	}

}
