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
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.core.Settings;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.dungeons.TheCity;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.CharacterStrings;
import com.megacrit.cardcrawl.localization.EventStrings;
import com.megacrit.cardcrawl.localization.Keyword;
import com.megacrit.cardcrawl.localization.PotionStrings;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;
import com.megacrit.cardcrawl.relics.SneckoSkull;
import com.megacrit.cardcrawl.relics.TheSpecimen;
import com.megacrit.cardcrawl.relics.TwistedFunnel;
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
import sagemod.cards.CaneStrike;
import sagemod.cards.CatchMeIfYouCan;
import sagemod.cards.Crank;
import sagemod.cards.CultistForm;
import sagemod.cards.DeadlyContraption;
import sagemod.cards.DefendSage;
import sagemod.cards.EndlessFear;
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
import sagemod.cards.HowToTalkToSpiders;
import sagemod.cards.HowToWakeLyons;
import sagemod.cards.HowToWarmElephants;
import sagemod.cards.LightWeighted;
import sagemod.cards.Lunchtime;
import sagemod.cards.MechanicsBreak;
import sagemod.cards.Meditation;
import sagemod.cards.NoxiousWave;
import sagemod.cards.OnFire;
import sagemod.cards.OnTheHead;
import sagemod.cards.PerpetuumMobile;
import sagemod.cards.PincerAttack;
import sagemod.cards.PincerRepair;
import sagemod.cards.PotionTrance;
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
import sagemod.cards.ToxicChains;
import sagemod.cards.TurnAround;
import sagemod.cards.VigorousBody;
import sagemod.cards.ViralStrike;
import sagemod.cards.debug.AvailablePotionUpgrade;
import sagemod.cards.debug.ForcePotionUpgrade;
import sagemod.character.SageCharEnum;
import sagemod.character.SageColorEnum;
import sagemod.character.TheSage;
import sagemod.events.CarpetTrader;
import sagemod.listeners.PotionListener;
import sagemod.listeners.VirusListener;
import sagemod.potions.FataMorgana;
import sagemod.potions.FlightPotion;
import sagemod.potions.UpgradedPotion;
import sagemod.powers.Accumulate;
import sagemod.powers.Airborne;
import sagemod.powers.AlchemyExpertPower;
import sagemod.powers.AncientPoisonPower;
import sagemod.powers.BookwormPower;
import sagemod.powers.Brew;
import sagemod.powers.Brewing;
import sagemod.powers.DeadlyContraptionPower;
import sagemod.powers.Disoriented;
import sagemod.powers.EndlessFearPower;
import sagemod.powers.ExtraPortionPower;
import sagemod.powers.LoseFlightNextTurn;
import sagemod.powers.NoEnergyPower;
import sagemod.powers.OnFirePower;
import sagemod.powers.PotionTrancePower;
import sagemod.powers.SageFlight;
import sagemod.powers.TasteThisOnePower;
import sagemod.powers.Thirsty;
import sagemod.powers.VigorousBodyPower;
import sagemod.powers.Virus;
import sagemod.relics.AncientMagnet;
import sagemod.relics.BalloonAnimal;
import sagemod.relics.Blowpipe;
import sagemod.relics.ByrdFeather;
import sagemod.relics.Cookbook;
import sagemod.relics.FlyingCarpet;
import sagemod.relics.RedBeastStatue;
import sagemod.relics.ThunderCarpet;
import sagemod.relics.deprecated.ByrdCarpet;
import sagemod.variables.BrewingDynamicVariable;
import sagemod.variables.MiscDynamicVariable;

