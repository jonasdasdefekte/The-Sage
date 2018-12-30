package sagemod.potions;

import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.AttackPotion;
import com.megacrit.cardcrawl.potions.BloodPotion;
import com.megacrit.cardcrawl.potions.EntropicBrew;
import com.megacrit.cardcrawl.potions.FairyPotion;
import com.megacrit.cardcrawl.potions.FirePotion;
import com.megacrit.cardcrawl.potions.GamblersBrew;
import com.megacrit.cardcrawl.potions.PowerPotion;
import com.megacrit.cardcrawl.potions.SkillPotion;
import com.megacrit.cardcrawl.potions.SmokeBomb;
import basemod.ReflectionHacks;
import basemod.abstracts.CustomPotion;

public class UpgradedPotion extends CustomPotion {

	public static final String SAVE_POSTFIX = "+SAGE+";

	public static final List<String> BLACKLIST = new ArrayList<>();
	public static final List<String> DOUBLE_USE_WHITELIST = new ArrayList<>();


	public static final float CHANCE = 0.20f;
	public static final float POTENCY_MULTIPLIER = 1.5f;
	public static final float PRICE_MULTIPLIER = 1.75f;

	public static final String TWICE = "2x: ";
	private static final Color OUTLINE_COLOR = new Color(0xFFD70088);
	private static final Color LIGHT_OUTLINE_COLOR = new Color(0xFFD70044);

	private static Texture plusImg;

	private AbstractPotion potion;
	private Texture outlineImg;

	private UpgradedPotion(AbstractPotion potion) {
		super(loadName(potion), potion.ID, potion.rarity, potion.size, potion.color);
		this.potion = potion;

		potency = loadPotency();
		ReflectionHacks.setPrivate(potion, AbstractPotion.class, "potency", potency);

		description = loadDescription();

		tips.add(new PowerTip(name, description));
		for (int i = 1; i < potion.tips.size(); i++) {
			tips.add(potion.tips.get(i));
		}


		isThrown = potion.isThrown;
		targetRequired = potion.targetRequired;

		outlineImg = (Texture) ReflectionHacks.getPrivate(this, AbstractPotion.class, "outlineImg");

	}


	public static AbstractPotion forceUpgrade(AbstractPotion potion) {
		return new UpgradedPotion(potion);
	}

	public static AbstractPotion getUpgradeIfAvailable(AbstractPotion potion) {
		if (potion == null) {
			return new FirePotion();
		}
		if (BLACKLIST.contains(potion.ID) || potion instanceof UpgradedPotion) {
			return potion;
		} else {
			return new UpgradedPotion(potion);
		}
	}

	public static void initLists() {
		BLACKLIST.add(EntropicBrew.POTION_ID);
		BLACKLIST.add(SmokeBomb.POTION_ID);
		BLACKLIST.add("Elixir");
		BLACKLIST.add("conspire:TimeTravelPotion");
		BLACKLIST.add("jedi:holywater");
		BLACKLIST.add("HastePotion");
		BLACKLIST.add("Gelsemium Tea");

		DOUBLE_USE_WHITELIST.add(AttackPotion.POTION_ID);
		DOUBLE_USE_WHITELIST.add(PowerPotion.POTION_ID);
		DOUBLE_USE_WHITELIST.add(SkillPotion.POTION_ID);
		DOUBLE_USE_WHITELIST.add(GamblersBrew.POTION_ID);
		DOUBLE_USE_WHITELIST.add(BloodPotion.POTION_ID);
		DOUBLE_USE_WHITELIST.add("blackbeard:" + BloodPotion.POTION_ID);
		DOUBLE_USE_WHITELIST.add("Flashbang");
		DOUBLE_USE_WHITELIST.add("infinitespire:BlackPotion");
		DOUBLE_USE_WHITELIST.add("WardPotion");
		DOUBLE_USE_WHITELIST.add("ShroomBrew");
		DOUBLE_USE_WHITELIST.add("Firefly");
		DOUBLE_USE_WHITELIST.add("Pckles");
		DOUBLE_USE_WHITELIST.add("Turbid Wine");
		DOUBLE_USE_WHITELIST.add("conspire:EchoDraught");
		DOUBLE_USE_WHITELIST.add("construct:MegaPotion");
		DOUBLE_USE_WHITELIST.add(FairyPotion.POTION_ID);
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
		if (potion instanceof UpgradedPotion || DOUBLE_USE_WHITELIST.contains(potion.ID)) {
			return potion.getPotency();
		} else if (potion.ID.equals("Doom Potion")) {
			return (int) (potion.getPotency() * 0.75f);
		}
		return MathUtils.ceil(potion.getPotency() * POTENCY_MULTIPLIER);

	}

	private String loadDescription() {
		if (DOUBLE_USE_WHITELIST.contains(potion.ID)) {
			return TWICE + potion.description;
		}

		return potion.description.replaceAll(String.valueOf(potion.getPotency()),
				String.valueOf(potency));
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
	public void use(AbstractCreature c) {
		ReflectionHacks.setPrivate(potion, AbstractPotion.class, "potency", potency);
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
		super.shopRender(sb);
		sb.setColor(Color.WHITE);
		sb.draw(plusImg, posX - 32.0F, posY - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, scale, scale,
				getAngle(), 0, 0, 64, 64, false, false);
	}

	@Override
	public void render(SpriteBatch sb) {
		super.render(sb);
		sb.setColor(Color.WHITE);
		sb.draw(plusImg, posX - 32.0F, posY - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, scale, scale,
				getAngle(), 0, 0, 64, 64, false, false);
	}

	@Override
	public void renderOutline(SpriteBatch sb) {
		sb.setColor(OUTLINE_COLOR);
		sb.draw(outlineImg, posX - 32.0F, posY - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, scale, scale,
				getAngle(), 0, 0, 64, 64, false, false);
	}

	@Override
	public void renderLightOutline(SpriteBatch sb) {
		sb.setColor(LIGHT_OUTLINE_COLOR);
		sb.draw(outlineImg, posX - 32.0F, posY - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, scale, scale,
				getAngle(), 0, 0, 64, 64, false, false);
	}

}
