package sagemod.cards;

import java.util.ArrayList;
import java.util.Collections;

import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.PotionSlot;

public class BurningFlask extends AbstractSageCard {

	public static final String ID = "Burning_Flask";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = 0;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.ATTACK;
	private static final CardRarity RARITY = CardRarity.UNCOMMON;
	private static final CardTarget TARGET = CardTarget.ENEMY;

	private static final int ATTACK_DMG = 20;
	private static final int UPGRADE_ATTACK_DMG = 4;

	public BurningFlask() {
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
		return new BurningFlask();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		ArrayList<AbstractPotion> potions = new ArrayList<>();
		for (AbstractPotion potion : player().potions) {
			if (potion instanceof PotionSlot) {
				continue;
			} else {
				potions.add(potion);
			}
		}
		Collections.shuffle(potions);
		player().removePotion(potions.get(0));

		attack(m, AttackEffect.SLASH_HEAVY);
	}

	@Override
	public boolean canUse(AbstractPlayer p, AbstractMonster m) {
		return canOnlyUseWhenHasPotion(p, m);
	}

	@Override
	public String getLoadedDescription() {
		return DESCRIPTION;
	}
}
