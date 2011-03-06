package de.sp1rit.signedit;

import org.bukkit.entity.*;
import org.bukkit.block.*;
import org.bukkit.event.block.*;

/**
 * SignEdit block listener
 * @author Sp1rit
 */
public class SignEditBlockListener extends BlockListener {
	private final SignEdit plugin;

    public SignEditBlockListener(SignEdit instance) {
        this.plugin = instance;
    }

    public void onBlockBreak(BlockBreakEvent event) {
    	if (event.isCancelled()) return;
    	
    	Block block = event.getBlock();
    	BlockState blockState = block.getState();
    	Player player = event.getPlayer();
    	
    	if (blockState instanceof Sign) {
    		if (plugin.signManager.signExists(block)) {
    			plugin.signManager.removeSign(block);
    			player.sendMessage(Language.SIGN_ID_BLOCK_REMOVED);
    		}
    	}
    }
    
    public void onSignChange(SignChangeEvent event) {
    	if (event.isCancelled()) return;
    	
/*		// rechteabfrage einbauen
		if (event.getLine(0).equalsIgnoreCase("[SESwitch]")) {
			if (plugin.saveManager.saveExists(event.getLine(1))) {
				String[] text = plugin.saveManager.loadText(event.getLine(1));
				event.setLine(0, text[0]);
				event.setLine(1, text[1]);
				event.setLine(2, text[2]);
				event.setLine(3, text[3]);
			}
		}*/
		
	    event.setLine(0, plugin.parseSignText(event.getLine(0), ""));
	    event.setLine(1, plugin.parseSignText(event.getLine(1), ""));
	    event.setLine(2, plugin.parseSignText(event.getLine(2), ""));
	    event.setLine(3, plugin.parseSignText(event.getLine(3), ""));
    }
    
/*    public void onBlockRightClick(BlockRightClickEvent event) {
    	if (event.getBlock().getState() instanceof Sign) {
    		plugin.saveManager.loadText("test1", (Sign)event.getBlock().getState());
    	}
    }*/
}