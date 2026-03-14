package nro.services.func.lr;

import nro.models.item.Item;
import nro.models.player.Player;
import nro.services.RewardService;
import nro.services.Service;

import java.util.List;
import nro.consts.ConstItem;

public class LuckyRoundTv extends AbsLuckyRound {

    private static LuckyRoundTv i;

    public static LuckyRoundTv gI() {
        if (i == null) {
            i = new LuckyRoundTv();
        }
        return i;
    }

    private LuckyRoundTv() {
        this.price = 1;
        this.ticket = 821;
        this.icons.add(419);
        this.icons.add(420);
        this.icons.add(421);
        this.icons.add(422);
        this.icons.add(423);
        this.icons.add(424);
        this.icons.add(425);
    }

    @Override
    public List<Item> reward(Player player, byte quantity) {
        List<Item> list = RewardService.gI().getListItemLuckyRound(player, quantity);
        addItemToBox(player, list);
        return list;
    }

    @Override
    public boolean checkMoney(Player player, int price) {
        if (!player.inventory.checkdusoluongitem(player, ConstItem.THOI_VANG, price)) {
            Service.getInstance().sendThongBaoOK(player, "Không đủ Thỏi vàng!");
            return false;
        }
        return true;
    }

    @Override
    public void payWithMoney(Player player, int price) {
        if (!player.inventory.checkdusoluongitem(player, ConstItem.THOI_VANG, price)) {
            Service.getInstance().sendThongBao(player, "Không đủ Thỏi vàng!");
            return;
        }
        player.inventory.subItemInBag(player, ConstItem.THOI_VANG, price);
        Service.getInstance().sendThongBao(player, "Đã trừ " + price + " Thỏi vàng");
    }

}
