package com.github.elrol.cpg.libs;

import java.util.ArrayList;
import java.util.List;

import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.text.Text;

import com.github.elrol.cpg.Main;
import com.pixelmonmod.pixelmon.config.PixelmonItemsBadges;

public class PluginInfo {

	public static final String ID = "cpg";
	
	public static final String NAME = "Custom Player Gym";
	
	public static final String VERSION = "Alpha 0.0.5";
	
	public static final String DESC = "A simple plugin to have players set warps to thier custom gym";
	
	public static class Descriptions{
		
		public static final Text registerGym = Text.of("Allows a player to set a warp to thier player gym, only if the player has all the badges");
		public static final Text playerGym = Text.of("Lists available player ran gyms");
		public static final Text defeat = Text.of("Annouces defeat and awards the player a badge");
	}
	
	public static class Permissions{
		
		public static final String registerGym = "cpg.command.registergym";
		public static final String playerGym = "cpg.command.playergym";
		public static final String defeat = "cpg.command.defeat";
		
	}
	
	public static List<String> getDefaultBadges(){
		//Beacon, Earth, volcano, Rainbow, Marsh, Icicle, Toxic, Fog
		List<String> badges = new ArrayList<String>();
		badges.add(((ItemType)PixelmonItemsBadges.beaconBadge).getId());
		Main.getInstance().getLogger().debug(((ItemType)PixelmonItemsBadges.beaconBadge).getId());
		badges.add(((ItemType)PixelmonItemsBadges.earthBadge).getId());
		Main.getInstance().getLogger().debug(((ItemType)PixelmonItemsBadges.earthBadge).getId());
		badges.add(((ItemType)PixelmonItemsBadges.volcanoBadge).getId());
		Main.getInstance().getLogger().debug(((ItemType)PixelmonItemsBadges.volcanoBadge).getId());
		badges.add(((ItemType)PixelmonItemsBadges.rainbowBadge).getId());
		Main.getInstance().getLogger().debug(((ItemType)PixelmonItemsBadges.rainbowBadge).getId());
		badges.add(((ItemType)PixelmonItemsBadges.marshBadge).getId());
		Main.getInstance().getLogger().debug(((ItemType)PixelmonItemsBadges.marshBadge).getId());
		badges.add(((ItemType)PixelmonItemsBadges.icicleBadge).getId());
		Main.getInstance().getLogger().debug(((ItemType)PixelmonItemsBadges.icicleBadge).getId());
		badges.add(((ItemType)PixelmonItemsBadges.toxicBadge).getId());
		Main.getInstance().getLogger().debug(((ItemType)PixelmonItemsBadges.toxicBadge).getId());
		badges.add(((ItemType)PixelmonItemsBadges.fogBadge).getId());
		Main.getInstance().getLogger().debug(((ItemType)PixelmonItemsBadges.fogBadge).getId());
		return badges;
	}
	
}
