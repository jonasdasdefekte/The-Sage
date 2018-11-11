package sagemod.relics;

import com.megacrit.cardcrawl.relics.AbstractRelic;

public class Blowpipe extends AbstractSageRelic {

	public static final String ID = "Blowpipe";
	public static final RelicTier TIER = RelicTier.RARE;
	public static final LandingSound SOUND = LandingSound.MAGICAL;

	// Implementation in potion listener
	public Blowpipe() {
		super(ID, TIER, SOUND);
	}

	@Override
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0];
	}

	@Override
	public AbstractRelic makeCopy() {
		return new Blowpipe();
	}

}
