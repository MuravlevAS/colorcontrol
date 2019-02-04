package ru.sgk.colorcontrol.scoreboard;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import ru.sgk.colorcontrol.MainColorControl;
import ru.sgk.colorcontrol.game.GameTeam;

public class CCScoreboard 
{
	public static int timer = 0;
	
	public static ScoreboardManager manager = Bukkit.getScoreboardManager();
	public Scoreboard board = manager.getNewScoreboard();
	public Objective objective;
	
	@SuppressWarnings("deprecation")
	public void setScoreboard(final List<Player> players, final List<GameTeam> teams)
	{
		timer = Bukkit.getServer().getScheduler().scheduleAsyncRepeatingTask(MainColorControl.plugin, new Runnable() 
		{
			public void run() 
			{
				for (Player player : players) 
				{
					if (board.getObjective("colorscontrol") == null)
						objective = board.registerNewObjective("colorscontrol", "dummy");
					objective = board.getObjective("colorscontrol");
					objective.setDisplaySlot(DisplaySlot.SIDEBAR);
					objective.setDisplayName("§b§lColorsControl");
					
					
					
					player.setScoreboard(board);
				}
			}
		}, 0, 10);
	}
	public void removeScoreboard()
	{
		Bukkit.getServer().getScheduler().cancelTask(timer);
	}
}
