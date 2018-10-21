package sagemod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;

public class MechanicsBreak extends AbstractSageCard {

	public static final String ID = "Mechanics_Break";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = 0;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.SKILL;
	private static final CardRarity RARITY = CardRarity.UNCOMMON;
	private static final CardTarget TARGET = CardTarget.ALL;

	private static final int BLOCK_AMT = 10;
	private static final int UPGRADE_BLOCK_AMT = 3;
	private static final int ARTIFACT_GAIN = 4;

	public MechanicsBreak() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
		baseBlock = BLOCK_AMT;
		baseMagicNumber = magicNumber = ARTIFACT_GAIN;
	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeBlock(UPGRADE_BLOCK_AMT);
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new MechanicsBreak();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		block();
		AbstractDungeon.getCurrRoom().monsters.monsters.forEach(mo -> {
			applyPower(new ArtifactPower(mo, magicNumber), mo);
		});
	}

	@Override
	public String getLoadedDescription() {
		return DESCRIPTION;
	}
}
