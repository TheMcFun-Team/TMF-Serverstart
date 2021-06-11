package net.themcfun.serverstart.Main;

import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

import net.md_5.bungee.BungeeCord;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

public class Main extends Plugin{

	static String defaultpath;
	static String config;
	static String startcomand;

	public static Main INSTANCE;
	
	public static ArrayList<String> forbiddenservernames;

	@Override
	public void onEnable() {

		INSTANCE = this;

		getLogger().info(ChatColor.RED + "[TMF-Serverstart] sucessfully loaded!");
		getLogger().info(" ");
		getLogger().info(ChatColor.GRAY + "----------------------------------------");
		getLogger().info(ChatColor.RED + "Version: 1.0");
		getLogger().info(ChatColor.RED + "Commands: /startserver");
		getLogger().info(ChatColor.RED + "Developers: Redstone_Studios & Mr_Comand");
		getLogger().info(ChatColor.GRAY + "----------------------------------------");

		registerCommands();
		config();

	}	

	private void config() {

		try {
			if(!getDataFolder().exists()) {
				getDataFolder().mkdir();
			}
			File file = new File(getDataFolder().getPath(), "config.yml");
			if(!file.exists()) {
				file.createNewFile();
				Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
				config.set("defaultpath", "..\\");
				config.set("startcomand", "sh {0} start.sh");
				config.set("#{0} wird durch den pfath ersetzt.", "");
				config.set("forbiddenServers", new ArrayList<String>());
				ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);
			} 

			Configuration config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(file);
			ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, file);

			defaultpath = config.getString("defaultpath");
			forbiddenservernames = (ArrayList<String>) config.getList("forbiddenServers");
			startcomand = config.getString("startcomand");
			
		} catch(IOException e) {

		}

	}

	@Override
	public void onDisable() {
		getLogger().info(ChatColor.RED + "[TMF-Serverstart] sucessfully disabled!");

	}	
	private void registerCommands() {

		ProxyServer.getInstance().getPluginManager().registerCommand(this, new COMMAND_Startserver("startserver"));

	}
	public static void startserver(String servername) {
		

		if (defaultpath != "unset") {
			
			boolean allowed = false;
			for (Iterator servers = BungeeCord.getInstance().getServers().keySet().iterator(); servers.hasNext();) {
				String server = (String) servers.next();
				if ((server.equalsIgnoreCase(servername))&&(forbiddenservernames.contains(server))) {
					allowed = true;
					break;	
				}
			}
			

			if (!allowed) {
				INSTANCE.getLogger().warning(ChatColor.RED + "[TMF-Serverstart] Invalid Servername!");
				return;
			}
			String path = (defaultpath + "\\" + servername);
			
			try {
				ProcessBuilder proc = new ProcessBuilder(String.format(startcomand, path));
				proc.redirectOutput(ProcessBuilder.Redirect.INHERIT);
				File pathfile = new File(path);
				proc.directory(pathfile);
				Process p = proc.start();

				InputStream stdIn = p.getInputStream();
				InputStreamReader isr = new InputStreamReader(stdIn);
				BufferedReader br = new BufferedReader(isr);

				/*
				String line = null;
				System.out.println("[" + servername + "-ConsoleFeed]");

				while ((line = br.readLine()) != null)
					System.out.println(line);
				

				int exitVal = p.waitFor();
				System.out.println("Process exitValue: " + exitVal);
				*/
			} catch (IOException e) {

				e.printStackTrace();
			}
		} else {

			INSTANCE.getLogger().warning("[TMF-Serverstart] Set a defaultpath first!");

		}
	}
}
