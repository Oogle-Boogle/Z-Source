package com.zamron.world.content.combat.strategy;

import java.util.HashMap;
import java.util.Map;

import com.zamron.world.content.combat.strategy.impl.*;
import com.zamron.world.content.combat.strategy.impl.dks.DagannothSupreme;
import com.zamron.world.content.combat.strategy.impl.godwars.Aviansie;
import com.zamron.world.content.combat.strategy.impl.godwars.Graardor;
import com.zamron.world.content.combat.strategy.impl.godwars.Grimspike;
import com.zamron.world.content.combat.strategy.impl.godwars.Gritch;
import com.zamron.world.content.combat.strategy.impl.godwars.Growler;
import com.zamron.world.content.combat.strategy.impl.godwars.KreeArra;
import com.zamron.world.content.combat.strategy.impl.godwars.Steelwill;
import com.zamron.world.content.combat.strategy.impl.godwars.Tsutsuroth;
import com.zamron.world.content.combat.strategy.impl.godwars.WingmanSkree;
import com.zamron.world.content.combat.strategy.impl.godwars.Zilyana;
import com.zamron.world.content.combat.strategy.impl.raid.BalanceElemental;
import com.zamron.world.content.combat.strategy.impl.raid.Exoden;
import com.zamron.world.content.combat.strategy.wilderness.Callisto;
import com.zamron.world.content.combat.strategy.wilderness.ChaosElemental;
import com.zamron.world.content.combat.strategy.wilderness.Venenatis;
import com.zamron.world.content.combat.strategy.wilderness.Vetion;
import com.zamron.world.content.raids.RaidNpc;
import com.zamron.world.entity.impl.npc.NPC;
import io.netty.handler.codec.stomp.StompSubframeAggregator;


public class CombatStrategies {

	private static final DefaultMeleeCombatStrategy defaultMeleeCombatStrategy = new DefaultMeleeCombatStrategy();
	private static final DefaultMagicCombatStrategy defaultMagicCombatStrategy = new DefaultMagicCombatStrategy();
	private static final DefaultRangedCombatStrategy defaultRangedCombatStrategy = new DefaultRangedCombatStrategy();
	private static final Map<Integer, CombatStrategy> STRATEGIES = new HashMap<Integer, CombatStrategy>();
	
