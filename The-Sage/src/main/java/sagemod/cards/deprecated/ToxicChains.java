package sagemod.cards.deprecated;

import com.megacrit.cardcrawl.actions.common.RemoveSpecificPowerAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FrailPower;
import com.megacrit.cardcrawl.powers.PoisonPower;
import sagemod.cards.AbstractSageCard;
import sagemod.character.SageColorEnum;

@Deprecated
public class ToxicChains extends AbstractSageCard {

	public static final String ID = "Toxic_Chains";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.SKILL;
	private static final CardRarity RARITY = CardRarity.SPECIAL;
	private static final CardTarget TARGET = CardTarget.ALL;

	private static final int ADDITIONAL_POISON = 0;
	private static final int UPGRADE_ADDITIONAL_POISON = 3;

	public ToxicChains() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET, SageColorEnum.THE_SAGE, true);
		baseMagicNumber = magicNumber = ADDITIONAL_POISON;
	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeMagicNumber(UPGRADE_ADDITIONAL_POISON);
			rawDescription = cardStrings.UPGRADE_DESCRIPTION;
			initializeDescription();
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new ToxicChains();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		int poison = magicNumber;
		if (AbstractDungeon.player.hasPower(FrailPower.POWER_ID)) {
			poison += AbstractDungeon.player.getPower(FrailPower.POWER_ID).amount;
			AbstractDungeon.actionManager.addToBottom(new RemoveSpecificPowerAction(p, p, FrailPower.POWER_ID));
		}
		if (poison > 0) {
			for (AbstractMonster mo : AbstractDungeon.getCurrRoom().monsters.monsters) {
				applyPower(new PoisonPower(mo, p, poison), mo);
			}
		}
	}

	@Override
	public String getLoadedDescription() {
		return DESCRIPTION;
	}

}
