package com.github.elrol.cpg.commands;

import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;

import com.github.elrol.cpg.libs.Methods;

public class DefeatExecutor implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if(src instanceof Player) {
			Player leader = (Player)src;
			Player player = args.<Player>getOne("player").get();
			player.getInventory().offer(Methods.getBadge(leader, player));
		}
		return CommandResult.success();
	}

}
