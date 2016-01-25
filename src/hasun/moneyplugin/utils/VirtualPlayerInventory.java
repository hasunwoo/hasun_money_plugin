package hasun.moneyplugin.utils;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * 플레이어에만 적용할 수 있는 가상인벤토리
 * 아이템 변경만 지원합니다.아직 구현되지 않은 메서드도 있습니다.
 */
public class VirtualPlayerInventory implements Inventory {
    //넘친 아이템의 개수
    public int overflowCount;
    private Inventory inv;
    private Map<Integer, ItemStack> clone = new HashMap<Integer, ItemStack>();

    public VirtualPlayerInventory(Player player) {
        Inventory inventory = player.getInventory();
        this.inv = inventory;
        for (int i = 0; i < inv.getSize(); i++) {
            clone.put(i, inv.getItem(i));
        }
    }

    public boolean commit(InventoryChecker inventoryChecker) {
        if (inventoryChecker.checkInventory(clone, inv)) {
            commit();
            return true;
        }
        return false;
    }

    public void commit() {
        for (int i = 0; i < inv.getSize(); i++) {
            ItemStack item = clone.get(i);
            if (item != null && item.getAmount() == 0) {
                item = null;
            }
            inv.setItem(i, item);
        }
    }

    @Override
    public int getSize() {
        return inv.getSize();
    }

    @Override
    public int getMaxStackSize() {
        return inv.getMaxStackSize();
    }

    @Override
    public void setMaxStackSize(int i) {
        inv.setMaxStackSize(i);
    }

    @Override
    public String getName() {
        return inv.getName();
    }

    @Override
    public ItemStack getItem(int i) {
        checkBound(i);
        return clone.get(i);
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
        checkBound(i);
        if (itemStack != null && itemStack.getAmount() == 0) {
            itemStack = null;
        }
        clone.put(i, itemStack);
    }


    @Override
    public HashMap<Integer, ItemStack> addItem(ItemStack... itemStacks) throws IllegalArgumentException {
        HashMap<Integer, ItemStack> failed = new HashMap<Integer, ItemStack>();
        for (int j = 0; j < itemStacks.length; j++) {
            ItemStack item = itemStacks[j];
            if (item != null) {
                int left = item.getAmount();
                for (int i = 0; i < inv.getSize(); i++) {
                    ItemStack item0 = clone.get(i);
                    if (item0 == null) {
                        clone.put(i, item);
                        left -= item.getAmount();
                        break;
                    } else if (item.isSimilar(item0)) {
                        int maxStackSize = item0.getMaxStackSize();
                        if ((maxStackSize - item0.getAmount()) >= left) {
                            item0.setAmount(item0.getAmount() + left);
                            clone.put(i, item0);
                            left = 0;
                            break;
                        } else {
                            int amount = maxStackSize - item0.getAmount();
                            item0.setAmount(item0.getMaxStackSize());
                            clone.put(i, item0);
                            left -= amount;
                        }
                    }
                }
                if (left > 0) {
                    overflowCount++;
                    ItemStack newStack = new ItemStack(item);
                    newStack.setAmount(left);
                    failed.put(j, newStack);
                }
            }
        }
        return failed;
    }

    @Override
    public HashMap<Integer, ItemStack> removeItem(ItemStack... itemStacks) throws IllegalArgumentException {
        //TODO implement method
        return null;
    }

    @Override
    public ItemStack[] getContents() {
        ItemStack[] contents = new ItemStack[inv.getSize()];
        for (int i = 0; i < inv.getSize(); i++) {
            contents[i] = clone.get(i);
        }
        return contents;
    }

    @Override
    public void setContents(ItemStack[] itemStacks) throws IllegalArgumentException {
        for (int i = 0; i < inv.getSize(); i++) {
            clone.put(i, itemStacks[i]);
        }
    }

