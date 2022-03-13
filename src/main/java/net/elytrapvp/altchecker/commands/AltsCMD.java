package net.elytrapvp.altchecker.commands;

import net.elytrapvp.altchecker.AltChecker;
import net.elytrapvp.altchecker.utils.ChatUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.protocol.packet.Chat;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class runs the alts command, which allows to check all accounts using the same ip.
 */
public class AltsCMD extends Command {
    private final AltChecker plugin;

    /**
     * Creates the /alts command with the permission "staff.alts" and no arguments.
     * @param plugin Instance of the plugin.
     */
    public AltsCMD(AltChecker plugin) {
        super("alts", "staff.alts");
        this.plugin = plugin;
    }

    /**
     * This is the code that runs when the command is sent.
     * @param sender The player (or console) that sent the command.
     * @param args The arguments of the command.
     */
    @Override
    public void execute(CommandSender sender, String[] args) {
        // Only players should be able to message each other.
        if(!(sender instanceof ProxiedPlayer player)) {
            return;
        }

        // Make sure they're using the command properly.
        if(args.length < 1) {
            ChatUtils.chat(player, "&c&lUsage &8» &c/alts [player]");
            return;
        }

        // Runs MySQL async.
        plugin.getProxy().getScheduler().runAsync(plugin, () -> {
            try {
                PreparedStatement statement = plugin.getMySQL().getConnection().prepareStatement("SELECT * FROM player_info WHERE username = ?");
                statement.setString(1, args[0]);
                ResultSet results = statement.executeQuery();

                if(results.next()) {
                    String target = results.getString(2);
                    String ip = results.getString(3);

                    PreparedStatement statement2 = plugin.getMySQL().getConnection().prepareStatement("SELECT * FROM player_info WHERE ip = ?");
                    statement2.setString(1, ip);
                    ResultSet results2 = statement2.executeQuery();

                    ChatUtils.chat(player, "&aAlts of &f" + target + "&a:");
                    while(results2.next()) {
                        ChatUtils.chat(player, "&7- &f" + results2.getString(2));
                    }
                }
                else {
                    ChatUtils.chat(player, "&cError &8» &cThat player has not played.");
                    return;
                }
            }
            catch (SQLException exception) {
                exception.printStackTrace();
            }
        });
    }
}