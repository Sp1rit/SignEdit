package de.sp1rit.signedit;

import java.util.List;

import org.anjocaido.groupmanager.dataholder.worlds.WorldsHolder;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import com.griefcraft.lwc.LWC;
import com.griefcraft.lwc.LWCPlugin;
import com.griefcraft.model.Protection;

import com.nijiko.permissions.PermissionHandler;

// TODO Rechte schachteln signedit.id.add, signedit.id.remove, signedit.save.save, signeidt.save.load etc.

public class Rights {
	private final SignEdit plugin;
	public PermissionHandler permissions;
	public WorldsHolder worldsHolder;
	
	public Rights(SignEdit instance) {
		this.plugin = instance;
	}
	
	public boolean canAddId(Player player, Block block) {
		if (canAdminProtection(player, block)) {
			if (hasPermission(player, "signedit.add")){
				return true;
			} else {
				player.sendMessage(Language.PERMISSIONS_PERMISSION);
			}
		} else {
			player.sendMessage(Language.PERMISSIONS_LWC);
		}
		return false;
	}
	
	public boolean canEditId(CommandSender sender, Block block) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			if (canAdminProtection(player, block)) {
				if (hasPermission(player, "signedit.edit")){
					return true;
				} else {
					player.sendMessage(Language.PERMISSIONS_PERMISSION);
				}
			} else {
				player.sendMessage(Language.PERMISSIONS_LWC);
			}
		} else {
			if (sender.isOp()) {
				return true;
			}
		}
		return false;
	}
	
	public boolean canRemoveId(CommandSender sender, Block block) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			if (canAdminProtection(player, block)) {
				if (hasPermission(player, "signedit.remove")){
					return true;
				} else {
					player.sendMessage(Language.PERMISSIONS_PERMISSION);
				}
			} else {
				player.sendMessage(Language.PERMISSIONS_LWC);
			}
		} else {
			if (sender.isOp()) {
				return true;
			}
		}
		return false;
	}
	
	public boolean canEditSignText(CommandSender sender, Block block) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			if (canAdminProtection(player, block)) {
				if (hasPermission(player, "signedit.edittext")){
					return true;
				} else {
					player.sendMessage(Language.PERMISSIONS_PERMISSION);
				}
			} else {
				player.sendMessage(Language.PERMISSIONS_LWC);
			}
		} else {
			if (sender.isOp()) {
				return true;
			}
		}
		return false;
	}
	
	public boolean canSaveText(CommandSender sender, Block block) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			if (canAdminProtection(player, block)) {
				if (hasPermission(player, "signedit.savetext")){
					return true;
				} else {
					player.sendMessage(Language.PERMISSIONS_PERMISSION);
				}
			} else {
				player.sendMessage(Language.PERMISSIONS_LWC);
			}
		} else {
			if (sender.isOp()) {
				return true;
			}
		}
		return false;
	}
	
	public boolean canRemoveSavedText(CommandSender sender) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			if (hasPermission(player, "signedit.removetext")) {
				return true;
			} else {
				player.sendMessage(Language.PERMISSIONS_PERMISSION);
			}
		} else {
			if (sender.isOp()) {
				return true;
			}
		}
		return false;
	}
	
	public boolean canLoadText(CommandSender sender, Block block) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			if (canAdminProtection(player, block)) {
				if (hasPermission(player, "signedit.loadtext")){
					return true;
				} else {
					player.sendMessage(Language.PERMISSIONS_PERMISSION);
				}
			} else {
				player.sendMessage(Language.PERMISSIONS_LWC);
			}
		} else {
			if (sender.isOp()) {
				return true;
			}
		}
		return false;
	}
	
	public boolean canUseColors(CommandSender sender, Block block) {
		if (sender instanceof Player) {
			Player player = (Player)sender;
			if (hasPermission(player, "signedit.colors")){
				return true;
			} else {
				player.sendMessage(Language.PERMISSIONS_PERMISSION);
			}
		} else {
			if (sender.isOp()) {
				return true;
			}
		}
		return false;
	}
	
	public boolean hasPermission(Player player, String permission) {
		if (worldsHolder != null) {
			if (worldsHolder.getWorldPermissions(player).has(player, permission)) {
				return true;
			} else {
				return false;
			}
		} else if (permissions != null) {
			if (permissions.has(player, permission)) {
				return true;
			} else {
				return false;
			}
		}
		return true;
	}
	
    public boolean canAdminProtection(Player player, Block block) {
    	Plugin lwcPlugin = plugin.getServer().getPluginManager().getPlugin("LWC");

    	if(lwcPlugin != null) {
    		LWC lwc = (LWC) ((LWCPlugin) lwcPlugin).getLWC();
    		
    		if(!lwc.isProtectable(block)) {
    			return false;
    		}
    		
    		List<Block> protectedBlocks = lwc.getProtectionSet(player.getWorld(), block.getX(), block.getY(), block.getZ());

    		if(protectedBlocks.size() > 0) {
    			for(Block protectedBlock : protectedBlocks) {
    				Protection protection = lwc.getPhysicalDatabase().loadProtectedEntity(protectedBlock.getX(), protectedBlock.getY(), protectedBlock.getZ());
    				
    				if(protection != null) {
    					return lwc.canAdminProtection(player, protection);
    				}
    			}
    		}
    	}

    	return true;
    }
}
