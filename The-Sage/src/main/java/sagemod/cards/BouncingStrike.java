package sagemod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.StrengthPower;
import sagemod.actions.ExecuteLaterAction;
import sagemod.powers.SageFlight;

public class BouncingStrike extends AbstractSageCard {

	public static final String ID = "Bouncing_Strike";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = 2;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.ATTACK;
	private static final CardRarity RARITY = CardRarity.UNCOMMON;
	private static final CardTarget TARGET = CardTarget.ENEMY;

	private static final int FRAIL_GAIN = 2;
	private static final int FLIGHT_GAIN = 1;

	public BouncingStrike() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
		baseMagicNumber = magicNumber = FLIGHT_GAIN;
		misc = FRAIL_GAIN;

		tags.add(CardTags.STRIKE);
	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			rawDescription = cardStrings.UPGRADE_DESCRIPTION;
			initializeDescription();
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new BouncingStrike();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		applyPowerToSelf(new FrailPower(p, misc, false));
		applyPowerToSelf(new SageFlight(p, magicNumber));
		AbstractDungeon.actionManager.addToBottom(new ExecuteLaterAction(() -> {
			int damageAmount = 0;
			if (p.hasPower(FrailPower.POWER_ID)) {
				damageAmount = p.getPower(FrailPower.POWER_ID).amount;
			}
			if (p.hasPower(SageFlight.POWER_ID)) {
				int amount = p.getPower(SageFlight.POWER_ID).amount;
				if (upgraded) {
					damageAmount *= amount;
				} else {
					damageAmount += amount;
				}
			}
			if (p.hasPower(StrengthPower.POWER_ID)) {
				damageAmount += p.getPower(StrengthPower.POWER_ID).amount;
			}
			attack(m, AttackEffect.SMASH, damageAmount);
		}));
		rawDescription = getLoadedDescription();
		initializeDescription();
	}

	private void updateExtendedDescription() {
		int damageAmount = magicNumber;
		if (player().hasPower(SageFlight.POWER_ID)) {
			damageAmount += player().getPower(SageFlight.POWER_ID).amount;
		}
		int amount = 0;
		if (!player().hasPower(ArtifactPower.POWER_ID)) {
			amount += misc;
		}
		if (player().hasPower(FrailPower.POWER_ID)) {
			amount += player().getPower(FrailPower.POWER_ID).amount;
		}
		if (upgraded) {
			damageAmount *= amount;
		} else {
			damageAmount += amount;
		}
		if (player().hasPower(StrengthPower.POWER_ID)) {
			damageAmount += player().getPower(StrengthPower.POWER_ID).amount;
		}
		rawDescription = getLoadedDescription();
		rawDescription = rawDescription + cardStrings.EXTENDED_DESCRIPTION[0] + damageAmount
				+ cardStrings.EXTENDED_DESCRIPTION[1];
		initializeDescription();
	}

	@Override
	public void applyPowers() {
		super.applyPowers();
		updateExtendedDescription();
	}

	@Override
	public void calculateCardDamage(AbstractMonster mo) {
		super.calculateCardDamage(mo);
		updateExtendedDescription();
	}

	@Override
	public void atTurnStart() {
		super.atTurnStart();
		updateExtendedDescription();
	}

	@Override
	public void triggerWhenDrawn() {
		updateExtendedDescription();
		super.triggerWhenDrawn();
	}

	@Override
	public void onMoveToDiscard() {
		rawDescription = getLoadedDescription();
		initializeDescription();
	}

	@Override
	public String getLoadedDescription() {
		return upgraded ? cardStrings.UPGRADE_DESCRIPTION : DESCRIPTION;
	}

}
