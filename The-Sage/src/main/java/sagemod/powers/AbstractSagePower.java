package sagemod.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.AbstractPower;

import sagemod.SageMod;

public abstract class AbstractSagePower extends AbstractPower {

	private static final String PREFIX = "sage/powers/";
	private static final String POSTFIX = ".png";

	public AbstractSagePower(String id, String name, AbstractCreature owner) {
		ID = id;
		this.name = name;
		this.owner = owner;
		img = getImg(id);
	}

	public AbstractSagePower(String id, String name, AbstractCreature owner, int amount) {
		this(id, name, owner);
		this.amount = amount;
	}

	private static Texture getImg(String id) {
		return ImageMaster.loadImage(SageMod.getExistingOrPlaceholder(PREFIX, id, POSTFIX));
	}

}
