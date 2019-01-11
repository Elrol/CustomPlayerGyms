package com.github.elrol.cpg.commands;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.args.GenericArguments;
import org.spongepowered.api.command.spec.CommandSpec;
import org.spongepowered.api.text.Text;

import com.github.elrol.cpg.Main;
import com.github.elrol.cpg.libs.PluginInfo.Descriptions;
import com.github.elrol.cpg.libs.PluginInfo.Permissions;

public class CommandRegistry {

	public static void setup(Main main) {
		
		//cpg register
		CommandSpec registerGym = CommandSpec.builder()
				.description(Descriptions.registerGym)
				.permission(Permissions.registerGym)
				.arguments()
				.executor(new RegisterGymExecutor())
				.build();
		
		//cpg defeat
		CommandSpec defeat = CommandSpec.builder()
				.description(Descriptions.defeat)
				.permission(Permissions.defeat)
				.arguments(GenericArguments.player(Text.of("player")))
				.executor(new DefeatExecutor())
				.build();
		
		//
		CommandSpec playerGym = CommandSpec.builder()
			    .description(Descriptions.playerGym)
			    .permission(Permissions.playerGym)
			    .child(registerGym, "register", "registergym")
			    .child(defeat, "defeat", "admitdefeat")
			    .executor(new PlayerGymExecutor())
			    .build();

		Sponge.getCommandManager().register(main, playerGym, "playergyms", "playergym", "pg");
	}	
}
