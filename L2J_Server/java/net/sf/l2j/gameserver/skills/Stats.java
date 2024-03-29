/*
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2, or (at your option)
 * any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA
 * 02111-1307, USA.
 *
 * http://www.gnu.org/copyleft/gpl.html
 */
package net.sf.l2j.gameserver.skills;

import java.util.NoSuchElementException;

/**
 * Enum of basic stats.
 *
 * @author mkizub
 */
public enum Stats
{
	//
	// Base stats, for each in Calculator a slot is allocated
	//

	// HP & MP
	MAX_HP("maxHp"), MAX_MP("maxMp"), MAX_CP("maxCp"), REGENERATE_HP_RATE(
			"regHp"), REGENERATE_CP_RATE("regCp"), REGENERATE_MP_RATE(
					"regMp"), RECHARGE_MP_RATE(
							"gainMp"), HEAL_EFFECTIVNESS("gainHp"),

	// Atk & Def
	POWER_DEFENCE("pDef"), MAGIC_DEFENCE("mDef"), POWER_ATTACK(
			"pAtk"), MAGIC_ATTACK("mAtk"), POWER_ATTACK_SPEED(
					"pAtkSpd"), MAGIC_ATTACK_SPEED("mAtkSpd"), // how fast a
																// spell is
																// casted
																// (including
																// animation)
	MAGIC_REUSE_RATE("mReuse"), // how fast spells becomes ready to reuse
	SHIELD_DEFENCE("sDef"), CRITICAL_DAMAGE("cAtk"), PVP_PHYSICAL_DMG(
			"pvpPhysDmg"), PVP_MAGICAL_DMG(
					"pvpMagicalDmg"), PVP_PHYS_SKILL_DMG("pvpPhysSkillsDmg"),

	// Atk & Def rates
	EVASION_RATE("rEvas"), SHIELD_RATE("rShld"), CRITICAL_RATE(
			"rCrit"), MCRITICAL_RATE(
					"mCritRate"), EXPSP_RATE("rExp"), ATTACK_CANCEL("cancel"),

	// Accuracy and range
	ACCURACY_COMBAT("accCombat"), POWER_ATTACK_RANGE(
			"pAtkRange"), MAGIC_ATTACK_RANGE("mAtkRange"), POWER_ATTACK_ANGLE(
					"pAtkAngle"), ATTACK_COUNT_MAX("atkCountMax"),
	// Run speed,
	// walk & escape speed are calculated proportionally,
	// magic speed is a buff
	RUN_SPEED("runSpd"),

	//
	// Player-only stats
	//
	STAT_STR("STR"), STAT_CON("CON"), STAT_DEX("DEX"), STAT_INT(
			"INT"), STAT_WIT("WIT"), STAT_MEN("MEN"),

	//
	// Special stats, share one slot in Calculator
	//

	// stats of various abilities
	BREATH("breath"),
	//
	AGGRESSION("aggression"), // locks a mob on tank caster
	BLEED("bleed"), // by daggers, like poison
	POISON("poison"), // by magic, hp dmg over time
	STUN("stun"), // disable move/ATTACK for a period of time
	ROOT("root"), // disable movement, but not ATTACK
	MOVEMENT("movement"), // slowdown movement, debuff
	CONFUSION("confusion"), // mob changes target, opposite to aggression/hate
	SLEEP("sleep"), // sleep (don't move/ATTACK) until attacked
	FIRE("fire"), WIND("wind"), WATER("water"), EARTH("earth"), HOLY(
			"holy"), DARK("dark"),
	//
	AGGRESSION_RES("aggressionRes"), BLEED_RES("bleedRes"), POISON_RES(
			"poisonRes"), STUN_RES("stunRes"), PARALYZE_RES(
					"paralyzeRes"), ROOT_RES("rootRes"), SLEEP_RES(
							"sleepRes"), CONFUSION_RES(
									"confusionRes"), MUTE_RES(
											"muteRes"), MOVEMENT_RES(
													"movementRes"), FIRE_RES(
															"fireRes"), WIND_RES(
																	"windRes"), WATER_RES(
																			"waterRes"), EARTH_RES(
																					"earthRes"), HOLY_RES(
																							"holyRes"), DARK_RES(
																									"darkRes"), CANCEL_RES(
																											"cancelRes"), // Resistance
																															// for
																															// cancel
																															// type
																															// skills

	NONE_WPN_RES("noneWpnRes"), // Shields!!!
	SWORD_WPN_RES("swordWpnRes"), BLUNT_WPN_RES("bluntWpnRes"), DAGGER_WPN_RES(
			"daggerWpnRes"), BOW_WPN_RES("bowWpnRes"), POLE_WPN_RES(
					"poleWpnRes"), ETC_WPN_RES("etcWpnRes"), FIST_WPN_RES(
							"fistWpnRes"), DUAL_WPN_RES(
									"dualWpnRes"), DUALFIST_WPN_RES(
											"dualFistWpnRes"),

	REFLECT_DAMAGE_PERCENT("reflectDam"), REFLECT_SKILL_MAGIC(
			"reflectSkillMagic"), REFLECT_SKILL_PHYSIC(
					"reflectSkillPhysic"), ABSORB_DAMAGE_PERCENT(
							"absorbDam"), TRANSFER_DAMAGE_PERCENT("transDam"),

	MAX_LOAD("maxLoad"),

	PATK_PLANTS("pAtk-plants"), PATK_INSECTS("pAtk-insects"), PATK_ANIMALS(
			"pAtk-animals"), PATK_MONSTERS("pAtk-monsters"), PATK_DRAGONS(
					"pAtk-dragons"), PATK_UNDEAD("pAtk-undead"),

	PDEF_UNDEAD("pDef-undead"),

	ATK_REUSE("atkReuse"),

	// ExSkill :)
	INV_LIM("inventoryLimit"), WH_LIM("whLimit"), FREIGHT_LIM(
			"FreightLimit"), P_SELL_LIM("PrivateSellLimit"), P_BUY_LIM(
					"PrivateBuyLimit"), REC_D_LIM(
							"DwarfRecipeLimit"), REC_C_LIM("CommonRecipeLimit"),

	// C4 Stats
	MP_CONSUME_RATE("MpConsumeRate"), HP_CONSUME_RATE(
			"HpConsumeRate"), MP_CONSUME(
					"MpConsume"), SOULSHOT_COUNT("soulShotCount");

	public static final int NUM_STATS = values().length;

	private String _value;

	public String getValue()
	{
		return _value;
	}

	private Stats(String s)
	{
		_value = s;
	}

	public static Stats valueOfXml(String name)
	{
		name = name.intern();
		for (Stats s : values())
		{
			if (s.getValue().equals(name))
				return s;
		}

		throw new NoSuchElementException(
				"Unknown name '" + name + "' for enum BaseStats");
	}
}
