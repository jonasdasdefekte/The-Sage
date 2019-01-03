package sagemod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sagemod.powers.Accumulate;

public class Accumulation extends AbstractSageCard {

	public static final String ID = "sagemod:Accumulation";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.POWER;
	private static final CardRarity RARITY = CardRarity.RARE;
	private static final CardTarget TARGET = CardTarget.SELF;

	private static final int LOSE_FLIGHT = 2;
	private static final int UPGRADE_LOSE_FLIGHT = -1;
	private static final int ACCUMULATION_GAIN = 1;

	public Accumulation() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
		baseMagicNumber = magicNumber = LOSE_FLIGHT;
	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeMagicNumber(UPGRADE_LOSE_FLIGHT);
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new Accumulation();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		if (isFlying()) {
			Accumulate.setNext(magicNumber);
			applyPowerToSelf(new Accumulate(p, ACCUMULATION_GAIN));
		}
	}

	@Override
	public boolean canUse(AbstractPlayer p, AbstractMonster m) {
		return canOnlyUseWhileFlying(p, m);
	}

	@Override
	public String getLoadedDescription() {
		return DESCRIPTION;
	}

}
