package sagemod;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.megacrit.cardcrawl.localization.CardStrings;
import com.megacrit.cardcrawl.localization.Keyword;
import com.megacrit.cardcrawl.localization.PowerStrings;
import com.megacrit.cardcrawl.localization.RelicStrings;

import basemod.BaseMod;
import basemod.interfaces.EditCardsSubscriber;
import basemod.interfaces.EditCharactersSubscriber;
import basemod.interfaces.EditKeywordsSubscriber;
import basemod.interfaces.EditStringsSubscriber;
import sagemod.character.SageCharEnum;
import sagemod.character.SageColorEnum;
import sagemod.character.TheSage;
import sagemode.cards.DefendSage;
import sagemode.cards.StrikeSage;

@SpireInitializer
public class SageMod
		implements EditCharactersSubscriber, EditCardsSubscriber, EditStringsSubscriber, EditKeywordsSubscriber {

	public static final Logger logger = LogManager.getLogger(SageMod.class.getName());

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
	}

	// BlueDeep #380474
	private void addColor() {
		BaseMod.addColor(SageColorEnum.THE_SAGE, new Color(0x380474ff), "sage/cards/512/bg_attack.png",
				"sage/cards/512/bg_skill.png", "sage/cards/512/bg_power.png", "sage/cards/512/orb.png",
				"sage/cards/1024/bg_attack.png", "sage/cards/1024/bg_skill.png", "sage/cards/1024/bg_power.png",
				"sage/cards/1024/orb.png", "sage/cards/desc_orb.png");
	}

	@Override
	public void receiveEditCharacters() {
		BaseMod.addCharacter(TheSage.class, TheSage.NAME, TheSage.NAME, SageColorEnum.THE_SAGE, TheSage.NAME,
				TheSage.BUTTON, TheSage.PORTRAIT, SageCharEnum.THE_SAGE);
	}

	@Override
	public void receiveEditCards() {
		logger.info("Adding Cards for TheSage");
		// Basic
		BaseMod.addCard(new StrikeSage());
		BaseMod.addCard(new DefendSage());
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
}
