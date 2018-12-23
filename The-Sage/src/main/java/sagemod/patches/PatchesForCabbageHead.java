package sagemod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import javassist.CannotCompileException;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;
import javassist.NotFoundException;
import sagemod.relics.CabbageHead;

public class PatchesForCabbageHead {

	@SpirePatch(clz = FrailPower.class, method = SpirePatch.CONSTRUCTOR)
	public static class AddOnInitialOnRemovePatch {
		public static void Raw(CtBehavior behavior)
				throws NotFoundException, CannotCompileException {
			CtClass ctClass = behavior.getDeclaringClass();

			CtMethod onInitial = CtNewMethod.make(CtClass.voidType, "onInitialApplication", null,
					null, "{ sagemod.patches.PatchesForCabbageHead.initial($0); }", ctClass);
			ctClass.addMethod(onInitial);

			CtMethod onRemove = CtNewMethod.make(CtClass.voidType, "onRemove", null,
					null, "{ sagemod.patches.PatchesForCabbageHead.remove($0); }", ctClass);
			ctClass.addMethod(onRemove);
		}

	}

	public static void initial(AbstractPower power) {
		CabbageHead cabbage = itsCabbageTime(power);
		if (cabbage != null) {
			cabbage.gainDexterity();
		}
	}

	public static void remove(AbstractPower power) {
		CabbageHead cabbage = itsCabbageTime(power);
		if (cabbage != null) {
			cabbage.loseDexterity();
		}
	}

	private static CabbageHead itsCabbageTime(AbstractPower power) {
		if (power.ID.equals(FrailPower.POWER_ID) && AbstractDungeon.player == power.owner
				&& AbstractDungeon.player.hasRelic(CabbageHead.ID)) {
			return (CabbageHead) AbstractDungeon.player.getRelic(CabbageHead.ID);
		} else {
			return null;
		}
	}
}
