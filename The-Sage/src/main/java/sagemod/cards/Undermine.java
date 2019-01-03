package sagemod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import sagemod.powers.UnderminePower;

public class Undermine extends AbstractSageCard {

	public static final String ID = "sagemod:Undermine";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.SKILL;
	private static final CardRarity RARITY = CardRarity.UNCOMMON;
	private static final CardTarget TARGET = CardTarget.SELF_AND_ENEMY;

	private static final int ARTIFACT_AMT = 5;
	private static final int UPGRADE_ARTIFACT_AMT = 3;
	private static final int POWER_GAIN = 1;

	public Undermine() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
		baseMagicNumber = magicNumber = ARTIFACT_AMT;
	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeMagicNumber(UPGRADE_ARTIFACT_AMT);
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new Undermine();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		applyPower(new ArtifactPower(m, magicNumber), m);
		applyPowerToSelf(new UnderminePower(p, POWER_GAIN));
	}

	@Override
	public String getLoadedDescription() {
		return DESCRIPTION;
	}
}
