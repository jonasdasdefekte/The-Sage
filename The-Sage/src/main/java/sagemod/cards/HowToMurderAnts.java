package sagemod.cards;

import com.megacrit.cardcrawl.actions.unique.SwordBoomerangAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.exordium.SpikeSlime_L;
import com.megacrit.cardcrawl.ui.panels.EnergyPanel;
import sagemod.actions.ExecuteLaterAction;

public class HowToMurderAnts extends AbstractSageCard {

	public static final String ID = "sagemod:How_To_Murder_Ants";
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
	private static AbstractMonster MONSTER;

	public HowToMurderAnts() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
		baseMagicNumber = magicNumber = BASE_TIMES;
		baseDamage = BASE_ATTACK_DMG;
	}
	
	private static AbstractMonster getDummy() {
		return MONSTER == null ? MONSTER = new SpikeSlime_L(-1000, -1000) : MONSTER;
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
			DamageInfo info = new DamageInfo(p, effect);
			info.applyPowers(p, m != null ? m : getDummy());
			int output = info.output;
			if (output > 0) {
				AbstractDungeon.actionManager.addToBottom(new SwordBoomerangAction(
						AbstractDungeon.getMonsters().getRandomMonster(true),
						new DamageInfo(p, effect), magicNumber));
				useXEnergy();
			}
			resetDescription(true);
		}
	}

	private void setPreview(AbstractMonster mo) {
		resetDescription(false);
		baseSageMisc = EnergyPanel.getCurrentEnergy();
		DamageInfo info = new DamageInfo(AbstractDungeon.player, baseSageMisc);
		info.applyPowers(AbstractDungeon.player, mo != null ? mo : getDummy());
		sageMisc = info.output;
		rawDescription += cardStrings.EXTENDED_DESCRIPTION[0];
		rawDescription += magicNumber;
		rawDescription += cardStrings.EXTENDED_DESCRIPTION[1];
		initializeDescription();
	}

	private void resetDescription(boolean initialize) {
		rawDescription = getLoadedDescription();
		if (initialize) {
			initializeDescription();
		}
	}
	
	@Override
	public void calculateCardDamage(AbstractMonster mo) {
		super.calculateCardDamage(mo);
		setPreview(mo);
	}

	@Override
	public void applyPowers() {
		super.applyPowers();
		setPreview(null);
	}

	@Override
	public void onPlayCard(AbstractCard c, AbstractMonster m) {
		super.onPlayCard(c, m);
		AbstractDungeon.actionManager.addToTop(new ExecuteLaterAction(() -> setPreview(null)));
	}

	@Override
	public void onMoveToDiscard() {
		super.onMoveToDiscard();
		resetDescription(true);
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
