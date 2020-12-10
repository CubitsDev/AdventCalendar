package dev.tomr.adventcalendar.commands;

import dev.tomr.adventcalendar.AdventCalendar;
import dev.tomr.adventcalendar.FireworkUtils;
import dev.tomr.adventcalendar.UserManager;
import dev.tomr.adventcalendar.WallHelper;
import javafx.geometry.Pos;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class MainCommand implements CommandExecutor {

    // This method is called, when somebody uses our command

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof BlockCommandSender)) {

            sender.sendMessage(ChatColor.RED + "[Advent] This command cannot be run by a player.");

        } else {

            try {
                Player p = Bukkit.getPlayer(args[0]);
                Date date = new Date();
                SimpleDateFormat dayFormat = new SimpleDateFormat("d");
                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("America/New_York"));
                cal.setTime(date);

                if (UserManager.userFileCheck(p.getUniqueId().toString())) {

                    File uFile = new File(AdventCalendar.getInstance().getDataFolder(), File.separator + "players/" + p.getUniqueId().toString() + ".yml");
                    FileConfiguration config = AdventCalendar.getInstance().getConfig();
                    FileConfiguration pData = YamlConfiguration.loadConfiguration(uFile);
                    if (Integer.parseInt(dayFormat.format(date)) > 25 | cal.get(Calendar.MONTH) + 1 != config.getInt("month")) {
                        p.sendMessage(ChatColor.RED + "[Advent] " + ChatColor.GOLD + "The Advent Calendar only runs December 1st to December 25th.");
                        Location pExit = new Location(p.getWorld(), config.getDouble("exit.x"), config.getDouble("exit.y"), config.getDouble("exit.z"));
                        p.teleport(pExit);
                        return true;
                    }

                    if (pData.getBoolean("daysComplete." + dayFormat.format(date))) {

                        p.sendMessage(ChatColor.RED + "[Advent] " + ChatColor.GOLD + "You have already unlocked this day!");
                        Location pExit = new Location(p.getWorld(), config.getDouble("exit.x"), config.getDouble("exit.y"), config.getDouble("exit.z"));
                        p.teleport(pExit);

                    } else {
                        p.sendMessage(ChatColor.RED + "[Advent] " + ChatColor.GOLD + "Woo! Time To Unlock Day: " + ChatColor.AQUA + dayFormat.format(date));
                        String day = dayFormat.format(date);
                        Location pWatch = new Location(p.getWorld(), config.getDouble(dayFormat.format(date) + ".playerWatch.x"), config.getDouble(dayFormat.format(date) + ".playerWatch.y"), config.getDouble(dayFormat.format(date) + ".playerWatch.z"), 180f, 0f);
                        Location fLaunch = new Location(p.getWorld(), config.getDouble(dayFormat.format(date) + ".playerWatch.x"), config.getDouble(dayFormat.format(date) + ".playerWatch.y"), config.getDouble(dayFormat.format(date) + ".playerWatch.z") -3 );
                        pData.set("daysComplete." + dayFormat.format(date), true);
                        pData.save(uFile);
                        p.teleport(pWatch);
                        p.playSound(pWatch, "block.chest.open", 1, 0);
                        double[] topRight = {config.getDouble(day + ".topRight.x"), config.getDouble(day + ".topRight.y"), config.getDouble(day + ".topRight.z")};
                        WallHelper.wallDestroyer(topRight, p.getWorld());
                        p.setWalkSpeed(0);
                        p.setFlySpeed(0);
                        PotionEffect noJump = new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, 128);
                        p.addPotionEffect(noJump);
                        Bukkit.getScheduler().runTaskTimer(AdventCalendar.plugin, new Runnable()
                        {
                            int time = 5;

                            @Override
                            public void run()
                            {
                                if (this.time == 0)
                                {
                                    Bukkit.getScheduler().cancelTasks(AdventCalendar.plugin);
                                    Bukkit.getScheduler().runTaskTimer(AdventCalendar.plugin, new Runnable()
                                        {
                                            int time = 5;
                                            @Override
                                            public void run()
                                            {
                                                if (this.time == 0) {
                                                    Location pExit = new Location(p.getWorld(), config.getDouble("exit.x"), config.getDouble("exit.y"), config.getDouble("exit.z"));
                                                    p.teleport(pExit);
                                                    WallHelper.wallBuilder(topRight, p.getWorld());
                                                    Bukkit.getScheduler().cancelTasks(AdventCalendar.plugin);
                                                    p.setWalkSpeed(0.2f);
                                                    p.setFlySpeed(0.1f);
                                                    p.removePotionEffect(PotionEffectType.JUMP);
                                                }
                                                if (this.time == 4)
                                                {
                                                    FireworkUtils.spawnRandomFirework(fLaunch);
                                                    this.time--;
                                                }
                                                if (this.time == 3) {
                                                    p.sendMessage(ChatColor.GREEN + "" + ChatColor.MAGIC + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
                                                    p.sendMessage(ChatColor.RED + "[Advent] " + ChatColor.GOLD + "Woo! You unlocked the following:");
                                                    p.sendMessage(ChatColor.AQUA + "            " + config.getString(day + ".prizeFormat"));
                                                    p.sendMessage(ChatColor.GREEN + "" + ChatColor.MAGIC + "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
                                                    ConsoleCommandSender console = Bukkit.getServer().getConsoleSender();
                                                    String command = config.getString(day + ".prize");
                                                    String newcmd = command.replace("%s", p.getDisplayName());
                                                    Bukkit.dispatchCommand(console, newcmd);
                                                }
                                                this.time--;
                                            }
                                        }, 0L, 20L);
                                    return;
                                }
                                    p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.RED + String.valueOf(time) + ChatColor.GOLD + " second(s) until prize!"));

                                this.time--;
                            }
                        }, 0L, 20L);
                    }

                } else {

                    throw new Exception("File Creation/Check Failed");

                }

            } catch (Exception e) {

                sender.sendMessage(ChatColor.RED + "[Advent] Error! Does that Player exist? Check Console for more information.");
                Bukkit.getLogger().severe(e.getMessage());
                e.printStackTrace();

            }
        }
        return true;
    }
}