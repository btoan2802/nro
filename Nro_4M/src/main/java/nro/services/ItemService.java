package nro.services;

import nro.models.item.ItemOptionTemplate;
import nro.models.item.ItemTemplate;
import nro.models.consignment.ConsignmentItem;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.shop.ItemShop;
import nro.server.Manager;
import nro.utils.TimeUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Random;
import nro.models.player.Player;
import nro.models.shop.Shop;
import nro.models.shop.TabShop;
import nro.utils.Util;

public class ItemService {

    private static ItemService i;

    public static ItemService gI() {
        if (i == null) {
            i = new ItemService();
        }
        return i;
    }

    public Item createItemNull() {
        Item item = new Item();
        return item;
    }

    public Item createItemFromItemShop(ItemShop itemShop) {
        Item item = new Item();
        item.template = itemShop.temp;
        item.quantity = 1;
        item.content = item.getContent();
        item.info = item.getInfo();
        for (ItemOption io : itemShop.options) {
            item.itemOptions.add(new ItemOption(io)); // Thêm mới itemOption vào item
            // Loại bỏ và thêm option template nếu cần thiết
            removeAndAddOptionTemplate(item.itemOptions, new ItemOption(io).optionTemplate.id);
        }
        return item;
    }

    public Item copyItem(Item item) {
        Item it = new Item();
        it.itemOptions = new ArrayList<>();
        it.template = item.template;
        it.splHistory = item.splHistory;
        it.info = item.info;
        it.content = item.content;
        it.quantity = item.quantity;
        it.createTime = item.createTime;
        for (ItemOption io : item.itemOptions) {
            it.itemOptions.add(new ItemOption(io));
        }
        return it;
    }

    public ConsignmentItem convertToConsignmentItem(Item item) {
        ConsignmentItem it = new ConsignmentItem();
        it.itemOptions = new ArrayList<>();
        it.template = item.template;
        it.info = item.info;
        it.content = item.content;
        it.quantity = item.quantity;
        it.createTime = item.createTime;
        for (ItemOption io : item.itemOptions) {
            it.itemOptions.add(new ItemOption(io));
        }
        it.setPriceGold(-1);
        it.setPriceGem(-1);
        return it;
    }

    public Item createNewItem(short tempId) {
        return createNewItem(tempId, 1);
    }

    public Item createNewItem(short tempId, int quantity) {
        Item item = new Item();
        item.template = getTemplate(tempId);
        item.quantity = quantity;
        item.createTime = System.currentTimeMillis();

        item.content = item.getContent();
        item.info = item.getInfo();
        return item;
    }

    public void removeAndAddOptionTemplate(List<ItemOption> itemOptions, int removeId) {
        switch (removeId) {
            case 172 -> {
                itemOptions.removeIf(io -> io.optionTemplate.id == removeId); // xóa option cũ
                // Thêm ngẫu nhiên SD, HP, KI
                int[] ids = {50, 77, 103};
                for (int id : ids) {
                    int param = Util.isTrue(80, 100)
                            ? Util.nextInt(30, 40)
                            : Util.nextInt(40, 60);
                    itemOptions.add(new ItemOption(id, param));
                }
            }
            case 218 -> {
                itemOptions.removeIf(io -> io.optionTemplate.id == removeId); // xóa option cũ
                // Thêm ngẫu nhiên SD, HP, KI
                int[] ids = {50, 77, 103};
                for (int id : ids) {
                    int param = Util.isTrue(80, 100)
                            ? Util.nextInt(70, 100)
                            : Util.nextInt(100, 170);
                    itemOptions.add(new ItemOption(id, param));
                }
            }
            case 219 -> {
                itemOptions.removeIf(io -> io.optionTemplate.id == removeId); // xóa option cũ
                // Thêm ngẫu nhiên SD, HP, KI
                int[] ids = {50, 77, 103};
                for (int id : ids) {
                    int param = Util.isTrue(80, 100)
                            ? Util.nextInt(10, 30)
                            : Util.nextInt(30, 50);
                    itemOptions.add(new ItemOption(id, param));
                }
            }
            case 217 -> {
                // 99% hsd
                if (Util.isTrue(99, 100)) {
                    itemOptions.removeIf(io -> io.optionTemplate.id == removeId); // xóa option cũ
                    itemOptions.add(new ItemOption(93, Util.nextInt(1, 7))); // Hạn sử dụng
                } else {
                    itemOptions.removeIf(io -> io.optionTemplate.id == removeId); // xóa option cũ
                }
            }

            default -> {
                // Không làm gì 
            }
        }
    }

    public Item DoThienSu(int itemId, int gender) {
        Item dots = createItemSetKichHoat(itemId, 1);
        List<Integer> ao = Arrays.asList(1048, 1049, 1050);
        List<Integer> quan = Arrays.asList(1051, 1052, 1053);
        List<Integer> gang = Arrays.asList(1054, 1055, 1056);
        List<Integer> giay = Arrays.asList(1057, 1058, 1059);
        List<Integer> nhan = Arrays.asList(1060, 1061, 1062);
        //áo
        if (ao.contains(itemId)) {
            dots.itemOptions.add(new ItemOption(47, Util.highlightsItem(gender == 2, new Random().nextInt(1201) + 2800))); // áo từ 2800-4000 giáp
            if (Util.isTrue(30, 100)) {
                dots.itemOptions.add(new ItemOption(108, Util.nextInt(3, 10)));
            }
        }
        //quần
        if (Util.isTrue(60, 100)) {
            if (quan.contains(itemId)) {
                dots.itemOptions.add(new ItemOption(22, Util.highlightsItem(gender == 0, new Random().nextInt(11) + 120))); // hp 120k-130k
            }
        } else {
            if (quan.contains(itemId)) {
                dots.itemOptions.add(new ItemOption(22, Util.highlightsItem(gender == 0, new Random().nextInt(51) + 130))); // hp 130-180k 15%
                if (Util.isTrue(30, 100)) {
                    dots.itemOptions.add(new ItemOption(77, Util.nextInt(5, 15)));
                }
            }
        }
        //găng
        if (Util.isTrue(60, 100)) {
            if (gang.contains(itemId)) {
                dots.itemOptions.add(new ItemOption(0, Util.highlightsItem(gender == 2, new Random().nextInt(7651) + 11000))); // 11000-18600
            }
        } else {
            if (gang.contains(itemId)) {
                dots.itemOptions.add(new ItemOption(0, Util.highlightsItem(gender == 2, new Random().nextInt(7001) + 12000))); // gang 15% 12-19k -xayda 12k1
                if (Util.isTrue(30, 100)) {
                    dots.itemOptions.add(new ItemOption(50, Util.nextInt(3, 10)));
                }
            }
        }
        //giày
        if (Util.isTrue(60, 100)) {
            if (giay.contains(itemId)) {
                dots.itemOptions.add(new ItemOption(23, Util.highlightsItem(gender == 1, new Random().nextInt(21) + 120))); // ki 90-110k
            }
        } else {
            if (giay.contains(itemId)) {
                dots.itemOptions.add(new ItemOption(23, Util.highlightsItem(gender == 1, new Random().nextInt(21) + 130))); // ki 110-130k
                if (Util.isTrue(30, 100)) {
                    dots.itemOptions.add(new ItemOption(103, Util.nextInt(5, 15)));
                }
            }
        }

        if (nhan.contains(itemId)) {
            dots.itemOptions.add(new ItemOption(14, Util.highlightsItem(gender == 1, new Random().nextInt(3) + 18))); // nhẫn 18-20%
            if (Util.isTrue(30, 100)) {
                dots.itemOptions.add(new ItemOption(117, Util.nextInt(3, 10)));
            }
        }
        dots.itemOptions.add(new ItemOption(21, 120));
        return dots;
    }

