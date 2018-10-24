package sagemod.powers;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.powers.ArtifactPower;

public class DeadlyContraptionPower extends AbstractSagePower {

	public static final String POWER_ID = "Deadly_Contraption";
	private static final PowerStrings powerStrings = CardCrawlGame.languagePack.getPowerStrings(POWER_ID);
	public static final String NAME = powerStrings.NAME;
	public static final String[] DESCRIPTIONS = powerStrings.DESCRIPTIONS;

	public DeadlyContraptionPower(AbstractCreature owner, int amount) {
		super(POWER_ID, NAME, owner, amount);
		updateDescription();
		type = AbstractPower.PowerType.BUFF;
	}

	@Override
	public void updateDescription() {
		description = DESCRIPTIONS[0] + amount + DESCRIPTIONS[1];
	}

	@Override
	public void atEndOfTurn(boolean isPlayer) {
		if (!isPlayer) {
			return;
		}
		boolean didSomething = false;
		for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
			if (mo.hasPower(ArtifactPower.POWER_ID)) {
				didSomething = true;
				AbstractDungeon.actionManager
				.addToBottom(new DamageAction(mo, new DamageInfo(owner,
						amount * mo.getPower(ArtifactPower.POWER_ID).amount, DamageType.HP_LOSS),
						AttackEffect.FIRE));
			}
		}
		if (didSomething) {
			flash();
		}
	}

}
