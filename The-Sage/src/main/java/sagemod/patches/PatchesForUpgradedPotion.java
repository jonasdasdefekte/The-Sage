package sagemod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PotionHelper;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.AbstractPotion.PotionRarity;
import com.megacrit.cardcrawl.potions.FirePotion;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile;
import com.megacrit.cardcrawl.saveAndContinue.SaveFile.SaveType;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import javassist.expr.MethodCall;
import sagemod.potions.UpgradedPotion;
import sagemod.relics.RedBeastStatue;


public class PatchesForUpgradedPotion {

	public static String getIDForSaving(AbstractPotion potion) {
		if (potion instanceof UpgradedPotion) {
			return potion.ID + UpgradedPotion.SAVE_POSTFIX;
		} else if (potion != null) {
			return potion.ID;
		} else {
			return FirePotion.POTION_ID;
		}
	}

	public static AbstractPotion getPotionForLoading(String id) {
		if (id == null) {
			return new FirePotion();
		}
		if (id.endsWith(UpgradedPotion.SAVE_POSTFIX)) {
			String realId = id.substring(0, id.indexOf(UpgradedPotion.SAVE_POSTFIX));
			return UpgradedPotion.getUpgradeIfAvailable(PotionHelper.getPotion(realId));
		} else {
			return PotionHelper.getPotion(id);
		}
	}

	@SpirePatch(clz = AbstractDungeon.class, method = "returnRandomPotion",
			paramtypez = {PotionRarity.class, boolean.class})
	public static class ObtainUpgradedChancePatch {

		@SpirePostfixPatch
		public static AbstractPotion Postfix(AbstractPotion potion, PotionRarity rarity,
				boolean limited) {
			if (potion == null) {
				return null;
			}
			float chance = UpgradedPotion.CHANCE;
			if (AbstractDungeon.player != null
					&& AbstractDungeon.player.hasRelic(RedBeastStatue.ID)) {
				chance *= (1 + RedBeastStatue.toPercentage(RedBeastStatue.PERCENTAGE_INCREASE));
			}
			if (AbstractDungeon.potionRng.randomBoolean(chance)) {
				return UpgradedPotion.getUpgradeIfAvailable(potion);
			} else {
				return potion;
			}
		}
	}

	@SpirePatch(clz = SaveFile.class, method = SpirePatch.CONSTRUCTOR,
			paramtypez = {SaveType.class})
	public static class SaveUpgradedPatch {
		public static ExprEditor Instrument() {
			return new ExprEditor() {
				@Override
				public void edit(FieldAccess f) throws CannotCompileException {
					if (f.getFieldName().equals("ID")
							&& f.getClassName().equals(AbstractPotion.class.getName())) {
						f.replace(
								"{ $_ = sagemod.patches.PatchesForUpgradedPotion.getIDForSaving($0); }");
					}
				}
			};

		}
	}

	@SpirePatch(clz = CardCrawlGame.class, method = "loadPlayerSave")
	public static class LoadUpgradedPatch {
		public static ExprEditor Instrument() {
			return new ExprEditor() {
				@Override
				public void edit(MethodCall m) throws CannotCompileException {
					if (m.getMethodName().equals("getPotion")
							&& m.getClassName().equals(PotionHelper.class.getName())) {
						m.replace(
								"{ $_ = sagemod.patches.PatchesForUpgradedPotion.getPotionForLoading($1); }");
					}
				}
			};

		}
	}

}
