package me.dotalw.KillinRewards.evnets;

import me.dotalw.KillinRewards.Core;
import me.dotalw.KillinRewards.libs.Color;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;

import java.util.ArrayList;
import java.util.Collections;

/**
 * KillinRewards (2017) was created by dotalw (C) 2011-2020
 * Licensed under the MIT license.
 */
public class EntityDeath implements Listener {
    private Core core;

    public EntityDeath(Core core) {
        this.core = core;
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        if (!core.getConfig().getBoolean("killin-entities.enabled"))
            return;
        else if (e.getEntity() instanceof Player)
            return;

        LivingEntity entity = e.getEntity();
        Player killer = entity.getKiller();
        if (killer == null) return;

        String st = null;
        ArrayList<String> str = new ArrayList<>();
        boolean r = false;

        if (core.getCache().isUsingVault()) {
            if (core.getConfig().getBoolean("killin-entities.reward-money.enabled")) {
                for (String s : core.getCache().getEntityMoneyRewards().keySet()) {
                    String[] parts = s.split("-");
                    if (!parts[0].equalsIgnoreCase(entity.getName()))
                        continue;
                    if (killer.hasPermission(core.getCache().getBasePlayerPermission() + "." + parts[1])) {
                        st = parts[1];
                        str.add(s);
                        r = true;
                    }
                }

                if (r)
                    if (killer.hasPermission(core.getCache().getBasePlayerPermission() + "." + st)) {
                        Double amount = core.getCache().getEntityMoneyRewards().get(Collections.max(str));
                        Double fixedAmount = Double.parseDouble(amount.toString().replace("-", ""));
                        if (amount.toString().startsWith("-")) {
                            core.getEcon().withdrawPlayer(killer, fixedAmount);
                            killer.sendMessage(Color.code(replace(core.getConfig().getString("messages.entity-kill-take"), killer, entity, fixedAmount)));
                        } else {
                            core.getEcon().depositPlayer(killer, fixedAmount);
                            killer.sendMessage(Color.code(replace(core.getConfig().getString("messages.entity-kill-give"), killer, entity, fixedAmount)));
                        }
                    }
            }
        }

        boolean b = false;
        r = false;
        st = null;
        if (core.getConfig().getBoolean("killin-entities.reward-command.enabled")) {
            for (String s : core.getCache().getEntityCommandsReward().keySet()) {
                if (!s.equalsIgnoreCase(entity.getName()))
                    continue;

                for (String st1 : core.getCache().getEntityCommandsReward().get(s).keySet())
                    if (killer.hasPermission(core.getCache().getBasePlayerPermission() + "." + st1)) {
                        st = st1;
                        r = true;
                    }
            }

            if (r) {
                if (killer.getWorld().getGameRuleValue("sendCommandFeedback").equalsIgnoreCase("true")) b = true;
                killer.getWorld().setGameRuleValue("sendCommandFeedback", "false");
                for (String s : core.getCache().getEntityCommandsReward().keySet()) {
                    if (s.equalsIgnoreCase(entity.getName())) {
                        for (String s0 : core.getCache().getEntityCommandsReward().get(s).keySet()) {
                            if (!s0.equalsIgnoreCase(st))
                                continue;
                            if (killer.hasPermission(core.getCache().getBasePlayerPermission() + "." + st)) {
                                for (String s1 : core.getCache().getEntityCommandsReward().get(s).get(s0))
                                    core.getServer().dispatchCommand(core.getServer().getConsoleSender(), replace(s1, killer, entity));
                                break;
                            }
                        }
                    }
                }
                if (b) killer.getWorld().setGameRuleValue("sendCommandFeedback", "true");
            }
        }
    }


    private String replace(String s, Player player, LivingEntity target, Double amount) {
        return s.replace("%player%", player.getName()).replace("%target%", target.getName()).replace("%amount%", core.getEcon().format(amount));
    }

    private String replace(String s, Player player, LivingEntity target) {
        return s.replace("%player%", player.getName()).replace("%target%", target.getName());
    }
}
