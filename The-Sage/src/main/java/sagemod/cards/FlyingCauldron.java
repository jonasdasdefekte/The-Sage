package sagemod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import sagemod.powers.Brewing;
import sagemod.powers.SageFlight;

public class FlyingCauldron extends AbstractSageCard {

	public static final String ID = "Flying_Cauldron";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.SKILL;
	private static final CardRarity RARITY = CardRarity.UNCOMMON;
	private static final CardTarget TARGET = CardTarget.SELF;

	private static final int BREWING_AND_FLIGHT_GAIN = 1;
	private static final int UPGRADED_COST = 0;

	public FlyingCauldron() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
		baseMagicNumber = magicNumber = BREWING_AND_FLIGHT_GAIN;
		exhaust = true;
	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeBaseCost(UPGRADED_COST);
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new FlyingCauldron();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		if (p.hasPower(SageFlight.POWER_ID)) {
			applyPowerToSelf(new Brewing(p, magicNumber));
		} else {
			applyPowerToSelf(new SageFlight(p, magicNumber));
		}
	}

	@Override
	public String getLoadedDescription() {
		return DESCRIPTION;
	}

}