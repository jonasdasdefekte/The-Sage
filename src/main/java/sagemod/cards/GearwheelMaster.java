package sagemod.cards;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;

public class GearwheelMaster extends AbstractSageCard {

	public static final String ID = "Gearwheel_Master";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = 0;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.SKILL;
	private static final CardRarity RARITY = CardRarity.UNCOMMON;
	private static final CardTarget TARGET = CardTarget.ALL;

	private static final int ARTIFACT_GAIN = 1;
	private static final int UPGRADE_ARTIFACT_GAIN = 1;

	public GearwheelMaster() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
		baseMagicNumber = magicNumber = ARTIFACT_GAIN;
		exhaust = true;
		isInnate = true;
	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeMagicNumber(UPGRADE_ARTIFACT_GAIN);
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new GearwheelMaster();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		AbstractDungeon.getCurrRoom().monsters.monsters.forEach(mo -> {
			applyPower(new ArtifactPower(mo, magicNumber), mo);
		});
		applyPowerToSelf(new ArtifactPower(p, magicNumber));
	}

}
