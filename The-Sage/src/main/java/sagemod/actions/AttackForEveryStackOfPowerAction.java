package sagemod.actions;

import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;

public class AttackForEveryStackOfPowerAction extends AbstractGameAction {

	AbstractCreature source;
	AbstractCreature target;
	String powerId;
	AttackEffect effect;
	int baseDamage;

	public AttackForEveryStackOfPowerAction(AbstractCreature source, AbstractCreature target,
			String powerId,
			AttackEffect effect, int damage, DamageType type) {
		duration = Settings.ACTION_DUR_FAST;
		this.source = source;
		this.target = target;
		this.powerId = powerId;
		this.effect = effect;
		damageType = type;
		baseDamage = damage;
	}

	public AttackForEveryStackOfPowerAction(AbstractCreature source, AbstractCreature target,
			String powerId,
			int damage, DamageType type) {
		this(source, target, powerId, AttackEffect.SLASH_HORIZONTAL, damage, type);
	}


	@Override
	public void update() {
		if (duration == Settings.ACTION_DUR_FAST) {
			if (source.hasPower(powerId)) {
				int times = source.getPower(powerId).amount;
				for (int i = 0; i < times; i++) {
					AbstractDungeon.actionManager
							.addToTop(new DamageAction(target,
									new DamageInfo(source, baseDamage, damageType), effect));
				}
			}
		}
		isDone = true;
	}

}
