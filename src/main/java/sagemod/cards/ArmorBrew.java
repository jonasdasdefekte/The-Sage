package sagemod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.BlockPotion;

import sagemod.powers.Brew;

public class ArmorBrew extends AbstractSageCard {

	public static final String ID = "Armor_Brew";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.SKILL;
	private static final CardRarity RARITY = CardRarity.COMMON;
	private static final CardTarget TARGET = CardTarget.SELF;

	private static final int BREW_IN = 4;
	private static final int UPGRADE_BREW_IN = -1;
	private static final int DRAW_AMT = 1;
	private static final int UPGRADE_DRAW_AMT = 1;

	public ArmorBrew() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
		baseMagicNumber = magicNumber = BREW_IN;
		misc = DRAW_AMT;
	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeMagicNumber(UPGRADE_BREW_IN);
			misc += UPGRADE_DRAW_AMT;
			rawDescription = cardStrings.UPGRADE_DESCRIPTION;
			initializeDescription();
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new ArmorBrew();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		draw(misc);
		Brew.addPotion(magicNumber, new BlockPotion(), p);
	}

}
