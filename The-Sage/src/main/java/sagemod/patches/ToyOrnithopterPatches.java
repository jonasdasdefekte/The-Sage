package sagemod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.HealAction;
import com.megacrit.cardcrawl.actions.common.RelicAboveCreatureAction;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.ToyOrnithopter;
import com.megacrit.cardcrawl.ui.panels.PotionPopUp;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.MethodCall;
import sagemod.character.TheSage;

public class ToyOrnithopterPatches {

	public static final String RELIC_ID = "sagemod:Toy_Ornithopter";

	@SpirePatch(clz = ToyOrnithopter.class, method = "getUpdatedDescription")
	public static class DescriptionPatch {
		public static String Replace(ToyOrnithopter relic) {
			if (AbstractDungeon.player instanceof TheSage) {
				String[] DESCRIPTIONS =
						CardCrawlGame.languagePack.getRelicStrings(RELIC_ID).DESCRIPTIONS;
				return DESCRIPTIONS[0] + ToyOrnithopter.HEAL_AMT + DESCRIPTIONS[1];
			} else {
				return relic.DESCRIPTIONS[0] + ToyOrnithopter.HEAL_AMT + relic.DESCRIPTIONS[1];
			}
		}
	}

	@SpirePatch(clz = ToyOrnithopter.class, method = "onUsePotion")
	public static class UsePotionPatch {
		public static void Replace(ToyOrnithopter relic) {
			if (!(AbstractDungeon.player instanceof TheSage)) {
				relic.flash();
				AbstractDungeon.actionManager
						.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, relic));
				AbstractDungeon.actionManager.addToBottom(
						new HealAction(AbstractDungeon.player, AbstractDungeon.player,
								ToyOrnithopter.HEAL_AMT));
			}
		}
	}

	@SpirePatch(clz = PotionPopUp.class, method = "updateInput")
	public static class DiscardPotionPatch {
		private static int counter = 0;

		private static final int SECOND_PLAY = 2;


		public static ExprEditor Instrument() {
			return new ExprEditor() {
				@Override
				public void edit(MethodCall m) throws CannotCompileException {
					if (m.getClassName().equals("com.megacrit.cardcrawl.audio.SoundMaster")
							&& m.getMethodName().equals("play")) {
						counter++;
						if (counter == SECOND_PLAY) {
							m.replace(
									"{ sagemod.patches.ToyOrnithopterPatches.healOnDiscardCheck(); $_ = $proceed($$); }");
						}
					}
				}
			};
		}
	}

	public static void healOnDiscardCheck() {
		if (AbstractDungeon.player.hasRelic(ToyOrnithopter.ID)
				&& AbstractDungeon.player instanceof TheSage) {
			ToyOrnithopter relic =
					(ToyOrnithopter) AbstractDungeon.player.getRelic(ToyOrnithopter.ID);
			relic.flash();
			AbstractDungeon.actionManager
					.addToBottom(new RelicAboveCreatureAction(AbstractDungeon.player, relic));
			AbstractDungeon.actionManager.addToBottom(
					new HealAction(AbstractDungeon.player, AbstractDungeon.player,
							ToyOrnithopter.HEAL_AMT));
		}
	}
}
