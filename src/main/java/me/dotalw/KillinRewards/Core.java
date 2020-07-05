package me.dotalw.KillinRewards;

import me.dotalw.KillinRewards.commands.KillinRewardsCommand;
import me.dotalw.KillinRewards.evnets.EntityDeath;
import me.dotalw.KillinRewards.evnets.PlayerDeath;
import me.dotalw.KillinRewards.libs.Cache;
import me.dotalw.KillinRewards.libs.SpigotUpdater;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * KillinRewards (2017) was created by dotalw (C) 2011-2020
 * Licensed under the MIT license.
 */
@SuppressWarnings("ConstantConditions")
public class Core extends JavaPlugin {
    private Cache cache;
    private Economy econ = null;

    @Override
    public void onEnable() {
        SpigotUpdater updateChecker = new SpigotUpdater(this);
        updateChecker.check(getServer().getConsoleSender());

        saveDefaultConfig();

        this.cache = new Cache(this);
        this.cache.reload();

        if (!setupEconomy()) {
            getLogger().info("Please make sure to have an economy plugin installed,");
            getLogger().info("such as EssCore, or any other economy plugin, and Vault as well!");
            getLogger().info("Please note that you cannot reward players with money when you don't have vault and an economy plugin installed.");
            cache.setUsingVault(false);
        }else
            cache.setUsingVault(true);

        if(getConfig().getDouble("kill-in-rewards") != 0.6d || getConfig().get("kill-in-rewards") == null) {
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
        getCommand("killinrewards").setExecutor(new KillinRewardsCommand(this));
    }

    public Cache getCache() {
        return cache;
    }

    public Economy getEcon() {
        return econ;
    }
}
