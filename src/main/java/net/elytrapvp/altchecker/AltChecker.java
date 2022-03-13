package net.elytrapvp.altchecker;

import net.elytrapvp.altchecker.commands.AltsCMD;
import net.md_5.bungee.api.plugin.Plugin;

public final class AltChecker extends Plugin {
    private SettingsManager settingsManager;
    private MySQL mySQL;

    /**
     * This is called when BungeeCord first loads the plugin.
     */
    @Override
    public void onEnable() {
        // Creates or Loads the configuration file.
        settingsManager = new SettingsManager(this);

        // Connects to the mysql database.
        mySQL = new MySQL(this);
        // Connection is opened async.
        getProxy().getScheduler().runAsync(this, () -> mySQL.openConnection());

        // We must also tell BungeeCord that our commands exist.
        getProxy().getPluginManager().registerCommand(this, new AltsCMD(this));
    }

    /**
     * Be able to connect to MySQL.
     * @return MySQL.
     */
    public MySQL getMySQL() {
        return mySQL;
    }

    /**
     * Get the Settings Manager, which gives us access to the plugin Configuration.
     * @return Settings Manager.
     */
    public SettingsManager getSettingsManager() {
        return settingsManager;
    }
}
