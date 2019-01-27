package sagemod.powers;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

// Dummy Power to apply Invisible DeadlyContraptionPower that actually does something
public class DeadlyContraptionPlayerPower extends AbstractSagePower {

	public static final String POWER_ID = "sagemod:Deadly_Contraption";
	private static final PowerStrings powerStrings =
			CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	public DeadlyContraptionPlayerPower(AbstractCreature owner, int amount) {
		super(POWER_ID, NAME, owner, amount);
		updateDescription();
		type = AbstractPower.PowerType.BUFF;
	}

	@Override
	public void updateDescription() {
		description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
	}
	
	@Override
	public void atStartOfTurn() {
		super.atStartOfTurn();
		applyInvisiblePower();
	}

	@Override
	public void atEndOfTurn(boolean isPlayer) {
		super.atEndOfTurn(isPlayer);
		applyInvisiblePower();
	}

	private void applyInvisiblePower() {
		for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
			if (!mo.hasPower(DeadlyContraptionPower.POWER_ID)) {
				AbstractDungeon.actionManager.addToTop(
						new ApplyPowerAction(mo, owner, new DeadlyContraptionPower(mo, -1), -1));
			}
		}
	}

}
