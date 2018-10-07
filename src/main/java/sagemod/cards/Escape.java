package sagemod.cards;

import com.megacrit.cardcrawl.actions.common.ApplyPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import sagemod.powers.SageFlight;

public class Escape extends AbstractSageCard {

	public static final String ID = "Escape";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.SKILL;
	private static final CardRarity RARITY = CardRarity.RARE;
	private static final CardTarget TARGET = CardTarget.SELF;

	public Escape() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
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
		return new Escape();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		if (air()) {
			int flightAmount = p.getPower(SageFlight.POWER_ID).amount;
			AbstractDungeon.actionManager
					.addToTop(new ApplyPowerAction(p, p, new SageFlight(p, flightAmount), flightAmount));
		}
	}

}
