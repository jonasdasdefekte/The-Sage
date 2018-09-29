package sagemod.relics;

import com.megacrit.cardcrawl.relics.AbstractRelic;

import sagemod.powers.SageFlight;

public class FlyingCarpet extends AbstractSageRelic {

	public static final String ID = "Flying_Carpet";
	public static final RelicTier TIER = RelicTier.STARTER;
	public static final LandingSound SOUND = LandingSound.MAGICAL;
	private static final int FLY_AMT = 1;

	public FlyingCarpet() {
		super(ID, TIER, SOUND);
	}

	@Override
	public void atBattleStart() {
		applyPowerToSelf(new SageFlight(player(), FLY_AMT));
	}

	@Override
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0] + FLY_AMT + DESCRIPTIONS[1];
	}

	@Override
	public AbstractRelic makeCopy() {
		return new FlyingCarpet();
	}

}
