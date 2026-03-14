package nro.services.func;

import nro.consts.*;
import nro.dialog.MenuDialog;
import nro.dialog.MenuRunable;
import nro.event.Event;
import nro.lib.RandomCollection;
import nro.manager.MiniPetManager;
import nro.manager.NamekBallManager;
import nro.manager.PetFollowManager;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.item.MinipetTemplate;
import nro.models.map.*;
import nro.models.map.dungeon.zones.ZSnakeRoad;
import nro.models.map.war.NamekBallWar;
import nro.models.player.Inventory;
import nro.models.player.MiniPet;
import nro.models.player.PetFollow;
import nro.models.player.Player;
import nro.models.skill.Skill;
import nro.server.Manager;
import nro.server.io.Message;
import nro.server.io.Session;
import nro.services.*;
import nro.utils.Log;
import nro.utils.SkillUtil;
import nro.utils.TimeUtil;
import nro.utils.Util;
import org.apache.log4j.Logger;

import java.util.Date;
import java.util.List;
import java.util.Random;
import nro.data.DataGame;
import nro.data.ItemData;
import nro.models.boss.BossManager;
import nro.models.map.ChungSinhBinhDang.ChungSinhBinhDangUseItem;

public class UseItem {

    private static final int ITEM_BOX_TO_BODY_OR_BAG = 0;
    private static final int ITEM_BAG_TO_BOX = 1;
    private static final int ITEM_BODY_TO_BOX = 3;
    private static final int ITEM_BAG_TO_BODY = 4;
    private static final int ITEM_BODY_TO_BAG = 5;
    private static final int ITEM_BAG_TO_PET_BODY = 6;
    private static final int ITEM_BODY_PET_TO_BAG = 7;

    private static final byte DO_USE_ITEM = 0;
    private static final byte DO_THROW_ITEM = 1;
    private static final byte ACCEPT_THROW_ITEM = 2;
    private static final byte ACCEPT_USE_ITEM = 3;

    private static UseItem instance;
    private static final Logger logger = Logger.getLogger(UseItem.class);

    private UseItem() {

    }

    public static UseItem gI() {
        if (instance == null) {
            instance = new UseItem();
        }
        return instance;
    }

    public void getItem(Session session, Message msg) {
        Player player = session.player;
        TransactionService.gI().cancelTrade(player);
        try {
            int type = msg.reader().readByte();
            int index = msg.reader().readByte();
            if (index == -1) {
                return;
            }
            switch (type) {
                case ITEM_BOX_TO_BODY_OR_BAG:
                    InventoryService.gI().itemBoxToBodyOrBag(player, index);
                    TaskService.gI().checkDoneTaskGetItemBox(player);
                    break;
                case ITEM_BAG_TO_BOX:
                    InventoryService.gI().itemBagToBox(player, index);
                    break;
                case ITEM_BODY_TO_BOX:
                    InventoryService.gI().itemBodyToBox(player, index);
                    break;
                case ITEM_BAG_TO_BODY:
                    InventoryService.gI().itemBagToBody(player, index);
                    break;
                case ITEM_BODY_TO_BAG:
                    InventoryService.gI().itemBodyToBag(player, index);
                    break;
                case ITEM_BAG_TO_PET_BODY:
                    InventoryService.gI().itemBagToPetBody(player, index);
                    break;
                case ITEM_BODY_PET_TO_BAG:
                    InventoryService.gI().itemPetBodyToBag(player, index);
                    break;
            }
            player.setClothes.setup();
            if (player.pet != null) {
                player.pet.setClothes.setup();
            }
            player.setClanMember();
            Service.getInstance().point(player);
        } catch (Exception e) {
            Log.error(UseItem.class, e);
        }
    }

    public void doItem(Player player, Message _msg) {
        TransactionService.gI().cancelTrade(player);
        Message msg;
        try {
            byte type = _msg.reader().readByte();
            int where = _msg.reader().readByte();
            int index = _msg.reader().readByte();
            switch (type) {
                case DO_USE_ITEM:
                    if (player != null && player.inventory != null) {
                        if (index != -1) {
                            if (index >= 0 && index < player.inventory.getCurrentBag().size()) {
                                Item item = player.inventory.getCurrentBag().get(index);
                                if (item.isNotNullItem()) {
                                    if (ItemData.IdMiniPet.contains((int) item.template.id)) {
                                        MinipetTemplate temp = MiniPetManager.gI().findByID(item.getId());
                                        if (temp == null) {
                                            System.err.println("khong tim thay minipet id: " + item.getId());
                                        }
                                        MiniPet.callMiniPet(player, item.template.id);
                                        InventoryService.gI().itemBagToBody(player, index);
                                        return;
                                    }
                                    if (item.template.type == 22) {
                                        msg = new Message(-43);
                                        msg.writer().writeByte(type);
                                        msg.writer().writeByte(where);
                                        msg.writer().writeByte(index);
                                        msg.writer().writeUTF("Bạn có muốn dùng " + player.inventory.getCurrentBag().get(index).template.name + "?");
                                        player.sendMessage(msg);
                                        msg.cleanup();
                                    } else if (item.template.type == 7) {
                                        msg = new Message(-43);
                                        msg.writer().writeByte(type);
                                        msg.writer().writeByte(where);
                                        msg.writer().writeByte(index);
                                        msg.writer().writeUTF("Bạn chắc chắn học " + player.inventory.getCurrentBag().get(index).template.name + "?");
                                        player.sendMessage(msg);
                                    } else if (player.isVersionAbove(220) && item.template.type == 23 || item.template.type == 24 || item.template.type == 11) {
                                        InventoryService.gI().itemBagToBody(player, index);
                                    } else if (item.template.id == 401) {
                                        msg = new Message(-43);
                                        msg.writer().writeByte(type);
                                        msg.writer().writeByte(where);
                                        msg.writer().writeByte(index);
                                        msg.writer().writeUTF("Sau khi đổi đệ sẽ mất toàn bộ trang bị trên người đệ tử nếu chưa tháo");
                                        player.sendMessage(msg);
                                    } else if (item.getType() == 72) {
                                        PetFollow pet = PetFollowManager.gI().findByID(item.getId());
                                        player.setPetFollow(pet);
                                        InventoryService.gI().itemBagToBody(player, index);
                                        PlayerService.gI().sendPetFollow(player);
                                    } else if (item.template.type == 74) {
                                        Service.getInstance().sendFoot(player, item.template.id);
                                        InventoryService.gI().itemBagToBody(player, index);
                                    } else if (item.template.type == 35) {
                                        InventoryService.gI().itemBagToBody(player, index);
                                    } else {
                                        useItem(player, item);
                                    }
                                }
                            }
                        } else {
                            InventoryService.gI().eatPea(player);
                        }
                    }
                    break;
                case DO_THROW_ITEM:
                    if (!(player.zone.map.mapId == 21 || player.zone.map.mapId == 22 || player.zone.map.mapId == 23)) {
                        Item item = null;
                        if (where == 0) {
                            if (index >= 0 && index < player.inventory.getCurrentBody().size()) {
                                item = player.inventory.getCurrentBody().get(index);
                            }
                        } else {
                            if (index >= 0 && index < player.inventory.getCurrentBag().size()) {
                                item = player.inventory.getCurrentBag().get(index);
                            }
                        }
                        if (item != null && item.isNotNullItem()) {
                            msg = new Message(-43);
                            msg.writer().writeByte(type);
                            msg.writer().writeByte(where);
                            msg.writer().writeByte(index);
                            msg.writer().writeUTF("Bạn chắc chắn muốn vứt " + item.template.name + "?");
                            player.sendMessage(msg);
                        }
                    } else {
                        Service.getInstance().sendThongBao(player, "Không thể thực hiện");
                    }
                    break;
                case ACCEPT_THROW_ITEM:
                    InventoryService.gI().throwItem(player, where, index);
                    break;
                case ACCEPT_USE_ITEM:
                    if (index >= 0 && index < player.inventory.getCurrentBag().size()) {
                        Item item = player.inventory.getCurrentBag().get(index);
                        if (item.isNotNullItem()) {
                            useItem(player, item);
                        }
                    }
                    break;
            }
        } catch (Exception e) {
            Log.error(UseItem.class, e);
        }
    }

    public void useSatellite(Player player, Item item) {
        Satellite satellite = null;
        if (player.zone != null) {
            int count = player.zone.getSatellites().size();
            if (count < 3) {
                switch (item.template.id) {
                    case ConstItem.VE_TINH_TRI_LUC:
                        satellite = new SatelliteMP(player.zone, ConstItem.VE_TINH_TRI_LUC, player.location.x, player.location.y, player);
                        break;

                    case ConstItem.VE_TINH_TRI_TUE:
                        satellite = new SatelliteExp(player.zone, ConstItem.VE_TINH_TRI_TUE, player.location.x, player.location.y, player);
                        break;

                    case ConstItem.VE_TINH_PHONG_THU:
                        satellite = new SatelliteDefense(player.zone, ConstItem.VE_TINH_PHONG_THU, player.location.x, player.location.y, player);
                        break;

                    case ConstItem.VE_TINH_SINH_LUC:
                        satellite = new SatelliteHP(player.zone, ConstItem.VE_TINH_SINH_LUC, player.location.x, player.location.y, player);
                        break;
                }
                if (satellite != null) {
                    InventoryService.gI().subQuantityItemsBag(player, item, 1);
                    Service.getInstance().dropItemMapForMe(player, satellite);
                    Service.getInstance().dropItemMap(player.zone, satellite);
                }
            } else {
                Service.getInstance().sendThongBaoOK(player, "Số lượng vệ tinh có thể đặt trong khu vực đã đạt mức tối đa.");
            }
        }
    }

    private void useItem(Player pl, Item item) {
        if (Event.isEvent() && Event.getInstance().useItem(pl, item)) {
            return;
        }
        if (item.template.strRequire <= pl.nPoint.power) {
            int type = item.getType();
            if (type == 6) {
                InventoryService.gI().eatPea(pl);
            } else if (type == 33) {
                RadaService.getInstance().useItemCard(pl, item);
            } else if (type == 5) {
                Service.getInstance().Send_Caitrang(pl);
            } else if (type == 22) {
                useSatellite(pl, item);
            } else if (type == 72) {
            } else if (type == 74) {
                Service.getInstance().sendFoot(pl, item.template.id);
            } else {
                switch (item.template.id) {
                    case 1798:
                        findNamekBall(pl, item);
                        break;
                    case 1799:
                        findNamekBallvip(pl, item);
                        break;
                    case 627:
                        capsule8thang3(pl, item);
                        break;
                    case ConstItem.CAPSULE_THOI_TRANG_30_NGAY:
                        capsuleThoiTrang(pl, item);
                        break;
                    case 1445:
                        openboxsukien(pl, item, ConstEvent.SU_KIEN_TET);
                        break;
                    case 570:
                        openWoodChest(pl, item);
                        break;
                    case 648:
                        openboxsukien(pl, item, 3);
                        break;
                    case 668:
                        hopQuaTanThu(pl, item);
                        break;
                    case 992:
                        if (TaskService.gI().getIdTask(pl) == ConstTask.TASK_32_0) {
                            TaskService.gI().doneTask(pl, ConstTask.TASK_32_0);
                        }
                        ChangeMapService.gI().goToPrimaryForest(pl);
                        break;
                    case 1483:
                        NpcService.gI().createMenuConMeo(pl, ConstNpc.VAO_MAP_NGOAI_VUC, -1,
                                "Bạn muốn Di chuyển đến mơi nào???",
                                "Map Hallowen", "Hành tinh\nBăng giá", "Map Địa Ngục");
                        break;
                    case 1444:
                        if (!pl.getSession().actived) {
                            Service.getInstance().sendThongBao(pl, "Vui lòng kích hoạt tài khoản để có thể sử dụng");
                            return;
                        }
                        Input.gI().createFormTangRuby(pl);
                        break;
                    case 1796:
                        if (!pl.getSession().actived) {
                            Service.getInstance().sendThongBao(pl, "Vui lòng kích hoạt tài khoản để có thể sử dụng");
                            return;
                        }
                        Input.gI().createFormTangCoin(pl);
                        break;
                    case 628: //phiếu cải trang hải tặc
                        openPhieuCaiTrangHaiTac(pl, item);
                        break;
                    case 1433: //Hop Qua Kich Hoat
                        openboxsukien(pl, item, 1);
                        break;
                    case 1441: //phiếu cải trang 20/10
                        openbox2010(pl, item);
                        break;
                    case 1442:
                        openboxsukien(pl, item, 2);
                        break;
                    case 457:
                        NpcService.gI().createMenuConMeo(pl, ConstNpc.DUNG_NHIEU_TV, 7710,
                                "|7|THỎI VÀNG\n"
                                + "|-1|Theo nguyện vọng góp ý từ các chiến binh, ta được Admin\n"
                                + "giao cho trọng trách hỗ trợ Sử dụng Thỏi vàng số lượng nhiều\n"
                                + "|0|Ngươi muốn Sử dụng bao nhiêu Thỏi vàng?",
                                "X1 Thỏi\n(500 Triệu)", "X5 Thỏi\n(2,5 Tỷ)", "X10 Thỏi\n(5 Tỷ)",
                                "X98 Thỏi\n(49 Tỷ)", "X198 Thỏi\n(99 Tỷ)");
                        return;
                    case 211: //nho tím
                    case 212: //nho xanh
                        eatGrapes(pl, item);
                        break;
                    case 2000:
                    case 2001:
                    case 2002:
                        openSkhThuong(pl, item);
                        break;
                    case 568: //cskb
                        openMabuEgg(pl, item);
                        break;
                    case 380: //cskb
                        openCSKB(pl, item);
                        break;
                    case 1548: //rương siêu anh hùng
                        RuongSieuAnhHung(pl, item);
                        break;
                    case 573: //capsule bạc
                        openCapsuleBac(pl, item);
                        break;
                    case ConstItem.TUI_VANG: //capsule bạc
                        tuivang(pl, item);
                        break;
                    case 574: //capsule vàng
                        openCapsuleVang(pl, item);
                        break;
                    case 2091:
                        ruongVip(pl, item);
                        break;
                    case 2092:
                        ruongThuong(pl, item);
                        break;
                    case 1801:
                        TuiMu(pl, item);
                        break;
                    case 1802:
                        TuiMuVip(pl, item);
                        break;
                    case 381: //cuồng nộ
                    case 382: //bổ huyết
                    case 383: //bổ khí
                    case 384: //giáp xên
                    case 385: //ẩn danh
                    case 379: //máy dò
                    case 1860: //vợt cá thường
                    case 1861: //vợt cá vip
                    case 663: //bánh pudding
                    case 664: //xúc xíc
                    case 665: //kem dâu
                    case 666: //mì ly
                    case 667: //sushi
                    case ConstItem.BANH_CHUNG_CHIN:
                    case ConstItem.BANH_TET_CHIN:
                    case ConstItem.CUONG_NO_2:
                    case ConstItem.BO_HUYET_2:
                    case ConstItem.GIAP_XEN_BO_HUNG_2:
                    case ConstItem.BO_KHI_2:
                    case 579:
                    case 1385:
                    case 899:
                    case 1317:
                    case 1201:
                    case 1386:
                    case 465:
                    case 466:
                    case 472:
                    case 473:
                        useItemTime(pl, item);
                        break;
                    case 694:
                        if (Manager.EVENT_SEVER == ConstEvent.SU_KIEN_HE) {
                            pl.itemTimesieucap.lastTimeTraiDua = System.currentTimeMillis();
                            pl.itemTimesieucap.isTraiDua = true;
                            ItemTimeService.gI().sendAllItemTime(pl);
                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                            InventoryService.gI().sendItemBags(pl);
                        } else {
                            Service.getInstance().sendThongBao(pl, "Đã hết sự kiện Hè. Bạn không thể sử dụng vật phẩm này");
                        }
                        break;
                    case 521: //tdlt
                        useTDLT(pl, item);
                        break;
                    case 1105://hop qua skh, item 2002 xd
                        UseItem.gI().Hopts(pl, item);
                        break;
                    case 1537://hộp skh hủy diệt
                        UseItem.gI().HopSKHHuyDiet(pl, item);
                        break;
                    case 1538://hộp skh hủy diệt
                        UseItem.gI().HopSKHThanLinh(pl, item);
                        break;
                    case 454: //bông tai
                        usePorata(pl);
                        break;
                    case 921: //bông tai
                        UseItem.gI().usePorata2(pl);
                        break;
                    case 1165: //bông tai
                        UseItem.gI().usePorata3(pl);
                        break;
                    case 1129: //bông tai
                        UseItem.gI().usePorata4(pl);
                        break;
                    case 1550: //bông tai cấp 5
                        UseItem.gI().usePorata5(pl);
                        break;
                    case 193: //gói 10 viên capsule
                        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                    case 194: //capsule đặc biệt
                        openCapsuleUI(pl);
                        break;
                    case 401: //đổi đệ tử
                        changePet(pl, item);
                        break;
                    case 402: //sách nâng chiêu 1 đệ tử
                    case 403: //sách nâng chiêu 2 đệ tử
                    case 404: //sách nâng chiêu 3 đệ tử
                    case 759: //sách nâng chiêu 4 đệ tử
                        upSkillPet(pl, item);
                        break;
                    case 1241: //đổi skill
                        doiskill4(pl, item);
                        break;
                    case 1459: //đổi đệ tử
                        changePetBerus(pl, item);
                        break;
                    case 1547: //đổi đệ tử
                        changePetFide(pl);
                        break;
                    case 1108: //đổi đệ tử
                        changePetBuu(pl, item);
                        break;
                    case 1559: //đổi đệ tử
                        changePetXen(pl);
                        break;
                    case 1359: //cần câu
                    case 1360:
                    case 1361:
                    case 1362:
                        Can_cau_ca(pl, item);
                        break;
                    case 1237: //pháp sư
                        openphapsu(pl, item);
                        break;
                    case 1525: //hộp sách tuyệt kĩ
                        OpenNguyenLieu(pl, item);
                        break;
                    case 1578: //rương hè thường
                        RuongHeThuong(pl, item);
                        break;
                    case 1579: //rương hè vip
                        RuongHeVip(pl, item);
                        break;
                    case 1526: //hộp cải trang Hit
                        OpenHit(pl, item);
                        break;
                    case 2006:
                        Input.gI().createFormChangeNameByItem(pl);
                        break;
                    case 1479: //cskb
                        RuongSaoPhaLe(pl, item);
                        break;
                    case 1769:
                        RuongSaoPhaLeDacBiet(pl, item);
                        break;
                    case 1781:
                        Ruongbuadevuong(pl, item);
                        break;
                    case 1795:
                        Hopanmade(pl, item);
                        break;
                    case 1334: //hộp đồ thần linh
                        hopthanlinh(pl, item);
                        break;
                    case 1460: //hộp đồ thần linh
                        hopHuyDiet(pl, item);
                        break;
                    case 1399: //rương skh vip
                        ruongskhVIP(pl, item);
                        break;
                    case 1407: //rương skh vip
                        RuongSkhThanhTon(pl, item);
                        break;
                    case 1296: //cskb
                        maydoboss(pl);
                        break;
                    case 1897: //cskb
                        maydobossvip(pl);
                        break;
                    case ConstItem.CAPSULE_TET_2022:
                        openCapsuleTet2022(pl, item);
                        break;
                    case 2072:
                        HopMuBeBa(pl, item);
                        break;
                    case 1788:
                        HopQua(pl, item);
                        break;
                    case 722:
                        capsuleHong(pl, item);
                        break;
                    case 1922:
                        kimDan(pl, item);
                        break;
                    case 1849: //Đậu thần xanh
                    case 1850: //Đậu thần tím
                    case 1851: //Đậu thần đỏ
                    case 1852: //Thẻ tăng sức đánh level 1
                    case 1853: //Thẻ tăng sức đánh level 2
                    case 1854: //Thẻ tăng sức đánh level 3
                    case 1855: //Thẻ tăng HP
                    case 1856: //Thẻ tăng KI
                    case 1857: //Thẻ gọi boss
                        ChungSinhBinhDangUseItem.gI().ChungSinhBinhDangUseItem(pl, item);
                        break;
                    default:
                        switch (item.template.type) {
                            case 7: //sách học, nâng skill
                                learnSkill(pl, item);
                                break;
                            case 17: //sách học, nâng skill
                                learnSkillBienHinh(pl, item);
                                break;
//                            case 12: //ngọc rồng các loại
//                                controllerCallRongThan(pl, item);
//                                break;
                            case 11: //item flag bag
                                useItemChangeFlagBag(pl, item);
                                break;
                        }
                }
            }
            InventoryService.gI().sendItemBags(pl);
        } else {
            Service.getInstance().sendThongBaoOK(pl, "Sức mạnh không đủ yêu cầu");
        }
    }

