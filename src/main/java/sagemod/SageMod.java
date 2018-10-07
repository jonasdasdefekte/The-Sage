package sagemod;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.Keyword;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;

import basemod.BaseMod;
import basemod.ModPanel;
import basemod.interfaces.EditCardsSubscriber;
import basemod.interfaces.EditCharactersSubscriber;
import basemod.interfaces.EditKeywordsSubscriber;
import basemod.interfaces.EditRelicsSubscriber;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.PostInitializeSubscriber;
import sagemod.cards.Accumulation;
import sagemod.cards.Altitude;
import sagemod.cards.ArmorBrew;
import sagemod.cards.BoldMove;
import sagemod.cards.CatchMeIfYouCan;
import sagemod.cards.DefendSage;
import sagemod.cards.Escape;
import sagemod.cards.ExplosiveBrew;
import sagemod.cards.Fly;
import sagemod.cards.GearwheelMaster;
import sagemod.cards.HowToBefriendATurtle;
import sagemod.cards.HowToMurderAnts;
import sagemod.cards.HowToWarmElephants;
import sagemod.cards.OnTheHead;
import sagemod.cards.PincerRepair;
import sagemod.cards.PourTarPitch;
import sagemod.cards.Quackster;
import sagemod.cards.SaltyStrike;
import sagemod.cards.StrikeSage;
import sagemod.cards.SwoopDown;
import sagemod.character.SageCharEnum;
import sagemod.character.SageColorEnum;
import sagemod.character.TheSage;
import sagemod.listeners.ThirstyListener;
import sagemod.patches.MiscDynamicVariable;
import sagemod.relics.FlyingCarpet;

@SpireInitializer
public class SageMod implements EditCharactersSubscriber, EditCardsSubscriber, EditRelicsSubscriber,
		EditStringsSubscriber, PostInitializeSubscriber, EditKeywordsSubscriber {

	public static final Logger logger = LogManager.getLogger(SageMod.class.getName());
	public static final String AUTHORS = "jonasdasdefekte, Skrelpoid";

	public static final String PLACEHOLDER = "Placeholder";

	/**
	 * The initializing method for ModTheSpire. This gets called before the game is
	 * loaded.
	 */
	public static void initialize() {
		logger.info("Initializing TheSage");
		new SageMod();
	}

	public SageMod() {
		addColor();
		BaseMod.subscribe(this);
		BaseMod.subscribe(new ThirstyListener());
	}

	private void addColor() {
		BaseMod.addColor(SageColorEnum.THE_SAGE, new Color(0xc65e03), "sage/cards/512/bg_attack.png",
				"sage/cards/512/bg_skill.png", "sage/cards/512/bg_power.png", "sage/cards/512/orb.png",
				"sage/cards/1024/bg_attack.png", "sage/cards/1024/bg_skill.png", "sage/cards/1024/bg_power.png",
				"sage/cards/1024/orb.png", "sage/cards/orb/desc_orb.png");
	}

	@Override
	public void receiveEditCharacters() {
		logger.info("Adding TheSage");
		BaseMod.addCharacter(TheSage.class, TheSage.NAME, TheSage.NAME, SageColorEnum.THE_SAGE, TheSage.NAME,
				TheSage.BUTTON, TheSage.PORTRAIT, SageCharEnum.THE_SAGE);
	}

	@Override
	public void receiveEditCards() {
		logger.info("Adding Cards for TheSage");

		BaseMod.addDynamicVariable(new MiscDynamicVariable());

		// Basic
		BaseMod.addCard(new StrikeSage());
		BaseMod.addCard(new DefendSage());
		BaseMod.addCard(new Fly());
		BaseMod.addCard(new BoldMove());

		// Common
		BaseMod.addCard(new HowToWarmElephants());
		BaseMod.addCard(new PincerRepair());
		BaseMod.addCard(new OnTheHead());
		BaseMod.addCard(new SwoopDown());
		BaseMod.addCard(new ExplosiveBrew());
		BaseMod.addCard(new Quackster());
		BaseMod.addCard(new ArmorBrew());
		BaseMod.addCard(new HowToBefriendATurtle());
		BaseMod.addCard(new CatchMeIfYouCan());
		BaseMod.addCard(new SaltyStrike());

		// Uncommon
		BaseMod.addCard(new PourTarPitch());
		BaseMod.addCard(new GearwheelMaster());
		BaseMod.addCard(new Altitude());

		// Rare
		BaseMod.addCard(new HowToMurderAnts());
		BaseMod.addCard(new Escape());
		BaseMod.addCard(new Accumulation());
	}

	@Override
	public void receiveEditRelics() {
		logger.info("Adding Relics for TheSage");

		// Starter
		BaseMod.addRelicToCustomPool(new FlyingCarpet(), SageColorEnum.THE_SAGE);
	}

	@Override
	public void receiveEditStrings() {
		logger.info("Loading Strings for TheSage");
		// TODO OrderJson these
		BaseMod.loadCustomStrings(CardStrings.class, loadJson("sage/local/cards.json"));
		BaseMod.loadCustomStrings(RelicStrings.class, loadJson("sage/local/relics.json"));
		BaseMod.loadCustomStrings(PowerStrings.class, loadJson("sage/local/powers.json"));
	}

	@Override
	public void receiveEditKeywords() {
		logger.info("Adding Keywords for TheSage");
		// Note: KeywordStrings is a horrible hardcoded class, we can't use it
		// use a custom class instead
		// Copied from MadScienceMod
		Type typeToken = new TypeToken<Map<String, Keyword>>() {}.getType();
		Gson gson = new Gson();
		String strings = loadJson("sage/local/keywords.json");
		Map<String, Keyword> keywords = gson.<Map<String, Keyword>>fromJson(strings, typeToken);
		for (Keyword kw : keywords.values()) {
			BaseMod.addKeyword(kw.NAMES, kw.DESCRIPTION);
		}
	}

	// Copied from MadScienceMod
	private static String loadJson(String jsonPath) {
		return Gdx.files.internal(jsonPath).readString(String.valueOf(StandardCharsets.UTF_8));
	}

	public static String getExistingOrPlaceholder(String prefix, String id, String postfix) {
		String maybeExisting = prefix + id + postfix;
		if (Gdx.files.internal(maybeExisting).exists()) {
			return maybeExisting;
		} else {
			SageMod.logger.info(id + " has no image configured. Defaulting to placeholder image");
			return prefix + PLACEHOLDER + postfix;
		}
	}

	@Override
	public void receivePostInitialize() {
		Texture badgeTexture = new Texture(Gdx.files.internal("sage/mod-badge.png"));
		BaseMod.registerModBadge(badgeTexture, TheSage.NAME, AUTHORS, TheSage.DESC, new ModPanel());
	}

}
