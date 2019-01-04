package sagemod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.AbstractPotion.PotionRarity;
import sagemod.potions.UpgradedPotion;
import sagemod.relics.RedBeastStatue;


public class PatchesForUpgradedPotion {

	@SpirePatch(clz = AbstractDungeon.class, method = "returnRandomPotion",
			paramtypez = {PotionRarity.class, boolean.class})
	public static class ObtainUpgradedChancePatch {

		@SpirePostfixPatch
		public static AbstractPotion Postfix(AbstractPotion potion, PotionRarity rarity,
				boolean limited) {
			if (potion == null) {
				return null;
			}
			float chance = UpgradedPotion.CHANCE;
			if (AbstractDungeon.player != null
					&& AbstractDungeon.player.hasRelic(RedBeastStatue.ID)) {
				chance *= (1
						+ RedBeastStatue.toPercentage(RedBeastStatue.PERCENTAGE_INCREASE_UPGRADED));
			}
			if (AbstractDungeon.potionRng.randomBoolean(chance)) {
				return UpgradedPotion.getUpgradeIfAvailable(potion);
			} else {
				return potion;
			}
		}
	}

}
