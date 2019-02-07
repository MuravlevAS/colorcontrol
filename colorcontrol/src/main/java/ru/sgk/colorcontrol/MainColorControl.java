package ru.sgk.colorcontrol;

import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;

import ru.sgk.colorcontrol.cmd.ColorControlCommand;
import ru.sgk.colorcontrol.data.MySQLMain;
import ru.sgk.colorcontrol.events.MainEvents;
import ru.sgk.colorcontrol.game.Game;
import ru.sgk.colorcontrol.game.GameCube;
import ru.sgk.colorcontrol.game.GameTeam;

public class MainColorControl extends JavaPlugin
{
	public static Logger logger;
	public static MainColorControl plugin;
	public static FileConfiguration config;
	public static String prefix = "§bColorControl §8>> ";
	public static boolean debug = false;
	public static World world;
	public static Game game = null;
	
	@Override
	public void onEnable() 
	{
		logger = getLogger();
		plugin = this;
		ConfigurationSerialization.registerClass(GameCube.class);
		ConfigurationSerialization.registerClass(GameTeam.class);
		initConfiguration().options().copyDefaults(true);
		MainColorControl.debug = config.getBoolean("debug");
		saveConfiguration();
		new MySQLMain();
		
		regCmdList();
		regListeners();
		world = Bukkit.getServer().getWorlds().get(0);
		world.setAutoSave(false);
		world.save();
		
		initGame();
		logger.info("§aПлагин включён!");
	}
	
	@Override
	public void onDisable() 
	{
		world.save();
		saveConfiguration();
		MySQLMain.disconnect();
		logger.info("§aПлагин выключен!");
	}
	
	public static FileConfiguration initConfiguration() 
	{
		config = plugin.getConfig();
		return config;
	}
	public static void saveConfiguration()
	{
		try {
			config.save(plugin.getDataFolder().getPath() + "/config.yml");
		} catch (IOException e) {
			e.printStackTrace();
		}
		debugMessage("§rКонфиг сохранён");
	}
	public void regCmdList()
	{
		getCommand("colorcontrol").setExecutor(new ColorControlCommand());
	}
	
	public void regListeners()
	{
		getServer().getPluginManager().registerEvents(new MainEvents(plugin), this);
	}
	public static void debugMessage(String message)
	{
		if (MainColorControl.debug)
			MainColorControl.logger.info("§c[DEBUG]§r " + MainColorControl.prefix + message);
	}
	
	public void initGame()
	{
		if (!config.getBoolean("gameserver"))
			return;
		if (!config.contains("game")) return;
		Game.getFromConfig();
		
	}
	
}
