package sagemod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.evacipated.cardcrawl.modthespire.lib.SpirePostfixPatch;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import sagemod.powers.VigorousBodyPower;

@SpirePatch(clz = ArtifactPower.class, method = "onSpecificTrigger")
public class PatchForVigorousBody {

	@SpirePostfixPatch
	public static void PostFix(ArtifactPower power) {
		if (AbstractDungeon.player.hasPower(VigorousBodyPower.POWER_ID)) {
			VigorousBodyPower vigBody =
					(VigorousBodyPower) AbstractDungeon.player.getPower(VigorousBodyPower.POWER_ID);
			vigBody.onArtifactTriggered(power);
		}
	}

}
