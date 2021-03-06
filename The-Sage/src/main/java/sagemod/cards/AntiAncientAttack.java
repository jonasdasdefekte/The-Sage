package sagemod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.ArtifactPower;
import sagemod.actions.ExecuteLaterAction;

public class AntiAncientAttack extends AbstractSageCard {

	public static final String ID = "sagemod:Anti_Ancient_Attack";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = 2;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.ATTACK;
	private static final CardRarity RARITY = CardRarity.RARE;
	private static final CardTarget TARGET = CardTarget.ENEMY;

	private static final int ATTACK_DMG = 6;
	private static final int UPGRADE_ATTACK_DMG = 2;
	private static final int ARTIFACT_GAIN = 1;
	private static final int UPGRADE_ARTIFACT_GAIN = 1;

	public AntiAncientAttack() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
		baseDamage = ATTACK_DMG;
		baseMagicNumber = magicNumber = ARTIFACT_GAIN;
	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeDamage(UPGRADE_ATTACK_DMG);
			upgradeMagicNumber(UPGRADE_ARTIFACT_GAIN);
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new AntiAncientAttack();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		applyPower(new ArtifactPower(m, magicNumber), m);
		AbstractDungeon.actionManager.addToBottom(new ExecuteLaterAction(() -> {
			int times = 0;
			times += artifactAmount(m);
			for (int i = 0; i < times; i++) {
				AbstractDungeon.actionManager.addToBottom(new DamageAction(m,
						new DamageInfo(player(), damage, damageTypeForTurn), AttackEffect.SMASH,
						true));
			}
		}));
	}

	private int artifactAmount(AbstractCreature c) {
		if (c.hasPower(ArtifactPower.POWER_ID)) {
			return c.getPower(ArtifactPower.POWER_ID).amount;
		}
		return 0;
	}

	private void updateExtendedDescription(AbstractMonster m) {
		int times = magicNumber;
		if (m != null) {
			times += artifactAmount(m);
		}
		rawDescription = getLoadedDescription();
		if (times == 1) {
			rawDescription += cardStrings.EXTENDED_DESCRIPTION[0];
		} else {
			rawDescription += cardStrings.EXTENDED_DESCRIPTION[1] + times
					+ cardStrings.EXTENDED_DESCRIPTION[2];
		}
		initializeDescription();
	}

	@Override
	public void applyPowers() {
		super.applyPowers();
		updateExtendedDescription(null);
	}

	@Override
	public void calculateCardDamage(AbstractMonster mo) {
		super.calculateCardDamage(mo);
		updateExtendedDescription(mo);
	}

	@Override
	public void atTurnStart() {
		super.atTurnStart();
		updateExtendedDescription(null);
	}

	@Override
	public void triggerWhenDrawn() {
		super.triggerWhenDrawn();
		updateExtendedDescription(null);
	}

	@Override
	public void onMoveToDiscard() {
		rawDescription = getLoadedDescription();
		initializeDescription();
	}

	@Override
	public String getLoadedDescription() {
		return DESCRIPTION;
	}
}
