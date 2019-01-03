package sagemod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.FrailPower;
import sagemod.actions.AttackForEveryStackOfPowerAction;

public class TurnAround extends AbstractSageCard {

	public static final String ID = "sagemod:Turn_Around";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.ATTACK;
	private static final CardRarity RARITY = CardRarity.UNCOMMON;
	private static final CardTarget TARGET = CardTarget.ENEMY;

	private static final int ATTACK_DMG = 4;
	private static final int UPGRADE_ATTACK_DMG = 2;
	private static final int FRAIL_GAIN = 1;

	public TurnAround() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
		baseDamage = ATTACK_DMG;
		baseMagicNumber = magicNumber = FRAIL_GAIN;
	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeDamage(UPGRADE_ATTACK_DMG);
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new TurnAround();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		applyPowerToSelf(new FrailPower(p, magicNumber, false));
		AbstractDungeon.actionManager.addToBottom(
				new AttackForEveryStackOfPowerAction(p, m, FrailPower.POWER_ID, AttackEffect.BLUNT_HEAVY, damage,
						damageTypeForTurn));
	}

	@Override
	public String getLoadedDescription() {
		return DESCRIPTION;
	}

}