@SuppressWarnings("deprecation")
@SpireInitializer
public class SageMod implements EditCharactersSubscriber, EditCardsSubscriber, EditRelicsSubscriber,
EditStringsSubscriber, PostInitializeSubscriber, EditKeywordsSubscriber,
PostBattleSubscriber, OnStartBattleSubscriber {

	public static final Logger logger = LogManager.getLogger(SageMod.class.getName());
	public static final String AUTHORS = "jonasdasdefekte, Skrelpoid";

	public static final String PLACEHOLDER = "Placeholder";
	public static final Color COLOR = new Color(0xc65e03);
	public static String LOCALIZATION_FOLDER = "sage/local/";

	public static final String EVENT_FOLDER = "sage/events/";

	public static BitmapFont brewFont;

	private static String localLanguage;

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
		BaseMod.subscribe(new VirusListener());
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
		BaseMod.addCard(new Crank());
		BaseMod.addCard(new EnergeticBrew());
		BaseMod.addCard(new ViralStrike());
		BaseMod.addCard(new GroundedStrike());
		BaseMod.addCard(new HowToCharmASentry());
		BaseMod.addCard(new CaneStrike());

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
		BaseMod.addCard(new ToxicChains());
		BaseMod.addCard(new HowToMurderAnts());
		BaseMod.addCard(new HowToAmuseSneckos());
		BaseMod.addCard(new PotionTrance());
		BaseMod.addCard(new NoxiousWave());

		// Rare
		BaseMod.addCard(new Bookworm());
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
		BaseMod.addCard(new AncientPoison());
		BaseMod.addCard(new VigorousBody());
		BaseMod.addCard(new EndlessFear());

		// Debug
		BaseMod.addCard(new AvailablePotionUpgrade());
		BaseMod.addCard(new ForcePotionUpgrade());

		registerPowers();
	}

	private void registerPowers() {
		BaseMod.addPower(Accumulate.class, Accumulate.POWER_ID);
		BaseMod.addPower(Airborne.class, Airborne.POWER_ID);
		BaseMod.addPower(AlchemyExpertPower.class, AlchemyExpertPower.POWER_ID);
		BaseMod.addPower(AncientPoisonPower.class, AncientPoisonPower.POWER_ID);
		BaseMod.addPower(BookwormPower.class, BookwormPower.POWER_ID);
		BaseMod.addPower(Brew.class, Brew.POWER_ID);
		BaseMod.addPower(Brewing.class, Brewing.POWER_ID);
		BaseMod.addPower(DeadlyContraptionPower.class, DeadlyContraptionPower.POWER_ID);
		BaseMod.addPower(Disoriented.class, Disoriented.POWER_ID);
		BaseMod.addPower(EndlessFearPower.class, EndlessFearPower.POWER_ID);
		BaseMod.addPower(ExtraPortionPower.class, ExtraPortionPower.POWER_ID);
		BaseMod.addPower(LoseFlightNextTurn.class, LoseFlightNextTurn.POWER_ID);
		BaseMod.addPower(NoEnergyPower.class, NoEnergyPower.POWER_ID);
		BaseMod.addPower(OnFirePower.class, OnFirePower.POWER_ID);
		BaseMod.addPower(PotionTrancePower.class, PotionTrancePower.POWER_ID);
		BaseMod.addPower(SageFlight.class, SageFlight.POWER_ID);
		BaseMod.addPower(TasteThisOnePower.class, TasteThisOnePower.POWER_ID);
		BaseMod.addPower(Thirsty.class, Thirsty.POWER_ID);
		BaseMod.addPower(VigorousBodyPower.class, VigorousBodyPower.POWER_ID);
		BaseMod.addPower(Virus.class, Virus.POWER_ID);

	}

	@Override
	public void receiveEditRelics() {
		logger.info("Adding Relics for TheSage");

		// Starter
		BaseMod.addRelicToCustomPool(new FlyingCarpet(), SageColorEnum.THE_SAGE);

		// Common

		// Uncommon
		BaseMod.addRelicToCustomPool(new Cookbook(), SageColorEnum.THE_SAGE);
		BaseMod.addRelicToCustomPool(new AncientMagnet(), SageColorEnum.THE_SAGE);
		BaseMod.addRelicToCustomPool(new BalloonAnimal(), SageColorEnum.THE_SAGE);
		BaseMod.addRelic(new RedBeastStatue(), RelicType.SHARED);

		// Rare
		BaseMod.addRelic(new Blowpipe(), RelicType.SHARED);

		// Boss
		BaseMod.addRelicToCustomPool(new ThunderCarpet(), SageColorEnum.THE_SAGE);

		// Special
		BaseMod.addRelicToCustomPool(new ByrdFeather(), SageColorEnum.THE_SAGE);

		// Deprecated
		BaseMod.addRelicToCustomPool(new ByrdCarpet(), SageColorEnum.THE_SAGE);

		// Add Silent's poison relics
		SneckoSkull sneckoSkull = new SneckoSkull();
		BaseMod.addRelicToCustomPool(sneckoSkull, SageColorEnum.THE_SAGE);
		RelicLibrary.commonList.remove(sneckoSkull);
		TheSpecimen theSpecimen = new TheSpecimen();
		BaseMod.addRelicToCustomPool(theSpecimen, SageColorEnum.THE_SAGE);
		RelicLibrary.rareList.remove(theSpecimen);
		TwistedFunnel twistedFunnel = new TwistedFunnel();
		BaseMod.addRelicToCustomPool(twistedFunnel, SageColorEnum.THE_SAGE);
		RelicLibrary.shopList.remove(twistedFunnel);
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
			case ZHS:
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
		String maybeExisting = prefix + id + postfix;
		if (Gdx.files.internal(maybeExisting).exists()) {
			return maybeExisting;
		} else {
			SageMod.logger.debug(
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
				StatAnalyzer.printStatsForAllCharacters();
			}));
		}
		BaseMod.registerModBadge(badgeTexture, TheSage.NAME, AUTHORS, TheSage.DESC, panel);

		initPotions();
		initEvents();
		initFonts();
		UpgradedPotion.initTextures();
		UpgradedPotion.initLists();
	}



	private void initPotions() {
		SageMod.logger.info("Adding Potions for TheSage");
		// character specific: The Sage
		BaseMod.addPotion(FlightPotion.class, FlightPotion.LIQUID_COLOR, FlightPotion.HYBRID_COLOR,
				FlightPotion.SPOTS_COLOR, FlightPotion.POTION_ID, SageCharEnum.THE_SAGE);
		// all characters
		BaseMod.addPotion(FataMorgana.class, FataMorgana.LIQUID_COLOR, FataMorgana.HYBRID_COLOR,
				FataMorgana.SPOTS_COLOR, FataMorgana.POTION_ID);
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
