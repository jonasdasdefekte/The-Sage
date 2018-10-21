package sagemod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;

@SpirePatch(clz = com.megacrit.cardcrawl.ui.buttons.DynamicButton.class, method = "hide")
public class DynamicButtonBrewPatch {
	// delete all potions if the player closes the brew rewards
	@SpirePrefixPatch
	public static void Prefix(Object o) {
		// if (Brew.isBrewRewards) {
		// Brew.isBrewRewards = false;
		// SageMod.logger.info("Closing Brew Rewards and deleting excess potions");
		// Brew.removePotionsFromRewards();
		// }
	}
}