    public void settaiyoken(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1105);
        Item ao = ItemService.gI().otpts((short) 1048);
        Item quan = ItemService.gI().otpts((short) 1051);
        Item gang = ItemService.gI().otpts((short) 1054);
        Item giay = ItemService.gI().otpts((short) 1057);
        Item nhan = ItemService.gI().otpts((short) 1060);
        ao.itemOptions.add(new ItemOption(127, 1));
        quan.itemOptions.add(new ItemOption(127, 1));
        gang.itemOptions.add(new ItemOption(127, 1));
        giay.itemOptions.add(new ItemOption(127, 1));
        nhan.itemOptions.add(new ItemOption(127, 1));
        ao.itemOptions.add(new ItemOption(139, 1));
        quan.itemOptions.add(new ItemOption(139, 1));
        gang.itemOptions.add(new ItemOption(139, 1));
        giay.itemOptions.add(new ItemOption(139, 1));
        nhan.itemOptions.add(new ItemOption(139, 1));
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        nhan.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 0);
            InventoryService.gI().addItemBag(player, quan, 0);
            InventoryService.gI().addItemBag(player, gang, 0);
            InventoryService.gI().addItemBag(player, giay, 0);
            InventoryService.gI().addItemBag(player, nhan, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được set thiên sứ ");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public void setThienHoangDB(Player player) throws Exception {
    Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1538);

    Item ao = Util.ratiItemTL((short) 559);
    Item quan = Util.ratiItemTL((short) 560);
    Item gang = Util.ratiItemTL((short) 566);
    Item giay = Util.ratiItemTL((short) 567);
    Item nhan = Util.ratiItemTL((short) 561);

    // SKH Thiên Hoàng
    ao.itemOptions.add(new ItemOption(53, 1));
    quan.itemOptions.add(new ItemOption(53, 1));
    gang.itemOptions.add(new ItemOption(53, 1));
    giay.itemOptions.add(new ItemOption(53, 1));
    nhan.itemOptions.add(new ItemOption(53, 1));

    // +700% GL
    ao.itemOptions.add(new ItemOption(45, 700));
    quan.itemOptions.add(new ItemOption(45, 700));
    gang.itemOptions.add(new ItemOption(45, 700));
    giay.itemOptions.add(new ItemOption(45, 700));
    nhan.itemOptions.add(new ItemOption(45, 700));

    // khóa item
    ao.itemOptions.add(new ItemOption(30, 1));
    quan.itemOptions.add(new ItemOption(30, 1));
    gang.itemOptions.add(new ItemOption(30, 1));
    giay.itemOptions.add(new ItemOption(30, 1));
    nhan.itemOptions.add(new ItemOption(30, 1));

    if (InventoryService.gI().getCountEmptyBag(player) > 4) {

        InventoryService.gI().addItemBag(player, ao, 0);
        InventoryService.gI().addItemBag(player, quan, 0);
        InventoryService.gI().addItemBag(player, gang, 0);
        InventoryService.gI().addItemBag(player, giay, 0);
        InventoryService.gI().addItemBag(player, nhan, 0);

        InventoryService.gI().sendItemBags(player);

        Service.getInstance().sendThongBao(player, "Bạn đã nhận được 5 món SKH Thiên Hoàng Đặc Biệt");

        InventoryService.gI().subQuantityItemsBag(player, hq, 1);
        InventoryService.gI().sendItemBags(player);

    } else {
        Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
    }
}

    public void setgenki(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1105);
        Item ao = ItemService.gI().otpts((short) 1048);
        Item quan = ItemService.gI().otpts((short) 1051);
        Item gang = ItemService.gI().otpts((short) 1054);
        Item giay = ItemService.gI().otpts((short) 1057);
        Item nhan = ItemService.gI().otpts((short) 1060);
        ao.itemOptions.add(new ItemOption(128, 1));
        quan.itemOptions.add(new ItemOption(128, 1));
        gang.itemOptions.add(new ItemOption(128, 1));
        giay.itemOptions.add(new ItemOption(128, 1));
        nhan.itemOptions.add(new ItemOption(128, 1));
        ao.itemOptions.add(new ItemOption(140, 1));
        quan.itemOptions.add(new ItemOption(140, 1));
        gang.itemOptions.add(new ItemOption(140, 1));
        giay.itemOptions.add(new ItemOption(140, 1));
        nhan.itemOptions.add(new ItemOption(140, 1));
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        nhan.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 0);
            InventoryService.gI().addItemBag(player, quan, 0);
            InventoryService.gI().addItemBag(player, gang, 0);
            InventoryService.gI().addItemBag(player, giay, 0);
            InventoryService.gI().addItemBag(player, nhan, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được set thiên sứ ");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public void setkamejoko(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1105);
        Item ao = ItemService.gI().otpts((short) 1048);
        Item quan = ItemService.gI().otpts((short) 1051);
        Item gang = ItemService.gI().otpts((short) 1054);
        Item giay = ItemService.gI().otpts((short) 1057);
        Item nhan = ItemService.gI().otpts((short) 1060);
        ao.itemOptions.add(new ItemOption(129, 1));
        quan.itemOptions.add(new ItemOption(129, 1));
        gang.itemOptions.add(new ItemOption(129, 1));
        giay.itemOptions.add(new ItemOption(129, 1));
        nhan.itemOptions.add(new ItemOption(129, 1));
        ao.itemOptions.add(new ItemOption(141, 1));
        quan.itemOptions.add(new ItemOption(141, 1));
        gang.itemOptions.add(new ItemOption(141, 1));
        giay.itemOptions.add(new ItemOption(141, 1));
        nhan.itemOptions.add(new ItemOption(141, 1));
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        nhan.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 0);
            InventoryService.gI().addItemBag(player, quan, 0);
            InventoryService.gI().addItemBag(player, gang, 0);
            InventoryService.gI().addItemBag(player, giay, 0);
            InventoryService.gI().addItemBag(player, nhan, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được set thiên sứ ");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public void setgodki(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1105);
        Item ao = ItemService.gI().otpts((short) 1049);
        Item quan = ItemService.gI().otpts((short) 1052);
        Item gang = ItemService.gI().otpts((short) 1055);
        Item giay = ItemService.gI().otpts((short) 1058);
        Item nhan = ItemService.gI().otpts((short) 1061);
        ao.itemOptions.add(new ItemOption(130, 1));
        quan.itemOptions.add(new ItemOption(130, 1));
        gang.itemOptions.add(new ItemOption(130, 1));
        giay.itemOptions.add(new ItemOption(130, 1));
        nhan.itemOptions.add(new ItemOption(130, 1));
        ao.itemOptions.add(new ItemOption(142, 1));
        quan.itemOptions.add(new ItemOption(142, 1));
        gang.itemOptions.add(new ItemOption(142, 1));
        giay.itemOptions.add(new ItemOption(142, 1));
        nhan.itemOptions.add(new ItemOption(142, 1));
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        nhan.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 0);
            InventoryService.gI().addItemBag(player, quan, 0);
            InventoryService.gI().addItemBag(player, gang, 0);
            InventoryService.gI().addItemBag(player, giay, 0);
            InventoryService.gI().addItemBag(player, nhan, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được set thiên sứ ");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public void setgoddam(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1105);
        Item ao = ItemService.gI().otpts((short) 1049);
        Item quan = ItemService.gI().otpts((short) 1052);
        Item gang = ItemService.gI().otpts((short) 1055);
        Item giay = ItemService.gI().otpts((short) 1058);
        Item nhan = ItemService.gI().otpts((short) 1061);
        ao.itemOptions.add(new ItemOption(131, 1));
        quan.itemOptions.add(new ItemOption(131, 1));
        gang.itemOptions.add(new ItemOption(131, 1));
        giay.itemOptions.add(new ItemOption(131, 1));
        nhan.itemOptions.add(new ItemOption(131, 1));
        ao.itemOptions.add(new ItemOption(143, 1));
        quan.itemOptions.add(new ItemOption(143, 1));
        gang.itemOptions.add(new ItemOption(143, 1));
        giay.itemOptions.add(new ItemOption(143, 1));
        nhan.itemOptions.add(new ItemOption(143, 1));
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        nhan.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 0);
            InventoryService.gI().addItemBag(player, quan, 0);
            InventoryService.gI().addItemBag(player, gang, 0);
            InventoryService.gI().addItemBag(player, giay, 0);
            InventoryService.gI().addItemBag(player, nhan, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được set thiên sứ ");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public void setsummon(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1105);
        Item ao = ItemService.gI().otpts((short) 1049);
        Item quan = ItemService.gI().otpts((short) 1052);
        Item gang = ItemService.gI().otpts((short) 1055);
        Item giay = ItemService.gI().otpts((short) 1058);
        Item nhan = ItemService.gI().otpts((short) 1061);
        ao.itemOptions.add(new ItemOption(132, 1));
        quan.itemOptions.add(new ItemOption(132, 1));
        gang.itemOptions.add(new ItemOption(132, 1));
        giay.itemOptions.add(new ItemOption(132, 1));
        nhan.itemOptions.add(new ItemOption(132, 1));
        ao.itemOptions.add(new ItemOption(144, 1));
        quan.itemOptions.add(new ItemOption(144, 1));
        gang.itemOptions.add(new ItemOption(144, 1));
        giay.itemOptions.add(new ItemOption(144, 1));
        nhan.itemOptions.add(new ItemOption(144, 1));
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        nhan.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 0);
            InventoryService.gI().addItemBag(player, quan, 0);
            InventoryService.gI().addItemBag(player, gang, 0);
            InventoryService.gI().addItemBag(player, giay, 0);
            InventoryService.gI().addItemBag(player, nhan, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được set thiên sứ ");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public void setgodgalick(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1105);
        Item ao = ItemService.gI().otpts((short) 1050);
        Item quan = ItemService.gI().otpts((short) 1053);
        Item gang = ItemService.gI().otpts((short) 1056);
        Item giay = ItemService.gI().otpts((short) 1059);
        Item nhan = ItemService.gI().otpts((short) 1062);
        ao.itemOptions.add(new ItemOption(133, 1));
        quan.itemOptions.add(new ItemOption(133, 1));
        gang.itemOptions.add(new ItemOption(133, 1));
        giay.itemOptions.add(new ItemOption(133, 1));
        nhan.itemOptions.add(new ItemOption(133, 1));
        ao.itemOptions.add(new ItemOption(136, 1));
        quan.itemOptions.add(new ItemOption(136, 1));
        gang.itemOptions.add(new ItemOption(136, 1));
        giay.itemOptions.add(new ItemOption(136, 1));
        nhan.itemOptions.add(new ItemOption(136, 1));
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        nhan.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 0);
            InventoryService.gI().addItemBag(player, quan, 0);
            InventoryService.gI().addItemBag(player, gang, 0);
            InventoryService.gI().addItemBag(player, giay, 0);
            InventoryService.gI().addItemBag(player, nhan, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được set thiên sứ ");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public void setmonkey(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1105);
        Item ao = ItemService.gI().otpts((short) 1050);
        Item quan = ItemService.gI().otpts((short) 1053);
        Item gang = ItemService.gI().otpts((short) 1056);
        Item giay = ItemService.gI().otpts((short) 1059);
        Item nhan = ItemService.gI().otpts((short) 1062);
        ao.itemOptions.add(new ItemOption(134, 1));
        quan.itemOptions.add(new ItemOption(134, 1));
        gang.itemOptions.add(new ItemOption(134, 1));
        giay.itemOptions.add(new ItemOption(134, 1));
        nhan.itemOptions.add(new ItemOption(134, 1));
        ao.itemOptions.add(new ItemOption(137, 1));
        quan.itemOptions.add(new ItemOption(137, 1));
        gang.itemOptions.add(new ItemOption(137, 1));
        giay.itemOptions.add(new ItemOption(137, 1));
        nhan.itemOptions.add(new ItemOption(137, 1));
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        nhan.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 0);
            InventoryService.gI().addItemBag(player, quan, 0);
            InventoryService.gI().addItemBag(player, gang, 0);
            InventoryService.gI().addItemBag(player, giay, 0);
            InventoryService.gI().addItemBag(player, nhan, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được set thiên sứ ");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public void setgodhp(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1105);
        Item ao = ItemService.gI().otpts((short) 1050);
        Item quan = ItemService.gI().otpts((short) 1053);
        Item gang = ItemService.gI().otpts((short) 1056);
        Item giay = ItemService.gI().otpts((short) 1059);
        Item nhan = ItemService.gI().otpts((short) 1062);
        ao.itemOptions.add(new ItemOption(135, 1));
        quan.itemOptions.add(new ItemOption(135, 1));
        gang.itemOptions.add(new ItemOption(135, 1));
        giay.itemOptions.add(new ItemOption(135, 1));
        nhan.itemOptions.add(new ItemOption(135, 1));
        ao.itemOptions.add(new ItemOption(138, 1));
        quan.itemOptions.add(new ItemOption(138, 1));
        gang.itemOptions.add(new ItemOption(138, 1));
        giay.itemOptions.add(new ItemOption(138, 1));
        nhan.itemOptions.add(new ItemOption(138, 1));
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        nhan.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 0);
            InventoryService.gI().addItemBag(player, quan, 0);
            InventoryService.gI().addItemBag(player, gang, 0);
            InventoryService.gI().addItemBag(player, giay, 0);
            InventoryService.gI().addItemBag(player, nhan, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được set thiên sứ ");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public void settaiyokenHD(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1537);
        Item ao = Util.ratiItemHuyDiet((short) 650);
        Item quan = Util.ratiItemHuyDiet((short) 651);
        Item gang = Util.ratiItemHuyDiet((short) 657);
        Item giay = Util.ratiItemHuyDiet((short) 658);
        Item nhan = Util.ratiItemHuyDiet((short) 656);
        ao.itemOptions.add(new ItemOption(127, 1));
        quan.itemOptions.add(new ItemOption(127, 1));
        gang.itemOptions.add(new ItemOption(127, 1));
        giay.itemOptions.add(new ItemOption(127, 1));
        nhan.itemOptions.add(new ItemOption(127, 1));
        ao.itemOptions.add(new ItemOption(139, 1));
        quan.itemOptions.add(new ItemOption(139, 1));
        gang.itemOptions.add(new ItemOption(139, 1));
        giay.itemOptions.add(new ItemOption(139, 1));
        nhan.itemOptions.add(new ItemOption(139, 1));
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        nhan.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 0);
            InventoryService.gI().addItemBag(player, quan, 0);
            InventoryService.gI().addItemBag(player, gang, 0);
            InventoryService.gI().addItemBag(player, giay, 0);
            InventoryService.gI().addItemBag(player, nhan, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được 5 món SKH Hủy diệt");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public void setgenkiHD(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1537);
        Item ao = Util.ratiItemHuyDiet((short) 650);
        Item quan = Util.ratiItemHuyDiet((short) 651);
        Item gang = Util.ratiItemHuyDiet((short) 657);
        Item giay = Util.ratiItemHuyDiet((short) 658);
        Item nhan = Util.ratiItemHuyDiet((short) 656);
        ao.itemOptions.add(new ItemOption(128, 1));
        quan.itemOptions.add(new ItemOption(128, 1));
        gang.itemOptions.add(new ItemOption(128, 1));
        giay.itemOptions.add(new ItemOption(128, 1));
        nhan.itemOptions.add(new ItemOption(128, 1));
        ao.itemOptions.add(new ItemOption(140, 1));
        quan.itemOptions.add(new ItemOption(140, 1));
        gang.itemOptions.add(new ItemOption(140, 1));
        giay.itemOptions.add(new ItemOption(140, 1));
        nhan.itemOptions.add(new ItemOption(140, 1));
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        nhan.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 0);
            InventoryService.gI().addItemBag(player, quan, 0);
            InventoryService.gI().addItemBag(player, gang, 0);
            InventoryService.gI().addItemBag(player, giay, 0);
            InventoryService.gI().addItemBag(player, nhan, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được 5 món SKH Hủy diệt");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public void setkamejokoHD(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1537);
        Item ao = Util.ratiItemHuyDiet((short) 650);
        Item quan = Util.ratiItemHuyDiet((short) 651);
        Item gang = Util.ratiItemHuyDiet((short) 657);
        Item giay = Util.ratiItemHuyDiet((short) 658);
        Item nhan = Util.ratiItemHuyDiet((short) 656);
        ao.itemOptions.add(new ItemOption(129, 1));
        quan.itemOptions.add(new ItemOption(129, 1));
        gang.itemOptions.add(new ItemOption(129, 1));
        giay.itemOptions.add(new ItemOption(129, 1));
        nhan.itemOptions.add(new ItemOption(129, 1));
        ao.itemOptions.add(new ItemOption(141, 1));
        quan.itemOptions.add(new ItemOption(141, 1));
        gang.itemOptions.add(new ItemOption(141, 1));
        giay.itemOptions.add(new ItemOption(141, 1));
        nhan.itemOptions.add(new ItemOption(141, 1));
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        nhan.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 0);
            InventoryService.gI().addItemBag(player, quan, 0);
            InventoryService.gI().addItemBag(player, gang, 0);
            InventoryService.gI().addItemBag(player, giay, 0);
            InventoryService.gI().addItemBag(player, nhan, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được 5 món SKH Hủy diệt");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public void setgodkiHD(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1537);
        Item ao = Util.ratiItemHuyDiet((short) 652);
        Item quan = Util.ratiItemHuyDiet((short) 653);
        Item gang = Util.ratiItemHuyDiet((short) 659);
        Item giay = Util.ratiItemHuyDiet((short) 660);
        Item nhan = Util.ratiItemHuyDiet((short) 656);
        ao.itemOptions.add(new ItemOption(130, 1));
        quan.itemOptions.add(new ItemOption(130, 1));
        gang.itemOptions.add(new ItemOption(130, 1));
        giay.itemOptions.add(new ItemOption(130, 1));
        nhan.itemOptions.add(new ItemOption(130, 1));
        ao.itemOptions.add(new ItemOption(142, 1));
        quan.itemOptions.add(new ItemOption(142, 1));
        gang.itemOptions.add(new ItemOption(142, 1));
        giay.itemOptions.add(new ItemOption(142, 1));
        nhan.itemOptions.add(new ItemOption(142, 1));
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        nhan.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 0);
            InventoryService.gI().addItemBag(player, quan, 0);
            InventoryService.gI().addItemBag(player, gang, 0);
            InventoryService.gI().addItemBag(player, giay, 0);
            InventoryService.gI().addItemBag(player, nhan, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được 5 món SKH Hủy diệt");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public void setgoddamHD(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1537);
        Item ao = Util.ratiItemHuyDiet((short) 652);
        Item quan = Util.ratiItemHuyDiet((short) 653);
        Item gang = Util.ratiItemHuyDiet((short) 659);
        Item giay = Util.ratiItemHuyDiet((short) 660);
        Item nhan = Util.ratiItemHuyDiet((short) 656);
        ao.itemOptions.add(new ItemOption(131, 1));
        quan.itemOptions.add(new ItemOption(131, 1));
        gang.itemOptions.add(new ItemOption(131, 1));
        giay.itemOptions.add(new ItemOption(131, 1));
        nhan.itemOptions.add(new ItemOption(131, 1));
        ao.itemOptions.add(new ItemOption(143, 1));
        quan.itemOptions.add(new ItemOption(143, 1));
        gang.itemOptions.add(new ItemOption(143, 1));
        giay.itemOptions.add(new ItemOption(143, 1));
        nhan.itemOptions.add(new ItemOption(143, 1));
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        nhan.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 0);
            InventoryService.gI().addItemBag(player, quan, 0);
            InventoryService.gI().addItemBag(player, gang, 0);
            InventoryService.gI().addItemBag(player, giay, 0);
            InventoryService.gI().addItemBag(player, nhan, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được 5 món SKH Hủy diệt");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public void setsummonHD(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1537);
        Item ao = Util.ratiItemHuyDiet((short) 652);
        Item quan = Util.ratiItemHuyDiet((short) 653);
        Item gang = Util.ratiItemHuyDiet((short) 659);
        Item giay = Util.ratiItemHuyDiet((short) 660);
        Item nhan = Util.ratiItemHuyDiet((short) 656);
        ao.itemOptions.add(new ItemOption(132, 1));
        quan.itemOptions.add(new ItemOption(132, 1));
        gang.itemOptions.add(new ItemOption(132, 1));
        giay.itemOptions.add(new ItemOption(132, 1));
        nhan.itemOptions.add(new ItemOption(132, 1));
        ao.itemOptions.add(new ItemOption(144, 1));
        quan.itemOptions.add(new ItemOption(144, 1));
        gang.itemOptions.add(new ItemOption(144, 1));
        giay.itemOptions.add(new ItemOption(144, 1));
        nhan.itemOptions.add(new ItemOption(144, 1));
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        nhan.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 0);
            InventoryService.gI().addItemBag(player, quan, 0);
            InventoryService.gI().addItemBag(player, gang, 0);
            InventoryService.gI().addItemBag(player, giay, 0);
            InventoryService.gI().addItemBag(player, nhan, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được 5 món SKH Hủy diệt");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public void setgodgalickHD(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1537);
        Item ao = Util.ratiItemHuyDiet((short) 654);
        Item quan = Util.ratiItemHuyDiet((short) 655);
        Item gang = Util.ratiItemHuyDiet((short) 661);
        Item giay = Util.ratiItemHuyDiet((short) 662);
        Item nhan = Util.ratiItemHuyDiet((short) 656);
        ao.itemOptions.add(new ItemOption(133, 1));
        quan.itemOptions.add(new ItemOption(133, 1));
        gang.itemOptions.add(new ItemOption(133, 1));
        giay.itemOptions.add(new ItemOption(133, 1));
        nhan.itemOptions.add(new ItemOption(133, 1));
        ao.itemOptions.add(new ItemOption(136, 1));
        quan.itemOptions.add(new ItemOption(136, 1));
        gang.itemOptions.add(new ItemOption(136, 1));
        giay.itemOptions.add(new ItemOption(136, 1));
        nhan.itemOptions.add(new ItemOption(136, 1));
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        nhan.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 0);
            InventoryService.gI().addItemBag(player, quan, 0);
            InventoryService.gI().addItemBag(player, gang, 0);
            InventoryService.gI().addItemBag(player, giay, 0);
            InventoryService.gI().addItemBag(player, nhan, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được 5 món SKH Hủy diệt");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public void setmonkeyHD(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1537);
        Item ao = Util.ratiItemHuyDiet((short) 654);
        Item quan = Util.ratiItemHuyDiet((short) 655);
        Item gang = Util.ratiItemHuyDiet((short) 661);
        Item giay = Util.ratiItemHuyDiet((short) 662);
        Item nhan = Util.ratiItemHuyDiet((short) 656);
        ao.itemOptions.add(new ItemOption(134, 1));
        quan.itemOptions.add(new ItemOption(134, 1));
        gang.itemOptions.add(new ItemOption(134, 1));
        giay.itemOptions.add(new ItemOption(134, 1));
        nhan.itemOptions.add(new ItemOption(134, 1));
        ao.itemOptions.add(new ItemOption(137, 1));
        quan.itemOptions.add(new ItemOption(137, 1));
        gang.itemOptions.add(new ItemOption(137, 1));
        giay.itemOptions.add(new ItemOption(137, 1));
        nhan.itemOptions.add(new ItemOption(137, 1));
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        nhan.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 0);
            InventoryService.gI().addItemBag(player, quan, 0);
            InventoryService.gI().addItemBag(player, gang, 0);
            InventoryService.gI().addItemBag(player, giay, 0);
            InventoryService.gI().addItemBag(player, nhan, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được 5 món SKH Hủy diệt");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public void setgodhpHD(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1537);
        Item ao = Util.ratiItemHuyDiet((short) 654);
        Item quan = Util.ratiItemHuyDiet((short) 655);
        Item gang = Util.ratiItemHuyDiet((short) 661);
        Item giay = Util.ratiItemHuyDiet((short) 662);
        Item nhan = Util.ratiItemHuyDiet((short) 656);
        ao.itemOptions.add(new ItemOption(135, 1));
        quan.itemOptions.add(new ItemOption(135, 1));
        gang.itemOptions.add(new ItemOption(135, 1));
        giay.itemOptions.add(new ItemOption(135, 1));
        nhan.itemOptions.add(new ItemOption(135, 1));
        ao.itemOptions.add(new ItemOption(138, 1));
        quan.itemOptions.add(new ItemOption(138, 1));
        gang.itemOptions.add(new ItemOption(138, 1));
        giay.itemOptions.add(new ItemOption(138, 1));
        nhan.itemOptions.add(new ItemOption(138, 1));
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        nhan.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 0);
            InventoryService.gI().addItemBag(player, quan, 0);
            InventoryService.gI().addItemBag(player, gang, 0);
            InventoryService.gI().addItemBag(player, giay, 0);
            InventoryService.gI().addItemBag(player, nhan, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được 5 món SKH Hủy diệt");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public void settaiyokenTL(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1538);
        Item ao = Util.ratiItemTL((short) 555);
        Item quan = Util.ratiItemTL((short) 556);
        Item gang = Util.ratiItemTL((short) 562);
        Item giay = Util.ratiItemTL((short) 563);
        Item nhan = Util.ratiItemTL((short) 561);
        ao.itemOptions.add(new ItemOption(127, 1));
        quan.itemOptions.add(new ItemOption(127, 1));
        gang.itemOptions.add(new ItemOption(127, 1));
        giay.itemOptions.add(new ItemOption(127, 1));
        nhan.itemOptions.add(new ItemOption(127, 1));
        ao.itemOptions.add(new ItemOption(139, 1));
        quan.itemOptions.add(new ItemOption(139, 1));
        gang.itemOptions.add(new ItemOption(139, 1));
        giay.itemOptions.add(new ItemOption(139, 1));
        nhan.itemOptions.add(new ItemOption(139, 1));
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        nhan.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 0);
            InventoryService.gI().addItemBag(player, quan, 0);
            InventoryService.gI().addItemBag(player, gang, 0);
            InventoryService.gI().addItemBag(player, giay, 0);
            InventoryService.gI().addItemBag(player, nhan, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được 5 món SKH Thần linh");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public void setgenkiTL(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1538);
        Item ao = Util.ratiItemTL((short) 555);
        Item quan = Util.ratiItemTL((short) 556);
        Item gang = Util.ratiItemTL((short) 562);
        Item giay = Util.ratiItemTL((short) 563);
        Item nhan = Util.ratiItemTL((short) 561);
        ao.itemOptions.add(new ItemOption(128, 1));
        quan.itemOptions.add(new ItemOption(128, 1));
        gang.itemOptions.add(new ItemOption(128, 1));
        giay.itemOptions.add(new ItemOption(128, 1));
        nhan.itemOptions.add(new ItemOption(128, 1));
        ao.itemOptions.add(new ItemOption(140, 1));
        quan.itemOptions.add(new ItemOption(140, 1));
        gang.itemOptions.add(new ItemOption(140, 1));
        giay.itemOptions.add(new ItemOption(140, 1));
        nhan.itemOptions.add(new ItemOption(140, 1));
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        nhan.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 0);
            InventoryService.gI().addItemBag(player, quan, 0);
            InventoryService.gI().addItemBag(player, gang, 0);
            InventoryService.gI().addItemBag(player, giay, 0);
            InventoryService.gI().addItemBag(player, nhan, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được 5 món SKH Thần linh");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public void setkamejokoTL(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1538);
        Item ao = Util.ratiItemTL((short) 555);
        Item quan = Util.ratiItemTL((short) 556);
        Item gang = Util.ratiItemTL((short) 562);
        Item giay = Util.ratiItemTL((short) 563);
        Item nhan = Util.ratiItemTL((short) 561);
        ao.itemOptions.add(new ItemOption(129, 1));
        quan.itemOptions.add(new ItemOption(129, 1));
        gang.itemOptions.add(new ItemOption(129, 1));
        giay.itemOptions.add(new ItemOption(129, 1));
        nhan.itemOptions.add(new ItemOption(129, 1));
        ao.itemOptions.add(new ItemOption(141, 1));
        quan.itemOptions.add(new ItemOption(141, 1));
        gang.itemOptions.add(new ItemOption(141, 1));
        giay.itemOptions.add(new ItemOption(141, 1));
        nhan.itemOptions.add(new ItemOption(141, 1));
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        nhan.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 0);
            InventoryService.gI().addItemBag(player, quan, 0);
            InventoryService.gI().addItemBag(player, gang, 0);
            InventoryService.gI().addItemBag(player, giay, 0);
            InventoryService.gI().addItemBag(player, nhan, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được 5 món SKH Thần linh");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public void setgodkiTL(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1538);
        Item ao = Util.ratiItemTL((short) 557);
        Item quan = Util.ratiItemTL((short) 558);
        Item gang = Util.ratiItemTL((short) 564);
        Item giay = Util.ratiItemTL((short) 565);
        Item nhan = Util.ratiItemTL((short) 561);
        ao.itemOptions.add(new ItemOption(130, 1));
        quan.itemOptions.add(new ItemOption(130, 1));
        gang.itemOptions.add(new ItemOption(130, 1));
        giay.itemOptions.add(new ItemOption(130, 1));
        nhan.itemOptions.add(new ItemOption(130, 1));
        ao.itemOptions.add(new ItemOption(142, 1));
        quan.itemOptions.add(new ItemOption(142, 1));
        gang.itemOptions.add(new ItemOption(142, 1));
        giay.itemOptions.add(new ItemOption(142, 1));
        nhan.itemOptions.add(new ItemOption(142, 1));
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        nhan.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 0);
            InventoryService.gI().addItemBag(player, quan, 0);
            InventoryService.gI().addItemBag(player, gang, 0);
            InventoryService.gI().addItemBag(player, giay, 0);
            InventoryService.gI().addItemBag(player, nhan, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được 5 món SKH Thần linh");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public void setgoddamTL(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1538);
        Item ao = Util.ratiItemTL((short) 557);
        Item quan = Util.ratiItemTL((short) 558);
        Item gang = Util.ratiItemTL((short) 564);
        Item giay = Util.ratiItemTL((short) 565);
        Item nhan = Util.ratiItemTL((short) 561);
        ao.itemOptions.add(new ItemOption(131, 1));
        quan.itemOptions.add(new ItemOption(131, 1));
        gang.itemOptions.add(new ItemOption(131, 1));
        giay.itemOptions.add(new ItemOption(131, 1));
        nhan.itemOptions.add(new ItemOption(131, 1));
        ao.itemOptions.add(new ItemOption(143, 1));
        quan.itemOptions.add(new ItemOption(143, 1));
        gang.itemOptions.add(new ItemOption(143, 1));
        giay.itemOptions.add(new ItemOption(143, 1));
        nhan.itemOptions.add(new ItemOption(143, 1));
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        nhan.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 0);
            InventoryService.gI().addItemBag(player, quan, 0);
            InventoryService.gI().addItemBag(player, gang, 0);
            InventoryService.gI().addItemBag(player, giay, 0);
            InventoryService.gI().addItemBag(player, nhan, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được 5 món SKH Thần linh");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public void setsummonTL(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1538);
        Item ao = Util.ratiItemTL((short) 557);
        Item quan = Util.ratiItemTL((short) 558);
        Item gang = Util.ratiItemTL((short) 564);
        Item giay = Util.ratiItemTL((short) 565);
        Item nhan = Util.ratiItemTL((short) 561);
        ao.itemOptions.add(new ItemOption(132, 1));
        quan.itemOptions.add(new ItemOption(132, 1));
        gang.itemOptions.add(new ItemOption(132, 1));
        giay.itemOptions.add(new ItemOption(132, 1));
        nhan.itemOptions.add(new ItemOption(132, 1));
        ao.itemOptions.add(new ItemOption(144, 1));
        quan.itemOptions.add(new ItemOption(144, 1));
        gang.itemOptions.add(new ItemOption(144, 1));
        giay.itemOptions.add(new ItemOption(144, 1));
        nhan.itemOptions.add(new ItemOption(144, 1));
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        nhan.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 0);
            InventoryService.gI().addItemBag(player, quan, 0);
            InventoryService.gI().addItemBag(player, gang, 0);
            InventoryService.gI().addItemBag(player, giay, 0);
            InventoryService.gI().addItemBag(player, nhan, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được 5 món SKH Thần linh");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public void setgodgalickTL(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1538);
        Item ao = Util.ratiItemTL((short) 559);
        Item quan = Util.ratiItemTL((short) 560);
        Item gang = Util.ratiItemTL((short) 566);
        Item giay = Util.ratiItemTL((short) 567);
        Item nhan = Util.ratiItemTL((short) 561);
        ao.itemOptions.add(new ItemOption(133, 1));
        quan.itemOptions.add(new ItemOption(133, 1));
        gang.itemOptions.add(new ItemOption(133, 1));
        giay.itemOptions.add(new ItemOption(133, 1));
        nhan.itemOptions.add(new ItemOption(133, 1));
        ao.itemOptions.add(new ItemOption(136, 1));
        quan.itemOptions.add(new ItemOption(136, 1));
        gang.itemOptions.add(new ItemOption(136, 1));
        giay.itemOptions.add(new ItemOption(136, 1));
        nhan.itemOptions.add(new ItemOption(136, 1));
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        nhan.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 0);
            InventoryService.gI().addItemBag(player, quan, 0);
            InventoryService.gI().addItemBag(player, gang, 0);
            InventoryService.gI().addItemBag(player, giay, 0);
            InventoryService.gI().addItemBag(player, nhan, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được 5 món SKH Thần linh");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public void setmonkeyTL(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1538);
        Item ao = Util.ratiItemTL((short) 559);
        Item quan = Util.ratiItemTL((short) 560);
        Item gang = Util.ratiItemTL((short) 566);
        Item giay = Util.ratiItemTL((short) 567);
        Item nhan = Util.ratiItemTL((short) 561);
        ao.itemOptions.add(new ItemOption(134, 1));
        quan.itemOptions.add(new ItemOption(134, 1));
        gang.itemOptions.add(new ItemOption(134, 1));
        giay.itemOptions.add(new ItemOption(134, 1));
        nhan.itemOptions.add(new ItemOption(134, 1));
        ao.itemOptions.add(new ItemOption(137, 1));
        quan.itemOptions.add(new ItemOption(137, 1));
        gang.itemOptions.add(new ItemOption(137, 1));
        giay.itemOptions.add(new ItemOption(137, 1));
        nhan.itemOptions.add(new ItemOption(137, 1));
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        nhan.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 0);
            InventoryService.gI().addItemBag(player, quan, 0);
            InventoryService.gI().addItemBag(player, gang, 0);
            InventoryService.gI().addItemBag(player, giay, 0);
            InventoryService.gI().addItemBag(player, nhan, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được 5 món SKH Thần linh");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public void setgodhpTL(Player player) throws Exception {
        Item hq = InventoryService.gI().findItem(player.inventory.itemsBag, 1538);
        Item ao = Util.ratiItemTL((short) 559);
        Item quan = Util.ratiItemTL((short) 560);
        Item gang = Util.ratiItemTL((short) 566);
        Item giay = Util.ratiItemTL((short) 567);
        Item nhan = Util.ratiItemTL((short) 561);
        ao.itemOptions.add(new ItemOption(135, 1));
        quan.itemOptions.add(new ItemOption(135, 1));
        gang.itemOptions.add(new ItemOption(135, 1));
        giay.itemOptions.add(new ItemOption(135, 1));
        nhan.itemOptions.add(new ItemOption(135, 1));
        ao.itemOptions.add(new ItemOption(138, 1));
        quan.itemOptions.add(new ItemOption(138, 1));
        gang.itemOptions.add(new ItemOption(138, 1));
        giay.itemOptions.add(new ItemOption(138, 1));
        nhan.itemOptions.add(new ItemOption(138, 1));
        ao.itemOptions.add(new ItemOption(30, 0));
        quan.itemOptions.add(new ItemOption(30, 0));
        gang.itemOptions.add(new ItemOption(30, 0));
        giay.itemOptions.add(new ItemOption(30, 0));
        nhan.itemOptions.add(new ItemOption(30, 0));
        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 0);
            InventoryService.gI().addItemBag(player, quan, 0);
            InventoryService.gI().addItemBag(player, gang, 0);
            InventoryService.gI().addItemBag(player, giay, 0);
            InventoryService.gI().addItemBag(player, nhan, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được 5 món SKH Thần linh");
            InventoryService.gI().subQuantityItemsBag(player, hq, 1);
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }

    public Item otpts(short tempId) {
        return otpts(tempId, 1);
    }

    public Item otpts(short tempId, int quantity) {
        Item item = new Item();
        item.template = getTemplate(tempId);
        item.quantity = quantity;
        item.createTime = System.currentTimeMillis();
        if (item.template.type == 0) {
            item.itemOptions.add(new ItemOption(21, 120));
            item.itemOptions.add(new ItemOption(47, Util.nextInt(2000, 2500)));
        }
        if (item.template.type == 1) {
            item.itemOptions.add(new ItemOption(21, 120));
            item.itemOptions.add(new ItemOption(22, Util.nextInt(150, 200)));
        }
        if (item.template.type == 2) {
            item.itemOptions.add(new ItemOption(21, 120));
            item.itemOptions.add(new ItemOption(0, Util.nextInt(18000, 20000)));
        }
        if (item.template.type == 3) {
            item.itemOptions.add(new ItemOption(21, 120));
            item.itemOptions.add(new ItemOption(23, Util.nextInt(150, 200)));
        }
        if (item.template.type == 4) {
            item.itemOptions.add(new ItemOption(21, 120));
            item.itemOptions.add(new ItemOption(14, Util.nextInt(20, 25)));
        }
        item.content = item.getContent();
        item.info = item.getInfo();
        return item;
    }

    public int randomSKHId(byte gender) {
        if (gender == 3) {
            gender = 2;
        }
        int[][] options = {{128, 129, 127}, {130, 131, 132}, {133, 135, 134}};
        int skhv1 = 25;
        int skhv2 = 35;
        int skhc = 40;
        int skhId = -1;
        int rd = Util.nextInt(1, 100);
        if (rd <= skhv1) {
            skhId = 0;
        } else if (rd <= skhv1 + skhv2) {
            skhId = 1;
        } else if (rd <= skhv1 + skhv2 + skhc) {
            skhId = 2;
        }
        return options[gender][skhId];
    }

    public int randomSKHIdDevuong(byte gender) {
        if (gender == 3) {
            gender = 2;
        }
        int[][] options = {{51, 51, 51}, {52, 52, 52}, {53, 53, 53}};
        int skhv1 = 25;
        int skhv2 = 35;
        int skhc = 40;
        int skhId = -1;
        int rd = Util.nextInt(1, 100);
        if (rd <= skhv1) {
            skhId = 0;
        } else if (rd <= skhv1 + skhv2) {
            skhId = 1;
        } else if (rd <= skhv1 + skhv2 + skhc) {
            skhId = 2;
        }
        return options[gender][skhId];
    }
