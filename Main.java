package com.github.elrol.cpg;
import java.io.File;

import org.slf4j.Logger;
import org.spongepowered.api.config.ConfigDir;
import org.spongepowered.api.config.DefaultConfig;
import org.spongepowered.api.event.Listener;
import org.spongepowered.api.event.game.state.GameInitializationEvent;
import org.spongepowered.api.event.game.state.GamePostInitializationEvent;
import org.spongepowered.api.event.game.state.GamePreInitializationEvent;
import org.spongepowered.api.event.game.state.GameStartedServerEvent;
import org.spongepowered.api.event.game.state.GameStoppedServerEvent;
import org.spongepowered.api.plugin.Plugin;

import com.github.elrol.cpg.commands.CommandRegistry;
import com.github.elrol.cpg.config.DefaultConfiguration;
import com.github.elrol.cpg.config.GymListConfiguration;
import com.github.elrol.cpg.libs.PluginInfo;
import com.google.inject.Inject;

import net.minecraftforge.fml.common.eventhandler.EventBus;
import ninja.leaping.configurate.commented.CommentedConfigurationNode;
import ninja.leaping.configurate.hocon.HoconConfigurationLoader;
import ninja.leaping.configurate.loader.ConfigurationLoader;

@Plugin(id = PluginInfo.ID, name = PluginInfo.NAME, version = PluginInfo.VERSION, description = PluginInfo.DESC)
public class Main {

	private static Main instance;
	public EventBus EVENT_BUS = new EventBus();
	
	private Logger logger;
	
	private File defaultConfig;
	private ConfigurationLoader<CommentedConfigurationNode> configManager;
	
	private File gymList;
	private ConfigurationLoader<CommentedConfigurationNode> gymListManager;
	
	@Inject
	public Main(Logger logger, @DefaultConfig(sharedRoot = false) ConfigurationLoader<CommentedConfigurationNode> loader, @ConfigDir(sharedRoot = false) File configDir){
		this.logger = logger;
		
		this.defaultConfig = new File(configDir + "/general.conf");
		this.configManager = HoconConfigurationLoader.builder().setFile(defaultConfig).build();

		this.gymList = new File(configDir + "/gymlist.conf");
		this.gymListManager = HoconConfigurationLoader.builder().setFile(gymList).build();
	}

	@Listener
	public void onServerStart(GameStartedServerEvent event) {
		logger.info("Started Custom Player Gyms");
	}
	
	@Listener
	public void onServerStop(GameStoppedServerEvent event) {
		logger.info("Stopping Custom Player Gyms");
	}
	
	@Listener
	public void preInit(GamePreInitializationEvent event){
		
	}
	
	@Listener
	public void init(GameInitializationEvent event){
		logger.info("Registering Configs");
		
		 
		CommandRegistry.setup(this);
	}
	
	@Listener
	public void postInit(GamePostInitializationEvent event){
		instance = this;
		DefaultConfiguration.getInstance().setup(defaultConfig, configManager);
		GymListConfiguration.getInstance().setup(gymList, gymListManager);
	}
	
	public static Main getInstance() {
		return instance;
	}
	
	public Logger getLogger() {
		return logger;
	}
}
