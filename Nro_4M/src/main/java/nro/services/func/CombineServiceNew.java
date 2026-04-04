package nro.services.func;

import editor.Final_Static;
import java.util.ArrayList;
import java.util.Arrays;
import nro.consts.ConstNpc;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.npc.Npc;
import nro.models.npc.NpcManager;
import nro.models.player.Player;
import nro.server.ServerNotify;
import nro.server.io.Message;
import nro.services.InventoryService;
import nro.services.ItemService;
import nro.services.Service;
import nro.utils.Util;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import nro.consts.ConstItem;
import nro.data.ItemData;
import nro.server.Manager;
import nro.services.PhucLoi;
import nro.utils.FileUtils;

public class CombineServiceNew {

    private static final int COST_DOI_VE_DOI_DO_HUY_DIET = 500000000;
    private static final int COST_DAP_DO_KICH_HOAT = 500000000;
    private static final int COST_DOI_MANH_KICH_HOAT = 500000000;

    private static final int COST = 500000000;
    private static final int TI_LE_SKH_DB_SP = 10;
    private static final int TI_LE_SKH_DB_DT = 10;
    private static final byte MAX_STAR_ITEM = 8;
    private static final byte MAX_LEVEL_ITEM = 8;

    private static final byte OPEN_TAB_COMBINE = 0;
    private static final byte REOPEN_TAB_COMBINE = 1;
    private static final byte COMBINE_SUCCESS = 2;
    private static final byte COMBINE_FAIL = 3;
    private static final byte COMBINE_CHANGE_OPTION = 4;
    private static final byte COMBINE_DRAGON_BALL = 5;
    public static final byte OPEN_ITEM = 6;

    public static final int EP_SAO_TRANG_BI = 500;
    public static final int PHA_LE_HOA_TRANG_BI = 501;
    public static final int GO_SAO_PHA_LE_TRANG_BI = 502;
//    public static final int PHA_LE_HOA_TRANG_BI_X100 = 503;

    private static final int COST_GO_SAO_PHA_LE_TRANG_BI = 500000000;

    public static final int NANG_CAP_VAT_PHAM = 510;
    public static final int NANG_CAP_BONG_TAI = 511;
    public static final int LAM_PHEP_NHAP_DA = 512;
    public static final int NHAP_NGOC_RONG = 513;
    public static final int PHAN_RA_DO_THAN_LINH = 514;
    public static final int NANG_CAP_DO_TS = 515;
    public static final int NANG_CAP_SKH_VIP = 516;
    public static final int AN_TRANG_BI = 517;
    public static final int PHAP_SU_HOA = 518;
    public static final int TAY_PHAP_SU = 519;
    public static final int MO_CHI_SO_BONG_TAI = 520;
    public static final int NANG_CAP_SKH_TS = 521;
    public static final int DOT_PHA_BONG_TAI = 522;

    public static final int NANG_CAP_CHAN_MENH = 523;
    public static final int PHAN_RA_DO_HUY_DIET = 524;
    public static final int NANG_CAP_THAN_LINH = 525;
    public static final int NANG_CAP_HUY_DIET = 526;
    public static final int GIA_HAN_VAT_PHAM = 527;
    public static final int PHAN_RA_DO_TS = 528;
    public static final int NANG_CAP_CAI_TRANG = 529;
    public static final int PHAN_RA_VAT_PHAM = 530;
    public static final int NANG_CAP_BONG_TAI_VO_CUC = 531;
    public static final int NANG_CAP_PHU_KIEN = 532;
    public static final int NANG_CAP_BONG_TAI_VIP = 535;
    public static final int NANG_CAP_GIAP_TAP_LUYEN = 5360;

    // START _ SÁCH TUYỆT KỸ //
    public static final int GIAM_DINH_SACH = 2529;
    public static final int TAY_SACH = 2530;
    public static final int NANG_CAP_SACH_TUYET_KY = 2531;
    public static final int PHUC_HOI_SACH = 2532;
    public static final int PHAN_RA_SACH = 2533;
    // END _ SÁCH TUYỆT KỸ //s
    public static final int NANG_CAP_SKH_BILL = 536;
    public static final int DOI_SKH_BILL = 537;

    private final Npc baHatMit;
    private final Npc npcthanmau;
    private final Npc npcwhists;
    private final Npc mighty;

    private static CombineServiceNew i;

    public CombineServiceNew() {
        this.baHatMit = NpcManager.getNpc(ConstNpc.BA_HAT_MIT);
        this.npcthanmau = NpcManager.getNpc(ConstNpc.THAN_MAU);
        this.npcwhists = NpcManager.getNpc(ConstNpc.WHIS);
        this.mighty = NpcManager.getNpc(ConstNpc.MIGHTY);
    }

    public static CombineServiceNew gI() {
        if (i == null) {
            i = new CombineServiceNew();
        }
        return i;
    }



    private void appendSplHistory(Item item, int optionId, int param) {
        String entry = optionId + ":" + param;
        if (item.splHistory == null || item.splHistory.isEmpty()) {
            item.splHistory = entry;
        } else {
            item.splHistory += ";" + entry;
        }
    }

    private boolean hasSplHistory(Item item) {
        return item != null && item.splHistory != null && !item.splHistory.isEmpty();
    }

    private Item rollbackSplHistory(Item itemGoc) {
        if (!hasSplHistory(itemGoc)) {
            return null;
        }

        Item itemMoi = ItemService.gI().copyItem(itemGoc);

        String[] entries = itemMoi.splHistory.split(";");
        for (String entry : entries) {
            if (entry == null || entry.isEmpty() || !entry.contains(":")) {
                continue;
            }

            String[] parts = entry.split(":");
            if (parts.length != 2) {
                continue;
            }

            int optionId = Integer.parseInt(parts[0]);
            int param = Integer.parseInt(parts[1]);

            Iterator<ItemOption> iterator = itemMoi.itemOptions.iterator();
            while (iterator.hasNext()) {
                ItemOption io = iterator.next();
                if (io.optionTemplate.id == optionId) {
                    io.param -= param;
                    if (io.param <= 0) {
                        iterator.remove();
                    }
                    break;
                }
            }
        }

        itemMoi.itemOptions.removeIf(io -> io.optionTemplate.id == 102);
        itemMoi.splHistory = null;
        return itemMoi;
    }
    
