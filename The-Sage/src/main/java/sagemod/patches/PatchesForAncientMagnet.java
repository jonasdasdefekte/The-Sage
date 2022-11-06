package sagemod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.ApplyPoisonOnRandomMonsterAction;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.AbstractPower.PowerType;
import javassist.CannotCompileException;
import javassist.expr.ExprEditor;
import javassist.expr.FieldAccess;
import javassist.expr.MethodCall;
import sagemod.relics.AncientMagnet;

@SuppressWarnings("deprecation") // Normal Power Action is already handled in here
public class PatchesForAncientMagnet {
	
	public static PowerType manipulateArtifactType(AbstractPower a) {
		if (AbstractDungeon.player.hasRelic(AncientMagnet.ID) && a.type == PowerType.DEBUFF && a.owner != AbstractDungeon.player) {
			AncientMagnet magnet = (AncientMagnet) AbstractDungeon.player.getRelic(AncientMagnet.ID);
			magnet.flash();
			magnet.appearAbove(a.owner);
			return null;
		} else {
			return a.type;
		}
	}
	
	public static boolean manipulateArtifactHasPower(AbstractCreature c, String power) {
		boolean hasIt = c.hasPower(power);
		if (AbstractDungeon.player.hasRelic(AncientMagnet.ID)) {
			if (hasIt) {
				AncientMagnet magnet = (AncientMagnet) AbstractDungeon.player.getRelic(AncientMagnet.ID);
				magnet.flash();
				magnet.appearAbove(c);
			}
			return false;
		} else {
			return hasIt;
		}
	}

	@SpirePatch(clz = ApplyPowerAction.class, method = "update" )
	public static class ApplyPowerActionPatch {
		public static ExprEditor Instrument() {
			return new ExprEditor() {
				private boolean isFirstOccurrence = true;
				@Override
				public void edit(FieldAccess f) throws CannotCompileException {
					if (f.getFieldName().equals("type") && f.getClassName().equals("com.megacrit.cardcrawl.powers.AbstractPower") && isFirstOccurrence) {
						isFirstOccurrence = false;
						f.replace("{ $_ = sagemod.patches.PatchesForAncientMagnet.manipulateArtifactType($0); }");
					}
				}
			};
		}
	}
	
	@SpirePatch(clz = ApplyPoisonOnRandomMonsterAction.class, method = "update" )
	public static class ApplyPoisonOnRandomMonsterActionPatch {
		public static ExprEditor Instrument() {
			return new ExprEditor() {
				@Override
				public void edit(MethodCall m) throws CannotCompileException {
					if (m.getMethodName().equals("hasPower") && m.getClassName().equals("com.megacrit.cardcrawl.core.AbstractCreature")) {
						m.replace( "{ $_ = sagemod.patches.PatchesForAncientMagnet.manipulateArtifactHasPower($0, $1); }");
					}
				}
			};
		}
	}

}
