package sagemod.relics;

import com.megacrit.cardcrawl.relics.AbstractRelic;
import sagemod.powers.SageFlight;

public class FlyingCarpet extends AbstractSageRelic {

	public static final String ID = "Flying_Carpet";
	public static final RelicTier TIER = RelicTier.STARTER;
	public static final LandingSound SOUND = LandingSound.MAGICAL;
	public static final int FLY_AMT = 1;
	private static final int GAIN_FLIGHT_TURN = 2;

	private static int turns;

	public FlyingCarpet() {
		super(ID, TIER, SOUND);
		turns = 0;
	}

	@Override
	public void atBattleStartPreDraw() {
		turns = 0;
	}

	@Override
	public void atTurnStart() {
		turns++;
		if (turns == GAIN_FLIGHT_TURN) {
			applyPowerToSelf(new SageFlight(player(), FLY_AMT));
			flash();
			appearAbove(player());
		}
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