    /**
     * Mở tab đập đồ
     *
     * @param player
     * @param type kiểu đập đồ
     */
    public void openTabCombine(Player player, int type) {
        player.combineNew.setTypeCombine(type);
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(OPEN_TAB_COMBINE);
            msg.writer().writeUTF(getTextInfoTabCombine(type));
            msg.writer().writeUTF(getTextTopTabCombine(type));
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * Hiển thị thông tin đập đồ
     *
     * @param player
     * @param index
     */
    public void showInfoCombine(Player player, int[] index) {
        player.combineNew.clearItemCombine();
        if (index.length > 0) {
            for (int j = 0; j < index.length; j++) {
                player.combineNew.itemsCombine.add(player.inventory.itemsBag.get(index[j]));
            }
        }
        switch (player.combineNew.typeCombine) {
            case EP_SAO_TRANG_BI:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item trangBi = null;
                    Item daPhaLe = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (isTrangBiPhaLeHoa(item)) {
                            trangBi = item;
                        } else if (isDaPhaLe(item)) {
                            daPhaLe = item;
                        }
                    }
                    int star = 0; //sao pha lê đã ép
                    int starEmpty = 0; //lỗ sao pha lê
                    if (trangBi != null && daPhaLe != null) {
                        for (ItemOption io : trangBi.itemOptions) {
                            if (io.optionTemplate.id == 102) {
                                star = io.param;
                            } else if (io.optionTemplate.id == 107) {
                                starEmpty = io.param;
                            }
                        }
                        if (star < starEmpty) {
                            player.combineNew.gemCombine = getGemEpSao(star);
                            String npcSay = trangBi.template.name + "\n|2|";
                            for (ItemOption io : trangBi.itemOptions) {
                                if (io.optionTemplate.id != 102) {
                                    npcSay += io.getOptionString() + "\n";
                                }
                            }
                            if (daPhaLe.template.type == 30) {
                                for (ItemOption io : daPhaLe.itemOptions) {
                                    npcSay += "|7|" + io.getOptionString() + "\n";
                                }
                            } else {
                                npcSay += "|7|" + ItemService.gI().getItemOptionTemplate(getOptionDaPhaLe(daPhaLe)).name.replaceAll("#", getParamDaPhaLe(daPhaLe) + "") + "\n";
                            }
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.gemCombine) + " ngọc";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\ncần " + player.combineNew.gemCombine + " ngọc");

                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "Cần 1 trang bị có lỗ sao pha lê và 1 loại đá pha lê để ép vào", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 trang bị có lỗ sao pha lê và 1 loại đá pha lê để ép vào", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 trang bị có lỗ sao pha lê và 1 loại đá pha lê để ép vào", "Đóng");
                }
                break;
            case PHA_LE_HOA_TRANG_BI: {
                if (player.combineNew.itemsCombine.size() == 1) {
                    Item item = player.combineNew.itemsCombine.get(0);
                    if (item == null) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy hãy chọn 1 vật phẩm để pha lê hóa",
                                "Đóng");
                        return;
                    }
                    int star = 0;
                    for (ItemOption io : item.itemOptions) {
                        if (io.optionTemplate.id == 107) {
                            star = io.param;
                            break;
                        }
                    }
                    int slot;
                    slot = 1;
                    if (player.combineNew.itemsCombine.size() == slot) {
                        if (isTrangBiPhaLeHoa(item)) {
                            if (star < MAX_STAR_ITEM) {
                                if (star < 8) {
                                    player.combineNew.goldCombine = getGoldPhaLeHoa(star);
                                    player.combineNew.gemCombine = getGemPhaLeHoa(star);
                                    player.combineNew.ratioCombine = Manager.TILE_NCAP == 0 ? getRatioPhaLeHoa(star) : Manager.TILE_NCAP;

                                    String npcSay = item.template.name + "\n|2|";
                                    for (ItemOption io : item.itemOptions) {
                                        if (io.optionTemplate.id != 102) {
                                            npcSay += io.getOptionString() + "\n";
                                        }
                                    }
                                    npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                                    if (player.combineNew.goldCombine <= player.inventory.gold) {
                                        npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                                        baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                                "Nâng cấp\ncần " + player.combineNew.gemCombine + " ngọc");
                                    } else {
                                        npcSay += "Còn thiếu "
                                                + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold)
                                                + " vàng";
                                        baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                                    }
                                }
                            } else {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        "Vật phẩm đã đạt tối đa sao pha lê", "Đóng");
                            }
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Vật phẩm này không thể đục lỗ",
                                    "Đóng");
                        }
                    } else {
                        if (slot == 1) {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy hãy chọn 1 vật phẩm để pha lê hóa",
                                    "Đóng");
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy hãy chọn 1 vật phẩm để pha lê hóa",
                                    "Đóng");
                        }
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy hãy chọn 1 vật phẩm để pha lê hóa", "Đóng");
                }
                break;
            }
            case GO_SAO_PHA_LE_TRANG_BI: {
                if (player.combineNew.itemsCombine.size() == 1) {
                    Item item = player.combineNew.itemsCombine.get(0);
                    if (item == null || !item.isNotNullItem()) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Hãy chọn 1 trang bị đã ép sao pha lê", "Đóng");
                        return;
                    }
                    if (!hasSaoPhaLeDaEp(item)) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Trang bị này chưa có sao pha lê đã khảm", "Đóng");
                        return;
                    }

                    if (!hasSplHistory(item)) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Trang bị cũ này chưa có dữ liệu lịch sử pha lê để tách chính xác", "Đóng");
                        return;
                    }
                    Item itemMoi = createItemAfterRemoveSpl(item);
                    if (itemMoi == null) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Vật phẩm này chưa được hỗ trợ tách sao pha lê", "Đóng");
                        return;
                    }

                    String npcSay = item.template.name + "\n|2|";
                    for (ItemOption io : item.itemOptions) {
                        if (io.optionTemplate.id != 102 && io.optionTemplate.id != 107) {
                            npcSay += io.getOptionString() + "\n";
                        }
                    }
                    npcSay += "|7|Sau khi tách, trang bị sẽ trở về trạng thái ban đầu\n";
                    npcSay += (player.inventory.gold < COST_GO_SAO_PHA_LE_TRANG_BI ? "|7|" : "|1|")
                            + "Cần " + Util.numberToMoney(COST_GO_SAO_PHA_LE_TRANG_BI) + " vàng";

                    if (player.inventory.gold < COST_GO_SAO_PHA_LE_TRANG_BI) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Còn thiếu vàng");
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                "Tách bỏ\nTinh Thể");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Chỉ cần 1 trang bị đã ép sao pha lê", "Đóng");
                }
                break;
            }
            case NHAP_NGOC_RONG:
                if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                    if (player.combineNew.itemsCombine.size() == 1) {
                        Item item = player.combineNew.itemsCombine.get(0);
                        if (item != null) {
                            int soluong = 7;
                            if (item.isNotNullItem() && (item.template.id > 14 && item.template.id <= 20) && item.quantity >= soluong) {
                                String npcSay = "|2|Con có muốn biến " + soluong + " " + item.template.name + " thành\n"
                                        + "1 viên " + ItemService.gI().getTemplate((short) (item.template.id - 1)).name + "\n"
                                        + "|7|Cần " + soluong + " " + item.template.name;
                                this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay, "Làm phép", "Từ chối");
                            } else {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Không có ép lên được nữa !!!", "Đóng");
                            }
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cần 7 viên ngọc rồng cùng sao trở lên", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hành trang cần ít nhất 1 chỗ trống", "Đóng");
                }
                break;
            case AN_TRANG_BI:
                if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                    if (player.combineNew.itemsCombine.size() == 2) {
                        Item item = player.combineNew.itemsCombine.get(0);
                        Item dangusac = player.combineNew.itemsCombine.get(1);
                        int soluong = 9999;
                        if (item.isNotNullItem() && dangusac.isNotNullItem() && (dangusac.template.id == 1232 || dangusac.template.id == 1233 || dangusac.template.id == 1234)) {
                            if (item.isThanhTon()) {
                                soluong = 299;
                            } else if (item.isThienSuDT()) {
                                soluong = 999;
                            }else if (item.isDTS()) {
                                soluong = 99;
                            }
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cần bỏ đủ vật phẩm yêu cầu", "Đóng");
                            return;
                        }
                        if (isTrangBiAn(item)) {
                            if (item.isNotNullItem() && dangusac.isNotNullItem() && (dangusac.template.id == 1232 || dangusac.template.id == 1233 || dangusac.template.id == 1234) && dangusac.quantity >= soluong) {
                                String npcSay = item.template.name + "\n|2|";
                                for (ItemOption io : item.itemOptions) {
                                    npcSay += io.getOptionString() + "\n";
                                }
                                npcSay += "|1|Con có muốn biến trang bị " + item.template.name + " thành\n"
                                        + "trang bị Ấn không?\b|4|Đục là lên\n"
                                        + "|7|Cần " + soluong + " " + dangusac.template.name;
                                this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay, "Làm phép", "Từ chối");
                            } else {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Bạn chưa bỏ đủ vật phẩm !!!", "Đóng");
                            }
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Vật phẩm này không thể hóa ấn", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cần bỏ đủ vật phẩm yêu cầu", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hành trang cần ít nhất 1 chỗ trống", "Đóng");
                }
                break;
            case NANG_CAP_VAT_PHAM:
                if (player.combineNew.itemsCombine.size() >= 2 && player.combineNew.itemsCombine.size() < 4) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type < 5).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ nâng cấp", "Đóng");
                        break;
                    }
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type == 14).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đá nâng cấp", "Đóng");
                        break;
                    }
                    if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 987).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ nâng cấp", "Đóng");
                        break;
                    }
                    Item itemDo = null;
                    Item itemDNC = null;
                    Item itemDBV = null;
                    for (int j = 0; j < player.combineNew.itemsCombine.size(); j++) {
                        if (player.combineNew.itemsCombine.get(j).isNotNullItem()) {
                            if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.get(j).template.id == 987) {
                                itemDBV = player.combineNew.itemsCombine.get(j);
                                continue;
                            }
                            if (player.combineNew.itemsCombine.get(j).template.type < 5) {
                                itemDo = player.combineNew.itemsCombine.get(j);
                            } else {
                                itemDNC = player.combineNew.itemsCombine.get(j);
                            }
                        }
                    }
                    if (isCoupleItemNangCapCheck(itemDo, itemDNC)) {
                        int level = 0;
                        for (ItemOption io : itemDo.itemOptions) {
                            if (io.optionTemplate.id == 72) {
                                level = io.param;
                                break;
                            }
                        }
                        if (level < MAX_LEVEL_ITEM) {
                            player.combineNew.goldCombine = getGoldNangCapDo(level);
                            player.combineNew.ratioCombine = (float) getTileNangCapDo(level);
                            player.combineNew.countDaNangCap = getCountDaNangCapDo(level);
                            player.combineNew.countDaBaoVe = (short) getCountDaBaoVe(level);
                            String npcSay = "|2|Hiện tại " + itemDo.template.name + " (+" + level + ")\n|0|";
                            for (ItemOption io : itemDo.itemOptions) {
                                if (io.optionTemplate.id != 72) {
                                    npcSay += io.getOptionString() + "\n";
                                }
                            }
                            String option = null;
                            int param = 0;
                            for (ItemOption io : itemDo.itemOptions) {
                                if (io.optionTemplate.id == 47
                                        || io.optionTemplate.id == 6
                                        || io.optionTemplate.id == 0
                                        || io.optionTemplate.id == 7
                                        || io.optionTemplate.id == 14
                                        || io.optionTemplate.id == 22
                                        || io.optionTemplate.id == 23) {
                                    option = io.optionTemplate.name;
                                    param = io.param + (io.param * 10 / 100);
                                    break;
                                }
                            }
                            npcSay += "|2|Sau khi nâng cấp (+" + (level + 1) + ")\n|7|"
                                    + option.replaceAll("#", String.valueOf(param))
                                    + "\n|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%\n"
                                    + (player.combineNew.countDaNangCap > itemDNC.quantity ? "|7|" : "|1|")
                                    + "Cần " + player.combineNew.countDaNangCap + " " + itemDNC.template.name
                                    + "\n" + (player.combineNew.goldCombine > player.inventory.gold ? "|7|" : "|1|")
                                    + "Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";

                            String daNPC = player.combineNew.itemsCombine.size() == 3 && itemDBV != null ? String.format("\nCần tốn %s đá bảo vệ", player.combineNew.countDaBaoVe) : "";
                            if ((level == 2 || level == 4 || level == 6 || level == 7) && !(player.combineNew.itemsCombine.size() == 3 && itemDBV != null)) {
                                npcSay += "\nNếu thất bại sẽ rớt xuống (+" + (level - 1) + ")";
                                npcSay += "\nVà giảm 5% chỉ số gốc";
                            }
                            if (player.combineNew.countDaNangCap > itemDNC.quantity) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        npcSay, "Còn thiếu\n" + (player.combineNew.countDaNangCap - itemDNC.quantity) + " " + itemDNC.template.name);
                            } else if (player.combineNew.goldCombine > player.inventory.gold) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        npcSay, "Còn thiếu\n" + Util.numberToMoney((player.combineNew.goldCombine - player.inventory.gold)) + " vàng");
                            } else if (player.combineNew.itemsCombine.size() == 3 && Objects.nonNull(itemDBV) && itemDBV.quantity < player.combineNew.countDaBaoVe) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        npcSay, "Còn thiếu\n" + (player.combineNew.countDaBaoVe - itemDBV.quantity) + " đá bảo vệ");
                            } else {
                                this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                                        npcSay, "Nâng cấp\n" + Util.numberToMoney(player.combineNew.goldCombine) + " vàng" + daNPC, "Từ chối");
                            }
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Trang bị của ngươi đã đạt cấp tối đa", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy chọn 1 trang bị và 1 loại đá nâng cấp", "Đóng");
                    }
                } else {
                    if (player.combineNew.itemsCombine.size() > 3) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cất đi con ta không thèm", "Đóng");
                        break;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy chọn 1 trang bị và 1 loại đá nâng cấp", "Đóng");
                }
                break;

            case NANG_CAP_CAI_TRANG:
                if (player.combineNew.itemsCombine.size() == 3) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type == 5).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Chưa bỏ Cải trang vào", "Đóng");
                        break;
                    }
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDHD()).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ hủy diệt", "Đóng");
                        return;
                    }
                    Item itemDo = null;
                    Item itemTV = null;
                    Item cloudsky = null;
                    for (int j = 0; j < player.combineNew.itemsCombine.size(); j++) {
                        if (player.combineNew.itemsCombine.get(j).isNotNullItem()) {
                            if (player.combineNew.itemsCombine.get(j).template.type == 5) {
                                itemDo = player.combineNew.itemsCombine.get(j);
                            } else if (player.combineNew.itemsCombine.get(j).template.id == ConstItem.XU_DAC_BIET) {
                                cloudsky = player.combineNew.itemsCombine.get(j);
                            } else {
                                itemTV = player.combineNew.itemsCombine.get(j);
                            }
                        }
                    }
                    int level = 0;
                    if (itemDo != null) {
                        for (ItemOption io : itemDo.itemOptions) {
                            if (io.optionTemplate.id == 72) {
                                level = io.param;
                                break;
                            }
                        }
                    }
                    if (level < 10) {
                        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 457).count() < 1) {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Chưa bỏ Thỏi vàng vào", "Đóng");
                            break;
                        }
                    }
                    if (level < 10) {
                        player.combineNew.ratioCombine = (float) getTileNangCaiTrang(level);
                        player.combineNew.countDaNangCap = getCountDaNangCapCaiTrang(level);
                        if (itemDo == null || !ItemData.caitrang_update.contains((int) itemDo.template.id)) {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cải trang này không thể nâng cấp", "Đóng");
                            break;
                        }
                        if (itemTV == null || itemTV.quantity < player.combineNew.countDaNangCap) {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Chưa đủ Thỏi vàng", "Đóng");
                            break;
                        }
                        String npcSay = "|2|Hiện tại " + itemDo.template.name + " (+" + level + ")\n|0|";
                        for (ItemOption io : itemDo.itemOptions) {
                            if (io.optionTemplate.id != 72) {
                                npcSay += io.getOptionString() + "\n";
                            }
                        }
                        npcSay += "|2|Sau khi nâng cấp (+" + (level + 1) + ")\n|7|"
                                + "+" + getChiSoSD(level) + "%Sức đánh"
                                + "+" + getChiSoHPKI(level) + "%HP"
                                + "+" + getChiSoHPKI(level) + "%KI"
                                + "\n|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%\n"
                                + (player.combineNew.countDaNangCap > itemTV.quantity ? "|7|" : "|1|")
                                + "Cần " + player.combineNew.countDaNangCap + " " + itemTV.template.name;
                        if (player.combineNew.countDaNangCap > itemTV.quantity) {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    npcSay, "Còn thiếu\n" + (player.combineNew.countDaNangCap - itemTV.quantity) + " " + itemTV.template.name);
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                                    npcSay, "Nâng cấp\n" + Util.numberToMoney(player.combineNew.countDaNangCap) + " Thỏi vàng", "Từ chối");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Trang bị của ngươi đã đạt cấp tối đa", "Đóng");
                    }
                } else {
                    if (player.combineNew.itemsCombine.size() > 2) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cất đi con ta không thèm", "Đóng");
                        break;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy chọn 1 Cải trang chỉ định và Thỏi vàng để nâng cấp", "Đóng");
                }
                break;
            case NANG_CAP_PHU_KIEN:
                if (player.combineNew.itemsCombine.size() == 4) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && isTrangBiPhuKien(item)).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Chưa bỏ Phụ kiện vào", "Đóng");
                        break;
                    }
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDTL()).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ Thần linh", "Đóng");
                        return;
                    }
                    Item itemDo = null;
                    Item itemTV = null;
                    Item itemDNS = null;
                    for (int j = 0; j < player.combineNew.itemsCombine.size(); j++) {
                        if (player.combineNew.itemsCombine.get(j).isNotNullItem()) {
                            if (isTrangBiPhuKien(player.combineNew.itemsCombine.get(j))) {
                                itemDo = player.combineNew.itemsCombine.get(j);
                            } else if (player.combineNew.itemsCombine.get(j).template.id == 674) {
                                itemDNS = player.combineNew.itemsCombine.get(j);
                            } else if (player.combineNew.itemsCombine.get(j).template.id == 457) {
                                itemTV = player.combineNew.itemsCombine.get(j);
                            }
                        }
                    }
                    int level = 0;
                    if (itemDo != null) {
                        for (ItemOption io : itemDo.itemOptions) {
                            if (io.optionTemplate.id == 72) {
                                level = io.param;
                                break;
                            }
                        }
                    }
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 457).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Chưa bỏ Thỏi vàng vào", "Đóng");
                        break;
                    }
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 674).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Chưa bỏ Đá ngũ sắc vào", "Đóng");
                        break;
                    }
                    if (level < 10) {
                        player.combineNew.ratioCombine = (float) getTileNangPK(level);
                        player.combineNew.countDaNangCap = getCountDaNangPK(level);
                        player.combineNew.goldCombine = getCountTVNangPK(level);
                        player.combineNew.countDaNangCap = getCountDaNangPK(level);
                        if (itemDo == null || !isTrangBiPhuKien(itemDo)) {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Phụ kiện này không thể nâng cấp", "Đóng");
                            break;
                        }
                        if (itemTV == null || itemTV.quantity < player.combineNew.goldCombine) {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Chưa đủ Thỏi vàng", "Đóng");
                            break;
                        }
                        if (itemDNS == null || itemDNS.quantity < player.combineNew.countDaNangCap) {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Chưa đủ Đá ngũ sắc", "Đóng");
                            break;
                        }
                        String npcSay = "|2|Hiện tại " + itemDo.template.name + " (+" + level + ")\n|0|";
                        for (ItemOption io : itemDo.itemOptions) {
                            if (io.optionTemplate.id != 72) {
                                npcSay += io.getOptionString() + "\n";
                            }
                        }
                        npcSay += "|2|Sau khi nâng cấp (+" + (level + 1) + ")\n|7|"
                                + "+" + getChiSoSDHPKI(level) + "%Sức đánh"
                                + "+" + getChiSoSDHPKI(level) + "%HP"
                                + "+" + getChiSoSDHPKI(level) + "%KI"
                                + "\n|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%\n"
                                + (player.combineNew.countDaNangCap > itemDNS.quantity ? "|7|" : "|1|")
                                + "Cần " + player.combineNew.countDaNangCap + " " + itemDNS.template.name;
                        if (player.combineNew.goldCombine > itemTV.quantity) {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    npcSay, "Còn thiếu\n" + (player.combineNew.goldCombine - itemTV.quantity) + " " + itemTV.template.name);
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                                    npcSay, "Nâng cấp\n" + Util.numberToMoney(player.combineNew.goldCombine) + " Thỏi vàng", "Từ chối");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Trang bị của ngươi đã đạt cấp tối đa", "Đóng");
                    }
                } else {
                    if (player.combineNew.itemsCombine.size() > 4) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cất đi con ta không thèm", "Đóng");
                        break;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy chọn 1 Phụ kiện + 1 món Hủy diệt + Đá ngũ sắc và Thỏi vàng để nâng cấp", "Đóng");
                }
                break;
            case NANG_CAP_CHAN_MENH:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item bongTai = null;
                    Item manhVo = null;
                    int star = 0;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (item.template.id == 1318) {
                            manhVo = item;
                        } else if (item.template.id >= 1300 && item.template.id <= 1308) {
                            bongTai = item;
                            star = item.template.id - 1300;
                        }
                    }
                    if (bongTai != null && bongTai.template.id == 1308) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Chân Mệnh đã đạt cấp tối đa", "Đóng");
                        return;
                    }
                    player.combineNew.DiemNangcap = getDiemNangcapChanmenh(star);
                    player.combineNew.DaNangcap = getDaNangcapChanmenh(star);
                    player.combineNew.TileNangcap = Manager.TILE_NCAP == 0 ? getTiLeNangcapChanmenh(star) : Manager.TILE_NCAP;
                    if (bongTai != null && manhVo != null && (bongTai.template.id >= 1300 && bongTai.template.id < 1308)) {
                        String npcSay = bongTai.template.name + "\n|2|";
                        for (ItemOption io : bongTai.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.TileNangcap + "%" + "\n";
                        if (player.combineNew.DiemNangcap <= player.inventory.ruby) {
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.DiemNangcap) + " Hồng ngọc";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\ncần " + player.combineNew.DaNangcap + " Đá Hoàng Kim");
                        } else {
                            npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.DiemNangcap - player.inventory.ruby) + " Hồng ngọc";
                            baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 Chân Mệnh và Đá Hoàng Kim", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 Chân Mệnh và Đá Hoàng Kim", "Đóng");
                }
                break;
            case NANG_CAP_BONG_TAI:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item bongTai = null;
                    Item manhVo = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        switch (item.template.id) {
                            case 454:
                                bongTai = item;
                                break;
                            case 933:
                                manhVo = item;
                                break;
                            case 921:
                                bongTai = item;
                                break;
                            case 1165:
                                bongTai = item;
                                break;
                            case 1129:
                                bongTai = item;
                                break;
                            default:
                                break;
                        }
                    }
                    if (bongTai != null && manhVo != null && manhVo.quantity >= 99 && bongTai.template.id == 454) {

                        player.combineNew.goldCombine = 500000000;
                        player.combineNew.gemCombine = 1000;
                        player.combineNew.ratioCombine = 50;

                        String npcSay = "Bông tai Porata cấp 2" + "\n|2|";
                        for (ItemOption io : bongTai.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                        if (player.combineNew.goldCombine <= player.inventory.gold) {
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\ncần " + player.combineNew.gemCombine + " Hồng ngọc");
                        } else {
                            npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else if (bongTai != null && manhVo != null && manhVo.quantity >= 99 && bongTai.template.id == 921) {

                        player.combineNew.goldCombine = 1000000;
                        player.combineNew.gemCombine = 5000;
                        player.combineNew.ratioCombine = 20;

                        String npcSay = "Bông tai Porata cấp 3" + "\n|2|";
                        for (ItemOption io : bongTai.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                        if (player.combineNew.goldCombine <= player.inventory.gold) {
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\ncần " + player.combineNew.gemCombine + " Hồng ngọc");
                        } else {
                            npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else if (bongTai != null && manhVo != null && manhVo.quantity >= 99 && bongTai.template.id == 1165) {

                        player.combineNew.goldCombine = 1000000;
                        player.combineNew.gemCombine = 15000;
                        player.combineNew.ratioCombine = 10;

                        String npcSay = "Bông tai Porata cấp 4" + "\n|2|";
                        for (ItemOption io : bongTai.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                        if (player.combineNew.goldCombine <= player.inventory.gold) {
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\ncần " + player.combineNew.gemCombine + " Hồng ngọc");
                        } else {
                            npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else if (bongTai != null && manhVo != null && manhVo.quantity >= 99 && bongTai.template.id == 1129) {

                        player.combineNew.goldCombine = 2000000000;
                        player.combineNew.gemCombine = 50000;
                        player.combineNew.ratioCombine = 10;

                        String npcSay = "Bông tai Porata cấp 5" + "\n|2|";
                        for (ItemOption io : bongTai.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                        if (player.combineNew.goldCombine <= player.inventory.gold) {
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\ncần " + player.combineNew.gemCombine + " Hồng ngọc");
                        } else {
                            npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 Bông tai Porata cấp 1, 2, 3, 4 và Mảnh vỡ bông tai", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 Bông tai Porata cấp 1, 2, 3, 4 và Mảnh vỡ bông tai", "Đóng");
                }
                break;
            case NANG_CAP_BONG_TAI_VO_CUC:
                if (player.combineNew.itemsCombine.size() == 3) {
                    Item bongTai = null;
                    Item manhVo = null;
                    Item xuVang = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        switch (item.template.id) {
                            case 1550:
                                bongTai = item;
                                break;
                            case 1549:
                                manhVo = item;
                                break;
                            case ConstItem.XU_SERVER:
                                xuVang = item;
                                break;
                            default:
                                break;
                        }
                    }
                    if (xuVang == null || xuVang.quantity < 20) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Không đủ 20 " + ConstItem.NAME_XU_SERVER, "Đóng");
                        return;
                    }
                    if (bongTai != null && manhVo != null && manhVo.quantity >= 99 && bongTai.template.id == 1550) {

                        player.combineNew.goldCombine = 500000;
                        player.combineNew.gemCombine = 1000;
                        player.combineNew.ratioCombine = 10;

                        String npcSay = "Bông tai Porata Vô Cực" + "\n|2|";
                        for (ItemOption io : bongTai.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                        if (player.combineNew.goldCombine <= player.inventory.gold) {
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\ncần " + player.combineNew.gemCombine + " Hồng ngọc\n"
                                    + "Và 20 " + ConstItem.NAME_XU_SERVER);
                        } else {
                            npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 Bông tai Porata cấp 5, Mảnh vỡ bông tai Vô cực và " + ConstItem.NAME_XU_SERVER, "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 Bông tai Porata cấp 5, Mảnh vỡ bông tai Vô cực và " + ConstItem.NAME_XU_SERVER, "Đóng");
                }
                break;
            case MO_CHI_SO_BONG_TAI:
                if (player.combineNew.itemsCombine.size() == 3) {
                    Item bongTai = null;
                    Item manhHon = null;
                    Item daXanhLam = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        switch (item.template.id) {
                            case 1944://c7
                            case 1945://c8
                            case 1946://c9
                            case 1999:
                            case 1550:
                            case 1129:
                            case 1165:
                            case 921:
                                bongTai = item;
                                break;
                            case 934:
                                manhHon = item;
                                break;
                            case 935:
                                daXanhLam = item;
                                break;
                            default:
                                break;
                        }
                    }
                    if (bongTai != null && manhHon != null && daXanhLam != null && manhHon.quantity >= 99) {

                        player.combineNew.goldCombine = 2000000000;
                        player.combineNew.gemCombine = 1000;

                        String npcSay;
                        switch (bongTai.template.id) {
                            case 1944:
                                npcSay = "Bông tai Porata cấp 7" + "\n|2|";
                                player.combineNew.ratioCombine = 20;
                                break;
                            case 1945:
                                npcSay = "Bông tai Porata cấp 8" + "\n|2|";
                                player.combineNew.ratioCombine = 20;
                                break;
                            case 1946:
                                npcSay = "Bông tai Porata cấp 9" + "\n|2|";
                                player.combineNew.ratioCombine = 20;
                                break;
                            case 1999:
                                npcSay = "Bông tai Porata Vô Cực" + "\n|2|";
                                player.combineNew.ratioCombine = 20;
                                break;
                            case 1550:
                                npcSay = "Bông tai Porata cấp 5" + "\n|2|";
                                player.combineNew.ratioCombine = 40;
                                break;
                            case 1129:
                                npcSay = "Bông tai Porata cấp 4" + "\n|2|";
                                player.combineNew.ratioCombine = 70;
                                break;
                            case 1165:
                                npcSay = "Bông tai Porata cấp 3" + "\n|2|";
                                player.combineNew.ratioCombine = 70;
                                break;
                            default:
                                npcSay = "Bông tai Porata cấp 2" + "\n|2|";
                                player.combineNew.ratioCombine = 70;
                                break;
                        }
                        for (ItemOption io : bongTai.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                        if (player.combineNew.goldCombine <= player.inventory.gold) {
                            npcSay += "|1|Cần " + Util.numberToMoney(player.combineNew.goldCombine) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\ncần " + player.combineNew.gemCombine + " Hồng ngọc");
                        } else {
                            npcSay += "Còn thiếu " + Util.numberToMoney(player.combineNew.goldCombine - player.inventory.gold) + " vàng";
                            baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, npcSay, "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 Bông tai Porata, X99 Mảnh hồn bông tai và 1 Đá xanh lam", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 Bông tai Porata, X99 Mảnh hồn bông tai và 1 Đá xanh lam", "Đóng");
                }
                break;
            case DOT_PHA_BONG_TAI:
                if (player.combineNew.itemsCombine.size() == 3) {
                    Item bongTai = null;
                    Item thoivang = null;
                    Item daXanhLam = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        switch (item.template.id) {
                            case 1550:
                            case 1129:
                            case 1165:
                            case 921:
                                bongTai = item;
                                break;
                            case 457:
                                thoivang = item;
                                break;
                            case 935:
                                daXanhLam = item;
                                break;
                            default:
                                break;
                        }
                    }
                    if (bongTai != null) {
                        if (bongTai.itemOptions.size() >= 1) {
                            if (checkHaveOption(bongTai, 0, 248)) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        "Bạn đã làm rồi mà", "Đóng");
                                return;
                            }
                        }
                        if (bongTai.itemOptions.size() >= 2) {
                            if (checkHaveOption(bongTai, 1, 248)) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        "Bạn đã làm rồi mà", "Đóng");
                                return;
                            }
                        }
                        if (bongTai.itemOptions.size() >= 3) {
                            if (checkHaveOption(bongTai, 2, 248)) {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                        "Bạn đã làm rồi mà", "Đóng");
                                return;
                            }
                        }
                        if (thoivang != null && daXanhLam != null && thoivang.quantity >= 10) {

                            player.combineNew.goldCombine = 2000000000;
                            player.combineNew.gemCombine = 50000;
                            player.combineNew.ratioCombine = 50;

                            String npcSay;
                            switch (bongTai.template.id) {
                                case 1550:
                                    npcSay = "Bông tai Porata cấp 5" + "\n|2|";
                                    break;
                                case 1129:
                                    npcSay = "Bông tai Porata cấp 4" + "\n|2|";
                                    break;
                                case 1165:
                                    npcSay = "Bông tai Porata cấp 3" + "\n|2|";
                                    break;
                                default:
                                    npcSay = "Bông tai Porata cấp 2" + "\n|2|";
                                    break;
                            }
                            for (ItemOption io : bongTai.itemOptions) {
                                npcSay += io.getOptionString() + "\n";
                            }
                            npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                            baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                    "Nâng cấp\ncần " + player.combineNew.gemCombine + " Hồng ngọc");
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                    "Cần 1 Bông tai Porata, 10 Thỏi vàng và 1 Đá xanh lam", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 Bông tai Porata, 10 Thỏi vàng và 1 Đá xanh lam", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 Bông tai Porata, 10 Thỏi vàng và 1 Đá xanh lam", "Đóng");
                }
                break;
            case PHAN_RA_DO_HUY_DIET:
                if (player.combineNew.itemsCombine.size() == 0) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Con hãy đưa ta đồ Hủy diệt", "Đóng");
                    return;
                }
                if (player.combineNew.itemsCombine.size() == 1) {
                    int huydietok = 0;
                    Item item = player.combineNew.itemsCombine.get(0);
                    if (item.isNotNullItem()) {
                        if (item.template.id >= 650 && item.template.id <= 662) {
                            huydietok = 1;
                        }
                    }
                    if (huydietok == 0) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Ta chỉ có thể chuyển hóa đồ Hủy diệt thôi", "Đóng");
                        return;
                    }

                    // Kiểm tra số lượng thỏi vàng (ID 457)
                    int requiredGoldBars = 20_000;
                    Item thoivang = InventoryService.gI().findItemBagByTemp(player, 457);
                    int playerGoldBars = thoivang != null ? thoivang.quantity : 0;

                    String npcSay = "|2|Sau khi chuyển hóa vật phẩm\n|7|"
                            + "Bạn sẽ nhận được : 1 " + " Phiếu Hủy diệt Tương ứng\n"
                            + (playerGoldBars < requiredGoldBars ? "|7|" : "|1|")
                            + "Cần " + requiredGoldBars + " Thỏi vàng";

                    if (playerGoldBars < requiredGoldBars) {
                        this.baHatMit.npcChat(player, "Không đủ thỏi vàng để thực hiện.");
                        return;
                    }

                    this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                            npcSay, "Nâng cấp\n" + requiredGoldBars + " Thỏi vàng", "Từ chối");

                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Ta chỉ có thể chuyển hóa 1 lần 1 món đồ Hủy diệt", "Đóng");
                }
                break;
            case PHAN_RA_DO_TS:
                if (player.combineNew.itemsCombine.size() == 0) {
                    this.npcwhists.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Con hãy đưa ta đồ Thiên sứ", "Đóng");
                    return;
                }
                if (player.combineNew.itemsCombine.size() == 1) {
                    int dothiensu = 0;
                    Item item = player.combineNew.itemsCombine.get(0);

                    if (!item.isDTS()) {
                        this.npcwhists.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ Thiên sứ", "Đóng");
                        return;
                    }
                    if (item.isNotNullItem()) {
                        if (item.isDTS()) {
                            dothiensu = 1;
                        }
                    }
                    if (dothiensu == 0) {
                        this.npcwhists.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Ta chỉ có thể chuyển hóa đồ Thiên sứ thôi", "Đóng");
                        return;
                    }
                    String npcSay = "|2|Sau khi chuyển hóa vật phẩm\n|7|"
                            + "Bạn sẽ nhận được : 500 " + " Mảnh thiên sứ Tương ứng\n"
                            + (500000000 > player.inventory.gold ? "|7|" : "|1|")
                            + "Cần " + Util.numberToMoney(500000000) + " vàng";

                    if (player.inventory.gold < 500000000) {
                        this.npcwhists.npcChat(player, "Hết tiền rồi\nẢo ít thôi con");
                        return;
                    }
                    this.npcwhists.createOtherMenu(player, ConstNpc.MENU_PHAN_RA_TS,
                            npcSay, "Nâng cấp\n" + Util.numberToMoney(500000000) + " vàng", "Từ chối");
                } else {
                    this.npcwhists.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Ta chỉ có thể chuyển hóa 1 lần 1 món đồ Hủy diệt", "Đóng");
                }
                break;
            case NANG_CAP_DO_TS:
                if (player.combineNew.itemsCombine.size() == 0) {
                    this.npcwhists.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy đưa ta 2 món Hủy Diệt bất kì và 1 món Thần Linh cùng loại", "Đóng");
                    return;
                }
                if (player.combineNew.itemsCombine.size() == 3) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isCongThuc()).count() < 1) {
                        this.npcwhists.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu mảnh Công thức", "Đóng");
                        return;
                    }
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 1083).count() < 1) {
                        this.npcwhists.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đá cầu vòng", "Đóng");
                        return;
                    }
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isManhTS() && item.quantity >= 999).count() < 1) {
                        this.npcwhists.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu mảnh thiên sứ", "Đóng");
                        return;
                    }

                    String npcSay = "|2|Con có muốn đổi các món nguyên liệu ?\n|7|"
                            + "Và nhận được " + player.combineNew.itemsCombine.stream().filter(Item::isManhTS).findFirst().get().typeNameManh() + " thiên sứ tương ứng\n"
                            + "|1|Cần " + Util.numberToMoney(COST) + " vàng";

                    if (player.inventory.gold < COST) {
                        this.npcwhists.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hết tiền rồi\nẢo ít thôi con", "Đóng");
                        return;
                    }
                    this.npcwhists.createOtherMenu(player, ConstNpc.MENU_NANG_CAP_DO_TS,
                            npcSay, "Nâng cấp\n" + Util.numberToMoney(COST) + " vàng", "Từ chối");
                } else {
                    if (player.combineNew.itemsCombine.size() > 3) {
                        this.npcwhists.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cất đi con ta không thèm", "Đóng");
                        return;
                    }
                    this.npcwhists.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Còn thiếu nguyên liệu để nâng cấp hãy quay lại sau", "Đóng");
                }
                break;
            case NANG_CAP_SKH_VIP:
                if (player.combineNew.itemsCombine.size() == 0) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy đưa ta 3 món Hủy diệt", "Đóng");
                    return;
                }
                if (player.combineNew.itemsCombine.size() == 3) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDHD()).count() < 3) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ hủy diệt", "Đóng");
                        return;
                    }
                    Item thoivang = null;
                    try {
                        thoivang = InventoryService.gI().findItemBagByTemp(player, 457);
                    } catch (Exception e) {
                    }
                    String npcSay = "|2|Con có muốn đổi các món nguyên liệu ?\n|7|"
                            + "Và nhận được\n|0|"
                            + player.combineNew.itemsCombine.stream().filter(Item::isDHD).findFirst().get().typeName() + " kích hoạt VIP tương ứng\n"
                            + ((thoivang == null || thoivang.quantity < 30) ? "|7|" : "|1|")
                            + "Cần 30 Thỏi vàng";

                    if (player.inventory.gem < 1000) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hết tiền rồi\nẢo ít thôi con", "Đóng");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.MENU_NANG_DOI_SKH_VIP,
                            npcSay, "Nâng cấp\n" + 1000 + " ngọc", "Từ chối");
                } else {
                    if (player.combineNew.itemsCombine.size() > 3) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Nguyên liệu không phù hợp", "Đóng");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Còn thiếu nguyên liệu để nâng cấp hãy quay lại sau", "Đóng");
                }
                break;
            case NANG_CAP_SKH_TS:
                if (player.combineNew.itemsCombine.size() == 0) {
                    this.npcwhists.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy đưa ta 3 món Thiên sứ", "Đóng");
                    return;
                }
                if (player.combineNew.itemsCombine.size() == 3) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDTS()).count() < 3) {
                        this.npcwhists.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ Thiên sứ", "Đóng");
                        return;
                    }
                    Item thoivang = null;
                    try {
                        thoivang = InventoryService.gI().findItemBagByTemp(player, 1400);
                    } catch (Exception e) {
                    }
                    String npcSay = "|2|Con có muốn đổi các món nguyên liệu ?\n|7|"
                            + "Và nhận được\n|0|"
                            + player.combineNew.itemsCombine.stream().filter(Item::isDTS).findFirst().get().typeName() + " kích hoạt VIP tương ứng\n"
                            + ((thoivang == null || thoivang.quantity < 50) ? "|7|" : "|1|")
                            + "Cần 50 " + ConstItem.NAME_XU_SERVER;

                    if (player.inventory.gem < 1000) {
                        this.npcwhists.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hết tiền rồi\nẢo ít thôi con", "Đóng");
                        return;
                    }
                    this.npcwhists.createOtherMenu(player, ConstNpc.MENU_NANG_DO_SKH_TS,
                            npcSay, "Nâng cấp\n" + 1000 + " ngọc", "Từ chối");
                } else {
                    if (player.combineNew.itemsCombine.size() > 3) {
                        this.npcwhists.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Nguyên liệu không phù hợp", "Đóng");
                        return;
                    }
                    this.npcwhists.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Còn thiếu nguyên liệu để nâng cấp hãy quay lại sau", "Đóng");
                }
                break;
            case NANG_CAP_THAN_LINH:
                if (player.combineNew.itemsCombine.isEmpty()) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy đưa ta 1 món Thần linh", "Đóng");
                    return;
                }
                if (player.combineNew.itemsCombine.size() == 1) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDTL()).count() < 1) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ Thần linh", "Đóng");
                        return;
                    }
                    Item doThanLinh = player.combineNew.itemsCombine.get(0);
                    String npcSay = "|2|Con có muốn nâng cấp " + doThanLinh.template.name + " Thành" + "\n|7|"
                            + doThanLinh.typeName() + " Hủy diệt " + Service.getInstance().get_HanhTinh(doThanLinh.template.gender) + "\n|0|"
                            + doThanLinh.typeOption() + "+?\n"
                            + "Yêu cầu sức mạnh 80 tỉ\n"
                            + "Không thể giao dịch\n"
                            + ((player.inventory.ruby < 10000) ? "|7|" : "|1|")
                            + "Cần 2Tỷ vàng";

                    if (player.inventory.gold < 2_000_000_000) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hết tiền rồi\nẢo ít thôi con", "Đóng");
                        return;
                    }
                    this.baHatMit.createOtherMenu(player, ConstNpc.MENU_NANG_CAP_THAN_LINH,
                            npcSay, "Nâng cấp\n2Tỷ vàng", "Từ chối");
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Còn thiếu nguyên liệu để nâng cấp hãy quay lại sau", "Đóng");
                }
                break;
            case GIA_HAN_VAT_PHAM:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item thegh = null;
                    Item itemGiahan = null;
                    for (Item item_ : player.combineNew.itemsCombine) {
                        if (item_.template.id == 1346) {
                            thegh = item_;
                        } else if (item_.isTrangBiHSD()) {
                            itemGiahan = item_;
                        }
                    }
                    if (thegh == null) {
                        Service.getInstance().sendThongBaoOK(player, "Cần 1 trang bị có hạn sử dụng và 1 phiếu Gia hạn");
                        return;
                    }
                    if (itemGiahan == null) {
                        Service.getInstance().sendThongBaoOK(player, "Cần 1 trang bị có hạn sử dụng và 1 phiếu Gia hạn");
                        return;
                    }
                    for (ItemOption itopt : itemGiahan.itemOptions) {
                        if (itopt.optionTemplate.id == 93) {
                            if (itopt.param < 0 || itopt == null) {
                                Service.getInstance().sendThongBaoOK(player, "Trang bị này không phải trang bị có Hạn Sử Dụng");
                                return;
                            }
                        }
                    }
                    String npcSay = "Trang bị được gia hạn \"" + itemGiahan.template.name + "\"\n|1|";
                    npcSay += itemGiahan.template.name + "\n|2|";
                    for (ItemOption io : itemGiahan.itemOptions) {
                        npcSay += io.getOptionString() + "\n";
                    }
                    npcSay += "\n|0|Sau khi gia hạn +1 ngày\n";

                    npcSay += "|0|Tỉ lệ thành công: 100%" + "\n";
                    if (player.inventory.gold > 200000000) {
                        npcSay += "|2|Cần 200Tr vàng";
                        this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                "Nâng cấp", "Từ chối");

                    } else if (player.inventory.gold < 200000000) {
                        int SoVangThieu2 = (int) (200000000 - player.inventory.gold);
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Bạn còn thiếu " + SoVangThieu2 + " vàng");
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cần 1 trang bị có hạn sử dụng và 1 phiếu Gia hạn");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hành trang cần ít nhất 1 chỗ trống");
                }
                break;
            case PHAP_SU_HOA:
                if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                    if (player.combineNew.itemsCombine.size() == 2) {
                        Item item = player.combineNew.itemsCombine.get(0);
                        Item dangusac = player.combineNew.itemsCombine.get(1);
                        if (isTrangBiPhapsu(item)) {
                            if (item != null && item.isNotNullItem() && dangusac != null && dangusac.isNotNullItem() && dangusac.template.id == 1235 && dangusac.quantity >= 1) {
                                String npcSay = item.template.name + "\n|2|";
                                for (ItemOption io : item.itemOptions) {
                                    npcSay += io.getOptionString() + "\n";
                                }
                                npcSay += "|1|Con có muốn biến trang bị " + item.template.name + " thành\n"
                                        + "trang bị Pháp sư hóa không?\n"
                                        + "|7|Cần 1 " + dangusac.template.name;
                                this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay, "Làm phép", "Từ chối");
                            } else {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Bạn chưa bỏ đủ vật phẩm !!!", "Đóng");
                            }
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Vật phẩm này không thể Pháp sư hóa", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cần bỏ đủ vật phẩm yêu cầu", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hành trang cần ít nhất 1 chỗ trống", "Đóng");
                }
                break;
            case TAY_PHAP_SU:
                if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                    if (player.combineNew.itemsCombine.size() == 2) {
                        Item item = player.combineNew.itemsCombine.get(0);
                        Item dangusac = player.combineNew.itemsCombine.get(1);
                        if (isTrangBiPhapsu(item)) {
                            if (item != null && item.isNotNullItem() && dangusac != null && dangusac.isNotNullItem() && dangusac.template.id == 1236 && dangusac.quantity >= 1) {
                                String npcSay = item.template.name + "\n|2|";
                                for (ItemOption io : item.itemOptions) {
                                    npcSay += io.getOptionString() + "\n";
                                }
                                npcSay += "|1|Con có muốn tẩy trang bị " + item.template.name + " về\n"
                                        + "lúc chưa Pháp sư hóa không?\n"
                                        + "|7|Cần 1 " + dangusac.template.name;
                                this.baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay, "Làm phép", "Từ chối");
                            } else {
                                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Bạn chưa bỏ đủ vật phẩm !!!", "Đóng");
                            }
                        } else {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Vật phẩm này không thể thực hiện", "Đóng");
                        }
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Cần bỏ đủ vật phẩm yêu cầu", "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hành trang cần ít nhất 1 chỗ trống", "Đóng");
                }
                break;

            // START _ SÁCH TUYỆT KỸ //
            case GIAM_DINH_SACH:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item sachTuyetKy = null;
                    Item buaGiamDinh = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (issachTuyetKy(item)) {
                            sachTuyetKy = item;
                        } else if (item.template.id == 1508) {
                            buaGiamDinh = item;
                        }
                    }
                    if (sachTuyetKy != null && buaGiamDinh != null) {

                        String npcSay = "|1|" + sachTuyetKy.getName() + "\n";
                        npcSay += "|2|" + buaGiamDinh.getName() + " " + buaGiamDinh.quantity + "/5" + "\n";
                        npcSay += "|2|Cần 10.000 Hồng ngọc";
                        baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                "Giám định", "Từ chối");
                    } else {
                        Service.getInstance().sendThongBaoOK(player, "Cần Sách Tuyệt Kỹ và bùa giám định");
                        return;
                    }
                } else {
                    Service.getInstance().sendThongBaoOK(player, "Cần Sách Tuyệt Kỹ và bùa giám định");
                    return;
                }
                break;
            case TAY_SACH:
                if (player.combineNew.itemsCombine.size() == 1) {
                    Item sachTuyetKy = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (issachTuyetKy(item)) {
                            sachTuyetKy = item;
                        }
                    }
                    if (sachTuyetKy != null) {
                        String npcSay = "|2|Tẩy Sách Tuyệt Kỹ";
                        baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                "Đồng ý", "Từ chối");
                    } else {
                        Service.getInstance().sendThongBaoOK(player, "Cần Sách Tuyệt Kỹ để tẩy");
                        return;
                    }
                } else {
                    Service.getInstance().sendThongBaoOK(player, "Cần Sách Tuyệt Kỹ để tẩy");
                    return;
                }
                break;

            case NANG_CAP_SACH_TUYET_KY:
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item sachTuyetKy = null;
                    Item kimBamGiay = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (issachTuyetKy(item) && (item.template.id == 1510 || item.template.id == 1512 || item.template.id == 1514)) {
                            sachTuyetKy = item;
                        } else if (item.template.id == 1507) {
                            kimBamGiay = item;
                        }
                    }
                    if (sachTuyetKy != null && kimBamGiay != null) {
                        String npcSay = "|2|Nâng cấp sách tuyệt kỹ\n";
                        npcSay += "Cần 100 Kìm bấm giấy\n"
                                + "Tỉ lệ thành công: 30%\n"
                                + "Cần 50.000 Hồng ngọc\n"
                                + "Nâng cấp thất bại sẽ mất 100 Kìm bấm giấy";
                        baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                "Nâng cấp", "Từ chối");
                    } else {
                        Service.getInstance().sendThongBaoOK(player, "Cần Sách Tuyệt Kỹ 1 và 100 Kìm bấm giấy.");
                        return;
                    }
                } else {
                    Service.getInstance().sendThongBaoOK(player, "Cần Sách Tuyệt Kỹ 1 và 10 Kìm bấm giấy.");
                    return;
                }
                break;
            case PHUC_HOI_SACH:
                if (player.combineNew.itemsCombine.size() == 1) {
                    Item sachTuyetKy = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (issachTuyetKy(item)) {
                            sachTuyetKy = item;
                        }
                    }
                    if (sachTuyetKy != null) {
                        String npcSay = "|2|Phục hồi " + sachTuyetKy.getName() + "\n"
                                + "Cần 10 cuốn sách cũ\n"
                                + "Phí phục hồi 10 triệu vàng";
                        baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                "Đồng ý", "Từ chối");
                    } else {
                        Service.getInstance().sendThongBaoOK(player, "Không tìm thấy vật phẩm");
                        return;
                    }
                } else {
                    Service.getInstance().sendThongBaoOK(player, "Không tìm thấy vật phẩm");
                    return;
                }
                break;
            case PHAN_RA_SACH:
                if (player.combineNew.itemsCombine.size() == 1) {
                    Item sachTuyetKy = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        if (issachTuyetKy(item)) {
                            sachTuyetKy = item;
                        }
                    }
                    if (sachTuyetKy != null) {
                        String npcSay = "|2|Phân rã sách\n"
                                + "Nhận lại 5 cuốn sách cũ\n"
                                + "Phí rã 10 triệu vàng";
                        baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                "Đồng ý", "Từ chối");
                    } else {
                        Service.getInstance().sendThongBaoOK(player, "Không tìm thấy vật phẩm");
                        return;
                    }
                } else {
                    Service.getInstance().sendThongBaoOK(player, "Không tìm thấy vật phẩm");
                    return;
                }
                break;

            // END _ SÁCH TUYỆT KỸ //
            case PHAN_RA_VAT_PHAM:
                if (player.combineNew.itemsCombine.size() == 0) {
                    this.mighty.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Con hãy đưa ta đồ Thần linh hoặc Hủy diệt", "Đóng");
                    return;
                }
                if (player.combineNew.itemsCombine.size() == 1) {
                    int dothiensu = 0;
                    Item item = player.combineNew.itemsCombine.get(0);

                    if (!item.isDTL() && !item.isDHD()) {
                        this.mighty.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu đồ Thần linh hoặc Hủy diệt", "Đóng");
                        return;
                    }
                    if (item.isNotNullItem()) {
                        if (item.isDTL() || item.isDHD()) {
                            dothiensu = 1;
                        }
                    }
                    if (dothiensu == 0) {
                        this.mighty.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Ta chỉ có thể chuyển hóa đồ Thần linh hoặc Hủy diệt thôi", "Đóng");
                        return;
                    }
                    String soluong = "random vật phẩm";
//                    if (item.isDTL()) {
//                        soluong = "1-30 Thỏi vàng";
//                    } else {
//                        soluong = "1-50 Thỏi vàng";
//                    }
                    String npcSay = "|2|Sau khi chuyển hóa vật phẩm\n|7|"
                            + "Bạn sẽ nhận được " + soluong + "\n"
                            + (20000 > player.inventory.ruby ? "|7|" : "|1|")
                            + "Cần " + Util.numberToMoney(20000) + " Hồng ngọc";

                    if (player.inventory.ruby < 20000) {
                        this.mighty.npcChat(player, "Hết Ngọc rồi\nẢo ít thôi con");
                        return;
                    }
                    this.mighty.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                            npcSay, "Nâng cấp\n" + Util.numberToMoney(20000) + " Hồng ngọc", "Từ chối");
                } else {
                    this.mighty.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Ta chỉ có thể chuyển hóa 1 lần 1 món đồ Thần linh hoặc Hủy diệt", "Đóng");
                }
                break;
            case NANG_CAP_SKH_BILL:
                if (player.combineNew.itemsCombine.size() == 0) {
                    this.npcthanmau.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy đưa ta 1 món trang bị", "Đóng");
                    return;
                }
                if (player.combineNew.itemsCombine.size() == 2) {
                    if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isManhTS() && item.quantity >= 10000).count() < 1) {
                        this.npcthanmau.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Mảnh đồ thiên sứ", "Đóng");
                        return;
                    }
                    Item dahoangkim = null; //1782 
                    try {
                        dahoangkim = InventoryService.gI().findItemBagByTemp(player, 1791);
                    } catch (Exception e) {
                    }

                    Optional<Item> optTrangBi = player.combineNew.itemsCombine.stream()
                            .filter(Item::isTrangBi)
                            .findFirst();

                    String tenTrangBi = optTrangBi.map(Item::typeName).orElse("Trang bị");

                    String npcSay = "|2|Con có muốn đổi các món nguyên liệu ?\n|7|"
                            + "Và nhận được\n|0|"
                            + tenTrangBi + " kích hoạt Bill tương ứng\n"
                            + ((dahoangkim == null || dahoangkim.quantity < 100) ? "|7|" : "|1|")
                            + "Cần 100 Đá Vũ Trụ\n"
                            + "|7|Tỉ lệ thành công 50% / 100%\n";

                    this.npcthanmau.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                            npcSay, "Nâng cấp\n" + Util.numberToMoney(100) + " Đá Vũ Trụ");
                } else {
                    if (player.combineNew.itemsCombine.size() > 2) {
                        this.npcthanmau.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Nguyên liệu không phù hợp", "Đóng");
                        return;
                    }
                    this.npcthanmau.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Còn thiếu nguyên liệu để nâng cấp hãy quay lại sau", "Đóng");
                }
                break;
            case DOI_SKH_BILL:
                if (player.combineNew.itemsCombine.size() == 0) {
                    this.npcthanmau.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy đưa ta 1 món trang bị có dòng SKH Vũ Trụ", "Đóng");
                    return;
                }
                if (player.combineNew.itemsCombine.size() == 2) {
                    Item dahoangkim = null; //1782 
                    Item dochinh = null;
                    Item dophoi = null;
                    for (int j = 0; j < player.combineNew.itemsCombine.size(); j++) {
                        Item item = player.combineNew.itemsCombine.get(j);
                        if (item.isNotNullItem() && item.template.type < 5) {
                            if (dochinh == null && item.isCheckSKHBill()) {
                                dochinh = item; // Gán dochinh nếu là trang bị và có SKH Bill
                            } else if (item != dochinh && !(item.isCheckSKHBill())) {
                                dophoi = item; // Gán dophoi là item còn lại (khác dochinh)
                            }
                        }
                    }

                    if (dochinh == null) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Đồ SKH Vũ Trụ", "Đóng");
                        break;
                    }
                    if (dophoi == null) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu Đồ Phôi", "Đóng");
                        break;
                    }

                    try {
                        dahoangkim = InventoryService.gI().findItemBagByTemp(player, 1791);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    String tenTrangBi = dophoi.typeName();

                    String npcSay = "|2|Con có muốn đổi các món nguyên liệu ?\n|7|"
                            + "Và nhận được\n|0|"
                            + tenTrangBi + " có chỉ số kích hoạt Vũ Trụ\n"
                            + ((dahoangkim == null || dahoangkim.quantity < 1000) ? "|7|" : "|1|")
                            + "Cần 1000 Đá Vũ Trụ\n"
                            + "|7|Tỉ lệ thành công 100% / 100%\n";

                    this.npcthanmau.createOtherMenu(player, ConstNpc.MENU_START_COMBINE,
                            npcSay, "Nâng cấp\n" + Util.numberToMoney(100) + " Đá Vũ Trụ");
                } else {
                    if (player.combineNew.itemsCombine.size() > 2) {
                        this.npcthanmau.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Nguyên liệu không phù hợp", "Đóng");
                        return;
                    }
                    this.npcthanmau.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Còn thiếu nguyên liệu để nâng cấp hãy quay lại sau", "Đóng");
                }
                break;
            case NANG_CAP_BONG_TAI_VIP:
                if (player.combineNew.itemsCombine.size() == 3) {
                    Item bongTai = null;
                    Item phieuHD = null;
                    Item daAS = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        switch (item.template.id) {
                            case 1999://btai c6
                            case 1944://btai c7
                            case 1945://btai c8
                                bongTai = item;
                                break;
                            case 1327:
                            case 1328:
                            case 1329:
                            case 1330:
                            case 1331:
                                phieuHD = item;
                                break;
                            case ConstItem.XU_SERVER:
                                daAS = item;
                                break;
                            default:
                                break;
                        }
                    }
                    if (daAS == null || daAS.quantity < 99) {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu 200 " + ConstItem.NAME_XU_SERVER, "Đóng");
                        return;
                    }
                    if (bongTai != null && phieuHD != null && phieuHD.quantity >= 10 && bongTai.template.id == 1999) {

                        player.combineNew.DaNangcap = 99;
                        player.combineNew.gemCombine = 100000;
                        player.combineNew.ratioCombine = 30;

                        String npcSay = "Bông tai Porata cấp 6" + "\n|2|";
                        for (ItemOption io : bongTai.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                        baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                "Nâng cấp\ncần " + player.combineNew.gemCombine + " Hồng ngọc");
                    } else if (bongTai != null && phieuHD != null && phieuHD.quantity >= 10 && bongTai.template.id == 1944) {

                        player.combineNew.DaNangcap = 99;
                        player.combineNew.gemCombine = 100000;
                        player.combineNew.ratioCombine = 20;

                        String npcSay = "Bông tai Porata cấp 7" + "\n|2|";
                        for (ItemOption io : bongTai.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                        baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                "Nâng cấp\ncần " + player.combineNew.gemCombine + " Hồng ngọc");
                    } else if (bongTai != null && phieuHD != null && phieuHD.quantity >= 10 && bongTai.template.id == 1945) {

                        player.combineNew.DaNangcap = 99;
                        player.combineNew.gemCombine = 100000;
                        player.combineNew.ratioCombine = 10;

                        String npcSay = "Bông tai Porata cấp 8" + "\n|2|";
                        for (ItemOption io : bongTai.itemOptions) {
                            npcSay += io.getOptionString() + "\n";
                        }
                        npcSay += "|7|Tỉ lệ thành công: " + player.combineNew.ratioCombine + "%" + "\n";
                        baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                "Nâng cấp\ncần " + player.combineNew.gemCombine + " Hồng ngọc");
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 Bông tai Porata cấp 6, 7, 8 + 10 Phiếu Hủy diệt bất kì và " + ConstItem.NAME_XU_SERVER, "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 Bông tai Porata cấp 6, 7, 8 + 10 Phiếu Hủy diệt bất kì và " + ConstItem.NAME_XU_SERVER, "Đóng");
                }
                break;
            case NANG_CAP_GIAP_TAP_LUYEN:
                if (player.combineNew.itemsCombine.size() == 3) {
                    Item giapTapLuyen = null;
                    Item manhTS = null;
                    Item xuVang = null;
                    for (Item item : player.combineNew.itemsCombine) {
                        switch (item.template.id) {
                            case 1901://gtl 4
                            case 1902://gtl 5
                            case 1903://gtl 6
                            case 1904://gtl 7
                            case 1905://gtl 8
                                giapTapLuyen = item;
                                break;
                            case 1066:
                            case 1067:
                            case 1068:
                            case 1069:
                            case 1070:
                                manhTS = item;
                                break;
                            case ConstItem.XU_SERVER:
                                xuVang = item;
                                break;
                            default:
                                break;
                        }
                    }

                    if (giapTapLuyen != null && manhTS != null && manhTS.quantity >= 999) {

                        player.combineNew.DaNangcap = getLinhThachNCapGTL(giapTapLuyen.template.id);
                        player.combineNew.gemCombine = 100000;
                        player.combineNew.ratioCombine = getTiLeNCapGTL(giapTapLuyen.template.id);

                        if (xuVang == null || xuVang.quantity < player.combineNew.DaNangcap) {
                            this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Không đủ " + player.combineNew.DaNangcap + " " + ConstItem.NAME_XU_SERVER, "Đóng");
                            return;
                        }
                        String npcSay = giapTapLuyen.template.name + "\n";
                        npcSay += "|2|Sau nâng cấp:\n";
                        npcSay += "|1|" + getOptionsGTL(giapTapLuyen.template.id + 1) + "% Sức đánh" + "\n";
                        npcSay += getOptionsGTL(giapTapLuyen.template.id + 1) + "% HP" + "\n";
                        npcSay += getOptionsGTL(giapTapLuyen.template.id + 1) + "% KI" + "\n";
                        npcSay += "|7|Tỉ lệ thành công: " + getTiLeNCapGTL_Ao(giapTapLuyen.template.id) + "%" + "\n";
                        npcSay += "|7|Tốn: " + player.combineNew.DaNangcap + " " + ConstItem.NAME_XU_SERVER + "\n";
                        baHatMit.createOtherMenu(player, ConstNpc.MENU_START_COMBINE, npcSay,
                                "Nâng cấp\ncần " + player.combineNew.gemCombine + " Hồng ngọc");
                    } else {
                        this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                "Cần 1 Giáp Tập Luyện cấp 4,5,6,7,8 + 999 mảnh Thiên sứ bất kì và " + ConstItem.NAME_XU_SERVER, "Đóng");
                    }
                } else {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                            "Cần 1 Giáp Tập Luyện cấp 4,5,6,7,8 + 999 mảnh Thiên sứ bất kì và " + ConstItem.NAME_XU_SERVER, "Đóng");
                }
                break;
        }
    }

    /**
     * Bắt đầu đập đồ - điều hướng từng loại đập đồ
     *
     * @param player
     */
    public void startCombine(Player player) {
        switch (player.combineNew.typeCombine) {
            case EP_SAO_TRANG_BI:
                epSaoTrangBi(player);
                break;
            case PHA_LE_HOA_TRANG_BI:
                phaLeHoaTrangBi(player);
                break;
            case GO_SAO_PHA_LE_TRANG_BI:
                goSaoPhaLeTrangBi(player);
                break;
            case NHAP_NGOC_RONG:
                nhapNgocRong(player);
                break;
            case AN_TRANG_BI:
                antrangbi(player);
                break;
            case PHAN_RA_DO_HUY_DIET:
                chuyenhoahuydiet(player);
                break;
            case PHAN_RA_DO_TS:
                PhanRaDoTS(player);
                break;
            case NANG_CAP_DO_TS:
                openDTS(player);
                break;
            case NANG_CAP_SKH_VIP:
                openSKHVIP(player);
                break;
            case NANG_CAP_SKH_TS:
                openSKHts(player);
                break;
            case NANG_CAP_THAN_LINH:
                NcapDoThanLinh(player);
                break;
            case NANG_CAP_VAT_PHAM:
                nangCapVatPham(player);
                break;
            case NANG_CAP_CAI_TRANG:
                nangCapCaiTrang(player);
                break;
            case NANG_CAP_PHU_KIEN:
                nangCapPhuKien(player);
                break;
            case NANG_CAP_BONG_TAI:
                nangCapBongTai(player);
                break;
            case NANG_CAP_BONG_TAI_VO_CUC:
                nangCapBongTaiVoCuc(player);
                break;
            case MO_CHI_SO_BONG_TAI:
                moChiSoBongTai2345(player);
            case DOT_PHA_BONG_TAI:
                DotPhaBongTai(player);
            case PHAP_SU_HOA:
                phapsuhoa(player);
                break;
            case TAY_PHAP_SU:
                tayphapsu(player);
                break;
            case NANG_CAP_CHAN_MENH:
                nangCapChanMenh(player);
                break;
            case GIA_HAN_VAT_PHAM:
                GiaHanTrangBi(player);
                break;
//            case OPTION_PORATA:
//                nangCapVatPham(player);
//                break;   
            // START _ SÁCH TUYỆT KỸ //
            case GIAM_DINH_SACH:
                giamDinhSach(player);
                break;
            case TAY_SACH:
                taySach(player);
                break;
            case NANG_CAP_SACH_TUYET_KY:
                nangCapSachTuyetKy(player);
                break;
            case PHUC_HOI_SACH:
                phucHoiSach(player);
                break;
            case PHAN_RA_SACH:
                phanRaSach(player);
                break;
            // END _ SÁCH TUYỆT KỸ //
            case PHAN_RA_VAT_PHAM:
                PhanRaVatPham(player);
                break;
            case NANG_CAP_SKH_BILL:
                openSKHbill(player);
                break;
            case DOI_SKH_BILL:
                openDoiSKHbill(player);
                break;
            case NANG_CAP_BONG_TAI_VIP:
                nangCapBongTaiVIP(player);
                break;
            case NANG_CAP_GIAP_TAP_LUYEN:
                nangCapGiapTapLuyen(player);
                break;
        }

        player.iDMark.setIndexMenu(ConstNpc.IGNORE_MENU);
        player.combineNew.clearParamCombine();
        player.combineNew.lastTimeCombine = System.currentTimeMillis();

    }

    public void GetTrangBiKichHoathuydiet(Player player, int id) {
        Item item = ItemService.gI().createNewItem((short) id);
        int[][] optionNormal = {{127, 128}, {130, 132}, {133, 135}};
        int[][] paramNormal = {{139, 140}, {142, 144}, {136, 138}};
        int[][] optionVIP = {{129}, {131}, {134}};
        int[][] paramVIP = {{141}, {143}, {137}};
        int random = Util.nextInt(optionNormal.length);
        int randomSkh = Util.nextInt(100);
        if (item.template.type == 0) {
            item.itemOptions.add(new ItemOption(47, Util.nextInt(1500, 2000)));
        }
        if (item.template.type == 1) {
            item.itemOptions.add(new ItemOption(22, Util.nextInt(100, 150)));
        }
        if (item.template.type == 2) {
            item.itemOptions.add(new ItemOption(0, Util.nextInt(9000, 11000)));
        }
        if (item.template.type == 3) {
            item.itemOptions.add(new ItemOption(23, Util.nextInt(90, 150)));
        }
        if (item.template.type == 4) {
            item.itemOptions.add(new ItemOption(14, Util.nextInt(15, 20)));
        }
        if (randomSkh <= 20) {//tile ra do kich hoat
            if (randomSkh <= 5) { // tile ra option vip
                item.itemOptions.add(new ItemOption(optionVIP[player.gender][0], 0));
                item.itemOptions.add(new ItemOption(paramVIP[player.gender][0], 0));
                item.itemOptions.add(new ItemOption(30, 0));
            } else {// 
                item.itemOptions.add(new ItemOption(optionNormal[player.gender][random], 0));
                item.itemOptions.add(new ItemOption(paramNormal[player.gender][random], 0));
                item.itemOptions.add(new ItemOption(30, 0));
            }
        }

        InventoryService.gI().addItemBag(player, item, 0);
        InventoryService.gI().sendItemBags(player);
    }

    public void GetTrangBiKichHoatthiensu(Player player, int id) {
        Item item = ItemService.gI().createNewItem((short) id);
        int[][] optionNormal = {{127, 128}, {130, 132}, {133, 135}};
        int[][] paramNormal = {{139, 140}, {142, 144}, {136, 138}};
        int[][] optionVIP = {{129}, {131}, {134}};
        int[][] paramVIP = {{141}, {143}, {137}};
        int random = Util.nextInt(optionNormal.length);
        int randomSkh = Util.nextInt(100);
        if (item.template.type == 0) {
            item.itemOptions.add(new ItemOption(47, Util.nextInt(2000, 2500)));
        }
        if (item.template.type == 1) {
            item.itemOptions.add(new ItemOption(22, Util.nextInt(150, 200)));
        }
        if (item.template.type == 2) {
            item.itemOptions.add(new ItemOption(0, Util.nextInt(18000, 20000)));
        }
        if (item.template.type == 3) {
            item.itemOptions.add(new ItemOption(23, Util.nextInt(150, 200)));
        }
        if (item.template.type == 4) {
            item.itemOptions.add(new ItemOption(14, Util.nextInt(20, 25)));
        }
        if (randomSkh <= 20) {//tile ra do kich hoat
            if (randomSkh <= 5) { // tile ra option vip
                item.itemOptions.add(new ItemOption(optionVIP[player.gender][0], 0));
                item.itemOptions.add(new ItemOption(paramVIP[player.gender][0], 0));
                item.itemOptions.add(new ItemOption(30, 0));
            } else {// 
                item.itemOptions.add(new ItemOption(optionNormal[player.gender][random], 0));
                item.itemOptions.add(new ItemOption(paramNormal[player.gender][random], 0));
                item.itemOptions.add(new ItemOption(30, 0));
            }
        }

        InventoryService.gI().addItemBag(player, item, 0);
        InventoryService.gI().sendItemBags(player);
    }

    private void doiManhKichHoat(Player player) {
        if (player.combineNew.itemsCombine.size() == 2 || player.combineNew.itemsCombine.size() == 3) {
            Item nr1s = null, doThan = null, buaBaoVe = null;
            for (Item it : player.combineNew.itemsCombine) {
                if (it.template.id == 14) {
                    nr1s = it;
                } else if (it.template.id == 2010) {
                    buaBaoVe = it;
                } else if (it.template.id >= 555 && it.template.id <= 567) {
                    doThan = it;
                }
            }

            if (nr1s != null && doThan != null) {
                if (InventoryService.gI().getCountEmptyBag(player) > 0
                        && player.inventory.gold >= COST_DOI_MANH_KICH_HOAT) {
                    player.inventory.gold -= COST_DOI_MANH_KICH_HOAT;
                    int tiLe = buaBaoVe != null ? 100 : 50;
                    if (Util.isTrue(tiLe, 100)) {
                        sendEffectSuccessCombine(player);
                        Item item = ItemService.gI().createNewItem((short) 2009);
                        item.itemOptions.add(new ItemOption(30, 0));
                        InventoryService.gI().addItemBag(player, item, 0);
                    } else {
                        sendEffectFailCombine(player);
                    }
                    InventoryService.gI().subQuantityItemsBag(player, nr1s, 1);
                    InventoryService.gI().subQuantityItemsBag(player, doThan, 1);
                    if (buaBaoVe != null) {
                        InventoryService.gI().subQuantityItemsBag(player, buaBaoVe, 1);
                    }
                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendMoney(player);
                    reOpenItemCombine(player);
                }
            } else {
                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Hãy chọn 1 trang bị thần linh và 1 viên ngọc rồng 1 sao", "Đóng");
            }
        }
    }

    private void chuyenhoahuydiet(Player player) {
        if (player.combineNew.itemsCombine.size() == 1) {
            Item thoivang = null;
            try {
                thoivang = InventoryService.gI().findItemBagByTemp(player, 457);
            } catch (Exception e) {
            }
            if (thoivang == null || thoivang.quantity < 20_000) {
                Service.getInstance().sendThongBao(player, "Không đủ Thỏi vàng");
                return;
            }
            Item item = player.combineNew.itemsCombine.get(0);
            Item phieu = null;
            switch (item.template.id) {
                case 650:
                case 652:
                case 654:
                    phieu = ItemService.gI().createNewItem((short) 1327);
                    break;
                case 651:
                case 653:
                case 655:
                    phieu = ItemService.gI().createNewItem((short) 1328);
                    break;
                case 657:
                case 659:
                case 661:
                    phieu = ItemService.gI().createNewItem((short) 1329);
                    break;
                case 658:
                case 660:
                case 662:
                    phieu = ItemService.gI().createNewItem((short) 1330);
                    break;
                default:
                    phieu = ItemService.gI().createNewItem((short) 1331);
                    break;
            }
            sendEffectSuccessCombine(player);
            this.baHatMit.npcChat(player, "Con đã nhận được 1 " + phieu.template.name);
            InventoryService.gI().subQuantityItemsBag(player, item, 1);
            InventoryService.gI().subQuantityItemsBag(player, thoivang, 20_000);
            player.combineNew.itemsCombine.clear();
            InventoryService.gI().addItemBag(player, phieu, 0);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendMoney(player);
            reOpenItemCombine(player);
        }
    }

    private void PhanRaDoTS(Player player) {
        if (player.combineNew.itemsCombine.size() == 1) {
            player.inventory.gold -= 500000000;
            Item item = player.combineNew.itemsCombine.get(0);
            Item manhts = null;
            switch (item.template.id) {
                case 1048:
                case 1049:
                case 1050:
                    manhts = ItemService.gI().createNewItem((short) 1066);
                    break;
                case 1051:
                case 1052:
                case 1053:
                    manhts = ItemService.gI().createNewItem((short) 1067);
                    break;
                case 1054:
                case 1055:
                case 1056:
                    manhts = ItemService.gI().createNewItem((short) 1070);
                    break;
                case 1057:
                case 1058:
                case 1059:
                    manhts = ItemService.gI().createNewItem((short) 1068);
                    break;
                default:
                    manhts = ItemService.gI().createNewItem((short) 1069);
                    break;
            }
            sendEffectSuccessCombine(player);
            manhts.quantity = 500;
            this.npcwhists.npcChat(player, "Con đã nhận được 500 " + manhts.template.name);
            InventoryService.gI().subQuantityItemsBag(player, item, 1);
            player.combineNew.itemsCombine.clear();
            InventoryService.gI().addItemBag(player, manhts, 999);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendMoney(player);
            reOpenItemCombine(player);
        }
    }

    public void openDTS(Player player) {
        //check sl đồ tl, đồ hd
        // new update 2 mon huy diet + 1 mon than linh(skh theo style) +  5 manh bat ki
        if (player.combineNew.itemsCombine.size() != 3) {
            Service.getInstance().sendThongBao(player, "Thiếu đồ");
            return;
        }
        if (player.inventory.gold < COST) {
            Service.getInstance().sendThongBao(player, "Ảo ít thôi con...");
            return;
        }
        if (InventoryService.gI().getCountEmptyBag(player) < 1) {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
            return;
        }
        Item itemTL = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isCongThuc()).findFirst().get();
        Item itemHDs = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 1083).findFirst().get();
        Item itemManh = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isManhTS() && item.quantity >= 999).findFirst().get();

        player.inventory.gold -= COST;
        sendEffectSuccessCombine(player);
        short[][] itemIds = {{1048, 1051, 1054, 1057, 1060}, {1049, 1052, 1055, 1058, 1061}, {1050, 1053, 1056, 1059, 1062}}; // thứ tự td - 0,nm - 1, xd - 2

        Item itemTS = ItemService.gI().DoThienSu(itemIds[itemTL.template.gender > 2 ? player.gender : itemTL.template.gender][itemManh.typeIdManh()], itemTL.template.gender);
        InventoryService.gI().addItemBag(player, itemTS, 0);

        InventoryService.gI().subQuantityItemsBag(player, itemTL, 1);
        InventoryService.gI().subQuantityItemsBag(player, itemManh, 999);
        InventoryService.gI().subQuantityItemsBag(player, itemHDs, 1);
        InventoryService.gI().sendItemBags(player);
        Service.getInstance().sendMoney(player);
        Service.getInstance().sendThongBao(player, "Bạn đã nhận được " + itemTS.template.name);
        player.combineNew.itemsCombine.clear();
        reOpenItemCombine(player);
    }

    public void openSKHVIP(Player player) {
        Item thoivang = null;
        try {
            thoivang = InventoryService.gI().findItemBagByTemp(player, 457);
        } catch (Exception e) {
        }
        if (thoivang == null || thoivang.quantity < 30) {
            Service.getInstance().sendThongBao(player, "Không đủ Thỏi vàng");
            return;
        }
        if (player.combineNew.itemsCombine.size() != 3) {
            Service.getInstance().sendThongBao(player, "Thiếu nguyên liệu");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDHD()).count() != 3) {
            Service.getInstance().sendThongBao(player, "Thiếu đồ hủy diệt");
            return;
        }
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            if (player.inventory.gem < 1000) {
                Service.getInstance().sendThongBao(player, "Con cần thêm ngoc xanh để đổi...");
                return;
            }
            player.inventory.gem -= 1000;
            Item itemTS = player.combineNew.itemsCombine.stream().filter(Item::isDHD).findFirst().get();
            List<Item> itemDHD = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDHD()).collect(Collectors.toList());
            CombineServiceNew.gI().sendEffectOpenItem(player, itemTS.template.iconID, itemTS.template.iconID);
            short itemId;
            if (player.gender == 3 || itemTS.template.type == 4) {
                itemId = Manager.radaSKHVip[Util.nextInt(0, 5)];
                if (Util.isTrue(3, (int) 100)) {
                    itemId = Manager.radaSKHVip[6];
                }
            } else {
                itemId = Manager.doSKHVip[player.gender][itemTS.template.type][Util.nextInt(0, 5)];
                if (Util.isTrue(3, (int) 100)) {
                    itemId = Manager.doSKHVip[player.gender][itemTS.template.type][6];
                }
            }
            int skhId = ItemService.gI().randomSKHId(player.gender);
            Item item;
            if (new Item(itemId).isDTL()) {
                item = Util.ratiItemTL(itemId);
                item.itemOptions.add(new ItemOption(skhId, 1));
                item.itemOptions.add(new ItemOption(ItemService.gI().optionIdSKH(skhId), 1));
                item.itemOptions.remove(item.itemOptions.stream().filter(itemOption -> itemOption.optionTemplate.id == 21).findFirst().get());
                item.itemOptions.add(new ItemOption(21, 15));
                item.itemOptions.add(new ItemOption(30, 1));
            } else {
                item = ItemService.gI().itemSKH(itemId, skhId);
            }
            InventoryService.gI().addItemBag(player, item, 0);
            InventoryService.gI().subQuantityItemsBag(player, itemTS, 1);
            itemDHD.forEach(j -> InventoryService.gI().subQuantityItemsBag(player, j, 1));
            InventoryService.gI().subQuantityItemsBag(player, thoivang, 30);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendMoney(player);
            player.combineNew.itemsCombine.clear();
            reOpenItemCombine(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
        }
    }

    public void openSKHts(Player player) {
        Item thoivang = null;
        try {
            thoivang = InventoryService.gI().findItemBagByTemp(player, 1400);
        } catch (Exception e) {
        }
        if (thoivang == null || thoivang.quantity < 50) {
            Service.getInstance().sendThongBao(player, "Không đủ " + ConstItem.NAME_XU_SERVER);
            return;
        }
        if (player.combineNew.itemsCombine.size() != 3) {
            Service.getInstance().sendThongBao(player, "Thiếu nguyên liệu");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDTS()).count() != 3) {
            Service.getInstance().sendThongBao(player, "Thiếu đồ Thiên sứ");
            return;
        }
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            if (player.inventory.gem < 1000) {
                Service.getInstance().sendThongBao(player, "Con cần thêm ngoc xanh để đổi...");
                return;
            }
            player.inventory.gem -= 1000;
            Item itemTS = player.combineNew.itemsCombine.stream().filter(Item::isDTS).findFirst().get();
            List<Item> itemDTS = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDTS()).collect(Collectors.toList());
            CombineServiceNew.gI().sendEffectOpenItem(player, itemTS.template.iconID, itemTS.template.iconID);
            short itemId;
            if (Util.isTrue(10, (int) 100)) {
                itemId = Manager.doSKHhoangkim[itemTS.template.type];
            } //            else if (Util.isTrue(10, (int) 100)) {
            //                itemId = Manager.doSKHVuongThan[itemTS.template.type];
            //            }
            else if (Util.isTrue(30, (int) 100)) {
                itemId = Manager.doSKHTs[player.gender][itemTS.template.type];
            } else {
                itemId = Manager.doSKHHd[player.gender][itemTS.template.type];
            }
            int skhId = ItemService.gI().randomSKHId(player.gender);
