package sagemod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class Defend extends AbstractSageCard {

	public static final String ID = "sagemod:Defend";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.SKILL;
	private static final CardRarity RARITY = CardRarity.BASIC;
	private static final CardTarget TARGET = CardTarget.SELF;

	private static final int BLOCK_AMT = 5;
	private static final int UPGRADE_BLOCK_AMT = 3;

	public Defend() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
		baseBlock = BLOCK_AMT;

		tags.add(CardTags.STARTER_DEFEND);
	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeBlock(UPGRADE_BLOCK_AMT);
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new Defend();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		block();
	}

	@Override
	public String getLoadedDescription() {
		return DESCRIPTION;
	}

}
