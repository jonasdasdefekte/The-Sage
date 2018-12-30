package sagemod.cards.colorless;

import java.util.ArrayList;
import java.util.List;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.PotionSlot;
import sagemod.cards.AbstractSageCard;
import sagemod.potions.UpgradedPotion;

public class Refine extends AbstractSageCard {

	public static final String ID = "Refine";
	private static final CardStrings cardStrings = CardCrawlGame.languagePack.getCardStrings(ID);
	public static final String NAME = cardStrings.NAME;
	private static final int COST = 1;
	public static final String DESCRIPTION = cardStrings.DESCRIPTION;
	private static final CardType TYPE = CardType.SKILL;
	private static final CardRarity RARITY = CardRarity.UNCOMMON;
	private static final CardTarget TARGET = CardTarget.SELF;

	public Refine() {
		super(ID, NAME, COST, DESCRIPTION, TYPE, RARITY, TARGET, CardColor.COLORLESS, false);
		exhaust = true;
	}

	@Override
	public void upgrade() {
		if (!upgraded) {
			upgradeName();
			rawDescription = cardStrings.UPGRADE_DESCRIPTION;
			initializeDescription();
		}
	}

	@Override
	public AbstractCard makeCopy() {
		return new Refine();
	}

	@Override
	public void use(AbstractPlayer p, AbstractMonster m) {
		if (upgraded) {
			for (int i = 0; i < p.potionSlots; i++) {
				AbstractPotion po = p.potions.get(i);
				if (canUpgrade(po)) {
					p.obtainPotion(i, UpgradedPotion.getUpgradeIfAvailable(po));
				}
			}
		} else {
			List<Integer> possible = new ArrayList<>();
			for (int i = 0; i < p.potionSlots; i++) {
				AbstractPotion po = p.potions.get(i);
				if (canUpgrade(po)) {
					possible.add(i);
				}
			}
			if (!possible.isEmpty()) {
				int i = possible.get(AbstractDungeon.potionRng.random(possible.size() - 1));
				AbstractPotion po = p.potions.get(i);
				p.obtainPotion(i, UpgradedPotion.getUpgradeIfAvailable(po));
			}
		}
	}

	private boolean canUpgrade(AbstractPotion po) {
		return !(po instanceof PotionSlot || po instanceof UpgradedPotion
				|| UpgradedPotion.BLACKLIST.contains(po.ID));
	}

	@Override
	public String getLoadedDescription() {
		return DESCRIPTION;
	}

}
