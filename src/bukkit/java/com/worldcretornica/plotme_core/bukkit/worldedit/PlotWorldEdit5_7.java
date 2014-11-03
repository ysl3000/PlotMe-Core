package com.worldcretornica.plotme_core.bukkit.worldedit;

import com.sk89q.worldedit.LocalSession;
import com.sk89q.worldedit.LocalWorld;
import com.sk89q.worldedit.Vector;
import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.masks.Mask;
import com.sk89q.worldedit.masks.RegionMask;
import com.sk89q.worldedit.regions.CuboidRegion;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.PlotWorldEdit;
import com.worldcretornica.plotme_core.bukkit.api.BukkitLocation;
import com.worldcretornica.plotme_core.bukkit.api.BukkitPlayer;
import com.worldcretornica.plotme_core.bukkit.api.BukkitWorld;
import org.bukkit.Location;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@SuppressWarnings({ "deprecation" })
public class PlotWorldEdit5_7 implements PlotWorldEdit {

    private final PlotMe_Core plugin;
    private final WorldEditPlugin we;

    public PlotWorldEdit5_7(PlotMe_Core instance, WorldEditPlugin wep) {
        plugin = instance;
        we = wep;
    }

    @Override
    public void setMask(IPlayer player) {
        String id = PlotMeCoreManager.getPlotId(player);
        setMask(player, id);
    }

    @Override
    public void setMask(IPlayer p, String id) {
        BukkitWorld w = (BukkitWorld) p.getWorld();
        BukkitPlayer bp = (BukkitPlayer) p;

        BukkitLocation bottom;
        BukkitLocation top;

        LocalSession session = we.getSession(bp.getPlayer());

        if (!"".equalsIgnoreCase(id)) {
            Plot plot = plugin.getPlotMeCoreManager().getPlotById(p, id);

            if (plot != null && plot.isAllowed(p.getUniqueId())) {
                bottom = (BukkitLocation) PlotMeCoreManager.getPlotBottomLoc(w, id);
                top = (BukkitLocation) PlotMeCoreManager.getPlotTopLoc(w, id);

                com.sk89q.worldedit.bukkit.BukkitPlayer player = we.wrapPlayer(bp.getPlayer());
                LocalWorld world = player.getWorld();

                Vector pos1 = new Vector(bottom.getBlockX(), bottom.getBlockY(), bottom.getBlockZ());
                Vector pos2 = new Vector(top.getBlockX(), top.getBlockY(), top.getBlockZ());

                CuboidRegion cr = new CuboidRegion(world, pos1, pos2);

                RegionMask rm = new RegionMask(cr);

                session.setMask(rm);
                return;
            }
        }

        bottom = new BukkitLocation(new Location(w.getWorld(), 0, 0, 0));
        top = new BukkitLocation(new Location(w.getWorld(), 0, 0, 0));

        Object result = null;

        try {
            Class<? extends LocalSession> csession = session.getClass();
            Method method = csession.getMethod("getMask", (Class<?>[]) null);
            result = method.invoke(session, (Object[]) null);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        if (result == null) {
            com.sk89q.worldedit.bukkit.BukkitPlayer player = we.wrapPlayer(bp.getPlayer());
            LocalWorld world = player.getWorld();

            Vector pos1 = new Vector(bottom.getBlockX(), bottom.getBlockY(), bottom.getBlockZ());
            Vector pos2 = new Vector(top.getBlockX(), top.getBlockY(), top.getBlockZ());

            CuboidRegion cr = new CuboidRegion(world, pos1, pos2);

            RegionMask rm = new RegionMask(cr);

            session.setMask(rm);
        }
    }

    @Override
    public void removeMask(IPlayer player) {
        BukkitPlayer bp = (BukkitPlayer) player;
        LocalSession session = we.getSession(bp.getPlayer());
        session.setMask((Mask) null);
    }
}
