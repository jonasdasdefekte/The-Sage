package sagemod.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class UnderminePlayerPower extends AbstractSagePower {

	public static final String POWER_ID = "sagemod:Undermine";
	private static final PowerStrings powerStrings =
			CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;


	public UnderminePlayerPower(AbstractCreature owner, int amount) {
		super(POWER_ID, NAME, owner, amount);
		updateDescription();
		type = AbstractPower.PowerType.BUFF;
		isTurnBased = true;
	}

	@Override
	public void onInitialApplication() {
		super.onInitialApplication();
		applyEnemyPower();
	}

	@Override
	public void stackPower(int stackAmount) {
		super.stackPower(stackAmount);
		applyEnemyPower();
	}

	@Override
	public void atStartOfTurn() {
		super.atStartOfTurn();
		applyEnemyPower();
	}

	private void applyEnemyPower() {
		for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
			if (!mo.hasPower(UnderminePower.POWER_ID)) {
				AbstractDungeon.actionManager
						.addToTop(new ApplyPowerAction(mo, mo, new UnderminePower(mo, 1), 1));
			}
		}
	}

	@Override
	public void atEndOfRound() {
		if (amount <= 1) {
			for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
				AbstractDungeon.actionManager
					.addToBottom(new RemoveSpecificPowerAction(mo, mo, POWER_ID));
			}
			AbstractDungeon.actionManager
					.addToBottom(new RemoveSpecificPowerAction(owner, owner, POWER_ID));
		} else {
			AbstractDungeon.actionManager
					.addToBottom(new ReducePowerAction(owner, owner, POWER_ID, 1));
		}
	}

	@Override
	public void updateDescription() {
		if (amount > 1) {
			description = DESCRIPTIONS[1] + amount + DESCRIPTIONS[2];
		} else {
			description = DESCRIPTIONS[0];
		}
	}

}
