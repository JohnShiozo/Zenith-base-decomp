package me.gopro336.zenith.command;

import java.util.ArrayList;
import me.gopro336.zenith.command.Command;
import me.gopro336.zenith.command.Font;
import me.gopro336.zenith.command.Help;
import me.gopro336.zenith.command.Login;
import me.gopro336.zenith.command.Prefix;
import me.gopro336.zenith.util.LoggerUtil;

public class CommandManager {
    private final ArrayList<Command> commands = new ArrayList();
    private String prefix = ".";

    public CommandManager() {
        this.commands.add(new Help("Help", new String[]{"h", "help"}, "help"));
        this.commands.add(new Prefix("Prefix", new String[]{"prefix"}, "prefix <char>"));
        this.commands.add(new Login("Login", new String[]{"login"}, "login <email> <password>"));
        this.commands.add(new Font("Font", new String[]{"font"}, "font <font>"));
    }

    public void runCommand(String args) {
        boolean found = false;
        String[] split = args.split(" ");
        String startCommand = split[0];
        String arguments = args.substring(startCommand.length()).trim();
        for (Command command : this.getCommands()) {
            for (String alias : command.getAlias()) {
                if (!startCommand.equals(this.getPrefix() + alias)) continue;
                command.onTrigger(arguments);
                found = true;
            }
        }
        if (!found) {
            LoggerUtil.sendMessage("Unknown command");
        }
    }

    public ArrayList<Command> getCommands() {
        return this.commands;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
