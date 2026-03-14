package nro.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.player.Player;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import nro.consts.ConstItem;
import nro.jdbc.DBService;
import nro.server.io.Message;
import nro.utils.Log;
import nro.utils.Util;
import org.json.simple.JSONArray;
import org.json.simple.JSONValue;

/**
 *
 * @author Hoàng Việt - 0857853150
 *
 */
public class TamBao {

    private final List<Item> TAMBAO_LIST_ITEM = new ArrayList<>();
    private final int ID_KEY = 1787;//id Item dùng để quay
    public static final List<TamBao_Item> MOC_TAMBAO = new ArrayList<>();
    private final int[] TILE_ITEM = new int[14];
    private final int[] ACTIVE_VIP = new int[14];
    private static final byte START = 1;

    private static TamBao i;

    public static TamBao gI() {
        if (i == null) {
            i = new TamBao();
        }
        return i;
    }

    public void loadItem_TamBao() {
        Item item1 = ItemService.gI().createNewItem((short) ConstItem.THAN_1, 2);
        item1.itemOptions.add(new ItemOption(73, 1));
        TAMBAO_LIST_ITEM.add(item1);
        TILE_ITEM[0] = 1;
        ACTIVE_VIP[0] = 1;

        Item item2 = ItemService.gI().createNewItem((short) ConstItem.THAN_2, 2);
        item1.itemOptions.add(new ItemOption(73, 1));
        TAMBAO_LIST_ITEM.add(item2);
        TILE_ITEM[1] = 2;
        ACTIVE_VIP[1] = 1;

        Item item7 = ItemService.gI().createNewItem((short) ConstItem.THAN_3, 2);
        item1.itemOptions.add(new ItemOption(73, 1));
        TAMBAO_LIST_ITEM.add(item7);
        TILE_ITEM[2] = 5;
        ACTIVE_VIP[2] = 1;

        Item item4 = ItemService.gI().createNewItem((short) ConstItem.THAN_4, 2);
        item4.itemOptions.add(new ItemOption(73, 1));
        TAMBAO_LIST_ITEM.add(item4);
        TILE_ITEM[3] = 15;
        ACTIVE_VIP[3] = 1;

        Item item5 = ItemService.gI().createNewItem((short) ConstItem.THAN_5, 2);
        item5.itemOptions.add(new ItemOption(73, 1));
        TAMBAO_LIST_ITEM.add(item5);
        TILE_ITEM[4] = 20;
        ACTIVE_VIP[4] = 1;

        Item item6 = ItemService.gI().createNewItem((short) ConstItem.MANH_1, 2);
        item6.itemOptions.add(new ItemOption(73, 1));
        TAMBAO_LIST_ITEM.add(item6);
        TILE_ITEM[5] = 30;
        ACTIVE_VIP[5] = 1;

        Item item3 = ItemService.gI().createNewItem((short) ConstItem.MANH_2, 2);
        item3.itemOptions.add(new ItemOption(73, 1));
        TAMBAO_LIST_ITEM.add(item3);
        TILE_ITEM[6] = 50;
        ACTIVE_VIP[6] = 1;

        Item item8 = ItemService.gI().createNewItem((short) ConstItem.MANH_3, 2);
        item8.itemOptions.add(new ItemOption(73, 1));
        TAMBAO_LIST_ITEM.add(item8);
        TILE_ITEM[7] = 70;
        ACTIVE_VIP[7] = 1;

        Item item9 = ItemService.gI().createNewItem((short) ConstItem.MANH_4, 2);
        item9.itemOptions.add(new ItemOption(73, 1));
        TAMBAO_LIST_ITEM.add(item9);
        TILE_ITEM[8] = 90;
        ACTIVE_VIP[8] = 1;

        Item item10 = ItemService.gI().createNewItem((short) ConstItem.MANH_5, 2);
        item10.itemOptions.add(new ItemOption(73, 1));
        TAMBAO_LIST_ITEM.add(item10);
        TILE_ITEM[9] = 130;
        ACTIVE_VIP[9] = 1;

        Item item11 = ItemService.gI().createNewItem((short) ConstItem.NGOC_RONG_4_SAO, 5);
        item11.itemOptions.add(new ItemOption(73, 1));
        TAMBAO_LIST_ITEM.add(item11);
        TILE_ITEM[10] = 200;
        ACTIVE_VIP[10] = 1;

        Item item12 = ItemService.gI().createNewItem((short) ConstItem.NGOC_RONG_3_SAO, 5);
        item12.itemOptions.add(new ItemOption(73, 1));
        TAMBAO_LIST_ITEM.add(item12);
        TILE_ITEM[11] = 300;
        ACTIVE_VIP[11] = 1;

        Item item13 = ItemService.gI().createNewItem((short) ConstItem.NGOC_RONG_2_SAO, 5);
        item13.itemOptions.add(new ItemOption(73, 1));
        TAMBAO_LIST_ITEM.add(item13);
        TILE_ITEM[12] = 400;
        ACTIVE_VIP[12] = 1;

        Item item14 = ItemService.gI().createNewItem((short) ConstItem.NGOC_RONG_1_SAO, 5);
        item14.itemOptions.add(new ItemOption(73, 1));
        TAMBAO_LIST_ITEM.add(item14);
        TILE_ITEM[13] = 600;
        ACTIVE_VIP[13] = 1;
    }

