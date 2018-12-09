package sagemod.cards;

import com.megacrit.cardcrawl.actions.unique.SwordBoomerangAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;

public class HowToMurderAnts extends AbstractSageCard {

	public static final String ID = "How_To_Murder_Ants";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = -1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.ATTACK;
	private static final CardRarity RARITY = CardRarity.UNCOMMON;
	private static final CardTarget TARGET = CardTarget.ALL_ENEMY;

	private static final int BASE_ATTACK_DMG = 0;
	private static final int BASE_TIMES = 6;
	private static final int UPGRADE_TIMES = 2;

	public HowToMurderAnts() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
		baseMagicNumber = magicNumber = BASE_TIMES;
		baseDamage = BASE_ATTACK_DMG;
	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeMagicNumber(UPGRADE_TIMES);
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new HowToMurderAnts();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		if (isFlying()) {
			int effect = getXEffect();
			if (effect > 0) {
				AbstractDungeon.actionManager.addToBottom(new SwordBoomerangAction(
						AbstractDungeon.getMonsters().getRandomMonster(true),
						new DamageInfo(p, effect), magicNumber));
				useXEnergy();
			}
		}
	}

	@Override
	public boolean canUse(AbstractPlayer p, AbstractMonster m) {
		return canOnlyUseWhileFlying(p, m);
	}

	@Override
	public String getLoadedDescription() {
		return DESCRIPTION;
	}
}
