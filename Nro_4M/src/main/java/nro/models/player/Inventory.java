package nro.models.player;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import nro.consts.ConstItem;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.services.InventoryService;
import nro.services.MapService;
import nro.services.Service;

/**
 *
 * @author Hoàng Việt - 0857853150
 * @copyright 💖 GirlkuN 💖
 *
 */
public class Inventory {

    public static final long LIMIT_GOLD = 300_000_000_000L;
    public static final int LIMIT_RUBY = 4_500_000;

    private Player player;
    public Item trainArmor;
    public List<Item> itemsBody;
    public List<Item> itemsBag;
    public List<Item> itemsBox;
    public List<Item> itemsBoxCrackBall;
    public List<Item> itemsMailBox;
    public List<Item> itemsBagTemp; // Đồ hành trang ảo
    public List<Item> itemsBodyTemp; // Đồ body ảo
    public long gold, goldLimit;
    public int gem;
    public int ruby;

    public Inventory(Player player) {
        this.player = player;
        itemsBody = new ArrayList<>();
        itemsBag = new ArrayList<>();
        itemsBodyTemp = new ArrayList<>();
        itemsBagTemp = new ArrayList<>();
        itemsBox = new ArrayList<>();
        itemsBoxCrackBall = new ArrayList<>();
        itemsMailBox = new ArrayList<>();
    }

    public List<Item> getCurrentBody() {
        if (player.isPl() && player != null && player.zone != null && player.zone.map != null
                && MapService.gI().isMapChungSinhBinhDang(player.zone.map.mapId)
                && itemsBodyTemp != null && !itemsBodyTemp.isEmpty()) {
            return itemsBodyTemp;
        }
        return itemsBody;
    }

    public List<Item> getCurrentBag() {
        if (player.isPl() && player != null && player.zone != null && player.zone.map != null
                && MapService.gI().isMapChungSinhBinhDang(player.zone.map.mapId)
                && itemsBagTemp != null && !itemsBagTemp.isEmpty()) {
            return itemsBagTemp;
        }
        return itemsBag;
    }

    public int getGem() {
        return this.gem;
    }

    public long getGold() {
        return this.gold;
    }

    public long getGoldLimit() {
        return goldLimit + LIMIT_GOLD;
    }

    public long getGoldDisplay() {
        long amount = gold;
        if (amount > Integer.MAX_VALUE && !player.isVersionAbove(214)) {
            return Integer.MAX_VALUE;
        }
        return amount;
    }

    public int getParam(Item it, int id) {
        for (ItemOption op : it.itemOptions) {
            if (op != null && op.optionTemplate.id == id) {
                return op.param;
            }
        }
        return 0;
    }

    public boolean haveOption(List<Item> l, int index, int id) {
        Item it = l.get(index);
        if (it != null && it.isNotNullItem()) {
            return it.itemOptions.stream().anyMatch(op -> op != null && op.optionTemplate.id == id);
        }
        return false;
    }

    public long getRuby() {
        return this.ruby;
    }

    public void subGem(int num) {
        this.gem -= num;
    }

    public void subGold(int num) {
        this.gold -= num;
    }

    public void subRuby(int num) {
        this.ruby -= num;
    }

    public void addGold(int gold) {
        this.gold += gold;
        long goldLimit = getGoldLimit();
        if (this.gold > goldLimit) {
            this.gold = goldLimit;
        }
    }

    public void addRuby(int addruby) {
        if (addruby < 0) {
            return;
        }
        this.ruby += addruby;
        if (this.ruby > LIMIT_RUBY) {
            this.ruby = LIMIT_RUBY;
        }
    }
    
    
     public boolean subTieuCoudSky2(Player pl, int quantity) {
        List<Item> itemsWithIdCoudSky = pl.inventory.itemsBag.stream()
                .filter(item -> item.isNotNullItem() && item.template.id == ConstItem.XU_SERVER)
                .collect(Collectors.toList());

        int totalQuantity = itemsWithIdCoudSky.stream().mapToInt(item -> item.quantity).sum();

        if (totalQuantity < quantity) {
            return false;
        }

        for (Item item : itemsWithIdCoudSky) {
            if (quantity <= 0) {
                break;
            }
            int subQuantity = Math.min(quantity, item.quantity);
            InventoryService.gI().subQuantityItemsBag(pl, item, subQuantity);
            pl.event.addEventPointTP(subQuantity);
            quantity -= subQuantity;
        }
        InventoryService.gI().sendItemBags(pl);
        return true;
    }
     
