package sagemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import sagemod.powers.Airborne;
import sagemod.powers.Flight;

public class ReduceFlightBlockableAction extends AbstractGameAction {

	private int amount;

	public ReduceFlightBlockableAction(int amount, AbstractPlayer p) {
		this.amount = amount;
		target = p;
		duration = Settings.ACTION_DUR_XFAST;
		actionType = AbstractGameAction.ActionType.SPECIAL;
	}

	@Override
	public void update() {
		if (!target.hasPower(Flight.POWER_ID)) {
			isDone = true;
			return;
		}
		if (target.hasPower(Airborne.POWER_ID)) {
			int tempAmount = amount;
			Airborne power = (Airborne) target.getPower(Airborne.POWER_ID);
			tempAmount -= power.amount;
			if (tempAmount > 0) {
				amount = tempAmount;
				power.remove(power.amount);
			} else if (tempAmount < 0) {
				power.remove(amount);
				isDone = true;
				return;
			} else {
				power.remove(power.amount);
				isDone = true;
				return;
			}
		}
		if (amount > 0) {
			AbstractDungeon.actionManager.addToBottom(
					new ReducePowerAction(target, target, Flight.POWER_ID, amount));
		}
		isDone = true;
	}

}
