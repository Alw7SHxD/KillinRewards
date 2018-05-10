package me.Alw7SHxD.KillinRewards.evnets;

import me.Alw7SHxD.KillinRewards.Core;
import me.Alw7SHxD.KillinRewards.libs.Color;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * KillinRewards (2017) was created by Alw7SHxD (C) 2011-2018
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
        if (killer == null) return;

        player.spigot().respawn();

        String st = null;
        if (core.getLists().isUsingVault()) {
            if (core.getConfig().getBoolean("killin-players.reward-money.enabled")) {
                for (String s : core.getLists().getPlayerMoneyRewards().keySet())
                    if (killer.hasPermission(core.getLists().getBasePlayerPermission() + "." + s))
                        st = s;

                if (killer.hasPermission(core.getLists().getBasePlayerPermission() + "." + st)) {
                    Double amount = core.getLists().getPlayerMoneyRewards().get(st);
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
                for (String s : core.getLists().getPlayerCommandsReward().keySet())
                    if (killer.hasPermission(core.getLists().getBasePlayerPermission() + "." + s))
                        st = s;

                if (killer.hasPermission(core.getLists().getBasePlayerPermission() + "." + st))
                    for (String s : core.getLists().getPlayerCommandsReward().get(st))
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
