package sagemod.ui;

import java.util.ArrayList;
import java.util.List;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.helpers.MathHelper;
import com.megacrit.cardcrawl.helpers.controller.CInputActionSet;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.potions.AbstractPotion;
import com.megacrit.cardcrawl.potions.PotionSlot;
import com.megacrit.cardcrawl.rewards.RewardItem;
import sagemod.potions.UpgradedPotion;
import sagemod.relics.SingingVial;

public class SingingVialButton {
	private static final RelicStrings strings =
			CardCrawlGame.languagePack.getRelicStrings(SingingVial.ID);
	private static final String[] TEXT = strings.DESCRIPTIONS;
	private static final float TAKE_Y = 140.0F * Settings.scale;
	private static final float SHOW_X = Settings.WIDTH / 2.0F;
	private static final float HIDE_X = Settings.WIDTH / 2.0F;
	private float current_x;
	private float target_x;
	private Color textColor;
	private Color btnColor;
	private boolean isHidden;
	private RewardItem rItem;
	private static final float HITBOX_W = 260.0F * Settings.scale;;
	private static final float HITBOX_H = 80.0F * Settings.scale;;
	public Hitbox hb;

	public SingingVialButton() {
		current_x = HIDE_X;
		target_x = current_x;
		textColor = Color.WHITE.cpy();
		btnColor = Color.WHITE.cpy();
		isHidden = true;
		rItem = null;


		hb = new Hitbox(0.0F, 0.0F, HITBOX_W, HITBOX_H);


		hb.move(Settings.WIDTH / 2.0F, TAKE_Y);
	}

	public void update() {
		if (!isHidden) {
			hb.update();

			if (hb.justHovered) {
				CardCrawlGame.sound.play("UI_HOVER");
			}
			if (hb.hovered && InputHelper.justClickedLeft) {
				hb.clickStarted = true;
				CardCrawlGame.sound.play("UI_CLICK_1");
			}
			if (hb.clicked || CInputActionSet.altDown.isJustPressed()) {
				CInputActionSet.proceed.unpress();
				hb.clicked = false;
				onClick();
				AbstractDungeon.cardRewardScreen.closeFromBowlButton();
				AbstractDungeon.closeCurrentScreen();
				hide();
			}

			if (current_x != target_x) {
				current_x = MathUtils.lerp(current_x, target_x, Gdx.graphics.getDeltaTime() * 9.0F);
				if (Math.abs(current_x - target_x) < Settings.UI_SNAP_THRESHOLD) {
					current_x = target_x;
					hb.move(current_x, TAKE_Y);
				}
			}
			textColor.a = MathHelper.fadeLerpSnap(textColor.a, 1.0F);
			btnColor.a = textColor.a;
		}
	}

	public void onClick() {
		AbstractDungeon.player.getRelic(SingingVial.ID).flash();
		CardCrawlGame.sound.playA("SINGING_BOWL", MathUtils.random(-0.2F, 0.1F));
		List<Integer> possiblePotions = possiblePotionUpgrades();
		if (!possiblePotions.isEmpty()) {
			int i = possiblePotions
					.get(AbstractDungeon.potionRng.random(possiblePotions.size() - 1));
			AbstractPotion p = AbstractDungeon.player.potions.get(i);
			AbstractDungeon.player.obtainPotion(i, UpgradedPotion.getUpgradeIfAvailable(p));
		}
		AbstractDungeon.combatRewardScreen.rewards.remove(rItem);
	}

	public static List<Integer> possiblePotionUpgrades() {
		List<Integer> list = new ArrayList<>();
		for (int i = 0; i < AbstractDungeon.player.potionSlots; i++) {
			AbstractPotion p = AbstractDungeon.player.potions.get(i);
			if (!(p instanceof PotionSlot || p instanceof UpgradedPotion
					|| UpgradedPotion.BLACKLIST.contains(p.ID))) {
				list.add(i);
			}
		}
		return list;
	}

	public void hide() {
		if (!isHidden) {
			isHidden = true;
		}
	}

	public void show(RewardItem rItem) {
		isHidden = false;
		textColor.a = 0.0F;
		btnColor.a = 0.0F;
		current_x = HIDE_X;
		target_x = SHOW_X;
		this.rItem = rItem;
	}

	public void render(SpriteBatch sb) {
		if (!isHidden) {
			renderButton(sb);
			FontHelper.renderFontCentered(sb, FontHelper.buttonLabelFont, TEXT[1], current_x,
					TAKE_Y, textColor);
		}
	}

	public boolean isHidden() {
		return isHidden;
	}

	private void renderButton(SpriteBatch sb) {
		sb.setColor(btnColor);
		sb.draw(ImageMaster.REWARD_SCREEN_TAKE_BUTTON, current_x - 256.0F, TAKE_Y - 128.0F, 256.0F,
				128.0F, 512.0F, 256.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 512, 256, false,
				false);



		if (hb.hovered && !hb.clickStarted) {
			sb.setBlendFunction(770, 1);
			sb.setColor(new Color(1.0F, 1.0F, 1.0F, 0.3F));
			sb.draw(ImageMaster.REWARD_SCREEN_TAKE_BUTTON, current_x - 256.0F, TAKE_Y - 128.0F,
					256.0F, 128.0F, 512.0F, 256.0F, Settings.scale, Settings.scale, 0.0F, 0, 0, 512,
					256, false, false);



			sb.setBlendFunction(770, 771);
		}

		if (Settings.isControllerMode) {
			sb.setColor(Color.WHITE);
			sb.draw(CInputActionSet.altDown.getKeyImg(),
					current_x - 32.0F - 140.0F * Settings.scale,
					TAKE_Y - 32.0F, 32.0F, 32.0F, 64.0F, 64.0F, Settings.scale, Settings.scale,
					0.0F, 0, 0, 64, 64, false, false);



		}

		hb.render(sb);
	}
}
