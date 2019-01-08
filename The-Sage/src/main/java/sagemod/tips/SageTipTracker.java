package sagemod.tips;

import java.io.IOException;
import java.util.HashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import sagemod.SageMod;

public class SageTipTracker {

	private static final Logger logger = LogManager.getLogger(SageTipTracker.class.getName());

	public static final String OVER_BREW = "sagemod:Over_Brew";
	public static final String PROMO = "sagemod:Promo";
	public static final String FLIGHT_REDUCTION = "sagemod:Flight_Reduction";

	public static HashMap<String, Boolean> tips = new HashMap<>();

	public static void initialize() {
		tips.put(OVER_BREW, SageMod.config.getBool(OVER_BREW));
		tips.put(PROMO, SageMod.config.getBool(PROMO));
		tips.put(FLIGHT_REDUCTION, SageMod.config.getBool(FLIGHT_REDUCTION));
	}

	public static boolean hasShown(String tip) {
		return tips.getOrDefault(tip, false);
	}

	public static void neverShowAgain(String tip) {
		logger.info("Never showing again: " + tip);
		SageMod.config.setBool(tip, true);
		tips.put(tip, Boolean.valueOf(true));
		try {
			SageMod.config.save();
		} catch (IOException ex) {
			logger.catching(ex);
		}
	}

}
