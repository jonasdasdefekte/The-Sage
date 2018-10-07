package sagemod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.helpers.BaseModCardTags;
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

	private static final int COST_WHEN_UPGRADED = 1;
	private static final int FLIGHT_AMT = 1;

	public Fly() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
		baseMagicNumber = magicNumber = FLIGHT_AMT;

		tags.add(BaseModCardTags.GREMLIN_MATCH);
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
		if (!hasPower(SageFlight.POWER_ID)) {
			applyPowerToSelf(new SageFlight(player(), magicNumber));
		}
	}

	@Override
	public boolean canUse(AbstractPlayer p, AbstractMonster m) {
		return canOnlyUseWithNoFlight(p, m);
	}

}
