package dev.tomr.adventcalendar;

import dev.tomr.adventcalendar.commands.MainCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class AdventCalendar extends JavaPlugin {
    FileConfiguration config = getConfig();
    public static org.bukkit.plugin.Plugin plugin = null;

    @Override
    public void onEnable() {
        // Plugin startup logic
        getLogger().info("Starting Up Advent Calendar");
        saveDefaultConfig();
        File dir = new File(getDataFolder() + File.separator + "players");
        if (!dir.exists()) {
            dir.mkdir();
        }
        this.getCommand("advent").setExecutor(new MainCommand());
        plugin = this;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        getLogger().info("Advent Calendar has shutdown");
    }
    public static Plugin getInstance() {
        return plugin;
    }
}
