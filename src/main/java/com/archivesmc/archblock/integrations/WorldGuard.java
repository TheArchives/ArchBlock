package com.archivesmc.archblock.integrations;

import com.archivesmc.archblock.wrappers.Plugin;
import com.archivesmc.archblock.wrappers.Block;
import com.archivesmc.archblock.wrappers.bukkit.BukkitPlugin;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.managers.RegionManager;
import org.bukkit.World;

import static com.sk89q.worldguard.bukkit.BukkitUtil.toVector;

/**
 * WorldGuard integration, which is used to check if a block is within a
 * region that protection has been disabled in
 */
public class WorldGuard {
    private final Plugin plugin;

    public WorldGuard(Plugin plugin) {
        this.plugin = plugin;
    }

    /**
     * Check whether a block is in a region we don't check for protection in
     * @param block The block to check
     * @return true if the region exists and has protection disabled, false otherwise
     */
    public boolean isInIgnoredRegion(Block block) {
        if (this.plugin instanceof BukkitPlugin) {
            Vector point = toVector((org.bukkit.block.Block) block.getWrapped());
            RegionManager regionManager = ((BukkitPlugin) this.plugin).getWorldGuard().getRegionManager((World) block.getWorld().getWrapped());
            ApplicableRegionSet set = regionManager.getApplicableRegions(point);

            Boolean result = set.getFlag(BukkitPlugin.bypassProtectionFlag);

            if (result == null) {
                return false;
            }

            return result;
        }

        return false;
    }
}