//--------------------------------------------------------------------------------------------

    public Item getCleanItemFromShopDeTu(short itemId) {
        Item item = ItemService.gI().createNewItem(itemId); // tạo item mới
        item.itemOptions.addAll(getListOptionItemShopDeTu(itemId)); // add option từ shop
        return item;
    }

    public List<ItemOption> getListOptionItemShopDeTu(short id) {
        List<ItemOption> list = new ArrayList<>();
        Manager.SHOPS.forEach(shop -> shop.tabShops.forEach(tabShop -> tabShop.itemShops.forEach(itemShop -> {
            boolean isDeTu = itemShop.options.stream().anyMatch(io -> io.optionTemplate.id == 197);
            if (itemShop.temp.id == id && isDeTu && list.isEmpty()) {
                list.addAll(itemShop.options);
            }
        })));
        return list;
    }

    public Item getCleanItemFromShopCaiTrang(short itemId) {
        Item item = ItemService.gI().createNewItem(itemId); // tạo item mới
        item.itemOptions.addAll(getListOptionItemShopCaiTrang(itemId)); // add option từ shop
        return item;
    }

    public List<ItemOption> getListOptionItemShopCaiTrang(short id) {
        List<ItemOption> list = new ArrayList<>();
        Manager.SHOPS.forEach(shop -> shop.tabShops.forEach(tabShop -> tabShop.itemShops.forEach(itemShop -> {
            boolean isCaiTrang = itemShop.temp.type == 5;
            if (itemShop.temp.id == id && isCaiTrang && list.isEmpty()) {
                list.addAll(itemShop.options);
            }
        })));
        return list;
    }
