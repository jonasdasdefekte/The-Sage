package sagemod.powers;

import java.lang.reflect.Field;

import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.FlightPower;

public class SageFlight extends FlightPower {

	public static final String POWER_ID = "Sage_Flight";
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings("Flight");
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	private Field storedAmount;

	public SageFlight(AbstractCreature owner, int amount) {
		super(owner, amount);
		name = NAME;
		ID = POWER_ID;
		canGoNegative = false;
		try {
			storedAmount = FlightPower.class.getDeclaredField("storedAmount");
			storedAmount.setAccessible(true);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public int onAttacked(DamageInfo info, int damageAmount) {
		Boolean willLive = calculateDamageTakenAmount(damageAmount, info.type) < owner.currentHealth;
		if (info.owner != null && info.type != DamageInfo.DamageType.HP_LOSS
				&& info.type != DamageInfo.DamageType.THORNS && damageAmount > 0 && willLive.booleanValue()) {
			flash();
			AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(owner, owner, POWER_ID, 1));
		}
		return damageAmount;
	}

	@Override
	public void stackPower(int stackAmount) {
		super.stackPower(stackAmount);
		try {
			storedAmount.setInt(this, getStoredAmount() + stackAmount);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	private float calculateDamageTakenAmount(float damage, DamageInfo.DamageType type) {
		if (type != DamageInfo.DamageType.HP_LOSS && type != DamageInfo.DamageType.THORNS) {
			return damage / 2.0f;
		}
		return damage;
	}

	public int getStoredAmount() {
		try {
			return storedAmount.getInt(this);
		} catch (Exception ex) {
			return 0;
		}
	}

	@Override
	public void onRemove() {
		// Do nothing
	}

}
