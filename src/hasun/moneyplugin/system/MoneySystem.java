package hasun.moneyplugin.system;

import hasun.moneyplugin.utils.BalanceManager;
import hasun.moneyplugin.utils.BigIntegerUtil;
import hasun.moneyplugin.utils.MoneyUnitConversion;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Map;

public class MoneySystem {
    public static double getAmount(String playerName) {
        return getAmount(Bukkit.getServer().getPlayer(playerName));
    }

    public static double getAmount(OfflinePlayer offlineplayer) {
        return getAmount(offlineplayer);
    }

    public static double getAmount(Player player) {
        BigInteger amount = BalanceManager.countMoney(player);
        return BigIntegerUtil.convertToDouble(amount);
    }

    public static boolean withdraw(OfflinePlayer offlineplayer) {
        return withdraw(offlineplayer);
    }

    public static boolean withdraw(Player player, double amount) {
        BigInteger a = new BigInteger(Long.toString((long) amount));
        return takeMoney(player, a);
    }

    public static boolean takeMoney(Player player, BigInteger amount) {
        //만약 가져가려는 돈이 플레이어가 가지고 있는 돈보다 많으면 false 를 리턴한다.
        if (BalanceManager.countMoney(player).compareTo(amount) < 0) return false;
        BigInteger left = amount;
        Inventory inv = player.getInventory();
        for (int i = 0; i < MoneyUnitConversion.KOREA_UNIT.length; i++) {
            //만약 요청된 돈을 다 가져갔을경우 리턴한다
            if (left.equals(BigInteger.ZERO)) break;
            int unit = MoneyUnitConversion.KOREA_UNIT[i];
            int quantity = left.divide(BigInteger.valueOf(unit)).intValue();
            //만약 현재 화폐단위의 돈 갯수를 다 가져갈수 있는경우
            if (getMoneyCount(player, unit) >= quantity) {
                takeMoneyInternal(player, unit, quantity);
                left = left.subtract(BigInteger.valueOf(unit * quantity));
            } else {
                //여러 화폐단위로 나눠서 가져가야 하는경우
                int a = getMoneyCount(player, unit);
                takeMoneyInternal(player, unit, a);
                left = left.subtract(BigInteger.valueOf(unit * a));
            }
        }
        if (left.equals(BigInteger.ZERO)) return true;
        //남은 돈을 환전한다
        exchangeMoneyInternal(player, left);
        //환전시킨 돈을 가져간다
        return takeMoney(player, left);
    }

    private static int getMoneyCount(Player player, int unit) {
        int count = 0;
        Inventory inv = player.getInventory();
        for (ItemStack i : inv) {
            if (i != null && i.getType().toString().equals("MONEY_HASUN_MONEY_HASUNITEMMONEY") && i.getDurability() == unit) {
                count += i.getAmount();
            }
        }
        return count;
    }

    private static boolean takeMoneyInternal(Player player, int unit, int amount) {
        //만약 가져가려는 돈의 개수가 현재 플레이어가 가진 돈의 개수보다 많으면 false 를 리턴한다
        if (getMoneyCount(player, unit) < amount) return false;
        int taken = 0;
        Inventory inv = player.getInventory();
        for (int j = 0; j < inv.getSize(); j++) {
            ItemStack i = inv.getItem(j);
            if (i != null && i.getType().toString().equals("MONEY_HASUN_MONEY_HASUNITEMMONEY") && i.getDurability() == unit) {
                if (taken == unit) return true;
                if (i.getAmount() >= amount) {
                    taken += amount;
                    i.setAmount(i.getAmount() - amount);
                    inv.setItem(j, i);
                } else {
                    taken += i.getAmount();
                    i.setAmount(0);
                    inv.setItem(j, i);
                }
            }
        }
        return false;
    }

    //돈을 환전합니다. 타겟티어가 될때까지 돈을 하위티어로 쪼갭니다.
    private static boolean exchangeMoneyInternal(Player player, BigInteger amount) {
        Map<Integer, Integer> split = MoneyUnitConversion.convertToMoneyUnit(amount, MoneyUnitConversion.KOREA_UNIT);
        for (Integer targetTier : split.keySet()) {
            if (split.get(targetTier) == 0) continue;
            //현재 돈의 티어를 타겟티어로 설정합니다
            int currentTier = targetTier;
            while (true) {
                //만약 플레이어가 가지고 있는 현재 티어의 돈이 없으면 티어를 한단계씩 높임니다
                if (getMoneyCount(player, currentTier) == 0) {
                    currentTier = getNextTier(currentTier);
                    //만약 최상위 티어까지 탐색해도 못찾으면 false 를 리턴합니다
                    if (currentTier == -1) return false;
                    continue;
                }
                break;
            }
            //타겟티어에 도달할 때 까지 하위티어로 쪼갭니다
            while (targetTier != currentTier) {
                //만약 돈을 쪼개는데 성공하면 현재 티어를 하나 낮춤니다
                if (splitToLowerTier(player, currentTier)) {
                    currentTier = getPreviousTier(currentTier);
                } else {
                    return false;
                }
            }
            break;
        }
        return true;
    }

    //상위단계의 화폐를 구합니다 ex)getNextTier(10) = 100;
    //오류시 -1 리턴
    private static int getNextTier(int a) {
        int[] arr = MoneyUnitConversion.KOREA_UNIT;
        Arrays.sort(arr);
        for (int i = 0; i < MoneyUnitConversion.KOREA_UNIT.length; i++) {
            if (MoneyUnitConversion.KOREA_UNIT[i] == a) {
                return MoneyUnitConversion.KOREA_UNIT[i + 1];
            }
        }
        return -1;
    }

    //하위단위의 화폐를 구합니다 ex)getPreviousTier(100) = 10
    //오류시 -1 리턴
    private static int getPreviousTier(int a) {
        int[] arr = MoneyUnitConversion.KOREA_UNIT;
        Arrays.sort(arr);
        for (int i = 0; i < MoneyUnitConversion.KOREA_UNIT.length; i++) {
            if (MoneyUnitConversion.KOREA_UNIT[i] == a) {
                return MoneyUnitConversion.KOREA_UNIT[i - 1];
            }
        }
        return -1;
    }

    //ex) 500원 하나를 100원 5개로 나눕니다
    private static boolean splitToLowerTier(Player player, int unit) {
        if (unit == 1) return false;
        int amount = getMoneyCount(player, unit);
        if (amount > 0) {
            takeMoneyInternal(player, unit, 1);
            int prev = getPreviousTier(unit);
            ItemStack money = new ItemStack(Material.getMaterial("MONEY_HASUN_MONEY_HASUNITEMMONEY"), unit / prev, (short) prev);
            player.getInventory().addItem(money);
            return true;
        } else {
            return false;
        }
    }
}