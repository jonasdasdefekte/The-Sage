package sagemod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import sagemod.powers.DeadlyContraptionPlayerPower;
import sagemod.powers.DeadlyContraptionPower;

public class DeadlyContraption extends AbstractSageCard {

	public static final String ID = "sagemod:Deadly_Contraption";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.SKILL;
	private static final CardRarity RARITY = CardRarity.UNCOMMON;
	private static final CardTarget TARGET = CardTarget.ALL;

	private static final int ARTIFACT_AMT = 3;
	private static final int POWER_AMT = 2;
	private static final int UPGRADE_ARTIFACT_AMT = 1;

	public DeadlyContraption() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
		baseMagicNumber = magicNumber = ARTIFACT_AMT;
		initSageMisc(POWER_AMT);
	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeSageMisc(UPGRADE_ARTIFACT_AMT);
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new DeadlyContraption();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		applyPower(new DeadlyContraptionPlayerPower(p, sageMisc), p);
		for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
			applyPower(new ArtifactPower(mo, magicNumber), mo);
			if (!mo.hasPower(DeadlyContraptionPower.POWER_ID)) {
				applyPower(new DeadlyContraptionPower(mo, sageMisc), mo);
			}
		}
	}

	@Override
	public String getLoadedDescription() {
		return DESCRIPTION;
	}
}
