package sagemod.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.AbstractMonster.Intent;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.powers.AbstractPower;
import basemod.ReflectionHacks;

public class Disoriented extends AbstractSagePower {

	public static final String POWER_ID = "Disoriented";
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	private boolean blocked1FromMultiAttack;
	private boolean changedIntent;
	private boolean isAlmostRemoved;

	public Disoriented(AbstractCreature owner, int amount) {
		super(POWER_ID, NAME, owner, amount);
		updateDescription();
		type = AbstractPower.PowerType.DEBUFF;
		isTurnBased = true;
		blocked1FromMultiAttack = false;
		changedIntent = false;
		isAlmostRemoved = false;
	}

	@Override
	public void atEndOfRound() {
		blocked1FromMultiAttack = false;
		changedIntent = false;
		isAlmostRemoved = false;
		if (amount == 0) {
			AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(owner, owner, POWER_ID));
		} else {
			AbstractDungeon.actionManager.addToBottom(new ReducePowerAction(owner, owner, POWER_ID, 1));
		}
		if (amount == 1) {
			isAlmostRemoved = true;
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

	@Override
	public float atDamageFinalGive(float damage, DamageType type) {
		if (type == DamageType.NORMAL && owner instanceof AbstractMonster && !changedIntent && !isAlmostRemoved) {
			AbstractMonster monster = (AbstractMonster) owner;
			EnemyMoveInfo move = (EnemyMoveInfo) ReflectionHacks.getPrivate(monster, AbstractMonster.class, "move");
			if (isAttack(move.intent)) {
				if (move.isMultiDamage) {
					changedIntent = true;
					// remove 1 multiplier
					move.multiplier--;
					if (move.multiplier == 1) {
						move.isMultiDamage = false;
					}
					// update display
					monster.createIntent();
				} else {
					// for non multi attacks just set the damage to 0
					return 0;
				}
			}
		}
		return damage;
	}

	@Override
	public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
		if (!blocked1FromMultiAttack && !isAlmostRemoved) {
			AbstractGameAction removeLater = null;

			// remove 1 DamageAction where the source is the owner of this power and the
			// damage type is normal
			for (AbstractGameAction a : AbstractDungeon.actionManager.actions) {

				if (a instanceof DamageAction) {

					DamageAction d = (DamageAction) a;
					DamageInfo dInfo = (DamageInfo) ReflectionHacks.getPrivate(d, DamageAction.class, "info");

					if (dInfo.owner == owner && dInfo.type == DamageType.NORMAL) {
						removeLater = a;
						break;
					}
				}
			}

			if (removeLater != null) {
				blocked1FromMultiAttack = true;
				AbstractDungeon.actionManager.actions.remove(removeLater);
			}

		}
	}

	private boolean isAttack(Intent intent) {
		return intent == Intent.ATTACK || intent == Intent.ATTACK_BUFF || intent == Intent.ATTACK_DEBUFF
				|| intent == Intent.ATTACK_DEFEND;
	}

}
