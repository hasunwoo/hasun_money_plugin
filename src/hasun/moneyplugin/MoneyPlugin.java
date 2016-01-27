package hasun.moneyplugin;

import hasun.moneyplugin.system.MoneySystem;
import hasun.moneyplugin.utils.BalanceManager;
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
        System.out.println("Testing ...");
        if (command.getName().equals("takeMoney")) {
            MoneySystem.withdraw((Player) sender, Integer.parseInt(args[0]));
        } else if (command.getName().equals("balance")) {
            sender.sendMessage("돈: " + BalanceManager.countMoney((Player) sender));
        }
        return false;
    }
}
