package me.dotalw.KillinRewards.libs;

import me.dotalw.KillinRewards.Core;

import java.util.HashMap;
import java.util.List;

/**
 * KillinRewards (2017) was created by dotalw (C) 2011-2020
 * Licensed under the MIT license.
 */
public class Cache {
    private Core core;
    private HashMap<String, Double> playerMoneyRewards = new HashMap<>();
    private HashMap<String, List<String>> playerCommandsReward = new HashMap<>();
    private HashMap<String, HashMap<String, List<String>>> entityCommandsReward = new HashMap<>();
    private HashMap<String, Double> entityMoneyRewards = new HashMap<>();
    private String basePlayerPermission = "kr.player";
    private boolean isUsingVault = false;

    public Cache(Core core) {
        this.core = core;
    }

    public void reload(){
        loadPlayerMoneyRewards();
        loadPlayerCommandRewards();
        loadEntityCommandRewards();
        loadEntityMoneyRewards();
        setBasePlayerPermission(core.getConfig().getString("permissions.player"));
    }

    private void loadPlayerMoneyRewards(){
        if(core.getConfig().getConfigurationSection("killin-players.reward-money.amounts") != null)
            for(String s: core.getConfig().getConfigurationSection("killin-players.reward-money.amounts").getKeys(false))
                playerMoneyRewards.put(s, core.getConfig().getDouble(String.format("killin-players.reward-money.amounts.%s", s)));
    }

    private void loadPlayerCommandRewards(){
        if(core.getConfig().getConfigurationSection("killin-players.reward-command.commands") != null)
            for(String s: core.getConfig().getConfigurationSection("killin-players.reward-command.commands").getKeys(false))
                playerCommandsReward.put(s, core.getConfig().getStringList(String.format("killin-players.reward-command.commands.%s", s)));
    }

    private void loadEntityCommandRewards() {
        try {
            entityCommandsReward.clear();
            if (core.getConfig().getConfigurationSection("killin-entities.reward-command.commands") != null) {
                for (String s : core.getConfig().getConfigurationSection("killin-entities.reward-command.commands").getKeys(false)) {
                    for (String st : core.getConfig().getConfigurationSection(String.format("killin-entities.reward-command.commands.%s", s)).getKeys(false)) {
                        HashMap<String, List<String>> hashMap = new HashMap<>();
                        if (!entityCommandsReward.isEmpty())
                            if(entityCommandsReward.get(s) != null)
                                hashMap = entityCommandsReward.get(s);
                        hashMap.put(st, core.getConfig().getStringList(String.format("killin-entities.reward-command.commands.%s.%s", s, st)));
                        entityCommandsReward.put(s, hashMap);
                    }
                }
            }
        }catch (NullPointerException e){
            core.getLogger().severe("An error occurred while trying to retrieve entity command rewards, please make sure your config.yml is updated to the latest version.");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void loadEntityMoneyRewards(){
        if(core.getConfig().getConfigurationSection("killin-entities.reward-money.amounts") != null)
            for(String s: core.getConfig().getConfigurationSection("killin-entities.reward-money.amounts").getKeys(false))
                for(String st: core.getConfig().getConfigurationSection(String.format("killin-entities.reward-money.amounts.%s", s)).getKeys(false))
                    entityMoneyRewards.put(s + "-" + st, core.getConfig().getDouble(String.format("killin-entities.reward-money.amounts.%s.%s", s, st)));
    }

    public HashMap<String, Double> getPlayerMoneyRewards() {
        return playerMoneyRewards;
    }

    public HashMap<String, List<String>> getPlayerCommandsReward() {
        return playerCommandsReward;
    }

    public HashMap<String, HashMap<String, List<String>>> getEntityCommandsReward() {
        return entityCommandsReward;
    }

    public HashMap<String, Double> getEntityMoneyRewards() {
        return entityMoneyRewards;
    }

    public boolean isUsingVault() {
        return isUsingVault;
    }

    public void setUsingVault(boolean usingVault) {
        isUsingVault = usingVault;
    }

    public String getBasePlayerPermission() {
        return basePlayerPermission;
    }

    private void setBasePlayerPermission(String basePlayerPermission) {
        if(basePlayerPermission == null)
            this.basePlayerPermission = "kr.player";
        this.basePlayerPermission = basePlayerPermission;
    }
}
