package com.github.elrol.cpg.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.serializer.TextSerializers;

import com.github.elrol.cpg.libs.PluginInfo;
import com.google.common.base.Function;
import com.pixelmonmod.pixelmon.battles.rules.BattleRules;
import com.pixelmonmod.pixelmon.battles.rules.clauses.AbilityClause;
import com.pixelmonmod.pixelmon.battles.rules.clauses.BattleClause;
import com.pixelmonmod.pixelmon.battles.rules.clauses.BattleClauseRegistry;
import com.pixelmonmod.pixelmon.battles.rules.clauses.PokemonClause;
import com.pixelmonmod.pixelmon.config.PixelmonItemsBadges;
import com.pixelmonmod.pixelmon.entities.pixelmon.abilities.AbilityBase;
import com.pixelmonmod.pixelmon.enums.EnumPokemon;

import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

public class DefaultConfiguration {

	private static DefaultConfiguration instance = new DefaultConfiguration();
	
	private ConfigurationLoader<CommentedConfigurationNode> loader;
	private CommentedConfigurationNode config;
	
	Function<Object, String> stringTransformer = new Function<Object,String>() {
        public String apply(Object input) {
            if (input instanceof String) {
                return (String) input;
            } else {
                return null;
            }
        }
    };
	
	private List<String> badgeList = new ArrayList<String>();
	
	public Text getHeader() {
		return TextSerializers.FORMATTING_CODE.deserialize(config.getNode("General", "Header").getString());
	}
	
	public static DefaultConfiguration getInstance() {
		return instance;
	}
	
	public void setup(File configFile, ConfigurationLoader<CommentedConfigurationNode> loader) {
		this.loader = loader;
		if(!configFile.exists()) {
			try {
				configFile.createNewFile();
				loadConfig();
				//General
				config.getNode("General", "Header").setValue("&3[&aCPG&3]&r");
				config.getNode("General", "Custom Badge", "Item").setValue(((ItemType)PixelmonItemsBadges.boulderBadge).getId());
				
				config.getNode("General", "Custom Badge", "Name").setComment("Sets the name of the badges, use {leader} to specify the leaders username");
				config.getNode("General", "Custom Badge", "Name").setValue("&a{leader}'s Badge");
				
				config.getNode("General", "Custom Badge", "Lore").setComment("Sets the lore of the badge, use {player} to specify the challengers username");
				config.getNode("General", "Custom Badge", "Lore").setValue("&3Earned by {player}");
				
				config.getNode("General", "Battle Rules", "Pokemon").setComment("Sets how many pokemon are allowed for the Custom Gym Battle");
				config.getNode("General", "Battle Rules", "Pokemon").setValue(6);
				
				config.getNode("General", "Battle Rules", "Level Cap").setComment("Sets the max level for Custom Gym Battles");
				config.getNode("General", "Battle Rules", "Level Cap").setValue(100);
				
				config.getNode("General", "Battle Rules", "Raise to Level").setComment("Brings all pokemon in the Custom Gym Battle to the level cap");
				config.getNode("General", "Battle Rules", "Raise to Level").setValue(false);
				
				config.getNode("General", "Battle Rules", "Full Heal").setComment("Automatically heals all pokemon at the start of the Custom Gym Battle");
				config.getNode("General", "Battle Rules", "Full Heal").setValue(true);
				
				config.getNode("General", "Battle Rules", "Clauses").setComment("If true, adds the clause to the rules");
				config.getNode("General", "Battle Rules", "Clauses", "Ability Clause").setValue(false);
				config.getNode("General", "Battle Rules", "Clauses", "Ability Clause", "Disallowed Abilities").setValue(new ArrayList<String>().add("Adaptability"));
				config.getNode("General", "Battle Rules", "Clauses", "Bag Clause").setValue(false);
				
				//Badges
				config.getNode("Badges").setComment("All values must be the resource location of the item (String)");
				config.getNode("Badges").setValue(PluginInfo.getDefaultBadges());
				
				saveConfig();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}else {
			loadConfig();
		}
	}
	
	public CommentedConfigurationNode getConfig() {
		return config;
	}
	

	public void saveConfig() {
		try {
			loader.save(config);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loadConfig() {
		try {
			config = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public List<String> getBadges(){
		badgeList = config.getNode("Badges").getList(stringTransformer);
		if(badgeList == null)
			return new ArrayList<String>();
		return badgeList;
	}
	
	public String getCustomBadgeItem() {
		String badge = config.getNode("General", "Custom Badge", "Item").getString();
		if(badge == null)
			return "";
		return badge;
	}
	
	public Text getBadgeName(Player leader) {
		String unformatedString = config.getNode("General", "Custom Badge", "Name").getString();
		return TextSerializers.FORMATTING_CODE.deserialize(unformatedString.replaceAll("\\{leader\\}", leader.getName()));
	}
	
	public List<Text> getBadgeLore(Player player) {
		String unformatedString = config.getNode("General", "Custom Badge", "Lore").getString();
		List<Text> lore = new ArrayList<Text>();
		lore.add(TextSerializers.FORMATTING_CODE.deserialize(unformatedString.replaceAll("\\{player\\}", player.getName())));
		return lore;
	}
	
	public BattleRules getRules() {
		BattleRules rules = new BattleRules();
		
		rules.numPokemon = config.getNode("General", "Battle Rules", "Pokemon").getInt();
		rules.levelCap = config.getNode("General", "Battle Rules", "Level Cap").getInt();
		rules.raiseToCap = config.getNode("General", "Battle Rules", "Raise to Level").getBoolean();
		rules.fullHeal = config.getNode("General", "Battle Rules", "Full Heal").getBoolean();
		
		rules.setNewClauses(getClauses());
		
		return rules;
	}
	
	public List<BattleClause> getClauses() {
		List<BattleClause> clauses = new ArrayList<BattleClause>();
		if(config.getNode("General", "Battle Rules", "Clauses", "Bag Clause").getBoolean())
			clauses.add(BattleClauseRegistry.getClauseRegistry().getClause(BattleClauseRegistry.BAG_CLAUSE));
		if(config.getNode("General", "Battle Rules", "Clauses", "Legendary Clause").getBoolean())
			clauses.add(new PokemonClause("", EnumPokemon.LEGENDARY_ENUMS));
		if(config.getNode("General", "Battle Rules", "Clauses", "Ability Clause").getBoolean())
			clauses.add(new AbilityClause("", getDisallowedAbilities()));
		return clauses;
	}
	
	@SuppressWarnings("unchecked")
	public Class<? extends AbilityBase>[] getDisallowedAbilities(){
		List<Class<? extends AbilityBase>> abilities = new ArrayList<Class<? extends AbilityBase>>();
		List<String> abilityNames = config.getNode("General", "Battle Rules", "Clauses", "Ability Clause", "Disallowed Abilities").getList(stringTransformer);
		for(String name : abilityNames) {
			try {
				Class<?> ability = Class.forName(name);
				if(ability != AbilityBase.class && ability.isAssignableFrom(AbilityBase.class)) {
					abilities.add((Class<? extends AbilityBase>) ability);
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			}
			
		}
		return (Class<? extends AbilityBase>[]) abilities.toArray();
	}
	
	
}
