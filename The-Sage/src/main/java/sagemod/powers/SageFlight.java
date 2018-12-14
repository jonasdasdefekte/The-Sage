package sagemod.powers;

import static sagemod.character.TheSage.FLIGHT;
import static sagemod.character.TheSage.GROUND;
import static sagemod.character.TheSage.setSageAnimation;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;

public class SageFlight extends AbstractSagePower {

	public static final String POWER_ID = "Sage_Flight";
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	public SageFlight(AbstractCreature owner, int amount) {
		super(POWER_ID, NAME, owner, amount);
		updateDescription();
		priority = 50;
	}

	@Override
	public void playApplyPowerSfx() {
		CardCrawlGame.sound.play("POWER_FLIGHT", 0.05f);
	}

	@Override
	public void onInitialApplication() {
		setSageAnimation(GROUND, FLIGHT);
	}

	@Override
	public void onRemove() {
		setSageAnimation(FLIGHT, GROUND);
	}

	@Override
	public void updateDescription() {
		description = DESCRIPTIONS[0];
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

	@Override
	public int onAttacked(DamageInfo info, int damageAmount) {
		boolean willLive = calculateDamageTakenAmount(damageAmount, info.type) < owner.currentHealth;
		if (info.owner != null && info.type != DamageInfo.DamageType.HP_LOSS
				&& info.type != DamageInfo.DamageType.THORNS && damageAmount > 0 && willLive) {
			flash();
			// Airborne prevents Flight loss
			if (!owner.hasPower(Airborne.POWER_ID)) {
				AbstractDungeon.actionManager
						.addToTop(new ReducePowerAction(owner, info.owner, this, 1));
			} else {
				// will reduce Airborne and play a sound
				owner.getPower(Airborne.POWER_ID).onSpecificTrigger();
			}
		}
		return damageAmount;
	}

}
