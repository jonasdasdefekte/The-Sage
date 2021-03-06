package sagemod.cards;

import com.evacipated.cardcrawl.mod.stslib.variables.RefundVariable;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sagemod.actions.ReduceFlightBlockableAction;
import sagemod.powers.Flight;

public class HowToAmuseSneckos extends AbstractSageCard {

	public static final String ID = "sagemod:How_To_Amuse_Sneckos";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = -1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.SKILL;
	private static final CardRarity RARITY = CardRarity.UNCOMMON;
	private static final CardTarget TARGET = CardTarget.SELF;

	private static final int REFUND_AMT = 1;
	private static final int FLIGHT_LOSS = 1;
	private static final int DRAW_CALC = 1;
	private static final int UPGRADE_DRAW_CALC = 1;

	public HowToAmuseSneckos() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
		RefundVariable.setBaseValue(this, REFUND_AMT);
		baseMagicNumber = magicNumber = FLIGHT_LOSS;
		initSageMisc(DRAW_CALC);
	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeSageMisc(UPGRADE_DRAW_CALC);
			rawDescription = cardStrings.UPGRADE_DESCRIPTION;
			initializeDescription();
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new HowToAmuseSneckos();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		int effect = getXEffect();

		if (p.hasPower(Flight.POWER_ID)) {
			AbstractDungeon.actionManager.addToBottom(
					new ReduceFlightBlockableAction(magicNumber, AbstractDungeon.player));
		}

		int draw = 0;
		if (upgraded) {
			draw = effect * sageMisc;
		} else {
			draw = effect + sageMisc;
		}
		if (draw > 0) {
			draw(draw);
		}

		useXEnergy();
	}

	@Override
	public String getLoadedDescription() {
		return upgraded ? cardStrings.UPGRADE_DESCRIPTION : DESCRIPTION;
	}

}
