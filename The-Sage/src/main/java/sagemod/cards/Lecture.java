package sagemod.cards;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import sagemod.actions.ExecuteLaterAction;
import sagemod.listeners.LectureListener;

public class Lecture extends AbstractSageCard {

	public static final String ID = "sagemod:Lecture";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.ATTACK;
	private static final CardRarity RARITY = CardRarity.RARE;
	private static final CardTarget TARGET = CardTarget.ENEMY;

	private static final int ATTACK_DMG = 4;
	private static final int UPGRADE_ATTACK_DMG = 2;


	public Lecture() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
		baseDamage = ATTACK_DMG;
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
		return new Lecture();
	}

	private void updateExtendedDescription() {
		rawDescription = DESCRIPTION;
		if (LectureListener.xCardsPlayedThisCombat == 1) {
			rawDescription += cardStrings.EXTENDED_DESCRIPTION[2];
		} else {
			rawDescription += cardStrings.EXTENDED_DESCRIPTION[0]
					+ LectureListener.xCardsPlayedThisCombat + cardStrings.EXTENDED_DESCRIPTION[1];
		}
		initializeDescription();
	}

	@Override
	public void applyPowers() {
		super.applyPowers();
		updateExtendedDescription();
	}

	@Override
	public void atTurnStart() {
		super.atTurnStart();
		updateExtendedDescription();
	}

	@Override
	public void triggerWhenDrawn() {
		super.triggerWhenDrawn();
		updateExtendedDescription();
	}

	@Override
	public void onPlayCard(AbstractCard c, AbstractMonster m) {
		super.onPlayCard(c, m);
		AbstractDungeon.actionManager
				.addToBottom(new ExecuteLaterAction(this::updateExtendedDescription));
	}

	@Override
	public void calculateCardDamage(AbstractMonster mo) {
		super.calculateCardDamage(mo);
		updateExtendedDescription();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		for (int i = 0; i < LectureListener.xCardsPlayedThisCombat; i++) {
			attack(m, AttackEffect.SLASH_DIAGONAL);
		}
		rawDescription = DESCRIPTION;
		initializeDescription();
	}

	@Override
	public void onMoveToDiscard() {
		rawDescription = DESCRIPTION;
		initializeDescription();
	}


	@Override
	public String getLoadedDescription() {
		return DESCRIPTION;
	}
}
