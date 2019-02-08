package ru.sgk.colorcontrol.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ru.sgk.colorcontrol.MainColorControl;
import ru.sgk.colorcontrol.game.Game;

public class ColorControlCommand implements CommandExecutor
{
	// cc join									- Тут всё ясно (join будет уже в отдельном плагине для лобби, это я сам сделаю)      [Y]
	//// cc leave								- -------------                                                                      [X]
	//// cc create <name> <maxPlayers>			- -------------                                                                      [X]
	// cc setteams <2|4>						- Устанавливает количество команд 2 или 4                                            [X]
	// cc setcenter								- Устанавливает центр поля                                                           [X]
	// cc setteamhealth	<health>				- устанавливает количество жизней в команде                                          [X]
	// cc setdeathmatch <timeInMinutes>			- Устанавливает время с которого начинается дезматч                                  [X]
	// cc setdeathmatchduration <timeInMinutes> - Устанавливает длительность дезматча                                                [X]
	// Максимальная продолжительность игры равна сумме аргументов двух предыдущих команд
	// cc maxplayers <amount> 					- количество игроков с которого начинается игра                                      [X]
	// cc minplayers <amount> 					- максимальное количество игроков                                                    [X]
	// cc start									- Тут всё ясно                                                                       [X]
	// cc stop									- -------------                                                                      [X]
	
	private boolean gameServer;
	
	public boolean isDev(CommandSender sender)
	{
		return sender.hasPermission("colorcontrol.dev");
	}
	
