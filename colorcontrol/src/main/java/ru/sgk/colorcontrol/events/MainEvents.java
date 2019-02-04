package ru.sgk.colorcontrol.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import ru.sgk.colorcontrol.MainColorControl;

public class MainEvents implements Listener
{
	MainColorControl plugin;
	
	public MainEvents(MainColorControl plugin)
	{
		this.plugin = plugin;
	}
	
	
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e)
	{
		Player player = e.getPlayer();
		MainColorControl.game.join(player);
	}
	
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent e)
	{
		Player player = e.getPlayer();
		MainColorControl.game.leave(player);
	}
}