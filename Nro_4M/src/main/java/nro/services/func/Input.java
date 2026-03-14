package nro.services.func;

import editor.Final_Static;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import nro.consts.ConstNpc;
import nro.jdbc.daos.PlayerDAO;
import nro.models.item.Item;
import nro.models.map.Zone;
import nro.models.npc.Npc;
import nro.models.npc.NpcManager;
import nro.models.player.Player;
import nro.server.Client;
import nro.server.io.Message;
import nro.services.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import nro.consts.ConstItem;
import nro.models.item.ItemOption;
import nro.server.Manager;
import nro.utils.FileUtils;
import nro.utils.TimeUtil;
import nro.utils.Util;

public class Input {

    private static final Map<Integer, Object> PLAYER_ID_OBJECT = new HashMap<Integer, Object>();

    public static final int CHANGE_PASSWORD = 500;
    public static final int GIFT_CODE = 501;
    public static final int FIND_PLAYER = 502;
    public static final int CHANGE_NAME = 503;
    public static final int CHOOSE_LEVEL_BDKB = 5066;
    public static final int CHOOSE_LEVEL_CDRD = 7700;
    public static final int TANG_NGOC_HONG = 505;
    public static final int ADD_ITEM = 506;
    public static final int QUY_DOI_HONG_NGOC = 508;
    public static final int QUY_DOI_XU_VANG = 509;
    public static final int BUFF_ITEM_OPTION = 510;
    public static final int XIU_TAIXIU = 514;
    public static final int TAI_TAIXIU = 515;
    public static final int BUFF_DANH_HIEU = 516;
    public static final int CHANGE_NAME_BY_ITEM = 517;
    public static final int CHON_SO_MAY_MAN = 518;
    public static final int GIFT_MEMBER = 519;
    public static final int XIU_XU = 521;
    public static final int TAI_XU = 522;
    public static final int TANG_COIN = 523;
    public static final int TACH_ITEM = 524;
    public static final int GOP_ITEM = 525;

    public static final int XIU_CLIENT = 5000;
    public static final int TAI_CLIENT = 5001;
    public static final int CHAT_TAI_XIU = 5002;

    public static final byte NUMERIC = 0;
    public static final byte ANY = 1;
    public static final byte PASSWORD = 2;

    private static Input intance;

    public static Input gI() {
        if (intance == null) {
            intance = new Input();
        }
        return intance;
    }

