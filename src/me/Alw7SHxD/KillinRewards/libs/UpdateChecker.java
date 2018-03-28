package me.Alw7SHxD.KillinRewards.libs;

import com.sun.istack.internal.NotNull;
import me.Alw7SHxD.KillinRewards.Core;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.UnknownHostException;

/**
 * KillinRewards was created by Alw7SHxD (C) 2017
 */
public class UpdateChecker {
    private Core core;

    public UpdateChecker(Core core) {
        this.core = core;
    }

    private String getLatestVersion() {
        try {
            int id = 44419;
            HttpURLConnection con = (HttpURLConnection) new URL("http://www.spigotmc.org/api/general.php").openConnection();
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.getOutputStream().write(
                    ("key=98BE0FE67F88AB82B4C197FAF1DC3B69206EFDCC4D3B80FC83A00037510B99B4&resource=" + id).getBytes("UTF-8"));
            String latestVersion = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
            return latestVersion.substring(1, latestVersion.length() - 1);
        } catch (UnknownHostException e){
            core.getLogger().warning("had an issue while trying to get the latest version.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void check(@NotNull String currentUpdate, @NotNull Player player){
        String latestVersion = getLatestVersion();
        if(latestVersion == null) return;

        if(currentUpdate.compareTo(latestVersion) < 0){
            player.sendMessage(Color.code("&a&lKillinRewards &8» &7found a new version (&c&l" + latestVersion + "&7)"));
            player.sendMessage(Color.code("&a&lKillinRewards &8» &7make sure to update to the latest version."));
            player.sendMessage(Color.code("&a&lKillinRewards &8» &7https://goo.gl/TQEZuZ"));
        }else{
            player.sendMessage(Color.code("&a&lKillinRewards &8» &7running the latest version."));
        }
    }

    public void check(@NotNull String currentUpdate, @NotNull CommandSender sender){
        String latestVersion = getLatestVersion();
        if(latestVersion == null) return;

        if(currentUpdate.compareTo(latestVersion) < 0){
            sender.sendMessage(Color.code("&a&lKillinRewards &8» &7found a new version (&c&l" + latestVersion + "&7)"));
            sender.sendMessage(Color.code("&a&lKillinRewards &8» &7make sure to update to the latest version."));
            sender.sendMessage(Color.code("&a&lKillinRewards &8» &7https://goo.gl/TQEZuZ"));
        }else{
            sender.sendMessage(Color.code("&a&lKillinRewards &8» &7currently running the latest version."));
        }
    }

    public void check(Core core, @NotNull String currentUpdate){
        String latestVersion = getLatestVersion();
        if(latestVersion == null) return;

        if(currentUpdate.compareTo(latestVersion) < 0){
            core.getLogger().warning(ChatColor.YELLOW + "---------------------");
            core.getLogger().warning(ChatColor.RED + " FOUND NEW VERSION FOR");
            core.getLogger().warning(ChatColor.RED + " KillinRewards, please update");
            core.getLogger().warning(ChatColor.RED + " https://goo.gl/TQEZuZ");
            core.getLogger().warning(ChatColor.YELLOW + "---------------------");
        }else{
            core.getLogger().info("running latest version (" + currentUpdate + ")");
        }
    }
}