package sagemod.cards;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.AbstractPower;
import sagemod.powers.Flight;

public class HowToGreetByrds extends AbstractSageCard {

	public static final String ID = "sagemod:How_To_Greet_Byrds";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = -1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.SKILL;
	private static final CardRarity RARITY = CardRarity.UNCOMMON;
	private static final CardTarget TARGET = CardTarget.SELF;

	public HowToGreetByrds() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);

		exhaust = true;
		isInnate = true;

	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			rawDescription = cardStrings.UPGRADE_DESCRIPTION;
			initializeDescription();
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new HowToGreetByrds();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		int effect = getXEffect();

		if (upgraded) {
			if (effect > 0) {
				applyPowerToSelf(new Flight(p, effect));
			}
		} else {
			if (p.hasPower(Flight.POWER_ID)) {
				AbstractPower power = p.getPower(Flight.POWER_ID);
				if (effect <= 0) {
					AbstractDungeon.actionManager
							.addToBottom(new RemoveSpecificPowerAction(p, p, power));
				} else {
					power.amount = effect;
					power.flash();
				}
			} else if (effect > 0) {
				applyPowerToSelf(new Flight(p, effect));
			}
		}

		useXEnergy();
	}

	@Override
	public String getLoadedDescription() {
		return DESCRIPTION;
	}

}