    public void QuayTamBao(Player pl, int solan) {
        pl.list_id_nhan = new int[14];
        Item key = InventoryService.gI().findItemBagByTemp(pl, ID_KEY);
        if (key == null || key.quantity < solan) {
            Service.getInstance().sendThongBao(pl, "|7|Không đủ " + ItemService.gI().getTemplate(ID_KEY).name);
            return;
        }
        if (InventoryService.gI().getCountEmptyBag(pl) >= solan) {
            String text = "|7|Nhận được\n";
            for (int i = 0; i < solan; i++) {
                int tile = Util.nextInt(1, TILE_ITEM[13]);
                if (pl.diem_quay < 1000 && (tile < TILE_ITEM[0] || tile < TILE_ITEM[1] || tile < TILE_ITEM[2] || tile < TILE_ITEM[3] || tile < TILE_ITEM[4])) {
                    tile = Util.nextInt(TILE_ITEM[3], TILE_ITEM[13]);
                }
                for (int k = 0; k < TILE_ITEM.length; k++) {
                    if (tile <= TILE_ITEM[k]) {
//                        int random = new Random().nextInt(0, TAMBAO_LIST_ITEM.size() - 1);
                        Item item = TAMBAO_LIST_ITEM.get(k);
                        if (item.template.type == 9) {
                            item.quantity = Util.nextInt(1_000_000, 20_000_000);
                        }
                        pl.list_id_nhan[k] = 1;
                        InventoryService.gI().addItemBag(pl, item, -333);
                        if (item.template.type == 9) {
                            text += "|5|x" + Util.format(item.quantity) + " " + item.template.name + "\n";
                        } else if (item.template.id == 457) {
                            text += "|8|x" + Util.format(item.quantity) + " " + item.template.name + "\n";
                        } else if (ACTIVE_VIP[k] == 1) {
                            text += "|1|x" + Util.format(item.quantity) + " " + item.template.name + "\n";
                        } else {
                            text += "|6|x" + Util.format(item.quantity) + " " + item.template.name + "\n";
                        }
                        break;
                    }
                }
            }
            pl.diem_quay += solan;
            InventoryService.gI().subQuantityItemsBag(pl, key, solan);
            Service.getInstance().sendThongBaoFromAdmin(pl, text);
            InventoryService.gI().sendItemBags(pl);
            Send_MocTamBao(pl);
            Send_QuayThuong(pl);
        } else {
            Service.getInstance().sendThongBao(pl, "Hành trang cần ít nhất " + solan + " chổ trống");
        }
    }

