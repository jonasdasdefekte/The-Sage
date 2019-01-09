package sagemod.powers;

import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import sagemod.actions.ReduceFlightBlockableAction;

public class Accumulate extends AbstractSagePower {

	public static final String POWER_ID = "sagemod:Accumulate";
	private static final PowerStrings powerStrings =
			CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	private static int nextFlightLoss;

	private int flightLoss;

	public Accumulate(AbstractCreature owner, int amount) {
		super(POWER_ID, NAME, owner, amount);
		updateDescription();
	}

	public static void setNext(int flightLoss) {
		nextFlightLoss = flightLoss;
	}

	@Override
	public void updateDescription() {
		StringBuilder sb = new StringBuilder();
		sb.append(DESCRIPTIONS[0]);
		for (int i = 0; i < amount; ++i) {
			sb.append("[E] ");
		}
		sb.append(DESCRIPTIONS[1]);
		sb.append(flightLoss);
		sb.append(DESCRIPTIONS[2]);
		description = sb.toString();
	}

	@Override
	public void onInitialApplication() {
		super.onInitialApplication();
		flightLoss += nextFlightLoss;
		nextFlightLoss = 0;
		updateDescription();
	}

	@Override
	public void stackPower(int stackAmount) {
		super.stackPower(stackAmount);
		flightLoss += nextFlightLoss;
		nextFlightLoss = 0;
	}

	@Override
	public void reducePower(int reduceAmount) {
		super.reducePower(reduceAmount);
		flightLoss -= nextFlightLoss;
		nextFlightLoss = 0;
	}

	@Override
	public void atStartOfTurn() {
		AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(amount));
		if (owner.hasPower(Flight.POWER_ID)) {
			AbstractDungeon.actionManager.addToBottom(
					new ReduceFlightBlockableAction(flightLoss, AbstractDungeon.player));
		}
		flash();
	}

}
