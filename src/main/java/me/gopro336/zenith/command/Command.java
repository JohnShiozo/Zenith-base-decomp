package me.gopro336.zenith.command;

import me.gopro336.zenith.Client;
import me.gopro336.zenith.util.LoggerUtil;

public class Command {
    private String name;
    private String[] alias;
    private String usage;

    public Command(String name, String[] alias, String usage) {
        this.setName(name);
        this.setAlias(alias);
        this.setUsage(usage);
    }

    public void onTrigger(String arguments) {
    }

    public void printUsage() {
        LoggerUtil.sendMessage("Usage: " + Client.commandManager.getPrefix() + this.usage);
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getAlias() {
        return this.alias;
    }

    public void setAlias(String[] alias) {
        this.alias = alias;
    }

    public String getUsage() {
        return this.usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }
}
