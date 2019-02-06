package ru.sgk.colorcontrol.game;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import ru.sgk.colorcontrol.MainColorControl;
import ru.sgk.colorcontrol.scoreboard.CCScoreboard;

public class Game
{
	public static boolean reloading = false;
	
	public List<GamePlayer> players;		// Сами игроки
	public List<GameTeam> teams;			// Команды в игре
	public int playersCount;				// Текущее количество игроков в игре
	public int timerIndex = -1;				// Индекс таймера. Нужен для остановки таймера (игры)
	public int maxPlayers;					// Максимальное количество игроков на карте
	public int minPlayers;					// Минимальное количество игроков (со скольки игроков начинается игра)
	public String gameName;					// Название игры
	
	public Location mapCenter;				// Центр карты
	
	public int deathmatchTime = 900;		// Время с которого начинается дезмач. В секундах
	public int deathmatchDuration = 300; 	// Продолжительность дезмач и всей игры. В секундах

	public List<GamePlayer> livingPlayers = null;
	public CCScoreboard scoreboard;
	
	private int timer = 0;					// Основной таймер игры (в секундах)
	private boolean started = false;   		// Запущена ли игра
	private boolean deathmatch = false;		
	private int countdownTimer;				// Таймер обратного отсчёта (до начала игры)
	public World world;
	
	private boolean stop;
	
	public boolean forcedStart = false;
	
	public List<GameCube> cubes = new ArrayList<GameCube>();
	public Game()
	{
		players = new ArrayList<GamePlayer>();
	}
	
	
	/********************/
	/** static methods **/
	/********************/

	public static void newGame(	int maxPlayers, int minPlayers, int deathmatchTime, int deathmatchDuration,
								List<GameTeam> teams, Location gameCenter)
	{
		MainColorControl.game = new Game();
		MainColorControl.game.maxPlayers = maxPlayers;
		MainColorControl.game.minPlayers = minPlayers;
		MainColorControl.game.deathmatchTime = deathmatchTime;
		MainColorControl.game.deathmatchDuration = deathmatchDuration;
		if (teams != null)
			MainColorControl.game.teams = teams;
		if (gameCenter != null)
			MainColorControl.game.mapCenter = gameCenter;
	}

	public static void newGame(	int maxPlayers, int minPlayers,
								Location gameCenter)
	{
		MainColorControl.game = new Game();
		MainColorControl.game.maxPlayers = maxPlayers;
		MainColorControl.game.minPlayers = minPlayers;
	}
	
	public static void newGame(	int maxPlayers,
			Location gameCenter)
	{
		MainColorControl.game = new Game();
		MainColorControl.game.maxPlayers = maxPlayers;
		MainColorControl.game.minPlayers = maxPlayers;
	}

	/*************/
	/** methods **/
	/*************/
	
	public GameTeam getTeam(String team)
	{
		for (GameTeam gameTeam : teams) 
		{
			if (gameTeam.color.toString().equalsIgnoreCase(team))
			{
				return gameTeam; 
			}
		}
		return null;
	}
	

	public void createCube(Location loc, String team, String id)
	{
		if (id.contains(":"))
		{
			
			String[] fullId = id.split(":");
			@SuppressWarnings("deprecation")
			ItemStack item = new ItemStack(Material.getMaterial(Integer.parseInt(fullId[0])), 1, Short.parseShort(fullId[1]));
			GameTeam gameTeam = getTeam(team);
			GameCube cube = new GameCube(loc, gameTeam, item);
			cubes.add(cube);
		}
		else
		{
			ItemStack item = new ItemStack(Material.matchMaterial(id));
			GameTeam gameTeam = getTeam(team);
			GameCube cube = new GameCube(loc, gameTeam, item);
			cubes.add(cube);
		}
		
	}
	
	public void createCube(Location loc, String id)
	{
		if (id.contains(":"))
		{
			
			String[] fullId = id.split(":");
			@SuppressWarnings("deprecation")
			ItemStack item = new ItemStack(Material.getMaterial(Integer.parseInt(fullId[0])), 1, Short.parseShort(fullId[1]));
			GameCube cube = new GameCube(loc, item);
			cubes.add(cube);
		}
		else
		{
			ItemStack item = new ItemStack(Material.matchMaterial(id));
			GameCube cube = new GameCube(loc, item);
			cubes.add(cube);
		}
		
	}
	
	public void addTeam(Location teamSpawn)
	{
		GameTeam team1 = new GameTeam(teamSpawn, 1);
		teams.add(team1);
		setTeamsHP();
	}
	
	void setTeamsHP()
	{
		for (GameTeam team : teams) 
		{
			teams.get(teams.indexOf(team)).teamSize = maxPlayers/teams.size();
		}
	}
	
	public void setTeamSpawn(Location loc, String color)
	{
		for (GameTeam team : teams)
		{
			if (team.name.equalsIgnoreCase(color.toLowerCase()))
			{
				teams.get(teams.indexOf(team)).teamSpawn = loc;
			}
		}
	}
	
	public void join(Player player)
	{
		if (playersCount == maxPlayers) return;
		GamePlayer p = new GamePlayer(player);
		players.add(p);
		playersCount++;
		
		if (playersCount == minPlayers)
			start();
	}

	public void leave(GamePlayer player)
	{
		if (playersCount > 0)
		{
			players.remove(player);
			playersCount--;
		}
		if (playersCount == 0 && !stop)
			stop();
	}
	
	public void leave(Player player)
	{
		leave(new GamePlayer(player));
	}
	
