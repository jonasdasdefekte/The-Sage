package sagemod.powers;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;

public class LoseFlightNextTurn extends AbstractSagePower {

	public static final String POWER_ID = "Lose_Flight_Next_Turn";
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	public LoseFlightNextTurn(AbstractCreature owner, int amount) {
		super(POWER_ID, NAME, owner, amount);
		updateDescription();
	}

	@Override
	public void atStartOfTurn() {
		AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(owner, owner, SageFlight.POWER_ID, amount));
		AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(owner, owner, this));
		flash();

	}

	@Override
	public void updateDescription() {
		description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
	}

}
