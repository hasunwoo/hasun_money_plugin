package hasun.moneyplugin.utils;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * 플레이어에만 적용할 수 있는 가상인벤토리
 */
public class VirtualPlayerInventory {
    private Inventory inv;
    private Map<Integer, ItemStack> invclone = new HashMap<Integer, ItemStack>();
}
