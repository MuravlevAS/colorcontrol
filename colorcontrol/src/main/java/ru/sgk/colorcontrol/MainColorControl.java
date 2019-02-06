package ru.sgk.colorcontrol;

import java.io.IOException;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import ru.sgk.colorcontrol.cmd.ColorControlCommand;
import ru.sgk.colorcontrol.data.MySQLMain;
import ru.sgk.colorcontrol.events.MainEvents;
import ru.sgk.colorcontrol.game.Game;

public class MainColorControl extends JavaPlugin
{
	public static Logger logger;
	public static MainColorControl plugin;
	public static FileConfiguration config;
	public static String prefix = "§bColorControl §8>> ";
	public static boolean debug = false;
	public static World world;
	public static Game game;
	
	@Override
	public void onEnable() 
	{
		logger = getLogger();
		plugin = this;
		initConfiguration();
		debug = config.getBoolean("debug");
		new MySQLMain();
		regCmdList();
		regListeners();
		initGame();
		world = Bukkit.getServer().getWorlds().get(0);
		
		world.setAutoSave(false);
		world.save();
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
	
	public static void initConfiguration() 
	{
		config = plugin.getConfig();
		config.options().copyDefaults(true);
		debugMessage("§rКонфиг загружен");
	}
	public static void saveConfiguration()
	{
		try {
			config.save("config.yml");
			debugMessage("§rКонфиг сохранён");
		} catch (IOException e) {
			debugMessage("§cОшибка при сохранении конфига");
			e.printStackTrace();
		}
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
		
		
	}
	
}
