package sagemod.patches;

import com.evacipated.cardcrawl.modthespire.lib.SpireField;
import com.evacipated.cardcrawl.modthespire.lib.SpirePatch;
import com.megacrit.cardcrawl.powers.PoisonPower;

@SpirePatch(clz = PoisonPower.class, method = SpirePatch.CLASS)
public class PatchesForVirus {
	public static SpireField<Boolean> appliedByVirus = new SpireField<>(() -> false);
}
