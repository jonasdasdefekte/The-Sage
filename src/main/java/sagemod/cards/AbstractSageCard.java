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
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;

import basemod.abstracts.CustomCard;
import sagemod.SageMod;
import sagemod.actions.LoseXEnergyAction;
import sagemod.character.SageColorEnum;
import sagemod.powers.Brewing;
import sagemod.powers.SageFlight;

public abstract class AbstractSageCard extends CustomCard {

	private static final String NO_FLIGHT = "I can only play this if I have no Flight!";
	private static final String WHILE_FLYING = "I can only play this if I am flying!";
	private static final String NEEDS_POTION = "I can only play this if I have a potion!";

	private static final String PREFIX = "sage/cards/";
	private static final String POSTFIX = ".png";

	public int baseBrewIn = 0;
	public int brewIn = 0;
	public boolean upgradedBrewIn;
	public boolean isBrewInModified;
	public boolean usesBrewIn;

	public AbstractSageCard(String id, String name, int cost, String rawDescription, CardType type, CardRarity rarity,
			CardTarget target) {
		super(id, name, SageMod.getExistingOrPlaceholder(PREFIX, id, POSTFIX), cost, rawDescription, type,
				SageColorEnum.THE_SAGE, rarity, target);
	}

	public abstract String getLoadedDescription();

	public void initBrewIn(int value) {
		baseBrewIn = value;
		brewIn = value;
		usesBrewIn = true;
	}

	public void upgradeBrewIn(int by) {
		baseBrewIn += by;
		brewIn = baseBrewIn;
		upgradedBrewIn = true;
	}

	@Override
	public void applyPowers() {
		super.applyPowers();
		isBrewInModified = false;

		if (player().hasPower(Brewing.POWER_ID)) {

			int amount = player().getPower(Brewing.POWER_ID).amount;

			if (amount > 0) {

				isBrewInModified = true;
				brewIn = Math.max(0, baseBrewIn - amount);

			}
			if (usesBrewIn && brewIn <= 1) {
				rawDescription = getLoadedDescription();
				if (cost == -1) {
					if (brewIn == 0) {
						rawDescription = rawDescription.replaceAll(
								"[(NL) ]*in[(NL) ]*!BRW![(NL) ]*-[(NL) ]*X*.*[(NL) ]*turns*\\.",
								".");
						SageMod.logger.info("Changing Description for " + name + " (0 - X to .)");
					}
				} else {
					if (brewIn == 0) {
						rawDescription = rawDescription.replaceAll("[(NL) ]*in[(NL) ]*!BRW![(NL) ]*turns*\\.",
								".");
						SageMod.logger.info("Changing Description for " + name + " (0 to .)");
					} else if (brewIn == 1) {
						rawDescription = rawDescription.replaceAll(" *in[(NL) ]*!BRW![(NL) ]*turns*\\.*",
								" next turn.");
						SageMod.logger.info("Changing Description for " + name + " (1 to next turn)");
					}
				}
				System.out.println("rawAfter: " + rawDescription);
			}


			initializeDescription();
		}
	}

	/**
	 * Deals card.damage with card.damageTypeForTurn to the specified monster
	 */
	protected void attack(AbstractMonster m, AttackEffect effect) {
		AbstractDungeon.actionManager
		.addToBottom(new DamageAction(m, new DamageInfo(player(), damage, damageTypeForTurn), effect));
	}

	protected void attackAllEnemies(AttackEffect effect) {
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

	protected boolean isFlying() {
		return player().hasPower(SageFlight.POWER_ID);
	}

	protected int getXEffect() {
		if (energyOnUse < EnergyPanel.totalCount) {
			energyOnUse = EnergyPanel.totalCount;
		}

		int effect = EnergyPanel.totalCount;
		if (energyOnUse != -1) {
			effect = energyOnUse;
		}
		if (player().hasRelic(ChemicalX.ID)) {
			effect += ChemicalX.BOOST;
			player().getRelic(ChemicalX.ID).flash();
		}
		return effect;
	}

	protected void useXEnergy() {
		AbstractDungeon.actionManager.addToTop(new LoseXEnergyAction(player(), freeToPlayOnce));
	}

	protected boolean canOnlyUseWhileFlying(AbstractPlayer p, AbstractMonster m) {
		boolean superCanUse = super.canUse(p, m);
		boolean hasFlight = isFlying();
		if (!hasFlight) {
			cantUseMessage = WHILE_FLYING;
		}
		return superCanUse && hasFlight;
	}

	protected boolean canOnlyUseWithNoFlight(AbstractPlayer p, AbstractMonster m) {
		boolean superCanUse = super.canUse(p, m);
		boolean hasFlight = isFlying();
		if (hasFlight) {
			cantUseMessage = NO_FLIGHT;
		}
		return superCanUse && !hasFlight;
	}

	protected boolean canOnlyUseWhenHasPotion(AbstractPlayer p, AbstractMonster m) {
		boolean superCanUse = super.canUse(p, m);
		boolean hasPotion = player().hasAnyPotions();
		if (!hasPotion) {
			cantUseMessage = NEEDS_POTION;
		}
		return superCanUse && hasPotion;
	}

}
