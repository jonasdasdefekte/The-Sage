package sagemod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class ShiningPowder extends AbstractSageCard {

	public static final String ID = "Shining_Powder";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = -2;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.SKILL;
	private static final CardRarity RARITY = CardRarity.UNCOMMON;
	private static final CardTarget TARGET = CardTarget.NONE;

	private static final int ENERGY_GAIN = 1;
	private static final int UPGRADE_ENERGY_GAIN = 1;

	// Implementation in PotionListener
	public ShiningPowder() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
		baseMagicNumber = magicNumber = ENERGY_GAIN;
	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeMagicNumber(UPGRADE_ENERGY_GAIN);
			rawDescription = cardStrings.UPGRADE_DESCRIPTION;
			initializeDescription();
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new ShiningPowder();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		gainEnergy(magicNumber);
	}

	@Override
	public boolean canUse(AbstractPlayer p, AbstractMonster m) {
		cantUseMessage = getCantPlayMessage();
		return false;
	}

	@Override
	public String getLoadedDescription() {
		return DESCRIPTION;
	}

}
