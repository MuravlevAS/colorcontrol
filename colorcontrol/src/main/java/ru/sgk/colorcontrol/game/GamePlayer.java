package ru.sgk.colorcontrol.game;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class GamePlayer 
{	
	public Player player;
	public String playerName;
	public String currentMap;
	public GameTeam team;
	public int kills;
	public int deaths;
	public int health;
	
	
	public GamePlayer(Player player)
	{
		this.player = player;
		this.playerName = player.getName();
	}
	
	public GamePlayer(String playerName)
	{
		this.playerName = playerName;
		this.player = Bukkit.getPlayer(playerName);
	}
	
	public GamePlayer(Player player, String currentMap)
	{
		this.player = player;
		this.playerName = player.getName();
		this.currentMap = currentMap;
	}
	
	public GamePlayer(String playerName, String currentMap)
	{
		this.playerName = playerName;
		this.player = Bukkit.getPlayer(playerName);
		this.currentMap = currentMap;
	}
	
	public void setMap(String map)
	{
		currentMap = map;
	}

	public boolean living()
	{
		return health > 0;
	}
	
	public void removeKill()
	{
		kills--;
	}
	
	public void removeKills(int amount)
	{
		kills -= amount;
	}
	
	public void addKill()
	{
		kills++;
	}

	public void addKills(int amount)
	{
		kills += amount;
	}
}
