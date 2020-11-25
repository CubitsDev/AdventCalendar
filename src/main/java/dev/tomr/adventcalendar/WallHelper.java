package dev.tomr.adventcalendar;

import org.bukkit.DyeColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;

public class WallHelper {

    public static void wallBuilder(double[] topRight, World world) {
        for (int i = 0; i < 8; i++) {
            Location loc = new Location(world, topRight[0] - i, topRight[1], topRight[2]);
            loc.getBlock().setType(Material.CONCRETE);
            loc.getBlock().setData((byte) 11);
        }
        for (int i = 0; i < 8; i++) {
            Location loc = new Location(world, topRight[0] - i, topRight[1] - 1, topRight[2]);
            loc.getBlock().setType(Material.CONCRETE);
            loc.getBlock().setData((byte) 9);
        }
        for (int i = 0; i < 8; i++) {
            Location loc = new Location(world, topRight[0] - i, topRight[1] - 2, topRight[2]);
            loc.getBlock().setType(Material.CONCRETE);
            loc.getBlock().setData((byte) 3);
        }
        for (int i = 0; i < 8; i++) {
            Location loc = new Location(world, topRight[0] - i, topRight[1] - 3, topRight[2]);
            loc.getBlock().setType(Material.CONCRETE);
            loc.getBlock().setData((byte) 3);
        }
        for (int i = 0; i < 8; i++) {
            Location loc = new Location(world, topRight[0] - i, topRight[1] - 4, topRight[2]);
            loc.getBlock().setType(Material.CONCRETE);
            loc.getBlock().setData((byte) 9);
        }
        for (int i = 0; i < 8; i++) {
            Location loc = new Location(world, topRight[0] - i, topRight[1] - 5, topRight[2]);
            loc.getBlock().setType(Material.CONCRETE);
            loc.getBlock().setData((byte) 11);
        }
    }

    public static void wallDestroyer(double[] topRight, World world) {
        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 8; j++) {
                Location loc = new Location(world, topRight[0] - j, topRight[1] - i, topRight[2]);
                loc.getBlock().setType(Material.AIR);
            }
        }
    }
}
