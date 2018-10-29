package sagemod;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.Keyword;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;

import basemod.BaseMod;
import basemod.ModButton;
import basemod.ModPanel;
import basemod.interfaces.EditCardsSubscriber;
import basemod.interfaces.EditCharactersSubscriber;
import basemod.interfaces.EditKeywordsSubscriber;
import basemod.interfaces.EditRelicsSubscriber;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.PostInitializeSubscriber;
import sagemod.cards.Accumulation;
import sagemod.cards.AlchemyExpert;
import sagemod.cards.Altitude;
import sagemod.cards.Ambition;
import sagemod.cards.AncientPoison;
import sagemod.cards.AntiAncientAttack;
import sagemod.cards.ArmorBrew;
import sagemod.cards.BoldMove;
import sagemod.cards.Bookworm;
import sagemod.cards.BouncingStrike;
import sagemod.cards.Brewmaster;
import sagemod.cards.BurningFlask;
import sagemod.cards.CatchMeIfYouCan;
import sagemod.cards.CultistForm;
import sagemod.cards.DeadlyContraption;
import sagemod.cards.DefendSage;
import sagemod.cards.Escape;
import sagemod.cards.ExtraPortion;
import sagemod.cards.FireBrew;
import sagemod.cards.FlightFeint;
import sagemod.cards.Fly;
import sagemod.cards.FlyingCauldron;
import sagemod.cards.FoldedCarpet;
import sagemod.cards.GearwheelMaster;
import sagemod.cards.HowToAmuseSneckos;
import sagemod.cards.HowToBefriendATurtle;
import sagemod.cards.HowToFeedApes;
import sagemod.cards.HowToGreetByrds;
import sagemod.cards.HowToMurderAnts;
import sagemod.cards.HowToPokeAtSlimes;
import sagemod.cards.HowToTalkToSpiders;
import sagemod.cards.HowToWakeLyons;
import sagemod.cards.HowToWarmElephants;
import sagemod.cards.LightWeighted;
import sagemod.cards.Lunchtime;
import sagemod.cards.MechanicsBreak;
import sagemod.cards.Meditation;
import sagemod.cards.OnFire;
import sagemod.cards.OnTheHead;
import sagemod.cards.PerpetuumMobile;
import sagemod.cards.PincerAttack;
import sagemod.cards.PincerRepair;
import sagemod.cards.PourTarPitch;
import sagemod.cards.Quackster;
import sagemod.cards.SaltyStrike;
import sagemod.cards.Slalom;
import sagemod.cards.SpoiledFood;
import sagemod.cards.StepBack;
import sagemod.cards.StrikeSage;
import sagemod.cards.Study;
import sagemod.cards.SwoopDown;
import sagemod.cards.TasteThisOne;
import sagemod.cards.TurnAround;
import sagemod.character.SageCharEnum;
import sagemod.character.SageColorEnum;
import sagemod.character.TheSage;
import sagemod.listeners.PotionListener;
import sagemod.patches.BrewingDynamicVariable;
import sagemod.patches.MiscDynamicVariable;
import sagemod.potions.FataMorgana;
import sagemod.potions.FlightPotion;
import sagemod.relics.ByrdCarpet;
import sagemod.relics.FlyingCarpet;

