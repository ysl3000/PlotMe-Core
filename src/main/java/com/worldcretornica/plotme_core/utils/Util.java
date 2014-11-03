package com.worldcretornica.plotme_core.utils;

import com.worldcretornica.plotme_core.PlotMe_Core;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.ChatColor;

import java.util.logging.Level;

public class Util {

    private final PlotMe_Core plugin;

    public Util(PlotMe_Core instance) {
        plugin = instance;
    }

    public String C(String s) {
        if (plugin.getCaptionConfig().contains(s)) {
            return addColor(plugin.getCaptionConfig().getString(s));
        } else {
            plugin.getLogger().log(Level.WARNING, "Missing caption: {0}", s);
            return "ERROR:Missing caption '" + s + "'";
        }
    }

    private static String addColor(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static StringBuilder whitespace(int length) {
        int spaceWidth = MinecraftFontWidthCalculator.getStringWidth(" ");

        StringBuilder ret = new StringBuilder();

        for (int i = 0; i + spaceWidth < length; i += spaceWidth) {
            ret.append(" ");
        }

        return ret;
    }

    public static String round(double money) {
        if (money % 1 == 0) return String.valueOf(Math.round(money));
        else return "" + money;
    }

    public String moneyFormat(double price) {
        return moneyFormat(price, true);
    }

    public String moneyFormat(double price, boolean showsign) {
        if (price == 0) {
            return "";
        }

        String format = round(Math.abs(price));

        Economy econ = plugin.getServerBridge().getEconomy();
        
        if (econ != null) {
            format = price <= 1 && price >= -1 ? format + " " + econ.currencyNameSingular() : format + " " + econ.currencyNamePlural();
        }

        if (showsign) {
            return "§a" + (price > 0 ? "+" + format : "-" + format);
        } else {
            return "§a" + format;
        }
    }

}
