package hasun.moneyplugin.utils;

import hasun.moneyitem.items.IMoney;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Method;
import java.math.BigInteger;


public class BalanceManager {
    public static BigInteger countMoney(Player player) {
        return countMoney(player.getInventory());
    }

    public static BigInteger countMoney(Inventory inv) {
        BigInteger balance = BigInteger.ZERO;
        if (inv != null) {
            for (ItemStack stack : inv) {
                if (stack != null) {
                    //돈일때 처리
                    if (stack.getType().toString().equals("MONEY_HASUN_MONEY_HASUNITEMMONEY")) {
                        BigInteger money = BigInteger.valueOf(stack.getDurability());
                        money = money.multiply(BigInteger.valueOf(stack.getAmount()));
                        balance = balance.add(money);
                    }
                }
            }
        }
        return balance;
    }

    private static int readValueFromCheck(ItemStack itemStack) {
        try {
            Bukkit.getConsoleSender().sendMessage(itemStack.getClass().getSuperclass().getName());
            Class c = Class.forName("hasun.moneyitem.items.ItemCheck", false, BalanceManager.class.getClassLoader());
            IMoney m = (IMoney) c.newInstance();
            return m.getValue(asNMSItemStack(itemStack));
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("Reflection from readValueFromCheck failed.");
            e.printStackTrace();
        }
        return 0;
    }

    private static Object asNMSItemStack(ItemStack itemStack) {
        String Version = Bukkit.getServer().getClass().getPackage().getName().replace(".", ",").split(",")[3];
        try {
            //craftbukkit 패키지는 bukkit API와 마인크래프트 코드와의 상호작용을 담당합니다.
            Class c = Class.forName("org.bukkit.craftbukkit." + Version + ".inventory.CraftItemStack");
            //bukkit API의 ItemStack을 마인크래프트 코드의 ItemStack으로 변환합니다.
            Method m = c.getMethod("asNMSCopy", ItemStack.class);
            return m.invoke(null, itemStack);
        } catch (Exception ex) {
            Bukkit.getConsoleSender().sendMessage("Conversion from Bukkit ItemStack to NMS ItemStack failed.");
            ex.printStackTrace();
            return null;
        }
    }
}