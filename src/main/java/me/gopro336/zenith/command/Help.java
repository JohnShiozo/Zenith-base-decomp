package me.gopro336.zenith.command;

import me.gopro336.zenith.Client;
import me.gopro336.zenith.command.Command;
import me.gopro336.zenith.util.LoggerUtil;

public class Help
extends Command {
    public Help(String name, String[] alias, String usage) {
        super(name, alias, usage);
    }

    @Override
    public void onTrigger(String arguments) {
        LoggerUtil.sendMessage("ZenithCore");
        for (Command command : Client.commandManager.getCommands()) {
            LoggerUtil.sendMessage(command.getName() + " - " + command.getUsage());
        }
    }
}
