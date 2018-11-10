package sagemod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import basemod.ReflectionHacks;
import sagemod.relics.AncientMagnet;

public class PatchesForAncientMagnet {

	@SpirePatch(clz = ApplyPowerAction.class, method = "update")
	public static class ApplyPowerActionPatch {
		@SpireInsertPatch(rloc = 18)
		public static void Insert(ApplyPowerAction action) {
			AbstractPower powerToApply = (AbstractPower) ReflectionHacks.getPrivate(action,
					ApplyPowerAction.class, "powerToApply");
			if (AbstractDungeon.player.hasRelic(AncientMagnet.ID)
					&& powerToApply.ID.equals(ArtifactPower.POWER_ID)
					&& action.target != AbstractDungeon.player) {
				int newArtifactAmount = powerToApply.amount;
				if (action.target.hasPower(ArtifactPower.POWER_ID)) {
					newArtifactAmount += action.target.getPower(ArtifactPower.POWER_ID).amount;
				}

				AncientMagnet relic =
						((AncientMagnet) AbstractDungeon.player.getRelic(AncientMagnet.ID));
				relic.onArtifactModified(action.target, newArtifactAmount);

			}
		}
	}

	@SpirePatch(clz = ReducePowerAction.class, method = "update")
	public static class ReducePowerActionPatch {
		@SpireInsertPatch(rloc = 10, localvars = "reduceMe")
		public static void Insert(ReducePowerAction action, AbstractPower reduceMe) {

			if (reduceMe == null) {
				return;
			}

			if (AbstractDungeon.player.hasRelic(AncientMagnet.ID)
					&& reduceMe.ID.equals(ArtifactPower.POWER_ID)
					&& action.target != AbstractDungeon.player) {
				int newArtifactAmount = -action.amount;
				if (action.target.hasPower(ArtifactPower.POWER_ID)) {
					newArtifactAmount += action.target.getPower(ArtifactPower.POWER_ID).amount;
				}

				AncientMagnet relic =
						((AncientMagnet) AbstractDungeon.player.getRelic(AncientMagnet.ID));
				relic.onArtifactModified(action.target, newArtifactAmount);
			}
		}
	}

	@SpirePatch(clz = RemoveSpecificPowerAction.class, method = "update")
	public static class RemoveSpecificPowerActionPatch {
		@SpireInsertPatch(rloc = 15, localvars = "removeMe")
		public static void Insert(RemoveSpecificPowerAction action, AbstractPower removeMe) {
			if (removeMe == null) {
				return;
			}

			if (AbstractDungeon.player.hasRelic(AncientMagnet.ID)
					&& removeMe.ID.equals(ArtifactPower.POWER_ID)
					&& action.target != AbstractDungeon.player) {

				AncientMagnet relic =
						((AncientMagnet) AbstractDungeon.player.getRelic(AncientMagnet.ID));
				relic.onArtifactModified(action.target, 0);
			}
		}
	}

}
