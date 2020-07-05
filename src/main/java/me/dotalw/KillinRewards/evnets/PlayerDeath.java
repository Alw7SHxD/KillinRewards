package me.dotalw.KillinRewards.evnets;

import me.dotalw.KillinRewards.Core;
import me.dotalw.KillinRewards.libs.Color;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

/**
 * KillinRewards (2017) was created by dotalw (C) 2011-2020
 * Licensed under the MIT license.
 */
public class PlayerDeath implements Listener {
    private Core core;

    public PlayerDeath(Core core) {
        this.core = core;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        if (!core.getConfig().getBoolean("killin-players.enabled"))
            return;

        Player player = e.getEntity();
        Player killer = player.getKiller();
        if (killer == null || killer == player) return;

        Bukkit.getScheduler().runTask(core, () -> {
            player.spigot().respawn();
        });

        String st = null;
        if (core.getCache().isUsingVault()) {
            if (core.getConfig().getBoolean("killin-players.reward-money.enabled")) {
                for (String s : core.getCache().getPlayerMoneyRewards().keySet())
                    if (killer.hasPermission(core.getCache().getBasePlayerPermission() + "." + s))
                        st = s;

                if (killer.hasPermission(core.getCache().getBasePlayerPermission() + "." + st)) {
                    Double amount = core.getCache().getPlayerMoneyRewards().get(st);
                    Double fixedAmount = Double.parseDouble(amount.toString().replace("-", ""));
                    if (amount.toString().startsWith("-")) {
                        core.getEcon().withdrawPlayer(killer, fixedAmount);
                        killer.sendMessage(Color.code(replace(core.getConfig().getString("messages.player-kill-take"), killer, player, fixedAmount)));
                    } else {
                        core.getEcon().depositPlayer(killer, fixedAmount);
                        killer.sendMessage(Color.code(replace(core.getConfig().getString("messages.player-kill-give"), killer, player, fixedAmount)));
                    }
                }
            }
        }

        st = null;

        try {
            if (core.getConfig().getBoolean("killin-players.reward-command.enabled")) {
                boolean b = false;
                if (killer.getWorld().getGameRuleValue("sendCommandFeedback").equalsIgnoreCase("true")) b = true;
                killer.getWorld().setGameRuleValue("sendCommandFeedback", "false");
                for (String s : core.getCache().getPlayerCommandsReward().keySet())
                    if (killer.hasPermission(core.getCache().getBasePlayerPermission() + "." + s))
                        st = s;

                if (killer.hasPermission(core.getCache().getBasePlayerPermission() + "." + st))
                    for (String s : core.getCache().getPlayerCommandsReward().get(st))
                        core.getServer().dispatchCommand(core.getServer().getConsoleSender(), replace(s, killer, player));
                if (b) killer.getWorld().setGameRuleValue("sendCommandFeedback", "true");
            }
        } catch (Exception ex) {
            core.getLogger().severe("An error occurred, please make sure that your config.yml is updated to the latest version.");
            ex.printStackTrace();
        }
    }

    private String replace(String s, Player player, Player target, Double amount) {
        return s.replace("%player%", player.getName()).replace("%target%", target.getDisplayName()).replace("%amount%", core.getEcon().format(amount));
    }

    private String replace(String s, Player player, Player target) {
        return s.replace("%player%", player.getName()).replace("%target%", target.getDisplayName());
    }
}
