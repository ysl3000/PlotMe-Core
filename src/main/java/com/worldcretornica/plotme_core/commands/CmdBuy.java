package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.Plot;
import com.worldcretornica.plotme_core.PlotMeCoreManager;
import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.IOfflinePlayer;
import com.worldcretornica.plotme_core.api.IPlayer;
import com.worldcretornica.plotme_core.api.IWorld;
import com.worldcretornica.plotme_core.api.event.InternalPlotBuyEvent;
import net.milkbowl.vault.economy.EconomyResponse;

public class CmdBuy extends PlotCommand {

    public CmdBuy(PlotMe_Core instance) {
        super(instance);
    }

    public boolean exec(IPlayer p) {
        IWorld world = p.getWorld();
        if (plugin.getPlotMeCoreManager().isPlotWorld(world)) {
            if (plugin.getPlotMeCoreManager().isEconomyEnabled(world)) {
                if (p.hasPermission("PlotMe.use.buy") || p.hasPermission("PlotMe.admin.buy")) {
                    String id = PlotMeCoreManager.getPlotId(p);

                    if (id.isEmpty()) {
                        p.sendMessage("§c" + C("MsgNoPlotFound"));
                    } else if (!plugin.getPlotMeCoreManager().isPlotAvailable(id, world)) {
                        Plot plot = plugin.getPlotMeCoreManager().getPlotById(world, id);

                        if (plot.isForSale()) {
                            String buyer = p.getName();

                            if (plot.getOwner().equalsIgnoreCase(buyer)) {
                                p.sendMessage("§c" + C("MsgCannotBuyOwnPlot"));
                            } else {
                                int plotlimit = getPlotLimit(p);

                                if (plotlimit != -1 && plugin.getPlotMeCoreManager().getNbOwnedPlot(p, world) >= plotlimit) {
                                    p.sendMessage(C("MsgAlreadyReachedMaxPlots") + " ("
                                                          + plugin.getPlotMeCoreManager().getNbOwnedPlot(p, world) + "/" + getPlotLimit(p) + "). "
                                                          + C("WordUse") + " §c/plotme home§r " + C("MsgToGetToIt"));
                                } else {

                                    double cost = plot.getCustomPrice();

                                    if (sob.getBalance(p) < cost) {
                                        p.sendMessage("§c" + C("MsgNotEnoughBuy"));
                                    } else {

                                        InternalPlotBuyEvent event = sob.getEventFactory().callPlotBuyEvent(plugin, world, plot, p, cost);

                                        if (!event.isCancelled()) {
                                            EconomyResponse er = sob.withdrawPlayer(p, cost);

                                            if (er.transactionSuccess()) {
                                                String oldowner = plot.getOwner();
                                                IOfflinePlayer playercurrentbidder = null;

                                                if (plot.getOwnerId() != null) {
                                                    playercurrentbidder = sob.getOfflinePlayer(plot.getOwnerId());
                                                }

                                                if (!"$Bank$".equalsIgnoreCase(oldowner) && playercurrentbidder != null) {
                                                    EconomyResponse er2 = sob.depositPlayer(playercurrentbidder, cost);

                                                    if (er2.transactionSuccess()) {
                                                        for (IPlayer player : sob.getOnlinePlayers()) {
                                                            if (player.getName().equalsIgnoreCase(oldowner)) {
                                                                player.sendMessage(C("WordPlot") + " " + id + " "
                                                                                           + C("MsgSoldTo") + " " + buyer + ". " + Util().moneyFormat(cost));
                                                                break;
                                                            }
                                                        }
                                                    } else {
                                                        p.sendMessage("§c" + er2.errorMessage);
                                                        warn(er2.errorMessage);
                                                    }
                                                }

                                                plot.setOwner(buyer);
                                                plot.setCustomPrice(0);
                                                plot.setForSale(false);

                                                plot.updateField("owner", buyer);
                                                plot.updateField("customprice", 0);
                                                plot.updateField("forsale", false);

                                                plugin.getPlotMeCoreManager().adjustWall(p);
                                                plugin.getPlotMeCoreManager().setSellSign(world, plot);
                                                PlotMeCoreManager.setOwnerSign(world, plot);

                                                p.sendMessage(C("MsgPlotBought") + " " + Util().moneyFormat(-cost));

                                                if (isAdvancedLogging()) {
                                                    plugin.getLogger().info(LOG + buyer + " " + C("MsgBoughtPlot") + " " + id + " " + C("WordFor") + " " + cost);
                                                }
                                            } else {
                                                p.sendMessage("§c" + er.errorMessage);
                                                warn(er.errorMessage);
                                            }
                                        }
                                    }
                                }
                            }
                        } else {
                            p.sendMessage("§c" + C("MsgPlotNotForSale"));
                        }
                    } else {
                        p.sendMessage("§c" + C("MsgThisPlot") + "(" + id + ") " + C("MsgHasNoOwner"));
                    }
                } else {
                    p.sendMessage("§c" + C("MsgPermissionDenied"));
                    return false;
                }
            } else {
                p.sendMessage("§c" + C("MsgEconomyDisabledWorld"));
            }
        }
        return true;
    }
}
