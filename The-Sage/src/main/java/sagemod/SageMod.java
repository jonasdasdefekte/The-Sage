package sagemod;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.evacipated.cardcrawl.modthespire.Loader;
import com.evacipated.cardcrawl.modthespire.lib.SpireConfig;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.helpers.CardHelper;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.localization.Keyword;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.localization.TutorialStrings;
import com.megacrit.cardcrawl.rooms.AbstractRoom;
import basemod.BaseMod;
import basemod.ModButton;
import basemod.ModPanel;
import basemod.helpers.RelicType;
import basemod.interfaces.EditCardsSubscriber;
import basemod.interfaces.EditCharactersSubscriber;
import basemod.interfaces.EditKeywordsSubscriber;
import basemod.interfaces.EditRelicsSubscriber;
import basemod.interfaces.EditStringsSubscriber;
import basemod.interfaces.OnStartBattleSubscriber;
import basemod.interfaces.PostBattleSubscriber;
import basemod.interfaces.PostInitializeSubscriber;
import sagemod.cards.AboutClark;
import sagemod.cards.Accumulation;
import sagemod.cards.AlchemyExpert;
import sagemod.cards.Altitude;
import sagemod.cards.Ambition;
import sagemod.cards.AntiAncientAttack;
import sagemod.cards.ArmorBrew;
import sagemod.cards.Blurry;
import sagemod.cards.Bookworm;
import sagemod.cards.BouncingStrike;
import sagemod.cards.Brewmaster;
import sagemod.cards.BurningFlask;
import sagemod.cards.CaneStrike;
import sagemod.cards.CatchMeIfYouCan;
import sagemod.cards.Crank;
import sagemod.cards.CultistForm;
import sagemod.cards.DeadlyContraption;
import sagemod.cards.Defend;
import sagemod.cards.EnergeticBrew;
import sagemod.cards.Escape;
import sagemod.cards.ExtraPortion;
import sagemod.cards.FireBrew;
import sagemod.cards.FlightFeint;
import sagemod.cards.Fly;
import sagemod.cards.FlyingCauldron;
import sagemod.cards.FoldedCarpet;
import sagemod.cards.GearwheelMaster;
import sagemod.cards.GroundedStrike;
import sagemod.cards.HowToAmuseSneckos;
import sagemod.cards.HowToBefriendATurtle;
import sagemod.cards.HowToCharmASentry;
import sagemod.cards.HowToFeedApes;
import sagemod.cards.HowToGreetByrds;
import sagemod.cards.HowToMurderAnts;
import sagemod.cards.HowToPokeAtSlimes;
import sagemod.cards.HowToWakeLyons;
import sagemod.cards.HowToWarmElephants;
import sagemod.cards.Lecture;
import sagemod.cards.LetItGo;
import sagemod.cards.LightWeighted;
import sagemod.cards.Lunchtime;
import sagemod.cards.MechanicsBreak;
import sagemod.cards.Meditation;
import sagemod.cards.Momentum;
import sagemod.cards.OnFire;
import sagemod.cards.OnTheHead;
import sagemod.cards.PerpetuumMobile;
import sagemod.cards.PincerAttack;
import sagemod.cards.PincerRepair;
import sagemod.cards.Poem;
import sagemod.cards.PotionExplosion;
import sagemod.cards.PotionTrance;
import sagemod.cards.PourTarPitch;
import sagemod.cards.Prey;
import sagemod.cards.Quackster;
import sagemod.cards.ReadTheRiotAct;
import sagemod.cards.Riches;
import sagemod.cards.RicketyDefense;
import sagemod.cards.RockOil;
import sagemod.cards.SaltyStrike;
import sagemod.cards.Shatter;
import sagemod.cards.ShiningPowder;
import sagemod.cards.Slalom;
import sagemod.cards.SpoiledFood;
import sagemod.cards.SpoonStrike;
import sagemod.cards.StepBack;
import sagemod.cards.Strike;
import sagemod.cards.Study;
import sagemod.cards.SwoopDown;
import sagemod.cards.TasteThisOne;
import sagemod.cards.Tinker;
import sagemod.cards.TurnAround;
import sagemod.cards.Undermine;
import sagemod.cards.Upwards;
import sagemod.cards.VigorousBody;
import sagemod.cards.colorless.PerplexingGlare;
import sagemod.cards.colorless.Refine;
import sagemod.cards.debug.AvailablePotionUpgrade;
import sagemod.cards.debug.ConsolePotion;
import sagemod.cards.debug.CyclePotions;
import sagemod.cards.debug.ForcePotionUpgrade;
import sagemod.character.SageCharEnum;
import sagemod.character.SageColorEnum;
import sagemod.character.TheSage;
import sagemod.events.CarpetTrader;
import sagemod.listeners.LectureListener;
import sagemod.listeners.PotionListener;
import sagemod.potions.FataMorgana;
import sagemod.potions.FlightPotion;
import sagemod.potions.HotPepperino;
import sagemod.potions.UpgradedPotion;
import sagemod.potions.UpgradedPotion.UpgradedPotionSave;
import sagemod.potions.Wine;
import sagemod.powers.Accumulate;
import sagemod.powers.Airborne;
import sagemod.powers.AlchemyExpertPower;
import sagemod.powers.AltitudePower;
import sagemod.powers.BookwormPower;
import sagemod.powers.Brew;
import sagemod.powers.Brewing;
import sagemod.powers.DeadlyContraptionPlayerPower;
import sagemod.powers.Disoriented;
import sagemod.powers.ExtraPortionPower;
import sagemod.powers.Flight;
import sagemod.powers.LoseFlightNextTurn;
import sagemod.powers.NoEnergyPower;
import sagemod.powers.OnFirePower;
import sagemod.powers.PoemPower;
import sagemod.powers.PotionTrancePower;
import sagemod.powers.RichesPower;
import sagemod.powers.RicketyDefensePower;
import sagemod.powers.TasteThisOnePower;
import sagemod.powers.Thirsty;
import sagemod.powers.TrixterPower;
import sagemod.powers.UnderminePlayerPower;
import sagemod.powers.VigorousBodyPower;
import sagemod.relics.AncientMagnet;
import sagemod.relics.BalloonAnimal;
import sagemod.relics.Blowpipe;
import sagemod.relics.ByrdFeather;
import sagemod.relics.CabbageHead;
import sagemod.relics.Cookbook;
import sagemod.relics.CreepyCarousel;
import sagemod.relics.FalmelsAmulet;
import sagemod.relics.FlyingCarpet;
import sagemod.relics.RedBeastStatue;
import sagemod.relics.SingingVial;
import sagemod.relics.TheGuidebookGuide;
import sagemod.relics.ThunderCarpet;
import sagemod.tips.SageTipTracker;
import sagemod.variables.BrewingDynamicVariable;
import sagemod.variables.MiscDynamicVariable;