	@SuppressWarnings("deprecation")
	public void start()
	{
		if (playersCount >= minPlayers && playersCount < maxPlayers)
			countdownTimer = 10;
		else if (playersCount == maxPlayers) countdownTimer = 5;
		timerIndex = Bukkit.getServer().getScheduler().scheduleAsyncRepeatingTask(MainColorControl.plugin, new Runnable() 
		{
			public void run() 
			{
				if (timer >= Integer.MAX_VALUE) timer = 0;
				if (!started && playersCount < minPlayers && !forcedStart)
				{
					MainColorControl.debugMessage("§cНе набрано минимальное количество игроков для начала игры");
					broadcastMessage("§cНе набрано минимальное количество игроков для начала игры");
					stop();
				}
				if (playersCount <= 1)
				{
					MainColorControl.debugMessage("§cВ игре остался только один игрок");
					if (started)
					{
						broadcastMessage("Победила команда: §l" + players.get(0).team.toString());
						MainColorControl.debugMessage("Победила команда: §l" + players.get(0).team.toString());
					}
					stop();
				}
				
				if (playersCount == maxPlayers || (forcedStart && playersCount > 0))
				{
					if (countdownTimer > 5)
					{
						countdownTimer = 5;
					}
				}
				
				if (countdownTimer > 0) countdownTimer--;
				else
				{
					if (!started) started = true;
				}
				
				if (started)
				{
					if (timer >= deathmatchTime) if (!deathmatch)
					{
						broadcastMessage("§aПришло время дезматча");
						deathmatch = true;
					}
					if (deathmatch)
					{
						if (world == null) world = players.get(0).player.getWorld();
						MainColorControl.debugMessage("§aИгра закончена");
						
						world.getWorldBorder().setCenter(mapCenter);
						world.getWorldBorder().setSize(0,(deathmatchTime + deathmatchDuration) - timer);
						
						// Конец игры
						if (teams.size() == 1)
						{
							MainColorControl.debugMessage("§aИгра закончена");
							stop();
							reloadWorld();
						}
					}
					timer++;					
				}
			}
		}, 0, 20);
		
	}
	public static void reloadWorld()
	{
		for (Chunk chunk : Bukkit.getServer().getWorld("world").getLoadedChunks())
		{
			for (Player player : Bukkit.getOnlinePlayers())
			{
				player.kickPlayer("Карта перезагружается, подождите немного :)");
			}
			chunk.unload(false);
			MainColorControl.debugMessage("chung unloaded!");
		}
		MainColorControl.debugMessage("§cWorld reloaded.");
	}
	public void initLivingPlayers()
	{
		if (livingPlayers == null || livingPlayers.size() == 0)
			livingPlayers = players;
	}
	
	public List<GamePlayer> livingPlayers()
	{
		List<GamePlayer> plist = new ArrayList<GamePlayer>();
		for (GamePlayer player : players)
		{
			if (player.living()) plist.add(player);
		}
		return plist;
	}
	
	public void killPlayer(GamePlayer killer, GamePlayer player)
	{
		
	}
	
	public void broadcastMessage(String message)
	{
		for (GamePlayer player : players)
		{
			player.player.sendMessage(MainColorControl.prefix + message);
		}
	}
	
	public void setPlayerTeam(Player player, String color)
	{
		for (GameTeam team : teams) 
		{
			if (team.color == TeamColor.valueOf(color.toUpperCase()))
			{
				team.addPlayer(player);
			}
		}
		MainColorControl.logger.info(MainColorControl.prefix + "§cТакой команды (team) не существует :(");
	}
	
	public void stop()
	{
		if (stop) return;
		Bukkit.getServer().getScheduler().cancelTask(timerIndex);
		timerIndex = -1;
		deathmatch = false;
		timer = 0;
		livingPlayers = players;
		if (started)
			reloadWorld();
		started = false;
		stop = true;
	}
	
	public List<Player> gamePlayersToPlayers()
	{
		List<Player> players = new ArrayList<Player>();
		for (GamePlayer player : this.players)
		{
			players.add(player.player);
		}
		return players;
	}
	
	public static void getFromConfig()
	{
		FileConfiguration config = MainColorControl.initConfiguration();
		int maxPlayers = config.getInt("game.max-players");
		int minPlayers = config.getInt("game.min-players");
		int deathmatchTime = config.getInt("game.dm-time");
		int deathmatchDuration = config.getInt("game.dm-dur");
		Location gameCenter = (Location)config.get("game.center");
		List<GameTeam> teams = new ArrayList<GameTeam>();
		List<GameCube> cubes = new ArrayList<GameCube>();		
		for (Object gameTeam : config.getList("game.teams")) 
		{
			teams.add((GameTeam) gameTeam);
		}
		for (Object gameCube : config.getList("game.cubes")) 
		{
			cubes.add((GameCube)gameCube);
		}
		newGame(maxPlayers, minPlayers, deathmatchTime, deathmatchDuration, teams, gameCenter);
		
		MainColorControl.game.cubes = cubes;
		
	}
	
	public void saveInConfig()
	{
		MainColorControl.config.getList("");
		MainColorControl.config.set("game.name", gameName);
		MainColorControl.config.set("game.max-players", maxPlayers);
		MainColorControl.config.set("game.min-players", minPlayers);
		MainColorControl.config.set("game.center", mapCenter);
		MainColorControl.config.set("game.dm-time", deathmatchTime);
		MainColorControl.config.set("game.dm-dur", deathmatchDuration);
		MainColorControl.config.set("game.teams", teams);
		MainColorControl.config.set("game.cubes", cubes);
		MainColorControl.saveConfiguration();
	}
}
