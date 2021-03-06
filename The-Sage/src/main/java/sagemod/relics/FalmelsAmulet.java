package sagemod.relics;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import com.megacrit.cardcrawl.actions.AbstractGameAction.AttackEffect;
import com.megacrit.cardcrawl.actions.common.DamageAction;
import com.megacrit.cardcrawl.actions.common.GainEnergyAction;
import com.megacrit.cardcrawl.cards.DamageInfo;
import com.megacrit.cardcrawl.cards.DamageInfo.DamageType;
import com.megacrit.cardcrawl.dungeons.AbstractDungeon;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.monsters.AbstractMonster.Intent;
import com.megacrit.cardcrawl.monsters.EnemyMoveInfo;
import com.megacrit.cardcrawl.relics.AbstractRelic;
import basemod.ReflectionHacks;
import sagemod.powers.Disoriented;

public class FalmelsAmulet extends AbstractSageRelic {

	public static final String ID = "sagemod:Falmels_Amulet";
	public static final RelicTier TIER = RelicTier.BOSS;
	public static final LandingSound SOUND = LandingSound.CLINK;
	private static final int ENERGY_GAIN = 1;

	private int turn;
	private AbstractMonster target;

	public FalmelsAmulet() {
		super(ID, TIER, SOUND);
	}

	@Override
	public void atBattleStartPreDraw() {
		turn = 0;
		target = null;
		List<AbstractMonster> monsters = new ArrayList<>(AbstractDungeon.getCurrRoom().monsters.monsters);
		Collections.shuffle(monsters);
		for (AbstractMonster mo : monsters) {
			EnemyMoveInfo move =
					(EnemyMoveInfo) ReflectionHacks.getPrivate(mo, AbstractMonster.class, "move");
			if (isAttack(move.intent)) {
				target = mo;
				addAttack(mo, move);
				break;
			}
		}
	}

	private void addAttack(AbstractMonster mo, EnemyMoveInfo move) {
			if (move.isMultiDamage) {// add 1 multiplier
				move.multiplier++;
			} else {
				move.multiplier = 2;
				move.isMultiDamage = true;
			}
			// update display
			mo.createIntent();
			flash();
			appearAbove(mo);
	}

	@Override
	public void atTurnStart() {
		turn++;
		AbstractDungeon.actionManager.addToBottom(new GainEnergyAction(ENERGY_GAIN));
	}

	@Override
	public int onAttacked(DamageInfo info, int damageAmount) {
		if (turn == 1) {
			if (info.owner == target && info.type == DamageType.NORMAL) {
				target = null;
				if (!info.owner.hasPower(Disoriented.POWER_ID)) {
					AbstractDungeon.actionManager.addToTop(new DamageAction(AbstractDungeon.player,
							new DamageInfo(info.owner, info.base, info.type),
							AttackEffect.NONE));
				}
			}
		}
		return damageAmount;
	}

	private boolean isAttack(Intent intent) {
		return intent == Intent.ATTACK || intent == Intent.ATTACK_BUFF
				|| intent == Intent.ATTACK_DEBUFF
				|| intent == Intent.ATTACK_DEFEND;
	}

	@Override
	public String getUpdatedDescription() {
		return DESCRIPTIONS[0];
	}

	@Override
	public AbstractRelic makeCopy() {
		return new FalmelsAmulet();
	}

}