@SpireInitializer
public class SageMod implements EditCharactersSubscriber, EditCardsSubscriber, EditRelicsSubscriber,
EditStringsSubscriber, PostInitializeSubscriber, EditKeywordsSubscriber {

	public static final Logger logger = LogManager.getLogger(SageMod.class.getName());
	public static final String AUTHORS = "jonasdasdefekte, Skrelpoid";

	public static final String PLACEHOLDER = "Placeholder";
	public static final Color COLOR = new Color(0xc65e03);

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
		BaseMod.subscribe(new PotionListener());
	}

	private void addColor() {
		BaseMod.addColor(SageColorEnum.THE_SAGE, COLOR, "sage/cards/512/bg_attack.png", "sage/cards/512/bg_skill.png",
				"sage/cards/512/bg_power.png", "sage/cards/512/orb.png", "sage/cards/1024/bg_attack.png",
				"sage/cards/1024/bg_skill.png", "sage/cards/1024/bg_power.png", "sage/cards/1024/orb.png",
				"sage/cards/orb/desc_orb.png");
	}

	@Override
	public void receiveEditCharacters() {
		logger.info("Adding TheSage");
		BaseMod.addCharacter(new TheSage(TheSage.NAME, SageCharEnum.THE_SAGE), TheSage.BUTTON, TheSage.PORTRAIT,
				SageCharEnum.THE_SAGE);
	}

	@Override
	public void receiveEditCards() {
		logger.info("Adding DynamicVariables for TheSage");
		BaseMod.addDynamicVariable(new MiscDynamicVariable());
		BaseMod.addDynamicVariable(new BrewingDynamicVariable());

		logger.info("Adding Cards for TheSage");

		// Basic
		BaseMod.addCard(new StrikeSage());
		BaseMod.addCard(new DefendSage());
		BaseMod.addCard(new Fly());
		BaseMod.addCard(new BoldMove());

		// Common
		BaseMod.addCard(new HowToWarmElephants());
		BaseMod.addCard(new PincerRepair());
		BaseMod.addCard(new SwoopDown());
		BaseMod.addCard(new FireBrew());
		BaseMod.addCard(new ArmorBrew());
		BaseMod.addCard(new HowToBefriendATurtle());
		BaseMod.addCard(new CatchMeIfYouCan());
		BaseMod.addCard(new SaltyStrike());
		BaseMod.addCard(new HowToTalkToSpiders());
		BaseMod.addCard(new Ambition());
		BaseMod.addCard(new StepBack());
		BaseMod.addCard(new HowToPokeAtSlimes());
		BaseMod.addCard(new Slalom());

		// Uncommon
		BaseMod.addCard(new OnTheHead());
		BaseMod.addCard(new Quackster());
		BaseMod.addCard(new PourTarPitch());
		BaseMod.addCard(new GearwheelMaster());
		BaseMod.addCard(new Altitude());
		BaseMod.addCard(new BurningFlask());
		BaseMod.addCard(new TasteThisOne());
		BaseMod.addCard(new FlightFeint());
		BaseMod.addCard(new Brewmaster());
		BaseMod.addCard(new PincerAttack());
		BaseMod.addCard(new BouncingStrike());
		BaseMod.addCard(new TurnAround());
		BaseMod.addCard(new HowToWakeLyons());
		BaseMod.addCard(new HowToFeedApes());
		BaseMod.addCard(new PerpetuumMobile());
		BaseMod.addCard(new Study());
		BaseMod.addCard(new FlyingCauldron());
		BaseMod.addCard(new Lunchtime());
		BaseMod.addCard(new MechanicsBreak());
		BaseMod.addCard(new SpoiledFood());
		BaseMod.addCard(new HowToGreetByrds());
		BaseMod.addCard(new DeadlyContraption());

		// Rare
		BaseMod.addCard(new Bookworm());
		BaseMod.addCard(new HowToMurderAnts());
		BaseMod.addCard(new Escape());
		BaseMod.addCard(new Accumulation());
		BaseMod.addCard(new AntiAncientAttack());
		BaseMod.addCard(new HowToAmuseSneckos());
		BaseMod.addCard(new ExtraPortion());
		BaseMod.addCard(new FoldedCarpet());
		BaseMod.addCard(new LightWeighted());
		BaseMod.addCard(new Meditation());
		BaseMod.addCard(new CultistForm());
		BaseMod.addCard(new OnFire());
		BaseMod.addCard(new AlchemyExpert());
		BaseMod.addCard(new AncientPoison());

	}

	@Override
	public void receiveEditRelics() {
		logger.info("Adding Relics for TheSage");

		// Starter
		BaseMod.addRelicToCustomPool(new FlyingCarpet(), SageColorEnum.THE_SAGE);

		// Boss
		BaseMod.addRelicToCustomPool(new ByrdCarpet(), SageColorEnum.THE_SAGE);
	}

	@Override
	public void receiveEditStrings() {
		logger.info("Loading Strings for TheSage");
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
			SageMod.logger.info(
					id + " has no image configured. Defaulting to placeholder image (should be in " + prefix + ")");
			return prefix + PLACEHOLDER + postfix;
		}
	}

	@Override
	public void receivePostInitialize() {
		Texture badgeTexture = new Texture(Gdx.files.internal("sage/mod-badge.png"));
		ModPanel panel = new ModPanel();
		if (Loader.DEBUG) {
			panel.addUIElement(new ModButton(400, 400, panel, b -> {
				StatAnalyzer.printStatsForCharacter(SageCharEnum.THE_SAGE);
			}));
		}
		BaseMod.registerModBadge(badgeTexture, TheSage.NAME, AUTHORS, TheSage.DESC, panel);

		SageMod.logger.info("Adding Potions for TheSage");
		// character specific: The Sage
		BaseMod.addPotion(FlightPotion.class, FlightPotion.LIQUID_COLOR, FlightPotion.HYBRID_COLOR,
				FlightPotion.SPOTS_COLOR, FlightPotion.POTION_ID, SageCharEnum.THE_SAGE);
		// all characters
		BaseMod.addPotion(FataMorgana.class, FataMorgana.LIQUID_COLOR, FataMorgana.HYBRID_COLOR,
				FataMorgana.SPOTS_COLOR, FataMorgana.POTION_ID);
	}


}
