package sagemod.patches;

import java.util.ArrayList;
import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher.FieldAccessMatcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.actions.unique.DiscoveryAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.screens.CardRewardScreen;
import javassist.CtBehavior;
import sagemod.potions.UpgradedPotion;

public class PatchesForDiscoveryUpgradedPotions {

	private static class Locator extends SpireInsertLocator {

		@Override
		public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
			return LineFinder.findInOrder(ctMethodToPatch,
					new FieldAccessMatcher(CardRewardScreen.class, "rewardGroup"));
		}

	}

	@SpirePatch(clz = CardRewardScreen.class, method = "discoveryOpen", paramtypez = {})
	public static class NoParams {
		@SpireInsertPatch(locator = Locator.class, localvars = {"derp"})
		public static void Insert(Object o, ArrayList<AbstractCard> derp) {
			if (UpgradedPotion.discoveryOpenUpgrades > 0) {
				UpgradedPotion.discoveryOpenUpgrades--;
				for (AbstractCard card : derp) {
					card.upgrade();
				}
			}
		}
	}
	
	@SpirePatch(clz = CardRewardScreen.class, method = "discoveryOpen", paramtypez = {AbstractCard.CardType.class})
	public static class TypeParams {
		@SpireInsertPatch(locator = Locator.class, localvars = {"derp"})
		public static void Insert(Object o, Object type, ArrayList<AbstractCard> derp) {
			if (UpgradedPotion.discoveryOpenUpgrades > 0) {
				UpgradedPotion.discoveryOpenUpgrades--;
				for (AbstractCard card : derp) {
					card.upgrade();
				}
			}
		}
	}
	
	@SpirePatch(clz = DiscoveryAction.class, method = "generateColorlessCardChoices")
	public static class DiscoveryActionColorless {
		@SpirePostfixPatch
		public static ArrayList<AbstractCard> Postfix(ArrayList<AbstractCard> cards, DiscoveryAction action) {
			if (UpgradedPotion.discoveryOpenUpgrades > 0) {
				UpgradedPotion.discoveryOpenUpgrades--;
				for (AbstractCard card : cards) {
					card.upgrade();
				}
			}
			return cards;
		}
	}
	
	@SpirePatch(clz = DiscoveryAction.class, method = "generateCardChoices", paramtypez = {CardType.class})
	public static class DiscoveryActionColor {
		@SpirePostfixPatch
		public static ArrayList<AbstractCard> Postfix(ArrayList<AbstractCard> cards, DiscoveryAction action, CardType type) {
			if (UpgradedPotion.discoveryOpenUpgrades > 0) {
				UpgradedPotion.discoveryOpenUpgrades--;
				for (AbstractCard card : cards) {
					card.upgrade();
				}
			}
			return cards;
		}
	}
	
}
