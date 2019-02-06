package ru.sgk.colorcontrol.cmd;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import ru.sgk.colorcontrol.MainColorControl;
import ru.sgk.colorcontrol.game.Game;

public class ColorControlCommand implements CommandExecutor
{
	// cc join									- Тут всё ясно
	// cc leave									- -------------
	// cc create <name> <maxPlayers>			- -------------
	// cc setteams <2|4>						- Устанавливает количество команд 2 или 4
	// cc setteamhealth							- устанавливает количество жизней в команде
	// cc setdeathmatch <timeInMinutes>			- Устанавливает время с которого начинается дезматч
	// cc setdeathmatchduration <timeInMinutes> - Устанавливает длительность дезматча
	// Максимальная продолжительность игры равна сумме аргументов двух предыдущих команд
	// cc maxplayers <amount> 					- количество игроков с которого начинается игра
	// cc minplayers <amount> 					- максимальное количество игроков
	// cc start									- Тут всё ясно
	// cc stop									- -------------
	
	private boolean gameServer;
	
	public boolean isDev(CommandSender sender)
	{
		return sender.hasPermission("colorcontrol.dev");
	}
	
	public boolean isAdmin(CommandSender sender)
	{
		return sender.hasPermission("colorcontrol.admin");
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
					if (MainColorControl.game == null)
					{
						sendMessage(sender, "§cИгра уже создана!");
						return true;
					}
					Player player = (Player)sender;
					if (args.length == 3)
					{
						try {
							int maxPlayers = Integer.parseInt(args[2]);
							Game.newGame(maxPlayers, player.getLocation());
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
				else if (args[0].equalsIgnoreCase("help"))
				{
					sendHelp(sender);
				}
				else
				{
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
		sender.sendMessage("§e");
	}
//	private void sendMessage(Player sender, String message)
//	{
//		sender.sendMessage(MainColorControl.prefix + message);
//	}
}
