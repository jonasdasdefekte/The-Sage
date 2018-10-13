package sagemod.character;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.screens.CharSelectInfo;

import basemod.abstracts.CustomPlayer;
import sagemod.SageMod;
import sagemod.cards.BoldMove;
import sagemod.cards.DefendSage;
import sagemod.cards.Fly;
import sagemod.cards.StrikeSage;
import sagemod.relics.FlyingCarpet;

/**
 * The actual Character class
 */
public class TheSage extends CustomPlayer {

	public static final String NAME = "The Sage";
	public static final String DESC = "A mechanic who studied about the cult of a blue bird. NL "
			+ "She learned flying a carpet and brewing potions in order to stop its rise.";
	public static final String BUTTON = "sage/character/button.png";
	public static final String PORTRAIT = "sage/character/portrait.jpg";

	public static final String[] ORB_TEXTURES = new String[] { "sage/character/orb/layer1.png",
			"sage/character/orb/layer2.png", "sage/character/orb/layer3.png", "sage/character/orb/layer4.png",
			"sage/character/orb/layer5.png", "sage/character/orb/layer6.png", "sage/character/orb/layer1d.png",
			"sage/character/orb/layer2d.png", "sage/character/orb/layer3d.png", "sage/character/orb/layer4d.png",
			"sage/character/orb/layer5d.png", };

	public static final int START_HP = 40;
	public static final int CARD_DRAW = 5;
	public static final int MAX_ORBS = 0;
	public static final int ENERGY = 3;
	public static final int START_GOLD = 169;

	private static final String CHAR_SOUND = "ATTACK_MAGIC_SLOW_1";

	private PlayerClass playerClass;

	public TheSage(String name, PlayerClass playerClass) {
		super(name, playerClass, ORB_TEXTURES, "sage/character/orb/vfx.png", (String) null, (String) null);
		this.playerClass = playerClass;
		initializeClass(null, "sage/character/shoulder2.png", "sage/character/shoulder.png",
				"sage/character/corpse.png", getLoadout(), 20.0f, -10.0f, 220.0f, 290.0f, new EnergyManager(ENERGY));
		loadAnimation("sage/character/idle/skeleton.atlas", "sage/character/idle/skeleton.json", 1.0f);
		AnimationState.TrackEntry e = state.setAnimation(0, "idle", true);
		e.setTime(e.getEndTime() * MathUtils.random());
		if (ModHelper.enabledMods.size() > 0 && (ModHelper.isModEnabled("Diverse") || ModHelper.isModEnabled("Chimera"))
				|| Settings.isTrial && customMods != null && customMods.contains("Blue Cards")) {
			masterMaxOrbs = 1;
		}
	}

	@Override
	public CharSelectInfo getLoadout() {
		return new CharSelectInfo(NAME, DESC, START_HP, START_HP, MAX_ORBS, START_GOLD, CARD_DRAW, this,
				getStartingRelics(), getStartingDeck(), false);
	}

	@Override
	public ArrayList<String> getStartingDeck() {
		ArrayList<String> cards = new ArrayList<>();
		// 4x Strike
		cards.add(StrikeSage.ID);
		cards.add(StrikeSage.ID);
		cards.add(StrikeSage.ID);
		cards.add(StrikeSage.ID);

		// 4x Defend
		cards.add(DefendSage.ID);
		cards.add(DefendSage.ID);
		cards.add(DefendSage.ID);
		cards.add(DefendSage.ID);

		// 1x Fly
		cards.add(Fly.ID);

		// 1x Bold Move
		cards.add(BoldMove.ID);
		return cards;
	}

	@Override
	public ArrayList<String> getStartingRelics() {
		ArrayList<String> relics = new ArrayList<>();

		// Flying Carpet
		relics.add(FlyingCarpet.ID);

		return relics;
	}

	@Override
	public String getTitle(PlayerClass playerClass) {
		return NAME;
	}

	@Override
	public Color getCardColor() {
		return SageMod.COLOR;
	}

	@Override
	public AbstractCard getStartCardForEvent() {
		// either Fly or BoldMove
		return AbstractDungeon.cardRandomRng.randomBoolean() ? new Fly() : new BoldMove();
	}

	@Override
	public Color getCardTrailColor() {
		return SageMod.COLOR;
	}

	@Override
	public int getAscensionMaxHPLoss() {
		return 3;
	}

	@Override
	public BitmapFont getEnergyNumFont() {
		return FontHelper.energyNumFontGreen;
	}

	@Override
	public void doCharSelectScreenSelectEffect() {
		CardCrawlGame.sound.playA(CHAR_SOUND, MathUtils.random(-0.2f, 0.2f));
		CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.SHORT, false);
	}

	@Override
	public String getCustomModeCharacterButtonSoundKey() {
		return CHAR_SOUND;
	}

	@Override
	public String getLocalizedCharacterName() {
		return NAME;
	}

	@Override
	public AbstractPlayer newInstance() {
		return new TheSage(name, playerClass);
	}

}
