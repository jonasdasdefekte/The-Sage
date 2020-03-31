package sagemod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ConfusionPower;
import sagemod.powers.Flight;

public class AboutClark extends AbstractSageCard {

	public static final String ID = "sagemod:About_Clark";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = 2;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.ATTACK;
	private static final CardRarity RARITY = CardRarity.COMMON;
	private static final CardTarget TARGET = CardTarget.ENEMY;

	private static final int ATTACK_DMG = 8;
	private static final int UPGRADE_ATTACK_DMG = 2;
	private static final int UPGRADE_COST_TO = 1;

	private int curCost;

	public AboutClark() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
		baseDamage = ATTACK_DMG;
		curCost = COST;
	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeDamage(UPGRADE_ATTACK_DMG);
			upgradeBaseCost(UPGRADE_COST_TO);
			curCost = UPGRADE_COST_TO;
		}
	}

	@Override
	public void triggerWhenDrawn() {
		super.triggerWhenDrawn();
		if (player().hasPower(ConfusionPower.POWER_ID) && cost >= 0) {
			int newCost = AbstractDungeon.cardRandomRng.random(3);
			curCost = newCost;
			if (cost != newCost) {
				cost = newCost;
				costForTurn = cost;
				isCostModified = true;
			}
		}
	}

	@Override
	public void applyPowers() {
		super.applyPowers();
		updateCost(curCost - cost);
		if (player().hasPower(Flight.POWER_ID)) {
			updateCost(player().getPower(Flight.POWER_ID).amount);
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new AboutClark();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		attack(m, AttackEffect.SLASH_DIAGONAL);
		attack(m, AttackEffect.SLASH_HORIZONTAL);
	}

	@Override
	public String getLoadedDescription() {
		return DESCRIPTION;
	}
}
