package sagemod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.SwiftPotion;
import sagemod.potions.UpgradedPotion;
import sagemod.powers.Brew;

public class Momentum extends AbstractSageCard {

	public static final String ID = "Momentum";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = 0;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.SKILL;
	private static final CardRarity RARITY = CardRarity.UNCOMMON;
	private static final CardTarget TARGET = CardTarget.SELF;

	private static final int BREW_IN = 4;

	public Momentum() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
		initBrewIn(BREW_IN);
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
		return new Momentum();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractPotion potion = new SwiftPotion();
		if (upgraded) {
			potion = UpgradedPotion.getUpgradeIfAvailable(potion);
		}
		Brew.addPotion(brewIn, potion, p);
	}

	@Override
	public String getLoadedDescription() {
		return upgraded ? cardStrings.UPGRADE_DESCRIPTION : DESCRIPTION;
	}

}
