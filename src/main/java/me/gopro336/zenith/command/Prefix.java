package me.gopro336.zenith.command;

import me.gopro336.zenith.Client;
import me.gopro336.zenith.command.Command;
import me.gopro336.zenith.util.LoggerUtil;

public class Prefix
extends Command {
    public Prefix(String name, String[] alias, String usage) {
        super(name, alias, usage);
    }

    @Override
    public void onTrigger(String arguments) {
        if (arguments.equals("")) {
            this.printUsage();
            return;
        }
        Client.commandManager.setPrefix(arguments);
        LoggerUtil.sendMessage("Prefix set to " + arguments);
    }
}
