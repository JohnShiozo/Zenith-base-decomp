package me.gopro336.zenith.command;

import me.gopro336.zenith.Client;
import me.gopro336.zenith.command.Command;
import me.gopro336.zenith.util.LoggerUtil;
import me.gopro336.zenith.util.font.CustomFontRenderer;
import me.gopro336.zenith.util.font.FontUtil;

public class Font
extends Command {
    public Font(String name, String[] alias, String usage) {
        super(name, alias, usage);
    }

    @Override
    public void onTrigger(String arguments) {
        if (arguments.equals("")) {
            this.printUsage();
            return;
        }
        if (FontUtil.validateFont(arguments)) {
            try {
                Client.customFontRenderer = new CustomFontRenderer(new java.awt.Font(arguments, 0, 19), true, false);
                LoggerUtil.sendMessage("New font set to " + arguments);
            }
            catch (Exception e) {
                LoggerUtil.sendMessage("Failed to set font");
            }
        } else {
            LoggerUtil.sendMessage("Invalid font");
        }
    }
}
