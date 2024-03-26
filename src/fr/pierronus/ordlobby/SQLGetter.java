package fr.pierronus.ordlobby;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class SQLGetter {

	private Main plugin;
	private static final SimpleDateFormat sdf3 = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
	
	public SQLGetter(Main plugin) {
		this.plugin = plugin;
	}
	
	public void createTable() {
		PreparedStatement ps;
		PreparedStatement ps2;
		try {
			ps = plugin.SQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS ordcraft_joueurs " 
		+ "(USERNAME VARCHAR(100),UUID VARCHAR(100),DATE_REG VARCHAR(50),IP VARCHAR(255),PRIMARY KEY (USERNAME))");
			ps2 = plugin.SQL.getConnection().prepareStatement("CREATE TABLE IF NOT EXISTS points " 
					+ "(USERNAME VARCHAR(100),UUID VARCHAR(100),POINTS INT(11),PRIMARY KEY (USERNAME))");
		    ps.executeUpdate();
		    ps2.executeUpdate();
		} catch (SQLException e){
			e.printStackTrace();
		}
	}
	
	public void createPlayer(Player player) {
		try {
			UUID uuid = player.getUniqueId();
			if(!exists(uuid)) {
				PreparedStatement ps2 = plugin.SQL.getConnection().prepareStatement("INSERT IGNORE INTO ordcraft_joueurs" +
			" (USERNAME,UUID,DATE_REG,IP) VALUES (?,?,?,?)");
				String ip = String.valueOf(player.getAddress().getHostString());
				Timestamp timestamp = new Timestamp(System.currentTimeMillis());
				ps2.setString(1, player.getName());
				ps2.setString(2, uuid.toString());
				ps2.setString(3, sdf3.format(timestamp));
				ps2.setString(4, ip);
				
				ps2.executeUpdate();
				return;
			}else {
				return;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void createPlayerPoints(Player player) {
		try {
			UUID uuid = player.getUniqueId();
			if(!existspoints(uuid)) {
				PreparedStatement ps2 = plugin.SQL.getConnection().prepareStatement("INSERT IGNORE INTO points" +
			" (USERNAME,UUID,POINTS) VALUES (?,?,?)");
				ps2.setString(1, player.getName());
				ps2.setString(2, uuid.toString());
				ps2.setInt(3, 0);
				
				ps2.executeUpdate();
				return;
			}else {
				return;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public boolean exists(UUID uuid) {
		try {
			PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT * FROM ordcraft_joueurs WHERE UUID=?");
			ps.setString(1, uuid.toString());
			ResultSet results = ps.executeQuery();
			if(results.next()) {
				return true;
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public boolean existspoints(UUID uuid) {
		try {
			PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT * FROM points WHERE UUID=?");
			ps.setString(1, uuid.toString());
			ResultSet results = ps.executeQuery();
			if(results.next()) {
				return true;
			}
			return false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void setIp(UUID uuid) {
		try {
			Player player = Bukkit.getPlayer(uuid);
			String ip = String.valueOf(player.getAddress().getHostString());
		PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("UPDATE ordcraft_joueurs SET IP=? WHERE UUID=?");
		ps.setString(1, ip);
		ps.setString(2, uuid.toString());
		ps.executeUpdate();
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int getPoints(UUID uuid) {
		try {
			PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("SELECT POINTS FROM points WHERE UUID=?");
			ps.setString(1, uuid.toString());
			ResultSet rs = ps.executeQuery();
			int points = 0;
			if(rs.next()) {
				points = rs.getInt("POINTS");
				return points;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
		
	}
	
	public void addPoints(UUID uuid,int points) {
		try {
		PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("UPDATE points SET POINTS=? WHERE UUID=?");
		ps.setInt(1, (getPoints(uuid) + points));
		ps.setString(2, uuid.toString());
		ps.executeUpdate();
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void removePoints(UUID uuid,int points) {
		try {
		PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("UPDATE points SET POINTS=? WHERE UUID=?");
		ps.setInt(1, (getPoints(uuid) - points));
		ps.setString(2, uuid.toString());
		ps.executeUpdate();
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void resetPoints(UUID uuid) {
		try {
		PreparedStatement ps = plugin.SQL.getConnection().prepareStatement("UPDATE points SET POINTS=0 WHERE UUID=?");
		ps.setString(1, uuid.toString());
		ps.executeUpdate();
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
