package sagemod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sagemod.powers.Thirsty;

public class SpoiledFood extends AbstractSageCard {

	public static final String ID = "sagemod:Spoiled_Food";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.SKILL;
	private static final CardRarity RARITY = CardRarity.UNCOMMON;
	private static final CardTarget TARGET = CardTarget.ENEMY;

	private static final int THIRSTY_GAIN = 99;
	private static final int UPGRADED_COST = 0;

	public SpoiledFood() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
		baseMagicNumber = magicNumber = THIRSTY_GAIN;
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
		return new SpoiledFood();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		applyPower(new Thirsty(m, magicNumber), m);
	}

	@Override
	public String getLoadedDescription() {
		return DESCRIPTION;
	}
}
