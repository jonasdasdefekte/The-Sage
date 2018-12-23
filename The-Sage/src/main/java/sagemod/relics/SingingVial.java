package sagemod.relics;

import com.megacrit.cardcrawl.relics.AbstractRelic;

public class SingingVial extends AbstractSageRelic {

	public static final String ID = "Singing_Vial";
	public static final RelicTier TIER = RelicTier.UNCOMMON;
	public static final LandingSound SOUND = LandingSound.MAGICAL;

	public SingingVial() {
		super(ID, TIER, SOUND);
	}

	@Override
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0];
	}

	@Override
	public AbstractRelic makeCopy() {
		return new SingingVial();
	}

}
