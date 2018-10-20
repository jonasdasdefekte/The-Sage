package sagemod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.ExplosivePotion;

import sagemod.powers.Brew;

public class HowToWarmElephants extends AbstractSageCard {

	public static final String ID = "How_To_Warm_Elephants";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = -1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.SKILL;
	private static final CardRarity RARITY = CardRarity.COMMON;
	private static final CardTarget TARGET = CardTarget.SELF;

	private static final int TURNS = 4;
	private static final int UPGRADE_TURNS = -1;

	public HowToWarmElephants() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
		initBrewIn(TURNS);
	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeBrewIn(UPGRADE_TURNS);
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new HowToWarmElephants();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		int turns = Math.max(0, brewIn - getXEffect());

		Brew.addPotion(turns, new ExplosivePotion(), p);

		useXEnergy();
	}

}
