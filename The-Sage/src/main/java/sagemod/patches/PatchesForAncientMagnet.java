package sagemod.patches;

import java.util.ArrayList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.evacipated.cardcrawl.modthespire.lib.LineFinder;
import com.evacipated.cardcrawl.modthespire.lib.Matcher;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertLocator;
import com.evacipated.cardcrawl.modthespire.lib.SpireInsertPatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import basemod.ReflectionHacks;
import javassist.CtBehavior;
import sagemod.relics.AncientMagnet;

public class PatchesForAncientMagnet {
	
	private static final Logger logger = LogManager.getLogger(PatchesForAncientMagnet.class);

	@SpirePatch(clz = ApplyPowerAction.class, method = "update")
	public static class ApplyPowerActionPatch {
		@SpireInsertPatch(locator = Locator.class)
		public static void Insert(ApplyPowerAction action) {
			AbstractPower powerToApply = (AbstractPower) ReflectionHacks.getPrivate(action,
					ApplyPowerAction.class, "powerToApply");
			if (AbstractDungeon.player.hasRelic(AncientMagnet.ID)
					&& powerToApply.ID.equals(ArtifactPower.POWER_ID)
					&& action.target != AbstractDungeon.player) {
				logger.info("Ancient Magnet Apply Power triggered");
				int newArtifactAmount = powerToApply.amount;
				if (action.target.hasPower(ArtifactPower.POWER_ID)) {
					newArtifactAmount += action.target.getPower(ArtifactPower.POWER_ID).amount;
				}

				AncientMagnet relic =
						((AncientMagnet) AbstractDungeon.player.getRelic(AncientMagnet.ID));
				relic.onArtifactModified(action.target, newArtifactAmount);

			}
		}
		
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
				return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(),
						new Matcher.FieldAccessMatcher(AbstractDungeon.class, "effectList"));
			}
		}
	}

	@SpirePatch(clz = ReducePowerAction.class, method = "update")
	public static class ReducePowerActionPatch {
		@SpireInsertPatch(locator = Locator.class, localvars = "reduceMe")
		public static void Insert(ReducePowerAction action, AbstractPower reduceMe) {

			if (reduceMe == null) {
				return;
			}

			if (AbstractDungeon.player.hasRelic(AncientMagnet.ID)
					&& reduceMe.ID.equals(ArtifactPower.POWER_ID)
					&& action.target != AbstractDungeon.player) {
				logger.info("Ancient Magnet Reduce Power triggered");
				int newArtifactAmount = -action.amount;
				if (action.target.hasPower(ArtifactPower.POWER_ID)) {
					newArtifactAmount += action.target.getPower(ArtifactPower.POWER_ID).amount;
				}

				AncientMagnet relic =
						((AncientMagnet) AbstractDungeon.player.getRelic(AncientMagnet.ID));
				relic.onArtifactModified(action.target, newArtifactAmount);
			}
		}
		
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
				return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(),
						new Matcher.MethodCallMatcher(AbstractPower.class, "reducePower"));
			}
		}
	}

	@SpirePatch(clz = RemoveSpecificPowerAction.class, method = "update")
	public static class RemoveSpecificPowerActionPatch {
		@SpireInsertPatch(locator = Locator.class, localvars = "removeMe")
		public static void Insert(RemoveSpecificPowerAction action, AbstractPower removeMe) {
			if (removeMe == null) {
				return;
			}

			if (AbstractDungeon.player.hasRelic(AncientMagnet.ID)
					&& removeMe.ID.equals(ArtifactPower.POWER_ID)
					&& action.target != AbstractDungeon.player) {
				logger.info("Ancient Magnet Remove Specific Power triggered");
				AncientMagnet relic =
						((AncientMagnet) AbstractDungeon.player.getRelic(AncientMagnet.ID));
				relic.onArtifactModified(action.target, 0);
			}
		}
		
		private static class Locator extends SpireInsertLocator {
			@Override
			public int[] Locate(CtBehavior ctMethodToPatch) throws Exception {
				return LineFinder.findInOrder(ctMethodToPatch, new ArrayList<Matcher>(),
						new Matcher.FieldAccessMatcher(AbstractDungeon.class, "effectList"));
			}
		}
	}

}
