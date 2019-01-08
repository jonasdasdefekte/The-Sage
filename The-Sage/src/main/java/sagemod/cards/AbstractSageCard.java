package sagemod.cards;

import java.util.Locale;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.DamageAllEnemiesAction;
import com.megacrit.cardcrawl.actions.common.DrawCardAction;
import com.megacrit.cardcrawl.actions.common.GainBlockAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import com.megacrit.cardcrawl.relics.ChemicalX;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import basemod.abstracts.CustomCard;
import sagemod.SageMod;
import sagemod.actions.ExecuteLaterAction;
import sagemod.actions.LoseXEnergyAction;
import sagemod.character.SageColorEnum;
import sagemod.powers.Brewing;
import sagemod.powers.Flight;

public abstract class AbstractSageCard extends CustomCard {

	private static Logger logger = LogManager.getLogger(AbstractSageCard.class);

	public static final CardStrings cardStrings =
			CardCrawlGame.languagePack.getCardStrings("sagemod:AbstractSageCard");
	public static final String[] TEXT = cardStrings.EXTENDED_DESCRIPTION;

	private static final String NO_FLIGHT = TEXT[0];
	private static final String WHILE_FLYING = TEXT[1];
	private static final String NEEDS_POTION = TEXT[2];

	private static final String PREFIX = "sage/cards/";
	private static final String POSTFIX = ".png";

	private static final String DEPRECATED_ID = "Deprecated";

	public int baseBrewIn = 0;
	public int brewIn = 0;
	public boolean upgradedBrewIn;
	public boolean isBrewInModified;
	public boolean usesBrewIn;
	public boolean showBrewInAsModified;
	public int baseSageMisc = 0;
	public int sageMisc = 0;
	public boolean upgradedSageMisc;
	public boolean showSageMiscAsModified;
	private boolean isTaxing;

	public AbstractSageCard(String id, String name, int cost, String rawDescription, CardType type, CardRarity rarity,
			CardTarget target) {
		this(id, name, cost, rawDescription, type, rarity, target, SageColorEnum.THE_SAGE, false);
	}

	public AbstractSageCard(String id, String name, int cost, String rawDescription, CardType type,
			CardRarity rarity, CardTarget target, CardColor color, boolean deprecated) {
		super(id, name,
				SageMod.getExistingOrPlaceholder(getPrefix(type), deprecated ? DEPRECATED_ID : id,
						POSTFIX),
				cost,
				rawDescription, type,
				color, rarity, target);
	}

	private static String getPrefix(CardType type) {
		return PREFIX + type.toString().toLowerCase(Locale.ROOT) + "/";
	}

	public abstract String getLoadedDescription();

	public void initBrewIn(int value) {
		baseBrewIn = value;
		brewIn = value;
		usesBrewIn = true;
	}

	public void upgradeBrewIn(int by) {
		baseBrewIn += by;
		brewIn = +by;
		upgradedBrewIn = true;
	}

	public void initSageMisc(int value) {
		baseSageMisc = value;
		sageMisc = value;
	}

	public void upgradeSageMisc(int by) {
		baseSageMisc += by;
		sageMisc += by;
		upgradedSageMisc = true;
	}

	public void initTaxingCard() {
		isTaxing = true;
		if (CardCrawlGame.dungeon != null) {
			for (AbstractCard c : AbstractDungeon.actionManager.cardsPlayedThisCombat) {
				if (c.cardID.equals(cardID)) {
					updateCost(1);
				}
			}
		}
	}

	@Override
	public void triggerOnCardPlayed(AbstractCard cardPlayed) {
		super.triggerOnCardPlayed(cardPlayed);
		if (isTaxing && cardPlayed.cardID.equals(cardID)) {
			AbstractDungeon.actionManager.addToBottom(new ExecuteLaterAction(() -> updateCost(1)));
		}
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
								TEXT[5],
								TEXT[3]);
						logger.debug("Changing Description for " + name + " (0 - X to .)");
					}
				} else {
					if (brewIn == 0) {
						rawDescription = rawDescription.replaceAll(TEXT[6],
								TEXT[3]);
						logger.debug("Changing Description for " + name + " (0 to .)");
					} else if (brewIn == 1) {
						rawDescription = rawDescription.replaceAll(TEXT[7],
								TEXT[4]);
						logger.debug("Changing Description for " + name + " (1 to next turn)");
					}
				}
			}

			initializeDescription();
		}
	}

	@Override
	public AbstractCard makeStatEquivalentCopy() {
		AbstractCard card = super.makeStatEquivalentCopy();
		if (card instanceof AbstractSageCard) {
			AbstractSageCard sageCard = (AbstractSageCard) card;
			sageCard.brewIn = brewIn;
			sageCard.baseBrewIn = baseBrewIn;
			sageCard.isBrewInModified = isBrewInModified;
			sageCard.upgradedBrewIn = upgradedBrewIn;
			sageCard.usesBrewIn = usesBrewIn;
			return sageCard;
		}
		return card;
	}

	@Override
	public void resetAttributes() {
		super.resetAttributes();
		brewIn = baseBrewIn;
		isBrewInModified = false;
	}

	/**
	 * Deals card.damage with card.damageTypeForTurn to the specified monster
	 */
	protected void attack(AbstractMonster m, AttackEffect effect) {
		attack(m, effect, damage);
	}

	protected void attack(AbstractMonster m, AttackEffect effect, int damageAmount) {
		AbstractDungeon.actionManager.addToBottom(new DamageAction(m,
				new DamageInfo(player(), damageAmount, damageTypeForTurn), effect));
	}

	protected void attackAllEnemies(AttackEffect effect) {
		attackAllEnemies(effect, multiDamage);
	}

	protected void attackAllEnemies(AttackEffect effect, int[] damageAmounts) {
		AbstractDungeon.actionManager.addToBottom(
				new DamageAllEnemiesAction(player(), damageAmounts, damageTypeForTurn, effect));
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
		return player().hasPower(Flight.POWER_ID);
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
