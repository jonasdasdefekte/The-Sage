package sagemod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.ApplyPoisonOnRandomMonsterAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.AbstractPower.PowerType;
import com.megacrit.cardcrawl.powers.PoisonPower;

import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import javassist.expr.MethodCall;
import sagemod.powers.AncientPoisonPower;

public class PatchesForAncientPoison {
	@SpirePatch(clz = ApplyPowerAction.class, method = "update")
	public static class ApplyPowerActionPatch {

		// we want the first type that appears
		private static final int OCCURENCE = 0;
		private static int counter = 0;

		public static ExprEditor Instrument() {
			return new ExprEditor() {
				@Override
				public void edit(FieldAccess f) throws CannotCompileException {
					if (f.getFieldName().equals("type") && counter++ == OCCURENCE) {
						f.replace(
								"{ $_ = sagemod.patches.PatchesForAncientPoison.returnWrongPowerTypeForPoison($0); }");
					}
				}
			};
		}
	}

	@SpirePatch(clz = ApplyPoisonOnRandomMonsterAction.class, method = "update")
	public static class ApplyPoisonOnRandomMonsterActionPatch {

		public static ExprEditor Instrument() {
			return new ExprEditor() {
				@Override
				public void edit(MethodCall m) throws CannotCompileException {
					if (m.getMethodName().equals("hasPower")) {
						m.replace(
								"{ $_ = sagemod.patches.PatchesForAncientPoison.returnFalseIfPlayerHasAncientPoison($0, $1); }");
					}
				}
			};
		}
	}

	public static PowerType returnWrongPowerTypeForPoison(AbstractPower power) {
		if (power.ID.equals(PoisonPower.POWER_ID) && AbstractDungeon.player.hasPower(AncientPoisonPower.POWER_ID)) {
			AbstractDungeon.player.getPower(AncientPoisonPower.POWER_ID).flash();
			return PowerType.BUFF;
		} else {
			return power.type;
		}
	}

	public static boolean returnFalseIfPlayerHasAncientPoison(AbstractCreature target, String powerId) {
		boolean hasPower = target.hasPower(powerId);
		if (AbstractDungeon.player.hasPower(AncientPoisonPower.POWER_ID)) {
			if (hasPower) {
				AbstractDungeon.player.getPower(AncientPoisonPower.POWER_ID).flash();
			}
			return false;
		} else {
			return hasPower;
		}
	}
}
