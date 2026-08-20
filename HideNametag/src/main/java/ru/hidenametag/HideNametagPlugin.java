package ru.hidenametag;

import org.bukkit.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.NameTagVisibility;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class HideNametagPlugin extends JavaPlugin implements Listener {
    private static final String TEAM_NAME = "hn_hidden";
    private boolean enabled;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        enabled = getConfig().getBoolean("hide-nametags", true);
        getServer().getPluginManager().registerEvents(this, this);
        applyToAll();
        getLogger().info("HideNametag включен. Скрытие ников: " + (enabled ? "ВКЛ" : "ВЫКЛ"));
    }

    @Override
    public void onDisable() {
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = board.getTeam(TEAM_NAME);
        if (team != null) team.unregister();
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        if (enabled) addPlayerToTeam(event.getPlayer());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("hidenametag")) return false;
        if (args.length == 0) {
            sender.sendMessage(ChatColor.YELLOW + "Использование: /hidenametag <on|off|status>");
            return true;
        }
        switch (args[0].toLowerCase()) {
            case "on" -> {
                enabled = true;
                getConfig().set("hide-nametags", true);
                saveConfig();
                applyToAll();
                sender.sendMessage(ChatColor.GREEN + "Ники над головой теперь скрыты.");
            }
            case "off" -> {
                enabled = false;
                getConfig().set("hide-nametags", false);
                saveConfig();
                removeTeam();
                sender.sendMessage(ChatColor.GREEN + "Ники над головой теперь видны.");
            }
            case "status" -> sender.sendMessage(ChatColor.AQUA + "Скрытие ников сейчас: "
                    + (enabled ? ChatColor.GREEN + "включено" : ChatColor.RED + "выключено"));
            default -> sender.sendMessage(ChatColor.YELLOW + "Использование: /hidenametag <on|off|status>");
        }
        return true;
    }

    private void applyToAll() {
        if (!enabled) {
            removeTeam();
            return;
        }
        getOrCreateTeam();
        for (Player player : Bukkit.getOnlinePlayers()) addPlayerToTeam(player);
    }

    private Team getOrCreateTeam() {
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = board.getTeam(TEAM_NAME);
        if (team == null) team = board.registerNewTeam(TEAM_NAME);
        team.setNameTagVisibility(NameTagVisibility.NEVER);
        return team;
    }

    private void addPlayerToTeam(Player player) {
        Team team = getOrCreateTeam();
        if (!team.hasEntry(player.getName())) team.addEntry(player.getName());
    }

    private void removeTeam() {
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();
        Team team = board.getTeam(TEAM_NAME);
        if (team != null) team.unregister();
    }
}