	public boolean isAdmin(CommandSender sender)
	{
		return sender.hasPermission("colorcontrol.admin") || isDev(sender);
	}
	public boolean hasPermission(CommandSender sender, String perm)
	{
		return sender.hasPermission(perm) || isDev(sender);
	}
	public ColorControlCommand() 
	{
		gameServer = MainColorControl.config.getBoolean("gameserver");
	}
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) 
	{
		if (command.getName().equalsIgnoreCase("colorcontrol"))
		{
			if (args.length >= 1)
			{
				if (args[0].equalsIgnoreCase("start"))
				{
					MainColorControl.debugMessage("§rИгрок ввёл команду §сstart");
					if (isAdmin(sender))
					{
						MainColorControl.debugMessage("§rУ него есть на это права");
						if (gameServer)
						{
							MainColorControl.debugMessage("§rИгра была принудительно запущена");	
							MainColorControl.game.start();
						}
						else
						{
							sendMessage(sender, "§cСервер не является игровым");
							MainColorControl.debugMessage("§cСервер не является игровым");
						}
					} 
					else
					{
						sendMessage(sender, "§cУ вас нет прав");
						MainColorControl.debugMessage("§cУ игрока нет прав");
					}
				}
				else if (args[0].equalsIgnoreCase("stop"))
				{
					MainColorControl.debugMessage("§rИгрок ввёл команду §сstop");
					if (isAdmin(sender))
					{
						MainColorControl.debugMessage("§rУ него есть на это права");
						if (gameServer)
						{
							MainColorControl.debugMessage("§rИгра была принудительно остановлена");
							MainColorControl.game.stop();
						}
						else
						{
							sendMessage(sender, "§cСервер не является игровым");
							MainColorControl.debugMessage("§cСервер не является игровым");
						}
					} 
					else
					{
						sendMessage(sender, "§cУ вас нет прав");
						MainColorControl.debugMessage("§cУ игрока нет прав");
					}
				
				}
				else if (args[0].equalsIgnoreCase("leave"))
				{
					if (!(sender instanceof Player)) {
						sendMessage(sender, "§cКоманда только для игроков");
						return true;
					}
					MainColorControl.debugMessage("§rИгрок ввёл команду §cleave");
					if (!gameServer) return true;
					Player player = (Player)sender;
					MainColorControl.game.leave(player);
				}
				// odmen commands
				else if (args[0].equalsIgnoreCase("create"))
				{
					if (!isDev(sender))
					{
						sendMessage(sender, "§cУ вас недостаточно прав!");
						return true;
					}
					if (!(sender instanceof Player)) {
						sendMessage(sender, "§cКоманда только для игроков!");
						return true;
					}
					if (!gameServer)
					{
						sendMessage(sender, "§cСервер не является игровым!");
						return true;
					}
					if (MainColorControl.game != null)
					{
						sendMessage(sender, "§cИгра уже создана!");
						return true;
					}
					Player player = (Player)sender;
					if (args.length == 3)
					{
						try {
							int maxPlayers = Integer.parseInt(args[2]);
							Game.newGame(args[1], maxPlayers, player.getLocation());
							MainColorControl.game.saveInConfig();
						}catch(Exception e)
						{
							sendMessage(sender, "§cНеправильно введены аргументы!");
							sendMessage(sender, "§cИспользование команды: /create <name> <maxPlayers>");
						}
					}
					else
					{
						sendMessage(sender, "§cНеправильно введены аргументы");
						sendMessage(sender, "§cИспользование команды: /create <name> <maxPlayers>");
					}
				}
				else if (args[0].equalsIgnoreCase("setteams"))
				{
					if (!isDev(sender))
					{
						sendMessage(sender, "§cУ вас недостаточно прав");
						return true;
					}
					if (!gameServer)
					{
						sendMessage(sender, "§cСервер не является игровым");
						return true;
					}
					if (!(sender instanceof Player)) {
						sendMessage(sender, "§cКоманда только для игроков");
						return true;
					}
					if (args.length == 2 && (args[1] == "1" || args[1] == "2"))
					{
						
					}
				}
				else if (args[0].equalsIgnoreCase("teams"))
				{
					if (!isDev(sender))
					{
						sendMessage(sender, "§cУ вас недостаточно прав");
						return true;
					}
					if (!gameServer)
					{
						sendMessage(sender, "§cСервер не является игровым");
						return true;
					}
					
				}
				else if (args[0].equalsIgnoreCase("setcube"))
				{
					if (!(sender instanceof Player)) return true;
					Player player = (Player) sender;
					if (args.length == 2)
					{
						MainColorControl.debugMessage("§rsetcube is correct");
						MainColorControl.game.createCube(player.getLocation(), args[1]);
					}
					else
					{
						sendMessage(sender, "§cИспользование: /colorcontrol setcube <id> [team]");
					}
				}
				else if (args[0].equalsIgnoreCase("rotatecube")) 
				{
					if (gameServer)
						if (MainColorControl.game != null)
						{
							MainColorControl.game.rotateCubes();
						}
				}
				else if (args[0].equalsIgnoreCase("help"))
				{
					sendHelp(sender);
				}
				else if (args[0].equalsIgnoreCase("setteamhealth")){
					if(args.length == 1) {
						sendMessage(sender, "§cИспользование: /colorcontrol setteamhealth <health>");
					}else {
						MainColorControl.debugMessage("§rИгрок изменил хп командам");
					}
				}else if(args[0].equalsIgnoreCase("setcenter")) {
					MainColorControl.debugMessage("§rИгрок изменил центр");
					
				}else if(args[0].equalsIgnoreCase("setdeathmatch")) {
					if(args.length == 1) {
						sendMessage(sender, "§cИспользование: /colorcontrol setdeathmatch <timeInMinutes>");
					}else {
						MainColorControl.debugMessage("§rИгрок изменил время до ДМ");
					}
				}else if(args[0].equalsIgnoreCase("setdeathmatchduration")) {
					if(args.length == 1) {
						sendMessage(sender, "§cИспользование: /colorcontrol setdeathmatchduration <timeInMinutes>");
					}else {
						MainColorControl.debugMessage("§rИгрок изменил длительность ДМ");
					}
				}else if(args[0].equalsIgnoreCase("maxplayers")) {
					if(args.length == 1) {
						sendMessage(sender, "§cИспользование: /colorcontrol maxplayers <players>");
					}else {
						MainColorControl.debugMessage("§rИгрок изменил maxplayers");
					}
				}else if(args[0].equalsIgnoreCase("minplayers")) {
					if(args.length == 1) {
						sendMessage(sender, "§cИспользование: /colorcontrol minplayers <players>");
					}else {
						MainColorControl.debugMessage("§rИгрок изменил minplayers");
					}
				}else {
					sendMessage(sender, "§cНеизвестная команда. Введите /colorcontrol help для просморта списка команд.");
				}
			}
		}
		return true;
	}
	private void sendMessage(CommandSender sender, String message)
	{
		sender.sendMessage(MainColorControl.prefix + message);
	}
	private void sendHelp(CommandSender sender)
	{
		sendMessage(sender, "§eСписок команд:");
		sendMessage(sender, "§e/cc leave §f- покинуть игру");
		if (hasPermission(sender, "colorcontrol.dev"))
		{
			// команды, доступные с правами perm1
		
			sendMessage(sender, "§c/cc create <название> <макс.игроков> §f- создать арену");
			sendMessage(sender, "§c/cc setteams <2|4> §f- поставить кол-во команд");
			sendMessage(sender, "§c/cc setcenter §f- поставить центр");
			sendMessage(sender, "§c/cc setteamhealth <health> §f- поставить хп команде");
			sendMessage(sender, "§c/cc setdeathmatch <timeInMinutes> §f- поставить время до ДМ");
			sendMessage(sender, "§c/cc setdeathmatchduration <timeInMinutes> §f- поставить длительность ДМ");
			sendMessage(sender, "§c/cc maxplayers <players> §f- поставить макс.игроков");
			sendMessage(sender, "§c/cc minplayers <players> §f- поставить мин.игроков");
			//sendMessage(sender, "§c/cc  §f-");
		}
		if (hasPermission(sender, "perm2"))
		{
			// команды, доступные с правами perm2
			sendMessage(sender, "§6/cc start §f- запустить игру");
			sendMessage(sender, "§6/cc stop §f- остановить игру");
		}
		if (hasPermission(sender, "perm3"))
		{
			// команды, доступные с правами perm3
			sender.sendMessage("§e...");
		}
		if (hasPermission(sender, "permN"))
		{
			// команды, доступные с правами permN
			sender.sendMessage("§e...");
		}
	}
}
