package sagemod.relics;

import com.megacrit.cardcrawl.relics.AbstractRelic;
import sagemod.powers.Flight;

public class ThunderCarpet extends AbstractSageRelic {

	public static final String ID = "sagemod:Thunder_Carpet";
	public static final RelicTier TIER = RelicTier.BOSS;
	public static final LandingSound SOUND = LandingSound.MAGICAL;
	private static final int FLY_AMT = 3;

	public ThunderCarpet() {
		super(ID, TIER, SOUND);
	}

	@Override
	public void obtain() {
		if (player() != null && player().hasRelic(FlyingCarpet.ID)) {
			int index = player().relics.indexOf(player().getRelic(FlyingCarpet.ID));
			instantObtain(player(), index, true);
		} else {
			super.obtain();
		}
	}


	@Override
	public void atBattleStartPreDraw() {
		flash();
		appearAbove(player());
		applyPowerToSelf(new Flight(player(), FLY_AMT));
	}

	@Override
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0] + FLY_AMT + DESCRIPTIONS[1];
	}

	@Override
	public AbstractRelic makeCopy() {
		return new ThunderCarpet();
	}

}
