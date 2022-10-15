package sagemod.potions;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.Ambrosia;
import com.megacrit.cardcrawl.potions.AttackPotion;
import com.megacrit.cardcrawl.potions.BlessingOfTheForge;
import com.megacrit.cardcrawl.potions.BloodPotion;
import com.megacrit.cardcrawl.potions.ColorlessPotion;
import com.megacrit.cardcrawl.potions.DuplicationPotion;
import com.megacrit.cardcrawl.potions.Elixir;
import com.megacrit.cardcrawl.potions.EntropicBrew;
import com.megacrit.cardcrawl.potions.FirePotion;
import com.megacrit.cardcrawl.potions.GamblersBrew;
import com.megacrit.cardcrawl.potions.LiquidMemories;
import com.megacrit.cardcrawl.potions.PotionSlot;
import com.megacrit.cardcrawl.potions.PowerPotion;
import com.megacrit.cardcrawl.potions.SkillPotion;
import com.megacrit.cardcrawl.potions.SmokeBomb;
import com.megacrit.cardcrawl.potions.StancePotion;
import com.megacrit.cardcrawl.relics.SacredBark;
import basemod.ReflectionHacks;
import basemod.abstracts.CustomPotion;
import basemod.abstracts.CustomSavable;
import sagemod.SageMod;

public class UpgradedPotion extends CustomPotion {

	public static final List<String> BLACKLIST = new ArrayList<>();
	public static final List<String> DOUBLE_USE_WHITELIST = new ArrayList<>();
	public static final List<String> DISCOVERY_LIST = new ArrayList<>();
	public static final List<String> CUSTOM_DESCRIPTION_LIST = new ArrayList<>();

	public static final float CHANCE = 0.20f;
	public static final float POTENCY_MULTIPLIER = 1.5f;
	public static final float PRICE_MULTIPLIER = 1.5f;

	public static final String TWICE = "2x: ";
	private static final Color OUTLINE_COLOR = new Color(0xFFD70088);
	private static final Color LIGHT_OUTLINE_COLOR = new Color(0xFFD70044);

	public static int discoveryOpenUpgrades = 0;

	public static final String ON_UPGRADE = "_sagemod_upgradedInitializeData";
	public static final String UPGRADED_USE = "_sagemod_upgradedUse";

	private static Texture plusImg;

	private AbstractPotion potion;

	private Method upgradedInitializeData;
	private Method upgradedUse;

	private UpgradedPotion(AbstractPotion potion) {
		super(loadName(potion), potion.ID, potion.rarity, potion.size, potion.color);
		this.potion = potion;
		this.hb = potion.hb;

		loadCustomMethods();

		if (hasCustomUpgrade()) {
			customInitializeData();
			this.description = potion.description;
			this.tips = potion.tips;
			this.potency = potion.getPotency();
		} else {
			potency = loadPotency();
			ReflectionHacks.setPrivate(potion, AbstractPotion.class, "potency", potency);

			reloadDescription();
		}

		isThrown = potion.isThrown;
		targetRequired = potion.targetRequired;

	}

	private void loadCustomMethods() {
		try {
			upgradedInitializeData = this.potion.getClass().getDeclaredMethod(ON_UPGRADE);
		} catch (Exception ex) {
		}
		try {
			upgradedUse =
					this.potion.getClass().getDeclaredMethod(UPGRADED_USE, AbstractCreature.class);
		} catch (Exception ex) {
		}
	}

	private boolean hasCustomUpgrade() {
		return upgradedInitializeData != null || upgradedUse != null;
	}

	private void customInitializeData() {
		try {
			if (upgradedInitializeData != null) {
				upgradedInitializeData.invoke(this.potion);
			}
		} catch (Exception ex) {
		}
	}

	private void customUse(AbstractCreature c) {
		try {
			if (upgradedUse != null) {
				upgradedUse.invoke(this.potion, c);
			}
		} catch (Exception ex) {
		}
	}

	@Override
	public void initializeData() {
		super.initializeData();
		if (potion != null) {
			if (hasCustomUpgrade()) {
				customInitializeData();
			} else {
				potion.initializeData();
				potency = loadPotency();
				reloadDescription();
			}
		}
	}

	private void reloadDescription() {
		tips.clear();
		description = loadDescription();
		tips.add(new PowerTip(name, description));
		for (int i = 1; i < potion.tips.size(); i++) {
			tips.add(potion.tips.get(i));
		}
		potion.description = this.description;
		potion.tips = this.tips;
	}

