package sagemod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import sagemod.powers.LoseFlightNextTurn;
import sagemod.powers.SageFlight;

public class CatchMeIfYouCan extends AbstractSageCard {

	public static final String ID = "Catch_Me_If_You_Can";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.SKILL;
	private static final CardRarity RARITY = CardRarity.UNCOMMON;
	private static final CardTarget TARGET = CardTarget.SELF;

	private static final int TEMP_FLIGHT_GAIN = 3;
	private static final int UPGRADE_TEMP_FLIGHT_GAIN = 2;

	public CatchMeIfYouCan() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
		baseMagicNumber = magicNumber = TEMP_FLIGHT_GAIN;
		exhaust = true;
		isInnate = true;
	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeMagicNumber(UPGRADE_TEMP_FLIGHT_GAIN);
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new CatchMeIfYouCan();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		applyPowerToSelf(new SageFlight(p, magicNumber));
		applyPowerToSelf(new LoseFlightNextTurn(p, magicNumber));
	}

}
