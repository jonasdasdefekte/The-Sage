package sagemode.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
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

	public static final String PLACE_HOLDER = "Placeholder";

	public AbstractSageCard(String id, String name, int cost, String rawDescription, CardType type, CardRarity rarity,
			CardTarget target) {
		super(id, name, getImageforID(PLACE_HOLDER), cost, rawDescription, type, SageColorEnum.THE_SAGE, rarity,
				target);
		try {
			loadCardImage(getImageforID(id));
		} catch (Exception ex) {
			SageMod.logger.info("Card with ID " + id + " has no image. Defaulting to placeholder image");
		}
	}

	/**
	 * Deals card.damage with card.damageTypeForTurn to the specified monster
	 */
	protected void damage(AbstractMonster m, AttackEffect effect) {
		AbstractDungeon.actionManager
				.addToBottom(new DamageAction(m, new DamageInfo(player(), damage, damageTypeForTurn), effect));
	}

	/**
	 * Blocks the player for card.block amount
	 */
	protected void block() {
		AbstractDungeon.actionManager.addToBottom(new GainBlockAction(player(), player(), block));
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

	private static String getImageforID(String id) {
		return "sage/cards/" + id + ".png";
	}

}