	public static AbstractPotion forceUpgrade(AbstractPotion potion) {
		return new UpgradedPotion(potion);
	}

	public static AbstractPotion getUpgradeIfAvailable(AbstractPotion potion) {
		if (potion == null) {
			return new FirePotion();
		}
		if (BLACKLIST.contains(potion.ID) || potion instanceof UpgradedPotion
				|| potion instanceof PotionSlot) {
			return potion;
		} else {
			return new UpgradedPotion(potion);
		}
	}

	public static void initLists() {
		BLACKLIST.add(EntropicBrew.POTION_ID);
		BLACKLIST.add(SmokeBomb.POTION_ID);
		BLACKLIST.add(Ambrosia.POTION_ID);
		BLACKLIST.add(Elixir.POTION_ID);
		BLACKLIST.add(BlessingOfTheForge.POTION_ID);
		BLACKLIST.add(StancePotion.POTION_ID);
		BLACKLIST.add("Elixir");
		BLACKLIST.add("conspire:TimeTravelPotion");
		BLACKLIST.add("jedi:holywater");
		BLACKLIST.add("HastePotion");
		BLACKLIST.add("Gelsemium Tea");
		BLACKLIST.add("qcfpunch:Challenger_Coin");

		DISCOVERY_LIST.add(AttackPotion.POTION_ID);
		DISCOVERY_LIST.add(PowerPotion.POTION_ID);
		DISCOVERY_LIST.add(SkillPotion.POTION_ID);
		DISCOVERY_LIST.add(ColorlessPotion.POTION_ID);
		DISCOVERY_LIST.add("infinitespire:BlackPotion");

		DOUBLE_USE_WHITELIST.add(GamblersBrew.POTION_ID);
		DOUBLE_USE_WHITELIST.add("blackbeard:" + BloodPotion.POTION_ID);
		DOUBLE_USE_WHITELIST.add("Flashbang");
		DOUBLE_USE_WHITELIST.add("WardPotion");
		DOUBLE_USE_WHITELIST.add("ShroomBrew");
		DOUBLE_USE_WHITELIST.add("Firefly");
		DOUBLE_USE_WHITELIST.add("Pckles");
		DOUBLE_USE_WHITELIST.add("Turbid Wine");
		DOUBLE_USE_WHITELIST.add("conspire:EchoDraught");
		DOUBLE_USE_WHITELIST.add("construct:MegaPotion");
		
		CUSTOM_DESCRIPTION_LIST.add(DuplicationPotion.POTION_ID);
		CUSTOM_DESCRIPTION_LIST.add(LiquidMemories.POTION_ID);
	}

	public static void initTextures() {
		plusImg = new Texture("sage/potions/plus.png");
	}

	private static String loadName(AbstractPotion p) {
		if (p instanceof UpgradedPotion) {
			return p.name;
		}
		return p.name + "+";
	}

	private int loadPotency() {
		if (potion instanceof UpgradedPotion || DOUBLE_USE_WHITELIST.contains(potion.ID)
				|| DISCOVERY_LIST.contains(potion.ID)) {
			return potion.getPotency();
		} else if (potion.ID.equals("Doom Potion")) {
			return (int) (potion.getPotency() * 0.75f);
		}
		return MathUtils.ceil(potion.getPotency() * POTENCY_MULTIPLIER);

	}

	private String loadDescription() {
		try {
			if (DOUBLE_USE_WHITELIST.contains(potion.ID)) {
				return TWICE + potion.description;
			}

			if (DISCOVERY_LIST.contains(potion.ID) || CUSTOM_DESCRIPTION_LIST.contains(potion.ID)) {
				int descIndex = (AbstractDungeon.player != null
						&& AbstractDungeon.player.hasRelic(SacredBark.ID)) ? 1 : 0;
				return CardCrawlGame.languagePack
						.getPotionString("sagemod:UPGRADED:" + potion.ID).DESCRIPTIONS[descIndex];
			}

			return potion.description.replaceAll(String.valueOf(potion.getPotency()),
					String.valueOf(potency));
		} catch (Exception ex) {
			SageMod.logger.catching(ex);
			return potion.description;
		}
	}

	@Override
	public int getPotency(int asc) {
		return potency;
	}

