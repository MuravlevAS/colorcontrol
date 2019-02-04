package ru.sgk.colorcontrol.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.bukkit.configuration.file.FileConfiguration;

import ru.sgk.colorcontrol.MainColorControl;

public class MySQLMain 
{
	public static String host;
	public static String port = "3306";
	public static String database;
	public static String user;
	public static String password;
	
	public static Connection con;
	
	public FileConfiguration config;
	
	public MySQLMain()
	{
		config = MainColorControl.config;
		
		host 		= config.getString("database.host");
		user 		= config.getString("database.username");
		database 	= config.getString("database.dbname");
		password 	= config.getString("database.password");
		
		connect();
	}
	
	public static boolean isConnected()
	{
		return con != null;
	}
	public static void connect()
	{
		if (!isConnected())
		{
			try {
				con = DriverManager.getConnection("jdbc:mysql://"+ host+":"+port+"/"+database+"?autoReconection=true", user, password);
				MainColorControl.logger.info(MainColorControl.prefix + "§aПодключение успешно!");
			} catch (SQLException e) {
				MainColorControl.logger.info(MainColorControl.prefix + "§cОшибка при подключении к MySQL");
				e.printStackTrace();
			}
			
		}
	}
	public static void disconnect()
	{
		try {
			con.close();
			MainColorControl.logger.info(MainColorControl.prefix + "§aОтключён успешно!");
		} catch (SQLException e) {
			MainColorControl.logger.info(MainColorControl.prefix + "§cОшибка при отключении MySQL!");
			e.printStackTrace();
		}
	}
	public static PreparedStatement getStatement(String sql)
	{
		if(isConnected())
		{
			PreparedStatement ps;
			
			try {
				ps = con.prepareStatement(sql);
				return ps;
			} catch (SQLException e) {
				MainColorControl.logger.info(MainColorControl.prefix + "§cОшибка при извлечении данных!");
				e.printStackTrace();
			}
			
		}
		return null;
	}
	
	public static ResultSet getResult(String sql){
		if(isConnected()){
			PreparedStatement ps;
			ResultSet rs;
			try {
				ps = getStatement(sql);
				rs = ps.executeQuery();
				return rs;
			} catch (Exception e) {
				MainColorControl.logger.info(MainColorControl.prefix+"§cОшибка при получении результата || Error with getting result.");
			}
		}
		return null;
	}
}
