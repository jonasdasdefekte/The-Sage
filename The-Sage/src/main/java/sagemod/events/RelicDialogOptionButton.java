package sagemod.events;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.events.AbstractEvent;
import com.megacrit.cardcrawl.helpers.Hitbox;
import com.megacrit.cardcrawl.helpers.TipHelper;
import com.megacrit.cardcrawl.helpers.input.InputHelper;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.ui.buttons.LargeDialogOptionButton;
import basemod.ReflectionHacks;

// From Halation, thanks reina
public class RelicDialogOptionButton extends LargeDialogOptionButton {
	private AbstractRelic r;
	private float x;
	private float y;
	private static Color TEXT_DISABLED_COLOR = Color.FIREBRICK.cpy();
	private Color boxInactiveColor;

	public RelicDialogOptionButton(int slot, String msg, AbstractRelic r, boolean isDisabled) {
		super(slot, msg);
		setTextColor(new Color(0.0F, 0.0F, 0.0F, 0.0F));
		setBoxColor(new Color(0.0F, 0.0F, 0.0F, 0.0F));
		this.boxInactiveColor = new Color(0.2F, 0.25F, 0.25F, 0.0F);
		this.r = r;
		this.y = -9999.0F * Settings.scale;
		this.pressed = false;
		this.slot = 0;
		switch (AbstractEvent.type) {
			case TEXT:
				this.x = 895.0F * Settings.scale;
				break;
			case IMAGE:
				this.x = 1260.0F * Settings.scale;
				break;
			case ROOM:
				this.x = 620.0F * Settings.scale;
				break;
			default:
				break;
		}

		this.slot = slot;
		this.isDisabled = isDisabled;
		if (isDisabled) {
			this.msg = this.stripColor(msg);
		} else {
			this.msg = msg;
		}
		if (isDisabled) {
			setTextColor(TEXT_DISABLED_COLOR);
			setBoxColor(this.boxInactiveColor);
		} else {
			this.hb = new Hitbox(892.0F * Settings.scale, 80.0F * Settings.scale);
		}
	}

	public void renderRelicPreview(SpriteBatch sb) {
		if (this.r != null && this.hb.hovered) {
			this.r.currentX = this.x + this.hb.width / 1.75F;
			if (this.y < this.r.hb.height / 2.0F + 5.0F) {
				this.y = this.r.hb.height / 2.0F + 5.0F;
			}

			this.r.currentY = this.y;
			TipHelper.queuePowerTips((float) InputHelper.mX - 350.0F * Settings.scale,
					Settings.HEIGHT / 3F, r.tips);
		}
	}

	@Override
	public void render(SpriteBatch sb) {
		super.render(sb);
		renderRelicPreview(sb);
	}

	private void setTextColor(Color c) {
		ReflectionHacks.setPrivate(this, LargeDialogOptionButton.class, "textColor", c);
	}

	private void setBoxColor(Color c) {
		ReflectionHacks.setPrivate(this, LargeDialogOptionButton.class, "boxColor", c);
	}

	private String stripColor(String input) {
		input = input.replace("#r", "");
		input = input.replace("#g", "");
		input = input.replace("#b", "");
		input = input.replace("#y", "");
		return input;
	}
}
