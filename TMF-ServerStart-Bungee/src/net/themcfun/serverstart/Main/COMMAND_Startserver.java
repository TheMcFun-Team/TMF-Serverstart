package net.themcfun.serverstart.Main;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class COMMAND_Startserver extends Command {

	static String playername;
	
	public COMMAND_Startserver(String name) {
		super(name);
	}

	@SuppressWarnings("deprecation")
	@Override
	public void execute(CommandSender sender, String[] args) {
		
		CommandSender cs = sender;
		playername = cs.getName();
		
		if(sender instanceof CommandSender) {
			
		// /startserver <Servername>
		
		if(args.length > 0) {
		
		Main.servername = args[1];
							
		} else {
			cs.sendMessage("Use /help to view a list of avaiable Commands!");
		}
		} else {
		   	playername = "Console";
   }
  }	
 }
