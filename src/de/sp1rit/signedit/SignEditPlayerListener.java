package de.sp1rit.signedit;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerChatEvent;
import org.bukkit.event.player.PlayerListener;


public class SignEditPlayerListener extends PlayerListener {
	 /*
    private final SignEdit plugin;

    public SignEditPlayerListener(SignEdit instance) {
        this.plugin = instance;
    }
    
    public void onPlayerCommand(PlayerChatEvent event) {
    	if (event.isCancelled()) return;

        String[] split = event.getMessage().split(" ");
        Player player = event.getPlayer();
        
        if (split[0].equalsIgnoreCase("/signedit") || split[0].equalsIgnoreCase("/se")) {
        	if (split.length > 1) {
	        	if (split[1].equalsIgnoreCase("add")) {
	        		plugin.add(player, split);
	        	} else if (split[1].equalsIgnoreCase("edit")) {
	        		plugin.edit(player, split);
	        	} else if (split[1].equalsIgnoreCase("remove")) {
	        		plugin.remove(player, split);
	        	} else if (split[1].equalsIgnoreCase("clear")) {
	        		plugin.clear(player, split);
	        	} else if (split[1].equalsIgnoreCase("set")) {
	        		plugin.set(player, split);
	        	} else if (split[1].equalsIgnoreCase("setid")) {
	        		plugin.setById(player, split);
	        	} else if (split[1].equalsIgnoreCase("save")) {
	        		plugin.save(player, split);
	        	} else if (split[1].equalsIgnoreCase("rsave")) {
	        		plugin.rsave(player, split);
	        	} else if (split[1].equalsIgnoreCase("load")) {
	        		plugin.load(player, split);
	            } else {
	            	showHelp(player);
	            }
        	} else {
        		showHelp(player);
        	}
            event.setCancelled(true);
        }
    }
    
*/
}