package sagemod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.PoisonPotion;
import com.megacrit.cardcrawl.powers.PoisonPower;

import sagemod.powers.Brew;

public class HowToTalkToSpiders extends AbstractSageCard {

	public static final String ID = "How_To_Talk_To_Spiders";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = -1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.SKILL;
	private static final CardRarity RARITY = CardRarity.COMMON;
	private static final CardTarget TARGET = CardTarget.ENEMY;

	private static final int BREW_IN = 4;
	private static final int UPGRADE_BREW_IN = -1;

	public HowToTalkToSpiders() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
		initBrewIn(BREW_IN);

	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeBrewIn(UPGRADE_BREW_IN);
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new HowToTalkToSpiders();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		int effect = getXEffect();
		int turns = Math.max(0, brewIn - effect);
		if (effect > 0) {
			applyPower(new PoisonPower(m, p, effect), m);
		}
		Brew.addPotion(turns, new PoisonPotion(), p);

		useXEnergy();
	}

	@Override
	public String getLoadedDescription() {
		return DESCRIPTION;
	}

}
