package me.dotalw.KillinRewards.commands;

import me.dotalw.KillinRewards.Core;
import me.dotalw.KillinRewards.libs.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * KillinRewards (2017) was created by dotalw (C) 2011-2020
 * Licensed under the MIT license.
 */
public class KillinRewardsCommand implements CommandExecutor {
    private Core core;

    public KillinRewardsCommand(Core core) {
        this.core = core;
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(strings.length >= 1) {
            if(strings[0].equalsIgnoreCase("reload") || strings[0].equalsIgnoreCase("rl")){
                if(!commandSender.hasPermission("kr.reload")){
                    commandSender.sendMessage(Color.code("&c&lHey! &7you don't have permissions to do that."));
                    return true;
                }

                core.reloadConfig();
                core.getCache().reload();
                commandSender.sendMessage(Color.code("&a&lSuccess! &7reloaded the configuration file."));
            }else if(strings[0].equalsIgnoreCase("help") || strings[0].equalsIgnoreCase("?")) {
                commandSender.sendMessage(Color.code("    &c&lKillinRewards &7commands (1/1)"));
                commandSender.sendMessage(Color.code(String.format("&8/&7%s reload &8» &7Reloads the configuration file.", s)));
            }else
                commandSender.sendMessage(Color.code(String.format("&c&lWoah! &7correct usage &6/%s &9help", s)));
        }else{
            commandSender.sendMessage(Color.code(String.format("&c&lKillinRewards &7version &c&l%s", core.getDescription().getVersion())));
            commandSender.sendMessage(Color.code("&7Created by &c&ldotalw"));
        }
        return true;
    }
}
