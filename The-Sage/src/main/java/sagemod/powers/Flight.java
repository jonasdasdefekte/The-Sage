package sagemod.powers;

import static sagemod.character.TheSage.FLIGHT;
import static sagemod.character.TheSage.GROUND;
import static sagemod.character.TheSage.setSageAnimation;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.TutorialStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.AbstractMonster.Intent;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.ui.FtueTip;
import com.megacrit.cardcrawl.ui.FtueTip.TipType;
import basemod.ReflectionHacks;
import sagemod.actions.ExecuteLaterAction;
import sagemod.relics.CabbageHead;
import sagemod.tips.SageTipTracker;

public class Flight extends AbstractSagePower {

	public static final String POWER_ID = "sagemod:Flight";
	private static final PowerStrings powerStrings =
			CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	private static final TutorialStrings tutStrings =
			CardCrawlGame.languagePack.getTutorialString(SageTipTracker.FLIGHT_REDUCTION);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	public Flight(AbstractCreature owner, int amount) {
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
		checkTip();
		if (AbstractDungeon.player.hasRelic(CabbageHead.ID) && owner == AbstractDungeon.player) {
			((CabbageHead) AbstractDungeon.player.getRelic(CabbageHead.ID)).loseDexterity();
		}
	}

	private void checkTip() {
		AbstractDungeon.actionManager.addToBottom(new ExecuteLaterAction(() -> {
			if (amount < timesAttacked()
					&& !SageTipTracker.hasShown(SageTipTracker.FLIGHT_REDUCTION)) {
				SageTipTracker.neverShowAgain(SageTipTracker.FLIGHT_REDUCTION);
				AbstractDungeon.ftue = new FtueTip(tutStrings.LABEL[0], tutStrings.TEXT[0],
						Settings.WIDTH / 2.0F, Settings.HEIGHT / 2.0F, TipType.COMBAT);
			}
		}));
	}

	@Override
	public void atStartOfTurn() {
		super.atStartOfTurn();
		checkTip();
	}

	@Override
	public void stackPower(int stackAmount) {
		super.stackPower(stackAmount);
		checkTip();
	}

	private int timesAttacked() {
		int times = 0;
		for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
			EnemyMoveInfo move =
					(EnemyMoveInfo) ReflectionHacks.getPrivate(mo, AbstractMonster.class, "move");
			if (isAttack(move.intent)) {
				if (move.isMultiDamage) {
					times += move.multiplier;
				} else {
					times++;
				}
			}
		}
		return times;
	}

	private boolean isAttack(Intent intent) {
		return intent == Intent.ATTACK || intent == Intent.ATTACK_BUFF
				|| intent == Intent.ATTACK_DEBUFF
				|| intent == Intent.ATTACK_DEFEND;
	}

	@Override
	public void onRemove() {
		setSageAnimation(FLIGHT, GROUND);
		if (owner == AbstractDungeon.player) {
			if (AbstractDungeon.player.hasRelic(CabbageHead.ID)) {
				((CabbageHead) AbstractDungeon.player.getRelic(CabbageHead.ID)).gainDexterity();;
			}
			if (AbstractDungeon.player.hasPower(RicketyDefensePower.POWER_ID)) {
				 AbstractDungeon.player.getPower(RicketyDefensePower.POWER_ID).onSpecificTrigger(); //Gain Block
			}
		}
	}

	@Override
	public void updateDescription() {
		if (amount == 1) {
			description = DESCRIPTIONS[0];
		} else {
			description = DESCRIPTIONS[1] + amount + DESCRIPTIONS[2];
		}
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
		boolean willLive =
				calculateDamageTakenAmount(damageAmount, info.type) < owner.currentHealth;
		if (info.owner != null && info.type != DamageInfo.DamageType.HP_LOSS
				&& info.type != DamageInfo.DamageType.THORNS && willLive) {
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
