package de.sp1rit.signedit;

import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.block.Sign;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Priority;
import org.bukkit.event.Event;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import org.anjocaido.groupmanager.GroupManager;
import com.nijikokun.bukkit.Permissions.Permissions;

public class SignEdit extends JavaPlugin {
	private static final Logger logger = Logger.getLogger("Minecraft.SignEdit");
	public SignManager signManager;
	public SaveManager saveManager;
	public Rights rights;
	
	public void onEnable() {
		SignEditBlockListener blockListener = new SignEditBlockListener(this);
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.BLOCK_BREAK, blockListener, Priority.Normal, this);
		pm.registerEvent(Event.Type.BLOCK_RIGHTCLICKED, blockListener, Priority.Normal, this);
		pm.registerEvent(Event.Type.SIGN_CHANGE, blockListener, Priority.Normal, this);
		
		signManager = new SignManager(this);
		saveManager = new SaveManager(this);
		rights = new Rights(this);
		
		setupPermissions();
		
		logger.info("[SignEdit] " + getDescription().getVersion() + " enabled.");
	}
	
	public void onDisable() {}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String commandLabel, String[] args) {
		String commandName = command.getName().toLowerCase();
        
        if (commandName.equalsIgnoreCase("se") || commandName.equalsIgnoreCase("signedit")) {
        	if (args.length > 0) {
	        	if (args[0].equalsIgnoreCase("add")) {
	        		add(sender, args);
	        	} else if (args[0].equalsIgnoreCase("edit")) {
	        		edit(sender, args);
	        	} else if (args[0].equalsIgnoreCase("remove")) {
	        		remove(sender, args);
	        	} else if (args[0].equalsIgnoreCase("clear")) {
	        		clear(sender, args);
	        	} else if (args[0].equalsIgnoreCase("set")) {
	        		set(sender, args);
	        	} else if (args[0].equalsIgnoreCase("setid")) {
	        		setById(sender, args);
	        	} else if (args[0].equalsIgnoreCase("save")) {
	        		save(sender, args);
	        	} else if (args[0].equalsIgnoreCase("rsave")) {
	        		rsave(sender, args);
	        	} else if (args[0].equalsIgnoreCase("load")) {
	        		load(sender, args);
	            }
        	}
    		showHelp(sender);
    		return true;
        }
		return false;
	}
	
    public void showHelp(CommandSender sender) {
    	sender.sendMessage(ChatColor.DARK_GREEN + " SignEdit Help: <> = needed; [] = optional;          ");
    	sender.sendMessage(ChatColor.BLUE       + "-----------------------------------------------------");
    	sender.sendMessage(ChatColor.RED        + " /se add <Name/ID> - Register ID for sign in view    ");
    	sender.sendMessage(ChatColor.RED        + " /se edit [Name/ID] <newName/newID> - Change ID      ");
    	sender.sendMessage(ChatColor.RED        + " /se remove [Name/ID] - Remove ID of sign in view    ");
    	sender.sendMessage(ChatColor.RED        + " /se set <Text> - Set the text for sign in view      ");
    	sender.sendMessage(ChatColor.RED        + " /se setid <Name/ID> <Text> - Set text for sign by ID");
    	sender.sendMessage(ChatColor.RED        + " /se clear [Name/ID] - Clear text of sign in view    ");
        sender.sendMessage(ChatColor.BLUE       + "-----------------------------------------------------");
        sender.sendMessage(ChatColor.DARK_GREEN + " <Text> formatting: \"Line 1\" \"&2Line 2\"...       ");
    }
	
	public void setupPermissions() {
        Plugin g = getServer().getPluginManager().getPlugin("GroupManager");
        Plugin p = getServer().getPluginManager().getPlugin("Permissions");
        if (g != null) {
            if (!g.isEnabled()) {
                getServer().getPluginManager().enablePlugin(g);
            }
            GroupManager gm = (GroupManager) g;
            rights.permissions = gm.getPermissionHandler();
        } else if (p!= null) {
    		if (!p.isEnabled()) {
    			getServer().getPluginManager().enablePlugin(p);
    		}
    		rights.permissions = ((Permissions) p).getHandler();
    	}
	}
	
    /**
     * Replace codes with colors etc.
     * @param line
     * @return
     */
    public String parseSignText(String newLine, String oldLine) {
    	if (newLine.matches("&[0-9a-f]") && !oldLine.equals("")) {
    		oldLine = oldLine.replaceAll("\\u00A7[0-9a-f]", "");
    		newLine = newLine.replaceFirst("&([0-9a-f])", "\\\u00A7$1");
    		return newLine + oldLine;
    	} else {
    		newLine = newLine.replaceAll("&([0-9a-f])([^&])", "\\\u00A7$1$2");
	    	return newLine;
    	}
    }
    
    /**
     * Schild im Sichtfeld registrieren.
     * @param sender
     * @param args
     * @return
     */
    public void add(CommandSender sender, String[] args) {
    	if (sender instanceof Player) {
	        if (args.length == 2) {
	            String signId = args[1];
	            if (!signManager.signExists(signId)) {
	                Sign sign = signManager.getTargetSign((Player)sender);
	                if (sign != null) {
	                    if (!signManager.signExists(sign)) {
	                    	if (rights.canAddId((Player)sender, sign.getBlock())) {
		                        if (signManager.addSign(signId, sign)) {
		                        	sender.sendMessage(Language.SIGN_ID_ADDED);
		                        } else {
		                        	sender.sendMessage(Language.ERROR_ID_ADD);
		                        }
	                    	}
	                    } else {
	                    	sender.sendMessage(Language.ERROR_SIGN_HAS_ID);
	                    	sender.sendMessage(Language.INFO_ID_EDIT);
	                    }
	                } else {
	                	sender.sendMessage(Language.ERROR_LOOK_AT_SIGN);
	                }
	            } else {
	            	sender.sendMessage(Language.ERROR_ID_ALREADY_REGISTERED);
	            }
	        } else {
	        	sender.sendMessage(Language.ERROR_WRONG_PARAMETER);
	            sender.sendMessage(ChatColor.RED + "SignEdit: /se add <Name/ID>");
	        }
    	} else {
    		sender.sendMessage(Language.ERROR_ADD_ONLY_PLAYER);
    	}
    }
    
    /**
     * Schild-ID bearbeiten.
     * @param player
     * @param split
     * @return
     */
    public void edit(CommandSender sender, String[] args) {
    	if (args.length == 2) {
    		if (sender instanceof Player) {
	    		Sign sign = signManager.getTargetSign((Player)sender);
	    		if (sign != null) {
	    			if (signManager.signExists(sign)) {
	    				String newId = args[1];
	    				if (!signManager.signExists(newId)) {
	    					if (rights.canEditId(sender, sign.getBlock())) {
		    					if (signManager.editSign(sign, newId)) {
		    						sender.sendMessage(Language.SIGN_ID_EDITED);
		    					} else {
		    						sender.sendMessage(Language.ERROR_ID_EDIT);
		    					}
	    					}
	    				} else {
	    					sender.sendMessage(Language.ERROR_ID_ALREADY_REGISTERED);
	    				}
	    			} else {
	    				sender.sendMessage(Language.ERROR_SIGN_NO_ID);
	    				sender.sendMessage(Language.INFO_ID_ADD);
	    			}
	    		} else {
	    			sender.sendMessage(Language.ERROR_LOOK_AT_SIGN);
	    		}
    		} else {
    			sender.sendMessage(Language.ERROR_EDIT_ONLY_PLAYER);
    		}
    	} else if (args.length == 3) {
    		String signId = args[1];
    		Sign sign = signManager.getSign(player, signId);
    		if (sign != null) {
    			if (signManager.signExists(sign)) {
    				String newId = args[2];
    				if (!signManager.signExists(newId)) {
    					if (rights.canEditId(player, sign.getBlock())) {
	    					if (signManager.editSign(sign, newId)) {
	    						sender.sendMessage(Language.SIGN_ID_EDITED);
	    					} else {
	    						sender.sendMessage(Language.ERROR_ID_EDIT);
	    					}
    					}
    				} else {
    					sender.sendMessage(Language.ERROR_ID_ALREADY_REGISTERED);
    				}
    			} else {
    				sender.sendMessage(Language.ERROR_SIGN_NO_ID);
    				sender.sendMessage(Language.INFO_ID_ADD);
    			}
    		} else {
    			sender.sendMessage(Language.ERROR_NO_SIGN_WITH_ID);
    		}
    	} else {
    		sender.sendMessage(Language.ERROR_WRONG_PARAMETER);
            sender.sendMessage(ChatColor.RED + "SignEdit: /se edit <newName/newID>");
            sender.sendMessage(ChatColor.RED + "SignEdit: /se edit <Name/ID> <newName/newID>");
    	}
    }
    
    /**
     * Schild-ID entfernen.
     * @param player
     * @param split
     * @return
     */
    public void remove(CommandSender sender, String[] split) {
    	if (split.length == 2) {
    		Sign sign = signManager.getTargetSign(player);
    		if (sign != null) {
    			if (signManager.signExists(sign)) {
    				if (rights.canRemoveId(player, sign.getBlock())) {
	    				if (signManager.removeSign(sign)) {
	    					sender.sendMessage(Language.SIGN_ID_REMOVED);
	    				} else {
	    					sender.sendMessage(Language.ERROR_ID_REMOVE);
	    				}
    				}
    			} else {
    				sender.sendMessage(Language.ERROR_SIGN_NO_ID);
    			}
    		} else {
    			sender.sendMessage(Language.ERROR_LOOK_AT_SIGN);
    		}
    	} else if (split.length == 3) {
    		String signId = split[2];
    		Sign sign = signManager.getSign(player, signId);
    		if (sign != null) {
    			if (signManager.signExists(sign)) {
    				if (rights.canRemoveId(player, sign.getBlock())) {
	    				if (signManager.removeSign(sign)) {
	    					sender.sendMessage(Language.SIGN_ID_REMOVED);
	    				} else {
	    					sender.sendMessage(Language.ERROR_ID_REMOVE);
	    				}
    				}
    			} else {
    				sender.sendMessage(Language.ERROR_SIGN_NO_ID);
    			}
    		} else {
    			sender.sendMessage(Language.ERROR_NO_SIGN_WITH_ID);
    		}
    	} else {
    		sender.sendMessage(Language.ERROR_WRONG_PARAMETER);
            sender.sendMessage(ChatColor.RED + "SignEdit: /se remove");
            sender.sendMessage(ChatColor.RED + "SignEdit: /se remove <Name/ID>");
    	}
    }
    
    /**
     * Clears the text of a sign.
     * @param player
     * @param split
     * @return
     */
    public void clear(CommandSender sender, String[] split) {
    	if (split.length == 2) {
    		Sign sign = signManager.getTargetSign(player);
    		if (sign != null) {
    			if (rights.canEditSignText(player, sign.getBlock())) {
	    			sign.setLine(0, "");
	    			sign.setLine(1, "");
	    			sign.setLine(2, "");
	    			sign.setLine(3, "");
	    			sign.update();
	    			sender.sendMessage(Language.SIGN_TEXT_REMOVED);
    			}
    		} else {
    			sender.sendMessage(Language.ERROR_LOOK_AT_SIGN);
    		}
    	} else if (split.length == 3) {
    		String signId = split[2];
    		Sign sign = signManager.getSign(player, signId);
    		if (sign != null) {
    			if (rights.canEditSignText(player, sign.getBlock())) {
	    			sign.setLine(0, "");
	    			sign.setLine(1, "");
	    			sign.setLine(2, "");
	    			sign.setLine(3, "");
	    			sign.update();
	    			sender.sendMessage(Language.SIGN_TEXT_REMOVED);
    			}
    		} else {
    			sender.sendMessage(Language.ERROR_NO_SIGN_WITH_ID);
    		}
    	} else {
    		sender.sendMessage(Language.ERROR_WRONG_PARAMETER);
    		sender.sendMessage(ChatColor.RED + "SignEdit: /se clear");
    		sender.sendMessage(ChatColor.RED + "SignEdit: /se clear <Name/ID>");
    	}
    }
    
    public void set(CommandSender sender, String[] split) {
        if (split.length > 2) {
            Sign sign = signManager.getTargetSign(player);
            if (sign != null) {
            	if (rights.canEditSignText(player, sign.getBlock())) {
	                String line, signText = "";
	                for(int i=2;i<split.length;i++)
	                    signText += " " + split[i];
	                Pattern p = Pattern.compile("[1-4]?\"[^\"]*\"");
	                Matcher m = p.matcher(signText);
	                for(int i=0;i<4;i++) {
	                     if (m.find()) {
	                        line = signText.substring(m.start(),m.end());
	                        if (line.startsWith("1")) {
	                            sign.setLine(0, parseSignText(line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\"")), sign.getLine(0)));
	                        } else if (line.startsWith("2")) {
	                            sign.setLine(1, parseSignText(line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\"")), sign.getLine(1)));
	                        } else if (line.startsWith("3")) {
	                            sign.setLine(2, parseSignText(line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\"")), sign.getLine(2)));
	                        } else if (line.startsWith("4")) {
	                            sign.setLine(3, parseSignText(line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\"")), sign.getLine(3)));
	                        } else {
	                            sign.setLine(i, parseSignText(line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\"")), sign.getLine(i)));
	                        }
	                    } else {
	                         if (i == 0) {
	                        	 sender.sendMessage(Language.ERROR_WRONG_FORMATTING);
	                             sender.sendMessage(ChatColor.RED + "SignEdit: /se set \"Text 1\" \"Text 2\"...");
	                             sender.sendMessage(ChatColor.RED + "SignEdit: /se set 2\"Text Zeile2\" 4\"Text Zeile4\"");
	                             return;
	                         }
	                         break;
	                    }
	                }
	                sign.update();
	                sender.sendMessage(Language.SIGN_TEXT_SET);
            	}
            } else {
            	sender.sendMessage(Language.ERROR_LOOK_AT_SIGN);
            }
        } else {
        	sender.sendMessage(Language.ERROR_WRONG_PARAMETER);
            sender.sendMessage(ChatColor.RED + "SignEdit: /se set <Text>");
            sender.sendMessage(ChatColor.RED + "SignEdit: /se setid <Name/ID> <Text>");
        }
    }

    public void setById(CommandSender sender, String[] split) {
        if (split.length > 3) {
            String signId = split[2];
            Sign sign = signManager.getSign(player, signId);
            if (sign != null) {
            	if (rights.canEditSignText(player, sign.getBlock())) {
	                String line, signText = "";
	                for(int i=3;i<split.length;i++)
	                    signText += " " + split[i];
	                Pattern p = Pattern.compile("[1-4]?\"[^\"]*\"");
	                Matcher m = p.matcher(signText);
	                for(int i=0;i<4;i++) {
	                     if (m.find()) {
	                        line = signText.substring(m.start(),m.end());
	                        if (line.startsWith("1")) {
	                            sign.setLine(0, parseSignText(line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\"")), sign.getLine(0)));
	                        } else if (line.startsWith("2")) {
	                            sign.setLine(1, parseSignText(line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\"")), sign.getLine(1)));
	                        } else if (line.startsWith("3")) {
	                            sign.setLine(2, parseSignText(line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\"")), sign.getLine(2)));
	                        } else if (line.startsWith("4")) {
	                            sign.setLine(3, parseSignText(line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\"")), sign.getLine(3)));
	                        } else {
	                            sign.setLine(i, parseSignText(line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\"")), sign.getLine(i)));
	                        }
	                    } else {
	                         if (i == 0) {
	                        	 sender.sendMessage(Language.ERROR_WRONG_FORMATTING);
	                             sender.sendMessage(ChatColor.RED + "SignEdit: /se setid <Name/ID> \"Text 1\" \"Text 2\"...");
	                             sender.sendMessage(ChatColor.RED + "SignEdit: /se setid <Name/ID> 2\"Text 2\" 4\"Text 4\"");
	                             return;
	                         }
	                         break;
	                    }
	                }
	                sign.update();
	                sender.sendMessage(Language.SIGN_TEXT_SET);
            	}
            } else {
            	sender.sendMessage(Language.ERROR_NO_SIGN_WITH_ID);
            }
        } else {
        	sender.sendMessage(Language.ERROR_WRONG_PARAMETER);
            sender.sendMessage(ChatColor.RED + "SignEdit: /se setid <Name/ID> <Text>");
        }
    }
    
    public void save(CommandSender sender, String[] split) {
    	if (split.length == 3) {
            Sign sign = signManager.getTargetSign(player);
            if (sign != null) {
            	String saveId = split[2];
            	if (!saveManager.saveExists(saveId)) {
            		if (rights.canSaveText(player, sign.getBlock())) {
	            		if (saveManager.saveText(saveId, sign.getLine(0), sign.getLine(1), sign.getLine(2), sign.getLine(3))) {
	            			sender.sendMessage(Language.SIGN_TEXT_SAVED);
    					} else {
    						sender.sendMessage(Language.ERROR_SAVE_TEXT);
	            		}
            		}
            	} else {
            		sender.sendMessage(Language.ERROR_SAVE_EXISTS);
            	}
            } else {
            	sender.sendMessage(Language.ERROR_LOOK_AT_SIGN);
            }
    	} else if (split.length == 4) {
            String signId = split[2];
            Sign sign = signManager.getSign(player, signId);
            if (sign != null) {
            	String saveId = split[3];
            	if (!saveManager.saveExists(saveId)) {
	            	if (rights.canSaveText(player, sign.getBlock())) {
	            		if (saveManager.saveText(saveId, sign.getLine(0), sign.getLine(1), sign.getLine(2), sign.getLine(3))) {
	            			sender.sendMessage(Language.SIGN_TEXT_SAVED);
    					} else {
    						sender.sendMessage(Language.ERROR_SAVE_TEXT);
	            		}
	            	}
            	} else {
            		sender.sendMessage(Language.ERROR_SAVE_EXISTS);
            	}
            } else {
            	sender.sendMessage(Language.ERROR_NO_SIGN_WITH_ID);
            }
    	} else {
    		sender.sendMessage(Language.ERROR_WRONG_PARAMETER);
    		sender.sendMessage(ChatColor.RED + "SignEdit: /se save <SaveName>");
    		sender.sendMessage(ChatColor.RED + "SignEdit: /se save <SignName/ID> <SaveName>");
    	}
    }
    
    public void rsave(CommandSender sender, String[] split) {
    	if (split.length == 3) {
            String saveId = split[2];
            if (saveManager.saveExists(saveId)) {
            	if (rights.canRemoveSavedText(player)) {
	            	if (saveManager.removeText(saveId)) {
	            		sender.sendMessage(Language.SAVED_TEXT_REMOVED);
    				} else {
    					sender.sendMessage(Language.ERROR_SAVE_REMOVE);
	            	}
            	}
            } else {
            	sender.sendMessage(Language.ERROR_SIGN_HAS_ID);
            	sender.sendMessage(Language.INFO_ID_EDIT);
            }
    	} else {
    		sender.sendMessage(Language.ERROR_WRONG_PARAMETER);
    		sender.sendMessage(ChatColor.RED + "SignEdit: /se rsave <SaveName>");
    	}
    }
    
    public void load(CommandSender sender, String[] split) {
    	if (split.length == 3) {
            Sign sign = signManager.getTargetSign(player);
            if (sign != null) {
            	String saveId = split[2];
            	if (saveManager.saveExists(saveId)) {
            		if (rights.canLoadText(player, sign.getBlock()) & rights.canEditSignText(player, sign.getBlock())) {
	            		if (saveManager.loadText(saveId, sign)) {
	            			sender.sendMessage(Language.SIGN_TEXT_LOADED);
    					} else {
    						sender.sendMessage(Language.ERROR_SAVE_LOAD);
	            		}
            		}
            	} else {
            		sender.sendMessage(Language.ERROR_SIGN_HAS_ID);
            		sender.sendMessage(Language.INFO_ID_EDIT);
            	}
            } else {
            	sender.sendMessage(Language.ERROR_LOOK_AT_SIGN);
            }
    	} else if (split.length == 4) {
            String signId = split[2];
            Sign sign = signManager.getSign(player, signId);
            if (sign != null) {
            	String saveId = split[3];
            	if (saveManager.saveExists(saveId)) {
	            	if (rights.canLoadText(player, sign.getBlock()) & rights.canEditSignText(player, sign.getBlock())) {
	            		if (saveManager.loadText(saveId, sign)) {
	            			sender.sendMessage(Language.SIGN_TEXT_LOADED);
    					} else {
    						sender.sendMessage(Language.ERROR_SAVE_LOAD);
	            		}
	            	}
            	} else {
            		sender.sendMessage(Language.ERROR_SIGN_HAS_ID);
            		sender.sendMessage(Language.INFO_ID_EDIT);
            	}
            } else {
            	sender.sendMessage(Language.ERROR_NO_SIGN_WITH_ID);
            }
    	} else {
    		sender.sendMessage(Language.ERROR_WRONG_PARAMETER);
    		sender.sendMessage(ChatColor.RED + "SignEdit: /se load <saveID>");
    		sender.sendMessage(ChatColor.RED + "SignEdit: /se load <Name/ID> <saveID>");
    	}
    }
}