    private void Hopts(Player pl, Item item) {//hop qua do huy diet
        NpcService.gI().createMenuConMeo(pl, item.template.id, -1, "Chọn hành tinh của mày đi", "Set trái đất", "Set namec", "Set xayda", "Từ chổi");
    }

    private void HopSKHHuyDiet(Player pl, Item item) {//hop qua do huy diet
        NpcService.gI().createMenuConMeo(pl, item.template.id, -1, "Chọn hành tinh của mày đi", "Set trái đất", "Set namec", "Set xayda", "Từ chổi");
    }

    private void HopSKHThanLinh(Player pl, Item item) {//hop qua do huy diet
        NpcService.gI().createMenuConMeo(pl, item.template.id, -1, "Chọn hành tinh của mày đi", "Set trái đất", "Set namec", "Set xayda", "Từ chổi");
    }

    private void changePet(Player player, Item item) {
        if (InventoryService.gI().getCountEmptyBody(player.pet) == 12) {
            if (player.pet != null) {
                int gender = player.pet.gender + 1;
                if (gender > 2) {
                    gender = 0;
                }
                PetService.gI().changeNormalPet(player, gender);
                InventoryService.gI().subQuantityItemsBag(player, item, 1);
            } else {
                Service.getInstance().sendThongBao(player, "Không thể thực hiện");
            }
        } else {
            Service.getInstance().sendThongBao(player, "Vui lòng tháo hết đồ đệ tử");
        }
    }

    private void changePetBuu(Player player, Item item) {
        if (player.pet == null) {
            Service.getInstance().sendThongBao(player, "Yêu cầu có đệ tử");
            return;
        }
        if (InventoryService.gI().getCountEmptyBody(player.pet) == 12) {
            if (player.pet != null) {
                NpcService.gI().createMenuConMeo(player, ConstNpc.DE_TU_SON_TINH, 13429,
                        "Vui lòng Chọn Hành tinh Đệ Tử Bư Teen ( 30% ) !!!",
                        "Trái Đất", "Namec", "Xayda");
            } else {
                Service.getInstance().sendThongBao(player, "Yêu cầu có đệ tử");
            }
        } else {
            Service.getInstance().sendThongBao(player, "Vui lòng tháo hết đồ đệ tử");
        }
    }

    private void changePetBerus(Player player, Item item) {
        if (player.pet == null) {
            Service.getInstance().sendThongBao(player, "Yêu cầu có đệ tử");
            return;
        }
        if (InventoryService.gI().getCountEmptyBody(player.pet) == 12) {
            if (player.pet != null) {
                NpcService.gI().createMenuConMeo(player, ConstNpc.DE_TU_BERUS, 22531,
                        "Vui lòng Chọn Hành Tinh Đệ Chiến Thần !!!",
                        "Trái Đất", "Namec", "Xayda");
            } else {
                Service.getInstance().sendThongBao(player, "Yêu cầu có đệ tử");
            }
        } else {
            Service.getInstance().sendThongBao(player, "Vui lòng tháo hết đồ đệ tử");
        }
    }

    private void changePetFide(Player player) {
        if (player.pet == null) {
            Service.getInstance().sendThongBao(player, "Yêu cầu có đệ tử");
            return;
        }
        if (InventoryService.gI().getCountEmptyBody(player.pet) == 12) {
            if (player.pet != null) {
                NpcService.gI().createMenuConMeo(player, ConstNpc.DE_TU_FIDE, 12824,
                        "Vui lòng Chọn Hành tinh Đệ Tử Thần Hủy Diệt ( 60% ) !!!",
                        "Trái Đất", "Namec", "Xayda");
            } else {
                Service.getInstance().sendThongBao(player, "Yêu cầu có đệ tử");
            }
        } else {
            Service.getInstance().sendThongBao(player, "Vui lòng tháo hết đồ đệ tử");
        }
    }

    private void changePetXen(Player player) {
        if (player.pet == null) {
            Service.getInstance().sendThongBao(player, "Yêu cầu có đệ tử");
            return;
        }
        if (InventoryService.gI().getCountEmptyBody(player.pet) == 12) {
            if (player.pet != null) {
                NpcService.gI().createMenuConMeo(player, ConstNpc.DE_TU_XEN, 16405,
                        "Vui lòng Chọn Hành tinh Đệ Tử Thần Vũ Trụ!!!",
                        "Trái Đất", "Namec", "Xayda");
            } else {
                Service.getInstance().sendThongBao(player, "Yêu cầu có đệ tử");
            }
        } else {
            Service.getInstance().sendThongBao(player, "Vui lòng tháo hết đồ đệ tử");
        }
    }

