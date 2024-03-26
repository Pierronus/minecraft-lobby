package fr.pierronus.ordlobby;
import java.sql.SQLException;
import java.text.DecimalFormat;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin implements Listener, CommandExecutor{
	
	private static DecimalFormat df = new DecimalFormat("0.00");

		public MySQL SQL;
		public SQLGetter data;
		
		ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
				
		@Override
		public void onEnable() {
			this.SQL = new MySQL();
			this.data = new SQLGetter(this);
			
			try {
				SQL.connect();
				
			} catch (ClassNotFoundException | SQLException e) {
				e.printStackTrace();
				Bukkit.getLogger().info("BDD non connectee");
			}
			if(SQL.isConnected()) {
				Bukkit.getLogger().info("BDD connectee");
				data.createTable();
			}
			getServer().getPluginManager().registerEvents(this, this);
			System.out.println("Le plugin GCA s'est bien active");
			
		}
		
		@Override
		public void onDisable() {
			System.out.println("Le plugin GCA s'est bien desactive");
			SQL.disconnect();
		}
		
		@EventHandler
		public void onJoin(PlayerJoinEvent event) {
			Player player = event.getPlayer();
			data.createPlayer(player);
			data.createPlayerPoints(player);
		}
		
		@Override
		public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
			
			if(label.equalsIgnoreCase("points")) {
			if(args.length == 0) {
			if(sender instanceof Player) {
				Player player = (Player) sender;
			if(player.hasPermission("ord.points")) {
				player.sendMessage("§6§lVos OrdCoins : §6" + String.valueOf(data.getPoints(player.getUniqueId())) + " OrdCoins");
				}
			}
			}
			if(args.length == 1) {
				if(sender instanceof Player) {
					Player player = (Player) sender;
				if(player.hasPermission("ord.points.other")) {
					String targetname = args[0];
					if(Bukkit.getPlayer(targetname) != null) {
						if(targetname != player.getName()) {
						Player target = Bukkit.getPlayer(targetname);
						player.sendMessage("§6§lOrdCoins de " + target.getName() + " : §6" + String.valueOf(data.getPoints(target.getUniqueId())) + " OrdCoins");
						
					}
					}
					else {
						player.sendMessage("§6§l[GCA] §f§cCe joueur n'existe pas");
					}
				
				}
			}else {
				
				String targetname = args[0];
				if(Bukkit.getPlayer(targetname) != null) {
					Player target = Bukkit.getPlayer(targetname);
					String pts = String.valueOf(data.getPoints(target.getUniqueId()));
					System.out.println("OrdCoins de " + target.getName() + " : " + pts + " OrdCoins");
					
				
				}
				else {
					System.out.println("Ce joueur n'existe pas (" + targetname + ")");
				}
				
				
			}
			}
			
			return false;
			}
			if(label.equalsIgnoreCase("addpoints")) {
				if(args.length == 2) {
					if(sender instanceof Player) {
						Player player = (Player) sender;
					if(player.getName().equals("Scrym6") || player.getName().equals("Pierronus")) {
						String targetname = args[0];
						int points = Integer.valueOf(args[1]);
						if(Bukkit.getPlayer(targetname) != null) {
							if(targetname != player.getName()) {
							Player target = Bukkit.getPlayer(targetname);
							data.addPoints(target.getUniqueId(), points);
							String nowpts = String.valueOf(data.getPoints(target.getUniqueId())); 
							player.sendMessage("§cAjouté " + points + " OrdCoins à " + target.getName() + "\n§cIl a maintenant " + nowpts);
							
						}
						}
						else {
							player.sendMessage("§6§l[GCA] §f§cCe joueur n'existe pas");
						}
					}
				}else {
					String targetname = args[0];
					int points = Integer.valueOf(args[1]);
					if(Bukkit.getPlayer(targetname) != null) {
						Player target = Bukkit.getPlayer(targetname);
						data.addPoints(target.getUniqueId(), points);
						String nowpts = String.valueOf(data.getPoints(target.getUniqueId())); 
						System.out.println("Ajouté " + points + " OrdCoins à " + target.getName() + ". Il a maintenant " + nowpts);
						
					
					}
					else {
						System.out.println("Ce joueur n'existe pas (" + targetname + ")");
					}
					
					
				}
				}
		}
			if(label.equalsIgnoreCase("removepoints")) {
				if(args.length == 2) {
					if(sender instanceof Player) {
						Player player = (Player) sender;
					if(player.getName().equals("Scrym6") || player.getName().equals("Pierronus")) { 
						String targetname = args[0];
						int points = Integer.valueOf(args[1]);
						if(Bukkit.getPlayer(targetname) != null) {
							if(targetname != player.getName()) {
							Player target = Bukkit.getPlayer(targetname);
							data.removePoints(target.getUniqueId(), points);
							String nowpts = String.valueOf(data.getPoints(target.getUniqueId())); 
							player.sendMessage("§cRetiré " + points + " OrdCoins à " + target.getName() + "\n§cIl a maintenant " + nowpts);
							
						}
						}
						else {
							player.sendMessage("§6§l[GCA] §f§cCe joueur n'existe pas");
						}
					}
				}else {
					String targetname = args[0];
					int points = Integer.valueOf(args[1]);
					if(Bukkit.getPlayer(targetname) != null) {
						Player target = Bukkit.getPlayer(targetname);
						data.removePoints(target.getUniqueId(), points);
						String nowpts = String.valueOf(data.getPoints(target.getUniqueId())); 
						System.out.println("Retiré " + points + " OrdCoins à " + target.getName() + ". Il a maintenant " + nowpts);
						
					
					}
					else {
						System.out.println("Ce joueur n'existe pas (" + targetname + ")");
					}
					
					
				}
				}
		}
			if(label.equalsIgnoreCase("resetpoints")) {
				if(args.length == 1) {
					if(sender instanceof Player) {
						Player player = (Player) sender;
					if(player.getName().equals("Scrym6") || player.getName().equals("Pierronus")) {
						String targetname = args[0];
						if(Bukkit.getPlayer(targetname) != null) {
							if(targetname != player.getName()) {
							Player target = Bukkit.getPlayer(targetname);
							data.resetPoints(target.getUniqueId());
							String nowpts = String.valueOf(data.getPoints(target.getUniqueId()));
							player.sendMessage("§cReset le compteur de OrdCoins de " + target.getName() + "\n§cIl a maintenant " + nowpts);
							
							
						}
						}
						else {
							player.sendMessage("§6§l[GCA] §f§cCe joueur n'existe pas");
						}
					}
				}
					else {
						String targetname = args[0];
						if(Bukkit.getPlayer(targetname) != null) {
							Player target = Bukkit.getPlayer(targetname);
							data.resetPoints(target.getUniqueId());
							String nowpts = String.valueOf(data.getPoints(target.getUniqueId())); 
							System.out.println("Remis à 0 le compteur de OrdCoins de " + target.getName() + ". Il a maintenant " + nowpts);
							
							
						
						}
						
						
					}
				}
		}
			//§8§l3200 §4§lGCA§6§lCoins
			return false;
		}
		
		
}