//            int skhIdVuongThan = ItemService.gI().randomSKHVuongThan(player.gender);
            int skhIdHoangKim = ItemService.gI().randomSKHHoangKim(player.gender);
            Item item;
            if (new Item(itemId).isThanhTon()) {
                item = Util.ratiItemThanhTon(itemId);
                item.itemOptions.add(new ItemOption(skhIdHoangKim, 1));
                item.itemOptions.add(new ItemOption(ItemService.gI().optionIdSKHHoangKim(skhIdHoangKim), 1));
                item.itemOptions.remove(item.itemOptions.stream().filter(itemOption -> itemOption.optionTemplate.id == 21).findFirst().get());
                item.itemOptions.add(new ItemOption(21, 200));
                item.itemOptions.add(new ItemOption(30, 1));
            } else if (new Item(itemId).isDHD()) {
                item = Util.ratiItemHuyDiet(itemId);
                item.itemOptions.add(new ItemOption(skhId, 1));
                item.itemOptions.add(new ItemOption(ItemService.gI().optionIdSKH(skhId), 1));
                item.itemOptions.remove(item.itemOptions.stream().filter(itemOption -> itemOption.optionTemplate.id == 21).findFirst().get());
                item.itemOptions.add(new ItemOption(21, 80));
                item.itemOptions.add(new ItemOption(30, 1));
            } else {
                item = ItemService.gI().DoThienSu(itemId, player.gender);
                item.itemOptions.add(new ItemOption(skhId, 1));
                item.itemOptions.add(new ItemOption(ItemService.gI().optionIdSKH(skhId), 1));
                item.itemOptions.remove(item.itemOptions.stream().filter(itemOption -> itemOption.optionTemplate.id == 21).findFirst().get());
                item.itemOptions.add(new ItemOption(21, 120));
                item.itemOptions.add(new ItemOption(30, 1));
            }
            InventoryService.gI().addItemBag(player, item, 0);
            InventoryService.gI().subQuantityItemsBag(player, itemTS, 1);
            itemDTS.forEach(j -> InventoryService.gI().subQuantityItemsBag(player, j, 1));
            InventoryService.gI().subQuantityItemsBag(player, thoivang, 50);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendMoney(player);
            player.combineNew.itemsCombine.clear();
            reOpenItemCombine(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
        }
    }

    public void NcapDoThanLinh(Player player) {
        // 1 thiên sứ + 2 món kích hoạt -- món đầu kh làm gốc
        if (player.combineNew.itemsCombine.size() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu nguyên liệu");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDTL()).count() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu đồ Thần linh");
            return;
        }
        Item doThanLinh = player.combineNew.itemsCombine.get(0);
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            if (player.inventory.gold < 2_000_000_000) {
                Service.getInstance().sendThongBao(player, "Con cần thêm vàng để đổi...");
                return;
            }
            player.inventory.gold -= 2_000_000_000;
            CombineServiceNew.gI().sendEffectOpenItem(player, doThanLinh.template.iconID, doThanLinh.template.iconID);
            Item item = Util.ratiItemHuyDiet(Manager.doHuyDiet[doThanLinh.template.gender][doThanLinh.template.type]);
            item.itemOptions.add(new ItemOption(30, 1));
            InventoryService.gI().addItemBag(player, item, 0);
            InventoryService.gI().subQuantityItemsBag(player, doThanLinh, 1);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendMoney(player);
            player.combineNew.itemsCombine.clear();
            reOpenItemCombine(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
        }
    }

    public void randomskh(Player player) {
        // 1 thiên sứ + 2 món kích hoạt -- món đầu kh làm gốc
        if (player.combineNew.itemsCombine.size() != 3) {
            Service.getInstance().sendThongBao(player, "Thiếu nguyên liệu");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDTL()).count() != 3) {
            Service.getInstance().sendThongBao(player, "Thiếu đồ Thần linh");
            return;
        }
        Item montldau = player.combineNew.itemsCombine.get(0);
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            if (player.inventory.gold < 1) {
                Service.getInstance().sendThongBao(player, "Con cần thêm vàng để đổi...");
                return;
            }
            if (player.inventory.gold < 1) {
                Service.getInstance().sendThongBao(player, "Con cần thêm vàng để đổi...");
                return;
            }
            player.inventory.gold -= COST;
            List<Item> itemDTL = player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDTL()).collect(Collectors.toList());
            CombineServiceNew.gI().sendEffectOpenItem(player, montldau.template.iconID, montldau.template.iconID);
            short itemId;
            if (player.gender == 3 || montldau.template.type == 4) {
                itemId = Manager.radaSKHThuong[0];
            } else {
                itemId = Manager.doSKHThuong[player.gender][montldau.template.type];
            }
            int skhId = ItemService.gI().randomSKHId(player.gender);
            Item item = ItemService.gI().itemSKH(itemId, skhId);
            InventoryService.gI().addItemBag(player, item, 0);
            itemDTL.forEach(i -> InventoryService.gI().subQuantityItemsBag(player, i, 1));
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendMoney(player);
            player.combineNew.itemsCombine.clear();
            reOpenItemCombine(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
        }
    }

    private void GiaHanTrangBi(Player player) {
        if (player.combineNew.itemsCombine.size() != 2) {
            Service.getInstance().sendThongBao(player, "Thiếu nguyên liệu");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isTrangBiHSD()).count() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu trang bị HSD");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 1346).count() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu Bùa Gia Hạn");
            return;
        }
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            Item thegh = player.combineNew.itemsCombine.stream().filter(item -> item.template.id == 1346).findFirst().get();
            Item tbiHSD = player.combineNew.itemsCombine.stream().filter(Item::isTrangBiHSD).findFirst().get();
            if (thegh == null) {
                Service.getInstance().sendThongBao(player, "Thiếu Bùa Gia Hạn");
                return;
            }
            if (tbiHSD == null) {
                Service.getInstance().sendThongBao(player, "Thiếu trang bị HSD");
                return;
            }
            if (tbiHSD != null) {
                for (ItemOption itopt : tbiHSD.itemOptions) {
                    if (itopt.optionTemplate.id == 93) {
                        if (itopt.param < 0 || itopt == null) {
                            Service.getInstance().sendThongBao(player, "Không Phải Trang Bị Có HSD");
                            return;
                        }
                    }
                }
            }
            if (Util.isTrue(100, 100)) {
                sendEffectSuccessCombine(player);
                for (ItemOption itopt : tbiHSD.itemOptions) {
                    if (itopt.optionTemplate.id == 93) {
                        itopt.param += 1;
                        break;
                    }
                }
            } else {
                sendEffectFailCombine(player);
            }
            InventoryService.gI().subQuantityItemsBag(player, thegh, 1);
            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendMoney(player);
            reOpenItemCombine(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
        }
    }

    private void epSaoTrangBi(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            int gem = player.combineNew.gemCombine;
            if (player.inventory.gem < gem) {
                Service.getInstance().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item trangBi = null;
            Item daPhaLe = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (isTrangBiPhaLeHoa(item)) {
                    trangBi = item;
                } else if (isDaPhaLe(item)) {
                    daPhaLe = item;
                }
            }
            int star = 0; //sao pha lê đã ép
            int starEmpty = 0; //lỗ sao pha lê
            if (trangBi != null && daPhaLe != null) {
                ItemOption optionStar = null;
                for (ItemOption io : trangBi.itemOptions) {
                    if (io.optionTemplate.id == 102) {
                        star = io.param;
                        optionStar = io;
                    } else if (io.optionTemplate.id == 107) {
                        starEmpty = io.param;
                    }
                }
                if (star < starEmpty) {
                    player.inventory.gem -= gem;
                    int optionId = getOptionDaPhaLe(daPhaLe);
                    int param = getParamDaPhaLe(daPhaLe);
                    ItemOption option = null;
                    for (ItemOption io : trangBi.itemOptions) {
                        if (io.optionTemplate.id == optionId) {
                            option = io;
                            break;
                        }
                    }
                    if (option != null) {
                        option.param += param;
                    } else {
                        trangBi.itemOptions.add(new ItemOption(optionId, param));
                    }
                    appendSplHistory(trangBi, optionId, param);
                    if (optionStar != null) {
                        optionStar.param++;
                    } else {
                        trangBi.itemOptions.add(new ItemOption(102, 1));
                    }

                    InventoryService.gI().subQuantityItemsBag(player, daPhaLe, 1);
                    sendEffectSuccessCombine(player);
                }
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }
//------------------------------------------------------------------------------------------------------------------------

    public Item createItemAfterRemoveSpl(Item trangBi) {
        int skhId = ItemService.gI().getSKHidFromItem(trangBi);
        Item itemMoi;

        if (new Item(trangBi.template.id).isDTL()
                && trangBi.itemOptions.stream().noneMatch(io -> io.optionTemplate.id == 197)) {
            itemMoi = Util.ratiItemTL(trangBi.template.id);
            if (skhId != -1) {
                itemMoi.itemOptions.add(0, new ItemOption(skhId, 1));
                itemMoi.itemOptions.add(1, new ItemOption(ItemService.gI().optionIdSKH(skhId), 1));
            }

        } else if (new Item(trangBi.template.id).isDHD()
                && trangBi.itemOptions.stream().noneMatch(io -> io.optionTemplate.id == 197)) {
            itemMoi = Util.ratiItemHuyDiet(trangBi.template.id);
            if (skhId != -1) {
                itemMoi.itemOptions.add(0, new ItemOption(skhId, 1));
                itemMoi.itemOptions.add(1, new ItemOption(ItemService.gI().optionIdSKH(skhId), 1));
            }

        } else if (new Item(trangBi.template.id).isDTS()
                && trangBi.itemOptions.stream().noneMatch(io -> io.optionTemplate.id == 197)) {
            itemMoi = Util.ratiItemThienSu(trangBi.template.id);
            if (skhId != -1) {
                itemMoi.itemOptions.add(0, new ItemOption(skhId, 1));
                itemMoi.itemOptions.add(1, new ItemOption(ItemService.gI().optionIdSKH(skhId), 1));
            }

        } else if (new Item(trangBi.template.id).isThanhTon()
                && trangBi.itemOptions.stream().noneMatch(io -> io.optionTemplate.id == 197)) {
            int[] skhInfo = ItemService.gI().getSKHHoangKimFromItem(trangBi);
            itemMoi = Util.ratiItemThanhTon(trangBi.template.id);
            if (skhInfo[0] != -1) {
                itemMoi.itemOptions.add(0, new ItemOption(skhInfo[0], 1));
            }
            if (skhInfo[1] != -1) {
                itemMoi.itemOptions.add(1, new ItemOption(skhInfo[1], 1));
            }

        } else if (Final_Static.ITEM_ID_DE_TU.contains((int) trangBi.template.id)
                && trangBi.itemOptions.stream().anyMatch(io -> io.optionTemplate.id == 197)) {
            itemMoi = ItemService.gI().getCleanItemFromShopDeTu(trangBi.template.id);

        } else if (Final_Static.ITEM_ID_CAI_TRANG_SHOP_CHO_GO_SPL.contains((int) trangBi.template.id)
                && trangBi.template.type == 5) {
            itemMoi = ItemService.gI().getCleanItemFromShopCaiTrang(trangBi.template.id);

        } else if (Final_Static.ITEM_ID_CAI_TRANG_PHUC_LOI_CHO_GO_SPL.contains((int) trangBi.template.id)
                && trangBi.template.type == 5) {
            itemMoi = PhucLoi.gI().getItemFromPhucLoi(trangBi.template.id);

        } else {
            // Đồ không hợp lệ -> trả null
            return null;
        }

        //add option 30 nếu trangBi có và itemMoi chưa có
        if (trangBi.itemOptions.stream().anyMatch(io -> io.optionTemplate.id == 30)
                && itemMoi.itemOptions.stream().noneMatch(io -> io.optionTemplate.id == 30)) {
            itemMoi.itemOptions.add(new ItemOption(30, 1));
        }

        return itemMoi;
    }

    private void goSaoPhaLeTrangBi(Player player) {
        if (player.combineNew.itemsCombine.size() != 1) {
            Service.getInstance().sendThongBao(player, "Chỉ cần 1 trang bị đã ép sao pha lê");
            return;
        }
        if (player.inventory.gold < COST_GO_SAO_PHA_LE_TRANG_BI) {
            Service.getInstance().sendThongBao(player, "Không đủ vàng để tách sao pha lê");
            return;
        }

        Item itemCu = player.combineNew.itemsCombine.get(0);
        if (itemCu == null || !itemCu.isNotNullItem() || !hasSaoPhaLeDaEp(itemCu)) {
            Service.getInstance().sendThongBao(player, "Trang bị này chưa có sao pha lê đã khảm");
            return;
        }

        if (!hasSplHistory(itemCu)) {
            Service.getInstance().sendThongBao(player, "Trang bị cũ này chưa có dữ liệu lịch sử pha lê để tách chính xác");
            return;
        }

        Item itemMoi = rollbackSplHistory(itemCu);
        if (itemMoi == null) {
            Service.getInstance().sendThongBao(player, "Không thể hoàn tác lịch sử pha lê");
            return;
        }

        byte indexBag = InventoryService.gI().getIndexBag(player, itemCu);
        int indexBody = -1;

        if (indexBag < 0) {
            for (int i = 0; i < player.inventory.itemsBody.size(); i++) {
                Item it = player.inventory.itemsBody.get(i);
                if (it == itemCu) {
                    indexBody = i;
                    break;
                }
            }
        }

        if (indexBag < 0 && indexBody < 0) {
            Service.getInstance().sendThongBao(player, "Không tìm thấy vật phẩm trong hành trang hoặc trang bị");
            return;
        }

        Item itemTruoc = ItemService.gI().copyItem(itemCu);
        Item itemSau = ItemService.gI().copyItem(itemMoi);

        player.inventory.gold -= COST_GO_SAO_PHA_LE_TRANG_BI;

        if (indexBag >= 0) {
            player.inventory.itemsBag.set(indexBag, itemMoi);
            InventoryService.gI().sendItemBags(player);
        } else {
            player.inventory.itemsBody.set(indexBody, itemMoi);
            InventoryService.gI().sendItemBody(player);
            Service.getInstance().point(player);
        }

        player.combineNew.itemsCombine.clear();

        sendEffectSuccessCombine(player);
        FileUtils.writeGoSaoTrangBiLog(player, itemTruoc, itemSau);
        Service.getInstance().sendMoney(player);
        Service.getInstance().sendThongBao(player, "Đã tách bỏ tinh thể đã khảm khỏi " + itemTruoc.template.name);
        reOpenItemCombine(player);
    }

//------------------------------------------------------------------------------------------------------------------------
    private void phaLeHoaTrangBi(Player player) {
        if (!player.combineNew.itemsCombine.isEmpty()) {
            int gold = player.combineNew.goldCombine;
            int gem = player.combineNew.gemCombine;
            Item item = player.combineNew.itemsCombine.get(0);
            Item cloudsky = null;
            for (int j = 0; j < player.combineNew.itemsCombine.size(); j++) {
                if (player.combineNew.itemsCombine.get(j).isNotNullItem()) {
                    if (player.combineNew.itemsCombine.get(j).template.id == ConstItem.XU_DAC_BIET) {
                        cloudsky = player.combineNew.itemsCombine.get(j);
                    }
                }
            }
            int star = 0;
            ItemOption optionStar = null;
            for (ItemOption io : item.itemOptions) {
                if (io.optionTemplate.id == 107) {
                    star = io.param;
                    optionStar = io;
                    break;
                }
            }
            if (player.inventory.gold < gold) {
                Service.getInstance().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            if (player.inventory.gem < gem) {
                Service.getInstance().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            if (isTrangBiPhaLeHoa(item)) {
                if (star < MAX_STAR_ITEM) {
                    player.inventory.gold -= gold;
                    player.inventory.gem -= gem;
                    byte ratio = (optionStar != null && optionStar.param > 4) ? (byte) 2 : 1;
                    if (Util.isTrue(player.combineNew.ratioCombine, 100 * ratio)) {
                        if (optionStar == null) {
                            item.itemOptions.add(new ItemOption(107, 1));
                        } else {
                            optionStar.param++;
                        }
                        sendEffectSuccessCombine(player);
                        if (optionStar != null && optionStar.param >= 10) {
                            ServerNotify.gI().notify("Chúc mừng " + player.name + " vừa pha lê hóa "
                                    + "thành công " + item.template.name + " lên " + optionStar.param + " sao pha lê");
                        }
                    } else {
                        sendEffectFailCombine(player);
                    }
                }
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void nhapNgocRong(Player player) {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            if (!player.combineNew.itemsCombine.isEmpty()) {
                Item item = player.combineNew.itemsCombine.get(0);
                if (item != null) {
                    int soluong = 7;
                    if (item.isNotNullItem() && (item.template.id > 14 && item.template.id <= 20) && item.quantity >= soluong) {
                        Item nr = ItemService.gI().createNewItem((short) (item.template.id - 1));
                        InventoryService.gI().addItemBag(player, nr, 0);
                        InventoryService.gI().subQuantityItemsBag(player, item, soluong);
                        InventoryService.gI().sendItemBags(player);
                        reOpenItemCombine(player);
                        sendEffectCombineDB(player, item.template.iconID);
                    }
                }
            }
        }
    }

    private void antrangbi(Player player) {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            if (!player.combineNew.itemsCombine.isEmpty()) {
                Item item = player.combineNew.itemsCombine.get(0);
                Item dangusac = player.combineNew.itemsCombine.get(1);
                int star = 0;
                int soluong = 99;
                if (item.isNotNullItem() && dangusac.isNotNullItem() && (dangusac.template.id == 1232 || dangusac.template.id == 1233 || dangusac.template.id == 1234)) {
                    if (item.isThanhTon()) {
                        soluong = 299;
                    } else if (item.isThienSuDT()) {
                        soluong = 999;
                    } else if (item.isDTS()) {
                        soluong = 99;
                    }
                }
                ItemOption optionStar = null;
                for (ItemOption io : item.itemOptions) {
                    if (io.optionTemplate.id == 34 || io.optionTemplate.id == 35 || io.optionTemplate.id == 35) {
                        star = io.param;
                        optionStar = io;
                        break;
                    }
                }
                if (item != null && item.isNotNullItem() && dangusac != null && dangusac.isNotNullItem()
                        && (dangusac.template.id == 1232 || dangusac.template.id == 1233 || dangusac.template.id == 1234) && dangusac.quantity >= soluong) {
                    if (optionStar == null) {
                        if (dangusac.template.id == 1232) {
                            item.itemOptions.add(new ItemOption(34, 1));
                            sendEffectSuccessCombine(player);
                        } else if (dangusac.template.id == 1233) {
                            item.itemOptions.add(new ItemOption(35, 1));
                            sendEffectSuccessCombine(player);
                        } else if (dangusac.template.id == 1234) {
                            item.itemOptions.add(new ItemOption(36, 1));
                            sendEffectSuccessCombine(player);
                        }
//                    InventoryService.gI().addItemBag(player, item, 0);
                        InventoryService.gI().subQuantityItemsBag(player, dangusac, soluong);
                        InventoryService.gI().sendItemBags(player);
                        reOpenItemCombine(player);
//                    sendEffectCombineDB(player, item.template.iconID);
                    } else {
                        Service.getInstance().sendThongBao(player, "Trang bị của bạn có ấn rồi mà !!!");
                    }
                }
            }
        }
    }

    // START _ SÁCH TUYỆT KỸ
    private void giamDinhSach(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {

            Item sachTuyetKy = null;
            Item buaGiamDinh = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (issachTuyetKy(item)) {
                    sachTuyetKy = item;
                } else if (item.template.id == 1508) {
                    buaGiamDinh = item;
                }
            }
            if (sachTuyetKy != null && buaGiamDinh != null) {
                if (player.inventory.ruby < 10000) {
                    Service.getInstance().sendThongBao(player, "Chưa đủ hồng ngọc");
                    return;
                }
                if (buaGiamDinh.quantity < 5) {
                    Service.getInstance().sendThongBao(player, "Chưa đủ bùa giám định");
                    return;
                }
                Item sachTuyetKy_2 = ItemService.gI().createNewItem((short) sachTuyetKy.template.id);
                if (checkHaveOption(sachTuyetKy, 0, 241)) {
                    int tyle = new Random().nextInt(100);
                    if (tyle >= 0 && tyle <= 33) {
                        sachTuyetKy_2.itemOptions.add(new ItemOption(50, issachTuyetKy1(sachTuyetKy_2) ? new Random().nextInt(5, 20) : new Random().nextInt(10, 40)));
                    } else if (tyle > 33 && tyle <= 66) {
                        sachTuyetKy_2.itemOptions.add(new ItemOption(77, issachTuyetKy1(sachTuyetKy_2) ? new Random().nextInt(5, 20) : new Random().nextInt(10, 40)));
                    } else {
                        sachTuyetKy_2.itemOptions.add(new ItemOption(103, issachTuyetKy1(sachTuyetKy_2) ? new Random().nextInt(5, 20) : new Random().nextInt(10, 40)));
                    }
                    for (int i = 1; i < sachTuyetKy.itemOptions.size(); i++) {
                        sachTuyetKy_2.itemOptions.add(new ItemOption(sachTuyetKy.itemOptions.get(i).optionTemplate.id, sachTuyetKy.itemOptions.get(i).param));
                    }
                    player.inventory.ruby -= 10000;
                    Service.getInstance().sendMoney(player);
                    sendEffectSuccessCombine(player);
                    InventoryService.gI().addItemBag(player, sachTuyetKy_2, 1);
                    InventoryService.gI().subQuantityItemsBag(player, sachTuyetKy, 1);
                    InventoryService.gI().subQuantityItemsBag(player, buaGiamDinh, 5);
                    InventoryService.gI().sendItemBags(player);
                    reOpenItemCombine(player);
                } else {
                    Service.getInstance().sendThongBao(player, "Vui lòng tẩy sách trước khi giảm định lần nữa");
                }
            }
        }
    }

    private void nangCapSachTuyetKy(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {

            Item sachTuyetKy = null;
            Item kimBamGiay = null;

            for (Item item : player.combineNew.itemsCombine) {
                if (issachTuyetKy(item)) {
                    sachTuyetKy = item;
                } else if (item.template.id == 1507) {
                    kimBamGiay = item;
                }
            }
            Item sachTuyetKy_2 = ItemService.gI().createNewItem((short) ((short) sachTuyetKy.template.id + 1));
            if (sachTuyetKy != null && kimBamGiay != null) {
                if (kimBamGiay.quantity < 100) {
                    Service.getInstance().sendThongBao(player, "Không đủ Kìm bấm giấy mà đòi nâng cấp");
                    return;
                }
                if (checkHaveOption(sachTuyetKy, 0, 241)) {
                    Service.getInstance().sendThongBao(player, "Chưa giám định mà đòi nâng cấp");
                    return;
                }
                if (player.inventory.ruby < 50000) {
                    Service.getInstance().sendThongBao(player, "Chưa đủ hồng ngọc");
                    return;
                }
                if (Util.isTrue(30, 100)) {
                    for (int i = 0; i < sachTuyetKy.itemOptions.size(); i++) {
                        sachTuyetKy_2.itemOptions.add(new ItemOption(sachTuyetKy.itemOptions.get(i).optionTemplate.id, sachTuyetKy.itemOptions.get(i).param));
                    }
                    player.inventory.ruby -= 50000;
                    sendEffectSuccessCombine(player);
                    InventoryService.gI().addItemBag(player, sachTuyetKy_2, 1);
                    InventoryService.gI().subQuantityItemsBag(player, sachTuyetKy, 1);
                    InventoryService.gI().subQuantityItemsBag(player, kimBamGiay, 100);
                } else {
                    player.inventory.ruby -= 50000;
                    Service.getInstance().sendMoney(player);
                    InventoryService.gI().subQuantityItemsBag(player, kimBamGiay, 100);
                    sendEffectFailCombine(player);
                }
                Service.getInstance().sendMoney(player);
                InventoryService.gI().sendItemBags(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void phucHoiSach(Player player) {
        if (player.combineNew.itemsCombine.size() == 1) {
            Item cuonSachCu = InventoryService.gI().findItemBagByTemp(player, (short) 1509);
            int goldPhanra = 10_000_000;
            Item sachTuyetKy = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (issachTuyetKy(item)) {
                    sachTuyetKy = item;
                }
            }
            if (sachTuyetKy != null) {
                int doBen = 0;
                ItemOption optionLevel = null;
                for (ItemOption io : sachTuyetKy.itemOptions) {
                    if (io.optionTemplate.id == 243) {
                        doBen = io.param;
                        optionLevel = io;
                        break;
                    }
                }
                if (cuonSachCu == null) {
                    Service.getInstance().sendThongBaoOK(player, "Cần sách tuyệt kỹ và 10 cuốn sách cũ");
                    return;
                }
                if (cuonSachCu.quantity < 10) {
                    Service.getInstance().sendThongBaoOK(player, "Cần sách tuyệt kỹ và 10 cuốn sách cũ");
                    return;
                }
                if (player.inventory.gold < goldPhanra) {
                    Service.getInstance().sendThongBao(player, "Không có tiền mà đòi phục hồi à");
                    return;
                }
                if (doBen != 1000) {
                    for (int i = 0; i < sachTuyetKy.itemOptions.size(); i++) {
                        if (sachTuyetKy.itemOptions.get(i).optionTemplate.id == 243) {
                            sachTuyetKy.itemOptions.get(i).param = 1000;
                            break;
                        }
                    }
                    player.inventory.gold -= 10_000_000;
                    InventoryService.gI().subQuantityItemsBag(player, cuonSachCu, 10);
                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendMoney(player);
                    sendEffectSuccessCombine(player);
                    reOpenItemCombine(player);
                } else {
                    Service.getInstance().sendThongBao(player, "Còn dùng được nên không thể phục hồi");
                    return;
                }
            }
        }
    }

    private void PhanRaVatPham(Player player) {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            if (player.combineNew.itemsCombine.size() == 1) {
                if (player.inventory.ruby < 20000) {
                    Service.getInstance().sendThongBao(player, "Bạn không đủ Hồng ngọc");
                    return;
                }
                Item item = player.combineNew.itemsCombine.get(0);
                int soluong = 1;
//                if (item.isDTL()) {
//                    soluong = Util.nextInt(1, 30);
//                } else if (item.isDHD()) {
//                    soluong = Util.nextInt(1, 50);
//                }
                int[] idVatpham = {457, 1232, 1233, 1234, 1400, ConstItem.XU_DAC_BIET, 1549, 933, 934, 861};
                player.inventory.ruby -= 20000;
                sendEffectSuccessCombine(player);
                Item thoivang = ItemService.gI().createNewItem((short) idVatpham[Util.nextInt(0, idVatpham.length - 1)], soluong);
                this.baHatMit.npcChat(player, "Con đã nhận được " + soluong + " " + thoivang.template.name);
                InventoryService.gI().subQuantityItemsBag(player, item, 1);
                player.combineNew.itemsCombine.clear();
                InventoryService.gI().addItemBag(player, thoivang, 999);
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);
            } else {
                Service.getInstance().sendThongBao(player, "Vui lòng bỏ 1 món Thần linh hoặc hủy diệt vào!!!");
            }
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
        }
    }

    private void phanRaSach(Player player) {
        if (player.combineNew.itemsCombine.size() == 1) {
            Item cuonSachCu = ItemService.gI().createNewItem((short) 1509, 5);
            int goldPhanra = 10_000_000;
            Item sachTuyetKy = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (issachTuyetKy(item)) {
                    sachTuyetKy = item;
                }
            }
            if (sachTuyetKy != null) {
                int luotTay = 0;
                ItemOption optionLevel = null;
                for (ItemOption io : sachTuyetKy.itemOptions) {
                    if (io.optionTemplate.id == 242) {
                        luotTay = io.param;
                        optionLevel = io;
                        break;
                    }
                }
                if (player.inventory.gold < goldPhanra) {
                    Service.getInstance().sendThongBao(player, "Không có tiền mà đòi phân rã à");
                    return;
                }
                if (luotTay == 0) {

                    player.inventory.gold -= goldPhanra;
                    InventoryService.gI().subQuantityItemsBag(player, sachTuyetKy, 1);
                    InventoryService.gI().addItemBag(player, cuonSachCu, 999);
                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendMoney(player);
                    sendEffectSuccessCombine(player);
                    reOpenItemCombine(player);

                } else {
                    Service.getInstance().sendThongBao(player, "Còn dùng được phân rã ăn cứt à");
                    return;
                }
            }
        }
    }

    private void taySach(Player player) {
        if (player.combineNew.itemsCombine.size() == 1) {
            Item sachTuyetKy = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (issachTuyetKy(item)) {
                    sachTuyetKy = item;
                }
            }
            if (sachTuyetKy != null) {
                int luotTay = 0;
                ItemOption optionLevel = null;
                for (ItemOption io : sachTuyetKy.itemOptions) {
                    if (io.optionTemplate.id == 242) {
                        luotTay = io.param;
                        optionLevel = io;
                        break;
                    }
                }
                if (luotTay == 0) {
                    Service.getInstance().sendThongBao(player, "Còn cái nịt mà tẩy");
                    return;
                }
                Item sachTuyetKy_2 = ItemService.gI().createNewItem((short) sachTuyetKy.template.id);
                if (checkHaveOption(sachTuyetKy, 0, 241)) {
                    Service.getInstance().sendThongBao(player, "Còn cái nịt mà tẩy");
                    return;
                }
                int tyle = new Random().nextInt(10);
                for (int i = 1; i < sachTuyetKy.itemOptions.size(); i++) {
                    if (sachTuyetKy.itemOptions.get(i).optionTemplate.id == 242) {
                        sachTuyetKy.itemOptions.get(i).param -= 1;
                    }
                }
                sachTuyetKy_2.itemOptions.add(new ItemOption(241, 0));
                for (int i = 1; i < sachTuyetKy.itemOptions.size(); i++) {
                    sachTuyetKy_2.itemOptions.add(new ItemOption(sachTuyetKy.itemOptions.get(i).optionTemplate.id, sachTuyetKy.itemOptions.get(i).param));
                }
                sendEffectSuccessCombine(player);
                InventoryService.gI().addItemBag(player, sachTuyetKy_2, 1);
                InventoryService.gI().subQuantityItemsBag(player, sachTuyetKy, 1);
                InventoryService.gI().sendItemBags(player);
                reOpenItemCombine(player);
            }
        }
    }

    private boolean checkHaveOption(Item item, int viTriOption, int idOption) {
        if (item != null && item.isNotNullItem()) {
            if (item.itemOptions.get(viTriOption).optionTemplate.id == idOption) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    // END _ SÁCH TUYỆT KỸ
    //    private void phanradothanlinh(Player player) {
//        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
//            if (!player.combineNew.itemsCombine.isEmpty()) {
//                Item item = player.combineNew.itemsCombine.get(0);
//                if (item != null && item.isNotNullItem() && (item.template.id > 0 && item.template.id <= 3) && item.quantity >= 1) {
//                    Item nr = ItemService.gI().createNewItem((short) (item.template.id - 78));
//                    InventoryService.gI().addItemBag(player, nr, 0);
//                    InventoryService.gI().subQuantityItemsBag(player, item, 1);
//                    InventoryService.gI().sendItemBags(player);
//                    reOpenItemCombine(player);
//                    sendEffectCombineDB(player, item.template.iconID);
//                    Service.getInstance().sendThongBao(player, "Đã nhận được 1 điểm");
//
//                }
//            }
//        }
//    }
    private void moChiSoBongTai2345(Player player) {
        if (player.combineNew.itemsCombine.size() == 3) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.getInstance().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }
            int ruby = player.combineNew.gemCombine;
            if (player.inventory.ruby < ruby) {
                Service.getInstance().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item BongTai = null;
            Item ManhHon = null;
            Item DaXanhLam = null;
            for (Item item : player.combineNew.itemsCombine) {
                switch (item.template.id) {
                    case 1944://c7
                    case 1945://c8
                    case 1946://c9
                    case 1999:
                    case 1550:
                    case 1129:
                    case 1165:
                    case 921:
                        BongTai = item;
                        break;
                    case 934:
                        ManhHon = item;
                        break;
                    case 935:
                        DaXanhLam = item;
                        break;
                    default:
                        break;
                }
            }
            if (BongTai != null && ManhHon != null && DaXanhLam != null && DaXanhLam.quantity >= 1 && ManhHon.quantity >= 99) {
                player.inventory.gold -= gold;
                player.inventory.ruby -= ruby;
                InventoryService.gI().subQuantityItemsBag(player, ManhHon, 99);
                InventoryService.gI().subQuantityItemsBag(player, DaXanhLam, 1);
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    BongTai.itemOptions.clear();
                    BongTai.itemOptions.add(new ItemOption(248, 1));
                    BongTai.itemOptions.add(new ItemOption(72, 2));
                    int rdUp = Util.nextInt(0, 7);
                    int IdBongTai = BongTai.template.id;
                    switch (rdUp) {
                        case 0:
                            BongTai.itemOptions.add(new ItemOption(50, (IdBongTai == 921 ? Util.nextInt(1, 10)//cap 2
                                    : IdBongTai == 1165 ? Util.nextInt(1, 15)//cap 3
                                            : IdBongTai == 1129 ? Util.nextInt(1, 20)//cap 4
                                                    : IdBongTai == 1550 ? Util.nextInt(1, 35)//cap 5
                                                            : IdBongTai == 1944 ? Util.nextInt(1, 70)//cap 7
                                                                    : IdBongTai == 1945 ? Util.nextInt(1, 100)//cap 8
                                                                            : IdBongTai == 1946 ? Util.nextInt(1, 150) //cap 9
                                                                                    : Util.nextInt(1, 50))));//cap 6
                            break;
                        case 1:
                            BongTai.itemOptions.add(new ItemOption(77, (IdBongTai == 921 ? Util.nextInt(1, 10)//cap 2
                                    : IdBongTai == 1165 ? Util.nextInt(1, 15)//cap 3
                                            : IdBongTai == 1129 ? Util.nextInt(1, 20)//cap 4
                                                    : IdBongTai == 1550 ? Util.nextInt(1, 35)//cap 5
                                                            : IdBongTai == 1944 ? Util.nextInt(1, 70)//cap 7
                                                                    : IdBongTai == 1945 ? Util.nextInt(1, 100)//cap 8
                                                                            : IdBongTai == 1946 ? Util.nextInt(1, 150) //cap 9
                                                                                    : Util.nextInt(1, 50))));//cap 6
                            break;
                        case 2:
                            BongTai.itemOptions.add(new ItemOption(103, (IdBongTai == 921 ? Util.nextInt(1, 10)//cap 2
                                    : IdBongTai == 1165 ? Util.nextInt(1, 15)//cap 3
                                            : IdBongTai == 1129 ? Util.nextInt(1, 20)//cap 4
                                                    : IdBongTai == 1550 ? Util.nextInt(1, 35)//cap 5
                                                            : IdBongTai == 1944 ? Util.nextInt(1, 70)//cap 7
                                                                    : IdBongTai == 1945 ? Util.nextInt(1, 100)//cap 8
                                                                            : IdBongTai == 1946 ? Util.nextInt(1, 150) //cap 9
                                                                                    : Util.nextInt(1, 50))));//cap 6
                            break;
                        case 3:
                            BongTai.itemOptions.add(new ItemOption(108, (IdBongTai == 921 ? Util.nextInt(1, 10)//cap 2
                                    : IdBongTai == 1165 ? Util.nextInt(1, 15)//cap 3
                                            : IdBongTai == 1129 ? Util.nextInt(1, 20)//cap 4
                                                    : IdBongTai == 1550 ? Util.nextInt(1, 35)//cap 5
                                                            : IdBongTai == 1944 ? Util.nextInt(1, 70)//cap 7
                                                                    : IdBongTai == 1945 ? Util.nextInt(1, 100)//cap 8
                                                                            : IdBongTai == 1946 ? Util.nextInt(1, 150) //cap 9
                                                                                    : Util.nextInt(1, 50))));//cap 6
                            break;
                        case 4:
                            BongTai.itemOptions.add(new ItemOption(94, (IdBongTai == 921 ? Util.nextInt(1, 10)//cap 2
                                    : IdBongTai == 1165 ? Util.nextInt(1, 15)//cap 3
                                            : IdBongTai == 1129 ? Util.nextInt(1, 20)//cap 4
                                                    : IdBongTai == 1550 ? Util.nextInt(1, 35)//cap 5
                                                            : IdBongTai == 1944 ? Util.nextInt(1, 70)//cap 7
                                                                    : IdBongTai == 1945 ? Util.nextInt(1, 100)//cap 8
                                                                            : IdBongTai == 1946 ? Util.nextInt(1, 150) //cap 9
                                                                                    : Util.nextInt(1, 50))));//cap 6
                            break;
                        case 5:
                            BongTai.itemOptions.add(new ItemOption(14, (IdBongTai == 921 ? Util.nextInt(1, 10)//cap 2
                                    : IdBongTai == 1165 ? Util.nextInt(1, 15)//cap 3
                                            : IdBongTai == 1129 ? Util.nextInt(1, 20)//cap 4
                                                    : IdBongTai == 1550 ? Util.nextInt(1, 35)//cap 5
                                                            : IdBongTai == 1944 ? Util.nextInt(1, 70)//cap 7
                                                                    : IdBongTai == 1945 ? Util.nextInt(1, 100)//cap 8
                                                                            : IdBongTai == 1946 ? Util.nextInt(1, 150) //cap 9
                                                                                    : Util.nextInt(1, 50))));//cap 6
                            break;
                        case 6:
                            BongTai.itemOptions.add(new ItemOption(80, (IdBongTai == 921 ? Util.nextInt(1, 10)//cap 2
                                    : IdBongTai == 1165 ? Util.nextInt(1, 15)//cap 3
                                            : IdBongTai == 1129 ? Util.nextInt(1, 20)//cap 4
                                                    : IdBongTai == 1550 ? Util.nextInt(1, 35)//cap 5
                                                            : IdBongTai == 1944 ? Util.nextInt(1, 70)//cap 7
                                                                    : IdBongTai == 1945 ? Util.nextInt(1, 100)//cap 8
                                                                            : IdBongTai == 1946 ? Util.nextInt(1, 150) //cap 9
                                                                                    : Util.nextInt(1, 50))));//cap 6
                            break;
                        case 7:
                            BongTai.itemOptions.add(new ItemOption(81, (IdBongTai == 921 ? Util.nextInt(1, 10)//cap 2
                                    : IdBongTai == 1165 ? Util.nextInt(1, 15)//cap 3
                                            : IdBongTai == 1129 ? Util.nextInt(1, 20)//cap 4
                                                    : IdBongTai == 1550 ? Util.nextInt(1, 35)//cap 5
                                                            : IdBongTai == 1944 ? Util.nextInt(1, 70)//cap 7
                                                                    : IdBongTai == 1945 ? Util.nextInt(1, 100)//cap 8
                                                                            : IdBongTai == 1946 ? Util.nextInt(1, 150) //cap 9
                                                                                    : Util.nextInt(1, 50))));//cap 6
                            break;
                        default:
                            break;
                    }
                    sendEffectSuccessCombine(player);
                } else {
                    sendEffectFailCombine(player);
                }
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void DotPhaBongTai(Player player) {
        if (player.combineNew.itemsCombine.size() == 3) {
            int ruby = player.combineNew.gemCombine;
            if (player.inventory.ruby < ruby) {
                Service.getInstance().sendThongBao(player, "Không đủ ngọc để thực hiện");
                return;
            }
            Item BongTai = null;
            Item ThoiVang = null;
            Item DaXanhLam = null;
            for (Item item : player.combineNew.itemsCombine) {
                switch (item.template.id) {
                    case 1550:
                        BongTai = item;
                        break;
                    case 1129:
                        BongTai = item;
                        break;
                    case 1165:
                        BongTai = item;
                        break;
                    case 921:
                        BongTai = item;
                        break;
                    case 457:
                        ThoiVang = item;
                        break;
                    case 935:
                        DaXanhLam = item;
                        break;
                    default:
                        break;
                }
            }
            if (BongTai != null && ThoiVang != null && DaXanhLam != null && DaXanhLam.quantity >= 1 && ThoiVang.quantity >= 10) {
                player.inventory.ruby -= ruby;
                InventoryService.gI().subQuantityItemsBag(player, ThoiVang, 10);
                InventoryService.gI().subQuantityItemsBag(player, DaXanhLam, 1);
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    BongTai.itemOptions.add(new ItemOption(248, 1));
                    sendEffectSuccessCombine(player);
                } else {
                    sendEffectFailCombine(player);
                }
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void nangCapBongTai(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.getInstance().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }

            int gem = player.combineNew.gemCombine;
            if (player.inventory.ruby < gem) {
                Service.getInstance().sendThongBao(player, "Không đủ Hồng ngọc để thực hiện");
                return;
            }

            Item bongTai = null;
            Item manhVo = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id == 454) {
                    bongTai = item;
                } else if (item.template.id == 933) {
                    manhVo = item;
                } else if (item.template.id == 921) {
                    bongTai = item;
                } else if (item.template.id == 1165) {
                    bongTai = item;
                } else if (item.template.id == 1129) {
                    bongTai = item;
                }
            }

            if (bongTai != null && manhVo != null && manhVo.quantity >= 99 && bongTai.template.id == 454) {
                Item findItemBag = InventoryService.gI().findItemBagByTemp(player, 921); //Khóa btc2
                if (findItemBag != null) {
                    Service.getInstance().sendThongBao(player, "Ngươi đã có bông tai Porata cấp 2 trong hàng trang rồi, không thể nâng cấp nữa.");
                    return;
                }
                player.inventory.gold -= gold;
                player.inventory.ruby -= gem;
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    InventoryService.gI().subQuantityItemsBag(player, manhVo, 99);
                    bongTai.template = ItemService.gI().getTemplate(921);
                    sendEffectSuccessCombine(player);
                } else {
                    InventoryService.gI().subQuantityItemsBag(player, manhVo, 99);
                    sendEffectFailCombine(player);
                }
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);
            } else if (bongTai != null && manhVo != null && manhVo.quantity >= 99 && bongTai.template.id == 921) {
                Item findItemBag = InventoryService.gI().findItemBagByTemp(player, 1165); //Khóa btc2
                if (findItemBag != null) {
                    Service.getInstance().sendThongBao(player, "Ngươi đã có bông tai Porata cấp 3 trong hàng trang rồi, không thể nâng cấp nữa.");
                    return;
                }
                player.inventory.gold -= gold;
                player.inventory.ruby -= gem;
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    InventoryService.gI().subQuantityItemsBag(player, manhVo, 99);
                    bongTai.template = ItemService.gI().getTemplate(1165);
                    sendEffectSuccessCombine(player);
                } else {
                    InventoryService.gI().subQuantityItemsBag(player, manhVo, 99);
                    sendEffectFailCombine(player);
                }
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);
            } else if (bongTai != null && manhVo != null && manhVo.quantity >= 99 && bongTai.template.id == 1165) {
                Item findItemBag = InventoryService.gI().findItemBagByTemp(player, 1129); //Khóa btc2
                if (findItemBag != null) {
                    Service.getInstance().sendThongBao(player, "Ngươi đã có bông tai Porata cấp 4 trong hàng trang rồi, không thể nâng cấp nữa.");
                    return;
                }
                player.inventory.gold -= gold;
                player.inventory.ruby -= gem;
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    InventoryService.gI().subQuantityItemsBag(player, manhVo, 99);
                    bongTai.template = ItemService.gI().getTemplate(1129);
                    sendEffectSuccessCombine(player);
                } else {
                    InventoryService.gI().subQuantityItemsBag(player, manhVo, 99);
                    sendEffectFailCombine(player);
                }
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);
            } else if (bongTai != null && manhVo != null && manhVo.quantity >= 99 && bongTai.template.id == 1129) {
                Item findItemBag = InventoryService.gI().findItemBagByTemp(player, 1550);
                if (findItemBag != null) {
                    Service.getInstance().sendThongBao(player, "Ngươi đã có bông tai Porata cấp 5 trong hàng trang rồi, không thể nâng cấp nữa.");
                    return;
                }
                player.inventory.gold -= gold;
                player.inventory.ruby -= gem;
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    InventoryService.gI().subQuantityItemsBag(player, manhVo, 99);
                    bongTai.template = ItemService.gI().getTemplate(1550);
                    sendEffectSuccessCombine(player);
                } else {
                    InventoryService.gI().subQuantityItemsBag(player, manhVo, 300);
                    sendEffectFailCombine(player);
                }
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void nangCapBongTaiVoCuc(Player player) {
        if (player.combineNew.itemsCombine.size() == 3) {
            int gold = player.combineNew.goldCombine;
            if (player.inventory.gold < gold) {
                Service.getInstance().sendThongBao(player, "Không đủ vàng để thực hiện");
                return;
            }

            int gem = player.combineNew.gemCombine;
            if (player.inventory.ruby < gem) {
                Service.getInstance().sendThongBao(player, "Không đủ Hồng ngọc để thực hiện");
                return;
            }

            Item bongTai = null;
            Item manhVo = null;
            Item xuVang = null;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id == 1550) {
                    bongTai = item;
                } else if (item.template.id == 1549) {
                    manhVo = item;
                } else if (item.template.id == ConstItem.XU_SERVER) {
                    xuVang = item;
                }
            }
            if (xuVang == null || xuVang.quantity < 20) {
                Service.getInstance().sendThongBao(player, "Không đủ 20 " + ConstItem.NAME_XU_SERVER);
                return;
            }

            if (bongTai != null && manhVo != null && manhVo.quantity >= 99 && bongTai.template.id == 1550) {
                Item findItemBag = InventoryService.gI().findItemBagByTemp(player, 1999); //Khóa btc2
                if (findItemBag != null) {
                    Service.getInstance().sendThongBao(player, "Ngươi đã có bông tai Porata Vô Cực trong hàng trang rồi, không thể nâng cấp nữa.");
                    return;
                }

                //-------------Trừ--Item-------------
                if (!player.inventory.subTieuCoudSky(player, 20)) {
                    Service.getInstance().sendThongBaoOK(player, "Không đủ " + ConstItem.NAME_XU_SERVER);
                    return;
                }
                player.inventory.gold -= gold;
                player.inventory.ruby -= gem;
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    InventoryService.gI().subQuantityItemsBag(player, manhVo, 99);
                    bongTai.template = ItemService.gI().getTemplate(1999);
                    sendEffectSuccessCombine(player);
                } else {
                    InventoryService.gI().subQuantityItemsBag(player, manhVo, 99);
                    sendEffectFailCombine(player);
                }
                //------------------------------------

                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void nangCapChanMenh(Player player) {
        if (player.combineNew.itemsCombine.size() == 2) {
            int diem = player.combineNew.DiemNangcap;
            if (player.inventory.ruby < diem) {
                Service.getInstance().sendThongBao(player, "Không đủ Hồng ngọc để thực hiện");
                return;
            }
            Item chanmenh = null;
            Item dahoangkim = null;
            int capbac = 0;
            for (Item item : player.combineNew.itemsCombine) {
                if (item.template.id == 1318) {
                    dahoangkim = item;
                } else if (item.template.id >= 1300 && item.template.id < 1308) {
                    chanmenh = item;
                    capbac = item.template.id - 1299;
                }
            }
            int soluongda = player.combineNew.DaNangcap;
            if (dahoangkim != null && dahoangkim.quantity >= soluongda) {
                if (chanmenh != null && (chanmenh.template.id >= 1300 && chanmenh.template.id < 1308)) {
                    player.inventory.ruby -= diem;
                    if (Util.isTrue(player.combineNew.TileNangcap, 100)) {
                        InventoryService.gI().subQuantityItemsBag(player, dahoangkim, soluongda);
                        chanmenh.template = ItemService.gI().getTemplate(chanmenh.template.id + 1);
                        chanmenh.itemOptions.clear();
                        chanmenh.itemOptions.add(new ItemOption(50, (5 + capbac * 3)));
                        chanmenh.itemOptions.add(new ItemOption(77, (7 + capbac * 4)));
                        chanmenh.itemOptions.add(new ItemOption(103, (7 + capbac * 4)));
                        chanmenh.itemOptions.add(new ItemOption(30, 1));
                        sendEffectSuccessCombine(player);
                    } else {
                        InventoryService.gI().subQuantityItemsBag(player, dahoangkim, soluongda);
                        sendEffectFailCombine(player);
                    }
                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendMoney(player);
                    reOpenItemCombine(player);
                }
            } else {
                Service.getInstance().sendThongBao(player, "Không đủ Đá Hoàng Kim để thực hiện");
            }
        }
    }

    private void nangCapVatPham(Player player) {
        if (player.combineNew.itemsCombine.size() >= 2 && player.combineNew.itemsCombine.size() < 4) {
            if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type < 5).count() != 1) {
                return;
            }
            if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type == 14).count() != 1) {
                return;
            }
            if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 987).count() != 1) {
                return;//admin
            }
            Item itemDo = null;
            Item itemDNC = null;
            Item itemDBV = null;
            for (int j = 0; j < player.combineNew.itemsCombine.size(); j++) {
                if (player.combineNew.itemsCombine.get(j).isNotNullItem()) {
                    if (player.combineNew.itemsCombine.size() == 3 && player.combineNew.itemsCombine.get(j).template.id == 987) {
                        itemDBV = player.combineNew.itemsCombine.get(j);
                        continue;
                    }
                    if (player.combineNew.itemsCombine.get(j).template.type < 5) {
                        itemDo = player.combineNew.itemsCombine.get(j);
                    } else {
                        itemDNC = player.combineNew.itemsCombine.get(j);
                    }
                }
            }
            if (isCoupleItemNangCapCheck(itemDo, itemDNC)) {
                int countDaNangCap = player.combineNew.countDaNangCap;
                int gold = player.combineNew.goldCombine;
                short countDaBaoVe = player.combineNew.countDaBaoVe;
                if (player.inventory.gold < gold) {
                    Service.getInstance().sendThongBao(player, "Không đủ vàng để thực hiện");
                    return;
                }

                if (itemDNC.quantity < countDaNangCap) {
                    return;
                }
                if (player.combineNew.itemsCombine.size() == 3) {
                    if (Objects.isNull(itemDBV)) {
                        return;
                    }
                    if (itemDBV.quantity < countDaBaoVe) {
                        return;
                    }
                }

                int level = 0;
                ItemOption optionLevel = null;
                for (ItemOption io : itemDo.itemOptions) {
                    if (io.optionTemplate.id == 72) {
                        level = io.param;
                        optionLevel = io;
                        break;
                    }
                }
                if (level < MAX_LEVEL_ITEM) {
                    player.inventory.gold -= gold;
                    ItemOption option = null;
                    ItemOption option2 = null;
                    for (ItemOption io : itemDo.itemOptions) {
                        if (io.optionTemplate.id == 47
                                || io.optionTemplate.id == 6
                                || io.optionTemplate.id == 0
                                || io.optionTemplate.id == 7
                                || io.optionTemplate.id == 14
                                || io.optionTemplate.id == 22
                                || io.optionTemplate.id == 23) {
                            option = io;
                        } else if (io.optionTemplate.id == 27
                                || io.optionTemplate.id == 28) {
                            option2 = io;
                        }
                    }
                    if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                        option.param += (option.param * 10 / 100);
                        if (option2 != null) {
                            option2.param += (option2.param * 10 / 100);
                        }
                        if (optionLevel == null) {
                            itemDo.itemOptions.add(new ItemOption(72, 1));
                        } else {
                            optionLevel.param++;
                        }
//                        if (optionLevel != null && optionLevel.param >= 5) {
//                            ServerNotify.gI().notify("Chúc mừng " + player.name + " vừa nâng cấp "
//                                    + "thành công " + trangBi.template.name + " lên +" + optionLevel.param);
//                        }
                        sendEffectSuccessCombine(player);
                    } else {
                        if ((level == 2 || level == 4 || level == 6 || level == 7) && (player.combineNew.itemsCombine.size() != 3)) {
                            option.param -= (option.param * 15 / 100);
                            if (option2 != null) {
                                option2.param -= (option2.param * 15 / 100);
                            }
                            optionLevel.param--;
                        }
                        sendEffectFailCombine(player);
                    }
                    if (player.combineNew.itemsCombine.size() == 3) {
                        InventoryService.gI().subQuantityItemsBag(player, itemDBV, countDaBaoVe);
                    }
                    InventoryService.gI().subQuantityItemsBag(player, itemDNC, player.combineNew.countDaNangCap);
                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendMoney(player);
                    reOpenItemCombine(player);
                }
            }
        }
    }

    private void nangCapCaiTrang(Player player) {
        if (player.combineNew.itemsCombine.size() == 3) {
            if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type == 5).count() != 1) {
                Service.getInstance().sendThongBao(player, "Vui lòng bỏ Cải trang chỉ định vào !!!");
                return;
            }
            Item itemDo = null;
            Item itemDNC = null;
            Item cloudsky = null;
            for (int j = 0; j < player.combineNew.itemsCombine.size(); j++) {
                if (player.combineNew.itemsCombine.get(j).isNotNullItem()) {
                    if (player.combineNew.itemsCombine.get(j).template.type == 5) {
                        itemDo = player.combineNew.itemsCombine.get(j);
                    } else if (player.combineNew.itemsCombine.get(j).template.id == ConstItem.XU_DAC_BIET) {
                        cloudsky = player.combineNew.itemsCombine.get(j);
                    } else {
                        itemDNC = player.combineNew.itemsCombine.get(j);
                    }
                }
            }
            int countDaNangCap = player.combineNew.countDaNangCap;

            if (itemDo == null) {
                Service.getInstance().sendThongBao(player, "Vui lòng bỏ Cải trang chỉ định vào !!!");
                return;
            }
            if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDHD()).count() != 1) {
                Service.getInstance().sendThongBao(player, "Thiếu đồ hủy diệt");
                return;
            }
            int level = 0;
            ItemOption optionLevel = null;
            for (ItemOption io : itemDo.itemOptions) {
                if (io.optionTemplate.id == 72) {
                    level = io.param;
                    optionLevel = io;
                    break;
                }
            }
            if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 457).count() != 1) {
                Service.getInstance().sendThongBao(player, "Thiếu thỏi vàng rồi !!!");
                return;
            }
            if (itemDNC == null || itemDNC.quantity < countDaNangCap) {
                Service.getInstance().sendThongBao(player, "Thiếu thỏi vàng rồi !!!");
                return;
            }
            Item itemDHD = player.combineNew.itemsCombine.stream().filter(Item::isDHD).findFirst().get();
            if (level < 10) {
                ItemOption option = null;
                ItemOption option1 = null;
                ItemOption option2 = null;
                for (ItemOption io : itemDo.itemOptions) {
                    if (io.optionTemplate.id == 50) {
                        option = io;
                    }
                    if (io.optionTemplate.id == 77) {
                        option1 = io;
                    }
                    if (io.optionTemplate.id == 103) {
                        option2 = io;
                    }
                }
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    if (option == null) {
                        itemDo.itemOptions.add(new ItemOption(50, getChiSoSD(level)));
                    } else {
                        option.param += getChiSoSD(level);
                    }
                    if (option1 == null) {
                        itemDo.itemOptions.add(new ItemOption(77, getChiSoHPKI(level)));
                    } else {
                        option1.param += getChiSoHPKI(level);
                    }
                    if (option2 == null) {
                        itemDo.itemOptions.add(new ItemOption(103, getChiSoHPKI(level)));
                    } else {
                        option2.param += getChiSoHPKI(level);
                    }
                    if (optionLevel == null) {
                        itemDo.itemOptions.add(new ItemOption(72, 1));
                    } else {
                        optionLevel.param++;
                    }
//                    if (level == 2 || level == 5 || level == 8) {
//                        for (int i = 0; i < itemDo.itemOptions.size(); i++) {
//                            updateCaiTrang.itemOptions.add(new ItemOption(itemDo.itemOptions.get(i).optionTemplate.id, itemDo.itemOptions.get(i).param));
//                        }
//                        InventoryService.gI().addItemBag(player, updateCaiTrang, 1);
//                        InventoryService.gI().subQuantityItemsBag(player, itemDo, 1);
//                    }
                    sendEffectSuccessCombine(player);
                } else {
                    sendEffectFailCombine(player);
                }
                InventoryService.gI().subQuantityItemsBag(player, itemDHD, 1);
                InventoryService.gI().subQuantityItemsBag(player, itemDNC, player.combineNew.countDaNangCap);
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);
            }
