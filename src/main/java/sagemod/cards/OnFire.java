package sagemod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

import sagemod.powers.OnFirePower;

public class OnFire extends AbstractSageCard {

	public static final String ID = "On_Fire";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.POWER;
	private static final CardRarity RARITY = CardRarity.RARE;
	private static final CardTarget TARGET = CardTarget.SELF;

	public static final int CARDS_NEEDED = 6;
	private static final int UPGRADE_COST_TO = 0;

	public OnFire() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
		baseMagicNumber = magicNumber = CARDS_NEEDED;
	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeBaseCost(UPGRADE_COST_TO);
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new OnFire();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		// do not apply the power again if the player already has it
		if (!p.hasPower(OnFirePower.POWER_ID)) {
			applyPowerToSelf(new OnFirePower(p, magicNumber));
		} else {
			// instead just flash
			p.getPower(OnFirePower.POWER_ID).flash();
		}
	}

	@Override
	public String getLoadedDescription() {
		return DESCRIPTION;
	}

}
