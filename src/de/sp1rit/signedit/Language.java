package de.sp1rit.signedit;

import org.bukkit.ChatColor;

public final class Language {
	public static final String SIGN_ID_BLOCK_REMOVED = ChatColor.GREEN + "SignEdit: Sign-ID removed.";
	public static final String SIGN_ID_ADDED         = ChatColor.GREEN + "SignEdit: Sign-ID successfully registered.";
	public static final String SIGN_ID_REMOVED       = ChatColor.GREEN + "SignEdit: Sign-ID successfully removed.";
	public static final String SIGN_ID_EDITED        = ChatColor.GREEN + "SignEdit: Sign-ID successfully edited.";
	public static final String SIGN_TEXT_REMOVED     = ChatColor.GREEN + "SignEdit: Sign-Text successfully removed.";
	public static final String SIGN_TEXT_SET         = ChatColor.GREEN + "SignEdit: Sign-Text successfully set.";
	public static final String SIGN_TEXT_SAVED		 = ChatColor.GREEN + "SignEdit: Sign-Text successfully saved.";
	public static final String SAVED_TEXT_REMOVED	 = ChatColor.GREEN + "SignEdit: Saved-Text successfully removed.";
	public static final String SIGN_TEXT_LOADED		 = ChatColor.GREEN + "SignEdit: Sign-Text successfully loaded.";
	
	public static final String ERROR_ADD_ONLY_PLAYER  = ChatColor.RED + "SignEdit: A sign can only be registered by players.";
	public static final String ERROR_EDIT_ONLY_PLAYER = ChatColor.RED + "SignEdit: As non-player you have to specify a sign-name.";
	
	public static final String ERROR_ID_ALREADY_REGISTERED = ChatColor.RED + "SignEdit: The sign ID is already registered.";
	public static final String ERROR_LOOK_AT_SIGN          = ChatColor.RED + "SignEdit: No sign in sight.";
	public static final String ERROR_NO_SIGN_WITH_ID       = ChatColor.RED + "SignEdit: There is no sign with this ID.";
	public static final String ERROR_SIGN_NO_ID            = ChatColor.RED + "SignEdit: The sign has no ID.";
	public static final String ERROR_WRONG_PARAMETER       = ChatColor.RED + "SignEdit: Wrong parameter count.";
	public static final String ERROR_WRONG_FORMATTING      = ChatColor.RED + "SignEdit: Wrong syntax.";
	public static final String ERROR_SIGN_HAS_ID           = ChatColor.RED + "SignEdit: The sign already has an ID.";
	public static final String ERROR_NO_SAVE_WITH_ID       = ChatColor.RED + "SignEdit: There is no saved text with this ID.";
	public static final String ERROR_SAVE_EXISTS		   = ChatColor.RED + "SignEdit: The save-name is already used.";
	
	public static final String ERROR_ID_ADD    	 = ChatColor.RED + "SignEdit: An error has occurred.";
	public static final String ERROR_ID_REMOVE 	 = ChatColor.RED + "SignEdit: An error has occurred.";
	public static final String ERROR_ID_EDIT  	 = ChatColor.RED + "SignEdit: An error has occurred.";
	public static final String ERROR_SAVE_TEXT	 = ChatColor.RED + "SignEdit: An error has occurred.";
	public static final String ERROR_SAVE_REMOVE = ChatColor.RED + "SignEdit: An error has occurred.";
	public static final String ERROR_SAVE_LOAD	 = ChatColor.RED + "SignEdit: An error has occurred.";
	
	public static final String INFO_ID_ADD  = ChatColor.RED + "SignEdit: Register an ID: /se add <Name/ID>";
	public static final String INFO_ID_EDIT = ChatColor.RED + "SignEdit: Edit an ID: /se edit <NeueID>";
	
	public static final String PERMISSIONS_LWC 			= ChatColor.RED + "SignEdit: The sign is protected by LWC.";
	public static final String PERMISSIONS_PERMISSION	= ChatColor.RED + "SignEdit: You dont have the permission to do this.";
}
