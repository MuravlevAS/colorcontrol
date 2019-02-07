package ru.sgk.colorcontrol.game;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

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
	@SuppressWarnings("unchecked")
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

	@SuppressWarnings("unchecked")
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

		PlayerLoc.setY(y);
		place(PlayerLoc, team);
		
		this.cubeTeam = team;
	}
	@SuppressWarnings("unchecked")
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
		
		PlayerLoc.setY(y);
		place(PlayerLoc);
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

	private void place(Location loc, GameTeam team)
	{
		
	}
	
	@SuppressWarnings("deprecation")
	private void place(Location loc)
	{
		min = new Location(loc.getWorld(), x - 2, y - 5, z - 3);
		max = new Location(loc.getWorld(), x + 2, y - 1, z + 1);
//		min.setX((int)(loc.getX()) - 2);
//		min.setY((int)(loc.getY()) - 5);
//		min.setZ((int)(loc.getZ()) - 2);
//		max.setX((int)(loc.getX()) + 2);
//		max.setY((int)(loc.getY()) - 1);
//		max.setZ((int)(loc.getZ()) + 2);
		
		MainColorControl.debugMessage("§cmin x: " + min.getX());
		MainColorControl.debugMessage("§cmax x: " + max.getX());
		MainColorControl.debugMessage("§cmin y: " + min.getY());
		MainColorControl.debugMessage("§cmax y: " + max.getY());
		MainColorControl.debugMessage("§cmin z: " + min.getZ());
		MainColorControl.debugMessage("§cmax z: " + max.getZ());
		
		if (!isRegionEmpty(min, max))
		{
			MainColorControl.debugMessage("§cВ области уже есть блоки. Нужно поставить куб в другое место");
			return;
		}

		Location placeLoc = min;
		for (int x = min.getBlockX(); x <= max.getBlockX(); x++) 
		{
			MainColorControl.debugMessage("§aУстановка новой координаты x: " + x);
			for (int y = min.getBlockY(); y <= max.getBlockY(); y++) 
			{
				MainColorControl.debugMessage("§aУстановка новой координаты y: " + y);
				for (int z = min.getBlockZ(); z <= max.getBlockZ(); z++) 
				{
					MainColorControl.debugMessage("§aУстановка новой координаты z: " + z);
					placeLoc = new Location(loc.getWorld(), x, y, z);

					MainColorControl.debugMessage("§aСтавим блок на координаты §c" + placeLoc.getX() + "" + placeLoc.getY() + "" + placeLoc.getZ());
					if (((x == min.getBlockX() || x == max.getBlockX()) && (y == min.getBlockY() || y == max.getBlockY())) ||
						((z == min.getBlockZ() || z == max.getBlockZ()) && (y == min.getBlockY() || y == max.getBlockY())) ||
						((x == min.getBlockX() || x == max.getBlockX()) && (z == min.getBlockZ() || z == max.getBlockZ())))
					{
						MainColorControl.debugMessage("§aУстановка рёбер куба");
						placeLoc.getBlock().setType(Material.CONCRETE);
						placeLoc.getBlock().setData((byte)15);
					}
					else
					{
						MainColorControl.debugMessage("§aУстановка граней куба");
						placeLoc.getBlock().setType(Material.CONCRETE);
					}
					
					/*
					 * Координаты граней куба, относительно координат самого куба
					 * 0 - верх
					 * 1 - низ
					 * 2 - лево
					 * 3 - право
					 * 4 - задняя сторона
					 * 5 - лицевая сторона
					 * Лицевую и заднюю стороны определяет ось Ox. Координата X задней стороны меньше чем координата X лицевой
					 * Левую и правую стороны определяет ось Oz. Координата Z левой стороны меньше чем координата Z правой
					 * Верх и низ определяет ось Oy. Координата Y верхней стороны всегда больше, чем координата Y нижней
					 */
					
					if (y == max.getBlockY() && (x != min.getBlockX() && x != max.getBlockX()) && (z != min.getBlockZ() && z != max.getBlockZ()))
					{
						facet[0].add(placeLoc.getBlock());
					}
					else if (y == min.getBlockY() && (x != min.getBlockX() && x != max.getBlockX()) && (z != min.getBlockZ() && z != max.getBlockZ()))
					{
						facet[1].add(placeLoc.getBlock());
					}
					else if (z == min.getBlockZ() && (y != min.getBlockY() && y != max.getBlockY()) && (x != min.getBlockX() && x != max.getBlockX()))
					{
						facet[2].add(placeLoc.getBlock());
					}
					else if (z == max.getBlockZ() && (y != min.getBlockY() && y != max.getBlockY()) && (x != min.getBlockX() && x != max.getBlockX()))
					{
						facet[3].add(placeLoc.getBlock());
					}
					else if (x == min.getBlockZ() && (y != min.getBlockY() && y != max.getBlockY()) && (z != min.getBlockZ() && z != max.getBlockZ()))
					{
						facet[4].add(placeLoc.getBlock());
					}
					else if (x == max.getBlockZ() && (y != min.getBlockY() && y != max.getBlockY()) && (z != min.getBlockZ() && z != max.getBlockZ()))
					{
						facet[5].add(placeLoc.getBlock());
					}
					activeFacet = 0;
				}
			}
		}
		MainColorControl.debugMessage("§aКуб поставлен");
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
						MainColorControl.debugMessage("§rКуб не может быть размещён здесь, т.к область занята");
						return false;
					}
				}
			}
		}
		MainColorControl.debugMessage("§aОбласть свободна, установка куба...");
		return true;
	}
	
	private void swapFacets(int oldFacet, int newFacet) 
	{
		Block tmpBlock;
		for (int i = 0; i < facet[oldFacet].size(); i++) 
		{
			tmpBlock = facet[newFacet].get(i);
			facet[newFacet].set(i, facet[oldFacet].get(i));
			facet[newFacet].set(i, tmpBlock);
		}
	}
	
	/*
	 * Координаты граней куба, относительно координат самого куба
	 * 0 - верх
	 * 1 - низ
	 * 2 - лево
	 * 3 - право
	 * 4 - задняя сторона
	 * 5 - лицевая сторона
	 * Лицевую и заднюю стороны определяет ось Ox. Координата X задней стороны меньше чем координата X лицевой
	 * Левую и правую стороны определяет ось Oz. Координата Z левой стороны меньше чем координата Z правой
	 * Верх и низ определяет ось Oy. Координата Y верхней стороны всегда больше, чем координата Y нижней
	 */
	
	public void rotate()
	{
		if (activeFacet == 0)
		{
			swapFacets(0, 3);
			swapFacets(0, 1);
			swapFacets(0, 2);
			activeFacet = 3;
			
		}
		else if (activeFacet == 3)
		{
			activeFacet = 5;
			swapFacets(3, 5);
			swapFacets(3, 2);
			swapFacets(3, 4);
		}
		else if (activeFacet == 5)
		{
			activeFacet = 1;
			swapFacets(5, 1);
			swapFacets(5, 4);
			swapFacets(5, 0);
		}
		else if (activeFacet == 1)
		{
			activeFacet = 2;
			swapFacets(1, 2);
			swapFacets(1, 0);
			swapFacets(1, 3);
		}
		else if (activeFacet == 2)
		{
			activeFacet = 4;
			swapFacets(2, 4);
			swapFacets(2, 3);
			swapFacets(2, 5);
		}
		else if (activeFacet == 4)
		{
			activeFacet = 0;
			swapFacets(4, 0);
			swapFacets(4, 5);
			swapFacets(4, 1);
		}
	}
}
