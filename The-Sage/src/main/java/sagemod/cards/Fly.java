package sagemod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import sagemod.powers.SageFlight;

public class Fly extends AbstractSageCard {

	public static final String ID = "Fly";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = 2;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.SKILL;
	private static final CardRarity RARITY = CardRarity.BASIC;
	private static final CardTarget TARGET = CardTarget.SELF;

	private static final int BLOCK_AMT = 10;
	private static final int COST_WHEN_UPGRADED = 1;
	private static final int FLIGHT_AMT = 1;


	public Fly() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
		baseBlock = BLOCK_AMT;
		baseMagicNumber = magicNumber = FLIGHT_AMT;
	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeBaseCost(COST_WHEN_UPGRADED);
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new Fly();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		if (isFlying()) {
			block();
		} else {
			applyPowerToSelf(new SageFlight(p, magicNumber));
		}
	}

	@Override
	public String getLoadedDescription() {
		return DESCRIPTION;
	}
}
