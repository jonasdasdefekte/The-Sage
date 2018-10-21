package sagemod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;

import sagemod.SageMod;
import sagemod.powers.Brew;

@SpirePatch(clz = com.megacrit.cardcrawl.dungeons.AbstractDungeon.class, method = "closeCurrentScreen")
public class AbstractDungeonPatches {
	// delete all potions if the player closes the brew rewards
	@SpirePrefixPatch
	public static void Prefix() {
		if (Brew.isBrewRewards) {
			Brew.isBrewRewards = false;
			SageMod.logger.info("Closing Brew Rewards and deleting excess potions");
			Brew.removePotionsFromRewards();
		}
	}
}
