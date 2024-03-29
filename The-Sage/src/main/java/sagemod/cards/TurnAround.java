package sagemod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sagemod.powers.Flight;

public class TurnAround extends AbstractSageCard {

	public static final String ID = "sagemod:Turn_Around";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.ATTACK;
	private static final CardRarity RARITY = CardRarity.UNCOMMON;
	private static final CardTarget TARGET = CardTarget.ENEMY;

	private static final int ATTACK_DMG = 13;
	private static final int UPGRADE_ATTACK_DMG = 6;
	private static final int DMG_REDUCTION_PER_FLIGHT = 2;

	public TurnAround() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
		baseDamage = ATTACK_DMG;
		baseMagicNumber = magicNumber = DMG_REDUCTION_PER_FLIGHT;
	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeDamage(UPGRADE_ATTACK_DMG);
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new TurnAround();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		attack(m, AttackEffect.SLASH_HEAVY, damage);
	}

	@Override
	public void calculateCardDamage(AbstractMonster mo) {
		super.calculateCardDamage(mo);
		if (isFlying()) {
			damage -= player().getPower(Flight.POWER_ID).amount * magicNumber;
			isDamageModified = true;
		}
	}

	@Override
	public void applyPowers() {
		super.applyPowers();
		if (isFlying()) {
			damage -= player().getPower(Flight.POWER_ID).amount * magicNumber;
			isDamageModified = true;
		}
	}

	@Override
	public String getLoadedDescription() {
		return DESCRIPTION;
	}

}
