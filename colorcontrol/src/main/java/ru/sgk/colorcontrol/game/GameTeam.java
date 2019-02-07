package ru.sgk.colorcontrol.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;


public class GameTeam implements ConfigurationSerializable 
{
	public Location teamSpawn;
	public List<Player> playersInTeam = new ArrayList<Player>();
	public int teamSize;
	public int currentAOP = 0; // current amount of players
	public TeamColor color;
	public Block teamBlock;
	public int health;
	public String name;
	public int maxPlayers = 5;
	
	
	
	public GameTeam(Player player, int teamSize)
	{
		this.teamSize = teamSize;
		teamSpawn = player.getLocation();
		name = this.color.toString();
	}
	
	public GameTeam(Location teamSpawn, int teamSize)
	{
		this.teamSize = teamSize;
		this.teamSpawn = teamSpawn;
		name = this.color.toString();
	}
	
	public GameTeam(Location teamSpawn, int teamSize, TeamColor color)
	{
		this.teamSize = teamSize;
		this.teamSpawn = teamSpawn;
		this.color = color;
		name = this.color.toString();
	}
	public GameTeam(Location teamSpawn, int teamSize, String color)
	{
		this.teamSize = teamSize;
		this.teamSpawn = teamSpawn;
		this.color = TeamColor.valueOf(color.toUpperCase());
		name = this.toString();
	}

	public GameTeam(Map<String, Object> map)
	{
		this.maxPlayers = (Integer) map.get("max-players");
		this.teamSize = (Integer) map.get("size");
		this.health = (Integer) map.get("health");
		this.color = TeamColor.valueOf((String) map.get("color"));
		this.teamSpawn = (Location) map.get("spawn");
		
	}
	public Map<String, Object> serialize() 
	{
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("max-players", maxPlayers);
		map.put("size", teamSize);
		map.put("health", health);
		map.put("color", color.toString());
		map.put("spawn", teamSpawn);
		return map;
	}
	
	public static GameTeam deserialize(Map<String, Object> map)
	{
		return new GameTeam(map);
	}
	
	@SuppressWarnings("deprecation")
	public void setColor(String color)
	{
		this.color = TeamColor.valueOf(color.toUpperCase());
		if (this.color == TeamColor.BLUE)
		{
			teamBlock.setType(Material.CONCRETE);
			teamBlock.setData((byte) 11);
		}
		else if (this.color == TeamColor.RED)
		{
			teamBlock.setType(Material.CONCRETE);
			teamBlock.setData((byte) 14);
		}
		else if (this.color == TeamColor.GREEN)
		{
			teamBlock.setType(Material.CONCRETE);
			teamBlock.setData((byte) 5);
		}
		else if (this.color == TeamColor.YELLOW)
		{
			teamBlock.setType(Material.CONCRETE);
			teamBlock.setData((byte) 4);
		}
		
		name = this.color.toString();
	}
	
	public void addPlayer(Player player)
	{
		if (currentAOP >= maxPlayers) return;
		if (!playersInTeam.contains(player)) 
		{
			GamePlayer gamePlayer = new GamePlayer(player);
			gamePlayer.health = 3;
			playersInTeam.add(player);
			currentAOP++;
		}	
		
	}
	
	public void removePlayer(Player player)
	{
		playersInTeam.remove(player);
		currentAOP--;
	}
	@Override
	public String toString() 
	{
		String str = "";
		if 		(this.color == TeamColor.BLUE) 		str = "§9BLUE";
		else if (this.color == TeamColor.RED) 		str = "§cRED";
		else if (this.color == TeamColor.GREEN) 	str = "§aGREEN";
		else if (this.color == TeamColor.YELLOW) 	str = "§eYELLOW";
		return str;
	}
}
