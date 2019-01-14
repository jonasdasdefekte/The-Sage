package sagemod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;

public class Tinker extends AbstractSageCard {

	public static final String ID = "sagemod:Tinker";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.SKILL;
	private static final CardRarity RARITY = CardRarity.UNCOMMON;
	private static final CardTarget TARGET = CardTarget.ALL;

	private static final int ARTIFACT_GAIN = 1;
	private static final int ADDITIONAL_BLOCK = 5;

	public Tinker() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
		initSageMisc(ADDITIONAL_BLOCK);
		baseMagicNumber = magicNumber = ARTIFACT_GAIN;
		baseBlock = ADDITIONAL_BLOCK;
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
		return new Tinker();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		if (upgraded) {
			applyPowerToSelf(new ArtifactPower(p, magicNumber));
		}
		block();
	}

	@Override
	public void applyPowers() {
		int amt = sageMisc;
		if (upgraded) {
			amt += magicNumber;
		}
		amt += artifactAmount(player());
		for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
			amt += artifactAmount(mo);
		}
		baseBlock = amt;
		block = amt;
		super.applyPowers();
	}

	private int artifactAmount(AbstractCreature c) {
		if (c.hasPower(ArtifactPower.POWER_ID)) {
			return c.getPower(ArtifactPower.POWER_ID).amount;
		}
		return 0;
	}

	@Override
	public String getLoadedDescription() {
		return DESCRIPTION;
	}

}
