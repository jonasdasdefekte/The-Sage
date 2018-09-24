package sagemode.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import basemod.helpers.BaseModTags;
import basemod.helpers.CardTags;
import sagemod.powers.SageFlight;

public class Fly extends AbstractSageCard {

	public static final String ID = "Fly";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.SKILL;
	private static final CardRarity RARITY = CardRarity.BASIC;
	private static final CardTarget TARGET = CardTarget.SELF;

	private static final int BLOCK_AMT = 8;
	private static final int UPGRADE_BLOCK_AMT = 4;
	private static final int FLIGHT_AMT = 1;

	public Fly() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
		baseBlock = BLOCK_AMT;
		baseMagicNumber = magicNumber = FLIGHT_AMT;
		exhaust = true;
		CardTags.addTags(this, BaseModTags.GREMLIN_MATCH);
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
		return new Fly();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		if (hasPower(SageFlight.POWER_ID)) {
			block();
		} else {
			applyPowerToSelf(new SageFlight(player(), magicNumber));
		}
		// TODO remove this
		if (Settings.isDebug) {
			applyPowerToSelf(new SageFlight(player(), 2));
		}
	}

}
