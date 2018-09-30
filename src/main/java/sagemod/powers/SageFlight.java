package sagemod.powers;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;

public class SageFlight extends AbstractSagePower {

	public static final String POWER_ID = "Sage_Flight";
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("Flight");
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	private int storedAmount;

	public SageFlight(AbstractCreature owner, int amount) {
		super(POWER_ID, NAME, owner, amount);
		storedAmount = amount;
		updateDescription();
		priority = 50;
	}

	@Override
	public void reducePower(int reduceAmount) {
		super.reducePower(reduceAmount);
		storedAmount = amount;
	}

	@Override
	public void stackPower(int stackAmount) {
		super.stackPower(stackAmount);
		storedAmount = amount;
	}

	@Override
	public void playApplyPowerSfx() {
		CardCrawlGame.sound.play("POWER_FLIGHT", 0.05f);
	}

	@Override
	public void updateDescription() {
		description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
	}

	@Override
	public void atStartOfTurn() {
		if (amount <= 0) {
			AbstractDungeon.actionManager.addToTop(new RemoveSpecificPowerAction(owner, owner, this));
		}
		amount = storedAmount;
	}

	@Override
	public float atDamageFinalReceive(float damage, DamageInfo.DamageType type) {
		return calculateDamageTakenAmount(damage, type);
	}

	private float calculateDamageTakenAmount(float damage, DamageInfo.DamageType type) {
		if (type != DamageInfo.DamageType.HP_LOSS && type != DamageInfo.DamageType.THORNS) {
			return damage / 2.0f;
		}
		return damage;
	}

	private void reduceOnlyAmount(int howMuch) {
		amount -= howMuch;
		fontScale = 8.0f;
		if (amount <= 0) {
			amount = 0;
			AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, this));
		} else {
			AbstractDungeon.onModifyPower();
			updateDescription();
		}
	}

	@Override
	public int onAttacked(DamageInfo info, int damageAmount) {
		boolean willLive = calculateDamageTakenAmount(damageAmount, info.type) < owner.currentHealth;
		if (info.owner != null && info.type != DamageInfo.DamageType.HP_LOSS
				&& info.type != DamageInfo.DamageType.THORNS && damageAmount > 0 && willLive) {
			flash();
			reduceOnlyAmount(1);
		}
		return damageAmount;
	}

	public int getStoredAmount() {
		return storedAmount;
	}

}