//            else if (level == 9) {
//                ItemOption option = null;
//                ItemOption option1 = null;
//                ItemOption option2 = null;
//                for (ItemOption io : itemDo.itemOptions) {
//                    if (io.optionTemplate.id == 50) {
//                        option = io;
//                    }
//                    if (io.optionTemplate.id == 77) {
//                        option1 = io;
//                    }
//                    if (io.optionTemplate.id == 103) {
//                        option2 = io;
//                    }
//                }
//                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
////                    Item updateCaiTrang = ItemService.gI().createNewItem((short) ((short) getChuyenHoaCaiTrang(itemDo.template.id)));
//                    if (option == null) {
//                        itemDo.itemOptions.add(new ItemOption(50, getChiSoSD(level)));
//                    } else {
//                        option.param += getChiSoSD(level);
//                    }
//                    if (option1 == null) {
//                        itemDo.itemOptions.add(new ItemOption(77, getChiSoHPKI(level)));
//                    } else {
//                        option1.param += getChiSoHPKI(level);
//                    }
//                    if (option2 == null) {
//                        itemDo.itemOptions.add(new ItemOption(103, getChiSoHPKI(level)));
//                    } else {
//                        option2.param += getChiSoHPKI(level);
//                    }
//                    if (optionLevel == null) {
//                        itemDo.itemOptions.add(new ItemOption(72, 1));
//                    } else {
//                        optionLevel.param++;
//                    }
////                    if (level == 2 || level == 5 || level == 8) {
////                        for (int i = 0; i < itemDo.itemOptions.size(); i++) {
////                            updateCaiTrang.itemOptions.add(new ItemOption(itemDo.itemOptions.get(i).optionTemplate.id, itemDo.itemOptions.get(i).param));
////                        }
////                        InventoryService.gI().addItemBag(player, updateCaiTrang, 1);
////                        InventoryService.gI().subQuantityItemsBag(player, itemDo, 1);
////                    }
//                    sendEffectSuccessCombine(player);
//                } else {
//                    sendEffectFailCombine(player);
//                }
//                InventoryService.gI().subQuantityItemsBag(player, itemXu, player.combineNew.countDaNangCap);
//                InventoryService.gI().sendItemBags(player);
//                Service.getInstance().sendMoney(player);
//                reOpenItemCombine(player);
//            }
        }
    }

    private void nangCapPhuKien(Player player) {
        if (player.combineNew.itemsCombine.size() == 4) {
            if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && isTrangBiPhuKien(item)).count() != 1) {
                Service.getInstance().sendThongBao(player, "Vui lòng bỏ Phụ kiện vào !!!");
                return;
            }
            Item itemDo = null;
            Item itemTV = null;
            Item itemDNS = null;
            for (int j = 0; j < player.combineNew.itemsCombine.size(); j++) {
                if (player.combineNew.itemsCombine.get(j).isNotNullItem()) {
                    if (isTrangBiPhuKien(player.combineNew.itemsCombine.get(j))) {
                        itemDo = player.combineNew.itemsCombine.get(j);
                    } else if (player.combineNew.itemsCombine.get(j).template.id == 674) {
                        itemDNS = player.combineNew.itemsCombine.get(j);
                    } else if (player.combineNew.itemsCombine.get(j).template.id == 457) {
                        itemTV = player.combineNew.itemsCombine.get(j);
                    }
                }
            }
            int countDaNangCap = player.combineNew.countDaNangCap;
            int countTVNangCap = player.combineNew.goldCombine;

            if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isDTL()).count() != 1) {
                Service.getInstance().sendThongBao(player, "Thiếu đồ Thần Linh");
                return;
            }
            Item itemDHD = player.combineNew.itemsCombine.stream().filter(Item::isDTL).findFirst().get();
            if (itemDo == null) {
                Service.getInstance().sendThongBao(player, "Vui lòng bỏ Phụ Kiện vào !!!");
                return;
            }
            int level = 0;
            ItemOption optionLevel = null;
            for (ItemOption io : itemDo.itemOptions) {
                if (io.optionTemplate.id == 72) {
                    level = io.param;
                    optionLevel = io;
                    break;
                }
            }
            if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 457).count() != 1) {
                Service.getInstance().sendThongBao(player, "Thiếu thỏi vàng rồi !!!");
                return;
            }
            if (itemTV == null || itemTV.quantity < countTVNangCap) {
                Service.getInstance().sendThongBao(player, "Thiếu thỏi vàng rồi !!!");
                return;
            }
            if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 674).count() != 1) {
                Service.getInstance().sendThongBao(player, "Thiếu Đá ngũ sắc rồi !!!");
                return;
            }
            if (itemDNS == null || itemDNS.quantity < countDaNangCap) {
                Service.getInstance().sendThongBao(player, "Thiếu Đá ngũ sắc rồi !!!");
                return;
            }
            if (level < 10) {
                ItemOption option = null;
                ItemOption option1 = null;
                ItemOption option2 = null;
                for (ItemOption io : itemDo.itemOptions) {
                    if (io.optionTemplate.id == 50) {
                        option = io;
                    }
                    if (io.optionTemplate.id == 77) {
                        option1 = io;
                    }
                    if (io.optionTemplate.id == 103) {
                        option2 = io;
                    }
                }
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    if (option == null) {
                        itemDo.itemOptions.add(new ItemOption(50, getChiSoSDHPKI(level)));
                    } else {
                        option.param += getChiSoSDHPKI(level);
                    }
                    if (option1 == null) {
                        itemDo.itemOptions.add(new ItemOption(77, getChiSoSDHPKI(level)));
                    } else {
                        option1.param += getChiSoSDHPKI(level);
                    }
                    if (option2 == null) {
                        itemDo.itemOptions.add(new ItemOption(103, getChiSoSDHPKI(level)));
                    } else {
                        option2.param += getChiSoSDHPKI(level);
                    }
                    if (optionLevel == null) {
                        itemDo.itemOptions.add(new ItemOption(72, 1));
                    } else {
                        optionLevel.param++;
                    }
                    sendEffectSuccessCombine(player);
                } else {
                    sendEffectFailCombine(player);
                }
                InventoryService.gI().subQuantityItemsBag(player, itemDHD, 1);
                InventoryService.gI().subQuantityItemsBag(player, itemDNS, player.combineNew.countDaNangCap);
                InventoryService.gI().subQuantityItemsBag(player, itemTV, player.combineNew.goldCombine);
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

