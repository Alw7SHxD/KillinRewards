package me.Alw7SHxD.KillinRewards.libs;

import me.Alw7SHxD.KillinRewards.Core;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * KillinRewards (2017) made by Alw7SHxD (C) 2011-2018
 * @author Alw7SHxD
 *
 * This class was created to check updates using SpigotMC's legacy API.
 * @author iShadey
 */

public class SpigotUpdater {
    private Core core;
    private int project = 44419;
    private URL checkURL;
    private String newVersion;

    public SpigotUpdater(Core core) {
        this.core = core;
        this.newVersion = core.getDescription().getVersion();
        try {
            this.checkURL = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + project);
        } catch (MalformedURLException e) {
        }
    }

    public int getProjectID() {
        return project;
    }

    public JavaPlugin getPlugin() {
        return core;
    }

    public String getLatestVersion() {
        return newVersion;
    }

    public String getVersion() {
        String s = core.getDescription().getVersion();
        if (this.newVersion.startsWith("("))
            s = String.format("(%s", s);
        if (this.newVersion.endsWith(")"))
            s = String.format("%s)", s);
        return s;
    }

    public String getResourceURL() {
        return "https://www.spigotmc.org/resources/" + project;
    }

    public boolean checkForUpdates() throws Exception {
        URLConnection con = checkURL.openConnection();
        this.newVersion = new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
        return getVersion().compareTo(getLatestVersion()) < 0;
    }

    public void check(CommandSender commandSender) {
        try {
            if (checkForUpdates()) {
                if (isConsole(commandSender)) commandSender.sendMessage(Color.code("&3----------------------------------------------"));
                commandSender.sendMessage("");
                commandSender.sendMessage(Color.code("&7Found a newer version for &a&lKillinRewards"));
                commandSender.sendMessage(Color.code(String.format("&7please make sure to update to version &a&l%s", getLatestVersion())));
                commandSender.sendMessage(Color.code("&eSpigot Download &8@ &e&nhttps://goo.gl/TQEZuZ"));
                commandSender.sendMessage("");
                if (isConsole(commandSender)) commandSender.sendMessage(Color.code("&3----------------------------------------------"));

            } else
                commandSender.sendMessage(Color.code((isConsole(commandSender) ? "[KillinRewards] " : "") + String.format("&7Currently running the latest version of &a&lKillinRewards &7(&a&l%s&7)", core.getDescription().getVersion())));
        } catch (Exception e) {
            commandSender.sendMessage(Color.code("&cCouldn't check for &aKillinRewards &cupdates."));
            commandSender.sendMessage(Color.code("&cPlease check https://goo.gl/TQEZuZ occasionally for an update!"));
            e.printStackTrace();
        }
    }

    private boolean isConsole(CommandSender commandSender) {
        return commandSender instanceof ConsoleCommandSender;
    }
}
