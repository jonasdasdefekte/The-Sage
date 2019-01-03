package sagemod;

import java.util.HashMap;
import java.util.Map;
import com.megacrit.cardcrawl.helpers.GameDictionary;
import com.megacrit.cardcrawl.helpers.PowerTip;
import com.megacrit.cardcrawl.helpers.TipHelper;

/**
 * helper class for keywords. contains constants and a method to get a power tip
 * (for potions)
 */
public class Keywords {
	public static final String AIR = "air";
	public static final String ARTIFACT = "artifact";
	public static final String BREW = "brew";
	public static final String BREWING = "brewing";
	public static final String DISORIENTED = "disoriented";
	public static final String FLIGHT = "flight";
	public static final String TAXING = "taxing";
	public static final String THIRSTY = "thirsty";
	public static final String VIRUS = "virus";

	public static final Map<String, String> langMap = new HashMap<>();

	public static PowerTip makePowerTip(String keyword) {
		String translated = langMap.get(keyword);
		return new PowerTip(TipHelper.capitalize(translated), GameDictionary.keywords.get(translated));
	}
}
