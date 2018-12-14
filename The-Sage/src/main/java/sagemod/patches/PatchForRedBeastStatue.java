package sagemod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PotionHelper;

import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import sagemod.relics.RedBeastStatue;

@SpirePatch(clz = AbstractDungeon.class, method = "returnRandomPotion", paramtypez = { boolean.class })
public class PatchForRedBeastStatue {
	public static ExprEditor Instrument() {
		return new ExprEditor() {
			@Override
			public void edit(FieldAccess f) throws CannotCompileException {
				if (f.getFieldName().equals("POTION_COMMON_CHANCE")) {
					f.replace("{ $_ = sagemod.patches.PatchForRedBeastStatue.getCommonChance(); }");
				} else if (f.getFieldName().equals("POTION_UNCOMMON_CHANCE")) {
					f.replace("{ $_ = sagemod.patches.PatchForRedBeastStatue.getUncommonChance(); }");
				}
			}
		};

	}

	public static int getCommonChance() {
		int originalChance = PotionHelper.POTION_COMMON_CHANCE;
		if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(RedBeastStatue.ID)) {
			float rareAndUncommonIncrease = RedBeastStatue.toPercentage(100 + RedBeastStatue.PERCENTAGE_INCREASE);
			float modifier = RedBeastStatue.getCommonChanceModifier(rareAndUncommonIncrease);
			int chance = Math.round(originalChance * modifier);
			return chance;
		} else {
			return originalChance;
		}
	}

	public static int getUncommonChance() {
		int originalChance = PotionHelper.POTION_UNCOMMON_CHANCE;
		if (AbstractDungeon.player != null && AbstractDungeon.player.hasRelic(RedBeastStatue.ID)) {
			float modifier = RedBeastStatue.toPercentage(100 + RedBeastStatue.PERCENTAGE_INCREASE);
			int chance = Math.round(originalChance * modifier);
			return chance;
		} else {
			return originalChance;
		}
	}
}