    private void RuongHeThuong(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] nro = {16, 17, 18, 19, 20};
            short[] caitrang = {618, 875, 637, 636, 635};
            short[] thuong = {381, 382, 383, 384, 385};
            int ran = Util.nextInt(1, 100);
            int tileThuong = 30;
            int tileCT = 34;
            if (ran <= tileThuong) {
                Item it = ItemService.gI().createNewItem(thuong[(byte) Util.nextInt(0, thuong.length - 1)]);
                it.itemOptions.add(new ItemOption(73, 0));
                InventoryService.gI().addItemBag(pl, it, 1);
                Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + it.template.name);
            } else if (ran <= (tileThuong + tileCT)) {
                Item it = ItemService.gI().createNewItem(caitrang[(byte) Util.nextInt(0, caitrang.length - 1)]);
                it.itemOptions.add(new ItemOption(50, Util.nextInt(1, 40)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(1, 45)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(1, 45)));
                it.itemOptions.add(new ItemOption(101, Util.nextInt(1, 50)));
                it.itemOptions.add(new ItemOption(5, Util.nextInt(1, 5)));
                it.itemOptions.add(new ItemOption(14, Util.nextInt(1, 5)));
                it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 3)));
                it.itemOptions.add(new ItemOption(199, 1));
                InventoryService.gI().addItemBag(pl, it, 1);
                Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + it.template.name);
            } else {
                Item it = ItemService.gI().createNewItem(nro[(byte) Util.nextInt(0, nro.length - 1)]);
                it.itemOptions.add(new ItemOption(73, 1));
                InventoryService.gI().addItemBag(pl, it, 1);
                Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + it.template.name);
            }
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
            pl.topRuong++;
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void RuongHeVip(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] rac = {457, 1237, 1525};
            short[] nro = {15, 16, 17, 18, 19, 20};
            short[] caitrang = {1208, 1214, 883, 1207, 1098};
            short[] thuong = {1099, 1100, 1101, 1102};
            int ran = Util.nextInt(1, 100);
            int tileRac = 10;
            int tileThuong = 20;
            int tileCT = 30;
            if (ran <= tileRac) {
                Item it = ItemService.gI().createNewItem(rac[(byte) Util.nextInt(0, rac.length - 1)], 1);
                it.itemOptions.add(new ItemOption(73, 0));
                InventoryService.gI().addItemBag(pl, it, 1);
                Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + it.template.name);
            } else if (ran <= (tileRac + tileThuong)) {
                Item it = ItemService.gI().createNewItem(thuong[(byte) Util.nextInt(0, thuong.length - 1)]);
                it.itemOptions.add(new ItemOption(73, 1));
                InventoryService.gI().addItemBag(pl, it, 1);
                Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + it.template.name);
            } else if (ran <= (tileRac + tileThuong + tileCT)) {
                Item it = ItemService.gI().createNewItem(caitrang[(byte) Util.nextInt(0, caitrang.length - 1)]);
                it.itemOptions.add(new ItemOption(50, Util.nextInt(1, 50)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(1, 55)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(1, 55)));
                it.itemOptions.add(new ItemOption(101, Util.nextInt(1, 100)));
                it.itemOptions.add(new ItemOption(5, Util.nextInt(1, 10)));
                it.itemOptions.add(new ItemOption(14, Util.nextInt(1, 5)));
                if (Util.isTrue(999, 1000)) {
                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 3)));
                    it.itemOptions.add(new ItemOption(199, 1));
                }
                InventoryService.gI().addItemBag(pl, it, 1);
                Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + it.template.name);
            } else {
                Item it = ItemService.gI().createNewItem(nro[(byte) Util.nextInt(0, nro.length - 1)]);
                it.itemOptions.add(new ItemOption(73, 0));
                InventoryService.gI().addItemBag(pl, it, 1);
                Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + it.template.name);
            }
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
            pl.topRuong += 2;
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void OpenNguyenLieu(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] thuong = {1506, 1507};
            short trangsach = 1516;
            short bua = 1508;
            byte index = (byte) Util.nextInt(0, thuong.length - 1);
            if (Util.isTrue(60, 100)) {
                Item it = ItemService.gI().createNewItem(trangsach, 50);
                it.itemOptions.add(new ItemOption(73, 0));
                InventoryService.gI().addItemBag(pl, it, 1);
                Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + it.template.name);
            } else if (Util.isTrue(15, 100)) {
                Item it = ItemService.gI().createNewItem(bua);
                it.itemOptions.add(new ItemOption(73, 0));
                InventoryService.gI().addItemBag(pl, it, 1);
                Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + it.template.name);
            } else {
                Item it = ItemService.gI().createNewItem(thuong[index]);
                it.itemOptions.add(new ItemOption(73, 0));
                InventoryService.gI().addItemBag(pl, it, 1);
                Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + it.template.name);
            }
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private static final int RUBY_MIN = 20_000;
    private static final int RUBY_MAX = 100_000;

    private void capsuleHong(Player pl, Item item) {
        try {
            if (InventoryService.gI().getCountEmptyBag(pl) == 0) {
                Service.getInstance().sendThongBao(pl, "Bạn phải có ít nhất 1 ô trống hành trang");
                return;
            }

            if (pl.inventory.ruby >= Inventory.LIMIT_RUBY) {
                Service.getInstance().sendThongBao(pl, "Bạn cần tiêu bớt ngọc hồng đi");
                return;
            }

            if (item != null) {
                int rubyNhanDuoc = Util.nextInt(RUBY_MIN, RUBY_MAX + 1);
                pl.inventory.addRuby(rubyNhanDuoc);
                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + rubyNhanDuoc + " ngọc hồng");
                Service.getInstance().sendMoney(pl);
                InventoryService.gI().sendItemBags(pl);
            } else {
                Service.getInstance().sendThongBao(pl, "Không tìm thấy túi hồng ngọc");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void kimDan(Player pl, Item item) {
        if (item != null) {
            if (!pl.isTuTien) {
                Service.getInstance().sendThongBao(pl, "Mở tính năng Tu Tiên để sử dụng vật phẩm");
                return;
            }
            int bonus = Util.nextInt(5, 10);
            pl.bonusTuTien += bonus;
            Service.getInstance().point(pl);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + bonus + " Chỉ số Bonus");
            InventoryService.gI().sendItemBags(pl);
        } else {
            Service.getInstance().sendThongBao(pl, "Không tìm thấy vật phẩm");
        }
    }

    private void HopQua(Player pl, Item item) {
        int[][] itemsThem = {
            {457, 100}, // thỏi vàng
            {1296, 100}, // thỏi vàng
            {ConstItem.XU_DAC_BIET, 100}, // thỏi vàng
            {16, 100}, // máy dò
            {77, 1000000}
        };
        int soLuongItemCanThem = itemsThem.length;
        if (InventoryService.gI().getCountEmptyBag(pl) >= soLuongItemCanThem) {
            for (int[] itemData : itemsThem) {
                short itemId = (short) itemData[0];
                int quantity = itemData[1];

                Item itemThem = ItemService.gI().createNewItem(itemId);
                itemThem.itemOptions.add(new ItemOption(30, 1));
                itemThem.quantity = quantity;
                InventoryService.gI().addItemBag(pl, itemThem, 1);
                Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + itemThem.template.name + " x" + quantity);
            }
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang không đủ chỗ trống để nhận quà");
        }
    }

    private void HopMuBeBa(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] petMu = {2074, 2075, 2076, 2077};
            byte index = (byte) Util.nextInt(0, petMu.length - 1);

            Item it = ItemService.gI().createNewItem(petMu[index]);
            it.itemOptions.add(new ItemOption(50, Util.nextInt(1, 100)));
            it.itemOptions.add(new ItemOption(77, Util.nextInt(1, 110)));
            it.itemOptions.add(new ItemOption(103, Util.nextInt(1, 110)));
            it.itemOptions.add(new ItemOption(5, Util.nextInt(1, 30)));
            it.itemOptions.add(new ItemOption(14, Util.nextInt(1, 10)));
            it.itemOptions.add(new ItemOption(30, 1));

            InventoryService.gI().addItemBag(pl, it, 1);
            Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + it.template.name);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void OpenHit(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] thuong = {16, 15};
            byte index = (byte) Util.nextInt(0, thuong.length - 1);
            if (Util.isTrue(50, 100)) {
                Item it = ItemService.gI().createNewItem(thuong[index]);
                it.itemOptions.add(new ItemOption(73, 0));
                InventoryService.gI().addItemBag(pl, it, 1);
                Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + it.template.name);
            } else {
                Item it = ItemService.gI().createNewItem((short) ConstItem.HIT_SAD_BOY);
                if (Util.isTrue(30, 100)) {
                    it.itemOptions.add(new ItemOption(5, Util.nextInt(5, 60)));
                } else {
                    if (Util.isTrue(60, 100)) {
                        it.itemOptions.add(new ItemOption(249, 1));
                        it.itemOptions.add(new ItemOption(5, Util.nextInt(5, 60)));
                    } else {
                        it.itemOptions.add(new ItemOption(249, 1));
                        it.itemOptions.add(new ItemOption(5, Util.nextInt(5, 60)));
                    }
                }
                it.itemOptions.add(new ItemOption(50, Util.nextInt(1, 10)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(1, 10)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(1, 10)));
                it.itemOptions.add(new ItemOption(101, Util.nextInt(100, 1000)));
                it.itemOptions.add(new ItemOption(14, Util.nextInt(1, 5)));
                it.itemOptions.add(new ItemOption(106, 1));
                it.itemOptions.add(new ItemOption(30, 1));
                if (Util.isTrue(90, 100)) {
                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 3)));
                } else {
                    it.itemOptions.add(new ItemOption(73, 1));
                }
                InventoryService.gI().addItemBag(pl, it, 1);
                Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + it.template.name);
            }
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void Can_cau_ca(Player pl, Item item) {
        if (pl.zone.map.mapId == 174) {
            int idcancau = item.template.id;
            Item moi = null;
            short[] ConCa = {1364, 1365, 1366, 1367, 1368, 1369, 1370, 1371};
            byte random = (byte) Util.nextInt(0, ConCa.length - 1);
            try {
                moi = InventoryService.gI().findItemBagByTemp(pl, 1363);
            } catch (Exception e) {
            }
            if (pl.useCanCau == false) {
                if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
                    pl.timeCauCa = 5000;
                    pl.iconCancau = item.template.iconID;
                    pl.useCanCau = true;
                    pl.lasttimeCanCau = System.currentTimeMillis();
                    ItemTimeService.gI().sendAllItemTime(pl);
                    if (moi != null) {
                        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                        InventoryService.gI().subQuantityItemsBag(pl, moi, 1);
                        Thread thread = new Thread(() -> {
                            try {
                                Thread.sleep(pl.timeCauCa); //time câu cá
                                if (idcancau == 1359 ? Util.isTrue(35, 100) : idcancau == 1360 ? Util.isTrue(45, 100) : idcancau == 1361 ? Util.isTrue(55, 100) : Util.isTrue(70, 100)) {
                                    Item it = ItemService.gI().createNewItem(ConCa[random]);
                                    if (idcancau == 1362) {
                                        it.quantity = 2;
                                    }
                                    it.itemOptions.add(new ItemOption(73, 0));
                                    InventoryService.gI().addItemBag(pl, it, 1);
                                    InventoryService.gI().sendItemBags(pl);
                                    Service.getInstance().sendThongBao(pl, "|4|Bạn đã nhận được " + it.template.name);
                                } else {
                                    Service.getInstance().sendThongBao(pl, "|3|Cá khôn quá chạy mất tiêu gòi");
                                }
                            } catch (Exception e) {
                            }
                        });
                        thread.start();
                    } else {
                        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                        Thread thread = new Thread(() -> {
                            try {
                                Thread.sleep(pl.timeCauCa);
                                if (idcancau == 1359 ? Util.isTrue(15, 100) : idcancau == 1360 ? Util.isTrue(25, 100) : idcancau == 1361 ? Util.isTrue(35, 100) : Util.isTrue(50, 100)) {
                                    Item it = ItemService.gI().createNewItem(ConCa[random]);
                                    if (idcancau == 1362) {
                                        it.quantity = 2;
                                    }
                                    it.itemOptions.add(new ItemOption(73, 0));
                                    InventoryService.gI().addItemBag(pl, it, 1);
                                    InventoryService.gI().sendItemBags(pl);
                                    Service.getInstance().sendThongBao(pl, "|4|Bạn đã nhận được " + it.template.name);
                                } else {
                                    Service.getInstance().sendThongBao(pl, "|3|Cá khôn quá chạy mất tiêu gòi");
                                }
                            } catch (Exception e) {
                            }
                        });
                        thread.start();
                    }
                } else {
                    Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
                }
            } else {
                Service.getInstance().sendThongBao(pl, "Từ từ thôi");
            }
        } else {
            Service.getInstance().sendThongBao(pl, "|7|Chỉ có thể câu cá tại map Câu cá");
        }
    }

    private void openphapsu(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] manh = {1232, 1233, 1234};
            short da = 1235;
            short bua = 1236;
            short[] rac = {1099, 1201};
            byte index = (byte) Util.nextInt(0, manh.length - 1);
            byte index2 = (byte) Util.nextInt(0, rac.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            if (Util.isTrue(35, 100)) {
                Item it = ItemService.gI().createNewItem(rac[index2]);
                it.itemOptions.add(new ItemOption(73, 0));
                InventoryService.gI().addItemBag(pl, it, 1);
                icon[1] = it.template.iconID;
                Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + it.template.name);
            } else if (Util.isTrue(13, 100)) {
                Item it = ItemService.gI().createNewItem(da);
                it.itemOptions.add(new ItemOption(73, 0));
                InventoryService.gI().addItemBag(pl, it, 1);
                icon[1] = it.template.iconID;
                Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + it.template.name);
            } else if (Util.isTrue(3, 100)) {
                Item it = ItemService.gI().createNewItem(bua);
                it.itemOptions.add(new ItemOption(73, 0));
                InventoryService.gI().addItemBag(pl, it, 1);
                icon[1] = it.template.iconID;
                Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + it.template.name);
            } else {
                Item it = ItemService.gI().createNewItem(manh[index]);
                it.itemOptions.add(new ItemOption(73, 0));
                InventoryService.gI().addItemBag(pl, it, 1);
                icon[1] = it.template.iconID;
                Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + it.template.name);
            }
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void RuongSaoPhaLe(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] manh = {1480, 1481, 1482};
            byte index = (byte) Util.nextInt(0, manh.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item it = ItemService.gI().createNewItem(manh[index]);
            switch (it.template.id) {
                case 1480:
                    it.itemOptions.add(new ItemOption(77, 7));
                    break;
                case 1481:
                    it.itemOptions.add(new ItemOption(103, 7));
                    break;
                case 1482:
                    it.itemOptions.add(new ItemOption(50, 5));
                    break;
                default:
                    break;
            }
            InventoryService.gI().addItemBag(pl, it, 1);
            icon[1] = it.template.iconID;
            Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + it.template.name);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void RuongSaoPhaLeDacBiet(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] manh = {1770, 1771, 1772};
            byte index = (byte) Util.nextInt(0, manh.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item it = ItemService.gI().createNewItem(manh[index]);
            switch (it.template.id) {
                case 1770:
                    it.itemOptions.add(new ItemOption(77, 15));
                    break;
                case 1771:
                    it.itemOptions.add(new ItemOption(103, 15));
                    break;
                case 1772:
                    it.itemOptions.add(new ItemOption(50, 10));
                    break;
                default:
                    break;
            }
            InventoryService.gI().addItemBag(pl, it, 1);
            icon[1] = it.template.iconID;
            Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + it.template.name);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void Ruongbuadevuong(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] manh = {1774, 1775, 1776};
            byte index = (byte) Util.nextInt(0, manh.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item it = ItemService.gI().createNewItem(manh[index]);
            switch (it.template.id) {
                case 1774:
                    it.itemOptions.add(new ItemOption(77, 30));
                    break;
                case 1775:
                    it.itemOptions.add(new ItemOption(103, 30));
                    break;
                case 1776:
                    it.itemOptions.add(new ItemOption(50, 20));
                    break;
                default:
                    break;
            }
            InventoryService.gI().addItemBag(pl, it, 1);
            icon[1] = it.template.iconID;
            Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + it.template.name);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void Hopanmade(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] manh = {1792, 1793, 1794};
            byte index = (byte) Util.nextInt(0, manh.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            Item it = ItemService.gI().createNewItem(manh[index]);
            switch (it.template.id) {
                case 1792:
                    it.itemOptions.add(new ItemOption(77, 70));
                    break;
                case 1793:
                    it.itemOptions.add(new ItemOption(103, 70));
                    break;
                case 1794:
                    it.itemOptions.add(new ItemOption(50, 50));
                    break;
                default:
                    break;
            }
            InventoryService.gI().addItemBag(pl, it, 1);
            icon[1] = it.template.iconID;
            Service.getInstance().sendThongBao(pl, "Bạn đã nhận được " + it.template.name);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    public void hopthanlinh(Player player, Item item) {
        byte randomDo = (byte) new Random().nextInt(Manager.itemIds_TL.length);
        Item thanlinh = Util.ratiItemTL(Manager.itemIds_TL[randomDo]);
        short[] icon = new short[2];
        icon[0] = item.template.iconID;
        if (InventoryService.gI().getCountEmptyBag(player) > 1) {
            thanlinh.itemOptions.add(new ItemOption(30, 1));
            InventoryService.gI().addItemBag(player, thanlinh, 1);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được " + thanlinh.template.name);
            InventoryService.gI().subQuantityItemsBag(player, item, 1);
            icon[1] = thanlinh.template.iconID;
            InventoryService.gI().sendItemBags(player);
            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
        }
    }

    public void hopHuyDiet(Player player, Item item) {
        byte randomDo = (byte) new Random().nextInt(Manager.itemIds_TL.length);
        Item thanlinh = Util.ratiItemHuyDiet(Manager.itemIds_HuyDiet[randomDo]);
        short[] icon = new short[2];
        icon[0] = item.template.iconID;
        if (InventoryService.gI().getCountEmptyBag(player) > 1) {
            thanlinh.itemOptions.add(new ItemOption(30, 1));
            InventoryService.gI().addItemBag(player, thanlinh, 1);
            Service.getInstance().sendThongBao(player, "Bạn đã nhận được " + thanlinh.template.name);
            InventoryService.gI().subQuantityItemsBag(player, item, 1);
            icon[1] = thanlinh.template.iconID;
            InventoryService.gI().sendItemBags(player);
            CombineServiceNew.gI().sendEffectOpenItem(player, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(player, "Bạn phải có ít nhất 1 ô trống hành trang");
        }
    }

    private void ruongskhVIP(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short itemId;
            int[] idhender = new int[]{0, 1, 2, 3, 4};
            int idr = new Random().nextInt(idhender.length);
            if (pl.gender == 3 || idr == 4) {
                itemId = Manager.radaSKHVip[Util.nextInt(0, 5)];
//                if (Util.isTrue(1, (int) 100)) {
//                    itemId = Manager.radaSKHVip[6];
//                }
            } else {
                itemId = Manager.doSKHVip[pl.gender][idr][Util.nextInt(0, 5)];
//                if (Util.isTrue(1, 100)) {
//                    itemId = Manager.doSKHVip[pl.gender][idr][6];
//                }
            }
            int skhId = ItemService.gI().randomSKHId(pl.gender);
            Item items;
            if (new Item(itemId).isDTL()) {
                items = Util.ratiItemTL(itemId);
                items.itemOptions.add(new ItemOption(skhId, 1));
                items.itemOptions.add(new ItemOption(ItemService.gI().optionIdSKH(skhId), 1));
                items.itemOptions.remove(items.itemOptions.stream().filter(itemOption -> itemOption.optionTemplate.id == 21).findFirst().get());
                items.itemOptions.add(new ItemOption(21, 15));
                items.itemOptions.add(new ItemOption(30, 1));
            } else {
                items = ItemService.gI().itemSKH(itemId, skhId);
                items.itemOptions.add(new ItemOption(107, 3));
            }
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().addItemBag(pl, items, 1);
            InventoryService.gI().sendItemBags(pl);
            Service.getInstance().sendThongBao(pl, "|1| Bạn nhận được " + items.template.name);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void RuongSkhThanhTon(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short itemId;
            itemId = Manager.DoThanhTon[Util.nextInt(0, 4)];
            int skhId = ItemService.gI().randomSKHHoangKim(pl.gender);
            Item items;
            if (new Item(itemId).isThanhTon()) {
                items = Util.ratiItemThanhTon(itemId);
                items.itemOptions.add(new ItemOption(skhId, 1));
                items.itemOptions.add(new ItemOption(ItemService.gI().optionIdSKHHoangKim(skhId), 1));
                items.itemOptions.remove(items.itemOptions.stream().filter(itemOption -> itemOption.optionTemplate.id == 21).findFirst().get());
                items.itemOptions.add(new ItemOption(21, 200));
                items.itemOptions.add(new ItemOption(30, 1));
            } else {
                items = ItemService.gI().itemSKH(itemId, skhId);
            }
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().addItemBag(pl, items, 1);
            InventoryService.gI().sendItemBags(pl);
            Service.getInstance().sendThongBao(pl, "|1| Bạn nhận được " + items.template.name);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    public boolean maydoboss(Player pl) {
        try {
            Item maydo = InventoryService.gI().findItemBagByTemp(pl, 1296);
            if (maydo != null) {
                BossManager.gI().showListBossMember(pl);
                InventoryService.gI().subQuantityItemsBag(pl, maydo, 1);
                InventoryService.gI().sendItemBags(pl);
                return true;
            } else {
                Service.getInstance().sendThongBao(pl, "|7|Bạn cần Máy dò Boss để sử dụng!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean maydobossvip(Player pl) {
        try {
            Item maydoVip = InventoryService.gI().findItemBagByTemp(pl, 1897);
            if (maydoVip != null) {
                BossManager.gI().showListBossMemberVIP(pl);
                InventoryService.gI().subQuantityItemsBag(pl, maydoVip, 1);
                InventoryService.gI().sendItemBags(pl);
                return true;
            } else {
                Service.getInstance().sendThongBao(pl, "|7|Bạn cần Máy dò Boss VIP để sử dụng!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void findNamekBallvip(Player pl, Item item) {
        List<NamekBall> balls = NamekBallManager.gI().getList();
        if (balls.isEmpty()) {
            Service.getInstance().sendThongBaoOK(pl, "[Ngọc Vũ Trụ Đang Tái Tạo] Vui lòng chờ 10p kể từ lúc ước!");
            InventoryService.gI().sendItemBags(pl);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            return;
        }
        StringBuffer sb = new StringBuffer();
        for (NamekBall namekBall : balls) {
            Map m = namekBall.zone.map;
            sb.append(namekBall.getIndex() + 1).append(" Sao: ").append(m.mapName).append(namekBall.getHolderName() == null ? "" : " - " + namekBall.getHolderName()).append("\n");
        }
        final int star = Util.nextInt(0, 6);
        final NamekBall ball = NamekBallManager.gI().findByIndex(star);
        final Inventory inventory = pl.inventory;
        MenuDialog menu = new MenuDialog(sb.toString(), new String[]{"Đến ngay\nViên " + (star + 1) + " Sao\n 50tr Vàng", "Đến ngay\nViên " + (star + 1) + " Sao\n 5 Hồng ngọc"}, new MenuRunable() {
            @Override
            public void run() {
                switch (getIndexSelected()) {
                    case 0:
                        if (inventory.gold < 50000000) {
                            Service.getInstance().sendThongBao(pl, "Không đủ tiền");
                            return;
                        }
                        inventory.subGold(50000000);
                        ChangeMapService.gI().changeMap(pl, ball.zone, ball.x, ball.y);
                        break;
                    case 1:
                        if (inventory.ruby < 5) {
                            Service.getInstance().sendThongBao(pl, "Không đủ tiền");
                            return;
                        }
                        inventory.subRuby(5);
                        ChangeMapService.gI().changeMap(pl, ball.zone, ball.x, ball.y);
                        break;
                }
                if (pl.isHoldNamecBall) {
                    NamekBallWar.gI().dropBall(pl);
                }
                Service.getInstance().sendMoney(pl);
            }
        });
        menu.show(pl);
        InventoryService.gI().sendItemBags(pl);
        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
    }

    private void findNamekBall(Player pl, Item item) {
        if (NamekBallManager.gI().getList().isEmpty()) {
            Service.getInstance().sendThongBaoOK(pl, "[Ngọc Vũ Trụ Đang Tái Tạo] Vui lòng chờ 10p kể từ lúc ước!");
            InventoryService.gI().sendItemBags(pl);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1); // trừ vật phẩm dùng để tìm
            return;
        }
        final int star = Util.nextInt(0, 6); // random từ 0 đến 6 -> tương ứng 1 đến 7 sao
        final NamekBall ball = NamekBallManager.gI().findByIndex(star);
        Map m = ball.zone.map;
        StringBuilder sb = new StringBuilder();
        sb.append((ball.getIndex() + 1)).append(" Sao: ").append(m.mapName);

        if (ball.getHolderName() != null) {
            sb.append(" - ").append(ball.getHolderName());
        }

        MenuDialog menu = new MenuDialog(sb.toString(), new String[]{"Đóng"}, new MenuRunable() {
            @Override
            public void run() {
                if (pl.isHoldNamecBall) {
                    NamekBallWar.gI().dropBall(pl);
                }
                Service.getInstance().sendMoney(pl);
            }
        });

        menu.show(pl);
        InventoryService.gI().sendItemBags(pl);
        InventoryService.gI().subQuantityItemsBag(pl, item, 1); // trừ vật phẩm dùng để tìm
    }

    private void capsuleThoiTrang(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            Item it = ItemService.gI().createNewItem((short) Util.nextInt(ConstItem.CAI_TRANG_GOKU_THOI_TRANG, ConstItem.CAI_TRANG_CA_DIC_THOI_TRANG));
            it.itemOptions.add(new ItemOption(50, 30));
            it.itemOptions.add(new ItemOption(77, 30));
            it.itemOptions.add(new ItemOption(103, 30));
            it.itemOptions.add(new ItemOption(106, 0));
            InventoryService.gI().addItemBag(pl, it, 0);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
            short icon1 = item.template.iconID;
            short icon2 = it.template.iconID;
            CombineServiceNew.gI().sendEffectOpenItem(pl, icon1, icon2);
        } else {
            Service.getInstance().sendThongBao(pl, "Hãy chừa 1 ô trống để mở.");
        }

    }

    private void openCapsuleTet2022(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) == 0) {
            Service.getInstance().sendThongBao(pl, "Hãy chừa 1 ô trống để mở.");
            return;
        }
        RandomCollection<Integer> rdItemID = new RandomCollection<>();
        rdItemID.add(1, ConstItem.PHAO_HOA);
        rdItemID.add(1, ConstItem.CAY_TRUC);
        rdItemID.add(1, ConstItem.NON_HO_VANG);
        if (pl.gender == 0) {
            rdItemID.add(1, ConstItem.NON_TRAU_MAY_MAN);
            rdItemID.add(1, ConstItem.NON_CHUOT_MAY_MAN);
        } else if (pl.gender == 1) {
            rdItemID.add(1, ConstItem.NON_TRAU_MAY_MAN_847);
            rdItemID.add(1, ConstItem.NON_CHUOT_MAY_MAN_755);
        } else {
            rdItemID.add(1, ConstItem.NON_TRAU_MAY_MAN_848);
            rdItemID.add(1, ConstItem.NON_CHUOT_MAY_MAN_756);
        }
        rdItemID.add(1, ConstItem.CAI_TRANG_HO_VANG);
        rdItemID.add(1, ConstItem.HO_MAP_VANG);
        rdItemID.add(2, ConstItem.DA_LUC_BAO);
        rdItemID.add(2, ConstItem.DA_SAPHIA);
        rdItemID.add(2, ConstItem.DA_TITAN);
        rdItemID.add(2, ConstItem.DA_THACH_ANH_TIM);
        rdItemID.add(2, ConstItem.DA_RUBY);
        rdItemID.add(3, ConstItem.VANG_190);
        int itemID = rdItemID.next();
        Item newItem = ItemService.gI().createNewItem((short) itemID);
        if (newItem.template.type == 9) {
            newItem.quantity = Util.nextInt(10, 50) * 1000000;
        } else if (newItem.template.type == 14 || newItem.template.type == 30) {
            newItem.quantity = 10;
        } else {
            switch (itemID) {
                case ConstItem.CAY_TRUC: {
                    RandomCollection<ItemOption> rdOption = new RandomCollection<>();
                    rdOption.add(2, new ItemOption(77, 15));//%hp
                    rdOption.add(2, new ItemOption(103, 15));//%hp
                    rdOption.add(1, new ItemOption(50, 15));//%hp
                    newItem.itemOptions.add(rdOption.next());
                }
                break;

                case ConstItem.HO_MAP_VANG: {
                    newItem.itemOptions.add(new ItemOption(77, Util.nextInt(10, 20)));
                    newItem.itemOptions.add(new ItemOption(103, Util.nextInt(10, 20)));
                    newItem.itemOptions.add(new ItemOption(50, Util.nextInt(10, 20)));
                }
                break;

                case ConstItem.NON_HO_VANG:
                case ConstItem.CAI_TRANG_HO_VANG:
                case ConstItem.NON_TRAU_MAY_MAN:
                case ConstItem.NON_TRAU_MAY_MAN_847:
                case ConstItem.NON_TRAU_MAY_MAN_848:
                case ConstItem.NON_CHUOT_MAY_MAN:
                case ConstItem.NON_CHUOT_MAY_MAN_755:
                case ConstItem.NON_CHUOT_MAY_MAN_756:
                    newItem.itemOptions.add(new ItemOption(77, 30));
                    newItem.itemOptions.add(new ItemOption(103, 30));
                    newItem.itemOptions.add(new ItemOption(50, 30));
                    break;
            }
            RandomCollection<Integer> rdDay = new RandomCollection<>();
            rdDay.add(6, 3);
            rdDay.add(3, 7);
            rdDay.add(1, 15);
            int day = rdDay.next();
            newItem.itemOptions.add(new ItemOption(93, day));
        }
        short icon1 = item.template.iconID;
        short icon2 = newItem.template.iconID;
        if (newItem.template.type == 9) {
            Service.getInstance().sendThongBao(pl, "Bạn nhận được " + Util.numberToMoney(newItem.quantity) + " " + newItem.template.name);
        } else if (newItem.quantity == 1) {
            Service.getInstance().sendThongBao(pl, "Bạn nhận được " + newItem.template.name);
        } else {
            Service.getInstance().sendThongBao(pl, "Bạn nhận được x" + newItem.quantity + " " + newItem.template.name);
        }
        CombineServiceNew.gI().sendEffectOpenItem(pl, icon1, icon2);
        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
        InventoryService.gI().addItemBag(pl, newItem, 99);
        InventoryService.gI().sendItemBags(pl);
    }

    public short[] selectItemByRate(short[] itemIds, int[] rates) {
        int totalRate = 0;
        for (int rate : rates) {
            totalRate += rate;
        }
        // Chuẩn hóa tỷ lệ nếu tổng > 100
        int[] normalizedRates = rates;
        if (totalRate > 100) {
            normalizedRates = new int[rates.length];
            for (int i = 0; i < rates.length; i++) {
                normalizedRates[i] = (int) Math.round((double) rates[i] * 100 / totalRate);
            }
            int normalizedSum = 0;
            for (int i = 0; i < normalizedRates.length - 1; i++) {
                normalizedSum += normalizedRates[i];
            }
            normalizedRates[normalizedRates.length - 1] = 100 - normalizedSum;
        }
        int randomRate = Util.nextInt(1, 100);
        short selectedItemId = 0;
        short selectedIndex = -1;
        int cumulativeRate = 0;
        for (int i = 0; i < itemIds.length; i++) {
            cumulativeRate += normalizedRates[i];
            if (randomRate <= cumulativeRate) {
                selectedItemId = itemIds[i];
                selectedIndex = (short) i;
                break;
            }
        }
        if (selectedItemId == 0) { // trường hợp quay ko ra 
            selectedItemId = itemIds[itemIds.length - 1]; // trả về id item cuối
            selectedIndex = 1; // trả về số lượng 1 cho item đó
        }
        return new short[]{selectedItemId, selectedIndex};
    }

    private void openWoodChest(Player pl, Item item) {
        boolean hasKey = InventoryService.gI().haveItemBag(pl, 1611);
        int time = (int) TimeUtil.diffDate(new Date(), new Date(item.createTime), TimeUtil.DAY);
        if (time != 0 || hasKey) {
            if (hasKey) {
                InventoryService.gI().subQuantityItemsBag(pl, InventoryService.gI().findItemBagByTemp(pl, 1611), 1);
            }
            int param = item.itemOptions.get(0).param;
            switch (param) {
                case 1: {
                    short[] itemIds = new short[]{1400, 1237, 987, 1525, 674}; //id item
                    int[] rates = new int[]{10, 15, 25, 25, 25}; // Tổng phải = 100
                    int[] quantities = new int[]{20, 5, 2, 10, 20}; // Số lượng tương ứng cho từng item

                    short[] selection = selectItemByRate(itemIds, rates);
                    short selectedItemId = selection[0];
                    int selectedIndex = selection[1];

                    Item Itemsc = ItemService.gI().createNewItem(selectedItemId);
                    Itemsc.itemOptions.add(new ItemOption(73, 0));
                    Itemsc.itemOptions.add(new ItemOption(30, 1));
                    Itemsc.quantity = quantities[selectedIndex];

                    InventoryService.gI().addItemBag(pl, Itemsc, 0);
                    NpcService.gI().createMenuConMeo(pl, ConstNpc.RUONG_GO, -1,
                            "Bạn Đã Nhận Được " + Itemsc.template.name + "\n"
                            + "SL: " + quantities[selectedIndex] + "\n",
                            "Đồng ý");
                    break;
                }
                case 2: {
                    short[] itemIds = new short[]{1400, 1237, 987, 1525, 674}; //id item
                    int[] rates = new int[]{10, 15, 25, 25, 25}; // Tổng phải = 100
                    int[] quantities = new int[]{20, 5, 2, 10, 20}; // Số lượng tương ứng cho từng item

                    short[] selection = selectItemByRate(itemIds, rates);
                    short selectedItemId = selection[0];
                    int selectedIndex = selection[1];

                    Item Itemsc = ItemService.gI().createNewItem(selectedItemId);
                    Itemsc.itemOptions.add(new ItemOption(73, 0));
                    Itemsc.itemOptions.add(new ItemOption(30, 1));
                    Itemsc.quantity = quantities[selectedIndex];

                    InventoryService.gI().addItemBag(pl, Itemsc, 0);
                    NpcService.gI().createMenuConMeo(pl, ConstNpc.RUONG_GO, -1,
                            "Bạn Đã Nhận Được " + Itemsc.template.name + "\n"
                            + "SL: " + quantities[selectedIndex] + "\n",
                            "Đồng ý");
                    break;
                }
                case 3: {
                    short[] itemIds = new short[]{1400, 1237, 987, 1525, 674}; //id item
                    int[] rates = new int[]{10, 15, 25, 25, 25}; // Tổng phải = 100
                    int[] quantities = new int[]{20, 5, 2, 10, 20}; // Số lượng tương ứng cho từng item

                    short[] selection = selectItemByRate(itemIds, rates);
                    short selectedItemId = selection[0];
                    int selectedIndex = selection[1];

                    Item Itemsc = ItemService.gI().createNewItem(selectedItemId);
                    Itemsc.itemOptions.add(new ItemOption(73, 0));
                    Itemsc.itemOptions.add(new ItemOption(30, 1));
                    Itemsc.quantity = quantities[selectedIndex];

                    InventoryService.gI().addItemBag(pl, Itemsc, 0);
                    NpcService.gI().createMenuConMeo(pl, ConstNpc.RUONG_GO, -1,
                            "Bạn Đã Nhận Được " + Itemsc.template.name + "\n"
                            + "SL: " + quantities[selectedIndex] + "\n",
                            "Đồng ý");
                    break;
                }
                case 4: {
                    short[] itemIds = new short[]{1400, 1237, 987, 1525, 674}; //id item
                    int[] rates = new int[]{10, 15, 25, 25, 25}; // Tổng phải = 100
                    int[] quantities = new int[]{20, 5, 2, 10, 20}; // Số lượng tương ứng cho từng item

                    short[] selection = selectItemByRate(itemIds, rates);
                    short selectedItemId = selection[0];
                    int selectedIndex = selection[1];

                    Item Itemsc = ItemService.gI().createNewItem(selectedItemId);
                    Itemsc.itemOptions.add(new ItemOption(73, 0));
                    Itemsc.itemOptions.add(new ItemOption(30, 1));
                    Itemsc.quantity = quantities[selectedIndex];

                    InventoryService.gI().addItemBag(pl, Itemsc, 0);
                    NpcService.gI().createMenuConMeo(pl, ConstNpc.RUONG_GO, -1,
                            "Bạn Đã Nhận Được " + Itemsc.template.name + "\n"
                            + "SL: " + quantities[selectedIndex] + "\n",
                            "Đồng ý");
                    break;
                }
                case 5: {
                    short[] itemIds = new short[]{1400, 1237, 987, 1525, 674}; //id item
                    int[] rates = new int[]{10, 15, 25, 25, 25}; // Tổng phải = 100
                    int[] quantities = new int[]{20, 5, 2, 10, 20}; // Số lượng tương ứng cho từng item

                    short[] selection = selectItemByRate(itemIds, rates);
                    short selectedItemId = selection[0];
                    int selectedIndex = selection[1];

                    Item Itemsc = ItemService.gI().createNewItem(selectedItemId);
                    Itemsc.itemOptions.add(new ItemOption(73, 0));
                    Itemsc.itemOptions.add(new ItemOption(30, 1));
                    Itemsc.quantity = quantities[selectedIndex];

                    InventoryService.gI().addItemBag(pl, Itemsc, 0);
                    NpcService.gI().createMenuConMeo(pl, ConstNpc.RUONG_GO, -1,
                            "Bạn Đã Nhận Được " + Itemsc.template.name + "\n"
                            + "SL: " + quantities[selectedIndex] + "\n",
                            "Đồng ý");
                    break;
                }
                case 6: {
                    short[] itemIds = new short[]{1400, 1237, 987, 1525, 674}; //id item
                    int[] rates = new int[]{10, 15, 25, 25, 25}; // Tổng phải = 100
                    int[] quantities = new int[]{20, 5, 2, 10, 20}; // Số lượng tương ứng cho từng item

                    short[] selection = selectItemByRate(itemIds, rates);
                    short selectedItemId = selection[0];
                    int selectedIndex = selection[1];

                    Item Itemsc = ItemService.gI().createNewItem(selectedItemId);
                    Itemsc.itemOptions.add(new ItemOption(73, 0));
                    Itemsc.itemOptions.add(new ItemOption(30, 1));
                    Itemsc.quantity = quantities[selectedIndex];

                    InventoryService.gI().addItemBag(pl, Itemsc, 0);
                    NpcService.gI().createMenuConMeo(pl, ConstNpc.RUONG_GO, -1,
                            "Bạn Đã Nhận Được " + Itemsc.template.name + "\n"
                            + "SL: " + quantities[selectedIndex] + "\n",
                            "Đồng ý");
                    break;
                }
                case 7: {
                    short[] itemIds = new short[]{1400, 1237, 987, 1525, 674}; //id item
                    int[] rates = new int[]{10, 15, 25, 25, 25}; // Tổng phải = 100
                    int[] quantities = new int[]{20, 5, 2, 10, 20}; // Số lượng tương ứng cho từng item

                    short[] selection = selectItemByRate(itemIds, rates);
                    short selectedItemId = selection[0];
                    int selectedIndex = selection[1];

                    Item Itemsc = ItemService.gI().createNewItem(selectedItemId);
                    Itemsc.itemOptions.add(new ItemOption(73, 0));
                    Itemsc.itemOptions.add(new ItemOption(30, 1));
                    Itemsc.quantity = quantities[selectedIndex];

                    InventoryService.gI().addItemBag(pl, Itemsc, 0);
                    NpcService.gI().createMenuConMeo(pl, ConstNpc.RUONG_GO, -1,
                            "Bạn Đã Nhận Được " + Itemsc.template.name + "\n"
                            + "SL: " + quantities[selectedIndex] + "\n",
                            "Đồng ý");
                    break;
                }
                case 8: {
                    short[] itemIds = new short[]{1400, 1237, 987, 1525, 674}; //id item
                    int[] rates = new int[]{10, 15, 25, 25, 25}; // Tổng phải = 100
                    int[] quantities = new int[]{20, 5, 2, 10, 20}; // Số lượng tương ứng cho từng item

                    short[] selection = selectItemByRate(itemIds, rates);
                    short selectedItemId = selection[0];
                    int selectedIndex = selection[1];

                    Item Itemsc = ItemService.gI().createNewItem(selectedItemId);
                    Itemsc.itemOptions.add(new ItemOption(73, 0));
                    Itemsc.itemOptions.add(new ItemOption(30, 1));
                    Itemsc.quantity = quantities[selectedIndex];

                    InventoryService.gI().addItemBag(pl, Itemsc, 0);
                    NpcService.gI().createMenuConMeo(pl, ConstNpc.RUONG_GO, -1,
                            "Bạn Đã Nhận Được " + Itemsc.template.name + "\n"
                            + "SL: " + quantities[selectedIndex] + "\n",
                            "Đồng ý");
                    break;
                }
                case 9: {
                    short[] itemIds = new short[]{1400, 1237, 987, 1525, 674}; //id item
                    int[] rates = new int[]{10, 15, 25, 25, 25}; // Tổng phải = 100
                    int[] quantities = new int[]{20, 5, 2, 10, 20}; // Số lượng tương ứng cho từng item

                    short[] selection = selectItemByRate(itemIds, rates);
                    short selectedItemId = selection[0];
                    int selectedIndex = selection[1];

                    Item Itemsc = ItemService.gI().createNewItem(selectedItemId);
                    Itemsc.itemOptions.add(new ItemOption(73, 0));
                    Itemsc.itemOptions.add(new ItemOption(30, 1));
                    Itemsc.quantity = quantities[selectedIndex];

                    InventoryService.gI().addItemBag(pl, Itemsc, 0);
                    NpcService.gI().createMenuConMeo(pl, ConstNpc.RUONG_GO, -1,
                            "Bạn Đã Nhận Được " + Itemsc.template.name + "\n"
                            + "SL: " + quantities[selectedIndex] + "\n",
                            "Đồng ý");
                    break;
                }
                case 10: {
                    short[] itemIds = new short[]{1400, 1237, 987, 1525, 674}; //id item
                    int[] rates = new int[]{10, 15, 25, 25, 25}; // Tổng phải = 100
                    int[] quantities = new int[]{20, 5, 2, 10, 20}; // Số lượng tương ứng cho từng item

                    short[] selection = selectItemByRate(itemIds, rates);
                    short selectedItemId = selection[0];
                    int selectedIndex = selection[1];

                    Item Itemsc = ItemService.gI().createNewItem(selectedItemId);
                    Itemsc.itemOptions.add(new ItemOption(73, 0));
                    Itemsc.itemOptions.add(new ItemOption(30, 1));
                    Itemsc.quantity = quantities[selectedIndex];

                    InventoryService.gI().addItemBag(pl, Itemsc, 0);
                    NpcService.gI().createMenuConMeo(pl, ConstNpc.RUONG_GO, -1,
                            "Bạn Đã Nhận Được " + Itemsc.template.name + "\n"
                            + "SL: " + quantities[selectedIndex] + "\n",
                            "Đồng ý");
                    break;
                }
                case 11: {
                    short[] itemIds = new short[]{1400, 1526, 1237, 1525}; //id item
                    int[] rates = new int[]{5, 15, 20, 30, 30}; // Tổng phải = 100
                    int[] quantities = new int[]{50, 3, 1, 5, 10}; // Số lượng tương ứng cho từng item

                    short[] selection = selectItemByRate(itemIds, rates);
                    short selectedItemId = selection[0];
                    int selectedIndex = selection[1];

                    Item Itemsc = ItemService.gI().createNewItem(selectedItemId);
                    Itemsc.itemOptions.add(new ItemOption(73, 0));
                    Itemsc.itemOptions.add(new ItemOption(30, 1));
                    Itemsc.quantity = quantities[selectedIndex];

                    InventoryService.gI().addItemBag(pl, Itemsc, 0);
                    NpcService.gI().createMenuConMeo(pl, ConstNpc.RUONG_GO, -1,
                            "Bạn Đã Nhận Được " + Itemsc.template.name + "\n"
                            + "SL: " + quantities[selectedIndex] + "\n",
                            "Đồng ý");
                    break;
                }
                default:
                    Service.getInstance().sendThongBao(pl, "Cấp rương không hợp lệ!");
                    break;
            }
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
            PlayerService.gI().sendInfoHpMpMoney(pl);
        } else {
            Service.getInstance().sendThongBao(pl, "Vì bạn quên không lấy chìa nên cần đợi 24h để bẻ khóa");
        }
    }

    private void useItemChangeFlagBag(Player player, Item item) {
        switch (item.template.id) {
            case 865: //kiem Z
                if (!player.effectFlagBag.useKiemz) {
                    player.effectFlagBag.reset();
                    player.effectFlagBag.useKiemz = !player.effectFlagBag.useKiemz;
                } else {
                    player.effectFlagBag.reset();
                }
                break;
            case 994: //vỏ ốc
                break;
            case 995: //cây kem
                break;
            case 996: //cá heo
                break;
            case 997: //con diều
                break;
            case 998: //diều rồng
                if (!player.effectFlagBag.useDieuRong) {
                    player.effectFlagBag.reset();
                    player.effectFlagBag.useDieuRong = !player.effectFlagBag.useDieuRong;
                } else {
                    player.effectFlagBag.reset();
                }
                break;
            case 999: //mèo mun
                if (!player.effectFlagBag.useMeoMun) {
                    player.effectFlagBag.reset();
                    player.effectFlagBag.useMeoMun = !player.effectFlagBag.useMeoMun;
                } else {
                    player.effectFlagBag.reset();
                }
                break;
            case 1000: //xiên cá
                if (!player.effectFlagBag.useXienCa) {
                    player.effectFlagBag.reset();
                    player.effectFlagBag.useXienCa = !player.effectFlagBag.useXienCa;
                } else {
                    player.effectFlagBag.reset();
                }
                break;
            case 1001: //phóng heo
                if (!player.effectFlagBag.usePhongHeo) {
                    player.effectFlagBag.reset();
                    player.effectFlagBag.usePhongHeo = !player.effectFlagBag.usePhongHeo;
                } else {
                    player.effectFlagBag.reset();
                }
                break;
            case 954:
                if (!player.effectFlagBag.useHoaVang) {
                    player.effectFlagBag.reset();
                    player.effectFlagBag.useHoaVang = !player.effectFlagBag.useHoaVang;
                } else {
                    player.effectFlagBag.reset();
                }
                break;
            case 955:
                if (!player.effectFlagBag.useHoaHong) {
                    player.effectFlagBag.reset();
                    player.effectFlagBag.useHoaHong = !player.effectFlagBag.useHoaHong;
                } else {
                    player.effectFlagBag.reset();
                }
                break;
            case 852:
                if (!player.effectFlagBag.useGayTre) {
                    player.effectFlagBag.reset();
                    player.effectFlagBag.useGayTre = !player.effectFlagBag.useGayTre;
                } else {
                    player.effectFlagBag.reset();
                }
                break;
        }
        Service.getInstance().point(player);
        Service.getInstance().sendFlagBag(player);
    }

    public void hopQuaTanThu(Player pl, Item it) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 14) {
            int gender = pl.gender;
            int[] id = {gender, 6 + gender, 21 + gender, 27 + gender, 12, 194, 441, 442, 443, 444, 445, 446, 447};
            int[] soluong = {1, 1, 1, 1, 1, 1, 10, 10, 10, 10, 10, 10, 10};
            int[] option = {0, 0, 0, 0, 0, 73, 95, 96, 97, 98, 99, 100, 101};
            int[] param = {0, 0, 0, 0, 0, 0, 5, 5, 5, 3, 3, 5, 5};
            int arrLength = id.length - 1;

            for (int i = 0; i < arrLength; i++) {
                if (i < 5) {
                    Item item = ItemService.gI().createNewItem((short) id[i]);
                    RewardService.gI().initBaseOptionClothes(item.template.id, item.template.type, item.itemOptions);
                    item.itemOptions.add(new ItemOption(107, 3));
                    InventoryService.gI().addItemBag(pl, item, 0);
                } else {
                    Item item = ItemService.gI().createNewItem((short) id[i]);
                    item.quantity = soluong[i];
                    item.itemOptions.add(new ItemOption(option[i], param[i]));
                    InventoryService.gI().addItemBag(pl, item, 0);
                }
            }

            int[] idpet = {916, 917, 918, 942, 943, 944, 1046, 1039, 1040};

            Item item = ItemService.gI().createNewItem((short) idpet[Util.nextInt(0, idpet.length - 1)]);
            item.itemOptions.add(new ItemOption(50, Util.nextInt(5, 10)));
            item.itemOptions.add(new ItemOption(77, Util.nextInt(5, 10)));
            item.itemOptions.add(new ItemOption(103, Util.nextInt(5, 10)));
            item.itemOptions.add(new ItemOption(93, 3));
            InventoryService.gI().addItemBag(pl, item, 0);

            InventoryService.gI().subQuantityItemsBag(pl, it, 1);
            InventoryService.gI().sendItemBags(pl);
            Service.getInstance().sendThongBao(pl, "Chúc bạn chơi game vui vẻ");
        } else {
            Service.getInstance().sendThongBao(pl, "Cần tối thiểu 14 ô trống để nhận thưởng");
        }
    }

    private void openbox2010(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {17, 16, 15, 675, 676, 677, 678, 679, 680, 681, 580, 581, 582};
            int[][] gold = {{5000, 20000}};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;

            Item it = ItemService.gI().createNewItem(temp[index]);

            if (temp[index] >= 15 && temp[index] <= 17) {
                it.itemOptions.add(new ItemOption(73, 0));

            } else if (temp[index] >= 580 && temp[index] <= 582 || temp[index] >= 675 && temp[index] <= 681) { // cải trang

                it.itemOptions.add(new ItemOption(77, Util.nextInt(20, 30)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(20, 30)));
                it.itemOptions.add(new ItemOption(50, Util.nextInt(20, 30)));
                it.itemOptions.add(new ItemOption(95, Util.nextInt(5, 15)));
                it.itemOptions.add(new ItemOption(96, Util.nextInt(5, 15)));

                if (Util.isTrue(1, 200)) {
                    it.itemOptions.add(new ItemOption(74, 0));
                } else {
                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                }

            } else {
                it.itemOptions.add(new ItemOption(73, 0));
            }
            InventoryService.gI().addItemBag(pl, it, 0);
            icon[1] = it.template.iconID;

            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);

            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void capsule8thang3(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {17, 16, 15, 675, 676, 677, 678, 679, 680, 681, 580, 581, 582, 1154, 1155, 1156, 860, 1041, 1042, 1043, 1103, 1104, 1105, 1106, 954, 955};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;

            Item it = ItemService.gI().createNewItem(temp[index]);

            if (Util.isTrue(30, 100)) {
                int ruby = Util.nextInt(1, 5);
                pl.inventory.ruby += ruby;
                CombineServiceNew.gI().sendEffectOpenItem(pl, item.template.iconID, (short) 7743);
                PlayerService.gI().sendInfoHpMpMoney(pl);
                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                InventoryService.gI().sendItemBags(pl);
                Service.getInstance().sendThongBao(pl, "Bạn nhận được " + ruby + " Hồng Ngọc");
                return;
            }
            if (it.template.type == 5) { // cải trang

                it.itemOptions.add(new ItemOption(50, Util.nextInt(20, 35)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(20, 35)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(20, 35)));
                it.itemOptions.add(new ItemOption(117, Util.nextInt(10, 20)));

            } else if (it.template.id == 954 || it.template.id == 955) {
                it.itemOptions.add(new ItemOption(50, Util.nextInt(10, 20)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(10, 20)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(10, 20)));
            }

            if (it.template.type == 5 || it.template.id == 954 || it.template.id == 955) {
                if (Util.isTrue(1, 200)) {
                    it.itemOptions.add(new ItemOption(74, 0));
                } else {
                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                }
            }
            InventoryService.gI().addItemBag(pl, it, 0);
            icon[1] = it.template.iconID;

            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);

            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    public void openboxsukien(Player pl, Item item, int idsukien) {
        try {
            switch (idsukien) {
                case 1:
                    if (Manager.EVENT_SEVER == idsukien) {
                        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
                            short[] temp = {16, 15, 865, 999, 1000, 1001, 739, 742, 743};
                            int[][] gold = {{5000, 20000}};
                            byte index = (byte) Util.nextInt(0, temp.length - 1);
                            short[] icon = new short[2];
                            icon[0] = item.template.iconID;

                            Item it = ItemService.gI().createNewItem(temp[index]);

                            if (temp[index] >= 15 && temp[index] <= 16) {
                                it.itemOptions.add(new ItemOption(73, 0));

                            } else if (temp[index] == 865) {

                                it.itemOptions.add(new ItemOption(30, 0));

                                if (Util.isTrue(1, 30)) {
                                    it.itemOptions.add(new ItemOption(93, 365));
                                } else {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }
                            } else if (temp[index] == 999) { // mèo mun
                                it.itemOptions.add(new ItemOption(77, 15));
                                it.itemOptions.add(new ItemOption(30, 0));
                                if (Util.isTrue(1, 50)) {
                                    it.itemOptions.add(new ItemOption(74, 0));
                                } else {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }
                            } else if (temp[index] == 1000) { // xiên cá
                                it.itemOptions.add(new ItemOption(103, 15));
                                it.itemOptions.add(new ItemOption(30, 0));
                                if (Util.isTrue(1, 50)) {
                                    it.itemOptions.add(new ItemOption(74, 0));
                                } else {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }
                            } else if (temp[index] == 1001) { // Phóng heo
                                it.itemOptions.add(new ItemOption(50, 15));
                                it.itemOptions.add(new ItemOption(30, 0));
                                if (Util.isTrue(1, 50)) {
                                    it.itemOptions.add(new ItemOption(74, 0));
                                } else {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }

                            } else if (temp[index] == 739) { // cải trang Billes

                                it.itemOptions.add(new ItemOption(77, Util.nextInt(30, 40)));
                                it.itemOptions.add(new ItemOption(103, Util.nextInt(30, 40)));
                                it.itemOptions.add(new ItemOption(50, Util.nextInt(30, 45)));

                                if (Util.isTrue(1, 100)) {
                                    it.itemOptions.add(new ItemOption(74, 0));
                                } else {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }

                            } else if (temp[index] == 742) { // cải trang Caufila

                                it.itemOptions.add(new ItemOption(77, Util.nextInt(30, 40)));
                                it.itemOptions.add(new ItemOption(103, Util.nextInt(30, 40)));
                                it.itemOptions.add(new ItemOption(50, Util.nextInt(30, 45)));

                                if (Util.isTrue(1, 100)) {
                                    it.itemOptions.add(new ItemOption(74, 0));
                                } else {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }
                            } else if (temp[index] == 743) { // chổi bay
                                it.itemOptions.add(new ItemOption(30, 0));
                                if (Util.isTrue(1, 50)) {
                                    it.itemOptions.add(new ItemOption(74, 0));
                                } else {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }

                            } else {
                                it.itemOptions.add(new ItemOption(73, 0));
                            }
                            InventoryService.gI().addItemBag(pl, it, 0);
                            icon[1] = it.template.iconID;

                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                            InventoryService.gI().sendItemBags(pl);

                            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
                        } else {
                            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
                        }
                        break;
                    } else {
                        Service.getInstance().sendThongBao(pl, "Sự kiện đã kết thúc");
                    }
                case ConstEvent.SU_KIEN_20_11:
                    if (Manager.EVENT_SEVER == idsukien) {
                        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
                            short[] temp = {16, 15, 1039, 954, 955, 710, 711, 1040, 2023, 999, 1000, 1001};
                            byte index = (byte) Util.nextInt(0, temp.length - 1);
                            short[] icon = new short[2];
                            icon[0] = item.template.iconID;
                            Item it = ItemService.gI().createNewItem(temp[index]);
                            if (temp[index] >= 15 && temp[index] <= 16) {
                                it.itemOptions.add(new ItemOption(73, 0));
                            } else if (temp[index] == 1039) {
                                it.itemOptions.add(new ItemOption(50, 10));
                                it.itemOptions.add(new ItemOption(77, 10));
                                it.itemOptions.add(new ItemOption(103, 10));
                                it.itemOptions.add(new ItemOption(30, 0));
                                it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                            } else if (temp[index] == 954) {
                                it.itemOptions.add(new ItemOption(50, 15));
                                it.itemOptions.add(new ItemOption(77, 15));
                                it.itemOptions.add(new ItemOption(103, 15));
                                it.itemOptions.add(new ItemOption(30, 0));
                                if (Util.isTrue(79, 80)) {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }
                            } else if (temp[index] == 955) {
                                it.itemOptions.add(new ItemOption(50, 20));
                                it.itemOptions.add(new ItemOption(77, 20));
                                it.itemOptions.add(new ItemOption(103, 20));
                                it.itemOptions.add(new ItemOption(30, 0));
                                if (Util.isTrue(79, 80)) {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }
                            } else if (temp[index] == 710) {//cải trang quy lão kame
                                it.itemOptions.add(new ItemOption(50, 22));
                                it.itemOptions.add(new ItemOption(77, 20));
                                it.itemOptions.add(new ItemOption(103, 20));
                                it.itemOptions.add(new ItemOption(194, 0));
                                it.itemOptions.add(new ItemOption(160, 35));
                                if (Util.isTrue(99, 100)) {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }
                            } else if (temp[index] == 711) { // cải trang jacky chun
                                it.itemOptions.add(new ItemOption(50, 23));
                                it.itemOptions.add(new ItemOption(77, 21));
                                it.itemOptions.add(new ItemOption(103, 21));
                                it.itemOptions.add(new ItemOption(195, 0));
                                it.itemOptions.add(new ItemOption(160, 50));
                                if (Util.isTrue(99, 100)) {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }
                            } else if (temp[index] == 1040) {
                                it.itemOptions.add(new ItemOption(50, 10));
                                it.itemOptions.add(new ItemOption(77, 10));
                                it.itemOptions.add(new ItemOption(103, 10));
                                it.itemOptions.add(new ItemOption(30, 0));
                                it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                            } else if (temp[index] == 2023) {
                                it.itemOptions.add(new ItemOption(30, 0));
                            } else if (temp[index] == 999) { // mèo mun
                                it.itemOptions.add(new ItemOption(77, 15));
                                it.itemOptions.add(new ItemOption(30, 0));
                                if (Util.isTrue(1, 50)) {
                                    it.itemOptions.add(new ItemOption(74, 0));
                                } else {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }
                            } else if (temp[index] == 1000) { // xiên cá
                                it.itemOptions.add(new ItemOption(103, 15));
                                it.itemOptions.add(new ItemOption(30, 0));
                                if (Util.isTrue(1, 50)) {
                                    it.itemOptions.add(new ItemOption(74, 0));
                                } else {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }
                            } else if (temp[index] == 1001) { // Phóng heo
                                it.itemOptions.add(new ItemOption(50, 15));
                                it.itemOptions.add(new ItemOption(30, 0));
                                if (Util.isTrue(1, 50)) {
                                    it.itemOptions.add(new ItemOption(74, 0));
                                } else {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }
                            } else {
                                it.itemOptions.add(new ItemOption(73, 0));
                            }
                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                            icon[1] = it.template.iconID;
                            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
                            InventoryService.gI().addItemBag(pl, it, 0);
                            int ruby = Util.nextInt(1, 5);
                            pl.inventory.ruby += ruby;
                            InventoryService.gI().sendItemBags(pl);
                            PlayerService.gI().sendInfoHpMpMoney(pl);
                            Service.getInstance().sendThongBao(pl, "Bạn được tặng kèm " + ruby + " Hồng Ngọc");
                        } else {
                            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
                        }
                    } else {
                        Service.getInstance().sendThongBao(pl, "Sự kiện đã kết thúc");
                    }
                    break;
                case ConstEvent.SU_KIEN_NOEL:
                    if (Manager.EVENT_SEVER == idsukien) {
                        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
                            int spl = Util.nextInt(441, 445);
                            int dnc = Util.nextInt(220, 224);
                            int nr = Util.nextInt(16, 18);
                            int nrBang = Util.nextInt(926, 931);

                            if (Util.isTrue(5, 90)) {
                                int ruby = Util.nextInt(1, 3);
                                pl.inventory.ruby += ruby;
                                CombineServiceNew.gI().sendEffectOpenItem(pl, item.template.iconID, (short) 7743);
                                PlayerService.gI().sendInfoHpMpMoney(pl);
                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                                InventoryService.gI().sendItemBags(pl);
                                Service.getInstance().sendThongBao(pl, "Bạn nhận được " + ruby + " Hồng Ngọc");
                            } else {
                                int[] temp = {spl, dnc, nr, nrBang, 387, 390, 393, 821, 822, 746, 380, 999, 1000, 1001, 936, 2022};
                                byte index = (byte) Util.nextInt(0, temp.length - 1);
                                short[] icon = new short[2];
                                icon[0] = item.template.iconID;
                                Item it = ItemService.gI().createNewItem((short) temp[index]);

                                if (temp[index] >= 441 && temp[index] <= 443) {// sao pha le
                                    it.itemOptions.add(new ItemOption(temp[index] - 346, 5));
                                    it.quantity = 10;
                                } else if (temp[index] >= 444 && temp[index] <= 445) {
                                    it.itemOptions.add(new ItemOption(temp[index] - 346, 3));
                                    it.quantity = 10;
                                } else if (temp[index] >= 220 && temp[index] <= 224) { // da nang cap
                                    it.quantity = 10;
                                } else if (temp[index] >= 387 && temp[index] <= 393) { // mu noel do
                                    it.itemOptions.add(new ItemOption(50, Util.nextInt(30, 40)));
                                    it.itemOptions.add(new ItemOption(77, Util.nextInt(30, 40)));
                                    it.itemOptions.add(new ItemOption(103, Util.nextInt(30, 40)));
                                    it.itemOptions.add(new ItemOption(80, Util.nextInt(10, 20)));
                                    it.itemOptions.add(new ItemOption(106, 0));
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 3)));
                                    it.itemOptions.add(new ItemOption(199, 0));
                                } else if (temp[index] == 936) { // tuan loc
                                    it.itemOptions.add(new ItemOption(50, Util.nextInt(5, 10)));
                                    it.itemOptions.add(new ItemOption(77, Util.nextInt(5, 10)));
                                    it.itemOptions.add(new ItemOption(103, Util.nextInt(5, 10)));
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(3, 30)));
                                } else if (temp[index] == 822) { //cay thong noel
                                    it.itemOptions.add(new ItemOption(50, Util.nextInt(10, 20)));
                                    it.itemOptions.add(new ItemOption(77, Util.nextInt(10, 20)));
                                    it.itemOptions.add(new ItemOption(103, Util.nextInt(10, 20)));
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(3, 30)));
                                    it.itemOptions.add(new ItemOption(30, 0));
                                    it.itemOptions.add(new ItemOption(74, 0));
                                } else if (temp[index] == 746) { // xe truot tuyet
                                    it.itemOptions.add(new ItemOption(74, 0));
                                    it.itemOptions.add(new ItemOption(30, 0));
                                    if (Util.isTrue(99, 100)) {
                                        it.itemOptions.add(new ItemOption(93, Util.nextInt(30, 360)));
                                    }
                                } else if (temp[index] == 999) { // mèo mun
                                    it.itemOptions.add(new ItemOption(77, 15));
                                    it.itemOptions.add(new ItemOption(74, 0));
                                    it.itemOptions.add(new ItemOption(30, 0));
                                    if (Util.isTrue(99, 100)) {
                                        it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                    }
                                } else if (temp[index] == 1000) { // xiên cá
                                    it.itemOptions.add(new ItemOption(103, 15));
                                    it.itemOptions.add(new ItemOption(74, 0));
                                    it.itemOptions.add(new ItemOption(30, 0));
                                    if (Util.isTrue(99, 100)) {
                                        it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                    }
                                } else if (temp[index] == 1001) { // Phóng heo
                                    it.itemOptions.add(new ItemOption(50, 15));
                                    it.itemOptions.add(new ItemOption(74, 0));
                                    it.itemOptions.add(new ItemOption(30, 0));
                                    if (Util.isTrue(99, 100)) {
                                        it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                    }
                                } else if (temp[index] == 2022 || temp[index] == 821) {
                                    it.itemOptions.add(new ItemOption(30, 0));
                                } else {
                                    it.itemOptions.add(new ItemOption(73, 0));
                                }
                                InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                                icon[1] = it.template.iconID;
                                CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
                                InventoryService.gI().addItemBag(pl, it, 0);
                                InventoryService.gI().sendItemBags(pl);
                            }
                        } else {
                            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
                        }
                    } else {
                        Service.getInstance().sendThongBao(pl, "Sự kiện đã kết thúc");
                    }
                    break;
                case ConstEvent.SU_KIEN_TET:
                    if (Manager.EVENT_SEVER == idsukien) {
                        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
                            short[] icon = new short[2];
                            icon[0] = item.template.iconID;
                            RandomCollection<Integer> rd = Manager.HOP_QUA_TET;
                            int tempID = rd.next();
                            Item it = ItemService.gI().createNewItem((short) tempID);
                            if (it.template.type == 11) {//FLAGBAG
                                it.itemOptions.add(new ItemOption(50, Util.nextInt(5, 20)));
                                it.itemOptions.add(new ItemOption(77, Util.nextInt(5, 20)));
                                it.itemOptions.add(new ItemOption(103, Util.nextInt(5, 20)));
                            } else if (tempID >= 1159 && tempID <= 1161) {
                                it.itemOptions.add(new ItemOption(50, Util.nextInt(20, 30)));
                                it.itemOptions.add(new ItemOption(77, Util.nextInt(20, 30)));
                                it.itemOptions.add(new ItemOption(103, Util.nextInt(20, 30)));
                                it.itemOptions.add(new ItemOption(106, 0));
                            } else if (tempID == ConstItem.CAI_TRANG_SSJ_3_WHITE) {
                                it.itemOptions.add(new ItemOption(50, Util.nextInt(30, 40)));
                                it.itemOptions.add(new ItemOption(77, Util.nextInt(30, 40)));
                                it.itemOptions.add(new ItemOption(103, Util.nextInt(30, 40)));
                                it.itemOptions.add(new ItemOption(5, Util.nextInt(10, 25)));
                                it.itemOptions.add(new ItemOption(104, Util.nextInt(5, 15)));
                            }
                            int type = it.template.type;
                            if (type == 5 || type == 11) {// cải trang & flagbag
                                if (Util.isTrue(199, 200)) {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                                }
                                it.itemOptions.add(new ItemOption(199, 0));//KHÔNG THỂ GIA HẠN
                            } else if (type == 23) {// thú cưỡi
                                if (Util.isTrue(199, 200)) {
                                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 5)));
                                }
                            }
                            if (tempID >= ConstItem.MANH_AO && tempID <= ConstItem.MANH_GANG_TAY) {
                                it.quantity = Util.nextInt(5, 15);
                            } else {
                                it.itemOptions.add(new ItemOption(74, 0));
                            }
                            icon[1] = it.template.iconID;
                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
                            InventoryService.gI().addItemBag(pl, it, 0);
                            InventoryService.gI().sendItemBags(pl);
                            break;
                        } else {
                            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
                        }
                    } else {
                        Service.getInstance().sendThongBao(pl, "Sự kiện đã kết thúc");
                    }
                    break;
            }
        } catch (Exception e) {
            logger.error("Lỗi mở hộp quà", e);
        }
    }

    private void openboxkichhoat(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {76, 188, 189, 190, 441, 442, 447, 2010, 2009, 865, 938, 939, 940, 16, 17, 18, 19, 20, 946, 947, 948, 382, 383, 384, 385};
            int[][] gold = {{5000, 20000}};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            if (index <= 3 && index >= 0) {
                pl.inventory.addGold(Util.nextInt(gold[0][0], gold[0][1]));
                PlayerService.gI().sendInfoHpMpMoney(pl);
                icon[1] = 930;
            } else {

                Item it = ItemService.gI().createNewItem(temp[index]);
                if (temp[index] == 441) {
                    it.itemOptions.add(new ItemOption(95, 5));
                } else if (temp[index] == 442) {
                    it.itemOptions.add(new ItemOption(96, 5));
                } else if (temp[index] == 447) {
                    it.itemOptions.add(new ItemOption(101, 5));
                } else if (temp[index] >= 2009 && temp[index] <= 2010) {
                    it.itemOptions.add(new ItemOption(30, 0));
                } else if (temp[index] == 865) {
                    it.itemOptions.add(new ItemOption(30, 0));
                    if (Util.isTrue(1, 20)) {
                        it.itemOptions.add(new ItemOption(93, 365));
                    } else {
                        it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                    }
                } else if (temp[index] >= 938 && temp[index] <= 940) {
                    it.itemOptions.add(new ItemOption(77, 35));
                    it.itemOptions.add(new ItemOption(103, 35));
                    it.itemOptions.add(new ItemOption(50, 35));
                    if (Util.isTrue(1, 50)) {
                        it.itemOptions.add(new ItemOption(116, 0));
                    } else {
                        it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                    }
                } else if (temp[index] >= 946 && temp[index] <= 948) {
                    it.itemOptions.add(new ItemOption(77, 35));
                    it.itemOptions.add(new ItemOption(103, 35));
                    it.itemOptions.add(new ItemOption(50, 35));
                    if (Util.isTrue(1, 20)) {
                        it.itemOptions.add(new ItemOption(93, 365));
                    } else {
                        it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 30)));
                    }
                } else {
                    it.itemOptions.add(new ItemOption(73, 0));
                }
                InventoryService.gI().addItemBag(pl, it, 0);
                icon[1] = it.template.iconID;

            }
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);

            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void openPhieuCaiTrangHaiTac(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            Item ct = ItemService.gI().createNewItem((short) Util.nextInt(618, 626));
            ct.itemOptions.add(new ItemOption(147, 3));
            ct.itemOptions.add(new ItemOption(77, 3));
            ct.itemOptions.add(new ItemOption(103, 3));
            ct.itemOptions.add(new ItemOption(149, 0));
            if (item.template.id == 2006) {
                ct.itemOptions.add(new ItemOption(93, Util.nextInt(1, 7)));
            } else if (item.template.id == 2007) {
                ct.itemOptions.add(new ItemOption(93, Util.nextInt(7, 30)));
            }
            InventoryService.gI().addItemBag(pl, ct, 0);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
            CombineServiceNew.gI().sendEffectOpenItem(pl, item.template.iconID, ct.template.iconID);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void eatGrapes(Player pl, Item item) {
        int percentCurrentStatima = pl.nPoint.stamina * 100 / pl.nPoint.maxStamina;
        if (percentCurrentStatima > 50) {
            Service.getInstance().sendThongBao(pl, "Thể lực vẫn còn trên 50%");
            return;
        } else if (item.template.id == 211) {
            pl.nPoint.stamina = pl.nPoint.maxStamina;
            Service.getInstance().sendThongBao(pl, "Thể lực của bạn đã được hồi phục 100%");
        } else if (item.template.id == 212) {
            pl.nPoint.stamina += (pl.nPoint.maxStamina * 20 / 100);
            Service.getInstance().sendThongBao(pl, "Thể lực của bạn đã được hồi phục 20%");
        }
        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
        InventoryService.gI().sendItemBags(pl);
        PlayerService.gI().sendCurrentStamina(pl);
    }

    private void openCapsuleVang(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {1066, 1067, 1068, 1069, 1070};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] temp1 = {1099, 1100, 1101, 1102};
            byte index1 = (byte) Util.nextInt(0, temp1.length - 1);
            if (Util.isTrue(1, 100)) {
                Item it = ItemService.gI().createNewItem((short) 1479, 1);
                it.itemOptions.add(new ItemOption(73, 0));
                InventoryService.gI().addItemBag(pl, it, 0);
                PlayerService.gI().sendInfoHpMpMoney(pl);
            } else if (Util.isTrue(5, 100)) {
                Item it = ItemService.gI().createNewItem((short) 934, 1);
                it.itemOptions.add(new ItemOption(73, 0));
                InventoryService.gI().addItemBag(pl, it, 0);
                PlayerService.gI().sendInfoHpMpMoney(pl);
            } else if (Util.isTrue(7, 100)) {
                Item it = ItemService.gI().createNewItem((short) 457, 1);
                it.itemOptions.add(new ItemOption(73, 0));
                InventoryService.gI().addItemBag(pl, it, 0);
                PlayerService.gI().sendInfoHpMpMoney(pl);
            } else if (Util.isTrue(10, 100)) {
                Item it = ItemService.gI().createNewItem(temp1[index1]);
                it.itemOptions.add(new ItemOption(73, 0));
                InventoryService.gI().addItemBag(pl, it, 0);
                PlayerService.gI().sendInfoHpMpMoney(pl);
            } else if (Util.isTrue(15, 100)) {
                Item it = ItemService.gI().createNewItem((short) 933, 1);
                it.itemOptions.add(new ItemOption(73, 0));
                InventoryService.gI().addItemBag(pl, it, 0);
                PlayerService.gI().sendInfoHpMpMoney(pl);
            } else {
                Item it = ItemService.gI().createNewItem(temp[index]);
                it.itemOptions.add(new ItemOption(73, 0));
                InventoryService.gI().addItemBag(pl, it, 0);
            }
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void openCapsuleBac(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {381, 382, 383, 384, 1066, 1067, 1068, 1069, 1070};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            Item it = ItemService.gI().createNewItem(temp[index]);
            it.itemOptions.add(new ItemOption(73, 0));
            InventoryService.gI().addItemBag(pl, it, 0);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void openSkhThuong(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] dotd = {233, 245, 257, 269, 281};
            short[] donm = {237, 249, 261, 273, 281};
            short[] doxd = {241, 253, 265, 277, 281};
            switch (item.template.id) {
                case 2000: {
                    Item it = ItemService.gI().createNewItem(dotd[(byte) Util.nextInt(0, dotd.length - 1)]);
                    RewardService.gI().initBaseOptionClothes(it.template.id, it.template.type, it.itemOptions);
                    addOptionSkh(0, it.template.type, it);
                    it.itemOptions.add(new ItemOption(107, 8));
                    InventoryService.gI().addItemBag(pl, it, 0);
                    break;
                }
                case 2001: {
                    Item it = ItemService.gI().createNewItem(donm[(byte) Util.nextInt(0, donm.length - 1)]);
                    RewardService.gI().initBaseOptionClothes(it.template.id, it.template.type, it.itemOptions);
                    addOptionSkh(1, it.template.type, it);
                    it.itemOptions.add(new ItemOption(107, 8));
                    InventoryService.gI().addItemBag(pl, it, 0);
                    break;
                }
                case 2002: {
                    Item it = ItemService.gI().createNewItem(doxd[(byte) Util.nextInt(0, doxd.length - 1)]);
                    RewardService.gI().initBaseOptionClothes(it.template.id, it.template.type, it.itemOptions);
                    addOptionSkh(2, it.template.type, it);
                    it.itemOptions.add(new ItemOption(107, 8));
                    InventoryService.gI().addItemBag(pl, it, 0);
                    break;
                }
                default:
                    break;
            }
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
        } else {
            Service.getInstance().sendThongBao(pl, "Hành trang không đủ chổ trống");
        }
    }

    private void ruongVip(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] caiTrang = {1251, 1252, 1416, 1253};
            byte index = (byte) Util.nextInt(0, caiTrang.length - 1);
            if (Util.isTrue(5, 100)) {
                Item it = ItemService.gI().createNewItem((short) 1015);
                InventoryService.gI().addItemBag(pl, it, 0);
            } else if (Util.isTrue(25, 100)) {
                Item it = ItemService.gI().createNewItem((short) 1479);
                InventoryService.gI().addItemBag(pl, it, 0);
            } else {
                Item it = ItemService.gI().createNewItem(caiTrang[index]);
                it.itemOptions.add(new ItemOption(216, 1));
                it.itemOptions.add(new ItemOption(50, Util.nextInt(100, 180)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(100, 200)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(100, 200)));
                it.itemOptions.add(new ItemOption(101, Util.nextInt(100, 3000)));
                it.itemOptions.add(new ItemOption(5, Util.nextInt(1, 30)));
                it.itemOptions.add(new ItemOption(14, Util.nextInt(1, 20)));
                it.itemOptions.add(new ItemOption(100, Util.nextInt(1, 20000)));
                it.itemOptions.add(new ItemOption(33, 1));
                it.itemOptions.add(new ItemOption(106, 1));
                it.itemOptions.add(new ItemOption(30, 1));
                if (Util.isTrue(95, 100)) {
                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 2)));
                }
                InventoryService.gI().addItemBag(pl, it, 0);
            }
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
            pl.topRuong += 10;
            Service.getInstance().sendThongBao(pl, "Bạn nhận được 10 Điểm mở rương");
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void ruongThuong(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] phuKien = {1625, 1626, 1718};
            byte index = (byte) Util.nextInt(0, phuKien.length - 1);

            short[] mts = {1066, 1067, 1068, 1069, 1070};
            byte index1 = (byte) Util.nextInt(0, mts.length - 1);

            if (Util.isTrue(10, 100)) {
                Item it = ItemService.gI().createNewItem((short) 1479);
                InventoryService.gI().addItemBag(pl, it, 0);
            } else if (Util.isTrue(30, 100)) {
                Item it = ItemService.gI().createNewItem(phuKien[index]);
                it.itemOptions.add(new ItemOption(218, 1));
                it.itemOptions.add(new ItemOption(50, Util.nextInt(1, 100)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(1, 100)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(1, 100)));
                it.itemOptions.add(new ItemOption(101, Util.nextInt(100, 1000)));
                it.itemOptions.add(new ItemOption(5, Util.nextInt(1, 20)));
                it.itemOptions.add(new ItemOption(14, Util.nextInt(1, 10)));
                it.itemOptions.add(new ItemOption(30, 1));
                if (Util.isTrue(95, 100)) {
                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 2)));
                }
                InventoryService.gI().addItemBag(pl, it, 0);
            } else {
                Item it = ItemService.gI().createNewItem(mts[index1], 5);
                InventoryService.gI().addItemBag(pl, it, 0);
            }
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
            pl.topRuong += 5;
            Service.getInstance().sendThongBao(pl, "Bạn nhận được 5 Điểm mở rương");
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void TuiMu(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            int sL = Util.nextInt(50, 150);
            Item it = ItemService.gI().createNewItem((short) ConstItem.XU_SERVER, sL);
            InventoryService.gI().addItemBag(pl, it, 0);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
            Service.getInstance().sendThongBaoOK(pl, "Bạn nhận được " + sL + " " + it.template.name);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void TuiMuVip(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            int sL = Util.nextInt(50, 150);
            Item it = ItemService.gI().createNewItem((short) ConstItem.XU_DAC_BIET, sL);
            InventoryService.gI().addItemBag(pl, it, 0);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
            Service.getInstance().sendThongBaoOK(pl, "Bạn nhận được " + sL + " " + it.template.name);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private static final int[][][] ACTIVATION_SET_NO = {{{129, 141}, {127, 139}, {128, 140}}, //songoku - thien xin hang - kirin
    {{131, 143}, {132, 144}, {130, 142}}, //oc tieu - pikkoro daimao - picolo
    {{135, 138}, {133, 136}, {134, 137}} //kakarot - cadic - nappa
};

    public void addOptionSkh(int gender, int type, Item item) {
        if (type <= 4) {
            int[] idOption = ACTIVATION_SET_NO[gender][Util.nextInt(0, 2)];
            item.itemOptions.add(new ItemOption(idOption[0], 1)); //tên set
            item.itemOptions.add(new ItemOption(idOption[1], 1)); //hiệu ứng set
            item.itemOptions.add(new ItemOption(30, 1)); //không thể giao dịch
        }
    }

    private void tuivang(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            int[][] gold = {{300000000, 500000000}};
            pl.inventory.addGold(Util.nextInt(gold[0][0], gold[0][1]));
            PlayerService.gI().sendInfoHpMpMoney(pl);
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void openCSKB(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] temp = {76, 188, 189, 190, 381, 382, 383, 384, 385};
            int[][] gold = {{5000, 20000}};
            byte index = (byte) Util.nextInt(0, temp.length - 1);
            short[] icon = new short[2];
            icon[0] = item.template.iconID;
            if (index <= 3) {
                pl.inventory.addGold(Util.nextInt(gold[0][0], gold[0][1]));
                PlayerService.gI().sendInfoHpMpMoney(pl);
                icon[1] = 930;
            } else {
                Item it = ItemService.gI().createNewItem(temp[index]);
                it.itemOptions.add(new ItemOption(73, 0));
                InventoryService.gI().addItemBag(pl, it, 0);
                icon[1] = it.template.iconID;
            }
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);

            CombineServiceNew.gI().sendEffectOpenItem(pl, icon[0], icon[1]);
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void RuongSieuAnhHung(Player pl, Item item) {
        if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
            short[] caiTrang = {989, 990, 991};
            byte index = (byte) Util.nextInt(0, caiTrang.length - 1);
            if (Util.isTrue(1, 100)) {
                Item it = ItemService.gI().createNewItem((short) 1083);
                InventoryService.gI().addItemBag(pl, it, 0);
            } else if (Util.isTrue(10, 100)) {
                Item it = ItemService.gI().createNewItem((short) 1479);
                InventoryService.gI().addItemBag(pl, it, 0);
            } else if (Util.isTrue(20, 100)) {
                Item it = ItemService.gI().createNewItem((short) 987);
                InventoryService.gI().addItemBag(pl, it, 0);
            } else if (Util.isTrue(30, 100)) {
                Item it = ItemService.gI().createNewItem((short) 457);
                InventoryService.gI().addItemBag(pl, it, 0);
            } else {
                Item it = ItemService.gI().createNewItem(caiTrang[index]);
                it.itemOptions.add(new ItemOption(50, Util.nextInt(1, 120)));
                it.itemOptions.add(new ItemOption(77, Util.nextInt(1, 120)));
                it.itemOptions.add(new ItemOption(103, Util.nextInt(1, 120)));
                it.itemOptions.add(new ItemOption(101, Util.nextInt(1, 2000)));
                it.itemOptions.add(new ItemOption(117, Util.nextInt(1, 30)));
                it.itemOptions.add(new ItemOption(5, Util.nextInt(1, 35)));
                it.itemOptions.add(new ItemOption(14, Util.nextInt(1, 20)));
                it.itemOptions.add(new ItemOption(100, Util.nextInt(1, 13000)));
                it.itemOptions.add(new ItemOption(33, 1));
                it.itemOptions.add(new ItemOption(106, 1));
                if (Util.isTrue(99, 100)) {
                    it.itemOptions.add(new ItemOption(93, Util.nextInt(1, 3)));
                    it.itemOptions.add(new ItemOption(30, 1));
                }
                InventoryService.gI().addItemBag(pl, it, 0);
            }
            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
            InventoryService.gI().sendItemBags(pl);
            pl.topRuong++;
            Service.getInstance().sendThongBao(pl, "Bạn nhận được 1 Điểm mở rương");
        } else {
            Service.getInstance().sendThongBao(pl, "Hàng trang đã đầy");
        }
    }

    private void openMabuEgg(Player pl, Item item) {
        if (pl.pet == null) {
            Service.getInstance().sendThongBao(pl, "Yêu cầu có đệ tử");
            return;
        }
        if (InventoryService.gI().getCountEmptyBody(pl.pet) == 12) {
            if (pl.pet != null) {
                NpcService.gI().createMenuConMeo(pl, ConstNpc.DE_TU_MABU, 4674,
                        "Vui lòng Chọn Hành tinh Đệ Trứng Bát Giới Mập !!!",
                        "Trái Đất", "Namec", "Xayda");
            } else {
                Service.getInstance().sendThongBao(pl, "Yêu cầu có đệ tử");
            }
        } else {
            Service.getInstance().sendThongBao(pl, "Vui lòng tháo hết đồ đệ tử");
        }
    }

    private void useItemTime(Player pl, Item item) {
        boolean updatePoint = false;
        switch (item.template.id) {
            case 382: //bổ huyết
                pl.itemTime.lastTimeBoHuyet = System.currentTimeMillis();
                pl.itemTime.isUseBoHuyet = true;
                updatePoint = true;
                break;
            case 383: //bổ khí
                if (pl.itemTime.isUseBoKhi2) {
                    Service.getInstance().sendThongBao(pl, "Chỉ có thể sự dụng cùng lúc 1 vật phẩm bổ trợ cùng loại");
                    return;
                }
                pl.itemTime.lastTimeBoKhi = System.currentTimeMillis();
                pl.itemTime.isUseBoKhi = true;
                updatePoint = true;
                break;
            case 384: //giáp xên
                if (pl.itemTime.isUseGiapXen2) {
                    Service.getInstance().sendThongBao(pl, "Chỉ có thể sự dụng cùng lúc 1 vật phẩm bổ trợ cùng loại");
                    return;
                }
                pl.itemTime.lastTimeGiapXen = System.currentTimeMillis();
                pl.itemTime.isUseGiapXen = true;
                updatePoint = true;
                break;
            case 381: //cuồng nộ
                pl.itemTime.lastTimeCuongNo = System.currentTimeMillis();
                pl.itemTime.isUseCuongNo = true;
                updatePoint = true;
                break;
            case 385: //ẩn danh
                pl.itemTime.lastTimeAnDanh = System.currentTimeMillis();
                pl.itemTime.isUseAnDanh = true;
                break;
            case ConstItem.BO_HUYET_2: //bổ huyết 2
                pl.itemTime.lastTimeBoHuyet2 = System.currentTimeMillis();
                pl.itemTime.isUseBoHuyet2 = true;
                updatePoint = true;
                break;
            case ConstItem.BO_KHI_2: //bổ khí 2
                pl.itemTime.lastTimeBoKhi2 = System.currentTimeMillis();
                pl.itemTime.isUseBoKhi2 = true;
                updatePoint = true;
                break;
            case ConstItem.GIAP_XEN_BO_HUNG_2: //giáp xên 2
                pl.itemTime.lastTimeGiapXen2 = System.currentTimeMillis();
                pl.itemTime.isUseGiapXen2 = true;
                updatePoint = true;
                break;
            case ConstItem.CUONG_NO_2: //cuồng nộ 2
                pl.itemTime.lastTimeCuongNo2 = System.currentTimeMillis();
                pl.itemTime.isUseCuongNo2 = true;
                updatePoint = true;
                break;
            case 379: //máy dò
                pl.itemTime.lastTimeUseMayDo = System.currentTimeMillis();
                pl.itemTime.isUseMayDo = true;
                break;
            case 1860: //vợt cá thường
                pl.itemTime.lastTimeUseVotCaThuong = System.currentTimeMillis();
                pl.itemTime.isUseVotCaThuong = true;
                break;
            case 1861: //vợt cá vip
                pl.itemTime.lastTimeUseVotCaVip = System.currentTimeMillis();
                pl.itemTime.isUseVotCaVip = true;
                break;
            case 663: //bánh pudding
            case 664: //xúc xíc
            case 665: //kem dâu
            case 666: //mì ly
            case 667: //sushi
                pl.itemTime.lastTimeEatMeal = System.currentTimeMillis();
                pl.itemTime.isEatMeal = true;
                ItemTimeService.gI().removeItemTime(pl, pl.itemTime.iconMeal);
                pl.itemTime.iconMeal = item.template.iconID;
                updatePoint = true;
                break;
            case ConstItem.BANH_CHUNG_CHIN:
                pl.itemTime.lastTimeBanhChung = System.currentTimeMillis();
                pl.itemTime.isUseBanhChung = true;
                updatePoint = true;
                break;
            case ConstItem.BANH_TET_CHIN:
                pl.itemTime.lastTimeBanhTet = System.currentTimeMillis();
                pl.itemTime.isUseBanhTet = true;
                updatePoint = true;
                break;
            case 1317:// cn
                pl.itemTimesieucap.lastTimeUseXiMuoi = System.currentTimeMillis();
                pl.itemTimesieucap.isUseXiMuoi = true;
                updatePoint = true;
                break;
            case 1385:
                pl.itemTimesieucap.lastTimeCaRot = System.currentTimeMillis();
                pl.itemTimesieucap.isUseCaRot = true;
                updatePoint = true;
                break;
            case 899:
                pl.itemTimesieucap.lastTimeKeo = System.currentTimeMillis();
                pl.itemTimesieucap.isKeo = true;
                updatePoint = true;
                break;
            case 1386:
                pl.itemTimesieucap.isCuongBao = true;
                pl.itemTimesieucap.lastTimeCuongBao = System.currentTimeMillis();
                updatePoint = true;
                break;
            case 579: // đuôi khỉ
                pl.itemTimesieucap.lastTimeDuoikhi = System.currentTimeMillis();
                pl.itemTimesieucap.isDuoikhi = true;
                updatePoint = true;
                break;
            case 1201: //Đá ngục tù
                pl.itemTimesieucap.lastTimeDaNgucTu = System.currentTimeMillis();
                pl.itemTimesieucap.isDaNgucTu = true;
                updatePoint = true;
                break;
            // bánh trung thu
            case 465:
            case 466:
            case 472:
            case 473:
                pl.itemTimesieucap.lastTimeUseBanh = System.currentTimeMillis();
                pl.itemTimesieucap.isUseTrungThu = true;
                ItemTimeService.gI().removeItemTime(pl, pl.itemTimesieucap.iconBanh);
                pl.itemTimesieucap.iconBanh = item.template.iconID;
                updatePoint = true;
                break;
        }
        if (updatePoint) {
            Service.getInstance().point(pl);
        }
        ItemTimeService.gI().sendAllItemTime(pl);
        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
        InventoryService.gI().sendItemBags(pl);
    }

    private void controllerCallRongThan(Player pl, Item item) {
        int tempId = item.template.id;
        if (tempId == SummonDragon.NGOC_RONG_SIEU_CAP) {
            SummonDragon.gI().openMenuSummonShenron(pl, (byte) 1015, SummonDragon.DRAGON_BLACK_SHENRON);
        }
    }

    private byte LevelSach(int id) {
        switch (id) {
            case 1560:
            case 1566:
            case 1572:
                return 1;
            case 1561:
            case 1567:
            case 1573:
                return 2;
            case 1562:
            case 1568:
            case 1574:
                return 3;
            case 1563:
            case 1569:
            case 1575:
                return 4;
            case 1564:
            case 1570:
            case 1576:
                return 5;
            case 1565:
            case 1571:
            case 1577:
                return 6;
        }
        return -1;
    }

    private void learnSkillBienHinh(Player player, Item item) {
        Message msg;
        try {
            if (item.template.gender == player.gender || item.template.gender == 3) {
                Skill curSkill = SkillUtil.getSkillbyId(player, Skill.BIEN_HINH);
                Skill curSkillTT = SkillUtil.getSkillbyId(player, Skill.TAI_TAO_SUC_MANH);
                byte levelSach = LevelSach(item.template.id);
                if (curSkill.point == 6) {
                    Service.getInstance().sendThongBao(player, "Kỹ năng đã đạt tối đa!");
                } else {
                    if (curSkill.point == 0) {
                        if (levelSach == 1) {
                            if (curSkillTT.point == 0) {
                                curSkillTT = SkillUtil.createSkill(Skill.TAI_TAO_SUC_MANH, 1);
                                SkillUtil.setSkill(player, curSkillTT);
                                DataGame.updateSkillPlayer(player);
                                msg = Service.getInstance().messageSubCommand((byte) 23);
                                msg.writer().writeShort(curSkillTT.skillId);
                                player.sendMessage(msg);
                                msg.cleanup();
                            }
                            curSkill = SkillUtil.createSkill(Skill.BIEN_HINH, 1);
                            SkillUtil.setSkill(player, curSkill);
                            InventoryService.gI().subQuantityItemsBag(player, item, 1);
                            DataGame.updateSkillPlayer(player);
                            msg = Service.getInstance().messageSubCommand((byte) 23);
                            msg.writer().writeShort(curSkill.skillId);
                            player.sendMessage(msg);
                            msg.cleanup();
                            DataGame.updateSkillPlayer(player);
                            Service.getInstance().player(player);
                            player.zone.loadAnotherToMe(player);
                            player.zone.load_Me_To_Another(player);
                            Service.getInstance().sendFlagBag(player);
                            Service.getInstance().Send_Caitrang(player);
                            PlayerService.gI().sendInfoHpMpMoney(player);
                            Service.getInstance().Send_Info_NV(player);
                            ItemTimeService.gI().sendAllItemTime(player);
                            Service.getInstance().sendTimeSkill(player);
                        } else {
                            Service.getInstance().sendThongBao(player, "Vui lòng học " + DataGame.NAME_BIENHINH[player.gender][0] + " trước!");
                        }
                    } else {
                        if (curSkill.point + 1 == levelSach) {
                            if (curSkillTT.point == 0) {
                                curSkillTT = SkillUtil.createSkill(Skill.TAI_TAO_SUC_MANH, 1);
                                SkillUtil.setSkill(player, curSkillTT);
                                DataGame.updateSkillPlayer(player);
                                msg = Service.getInstance().messageSubCommand((byte) 23);
                                msg.writer().writeShort(curSkillTT.skillId);
                                player.sendMessage(msg);
                                msg.cleanup();
                            }
                            curSkill = SkillUtil.createSkill(Skill.BIEN_HINH, curSkill.point + 1);
                            SkillUtil.setSkill(player, curSkill);
                            InventoryService.gI().subQuantityItemsBag(player, item, 1);
                            DataGame.updateSkillPlayer(player);
                            msg = Service.getInstance().messageSubCommand((byte) 62);
                            msg.writer().writeShort(curSkill.skillId);
                            player.sendMessage(msg);
                            msg.cleanup();
                            DataGame.updateSkillPlayer(player);
                            Service.getInstance().player(player);
                            player.zone.loadAnotherToMe(player);
                            player.zone.load_Me_To_Another(player);
                            Service.getInstance().sendFlagBag(player);
                            Service.getInstance().Send_Caitrang(player);
                            PlayerService.gI().sendInfoHpMpMoney(player);
                            Service.getInstance().Send_Info_NV(player);
                            ItemTimeService.gI().sendAllItemTime(player);
                            Service.getInstance().sendTimeSkill(player);
                        } else {
                            Service.getInstance().sendThongBao(player, "Vui lòng học " + DataGame.NAME_BIENHINH[player.gender][curSkill.point] + " trước!");
                        }
                    }
                    InventoryService.gI().sendItemBags(player);
                }
            } else {
                Service.getInstance().sendThongBao(player, "Không thể thực hiện");

            }
        } catch (Exception e) {
        }
    }

    private void learnSkill(Player pl, Item item) {
        Message msg;
        try {
            if (item.template.gender == pl.gender || item.template.gender == 3) {
                String[] subName = item.template.name.split("");
                byte level = Byte.parseByte(subName[subName.length - 1]);
                Skill curSkill = SkillUtil.getSkillByItemID(pl, item.template.id);
                int check = 7;
                if (curSkill == SkillUtil.getSkillbyId(pl, Skill.SUPER_KAME)
                        || curSkill == SkillUtil.getSkillbyId(pl, Skill.MA_PHONG_BA)
                        || curSkill == SkillUtil.getSkillbyId(pl, Skill.LIEN_HOAN_CHUONG)) {
                    check = 9;
                }
                if (curSkill == null) {
                    Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
                    return;
                }
                if (curSkill.point == check) {
                    Service.getInstance().sendThongBao(pl, "Kỹ năng đã đạt tối đa!");
                } else {
                    if (curSkill.point == 0) {
                        if (level == 1) {
                            curSkill = SkillUtil.createSkill(SkillUtil.getTempSkillSkillByItemID(item.template.id), level);
                            SkillUtil.setSkill(pl, curSkill);
                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                            msg = Service.getInstance().messageSubCommand((byte) 23);
                            msg.writer().writeShort(curSkill.skillId);
                            pl.sendMessage(msg);
                            msg.cleanup();
                        } else {
                            Skill skillNeed = SkillUtil.createSkill(SkillUtil.getTempSkillSkillByItemID(item.template.id), level);
                            Service.getInstance().sendThongBao(pl, "Vui lòng học " + skillNeed.template.name + " cấp " + skillNeed.point + " trước!");
                        }
                    } else {
                        if (curSkill.point + 1 == level) {
                            curSkill = SkillUtil.createSkill(SkillUtil.getTempSkillSkillByItemID(item.template.id), level);
                            //System.out.println(curSkill.template.name + " - " + curSkill.point);
                            SkillUtil.setSkill(pl, curSkill);
                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                            msg = Service.getInstance().messageSubCommand((byte) 62);
                            msg.writer().writeShort(curSkill.skillId);
                            pl.sendMessage(msg);
                            msg.cleanup();
                        } else {
                            Service.getInstance().sendThongBao(pl, "Vui lòng học " + curSkill.template.name + " cấp " + (curSkill.point + 1) + " trước!");
                        }
                    }
                    InventoryService.gI().sendItemBags(pl);
                }
            } else {
                Service.getInstance().sendThongBao(pl, "Không thể thực hiện");

            }
        } catch (Exception e) {
            Log.error(UseItem.class, e);
        }
    }

    private void useTDLT(Player pl, Item item) {
        if (pl.itemTime.isUseTDLT) {
            ItemTimeService.gI().turnOffTDLT(pl, item);
        } else {
            ItemTimeService.gI().turnOnTDLT(pl, item);
        }
    }

    private void usePorata(Player pl) {
        if (pl.pet == null || pl.fusion.typeFusion == 4) {
            Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
        } else {
            if (pl.fusion.typeFusion == ConstPlayer.NON_FUSION) {
                pl.pet.fusion(true);
            } else {
                pl.pet.unFusion();
            }
        }
    }

    private void usePorata2(Player pl) {
        if (pl.pet == null || pl.fusion.typeFusion == 4) {
            Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
        } else {
            if (pl.fusion.typeFusion == ConstPlayer.NON_FUSION) {
                for (Item item : pl.inventory.itemsBag) {
                    if (item.isNotNullItem() && (item.template.id == 1550 || item.template.id == 1129 || item.template.id == 1165
                            || item.template.id == 921 || item.template.id == 1999 || item.template.id == 1944 || item.template.id == 1945 || item.template.id == 1946)) {
                        for (ItemOption io : item.itemOptions) {
                            switch (io.optionTemplate.id) {
                                case 248: //ngoại hình
                                    pl.nPoint.isNgoaiHinh = true;
                                    break;
                            }
                        }
                        break;
                    }
                }
                pl.pet.fusion2(true);
            } else {
                pl.pet.unFusion();
            }
        }
    }

    private void usePorata3(Player pl) {
        if (pl.pet == null || pl.fusion.typeFusion == 4) {
            Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
        } else {
            if (pl.fusion.typeFusion == ConstPlayer.NON_FUSION) {
                for (Item item : pl.inventory.itemsBag) {
                    if (item.isNotNullItem() && (item.template.id == 1550 || item.template.id == 1129 || item.template.id == 1165
                            || item.template.id == 921 || item.template.id == 1999 || item.template.id == 1944 || item.template.id == 1945 || item.template.id == 1946)) {
                        for (ItemOption io : item.itemOptions) {
                            switch (io.optionTemplate.id) {
                                case 248: //ngoại hình
                                    pl.nPoint.isNgoaiHinh = true;
                                    break;
                            }
                        }
                        break;
                    }
                }
                pl.pet.fusion3(true);
            } else {
                pl.pet.unFusion();
            }
        }
    }

    private void usePorata4(Player pl) {
        if (pl.pet == null || pl.fusion.typeFusion == 4) {
            Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
        } else {
            if (pl.fusion.typeFusion == ConstPlayer.NON_FUSION) {
                for (Item item : pl.inventory.itemsBag) {
                    if (item.isNotNullItem() && (item.template.id == 1550 || item.template.id == 1129 || item.template.id == 1165
                            || item.template.id == 921 || item.template.id == 1999 || item.template.id == 1944 || item.template.id == 1945 || item.template.id == 1946)) {
                        for (ItemOption io : item.itemOptions) {
                            switch (io.optionTemplate.id) {
                                case 248: //ngoại hình
                                    pl.nPoint.isNgoaiHinh = true;
                                    break;
                            }
                        }
                        break;
                    }
                }
                pl.pet.fusion4(true);
            } else {
                pl.pet.unFusion();
            }
        }
    }

    private void usePorata5(Player pl) {
        if (pl.pet == null || pl.fusion.typeFusion == 4) {
            Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
        } else {
            if (pl.fusion.typeFusion == ConstPlayer.NON_FUSION) {
                for (Item item : pl.inventory.itemsBag) {
                    if (item.isNotNullItem() && (item.template.id == 1550 || item.template.id == 1129 || item.template.id == 1165
                            || item.template.id == 921 || item.template.id == 1999 || item.template.id == 1944 || item.template.id == 1945 || item.template.id == 1946)) {
                        for (ItemOption io : item.itemOptions) {
                            switch (io.optionTemplate.id) {
                                case 248: //ngoại hình
                                    pl.nPoint.isNgoaiHinh = true;
                                    break;
                            }
                        }
                        break;
                    }
                }
                pl.pet.fusion5(true);
            } else {
                pl.pet.unFusion();
            }
            RadaService.getInstance().setIDAuraEff(pl, pl.getAura());
        }
    }

    private void openCapsuleUI(Player pl) {
        if (pl.isHoldNamecBall) {
            NamekBallWar.gI().dropBall(pl);
            Service.getInstance().sendFlagBag(pl);
        }
        pl.iDMark.setTypeChangeMap(ConstMap.CHANGE_CAPSULE);
        ChangeMapService.gI().openChangeMapTab(pl);
    }

    public void choseMapCapsule(Player pl, int index) {
        int zoneId = -1;
        if (index < 0 || index >= pl.mapCapsule.size()) {
            return;
        }
        Zone zoneChose = pl.mapCapsule.get(index);
        if (index != 0 || zoneChose.map.mapId == 21 || zoneChose.map.mapId == 22 || zoneChose.map.mapId == 23) {
            if (!(pl.zone != null && pl.zone instanceof ZSnakeRoad)) {
                pl.mapBeforeCapsule = pl.zone;
            } else {
                pl.mapBeforeCapsule = null;
            }
        } else {
            zoneId = pl.mapBeforeCapsule != null ? pl.mapBeforeCapsule.zoneId : -1;
            pl.mapBeforeCapsule = null;
        }
        ChangeMapService.gI().changeMapBySpaceShip(pl, pl.mapCapsule.get(index).map.mapId, zoneId, -1);
    }

    private void doiskill4(Player pl, Item item) {
        if (pl.pet.nPoint.power > 20000000000L) {
            if (pl.pet != null) {
                if (pl.pet.playerSkill.skills.get(2).skillId != -1) {
                    pl.pet.openSkill4();
                    Service.getInstance().chatJustForMe(pl, pl.pet, "Cảm ơn sư phụ");
                    InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                    InventoryService.gI().sendItemBags(pl);
                } else {
                    Service.getInstance().sendThongBao(pl, "Ít nhất đệ tử ngươi phải có chiêu 3 chứ!");
                }
            } else {
                Service.getInstance().sendThongBao(pl, "Ngươi làm gì có đệ tử?");
            }
        } else {
            Service.getInstance().sendThongBao(pl, "Yêu cầu đệ tử có skill 4");
        }
    }

    private void upSkillPet(Player pl, Item item) {
        if (pl.pet == null) {
            Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
            return;
        }
        try {
            switch (item.template.id) {
                case 402: //skill 1
                    if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 0)) {
                        Service.getInstance().chatJustForMe(pl, pl.pet, "Cảm ơn sư phụ");
                        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                    } else {
                        Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
                    }
                    break;
                case 403: //skill 2
                    if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 1)) {
                        Service.getInstance().chatJustForMe(pl, pl.pet, "Cảm ơn sư phụ");
                        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                    } else {
                        Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
                    }
                    break;
                case 404: //skill 3
                    if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 2)) {
                        Service.getInstance().chatJustForMe(pl, pl.pet, "Cảm ơn sư phụ");
                        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                    } else {
                        Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
                    }
                    break;
                case 759: //skill 4
                    if (SkillUtil.upSkillPet(pl.pet.playerSkill.skills, 3)) {
                        Service.getInstance().chatJustForMe(pl, pl.pet, "Cảm ơn sư phụ");
                        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                    } else {
                        Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
                    }
                    break;
            }
        } catch (Exception e) {
            Service.getInstance().sendThongBao(pl, "Không thể thực hiện");
        }
    }
}
