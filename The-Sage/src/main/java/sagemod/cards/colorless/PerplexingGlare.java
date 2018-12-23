package sagemod.cards.colorless;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sagemod.cards.AbstractSageCard;
import sagemod.powers.Disoriented;

public class PerplexingGlare extends AbstractSageCard {

	public static final String ID = "Perplexing_Glare";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = 2;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.SKILL;
	private static final CardRarity RARITY = CardRarity.UNCOMMON;
	private static final CardTarget TARGET = CardTarget.ENEMY;

	private static final int DISORIENTED_AMT = 1;
	private static final int UPGRADE_DISORIENTED_AMT = 1;

	public PerplexingGlare() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET, CardColor.COLORLESS);
		baseMagicNumber = magicNumber = DISORIENTED_AMT;
		exhaust = true;
	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeMagicNumber(UPGRADE_DISORIENTED_AMT);
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new PerplexingGlare();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		applyPower(new Disoriented(m, magicNumber), m);
	}

	@Override
	public String getLoadedDescription() {
		return DESCRIPTION;
	}

}
