package com.github.elrol.cpg.libs;

import java.util.List;
import java.util.Optional;

import org.spongepowered.api.Sponge;
import org.spongepowered.api.command.CommandSource;
import org.spongepowered.api.data.key.Keys;
import org.spongepowered.api.entity.living.player.Player;
import org.spongepowered.api.item.ItemType;
import org.spongepowered.api.item.inventory.ItemStack;
import org.spongepowered.api.text.Text;
import org.spongepowered.api.text.action.TextActions;
import org.spongepowered.api.text.format.TextColors;
import org.spongepowered.api.text.format.TextStyles;

import com.github.elrol.cpg.config.DefaultConfiguration;
import com.pixelmonmod.pixelmon.battles.controller.BattleControllerBase;
import com.pixelmonmod.pixelmon.battles.controller.participants.PlayerParticipant;
import com.pixelmonmod.pixelmon.config.PixelmonItemsBadges;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;

public class Methods {
	
	public static void sendMessage(CommandSource src, Text text) {
		src.sendMessage(Text.of(DefaultConfiguration.getInstance().getHeader(), " ", text));
	}
	
	public static void sendError(CommandSource src, Text text) {
		sendMessage(src, Text.of(TextColors.RED, text));
	}
	
	public static ItemStack getBadge(Player leader, Player player) {
		Optional<ItemType> type = Sponge.getRegistry().getType(ItemType.class, DefaultConfiguration.getInstance().getCustomBadgeItem());
		ItemType badgeItem = (ItemType)PixelmonItemsBadges.basicBadge;;
		if(type.isPresent()) {
			badgeItem = type.get();
		} else {
			Methods.sendError(leader, Text.of("Badge set in config was invalid, defaulting to ", PixelmonItemsBadges.basicBadge.getRegistryName()));
		}
		ItemStack badge = ItemStack.of(badgeItem, 1);
		badge.offer(Keys.DISPLAY_NAME, DefaultConfiguration.getInstance().getBadgeName(leader));
		badge.offer(Keys.ITEM_LORE, DefaultConfiguration.getInstance().getBadgeLore(player));
		return badge; 
	}
	
	public static void sendIndentedMessage(CommandSource src, Text text) {
		src.sendMessage(Text.of("     ", text));
	}
	
	public static void sendListMessage(CommandSource src, List<Text> texts) {
		for(int i = 0; i < texts.size(); i++) {
			if(i == 0)
				sendMessage(src, texts.get(0));
			else
				sendIndentedMessage(src, texts.get(i));
		}
	}
	
	public static void tpAndNotify(Player player, Player leader, BlockPos gymLoc) {
		if(gymLoc == null)
			System.out.println("2174304908: " + "Gym Location is null");
		if(player == null)
			System.out.println("2174304908: " + "Player is null");
		if(leader == null)
			System.out.println("2174304908: " + "Leader is null");
		String command = "tppos " + player.getName() + " " + gymLoc.getX() + " " + gymLoc.getY() + " " + gymLoc.getZ();
		if(!Sponge.isServerAvailable()) {
			System.out.println("2174304908: " + "Server not available");
			return;
		}
		Sponge.getCommandManager().process(Sponge.getServer().getConsole(), command);
		Text text = Text.builder()
				.append(Text.of("Your gym has been challenged by ", player.getName(), "."))
				.build();
		
		Text accept = Text.builder().append(Text.of(TextColors.DARK_GREEN, TextStyles.BOLD,"[Accept]"))
				.onClick(TextActions.executeCallback(src -> {
					Player p = (Player)src; 
					Sponge.getCommandManager().process(Sponge.getServer().getConsole(), "tppos " + p.getName() + " " + gymLoc.getX() + " " + gymLoc.getY() + " " + gymLoc.getZ());
					startBattle(p, player);
				}))
				.onHover(TextActions.showText(Text.of(TextColors.DARK_GRAY, "Clicking this will teleport you to your gym and initiate the battle.")))
				.build();
		
		Text deny = Text.builder()
				.append(Text.of(TextColors.DARK_RED, TextStyles.BOLD, "[Deny]"))
				.onClick(TextActions.executeCallback(src -> {
					sendError(player, Text.of(leader.getName() + " has denied your battle request."));
				}))
				.onHover(TextActions.showText(Text.of(TextColors.DARK_GRAY, "Clicking this will notify the player that the Gym Battle Request has been denied.")))
				.build();
		sendMessage(leader, text);
		sendIndentedMessage(leader, Text.of(accept, " ", deny));
	}
	
	public static void startBattle(Player leader, Player challenger) {
		EntityPlayerMP leaderEP = (EntityPlayerMP)leader;
		EntityPlayerMP challengerEP = (EntityPlayerMP)challenger;
		BattleControllerBase base = new BattleControllerBase(new PlayerParticipant(leaderEP), new PlayerParticipant(challengerEP), DefaultConfiguration.getInstance().getRules());
	}
}
