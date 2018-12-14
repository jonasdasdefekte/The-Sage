package sagemod.patches;

import java.util.ArrayList;
import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.random.Random;
import javassist.CtBehavior;
import sagemod.character.TheSage;
import sagemod.events.CarpetTrader;

@SpirePatch(
		clz = AbstractDungeon.class,
		method = "getEvent")
public class PatchForCarpetTrader {
	@SpireInsertPatch(
			locator = Locator.class,
			localvars = {"tmp"})
	public static void Insert(Random rng, ArrayList<String> tmp) {
		if (AbstractDungeon.player instanceof TheSage) {
			tmp.remove(CarpetTrader.ID);
		}
	}

	private static class Locator extends SpireInsertLocator {
		@Override
		public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
			Matcher finalMatcher = new Matcher.MethodCallMatcher(ArrayList.class, "isEmpty");
			return LineFinder.findInOrder(ctMethodToPatch, finalMatcher);
		}
	}
}
