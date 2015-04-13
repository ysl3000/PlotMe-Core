package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PermissionNames;
import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotId;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ICommandSender;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.utils.NameFetcher;

import java.util.Collections;
import java.util.List;

public class CmdInfo extends PlotCommand {

    public CmdInfo(PlotMe_Core instance) {
        super(instance);
    }

    @Override
    public List getAliases() {
        return Collections.singletonList("i");
    }

    public String getName() {
        return "info";
    }

    public boolean execute(ICommandSender sender, String[] args) throws Exception{
        if (args.length > 1) {
            throw new BadUsageException(getUsage());
        }
        IPlayer player = (IPlayer) sender;
        if (player.hasPermission(PermissionNames.USER_INFO)) {
            IWorld world = player.getWorld();
            if (manager.isPlotWorld(world)) {
                PlotId id = manager.getPlotId(player);

                if (id == null) {
                    player.sendMessage(C("MsgNoPlotFound"));
                    return true;
                }
                if (!manager.isPlotAvailable(id)) {
                    Plot plot = manager.getPlotById(id);

                    player.sendMessage("Internal ID: " + plot.getInternalID());
                    player.sendMessage("ID: " + id + " " + C("InfoOwner") + ": " + plot.getOwner()
                            + " " + C("InfoBiome") + ": " + plot.getBiome());

                    if (plot.getExpiredDate() == null) {
                        if (plot.isFinished()) {
                            if (plot.isProtected()) {
                                player.sendMessage(C("InfoExpire") + ": " + C("WordNever")
                                        + " " + C("InfoFinished") + ": " + C("WordYes")
                                        + " " + C("InfoProtected") + ": " + C("WordYes"));
                            } else {
                                player.sendMessage(C("InfoExpire") + ": " + C("WordNever")
                                        + " " + C("InfoFinished") + ": " + C("WordYes")
                                        + " " + C("InfoProtected") + ": " + C("WordNo"));
                            }
                        } else if (plot.isProtected()) {
                            player.sendMessage(C("InfoExpire") + ": " + C("WordNever")
                                    + " " + C("InfoFinished") + ": " + C("WordNo")
                                    + " " + C("InfoProtected") + ": " + C("WordYes"));
                        } else {
                            player.sendMessage(C("InfoExpire") + ": " + C("WordNever")
                                    + " " + C("InfoFinished") + ": " + C("WordNo")
                                    + " " + C("InfoProtected") + ": " + C("WordNo"));
                        }
                    } else if (plot.isProtected()) {
                        if (plot.isFinished()) {
                            player.sendMessage(C("InfoExpire") + ": " + plot.getExpiredDate()
                                    + " " + C("InfoFinished") + ": " + C("WordYes")
                                    + " " + C("InfoProtected") + ": " + C("WordYes"));
                        } else {
                            player.sendMessage(C("InfoExpire") + ": " + plot.getExpiredDate()
                                    + " " + C("InfoFinished") + ": " + C("WordNo")
                                    + " " + C("InfoProtected") + ": " + C("WordYes"));
                        }
                    } else if (plot.isFinished()) {
                        player.sendMessage(C("InfoExpire") + ": " + plot.getExpiredDate()
                                + " " + C("InfoFinished") + ": " + C("WordYes")
                                + " " + C("InfoProtected") + ": " + C("WordNo"));
                    } else {
                        player.sendMessage(C("InfoExpire") + ": " + plot.getExpiredDate()
                                + " " + C("InfoFinished") + ": " + C("WordNo")
                                + " " + C("InfoProtected") + ": " + C("WordNo"));
                    }

                    if (plot.getAllowed().size() > 0) {
                        player.sendMessage(C("InfoAllowed") + ": " + plot.getAllowed().keySet().toString());
                    }

                    if (plot.getDenied().size() > 0) {
                        if (plot.getDenied().contains("*")) {
                            player.sendMessage(C("InfoDenied") + ": " + plot.getDenied().toString());
                        }

                        NameFetcher nameFetcher = new NameFetcher(plot.getDenied());
                        player.sendMessage(C("InfoDenied") + ": " + nameFetcher.call().toString());
                    }

                    if (manager.isEconomyEnabled(world)) {
                        if (plot.isForSale()) {
                            player.sendMessage(C("InfoForSale") + ": " + (Math.round(plot.getPrice())));
                        } else {
                            player.sendMessage(C("InfoForSale") + ": " + C("WordNo"));
                        }
                    }
                    int bottomX = manager.bottomX(id);
                    int bottomZ = manager.bottomZ(id);
                    int topX = manager.topX(id);
                    int topZ = manager.topZ(id);

                    player.sendMessage(C("WordBottom") + ": " + bottomX + "," + bottomZ);
                    player.sendMessage(C("WordTop") + ": " + topX + "," + topZ);

                } else {
                    player.sendMessage(C("MsgThisPlot") + " (" + id + ") " + C("MsgHasNoOwner"));
                }
            } else {
                player.sendMessage(C("MsgNotPlotWorld"));
            }
        } else {
            return false;
        }
        return true;
    }

    @Override
    public String getUsage() {
        return C("WordUsage") + ": /plotme info";
    }
}
