package sagemod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import basemod.helpers.BaseModCardTags;
import sagemod.actions.ReduceFlightBlockableAction;
import sagemod.powers.Disoriented;

public class CultistForm extends AbstractSageCard {

	public static final String ID = "Cultist_Form";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = 3;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.POWER;
	private static final CardRarity RARITY = CardRarity.RARE;
	private static final CardTarget TARGET = CardTarget.SELF;

	private static final int LOSE_FLIGHT = 2;
	private static final int DISORIENTED_AMT = 1;
	private static final int DISORIENTED_TIMES = 4;
	private static final int UPGRADE_DISORIENTED_TIMES = 1;

	public CultistForm() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
		baseMagicNumber = magicNumber = DISORIENTED_TIMES;
		misc = LOSE_FLIGHT;

		tags.add(BaseModCardTags.FORM);
	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeMagicNumber(UPGRADE_DISORIENTED_TIMES);
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new CultistForm();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		if (isFlying()) {
			AbstractDungeon.actionManager.addToBottom(new ReduceFlightBlockableAction(misc, p));
		}

		for (int i = 0; i < magicNumber; i++) {
			if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
				break;
			}
			AbstractMonster mo = AbstractDungeon.getMonsters().getRandomMonster(true);
			applyPower(new Disoriented(mo, DISORIENTED_AMT), mo);
		}
	}

	@Override
	public String getLoadedDescription() {
		return DESCRIPTION;
	}

}
