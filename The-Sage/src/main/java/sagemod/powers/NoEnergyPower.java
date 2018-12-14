package sagemod.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class NoEnergyPower extends AbstractSagePower {

	public static final String POWER_ID = "No_Energy";
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	// Implementation in PatchesForNoEnergyPower
	public NoEnergyPower(AbstractCreature owner, int amount) {
		super(POWER_ID, NAME, owner, -1);
		type = AbstractPower.PowerType.DEBUFF;
		updateDescription();
	}

	@Override
	public void atEndOfTurn(boolean isPlayer) {
		if (isPlayer) {
			AbstractDungeon.actionManager
			.addToBottom(new RemoveSpecificPowerAction(owner, owner, this));
		}
	}

	@Override
	public void stackPower(int stackAmount) {
		// do not stack
	}

	@Override
	public void reducePower(int reduceAmount) {
		// do not reduce
	}

	@Override
	public void updateDescription() {
		description = DESCRIPTIONS[0];
	}

}
