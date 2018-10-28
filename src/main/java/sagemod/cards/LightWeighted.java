package sagemod.cards;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FrailPower;

import sagemod.powers.SageFlight;

public class LightWeighted extends AbstractSageCard {

	public static final String ID = "Light_Weighted";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = 0;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.SKILL;
	private static final CardRarity RARITY = CardRarity.RARE;
	private static final CardTarget TARGET = CardTarget.SELF;

	private static final int EXTRA_FLIGHT = 0;
	private static final int UPGRADE_EXTRA_FLIGHT = 1;

	public LightWeighted() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
		baseMagicNumber = magicNumber = EXTRA_FLIGHT;
		exhaust = true;
	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeMagicNumber(UPGRADE_EXTRA_FLIGHT);
			rawDescription = cardStrings.UPGRADE_DESCRIPTION;
			initializeDescription();
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new LightWeighted();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		int flightGain = magicNumber;
		if (p.hasPower(FrailPower.POWER_ID)) {
			flightGain += p.getPower(FrailPower.POWER_ID).amount;
			AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p, p, FrailPower.POWER_ID));
		}
		if (flightGain > 0) {
			applyPowerToSelf(new SageFlight(p, flightGain));
		}
	}

	@Override
	public String getLoadedDescription() {
		return DESCRIPTION;
	}
}