    public void doInput(Player player, Message msg) {
        try {
            Player pl = null;
            String[] text = new String[msg.reader().readByte()];
            for (int i = 0; i < text.length; i++) {
                text[i] = msg.reader().readUTF();
            }
            switch (player.iDMark.getTypeInput()) {
                case CHANGE_PASSWORD:
                    Service.getInstance().changePassword(player, text[0], text[1], text[2]);
                    break;
                case GIFT_CODE:
                    GiftService.gI().use(player, text[0]);
                    break;
                case GIFT_MEMBER:
                    GiftcodePlayer.gI().use(player, text[0]);
                    break;
                case FIND_PLAYER:
                    Player plFound = null;
                    String name = text[0].trim();

                    for (Player p : Client.gI().getPlayers()) {
                        if (p != null && p.name.equalsIgnoreCase(name)) {
                            plFound = p;
                            break;
                        }
                    }

                    if (plFound != null) {
                        NpcService.gI().createMenuConMeo(player, ConstNpc.MENU_FIND_PLAYER, -1,
                                "Ngài muốn..?",
                                new String[]{
                                        "Đi tới\n" + plFound.name,
                                        "Gọi " + plFound.name + "\ntới đây",
                                        "Đổi tên",
                                        "Ban"},
                                plFound);
                    } else {
                        Service.getInstance().sendThongBao(player, "Người chơi không online");
                    }
                    break;
                case CHANGE_NAME:
                    Player plChanged = (Player) PLAYER_ID_OBJECT.get((int) player.id);
                    if (plChanged != null) {
                        if (PlayerDAO.isExistName(text[0])) {
                            Service.getInstance().sendThongBao(player, "Tên nhân vật đã tồn tại");
                        } else {
                            plChanged.name = text[0];
                            PlayerDAO.saveName(plChanged);
                            Service.getInstance().player(plChanged);
                            Service.getInstance().Send_Caitrang(plChanged);
                            Service.getInstance().sendFlagBag(plChanged);
                            Zone zone = plChanged.zone;
                            ChangeMapService.gI().changeMap(plChanged, zone, plChanged.location.x, plChanged.location.y);
                            Service.getInstance().sendThongBao(plChanged, "Chúc mừng bạn đã có cái tên mới đẹp đẽ hơn tên ban đầu");
                            Service.getInstance().sendThongBao(player, "Đổi tên người chơi thành công");
                        }
                    }
                    break;
                case CHANGE_NAME_BY_ITEM: {
                    if (PlayerDAO.isExistName(text[0])) {
                        Service.getInstance().sendThongBao(player, "Tên nhân vật đã tồn tại");
                        createFormChangeNameByItem(player);
                    } else {
                        Item theDoiTen = InventoryService.gI().findItem(player.inventory.itemsBag, 2006);
                        if (theDoiTen == null) {
                            Service.getInstance().sendThongBao(player, "Không tìm thấy thẻ đổi tên");
                        } else {
                            InventoryService.gI().subQuantityItemsBag(player, theDoiTen, 1);
                            player.name = text[0];
                            PlayerDAO.saveName(player);
                            Service.getInstance().player(player);
                            Service.getInstance().Send_Caitrang(player);
                            Service.getInstance().sendFlagBag(player);
                            Zone zone = player.zone;
                            ChangeMapService.gI().changeMap(player, zone, player.location.x, player.location.y);
                            Service.getInstance().sendThongBao(player, "Chúc mừng bạn đã có cái tên mới đẹp đẽ hơn tên ban đầu");
                        }
                    }
                }
                break;
                case CHOOSE_LEVEL_BDKB: {
                    int level = Integer.parseInt(text[0]);
                    if (level >= 1 && level <= 110) {
                        Npc npc = NpcManager.getByIdAndMap(ConstNpc.QUY_LAO_KAME, player.zone.map.mapId);
                        if (npc != null) {
                            npc.createOtherMenu(player, ConstNpc.MENU_ACCEPT_GO_TO_BDKB,
                                    "Con có chắc chắn muốn tới bản đồ kho báu cấp độ " + level + "?",
                                    new String[]{"Đồng ý", "Từ chối"}, level);
                        }
                    } else {
                        Service.getInstance().sendThongBao(player, "Không thể thực hiện");
                    }
                }

//                    BanDoKhoBauService.gI().openBanDoKhoBau(player, (byte) );
                break;
                case CHOOSE_LEVEL_CDRD: {
                    int level = Integer.parseInt(text[0]);
                    if (level >= 1 && level <= 110) {
                        Npc npc = NpcManager.getByIdAndMap(ConstNpc.THAN_VU_TRU, player.zone.map.mapId);
                        if (npc != null) {
                            npc.createOtherMenu(player, ConstNpc.MENU_ACCEPT_GO_TO_CDRD,
                                    "Con có chắc chắn muốn đến con đường rắn độc cấp độ " + level + "?",
                                    new String[]{"Đồng ý", "Từ chối"}, level);
                        }
                    } else {
                        Service.getInstance().sendThongBao(player, "Không thể thực hiện");
                    }
                }

//                    BanDoKhoBauService.gI().openBanDoKhoBau(player, (byte) );
                break;
                case TANG_NGOC_HONG:
                    pl = Client.gI().findPlayerByAddingVipPrefix(text[0].trim());
                    int numruby = Integer.parseInt((text[1]));
                    if (pl != null) {
                        if (numruby > 0 && player.inventory.ruby >= numruby) {
                            player.inventory.subRuby(numruby);
                            PlayerService.gI().sendInfoHpMpMoney(player);
                            pl.inventory.ruby += numruby;
                            PlayerService.gI().sendInfoHpMpMoney(pl);
                            Service.getInstance().sendThongBao(player, "Tặng Hồng ngọc thành công");
                            Service.getInstance().sendThongBao(pl, "Bạn được " + player.name + " tặng " + numruby + " Hồng ngọc");
                            InventoryService.gI().sendItemBags(player);
                        } else {
                            Service.getInstance().sendThongBao(player, "Không đủ Hồng ngọc để tặng");
                        }
                    } else {
                        Service.getInstance().sendThongBao(player, "Người chơi không tồn tại hoặc đang offline");
                    }
                    break;
                case TANG_COIN:
                    pl = Client.gI().findPlayerByAddingVipPrefix(text[0].trim());
                    int numcoin = Integer.parseInt((text[1]));
                    if (pl != null) {
                        if (numcoin > 0 && player.getSession().vnd >= numcoin) {
                            player.getSession().vnd -= numcoin;
                            PlayerService.gI().sendInfoHpMpMoney(player);
                            pl.getSession().vnd += numcoin;
                            PlayerService.gI().sendInfoHpMpMoney(pl);
                            Service.getInstance().sendThongBao(player, "Tặng VNĐ thành công");
                            Service.getInstance().sendThongBao(pl, "Bạn được " + player.name + " tặng " + numcoin + " VNĐ");
                            InventoryService.gI().sendItemBags(player);
                        } else {
                            Service.getInstance().sendThongBao(player, "Không đủ VNĐ để tặng");
                        }
                    } else {
                        Service.getInstance().sendThongBao(player, "Người chơi không tồn tại hoặc đang offline");
                    }
                    break;
                case ADD_ITEM:
                    short id = Short.parseShort((text[0]));
                    int quantity = Integer.parseInt(text[1]);
                    Item item = ItemService.gI().createNewItem(id);
                    if (item.template.type < 7) {
                        for (int i = 0; i < quantity; i++) {
                            item = ItemService.gI().createNewItem(id);
                            RewardService.gI().initBaseOptionClothes(item.template.id, item.template.type, item.itemOptions);
                            InventoryService.gI().addItemBag(player, item, 0);
                        }
                    } else {
                        item.quantity = quantity;
                        InventoryService.gI().addItemBag(player, item, 0);
                    }
                    InventoryService.gI().sendItemBags(player);
                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + item.template.name + " Số lượng: " + quantity);
                    break;
                    
                case BUFF_ITEM_OPTION:
                    if (player.isAdmin()) {
                          Player pBuffItem = Client.gI().findPlayerByAddingVipPrefix(text[0].trim());
                        int idItemBuff = Integer.parseInt(text[1]);
                        String idOptionBuff = text[2].trim();

                        int slItemBuff = Integer.parseInt(text[3]);

                        try {
                            if (pBuffItem != null) {
                                String txtBuff = player.name + " Buff cho : " + pBuffItem.name + "\b";

                                Item itemBuffTemplate = ItemService.gI().createNewItem((short) idItemBuff, slItemBuff);
                                if (!idOptionBuff.isEmpty()) {
                                    String arr[] = idOptionBuff.split("v");
                                    for (int i = 0; i < arr.length; i++) {
                                        String arr2[] = arr[i].split("-");
                                        int idoption = Integer.parseInt(arr2[0].trim());
                                        int param = Integer.parseInt(arr2[1].trim());
                                        itemBuffTemplate.itemOptions.add(new ItemOption(idoption, param));
                                    }

                                }
                                for (ItemOption io : itemBuffTemplate.itemOptions) {
                                    txtBuff += io.getOptionString() + "\n";
                                }
                                txtBuff += "Số lượng: " + slItemBuff + " " + itemBuffTemplate.template.name + "\b";
                                InventoryService.gI().addItemBag(pBuffItem, itemBuffTemplate, 99);
                                InventoryService.gI().sendItemBags(pBuffItem);
                                NpcService.gI().createTutorial(player, 24, txtBuff);
                                if (player.id != pBuffItem.id) {
                                    NpcService.gI().createTutorial(pBuffItem, 24, txtBuff);
                                }
                            } else {
                                Service.getInstance().sendThongBao(player, "Player không online");
                            }
                        } catch (Exception e) {
                            Service.getInstance().sendThongBao(player, "Đã có lỗi xảy ra vui lòng thử lại");
                        }

                    }
                    break;
                case BUFF_DANH_HIEU:
                    if (player.isAdmin()) {
                        Player pBuffItem = Client.gI().getPlayer(text[0]);
                        int idDhieu = Integer.parseInt(text[1]);
                        int SoNgay = Integer.parseInt(text[2]);
                        try {
                            if (pBuffItem != null) {
                                if ((idDhieu >= 1 && idDhieu <= 5) && SoNgay > 0) {
                                    String txtBuff = player.name + " vừa Tặng Danh hiệu " + (idDhieu == 1 ? "Đại Thần " : idDhieu == 2
                                            ? "Cần Thủ " : idDhieu == 3 ? "Tuổi Thơ " : idDhieu == 4 ? "Thợ Ngọc " : "Ai Mà Xinh Thế")
                                            + SoNgay + " Ngày cho người chơi : " + pBuffItem.name;
                                    switch (idDhieu) {
                                        case 1:
                                            if (pBuffItem.lastTimeTitle1 == 0) {
                                                pBuffItem.lastTimeTitle1 += System.currentTimeMillis() + (1000 * 60 * 60 * 24 * SoNgay);
                                            } else {
                                                pBuffItem.lastTimeTitle1 += (1000 * 60 * 60 * 24 * SoNgay);
                                            }
                                            pBuffItem.isTitleUse1 = true;
                                            Service.getInstance().point(pBuffItem);
                                            Service.getInstance().sendTitle(pBuffItem, 888);
                                            break;
                                        case 2:
                                            if (pBuffItem.lastTimeTitle2 == 0) {
                                                pBuffItem.lastTimeTitle2 += System.currentTimeMillis() + (1000 * 60 * 60 * 24 * SoNgay);
                                            } else {
                                                pBuffItem.lastTimeTitle2 += (1000 * 60 * 60 * 24 * SoNgay);
                                            }
                                            pBuffItem.isTitleUse2 = true;
                                            Service.getInstance().point(pBuffItem);
                                            Service.getInstance().sendTitle(pBuffItem, 889);
                                            break;
                                        case 3:
                                            if (pBuffItem.lastTimeTitle3 == 0) {
                                                pBuffItem.lastTimeTitle3 += System.currentTimeMillis() + (1000 * 60 * 60 * 24 * SoNgay);
                                            } else {
                                                pBuffItem.lastTimeTitle3 += (1000 * 60 * 60 * 24 * SoNgay);
                                            }
                                            pBuffItem.isTitleUse3 = true;
                                            Service.getInstance().point(pBuffItem);
                                            Service.getInstance().sendTitle(pBuffItem, 890);
                                            break;
                                        case 4:
                                            if (pBuffItem.lastTimeTitle4 == 0) {
                                                pBuffItem.lastTimeTitle4 += System.currentTimeMillis() + (1000 * 60 * 60 * 24 * SoNgay);
                                            } else {
                                                pBuffItem.lastTimeTitle4 += (1000 * 60 * 60 * 24 * SoNgay);
                                            }
                                            pBuffItem.isTitleUse4 = true;
                                            Service.getInstance().point(pBuffItem);
                                            Service.getInstance().sendTitle(pBuffItem, 891);
                                            break;
                                        case 5:
                                            if (pBuffItem.lastTimeTitle5 == 0) {
                                                pBuffItem.lastTimeTitle5 += System.currentTimeMillis() + (1000 * 60 * 60 * 24 * SoNgay);
                                            } else {
                                                pBuffItem.lastTimeTitle5 += (1000 * 60 * 60 * 24 * SoNgay);
                                            }
                                            pBuffItem.isTitleUse5 = true;
                                            Service.getInstance().point(pBuffItem);
                                            Service.getInstance().sendTitle(pBuffItem, 892);
                                            break;
                                    }
                                    NpcService.gI().createTutorial(player, 24, txtBuff);
                                    if (player.id != pBuffItem.id) {
                                        NpcService.gI().createTutorial(pBuffItem, 24, txtBuff);
                                    }
                                } else {
                                    Service.getInstance().sendThongBao(player, "Không có Danh hiệu này!!");
                                }
                            } else {
                                Service.getInstance().sendThongBao(player, "Player không online");
                            }
                        } catch (Exception e) {
                            Service.getInstance().sendThongBao(player, "Đã có lỗi xảy ra vui lòng thử lại");
                        }

                    }
                    break;
                case TAI_TAIXIU:
                    int sotvxiu1 = Integer.valueOf(text[0]);
                    try {
                        if (!player.getSession().actived) {
                            Service.getInstance().sendThongBao(player, "Vui lòng MTV để chơi");
                            return;
                        }
                        if (sotvxiu1 > 0 && sotvxiu1 <= 2000000000) {
                            Item thoivang = InventoryService.gI().findItemBagByTemp(player, 457);
                            if (thoivang != null && thoivang.quantity >= sotvxiu1) {
                                player.goldTai += sotvxiu1;
                                TaiXiu.gI().goldTai += sotvxiu1;
                                Service.getInstance().sendThongBao(player, "Bạn đã đặt " + Util.format(sotvxiu1) + " Thỏi vàng vào TÀI");
                                TaiXiu.gI().addPlayerTai(player);
                                InventoryService.gI().subQuantityItemsBag(player, thoivang, sotvxiu1);
                                InventoryService.gI().sendItemBags(player);
                                Service.getInstance().sendMoney(player);
                            } else {
                                Service.getInstance().sendThongBao(player, "Bạn không đủ Thỏi vàng để chơi.");
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Nhập từ 1-2Ty");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Service.getInstance().sendThongBao(player, "Lỗi.");
                        System.out.println("nnnnn2  ");
                    }
                    break;

                case XIU_TAIXIU:
                    int sotvxiu2 = Integer.valueOf(text[0]);
                    try {
                        if (!player.getSession().actived) {
                            Service.getInstance().sendThongBao(player, "Vui lòng MTV để chơi");
                            return;
                        }
                        if (sotvxiu2 > 0 && sotvxiu2 <= 2000000000) {
                            Item thoivang = InventoryService.gI().findItemBagByTemp(player, 457);
                            if (thoivang != null && thoivang.quantity >= sotvxiu2) {
                                player.goldXiu += sotvxiu2;
                                TaiXiu.gI().goldXiu += sotvxiu2;
                                Service.getInstance().sendThongBao(player, "Bạn đã đặt " + Util.format(sotvxiu2) + " Thỏi vàng vào XỈU");
                                TaiXiu.gI().addPlayerXiu(player);
                                InventoryService.gI().subQuantityItemsBag(player, thoivang, sotvxiu2);
                                InventoryService.gI().sendItemBags(player);
                                Service.getInstance().sendMoney(player);
                            } else {
                                Service.getInstance().sendThongBao(player, "Bạn không đủ Thỏi vàng để chơi.");
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Nhập từ 1-2Ty");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Service.getInstance().sendThongBao(player, "Lỗi.");
                        System.out.println("nnnnn2  ");
                    }
                    break;

                case TAI_XU:
                    int sotvxiu3 = Integer.valueOf(text[0]);
                    try {
                        if (!player.getSession().actived) {
                            Service.getInstance().sendThongBao(player, "Vui lòng MTV để chơi");
                            return;
                        }
                        if (sotvxiu3 > 0 && sotvxiu3 < 10000000) {
                            if (player.inventory.ruby >= sotvxiu3) {
                                player.goldTai_xu += sotvxiu3;
                                TaiXiu_Xu.gI().goldTai += sotvxiu3;
                                Service.getInstance().sendThongBao(player, "Bạn đã đặt " + Util.format(sotvxiu3) + " Hồng ngọc vào TÀI");
                                TaiXiu_Xu.gI().addPlayerTai(player);
                                player.inventory.ruby -= sotvxiu3;
                                InventoryService.gI().sendItemBags(player);
                                Service.getInstance().sendMoney(player);
                            } else {
                                Service.getInstance().sendThongBao(player, "Bạn không đủ Hồng ngọc để chơi.");
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Nhập từ 1-10Tr");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Service.getInstance().sendThongBao(player, "Lỗi.");
                        System.out.println("nnnnn2  ");
                    }
                    break;

                case XIU_XU:
                    int sotvxiu4 = Integer.valueOf(text[0]);
                    try {
                        if (!player.getSession().actived) {
                            Service.getInstance().sendThongBao(player, "Vui lòng MTV để chơi");
                            return;
                        }
                        if (sotvxiu4 > 0 && sotvxiu4 < 10000000) {
                            if (player.inventory.ruby >= sotvxiu4) {
                                player.goldXiu_xu += sotvxiu4;
                                TaiXiu_Xu.gI().goldXiu += sotvxiu4;
                                Service.getInstance().sendThongBao(player, "Bạn đã đặt " + Util.format(sotvxiu4) + " Hồng ngọc vào XỈU");
                                TaiXiu_Xu.gI().addPlayerXiu(player);
                                player.inventory.ruby -= sotvxiu4;
                                InventoryService.gI().sendItemBags(player);
                                Service.getInstance().sendMoney(player);
                            } else {
                                Service.getInstance().sendThongBao(player, "Bạn không đủ Hồng ngọc để chơi.");
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Nhập từ 1-10Tr");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Service.getInstance().sendThongBao(player, "Lỗi.");
                        System.out.println("nnnnn2  ");
                    }
                    break;
                case CHON_SO_MAY_MAN:
                    int sochon = Integer.parseInt(text[0]);
                    try {
                        if (sochon >= 0 && sochon <= 99) {
                            if (player.inventory.ruby >= 1000) {
                                player.inventory.ruby -= 1000;
                                player.soMayMan.add(sochon);
                                SoMayMan.gI().addPlayerSMM(player);
                                Service.getInstance().youNumber(player, sochon);
                                Service.getInstance().sendThongBao(player, "Bạn đã chọn số " + sochon + " thành công");
                                Service.getInstance().sendMoney(player);
                            } else {
                                Service.getInstance().sendThongBao(player, "Bạn không đủ Hồng ngọc để chơi.");
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Chọn 1 số từ 0 đến 99");
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Service.getInstance().sendThongBao(player, "Lỗi.");
                    }
                    break;
                case TAI_CLIENT: {
                    int sotien_tai = Integer.parseInt(text[0]);
                    try {
                        if (sotien_tai > 100_000_000) {
                            Service.getInstance().sendThongBao(player, "Tối đa đạt cược là 100Tr");
                            return;
                        }
                        if (GameDuDoan.gI().nanKG) {
                            Service.getInstance().sendThongBao(player, "Ngoài thời gian đặt cược");
                            return;
                        }
                        if (GameDuDoan.ID_ITEM_CUOC == 861) {
                            if (player.inventory.ruby >= sotien_tai) {
                                player.goldTai += sotien_tai;
                                GameDuDoan.gI().goldTai += sotien_tai;
                                Service.getInstance().sendThongBao(player, "Bạn đã đặt " + Util.format(sotien_tai) + " " + GameDuDoan.NAME_ITEM_CUOC + " vào " + GameDuDoan.LON);
                                GameDuDoan.gI().addPlayerTai(player);
                                player.inventory.ruby -= sotien_tai;
                                InventoryService.gI().sendItemBags(player);
                                Service.getInstance().sendMoney(player);
                                GameDuDoan.gI().Send_TaiXiu(player);
                            } else {
                                Service.getInstance().sendThongBao(player, "Bạn không đủ " + GameDuDoan.NAME_ITEM_CUOC + " để chơi.");
                            }
                        } else {
                            Item itemCuoc = InventoryService.gI().findItemBagByTemp(player, GameDuDoan.ID_ITEM_CUOC);
                            if (itemCuoc != null && itemCuoc.quantity >= sotien_tai) {
                                player.goldTai += sotien_tai;
                                GameDuDoan.gI().goldTai += sotien_tai;
                                Service.getInstance().sendThongBao(player, "Bạn đã đặt " + Util.format(sotien_tai) + " " + GameDuDoan.NAME_ITEM_CUOC + " vào " + GameDuDoan.LON);
                                GameDuDoan.gI().addPlayerTai(player);
                                InventoryService.gI().subQuantityItemsBag(player, itemCuoc, sotien_tai);
                                InventoryService.gI().sendItemBags(player);
                                Service.getInstance().sendMoney(player);
                                GameDuDoan.gI().Send_TaiXiu(player);
                            } else {
                                Service.getInstance().sendThongBao(player, "Bạn không đủ " + GameDuDoan.NAME_ITEM_CUOC + " để chơi.");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Service.getInstance().sendThongBao(player, "Lỗi.");
                    }
                    break;
                }
                case XIU_CLIENT: {
                    int sovang_xiu = Integer.parseInt(text[0]);
                    try {
                        if (sovang_xiu > 100_000_000) {
                            Service.getInstance().sendThongBao(player, "Tối đa đạt cược là 100Tr");
                            return;
                        }
                        if (GameDuDoan.gI().nanKG) {
                            Service.getInstance().sendThongBao(player, "Ngoài thời gian đặt cược");
                            return;
                        }
                        if (GameDuDoan.ID_ITEM_CUOC == 861) {
                            if (player.inventory.ruby >= sovang_xiu) {
                                player.goldXiu += sovang_xiu;
                                GameDuDoan.gI().goldXiu += sovang_xiu;
                                Service.getInstance().sendThongBao(player, "Bạn đã đặt " + Util.format(sovang_xiu) + " " + GameDuDoan.NAME_ITEM_CUOC + " vào " + GameDuDoan.NHO);
                                GameDuDoan.gI().addPlayerXiu(player);
                                player.inventory.ruby -= sovang_xiu;
                                InventoryService.gI().sendItemBags(player);
                                Service.getInstance().sendMoney(player);
                                GameDuDoan.gI().Send_TaiXiu(player);
                            } else {
                                Service.getInstance().sendThongBao(player, "Bạn không đủ " + GameDuDoan.NAME_ITEM_CUOC + " để chơi.");
                            }
                        } else {
                            Item itemCuoc = InventoryService.gI().findItemBagByTemp(player, GameDuDoan.ID_ITEM_CUOC);
                            if (itemCuoc != null && itemCuoc.quantity >= sovang_xiu) {
                                player.goldXiu += sovang_xiu;
                                GameDuDoan.gI().goldXiu += sovang_xiu;
                                Service.getInstance().sendThongBao(player, "Bạn đã đặt " + Util.format(sovang_xiu) + " " + GameDuDoan.NAME_ITEM_CUOC + " vào " + GameDuDoan.NHO);
                                GameDuDoan.gI().addPlayerXiu(player);
                                InventoryService.gI().subQuantityItemsBag(player, itemCuoc, sovang_xiu);
                                InventoryService.gI().sendItemBags(player);
                                Service.getInstance().sendMoney(player);
                                GameDuDoan.gI().Send_TaiXiu(player);
                            } else {
                                Service.getInstance().sendThongBao(player, "Bạn không đủ " + GameDuDoan.NAME_ITEM_CUOC + " để chơi.");
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        Service.getInstance().sendThongBao(player, "Lỗi.");
                    }
                    break;
                }
                case CHAT_TAI_XIU: {
                    String noidung = text[0];
                    try {
                        if (noidung.length() > 30) {
                            Service.getInstance().sendThongBao(player, "Tối đa 30 ký tự");
                            return;
                        }
                        if (noidung.length() > 0) {
                            GameDuDoan.gI().noiDungChat.add("[" + player.name + "] " + noidung);
                            GameDuDoan.gI().Send_TaiXiu(player);
                        }
                    } catch (Exception e) {
                    }
                    break;
                }
                case QUY_DOI_HONG_NGOC: {
                    int goldTradehn = Integer.parseInt(text[0]);
                    int kmnap;
                    if (Manager.KHUYEN_MAI_NAP <= 0) {
                        kmnap = 1;
                    } else {
                        kmnap = Manager.KHUYEN_MAI_NAP;
                    }
                    if (goldTradehn % 1000 == 0) {
                        if (goldTradehn <= 0 || goldTradehn >= 1000001) {
                            Service.getInstance().sendThongBao(player, "|7|Quá giới hạn mỗi lần tối đa 1.000.000");
                        } else if (player.getSession().vnd >= goldTradehn) {
                            if (!PlayerDAO.checkVnd(player, goldTradehn)) {
                                Service.getInstance().sendThongBao(player, "|7|Không đủ VNĐ");
                                return;
                            }
                            PlayerDAO.subVnd(player, goldTradehn);
                            player.tongnap += goldTradehn;
                            Item thoiVang = ItemService.gI().createNewItem((short) 861, (kmnap * (goldTradehn * 10 * kmnap)));
                            InventoryService.gI().addItemBag(player, thoiVang, 9999);
                            InventoryService.gI().sendItemBags(player);
                            Service.getInstance().sendThongBao(player, "Bạn nhận được " + Util.format((kmnap * (goldTradehn * 10 * kmnap)))
                                    + " " + thoiVang.template.name);
                        } else {
                            Service.getInstance().sendThongBao(player, "|7|Số VNĐ của bạn là " + player.getSession().vnd + " không đủ để quy "
                                    + " đổi " + (goldTradehn * 10 * kmnap) + " Hồng Ngọc" + " " + "bạn cần thêm " + (player.getSession().vnd - goldTradehn));
                        }
                    } else {
                        Service.getInstance().sendThongBao(player, "|7|Số VNĐ nhập phải là bội số của 1000");
                    }
                    break;
                }
                case QUY_DOI_XU_VANG: {
                    int goldTrade1 = Integer.parseInt(text[0]);
                    int kmnap1;
                    if (Manager.KHUYEN_MAI_NAP <= 0) {
                        kmnap1 = 1;
                    } else {
                        kmnap1 = Manager.KHUYEN_MAI_NAP;
                    }
                    if (goldTrade1 % 1000 == 0) {
                        if (goldTrade1 <= 0 || goldTrade1 >= 1000001) {
                            Service.getInstance().sendThongBao(player, "|7|Quá giới hạn mỗi lần tối đa 1.000.000");
                        } else if (player.getSession().vnd >= goldTrade1) {
                            if (!PlayerDAO.checkVnd(player, goldTrade1)) {
                                Service.getInstance().sendThongBao(player, "|7|Không đủ VNĐ");
                                return;
                            }
                            PlayerDAO.subVnd(player, goldTrade1);
                            player.tongnap += goldTrade1;
                            Item thoiVang = ItemService.gI().createNewItem((short) 1400, (kmnap1 * (goldTrade1 / 100)));
                            InventoryService.gI().addItemBag(player, thoiVang, 9999);
                            InventoryService.gI().sendItemBags(player);
                            Service.getInstance().sendThongBao(player, "Bạn nhận được " + Util.format((kmnap1 * (goldTrade1 / 100)))
                                    + " " + thoiVang.template.name);
                        } else {
                            Service.getInstance().sendThongBao(player, "|7|Số VNĐ của bạn là " + player.getSession().vnd + " không đủ để quy "
                                    + " đổi " + (goldTrade1 / 100) + " " + ConstItem.NAME_XU_SERVER + " " + "bạn cần thêm " + (player.getSession().vnd - goldTrade1));
                        }
                    } else {
                        Service.getInstance().sendThongBao(player, "|7|Số VNĐ nhập phải là bội số của 1000");
                    }
                    break;
                }
                case TACH_ITEM: {
                    try {
                        int itemId = Integer.parseInt(text[0]);
                        int indexInBag = Integer.parseInt(text[1]);
                        int quantityPerStack = Integer.parseInt(text[2]);
                        int numStacks = text.length > 3 ? Integer.parseInt(text[3]) : 1;

                        if (!Final_Static.isAllowTachItem(itemId)) {
                            Service.getInstance().sendThongBaoOK(player, "Vật phẩm id: " + itemId + " không thể tách!");
                            return;
                        }

                        if (quantityPerStack <= 0 || numStacks <= 0) {
                            Service.getInstance().sendThongBaoOK(player, "Số lượng stack và mỗi stack phải lớn hơn 0!");
                            return;
                        }

                        if (quantityPerStack > 9999) {
                            Service.getInstance().sendThongBaoOK(player, "Số lượng mỗi stack tối đa là 9999!");
                            return;
                        }

                        int totalNeeded = quantityPerStack * numStacks;

                        if (indexInBag < 0 || indexInBag >= player.inventory.itemsBag.size()) {
                            Service.getInstance().sendThongBao(player, "Vị trí túi không hợp lệ");
                            return;
                        }

                        Item itemGoc = player.inventory.itemsBag.get(indexInBag);
                        if (itemGoc == null || !itemGoc.isNotNullItem() || itemGoc.template.id != itemId) {
                            Service.getInstance().sendThongBao(player, "Không tìm thấy vật phẩm ở vị trí chỉ định");
                            return;
                        }

                        if (itemGoc.quantity < totalNeeded) {
                            Service.getInstance().sendThongBaoOK(player,
                                    "Không đủ vật phẩm. Cần " + totalNeeded + ", hiện có " + itemGoc.quantity);
                            return;
                        }

                        int emptySlots = InventoryService.gI().getCountEmptyBag(player);
                        if (emptySlots < numStacks) {
                            Service.getInstance().sendThongBao(player, "Không đủ ô trống. Cần " + numStacks + ", còn " + emptySlots);
                            return;
                        }

                        Item templateClone = ItemService.gI().copyItem(itemGoc);
                        templateClone.quantity = quantityPerStack;

                        // Trừ tổng số lượng
                        InventoryService.gI().subQuantityItemsBag(player, itemGoc, totalNeeded);

                        // Tạo từng stack mới
                        for (int i = 0; i < numStacks; i++) {
                            Item clone = ItemService.gI().copyItem(templateClone);
                            if (clone.quantity <= 0) {
                                continue;
                            }
                            InventoryService.gI().addItemBag(player, clone, -923710);
                        }

                        InventoryService.gI().sendItemBags(player);
                        Service.getInstance().sendThongBaoOK(player,
                                "Tách thành công " + numStacks + " stack x" + quantityPerStack + " " + templateClone.template.name);

                        FileUtils.writeTachItemLog(player, itemId, itemGoc.quantity, quantityPerStack, totalNeeded, numStacks, templateClone.template.name);

                    } catch (Exception e) {
                        Service.getInstance().sendThongBao(player, "Lỗi cú pháp hoặc dữ liệu tách không hợp lệ");
                    }
                    break;
                }
                case GOP_ITEM: {
                    try {
                        int itemId = Integer.parseInt(text[0]);

                        if (!Final_Static.isAllowTachItem(itemId)) {
                            Service.getInstance().sendThongBaoOK(player, "Vật phẩm id: " + itemId + " không thể gộp!");
                            return;
                        }

                        List<Item> bag = player.inventory.itemsBag;
                        List<Item> targetItems = bag.stream()
                                .filter(it -> it != null && it.isNotNullItem() && it.template.id == itemId)
                                .collect(Collectors.toList());

                        if (targetItems.size() <= 1) {
                            Service.getInstance().sendThongBao(player, "Không đủ vật phẩm để gộp!");
                            return;
                        }

                        List<List<Item>> grouped = new ArrayList<>();

                        for (Item it : targetItems) {
                            boolean added = false;
                            for (List<Item> group : grouped) {
                                if (group.get(0).isSameItemForMerge(it)) {
                                    group.add(it);
                                    added = true;
                                    break;
                                }
                            }
                            if (!added) {
                                List<Item> newGroup = new ArrayList<>();
                                newGroup.add(it);
                                grouped.add(newGroup);
                            }
                        }

                        int groupMerged = 0;
                        for (List<Item> group : grouped) {
                            if (group.size() <= 1) {
                                continue;
                            }

                            long totalBefore = group.stream().mapToLong(it -> it.quantity).sum();
                            Item base = group.get(0);
                            for (int i = 1; i < group.size(); i++) {
                                base.quantity += group.get(i).quantity;
                                group.get(i).quantity = 0;
                            }
                            groupMerged++;

                            for (int i = 0; i < bag.size(); i++) {
                                Item it = bag.get(i);
                                if (it != null && it.isNotNullItem() && it.quantity <= 0) {
                                    bag.set(i, ItemService.gI().createItemNull());
                                }
                            }

                            FileUtils.logMergeItem(player, itemId, totalBefore, base.quantity);
                        }

                        InventoryService.gI().sendItemBags(player);
                        Service.getInstance().sendThongBaoOK(player, "Đã gộp " + groupMerged + " nhóm vật phẩm!");

                    } catch (Exception e) {
                        Service.getInstance().sendThongBao(player, "Lỗi gộp vật phẩm!");
                    }
                    break;
                }
            }
        } catch (Exception e) {
        }
    }

    public void createForm(Player pl, int typeInput, String title, SubInput... subInputs) {
        pl.iDMark.setTypeInput(typeInput);
        Message msg;
        try {
            msg = new Message(-125);
            msg.writer().writeUTF(title);
            msg.writer().writeByte(subInputs.length);
            for (SubInput si : subInputs) {
                msg.writer().writeUTF(si.name);
                msg.writer().writeByte(si.typeInput);
            }
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void createFormChangePassword(Player pl) {
        createForm(pl, CHANGE_PASSWORD, "Đổi mật khẩu", new SubInput("Mật khẩu cũ", PASSWORD),
                new SubInput("Mật khẩu mới", PASSWORD),
                new SubInput("Nhập lại mật khẩu mới", PASSWORD));
    }

    public void createFormGiftCode(Player pl) {
        createForm(pl, GIFT_CODE, "Mã quà tặng:"
                + " Liên hệ admin để nhận mã quà tặng",
                new SubInput("Nhập mã quà tặng", ANY));
    }

    public void createGiftMember(Player pl) {
        createForm(pl, GIFT_MEMBER, "Mã quà tặng", new SubInput("Nhập mã quà tặng", ANY));
    }

    public void createFormFindPlayer(Player pl) {
        createForm(pl, FIND_PLAYER, "Tìm kiếm người chơi", new SubInput("Tên người chơi", ANY));
    }

    public void createFormChangeName(Player pl, Player plChanged) {
        PLAYER_ID_OBJECT.put((int) pl.id, plChanged);
        createForm(pl, CHANGE_NAME, "Đổi tên " + plChanged.name, new SubInput("Tên mới", ANY));
    }

    public void createFormChooseLevelBDKB(Player pl) {
        createForm(pl, CHOOSE_LEVEL_BDKB, "Chọn cấp độ", new SubInput("Cấp độ (1-110)", NUMERIC));
    }

    public void createFormChooseLevelCDRD(Player pl) {
        createForm(pl, CHOOSE_LEVEL_CDRD, "Chọn cấp độ", new SubInput("Cấp độ (1-110)", NUMERIC));
    }

    public void createFormTangRuby(Player pl) {
        createForm(pl, TANG_NGOC_HONG, "Tặng ngọc", new SubInput("Tên nhân vật", ANY),
                new SubInput("Số Hồng Ngọc Muốn Tặng", NUMERIC));
    }

    public void createFormTangCoin(Player pl) {
        createForm(pl, TANG_COIN, "Tặng VNĐ", new SubInput("Tên nhân vật", ANY),
                new SubInput("Số VNĐ Muốn Tặng", NUMERIC));
    }

    public void createFormAddItem(Player pl) {
        createForm(pl, ADD_ITEM, "Add Item", new SubInput("ID VẬT PHẨM", NUMERIC),
                new SubInput("SỐ LƯỢNG", NUMERIC));
    }

    public void createFormChangeNameByItem(Player pl) {
        createForm(pl, CHANGE_NAME_BY_ITEM, "Đổi tên " + pl.name, new SubInput("Tên mới", ANY));
    }

    public void createFormQDHongNgoc(Player pl) {
        createForm(pl, QUY_DOI_HONG_NGOC, "ĐỔI HỒNG NGỌC", new SubInput("Nhập số lượng muốn đổi", NUMERIC));
    }

    public void createFormQDXUSKY(Player pl) {
        createForm(pl, QUY_DOI_XU_VANG, "Đổi " + ConstItem.NAME_XU_SERVER, new SubInput("Nhập số lượng muốn đổi", NUMERIC));
    }

    public void createFormBuffItemVip(Player pl) {
        createForm(pl, BUFF_ITEM_OPTION, "BUFF VIP", new SubInput("Tên người chơi", ANY), new SubInput("Id Item", ANY), new SubInput("Chuỗi option vd : 50-20v30-1", ANY), new SubInput("Số lượng", ANY));
    }

    public void createFormBuffDanhHieu(Player pl) {
        createForm(pl, BUFF_DANH_HIEU, "Tặng Danh Hiệu", new SubInput("Tên người chơi", ANY),
                new SubInput("Danh hiệu: 1.Đại thần 2.Cần thủ 3.Tuổi thơ 4.Thợ ngọc 5.Ai mà xinh thế", ANY), new SubInput("Số Ngày", ANY));
    }

    public void TAI_taixiu(Player pl) {
        createForm(pl, TAI_TAIXIU, "Chọn số Thỏi vàng đặt Tài", new SubInput("Số Thỏi vàng cược", ANY));//????
    }

    public void XIU_taixiu(Player pl) {
        createForm(pl, XIU_TAIXIU, "Chọn số Thỏi vàng đặt Xỉu", new SubInput("Số Thỏi vàng cược", ANY));//????
    }

    public void TAI_XU(Player pl) {
        createForm(pl, TAI_XU, "Chọn số Hồng ngọc đặt Tài", new SubInput("Số Hồng ngọc cược", ANY));//????
    }

    public void XIU_XU(Player pl) {
        createForm(pl, XIU_XU, "Chọn số Hồng ngọc đặt Xỉu", new SubInput("Số Hồng ngọc cược", ANY));//????
    }

    public void ChonSo(Player pl) {
        createForm(pl, CHON_SO_MAY_MAN, "Hãy chọn 1 số từ: 0 đến 99 giá 1.000 Hồng ngọc", new SubInput("Số bạn chọn", ANY));//????
    }

    public void input_Tai(Player pl) {
        createForm(pl, TAI_CLIENT, "Chọn số " + GameDuDoan.NAME_ITEM_CUOC + " đặt " + GameDuDoan.LON, new SubInput("Số " + GameDuDoan.NAME_ITEM_CUOC + " cược", ANY));//????
    }

    public void input_Xiu(Player pl) {
        createForm(pl, XIU_CLIENT, "Chọn số " + GameDuDoan.NAME_ITEM_CUOC + " đặt " + GameDuDoan.NHO, new SubInput("Số " + GameDuDoan.NAME_ITEM_CUOC + " cược", ANY));//????
    }

    public void chat_TaiXiu(Player pl) {
        createForm(pl, CHAT_TAI_XIU, "Nhập nội dung", new SubInput("Nội dung", ANY));
    }

    public void createFormTachItem(Player pl) {
        createForm(pl, TACH_ITEM, "Tách stack vật phẩm",
                new SubInput("ID vật phẩm cần tách", NUMERIC),
                new SubInput("Vị trí ô vật phẩm trong túi (0 là ô đầu tiên, 1 là ô tiếp theo...)", NUMERIC),
                new SubInput("Số lượng mỗi stack", NUMERIC),
                new SubInput("Số stack muốn tạo", NUMERIC));
    }

    public void createFormGopItem(Player pl) {
        createForm(pl, GOP_ITEM, "Gộp vật phẩm",
                new SubInput("ID vật phẩm cần gộp", NUMERIC));
    }

    public class SubInput {

        private String name;
        private byte typeInput;

        public SubInput(String name, byte typeInput) {
            this.name = name;
            this.typeInput = typeInput;
        }
    }

}
