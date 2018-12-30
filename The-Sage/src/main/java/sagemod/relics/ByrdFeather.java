package sagemod.relics;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import sagemod.actions.ExecuteLaterAction;
import sagemod.powers.SageFlight;

public class ByrdFeather extends AbstractSageRelic {

	public static final String ID = "Byrd_Feather";
	public static final RelicTier TIER = RelicTier.SPECIAL;
	public static final LandingSound SOUND = LandingSound.MAGICAL;
	public static final int FLY_AMT = 3;
	private static final int SET_TO_FLY_AMT_WHEN1 = 1;
	private static final int SET_TO_FLY_AMT_WHEN2 = 2;

	public ByrdFeather() {
		super(ID, TIER, SOUND);
	}

	@Override
	public void atTurnStart() {
		AbstractDungeon.actionManager.addToBottom(new ExecuteLaterAction(this::flightRestore));
	}

	private void flightRestore() {
		if (player().hasPower(SageFlight.POWER_ID)) {
			SageFlight power = (SageFlight) player().getPower(SageFlight.POWER_ID);
			if (power.amount == SET_TO_FLY_AMT_WHEN1 || power.amount == SET_TO_FLY_AMT_WHEN2) {
				flash();
				appearAbove(player());
				int amount = FLY_AMT - power.amount;
				AbstractDungeon.actionManager.addToBottom(
						new ApplyPowerAction(player(), player(), new SageFlight(player(), amount), amount));
			}
		}
	}

	@Override
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0] + SET_TO_FLY_AMT_WHEN1 + DESCRIPTIONS[1]
				+ SET_TO_FLY_AMT_WHEN2 + DESCRIPTIONS[2] + FLY_AMT + DESCRIPTIONS[3];
	}

	@Override
	public AbstractRelic makeCopy() {
		return new ByrdFeather();
	}

}
