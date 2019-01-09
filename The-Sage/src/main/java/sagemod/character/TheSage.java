package sagemod.character;

import java.util.ArrayList;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.MathUtils;
import com.brashmonkey.spriter.PlayerTweener;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardColor;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.EnergyManager;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.events.city.Vampires;
import com.megacrit.cardcrawl.helpers.FontHelper;
import com.megacrit.cardcrawl.helpers.ModHelper;
import com.megacrit.cardcrawl.helpers.ScreenShake;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.screens.CharSelectInfo;
import basemod.abstracts.CustomPlayer;
import basemod.animations.SpriterAnimation;
import sagemod.SageMod;
import sagemod.cards.BoldMove;
import sagemod.cards.Defend;
import sagemod.cards.Fly;
import sagemod.cards.Strike;
import sagemod.relics.FlyingCarpet;

/**
 * The actual Character class
 */
public class TheSage extends CustomPlayer {

	public static final String ID = "Sage";
	public static final CharacterStrings characterStrings =
			CardCrawlGame.languagePack.getCharacterString(ID);
	public static final String NAME = characterStrings.NAMES[0];
	public static final String DESC = characterStrings.TEXT[0];
	public static final String BUTTON = "sage/character/button.png";
	public static final String PORTRAIT = "sage/character/portrait.jpg";

	public static final String[] ORB_TEXTURES = new String[] {"sage/character/orb/layer1.png",
			"sage/character/orb/layer2.png", "sage/character/orb/layer3.png",
			"sage/character/orb/layer4.png",
			"sage/character/orb/layer5.png", "sage/character/orb/layer6.png",
			"sage/character/orb/layer1d.png",
			"sage/character/orb/layer2d.png", "sage/character/orb/layer3d.png",
			"sage/character/orb/layer4d.png",
			"sage/character/orb/layer5d.png",};

	public static final int START_HP = 50;
	public static final int CARD_DRAW = 5;
	public static final int MAX_ORBS = 0;
	public static final int ENERGY = 3;
	public static final int START_GOLD = 169;

	private static final String CHAR_SOUND = "ATTACK_MAGIC_SLOW_1";
	private static final float GROUND_DIALOG_Y_OFFSET = 240f;
	private static final float FLIGHT_DIALOG_Y_OFFSET = 320f;

	public static final int GROUND = 0;
	public static final int FLIGHT = 1;

	private PlayerClass playerClass;
	private PlayerTweener animationPlayer;

	public TheSage(String name, PlayerClass playerClass) {
		super(name, playerClass, ORB_TEXTURES, "sage/character/orb/vfx.png",
				new SpriterAnimation("sage/character/idle/SageAnimations.scml"));
		animationPlayer = ((SpriterAnimation) animation).myPlayer;
		this.playerClass = playerClass;
		dialogX = drawX + 0.0F * Settings.scale; // set location for text bubbles
		dialogY = drawY + GROUND_DIALOG_Y_OFFSET * Settings.scale;
		initializeClass(null, "sage/character/shoulder2.png", "sage/character/shoulder.png",
				"sage/character/corpse.png", getLoadout(), 20.0f, -10.0f, 220.0f, 290.0f,
				new EnergyManager(ENERGY));
		if (ModHelper.enabledMods.size() > 0
				&& (ModHelper.isModEnabled("Diverse") || ModHelper.isModEnabled("Chimera"))
				|| Settings.isTrial && customMods != null && customMods.contains("Blue Cards")) {
			masterMaxOrbs = 1;
		}
	}

	public static void setSageAnimation(int from, int to) {
		if (AbstractDungeon.player instanceof TheSage) {
			TheSage sage = (TheSage) AbstractDungeon.player;
			sage.animationPlayer.setBaseAnimation(from);
			sage.animationPlayer.getFirstPlayer().setAnimation(from);
			sage.animationPlayer.getSecondPlayer().setAnimation(to);
			sage.animationPlayer.setWeight(1);
			sage.updateDialogY();
		}
	}

	public void updateDialogY() {
		if (animationPlayer.getSecondPlayer().getAnimation().id == FLIGHT) {
			dialogY = drawY + FLIGHT_DIALOG_Y_OFFSET * Settings.scale;
		} else {
			dialogY = drawY + GROUND_DIALOG_Y_OFFSET * Settings.scale;
		}
	}

	@Override
	public CharSelectInfo getLoadout() {
		return new CharSelectInfo(NAME, DESC, START_HP, START_HP, MAX_ORBS, START_GOLD, CARD_DRAW,
				this,
				getStartingRelics(), getStartingDeck(), false);
	}

	@Override
	public ArrayList<String> getStartingDeck() {
		ArrayList<String> cards = new ArrayList<>();
		// 4x Strike
		cards.add(Strike.ID);
		cards.add(Strike.ID);
		cards.add(Strike.ID);
		cards.add(Strike.ID);

		// 4x Defend
		cards.add(Defend.ID);
		cards.add(Defend.ID);
		cards.add(Defend.ID);
		cards.add(Defend.ID);

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
	public CardColor getCardColor() {
		return SageColorEnum.THE_SAGE;
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
		CardCrawlGame.screenShake.shake(ScreenShake.ShakeIntensity.MED, ScreenShake.ShakeDur.SHORT,
				false);
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

	@Override
	public Color getCardRenderColor() {
		return SageMod.COLOR;
	}

	@Override
	public String getSpireHeartText() {
		return characterStrings.TEXT[1];
	}

	@Override
	public Color getSlashAttackColor() {
		return SageMod.COLOR;
	}

	@Override
	public AttackEffect[] getSpireHeartSlashEffect() {
		return new AttackEffect[] {AttackEffect.SMASH, AttackEffect.FIRE, AttackEffect.POISON,
				AttackEffect.SMASH,
				AttackEffect.FIRE, AttackEffect.POISON};
	}

	@Override
	public String getVampireText() {
		// sister
		return Vampires.DESCRIPTIONS[1];
	}

}
