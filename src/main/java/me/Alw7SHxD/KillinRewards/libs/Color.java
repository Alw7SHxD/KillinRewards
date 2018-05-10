package me.Alw7SHxD.KillinRewards.libs;

import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

/**
 * KillinRewards was created by Alw7SHxD (C) 2017
 */
public class Color {
    @NotNull
    public static String code(String s){
        return ChatColor.translateAlternateColorCodes('&', s);
    }
}
