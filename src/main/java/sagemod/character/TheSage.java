package sagemod.character;

import java.util.ArrayList;

import com.badlogic.gdx.math.MathUtils;
import com.esotericsoftware.spine.AnimationState;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.daily.DailyMods;
import com.megacrit.cardcrawl.relics.Circlet;
import com.megacrit.cardcrawl.screens.CharSelectInfo;

import basemod.abstracts.CustomPlayer;
import sagemode.cards.DefendSage;
import sagemode.cards.FireBrew;
import sagemode.cards.Fly;
import sagemode.cards.StrikeSage;

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

	public TheSage(String name, PlayerClass playerClass) {
		super(name, playerClass, ORB_TEXTURES, "sage/character/orb/vfx.png", (String) null, (String) null);
		initializeClass(null, "sage/character/shoulder2.png", "sage/character/shoulder.png",
				"sage/character/corpse.png", getLoadout(), 20.0f, -10.0f, 220.0f, 290.0f, new EnergyManager(ENERGY));
		loadAnimation("sage/character/idle/skeleton.atlas", "sage/character/idle/skeleton.json", 1.0f);
		AnimationState.TrackEntry e = state.setAnimation(0, "idle", true);
		e.setTime(e.getEndTime() * MathUtils.random());
		if (Settings.dailyModsEnabled() && DailyMods.cardMods.get("Diverse").booleanValue()
				|| Settings.isTrial && customMods != null && customMods.contains("Blue Cards")) {
			masterMaxOrbs = 1;
		}
	}

	public static CharSelectInfo getLoadout() {
		return new CharSelectInfo(NAME, DESC, START_HP, START_HP, MAX_ORBS, START_GOLD, CARD_DRAW,
				SageCharEnum.THE_SAGE, getStartingRelics(), getStartingDeck(), false);
	}

	public static ArrayList<String> getStartingDeck() {
		ArrayList<String> cards = new ArrayList<>();
		// 5x Strike
		cards.add(StrikeSage.ID);
		cards.add(StrikeSage.ID);
		cards.add(StrikeSage.ID);
		cards.add(StrikeSage.ID);
		cards.add(StrikeSage.ID);

		// 5x Defend
		cards.add(DefendSage.ID);
		cards.add(DefendSage.ID);
		cards.add(DefendSage.ID);
		cards.add(DefendSage.ID);
		cards.add(DefendSage.ID);

		// 1x Fly
		cards.add(Fly.ID);

		// 1x Fire Brew
		cards.add(FireBrew.ID);
		return cards;
	}

	public static ArrayList<String> getStartingRelics() {
		ArrayList<String> relics = new ArrayList<>();
		relics.add(Circlet.ID);
		return relics;
	}

}
