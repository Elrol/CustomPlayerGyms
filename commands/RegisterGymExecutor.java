package com.github.elrol.cpg.commands;

import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandException;
import org.spongepowered.api.command.CommandResult;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.command.args.CommandContext;
import org.spongepowered.api.command.spec.CommandExecutor;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.ItemTypes;
import org.spongepowered.api.item.inventory.Inventory;
import org.spongepowered.api.text.Text;

import com.github.elrol.cpg.config.DefaultConfiguration;
import com.github.elrol.cpg.config.GymListConfiguration;
import com.github.elrol.cpg.libs.Methods;
import com.pixelmonmod.pixelmon.config.PixelmonItemsBadges;

public class RegisterGymExecutor implements CommandExecutor {

	@Override
	public CommandResult execute(CommandSource src, CommandContext args) throws CommandException {
		if(src instanceof Player) {
			Player player = (Player)src;
			for(int i = 0; i < DefaultConfiguration.getInstance().getBadges().size(); i++) {
				Optional<ItemType> opBadge = Sponge.getRegistry().getType(ItemType.class, DefaultConfiguration.getInstance().getBadges().get(i));
				ItemType badge = (ItemType)PixelmonItemsBadges.boulderBadge;
				if(opBadge.isPresent()) {
					badge = opBadge.get();
				} else {
					Methods.sendError(src, Text.of("Badge '", DefaultConfiguration.getInstance().getBadges().get(i), "' was invalid"));
				}
				boolean hasBadge = false;
				for(Inventory inv : player.getInventory().slots()) {
					if(inv.peek().isPresent()) {
						ItemType type = inv.peek().get().getType();
						if(type == null || type == ItemTypes.AIR)
							continue;
						if(type.equals(badge)) {
							hasBadge = true;
							break;
						}
					}
				}
				if(hasBadge) {
					continue;
				} else {
					Methods.sendError(src, Text.of("You are missing the " + badge.getName() + ". You need all of the Badges to create your own Gym!"));
					return CommandResult.empty();
				}
			}
			
			//Has all badges
			GymListConfiguration.getInstance().setGym(player);
			Methods.sendMessage(src, Text.of("Successfully made this your new Gym!"));
			return CommandResult.success();
		} else {
			Methods.sendError(src, Text.of("You must be a player to run this command"));
			return CommandResult.empty();
		}
	}

}
