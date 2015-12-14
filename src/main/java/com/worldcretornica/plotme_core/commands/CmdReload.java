package com.worldcretornica.plotme_core.commands;

import com.worldcretornica.plotme_core.PlotMe_Core;
import com.worldcretornica.plotme_core.api.ICommandSender;

public class CmdReload extends PlotCommand {

    public CmdReload(PlotMe_Core instance) {
        super(instance);
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public boolean execute(ICommandSender sender, String[] args) {
        if (args.length > 1) {
            sender.sendMessage(getUsage());
            return true;
        }
        if (sender.hasPermission("plotme.admin.reload")) {
            plugin.reload();
            sender.sendMessage(C("MsgReloadedSuccess"));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String getUsage() {
        return C("CmdRemoveUsage");
    }
}