	public static void init() {
		DefaultMagicCombatStrategy defaultMagicStrategy = new DefaultMagicCombatStrategy();
		STRATEGIES.put(13, defaultMagicStrategy);
		STRATEGIES.put(172, defaultMagicStrategy);
		STRATEGIES.put(174, defaultMagicStrategy);
		STRATEGIES.put(2025, defaultMagicStrategy);
		STRATEGIES.put(3495, defaultMagicStrategy);
		STRATEGIES.put(3496, defaultMagicStrategy);
		STRATEGIES.put(3491, defaultMagicStrategy);
		STRATEGIES.put(2882, defaultMagicStrategy);
		STRATEGIES.put(13451, defaultMagicStrategy);
		STRATEGIES.put(13452, defaultMagicStrategy);
		STRATEGIES.put(13453, defaultMagicStrategy);
		STRATEGIES.put(13454, defaultMagicStrategy);
		STRATEGIES.put(1643, defaultMagicStrategy);
		STRATEGIES.put(6254, defaultMagicStrategy);
		STRATEGIES.put(6257, defaultMagicStrategy);
		STRATEGIES.put(6278, defaultMagicStrategy);
		STRATEGIES.put(6221, defaultMagicStrategy);
		STRATEGIES.putIfAbsent(1, defaultMagicStrategy);
	;
		
		DefaultRangedCombatStrategy defaultRangedStrategy = new DefaultRangedCombatStrategy();
		STRATEGIES.put(688, defaultRangedStrategy);
		STRATEGIES.put(2028, defaultRangedStrategy);
		STRATEGIES.put(6220, defaultRangedStrategy);
		STRATEGIES.put(6256, defaultRangedStrategy);
		STRATEGIES.put(6276, defaultRangedStrategy);
		STRATEGIES.put(6252, defaultRangedStrategy);
		STRATEGIES.put(27, defaultRangedStrategy);
		STRATEGIES.put(1684, defaultRangedStrategy);
		STRATEGIES.put(1685, defaultRangedStrategy);
		
		STRATEGIES.put(2745, new Jad());
		STRATEGIES.put(8528, new Nomad());
		STRATEGIES.put(8349, new TormentedDemon());
		STRATEGIES.put(3200, new ChaosElemental());
		STRATEGIES.put(4540, new BandosAvatar());
		STRATEGIES.put(8133, new CorporealBeast());
		STRATEGIES.put(2896, new Spinolyp());
		STRATEGIES.put(2881, new DagannothSupreme());
		STRATEGIES.put(6260, new Graardor());
		STRATEGIES.put(13447, new Nex());
		STRATEGIES.put(6263, new Steelwill());
		STRATEGIES.put(6265, new Grimspike());
		STRATEGIES.put(6222, new KreeArra());
		STRATEGIES.put(6223, new WingmanSkree());
		STRATEGIES.put(6225, new Geerin());
		STRATEGIES.put(6203, new Tsutsuroth());
		STRATEGIES.put(6208, new Kreeyath());
		STRATEGIES.put(6206, new Gritch());
		STRATEGIES.put(6247, new Zilyana());
		STRATEGIES.put(6250, new Growler());
		STRATEGIES.put(1382, new Glacor());
		STRATEGIES.put(9939, new PlaneFreezer());
		 STRATEGIES.put(2042, new RemadeZulrah());
			STRATEGIES.put(2043, new RemadeZulrah());
			STRATEGIES.put(2044, new RemadeZulrah());
			STRATEGIES.put(25, new Sephstrat());
		
		Dragon dragonStrategy = new Dragon();
		STRATEGIES.put(50, dragonStrategy);
		STRATEGIES.put(941, dragonStrategy);
		STRATEGIES.put(55, dragonStrategy);
		STRATEGIES.put(53, dragonStrategy);
		STRATEGIES.put(54, dragonStrategy);
		STRATEGIES.put(51, dragonStrategy);
		STRATEGIES.put(1590, dragonStrategy);
		STRATEGIES.put(1591, dragonStrategy);
		STRATEGIES.put(1592, dragonStrategy);
		STRATEGIES.put(5362, dragonStrategy);
		STRATEGIES.put(5363, dragonStrategy);
		STRATEGIES.put(1982, dragonStrategy);
		
		Aviansie aviansieStrategy = new Aviansie();
		STRATEGIES.put(6246, aviansieStrategy);
		STRATEGIES.put(6230, aviansieStrategy);
		STRATEGIES.put(6231, aviansieStrategy);
		
		KalphiteQueen kalphiteQueenStrategy = new KalphiteQueen();
		STRATEGIES.put(1158, kalphiteQueenStrategy);
		STRATEGIES.put(1160, kalphiteQueenStrategy);
		
		Revenant revenantStrategy = new Revenant();
		STRATEGIES.put(6715, revenantStrategy);
		STRATEGIES.put(6716, revenantStrategy);
		STRATEGIES.put(6701, revenantStrategy);
		STRATEGIES.put(6725, revenantStrategy);
		STRATEGIES.put(6691, revenantStrategy);
		
		STRATEGIES.put(2000, new Venenatis());
		STRATEGIES.put(2006, new Vetion());
		STRATEGIES.put(2010, new Callisto());
		STRATEGIES.put(1999, new Cerberus());
		STRATEGIES.put(6766, new LizardMan());
		STRATEGIES.put(8281, new BalanceElemental());
		STRATEGIES.put(499, new Thermonuclear());
		STRATEGIES.put(7286, new Skotizo());
		STRATEGIES.put(175, new Sire());
		/*
		 * new npc's. added by ME
		 */
		STRATEGIES.put(8597, new AvatarOfCreation());
		STRATEGIES.put(10051, new Icedemon()); //now it does , b
		STRATEGIES.put(9176, new SkeletalHorror()); // this one nee
		STRATEGIES.put(6208, new BalfrugKreeyath());
		STRATEGIES.put(9911, new HarLakkRiftsplitter());
		STRATEGIES.put(9855, new Lexicus());
		STRATEGIES.put(169, new Harambe());

		STRATEGIES.put(6311, new Dragonix()); //Custom hellhounds

		//easy raids
		STRATEGIES.put(177, new MiniDire()); //Frost demon
		STRATEGIES.put(3224, new MiniDire()); //Yippie
		STRATEGIES.put(3226, new DefaultMagicCombatStrategy()); //Kirby
		STRATEGIES.put(3225, new DefaultRangedCombatStrategy());//Tank
		STRATEGIES.put(527, new MiniDire());//Fuzed goku

		//Medium raids
		STRATEGIES.put(10126, new UnholyCursebearer());
		STRATEGIES.put(11751, new Necrolord());
		STRATEGIES.put(9766, new Sagittare());

		//Hard raids
		STRATEGIES.put(422, new Harambe());
		STRATEGIES.put(9912, new KCMinions());
		STRATEGIES.put(2862, new Harambe());
		//111
		STRATEGIES.put(110, new WildyWyrm());
		STRATEGIES.put(171, new Harambe());
		STRATEGIES.put(12240, new Exoden());
		STRATEGIES.put(12239, new CustomVoragoCombat());
		STRATEGIES.put(5958, new Necrolord());
		STRATEGIES.put(109, new Necrolord());
		STRATEGIES.put(111, new Harambe());

		//Starwars zone
		STRATEGIES.put(1069, new Sagittare());
		//Dark Ranger
		STRATEGIES.put(299, new RangerBoss()); //Sagittare
		//Broly
		STRATEGIES.put(1059, new BrolyCombat());
		STRATEGIES.put(420, new Joker());
		STRATEGIES.put(2843, new Charizard());
		STRATEGIES.put(174, new Charizard());
		STRATEGIES.put(812, new Toad());
		STRATEGIES.put(5922, new Zorbak());
		STRATEGIES.put(6357, new Mewtwo());

		/**
		 * Converted Bosses.
		 */
		
		STRATEGIES.put(4413, new DireWolf());
        STRATEGIES.put(6305, new Dragonix());
        STRATEGIES.put(10141, new BallakPummeler());
        STRATEGIES.put(10039, new ToKashBloodchiller());

		STRATEGIES.put(10038, new ToKashBloodchiller());
		STRATEGIES.put(10140, new BallakPummeler());

        STRATEGIES.put(6307, new ZamorakIktomi());
        STRATEGIES.put(839, new MiniDire());
        STRATEGIES.put(509, new Nazastarool());
        STRATEGIES.put(433, new Cyrisus());
        
		/**
		 * End of converted bosses.
		 */
		STRATEGIES.put(7553, new TheGeneral());
		STRATEGIES.put(12841, new Warmonger());
		STRATEGIES.put(6313, new ArmadylAbyzou());
		STRATEGIES.put(6314, new ArmadylAbyzou());
		STRATEGIES.put(6309, new ZamorakLefosh());
		STRATEGIES.put(7134, new BorkStrategy());
		STRATEGIES.put(6303, new Abbadon());
		STRATEGIES.put(9903, new Harambe());
		STRATEGIES.put(9913, new DailyNpcCombat());
		STRATEGIES.put(8507, new CustomVoragoCombat());
		STRATEGIES.put(9273, new WizardOfTridentCombat());
		STRATEGIES.put(9277, new RainbowTextureNPC());
		STRATEGIES.put(421, new RainbowTextureNPC());
		STRATEGIES.put(8548, new RainbowTextureNPC());
		STRATEGIES.put(9280, new KCMinions());
		STRATEGIES.put(9647, new InfernalWizardCombat());
		STRATEGIES.put(8675, new OlmMinionsCombat());
		STRATEGIES.put(2, new SuicsBoss());
		STRATEGIES.put(9944, new AssassinStrategy());
	}
	
	public static CombatStrategy getStrategy(NPC npc) {
		if(npc instanceof RaidNpc) {
			return defaultMagicCombatStrategy;
		}
		if(STRATEGIES.get(npc.getId()) != null) {
			return STRATEGIES.get(npc.getId());
		}
		return defaultMeleeCombatStrategy;
	}
	
	public static CombatStrategy getDefaultMeleeStrategy() {
		return defaultMeleeCombatStrategy;
	}

	public static CombatStrategy getDefaultMagicStrategy() {
		return defaultMagicCombatStrategy;
	}


	public static CombatStrategy getDefaultRangedStrategy() {
		return defaultRangedCombatStrategy;
	}
}