	@Override
	public AbstractPotion makeCopy() {
		return new UpgradedPotion(potion.makeCopy());
	}

	@Override
	public boolean canUse() {
		return potion.canUse();
	}

	@Override
	public boolean canDiscard() {
		return potion.canDiscard();
	}

	@Override
	public void use(AbstractCreature c) {
		if (upgradedUse != null) {
			customUse(c);
			return;
		}
		ReflectionHacks.setPrivate(potion, AbstractPotion.class, "potency", potency);
		if (DISCOVERY_LIST.contains(potion.ID)) {
			discoveryOpenUpgrades++;
		}
		potion.use(c);
		if (DOUBLE_USE_WHITELIST.contains(potion.ID)) {
			potion.use(c);
		}
	}

	@Override
	public int getPrice() {
		return (int) (potion.getPrice() * PRICE_MULTIPLIER);
	}

	private float getAngle() {
		return (Float) ReflectionHacks.getPrivate(this, AbstractPotion.class, "angle");
	}

	@Override
	public void shopRender(SpriteBatch sb) {
		this.scale = potion.scale;
		potion.posX = posX;
		potion.posY = posY;
		potion.shopRender(sb);
		sb.setColor(Color.WHITE);
		sb.draw(plusImg, posX - 32.0F, posY - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, potion.scale,
				potion.scale,
				getAngle(), 0, 0, 64, 64, false, false);
		renderOutline(sb);
	}

	@Override
	public void render(SpriteBatch sb) {
		potion.scale = scale;
		potion.posX = posX;
		potion.posY = posY;
		potion.render(sb);
		sb.setColor(Color.WHITE);
		sb.draw(plusImg, posX - 32.0F, posY - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, scale, scale,
				getAngle(), 0, 0, 64, 64, false, false);
	}

	@Override
	public void renderShiny(SpriteBatch sb) {
		potion.scale = scale;
		potion.posX = posX;
		potion.posY = posY;
		potion.renderShiny(sb);
		sb.setColor(Color.WHITE);
		sb.draw(plusImg, posX - 32.0F, posY - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, scale, scale,
				getAngle(), 0, 0, 64, 64, false, false);
	}

	@Override
	public void labRender(SpriteBatch sb) {
		potion.scale = scale;
		potion.posX = posX;
		potion.posY = posY;
		potion.labRender(sb);
		sb.setColor(Color.WHITE);
		sb.draw(plusImg, posX - 32.0F, posY - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, scale, scale,
				getAngle(), 0, 0, 64, 64, false, false);

	}

	@Override
	public void renderOutline(SpriteBatch sb, Color c) {
		potion.posX = posX;
		potion.posY = posY;
		potion.renderOutline(sb, c);
	}

	@Override
	public void renderOutline(SpriteBatch sb) {
		potion.posX = posX;
		potion.posY = posY;
		potion.renderOutline(sb, OUTLINE_COLOR);
	}

	@Override
	public void renderLightOutline(SpriteBatch sb) {
		potion.posX = posX;
		potion.posY = posY;
		potion.renderOutline(sb, LIGHT_OUTLINE_COLOR);
	}

	public static class UpgradedPotionSave implements CustomSavable<boolean[]> {

		public static final Logger logger =
				LogManager.getLogger(UpgradedPotionSave.class.getName());

		@Override
		public boolean[] onSave() {
			logger.info("Saving upgraded Potions");
			AbstractPlayer p = AbstractDungeon.player;
			boolean[] save = new boolean[p.potionSlots];
			for (int i = 0; i < p.potionSlots; i++) {
				AbstractPotion potion = p.potions.get(i);
				if (potion instanceof UpgradedPotion) {
					logger.info("Potion in Slot " + i + " is upgraded: " + potion.name);
					save[i] = true;
				} else {
					save[i] = false;
				}
			}
			return save;
		}

		@Override
		public void onLoad(boolean[] array) {
			logger.info("Loading upgraded Potions");
			if (array != null) {
				AbstractPlayer p = AbstractDungeon.player;
				for (int i = 0; i < array.length; i++) {
					if (i >= p.potionSlots) {
						break;
					}
					if (array[i]) {
						AbstractPotion old = p.potions.get(i);
						logger.info("Potion in Slot " + i + " will be upgraded: " + old.name);
						p.obtainPotion(i, UpgradedPotion.getUpgradeIfAvailable(old));
					}
				}
			} else {

			}
		}

	}

}