//--------------------------------------------------------------------------------------------

    public Item itemSKH(int itemId, int skhId) {
        Item item = createItemSetKichHoat(itemId, 1);
        if (item != null) {
            item.itemOptions.addAll(ItemService.gI().getListOptionItemShopFull((short) itemId));
            item.itemOptions.add(new ItemOption(skhId, 1));
            item.itemOptions.add(new ItemOption(optionIdSKH(skhId), 1));
            item.itemOptions.add(new ItemOption(30, 1));
        }
        return item;
    }

    public List<ItemOption> getListOptionItemShopFull(short id) {
        List<ItemOption> list = new ArrayList<>();
        Manager.SHOPS.forEach(shop -> shop.tabShops.forEach(tabShop -> tabShop.itemShops.forEach(itemShop -> {
            if (itemShop.temp.id == id && list.isEmpty()) {
                list.addAll(itemShop.options);
            }
        })));
        return list;
    }

    public int getSKHidFromItem(Item item) {
        for (ItemOption io : item.itemOptions) {
            if (io.optionTemplate != null && io.optionTemplate.id >= 127 && io.optionTemplate.id <= 135) {
                return io.optionTemplate.id;
            }
        }
        return -1;
    }

    public Item createItemSetKichHoat(int tempId, int quantity) {
        Item item = new Item();
        item.template = getTemplate(tempId);
        item.quantity = quantity;
        item.itemOptions = createItemNull().itemOptions;
        item.createTime = System.currentTimeMillis();
        item.content = item.getContent();
        item.info = item.getInfo();
        return item;
    }

    public ConsignmentItem createNewConsignmentItem(short tempId, int quantity) {
        ConsignmentItem item = new ConsignmentItem();
        item.template = getTemplate(tempId);
        item.quantity = quantity;
        item.createTime = System.currentTimeMillis();
        item.content = item.getContent();
        item.info = item.getInfo();
        return item;
    }

    public Item createItemFromItemMap(ItemMap itemMap) {
        Item item = createNewItem(itemMap.itemTemplate.id, itemMap.quantity);
        item.itemOptions = itemMap.options;
        return item;
    }

    public ItemOptionTemplate getItemOptionTemplate(int id) {
        return Manager.ITEM_OPTION_TEMPLATES.get(id);
    }

    public ItemTemplate getTemplate(int id) {
        return Manager.ITEM_TEMPLATES.get(id);
    }

    public boolean isItemActivation(Item item) {
        return false;
    }

    public int getPercentTrainArmor(Item item) {
//        if (item != null) {
//            switch (item.template.id) {
//                case 0:
//                case 1:
//                    return 10;
//                case 2:
//                case 3:
//                    return 20;
//                case 4:
//                case 5:
//                    return 30;
//                default:
//                    return 0;
//            }
//        } else {
//            return 0;
//        }
        return 0;
    }

    public boolean isTrainArmor(Item item) {
//        if (item != null) {
//            switch (item.template.id) {
//                case 0:
//                case 1:
//                case 2:
//                case 3:
//                case 4:
//                case 5:
//                    return true;
//                default:
//                    return false;
//            }
//        } else {
//            return false;
//        }
        return false;
    }

    public int optionIdSKH(int skhId) {
        switch (skhId) {
            case 127: //Set Viet Taiyoken
                return 139;
            case 128: //Set Viet Genki
                return 140;
            case 129: //Set Viet Kamejoko
                return 141;
            case 130: //Set Viet KI
                return 142;
            case 131: //Set Viet Dame
                return 143;
            case 132: //Set Viet Summon
                return 144;
            case 133: //Set Viet Galick
                return 136;
            case 134: //Set Viet Monkey
                return 137;
            case 135: //Set Viet HP
                return 138;
        }
        return 0;
    }

    public boolean isOutOfDateTime(Item item) {
        long now = System.currentTimeMillis();
        if (item != null) {
            for (ItemOption io : item.itemOptions) {
                if (io.optionTemplate.id == 93) {
                    int dayPass = (int) TimeUtil.diffDate(new Date(), new Date(item.createTime), TimeUtil.DAY);
                    if (dayPass != 0) {
                        io.param -= dayPass;
                        if (io.param <= 0) {
                            return true; // Hết hạn
                        } else {
                            item.createTime = now; // Cập nhật lại mốc tính
                        }
                    }
                }
            }
        }
        return false; // Không hết hạn
    }

    public boolean isOutOfHouseTime(Item item) {
        if (item != null) {
            for (ItemOption io : item.itemOptions) {
                if (io.optionTemplate.id == 225) {
                    int hourpass = (int) TimeUtil.diffDate(new Date(), new Date(item.createTime), TimeUtil.HOUR);
                    if (hourpass != 0) {
                        if (io.param - hourpass > 0) {
                            io.param -= hourpass;
                        } else {
                            io.param = 0;
                        }
                        if (io.param <= 0) {
                            return true;
                        } else {
                            item.createTime = System.currentTimeMillis();
                        }
                    }
                    if (io.param <= 0) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isItemNoLimitQuantity(int id) {// item k giới hạn số lượng
        if (id >= 1066 && id <= 1070) {// mảnh trang bị thiên sứ
            return true;
        }
        return false;
    }

    public int randomSKHThanhTon(byte gender) {
        if (gender == 3) {
            gender = 2;
        }
        int[] options = {234, 235, 236};
        return options[gender];
    }

    public int optionIdSKHThanhTon(int skhId) {
        switch (skhId) {
            case 234: //Set Thánh tôn trái dất
                return 237;
            case 235: //Set Thánh tôn namec
                return 238;
            case 236: //Set Thánh tôn xayda
                return 239;
        }
        return 0;
    }

    public int randomSKHDeVuong(byte gender) {
        if (gender == 3) {
            gender = 2;
        }
        int[] options = {201, 202, 203};
        return options[gender];
    }

    public int optionIdSKHDeVuong(int skhId) {
        switch (skhId) {
            case 201:
                return Util.nextInt(204, 205);
            case 202:
                return Util.nextInt(206, 207);
            case 203:
                return Util.nextInt(208, 209);
        }
        return 0;
    }

    public int[] getSKHHoangKimFromItem(Item item) {
        int skhId = -1;
        int bonusId = -1;
        for (ItemOption io : item.itemOptions) {
            int id = io.optionTemplate.id;
            if (id >= 220 && id <= 222) {
                skhId = id;
            } else if (id >= 223 && id <= 228) {
                bonusId = id;
            }
        }
        return new int[]{skhId, bonusId};
    }

    public int randomSKHHoangKim(byte gender) {
        if (gender == 3) {
            gender = 2;
        }
        int[] options = {220, 221, 222};
        return options[gender];
    }

    public int optionIdSKHHoangKim(int skhId) {
        switch (skhId) {
            case 220: //Set Hoàng kim trái dất
                return Util.nextInt(223, 224);
            case 221: //Set Hoàng kim namec
                return Util.nextInt(225, 226);
            case 222: //Set Hoàng kim xayda
                return Util.nextInt(227, 228);
        }
        return 0;
    }

    public int randomSKHVuongThan(byte gender) {
        if (gender == 3) {
            gender = 2;
        }
        int[] options = {220, 221, 222};
        return options[gender];
    }

    public void setSKHdacBiet(Player player, int idName, int id) {
        Item ao = Util.ratiItemDacBietFull((short) 1973);
        Item quan = Util.ratiItemDacBietFull((short) 1974);
        Item gang = Util.ratiItemDacBietFull((short) 1975);
        Item giay = Util.ratiItemDacBietFull((short) 1976);
        Item nhan = Util.ratiItemDacBietFull((short) 1977);

        ao.itemOptions.add(new ItemOption(idName, 1));
        ao.itemOptions.add(new ItemOption(id, 1));
        ao.itemOptions.remove(ao.itemOptions.stream().filter(itemOption -> itemOption.optionTemplate.id == 21).findFirst().get());
        ao.itemOptions.add(new ItemOption(21, 200));
        ao.itemOptions.add(new ItemOption(30, 1));

        quan.itemOptions.add(new ItemOption(idName, 1));
        quan.itemOptions.add(new ItemOption(id, 1));
        quan.itemOptions.remove(quan.itemOptions.stream().filter(itemOption -> itemOption.optionTemplate.id == 21).findFirst().get());
        quan.itemOptions.add(new ItemOption(21, 200));
        quan.itemOptions.add(new ItemOption(30, 1));

        gang.itemOptions.add(new ItemOption(idName, 1));
        gang.itemOptions.add(new ItemOption(id, 1));
        gang.itemOptions.remove(gang.itemOptions.stream().filter(itemOption -> itemOption.optionTemplate.id == 21).findFirst().get());
        gang.itemOptions.add(new ItemOption(21, 200));
        gang.itemOptions.add(new ItemOption(30, 1));

        giay.itemOptions.add(new ItemOption(idName, 1));
        giay.itemOptions.add(new ItemOption(id, 1));
        giay.itemOptions.remove(giay.itemOptions.stream().filter(itemOption -> itemOption.optionTemplate.id == 21).findFirst().get());
        giay.itemOptions.add(new ItemOption(21, 200));
        giay.itemOptions.add(new ItemOption(30, 1));

        nhan.itemOptions.add(new ItemOption(idName, 1));
        nhan.itemOptions.add(new ItemOption(id, 1));
        nhan.itemOptions.remove(nhan.itemOptions.stream().filter(itemOption -> itemOption.optionTemplate.id == 21).findFirst().get());
        nhan.itemOptions.add(new ItemOption(21, 200));
        nhan.itemOptions.add(new ItemOption(30, 1));

        if (InventoryService.gI().getCountEmptyBag(player) > 4) {
            InventoryService.gI().addItemBag(player, ao, 0);
            InventoryService.gI().addItemBag(player, quan, 0);
            InventoryService.gI().addItemBag(player, gang, 0);
            InventoryService.gI().addItemBag(player, giay, 0);
            InventoryService.gI().addItemBag(player, nhan, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được SKH Đặc biệt ");
            InventoryService.gI().sendItemBags(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 5 ô trống hành trang");
        }
    }
}
