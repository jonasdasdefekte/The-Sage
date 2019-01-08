package sagemod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.TutorialStrings;
import com.megacrit.cardcrawl.ui.FtueTip;
import com.megacrit.cardcrawl.ui.FtueTip.TipType;
import sagemod.character.TheSage;
import sagemod.tips.SageTipTracker;

public class PatchesForTips {

	@SpirePatch(clz = AbstractPlayer.class, method = "playDeathAnimation")
	public static class PromoTipEndOfRun {
		@SpirePostfixPatch
		public static void Postfix(AbstractPlayer p) {
			if (p instanceof TheSage && !SageTipTracker.hasShown(SageTipTracker.PROMO)) {
				TutorialStrings tutStrings =
						CardCrawlGame.languagePack.getTutorialString(SageTipTracker.PROMO);
				AbstractDungeon.ftue =
						new FtueTip(tutStrings.LABEL[0], tutStrings.TEXT[0], Settings.WIDTH / 2.0F,
								Settings.HEIGHT / 2.0F, TipType.COMBAT);
				SageTipTracker.neverShowAgain(SageTipTracker.PROMO);
			}
		}

	}
}
