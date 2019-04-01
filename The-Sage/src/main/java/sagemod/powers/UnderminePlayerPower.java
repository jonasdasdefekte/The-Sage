package sagemod.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
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
		super(POWER_ID, NAME, owner, -1);
		updateDescription();
		type = AbstractPower.PowerType.BUFF;
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
						.addToTop(new ApplyPowerAction(mo, mo, new UnderminePower(mo, -1), -1));
			}
		}
	}

	@Override
	public void updateDescription() {
		description = DESCRIPTIONS[0];
	}

}
