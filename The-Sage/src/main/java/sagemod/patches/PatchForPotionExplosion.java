package sagemod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import sagemod.listeners.PotionListener;

@SpirePatch(clz = AbstractPlayer.class, method = "applyStartOfTurnRelics")
public class PatchForPotionExplosion {

	@SpirePostfixPatch
	public static void Postfix(AbstractPlayer p) {
		PotionListener.potionsUsed = 0;
	}
}
