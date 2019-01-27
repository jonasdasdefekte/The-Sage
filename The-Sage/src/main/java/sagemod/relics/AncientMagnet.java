package sagemod.relics;

import com.megacrit.cardcrawl.relics.AbstractRelic;

public class AncientMagnet extends AbstractSageRelic {

	public static final String ID = "sagemod:Ancient_Magnet";
	public static final RelicTier TIER = RelicTier.UNCOMMON;
	public static final LandingSound SOUND = LandingSound.CLINK;

	// Actual Implementation in PatchesForAncientMagnet
	public AncientMagnet() {
		super(ID, TIER, SOUND);
	}

	@Override
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0];
	}

	@Override
	public AbstractRelic makeCopy() {
		return new AncientMagnet();
	}

}
