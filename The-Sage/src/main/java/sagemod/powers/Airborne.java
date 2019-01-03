package sagemod.powers;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

public class Airborne extends AbstractSagePower {

	public static final String POWER_ID = "sagemod:Airborne";
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	public Airborne(AbstractCreature owner, int amount) {
		super(POWER_ID, NAME, owner, amount);
		updateDescription();
		type = AbstractPower.PowerType.BUFF;
		priority = 99;
	}

	@Override
	public void atStartOfTurn() {
		flash();
		AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, POWER_ID));
	}

	// Actual implementation is in SageFlight
	@Override
	public void onSpecificTrigger() {
		remove(1);
	}

	public void remove(int i) {
		CardCrawlGame.sound.play("NULLIFY_SFX");
		flashWithoutSound();
		if (amount <= 0) {
			AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(owner, owner, POWER_ID));
		} else {
			AbstractDungeon.actionManager
			.addToTop(new ReducePowerAction(owner, owner, POWER_ID, i));
		}
	}

	@Override
	public void updateDescription() {
		if (amount > 1) {
			description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
		} else {
			description = DESCRIPTIONS[2];
		}
	}

}
