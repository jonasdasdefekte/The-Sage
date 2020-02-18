package sagemod.cards;

import com.megacrit.cardcrawl.actions.utility.LoseBlockAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sagemod.powers.Flight;

public class LightWeighted extends AbstractSageCard {

	public static final String ID = "sagemod:Light_Weighted";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = 0;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.SKILL;
	private static final CardRarity RARITY = CardRarity.RARE;
	private static final CardTarget TARGET = CardTarget.SELF;

	private static final int DIVIDE_FLIGHT_BY = 2;
	private static final int UPGRADE_DIVIDE_FLIGHT_BY = -1;
	
	public LightWeighted() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
		baseMagicNumber = magicNumber = DIVIDE_FLIGHT_BY;
		exhaust = true;
	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeMagicNumber(UPGRADE_DIVIDE_FLIGHT_BY);
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
		int block = player().currentBlock;
		AbstractDungeon.actionManager.addToBottom(new LoseBlockAction(p, p, 999));
		int flightGain = block / magicNumber;
		if (flightGain > 0) {
			applyPowerToSelf(new Flight(p, flightGain));
		}
	}

	@Override
	public String getLoadedDescription() {
		return upgraded ? cardStrings.UPGRADE_DESCRIPTION : DESCRIPTION;
	}
}
