package sagemod.potions;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.AttackPotion;
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


	public static final float CHANCE = 0.12f;
	public static final float DEBUG_CHANCE = 0.75f;
	public static final float POTENCY_MULTIPLIER = 2f;
	public static final float PRICE_MULTIPLIER = 1.75f;

	public static final String TWICE = "2x: ";
	private static final Color OUTLINE_COLOR = new Color(0xFFD70088);
	private static final Color LIGHT_OUTLINE_COLOR = new Color(0xFFD70044);

	private static Texture plusImg;

	private AbstractPotion potion;
	private Texture outlineImg;

	public UpgradedPotion(AbstractPotion potion) {
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

	public static AbstractPotion getUpgradeIfAvailable(AbstractPotion potion) {
		if (potion == null) {
			return new FirePotion();
		}
		if (potion.ID.equals(EntropicBrew.POTION_ID) || potion.ID.equals(SmokeBomb.POTION_ID)
				|| potion instanceof UpgradedPotion) {
			return potion;
		} else {
			return new UpgradedPotion(potion);
		}
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
		if (potion instanceof UpgradedPotion) {
			return potion.getPotency();
		}
		return (int) (potion.getPotency() * POTENCY_MULTIPLIER);

	}

	private String loadDescription() {
		if (isDoubledPotion() && !potion.ID.equals(FairyPotion.POTION_ID)) {
			return TWICE + potion.description;
		}
		return potion.description.replaceAll(String.valueOf(potion.getPotency()),
				String.valueOf(potency));
	}

	private boolean isDoubledPotion() {
		return potion.ID.equals(AttackPotion.POTION_ID) || potion.ID.equals(PowerPotion.POTION_ID)
				|| potion.ID.equals(SkillPotion.POTION_ID)
				|| potion.ID.equals(FairyPotion.POTION_ID)
				|| potion.ID.equals(GamblersBrew.POTION_ID);
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
		potion.use(c);
		if (isDoubledPotion()) {
			potion.use(c);
		}
	}

	@Override
	public int getPrice() {
		return (int) (potion.getPrice() * PRICE_MULTIPLIER);
	}

	public void multiplyPotencyBy(float f) {
		potency *= f;
		ReflectionHacks.setPrivate(potion, AbstractPotion.class, "potency", potency);
	}

	private float getAngle() {
		return (Float) ReflectionHacks.getPrivate(this, AbstractPotion.class, "angle");
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
