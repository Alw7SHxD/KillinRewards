package me.Alw7SHxD.KillinRewards;

import me.Alw7SHxD.KillinRewards.commands.*;
import me.Alw7SHxD.KillinRewards.evnets.EntityDeath;
import me.Alw7SHxD.KillinRewards.evnets.PlayerDeath;
import me.Alw7SHxD.KillinRewards.libs.Lists;
import me.Alw7SHxD.KillinRewards.libs.SpigotUpdater;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * KillinRewards was created by Alw7SHxD (C) 2017
 */
public class Core extends JavaPlugin {
    private Lists lists;
    private Economy econ = null;

    @Override
    public void onEnable() {
        SpigotUpdater updateChecker = new SpigotUpdater(this);
        updateChecker.check(getServer().getConsoleSender());

        saveDefaultConfig();

        this.lists = new Lists(this);
        this.lists.reload();

        if (!setupEconomy()) {
            getLogger().severe("Please make sure to have an economy plugin installed,");
            getLogger().severe("such as EssCore (made by: Alw7SHxD). or any other economy plugin, and Vault too.");
            getLogger().info("Please note that you cannot reward players with money when you don't have vault and an economy plugin installed.");
            lists.setUsingVault(false);
        }else
            lists.setUsingVault(true);

        if(getConfig().getDouble("kill-in-rewards") != 0.5d || getConfig().get("kill-in-rewards") == null) {
            getLogger().warning("Your config.yml is old, please make sure to update it to the latest version.");
            getLogger().warning("Or you can manually update it by following the default one @ https://goo.gl/TQEZuZ");
        }

        registerL(
                new PlayerDeath(this),
                new EntityDeath(this)
        );
        registerC();
    }

    @Override
    public void onDisable() {
        super.onDisable();
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }

    private void registerL(Listener... listeners){
        for(Listener listener: listeners)
            getServer().getPluginManager().registerEvents(listener, this);
    }

    private void registerC(){
        getCommand("killinrewards").setExecutor(new KillinRewards(this));
    }

    public Lists getLists() {
        return lists;
    }

    public Economy getEcon() {
        return econ;
    }
}
