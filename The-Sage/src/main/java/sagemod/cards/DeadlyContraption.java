package sagemod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import sagemod.powers.DeadlyContraptionPower;

public class DeadlyContraption extends AbstractSageCard {

	public static final String ID = "Deadly_Contraption";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.POWER;
	private static final CardRarity RARITY = CardRarity.UNCOMMON;
	private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

	private static final int POWER_AMT = 1;
	private static final int ARTIFACT_AMT = 3;
	private static final int UPGRADE_ARTIFACT_AMT = 2;

	public DeadlyContraption() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
		baseMagicNumber = magicNumber = ARTIFACT_AMT;
		misc = POWER_AMT;
		initTaxingCard();
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
		return new DeadlyContraption();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
			applyPower(new ArtifactPower(mo, magicNumber), mo);
			applyPower(new DeadlyContraptionPower(mo, misc), mo);
		}
	}

	@Override
	public String getLoadedDescription() {
		return DESCRIPTION;
	}
}
