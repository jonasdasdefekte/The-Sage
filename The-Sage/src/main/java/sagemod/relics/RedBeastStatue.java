package sagemod.relics;

import com.megacrit.cardcrawl.helpers.PotionHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class RedBeastStatue extends AbstractSageRelic {

	public static final String ID = "sagemod:Red_Beast_Statue";
	public static final RelicTier TIER = RelicTier.UNCOMMON;
	public static final LandingSound SOUND = LandingSound.SOLID;

	public static final int PERCENTAGE_INCREASE_RARITY = 50;
	public static final int PERCENTAGE_INCREASE_UPGRADED = 100;

	private static final int POTION_RARE_CHANCE = 100
			- (PotionHelper.POTION_COMMON_CHANCE + PotionHelper.POTION_UNCOMMON_CHANCE);

	// actual Implementation in PatchForRedBeastStatue and PatchesForUpgradedPotion
	public RedBeastStatue() {
		super(ID, TIER, SOUND);
	}

	@Override
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0] + PERCENTAGE_INCREASE_RARITY + DESCRIPTIONS[1]
				+ PERCENTAGE_INCREASE_UPGRADED
				+ DESCRIPTIONS[2];
	}

	@Override
	public AbstractRelic makeCopy() {
		return new RedBeastStatue();
	}

	public static float toPercentage(int i) {
		return i / 100f;
	}

	public static float getCommonChanceModifier(float percentage) {
		return 100f / PotionHelper.POTION_COMMON_CHANCE - (PotionHelper.POTION_UNCOMMON_CHANCE + POTION_RARE_CHANCE)
				* percentage / PotionHelper.POTION_COMMON_CHANCE;
	}

}