    public boolean subTieuCoudSky(Player pl, int quantity) {
        List<Item> itemsWithIdCoudSky = pl.inventory.itemsBag.stream()
                .filter(item -> item.isNotNullItem() && item.template.id == ConstItem.XU_DAC_BIET)
                .collect(Collectors.toList());

        int totalQuantity = itemsWithIdCoudSky.stream().mapToInt(item -> item.quantity).sum();

        if (totalQuantity < quantity) {
            return false;
        }

        for (Item item : itemsWithIdCoudSky) {
            if (quantity <= 0) {
                break;
            }
            int subQuantity = Math.min(quantity, item.quantity);
            InventoryService.gI().subQuantityItemsBag(pl, item, subQuantity);
            pl.event.addEventPointTP(subQuantity);
            quantity -= subQuantity;
        }
        InventoryService.gI().sendItemBags(pl);
        return true;
    }

    //--------------------------------------------------------------------------
    public boolean checkdusoluongitem(Player pl, int itemId, int quantity) {
        int totalQuantity = pl.inventory.itemsBag.stream()
                .filter(item -> item.isNotNullItem() && item.template.id == itemId)
                .mapToInt(item -> item.quantity)
                .sum();
        return totalQuantity >= quantity;
    }

    public void subItemInBag(Player pl, int itemId, int quantity) {
        List<Item> items = pl.inventory.itemsBag.stream()
                .filter(item -> item.isNotNullItem() && item.template.id == itemId)
                .collect(Collectors.toList());

        for (Item item : items) {
            if (quantity <= 0) {
                break;
            }
            int subQuantity = Math.min(quantity, item.quantity);
            InventoryService.gI().subQuantityItemsBag(pl, item, subQuantity);
            quantity -= subQuantity;
        }
        InventoryService.gI().sendItemBags(pl);
    }
    //--------------------------------------------------------------------------

    public void dispose() {
        this.player = null;
        if (this.trainArmor != null) {
            this.trainArmor.dispose();
        }
        this.trainArmor = null;
        if (this.itemsBody != null) {
            for (Item it : this.itemsBody) {
                it.dispose();
            }
            this.itemsBody.clear();
        }
        if (this.itemsBag != null) {
            for (Item it : this.itemsBag) {
                it.dispose();
            }
            this.itemsBag.clear();
        }
        if (this.itemsBodyTemp != null) {
            for (Item it : this.itemsBodyTemp) {
                it.dispose();
            }
            this.itemsBodyTemp.clear();
        }
        if (this.itemsBagTemp != null) {
            for (Item it : this.itemsBagTemp) {
                it.dispose();
            }
            this.itemsBagTemp.clear();
        }
        if (this.itemsBox != null) {
            for (Item it : this.itemsBox) {
                it.dispose();
            }
            this.itemsBox.clear();
        }
        if (this.itemsBoxCrackBall != null) {
            for (Item it : this.itemsBoxCrackBall) {
                it.dispose();
            }
            this.itemsBoxCrackBall.clear();
        }
        if (this.itemsMailBox != null) {
            for (Item it : this.itemsMailBox) {
                it.dispose();
            }
            this.itemsMailBox.clear();
        }
        this.itemsBody = null;
        this.itemsBag = null;
        this.itemsBodyTemp = null;
        this.itemsBagTemp = null;
        this.itemsBox = null;
        this.itemsBoxCrackBall = null;
        this.itemsMailBox = null;
    }
}
