package me.Alw7SHxD.KillinRewards.commands;

import me.Alw7SHxD.KillinRewards.Core;
import me.Alw7SHxD.KillinRewards.libs.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * KillinRewards was created by Alw7SHxD (C) 2017
 */
public class KillinRewards implements CommandExecutor {
    private Core core;

    public KillinRewards(Core core) {
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
                core.getLists().reload();
                commandSender.sendMessage(Color.code("&a&lSuccess! &7reloaded the configuration file."));
            }else if(strings[0].equalsIgnoreCase("help") || strings[0].equalsIgnoreCase("?")) {
                commandSender.sendMessage(Color.code("    &c&lKillinRewards &7commands (1/1)"));
                commandSender.sendMessage(Color.code(String.format("&8/&7%s reload &8» &7Reloads the configuration file.", s)));
            }else
                commandSender.sendMessage(Color.code(String.format("&c&lWoah! &7correct usage &6/%s &9help", s)));
        }else{
            commandSender.sendMessage(Color.code(String.format("&a&lKillinRewards &7version &a&l%s", core.getDescription().getVersion())));
            commandSender.sendMessage(Color.code("&7Created by &a&lAlw7SHxD"));
        }
        return true;
    }
}
