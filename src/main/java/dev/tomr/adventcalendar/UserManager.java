package dev.tomr.adventcalendar;

import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;

public class UserManager {


    public static boolean userFileCheck(String pUUID) {
        File customConfigFile;
        customConfigFile = new File(AdventCalendar.getInstance().getDataFolder(), File.separator + "players/" + pUUID + ".yml");
        try {
            customConfigFile.createNewFile();
        } catch (IOException e) {
            Bukkit.getLogger().severe(e.getMessage());
        }
        return true;
    }


}
