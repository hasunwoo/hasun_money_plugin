package hasun.moneyplugin;

import hasun.moneyplugin.system.MoneySystem;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class MoneyPlugin extends JavaPlugin {
    @Override
    public void onLoad() {

    }

    @Override
    public void onDisable() {

    }

    @Override
    public void onEnable() {

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equals("takeMoney")) {
            boolean success = MoneySystem.withdraw((Player) sender, Integer.parseInt(args[0]));
            sender.sendMessage(Boolean.toString(success));
        }
        return false;
    }
}
