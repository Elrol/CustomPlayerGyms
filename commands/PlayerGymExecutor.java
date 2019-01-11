package com.github.elrol.cpg.commands;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.entity.living.player.User;
import org.spongepowered.api.service.user.UserStorageService;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import com.github.elrol.cpg.config.GymListConfiguration;
import com.github.elrol.cpg.libs.Methods;
import com.github.elrol.cpg.libs.PluginInfo;

import net.minecraft.util.math.BlockPos;

public class PlayerGymExecutor implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		Methods.sendMessage(src, Text.of(PluginInfo.NAME, " ", PluginInfo.VERSION));
		if(src instanceof Player) {
			Player player = (Player)src;
			List<Player> onlineLeaders = new ArrayList<Player>();
			List<Text> tpText = new ArrayList<Text>();
			tpText.add(Text.of(TextStyles.BOLD, TextColors.GRAY, "Current Online Gyms"));
			Optional<UserStorageService> optStorage = Sponge.getServiceManager().provide(UserStorageService.class);
			if(!optStorage.isPresent()) {
				System.out.println("2174304908:" + "User Storage Service not loaded");
				return CommandResult.empty();
			}
			for(UUID uuid : GymListConfiguration.getInstance().getPlayers(player)) {
				Optional<User> user = optStorage.get().get(uuid);
				if(user.isPresent() && user.get().isOnline() && user.get().getPlayer().isPresent()) {
					onlineLeaders.add(user.get().getPlayer().get());
					System.out.println("2174304908:" + user.get().getName() + " is online");
				} else {
					if(user.isPresent())
						System.out.println("2174304908:" + user.get().getName() + " is offline");
					else
						System.out.println("2174304908:" + uuid + " is not a player");
				}
			}
			for(Player leader : onlineLeaders) {
				BlockPos gymLoc = GymListConfiguration.getInstance().getGym(leader);
				Text gymText = Text.builder().append(Text.of(leader.getName(), "'s Gym"))
						.onClick(TextActions.executeCallback(entity -> Methods.tpAndNotify(player, leader, gymLoc)))
						.build();
				//System.out.println("2174304908:" + "Added text to list");
				tpText.add(gymText);
			}
			Methods.sendListMessage(src, tpText);
			
		}
		return CommandResult.success();
	}

}
