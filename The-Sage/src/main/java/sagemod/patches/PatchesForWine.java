package sagemod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.ui.panels.PotionPopUp;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import sagemod.potions.Wine;

public class PatchesForWine {

	@SpirePatch(clz = PotionPopUp.class, method = "render" )
	@SpirePatch(clz = PotionPopUp.class, method = "updateInput" )
	public static class ApplyPowerActionPatch {
		public static ExprEditor Instrument() {
			return new ExprEditor() {
				@Override
				public void edit(FieldAccess f) throws CannotCompileException {
					if (f.getFieldName().equals("isScreenUp") && f.getClassName().equals("com.megacrit.cardcrawl.dungeons.AbstractDungeon")) {
						f.replace("{ $_ = sagemod.patches.PatchesForWine.isScreenUpOverwrite(this.potion); }");
					}
				}
			};
		}
	}
	
	public static boolean isScreenUpOverwrite(AbstractPotion p) {
		return AbstractDungeon.isScreenUp && !(Wine.POTION_ID.equals(p.ID));
	}
}
