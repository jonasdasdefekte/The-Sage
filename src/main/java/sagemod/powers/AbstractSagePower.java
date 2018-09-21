package sagemod.powers;

import com.badlogic.gdx.graphics.Texture;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.helpers.ImageMaster;
import com.megacrit.cardcrawl.powers.AbstractPower;

public abstract class AbstractSagePower extends AbstractPower {

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
		return ImageMaster.loadImage("sage/powers/" + id + ".png");
	}

}
