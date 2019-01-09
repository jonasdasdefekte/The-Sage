package sagemod.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.ReducePowerAction;
import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;

public class UnderminePower extends AbstractSagePower {

	public static final String POWER_ID = "sagemod:Undermine";
	private static final PowerStrings powerStrings =
			CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;


	public UnderminePower(AbstractCreature owner, int amount) {
		super(POWER_ID, NAME, owner, amount);
		updateDescription();
		type = AbstractPower.PowerType.BUFF;
		isTurnBased = true;
	}

	@Override
	public void atEndOfRound() {
		if (amount == 0) {
			AbstractDungeon.actionManager
					.addToBottom(new RemoveSpecificPowerAction(owner, owner, POWER_ID));
		} else {
			AbstractDungeon.actionManager
					.addToBottom(new ReducePowerAction(owner, owner, POWER_ID, 1));
		}
	}

	@Override
	public void updateDescription() {
		if (amount > 1) {
			description = DESCRIPTIONS[1] + amount + DESCRIPTIONS[2];
		} else {
			description = DESCRIPTIONS[0];
		}
	}

	@Override
	public void onAttack(DamageInfo info, int damageAmount, AbstractCreature target) {
		super.onAttack(info, damageAmount, target);
		if (info.type == DamageType.NORMAL && target.hasPower(ArtifactPower.POWER_ID)) {
			int amount = target.getPower(ArtifactPower.POWER_ID).amount;
			AbstractDungeon.actionManager.addToTop(new DamageAction(target,
					new DamageInfo(AbstractDungeon.player, amount, DamageType.THORNS),
					AttackEffect.FIRE));
			flash();
		}
	}


}
