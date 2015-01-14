package com.worldcretornica.plotme_core.bukkit.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.Player;
import com.worldcretornica.plotme_core.api.World;
import com.worldcretornica.plotme_core.api.event.InternalPlotAddAllowedEvent;
import com.worldcretornica.plotme_core.bukkit.api.BukkitPlayer;
import com.worldcretornica.plotme_core.bukkit.api.BukkitWorld;
import org.bukkit.event.Cancellable;

public class PlotAddAllowedEvent extends PlotEvent implements Cancellable {

    private final InternalPlotAddAllowedEvent event;

    public PlotAddAllowedEvent(PlotMe_Core instance, org.bukkit.World world, Plot plot, org.bukkit.entity.Player player, String allowed) {
        super(instance, plot, world);
        event = new InternalPlotAddAllowedEvent(instance, new BukkitWorld(world), plot, new BukkitPlayer(player), allowed);
    }

    public PlotAddAllowedEvent(PlotMe_Core instance, World world, Plot plot, Player player, String allowed) {
        super(instance, plot, world);
        event = new InternalPlotAddAllowedEvent(instance, world, plot, player, allowed);
    }

    @Override
    public boolean isCancelled() {
        return event.isCancelled();
    }

    @Override
    public void setCancelled(boolean val) {
        event.setCanceled(val);
    }

    public org.bukkit.entity.Player getPlayer() {
        return ((BukkitPlayer) event.getPlayer()).getPlayer();
    }

    public String getNewAllowed() {
        return event.getNewAllowed();
    }

    public InternalPlotAddAllowedEvent getInternal() {
        return event;
    }
}
