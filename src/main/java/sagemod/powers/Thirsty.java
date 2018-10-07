package sagemod.powers;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;

import sagemod.listeners.ThirstyListener;

public class Thirsty extends AbstractSagePower {

	public static final String POWER_ID = "Thirsty";
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	public static final float MULTIPLIER = 1.5f;

	public Thirsty(AbstractCreature owner, int amount) {
		super(POWER_ID, NAME, owner, amount);
		type = AbstractPower.PowerType.DEBUFF;
		isTurnBased = true;
		priority = 99;
		updateDescription();
	}

	@Override
	public void updateDescription() {
		description = DESCRIPTIONS[0] + amount + (amount > 1 ? DESCRIPTIONS[1] : DESCRIPTIONS[2]);
	}

	@Override
	public int onAttacked(DamageInfo info, int damageAmount) {
		return (int) calculateDamageTakenAmount(damageAmount);
	}

	private float calculateDamageTakenAmount(float damage) {
		if (ThirstyListener.wasPotionUsed) {
			return damage * MULTIPLIER;
		}
		return damage;
	}

	@Override
	public void atEndOfRound() {
		if (amount == 0) {
			AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, POWER_ID));
		} else {
			AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(owner, owner, POWER_ID, 1));
		}
	}
}
