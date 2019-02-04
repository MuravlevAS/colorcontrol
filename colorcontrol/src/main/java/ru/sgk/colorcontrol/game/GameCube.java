package ru.sgk.colorcontrol.game;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import io.netty.handler.codec.AsciiHeadersEncoder.NewlineType;
import ru.sgk.colorcontrol.MainColorControl;

public class GameCube 
{
	public GameTeam cubeTeam;
	
	public int rotateInterval;
	public List<Block>[] facet;		// Все грани куба
	public boolean[] activeFacets;	// Активная грань - грань куба, находящаяся сверху
	public int activeFacet = 1;		// Индекс активной грани			
	public Block[] edge;			// Все рёбра куба
	public ItemStack item;			// Предмет, выдаваемый кубом			
	public boolean isBase = false;	// Является ли данный куб чьей-то базой
	public int x, z;
	
	public Location min, max;
	public static int y = 50;
	
	public List<GamePlayer> players = new ArrayList<GamePlayer>(); // 
	public GameCube() 
	{
		this.rotateInterval = 5*60;
		this.facet = new List[6];
		for (int i = 0; i < 6; i++) {
			this.facet[i] = new ArrayList<Block>();
		}
		this.activeFacets = new boolean[6];
		this.edge = new Block[44];
	}

	public GameCube(Location PlayerLoc, GameTeam team, ItemStack item)
	{
		this.rotateInterval = 5*60;
		this.facet = new List[6];
		for (int i = 0; i < 6; i++) {
			this.facet[i] = new ArrayList<Block>();
		}
		this.activeFacets = new boolean[6];
		setActiveFacet(0);
		this.edge = new Block[44];
		
		this.x = (int) PlayerLoc.getX();
		this.z = (int) PlayerLoc.getZ();
		
		this.cubeTeam = team;
	}
	public GameCube(Location PlayerLoc, ItemStack item)
	{
		this.rotateInterval = 5*60;
		this.facet = new List[6];
		for (int i = 0; i < 6; i++) {
			this.facet[i] = new ArrayList<Block>();
		}
		this.activeFacets = new boolean[6];
		for (int i = 0; i < activeFacets.length; i++) 
		{
			activeFacets[i] = false;
		}
		this.edge = new Block[44];

		this.x = (int) PlayerLoc.getX();
		this.z = (int) PlayerLoc.getZ();
		
		cubeTeam = null;
	}

	public void setActiveFacet(int index)
	{
		for (int i = 0; i < activeFacets.length; i++) 
		{
			activeFacets[i] = false;
		}
		activeFacets[index] = true;
	}
	
	public void setBase(boolean b)
	{
		isBase = b;
	}
	
	private void place(Location loc)
	{
		min = loc;
		max = loc;
		min.setX((int)(loc.getX()) - 2);
		min.setY((int)(loc.getY()) - 5);
		min.setZ((int)(loc.getZ()) - 2);
		max.setX((int)(loc.getX()) + 2);
		max.setY((int)(loc.getY()) - 1);
		max.setZ((int)(loc.getZ()) + 2);
		
		if (!isRegionEmpty(min, max))
		{
			MainColorControl.debugMessage("§cВ области уже есть блоки. Нужно поставить куб в другое место");
			return;
		}
		
		
		
	}
	
	private boolean isRegionEmpty(Location min, Location max)
	{
		Location loc1 = new Location(min.getWorld(), min.getX(), min.getY(), min.getZ());
		for (int i = min.getBlockX(); i <= max.getBlockX(); i++)
		{
			for (int j = min.getBlockZ(); j <= max.getBlockZ(); j++)
			{
				for (int h = min.getBlockY(); h <= max.getBlockY(); h++)
				{
					loc1 = new Location(min.getWorld(), i, h, j);
					if (!loc1.getBlock().isEmpty())
					{
						return false;
					}
				}
			}
		}
		return true;
	}
	
	public void rotate()
	{
		
	}
}
