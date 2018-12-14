package sagemod.patches;

import com.evacipated.cardcrawl.modthespire.lib.ByRef;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePrefixPatch;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.actions.defect.DoubleEnergyAction;
import com.megacrit.cardcrawl.actions.unique.GainEnergyIfDiscardAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import basemod.ReflectionHacks;
import sagemod.powers.NoEnergyPower;

public class PatchesForNoEnergyPower {

	@SpirePatch(clz = AbstractPlayer.class, method = "gainEnergy")
	public static class AbstractPlayerPatch {
		@SpirePrefixPatch
		public static void PreFix(AbstractPlayer player, @ByRef int[] e) {
			if (AbstractDungeon.player.hasPower(NoEnergyPower.POWER_ID)) {
				e[0] = 0;
				AbstractDungeon.player.getPower(NoEnergyPower.POWER_ID).flash();
			}
		}
	}

	private static void maybeAbortAction(AbstractGameAction action) {
		if (AbstractDungeon.player.hasPower(NoEnergyPower.POWER_ID)) {
			ReflectionHacks.setPrivate(action, AbstractGameAction.class, "duration", -1f);
			AbstractDungeon.player.getPower(NoEnergyPower.POWER_ID).flash();
		}
	}

	@SpirePatch(clz = GainEnergyAction.class, method = SpirePatch.CONSTRUCTOR)
	public static class GainEnergyActionPatch {
		@SpirePostfixPatch
		public static void PostFix(GainEnergyAction action, int amount) {
			maybeAbortAction(action);
		}
	}

	@SpirePatch(clz = GainEnergyIfDiscardAction.class, method = SpirePatch.CONSTRUCTOR)
	public static class GainEnergyIfDiscardActionPatch {
		@SpirePostfixPatch
		public static void PostFix(GainEnergyIfDiscardAction action, int amount) {
			maybeAbortAction(action);
		}
	}

	@SpirePatch(clz = DoubleEnergyAction.class, method = SpirePatch.CONSTRUCTOR)
	public static class DoubleEnergyActionPatch {
		@SpirePostfixPatch
		public static void PostFix(DoubleEnergyAction action) {
			maybeAbortAction(action);
		}
	}

}
