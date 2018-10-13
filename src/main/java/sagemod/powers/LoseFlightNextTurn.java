package sagemod.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import sagemod.actions.ReduceFlightBlockableByArtifactAction;

public class LoseFlightNextTurn extends AbstractSagePower {

	public static final String POWER_ID = "Lose_Flight_Next_Turn";
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	public LoseFlightNextTurn(AbstractCreature owner, int amount) {
		super(POWER_ID, NAME, owner, amount);
		updateDescription();
		type = AbstractPower.PowerType.DEBUFF;
		priority = 40;
	}

	@Override
	public void atStartOfTurn() {
		flash();
		AbstractDungeon.actionManager
				.addToBottom(new ReduceFlightBlockableByArtifactAction(amount, AbstractDungeon.player));
		AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, POWER_ID));

	}

	@Override
	public void updateDescription() {
		description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
	}

}