//    private void nangCapCaiTrang(Player player) {
//        if (player.combineNew.itemsCombine.size() == 2) {
//            if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.type == 5).count() != 1) {
//                Service.getInstance().sendThongBao(player, "Vui lòng bỏ Cải trang chỉ định vào !!!");
//                return;
//            }
//            if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.template.id == 457).count() != 1) {
//                Service.getInstance().sendThongBao(player, "Thiếu thỏi vàng rồi !!!");
//                return;
//            }
//            Item itemDo = null;
//            Item itemDNC = null;
//            for (int j = 0; j < player.combineNew.itemsCombine.size(); j++) {
//                if (player.combineNew.itemsCombine.get(j).isNotNullItem()) {
//                    if (player.combineNew.itemsCombine.get(j).template.type == 5) {
//                        itemDo = player.combineNew.itemsCombine.get(j);
//                    } else {
//                        itemDNC = player.combineNew.itemsCombine.get(j);
//                    }
//                }
//            }
//            int countDaNangCap = player.combineNew.countDaNangCap;
//            if (itemDNC == null || itemDNC.quantity < countDaNangCap) {
//                Service.getInstance().sendThongBao(player, "Thiếu thỏi vàng rồi !!!");
//                return;
//            }
//            if (itemDo == null) {
//                Service.getInstance().sendThongBao(player, "Vui lòng bỏ Cải trang chỉ định vào !!!");
//                return;
//            }
//
//            int level = 0;
//            ItemOption optionLevel = null;
//            for (ItemOption io : itemDo.itemOptions) {
//                if (io.optionTemplate.id == 72) {
//                    level = io.param;
//                    optionLevel = io;
//                    break;
//                }
//            }
//            if (level < 9) {
//                ItemOption option = null;
//                ItemOption option1 = null;
//                ItemOption option2 = null;
//                for (ItemOption io : itemDo.itemOptions) {
//                    if (io.optionTemplate.id == 50) {
//                        option = io;
//                    }
//                    if (io.optionTemplate.id == 77) {
//                        option1 = io;
//                    }
//                    if (io.optionTemplate.id == 103) {
//                        option2 = io;
//                    }
//                }
//                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
////                    Item updateCaiTrang = ItemService.gI().createNewItem((short) ((short) getChuyenHoaCaiTrang(itemDo.template.id)));
//                    if (option == null) {
//                        itemDo.itemOptions.add(new ItemOption(50, getChiSoSD(level)));
//                    } else {
//                        option.param += getChiSoSD(level);
//                    }
//                    if (option1 == null) {
//                        itemDo.itemOptions.add(new ItemOption(77, getChiSoHPKI(level)));
//                    } else {
//                        option1.param += getChiSoHPKI(level);
//                    }
//                    if (option2 == null) {
//                        itemDo.itemOptions.add(new ItemOption(103, getChiSoHPKI(level)));
//                    } else {
//                        option2.param += getChiSoHPKI(level);
//                    }
//                    if (optionLevel == null) {
//                        itemDo.itemOptions.add(new ItemOption(72, 1));
//                    } else {
//                        optionLevel.param++;
//                    }
////                    if (level == 2 || level == 5 || level == 8) {
////                        for (int i = 0; i < itemDo.itemOptions.size(); i++) {
////                            updateCaiTrang.itemOptions.add(new ItemOption(itemDo.itemOptions.get(i).optionTemplate.id, itemDo.itemOptions.get(i).param));
////                        }
////                        InventoryService.gI().addItemBag(player, updateCaiTrang, 1);
////                        InventoryService.gI().subQuantityItemsBag(player, itemDo, 1);
////                    }
//                    sendEffectSuccessCombine(player);
//                } else {
//                    sendEffectFailCombine(player);
//                }
//                InventoryService.gI().subQuantityItemsBag(player, itemDNC, player.combineNew.countDaNangCap);
//                InventoryService.gI().sendItemBags(player);
//                Service.getInstance().sendMoney(player);
//                reOpenItemCombine(player);
//            }
//        }
//    }
    private void phapsuhoa(Player player) {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            if (!player.combineNew.itemsCombine.isEmpty()) {
                Item item = player.combineNew.itemsCombine.get(0);
                Item dangusac = player.combineNew.itemsCombine.get(1);
                int star = 0;
                short[] chiso = {229, 230, 231, 232};
                byte randomDo = (byte) new Random().nextInt(chiso.length);
                int lvchiso = 0;
                int cap = 1;
                ItemOption optionStar = null;
                int check = chiso[randomDo];
                int run = 0;
                int lvcheck = 0;

                for (ItemOption io : item.itemOptions) {
                    if (io.optionTemplate.id == 229 || io.optionTemplate.id == 230 || io.optionTemplate.id == 231 || io.optionTemplate.id == 232) {
                        star = io.param;
                        optionStar = io;
                        break;
                    }
                }

                for (ItemOption io2 : item.itemOptions) {
                    if (io2.optionTemplate.id == 233) {
                        lvcheck = io2.param;
                        break;
                    }
                }

                if (item != null && item.isNotNullItem() && dangusac != null && dangusac.isNotNullItem() && (dangusac.template.id == 1235) && dangusac.quantity >= 1) {
                    if (lvcheck < 6) {
                        if (optionStar == null) {
                            item.itemOptions.add(new ItemOption(233, cap));
                            if (check == 232) {
                                item.itemOptions.add(new ItemOption(check, lvchiso + 1));
                            } else {
                                item.itemOptions.add(new ItemOption(check, lvchiso + 2));
                            }
                            sendEffectSuccessCombine(player);
                            InventoryService.gI().subQuantityItemsBag(player, dangusac, 1);
                            InventoryService.gI().sendItemBags(player);
                            reOpenItemCombine(player);
                        } else {

                            for (ItemOption ioo : item.itemOptions) {
                                if (ioo.optionTemplate.id == 233) {
                                    ioo.param++;
                                }
                                if ((ioo.optionTemplate.id == 229 || ioo.optionTemplate.id == 230 || ioo.optionTemplate.id == 231 || ioo.optionTemplate.id == 232) && (ioo.optionTemplate.id == check)) {
                                    if (check == 232) {
                                        ioo.param += 1;
                                    } else {
                                        ioo.param += 2;
                                    }
                                    sendEffectSuccessCombine(player);
                                    InventoryService.gI().subQuantityItemsBag(player, dangusac, 1);
                                    InventoryService.gI().sendItemBags(player);
                                    reOpenItemCombine(player);
                                    run = 1;
                                    break;
                                } else {
                                    run = 2;
                                }
                            }

                            if (run == 2) {
                                if (check == 232) {
                                    item.itemOptions.add(new ItemOption(check, lvchiso + 1));
                                } else {
                                    item.itemOptions.add(new ItemOption(check, lvchiso + 2));
                                }
                                sendEffectSuccessCombine(player);
                                InventoryService.gI().subQuantityItemsBag(player, dangusac, 1);
                                InventoryService.gI().sendItemBags(player);
                                reOpenItemCombine(player);
                            }
                        }
                    } else {
                        Service.getInstance().sendThongBao(player, "Pháp sư hóa đã đạt cấp cao nhất !!!");
                    }
                }
            }
        }
    }

    private void tayphapsu(Player player) {
        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            if (!player.combineNew.itemsCombine.isEmpty()) {
                Item item = player.combineNew.itemsCombine.get(0);
                Item dangusac = player.combineNew.itemsCombine.get(1);
                ItemOption optionStar = null;

                for (ItemOption io : item.itemOptions) {
                    if (io.optionTemplate.id == 229 || io.optionTemplate.id == 230 || io.optionTemplate.id == 231 || io.optionTemplate.id == 232 || io.optionTemplate.id == 233) {
                        optionStar = io;
                        break;
                    }
                }

                if (item != null && item.isNotNullItem() && dangusac != null && dangusac.isNotNullItem() && (dangusac.template.id == 1236) && dangusac.quantity >= 1) {
                    if (optionStar == null) {
                        Service.getInstance().sendThongBao(player, "Có gì đâu mà tẩy !!!");
                    } else {

                        if (item.itemOptions != null) {

                            Iterator<ItemOption> iterator = item.itemOptions.iterator();
                            while (iterator.hasNext()) {
                                ItemOption ioo = iterator.next();
                                if (ioo.optionTemplate.id == 229 || ioo.optionTemplate.id == 230 || ioo.optionTemplate.id == 231 || ioo.optionTemplate.id == 232 || ioo.optionTemplate.id == 233) {
                                    iterator.remove();
                                }
                            }

                        }
                        //item.itemOptions.add(new ItemOption(73 , 1));  
                        sendEffectSuccessCombine(player);
                        InventoryService.gI().subQuantityItemsBag(player, dangusac, 1);
                        InventoryService.gI().sendItemBags(player);
                        reOpenItemCombine(player);

                    }
                } else {
                    Service.getInstance().sendThongBao(player, "Thiếu vật phẩm gòi !!!");
                }

            }
        }
    }

    public void openSKHbill(Player player) {
        Item dahoangkim = null;
        try {
            dahoangkim = InventoryService.gI().findItemBagByTemp(player, 1791);
        } catch (Exception e) {
        }
        if (dahoangkim == null || dahoangkim.quantity < 100) {
            Service.getInstance().sendThongBao(player, "Không đủ Đá Vũ Trụ");
            return;
        }
        if (player.combineNew.itemsCombine.size() != 2) {
            Service.getInstance().sendThongBao(player, "Thiếu nguyên liệu");
            return;
        }
        if (player.combineNew.itemsCombine.stream().filter(item -> item.isNotNullItem() && item.isTrangBi()).count() != 1) {
            Service.getInstance().sendThongBao(player, "Thiếu trang bị");
            return;
        }
        Item mTS = null;
        for (Item item : player.combineNew.itemsCombine) {
            if (item.isNotNullItem()) {
                if (item.isManhTS()) {
                    mTS = item;
                }
            }
        }
        if (mTS == null) {
            this.npcthanmau.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu mảnh thiên sứ", "Đóng");
            return;
        }
        int totalMTS = player.combineNew.itemsCombine.stream()
                .filter(item -> item.isNotNullItem() && item.isManhTS())
                .mapToInt(item -> item.quantity) // Lấy số lượng của từng item
                .sum(); // Tính tổng số lượng
        if (totalMTS < 10_000) {
            this.npcthanmau.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Không đủ 10.000 mảnh thiên sứ", "Đóng");
            return;
        }

        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
            Item Trangbi = player.combineNew.itemsCombine.stream().filter(Item::isTrangBi).findFirst().get();
            CombineServiceNew.gI().sendEffectOpenItem(player, Trangbi.template.iconID, Trangbi.template.iconID);
            if (Util.isTrue(80, 100)) {
                int[] optionIds = {214, 215, 216};
                boolean hasTargetOption = Trangbi.itemOptions.stream()
                        .anyMatch(option -> Arrays.stream(optionIds).anyMatch(id -> id == option.optionTemplate.id));
                int randomOptionId = optionIds[Util.nextInt(0, 2)];
                int insertIndex = -1;
                for (int i = 0; i < Trangbi.itemOptions.size(); i++) {
                    if (Trangbi.itemOptions.get(i).optionTemplate.id == 30) {
                        insertIndex = i + 1;
                        break;
                    }
                }

                if (insertIndex == -1 || insertIndex > Trangbi.itemOptions.size()) {
                    insertIndex = Trangbi.itemOptions.size();
                }
                if (hasTargetOption) {
                    Trangbi.itemOptions.removeIf(option
                            -> Arrays.stream(optionIds).anyMatch(id -> id == option.optionTemplate.id));
                    if (Trangbi.itemOptions.size() < insertIndex) {
                        insertIndex = 0;
                    }
                    Trangbi.itemOptions.add(insertIndex, new ItemOption(randomOptionId, 1));
                } else {
                    Trangbi.itemOptions.add(insertIndex, new ItemOption(randomOptionId, 1));
                }
                InventoryService.gI().subQuantityItemsBag(player, dahoangkim, 30);
                InventoryService.gI().subQuantityItemsBag(player, mTS, 999);
                Service.getInstance().sendThongBao(player, "Chúc mừng! Bạn đã nâng cấp thành công!");
            } else {
                InventoryService.gI().subQuantityItemsBag(player, dahoangkim, 30);
                InventoryService.gI().subQuantityItemsBag(player, mTS, 999);
                Service.getInstance().sendThongBao(player, "Rất tiếc! Nâng cấp thất bại!");
            }

            InventoryService.gI().sendItemBags(player);
            Service.getInstance().sendMoney(player);
            player.combineNew.itemsCombine.clear();
            reOpenItemCombine(player);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
        }
    }

    private void nangCapBongTaiVIP(Player player) {
        if (player.combineNew.itemsCombine.size() == 3) {
            int gem = player.combineNew.gemCombine;
            int slDa = player.combineNew.DaNangcap;
            if (player.inventory.ruby < gem) {
                Service.getInstance().sendThongBao(player, "Không đủ Hồng ngọc để thực hiện");
                return;
            }
            Item bongTai = null;
            Item phieuHD = null;
            Item daAS = null;
            for (Item item : player.combineNew.itemsCombine) {
                switch (item.template.id) {
                    case 1999://btai c6
                    case 1944://btai c7
                    case 1945://btai c8
                        bongTai = item;
                        break;
                    case 1327:
                    case 1328:
                    case 1329:
                    case 1330:
                    case 1331:
                        phieuHD = item;
                        break;
                    case ConstItem.XU_SERVER:
                        daAS = item;
                        break;
                    default:
                        break;
                }
            }

            if (daAS == null || daAS.quantity < slDa) {
                this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Thiếu " + slDa + " " + ConstItem.NAME_XU_SERVER, "Đóng");
                return;
            }

            if (bongTai != null && phieuHD != null && phieuHD.quantity >= 10 && bongTai.template.id == 1999) {
                Item findItemBag = InventoryService.gI().findItemBagByTemp(player, 1944);
                if (findItemBag != null) {
                    Service.getInstance().sendThongBao(player, "Ngươi đã có bông tai Porata cấp 7 trong hàng trang rồi, không thể nâng cấp nữa.");
                    return;
                }
                player.inventory.ruby -= gem;
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    InventoryService.gI().subQuantityItemsBag(player, phieuHD, 10);
                    InventoryService.gI().subQuantityItemsBag(player, daAS, slDa);
                    bongTai.template = ItemService.gI().getTemplate(1944);
                    sendEffectSuccessCombine(player);
                } else {
                    InventoryService.gI().subQuantityItemsBag(player, phieuHD, 10);
                    InventoryService.gI().subQuantityItemsBag(player, daAS, slDa);
                    sendEffectFailCombine(player);
                }
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);
            } else if (bongTai != null && phieuHD != null && phieuHD.quantity >= 10 && bongTai.template.id == 1944) {
                Item findItemBag = InventoryService.gI().findItemBagByTemp(player, 1945);
                if (findItemBag != null) {
                    Service.getInstance().sendThongBao(player, "Ngươi đã có bông tai Porata cấp 8 trong hàng trang rồi, không thể nâng cấp nữa.");
                    return;
                }
                player.inventory.ruby -= gem;
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    InventoryService.gI().subQuantityItemsBag(player, phieuHD, 10);
                    InventoryService.gI().subQuantityItemsBag(player, daAS, slDa);
                    bongTai.template = ItemService.gI().getTemplate(1945);
                    sendEffectSuccessCombine(player);
                } else {
                    InventoryService.gI().subQuantityItemsBag(player, phieuHD, 10);
                    InventoryService.gI().subQuantityItemsBag(player, daAS, slDa);
                    sendEffectFailCombine(player);
                }
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);
            } else if (bongTai != null && phieuHD != null && phieuHD.quantity >= 10 && bongTai.template.id == 1945) {
                Item findItemBag = InventoryService.gI().findItemBagByTemp(player, 1946);
                if (findItemBag != null) {
                    Service.getInstance().sendThongBao(player, "Ngươi đã có bông tai Porata cấp 9 trong hàng trang rồi, không thể nâng cấp nữa.");
                    return;
                }
                player.inventory.ruby -= gem;
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    InventoryService.gI().subQuantityItemsBag(player, phieuHD, 10);
                    InventoryService.gI().subQuantityItemsBag(player, daAS, slDa);
                    bongTai.template = ItemService.gI().getTemplate(1946);
                    sendEffectSuccessCombine(player);
                } else {
                    InventoryService.gI().subQuantityItemsBag(player, phieuHD, 10);
                    InventoryService.gI().subQuantityItemsBag(player, daAS, slDa);
                    sendEffectFailCombine(player);
                }
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    private void nangCapGiapTapLuyen(Player player) {
        if (player.combineNew.itemsCombine.size() == 3) {
            int gem = player.combineNew.gemCombine;
            int slDa = player.combineNew.DaNangcap;
            if (player.inventory.ruby < gem) {
                Service.getInstance().sendThongBao(player, "Không đủ Hồng ngọc để thực hiện");
                return;
            }
            Item giapTapLuyen = null;
            Item manhTS = null;
            Item xuVang = null;
            for (Item item : player.combineNew.itemsCombine) {
                switch (item.template.id) {
                    case 1901://gtl 4
                    case 1902://gtl 5
                    case 1903://gtl 6
                    case 1904://gtl 7
                    case 1905://gtl 8
                        giapTapLuyen = item;
                        break;
                    case 1066:
                    case 1067:
                    case 1068:
                    case 1069:
                    case 1070:
                        manhTS = item;
                        break;
                    case ConstItem.XU_SERVER:
                        xuVang = item;
                        break;
                    default:
                        break;
                }
            }

            if (giapTapLuyen != null && manhTS != null && manhTS.quantity >= 999) {
                if (xuVang == null || xuVang.quantity < player.combineNew.DaNangcap) {
                    this.baHatMit.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Không đủ " + player.combineNew.DaNangcap + " " + ConstItem.NAME_XU_SERVER, "Đóng");
                    return;
                }
                Item findItemBag = InventoryService.gI().findItemBagByTemp(player, (short) (giapTapLuyen.template.id + 1));
                if (findItemBag != null) {
                    Service.getInstance().sendThongBao(player, "Ngươi đã có " + findItemBag.template.name + " trong hàng trang rồi, không thể nâng cấp nữa.");
                    return;
                }
                player.inventory.ruby -= gem;
                if (Util.isTrue(player.combineNew.ratioCombine, 100)) {
                    InventoryService.gI().subQuantityItemsBag(player, manhTS, 999);
                    InventoryService.gI().subQuantityItemsBag(player, xuVang, slDa);
                    
                    giapTapLuyen.template = ItemService.gI().getTemplate(giapTapLuyen.template.id + 1);
                    giapTapLuyen.itemOptions.clear();
                    giapTapLuyen.itemOptions.add(new ItemOption(50, getOptionsGTL(giapTapLuyen.template.id)));
                    giapTapLuyen.itemOptions.add(new ItemOption(77, getOptionsGTL(giapTapLuyen.template.id)));
                    giapTapLuyen.itemOptions.add(new ItemOption(103, getOptionsGTL(giapTapLuyen.template.id)));
                    
                    sendEffectSuccessCombine(player);
                } else {
                    InventoryService.gI().subQuantityItemsBag(player, manhTS, 999);
                    InventoryService.gI().subQuantityItemsBag(player, xuVang, slDa);
                    sendEffectFailCombine(player);
                }
                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                reOpenItemCombine(player);
            }
        }
    }

    public void openDoiSKHbill(Player player) {
        Item dahoangkim = null; //1782 
        Item dochinh = null;
        Item dophoi = null;
        try {
            dahoangkim = InventoryService.gI().findItemBagByTemp(player, 1791);
        } catch (Exception e) {
        }
        if (dahoangkim == null || dahoangkim.quantity < 1000) {
            Service.getInstance().sendThongBao(player, "Không đủ Đá Vũ Trụ");
            return;
        }
        if (player.combineNew.itemsCombine.size() != 2) {
            Service.getInstance().sendThongBao(player, "Thiếu nguyên liệu");
            return;
        }
        for (int j = 0; j < player.combineNew.itemsCombine.size(); j++) {
            Item item = player.combineNew.itemsCombine.get(j);
            if (item.isNotNullItem() && item.template.type < 5) {
                if (dochinh == null && item.isCheckSKHBill()) {
                    dochinh = item; // Gán dochinh nếu là trang bị và có SKH Bill
                } else if (item != dochinh && !(item.isCheckSKHBill())) {
                    dophoi = item; // Gán dophoi là item còn lại (khác dochinh)
                }
            }
        }
        if (dochinh != null && dophoi != null) {
            if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                CombineServiceNew.gI().sendEffectOpenItem(player, dochinh.template.iconID, dophoi.template.iconID);
                if (Util.isTrue(100, 100)) {
                    int[] optionIds = {214, 215, 216};
                    Optional<ItemOption> targetOption = dochinh.itemOptions.stream()
                            .filter(option -> Arrays.stream(optionIds).anyMatch(id -> id == option.optionTemplate.id))
                            .findFirst();

                    if (targetOption.isPresent()) {
                        int optionId = targetOption.get().optionTemplate.id;
                        dophoi.itemOptions.add(new ItemOption(optionId, 1));
                    } else {
                        Service.getInstance().sendThongBao(player, "Lỗi: Đồ chính không có dòng SKH Bill!");
                        return;
                    }
                    InventoryService.gI().subQuantityItemsBag(player, dahoangkim, 100);
                    InventoryService.gI().subQuantityItemsBag(player, dochinh, 1);
                    Service.getInstance().sendThongBao(player, "Chúc mừng! Bạn đã nâng cấp thành công!");
                } else {
                    InventoryService.gI().subQuantityItemsBag(player, dahoangkim, 100);
                    Service.getInstance().sendThongBao(player, "Rất tiếc! Nâng cấp thất bại!");
                }

                InventoryService.gI().sendItemBags(player);
                Service.getInstance().sendMoney(player);
                player.combineNew.itemsCombine.clear();
                reOpenItemCombine(player);
            } else {
                Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
            }
        }
    }
    //--------------------------------------------------------------------------

    /**
     * r
     * Hiệu ứng mở item
     *
     * @param player
     * @param icon1
     * @param icon2
     */
    public void sendEffectOpenItem(Player player, short icon1, short icon2) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(OPEN_ITEM);
            msg.writer().writeShort(icon1);
            msg.writer().writeShort(icon2);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * Hiệu ứng đập đồ thành công
     *
     * @param player
     */
    private void sendEffectSuccessCombine(Player player) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(COMBINE_SUCCESS);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * Hiệu ứng đập đồ thất bại
     *
     * @param player
     */
    private void sendEffectFailCombine(Player player) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(COMBINE_FAIL);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * Gửi lại danh sách đồ trong tab combine
     *
     * @param player
     */
    private void reOpenItemCombine(Player player) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(REOPEN_TAB_COMBINE);
            msg.writer().writeByte(player.combineNew.itemsCombine.size());
            for (Item it : player.combineNew.itemsCombine) {
                for (int j = 0; j < player.inventory.itemsBag.size(); j++) {
                    if (it == player.inventory.itemsBag.get(j)) {
                        msg.writer().writeByte(j);
                    }
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    /**
     * Hiệu ứng ghép ngọc rồng
     *
     * @param player
     * @param icon
     */
    private void sendEffectCombineDB(Player player, short icon) {
        Message msg;
        try {
            msg = new Message(-81);
            msg.writer().writeByte(COMBINE_DRAGON_BALL);
            msg.writer().writeShort(icon);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    //--------------------------------------------------Chân mệnh/////
    private int getDiemNangcapChanmenh(int star) {
        switch (star) {
            case 0:
                return 500;
            case 1:
                return 1000;
            case 2:
                return 2000;
            case 3:
                return 2500;
            case 4:
                return 3000;
            case 5:
                return 3500;
            case 6:
                return 4000;
            case 7:
                return 4500;
        }
        return 0;
    }

    private int getDaNangcapChanmenh(int star) {
        switch (star) {
            case 0:
                return 30;
            case 1:
                return 35;
            case 2:
                return 40;
            case 3:
                return 45;
            case 4:
                return 50;
            case 5:
                return 60;
            case 6:
                return 65;
            case 7:
                return 80;
        }
        return 0;
    }

    private float getTiLeNangcapChanmenh(int star) {
        switch (star) {
            case 0:
                return 60f;
            case 1:
                return 40f;
            case 2:
                return 30f;
            case 3:
                return 20f;
            case 4:
                return 10f;
            case 5:
                return 8f;
            case 6:
                return 4f;
            case 7:
                return 2f;
        }
        return 0;
    }

    //--------------------------------------------------------------------------Ratio, cost combine
    private int getGoldPhaLeHoa(int star) {
        switch (star) {
            case 0:
                return 50000000;
            case 1:
                return 60000000;
            case 2:
                return 70000000;
            case 3:
                return 100000000;
            case 4:
                return 180000000;
            case 5:
                return 200000000;
            case 6:
                return 210000000;
            case 7:
                return 230000000;
            case 8:
                return 30;
            case 9:
                return 50;
        }
        return 0;
    }

    private float getRatioPhaLeHoa(int star) {
        switch (star) {
            case 0:
                return 70;
            case 1:
                return 50;
            case 2:
                return 40;
            case 3:
                return 30;
            case 4:
                return 15;
            case 5:
                return 2;
            case 6:
                return 0.7f;
            case 7:
                return 0.5f;
            case 8:
                return 0.5f;
            case 9:
                return 0.3f;
        }

        return 0;
    }

    private int getGemPhaLeHoa(int star) {
        switch (star) {
            case 0:
                return 30;
            case 1:
                return 40;
            case 2:
                return 50;
            case 3:
                return 60;
            case 4:
                return 70;
            case 5:
                return 75;
            case 6:
                return 80;
            case 7:
                return 80;
            case 8:
                return 100;
            case 9:
                return 120;
        }
        return 0;
    }

    private int getGemEpSao(int star) {
        switch (star) {
            case 0:
                return 1;
            case 1:
                return 2;
            case 2:
                return 5;
            case 3:
                return 10;
            case 4:
                return 25;
            case 5:
                return 50;
            case 6:
                return 100;
            case 7:
                return 110;
            case 8:
                return 120;
            case 9:
                return 130;
        }
        return 0;
    }

    private int getCloudSkyGiaiDoan(int lo) {
        switch (lo) {
            case 1:
                return 1000;
            case 2:
                return 1000;
            case 3:
                return 1000;
            case 4:
                return 1000;
            case 5:
                return 1000;
            case 6:
                return 1000;
            case 7:
                return 1000;
            case 8:
                return 1000;
            default:
                return 1000;
        }
    }

    private double getTileNangCapDo(int level) { //Nâng cấp đồ bà hạt mít
        switch (level) {
            case 0:
                return 95;
            case 1:
                return 80;
            case 2:
                return 70;
            case 3:
                return 60;
            case 4:
                return 35;
            case 5:
                return 15;
            case 6:
                return 5;
            case 7: // 7 sao
                return 0.5;
        }
        return 0;
    }

    private int getCountDaNangCapDo(int level) { // Số đá nâng cấp mỗi level
        switch (level) {
            case 0:
                return 3;
            case 1:
                return 7;
            case 2:
                return 11;
            case 3:
                return 17;
            case 4:
                return 23;
            case 5:
                return 35;
            case 6:
                return 50;
            case 7:
                return 60;
        }
        return 0;
    }

    private int getCountDaBaoVe(int level) {
        return level + 1;
    }

    private int getGoldNangCapDo(int level) {
        switch (level) {
            case 0:
                return 10000000;
            case 1:
                return 17000000;
            case 2:
                return 30000000;
            case 3:
                return 40000000;
            case 4:
                return 70000000;
            case 5:
                return 80000000;
            case 6:
                return 100000000;
            case 7:
                return 250000000;
        }
        return 0;
    }

    private double getTileNangPK(int level) {
        switch (level) {
            case 0:
                return 50;
            case 1:
                return 40;
            case 2:
                return 30;
            case 3:
                return 25;
            case 4:
                return 20;
            case 5:
                return 15;
            case 6:
                return 10;
            case 7:
                return 5;
            case 8:
                return 3;
            case 9:
                return 1;
        }
        return 0;
    }

    private int getCountDaNangPK(int level) {
        switch (level) {
            case 0:
                return 30;
            case 1:
                return 50;
            case 2:
                return 70;
            case 3:
                return 100;
            case 4:
                return 130;
            case 5:
                return 150;
            case 6:
                return 170;
            case 7:
                return 170;
            case 8:
                return 200;
            case 9:
                return 300;
        }
        return 0;
    }

    private int getCountTVNangPK(int level) {
        switch (level) {
            case 0:
                return 1000;
            case 1:
                return 2000;
            case 2:
                return 3000;
            case 3:
                return 4000;
            case 4:
                return 5000;
            case 5:
                return 6000;
            case 6:
                return 7000;
            case 7:
                return 8000;
            case 8:
                return 90000;
            case 9:
                return 10000;
        }
        return 0;
    }

    private int getChiSoSDHPKI(int level) {
        switch (level) {
            case 0:
                return 2;
            case 1:
                return 3;
            case 2:
                return 4;
            case 3:
                return 5;
            case 4:
                return 6;
            case 5:
                return 7;
            case 6:
                return 8;
            case 7:
                return 9;
            case 8:
                return 10;
            case 9:
                return 20;
        }
        return 0;
    }

    //--------------------------------------------------------------------------check
//nâng cấp cải trang
    private int getChiSoSD(int level) {
        switch (level) {
            case 0:
                return 5;
            case 1:
                return 5;
            case 2:
                return 5;
            case 3:
                return 10;
            case 4:
                return 10;
            case 5:
                return 10;
            case 6:
                return 20;
            case 7:
                return 20;
            case 8:
                return 35;
            case 9:
                return 50;
        }
        return 0;
    }

    private int getChiSoHPKI(int level) {
        switch (level) {
            case 0:
                return 5;
            case 1:
                return 5;
            case 2:
                return 5;
            case 3:
                return 10;
            case 4:
                return 10;
            case 5:
                return 10;
            case 6:
                return 20;
            case 7:
                return 20;
            case 8:
                return 35;
            case 9:
                return 50;
        }
        return 0;
    }

    private int getChuyenHoaCaiTrang(int idcaitrang) {
        switch (idcaitrang) {
            case 1589:
                return 1591;
            case 1591:
                return 1592;
            case 1592:
                return 1588;
        }
        return 0;
    }

    private double getTileNangCaiTrang(int level) {
        switch (level) {
            case 0:
                return 60;
            case 1:
                return 50;
            case 2:
                return 40;
            case 3:
                return 30;
            case 4:
                return 20;
            case 5:
                return 10;
            case 6:
                return 6;
            case 7:
                return 4;
            case 8:
                return 2;
            case 9:
                return 1;
        }
        return 0;
    }

    private int getCountDaNangCapCaiTrang(int level) {
        switch (level) {
            case 0:
                return 1000;
            case 1:
                return 2000;
            case 2:
                return 3000;
            case 3:
                return 4000;
            case 4:
                return 5000;
            case 5:
                return 6000;
            case 6:
                return 7000;
            case 7:
                return 8000;
            case 8:
                return 9000;
            case 9:
                return 10000;
        }
        return 0;
    }

    private int getLinhThachNCapGTL(int idTemp) {
        switch (idTemp) {
            case 1901:
                return 100;
            case 1902:
                return 100;
            case 1903:
                return 100;
            case 1904:
                return 150;
            case 1905:
                return 200;
        }
        return 0;
    }

    private int getTiLeNCapGTL_Ao(int idTemp) {
        switch (idTemp) {
            case 1901:
                return 50;
            case 1902:
                return 40;
            case 1903:
                return 30;
            case 1904:
                return 20;
            case 1905:
                return 10;
        }
        return 0;
    }

    private int getTiLeNCapGTL(int idTemp) {
        switch (idTemp) {
            case 1901:
                return 30;
            case 1902:
                return 20;
            case 1903:
                return 10;
            case 1904:
                return 5;
            case 1905:
                return 2;
        }
        return 0;
    }

    private int getOptionsGTL(int idTemp) {
        switch (idTemp) {
            case 1902:
                return 50;
            case 1903:
                return 70;
            case 1904:
                return 100;
            case 1905:
                return 140;
            case 1906:
                return 200;
        }
        return 0;
    }

    private boolean isCoupleItemNangCap(Item item1, Item item2) {
        Item trangBi = null;
        Item daNangCap = null;
        if (item1 != null && item1.isNotNullItem()) {
            if (item1.template.type < 5) {
                trangBi = item1;
            } else if (item1.template.type == 14) {
                daNangCap = item1;
            }
        }
        if (item2 != null && item2.isNotNullItem()) {
            if (item2.template.type < 5) {
                trangBi = item2;
            } else if (item2.template.type == 14) {
                daNangCap = item2;
            }
        }
        if (trangBi != null && daNangCap != null) {
            if (trangBi.template.type == 0 && daNangCap.template.id == 223) {
                return true;
            } else if (trangBi.template.type == 1 && daNangCap.template.id == 222) {
                return true;
            } else if (trangBi.template.type == 2 && daNangCap.template.id == 224) {
                return true;
            } else if (trangBi.template.type == 3 && daNangCap.template.id == 221) {
                return true;
            } else if (trangBi.template.type == 4 && daNangCap.template.id == 220) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isCoupleItemNangCapCheck(Item trangBi, Item daNangCap) {
        if (trangBi != null && daNangCap != null) {
            if (trangBi.template.type == 0 && daNangCap.template.id == 223) {
                return true;
            } else if (trangBi.template.type == 1 && daNangCap.template.id == 222) {
                return true;
            } else if (trangBi.template.type == 2 && daNangCap.template.id == 224) {
                return true;
            } else if (trangBi.template.type == 3 && daNangCap.template.id == 221) {
                return true;
            } else if (trangBi.template.type == 4 && daNangCap.template.id == 220) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean issachTuyetKy(Item item) {
        if (item != null && item.isNotNullItem()) {
            if (item.template.type == 35) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean issachTuyetKy1(Item item) {
        if (item != null && item.isNotNullItem()) {
            if (item.template.id == 1510 || item.template.id == 1512 || item.template.id == 1514) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isDaPhaLe(Item item) {
        return item != null
                && item.template != null
                && (item.template.type == 30
                || (item.template.id >= 14 && item.template.id <= 20)
                || (item.template.id >= 1185 && item.template.id <= 1191));
    }

    private boolean hasSaoPhaLeDaEp(Item item) {
        return item != null
                && item.isNotNullItem()
                && item.itemOptions.stream().anyMatch(io -> io.optionTemplate.id == 102 && io.param > 0);
    }

    private boolean isTrangBiPhaLeHoa(Item item) {
        if (item != null && item.isNotNullItem()) {
            if ((item.template.type < 5 || item.template.type == 32 || item.template.type == 5)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isTrangBiAn(Item item) {
        if (item != null && item.isNotNullItem()) {
            if (item.template.id >= 1048 && item.template.id <= 1062
                    || item.template.id >= 1401 && item.template.id <= 1405 || item.isThanhTon() || item.isThienSuDT()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isTrangBiPhuKien(Item item) {
        if (item != null && item.isNotNullItem()) {
            if ((item.template.type == 11 || item.template.type == 72 || item.template.type == 75 || item.template.type == 74
                    || item.template.type == 21 || item.template.type == 24)) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private boolean isTrangBiPhapsu(Item item) {
        if (item != null && item.isNotNullItem()) {
            if ((item.template.type == 5 || item.template.type == 11 || item.template.type == 72
                    || ItemData.list_dapdo.contains((int) item.template.id)) && !item.isTrangBiHSD()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private int getParamDaPhaLe(Item daPhaLe) {
        if (daPhaLe.template.type == 30) {
            return daPhaLe.itemOptions.get(0).param;
        }
        switch (daPhaLe.template.id) {
            case 20:
                return 5; // +5%hp
            case 19:
                return 5; // +5%ki
            case 18:
                return 5; // +5%hp/30s
            case 17:
                return 5; // +5%ki/30s
            case 16:
                return 3; // +3%sđ
            case 15:
                return 2; // +2%giáp
            case 14:
                return 2; // +2%né đòn
            case 1187:
                return 4; // +4%sđ
            case 1185:
                return 2; // +2%cm
            case 1190:
                return 7; // +7%ki
            case 1191:
                return 7; // +7%hp
            default:
                return -1;
        }
    }

    private int getOptionDaPhaLe(Item daPhaLe) {
        if (daPhaLe.template.type == 30) {
            return daPhaLe.itemOptions.get(0).optionTemplate.id;
        }
        switch (daPhaLe.template.id) {
            case 20:
                return 77;
            case 19:
                return 103;
            case 18:
                return 80;
            case 17:
                return 81;
            case 16:
                return 50;
            case 15:
                return 94;
            case 14:
                return 108;
            case 1187:
                return 50; //sd
            case 1185:
                return 14; //chi mang
            case 1190:
                return 103; //ki
            case 1191:
                return 77; //hp
            default:
                return -1;
        }
    }

    /**
     * Trả về id item c0
     *
     * @param gender
     * @param type
     * @return
     */
    private int getTempIdItemC0(int gender, int type) {
        if (type == 4) {
            return 12;
        }
        switch (gender) {
            case 0:
                switch (type) {
                    case 0:
                        return 0;
                    case 1:
                        return 6;
                    case 2:
                        return 21;
                    case 3:
                        return 27;
                }
                break;
            case 1:
                switch (type) {
                    case 0:
                        return 1;
                    case 1:
                        return 7;
                    case 2:
                        return 22;
                    case 3:
                        return 28;
                }
                break;
            case 2:
                switch (type) {
                    case 0:
                        return 2;
                    case 1:
                        return 8;
                    case 2:
                        return 23;
                    case 3:
                        return 29;
                }
                break;
        }
        return -1;
    }

    //Trả về tên đồ c0
    private String getNameItemC0(int gender, int type) {
        if (type == 4) {
            return "Rada cấp 1";
        }
        switch (gender) {
            case 0:
                switch (type) {
                    case 0:
                        return "Áo vải 3 lỗ";
                    case 1:
                        return "Quần vải đen";
                    case 2:
                        return "Găng thun đen";
                    case 3:
                        return "Giầy nhựa";
                }
                break;
            case 1:
                switch (type) {
                    case 0:
                        return "Áo sợi len";
                    case 1:
                        return "Quần sợi len";
                    case 2:
                        return "Găng sợi len";
                    case 3:
                        return "Giầy sợi len";
                }
                break;
            case 2:
                switch (type) {
                    case 0:
                        return "Áo vải thô";
                    case 1:
                        return "Quần vải thô";
                    case 2:
                        return "Găng vải thô";
                    case 3:
                        return "Giầy vải thô";
                }
                break;
        }
        return "";
    }

    //--------------------------------------------------------------------------Text tab combine
    private String getTextTopTabCombine(int type) {
        switch (type) {
            case EP_SAO_TRANG_BI:
                return "Ta sẽ phù phép\ncho trang bị của ngươi\ntrở lên mạnh mẽ";
            case PHA_LE_HOA_TRANG_BI:
                return "Ta sẽ phù phép\ncho trang bị của ngươi\ntrở thành trang bị pha lê";
            case GO_SAO_PHA_LE_TRANG_BI:
                return "Ta sẽ tách bỏ\nnhững tinh thể đã khảm\nkhỏi trang bị của ngươi";
            case AN_TRANG_BI:
                return "Ta sẽ phù phép\ncho trang bị của ngươi\ntrở thành trang bị Ấn";
            case NHAP_NGOC_RONG:
                return "Ta sẽ phù phép\ncho viên Ngọc Rồng cấp thấp\nthành 1 viên Ngọc Rồng cấp cao";
            case NANG_CAP_VAT_PHAM:
                return "Ta sẽ phù phép cho trang bị của ngươi trở lên mạnh mẽ";
            case NANG_CAP_BONG_TAI:
                return "Ta sẽ phù phép\ncho bông tai Porata của ngươi\nthành Bông tai cấp cao hơn 1 bậc";
            case NANG_CAP_BONG_TAI_VO_CUC:
                return "Ta sẽ phù phép\ncho bông tai Porata cấp 5 của ngươi\nthành Bông tai Porata Vô Cực";
            case MO_CHI_SO_BONG_TAI:
                return "Ta sẽ phù phép\ncho bông tai Porata cấp 2 của ngươi\ncó 1 chỉ số ngẫu nhiên";
            case DOT_PHA_BONG_TAI:
                return "Ta sẽ phù phép\ncho bông tai Porata của ngươi đạt hiệu ứng mới";
            case PHAN_RA_DO_THAN_LINH:
                return "Ta sẽ phân rã \n  trang bị của người thành điểm!";
            case PHAN_RA_DO_HUY_DIET:
                return "Ta sẽ phân rã \n  trang bị Hủy diệt của ngươi\nthành Phiếu hủy diệt!";
            case PHAN_RA_DO_TS:
                return "Ta sẽ phân rã \n  trang bị Thiên sứ của ngươi\nthành 500 mảnh thiên sứ cùng hệ!";
            case NANG_CAP_DO_TS:
                return "Ta sẽ nâng cấp \n  trang bị của người thành\n đồ thiên sứ!";
            case NANG_CAP_SKH_VIP:
                return "Thiên sứ nhờ ta nâng cấp \n  trang bị của người thành\n SKH VIP!";
            case NANG_CAP_SKH_TS:
                return "Thiên sứ nhờ ta nâng cấp \n  trang bị của người thành\n SKH VIP!";
            case NANG_CAP_THAN_LINH:
                return "Ta sẽ nâng cấp \n trang bị Thần linh của ngươi\n thành món Hủy diệt Tương ứng!";
            case PHAP_SU_HOA:
                return "Pháp sư hóa trang bị\nTa sẽ phù phép cho trang bị của ngươi trở lên mạnh mẽ";
            case TAY_PHAP_SU:
                return "Ta sẽ phù phép\ncho trang bị của ngươi\ntrở về lúc chưa 'Pháp sư hóa'";
            case NANG_CAP_CHAN_MENH:
                return "Ta sẽ Nâng cấp\nChân Mệnh của ngươi\ncao hơn một bậc";
            case GIA_HAN_VAT_PHAM:
                return "Ta sẽ phù phép\ncho trang bị của ngươi\nthêm hạn sử dụng";
            // START_ SÁCH TUYỆT KỸ //
            case GIAM_DINH_SACH:
                return "Ta sẽ giám định\nSách Tuyệt Kỹ cho ngươi";
            case TAY_SACH:
                return "Ta sẽ phù phép\ntẩy sách đó cho ngươi";
            case NANG_CAP_SACH_TUYET_KY:
                return "Ta sẽ nâng cấp\nSách Tuyệt Kỹ cho ngươi";
            case PHUC_HOI_SACH:
                return "Ta sẽ phục hồi\nsách cho ngươi";
            case PHAN_RA_SACH:
                return "Ta sẽ phân rã\nsách cho ngươi";
            // END _ SÁCH TUYỆT KỸ //
            case NANG_CAP_CAI_TRANG:
                return "Ta sẽ phù phép cho Cải trang của ngươi trở lên mạnh mẽ";
            case NANG_CAP_PHU_KIEN:
                return "Ta sẽ phù phép cho Phụ kiện của ngươi trở lên mạnh mẽ";
            case PHAN_RA_VAT_PHAM:
                return "Ta sẽ phân rã \n  trang bị của ngươi\nthành Vật phẩm bất kì!";
            case NANG_CAP_SKH_BILL:
                return "Admin nhờ ta nâng cấp\nchỉ số mới cho trang bị của người\n trở nên VIP hơn!";
            case DOI_SKH_BILL:
                return "Admin nhờ ta Chuyển Đổi chỉ số\ntrang bị của người\n trở nên VIP hơn!";
            case NANG_CAP_BONG_TAI_VIP:
                return "Ta sẽ phù phép\ncho bông tai Porata của ngươi\nthành Bông tai cấp cao hơn 1 bậc";
            case NANG_CAP_GIAP_TAP_LUYEN:
                return "Ta sẽ phù phép\ncho Giáp Tập Luyện của ngươi\ntrở nên đẳng cấp";
            default:
                return "";
        }
    }

    private String getTextInfoTabCombine(int type) {
        switch (type) {
            case EP_SAO_TRANG_BI:
                return "Chọn trang bị\n"
                        + "(Áo, quần, găng, giày hoặc rađa) có ô đặt sao pha lê\n"
                        + "Chọn loại sao pha lê\n"
                        + "Sau đó chọn 'Nâng cấp'";
            case PHA_LE_HOA_TRANG_BI:
                return "Chọn trang bị\n"
                        + "(Áo, quần, găng, giày\n"
                        + ", rađa hoặc Cải trang)\n"
                        + "Sau đó chọn 'Nâng cấp'";
            case GO_SAO_PHA_LE_TRANG_BI:
                return "Chọn 1 trang bị đã ép sao pha lê\n"
                        + "Sau đó chọn 'Tách bỏ Tinh Thể'\n"
                        + "Trang bị sẽ trở về trạng thái ban đầu";
            case AN_TRANG_BI:
                return "Vào hành trang\n"
                        + "Chọn 1 Trang bị Thiên Sứ - Thánh Tôn\n"
                        + "Trang bị Thiên Sứ 99 mảnh\n"
                        + "Trang bị Thánh Tôn 299 mảnh\n"
                        + "Sau đó chọn 'Làm phép'\n"
                        + "Tinh ấn 5 TB 20-50 %KI\n"
                        + "Nhật ấn 5 TB 20-50 %HP\n"
                        + "Nguyệt ấn 5 TB 20-50 %SD";
            case NHAP_NGOC_RONG:
                return "Vào hành trang\n"
                        + "Chọn 7, 10 hoặc 20 viên ngọc cùng sao\n"
                        + "Sau đó chọn 'Làm phép'";
            case NANG_CAP_VAT_PHAM:
                return "Vào hành trang\n"
                        + "Chọn trang bị\n"
                        + "(Áo, quần, găng, giày hoặc rađa)\n"
                        + "Chọn loại đá để nâng cấp\n"
                        + "Sau đó chọn 'Nâng cấp'";
            case NANG_CAP_BONG_TAI:
                return "Vào hành trang\n"
                        + "Chọn bông tai Porata 1\n"
                        + "Chọn mảnh bông tai để nâng cấp(Số lượng: 99)\n"
                        + "(Số lượng: 99)\n"
                        + "Sau đó chọn 'Nâng cấp'\n"
                        + "Nếu thất bại sẽ bị trừ đi 100 Mảnh bông tai\n"
                        + "Sau khi thành công Bông tai của ngươi sẽ tăng 1 bậc";
            case NANG_CAP_BONG_TAI_VO_CUC:
                return "Vào hành trang\n"
                        + "Chọn bông tai Porata 5\n"
                        + "Chọn mảnh bông tai Cấp 6 để nâng cấp(Số lượng: 99)\n"
                        + "Sau đó chọn 'Nâng cấp'\n"
                        + "Nếu thất bại sẽ bị trừ đi 99 Mảnh bông tai\n"
                        + "Sau khi thành công Bông tai của ngươi sẽ tăng 1 bậc";
            case MO_CHI_SO_BONG_TAI:
                return "Vào hành trang\n"
                        + "Chọn bông tai Porata cấp 2 trở lên\n"
                        + "Chọn mảnh hồn bông tai số lượng 99 cái\n"
                        + "và đá xanh lam để nâng cấp\n"
                        + "Sau đó chọn 'Nâng cấp'";
            case DOT_PHA_BONG_TAI:
                return "Vào hành trang\n"
                        + "Chọn bông tai Porata cấp 2,3,4 hoặc 5\n"
                        + "Chọn 10 Thỏi vàng\n"
                        + "và đá xanh lam để nâng cấp\n"
                        + "Sau đó chọn 'Nâng cấp'";
            case PHAN_RA_DO_THAN_LINH:
                return "Vào hành trang\n"
                        + "Chọn trang bị\n"
                        + "(Áo, quần, găng, giày hoặc rađa)\n"
                        + "Chọn loại đá để phân rã\n"
                        + "Sau đó chọn 'Phân Rã'";
            case PHAN_RA_DO_HUY_DIET:
                return "Vào hành trang\n"
                        + "Chọn trang bị\n"
                        + "(Áo, quần, găng, giày hoặc rađa) Hủy diệt\n"
                        + "Sau đó chọn 'Chuyển hóa'\n"
                        + "Yêu cầu: 20.000 thỏi vàng để chuyển hóa";
            case PHAN_RA_DO_TS:
                return "Vào hành trang\n"
                        + "Chọn trang bị\n"
                        + "(Áo, quần, găng, giày hoặc nhẫn) Thiên sứ\n"
                        + "Sau đó chọn 'Chuyển hóa'";
            case NANG_CAP_DO_TS:
                return "Vào hành trang\n"
                        + "Chọn 1 Công thức theo Hành tinh + 1 Đá cầu vòng\n"
                        + " và 999 mảnh thiên sứ\n "
                        + "sẽ cho ra đồ thiên sứ từ 0-15% chỉ số\n"
                        + "(Có tỉ lệ thêm dòng chỉ số ẩn)\n"
                        + "Sau đó chọn 'Nâng Cấp'";
            case NANG_CAP_SKH_VIP:
                return "Vào hành trang\n"
                        + "Chọn 3 trang bị Hủy diệt bất kì\n"
                        + "Đồ SKH VIP sẽ cùng loại với đồ Hủy diệt!\n"
                        + "Chọn 'Nâng Cấp'";
            case NANG_CAP_SKH_TS:
                return "Vào hành trang\n"
                        + "Cần 3 món đồ Thiên Sứ\n"
                        + "(lưu ý món Thiên Sứ nào cho vào trước sẽ ra món kích hoạt tương ứng)\n"
                        + "Khi nâng cấp sẽ ra đồ cùng hành tinh"
                        + " SKH Thánh Tôn , SKH Thiên Sứ , SKH Hủy Diệt ( random 3 loại SKH )!\n"
                        + "Chọn 'Nâng Cấp'";
            case NANG_CAP_THAN_LINH:
                return "Vào hành trang\n"
                        + "Chọn 1 món Thần linh bất kì\n"
                        + " Đồ Hủy diệt sẽ cùng loại và hành tinh của món đó\n"
                        + "Chọn 'Nâng Cấp'";
            case PHAP_SU_HOA:
                return "Vào hành trang\n"
                        + "Chọn trang bị\n"
                        + "(Pet, VP đeo, Danh hiệu, Linh thú, Cải trang)\n"
                        + "Chọn Đá Pháp Sư\n"
                        + "Sau đó chọn 'Nâng cấp'";
            case TAY_PHAP_SU:
                return "Vào hành trang\n"
                        + "Chọn trang bị\n"
                        + "(Pet, VP đeo, Danh hiệu, Linh thú, Cải trang 'đã Pháp sư hóa')\n"
                        + "Chọn Bùa Tẩy Pháp Sư\n"
                        + "Sau đó chọn 'Nâng cấp'";
            case NANG_CAP_CHAN_MENH:
                return "Vào hành trang\n"
                        + "Chọn Chân mệnh muốn nâng cấp\n"
                        + "Chọn Đá Hoàng Kim\n"
                        + "Sau đó chọn 'Nâng cấp'\n"
                        + "Lưu ý: Khi Nâng cấp Thành công SD tăng 3%, HP,KI tăng 4% chỉ số của cấp trước đó";
            case GIA_HAN_VAT_PHAM:
                return "Vào hành trang\n"
                        + "Chọn 1 trang bị có hạn sử dụng\n"
                        + "Chọn thẻ gia hạn\n"
                        + "Sau đó chọn 'Gia hạn'";
            // START_ SÁCH TUYỆT KỸ //
            case GIAM_DINH_SACH:
                return "Vào hành trang chọn\n1 Sách cần giám định\n"
                        + "Sau đó chọn Bùa Giám định";
            case TAY_SACH:
                return "Vào hành trang chọn\n1 sách cần tẩy";
            case NANG_CAP_SACH_TUYET_KY:
                return "Vào hành trang chọn\nSách Tuyệt Kỹ 1 cần nâng cấp và 10 Kìm bấm giấy";
            case PHUC_HOI_SACH:
                return "Vào hành trang chọn\nCác Sách Tuyệt Kỹ cần phục hồi\n"
                        + "Sau đó chọn 10 Cuốn sách cũ";
            case PHAN_RA_SACH:
                return "Vào hành trang chọn\n1 sách cần phân rã";
            // END _ SÁCH TUYỆT KỸ //
            case NANG_CAP_CAI_TRANG:
                return "Vào hành trang\n"
                        + "Chọn Cải trang chỉ định\n"
                        + "Thỏi vàng\n"
                        + "1 Món Đồ Hủy Diệt\n"
                        + "Sau đó chọn 'Nâng cấp'";
            case NANG_CAP_PHU_KIEN:
                return "Vào hành trang\n"
                        + "Chọn Đeo lưng, Pet, Thú Cưỡi hoặc Linh thú\n"
                        + "Và bỏ 1 món Thần linh\n"
                        + "Thỏi vàng và Đá ngũ sắc vào\n"
                        + "Sau đó chọn 'Nâng cấp'";
            case PHAN_RA_VAT_PHAM:
                return "Vào hành trang\n"
                        + "Chọn trang bị\n"
                        + "(Áo, quần, găng, giày hoặc nhẫn)\n"
                        + "Thần linh hoặc Hủy diệt\n"
                        + "Sau đó chọn 'Nâng cấp'";
            case NANG_CAP_SKH_BILL:
                return "Vào hành trang\n"
                        + "Cần 1 món đồ trang bị\n"
                        + "Và 999 mảnh Thiên Sứ\n"
                        + "Phí Nâng Cấp: 20 Đá Vũ Trụ\n"
                        + "Lưu ý\n: Random 3 chỉ số khác nhau\n"
                        + "50% Tốc Độ Đánh\n"
                        + "50% Tốc Độ Chưởng\n"
                        + "50% Sát Thương Chí Mạng\n"
                        + "Chọn 'Nâng Cấp'";
            case DOI_SKH_BILL:
                return "Vào hành trang\n"
                        + "Cần 1 món đồ trang bị [SKH Vũ Trụ]\n"
                        + "Phí Đổi: 1000 Đá Vũ Trụ\n"
                        + "Lưu ý\n: Sau khi đổi Thành công bạn sẽ mất trang bị chính\n"
                        + "Chọn 'Nâng Cấp'";
            case NANG_CAP_BONG_TAI_VIP:
                return "Vào hành trang\n"
                        + "Chọn bông tai Porata 6, 7 hoặc 8\n"
                        + "Chọn " + ConstItem.NAME_XU_SERVER + "\n"
                        + "Chọn 10 Phiếu Hủy diệt bất kì\n"
                        + "Sau đó chọn 'Nâng cấp'\n"
                        + "Nếu thất bại sẽ bị trừ vật phẩm\n"
                        + "Sau khi thành công Bông tai của ngươi sẽ tăng 1 bậc";
            case NANG_CAP_GIAP_TAP_LUYEN:
                return "Vào hành trang\n"
                        + "Chọn Giáp tập luyện 4,5,6,7 hoặc 8\n"
                        + "Chọn \n"
                        + "Chọn 999 Mảnh Thiên sứ bất kì\n"
                        + "Sau đó chọn 'Nâng cấp'\n"
                        + "Nếu thất bại sẽ bị trừ vật phẩm\n"
                        + "Sau khi thành công Giáp tập luyện của ngươi sẽ tăng 1 bậc";
            default:
                return "";
        }
    }
}
