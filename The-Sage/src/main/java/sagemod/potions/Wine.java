package sagemod.potions;

import java.util.ArrayList;
import com.badlogic.gdx.graphics.Color;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.rooms.ShopRoom;
import com.megacrit.cardcrawl.shop.ShopScreen;
import com.megacrit.cardcrawl.shop.StorePotion;
import com.megacrit.cardcrawl.shop.StoreRelic;
import basemod.ReflectionHacks;
import basemod.abstracts.CustomPotion;

public class Wine extends CustomPotion {

	public static final String POTION_ID = "sagemod:Wine";
	public static final PotionStrings potionStrings =
			CardCrawlGame.languagePack.getPotionString(POTION_ID);
	public static final String NAME = potionStrings.NAME;
	public static final String[] DESCRIPTIONS = potionStrings.DESCRIPTIONS;
	public static final int POTENCY = 20;
	public static final PotionRarity RARITY = PotionRarity.UNCOMMON;
	public static final PotionSize SIZE = PotionSize.BOTTLE;
	public static final PotionColor COLOR = PotionColor.WHITE;
	public static final Color LIQUID_COLOR = Color.SCARLET;
	public static final Color HYBRID_COLOR = Color.CLEAR;
	public static final Color SPOTS_COLOR = Color.SALMON;

	public Wine() {
		super(NAME, POTION_ID, RARITY, SIZE, COLOR);
		potency = getPotency();
		description = DESCRIPTIONS[0] + potency + DESCRIPTIONS[1];
		isThrown = false;
		tips.add(new PowerTip(name, description));
		tips.add(new PowerTip(DESCRIPTIONS[2], DESCRIPTIONS[3]));
	}

	@Override
	public int getPotency(int ascensionLevel) {
		return POTENCY;
	}

	@Override
	public AbstractPotion makeCopy() {
		return new Wine();
	}
	
	@Override
	public boolean canUse() {
		return AbstractDungeon.getCurrRoom() instanceof ShopRoom;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void use(AbstractCreature target) {
		ShopScreen shop = AbstractDungeon.shopScreen;
		ArrayList<StoreRelic> relics = (ArrayList<StoreRelic>) ReflectionHacks.getPrivate(shop, ShopScreen.class, "relics");
		ArrayList<StorePotion> potions = (ArrayList<StorePotion>) ReflectionHacks.getPrivate(shop, ShopScreen.class, "potions");
		ArrayList<AbstractCard> coloredCards = (ArrayList<AbstractCard>) ReflectionHacks.getPrivate(shop, ShopScreen.class, "coloredCards");
		ArrayList<AbstractCard> colorlessCards = (ArrayList<AbstractCard>) ReflectionHacks.getPrivate(shop, ShopScreen.class, "colorlessCards");
        for (StoreRelic r : relics) {
            r.price = r.price <= potency ? 0 : r.price - potency;
        }
        for (StorePotion p : potions) {
        	p.price = p.price <= potency ? 0 : p.price - potency;
        }
        for (AbstractCard c : coloredCards) {
        	c.price = c.price <= potency ? 0 : c.price - potency;
        }
        for (AbstractCard c : colorlessCards) {
        	c.price = c.price <= potency ? 0 : c.price - potency;
        }
        ShopScreen.actualPurgeCost = ShopScreen.actualPurgeCost <= potency ? 0 : ShopScreen.actualPurgeCost - potency;
	}

}
