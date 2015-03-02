package com.archivesmc.archblock.runnables;

import com.archivesmc.archblock.Plugin;
import org.bukkit.block.Block;

import java.util.List;
import java.util.UUID;

public class MassOwnershipChanger extends Thread {
    private Plugin plugin;
    private List<Block> blocks;
    private UUID owner;
    private Runnable finishRunnable;

    public MassOwnershipChanger(Plugin plugin, List<Block> blocks, UUID owner, Runnable finishRunnable) {
        this.plugin = plugin;
        this.blocks = blocks;
        this.owner = owner;
        this.finishRunnable = finishRunnable;
    }

    @Override
    public void run() {
        for (Block b : this.blocks) {
            if (this.plugin.getApi().getOwnerUUID(b) != null) {
                this.plugin.getApi().removeOwner(b);
            }

            if (this.owner != null) {
                this.plugin.getApi().setOwnerUUID(b, owner);
            }
        }

        this.plugin.getServer().getScheduler().runTask(this.plugin, this.finishRunnable);
    }
}
