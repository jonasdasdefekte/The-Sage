package sagemod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.CardGroup;
import com.megacrit.cardcrawl.screens.compendium.CardLibraryScreen;
import basemod.patches.com.megacrit.cardcrawl.screens.compendium.CardLibraryScreen.EverythingFix;
import sagemod.cards.AbstractSageCard;
import sagemod.character.SageColorEnum;

@SpirePatch(clz = CardLibraryScreen.class, method = "calculateScrollBounds")
public class PatchForCardLibraryScreen {
	@SpirePrefixPatch
	public static void Prefix(Object obj) {
		CardGroup c = EverythingFix.Fields.cardGroupMap.get(SageColorEnum.THE_SAGE);
		if (c != null) {
			c.group.removeIf(PatchForCardLibraryScreen::shouldNotDisplayInScreen);
		}
	}

	private static boolean shouldNotDisplayInScreen(AbstractCard c) {
		if (c instanceof AbstractSageCard) {
			return ((AbstractSageCard) c).shouldNotDisplayInScreen;
		}
		return false;
	}

}