    @Override
    public boolean contains(int materialID) {
        for (ItemStack i : clone.values()) {
            if (i != null && i.getTypeId() == materialID) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean contains(Material material) throws IllegalArgumentException {
        return contains(material.getId());
    }

    @Override
    public boolean contains(ItemStack itemStack) {
        return clone.values().contains(itemStack);
    }

    @Override
    public boolean contains(int materialID, int amount) {
        if (amount <= 0) return true;
        int total = 0;
        for (ItemStack i : clone.values()) {
            if (i != null && i.getTypeId() == materialID) {
                total += i.getAmount();
            }
        }
        return amount >= total;
    }

    @Override
    public boolean contains(Material material, int amount) throws IllegalArgumentException {
        return contains(material.getId(), amount);
    }

    @Override
    public boolean contains(ItemStack itemStack, int amount) {
        if (itemStack == null) return false;
        if (amount <= 0) return true;
        int total = 0;
        for (ItemStack i : clone.values()) {
            if (itemStack.equals(i)) {
                total += i.getAmount();
            }
        }
        return amount >= total;
    }

    @Override
    public boolean containsAtLeast(ItemStack itemStack, int amount) {
        if (itemStack == null) return false;
        if (amount <= 0) return true;
        int total = 0;
        for (ItemStack i : clone.values()) {
            if (itemStack.isSimilar(i)) {
                total += i.getAmount();
            }
        }
        return amount >= total;
    }

    @Override
    public HashMap<Integer, ? extends ItemStack> all(int i) {
        //TODO implement method
        return null;
    }

    @Override
    public HashMap<Integer, ? extends ItemStack> all(Material material) throws IllegalArgumentException {
        //TODO implement method
        return null;
    }

    @Override
    public HashMap<Integer, ? extends ItemStack> all(ItemStack itemStack) {

        //TODO implement method
        return null;
    }

    @Override
    public int first(int materialID) {
        for (int i = 0; i < inv.getSize(); i++) {
            ItemStack item = clone.get(i);
            if (item != null && item.getTypeId() == materialID) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public int first(Material material) throws IllegalArgumentException {
        return first(material.getId());
    }

    @Override
    public int first(ItemStack itemStack) {
        return -1;
    }

    @Override
    public int firstEmpty() {
        for (int i = 0; i < inv.getSize(); i++) {
            ItemStack item = clone.get(i);
            if (item == null) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void remove(int materialID) {
        for (int i = 0; i < inv.getSize(); i++) {
            ItemStack item = clone.get(i);
            if (item == null && item.getTypeId() == materialID) {
                clone.remove(i);
            }
        }
    }

    @Override
    public void remove(Material material) throws IllegalArgumentException {
        remove(material.getId());
    }

    @Override
    public void remove(ItemStack itemStack) {
        clone.remove(itemStack);
    }

    @Override
    public void clear(int i) {
        setItem(i, null);
    }

    @Override
    public void clear() {
        for (int i = 0; i < inv.getSize(); i++) {
            clear(i);
        }
    }

    @Override
    public List<HumanEntity> getViewers() {
        return inv.getViewers();
    }

    @Override
    public String getTitle() {
        return inv.getTitle();
    }

    @Override
    public InventoryType getType() {
        return inv.getType();
    }

    @Override
    public InventoryHolder getHolder() {
        return inv.getHolder();
    }

    @Override
    public ListIterator<ItemStack> iterator() {
        return new InventoryIterator(new ArrayList<ItemStack>(clone.values()), inv.getSize());
    }

    @Override
    public ListIterator<ItemStack> iterator(int i) {
        return null;
    }

    private void checkBound(int index) {
        if (index > inv.getSize()) throw new ArrayIndexOutOfBoundsException(index);
    }

    public interface InventoryChecker {
        boolean checkInventory(Map<Integer, ItemStack> clone, Inventory original);
    }

    public static class InventoryIterator implements ListIterator<ItemStack> {
        private List<ItemStack> list;
        private int size;
        private int current = 0;

        public InventoryIterator(List<ItemStack> values, int size) {
            this.list = values;
            this.size = size;
        }

        @Override
        public boolean hasNext() {
            return current != size;
        }

        @Override
        public ItemStack next() {
            if (hasNext()) {
                return list.get(current++);
            }
            return null;
        }

        @Override
        public boolean hasPrevious() {
            return current != 0;
        }

        @Override
        public ItemStack previous() {
            if (hasPrevious()) {
                return list.get(--current);
            }
            return null;
        }

        @Override
        public int nextIndex() {
            return current;
        }

        @Override
        public int previousIndex() {
            return current - 1;
        }

        @Override
        public void remove() {
            list.remove(current);
        }

        @Override
        public void set(ItemStack itemStack) {
            list.set(current, itemStack);
        }

        @Override
        public void add(ItemStack itemStack) {
            if (list.size() < size) {
                list.add(itemStack);
            }
        }
    }
}
