package sagemod.patches;

import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon.CurrentScreen;
import com.megacrit.cardcrawl.ui.buttons.CancelButton;
import javassist.CtBehavior;
import sagemod.powers.Brew;

public class PatchesForBrewScreen {
	private static Logger logger = LogManager.getLogger(PatchesForBrewScreen.class);

	@SpirePatch(clz = CancelButton.class, method = "update")
	public static class CancelButtonPatches {

		@SpireInsertPatch(locator = Locator.class)
		public static void Insert(Object o) {
			if (AbstractDungeon.screen == CurrentScreen.COMBAT_REWARD && Brew.isBrewRewards) {
				Brew.isBrewRewards = false;
				logger.info("Closing Brew Rewards and deleting excess potions");
				Brew.removePotionsFromRewards();
			}
		}

		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
				return LineFinder.findAllInOrder(ctMethodToPatch, new ArrayList<Matcher>(),
						new Matcher.MethodCallMatcher(CancelButton.class, "hide"));
			}
		}
	}
}
