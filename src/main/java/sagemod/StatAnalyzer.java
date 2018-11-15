package sagemod;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.cards.AbstractCard.CardColor;
import com.megacrit.cardcrawl.cards.AbstractCard.CardRarity;
import com.megacrit.cardcrawl.cards.AbstractCard.CardType;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.characters.AbstractPlayer.PlayerClass;
import com.megacrit.cardcrawl.core.CardCrawlGame;
import com.megacrit.cardcrawl.helpers.CardLibrary;
import com.megacrit.cardcrawl.helpers.RelicLibrary;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import com.megacrit.cardcrawl.relics.AbstractRelic.RelicTier;
import basemod.BaseMod;
import basemod.ReflectionHacks;

/**
 * prints many important stats of a given character
 */
public class StatAnalyzer {

	public static String REPORT_FILE;
	static {
		try {
			REPORT_FILE = new File(
					StatAnalyzer.class.getProtectionDomain().getCodeSource().getLocation().toURI())
					.getPath();
		} catch (URISyntaxException ex) {
			REPORT_FILE = System.getProperty("user.home");
			ex.printStackTrace();
		}
		REPORT_FILE += "/../StatAnalyzer.txt";
	}
	private static Logger logger = LogManager.getLogger(StatAnalyzer.class);
	private static List<String> lines;

	public static void printStatsForAllCharacters() {
		PlayerClass lastCharacter = null;
		try {
			lines = new ArrayList<>();
			Path path = Paths.get(REPORT_FILE);
			printEmptyLine();
			log("================================");
			printEmptyLine();
			for (PlayerClass character : PlayerClass.values()) {
				lastCharacter = character;
				startPrinting(character);
				printEmptyLine();
				log("================================");
				printEmptyLine();
			}
			File file = new File(REPORT_FILE);
			if (!file.exists()) {
				logger.info("creating report file: " + REPORT_FILE);
				file.createNewFile();
			}
			Files.write(path, lines, StandardCharsets.UTF_8);
		} catch (Exception ex) {
			logger.error("printingStatsForAllCharacters was aborted for " + lastCharacter, ex);
		}

	}

	public static void printStatsForCharacter(PlayerClass character) {
		try {
			lines = new ArrayList<>();
			Path path = Paths.get(REPORT_FILE);
			startPrinting(character);
			Files.write(path, lines, StandardCharsets.UTF_8);
		} catch (Exception ex) {
			logger.error("printingStatsForCharacter was aborted for " + character, ex);
		}
	}

	@SuppressWarnings("unchecked")
	private static void startPrinting(PlayerClass character) {
		AbstractPlayer player = CardCrawlGame.characterManager.getAllCharacters().stream()
				.filter(c -> c.chosenClass == character).findFirst().orElse(null);
		if (player == null) {
			logger.warn("The given playerclass was not found");
			return;
		}

		printEmptyLine();
		log("Printing stats for " + player.title);

		printEmptyLine();
		CardColor cardColor = player.getCardColor();
		log("CardColor: " + cardColor.toString());
		Set<AbstractCard> cards = CardLibrary.cards.values().stream().filter(card -> card.color == cardColor)
				.collect(Collectors.toSet());
		log("Cards: " + cards.size());

		printEmptyLine();
		log("Printing rarities");
		for (CardRarity rarity : CardRarity.values()) {
			printCountFor(cards, rarity.toString(), c -> c.rarity == rarity);
		}

		printEmptyLine();
		log("Printing types");
		for (CardType type : CardType.values()) {
			printCountFor(cards, type.toString(), c -> c.type == type);
		}
		printEmptyLine();

		log("Printing costs");
		Map<Integer, List<AbstractCard>> costMap = cards.stream().collect(Collectors.groupingBy(c -> c.cost));
		costMap.entrySet().stream().sorted((e1, e2) -> e1.getKey() - e2.getKey()).forEachOrdered(e -> {
			String name = e.getKey().equals(-1) ? "X" : e.getKey().equals(-2) ? "Unplayable" : e.getKey().toString();
			log(name + ": " + e.getValue().size());
		});
		printEmptyLine();

		Set<AbstractRelic> relics = new HashSet<>();
		switch (character) {
			case IRONCLAD:
				relics.addAll(
						((HashMap<String, AbstractRelic>) ReflectionHacks.getPrivateStatic(RelicLibrary.class, "redRelics"))
						.values());
				break;
			case THE_SILENT:
				relics.addAll(((HashMap<String, AbstractRelic>) ReflectionHacks.getPrivateStatic(RelicLibrary.class,
						"greenRelics")).values());
				break;
			case DEFECT:
				relics.addAll(((HashMap<String, AbstractRelic>) ReflectionHacks.getPrivateStatic(RelicLibrary.class,
						"blueRelics")).values());
				break;
			default:
				relics.addAll(BaseMod.getRelicsInCustomPool(cardColor).values());
				break;
		}

		log("Characterspecific Relics: " + relics.size());
		for (RelicTier tier : RelicTier.values()) {
			printCountFor(relics, tier.toString(), r -> r.tier == tier);
		}
		printEmptyLine();

		// See PotionHelper.initialize(PlayerClass);
		long potionCount = 0;
		switch (character) {
			case IRONCLAD:
			case THE_SILENT:
			case DEFECT:
				potionCount = 1;
				break;
			default:
				potionCount = ((HashMap<String, PlayerClass>) ReflectionHacks.getPrivateStatic(BaseMod.class,
						"potionPlayerClassMap")).entrySet().stream().filter(e -> e.getValue() == character).count();
				break;
		}

		log("Characterspecific Potions: " + potionCount);
	}

	private static void log(String str) {
		logger.info(str);
		lines.add(str);
	}

	private static void printEmptyLine() {
		logger.info("");
		lines.add("\n");
	}

	private static <T> void printCountFor(Collection<T> collection, String name, Predicate<T> predicate) {
		long count = collection.stream().filter(predicate).count();
		if (count > 0) {
			log(name + ": " + count);
		}
	}

}
