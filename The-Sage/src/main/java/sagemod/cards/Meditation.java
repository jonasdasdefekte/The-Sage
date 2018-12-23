package sagemod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sagemod.actions.ReduceFlightBlockableAction;
import sagemod.powers.SageFlight;

public class Meditation extends AbstractSageCard {

	public static final String ID = "Meditation";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = 0;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.SKILL;
	private static final CardRarity RARITY = CardRarity.RARE;
	private static final CardTarget TARGET = CardTarget.SELF;

	private static final int CARD_DRAW = 3;
	private static final int UPGRADE_CARD_DRAW = 2;

	public Meditation() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
		baseMagicNumber = magicNumber = CARD_DRAW;
		exhaust = true;
	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeMagicNumber(UPGRADE_CARD_DRAW);
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new Meditation();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		if (p.hasPower(SageFlight.POWER_ID)) {
			AbstractDungeon.actionManager.addToBottom(new ReduceFlightBlockableAction(999, p));
		}
		draw(magicNumber);
	}

	@Override
	public String getLoadedDescription() {
		return DESCRIPTION;
	}
}
