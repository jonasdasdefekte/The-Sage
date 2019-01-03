package sagemod.cards;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.mod.stslib.StSLib;
import com.megacrit.cardcrawl.actions.AbstractGameAction;
import com.megacrit.cardcrawl.actions.animations.VFXAction;
import com.megacrit.cardcrawl.actions.utility.ShowCardAndPoofAction;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.vfx.combat.BiteEffect;
import com.megacrit.cardcrawl.vfx.combat.FlashAtkImgEffect;
import sagemod.actions.ExecuteLaterAction;

public class Prey extends AbstractSageCard {

	public static final String ID = "Prey";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.ATTACK;
	private static final CardRarity RARITY = CardRarity.RARE;
	private static final CardTarget TARGET = CardTarget.ENEMY;

	private static final int ATTACK_DMG = 10;
	private static final int UPGRADE_ATTACK_DMG = 4;
	private static final int MAX_HP_GAIN = 10;
	private static final int UPGRADE_MAX_HP_GAIN = 4;

	public Prey() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET);
		baseDamage = ATTACK_DMG;
		baseMagicNumber = magicNumber = MAX_HP_GAIN;
	}


	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			upgradeDamage(UPGRADE_ATTACK_DMG);
			upgradeMagicNumber(UPGRADE_MAX_HP_GAIN);
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new Prey();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		if (m != null) {
			AbstractDungeon.actionManager
			.addToBottom(new VFXAction(
					new BiteEffect(hb.cX,
							hb.cY - 40.0F * com.megacrit.cardcrawl.core.Settings.scale,
							Color.SCARLET.cpy()),
					0.3F));
			AbstractDungeon.actionManager
			.addToBottom(new ExecuteLaterAction(() -> attackAndMaybeGainMaxHP(p, m)));
		}
	}

	private void attackAndMaybeGainMaxHP(AbstractPlayer p, AbstractMonster target) {
		AbstractDungeon.effectList.add(new FlashAtkImgEffect(target.hb.cX, target.hb.cY,
				AbstractGameAction.AttackEffect.NONE));
		target.damage(new DamageInfo(p, damage, damageTypeForTurn));
		if (target.maxHealth > p.maxHealth && !target.halfDead
				&& (target.isDying || target.isDead || target.currentHealth <= 0)) {
			p.increaseMaxHp(magicNumber, false);
			AbstractDungeon.actionManager.addToTop(new ShowCardAndPoofAction(this));
			AbstractDungeon.actionManager
					.addToBottom(new ExecuteLaterAction(() -> p.discardPile.removeCard(this)));
			AbstractCard c = StSLib.getMasterDeckEquivalent(this);
			if (c != null) {
				AbstractDungeon.player.masterDeck.removeCard(c);
			}
		}
		if (AbstractDungeon.getCurrRoom().monsters.areMonstersBasicallyDead()) {
			AbstractDungeon.actionManager.clearPostCombatActions();
		}
	}

	@Override
	public String getLoadedDescription() {
		return DESCRIPTION;
	}
}
