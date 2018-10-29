package sagemod.relics;

import com.megacrit.cardcrawl.relics.AbstractRelic;

import sagemod.powers.Brewing;

public class CookingBook extends AbstractSageRelic {

	public static final String ID = "Cooking_Book";
	public static final RelicTier TIER = RelicTier.COMMON;
	public static final LandingSound SOUND = LandingSound.FLAT;
	private static final int BREWING_AMT = 1;

	public CookingBook() {
		super(ID, TIER, SOUND);
	}

	@Override
	public void atBattleStartPreDraw() {
		applyPowerToSelf(new Brewing(player(), BREWING_AMT));
		flash();
		appearAbove(player());
	}

	@Override
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0] + BREWING_AMT + DESCRIPTIONS[1];
	}

	@Override
	public AbstractRelic makeCopy() {
		return new CookingBook();
	}

}
