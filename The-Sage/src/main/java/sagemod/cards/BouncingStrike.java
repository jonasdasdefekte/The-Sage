package sagemod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sagemod.powers.Flight;

public class BouncingStrike extends AbstractSageCard {

	public static final String ID = "sagemod:Bouncing_Strike";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.ATTACK;
	private static final CardRarity RARITY = CardRarity.UNCOMMON;
	private static final CardTarget TARGET = CardTarget.SELF_AND_ENEMY;

	private static final int FLIGHT_GAIN = 1;
	private static final int BASE_DMG_TIMES = 3;
	private static final int UPGRADE_DMG_TIMES = 2;

	public BouncingStrike() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
		baseMagicNumber = magicNumber = FLIGHT_GAIN;
		initSageMisc(BASE_DMG_TIMES);
		baseDamage = 0;

		tags.add(CardTags.STRIKE);
	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeSageMisc(UPGRADE_DMG_TIMES);
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new BouncingStrike();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		applyPowerToSelf(new Flight(p, magicNumber));
		applyPowers();
		calculateCardDamage(m);
		attack(m, AttackEffect.SLASH_DIAGONAL);
		rawDescription = DESCRIPTION;
		initializeDescription();
	}

	@Override
	public void applyPowers() {
		addFlightDamage();
		super.applyPowers();
	}
	
	private void addFlightDamage() {
		int amount = magicNumber;
		if (isFlying()) {
			amount += player().getPower(Flight.POWER_ID).amount;
		}
		baseDamage = amount * sageMisc;
		damage = baseDamage;
		rawDescription = DESCRIPTION + cardStrings.EXTENDED_DESCRIPTION[0];
		initializeDescription();
	}
	
	@Override
	public void onMoveToDiscard() {
		rawDescription = DESCRIPTION;
		initializeDescription();
	}

	@Override
	public void calculateDamageDisplay(AbstractMonster mo) {
		addFlightDamage();
		super.calculateDamageDisplay(mo);
	}
	
	@Override
	public void calculateCardDamage(AbstractMonster mo) {
		addFlightDamage();
		super.calculateCardDamage(mo);
	}

	@Override
	public String getLoadedDescription() {
		return DESCRIPTION;
	}

}
