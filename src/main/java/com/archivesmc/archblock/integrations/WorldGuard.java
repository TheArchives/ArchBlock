package com.archivesmc.archblock.integrations;

import com.archivesmc.archblock.Plugin;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import org.bukkit.block.Block;

import static com.sk89q.worldguard.bukkit.BukkitUtil.toVector;

public class WorldGuard {
    private final Plugin plugin;

    public WorldGuard(Plugin plugin) {
        this.plugin = plugin;
    }

    public Boolean isInIgnoredRegion(Block block) {
        Vector point = toVector(block);
        RegionManager regionManager = this.plugin.getWorldGuard().getRegionManager(block.getWorld());
        ApplicableRegionSet set = regionManager.getApplicableRegions(point);

        Boolean result = set.getFlag(Plugin.bypassProtectionFlag);

        if (result == null) {
            return false;
        }

        return result;
    }
}
