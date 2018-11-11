package sagemod.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.powers.AbstractPower.PowerType;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import com.megacrit.cardcrawl.relics.AbstractRelic;

public class AncientMagnet extends AbstractSageRelic {

	public static final String ID = "Ancient_Magnet";
	public static final RelicTier TIER = RelicTier.UNCOMMON;
	public static final LandingSound SOUND = LandingSound.CLINK;
	private static final int STRENGTH_LOSS_PER_ARTIFACT = 1;


	public AncientMagnet() {
		super(ID, TIER, SOUND);
	}

	public void onArtifactModified(AbstractCreature target, int newArtifactAmount) {
		int oldArtifactAmount = 0;
		if (target.hasPower(ArtifactPower.POWER_ID)) {
			oldArtifactAmount = target.getPower(ArtifactPower.POWER_ID).amount;
		}
		int difference = newArtifactAmount - oldArtifactAmount;
		if (difference != 0) {
			StrengthPower power = new StrengthPower(target, -difference);
			power.type = PowerType.BUFF;
			AbstractDungeon.actionManager.addToBottom(
					new ApplyPowerAction(target, AbstractDungeon.player, power, -difference));
		}
		if (difference > 0) {
			flash();
			appearAbove(target);
		}
	}

	@Override
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0] + STRENGTH_LOSS_PER_ARTIFACT + DESCRIPTIONS[1];
	}

	@Override
	public AbstractRelic makeCopy() {
		return new AncientMagnet();
	}

}
