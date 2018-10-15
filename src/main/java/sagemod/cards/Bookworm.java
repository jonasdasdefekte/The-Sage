package sagemod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import sagemod.powers.BookwormPower;

public class Bookworm extends AbstractSageCard {

	public static final String ID = "Bookworm";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = 2;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.POWER;
	private static final CardRarity RARITY = CardRarity.UNCOMMON;
	private static final CardTarget TARGET = CardTarget.SELF;

	private static final int ENERGY_GAIN = 1;
	private static final int UPGRADE_COST_TO = 1;

	public Bookworm() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeBaseCost(UPGRADE_COST_TO);
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new Bookworm();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		applyPowerToSelf(new BookwormPower(p, ENERGY_GAIN));
	}

}