    public void Active_TamBao(Player pl, int id) {
        TamBao_Item item = MOC_TAMBAO.get(id);
        if (pl.checkNhan_TamBao[id] == 0) {
            if (pl.diem_quay >= item.max_value) {
                if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
                    pl.listNhan_TamBao.add(id);
                    InventoryService.gI().addItemBag(pl, item, -333);
                    Service.getInstance().sendThongBao(pl, "|2|Đã nhận x" + item.quantity + " " + item.template.name + "\n");
                    InventoryService.gI().sendItemBags(pl);
                    Send_MocTamBao(pl);
                } else {
                    Service.getInstance().sendThongBao(pl, "|7|Hành trang không đủ chổ trống");
                }
            } else {
                Service.getInstance().sendThongBao(pl, "|7|Không đủ điều kiện Nhận thưởng");
            }
        } else {
            Service.getInstance().sendThongBao(pl, "|7|Bạn đã nhận rồi mà !!!");
        }
    }

    public void Check_active(Player pl) {
        try {
            pl.checkNhan_TamBao = new int[MOC_TAMBAO.size()];
            for (int a = 0; a < MOC_TAMBAO.size(); a++) {
                TamBao_Item moc = MOC_TAMBAO.get(a);
                for (int t = 0; t < pl.listNhan_TamBao.size(); t++) {
                    if (pl.listNhan_TamBao.get(t).equals(moc.id_moc)) {
                        pl.checkNhan_TamBao[a] = 1;
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    public void Send_QuayThuong(Player pl) {
        Check_active(pl);
        Message msg = null;
        try {
            msg = new Message(106);
            msg.writer().writeByte(2);
            msg.writer().writeByte(pl.list_id_nhan.length);
            for (int h = 0; h < pl.list_id_nhan.length; h++) {
                msg.writer().writeInt(pl.list_id_nhan[h]);
            }
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void Send_TamBao(Player pl) {
        Message msg = null;
        try {
            msg = new Message(106);
            msg.writer().writeByte(0);
            msg.writer().writeByte(START);
            msg.writer().writeShort((short) ID_KEY);
            msg.writer().writeShort(ItemService.gI().getTemplate(ID_KEY).iconID);
            msg.writer().writeByte(TAMBAO_LIST_ITEM.size());
            for (int h = 0; h < TAMBAO_LIST_ITEM.size(); h++) {
                msg.writer().writeByte(ACTIVE_VIP[h]);
                Item item = TAMBAO_LIST_ITEM.get(h);
                msg.writer().writeShort(item.template.id);
                msg.writer().writeInt(item.quantity);
                msg.writer().writeUTF(item.getInfo());
                msg.writer().writeUTF(item.getContent());
                List<ItemOption> itemOptions = item.getDisplayOptions();
                msg.writer().writeByte(itemOptions.size()); //options
                for (ItemOption o : itemOptions) {
                    msg.writer().writeByte(o.optionTemplate.id);
                    msg.writer().writeInt(o.param);
                }
            }
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void Send_MocTamBao(Player pl) {
        Check_active(pl);
        Message msg = null;
        try {
            msg = new Message(106);
            msg.writer().writeByte(1);
            msg.writer().writeInt(pl.diem_quay);
            msg.writer().writeByte(MOC_TAMBAO.size());
            for (int h = 0; h < MOC_TAMBAO.size(); h++) {
                msg.writer().writeInt(pl.checkNhan_TamBao[h]);
                TamBao_Item item = MOC_TAMBAO.get(h);
                msg.writer().writeShort(item.template.id);
                msg.writer().writeInt(item.quantity);
                msg.writer().writeUTF(item.getInfo());
                msg.writer().writeUTF(item.getContent());
                List<ItemOption> itemOptions = item.getDisplayOptions();
                msg.writer().writeInt(item.id_moc);
                msg.writer().writeInt(item.max_value);
                msg.writer().writeByte(itemOptions.size()); //options
                for (ItemOption o : itemOptions) {
                    msg.writer().writeByte(o.optionTemplate.id);
                    msg.writer().writeInt(o.param);
                }
            }
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void load_mocTamBao() {
        try {
            Connection con = DBService.gI().getConnectionForGame();
            PreparedStatement ps = con.prepareStatement("SELECT * FROM moc_vong_quay");
            ResultSet rs = ps.executeQuery();
            JSONArray jsonArray = null;
            JSONValue jsonValue = new JSONValue();
            while (rs.next()) {
                TamBao_Item tambao = new TamBao_Item();
                int id_moc = rs.getShort("id");
                int id = rs.getShort("item_id");
                tambao.template = ItemService.gI().getTemplate(id);
                tambao.quantity = rs.getInt("quantity");
                tambao.createTime = System.currentTimeMillis();
                tambao.content = tambao.getContent();
                tambao.info = tambao.getInfo();
                tambao.max_value = rs.getInt("max_value");
                tambao.id_moc = id_moc;
                jsonArray = (JSONArray) jsonValue.parse(rs.getString("item_options"));

                for (int j = 0; j < jsonArray.size(); j++) {
                    JSONArray opt = (JSONArray) jsonValue.parse(String.valueOf(jsonArray.get(j)));
                    tambao.itemOptions.add(new ItemOption(Integer.parseInt(String.valueOf(opt.get(0))),
                            Integer.parseInt(String.valueOf(opt.get(1)))));
                }
                MOC_TAMBAO.add(tambao);
            }
            Log.success("Load Mốc Tầm Bảo thành công (" + MOC_TAMBAO.size() + ")");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