@SpireInitializer
public class SageMod implements EditCharactersSubscriber, EditCardsSubscriber, EditRelicsSubscriber,
		EditStringsSubscriber, PostInitializeSubscriber, EditKeywordsSubscriber,
		PostBattleSubscriber, OnStartBattleSubscriber {

	public static final Logger logger = LogManager.getLogger(SageMod.class.getName());
	public static final String AUTHORS = "jonasdasdefekte, Skrelpoid";

	public static final String PLACEHOLDER = "Placeholder";
	public static final Color COLOR = CardHelper.getColor(198f, 94f, 3f);
	public static String LOCALIZATION_FOLDER = "sage/local/";

	public static final String EVENT_FOLDER = "sage/events/";
	public static final String MOD_ID_PREFIX = "sagemod:";
	public static final String MOD_NAME = "sagemod";
	public static final String MOD_CONFIG = "sagemod.config";

	public static BitmapFont brewFont;

	private static String localLanguage;

	public static SpireConfig config;

	public static boolean isSuperFastModeLoaded = false;
	static {
		isSuperFastModeLoaded = Loader.isModLoaded("superfastmode");
		logger.info("SuperFastMode is loaded");
	}

	/**
	 * The initializing method for ModTheSpire. This gets called before the game is loaded.
	 */
	public static void initialize() {
		logger.info("Initializing TheSage");
		new SageMod();
	}

	public SageMod() {
		addColor();
		BaseMod.subscribe(this);
		BaseMod.subscribe(new PotionListener());
		BaseMod.subscribe(new LectureListener());
		BaseMod.addSaveField("sage:upgraded_potions", new UpgradedPotionSave());
	}

	private void addColor() {
		BaseMod.addColor(SageColorEnum.THE_SAGE, COLOR, "sage/cards/512/bg_attack.png",
				"sage/cards/512/bg_skill.png",
				"sage/cards/512/bg_power.png", "sage/cards/512/orb.png",
				"sage/cards/1024/bg_attack.png",
				"sage/cards/1024/bg_skill.png", "sage/cards/1024/bg_power.png",
				"sage/cards/1024/orb.png",
				"sage/cards/orb/desc_orb.png");
	}

	@Override
	public void receiveEditCharacters() {
		logger.info("Adding TheSage");
		BaseMod.addCharacter(new TheSage(CardCrawlGame.playerName, SageCharEnum.THE_SAGE),
				TheSage.BUTTON, TheSage.PORTRAIT,
				SageCharEnum.THE_SAGE);
	}

	@Override
	public void receiveEditCards() {
		logger.info("Adding DynamicVariables for TheSage");
		BaseMod.addDynamicVariable(new MiscDynamicVariable());
		BaseMod.addDynamicVariable(new BrewingDynamicVariable());

		logger.info("Adding Cards for TheSage");

		// Basic
		BaseMod.addCard(new Strike());
		BaseMod.addCard(new Defend());
		BaseMod.addCard(new Fly());
		BaseMod.addCard(new Upwards());

		// Common
		BaseMod.addCard(new HowToWarmElephants());
		BaseMod.addCard(new PincerRepair());
		BaseMod.addCard(new SwoopDown());
		BaseMod.addCard(new FireBrew());
		BaseMod.addCard(new ArmorBrew());
		BaseMod.addCard(new HowToBefriendATurtle());
		BaseMod.addCard(new CatchMeIfYouCan());
		BaseMod.addCard(new SaltyStrike());
		BaseMod.addCard(new Ambition());
		BaseMod.addCard(new StepBack());
		BaseMod.addCard(new HowToPokeAtSlimes());
		BaseMod.addCard(new Slalom());
		BaseMod.addCard(new Crank());
		BaseMod.addCard(new EnergeticBrew());
		BaseMod.addCard(new GroundedStrike());
		BaseMod.addCard(new HowToCharmASentry());
		BaseMod.addCard(new CaneStrike());
		BaseMod.addCard(new AboutClark());

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
		BaseMod.addCard(new PerpetuumMobile());
		BaseMod.addCard(new Study());
		BaseMod.addCard(new FlyingCauldron());
		BaseMod.addCard(new Lunchtime());
		BaseMod.addCard(new MechanicsBreak());
		BaseMod.addCard(new SpoiledFood());
		BaseMod.addCard(new HowToGreetByrds());
		BaseMod.addCard(new DeadlyContraption());
		BaseMod.addCard(new HowToMurderAnts());
		BaseMod.addCard(new HowToAmuseSneckos());
		BaseMod.addCard(new PotionTrance());
		BaseMod.addCard(new RicketyDefense());
		BaseMod.addCard(new PotionExplosion());
		BaseMod.addCard(new LetItGo());
		BaseMod.addCard(new Tinker());
		BaseMod.addCard(new RockOil());
		BaseMod.addCard(new Momentum());
		BaseMod.addCard(new ShiningPowder());
		BaseMod.addCard(new Undermine());
		BaseMod.addCard(new SpoonStrike());
		BaseMod.addCard(new Poem());
		BaseMod.addCard(new Bookworm());

		// Rare
		BaseMod.addCard(new Escape());
		BaseMod.addCard(new Accumulation());
		BaseMod.addCard(new AntiAncientAttack());
		BaseMod.addCard(new ExtraPortion());
		BaseMod.addCard(new FoldedCarpet());
		BaseMod.addCard(new LightWeighted());
		BaseMod.addCard(new Meditation());
		BaseMod.addCard(new CultistForm());
		BaseMod.addCard(new OnFire());
		BaseMod.addCard(new AlchemyExpert());
		BaseMod.addCard(new VigorousBody());
		BaseMod.addCard(new Riches());
		BaseMod.addCard(new Lecture());
		BaseMod.addCard(new Prey());
		BaseMod.addCard(new Blurry());
		BaseMod.addCard(new ReadTheRiotAct());
		BaseMod.addCard(new Shatter());
		BaseMod.addCard(new HowToFeedApes());

		// Colorless
		BaseMod.addCard(new PerplexingGlare());
		BaseMod.addCard(new Refine());

		// Debug
		BaseMod.addCard(new AvailablePotionUpgrade());
		BaseMod.addCard(new ConsolePotion());
		BaseMod.addCard(new CyclePotions());
		BaseMod.addCard(new ForcePotionUpgrade());

		registerPowers();
	}

	private void registerPowers() {
		BaseMod.addPower(Accumulate.class, Accumulate.POWER_ID);
		BaseMod.addPower(Airborne.class, Airborne.POWER_ID);
		BaseMod.addPower(AlchemyExpertPower.class, AlchemyExpertPower.POWER_ID);
		BaseMod.addPower(AltitudePower.class, AltitudePower.POWER_ID);
		BaseMod.addPower(BookwormPower.class, BookwormPower.POWER_ID);
		BaseMod.addPower(Brew.class, Brew.POWER_ID);
		BaseMod.addPower(Brewing.class, Brewing.POWER_ID);
		BaseMod.addPower(DeadlyContraptionPlayerPower.class, DeadlyContraptionPlayerPower.POWER_ID);
		BaseMod.addPower(Disoriented.class, Disoriented.POWER_ID);
		BaseMod.addPower(ExtraPortionPower.class, ExtraPortionPower.POWER_ID);
		BaseMod.addPower(LoseFlightNextTurn.class, LoseFlightNextTurn.POWER_ID);
		BaseMod.addPower(NoEnergyPower.class, NoEnergyPower.POWER_ID);
		BaseMod.addPower(OnFirePower.class, OnFirePower.POWER_ID);
		BaseMod.addPower(PotionTrancePower.class, PotionTrancePower.POWER_ID);
		BaseMod.addPower(PoemPower.class, PoemPower.POWER_ID);
		BaseMod.addPower(RichesPower.class, RichesPower.POWER_ID);
		BaseMod.addPower(RicketyDefensePower.class, RicketyDefensePower.POWER_ID);
		BaseMod.addPower(Flight.class, Flight.POWER_ID);
		BaseMod.addPower(TasteThisOnePower.class, TasteThisOnePower.POWER_ID);
		BaseMod.addPower(Thirsty.class, Thirsty.POWER_ID);
		BaseMod.addPower(TrixterPower.class, TrixterPower.POWER_ID);
		BaseMod.addPower(UnderminePlayerPower.class, UnderminePlayerPower.POWER_ID);
		BaseMod.addPower(VigorousBodyPower.class, VigorousBodyPower.POWER_ID);
	}

	@Override
	public void receiveEditRelics() {
		logger.info("Adding Relics for TheSage");

		// Starter
		BaseMod.addRelicToCustomPool(new FlyingCarpet(), SageColorEnum.THE_SAGE);

		// Common
		BaseMod.addRelicToCustomPool(new CabbageHead(), SageColorEnum.THE_SAGE);

		// Uncommon
		BaseMod.addRelicToCustomPool(new Cookbook(), SageColorEnum.THE_SAGE);
		BaseMod.addRelicToCustomPool(new AncientMagnet(), SageColorEnum.THE_SAGE);
		BaseMod.addRelicToCustomPool(new BalloonAnimal(), SageColorEnum.THE_SAGE);
		BaseMod.addRelic(new RedBeastStatue(), RelicType.SHARED);
		BaseMod.addRelic(new SingingVial(), RelicType.SHARED);

		// Rare
		BaseMod.addRelic(new Blowpipe(), RelicType.SHARED);
		BaseMod.addRelic(new CreepyCarousel(), RelicType.SHARED);

		// Boss
		BaseMod.addRelicToCustomPool(new ThunderCarpet(), SageColorEnum.THE_SAGE);
		BaseMod.addRelic(new FalmelsAmulet(), RelicType.SHARED);

		// Special
		BaseMod.addRelicToCustomPool(new ByrdFeather(), SageColorEnum.THE_SAGE);

		// Shop
		BaseMod.addRelic(new TheGuidebookGuide(), RelicType.SHARED);
	}

	private void maybeLoadLanguage() {
		if (localLanguage != null) {
			return;
		}
		logger.info("Determining Language for TheSage");
		switch (Settings.language) {
			case DEU:
				localLanguage = "deu/";
				break;
			case ZHS:
				localLanguage = "zhs/";
				break;
			case EPO:
			case FRA:
			case GRE:
			case IND:
			case ITA:
			case JPN:
			case KOR:
			case NOR:
			case POL:
			case PTB:
			case RUS:
			case SPA:
			case SRB:
			case SRP:
			case THA:
			case TUR:
			case UKR:
			case WWW:
			case ZHT:
			case ENG:
				localLanguage = "eng/";
				break;
			default:
				localLanguage = "eng/";
				break;
		}
	}

	@Override
	public void receiveEditStrings() {

		maybeLoadLanguage();

		logger.info("Loading localization Strings for TheSage");
		BaseMod.loadCustomStrings(CardStrings.class,
				loadJson(LOCALIZATION_FOLDER + localLanguage + "cards.json"));
		BaseMod.loadCustomStrings(RelicStrings.class,
				loadJson(LOCALIZATION_FOLDER + localLanguage + "relics.json"));
		BaseMod.loadCustomStrings(PowerStrings.class,
				loadJson(LOCALIZATION_FOLDER + localLanguage + "powers.json"));
		BaseMod.loadCustomStrings(PotionStrings.class,
				loadJson(LOCALIZATION_FOLDER + localLanguage + "potions.json"));
		BaseMod.loadCustomStrings(CharacterStrings.class,
				loadJson(LOCALIZATION_FOLDER + localLanguage + "characters.json"));
		BaseMod.loadCustomStrings(EventStrings.class,
				loadJson(LOCALIZATION_FOLDER + localLanguage + "events.json"));
		BaseMod.loadCustomStrings(TutorialStrings.class,
				loadJson(LOCALIZATION_FOLDER + localLanguage + "tutorials.json"));
	}

	@Override
	public void receiveEditKeywords() {
		maybeLoadLanguage();

		logger.info("Adding Keywords for TheSage");
		// Note: KeywordStrings is a horrible hardcoded class, we can't use it
		// use a custom class instead
		// Copied from MadScienceMod
		Type typeToken = new TypeToken<Map<String, Keyword>>() {}.getType();
		Gson gson = new Gson();
		String strings = loadJson(LOCALIZATION_FOLDER + localLanguage + "keywords.json");
		Map<String, Keyword> keywords = gson.<Map<String, Keyword>>fromJson(strings, typeToken);
		for (Entry<String, Keyword> entry : keywords.entrySet()) {
			Keyword kw = entry.getValue();
			BaseMod.addKeyword(kw.NAMES, kw.DESCRIPTION);
			Keywords.langMap.put(entry.getKey().toLowerCase(Locale.ROOT), kw.NAMES[0]);
		}
	}

	// Copied from MadScienceMod
	private static String loadJson(String jsonPath) {
		return Gdx.files.internal(jsonPath).readString(String.valueOf(StandardCharsets.UTF_8));
	}

	public static String getExistingOrPlaceholder(String prefix, String id, String postfix) {
		String idWithoutModName = id.replaceAll(MOD_ID_PREFIX, "");
		String maybeExisting = prefix + idWithoutModName + postfix;
		if (Gdx.files.internal(maybeExisting).exists()) {
			return maybeExisting;
		} else {
			SageMod.logger.debug(
					id + " has no image configured. Defaulting to placeholder image (should be in "
							+ prefix + ")");
			return prefix + PLACEHOLDER + postfix;
		}
	}

	@Override
	public void receivePostInitialize() {
		Texture badgeTexture = new Texture(Gdx.files.internal("sage/mod-badge.png"));
		ModPanel panel = new ModPanel();
		if (Loader.DEBUG) {
			panel.addUIElement(new ModButton(400, 400, panel, b -> {
				StatAnalyzer.printStatsForAllCharacters();
			}));
		}
		BaseMod.registerModBadge(badgeTexture, TheSage.NAME, AUTHORS, TheSage.DESC, panel);

		initPotions();
		initEvents();
		initFonts();
		UpgradedPotion.initTextures();
		UpgradedPotion.initLists();
		loadConfig();
	}

	private void initPotions() {
		SageMod.logger.info("Adding Potions for TheSage");
		// character specific: The Sage
		BaseMod.addPotion(FlightPotion.class, FlightPotion.LIQUID_COLOR, FlightPotion.HYBRID_COLOR,
				FlightPotion.SPOTS_COLOR, FlightPotion.POTION_ID, SageCharEnum.THE_SAGE);
		BaseMod.addPotion(FataMorgana.class, FataMorgana.LIQUID_COLOR, FataMorgana.HYBRID_COLOR,
				FataMorgana.SPOTS_COLOR, FataMorgana.POTION_ID, SageCharEnum.THE_SAGE);
		BaseMod.addPotion(HotPepperino.class, HotPepperino.LIQUID_COLOR, HotPepperino.HYBRID_COLOR,
				HotPepperino.SPOTS_COLOR, HotPepperino.POTION_ID, SageCharEnum.THE_SAGE);
		// all characters
		BaseMod.addPotion(Wine.class, Wine.LIQUID_COLOR, Wine.HYBRID_COLOR,
				Wine.SPOTS_COLOR, Wine.POTION_ID);
	}

	private void initEvents() {
		BaseMod.addEvent(CarpetTrader.ID, CarpetTrader.class, TheCity.ID);
	}

	private void initFonts() {
		FreeTypeFontGenerator generator =
				new FreeTypeFontGenerator(Gdx.files.internal("font/04b03.ttf"));
		FreeTypeFontParameter parameter = new FreeTypeFontParameter();
		parameter.size = Math.round(16 * Settings.scale);
		brewFont = generator.generateFont(parameter);
		generator.dispose();
	}

	private void loadConfig() {
		try {
			config = new SpireConfig(MOD_NAME, MOD_CONFIG);
			config.load();
			SageTipTracker.initialize();
		} catch (Exception ex) {
			logger.catching(ex);
		}
	}

	@Override
	public void receivePostBattle(AbstractRoom battleRoom) {
		if (AbstractDungeon.player != null) {
			TheSage.setSageAnimation(TheSage.FLIGHT, TheSage.GROUND);
		}
	}

	@Override
	public void receiveOnBattleStart(AbstractRoom room) {
		if (AbstractDungeon.player instanceof TheSage) {
			((TheSage) AbstractDungeon.player).updateDialogY();
		}
	}


}
