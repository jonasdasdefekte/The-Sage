package sagemod.cards;

import java.util.ArrayList;
import java.util.Arrays;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.BloodPotion;
import com.megacrit.cardcrawl.potions.FruitJuice;
import com.megacrit.cardcrawl.potions.RegenPotion;
import sagemod.listeners.PotionListener;
import sagemod.powers.Brew;

public class Quackster extends AbstractSageCard {

	public static final String ID = "sagemod:Quackster";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = 0;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.SKILL;
	private static final CardRarity RARITY = CardRarity.UNCOMMON;
	private static final CardTarget TARGET = CardTarget.SELF;

	private static final int BREW_IN = 4;

	private static final ArrayList<String> EXCLUDED = new ArrayList<>(
			Arrays.asList(FruitJuice.POTION_ID, BloodPotion.POTION_ID, RegenPotion.POTION_ID));

	public Quackster() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
		initBrewIn(BREW_IN);
		exhaust = true;
	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			exhaust = false;
			rawDescription = cardStrings.UPGRADE_DESCRIPTION;
			initializeDescription();
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new Quackster();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		Brew.addPotion(brewIn, PotionListener.getRandomPotion(EXCLUDED), p);
	}

	@Override
	public String getLoadedDescription() {
		return DESCRIPTION;
	}

}
