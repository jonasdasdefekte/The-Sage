package sagemod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import basemod.abstracts.CustomCard;
import sagemod.SageMod;
import sagemod.character.SageColorEnum;

public abstract class AbstractSageCard extends CustomCard {

	private static final String PREFIX = "sage/cards/";
	private static final String POSTFIX = ".png";

	public AbstractSageCard(String id, String name, int cost, String rawDescription, CardType type, CardRarity rarity,
			CardTarget target) {
		super(id, name, SageMod.getExistingOrPlaceholder(PREFIX, id, POSTFIX), cost, rawDescription, type,
				SageColorEnum.THE_SAGE, rarity, target);
	}

	/**
	 * Deals card.damage with card.damageTypeForTurn to the specified monster
	 */
	protected void attack(AbstractMonster m, AttackEffect effect) {
		AbstractDungeon.actionManager
				.addToBottom(new DamageAction(m, new DamageInfo(player(), damage, damageTypeForTurn), effect));
	}

	protected void multiAttack(AttackEffect effect) {
		AbstractDungeon.actionManager
				.addToBottom(new DamageAllEnemiesAction(player(), multiDamage, damageTypeForTurn, effect));
	}

	/**
	 * Blocks the player for card.block amount
	 */
	protected void block() {
		block(block);
	}

	protected void block(int amount) {
		AbstractDungeon.actionManager.addToBottom(new GainBlockAction(player(), player(), amount));
	}

	protected void applyPowerToSelf(AbstractPower power) {
		applyPower(power, player());
	}

	protected void draw(int cards) {
		AbstractDungeon.actionManager.addToBottom(new DrawCardAction(player(), cards));
	}

	protected void gainEnergy(int energy) {
		AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(energy));
	}

	protected void applyPower(AbstractPower power, AbstractCreature target) {
		AbstractDungeon.actionManager.addToBottom(new ApplyPowerAction(target, player(), power, power.amount));
	}

	protected boolean hasPower(String power) {
		return player().hasPower(power);
	}

	protected AbstractPlayer player() {
		return AbstractDungeon.player;
	}

}
