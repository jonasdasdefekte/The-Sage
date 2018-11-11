package sagemod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sagemod.powers.VigorousBodyPower;

public class VigorousBody extends AbstractSageCard {

	public static final String ID = "Vigorous_Body";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = 2;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.POWER;
	private static final CardRarity RARITY = CardRarity.RARE;
	private static final CardTarget TARGET = CardTarget.SELF;

	private static final int POWER_GAIN = 1;

	public VigorousBody() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
		baseMagicNumber = magicNumber = POWER_GAIN;
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
		return new VigorousBody();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		VigorousBodyPower power = new VigorousBodyPower(p, magicNumber);
		if (upgraded) {
			power.upgraded = true;
			if (p.hasPower(VigorousBodyPower.POWER_ID)) {
				VigorousBodyPower vigorBody =
						(VigorousBodyPower) p.getPower(VigorousBodyPower.POWER_ID);
				vigorBody.upgraded = true;
			}
		}
		applyPowerToSelf(power);
	}

	@Override
	public String getLoadedDescription() {
		return DESCRIPTION;
	}

}
