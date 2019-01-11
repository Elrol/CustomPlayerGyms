package com.github.elrol.cpg.config;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.text.Text;

import com.github.elrol.cpg.libs.Methods;

import net.minecraft.util.math.BlockPos;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.loader.ConfigurationLoader;

public class GymListConfiguration {

	private static GymListConfiguration instance = new GymListConfiguration();
	
	private ConfigurationLoader<CommentedConfigurationNode> loader;
	private CommentedConfigurationNode config;
	
	public static GymListConfiguration getInstance() {
		return instance;
	}
	
	public void setup(File configFile, ConfigurationLoader<CommentedConfigurationNode> loader) {
		this.loader = loader;
		if(!configFile.exists()) {
			try {
				configFile.createNewFile();
				loadConfig();
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
	
	public void setGym(Player player) {
		String uuid = player.getUniqueId().toString();
		config.getNode(uuid, "Name").setValue(player.getName());
		config.getNode(uuid, "X").setValue(player.getLocation().getBlockX());
		config.getNode(uuid, "Y").setValue(player.getLocation().getBlockY());
		config.getNode(uuid, "Z").setValue(player.getLocation().getBlockZ());
		saveConfig();
	}
	
	public BlockPos getGym(Player player) {
		loadConfig();
		String uuid = player.getUniqueId().toString();
		if(config.getNode(uuid, "Name").getString() != null) {
			int x = config.getNode(uuid, "X").getInt();
			int y = config.getNode(uuid, "Y").getInt();
			int z = config.getNode(uuid, "Z").getInt();
			return new BlockPos(x, y, z);
		}
		Methods.sendError(player, Text.of("Gym Location is null"));
		return null;
	}
	
	public boolean hasGym(Player player) {
		return getGym(player) != null;
	}
	
	public List<UUID> getPlayers(Player player){
		loadConfig();
		List<UUID> uuids = new ArrayList<UUID>();
		List<String> nodes = config.getChildrenMap().keySet().stream().map(Object::toString).collect(Collectors.toList());
		if(nodes.isEmpty())
			System.out.println("2174304908:" + "Children List is empty");
		for(String node : nodes) {
			System.out.println("Found node for: " + node);
			try {
				uuids.add(UUID.fromString(node));
				System.out.println("2174304908:" + "added UUID " + node);
			} catch(Exception e) {
				Methods.sendError(player, Text.of("UUID Invalid: " + node));
				e.printStackTrace();
			}
		}
		return uuids;
	}
}
