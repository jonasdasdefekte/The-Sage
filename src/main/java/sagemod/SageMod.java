package sagemod;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.badlogic.gdx.graphics.Color;
import com.evacipated.cardcrawl.modthespire.lib.SpireInitializer;

import basemod.BaseMod;
import basemod.interfaces.EditCharactersSubscriber;
import sagemod.character.SageCharEnum;
import sagemod.character.SageColorEnum;
import sagemod.character.TheSage;

@SpireInitializer
public class SageMod implements EditCharactersSubscriber {

	public static final Logger logger = LogManager.getLogger(SageMod.class.getName());

	/**
	 * The initializing method for ModTheSpire. This gets called before the game is
	 * loaded.
	 */
	public static void initialize() {
		logger.info("Initializing SageMod");
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
}
