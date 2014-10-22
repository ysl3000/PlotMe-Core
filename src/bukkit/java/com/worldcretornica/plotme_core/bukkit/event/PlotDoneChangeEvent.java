package com.worldcretornica.plotme_core.bukkit.event;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotDoneChangeEvent;
import com.worldcretornica.plotme_core.bukkit.api.BukkitPlayer;
import com.worldcretornica.plotme_core.bukkit.api.BukkitWorld;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

public class PlotDoneChangeEvent extends PlotEvent implements Cancellable {

    private InternalPlotDoneChangeEvent event;

    public PlotDoneChangeEvent(PlotMe_Core instance, World world, Plot plot, Player player, boolean done) {
        super(instance, plot, world);
        event = new InternalPlotDoneChangeEvent(instance, new BukkitWorld(world), plot, new BukkitPlayer(player), done);
    }

    public PlotDoneChangeEvent(PlotMe_Core instance, IWorld world, Plot plot, IPlayer player, boolean done) {
        super(instance, plot, world);
        event = new InternalPlotDoneChangeEvent(instance, world, plot, player, done);
    }

    @Override
    public boolean isCancelled() {
        return event.isCancelled();
    }

    @Override
    public void setCancelled(boolean cancel) {
        event.setCancelled(cancel);
    }

    public Player getPlayer() {
        return ((BukkitPlayer) event.getPlayer()).getPlayer();
    }

    public boolean getDone() {
        return event.getDone();
    }

    public InternalPlotDoneChangeEvent getInternal() {
        return event;
    }
}