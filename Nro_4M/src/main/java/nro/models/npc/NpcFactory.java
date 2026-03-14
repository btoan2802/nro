package nro.models.npc;

import java.sql.Connection;
import nro.attr.Attribute;
import nro.attr.AttributeManager;
import nro.consts.*;
import nro.dialog.ConfirmDialog;
import nro.dialog.MenuDialog;
import nro.jdbc.daos.PlayerDAO;
import nro.lib.RandomCollection;
import nro.models.boss.Boss;
import nro.models.boss.BossFactory;
import nro.models.boss.BossManager;
import nro.models.boss.event.EscortedBoss;
import nro.models.boss.event.Qilin;
import nro.models.clan.Clan;
import nro.models.clan.ClanMember;
import nro.models.consignment.ConsignmentShop;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.item.ItemTemplate;
import nro.models.map.ItemMap;
import nro.models.map.Map;
import nro.models.map.Zone;
import nro.models.map.challenge.MartialCongressService;
import nro.models.map.dungeon.SnakeRoad;
import nro.models.map.dungeon.zones.ZSnakeRoad;
import nro.models.map.mabu.MabuWar;
import nro.models.map.phoban.BanDoKhoBau;
import nro.models.map.phoban.DoanhTrai;
import nro.models.map.war.BlackBallWar;
import nro.models.map.war.NamekBallWar;
import nro.models.player.NPoint;
import nro.models.player.Player;
import nro.models.skill.Skill;
import nro.noti.NotiManager;
import nro.server.Maintenance;
import nro.server.Manager;
import nro.server.ServerManager;
import nro.server.io.Message;
import nro.services.*;
import nro.services.func.*;
import nro.utils.Log;
import nro.utils.SkillUtil;
import nro.utils.TimeUtil;
import nro.utils.Util;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import nro.jdbc.DBService;
import nro.manager.TopManager;
import nro.models.boss.BossData;
import nro.models.boss.bill.Boss_Leo_Thap;
import nro.models.map.ChungSinhBinhDang.ChungSinhBinhDang;
import nro.models.map.mabu.MabuWar14h;
import nro.models.phuban.DragonNamecWar.TranhNgoc;
import nro.models.phuban.DragonNamecWar.TranhNgocService;
import nro.models.player.Pet;
import nro.models.pvp.TOP;

import static nro.server.Manager.*;
import static nro.services.func.LuckyRoundService.USING_TV;
import static nro.services.func.SummonDragon.*;

public class NpcFactory {

    private static boolean nhanVang = true;
    private static boolean nhanDeTu = true;

    public static final java.util.Map<Long, Object> PLAYERID_OBJECT = new HashMap<Long, Object>();

    public static Npc createNPC(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        Npc npc = null;
        try {
            switch (tempId) {
                case ConstNpc.FIDE:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (player.iDMark.getTranhNgoc() == 1) {
                                this.createOtherMenu(player, ConstNpc.BASE_MENU, "Cút!Ta không nói chuyện với sinh vật hạ đẳng", "Đóng");
                                return;
                            }
                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "Hãy mang ngọc rồng về cho ta", "Đưa ngọc", "Đóng");
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                switch (select) {
                                    case 0:
                                        if (player.iDMark.getTranhNgoc() == 2 && player.isHoldNamecBallTranhDoat) {
                                            if (!Util.canDoWithTime(player.lastTimePickItem, 40000)) {
                                                Service.getInstance().sendThongBao(player, "Vui lòng đợi " + ((player.lastTimePickItem + 40000 - System.currentTimeMillis()) / 1000) + " giây để có thể trả");
                                                return;
                                            }
                                            TranhNgocService.getInstance().dropBall(player, (byte) 2);
                                            player.zone.pointFide++;
                                            if (player.zone.pointFide > ConstTranhNgocNamek.MAX_POINT) {
                                                player.zone.pointFide = ConstTranhNgocNamek.MAX_POINT;
                                            }
                                            TranhNgocService.getInstance().sendUpdatePoint(player);
                                        }
                                        break;
                                    case 1:
                                        break;
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.CADIC:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (player.iDMark.getTranhNgoc() == 2) {
                                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                        "Cút!Ta không nói chuyện với sinh vật hạ đẳng", "Đóng");
                                return;
                            }
                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "Hãy mang ngọc rồng về cho ta", "Đưa ngọc", "Đóng");
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                switch (select) {
                                    case 0:
                                        if (player.iDMark.getTranhNgoc() == 1 && player.isHoldNamecBallTranhDoat) {
                                            if (!Util.canDoWithTime(player.lastTimePickItem, 40000)) {
                                                Service.getInstance().sendThongBao(player, "Vui lòng đợi " + ((player.lastTimePickItem + 40000 - System.currentTimeMillis()) / 1000) + " giây để có thể trả");
                                                return;
                                            }
                                            TranhNgocService.getInstance().dropBall(player, (byte) 1);
                                            player.zone.pointCadic++;
                                            if (player.zone.pointCadic > ConstTranhNgocNamek.MAX_POINT) {
                                                player.zone.pointCadic = ConstTranhNgocNamek.MAX_POINT;
                                            }
                                            TranhNgocService.getInstance().sendUpdatePoint(player);
                                        }
                                        break;
                                    case 1:
                                        break;
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.TORIBOT:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "Chào mừng bạn đến với cửa hàng đá qúy số 1 thời đại", "Cửa Hàng");
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                ShopService.gI().openShopSpecial(player, this, ConstNpc.SHOP_TORIBOT, 0, -1);
                            }
                        }
                    };
                    break;
                case ConstNpc.NGO_KHONG:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU, "Chu mi nga", "Tặng quả\nHồng đào\nChín");
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                int itemNeed = ConstItem.QUA_HONG_DAO_CHIN;
                                Item item = InventoryService.gI().findItemBagByTemp(player, itemNeed);
                                if (item != null) {
                                    RandomCollection<Integer> rc = Manager.HONG_DAO_CHIN;
                                    int itemID = rc.next();
                                    int x = cx + Util.nextInt(-50, 50);
                                    int y = player.zone.map.yPhysicInTop(x, cy - 24);
                                    int quantity = 1;
                                    if (itemID == ConstItem.HONG_NGOC) {
                                        quantity = Util.nextInt(1, 2);
                                    }
                                    InventoryService.gI().subQuantityItemsBag(player, item, 1);
                                    InventoryService.gI().sendItemBags(player);
                                    ItemMap itemMap = new ItemMap(player.zone, itemID, quantity, x, y, player.id);
                                    Service.getInstance().dropItemMap(player.zone, itemMap);
                                    npcChat(player.zone, "Xie xie");
                                } else {
                                    Service.getInstance().sendThongBao(player, "Không tìm thấy!");
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.DUONG_TANG:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (this.mapId == MapName.LANG_ARU) {
                                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                        "|7|NGŨ HÀNH SƠN"
                                        + "\n|2|A mi phò phò, thí chủ hãy giúp giải cứu đồ đệ của bần tăng đang bị phong ấn tại ngũ hành sơn."
                                        + "\n|3|Tại đây sức mạnh dưới 16 Tỷ đánh quái được x2 TNSM",
                                        "Đồng ý", "Từ chối");
                            }
                            if (this.mapId == MapName.NGU_HANH_SON_3) {
                                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                        "A mi phò phò, thí chủ hãy thu thập bùa 'giải khai phong ấn', mỗi chữ 10 cái.",
                                        "Về\nLàng Aru", "Từ chối");
                            }
                            if (this.mapId == MapName.NGU_HANH_SON) {
                                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                        "A mi phò phò, thí chủ hãy thu thập bùa 'giải khai phong ấn', mỗi chữ 10 cái.",
                                        "Đổi đào chín", "Giải phong ấn", "Từ chối");
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == MapName.LANG_ARU) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:

                                                if (!Manager.gI().getGameConfig().isOpenPrisonPlanet()) {
                                                    Service.getInstance().sendThongBao(player,
                                                            "Lối vào ngũ hành sơn chưa mở");
                                                    return;
                                                }

                                                Zone zone = MapService.gI().getZoneJoinByMapIdAndZoneId(player, 124, 0);
                                                if (zone != null) {
                                                    player.location.x = 100;
                                                    player.location.y = 384;
                                                    MapService.gI().goToMap(player, zone);
                                                    Service.getInstance().clearMap(player);
                                                    zone.mapInfo(player);
                                                    player.zone.loadAnotherToMe(player);
                                                    player.zone.load_Me_To_Another(player);
                                                }
                                                // Service.getInstance().sendThongBao(player, "Lối vào ngũ hành sơn chưa
                                                // mở");
                                                break;
                                        }
                                    }
                                }
                                if (this.mapId == MapName.NGU_HANH_SON_3) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                Zone zone = MapService.gI().getZoneJoinByMapIdAndZoneId(player, 0, 0);
                                                if (zone != null) {
                                                    player.location.x = 600;
                                                    player.location.y = 432;
                                                    MapService.gI().goToMap(player, zone);
                                                    Service.getInstance().clearMap(player);
                                                    zone.mapInfo(player);
                                                    player.zone.loadAnotherToMe(player);
                                                    player.zone.load_Me_To_Another(player);
                                                }
                                                break;
                                        }
                                    }
                                }
                                if (this.mapId == MapName.NGU_HANH_SON) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                // Đổi đào
                                                Item item = InventoryService.gI().findItemBagByTemp(player,
                                                        ConstItem.QUA_HONG_DAO);
                                                if (item == null || item.quantity < 10) {
                                                    npcChat(player,
                                                            "Cần 10 quả đào xanh để đổi lấy đào chín từ bần tăng.");
                                                    return;
                                                }
                                                if (InventoryService.gI().getCountEmptyBag(player) == 0) {
                                                    npcChat(player, "Túi đầy rồi kìa.");
                                                    return;
                                                }
                                                Item newItem = ItemService.gI()
                                                        .createNewItem((short) ConstItem.QUA_HONG_DAO_CHIN, 1);
                                                InventoryService.gI().subQuantityItemsBag(player, item, 10);
                                                InventoryService.gI().addItemBag(player, newItem, 0);
                                                InventoryService.gI().sendItemBags(player);
                                                npcChat(player,
                                                        "Ta đã đổi cho thí chủ rồi đó, hãy mang cho đệ tử ta đi nào.");
                                                break;

                                            case 1:
                                                // giải phong ấn
                                                if (InventoryService.gI().getCountEmptyBag(player) == 0) {
                                                    npcChat(player, "Túi đầy rồi kìa.");
                                                    return;
                                                }
                                                int[] itemsNeed = {ConstItem.CHU_GIAI, ConstItem.CHU_KHAI,
                                                    ConstItem.CHU_PHONG, ConstItem.CHU_AN};
                                                List<Item> items = InventoryService.gI().getListItem(player, itemsNeed)
                                                        .stream().filter(i -> i.quantity >= 10)
                                                        .collect(Collectors.toList());
                                                boolean[] flags = new boolean[4];
                                                for (Item i : items) {
                                                    switch ((int) i.template.id) {
                                                        case ConstItem.CHU_GIAI:
                                                            flags[0] = true;
                                                            break;

                                                        case ConstItem.CHU_KHAI:
                                                            flags[1] = true;
                                                            break;

                                                        case ConstItem.CHU_PHONG:
                                                            flags[2] = true;
                                                            break;

                                                        case ConstItem.CHU_AN:
                                                            flags[3] = true;
                                                            break;
                                                    }
                                                }
                                                for (int i = 0; i < flags.length; i++) {
                                                    if (!flags[i]) {
                                                        ItemTemplate template = ItemService.gI()
                                                                .getTemplate(itemsNeed[i]);
                                                        npcChat("Thí chủ còn thiếu " + template.name);
                                                        return;
                                                    }
                                                }

                                                for (Item i : items) {
                                                    InventoryService.gI().subQuantityItemsBag(player, i, 10);
                                                }

                                                RandomCollection<Integer> rc = new RandomCollection<>();
                                                rc.add(10, ConstItem.CAI_TRANG_TON_NGO_KHONG_DE_TU);
                                                rc.add(10, ConstItem.CAI_TRANG_BAT_GIOI_DE_TU);
                                                rc.add(50, ConstItem.GAY_NHU_Y);
                                                switch (player.gender) {
                                                    case ConstPlayer.TRAI_DAT:
                                                        rc.add(30, ConstItem.CAI_TRANG_TON_NGO_KHONG);
                                                        break;

                                                    case ConstPlayer.NAMEC:
                                                        rc.add(30, ConstItem.CAI_TRANG_TON_NGO_KHONG_545);
                                                        break;

                                                    case ConstPlayer.XAYDA:
                                                        rc.add(30, ConstItem.CAI_TRANG_TON_NGO_KHONG_546);
                                                        break;
                                                }
                                                int itemID = rc.next();
                                                Item nItem = ItemService.gI().createNewItem((short) itemID);
                                                boolean all = itemID == ConstItem.CAI_TRANG_TON_NGO_KHONG_DE_TU
                                                        || itemID == ConstItem.CAI_TRANG_BAT_GIOI_DE_TU
                                                        || itemID == ConstItem.CAI_TRANG_TON_NGO_KHONG
                                                        || itemID == ConstItem.CAI_TRANG_TON_NGO_KHONG_545
                                                        || itemID == ConstItem.CAI_TRANG_TON_NGO_KHONG_546;
                                                if (all) {
                                                    nItem.itemOptions.add(new ItemOption(50, Util.nextInt(20, 35)));
                                                    nItem.itemOptions.add(new ItemOption(77, Util.nextInt(20, 35)));
                                                    nItem.itemOptions.add(new ItemOption(103, Util.nextInt(20, 35)));
                                                    nItem.itemOptions.add(new ItemOption(94, Util.nextInt(5, 10)));
                                                    nItem.itemOptions.add(new ItemOption(100, Util.nextInt(10, 20)));
                                                    nItem.itemOptions.add(new ItemOption(101, Util.nextInt(10, 20)));
                                                }
                                                if (itemID == ConstItem.CAI_TRANG_TON_NGO_KHONG
                                                        || itemID == ConstItem.CAI_TRANG_TON_NGO_KHONG_545
                                                        || itemID == ConstItem.CAI_TRANG_TON_NGO_KHONG_546) {
                                                    nItem.itemOptions.add(new ItemOption(80, Util.nextInt(5, 15)));
                                                    nItem.itemOptions.add(new ItemOption(81, Util.nextInt(5, 15)));
                                                    nItem.itemOptions.add(new ItemOption(106, 0));
                                                } else if (itemID == ConstItem.CAI_TRANG_TON_NGO_KHONG_DE_TU
                                                        || itemID == ConstItem.CAI_TRANG_BAT_GIOI_DE_TU) {
                                                    nItem.itemOptions.add(new ItemOption(197, 0));
                                                }
                                                if (all) {
                                                    if (Util.isTrue(499, 500)) {
                                                        nItem.itemOptions.add(new ItemOption(93, Util.nextInt(3, 30)));
                                                    }
                                                } else if (itemID == ConstItem.GAY_NHU_Y) {
                                                    RandomCollection<Integer> rc2 = new RandomCollection<>();
                                                    rc2.add(60, 30);
                                                    rc2.add(30, 90);
                                                    rc2.add(10, 365);
                                                    nItem.itemOptions.add(new ItemOption(93, rc2.next()));
                                                }
                                                InventoryService.gI().addItemBag(player, nItem, 0);
                                                InventoryService.gI().sendItemBags(player);
                                                npcChat(player.zone,
                                                        "A mi phò phò, đa tạ thí chủ tương trợ, xin hãy nhận món quà mọn này, bần tăng sẽ niệm chú giải thoát cho Ngộ Không");
                                                break;
                                        }
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.MR_POPO:

                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 0) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                            "Ta là người đang giữ rương quà cho ngươi, nếu có bất kì món quà nào hãy tới gặp ta để nhận."
                                            + "\n Nhớ nhận ngay để không bị mất khi có quà mới nhé!",
                                            "Rương\nQuà tặng", "Bảng\n xếp hạng", "Từ chối");
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 0) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                ShopService.gI().openBoxItemReward(player);
                                                break;
                                            case 1:
                                                Service.getInstance().showTopPower(player, Service.getInstance().TOP_SUCMANH);
                                                break;
                                        }
                                    }
                                }
                            }
                        }
                    };

                    break;
                case ConstNpc.LY_TIEU_NUONG:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                createOtherMenu(player, ConstNpc.BASE_MENU,
                                        "\b|8|Trò chơi Tài Xỉu đang được diễn ra\n\n|6|Thử vận may của bạn với trò chơi Tài Xỉu! Đặt cược và dự đoán đúng"
                                        + "\n kết quả, bạn sẽ được nhận thưởng lớn. Hãy tham gia ngay và\n cùng trải nghiệm sự hồi hộp, thú vị trong trò chơi này!"
                                        + "\n\n|7|(Điều kiện tham gia : Nhiệm vụ Fide)"
                                        + "\nCược tối đa 2 Tỷ cho Thỏi vàng"
                                        + "\nTối đa 10tr cho Hồng ngọc"
                                        + "\n\n|7| Lưu ý : Thoát game sẽ MẤT Tiền cược",
                                        "Thể lệ",
                                        "Cược\nThỏi Vàng",
                                        "Cược\nHồng Ngọc");
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (player.iDMark.isBaseMenu()) {
                                    switch (select) {
                                        case 0:
                                            createOtherMenu(player, ConstNpc.IGNORE_MENU, "|5|Có 2 nhà cái Tài và Xĩu, bạn chỉ được chọn 1 nhà để tham gia"
                                                    + "\n\n|6|Sau khi kết thúc thời gian đặt cược. Hệ thống sẽ tung xí ngầu để biết kết quả Tài Xỉu"
                                                    + "\n\nNếu Tổng số 3 con xí ngầu <=10 : XỈU\nNếu Tổng số 3 con xí ngầu >10 : TÀI"
                                                    + "\n\n|7|Lưu ý: Phần thưởng nhận được sẽ bị nhà cái lụm đi 10%. Trong quá trình diễn ra khi đặt cược nếu thoát game sẽ bị MẤT TIỀN ĐẶT CƯỢC", "Ok");
                                            break;
                                        case 1:
                                            String time = ((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) + " giây";
                                            if (TaiXiu.gI().baotri == false) {
                                                if (player.goldTai == 0 && player.goldXiu == 0) {
                                                    NpcService.gI().createMenuConMeo(player, ConstNpc.TAIXIU, 9077, "\n|7|---NHÀ CÁI TÀI XỈU---"
                                                            + "\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z + TaiXiu.gI().ketqua
                                                            + "\n|4|Kết quả 10 lần gần nhất:  " + TaiXiu.gI().InKetQua()
                                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Thỏi vàng"
                                                            + "\n\nTổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Thỏi vàng\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Theo TÀI", "Theo XỈU", "Đóng");
                                                } else if (player.goldTai > 0) {
                                                    NpcService.gI().createMenuConMeo(player, ConstNpc.TAIXIU, 9077, "\n|7|---NHÀ CÁI TÀI XỈU---"
                                                            + "\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z + TaiXiu.gI().ketqua
                                                            + "\n|4|Kết quả 10 lần gần nhất:  " + TaiXiu.gI().InKetQua()
                                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Thỏi vàng"
                                                            + "\n\nTổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Thỏi vàng\n\n|5|Thời gian còn lại: " + time + "\n\n|7|Bạn đã cược Tài : " + Util.format(player.goldTai) + " Thỏi vàng", "Cập nhập", "Đóng");
                                                } else {
                                                    NpcService.gI().createMenuConMeo(player, ConstNpc.TAIXIU, 9077, "\n|7|---NHÀ CÁI TÀI XỈU---"
                                                            + "\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z + TaiXiu.gI().ketqua
                                                            + "\n|4|Kết quả 10 lần gần nhất:  " + TaiXiu.gI().InKetQua()
                                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Thỏi vàng"
                                                            + "\n\nTổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Thỏi vàng\n\n|5|Thời gian còn lại: " + time + "\n\n|7|Bạn đã cược Xỉu : " + Util.format(player.goldXiu) + " Thỏi vàng", "Cập nhập", "Đóng");
                                                }
                                            } else {
                                                if (player.goldTai == 0 && player.goldXiu == 0) {
                                                    NpcService.gI().createMenuConMeo(player, ConstNpc.TAIXIU, 9077, "\n|7|---NHÀ CÁI TÀI XỈU---"
                                                            + "\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z + TaiXiu.gI().ketqua
                                                            + "\n|4|Kết quả 10 lần gần nhất:  " + TaiXiu.gI().InKetQua()
                                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Thỏi vàng"
                                                            + "\n\nTổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Thỏi vàng\n\n|5|Thời gian còn lại: " + time + "\n\n|7|Hệ thống sắp bảo trì", "Cập nhập", "Đóng");
                                                } else if (player.goldTai > 0) {
                                                    NpcService.gI().createMenuConMeo(player, ConstNpc.TAIXIU, 9077, "\n|7|---NHÀ CÁI TÀI XỈU---"
                                                            + "\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z + TaiXiu.gI().ketqua
                                                            + "\n|4|Kết quả 10 lần gần nhất:  " + TaiXiu.gI().InKetQua()
                                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Thỏi vàng"
                                                            + "\n\nTổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Thỏi vàng\n\n|5|Thời gian còn lại: " + time + "\n\n|7|Bạn đã cược Tài : " + Util.format(player.goldTai) + " Thỏi vàng" + "\n\n|7|Hệ thống sắp bảo trì", "Cập nhập", "Đóng");
                                                } else {
                                                    NpcService.gI().createMenuConMeo(player, ConstNpc.TAIXIU, 9077, "\n|7|---NHÀ CÁI TÀI-XỈU---"
                                                            + "\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z + TaiXiu.gI().ketqua
                                                            + "\n|4|Kết quả 10 lần gần nhất:  " + TaiXiu.gI().InKetQua()
                                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Thỏi vàng"
                                                            + "\n\nTổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Thỏi vàng\n\n|5|Thời gian còn lại: " + time + "\n\n|7|Bạn đã cược Xỉu : " + Util.format(player.goldXiu) + " Thỏi vàng" + "\n\n|7|Hệ thống sắp bảo trì", "Cập nhập", "Đóng");
                                                }
                                            }
                                            break;
                                        case 2:
                                            String timexu = ((TaiXiu_Xu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) + " giây";
                                            if (TaiXiu_Xu.gI().baotri == false) {
                                                if (player.goldTai_xu == 0 && player.goldXiu_xu == 0) {
                                                    NpcService.gI().createMenuConMeo(player, ConstNpc.TAIXIU_XU, 9077, "\n|7|---NHÀ CÁI TÀI XỈU---"
                                                            + "\n\n|3|Kết quả kì trước:  " + TaiXiu_Xu.gI().x + " : " + TaiXiu_Xu.gI().y + " : " + TaiXiu_Xu.gI().z + TaiXiu_Xu.gI().ketqua
                                                            + "\n|4|Kết quả 10 lần gần nhất:  " + TaiXiu_Xu.gI().InKetQua()
                                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu_Xu.gI().goldTai) + " Hồng ngọc"
                                                            + "\n\nTổng nhà XỈU: " + Util.format(TaiXiu_Xu.gI().goldXiu) + " Hồng ngọc\n\n|5|Thời gian còn lại: " + timexu, "Cập nhập", "Theo TÀI", "Theo XỈU", "Đóng");
                                                } else if (player.goldTai_xu > 0) {
                                                    NpcService.gI().createMenuConMeo(player, ConstNpc.TAIXIU_XU, 9077, "\n|7|---NHÀ CÁI TÀI XỈU---"
                                                            + "\n\n|3|Kết quả kì trước:  " + TaiXiu_Xu.gI().x + " : " + TaiXiu_Xu.gI().y + " : " + TaiXiu_Xu.gI().z + TaiXiu_Xu.gI().ketqua
                                                            + "\n|4|Kết quả 10 lần gần nhất:  " + TaiXiu_Xu.gI().InKetQua()
                                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu_Xu.gI().goldTai) + " Hồng ngọc"
                                                            + "\n\nTổng nhà XỈU: " + Util.format(TaiXiu_Xu.gI().goldXiu) + " Hồng ngọc\n\n|5|Thời gian còn lại: " + timexu + "\n\n|7|Bạn đã cược Tài : " + Util.format(player.goldTai_xu) + " Hồng ngọc", "Cập nhập", "Đóng");
                                                } else {
                                                    NpcService.gI().createMenuConMeo(player, ConstNpc.TAIXIU_XU, 9077, "\n|7|---NHÀ CÁI TÀI XỈU---"
                                                            + "\n\n|3|Kết quả kì trước:  " + TaiXiu_Xu.gI().x + " : " + TaiXiu_Xu.gI().y + " : " + TaiXiu_Xu.gI().z + TaiXiu_Xu.gI().ketqua
                                                            + "\n|4|Kết quả 10 lần gần nhất:  " + TaiXiu_Xu.gI().InKetQua()
                                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu_Xu.gI().goldTai) + " Hồng ngọc"
                                                            + "\n\nTổng nhà XỈU: " + Util.format(TaiXiu_Xu.gI().goldXiu) + " Hồng ngọc\n\n|5|Thời gian còn lại: " + timexu + "\n\n|7|Bạn đã cược Xỉu : " + Util.format(player.goldXiu_xu) + " Hồng ngọc", "Cập nhập", "Đóng");
                                                }
                                            } else {
                                                if (player.goldTai_xu == 0 && player.goldXiu_xu == 0) {
                                                    NpcService.gI().createMenuConMeo(player, ConstNpc.TAIXIU_XU, 9077, "\n|7|---NHÀ CÁI TÀI XỈU---"
                                                            + "\n\n|3|Kết quả kì trước:  " + TaiXiu_Xu.gI().x + " : " + TaiXiu_Xu.gI().y + " : " + TaiXiu_Xu.gI().z + TaiXiu_Xu.gI().ketqua
                                                            + "\n|4|Kết quả 10 lần gần nhất:  " + TaiXiu_Xu.gI().InKetQua()
                                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu_Xu.gI().goldTai) + " Hồng ngọc"
                                                            + "\n\nTổng nhà XỈU: " + Util.format(TaiXiu_Xu.gI().goldXiu) + " Hồng ngọc\n\n|5|Thời gian còn lại: " + timexu + "\n\n|7|Hệ thống sắp bảo trì", "Cập nhập", "Đóng");
                                                } else if (player.goldTai_xu > 0) {
                                                    NpcService.gI().createMenuConMeo(player, ConstNpc.TAIXIU_XU, 9077, "\n|7|---NHÀ CÁI TÀI XỈU---"
                                                            + "\n\n|3|Kết quả kì trước:  " + TaiXiu_Xu.gI().x + " : " + TaiXiu_Xu.gI().y + " : " + TaiXiu_Xu.gI().z + TaiXiu_Xu.gI().ketqua
                                                            + "\n|4|Kết quả 10 lần gần nhất:  " + TaiXiu_Xu.gI().InKetQua()
                                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu_Xu.gI().goldTai) + " Hồng ngọc"
                                                            + "\n\nTổng nhà XỈU: " + Util.format(TaiXiu_Xu.gI().goldXiu) + " Hồng ngọc\n\n|5|Thời gian còn lại: " + timexu + "\n\n|7|Bạn đã cược Tài : " + Util.format(player.goldTai_xu) + " Hồng ngọc" + "\n\n|7|Hệ thống sắp bảo trì", "Cập nhập", "Đóng");
                                                } else {
                                                    NpcService.gI().createMenuConMeo(player, ConstNpc.TAIXIU_XU, 9077, "\n|7|---NHÀ CÁI TÀI-XỈU---"
                                                            + "\n\n|3|Kết quả kì trước:  " + TaiXiu_Xu.gI().x + " : " + TaiXiu_Xu.gI().y + " : " + TaiXiu_Xu.gI().z + TaiXiu_Xu.gI().ketqua
                                                            + "\n|4|Kết quả 10 lần gần nhất:  " + TaiXiu_Xu.gI().InKetQua()
                                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu_Xu.gI().goldTai) + " Hồng ngọc"
                                                            + "\n\nTổng nhà XỈU: " + Util.format(TaiXiu_Xu.gI().goldXiu) + " Hồng ngọc\n\n|5|Thời gian còn lại: " + timexu + "\n\n|7|Bạn đã cược Xỉu : " + Util.format(player.goldXiu_xu) + " Hồng ngọc" + "\n\n|7|Hệ thống sắp bảo trì", "Cập nhập", "Đóng");
                                                }
                                            }
                                            break;
                                        case 3:
                                            this.createOtherMenu(player, 9191,
                                                    "|7|DANH HIỆU VĨNH VIỄN"
                                                    + "\n|5|Thợ Ngọc: Mua với giá 10Tr Hồng ngọc"
                                                    + "\nAi mà xinh thế: Mua với giá 500Tr Hồng ngọc"
                                                    + "\n|2|(Danh hiệu không tốn ô hành trang)",
                                                    "Mua\nThợ Ngọc", "Mua\nAi mà Xinh thế");
                                            break;
                                    }
                                } else if (player.iDMark.getIndexMenu() == 9191) {
                                    switch (select) {
                                        case 0:
                                            if (player.DH_VV_4) {
                                                Service.getInstance().sendThongBao(player, "Bạn đã nhận rồi mà !!!!!!");
                                                return;
                                            }
                                            if (player.inventory.ruby < 10_000_000) {
                                                Service.getInstance().sendThongBao(player, "Bạn chưa đủ 10Tr Hồng ngọc !!!!!!");
                                                return;
                                            }
                                            player.inventory.ruby -= 10_000_000;
                                            player.isTitleUse4 = true;
                                            player.DH_VV_4 = true;
                                            Service.getInstance().point(player);
                                            Service.getInstance().sendTitle(player, 891);
                                            Service.getInstance().sendMoney(player);
                                            Service.getInstance().sendThongBao(player, "Nhận thành công danh hiệu Thợ Ngọc Vĩnh viễn");
                                            break;
                                        case 1:
                                            if (player.DH_VV_5) {
                                                Service.getInstance().sendThongBao(player, "Bạn đã nhận rồi mà !!!!!!");
                                                return;
                                            }
                                            if (player.inventory.ruby < 500_000_000) {
                                                Service.getInstance().sendThongBao(player, "Bạn chưa đủ 500Tr Hồng ngọc !!!!!!");
                                                return;
                                            }
                                            player.inventory.ruby -= 500_000_000;
                                            player.isTitleUse5 = true;
                                            player.DH_VV_5 = true;
                                            Service.getInstance().point(player);
                                            Service.getInstance().sendTitle(player, 892);
                                            Service.getInstance().sendMoney(player);
                                            Service.getInstance().sendThongBao(player, "Nhận thành công danh hiệu Ai mà Xinh Thế Vĩnh viễn");
                                            break;
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.SO_MAY_MAN:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                String time = ((SoMayMan.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) + " giây";
                                StringBuilder stringBuilder = new StringBuilder();
                                for (Player mem : SoMayMan.gI().TrungGiai) {
                                    if (stringBuilder.length() > 0) {
                                        stringBuilder.append(", ");
                                    }
                                    stringBuilder.append(mem.name);
                                }
                                String NamePl = stringBuilder.toString();
                                createOtherMenu(player, ConstNpc.BASE_MENU,
                                        "Kết quả giải trước: " + SoMayMan.gI().SoGiaiTruoc
                                        + "\nNgười trúng giải trước: " + NamePl
                                        + "\nTham gia: " + SoMayMan.gI().PlayerThamGia.size() + " người"
                                        + "\nĐoán trúng sẽ nhận thưởng 100.000 Hồng ngọc"
                                        + "\nThời gian quay số: " + time, "1 số\n1.000 Hồng ngọc", "Hướng dẫn", "Đóng");
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (player.iDMark.isBaseMenu()) {
                                    switch (select) {
                                        case 0:
                                            Input.gI().ChonSo(player);
                                            break;
                                        case 1:
                                            createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                    "Sau khi hết thời gian đếm ngược"
                                                    + "\nHệ thống sẽ quay số cho ra kết quả"
                                                    + "\nNgười thắng sẽ nhận được 100.000 Hồng ngọc"
                                                    + "\n(Mỗi 1 số dự đoán sẽ mất 1.000 Hồng ngọc)", "Ok");
                                            break;
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.GOHAN:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                createOtherMenu(player, ConstNpc.BASE_MENU,
                                        "XIN CHÀO NGƯỜI AE HÃY HỌC THÊM SKIL ĐỂ GIÚP NGƯƠI MẠNH HƠN NÀO\n"
                                        + "KHI FULL NHIỆM VỤ ( 34 )\n"
                                        + "Tôi sẽ tặng bạn\n"
                                        + "|2|99 Phiếu Bé Ngoan\n"
                                        + "|2|3k " + ConstItem.NAME_XU_SERVER
                                        + "\n100k Thỏi Vàng\n"
                                        + "|2|1m Hồng Ngọc\n",
                                        "Shop\nSkill 9",
                                        "Shop\nSkill 10-11",
                                        "Thưởng\nNhiệm Vụ");
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (player.iDMark.isBaseMenu()) {
                                    switch (select) {
                                        case 0:
                                            ShopService.gI().openShopSpecial(player, this, ConstNpc.SKIL_0, 0, -1);
                                            break;
                                        case 1:
                                            ShopService.gI().openShopSpecial(player, this, ConstNpc.SKIL_1, 1, -1);
                                            break;
                                        case 2:
                                            if (TaskService.gI().getIdTask(player) < ConstTask.TASK_34_0) {
                                                this.npcChat(player, "|7|Vui lòng hoàn thành tất cả nhiệm vụ chính");
                                                return;
                                            }
                                            this.createOtherMenu(player, 5701,
                                                    "Bạn đã hoàn thành tất cả nhiệm vụ"
                                                    + "\nTôi sẽ tặng bạn:"
                                                    + "\n|2|99 Phiếu Bé Ngoan"
                                                    + "\n|2|3k " + ConstItem.NAME_XU_SERVER
                                                    + "\n|2|100k Thỏi Vàng "
                                                    + "\n|2|1m Hồng Ngọc",
                                                    ((player.ThuongNhiemVu == 0) ? "(Chưa nhận)" : "(Đã nhận)"));
                                            break;
                                    }
                                } else if (player.iDMark.getIndexMenu() == 5701) {
                                    switch (select) {
                                        case 0:
                                            if (TaskService.gI().getIdTask(player) < ConstTask.TASK_34_0) {
                                                this.npcChat(player, "|7|Vui lòng hoàn thành tất cả nhiệm vụ chính");
                                                return;
                                            }
                                            if (player.ThuongNhiemVu != 0) {
                                                this.npcChat(player, "|7|Bạn đã nhận thưởng rồi mà !!!!");
                                                return;
                                            }
                                            if (InventoryService.gI().getCountEmptyBag(player) < 3) {
                                                this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                                return;
                                            }
                                            if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_34_0 && player.ThuongNhiemVu == 0) {
                                                player.ThuongNhiemVu++;
                                                Item bengoan = ItemService.gI().createNewItem((short) 1802, 99);
                                                Item thoivang = ItemService.gI().createNewItem((short) 457, 100000);
                                                Item xunro = ItemService.gI().createNewItem((short) 1400, 3000);
                                                Item ruby = ItemService.gI().createNewItem((short) 861, 1000000);
                                                InventoryService.gI().addItemBag(player, thoivang, 1);
                                                InventoryService.gI().addItemBag(player, ruby, 1);
                                                InventoryService.gI().addItemBag(player, xunro, 1);
                                                InventoryService.gI().addItemBag(player, bengoan, 1);
                                                InventoryService.gI().sendItemBags(player);
                                                this.npcChat(player, "Bạn nhận được 99 phiếu bé ngoan và 100k Thỏi vàng và 1 triệu Hồng Ngọc và 3k " + ConstItem.NAME_XU_SERVER);
                                            }
                                            break;
                                    }
                                }
                            }
                        }
                    };
                    break;

                case ConstNpc.VEGETA_GOD:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                createOtherMenu(player, ConstNpc.BASE_MENU,
                                        "|7|Truy tìm Siêu anh hùng"
                                        + "\n|5|Đổi rương siêu anh hùng cần :"
                                        + "\n|6|- Hộp sự kiện Thường: x50 Đá sự kiện + 30 " + ConstItem.NAME_XU_SERVER
                                        + "\n- Hộp sự kiện VIP: x100 Đá sự kiện + 50 " + ConstItem.NAME_XU_SERVER,
                                        "Shop\nSự Kiện", "Đổi rương\nThường", "Đổi rương\n VIP");
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (player.iDMark.isBaseMenu()) {
                                    switch (select) {
                                        case 0: {
                                            ShopService.gI().openShopSpecial(player, this, ConstNpc.SHOP_VEGETA_GOD, 0, -1);
                                            break;
                                        }
                                        case 1: {
                                            Item dah1 = null;
                                            Item dah2 = null;
                                            Item dah3 = null;
                                            Item dah4 = null;
                                            Item dah5 = null;
                                            Item dah6 = null;
                                            Item datitan = null;
                                            try {
                                                dah1 = InventoryService.gI().findItemBagByTemp(player, 2080);
                                                dah2 = InventoryService.gI().findItemBagByTemp(player, 2081);
                                                dah3 = InventoryService.gI().findItemBagByTemp(player, 2082);
                                                dah4 = InventoryService.gI().findItemBagByTemp(player, 2083);
                                                dah5 = InventoryService.gI().findItemBagByTemp(player, 2084);
                                                dah6 = InventoryService.gI().findItemBagByTemp(player, 2085);
                                                datitan = InventoryService.gI().findItemBagByTemp(player, 1400);
                                            } catch (Exception e) {
                                            }
                                            if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                                                if (dah1 == null || dah2 == null || dah3 == null || dah4 == null || dah5 == null || dah6 == null || datitan == null) {
                                                    this.npcChat(player, "Thiếu nguyên liệu");
                                                    return;
                                                }
                                                if (dah1.quantity < 50 || dah2.quantity < 50 || dah3.quantity < 50 || dah4.quantity < 50
                                                        || dah5.quantity < 50 || dah6.quantity < 50 || datitan.quantity < 30) {
                                                    this.npcChat(player, "Thiếu nguyên liệu");
                                                    return;
                                                }
                                                InventoryService.gI().subQuantityItemsBag(player, dah1, 50);
                                                InventoryService.gI().subQuantityItemsBag(player, dah2, 50);
                                                InventoryService.gI().subQuantityItemsBag(player, dah3, 50);
                                                InventoryService.gI().subQuantityItemsBag(player, dah4, 50);
                                                InventoryService.gI().subQuantityItemsBag(player, dah5, 50);
                                                InventoryService.gI().subQuantityItemsBag(player, dah6, 50);
                                                InventoryService.gI().subQuantityItemsBag(player, datitan, 30);
                                                Item ruong = ItemService.gI().createNewItem((short) 2092);
                                                InventoryService.gI().addItemBag(player, ruong, 0);
                                                InventoryService.gI().sendItemBags(player);
                                                this.npcChat(player, "|1|Bạn nhận được " + ruong.template.name);
                                            } else {
                                                this.npcChat(player, "Hành trang không đủ chổ trống");
                                            }
                                            break;
                                        }
                                        case 2: {
                                            Item dah1 = null;
                                            Item dah2 = null;
                                            Item dah3 = null;
                                            Item dah4 = null;
                                            Item dah5 = null;
                                            Item dah6 = null;
                                            Item datitan = null;
                                            try {
                                                dah1 = InventoryService.gI().findItemBagByTemp(player, 2080);
                                                dah2 = InventoryService.gI().findItemBagByTemp(player, 2081);
                                                dah3 = InventoryService.gI().findItemBagByTemp(player, 2082);
                                                dah4 = InventoryService.gI().findItemBagByTemp(player, 2083);
                                                dah5 = InventoryService.gI().findItemBagByTemp(player, 2084);
                                                dah6 = InventoryService.gI().findItemBagByTemp(player, 2085);
                                                datitan = InventoryService.gI().findItemBagByTemp(player, 1400);
                                            } catch (Exception e) {
                                            }
                                            if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                                                if (dah1 == null || dah2 == null || dah3 == null || dah4 == null || dah5 == null || dah6 == null || datitan == null) {
                                                    this.npcChat(player, "Thiếu nguyên liệu");
                                                    return;
                                                }
                                                if (dah1.quantity < 100 || dah2.quantity < 100 || dah3.quantity < 100 || dah4.quantity < 100
                                                        || dah5.quantity < 100 || dah6.quantity < 100 || datitan.quantity < 50) {
                                                    this.npcChat(player, "Thiếu nguyên liệu");
                                                    return;
                                                }
                                                InventoryService.gI().subQuantityItemsBag(player, dah1, 100);
                                                InventoryService.gI().subQuantityItemsBag(player, dah2, 100);
                                                InventoryService.gI().subQuantityItemsBag(player, dah3, 100);
                                                InventoryService.gI().subQuantityItemsBag(player, dah4, 100);
                                                InventoryService.gI().subQuantityItemsBag(player, dah5, 100);
                                                InventoryService.gI().subQuantityItemsBag(player, dah6, 100);
                                                InventoryService.gI().subQuantityItemsBag(player, datitan, 50);
                                                Item ruong = ItemService.gI().createNewItem((short) 2091);
                                                InventoryService.gI().addItemBag(player, ruong, 0);
                                                InventoryService.gI().sendItemBags(player);
                                                this.npcChat(player, "|1|Bạn nhận được " + ruong.template.name);
                                            } else {
                                                this.npcChat(player, "Hành trang không đủ chổ trống");
                                            }
                                            break;
                                        }
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.GOHAN_ROSE:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {

                        @Override
                        public void openBaseMenu(Player player) {
                            this.createOtherMenu(player, ConstNpc.MENU_CHUYEN_SINH,
                                    "|2|╔════════════════════════════╗\n"
                                    + "|7|• Khi chuyển sinh:\n"
                                    + "|5|  - Tái sinh tại hành tinh ngẫu nhiên\n"
                                    + "|5|  - Reset chiêu thức về cấp 1\n"
                                    + "|5|  - Sức mạnh giảm còn 1.500.000\n"
                                    + "|5|  - Tăng % HP, KI, SĐ theo số lần chuyển sinh\n"
                                    + "|2|╚════════════════════════════╝",
                                    "Chuyển sinh",
                                    "Thông tin",
                                    "Nhận danh hiệu"
                            );
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (player.iDMark.getIndexMenu() == ConstNpc.MENU_CHUYEN_SINH) {
                                    switch (select) {
                                        case 0:
                                            this.createOtherMenu(player, ConstNpc.MENU_CHUYEN_SINH_OPEN,
                                                    "|2|╔════════════════════════════════╗\n"
                                                    + "|6|       CHỌN ĐỐI TƯỢNG CHUYỂN SINH\n"
                                                    + "|2|╠════════════════════════════════╣\n"
                                                    + "|7|• YÊU CẦU SƯ PHỤ:\n"
                                                    + "|2|  - Sức mạnh ≥ 500 Tỷ\n"
                                                    + "|2|  - Có kỹ năng: " + player.tenskill9(player.gender) + "\n"
                                                    + "|2|╠════════════════════════════════╣",
                                                    "Chuyển sinh\n[Sư phụ]"
                                            );
                                            break;
                                        case 1:
                                            String msg
                                                    = "╔══════════════════════╗\n"
                                                    + "     THÔNG TIN CHUYỂN SINH\n"
                                                    + "╠══════════════════════╣\n"
                                                    + "• SƯ PHỤ:\n"
                                                    + "  - Cấp: " + player.chuyensinhsuphu + "\n"
                                                    + "  - HP: +" + player.chuyensinhsuphu + "%\n"
                                                    + "  - KI: +" + player.chuyensinhsuphu + "%\n"
                                                    + "  - Sức đánh: +" + player.chuyensinhsuphu + "%\n"
                                                    + "╠══════════════════════╣";
                                            Service.getInstance().sendThongBaoOK(player, msg);
                                            break;
                                        case 2:
                                            this.createOtherMenu(player, ConstNpc.NHAN_DANH_HIEU,
                                                    "|2|╔════════════════════════════╗\n"
                                                    + "|6|         DANH HIỆU VĨNH VIỄN\n"
                                                    + "|2|╠════════════════════════════╣\n"
                                                    + "|7| Danh hiệu: Đại Thần\n"
                                                    + "|7| Yêu cầu: Chuyển sinh cấp 150\n"
                                                    + "|5| (Danh hiệu không tốn ô hành trang)\n"
                                                    + "|2|╚════════════════════════════╝",
                                                    "Nhận Đại Thần");
                                            break;
                                    }
                                } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_CHUYEN_SINH_OPEN) {
                                    switch (select) {
                                        case 0:
                                            this.createOtherMenu(player, ConstNpc.CHUYEN_SINH_SU_PHU,
                                                    "|7|CHUYỂN SINH SƯ PHỤ"
                                                    + "\n\n|5|Bạn đang chuyển sinh : " + player.chuyensinhsuphu
                                                    + " \nCấp tiếp theo với tỉ lệ : " + Service.getInstance().tileChuyensinh(player.chuyensinhsuphu)
                                                    + "% \n Mức giá chuyển sinh : " + ((player.chuyensinhsuphu <= 45) ? "1000 Thỏi vàng" : "30 " + ConstItem.NAME_XU_SERVER)
                                                    + "\n\n|7|Bạn có muốn chuyển sinh ?",
                                                    "Đồng ý", "Từ chối");
                                            break;
                                    }
                                } else if (player.iDMark.getIndexMenu() == ConstNpc.NHAN_DANH_HIEU) {
                                    switch (select) {
                                        case 0 -> {
                                            if (player.DH_VV_1) {
                                                Service.getInstance().sendThongBao(player, "Bạn đã nhận rồi mà !!!!!!");
                                                return;
                                            }
                                            if (player.chuyensinhsuphu < 150) {
                                                Service.getInstance().sendThongBao(player, "Bạn chưa chuyển sinh đủ 150 Cấp !!!!!!");
                                                return;
                                            }
                                            player.isTitleUse1 = true;
                                            player.DH_VV_1 = true;
                                            Service.getInstance().point(player);
                                            Service.getInstance().sendTitle(player, 888);
                                            Service.getInstance().sendThongBao(player, "Nhận thành công danh hiệu Đại Thần Vĩnh viễn");
                                        }
                                    }
                                } else if (player.iDMark.getIndexMenu() == ConstNpc.CHUYEN_SINH_SU_PHU) {
                                    if (player.chuyensinhsuphu >= 999) {
                                        npcChat(player, "|7| Cấp Chuyển sinh sư phụ đạt MAX là 999 Cấp");
                                        return;
                                    }
                                    if (player.playerSkill.skills.get(7).point == 0) {
                                        npcChat(player, "|7|Yêu cầu phải học kỹ năng " + player.tenskill9(player.gender));
                                        return;
                                    }
                                    if (player.nPoint.power < 500_000_000_000L) {
                                        npcChat(player, "|7|Bạn chưa đủ sức mạnh yêu cầu để Chuyển sinh");
                                    } else {
                                        Item thoivang = null;
                                        Item datitan = null;
                                        Item nguyenlieu = null;
                                        int soluong;
                                        try {
                                            thoivang = InventoryService.gI().findItemBagByTemp(player, 457);
                                            datitan = InventoryService.gI().findItemBagByTemp(player, 1400);
                                        } catch (Exception e) {
                                        }
                                        if (player.chuyensinhsuphu <= 45) {
                                            nguyenlieu = thoivang;
                                            soluong = 1000;
                                        } else {
                                            nguyenlieu = datitan;
                                            soluong = 30;
                                        }
                                        if (nguyenlieu == null || nguyenlieu.quantity < soluong) {
                                            npcChat(player, "Bạn chưa đủ " + ((player.chuyensinhsuphu <= 45) ? "1000 Thỏi vàng" : "30 " + ConstItem.NAME_XU_SERVER) + " để chuyển sinh");
                                            return;
                                        }
                                        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                                            if (player.inventory.itemsBody.get(0).quantity < 1
                                                    && player.inventory.itemsBody.get(1).quantity < 1
                                                    && player.inventory.itemsBody.get(2).quantity < 1
                                                    && player.inventory.itemsBody.get(3).quantity < 1
                                                    && player.inventory.itemsBody.get(4).quantity < 1) {
                                                if (Util.nextInt(0, 100) < Service.getInstance().tileChuyensinh(player.chuyensinhsuphu)) {
                                                    int level = player.playerSkill.skills.get(7).point;
                                                    int levelbh = player.playerSkill.skills.get(9).point;
                                                    int levelPhanThan = player.playerSkill.skills.get(11).point;
                                                    InventoryService.gI().subQuantityItemsBag(player, nguyenlieu, soluong);
                                                    player.gender += 1;
                                                    player.nPoint.power = 1_500_000;
                                                    player.nPoint.tiemNang = 1_500_000;
                                                    player.chuyensinhsuphu++;
                                                    if (player.gender > 2) {
                                                        player.gender = 0;
                                                    }
                                                    short[] headtd = {30, 31, 64};
                                                    short[] headnm = {9, 29, 32};
                                                    short[] headxd = {27, 28, 6};
                                                    player.playerSkill.skills.clear();
                                                    for (Skill skill : player.playerSkill.skills) {
                                                        skill.point = 1;
                                                    }
                                                    int[] skillsArr = player.gender == 0 ? new int[]{0, 1, 6, 9, 10, 20, 22, 24, 19, 27, 28, 29}
                                                            : player.gender == 1 ? new int[]{2, 3, 7, 11, 12, 17, 18, 26, 19, 27, 28, 29}
                                                            : new int[]{4, 5, 8, 13, 14, 21, 23, 25, 19, 27, 28, 29};
                                                    for (int i = 0; i < skillsArr.length; i++) {
                                                        if (skillsArr[i] == Skill.SUPER_KAME || skillsArr[i] == Skill.LIEN_HOAN_CHUONG || skillsArr[i] == Skill.MA_PHONG_BA) {
                                                            player.playerSkill.skills.add(SkillUtil.createSkill(skillsArr[i], level));
                                                        } else if (skillsArr[i] == Skill.BIEN_HINH) {
                                                            if (levelbh != 0) {
                                                                player.playerSkill.skills.add(SkillUtil.createSkill(skillsArr[i], levelbh));
                                                            } else {
                                                                player.playerSkill.skills.add(SkillUtil.createSkillLevel0(skillsArr[i]));
                                                            }
                                                        } else if (skillsArr[i] == Skill.TAI_TAO_SUC_MANH) {
                                                            if (levelbh != 0) {
                                                                player.playerSkill.skills.add(SkillUtil.createSkill(skillsArr[i], 1));
                                                            } else {
                                                                player.playerSkill.skills.add(SkillUtil.createSkillLevel0(skillsArr[i]));
                                                            }
                                                        } else if (skillsArr[i] == Skill.PHAN_THAN) {
                                                            if (levelPhanThan != 0) {
                                                                player.playerSkill.skills.add(SkillUtil.createSkill(skillsArr[i], levelPhanThan));
                                                            } else {
                                                                player.playerSkill.skills.add(SkillUtil.createSkillLevel0(skillsArr[i]));
                                                            }
                                                        } else {
                                                            player.playerSkill.skills.add(SkillUtil.createSkill(skillsArr[i], 1));
                                                        }
                                                    }
                                                    player.playerIntrinsic.intrinsic = IntrinsicService.gI().getIntrinsicById(0);
                                                    player.playerIntrinsic.intrinsic.param1 = 0;
                                                    player.playerIntrinsic.intrinsic.param2 = 0;
                                                    player.playerIntrinsic.countOpen = 0;
                                                    switch (player.gender) {
                                                        case 0:
                                                            player.head = headtd[Util.nextInt(headtd.length)];
                                                            break;
                                                        case 1:
                                                            player.head = headnm[Util.nextInt(headnm.length)];
                                                            break;
                                                        case 2:
                                                            player.head = headxd[Util.nextInt(headxd.length)];
                                                            break;
                                                        default:
                                                            break;
                                                    }
                                                    npcChat(player, "|1|Chuyển sinh thành công \n sư phụ cấp hiện tại :" + player.chuyensinhsuphu);
                                                    Service.getInstance().player(player);
                                                    player.zone.loadAnotherToMe(player);
                                                    player.zone.load_Me_To_Another(player);
                                                    Service.getInstance().sendFlagBag(player);
                                                    Service.getInstance().Send_Caitrang(player);
                                                    PlayerService.gI().sendInfoHpMpMoney(player);
                                                    Service.getInstance().point(player);
                                                    Service.getInstance().Send_Info_NV(player);
                                                    InventoryService.gI().sendItemBags(player);
                                                    Service.getInstance().sendMoney(player);
                                                } else {
                                                    npcChat(player, "|7|Chuyển sinh thất bại \n sư phụ cấp hiện tại :" + player.chuyensinhsuphu);
                                                    InventoryService.gI().subQuantityItemsBag(player, nguyenlieu, soluong);
                                                    Service.getInstance().point(player);
                                                    Service.getInstance().Send_Info_NV(player);
                                                    InventoryService.gI().sendItemBags(player);
                                                    Service.getInstance().sendMoney(player);
                                                }
                                            } else {
                                                Service.getInstance().sendThongBao(player, "Tháo hết 5 món đầu bạn đang mặc ra nha");
                                            }
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Hành trang đầy");
                                        }
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.QUY_LAO_KAME:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                EscortedBoss escortedBoss = player.getEscortedBoss();
                                if (escortedBoss != null && escortedBoss instanceof Qilin) {
                                    this.createOtherMenu(player, ConstNpc.ESCORT_QILIN_MENU,
                                            "Ah con đã tìm thấy lân con thất lạc của ta\nTa sẽ thưởng cho con 1 viên Capsule Tết 2024.",
                                            "Đồng ý", "Từ chối");
                                } else {
                                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Chào con, con muốn ta giúp gì nào?",
                                                "Quy đổi\nVNĐ",
                                                "Giải tán\nbang hội",
                                                "Lãnh địa\nbang hội",
                                                "Kho báu\ndưới biển");
                                    }
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (player.iDMark.isBaseMenu()) {
                                    switch (select) {
                                        case 0:
                                            this.createOtherMenu(player, ConstNpc.QUY_DOI_VND,
                                                    "Chào con, con muốn ta giúp gì nào?",
                                                    "Đổi\nHồng Ngọc",
                                                    "Đổi\n" + ConstItem.NAME_XU_SERVER);
                                            break;
                                        case 1:
                                            if (player.clan != null) {
                                                if (player.clan.getMembers().size() > 1) {
                                                    Service.getInstance().sendThongBao(player,
                                                            "Bạn không thể giải tán bang hội khi còn thành viên trong bang!!!");
                                                    return;
                                                }
                                                ClanService.gI().RemoveClanAll(player);
                                            } else {
                                                Service.getInstance().sendThongBao(player,
                                                        "Bạn không có bang hội nào để giải tán.");
                                            }
                                            break;
                                        case 2:
                                            if (player.clan != null) {
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 153, -1, -1);
                                            } else {
                                                Service.getInstance().sendThongBao(player, "Yêu cầu có bang hội !!!");
                                            }
                                            break;
                                        case 3:
                                            if (player.clan != null) {
                                                if (player.clan.banDoKhoBau != null) {
                                                    this.createOtherMenu(player, ConstNpc.MENU_OPENED_DBKB,
                                                            "Bang hội của con đang đi tìm kho báu dưới biển cấp độ "
                                                            + player.clan.banDoKhoBau.level
                                                            + "\nCon có muốn đi theo không?",
                                                            "Đồng ý", "Từ chối");
                                                } else {
                                                    this.createOtherMenu(player, ConstNpc.MENU_OPEN_DBKB,
                                                            "Đây là bản đồ kho báu hải tặc tí hon\nCác con cứ yên tâm lên đường\n"
                                                            + "Ở đây có ta lo\nNhớ chọn cấp độ vừa sức mình nhé",
                                                            "Chọn\ncấp độ", "Từ chối");
                                                }
                                            } else {
                                                this.npcChat(player, "Con phải có bang hội ta mới có thể cho con đi");
                                            }
                                    }

                                } else if (player.iDMark.getIndexMenu() == ConstNpc.QUY_DOI_VND) {
                                    switch (select) {
                                        case 0:
                                            if (Manager.KHUYEN_MAI_NAP != 1) {
                                                this.createOtherMenu(player, ConstNpc.QUY_DOI_HONG_NGOC,
                                                        "|7|Quy đổi Hồng Ngọc"
                                                        + "\n|6|Giới hạn đổi không quá 1.000.000 VNĐ"
                                                        + "\n|1|VNĐ hiện còn : " + " " + Util.format(player.getSession().vnd)
                                                        + "\n\n|5|Nhập 10.000Đ được 100k Hồng Ngọc"
                                                        + "\n|3| Server đang x" + Manager.KHUYEN_MAI_NAP + " Quy đổi "
                                                        + "(10.000 VNĐ = " + Util.format(Manager.KHUYEN_MAI_NAP * 10000000) + " Hồng Ngọc)",
                                                        "Đồng ý", "Từ chối");
                                            } else {
                                                this.createOtherMenu(player, ConstNpc.QUY_DOI_HONG_NGOC,
                                                        "|7|Quy đổi Hồng Ngọc"
                                                        + "\n|6|Giới hạn đổi không quá 1.000.000 VNĐ"
                                                        + "\n|1|VNĐ hiện còn : " + " " + Util.format(player.getSession().vnd)
                                                        + "\n\n|5|Nhập 10.000 VNĐ được 100k Hồng Ngọc",
                                                        "Đồng ý", "Từ chối");
                                            }
                                            break;
                                        case 1:
                                            if (Manager.KHUYEN_MAI_NAP != 1) {
                                                this.createOtherMenu(player, ConstNpc.QUY_DOI_XU_SKY,
                                                        "|7|Quy đổi " + ConstItem.NAME_XU_SERVER
                                                        + "\n|6|Giới hạn đổi không quá 1.000.000 VNĐ"
                                                        + "\n|1|VNĐ hiện còn : " + " " + Util.format(player.getSession().vnd)
                                                        + "\n\n|5|Nhập 10.000 VNĐ được 100 " + ConstItem.NAME_XU_SERVER
                                                        + "\n|3| Server đang x" + Manager.KHUYEN_MAI_NAP + " Quy đổi "
                                                        + "(10.000 VNĐ = " + Util.format(Manager.KHUYEN_MAI_NAP * 100) + " " + ConstItem.NAME_XU_SERVER + ")",
                                                        "Đồng ý", "Từ chối");
                                            } else {
                                                this.createOtherMenu(player, ConstNpc.QUY_DOI_XU_SKY,
                                                        "|7|Quy đổi " + ConstItem.NAME_XU_SERVER
                                                        + "\n|6|Giới hạn đổi không quá 1.000.000 VNĐ"
                                                        + "\n|1|VNĐ hiện còn : " + " " + Util.format(player.getSession().vnd)
                                                        + "\n\n|5|Nhập 10.000 VNĐ được 100 " + ConstItem.NAME_XU_SERVER,
                                                        "Đồng ý", "Từ chối");
                                            }
                                            break;
                                    }
                                } else if (player.iDMark.getIndexMenu() == ConstNpc.QUY_DOI_HONG_NGOC) {
                                    switch (select) {
                                        case 0:
                                            Input.gI().createFormQDHongNgoc(player);
                                            break;
                                    }
                                } else if (player.iDMark.getIndexMenu() == ConstNpc.QUY_DOI_XU_SKY) {
                                    switch (select) {
                                        case 0:
                                            Input.gI().createFormQDXUSKY(player);
                                            break;
                                    }
                                } else if (player.iDMark.getIndexMenu() == 444) {
                                    switch (select) {
                                        case 0: {
                                            Item chuvanb = InventoryService.gI().findItem(player, ConstItem.CHU_VAN, 99);
                                            Item chusub = InventoryService.gI().findItem(player, ConstItem.CHU_SU, 99);
                                            Item chunhub = InventoryService.gI().findItem(player, ConstItem.CHU_NHU, 99);
                                            Item chuyb = InventoryService.gI().findItem(player, ConstItem.CHU_Y, 99);
                                            Item chu2025 = InventoryService.gI().findItem(player, ConstItem.CHU_2025, 99);
                                            Item thiepb = InventoryService.gI().findItem(player, ConstItem.THIEP_CHUC_TET_2024, 100);
                                            if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                                                if (chuvanb != null && chusub != null && chunhub != null && chuyb != null && chu2025 != null && thiepb != null) {
                                                    InventoryService.gI().subQuantityItemsBag(player, chuvanb, 99);
                                                    InventoryService.gI().subQuantityItemsBag(player, chusub, 99);
                                                    InventoryService.gI().subQuantityItemsBag(player, chunhub, 99);
                                                    InventoryService.gI().subQuantityItemsBag(player, chuyb, 99);
                                                    InventoryService.gI().subQuantityItemsBag(player, chu2025, 99);
                                                    InventoryService.gI().subQuantityItemsBag(player, thiepb, 100);

                                                    Item thoivang = ItemService.gI().createNewItem((short) 457, 10000);
                                                    InventoryService.gI().addItemBag(player, thoivang, 0);
                                                    InventoryService.gI().sendItemBags(player);
                                                    Service.getInstance().sendThongBao(player, "Bạn nhận được 10.000 " + thoivang.template.name);
                                                } else {
                                                    Service.getInstance().sendThongBao(player, "Không đủ nguyên liệu");
                                                }
                                            } else {
                                                Service.getInstance().sendThongBao(player, "Hành trang đầy.");
                                            }
                                            break;
                                        }
                                        case 1: {
                                            Item chuvanb = InventoryService.gI().findItem(player, ConstItem.CHU_VAN, 500);
                                            Item chusub = InventoryService.gI().findItem(player, ConstItem.CHU_SU, 500);
                                            Item chunhub = InventoryService.gI().findItem(player, ConstItem.CHU_NHU, 500);
                                            Item chuyb = InventoryService.gI().findItem(player, ConstItem.CHU_Y, 500);
                                            Item chu2025 = InventoryService.gI().findItem(player, ConstItem.CHU_2025, 500);
                                            Item thiepb = InventoryService.gI().findItem(player, ConstItem.THIEP_CHUC_TET_2024, 500);
                                            Item daTitan = InventoryService.gI().findItem(player, 1400, 1000);
                                            if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                                                if (chuvanb != null && chusub != null && chunhub != null && chuyb != null && chu2025 != null && thiepb != null && daTitan != null) {
                                                    InventoryService.gI().subQuantityItemsBag(player, chuvanb, 500);
                                                    InventoryService.gI().subQuantityItemsBag(player, chusub, 500);
                                                    InventoryService.gI().subQuantityItemsBag(player, chunhub, 500);
                                                    InventoryService.gI().subQuantityItemsBag(player, chuyb, 500);
                                                    InventoryService.gI().subQuantityItemsBag(player, chu2025, 500);
                                                    InventoryService.gI().subQuantityItemsBag(player, thiepb, 500);
                                                    InventoryService.gI().subQuantityItemsBag(player, daTitan, 1000);

                                                    Item danhhieu = ItemService.gI().createNewItem((short) 1325, 1);
                                                    danhhieu.itemOptions.add(new ItemOption(50, 30));
                                                    danhhieu.itemOptions.add(new ItemOption(77, 40));
                                                    danhhieu.itemOptions.add(new ItemOption(103, 40));
                                                    danhhieu.itemOptions.add(new ItemOption(101, 500));
                                                    danhhieu.itemOptions.add(new ItemOption(5, 10));
                                                    danhhieu.itemOptions.add(new ItemOption(95, 10));
                                                    danhhieu.itemOptions.add(new ItemOption(96, 10));
                                                    danhhieu.itemOptions.add(new ItemOption(197, 1));
                                                    danhhieu.itemOptions.add(new ItemOption(30, 1));

                                                    InventoryService.gI().addItemBag(player, danhhieu, 0);
                                                    InventoryService.gI().sendItemBags(player);
                                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + danhhieu.template.name);
                                                } else {
                                                    Service.getInstance().sendThongBao(player, "Không đủ nguyên liệu");
                                                }
                                            } else {
                                                Service.getInstance().sendThongBao(player, "Hành trang đầy.");
                                            }
                                            break;
                                        }
                                        case 2: {
                                            Item chuvanb = InventoryService.gI().findItem(player, ConstItem.CHU_VAN, 1000);
                                            Item chusub = InventoryService.gI().findItem(player, ConstItem.CHU_SU, 1000);
                                            Item chunhub = InventoryService.gI().findItem(player, ConstItem.CHU_NHU, 1000);
                                            Item chuyb = InventoryService.gI().findItem(player, ConstItem.CHU_Y, 1000);
                                            Item chu2025 = InventoryService.gI().findItem(player, ConstItem.CHU_2025, 1000);
                                            Item thiepb = InventoryService.gI().findItem(player, ConstItem.THIEP_CHUC_TET_2024, 1000);
                                            Item daTitan = InventoryService.gI().findItem(player, 1400, 3000);
                                            if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                                                if (chuvanb != null && chusub != null && chunhub != null && chuyb != null && chu2025 != null && thiepb != null && daTitan != null) {
                                                    InventoryService.gI().subQuantityItemsBag(player, chuvanb, 1000);
                                                    InventoryService.gI().subQuantityItemsBag(player, chusub, 1000);
                                                    InventoryService.gI().subQuantityItemsBag(player, chunhub, 1000);
                                                    InventoryService.gI().subQuantityItemsBag(player, chuyb, 1000);
                                                    InventoryService.gI().subQuantityItemsBag(player, chu2025, 1000);
                                                    InventoryService.gI().subQuantityItemsBag(player, thiepb, 1000);
                                                    InventoryService.gI().subQuantityItemsBag(player, daTitan, 3000);

                                                    Item caVang = ItemService.gI().createNewItem((short) 1663, 1);
                                                    caVang.itemOptions.add(new ItemOption(50, 100));
                                                    caVang.itemOptions.add(new ItemOption(77, 100));
                                                    caVang.itemOptions.add(new ItemOption(103, 100));
                                                    caVang.itemOptions.add(new ItemOption(101, 1000));
                                                    caVang.itemOptions.add(new ItemOption(5, 10));
                                                    caVang.itemOptions.add(new ItemOption(95, 20));
                                                    caVang.itemOptions.add(new ItemOption(96, 20));
                                                    caVang.itemOptions.add(new ItemOption(197, 1));
                                                    caVang.itemOptions.add(new ItemOption(30, 1));

                                                    InventoryService.gI().addItemBag(player, caVang, 0);
                                                    InventoryService.gI().sendItemBags(player);
                                                    Service.getInstance().sendThongBao(player, "Bạn nhận được " + caVang.template.name);
                                                } else {
                                                    Service.getInstance().sendThongBao(player, "Không đủ nguyên liệu");
                                                }
                                            } else {
                                                Service.getInstance().sendThongBao(player, "Hành trang đầy.");
                                            }
                                            break;
                                        }
                                    }
                                } else if (player.iDMark.getIndexMenu() == 1432) {
                                    switch (select) {
                                        case 0:
                                            if (player.tongnap < 50000) {
                                                this.npcChat(player, "|7|Bạn chưa đủ điều kiện nhận!!!!");
                                                return;
                                            }
                                            if (player.mot != 0) {
                                                this.npcChat(player, "|7|Bạn đã nhận thưởng rồi mà !!!!");
                                                return;
                                            }
                                            if (InventoryService.gI().getCountEmptyBag(player) < 8) {
                                                this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                                return;
                                            }
                                            if (player.tongnap >= 50000 && player.mot == 0) {

                                                player.mot++;

                                                Item nro1 = ItemService.gI().createNewItem((short) 14);
                                                Item nro2 = ItemService.gI().createNewItem((short) 15);
                                                Item nro3 = ItemService.gI().createNewItem((short) 16);
                                                Item nro4 = ItemService.gI().createNewItem((short) 17);
                                                Item nro5 = ItemService.gI().createNewItem((short) 18);
                                                Item nro6 = ItemService.gI().createNewItem((short) 19);
                                                Item nro7 = ItemService.gI().createNewItem((short) 20);
                                                Item cuonghoa = ItemService.gI().createNewItem((short) 1386);

                                                nro1.quantity = 5;
                                                nro2.quantity = 5;
                                                nro3.quantity = 5;
                                                nro4.quantity = 5;
                                                nro5.quantity = 5;
                                                nro6.quantity = 5;
                                                nro7.quantity = 5;
                                                cuonghoa.quantity = 29;

                                                InventoryService.gI().addItemBag(player, nro1, 99);
                                                InventoryService.gI().addItemBag(player, nro2, 99);
                                                InventoryService.gI().addItemBag(player, nro3, 99);
                                                InventoryService.gI().addItemBag(player, nro4, 99);
                                                InventoryService.gI().addItemBag(player, nro5, 99);
                                                InventoryService.gI().addItemBag(player, nro6, 99);
                                                InventoryService.gI().addItemBag(player, nro7, 99);
                                                InventoryService.gI().addItemBag(player, cuonghoa, 99);

                                                InventoryService.gI().sendItemBags(player);
                                                this.npcChat(player, "Bạn nhận được 5 Bộ Ngọc rồng 1s, 29 item Cuồng Bạo");
                                            }
                                            break;
                                        case 1:
                                            if (player.tongnap < 100000) {
                                                this.npcChat(player, "|7|Bạn chưa đủ điều kiện nhận!!!!");
                                                return;
                                            }
                                            if (player.hai != 0) {
                                                this.npcChat(player, "|7|Bạn đã nhận thưởng rồi mà !!!!");
                                                return;
                                            }
                                            if (InventoryService.gI().getCountEmptyBag(player) < 3) {
                                                this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                                return;
                                            }
                                            if (player.tongnap >= 100000 && player.hai == 0) {
                                                player.hai++;

                                                Item ruongsach = ItemService.gI().createNewItem((short) 1525);
                                                Item cuonghoa = ItemService.gI().createNewItem((short) 1386);
                                                Item thedoiten = ItemService.gI().createNewItem((short) 2006);

                                                ruongsach.quantity = 100;
                                                cuonghoa.quantity = 49;
                                                thedoiten.quantity = 1;

                                                InventoryService.gI().addItemBag(player, ruongsach, 1);
                                                InventoryService.gI().addItemBag(player, cuonghoa, 1);
                                                InventoryService.gI().addItemBag(player, thedoiten, 1);

                                                InventoryService.gI().sendItemBags(player);
                                                this.npcChat(player, "100 Hộp sách kỹ năng, 29 item Cuồng Bạo,1 Thẻ đổi tên");
                                            }
                                            break;
                                        case 2:
                                            if (player.tongnap < 200000) {
                                                this.npcChat(player, "|7|Bạn chưa đủ điều kiện nhận!!!!");
                                                return;
                                            }
                                            if (player.ba != 0) {
                                                this.npcChat(player, "|7|Bạn đã nhận thưởng rồi mà !!!!");
                                                return;
                                            }
                                            if (InventoryService.gI().getCountEmptyBag(player) < 3) {
                                                this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                                return;
                                            }
                                            if (player.tongnap >= 200000 && player.ba == 0) {
                                                player.ba++;
                                                Item ruongsach = ItemService.gI().createNewItem((short) 1525);
                                                Item cuonghoa = ItemService.gI().createNewItem((short) 1386);
                                                ruongsach.quantity = 300;
                                                cuonghoa.quantity = 99;
                                                InventoryService.gI().addItemBag(player, ruongsach, 1);
                                                InventoryService.gI().addItemBag(player, cuonghoa, 1);
                                                InventoryService.gI().sendItemBags(player);
                                                this.npcChat(player, "Bạn nhận được 500 Hộp sách kỹ năng, 50 item Cuồng Bạo");
                                            }
                                            break;
                                        case 3:
                                            if (player.tongnap < 500000) {
                                                this.npcChat(player, "|7|Bạn chưa đủ điều kiện nhận!!!!");
                                                return;
                                            }
                                            if (player.bon != 0) {
                                                this.npcChat(player, "|7|Bạn đã nhận thưởng rồi mà !!!!");
                                                return;
                                            }
                                            if (InventoryService.gI().getCountEmptyBag(player) < 4) {
                                                this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                                return;
                                            }
                                            if (player.tongnap >= 500000 && player.bon == 0) {
                                                player.bon++;
                                                Item ruongsach = ItemService.gI().createNewItem((short) 1525);
                                                Item ruongspl = ItemService.gI().createNewItem((short) 1479);
                                                Item danhhieu = ItemService.gI().createNewItem((short) 1326);
                                                Item tvang = ItemService.gI().createNewItem((short) 457);

                                                danhhieu.itemOptions.add(new ItemOption(50, 40));
                                                danhhieu.itemOptions.add(new ItemOption(77, 50));
                                                danhhieu.itemOptions.add(new ItemOption(103, 50));
                                                danhhieu.itemOptions.add(new ItemOption(14, 5));
                                                danhhieu.itemOptions.add(new ItemOption(101, 500));
                                                danhhieu.itemOptions.add(new ItemOption(30, 1));

                                                ruongspl.quantity = 50;
                                                ruongsach.quantity = 500;
                                                tvang.quantity = 500;

                                                InventoryService.gI().addItemBag(player, ruongspl, 99);
                                                InventoryService.gI().addItemBag(player, ruongsach, 1);
                                                InventoryService.gI().addItemBag(player, danhhieu, 1);
                                                InventoryService.gI().addItemBag(player, tvang, 99);
                                                InventoryService.gI().sendItemBags(player);
                                                this.npcChat(player, "Bạn nhận được 500 Hộp sách kỹ năng, 50 rương Sao pha lê VIP, Danh hiệu Thiên Tử, 500 thỏi vàng");
                                            }
                                            break;
                                        case 4:
                                            if (player.tongnap < 1000000) {
                                                this.npcChat(player, "|7|Bạn chưa đủ điều kiện nhận!!!!");
                                                return;
                                            }
                                            if (player.nam != 0) {
                                                this.npcChat(player, "|7|Bạn đã nhận thưởng rồi mà !!!!");
                                                return;
                                            }
                                            if (InventoryService.gI().getCountEmptyBag(player) < 5) {
                                                this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                                return;
                                            }
                                            if (player.tongnap >= 1000000 && player.nam == 0) {

                                                player.nam++;

                                                Item hopps = ItemService.gI().createNewItem((short) 1237);
                                                Item thucuoi = ItemService.gI().createNewItem((short) 1326);
                                                Item cauvong = ItemService.gI().createNewItem((short) 1083);
                                                Item nrosc = ItemService.gI().createNewItem((short) 1015);
                                                Item tvang = ItemService.gI().createNewItem((short) 457);

                                                thucuoi.itemOptions.add(new ItemOption(50, 50));
                                                thucuoi.itemOptions.add(new ItemOption(77, 60));
                                                thucuoi.itemOptions.add(new ItemOption(103, 60));
                                                thucuoi.itemOptions.add(new ItemOption(101, 700));
                                                thucuoi.itemOptions.add(new ItemOption(95, 10));
                                                thucuoi.itemOptions.add(new ItemOption(96, 10));
                                                thucuoi.itemOptions.add(new ItemOption(14, 5));
                                                thucuoi.itemOptions.add(new ItemOption(30, 1));

                                                nrosc.quantity = 20;
                                                cauvong.quantity = 20;
                                                hopps.quantity = 500;
                                                tvang.quantity = 1000;

                                                InventoryService.gI().addItemBag(player, nrosc, 99);
                                                InventoryService.gI().addItemBag(player, thucuoi, 99);
                                                InventoryService.gI().addItemBag(player, cauvong, 99);
                                                InventoryService.gI().addItemBag(player, hopps, 999);
                                                InventoryService.gI().addItemBag(player, tvang, 999);

                                                InventoryService.gI().sendItemBags(player);

                                                this.npcChat(player, "Bạn nhận được thúa cưỡi Pegasus, 20 Đá cầu vòng và 500 Hộp pháp sư, 20 Viên Ngọc rồng Siêu cấp, 1k thỏi vàng");
                                            }
                                            break;
                                        case 5:
                                            if (player.tongnap < 2000000) {
                                                this.npcChat(player, "|7|Bạn chưa đủ điều kiện nhận!!!!");
                                                return;
                                            }
                                            if (player.sau != 0) {
                                                this.npcChat(player, "|7|Bạn đã nhận thưởng rồi mà !!!!");
                                                return;
                                            }
                                            if (InventoryService.gI().getCountEmptyBag(player) < 6) {
                                                this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                                return;
                                            }
                                            if (player.tongnap >= 2000000 && player.sau == 0) {

                                                player.sau++;

                                                Item csbac = ItemService.gI().createNewItem((short) 573, 3000);
                                                Item csvang = ItemService.gI().createNewItem((short) 574, 2000);
                                                Item dahkim = ItemService.gI().createNewItem((short) 1318, 2000);
                                                Item nrsc = ItemService.gI().createNewItem((short) 1015, 30);
                                                Item dabaove = ItemService.gI().createNewItem((short) 987, 200);
                                                Item tvang = ItemService.gI().createNewItem((short) 457, 5000);

                                                InventoryService.gI().addItemBag(player, csbac, 1);
                                                InventoryService.gI().addItemBag(player, csvang, 1);
                                                InventoryService.gI().addItemBag(player, dahkim, 1);
                                                InventoryService.gI().addItemBag(player, nrsc, 1);
                                                InventoryService.gI().addItemBag(player, dabaove, 1);
                                                InventoryService.gI().addItemBag(player, tvang, 1);

                                                InventoryService.gI().sendItemBags(player);
                                                this.npcChat(player, "Bạn nhận được 3000 Rương Bạc, 2000 Rương Vàng, 2000 Đá Hoàng Kim"
                                                        + ", 30 Viên Ngọc rồng Siêu cấp, 200 Đá bảo vệ, 5.000 thỏi vàng");
                                            }
                                            break;
                                        case 6:
                                            if (player.tongnap < 5000000) {
                                                this.npcChat(player, "|7|Bạn chưa đủ điều kiện nhận!!!!");
                                                return;
                                            }
                                            if (player.bay != 0) {
                                                this.npcChat(player, "|7|Bạn đã nhận thưởng rồi mà !!!!");
                                                return;
                                            }
                                            if (InventoryService.gI().getCountEmptyBag(player) < 5) {
                                                this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                                return;
                                            }
                                            if (player.tongnap >= 5000000 && player.bay == 0) {
                                                player.bay++;
                                                Item csbac = ItemService.gI().createNewItem((short) 573, 5000);
                                                Item csvang = ItemService.gI().createNewItem((short) 574, 3000);
                                                Item dabaove = ItemService.gI().createNewItem((short) 987, 500);
                                                Item nrosc = ItemService.gI().createNewItem((short) 1015, 100);
                                                Item tvang = ItemService.gI().createNewItem((short) 457, 15000);

                                                InventoryService.gI().addItemBag(player, csbac, 1);
                                                InventoryService.gI().addItemBag(player, csvang, 1);
                                                InventoryService.gI().addItemBag(player, dabaove, 1);
                                                InventoryService.gI().addItemBag(player, nrosc, 1);
                                                InventoryService.gI().addItemBag(player, tvang, 1);

                                                InventoryService.gI().sendItemBags(player);
                                                this.npcChat(player, "Bạn nhận được 5000 Rương Bạc, 3000 Rương Vàng, 500 Đá bảo vệ , 100 viên ngọc rồng siêu cấp, 15.000 Thỏi vàng");
                                            }
                                            break;
                                    }
                                } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPEN_SUKIEN) {
                                    openMenuSuKien(player, this, tempId, select);
                                } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPENED_DBKB) {
                                    switch (select) {
                                        case 0:
                                            if (player.isAdmin()
                                                    || player.nPoint.power >= BanDoKhoBau.POWER_CAN_GO_TO_DBKB) {
                                                ChangeMapService.gI().goToDBKB(player);
                                            } else {
                                                this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                                        + Util.numberToMoney(BanDoKhoBau.POWER_CAN_GO_TO_DBKB));
                                            }
                                            break;

                                    }
                                } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPEN_DBKB) {
                                    switch (select) {
                                        case 0:
                                            if (player.isAdmin()
                                                    || player.nPoint.power >= BanDoKhoBau.POWER_CAN_GO_TO_DBKB) {
                                                Input.gI().createFormChooseLevelBDKB(player);
                                            } else {
                                                this.npcChat(player, "Sức mạnh của con phải ít nhất phải đạt "
                                                        + Util.numberToMoney(BanDoKhoBau.POWER_CAN_GO_TO_DBKB));
                                            }
                                            break;
                                    }

                                } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_ACCEPT_GO_TO_BDKB) {
                                    switch (select) {
                                        case 0:
                                            BanDoKhoBauService.gI().openBanDoKhoBau(player,
                                                    Byte.parseByte(String.valueOf(PLAYERID_OBJECT.get(player.id))));
                                            break;
                                    }

                                } else if (player.iDMark.getIndexMenu() == ConstNpc.ESCORT_QILIN_MENU) {
                                    switch (select) {
                                        case 0: {
                                            if (InventoryService.gI().getCountEmptyBag(player) == 0) {
                                                this.npcChat(player,
                                                        "Con phải có ít nhất 1 ô trống trong hành trang ta mới đưa cho con được");
                                                return;
                                            }
                                            EscortedBoss escortedBoss = player.getEscortedBoss();
                                            if (escortedBoss != null) {
                                                escortedBoss.stopEscorting();
                                                Item item = ItemService.gI()
                                                        .createNewItem((short) ConstItem.CAPSULE_TET_2022);
                                                item.quantity = 1;
                                                InventoryService.gI().addItemBag(player, item, 0);
                                                InventoryService.gI().sendItemBags(player);
                                                Service.getInstance().sendThongBao(player,
                                                        "Bạn nhận được " + item.template.name);
                                            }
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.TRUONG_LAO_GURU:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            Item mcl = InventoryService.gI().findItemBagByTemp(player, 1517);
                            int slMCL = (mcl == null) ? 0 : mcl.quantity;
                            if (canOpenNpc(player)) {
                                if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                            "Ngọc rồng Namếc đang bị 2 thế lực tranh giành\nHãy chọn cấp độ tham gia tùy theo sức mạnh bản thân",
                                            "Tham gia", "Đổi điểm\nThưởng\n[" + slMCL + "]", "Từ chối");
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                switch (player.iDMark.getIndexMenu()) {
                                    case ConstNpc.BASE_MENU:
                                        switch (select) {
                                            case 0:
                                                if (TranhNgoc.gI().isTimeRegisterWar()) {
                                                    if (player.iDMark.getTranhNgoc() == -1) {
                                                        this.createOtherMenu(player, ConstNpc.REGISTER_TRANH_NGOC,
                                                                "Ngọc rồng Namếc đang bị 2 thế lực tranh giành\nHãy chọn cấp độ tham gia tùy theo sức mạnh bản thân\nPhe Cadic: " + TranhNgoc.gI().getPlayersCadic().size() + "\nPhe Fide: " + TranhNgoc.gI().getPlayersFide().size(),
                                                                "Tham gia phe Cadic", "Tham gia phe Fide", "Đóng");
                                                    } else {
                                                        this.createOtherMenu(player, ConstNpc.LOG_OUT_TRANH_NGOC,
                                                                "Ngọc rồng Namếc đang bị 2 thế lực tranh giành\nHãy chọn cấp độ tham gia tùy theo sức mạnh bản thân\nPhe Cadic: " + TranhNgoc.gI().getPlayersCadic().size() + "\nPhe Fide: " + TranhNgoc.gI().getPlayersFide().size(),
                                                                "Hủy\nĐăng Ký", "Đóng");
                                                    }
                                                    return;
                                                }
                                                Service.getInstance().sendPopUpMultiLine(player, 0, 7184, "Sự kiện sẽ mở đăng ký vào lúc " + TranhNgoc.HOUR_REGISTER + ":" + TranhNgoc.MIN_REGISTER + "\nSự kiện sẽ bắt đầu vào " + TranhNgoc.HOUR_OPEN + ":" + TranhNgoc.MIN_OPEN + " và kết thúc vào " + TranhNgoc.HOUR_CLOSE + ":" + TranhNgoc.MIN_CLOSE);
                                                break;
                                            case 1:// Shop
                                                ShopService.gI().openShopSpecial(player, this,
                                                        ConstNpc.TRUONG_LAO_GURU, 0, -1);
                                                break;
                                            case 2:
                                                Service.getInstance().sendThongBao(player, "Update coming soon");
                                                break;
                                        }
                                        break;
                                    case ConstNpc.REGISTER_TRANH_NGOC:
                                        switch (select) {
                                            case 0:
                                                if (!player.getSession().actived) {
                                                    Service.getInstance().sendThongBao(player, "Vui lòng kích hoạt tài khoản để sửa dụng chức năng này!");
                                                    return;
                                                }
                                                player.iDMark.setTranhNgoc((byte) 1);
                                                TranhNgoc.gI().addPlayersCadic(player);
                                                Service.getInstance().sendThongBao(player, "Đăng ký vào phe Cadic thành công");
                                                break;
                                            case 1:
                                                if (!player.getSession().actived) {
                                                    Service.getInstance().sendThongBao(player, "Vui lòng kích hoạt tài khoản để sửa dụng chức năng này!");
                                                    return;
                                                }
                                                player.iDMark.setTranhNgoc((byte) 2);
                                                TranhNgoc.gI().addPlayersFide(player);
                                                Service.getInstance().sendThongBao(player, "Đăng ký vào phe Fide thành công");
                                                break;
                                        }
                                        break;
                                    case ConstNpc.LOG_OUT_TRANH_NGOC:
                                        switch (select) {
                                            case 0:
                                                player.iDMark.setTranhNgoc((byte) -1);
                                                TranhNgoc.gI().removePlayersCadic(player);
                                                TranhNgoc.gI().removePlayersFide(player);
                                                Service.getInstance().sendThongBao(player, "Hủy đăng ký thành công");
                                                break;
                                        }
                                        break;
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.ADMIN:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                createOtherMenu(player, ConstNpc.BASE_MENU,
                                        "Con muốn ước gì?",
                                        "Ngọc Rồng\nThường",
                                        "Ngọc Rồng\nBăng",
                                        "Ngọc Rồng\nSiêu Cấp");
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (player.iDMark.isBaseMenu()) {
                                    switch (select) {
                                        case 0:
                                            createOtherMenu(player, ConstNpc.RONG_THUONG,
                                                    "|1|Hãy Đưa Cho Ta 1 Bộ Ngọc Rồng"
                                                    + "\n|2|Ta sẽ ban cho ngươi 1 điều ước, hãy suy nghĩ thật kĩ trước khi quyết định?",
                                                    "Đại Gia\n+1000\nThỏi Vàng",
                                                    "Thay\nChiêu 2-3\nĐệ Tử",
                                                    "Đẹp Trai\nNhất\nVũ Trụ",
                                                    "Giàu Có\n+1000\nHồng Ngọc",
                                                    "100tr\n+Sức Mạnh\nVà\nSức Mạnh");
                                            break;
                                        case 1:
                                            createOtherMenu(player, ConstNpc.RONG_BANG,
                                                    "|1|Hãy Đưa Cho Ta 1 Bộ Ngọc Rồng Băng "
                                                    + "\n|2|Ta sẽ ban cho ngươi 1 điều ước, hãy suy nghĩ thật kĩ trước khi quyết định?",
                                                    "Cộng Chỉ Số\nTrang Bị",
                                                    "Găng Tay\nĐang Mang\n+1 Cấp",
                                                    "Găng Tay\nĐệ Tử\n+1 Cấp");
                                            break;
                                        case 2:
                                            createOtherMenu(player, ConstNpc.RONG_SIEU_CAP,
                                                    "|1|Hãy Đưa Cho Ta 1 Viên Ngọc Rồng Siêu Cấp "
                                                    + "\n|2|Ta sẽ ban cho ngươi 1 điều ước, hãy suy nghĩ thật kĩ trước khi quyết định?",
                                                    "1.000\nSD Gốc",
                                                    "2.000\nHp Gốc",
                                                    "2.000\nKI Gốc",
                                                    "100% 10P\n SD.HP.KI\nSư Phụ",
                                                    "100% 10P\nSD.HP.KI\nĐệ Tử");
                                            break;
                                    }
                                } else if (player.iDMark.getIndexMenu() == ConstNpc.RONG_THUONG) {
                                    Item nro1s = InventoryService.gI().findItemBagByTemp(player, 14);
                                    Item nro2s = InventoryService.gI().findItemBagByTemp(player, 15);
                                    Item nro3s = InventoryService.gI().findItemBagByTemp(player, 16);
                                    Item nro4s = InventoryService.gI().findItemBagByTemp(player, 17);
                                    Item nro5s = InventoryService.gI().findItemBagByTemp(player, 18);
                                    Item nro6s = InventoryService.gI().findItemBagByTemp(player, 19);
                                    Item nro7s = InventoryService.gI().findItemBagByTemp(player, 20);
                                    if (nro1s == null || nro2s == null || nro3s == null || nro4s == null || nro5s == null || nro6s == null || nro7s == null) {
                                        Service.getInstance().sendThongBao(player, "Yêu cầu đủ bộ Ngọc rồng từ 1s đến 7s");
                                        return;
                                    }
                                    boolean active = false;
                                    switch (select) {
                                        case 0: //200 thỏi vàng
                                            if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                                                active = true;
                                                Item thoivang = ItemService.gI().createNewItem((short) 457, 1000);
                                                InventoryService.gI().addItemBag(player, thoivang, 0);
                                                this.npcChat(player, "|1|Bạn nhận được 1000 Thỏi vàng");
                                            } else {
                                                this.npcChat(player, "Hành trang không đủ chổ trống");
                                            }
                                            break;
                                        case 1: //thay chiêu 2-3 đệ tử
                                            if (player.pet != null) {
                                                if (player.pet.playerSkill.skills.get(1).skillId != -1) {
                                                    active = true;
                                                    player.pet.openSkill2();
                                                    if (player.pet.playerSkill.skills.get(2).skillId != -1) {
                                                        player.pet.openSkill3();
                                                    }
                                                } else {
                                                    Service.getInstance().sendThongBao(player, "Ít nhất đệ tử ngươi phải có chiêu 2 chứ!");
                                                }
                                            } else {
                                                Service.getInstance().sendThongBao(player, "Ngươi làm gì có đệ tử?");
                                            }
                                            break;
                                        case 2: //đẹp trai nhất vũ trụ
                                            if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                                                active = true;
                                                byte gender = player.gender;
                                                Item avtVip = ItemService.gI().createNewItem((short) (gender == ConstPlayer.TRAI_DAT ? 227
                                                        : gender == ConstPlayer.NAMEC ? 228 : 229));
                                                avtVip.itemOptions.add(new ItemOption(50, Util.nextInt(1, 70)));
                                                avtVip.itemOptions.add(new ItemOption(77, Util.nextInt(1, 70)));
                                                avtVip.itemOptions.add(new ItemOption(103, Util.nextInt(1, 70)));
                                                avtVip.itemOptions.add(new ItemOption(159, Util.nextInt(1, 4)));
                                                avtVip.itemOptions.add(new ItemOption(106, 1));
                                                avtVip.itemOptions.add(new ItemOption(30, 1));
                                                if (Util.isTrue(90, 100)) {
                                                    avtVip.itemOptions.add(new ItemOption(93, 1));
                                                }
                                                InventoryService.gI().addItemBag(player, avtVip, 0);
                                                InventoryService.gI().sendItemBags(player);
                                            } else {
                                                Service.getInstance().sendThongBao(player, "Hành trang đã đầy");
                                            }
                                            break;
                                        case 3: //+10k Hồng ngọc
                                            active = true;
                                            player.inventory.ruby += 1000;
                                            PlayerService.gI().sendInfoHpMpMoney(player);
                                            break;
                                        case 4: //+100 tr smtn
                                            if (player.nPoint.power < 10_000_000_000L) {
                                                active = true;
                                                Service.getInstance().addSMTN(player, (byte) 2, 100_000_000, false);
                                            } else {
                                                Service.getInstance().sendThongBao(player, "Sức mạnh của ngươi đã vượt quá 10 Tỷ");
                                            }
                                            break;
                                    }
                                    if (active) {
                                        InventoryService.gI().subQuantityItemsBag(player, nro1s, 1);
                                        InventoryService.gI().subQuantityItemsBag(player, nro2s, 1);
                                        InventoryService.gI().subQuantityItemsBag(player, nro3s, 1);
                                        InventoryService.gI().subQuantityItemsBag(player, nro4s, 1);
                                        InventoryService.gI().subQuantityItemsBag(player, nro5s, 1);
                                        InventoryService.gI().subQuantityItemsBag(player, nro6s, 1);
                                        InventoryService.gI().subQuantityItemsBag(player, nro7s, 1);
                                        Service.getInstance().sendMoney(player);
                                        InventoryService.gI().sendItemBags(player);
                                        this.npcChat(player, "|1|Điều ước của ngươi đã thành sự thật");
                                    } else {
                                        this.npcChat(player, "Điều ước này đã quá sức với ta, ta sẽ cho ngươi chọn lại");
                                    }
                                } else if (player.iDMark.getIndexMenu() == ConstNpc.RONG_BANG) {
                                    Item nro1s = InventoryService.gI().findItemBagByTemp(player, 925);
                                    Item nro2s = InventoryService.gI().findItemBagByTemp(player, 926);
                                    Item nro3s = InventoryService.gI().findItemBagByTemp(player, 927);
                                    Item nro4s = InventoryService.gI().findItemBagByTemp(player, 928);
                                    Item nro5s = InventoryService.gI().findItemBagByTemp(player, 929);
                                    Item nro6s = InventoryService.gI().findItemBagByTemp(player, 930);
                                    Item nro7s = InventoryService.gI().findItemBagByTemp(player, 931);
                                    if (nro1s == null || nro2s == null || nro3s == null || nro4s == null || nro5s == null || nro6s == null || nro7s == null) {
                                        Service.getInstance().sendThongBao(player, "Yêu cầu đủ bộ Ngọc rồng Băng từ 1s đến 7s");
                                        return;
                                    }
                                    boolean active = false;
                                    boolean selectMenu = false;
                                    switch (select) {
                                        case 0:
                                            selectMenu = true;
                                            createOtherMenu(player, ConstNpc.RONG_BANG_CHI_SO,
                                                    "|1|Hãy đưa cho ta 1 bộ Ngọc Rồng Băng "
                                                    + "\n|2|Ta sẽ ban cho ngươi 1 điều ước, hãy suy nghĩ thật kĩ trước khi quyết định?",
                                                    "Áo +1%\nSD.HP.KI]",
                                                    "Quần +1%\nSD.HP.KI]",
                                                    "Găng +1%\nSD.HP.KI]",
                                                    "Giày +1%\nSD.HP.KI]",
                                                    "Nhẫn +1%\nSD.HP.KI]");
                                            break;
                                        case 1: //găng tay đang đeo lên 1 cấp
                                            Item item = player.inventory.itemsBody.get(2);
                                            if (item.isNotNullItem()) {
                                                int level = 0;
                                                for (ItemOption io : item.itemOptions) {
                                                    if (io.optionTemplate.id == 72) {
                                                        level = io.param;
                                                        if (level < 7) {
                                                            io.param++;
                                                        }
                                                        break;
                                                    }
                                                }
                                                if (level < 7) {
                                                    active = true;
                                                    if (level == 0) {
                                                        item.itemOptions.add(new ItemOption(72, 1));
                                                    }
                                                    for (ItemOption io : item.itemOptions) {
                                                        if (io.optionTemplate.id == 0) {
                                                            io.param += (io.param * 10 / 100);
                                                            break;
                                                        }
                                                    }
                                                    InventoryService.gI().sendItemBody(player);
                                                } else {
                                                    Service.getInstance().sendThongBao(player, "Găng tay của ngươi đã đạt cấp tối đa");
                                                }
                                            } else {
                                                Service.getInstance().sendThongBao(player, "Ngươi hiện tại có đeo găng đâu");
                                            }
                                            break;
                                        case 2: //găng tay đệ lên 1 cấp
                                            if (player.pet != null) {
                                                Item itemr = player.pet.inventory.itemsBody.get(2);
                                                if (itemr.isNotNullItem()) {
                                                    int level = 0;
                                                    for (ItemOption io : itemr.itemOptions) {
                                                        if (io.optionTemplate.id == 72) {
                                                            level = io.param;
                                                            if (level < 7) {
                                                                io.param++;
                                                            }
                                                            break;
                                                        }
                                                    }
                                                    if (level < 7) {
                                                        active = true;
                                                        if (level == 0) {
                                                            itemr.itemOptions.add(new ItemOption(72, 1));
                                                        }
                                                        for (ItemOption io : itemr.itemOptions) {
                                                            if (io.optionTemplate.id == 0) {
                                                                io.param += (io.param * 10 / 100);
                                                                break;
                                                            }
                                                        }
                                                        Service.getInstance().point(player);
                                                    } else {
                                                        Service.getInstance().sendThongBao(player, "Găng tay của đệ ngươi đã đạt cấp tối đa");
                                                    }
                                                } else {
                                                    Service.getInstance().sendThongBao(player, "Đệ ngươi hiện tại có đeo găng đâu");
                                                }
                                            } else {
                                                Service.getInstance().sendThongBao(player, "Ngươi đâu có đệ tử");
                                            }
                                            break;
                                    }
                                    if (active) {
                                        InventoryService.gI().subQuantityItemsBag(player, nro1s, 1);
                                        InventoryService.gI().subQuantityItemsBag(player, nro2s, 1);
                                        InventoryService.gI().subQuantityItemsBag(player, nro3s, 1);
                                        InventoryService.gI().subQuantityItemsBag(player, nro4s, 1);
                                        InventoryService.gI().subQuantityItemsBag(player, nro5s, 1);
                                        InventoryService.gI().subQuantityItemsBag(player, nro6s, 1);
                                        InventoryService.gI().subQuantityItemsBag(player, nro7s, 1);
                                        Service.getInstance().sendMoney(player);
                                        InventoryService.gI().sendItemBags(player);
                                        this.npcChat(player, "|1|Điều ước của ngươi đã thành sự thật");
                                    } else {
                                        if (!selectMenu) {
                                            this.npcChat(player, "Điều ước này đã quá sức với ta, ta sẽ cho ngươi chọn lại");
                                        }
                                    }
                                } else if (player.iDMark.getIndexMenu() == ConstNpc.RONG_BANG_CHI_SO) {
                                    Item nro1s = InventoryService.gI().findItemBagByTemp(player, 925);
                                    Item nro2s = InventoryService.gI().findItemBagByTemp(player, 926);
                                    Item nro3s = InventoryService.gI().findItemBagByTemp(player, 927);
                                    Item nro4s = InventoryService.gI().findItemBagByTemp(player, 928);
                                    Item nro5s = InventoryService.gI().findItemBagByTemp(player, 929);
                                    Item nro6s = InventoryService.gI().findItemBagByTemp(player, 930);
                                    Item nro7s = InventoryService.gI().findItemBagByTemp(player, 931);
                                    if (nro1s == null || nro2s == null || nro3s == null || nro4s == null || nro5s == null || nro6s == null || nro7s == null) {
                                        Service.getInstance().sendThongBao(player, "Yêu cầu đủ bộ Ngọc rồng Băng từ 1s đến 7s");
                                        return;
                                    }
                                    boolean active = false;
                                    switch (select) {
                                        case 0:
                                        case 1:
                                        case 2:
                                        case 3:
                                        case 4:
                                            Item item = player.inventory.itemsBody.get(select);
                                            if (item.isNotNullItem()) {
                                                int level = 0;
                                                ItemOption opsLevel = null;
                                                ItemOption ops1 = null;
                                                ItemOption ops2 = null;
                                                ItemOption ops3 = null;
                                                for (ItemOption ioo : item.itemOptions) {
                                                    if (ioo.optionTemplate.id == 248) {
                                                        opsLevel = ioo;
                                                        level = ioo.param;
                                                    }
                                                    if (ioo.optionTemplate.id == 50) {
                                                        ops1 = ioo;
                                                    }
                                                    if (ioo.optionTemplate.id == 77) {
                                                        ops2 = ioo;
                                                    }
                                                    if (ioo.optionTemplate.id == 103) {
                                                        ops3 = ioo;
                                                    }
                                                }
                                                if (level < 20) {
                                                    active = true;
                                                    if (opsLevel == null) {
                                                        item.itemOptions.add(new ItemOption(248, 1));
                                                    } else {
                                                        opsLevel.param++;
                                                    }
                                                    if (ops1 == null) {
                                                        item.itemOptions.add(new ItemOption(50, 1));
                                                    } else {
                                                        ops1.param++;
                                                    }
                                                    if (ops2 == null) {
                                                        item.itemOptions.add(new ItemOption(77, 1));
                                                    } else {
                                                        ops2.param++;
                                                    }
                                                    if (ops3 == null) {
                                                        item.itemOptions.add(new ItemOption(103, 1));
                                                    } else {
                                                        ops3.param++;
                                                    }
                                                    InventoryService.gI().sendItemBody(player);
                                                } else {
                                                    Service.getInstance().sendThongBao(player, "Tối đa 20 lần ước");
                                                }
                                            } else {
                                                Service.getInstance().sendThongBao(player, "Ngươi hiện tại có đeo món này đâu");
                                            }
                                            break;
                                    }
                                    if (active) {
                                        InventoryService.gI().subQuantityItemsBag(player, nro1s, 1);
                                        InventoryService.gI().subQuantityItemsBag(player, nro2s, 1);
                                        InventoryService.gI().subQuantityItemsBag(player, nro3s, 1);
                                        InventoryService.gI().subQuantityItemsBag(player, nro4s, 1);
                                        InventoryService.gI().subQuantityItemsBag(player, nro5s, 1);
                                        InventoryService.gI().subQuantityItemsBag(player, nro6s, 1);
                                        InventoryService.gI().subQuantityItemsBag(player, nro7s, 1);
                                        Service.getInstance().sendMoney(player);
                                        InventoryService.gI().sendItemBags(player);
                                        this.npcChat(player, "|1|Điều ước của ngươi đã thành sự thật");
                                    } else {
                                        this.npcChat(player, "Điều ước này đã quá sức với ta, ta sẽ cho ngươi chọn lại");
                                    }
                                } else if (player.iDMark.getIndexMenu() == ConstNpc.RONG_SIEU_CAP) {
                                    Item nrosc = InventoryService.gI().findItemBagByTemp(player, 1015);
                                    if (nrosc == null) {
                                        Service.getInstance().sendThongBao(player, "Yêu cầu 1 viên Ngọc rồng Siêu cấp");
                                        return;
                                    }
                                    boolean active = false;
                                    switch (select) {
                                        case 0:
                                            if ((player.nPoint.dameg + 1000) <= (player.nPoint.powerLimit.getDamage() + 20000)) {
                                                player.nPoint.dameg += 1000;
                                                Service.getInstance().point(player);
                                                active = true;
                                            } else {
                                                Service.getInstance().sendThongBaoOK(player, "Tối đa thêm 20k SDG");
                                                return;
                                            }
                                            break;
                                        case 1:
                                            if ((player.nPoint.hpg + 2000) <= (player.nPoint.powerLimit.getHp() + 40000)) {
                                                player.nPoint.hpg += 2000;
                                                Service.getInstance().point(player);
                                                active = true;
                                            } else {
                                                Service.getInstance().sendThongBaoOK(player, "Tối đa thêm 40k HPG");
                                                return;
                                            }
                                            break;
                                        case 2:
                                            if ((player.nPoint.mpg + 2000) <= (player.nPoint.powerLimit.getMp() + 40000)) {
                                                player.nPoint.mpg += 2000;
                                                Service.getInstance().point(player);
                                                active = true;
                                            } else {
                                                Service.getInstance().sendThongBaoOK(player, "Tối đa thêm 40k MPG");
                                                return;
                                            }
                                            break;
                                        case 3:
                                            player.itemTimesieucap.lastTimeSuPhuSC = System.currentTimeMillis();
                                            player.itemTimesieucap.isSuPhuSC = true;
                                            Service.getInstance().point(player);
                                            ItemTimeService.gI().sendAllItemTime(player);
                                            active = true;
                                            break;
                                        case 4:
                                            player.itemTimesieucap.lastTimeDeTuSC = System.currentTimeMillis();
                                            player.itemTimesieucap.isDeTuSC = true;
                                            Service.getInstance().point(player);
                                            ItemTimeService.gI().sendAllItemTime(player);
                                            active = true;
                                            break;
                                    }
                                    if (active) {
                                        InventoryService.gI().subQuantityItemsBag(player, nrosc, 1);
                                        Service.getInstance().sendMoney(player);
                                        InventoryService.gI().sendItemBags(player);
                                        this.npcChat(player, "|1|Điều ước của ngươi đã thành sự thật");
                                    } else {
                                        this.npcChat(player, "Điều ước này đã quá sức với ta, ta sẽ cho ngươi chọn lại");
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.VUA_VEGETA:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                EscortedBoss escortedBoss = player.getEscortedBoss();
                                if (escortedBoss != null && escortedBoss instanceof Qilin) {
                                    this.createOtherMenu(player, ConstNpc.ESCORT_QILIN_MENU,
                                            "Ah con đã tìm thấy lân con thất lạc của ta\nTa sẽ thưởng cho con 1 viên Capsule Tết 2024.",
                                            "Đồng ý", "Từ chối");
                                } else {
                                    if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                                        super.openBaseMenu(player);
                                    }
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (player.iDMark.getIndexMenu() == ConstNpc.ESCORT_QILIN_MENU) {
                                    switch (select) {
                                        case 0: {
                                            if (InventoryService.gI().getCountEmptyBag(player) == 0) {
                                                this.npcChat(player,
                                                        "Con phải có ít nhất 1 ô trống trong hành trang ta mới đưa cho con được");
                                                return;
                                            }
                                            EscortedBoss escortedBoss = player.getEscortedBoss();
                                            if (escortedBoss != null) {
                                                escortedBoss.stopEscorting();
                                                Item item = ItemService.gI()
                                                        .createNewItem((short) ConstItem.CAPSULE_TET_2022);
                                                item.quantity = 1;
                                                InventoryService.gI().addItemBag(player, item, 0);
                                                InventoryService.gI().sendItemBags(player);
                                                Service.getInstance().sendThongBao(player,
                                                        "Bạn nhận được " + item.template.name);
                                            }
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.TRONG_TAI:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 113) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Đại hội võ thuật Siêu Hạng"
                                            + "\ndiễn ra 24/7 kể cả ngày lễ và chủ nhật"
                                            + "\nHãy thi đấu để khẳng định đẳng cấp của mình nhé"
                                            + "\n|7|(Chức năng đang bảo trì)",
                                            "Top 100\nCao thủ", "Nhận thưởng quà top", "Đấu ngay", "Về\nĐại Hội\nVõ Thuật");
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 113) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                             try (Connection con = DBService.gI().getConnectionForGetPlayer()) {
                                                Manager.topSieuHang = TopManager.realTopSieuHang(con);
                                            } catch (Exception ignored) {
                                                ignored.printStackTrace();
                                            }
                                            Service.getInstance().showListTop(player, Manager.topSieuHang, (byte) 1);
                                            break;
                                            case 1:
                                                LocalDate currentDate = LocalDate.now();
                                                LocalDate lastClaimDate = Instant.ofEpochMilli(player.timesieuhang).atZone(ZoneId.systemDefault()).toLocalDate();
                                                this.createOtherMenu(player, 1, "|7|Ngươi ở Top " + player.rankSieuHang
                                                        + "\n|0|Mỗi ngày chỉ có thể nhận thưởng một lần"
                                                        + "\n|5|" + ((lastClaimDate.isEqual(currentDate)) ? "(Đã nhận thưởng)" : "(Chưa nhận thưởng)"), "Nhận thưởng");
                                                break;
                                            case 2:
                                                List<TOP> tops = new ArrayList<>();
                                                try (Connection con = DBService.gI().getConnectionForGetPlayer()) {
                                                    tops.addAll(TopManager.realTopSieuHang(con));
                                                } catch (Exception ignored) {
                                                    ignored.printStackTrace();
                                                }
                                                Service.getInstance().showListTopDauNhanh(player, tops, (byte) 1, tops.get(0).rank);
                                                tops.clear();
                                                break;
                                            case 3:
                                                ChangeMapService.gI().changeMapNonSpaceship(player, 52, -1, 432);
                                                break;
                                        }
                                    } else if (player.iDMark.getIndexMenu() == 1) {
                                        switch (select) {
                                            case 0:
                                                if (!player.isnhanthuong1) {
                                                    LocalDate currentDate = LocalDate.now();
                                                    LocalDate lastClaimDate = Instant.ofEpochMilli(player.timesieuhang).atZone(ZoneId.systemDefault()).toLocalDate();

                                                    if (lastClaimDate.isEqual(currentDate)) {
                                                        Service.getInstance().sendThongBao(player, "Bạn đã nhận thưởng rồi trong ngày hôm nay");
                                                    } else {
                                                        Item voso = null;
                                                        voso = ItemService.gI().createNewItem((short) 1296);
                                                        if (player.rankSieuHang == 10) {
                                                            voso.quantity += 1;
                                                        }
                                                        if (player.rankSieuHang == 9) {
                                                            voso.quantity += 1;
                                                        }
                                                        if (player.rankSieuHang == 8) {
                                                            voso.quantity += 1;
                                                        }
                                                        if (player.rankSieuHang == 7) {
                                                            voso.quantity += 1;
                                                        }
                                                        if (player.rankSieuHang == 6) {
                                                            voso.quantity += 1;
                                                        }
                                                        if (player.rankSieuHang == 5) {
                                                            voso.quantity += 1;
                                                        }
                                                        if (player.rankSieuHang == 4) {
                                                            voso.quantity += 1;
                                                        }
                                                        if (player.rankSieuHang == 3) {
                                                            voso.quantity += 1;
                                                        }
                                                        if (player.rankSieuHang == 2) {
                                                            voso.quantity += 1;
                                                        }
                                                        if (player.rankSieuHang == 1) {
                                                            voso.quantity += 2;
                                                        }
                                                        InventoryService.gI().addItemBag(player, voso, 99);
                                                        InventoryService.gI().sendItemBags(player);
                                                        PlayerService.gI().sendInfoHpMpMoney(player);
                                                        Service.getInstance().sendThongBao(player, "Bạn đã nhận được " + voso.template.name);

                                                        // Cập nhật thông tin ngày nhận thưởng
                                                        player.timesieuhang = System.currentTimeMillis();
                                                        player.isnhanthuong1 = true;
                                                    }
                                                    if (!lastClaimDate.isEqual(currentDate)) {
                                                        player.isnhanthuong1 = false;
                                                    }
                                                } else {
                                                    Service.getInstance().sendThongBao(player, "Bạn đã nhận thưởng rồi");
                                                }
                                                break;

                                        }
                                    }

                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.ONG_GOHAN:
                case ConstNpc.ONG_MOORI:
                case ConstNpc.ONG_PARAGUS:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                String mtv;
                                if (player.getSession().actived) {
                                    mtv = "Bạn đã là thành viên chính thức của " + Manager.SERVER_NAME
                                            + "\nĐã mở khóa tính năng Giao dịch và Chat thế giới !!!";
                                } else {
                                    mtv = "Tài khoản của bạn chưa được Mở thành viên !!!";
                                }
                                if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                            "Hãy cùng khám phá Thế giới này nào\n"
                                                    .replaceAll("%1", player.gender == ConstPlayer.TRAI_DAT ? "Quy lão Kamê"
                                                            : player.gender == ConstPlayer.NAMEC ? "Trưởng lão Guru" : "Vua Vegeta")
                                            + "Ta đang giữ tiền tiết kiệm của con"
                                            + "\n|1|Coin hiện còn : " + " " + Util.format(player.getSession().vnd)
                                            + "\n\n|2| ***" + mtv + "***",
                                            "Mở\nThành Viên", "Đổi\nMật Khẩu", "Giftcode", "Nhận Đệ Tử",
                                            "Khu\nTest Đam", "Next NV");//, "Điểm danh\nhằng ngày");//, "Mở thành viên", "Quà mốc nạp", 
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (player.iDMark.isBaseMenu()) {
                                    switch (select) {
                                        case 0:
                                            if (player.getSession().actived == true) {
                                                this.createOtherMenu(player, 53747,
                                                        "|7|MỞ THÀNH VIÊN"
                                                        + "\n|5|Bạn đã là thành viên chính thức của " + Manager.SERVER_NAME
                                                        + "\nĐã mở khóa chức năng Giao dịch và Chat thế giới"
                                                        + "\n|4|Hãy tiếp tục nâng cao sức mạnh của mình lên nào",
                                                        "Ố kê");
                                            } else {
                                                this.createOtherMenu(player, 1456,
                                                        "|7|MỞ THÀNH VIÊN"
                                                        + "\n|5|Khi bạn trờ thành thành viên chính thức của " + Manager.SERVER_NAME + " sẽ được mở khóa chức năng Giao dịch và Chat thế giới"
                                                        + "\n|3|Giá Kích hoạt tài khoản: 10.000 Coin"
                                                        + "\n|1|Coin hiện còn : " + " " + Util.format(player.getSession().vnd) + " Coin"
                                                        + "\n|7|Bạn có chắc muốn Kích hoạt tài khoản không?",
                                                        "Đồng ý", "Từ chối");
                                            }
                                            break;
                                        case 1:
                                            Input.gI().createFormChangePassword(player);
                                            break;
                                        case 2:
                                            Input.gI().createFormGiftCode(player);
                                            break;
                                        case 3:
                                            if (player.pet == null) {
                                                PetService.gI().createNormalPet(player);
                                                Service.getInstance().sendThongBao(player, "Bạn vừa nhận được đệ tử");
                                            } else {
                                                this.npcChat(player, "Tham Lam");
                                            }
                                            break;
//                                        case 3:
//                                            if (player.getSession().goldBar > 0) {
//                                                this.createOtherMenu(player, ConstNpc.MENU_PHAN_THUONG,
//                                                        "Ta đang giữ cho con: " + player.getSession().goldBar
//                                                        + " Thỏi vàng, con có nhận luôn không?",
//                                                        "Nhận " + player.getSession().goldBar + "\nThỏi vàng",
//                                                        "Từ chối");
//                                                // "Rương Quà",
//                                            } else {
//                                                this.createOtherMenu(player, ConstNpc.MENU_PHAN_THUONG,
//                                                        "Hiện tại ông không giữ của con thỏi vàng nào cả!", "Từ chối");
//                                                // "Mở rương\nquà",
//                                            }
//                                            break;
                                        case 4:
                                            ChangeMapService.gI().changeMapBySpaceShip(player, 165, -1, -1);
                                            break;
                                        case 5:
                                            if (TaskService.gI().TaskNext(player) == true && TaskService.gI().getIdTask(player) < ConstTask.TASK_34_0) {
                                                player.playerTask.taskMain.index++;
                                                if (player.playerTask.taskMain.index >= player.playerTask.taskMain.subTasks.size()) {
                                                    TaskService.gI().sendNextTaskMain(player);
                                                } else {
                                                    TaskService.gI().sendNextSubTask(player);
                                                }
                                            } else {
                                                this.npcChat(player, "Chỉ hỗ trợ Next Nhiệm vụ Kết bạn và Vào bang");
                                            }
                                            //                                           if (TaskService.gI().getIdTask(player) < ConstTask.TASK_34_0) {
                                            //                                               player.playerTask.taskMain.index++;
                                            //                                              if (player.playerTask.taskMain.index >= player.playerTask.taskMain.subTasks.size()) {
                                            //                                                  TaskService.gI().sendNextTaskMain(player);
                                            //                                              } else {
                                            //                                                 TaskService.gI().sendNextSubTask(player);
                                            //                                             }
                                            //                                          } else {
                                            //                                             this.npcChat(player, "Đã hết nhiệm vụ");
                                            //                                           }
                                            break;
                                        case 6:
                                            this.createOtherMenu(player, 6565,
                                                    "Điểm danh hằng ngày nhận các phần thưởng sau:"
                                                    + "\n- 1000 Thỏi vàng"
                                                    + "\n- 10 " + ConstItem.NAME_XU_SERVER
                                                    + "\n- 1 Máy dò Boss"
                                                    + "\n" + ((player.getSession().actived) ? "|1|Đã mở thành viên" : "|7|Bạn chưa mở thành viên"),
                                                    "Nhận");
                                            break;
//                                        case 7:
//                                            if (player.getSession().actived == true) {
//                                                this.createOtherMenu(player, 53747,
//                                                        "|7|MỞ THÀNH VIÊN"
//                                                        + "\n|5|Bạn đã là thành viên chính thức của Ngọc Rồng"
//                                                        + "\nĐã mở khóa chức năng Giao dịch và Chat thế giới"
//                                                        + "\n|4|Hãy tiếp tục nâng cao sức mạnh của mình lên nào",
//                                                        "Ố kê");
//                                            } else {
//                                                this.createOtherMenu(player, 1456,
//                                                        "|7|MỞ THÀNH VIÊN"
//                                                        + "\n|5|Khi bạn trờ thành thành viên chính thức của Ngọc Rồng sẽ được mở khóa chức năng Giao dịch và Chat thế giới"
//                                                        + "\n|3|Giá Kích hoạt tài khoản: 10.000 Coin"
//                                                        + "\n|1|Coin hiện còn : " + " " + Util.format(player.getSession().vnd) + " Coin"
//                                                        + "\n|7|Bạn có chắc muốn Kích hoạt tài khoản không?",
//                                                        "Đồng ý", "Từ chối");
//                                            }
//                                            break;
                                        }
                                } 
                                
                                
                                // Auto mở thành viên khi đạt nhiệm vụ 21
if (!player.getSession().actived && player.playerTask.taskMain.id >= 21) {
    try {
        PlayerDAO.subActive(player, 1);
        player.getSession().actived = true;
        Service.getInstance().sendThongBao(player,
                "|2|Bạn đã mở thành viên vì đạt nhiệm vụ 21!");
    } catch (Exception e) {
        e.printStackTrace();
    }
}
                                
                                
                                
                                
                                else if (player.iDMark.getIndexMenu() == 6565) {
                                    switch (select) {
                                        case 0:
                                            if (player.getSession().actived) {
                                                if (player.diemdanh == 0) {
                                                    if (InventoryService.gI().getCountEmptyBag(player) < 3) {
                                                        this.npcChat(player,
                                                                "Con phải có ít nhất 3 ô trống trong hành trang ta mới đưa cho con được");
                                                        return;
                                                    }
                                                    Item thoivang = ItemService.gI().createNewItem((short) 457, 1000);
                                                    Item datitan = ItemService.gI().createNewItem((short) 1400, 10);
                                                    Item maydoboss = ItemService.gI().createNewItem((short) 1296, 1);
                                                    InventoryService.gI().addItemBag(player, thoivang, 1);
                                                    InventoryService.gI().addItemBag(player, datitan, 1);
                                                    InventoryService.gI().addItemBag(player, maydoboss, 1);
                                                    InventoryService.gI().sendItemBags(player);
                                                    player.diemdanh = System.currentTimeMillis();
                                                    Service.getInstance().sendThongBao(player, "|7|Bạn vừa nhận được 1000 Thỏi vàng, 10 " + ConstItem.NAME_XU_SERVER + ", 1 Máy dò Boss");
                                                } else {
                                                    this.npcChat(player, "Hôm nay đã nhận rồi mà !!!");
                                                }
                                            } else {
                                                this.npcChat(player, "Vui lòng mở thành viên để mở khóa chức năng này !!!");
                                            }
                                            break;
                                    }
                                } else if (player.iDMark.getIndexMenu() == 1456) {
                                    switch (select) {
                                        case 0:
                                            if (player.getSession().actived == true) {
                                                Service.getInstance().sendThongBao(player, "|4|Bạn đã mở thành viên rồi mà. Tiếp tục chơi game thui nào!!!!");
                                                return;
                                            }
                                            if (player.getSession().vnd < 10000 && player.getSession().actived == false) {
                                                Service.getInstance().sendThongBao(player, "|3|Coin chưa đủ 10.000Đ. Vui lòng liên hệ ADMIN hoặc lên Website để nạp!!");
                                                return;
                                            }
                                            if (!PlayerDAO.checkVnd(player, 10000)) {
                                                Service.getInstance().sendThongBao(player, "|7|Không đủ VNĐ");
                                                return;
                                            }
                                            if (player.getSession().vnd >= 10000 && player.getSession().actived == false) {
                                                try {
                                                    PlayerDAO.subActive(player, 1);
                                                    PlayerDAO.subVnd(player, 10000);
                                                    player.getSession().actived = true;
                                                    Service.getInstance().sendThongBao(player, "|2|Bạn đã mở thành viên Thành công. Đã mở khóa chức năng Giao dịch và Chat thế giới !!");
                                                } catch (Exception e) {
                                                    System.out.println("Loi chuc nang mo thanh vien");
                                                }
                                            }
                                            break;
                                    }
                                } else if (player.iDMark.getIndexMenu() == ConstNpc.QUA_TAN_THU) {
                                    switch (select) {
                                        case 0:
                                            // if (!player.gift.gemTanThu) {
                                            if (true) {
                                                player.inventory.gem = 200000;
                                                Service.getInstance().sendMoney(player);
                                                Service.getInstance().sendThongBao(player,
                                                        "Bạn vừa nhận được 100K ngọc xanh");
                                                player.gift.gemTanThu = true;
                                            } else {
                                                this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                        "Con đã nhận phần quà này rồi mà", "Đóng");
                                            }
                                            break;
                                        // case 1:
                                        // if (nhanVang) {
                                        // player.inventory.gold = Inventory.LIMIT_GOLD;
                                        // Service.getInstance().sendMoney(player);
                                        // Service.getInstance().sendThongBao(player, "Bạn vừa nhận được 2 tỉ vàng");
                                        // } else {
                                        // this.npcChat(player,"Tính năng Nhận vàng đã đóng.");
                                        // }
                                        // break;
                                        case 1:
                                            if (nhanDeTu) {
                                                if (player.pet == null) {
                                                    PetService.gI().createNormalPet(player);
                                                    Service.getInstance().sendThongBao(player,
                                                            "Bạn vừa nhận được đệ tử");
                                                } else {
                                                    this.npcChat(player, "Con đã nhận đệ tử rồi");
                                                }
                                            } else {
                                                this.npcChat(player, "Tính năng Nhận đệ tử đã đóng.");
                                            }
                                            break;

                                    }
                                } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_PHAN_THUONG) {
                                    switch (select) {
                                        // case 0:
                                        // ShopService.gI().openBoxItemReward(player);
                                        // break;
                                        case 0:
                                            if (player.getSession().goldBar > 0) {
                                                if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                                                    int quantity = player.getSession().goldBar;
                                                    Item goldBar = ItemService.gI().createNewItem((short) 457,
                                                            quantity);
                                                    InventoryService.gI().addItemBag(player, goldBar, 0);
                                                    InventoryService.gI().sendItemBags(player);
                                                    this.npcChat(player, "Ông đã để " + quantity
                                                            + " Thỏi vàng vào hành trang con rồi đấy");
                                                    PlayerDAO.subGoldBar(player, quantity);
                                                    player.getSession().goldBar = 0;
                                                } else {
                                                    this.npcChat(player,
                                                            "Con phải có ít nhất 1 ô trống trong hành trang ông mới đưa cho con được");
                                                }
                                            }
                                            break;
                                    }
                                }
                            }
                        }
                    };
                    break;

                case ConstNpc.BUNMA:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                            "Cậu cần trang bị gì cứ đến chỗ tôi nhé",
                                            "Cửa\nhàng", "Xử lý\nvật phẩm");
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (player.iDMark.isBaseMenu()) {
                                    switch (select) {
                                        case 0 -> {
                                            if (player.gender == ConstPlayer.TRAI_DAT) {
                                                this.openShopWithGender(player, ConstNpc.SHOP_BUNMA_QK_0, 0);
                                            } else {
                                                this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                        "Xin lỗi cưng, chị chỉ bán đồ cho người Trái Đất", "Đóng");
                                            }
                                        }
                                        case 1 ->
                                            this.createOtherMenu(player, ConstNpc.XU_LY_VAT_PHAM,
                                                    "Ta có thể giúp ngươi xử lý vật phẩm trong hành trang?",
                                                    "Tách Stacks\nVật Phẩm",
                                                    "Gộp\nVật Phẩm",
                                                    "Xóa\nVật Phẩm");
                                    }
                                } else if (player.iDMark.getIndexMenu() == ConstNpc.XU_LY_VAT_PHAM
                                        || player.iDMark.getIndexMenu() == ConstNpc.XOA_VAT_PHAM) {
                                    handleXuLyVatPham(this, player, select);
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.DENDE:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                                    if (player.isHoldNamecBall) {
                                        this.createOtherMenu(player, ConstNpc.ORTHER_MENU,
                                                "Ô,ngọc rồng Namek... tìm đủ 7 viên ngọc có thể triệu hồi Rồng Thần!",
                                                "Gọi rồng", "Từ chối");
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                                "Anh cần trang bị gì cứ đến chỗ em nhé",
                                                "Cửa\nhàng", "Xử lý\nvật phẩm");
                                    }
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (player.iDMark.isBaseMenu()) {
                                    switch (select) {
                                        case 0 -> {
                                            if (player.gender == ConstPlayer.NAMEC) {
                                                this.openShopWithGender(player, ConstNpc.SHOP_DENDE_0, 0);
                                            } else {
                                                this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                        "Xin lỗi anh, em chỉ bán đồ cho dân tộc Namếc", "Đóng");
                                            }
                                        }
                                        case 1 ->
                                            this.createOtherMenu(player, ConstNpc.XU_LY_VAT_PHAM,
                                                    "Ta có thể giúp gì cho ngươi?",
                                                    "Tách Stacks\nVật Phẩm",
                                                    "Gộp\nVật Phẩm",
                                                    "Xóa\nVật Phẩm");
                                    }
                                } else if (player.iDMark.getIndexMenu() == ConstNpc.XU_LY_VAT_PHAM
                                        || player.iDMark.getIndexMenu() == ConstNpc.XOA_VAT_PHAM) {
                                    handleXuLyVatPham(this, player, select);
                                } else if (player.iDMark.getIndexMenu() == ConstNpc.ORTHER_MENU) {
                                    NamekBallWar.gI().summonDragon(player, this);
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.APPULE:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                            "Ngươi cần trang bị gì cứ đến chỗ ta nhé",
                                            "Cửa\nhàng", "Xử lý\nvật phẩm");
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (player.iDMark.isBaseMenu()) {
                                    switch (select) {
                                        case 0 -> {
                                            if (player.gender == ConstPlayer.XAYDA) {
                                                this.openShopWithGender(player, ConstNpc.SHOP_APPULE_0, 0);
                                            } else {
                                                this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                        "Ta chỉ bán đồ cho người Xayda. Cút!", "Đóng");
                                            }
                                        }
                                        case 1 ->
                                            this.createOtherMenu(player, ConstNpc.XU_LY_VAT_PHAM,
                                                    "Ta có thể giúp gì cho ngươi?",
                                                    "Tách Stacks\nVật Phẩm",
                                                    "Gộp\nVật Phẩm",
                                                    "Xóa\nVật Phẩm");
                                    }
                                } else if (player.iDMark.getIndexMenu() == ConstNpc.XU_LY_VAT_PHAM
                                        || player.iDMark.getIndexMenu() == ConstNpc.XOA_VAT_PHAM) {
                                    handleXuLyVatPham(this, player, select);
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.DR_DRIEF:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player pl) {
                            if (canOpenNpc(pl)) {
                                if (this.mapId == 84) {
                                    this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                            "Tàu Vũ Trụ của ta có thể đưa cậu đến hành tinh khác chỉ trong 3 giây. Cậu muốn đi đâu?",
                                            pl.gender == ConstPlayer.TRAI_DAT ? "Đến\nTrái Đất"
                                                    : pl.gender == ConstPlayer.NAMEC ? "Đến\nNamếc" : "Đến\nXayda");
                                } else if (this.mapId == 153) {
                                    Clan clan = pl.clan;
                                    ClanMember cm = pl.clanMember;
                                    if (cm != null && cm.role == Clan.LEADER) {
                                        this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                                "Cần 1000 capsule bang [đang có " + clan.clanPoint
                                                + " capsule bang] để nâng cấp bang hội lên cấp "
                                                + (clan.level + 1) + "\n"
                                                + "+1 tối đa số lượng thành viên\n"
                                                + "|3|Cùng 1 thành viên trong bang đánh quái ở Lãnh địa bang hội để nhận được Capsule bang hội",
                                                "Về\nĐảoKame", "Góp " + cm.memberPoint + " capsule", "Nâng cấp", "Từ chối");
                                    } else if (cm != null) {
                                        this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                                "Bạn đang có " + cm.memberPoint + " capsule bang, bạn có muốn đóng góp toàn bộ cho bang hội của mình không?",
                                                "Về\nĐảoKame", "Đồng ý", "Từ chối");
                                    } else {
                                        this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                                "Dữ liệu bang hội của bạn đang bị lỗi, vui lòng thử lại sau.",
                                                "Về\nĐảoKame");
                                    }
                                } else if (!TaskService.gI().checkDoneTaskTalkNpc(pl, this)) {
                                    if (pl.playerTask.taskMain.id == 7) {
                                        NpcService.gI().createTutorial(pl, this.avartar,
                                                "Hãy lên đường cứu đứa bé nhà tôi\n"
                                                + "Chắc bây giờ nó đang sợ hãi lắm rồi");
                                    } else {
                                        this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                                "Tàu Vũ Trụ của ta có thể đưa cậu đến hành tinh khác chỉ trong 3 giây. Cậu muốn đi đâu?",
                                                "Đến\nNamếc", "Đến\nXayda", "Siêu thị");
                                    }
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 84) {
                                    ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 24, -1, -1);
                                } else if (mapId == 153) {
                                    switch (select) {
                                        case 0:
                                            ChangeMapService.gI().changeMap(player, ConstMap.DAO_KAME, -1, 1059, 408);
                                            break;
                                        case 1:
                                            Clan clan = player.clan;
                                            if (clan == null) {
                                                Service.getInstance().sendThongBao(player, "Chưa có bang hội");
                                                return;
                                            }
                                            ClanMember cm = player.clanMember;
                                            player.clan.clanPoint += cm.memberPoint;
                                            cm.clanPoint += cm.memberPoint;
                                            cm.memberPoint = 0;
                                            Service.getInstance().sendThongBao(player, "Đóng góp thành công");
                                            break;
                                        case 2:
                                            Clan clan1 = player.clan;
                                            if (clan1 == null) {
                                                Service.getInstance().sendThongBao(player,
                                                        "Bạn chưa có bang hội");
                                                return;
                                            }
                                            if (clan1.level >= 10) {
                                                Service.getInstance().sendThongBao(player,
                                                        "Bang hội của bạn đã đạt cấp tối đa");
                                                return;
                                            }
                                            if (clan1.clanPoint < 1000) {
                                                Service.getInstance().sendThongBao(player, "Không đủ capsule");
                                            } else {
                                                clan1.level++;
                                                clan1.maxMember++;
                                                clan1.clanPoint -= 1000;
                                                Service.getInstance().sendThongBao(player,
                                                        "Bang hội của bạn đã được nâng cấp lên cấp " + clan1.level);
                                            }
                                            break;
                                    }
                                } else if (player.iDMark.isBaseMenu()) {
                                    switch (select) {
                                        case 0:
                                            ChangeMapService.gI().changeMapBySpaceShip(player, 25, -1, -1);
                                            break;
                                        case 1:
                                            ChangeMapService.gI().changeMapBySpaceShip(player, 26, -1, -1);
                                            break;
                                        case 2:
                                            ChangeMapService.gI().changeMapBySpaceShip(player, 84, -1, -1);
                                            break;
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.CARGO:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player pl) {
                            if (canOpenNpc(pl)) {
                                if (!TaskService.gI().checkDoneTaskTalkNpc(pl, this)) {
                                    if (pl.playerTask.taskMain.id == 7) {
                                        NpcService.gI().createTutorial(pl, this.avartar,
                                                "Hãy lên đường cứu đứa bé nhà tôi\n"
                                                + "Chắc bây giờ nó đang sợ hãi lắm rồi");
                                    } else {
                                        this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                                "Tàu Vũ Trụ của ta có thể đưa cậu đến hành tinh khác chỉ trong 3 giây. Cậu muốn đi đâu?",
                                                "Đến\nTrái Đất", "Đến\nXayda", "Siêu thị");
                                    }
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (player.iDMark.isBaseMenu()) {
                                    switch (select) {
                                        case 0:
                                            ChangeMapService.gI().changeMapBySpaceShip(player, 24, -1, -1);
                                            break;
                                        case 1:
                                            ChangeMapService.gI().changeMapBySpaceShip(player, 26, -1, -1);
                                            break;
                                        case 2:
                                            ChangeMapService.gI().changeMapBySpaceShip(player, 84, -1, -1);
                                            break;
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.THO_DAI_CA:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                createOtherMenu(player, ConstNpc.BASE_MENU,
                                        "Xin chào, người anh em có muốn đổi Vật Phẩm thường thành Vật Phẩm siêu cấp không?",
                                        "Shop Đổi\nItem", "Shop Đổi\nTrang bị");
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (player.iDMark.isBaseMenu()) {
                                    switch (select) {
                                        case 0:
                                            ShopService.gI().openShopSpecial(player, this, ConstNpc.SHOP_ITEM, 0, -1);
                                            break;
                                        case 1:
                                            ShopService.gI().openShopSpecial(player, this, ConstNpc.SHOP_ITEM_1, 1, -1);
                                            break;
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.RONG_THIENG_VIP:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                                    if (player.isHoldNamecBall) {
                                        this.createOtherMenu(player, ConstNpc.ORTHER_MENU,
                                                "Ô,ngọc rồng Vũ trụ,anh thật may mắn,nếu tìm đủ 7 viên ngọc có thể triệu hồi Rồng Thần Vũ trụ,",
                                                "Gọi rồng", "Từ chối");
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                                "Bang Hội Ngươi Mạnh Đấy Muốn Mua Gì Nào....", "Shop\nBang Hội");
                                    }
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (player.iDMark.isBaseMenu()) {
                                    switch (select) {
                                        case 0:
                                            ShopService.gI().openShopSpecial(player, this, ConstNpc.RONG_1, 0, -1);
                                            break;
                                    }
                                } else if (player.iDMark.getIndexMenu() == ConstNpc.ORTHER_MENU) {
                                    NamekBallWar.gI().summonDragon(player, this);
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.RONG_NHO:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                createOtherMenu(player, ConstNpc.BASE_MENU,
                                        "|1|Hãy đưa cho ta 1 bộ Ngọc Rồng"
                                        + "\n|2|Ta sẽ ban cho ngươi 1 điều ước, hãy suy nghĩ thật kĩ trước khi quyết định?",
                                        "Đại Gia\n+10k\nThỏi Vàng",
                                        "Chí Mạng\nGốc +1%",
                                        "Thay\nChiêu 2-3\nĐệ Tử",
                                        "Đẹp Trai\nNhất\nVũ Trụ",
                                        "Giàu Có\n+10k\nHồng Ngọc",
                                        "100tr\n+Sức Mạnh\nVà\nSức Mạnh");
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (player.iDMark.isBaseMenu()) {
                                    Item nro1s = InventoryService.gI().findItemBagByTemp(player, 14);
                                    Item nro2s = InventoryService.gI().findItemBagByTemp(player, 15);
                                    Item nro3s = InventoryService.gI().findItemBagByTemp(player, 16);
                                    Item nro4s = InventoryService.gI().findItemBagByTemp(player, 17);
                                    Item nro5s = InventoryService.gI().findItemBagByTemp(player, 18);
                                    Item nro6s = InventoryService.gI().findItemBagByTemp(player, 19);
                                    Item nro7s = InventoryService.gI().findItemBagByTemp(player, 20);
                                    if (nro1s == null || nro2s == null || nro3s == null || nro4s == null || nro5s == null || nro6s == null || nro7s == null) {
                                        Service.getInstance().sendThongBao(player, "Yêu cầu đủ bộ Ngọc rồng từ 1s đến 7s");
                                        return;
                                    }
                                    boolean active = false;
                                    switch (select) {
                                        case 0: //200 thỏi vàng
                                            if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                                                active = true;
                                                Item thoivang = ItemService.gI().createNewItem((short) 457, 10000);
                                                InventoryService.gI().addItemBag(player, thoivang, 0);
                                                this.npcChat(player, "|1|Bạn nhận được 10.000 Thỏi vàng");
                                            } else {
                                                this.npcChat(player, "Hành trang không đủ chổ trống");
                                            }
                                            break;
                                        case 1: //chí mạng +1%
                                            if (player.nPoint.critg < 9) {
                                                active = true;
                                                player.nPoint.critg += 1;
                                            }
                                            break;
                                        case 2: //thay chiêu 2-3 đệ tử
                                            if (player.pet != null) {
                                                if (player.pet.playerSkill.skills.get(1).skillId != -1) {
                                                    active = true;
                                                    player.pet.openSkill2();
                                                    if (player.pet.playerSkill.skills.get(2).skillId != -1) {
                                                        player.pet.openSkill3();
                                                    }
                                                } else {
                                                    Service.getInstance().sendThongBao(player, "Ít nhất đệ tử ngươi phải có chiêu 2 chứ!");
                                                }
                                            } else {
                                                Service.getInstance().sendThongBao(player, "Ngươi làm gì có đệ tử?");
                                            }
                                            break;
                                        case 3: //đẹp trai nhất vũ trụ
                                            if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                                                active = true;
                                                byte gender = player.gender;
                                                Item avtVip = ItemService.gI().createNewItem((short) (gender == ConstPlayer.TRAI_DAT ? 227
                                                        : gender == ConstPlayer.NAMEC ? 228 : 229));
                                                avtVip.itemOptions.add(new ItemOption(50, Util.nextInt(1, 50)));
                                                avtVip.itemOptions.add(new ItemOption(77, Util.nextInt(1, 50)));
                                                avtVip.itemOptions.add(new ItemOption(103, Util.nextInt(1, 50)));
                                                avtVip.itemOptions.add(new ItemOption(159, Util.nextInt(1, 4)));
                                                avtVip.itemOptions.add(new ItemOption(106, 1));
                                                avtVip.itemOptions.add(new ItemOption(30, 1));
                                                if (Util.isTrue(99, 100)) {
                                                    avtVip.itemOptions.add(new ItemOption(93, 1));
                                                }
                                                InventoryService.gI().addItemBag(player, avtVip, 0);
                                                InventoryService.gI().sendItemBags(player);
                                            } else {
                                                Service.getInstance().sendThongBao(player, "Hành trang đã đầy");
                                            }
                                            break;
                                        case 4: //+10k Hồng ngọc
                                            active = true;
                                            player.inventory.ruby += 10000;
                                            PlayerService.gI().sendInfoHpMpMoney(player);
                                            break;
                                        case 5: //+100 tr smtn
                                            if (player.nPoint.power < 10_000_000_000L) {
                                                active = true;
                                                Service.getInstance().addSMTN(player, (byte) 2, 100_000_000, false);
                                            } else {
                                                Service.getInstance().sendThongBao(player, "Sức mạnh của ngươi đã vượt quá 10 Tỷ");
                                            }
                                            break;
                                    }
                                    if (active) {
                                        InventoryService.gI().subQuantityItemsBag(player, nro1s, 1);
                                        InventoryService.gI().subQuantityItemsBag(player, nro2s, 1);
                                        InventoryService.gI().subQuantityItemsBag(player, nro3s, 1);
                                        InventoryService.gI().subQuantityItemsBag(player, nro4s, 1);
                                        InventoryService.gI().subQuantityItemsBag(player, nro5s, 1);
                                        InventoryService.gI().subQuantityItemsBag(player, nro6s, 1);
                                        InventoryService.gI().subQuantityItemsBag(player, nro7s, 1);
                                        Service.getInstance().sendMoney(player);
                                        InventoryService.gI().sendItemBags(player);
                                        this.npcChat(player, "|1|Điều ước của ngươi đã thành sự thật");
                                    } else {
                                        this.npcChat(player, "Điều ước này đã quá sức với ta, ta sẽ cho ngươi chọn lại");
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.MIGHTY:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                createOtherMenu(player, ConstNpc.BASE_MENU,
                                        "Xin chào Anh Trai, Tại đây em có bán danh hiệu..!",
                                        "Shop\nSự Kiện",
                                        "Mua\nDanh Hiệu VIP");
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (player.iDMark.isBaseMenu()) {
                                    switch (select) {
                                        case 0:
                                            this.createOtherMenu(player, 1,
                                                    "|2|MÙA HÈ SÔI ĐỘNG - QUÀ TẶNG NGẬP TRÀN\n"
                                                    + "|7|Admin đã mở lại các shop sự kiện cũ để bạn không bỏ lỡ phần thưởng giá trị!\n"
                                                    + "|0|Hãy chọn một sự kiện để khám phá và mua những vật phẩm đặc biệt:",
                                                    "Nước Mía",
                                                    "Câu Cá",
                                                    "Săn Quỷ"
                                            );
                                            break;
                                        case 1:
                                            this.createOtherMenu(player, 2,
                                                    "|7|DANH HIỆU VĨNH VIỄN"
                                                    + "\n|1|Thợ Ngọc : Mua với giá 10Tr Hồng Ngọc"
                                                    + "\n|1|Ai mà xinh thế : Mua với giá 99 Bông Hồng"
                                                    + "\n|1|Tuổi Thơ : Mua với giá 99 Phiếu Bé Ngoan"
                                                    + "\n|1|Cần Thủ : Mua với giá 999 Xô Cá Vàng"
                                                    + "\n|1|(Danh hiệu không tốn ô hành trang)"
                                                    + "\n|1|Lệnh Xem Danh Hiệu : dh",
                                                    "Mua\nThợ Ngọc",
                                                    "Mua\nAi mà Xinh thế",
                                                    "Mua\nTuổi Thơ",
                                                    "Mua\nCần Thủ");
                                            break;
                                    }
                                } else if (player.iDMark.getIndexMenu() == 1) {
                                    switch (select) {
                                        case 0:
                                            ShopService.gI().openShopSpecial(player, this, ConstNpc.SHOP_SU_KIEN_CU_0, 0, -1);
                                            break;
                                        case 1:
                                            this.npcChat(player, "Sự kiện này chưa kết thúc!");
                                            break;
                                        case 2:
                                            this.npcChat(player, "Sự kiện này chưa kết thúc!");
                                            break;
                                    }
                                } else if (player.iDMark.getIndexMenu() == 2) {
                                    switch (select) {
                                        case 0:
                                            if (player.DH_VV_4) {
                                                Service.getInstance().sendThongBao(player, "Bạn đã nhận rồi mà !!!!!!");
                                                return;
                                            }
                                            if (player.inventory.ruby < 10_000_000) {
                                                Service.getInstance().sendThongBao(player, "Bạn chưa đủ 10Tr Hồng ngọc !!!!!!");
                                                return;
                                            }
                                            player.inventory.ruby -= 10_000_000;
                                            player.isTitleUse4 = true;
                                            player.DH_VV_4 = true;
                                            Service.getInstance().point(player);
                                            Service.getInstance().sendTitle(player, 891);
                                            Service.getInstance().sendMoney(player);
                                            Service.getInstance().sendThongBao(player, "Nhận thành công danh hiệu Thợ Ngọc Vĩnh viễn");
                                            break;
                                        case 1:
                                            Item hoa = InventoryService.gI().findItemBagByTemp(player, 1803);
                                            if (player.DH_VV_5) {
                                                Service.getInstance().sendThongBao(player, "Bạn đã nhận rồi mà !!!!!!");
                                                return;
                                            }
                                            if (hoa == null || hoa.quantity < 99) {
                                                Service.getInstance().sendThongBao(player, "Bạn chưa đủ 99 Bông Hồng!");
                                                return;
                                            }
                                            player.isTitleUse5 = true;
                                            player.DH_VV_5 = true;
                                            InventoryService.gI().subQuantityItemsBag(player, hoa, 99);
                                            Service.getInstance().point(player);
                                            Service.getInstance().sendTitle(player, 892);
                                            InventoryService.gI().sendItemBags(player);
                                            Service.getInstance().sendThongBao(player, "Nhận thành công danh hiệu Ai mà Xinh Thế Vĩnh viễn");
                                            break;
                                        case 2:
                                            Item phieu = InventoryService.gI().findItemBagByTemp(player, 1802);
                                            if (player.DH_VV_3) {
                                                Service.getInstance().sendThongBao(player, "Bạn đã nhận rồi mà !!!!!!");
                                                return;
                                            }
                                            if (phieu == null || phieu.quantity < 99) {
                                                Service.getInstance().sendThongBao(player, "Bạn chưa đủ 99 Phiếu!");
                                                return;
                                            }
                                            player.isTitleUse3 = true;
                                            player.DH_VV_3 = true;
                                            InventoryService.gI().subQuantityItemsBag(player, phieu, 99);
                                            Service.getInstance().point(player);
                                            Service.getInstance().sendTitle(player, 890);
                                            InventoryService.gI().sendItemBags(player);
                                            Service.getInstance().sendThongBao(player, "Nhận thành công danh hiệu Tuổi Thơ Vĩnh viễn");
                                            break;
                                        case 3:
                                            Item xoCaVang = InventoryService.gI().findItemBagByTemp(player, 1006);
                                            if (player.DH_VV_2) {
                                                Service.getInstance().sendThongBao(player, "Bạn đã nhận rồi mà !!!!!!");
                                                return;
                                            }
                                            if (xoCaVang == null || xoCaVang.quantity < 999) {
                                                this.npcChat(player, "Bạn chưa đủ 999 Xô Cá Vàng");
                                                return;
                                            }
                                            player.isTitleUse2 = true;
                                            player.DH_VV_2 = true;
                                            InventoryService.gI().subQuantityItemsBag(player, xoCaVang, 999);
                                            Service.getInstance().point(player);
                                            Service.getInstance().sendTitle(player, 889);
                                            Service.getInstance().sendMoney(player);
                                            InventoryService.gI().sendItemBags(player);
                                            Service.getInstance().sendThongBao(player, "Nhận thành công danh hiệu Cần Thủ Vĩnh viễn");
                                            break;
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.BEERYY:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                createOtherMenu(player, ConstNpc.BASE_MENU,
                                        "|1|Hãy đưa cho ta 1 bộ Ngọc Rồng Siêu Cấp"
                                        + "\n|2|Ta sẽ ban cho ngươi 1 điều ước, hãy suy nghĩ thật kĩ trước khi quyết định?",
                                        //"Shop\nThần Bí",
                                        "Đục Lỗ\nTrang Bị",
                                        "Buff 100%\nSD.HP.KI\n(6h)");
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (player.iDMark.isBaseMenu()) {
                                    Item nro1s = InventoryService.gI().findItemBagByTemp(player, 1155);
                                    Item nro2s = InventoryService.gI().findItemBagByTemp(player, 1156);
                                    Item nro3s = InventoryService.gI().findItemBagByTemp(player, 1157);
                                    Item nro4s = InventoryService.gI().findItemBagByTemp(player, 1158);
                                    Item nro5s = InventoryService.gI().findItemBagByTemp(player, 1159);
                                    Item nro6s = InventoryService.gI().findItemBagByTemp(player, 1160);
                                    Item nro7s = InventoryService.gI().findItemBagByTemp(player, 1161);
                                    boolean active = false;
                                    switch (select) {
//                                        case 0:
//                                            ShopService.gI().openShopSpecial(player, this, ConstNpc.SHOP_BEERYY_0, 0, -1);
//                                            break;
                                        case 0:
                                            if (nro1s == null || nro2s == null || nro3s == null || nro4s == null || nro5s == null || nro6s == null || nro7s == null) {
                                                Service.getInstance().sendThongBao(player, "Yêu cầu đủ bộ Ngọc rồng Siêu cấp từ 1s đến 7s");
                                                return;
                                            }
                                            boolean check = false;
                                            for (int i = 0; i < 7; i++) {
                                                Item it = player.inventory.itemsBody.get(i);
                                                if (it.template == null) {
                                                    check = true;
                                                    break;
                                                }
                                            }
                                            if (check == true) {
                                                Service.getInstance().sendThongBao(player, "Vui lòng mang đủ 7 món đầu tiên");
                                                return;
                                            }
                                            int vitri = Util.nextInt(0, 6);
                                            Item itemk = player.inventory.itemsBody.get(vitri);
                                            int star = 0;
                                            ItemOption optionStar = null;
                                            for (ItemOption io : itemk.itemOptions) {
                                                if (io.optionTemplate.id == 107) {
                                                    star = io.param;
                                                    optionStar = io;
                                                    break;
                                                }
                                            }
                                            if (star >= 18) {
                                                Service.getInstance().sendThongBao(player, "Món này đã đạt Max 18 Sao. Thử lại lần nữa nào");
                                                return;
                                            }
                                            if (optionStar == null) {
                                                itemk.itemOptions.add(new ItemOption(107, 1));
                                            } else {
                                                optionStar.param++;
                                            }
                                            active = true;
                                            InventoryService.gI().sendItemBody(player);
                                            Service.getInstance().sendThongBao(player, itemk.template.name + " đã lên " + (star + 1) + " Sao pha lê");
                                            if (active) {
                                                InventoryService.gI().subQuantityItemsBag(player, nro1s, 1);
                                                InventoryService.gI().subQuantityItemsBag(player, nro2s, 1);
                                                InventoryService.gI().subQuantityItemsBag(player, nro3s, 1);
                                                InventoryService.gI().subQuantityItemsBag(player, nro4s, 1);
                                                InventoryService.gI().subQuantityItemsBag(player, nro5s, 1);
                                                InventoryService.gI().subQuantityItemsBag(player, nro6s, 1);
                                                InventoryService.gI().subQuantityItemsBag(player, nro7s, 1);
                                                Service.getInstance().sendMoney(player);
                                                InventoryService.gI().sendItemBags(player);
                                                this.npcChat(player, "|1|Điều ước của ngươi đã thành sự thật");
                                            } else {
                                                this.npcChat(player, "Điều ước này đã quá sức với ta, ta sẽ cho ngươi chọn lại");
                                            }
                                            break;
                                        case 1: //50% HP,KI,SD
                                            if (nro1s == null || nro2s == null || nro3s == null || nro4s == null || nro5s == null || nro6s == null || nro7s == null) {
                                                Service.getInstance().sendThongBao(player, "Yêu cầu đủ bộ Ngọc rồng Siêu cấp từ 1s đến 7s");
                                                return;
                                            }
                                            Clan clan = player.clan;
                                            if (clan == null) {
                                                Service.getInstance().sendThongBao(player, "Vui lòng vào 1 bang hội");
                                                return;
                                            }
                                            active = true;
                                            for (Player pl : clan.membersInGame) {
                                                pl.itemTimesieucap.lastTimeRongHoangKim = System.currentTimeMillis();
                                                pl.itemTimesieucap.isRongHoangKim = true;
                                                Service.getInstance().point(player);
                                                ItemTimeService.gI().sendAllItemTime(player);
                                            }
                                            if (active) {
                                                InventoryService.gI().subQuantityItemsBag(player, nro1s, 1);
                                                InventoryService.gI().subQuantityItemsBag(player, nro2s, 1);
                                                InventoryService.gI().subQuantityItemsBag(player, nro3s, 1);
                                                InventoryService.gI().subQuantityItemsBag(player, nro4s, 1);
                                                InventoryService.gI().subQuantityItemsBag(player, nro5s, 1);
                                                InventoryService.gI().subQuantityItemsBag(player, nro6s, 1);
                                                InventoryService.gI().subQuantityItemsBag(player, nro7s, 1);
                                                Service.getInstance().sendMoney(player);
                                                InventoryService.gI().sendItemBags(player);
                                                this.npcChat(player, "|1|Điều ước của ngươi đã thành sự thật");
                                            } else {
                                                this.npcChat(player, "Điều ước này đã quá sức với ta, ta sẽ cho ngươi chọn lại");
                                            }
                                            break;
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.CUI:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {

                        private final int COST_FIND_BOSS = 20000000;

                        @Override
                        public void openBaseMenu(Player pl) {
                            if (canOpenNpc(pl)) {
                                if (!TaskService.gI().checkDoneTaskTalkNpc(pl, this)) {
                                    if (pl.playerTask.taskMain.id == 7) {
                                        NpcService.gI().createTutorial(pl, this.avartar,
                                                "Hãy lên đường cứu đứa bé nhà tôi\n"
                                                + "Chắc bây giờ nó đang sợ hãi lắm rồi");
                                    } else {
                                        if (this.mapId == 19) {

                                            int taskId = TaskService.gI().getIdTask(pl);
                                            switch (taskId) {
                                                case ConstTask.TASK_19_0:
                                                    this.createOtherMenu(pl, ConstNpc.MENU_FIND_KUKU,
                                                            "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                                                            "Đến chỗ\nKuku\n(" + Util.numberToMoney(COST_FIND_BOSS)
                                                            + " vàng)",
                                                            "Đến Cold", "Đến\nNappa", "Từ chối");
                                                    break;
                                                case ConstTask.TASK_19_1:
                                                    this.createOtherMenu(pl, ConstNpc.MENU_FIND_MAP_DAU_DINH,
                                                            "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                                                            "Đến chỗ\nMập đầu đinh\n("
                                                            + Util.numberToMoney(COST_FIND_BOSS) + " vàng)",
                                                            "Đến Cold", "Đến\nNappa", "Từ chối");
                                                    break;
                                                case ConstTask.TASK_19_2:
                                                    this.createOtherMenu(pl, ConstNpc.MENU_FIND_RAMBO,
                                                            "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                                                            "Đến chỗ\nRambo\n(" + Util.numberToMoney(COST_FIND_BOSS)
                                                            + " vàng)",
                                                            "Đến Cold", "Đến\nNappa", "Từ chối");
                                                    break;
                                                default:
                                                    this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                                            "Đội quân của Fide đang ở Thung lũng Nappa, ta sẽ đưa ngươi đến đó",
                                                            "Đến Cold", "Đến\nNappa", "Từ chối");

                                                    break;
                                            }
                                        } else if (this.mapId == 68) {
                                            this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                                    "Ngươi muốn về Thành Phố Vegeta", "Đồng ý", "Từ chối");
                                        } else {
                                            this.createOtherMenu(pl, ConstNpc.BASE_MENU,
                                                    "Tàu vũ trụ Xayda sử dụng công nghệ mới nhất, "
                                                    + "có thể đưa ngươi đi bất kỳ đâu, chỉ cần trả tiền là được.",
                                                    "Đến\nTrái Đất", "Đến\nNamếc", "Siêu thị");
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 26) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 24, -1, -1);
                                                break;
                                            case 1:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 25, -1, -1);
                                                break;
                                            case 2:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 84, -1, -1);
                                                break;
                                        }
                                    }
                                }
                                if (this.mapId == 19) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                                break;
                                            case 1:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, 90);
                                                break;
                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_FIND_KUKU) {
                                        switch (select) {
                                            case 0:
                                                Boss boss = BossManager.gI().getBossById(BossFactory.KUKU);
                                                if (boss != null && !boss.isDie()) {
                                                    if (player.inventory.gold >= COST_FIND_BOSS) {
                                                        player.inventory.gold -= COST_FIND_BOSS;
                                                        ChangeMapService.gI().changeMap(player, boss.zone,
                                                                boss.location.x, boss.location.y);
                                                        Service.getInstance().sendMoney(player);
                                                    } else {
                                                        Service.getInstance().sendThongBao(player,
                                                                "Không đủ vàng, còn thiếu "
                                                                + Util.numberToMoney(
                                                                        COST_FIND_BOSS - player.inventory.gold)
                                                                + " vàng");
                                                    }
                                                }
                                                break;
                                            case 1:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                                break;
                                            case 2:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, 90);
                                                break;
                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_FIND_MAP_DAU_DINH) {
                                        switch (select) {
                                            case 0:
                                                Boss boss = BossManager.gI().getBossById(BossFactory.MAP_DAU_DINH);
                                                if (boss != null && !boss.isDie()) {
                                                    if (player.inventory.gold >= COST_FIND_BOSS) {
                                                        player.inventory.gold -= COST_FIND_BOSS;
                                                        ChangeMapService.gI().changeMap(player, boss.zone,
                                                                boss.location.x, boss.location.y);
                                                        Service.getInstance().sendMoney(player);
                                                    } else {
                                                        Service.getInstance().sendThongBao(player,
                                                                "Không đủ vàng, còn thiếu "
                                                                + Util.numberToMoney(
                                                                        COST_FIND_BOSS - player.inventory.gold)
                                                                + " vàng");
                                                    }
                                                }
                                                break;
                                            case 1:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                                break;
                                            case 2:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, 90);
                                                break;
                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_FIND_RAMBO) {
                                        switch (select) {
                                            case 0:
                                                Boss boss = BossManager.gI().getBossById(BossFactory.RAMBO);
                                                if (boss != null && !boss.isDie()) {
                                                    if (player.inventory.gold >= COST_FIND_BOSS) {
                                                        player.inventory.gold -= COST_FIND_BOSS;
                                                        ChangeMapService.gI().changeMap(player, boss.zone,
                                                                boss.location.x, boss.location.y);
                                                        Service.getInstance().sendMoney(player);
                                                    } else {
                                                        Service.getInstance().sendThongBao(player,
                                                                "Không đủ vàng, còn thiếu "
                                                                + Util.numberToMoney(
                                                                        COST_FIND_BOSS - player.inventory.gold)
                                                                + " vàng");
                                                    }
                                                }
                                                break;
                                            case 1:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 109, -1, 295);
                                                break;
                                            case 2:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 68, -1, 90);
                                                break;
                                        }
                                    }
                                }
                                if (this.mapId == 68) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 19, -1, 1100);
                                                break;
                                        }
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.SANTA:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                createOtherMenu(player, ConstNpc.BASE_MENU,
                                        "Xin chào, ta có một số vật phẩm đặt biệt cậu có muốn xem không?",
                                        "Cửa Hàng\nThỏi Vàng",
                                        "Cửa Hàng\n" + ConstItem.NAME_XU_SERVER,
                                        "Cửa Hàng\nTrang Bị",
                                        "Cửa Hàng\nSkill",
                                        "Cửa Hàng\nCải Trang");
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 5 || this.mapId == 13 || this.mapId == 20) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0: //thỏi vàng
                                                ShopService.gI().openShopSpecial(player, this, ConstNpc.SHOP_SANTA_3, 3, -1);
                                                break;
                                            case 1:
                                                ShopService.gI().openShopSpecial(player, this, ConstNpc.SHOP_SANTA_5, 5, -1);
                                                break;
                                            case 2: //Shop Đệ tử
                                                createOtherMenu(player, ConstNpc.MENU_SHOP_DE_TU,
                                                        "|2|Hãy Suy Nghĩ Thật Kĩ Rồi Hãy Mua...\n"
                                                        + "Cậu muốn xem loại nào?",
                                                        "Shop\nPhụ Kiện",
                                                        "Trang Bị\nĐệ Tử",
                                                        "Trang Bị\nVip");
                                                break;
                                            case 3: //Shop SKILL
                                                createOtherMenu(player, ConstNpc.MENU_SHOP_SKILL,
                                                        "|2|Hãy Suy Nghĩ Thật Kĩ Rồi Hãy Mua...\n"
                                                        + "Cậu muốn xem loại nào?",
                                                        "Shop\nSkill 9",
                                                        "Shop\nSkill 10",
                                                        "Shop\nSkill 11");
                                                break;
                                            case 4: //rương sưu tầm
                                                ShopService.gI().openShopSpecial(player, this, ConstNpc.SHOP_SANTA_2, 2, -1);
                                                break;
                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_SHOP_DE_TU) {
                                        switch (select) {
                                            case 0 -> //Thường
                                                ShopService.gI().openShopSpecial(player, this, ConstNpc.SHOP_SANTA_6, 6, -1);
                                            case 1 -> //Vip
                                                ShopService.gI().openShopSpecial(player, this, ConstNpc.SHOP_SANTA_7, 7, -1);
                                            case 2 -> //Vip 1
                                                ShopService.gI().openShopSpecial(player, this, ConstNpc.SHOP_SANTA_8, 8, -1);
                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_SHOP_SKILL) {
                                        switch (select) {
                                            case 0 -> //Thường
                                                ShopService.gI().openShopSpecial(player, this, ConstNpc.SHOP_SANTA_9, 9, -1);
                                            case 1 -> //Vip
                                                ShopService.gI().openShopSpecial(player, this, ConstNpc.SHOP_SANTA_10, 10, -1);
                                            case 2 -> //Vip 1
                                                ShopService.gI().openShopSpecial(player, this, ConstNpc.SHOP_SANTA_11, 11, -1);
                                        }
                                    }
                                }
                            }
                        }

                    };
                    break;
                case ConstNpc.GIUMA_DAU_BO:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 6 || this.mapId == 25 || this.mapId == 26) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                            "Gô Tên, Calich và Monaka đang gặp chuyện ở hành tinh Potaufeu \n Hãy đến đó ngay", "Đến \nPotaufeu");
                                } else if (this.mapId == 139) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                            "Người muốn trở về?", "Quay về", "Từ chối");
                                }//lãnh địa bang
                                else if (this.mapId == 153) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                            "Theo ta, ta sẽ đưa ngươi đến Khu vực Thánh địa\nNơi đây ngươi sẽ truy tìm mảnh bông tai cấp 2 và Hồn bông tai để mở chỉ số Bông tai Cấp 3."
                                            + "\n|7|Ngươi có muốn đến đó không?", "Đến\nThánh địa", "Từ chối");
                                } else if (this.mapId == 156) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                            "Người muốn trở về?", "Quay về", "Từ chối");
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 24 || this.mapId == 25 || this.mapId == 26) {
                                    if (player.iDMark.isBaseMenu()) {
                                        if (select == 0) {
                                            //đến potaufeu
                                            ChangeMapService.gI().goToPotaufeu(player);
                                        }
                                    }
                                } else if (this.mapId == 139) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            //về trạm vũ trụ
                                            case 0:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 24 + player.gender, -1, -1);
                                                break;
                                        }
                                    }
                                } else if (this.mapId == 153) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            //lãnh địa bang
                                            case 0:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 156, -1, -1);
                                                break;
                                        }
                                    }
                                } else if (this.mapId == 156) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            //về trạm vũ trụ
                                            case 0:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 21 + player.gender, -1, -1);
                                                break;
                                        }
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.URON:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player pl) {
                            if (canOpenNpc(pl)) {
                                this.openShopWithGender(pl, ConstNpc.SHOP_URON_0, 0);
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {

                            }
                        }
                    };
                    break;
                case ConstNpc.BA_HAT_MIT:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                switch (this.mapId) {
                                    case 5, 13, 20 ->
                                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                                "Ngươi tìm ta có việc gì?\n",
                                                "Chức Năng\nPha Lê",
                                                "Chức Năng\nĐột Phá",
                                                "Chức Năng\nNâng Cấp",
                                                "Chức Năng\nPhân Rã"
                                        );
                                    case 42, 43, 44, 84 ->
                                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                                "Ngươi tìm ta có việc gì?",
                                                "Cửa hàng\nBùa",
                                                "Nâng cấp\nVật phẩm",
                                                "Nhập\nNgọc Rồng",
                                                "Sách tuyệt kỹ"
                                        );
                                    case 121 ->
                                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                                "Ngươi tìm ta có việc gì?",
                                                "Về đảo\nKame"
                                        );
                                    default -> {
                                    }
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                switch (this.mapId) {
                                    case 5, 13, 20 -> {
                                        if (player.iDMark.isBaseMenu()) {
                                            switch (select) {
                                                case 0 ->
                                                    createOtherMenu(player, ConstNpc.CHUC_NANG_SAO_PHA_LE,
                                                            "|5|NGHIÊN CỨU SỨC MẠNH TINH THỂ\n"
                                                            + "|7|Trang bị của ngươi còn nhiều tiềm năng ẩn giấu.\n"
                                                            + "Ta có thể hỗ trợ ngươi cường hóa, hoặc tách bỏ tinh thể đã khảm.",
                                                            "Ép Sao\nTrang Bị",
                                                            "Pha Lê\nHóa Trang Bị");
                                                case 1 ->
                                                    this.createOtherMenu(player, ConstNpc.CHUC_NANG_DOT_PHA,
                                                            "|5|ĐỘT PHÁ GIỚI HẠN TRANG BỊ\n"
                                                            + "|7|Mỗi trang bị đều có giới hạn – nhưng không gì là không thể vượt qua! "
                                                            + "Ta sẽ giúp ngươi bộc phát toàn bộ sức mạnh tiềm ẩn.",
                                                            "Tinh Ẩn",
                                                            "Pháp Sư Hóa",
                                                            "Tẩy\nPháp Sư");
                                                case 2 ->
                                                    this.createOtherMenu(player, ConstNpc.CHUC_NANG_NANG_CAP_DB,
                                                            "|5|NÂNG CẤP ĐẶC BIỆT\n"
                                                            + "|7|Chiến binh thực thụ mới đủ tư cách chạm đến cấp độ sức mạnh này. "
                                                            + "Ngươi muốn nâng cấp điều gì?",
                                                            "Chân Mệnh",
                                                            "Cải Trang",
                                                            "Phụ Kiện",
                                                            "Bông Tai",
                                                            "Giáp Tập Luyện"
                                                    );
                                                case 3 ->
                                                    this.createOtherMenu(player, ConstNpc.MENU_PHAN_RA_DO_HUY_DIET,
                                                            "|5|PHÂN RÃ TRANG BỊ\n"
                                                            + "|7|Phân rã trang bị thành từng mảnh nhỏ.\n"
                                                            + "Ngươi muốn phân rã đồ nào?",
                                                            "Đồ\nHủy Diệt");
                                            }
                                        } else if (player.iDMark.getIndexMenu() == ConstNpc.CHUC_NANG_SAO_PHA_LE) {
                                            switch (select) {
                                                case 0 ->
                                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.EP_SAO_TRANG_BI);
                                                case 1 ->
                                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.PHA_LE_HOA_TRANG_BI);
                                            }
                                        } else if (player.iDMark.getIndexMenu() == ConstNpc.CHUC_NANG_DOT_PHA) {
                                            switch (select) {
                                                case 0 ->
                                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.AN_TRANG_BI);
                                                case 1 ->
                                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.PHAP_SU_HOA);
                                                case 2 ->
                                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.TAY_PHAP_SU);
                                            }
                                        } else if (player.iDMark.getIndexMenu() == ConstNpc.CHUC_NANG_NANG_CAP_DB) {
                                            switch (select) {
                                                case 0 ->
                                                    this.createOtherMenu(player, ConstNpc.MENU_CHAN_MENH,
                                                            "|5|CHÂN MỆNH - SỨ MỆNH TỐI CAO\n\n"
                                                            + "|7|Chỉ những chiến binh thực thụ mới có thể khai mở Chân Mệnh.\n"
                                                            + "|1|Để nhận Chân Mệnh cấp 1, cần 200 Đá Ánh Sáng.\n"
                                                            + "|3|Lưu ý: Mỗi người chỉ sở hữu **1 Chân Mệnh duy nhất** trong hành trang.\n\n"
                                                            + "|7|Nếu ngươi đã có, ta sẽ giúp nâng cấp nó lên cấp cao hơn — "
                                                            + "với chỉ số vượt bậc, khẳng định vị thế anh hùng!",
                                                            "Nhận Chân Mệnh",
                                                            "Nâng cấp Chân Mệnh");
                                                case 1 ->
                                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_CAI_TRANG);
                                                case 2 ->
                                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_PHU_KIEN);
                                                case 3 ->
                                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_BONG_TAI_VIP);
                                                case 4 ->
                                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_GIAP_TAP_LUYEN);
                                            }
                                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_PHAN_RA_DO_HUY_DIET) {
                                            switch (select) {
                                                case 0 ->
                                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.PHAN_RA_DO_HUY_DIET);
                                            }
                                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_CHAN_MENH) {
                                            switch (select) {
                                                case 0 -> {
                                                    for (int i = 0; i < 9; i++) {
                                                        Item findItemBag = InventoryService.gI().findItemBagByTemp(player, 1300 + i);
                                                        Item findItemBody = InventoryService.gI().findItemBodyByTemp(player, 1300 + i);
                                                        if (findItemBag != null || findItemBody != null) {
                                                            Service.getInstance().sendThongBao(player, "|7|Ngươi đã có Chân mệnh rồi mà");
                                                            return;
                                                        }
                                                    }
                                                    Item thoivang = null;
                                                    try {
                                                        thoivang = InventoryService.gI().findItemBagByTemp(player, 1400);
                                                    } catch (Exception e) {
                                                    }
                                                    if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                                                        if (thoivang == null || thoivang.quantity < 100) {
                                                            Service.getInstance().sendThongBao(player, "Không đủ Đá Ánh Sáng");
                                                        } else {
                                                            InventoryService.gI().subQuantityItemsBag(player, thoivang, 200);
                                                            Item chanmenh = ItemService.gI().createNewItem((short) 1300);
                                                            chanmenh.itemOptions.add(new ItemOption(50, 5));
                                                            chanmenh.itemOptions.add(new ItemOption(77, 7));
                                                            chanmenh.itemOptions.add(new ItemOption(103, 7));
                                                            chanmenh.itemOptions.add(new ItemOption(30, 1));
                                                            InventoryService.gI().addItemBag(player, chanmenh, 0);
                                                            Service.getInstance().sendMoney(player);
                                                            InventoryService.gI().sendItemBags(player);
                                                            this.npcChat(player, "|1|Bạn nhận được Chân mệnh Cấp 1");
                                                        }
                                                    } else {
                                                        this.npcChat(player, "Hành trang không đủ chổ trống");
                                                    }
                                                }
                                                case 1 ->
                                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_CHAN_MENH);
                                            }
                                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                                            switch (player.combineNew.typeCombine) {
                                                case CombineServiceNew.EP_SAO_TRANG_BI:
                                                case CombineServiceNew.AN_TRANG_BI:
                                                case CombineServiceNew.PHA_LE_HOA_TRANG_BI:
                                                case CombineServiceNew.PHAP_SU_HOA:
                                                case CombineServiceNew.TAY_PHAP_SU:
                                                case CombineServiceNew.NANG_CAP_CHAN_MENH:
                                                case CombineServiceNew.PHAN_RA_DO_HUY_DIET:
                                                case CombineServiceNew.NANG_CAP_THAN_LINH:
                                                case CombineServiceNew.NANG_CAP_SKH_VIP:
                                                case CombineServiceNew.GIA_HAN_VAT_PHAM:
                                                case CombineServiceNew.NANG_CAP_CAI_TRANG:
                                                case CombineServiceNew.NANG_CAP_PHU_KIEN:
                                                case CombineServiceNew.NANG_CAP_BONG_TAI_VIP:
                                                case CombineServiceNew.NANG_CAP_GIAP_TAP_LUYEN:
                                                    CombineServiceNew.gI().startCombine(player);
                                                    break;
                                            }
                                        }
                                    }
                                    case 42, 43, 44, 84 -> {
                                        if (player.iDMark.isBaseMenu()) {
                                            switch (select) {
                                                case 0: // shop bùa
                                                    createOtherMenu(player, ConstNpc.MENU_OPTION_SHOP_BUA,
                                                            "Bùa của ta rất lợi hại, nhìn ngươi yếu đuối thế này, chắc muốn mua bùa để "
                                                            + "mạnh mẽ à, mua không ta bán cho, xài rồi lại thích cho mà xem.",
                                                            "Bùa\n1 giờ",
                                                            "Bùa\n8 giờ",
                                                            "Bùa\n1 tháng",
                                                            "Bùa\nĐệ tử Mabư\n 1 giờ",
                                                            "Xóa bùa",
                                                            "Đóng");
                                                    break;
                                                case 1:
                                                    createOtherMenu(player, ConstNpc.NANG_CAP_TRANG_BI, "Ta có thể giúp gì cho ngươi?",
                                                            "Trang bị",
                                                            "Bông tai");
                                                    break;
                                                case 2:
                                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NHAP_NGOC_RONG);
                                                    break;
                                                case 3: // Sách tuyệt kỹ
                                                    createOtherMenu(player, ConstNpc.SACH_TUYET_KY, "Ta có thể giúp gì cho ngươi ?",
                                                            "Đóng thành\nSách cũ",
                                                            "Đổi Sách\nTuyệt kỹ",
                                                            "Giám định\nSách",
                                                            "Tẩy\nSách",
                                                            "Nâng cấp\nSách\nTuyệt kỹ",
                                                            "Hồi phục\nSách",
                                                            "Phân rã\nSách");
                                                    break;
                                                case 4: //nâng cấp bông tai
                                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_BONG_TAI_VO_CUC);
                                                    break;

                                            }
                                        } else if (player.iDMark.getIndexMenu() == ConstNpc.NANG_CAP_TRANG_BI) {
                                            switch (select) {
                                                case 0 -> //trang bị
                                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_VAT_PHAM);
                                                case 1 -> //bông tai
                                                    createOtherMenu(player, ConstNpc.NANG_CAP_BONG_TAI,
                                                            "Ngươi muốn nâng bông tai à",
                                                            "Nâng cấp\nBông tai",
                                                            "Mở Chỉ Số\nBông Tai",
                                                            "Đóng");
                                            }
                                        } else if (player.iDMark.getIndexMenu() == ConstNpc.NANG_CAP_BONG_TAI) {
                                            switch (select) {
                                                case 0 -> // nâng cấp bông tai
                                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_BONG_TAI);
                                                case 1 -> // mở chỉ số bông tai
                                                    CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.MO_CHI_SO_BONG_TAI);
                                            }
                                        } else if (player.iDMark.getIndexMenu() == ConstNpc.SACH_TUYET_KY) {
                                            switch (select) {
                                                case 0:
                                                    Item trangSachCu = InventoryService.gI().findItemBagByTemp(player, 1516);

                                                    Item biaSach = InventoryService.gI().findItemBagByTemp(player, 1506);
                                                    if ((trangSachCu != null && trangSachCu.quantity >= 9999) && (biaSach != null && biaSach.quantity >= 1)) {
                                                        createOtherMenu(player, ConstNpc.DONG_THANH_SACH_CU,
                                                                "|2|Chế tạo Cuốn sách cũ\n"
                                                                + "|1|Trang sách cũ " + trangSachCu.quantity + "/9999\n"
                                                                + "Bìa sách " + biaSach.quantity + "/1\n"
                                                                + "Tỉ lệ thành công: 60%\n"
                                                                + "Thất bại mất 99 trang sách và 1 bìa sách", "Đồng ý", "Từ chối");
                                                        break;
                                                    } else {
                                                        String NpcSay = "|2|Chế tạo Cuốn sách cũ\n";
                                                        if (trangSachCu == null) {
                                                            NpcSay += "|7|Trang sách cũ " + "0/9999\n";
                                                        } else {
                                                            NpcSay += "|1|Trang sách cũ " + trangSachCu.quantity + "/9999\n";
                                                        }
                                                        if (biaSach == null) {
                                                            NpcSay += "|7|Bìa sách " + "0/1\n";
                                                        } else {
                                                            NpcSay += "|1|Bìa sách " + biaSach.quantity + "/1\n";
                                                        }

                                                        NpcSay += "|7|Tỉ lệ thành công: 60%\n";
                                                        NpcSay += "|7|Thất bại mất 99 trang sách và 1 bìa sách";
                                                        createOtherMenu(player, ConstNpc.DONG_THANH_SACH_CU_2,
                                                                NpcSay, "Từ chối");
                                                        break;
                                                    }
                                                case 1:
                                                    Item cuonSachCu = InventoryService.gI().findItemBagByTemp(player, 1509);
                                                    Item kimBam = InventoryService.gI().findItemBagByTemp(player, 1507);

                                                    if ((cuonSachCu != null && cuonSachCu.quantity >= 10) && (kimBam != null && kimBam.quantity >= 1)) {
                                                        createOtherMenu(player, ConstNpc.DOI_SACH_TUYET_KY,
                                                                "|2|Đổi sách tuyệt kỹ 1\n"
                                                                + "|1|Cuốn sách cũ " + cuonSachCu.quantity + "/10\n"
                                                                + "Kìm bấm giấy " + kimBam.quantity + "/1\n"
                                                                + "Tỉ lệ thành công: 60%\n", "Đồng ý", "Từ chối");
                                                        break;
                                                    } else {
                                                        String NpcSay = "|2|Đổi sách Tuyệt kỹ 1\n";
                                                        if (cuonSachCu == null) {
                                                            NpcSay += "|7|Cuốn sách cũ " + "0/10\n";
                                                        } else {
                                                            NpcSay += "|1|Cuốn sách cũ " + cuonSachCu.quantity + "/10\n";
                                                        }
                                                        if (kimBam == null) {
                                                            NpcSay += "|7|Kìm bấm giấy " + "0/1\n";
                                                        } else {
                                                            NpcSay += "|1|Kìm bấm giấy " + kimBam.quantity + "/1\n";
                                                        }
                                                        NpcSay += "|7|Tỉ lệ thành công: 60%\n";
                                                        createOtherMenu(player, ConstNpc.DOI_SACH_TUYET_KY_2,
                                                                NpcSay, "Từ chối");
                                                    }
                                                    break;
                                                case 2:// giám định sách
                                                    CombineServiceNew.gI().openTabCombine(player,
                                                            CombineServiceNew.GIAM_DINH_SACH);
                                                    break;
                                                case 3:// tẩy sách
                                                    CombineServiceNew.gI().openTabCombine(player,
                                                            CombineServiceNew.TAY_SACH);
                                                    break;
                                                case 4:// nâng cấp sách
                                                    CombineServiceNew.gI().openTabCombine(player,
                                                            CombineServiceNew.NANG_CAP_SACH_TUYET_KY);
                                                    break;
                                                case 5:// phục hồi sách
                                                    CombineServiceNew.gI().openTabCombine(player,
                                                            CombineServiceNew.PHUC_HOI_SACH);
                                                    break;
                                                case 6:// phân rã sách
                                                    CombineServiceNew.gI().openTabCombine(player,
                                                            CombineServiceNew.PHAN_RA_SACH);
                                                    break;
                                            }
                                        } else if (player.iDMark.getIndexMenu() == ConstNpc.DOI_SACH_TUYET_KY) {
                                            switch (select) {
                                                case 0:
                                                    Item cuonSachCu = InventoryService.gI().findItemBagByTemp(player, 1509);
                                                    Item kimBam = InventoryService.gI().findItemBagByTemp(player, 1507);

                                                    short baseValue = 1512;
                                                    short genderModifier = (player.gender == 0) ? -2 : ((player.gender == 2) ? 2 : (short) 0);

                                                    Item sachTuyetKy = ItemService.gI().createNewItem((short) (baseValue + genderModifier));

                                                    if (Util.isTrue(60, 100)) {

                                                        sachTuyetKy.itemOptions.add(new ItemOption(241, 0));
                                                        sachTuyetKy.itemOptions.add(new ItemOption(21, 40));
                                                        sachTuyetKy.itemOptions.add(new ItemOption(30, 0));
                                                        sachTuyetKy.itemOptions.add(new ItemOption(87, 1));
                                                        sachTuyetKy.itemOptions.add(new ItemOption(242, 20));
                                                        sachTuyetKy.itemOptions.add(new ItemOption(243, 1000));
                                                        try { // send effect susscess
                                                            Message msg = new Message(-81);
                                                            msg.writer().writeByte(0);
                                                            msg.writer().writeUTF("test");
                                                            msg.writer().writeUTF("test");
                                                            msg.writer().writeShort(tempId);
                                                            player.sendMessage(msg);
                                                            msg.cleanup();
                                                            msg = new Message(-81);
                                                            msg.writer().writeByte(1);
                                                            msg.writer().writeByte(2);
                                                            msg.writer().writeByte(InventoryService.gI().getIndexBag(player, kimBam));
                                                            msg.writer().writeByte(InventoryService.gI().getIndexBag(player, cuonSachCu));
                                                            player.sendMessage(msg);
                                                            msg.cleanup();
                                                            msg = new Message(-81);
                                                            msg.writer().writeByte(7);
                                                            msg.writer().writeShort(sachTuyetKy.template.iconID);
                                                            msg.writer().writeShort(-1);
                                                            msg.writer().writeShort(-1);
                                                            msg.writer().writeShort(-1);
                                                            player.sendMessage(msg);
                                                            msg.cleanup();
                                                        } catch (Exception e) {
                                                            System.out.println("lỗi 4");
                                                        }
                                                        InventoryService.gI().addItemList(player.inventory.itemsBag, sachTuyetKy, 1);
                                                        InventoryService.gI().subQuantityItemsBag(player, cuonSachCu, 10);
                                                        InventoryService.gI().subQuantityItemsBag(player, kimBam, 1);
                                                        InventoryService.gI().sendItemBags(player);
                                                        return;
                                                    } else {
                                                        try { // send effect faile
                                                            Message msg = new Message(-81);
                                                            msg.writer().writeByte(0);
                                                            msg.writer().writeUTF("test");
                                                            msg.writer().writeUTF("test");
                                                            msg.writer().writeShort(tempId);
                                                            player.sendMessage(msg);
                                                            msg.cleanup();
                                                            msg = new Message(-81);
                                                            msg.writer().writeByte(1);
                                                            msg.writer().writeByte(2);
                                                            msg.writer().writeByte(InventoryService.gI().getIndexBag(player, kimBam));
                                                            msg.writer().writeByte(InventoryService.gI().getIndexBag(player, cuonSachCu));
                                                            player.sendMessage(msg);
                                                            msg.cleanup();
                                                            msg = new Message(-81);
                                                            msg.writer().writeByte(8);
                                                            msg.writer().writeShort(-1);
                                                            msg.writer().writeShort(-1);
                                                            msg.writer().writeShort(-1);
                                                            player.sendMessage(msg);
                                                            msg.cleanup();
                                                        } catch (Exception e) {
                                                            System.out.println("lỗi 3");
                                                        }
                                                        InventoryService.gI().subQuantityItemsBag(player, cuonSachCu, 5);
                                                        InventoryService.gI().subQuantityItemsBag(player, kimBam, 1);
                                                        InventoryService.gI().sendItemBags(player);
                                                    }
                                                    return;
                                                case 1:
                                                    break;
                                            }
                                        } else if (player.iDMark.getIndexMenu() == ConstNpc.DONG_THANH_SACH_CU) {
                                            switch (select) {
                                                case 0:

                                                    Item trangSachCu = InventoryService.gI().findItemBagByTemp(player, 1516);
                                                    Item biaSach = InventoryService.gI().findItemBagByTemp(player, 1506);
                                                    Item cuonSachCu = ItemService.gI().createNewItem((short) 1509);
                                                    if (Util.isTrue(60, 100)) {

                                                        try { // send effect susscess
                                                            Message msg = new Message(-81);
                                                            msg.writer().writeByte(0);
                                                            msg.writer().writeUTF("test");
                                                            msg.writer().writeUTF("test");
                                                            msg.writer().writeShort(tempId);
                                                            player.sendMessage(msg);
                                                            msg.cleanup();

                                                            msg = new Message(-81);
                                                            msg.writer().writeByte(1);
                                                            msg.writer().writeByte(2);
                                                            msg.writer().writeByte(InventoryService.gI().getIndexBag(player, trangSachCu));
                                                            msg.writer().writeByte(InventoryService.gI().getIndexBag(player, biaSach));
                                                            player.sendMessage(msg);
                                                            msg.cleanup();

                                                            msg = new Message(-81);
                                                            msg.writer().writeByte(7);
                                                            msg.writer().writeShort(cuonSachCu.template.iconID);
                                                            msg.writer().writeShort(-1);
                                                            msg.writer().writeShort(-1);
                                                            msg.writer().writeShort(-1);
                                                            player.sendMessage(msg);
                                                            msg.cleanup();

                                                        } catch (Exception e) {
                                                            System.out.println("lỗi 1");
                                                        }

                                                        InventoryService.gI().addItemList(player.inventory.itemsBag, cuonSachCu, 99);
                                                        InventoryService.gI().subQuantityItemsBag(player, trangSachCu, 9999);
                                                        InventoryService.gI().subQuantityItemsBag(player, biaSach, 1);
                                                        InventoryService.gI().sendItemBags(player);
                                                        return;
                                                    } else {
                                                        try { // send effect faile
                                                            Message msg = new Message(-81);
                                                            msg.writer().writeByte(0);
                                                            msg.writer().writeUTF("test");
                                                            msg.writer().writeUTF("test");
                                                            msg.writer().writeShort(tempId);
                                                            player.sendMessage(msg);
                                                            msg.cleanup();
                                                            msg = new Message(-81);
                                                            msg.writer().writeByte(1);
                                                            msg.writer().writeByte(2);
                                                            msg.writer().writeByte(InventoryService.gI().getIndexBag(player, biaSach));
                                                            msg.writer().writeByte(InventoryService.gI().getIndexBag(player, trangSachCu));
                                                            player.sendMessage(msg);
                                                            msg.cleanup();
                                                            msg = new Message(-81);
                                                            msg.writer().writeByte(8);
                                                            msg.writer().writeShort(-1);
                                                            msg.writer().writeShort(-1);
                                                            msg.writer().writeShort(-1);
                                                            player.sendMessage(msg);
                                                            msg.cleanup();
                                                        } catch (Exception e) {
                                                            System.out.println("lỗi 2");
                                                        }
                                                        InventoryService.gI().subQuantityItemsBag(player, trangSachCu, 99);
                                                        InventoryService.gI().subQuantityItemsBag(player, biaSach, 1);
                                                        InventoryService.gI().sendItemBags(player);
                                                    }
                                                    return;
                                                case 1:
                                                    break;
                                            }
                                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_SHOP_BUA) {
                                            switch (select) {
                                                case 0:
                                                    ShopService.gI().openShopBua(player, ConstNpc.SHOP_BA_HAT_MIT_0, 0);
                                                    break;
                                                case 1:
                                                    ShopService.gI().openShopBua(player, ConstNpc.SHOP_BA_HAT_MIT_1, 1);
                                                    break;
                                                case 2:
                                                    ShopService.gI().openShopBua(player, ConstNpc.SHOP_BA_HAT_MIT_2, 2);
                                                    break;
                                                case 3:
                                                    ShopService.gI().openShopBua(player, ConstNpc.SHOP_BA_HAT_MIT_3, 3);
                                                    break;
                                                case 4: // Xóa bùa
                                                    createOtherMenu(player, ConstNpc.MENU_OPTION_XOA_BUA,
                                                            "Bùa chú tuy mạnh mẽ, nhưng nếu ngươi muốn giải trừ để nhẹ người, ta sẽ giúp một tay!",
                                                            "Xóa Bùa\nTrí Tuệ",
                                                            "Xóa Bùa\nMạnh Mẽ",
                                                            "Xóa Bùa\nDa Trâu",
                                                            "Xóa Bùa\nOai Hùng",
                                                            "Xóa Bùa\nBất Tử",
                                                            "Xóa Bùa\nDẻo Dai",
                                                            "Xóa Bùa\nThu Hút",
                                                            "Xóa Bùa\nĐệ Tử",
                                                            "Xóa Bùa\nTrí Tuệ x3",
                                                            "Xóa Bùa\nTrí Tuệ x4",
                                                            "Xóa Bùa\nĐệ Tử [Mabu, Berus, Zeno]",
                                                            "Xóa tất cả\nbùa",
                                                            "Đóng");
                                                    break;
                                            }
                                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_XOA_BUA) {
                                            player.charms.removeCharmBySelect(select);
                                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                                            switch (player.combineNew.typeCombine) {
                                                case CombineServiceNew.NANG_CAP_VAT_PHAM:
                                                case CombineServiceNew.NANG_CAP_BONG_TAI:
                                                case CombineServiceNew.LAM_PHEP_NHAP_DA:
                                                case CombineServiceNew.NHAP_NGOC_RONG:
                                                case CombineServiceNew.PHAN_RA_DO_THAN_LINH:
                                                case CombineServiceNew.NANG_CAP_SKH_VIP:
                                                case CombineServiceNew.MO_CHI_SO_BONG_TAI:
                                                case CombineServiceNew.DOT_PHA_BONG_TAI:
                                                case CombineServiceNew.NANG_CAP_BONG_TAI_VO_CUC:
                                                //START _ SÁCH TUYỆT KỸ//
                                                case CombineServiceNew.GIAM_DINH_SACH:
                                                case CombineServiceNew.TAY_SACH:
                                                case CombineServiceNew.NANG_CAP_SACH_TUYET_KY:
                                                case CombineServiceNew.PHUC_HOI_SACH:
                                                case CombineServiceNew.PHAN_RA_SACH:
                                                    //END _ SÁCH TUYỆT KỸ//
                                                    if (select == 0) {
                                                        CombineServiceNew.gI().startCombine(player);
                                                    }
                                                    break;
                                            }
                                        }
                                    }
                                    case 112 -> {
                                        if (player.iDMark.isBaseMenu()) {
                                            if (select == 0) {
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 5, -1, 1156);
                                            }
                                        }
                                    }
                                    default -> {
                                    }
                                }
                            }
                        }
                    };
                    break;

                case ConstNpc.RUONG_DO:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                        "Vui lòng lựa chọn",
                                        "Rương Đồ",
                                        "Hòm Thư");
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (player.iDMark.isBaseMenu()) {
                                    switch (select) {
                                        case 0: //Rương đồ
                                            InventoryService.gI().sendItemBox(player);
                                            InventoryService.gI().openBox(player);
                                            break;
                                        case 1: //Hòm Thư
                                            this.createOtherMenu(player, 1,
                                                    "Ta có thể giúp gì?",
                                                    "Hòm Thư\n(" + (player.inventory.itemsMailBox.size()
                                                    - InventoryService.gI().getCountEmptyListItem(player.inventory.itemsMailBox))
                                                    + " món)",
                                                    "Xóa Hết\nHòm Thư", "Đóng");
                                            break;
                                    }
                                } else if (player.iDMark.getIndexMenu() == 1) {
                                    switch (select) {
                                        case 0:
                                            ShopService.gI().openBoxItemBoxmail(player);
                                            break;
                                        case 1:
                                            NpcService.gI().createMenuConMeo(player,
                                                    ConstNpc.CONFIRM_REMOVE_ALL_ITEM_MAIL_BOX, this.avartar,
                                                    "|3|Bạn chắc muốn xóa hết vật phẩm trong hòm thư?\n"
                                                    + "|7|Sau khi xóa sẽ không thể khôi phục!",
                                                    "Đồng ý", "Hủy bỏ");
                                            break;
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.DAU_THAN:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                player.magicTree.openMenuTree();
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                TaskService.gI().checkDoneTaskConfirmMenuNpc(player, this, (byte) select);
                                switch (player.iDMark.getIndexMenu()) {
                                    case ConstNpc.MAGIC_TREE_NON_UPGRADE_LEFT_PEA:
                                        if (select == 0) {
                                            player.magicTree.harvestPea();
                                        } else if (select == 1) {
                                            if (player.magicTree.level == 10) {
                                                player.magicTree.fastRespawnPea();
                                            } else {
                                                player.magicTree.showConfirmUpgradeMagicTree();
                                            }
                                        } else if (select == 2) {
                                            player.magicTree.fastRespawnPea();
                                        }
                                        break;
                                    case ConstNpc.MAGIC_TREE_NON_UPGRADE_FULL_PEA:
                                        if (select == 0) {
                                            player.magicTree.harvestPea();
                                        } else if (select == 1) {
                                            player.magicTree.showConfirmUpgradeMagicTree();
                                        }
                                        break;
                                    case ConstNpc.MAGIC_TREE_CONFIRM_UPGRADE:
                                        if (select == 0) {
                                            player.magicTree.upgradeMagicTree();
                                        }
                                        break;
                                    case ConstNpc.MAGIC_TREE_UPGRADE:
                                        if (select == 0) {
                                            player.magicTree.fastUpgradeMagicTree();
                                        } else if (select == 1) {
                                            player.magicTree.showConfirmUnuppgradeMagicTree();
                                        }
                                        break;
                                    case ConstNpc.MAGIC_TREE_CONFIRM_UNUPGRADE:
                                        if (select == 0) {
                                            player.magicTree.unupgradeMagicTree();
                                        }
                                        break;
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.CALICK:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        private final byte COUNT_CHANGE = 50;
                        private int count;

                        private void changeMap() {
                            if (this.mapId != 102) {
                                count++;
                                if (this.count >= COUNT_CHANGE) {
                                    count = 0;
                                    this.map.npcs.remove(this);
                                    Map map = MapService.gI().getMapForCalich();
                                    this.mapId = map.mapId;
                                    this.cx = Util.nextInt(100, map.mapWidth - 100);
                                    this.cy = map.yPhysicInTop(this.cx, 0);
                                    this.map = map;
                                    this.map.npcs.add(this);
                                }
                            }
                        }

                        @Override
                        public void openBaseMenu(Player player) {
                            player.iDMark.setIndexMenu(ConstNpc.BASE_MENU);
                            if (TaskService.gI().getIdTask(player) < ConstTask.TASK_20_0) {
                                Service.getInstance().hideWaitDialog(player);
                                Service.getInstance().sendThongBao(player, "Không thể thực hiện");
                                return;
                            }
                            if (this.mapId != player.zone.map.mapId) {
                                Service.getInstance().sendThongBao(player, "Calích đã rời khỏi map!");
                                Service.getInstance().hideWaitDialog(player);
                                return;
                            }

                            if (this.mapId == 102) {
                                this.createOtherMenu(player, ConstNpc.BASE_MENU, "Chào chú, cháu có thể giúp gì?",
                                        "Kể\nChuyện", "Quay về\nQuá khứ");
                            } else {
                                this.createOtherMenu(player, ConstNpc.BASE_MENU, "Chào chú, cháu có thể giúp gì?",
                                        "Kể\nChuyện", "Đi đến\nTương lai", "Từ chối");
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (this.mapId == 102) {
                                if (player.iDMark.isBaseMenu()) {
                                    if (select == 0) {
                                        // kể chuyện
                                        NpcService.gI().createTutorial(player, this.avartar, ConstNpc.CALICK_KE_CHUYEN);
                                    } else if (select == 1) {
                                        // về quá khứ
                                        ChangeMapService.gI().goToQuaKhu(player);
                                    }
                                }
                            } else if (player.iDMark.isBaseMenu()) {
                                if (select == 0) {
                                    // kể chuyện
                                    NpcService.gI().createTutorial(player, this.avartar, ConstNpc.CALICK_KE_CHUYEN);
                                } else if (select == 1) {
                                    // đến tương lai
                                    // changeMap();
                                    if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_20_0) {
                                        ChangeMapService.gI().goToTuongLai(player);
                                    }
                                } else {
                                    Service.getInstance().sendThongBao(player, "Không thể thực hiện");
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.JACO:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 0) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                            "|7| KHU VỰC BOSS NHÂN BẢN"
                                            + "\n\n|6|Gô Tên, Calich và Monaka đang gặp chuyện ở hành tinh Potaufeu"
                                            + "\nĐánh bại những kẻ giả mạo ngươi sẽ nhận được những phần thưởng hấp dẫn"
                                            + "\n|3|Hạ Boss Nhân Bản sẽ nhận được Item Siêu cấp"
                                            + "\n|2|Hãy đến đó ngay",
                                            "Đến \nPotaufeu");
                                } else {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                            "Người muốn trở về?", "Quay về", "Từ chối");
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 0) {
                                    if (player.iDMark.isBaseMenu()) {
                                        if (select == 0) {
                                            ChangeMapService.gI().goToPotaufeu(player);
                                        }
                                    }
                                } else {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, -1, 250);
                                                break;
                                        }
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.THAN_MEO_KARIN:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (mapId == ConstMap.THAP_KARIN) {
                                    if (player.zone instanceof ZSnakeRoad) {
                                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                                "Hãy cầm lấy hai hạt đậu cuối cùng ở đây\nCố giữ mình nhé "
                                                + player.name,
                                                "Cảm ơn\nsư phụ");
                                    } else if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                                "Chào con, con muốn ta giúp gì nào?", getMenuSuKien(EVENT_SEVER));
                                    }
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (mapId == ConstMap.THAP_KARIN) {
                                    if (player.iDMark.isBaseMenu()) {
                                        if (player.zone instanceof ZSnakeRoad) {
                                            switch (select) {
                                                case 0:
                                                    player.setInteractWithKarin(true);
                                                    Service.getInstance().sendThongBao(player,
                                                            "Hãy mau bay xuống chân tháp Karin");
                                                    break;
                                            }
                                        } else {
                                            switch (select) {
                                                case 0:
                                                    switch (EVENT_SEVER) {
                                                        case 2:
                                                            Attribute at = ServerManager.gI().getAttributeManager()
                                                                    .find(ConstAttribute.TNSM);
                                                            String text = "Sự kiện 20/11 chính thức tại Ngọc Rồng "
                                                                    + Manager.SERVER_NAME + "\n "
                                                                    + "Số điểm hiện tại của bạn là : "
                                                                    + player.event.getEventPoint()
                                                                    + "\nTổng số hoa đã tặng trên toàn máy chủ "
                                                                    + EVENT_COUNT_THAN_MEO % 999 + "/999";
                                                            this.createOtherMenu(player, ConstNpc.MENU_OPEN_SUKIEN,
                                                                    at != null && !at.isExpired() ? text
                                                                    + "\nToàn bộ máy chủ được tăng 20% TNSM cho đệ tử khi đánh quái,thời gian còn lại "
                                                                    + at.getTime() / 60 + " phút."
                                                                    : text + "\nKhi tặng đủ 999 bông hoa toàn bộ máy chủ được tăng tăng 20% TNSM cho đệ tử trong 60 phút\n",
                                                                    "Tặng 1\n Bông hoa", "Tặng\n10 Bông",
                                                                    "Tặng\n99 Bông", "Đổi\nHộp quà");
                                                            break;
                                                    }
                                            }
                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPEN_SUKIEN) {
                                        openMenuSuKien(player, this, tempId, select);
                                    }
                                }
                            }
                        }
                    };
                    break;

                case ConstNpc.THUONG_DE:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {

                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 45) {
                                    if (!player.istrain) {
                                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Con đã mạnh hơn ta"
                                                + ", ta sẽ chỉ đường cho con đến Kaio để gặp thần Vũ Trụ Phương Bắc"
                                                + "\nNgài là thần cai quản vũ trụ này, hãy theo ngài ấy học võ công",
                                                "Đóng",
                                                "Đến Kaio",
                                                "Quay số\nmay mắn");
//                                                 getMenuSuKien(EVENT_SEVER));
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Con đã mạnh hơn ta"
                                                + ", ta sẽ chỉ đường cho con đến Kaio để gặp thần Vũ Trụ Phương Bắc"
                                                + "\nNgài là thần cai quản vũ trụ này, hãy theo ngài ấy học võ công",
                                                "Hủy đăng ký tập tự động", "Đến Kaio");
                                    }
                                } else if (player.zone instanceof ZSnakeRoad) {
                                    if (mapId == ConstMap.CON_DUONG_RAN_DOC) {
                                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Hãy lắm lấy tay ta mau",
                                                "Về thần điện");
                                    }
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 45) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
//                                            case 0:
//                                                if (!player.istrain) {
//                                                    this.createOtherMenu(player, ConstNpc.MENU_TRAIN_OFFLINE,
//                                                            "Đăng ký để mỗi khi Offline quá 30 phút"
//                                                            + ", con sẽ được tự động luyện tập với tốc độ " + player.getexp() + " sức mạnh mỗi phút",
//                                                            "Hướng dẫn thêm", "Đồng ý 1 ngọc mỗi lần", "Không đồng ý");
//                                                } else {
//                                                    player.istrain = false;
//                                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU, "Con đã hủy thành công đăng ký tập tự động", "Đóng");
//                                                }
//                                                break;
                                            case 0:
                                                break;
                                            case 1:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 48, -1, 354);
                                                break;
                                            case 2:
                                                this.createOtherMenu(player, ConstNpc.MENU_CHOOSE_LUCKY_ROUND,
                                                        "Con muốn làm gì nào?",
                                                        "Quay bằng\nThỏi vàng",
                                                        "Rương phụ\n("
                                                        + (player.inventory.itemsBoxCrackBall.size()
                                                        - InventoryService.gI().getCountEmptyListItem(
                                                                player.inventory.itemsBoxCrackBall))
                                                        + " món)",
                                                        "Xóa hết\ntrong rương", "Đóng");
                                                break;
                                            case 3:
                                                switch (EVENT_SEVER) {
                                                    case 2:
                                                        Attribute at = ServerManager.gI().getAttributeManager()
                                                                .find(ConstAttribute.KI);
                                                        String text = "Sự kiện 20/11 chính thức tại Ngọc Rồng "
                                                                + Manager.SERVER_NAME + "\n + "
                                                                + "Số điểm hiện tại của bạn là : "
                                                                + player.event.getEventPoint()
                                                                + "\nTổng số hoa đã tặng trên toàn máy chủ "
                                                                + EVENT_COUNT_THUONG_DE % 999 + "/999";
                                                        this.createOtherMenu(player, ConstNpc.MENU_OPEN_SUKIEN,
                                                                at != null && !at.isExpired() ? text
                                                                + "\nToàn bộ máy chủ được tăng 20% KI,thời gian còn lại "
                                                                + at.getTime() / 60 + " phút."
                                                                : text + "\nKhi tặng đủ 999 bông hoa toàn bộ máy chủ được tăng 20% Ki trong 60 phút\n",
                                                                "Tặng 1\n Bông hoa", "Tặng\n10 Bông", "Tặng\n99 Bông",
                                                                "Đổi\nHộp quà");
                                                        break;
                                                }
                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_TRAIN_OFFLINE) {
                                        switch (select) {
                                            case 0:
                                                Service.getInstance().sendPopUpMultiLine(player, tempId, this.avartar, ConstNpc.INFOR_TRAIN_OFFLINE);
                                                break;
                                            case 1:
//                                                player.istrain = true;
                                                NpcService.gI().createTutorial(player, this.avartar, "Tính năng chưa phát triển xong");
                                                break;
                                            case 3:
                                                break;
                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_CHOOSE_LUCKY_ROUND) {
                                        switch (select) {
                                            case 0:
                                                LuckyRoundService.gI().openCrackBallUI(player, LuckyRoundService.USING_TV);
                                                break;
                                            case 1:
                                                ShopService.gI().openBoxItemLuckyRound(player);
                                                break;
                                            case 2:
                                                NpcService.gI().createMenuConMeo(player,
                                                        ConstNpc.CONFIRM_REMOVE_ALL_ITEM_LUCKY_ROUND, this.avartar,
                                                        "Con có chắc muốn xóa hết vật phẩm trong rương phụ? Sau khi xóa "
                                                        + "sẽ không thể khôi phục!",
                                                        "Đồng ý", "Hủy bỏ");
                                                break;
                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPEN_SUKIEN) {
                                        openMenuSuKien(player, this, tempId, select);
                                    }
                                } else if (player.zone instanceof ZSnakeRoad) {
                                    if (mapId == ConstMap.CON_DUONG_RAN_DOC) {
                                        ZSnakeRoad zroad = (ZSnakeRoad) player.zone;
                                        if (zroad.isKilledAll()) {
                                            SnakeRoad road = (SnakeRoad) zroad.getDungeon();
                                            ZSnakeRoad egr = (ZSnakeRoad) road.find(ConstMap.THAN_DIEN);
                                            egr.enter(player, 360, 408);
                                            Service.getInstance().sendThongBao(player, "Hãy xuống gặp thần mèo Karin");
                                        } else {
                                            Service.getInstance().sendThongBao(player,
                                                    "Hãy tiêu diệt hết quái vật ở đây!");
                                        }
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.THAN_VU_TRU:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 48) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Con muốn làm gì nào", "Di chuyển",
                                            getMenuSuKien(EVENT_SEVER));
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 48) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                this.createOtherMenu(player, ConstNpc.MENU_DI_CHUYEN,
                                                        "Con muốn đi đâu?", "Về\nthần điện", "Thánh địa\nKaio",
                                                        "Con\nđường\nrắn độc", getMenuSuKien(EVENT_SEVER), "Từ chối");
                                                break;
                                            case 1:
                                                switch (EVENT_SEVER) {
                                                    case 2:
                                                        Attribute at = ServerManager.gI().getAttributeManager()
                                                                .find(ConstAttribute.HP);
                                                        String text = "Sự kiện 20/11 chính thức tại Ngọc Rồng "
                                                                + Manager.SERVER_NAME + "\n "
                                                                + "Số điểm hiện tại của bạn là : "
                                                                + player.event.getEventPoint()
                                                                + "\nTổng số hoa đã tặng trên toàn máy chủ "
                                                                + EVENT_COUNT_THAN_VU_TRU % 999 + "/999";
                                                        this.createOtherMenu(player, ConstNpc.MENU_OPEN_SUKIEN,
                                                                at != null && !at.isExpired() ? text
                                                                + "\nToàn bộ máy chủ được tăng 20% HP,thời gian còn lại "
                                                                + at.getTime() / 60 + " phút."
                                                                : text + "\nKhi tặng đủ 999 bông hoa toàn bộ máy chủ được tăng 20% HP trong 60 phút\n",
                                                                "Tặng 1\n Bông hoa", "Tặng\n10 Bông", "Tặng\n99 Bông",
                                                                "Đổi\nHộp quà");
                                                        break;
                                                }
                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_DI_CHUYEN) {
                                        switch (select) {
                                            case 0:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 45, -1, 354);
                                                break;
                                            case 1:
                                                ChangeMapService.gI().changeMap(player, 50, -1, 318, 336);
                                                break;
                                            case 2:
                                                // con đường rắn độc
                                                // Service.getInstance().sendThongBao(player, "Comming Soon.");
                                                if (player.clan != null) {
                                                    Calendar calendar = Calendar.getInstance();
                                                    int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
                                                    if (!(dayOfWeek == Calendar.MONDAY
                                                            || dayOfWeek == Calendar.WEDNESDAY
                                                            || dayOfWeek == Calendar.FRIDAY
                                                            || dayOfWeek == Calendar.SUNDAY)) {
                                                        Service.getInstance().sendThongBao(player,
                                                                "Chỉ mở vào thứ 2, 4, 6, CN hàng tuần!");
                                                        return;
                                                    }
                                                    if (player.clanMember.getNumDateFromJoinTimeToToday() < 2) {
                                                        Service.getInstance().sendThongBao(player,
                                                                "Phải tham gia bang hội ít nhất 2 ngày mới có thể tham gia!");
                                                        return;
                                                    }
                                                    if (player.clan.snakeRoad == null) {
                                                        this.createOtherMenu(player, ConstNpc.MENU_CHON_CAP_DO,
                                                                "Hãy mau trở về bằng con đường rắn độc\nbọn Xayda đã đến Trái Đất",
                                                                "Chọn\ncấp độ", "Từ chối");
                                                    } else {
                                                        if (player.clan.snakeRoad.isClosed()) {
                                                            Service.getInstance().sendThongBao(player,
                                                                    "Bang hội đã hết lượt tham gia!");
                                                        } else {
                                                            this.createOtherMenu(player,
                                                                    ConstNpc.MENU_ACCEPT_GO_TO_CDRD,
                                                                    "Con có chắc chắn muốn đến con đường rắn độc cấp độ "
                                                                    + player.clan.snakeRoad.getLevel() + "?",
                                                                    "Đồng ý", "Từ chối");
                                                        }
                                                    }
                                                } else {
                                                    Service.getInstance().sendThongBao(player,
                                                            "Chỉ dành cho những người trong bang hội!");
                                                }
                                                break;

                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_CHON_CAP_DO) {
                                        switch (select) {
                                            case 0:
                                                Input.gI().createFormChooseLevelCDRD(player);
                                                break;

                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_ACCEPT_GO_TO_CDRD) {
                                        switch (select) {
                                            case 0:
                                                if (player.clan != null) {
                                                    synchronized (player.clan) {
                                                        if (player.clan.snakeRoad == null) {
                                                            int level = Byte.parseByte(
                                                                    String.valueOf(PLAYERID_OBJECT.get(player.id)));
                                                            SnakeRoad road = new SnakeRoad(level);
                                                            ServerManager.gI().getDungeonManager().addDungeon(road);
                                                            road.join(player);
                                                            player.clan.snakeRoad = road;
                                                        } else {
                                                            player.clan.snakeRoad.join(player);
                                                        }
                                                    }
                                                }
                                                break;

                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPEN_SUKIEN) {
                                        openMenuSuKien(player, this, tempId, select);
                                    }
                                }
                            }
                        }

                    };
                    break;
                case ConstNpc.KIBIT:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 50) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                            "Đến\nKaio", "Từ chối");
                                } else {
                                    super.openBaseMenu(player);
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 50) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                ChangeMapService.gI().changeMap(player, 48, -1, 354, 240);
                                                break;
                                        }
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.OSIN:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 50) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                            "Đến\nKaio", "Đến\nhành tinh\nBill", "Từ chối");
                                } else if (this.mapId == 52) {
                                    if (MabuWar.gI().isTimeMabuWar() || MabuWar14h.gI().isTimeMabuWar()) {
                                        if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                                    "Bây giờ tôi sẽ bí mật...\n đuổi theo 2 tên đồ tể... \n"
                                                    + "Quý vị nào muốn đi theo thì xin mời !",
                                                    "Ok", "Từ chối");
                                        }
                                    } else {
                                        if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                                            this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                    "Vào lúc 12h tôi sẽ bí mật...\n đuổi theo 2 tên đồ tể... \n"
                                                    + "Quý vị nào muốn đi theo thì xin mời !",
                                                    "Ok", "Từ chối");
                                        }
                                    }
                                } else if (this.mapId == 154) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "|3|Để đến được Hành tinh ngục tù yêu cầu mang 5 món đồ Hủy diệt"
                                            + "\n|1|Ta có thể giúp gì cho ngươi ?",
                                            "Về thánh địa", "Đến\nhành tinh\nngục tù", "Từ chối");
                                } else if (this.mapId == 155 || this.mapId == 165) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Ta có thể giúp gì cho ngươi ?",
                                            "Quay về", "Từ chối");
                                } else if (MapService.gI().isMapMabuWar(this.mapId) || MapService.gI().isMapMabuWar14H(this.mapId)) {
                                    if (MabuWar.gI().isTimeMabuWar()) {
                                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                                "Đừng vội xem thường Babyđây,ngay đến cha hắn là thần ma đạo sĩ\n"
                                                + "Bibiđây khi còn sống cũng phải sợ hắn đấy",
                                                "Giải trừ\nphép thuật\n50Tr Vàng",
                                                player.zone.map.mapId != 120 ? "Xuống\nTầng Dưới" : "Rời\nKhỏi đây");
                                    } else if (MabuWar14h.gI().isTimeMabuWar()) {
                                        createOtherMenu(player, ConstNpc.BASE_MENU, "Ta sẽ phù hộ cho ngươi bằng nguồn sức mạnh của Thần Kaiô"
                                                + "\n+1 triệu HP, +1 triệu MP, +10k Sức đánh"
                                                + "\nLưu ý: sức mạnh sẽ biến mất khi ngươi rời khỏi đây",
                                                "Phù hộ\n55 hồng ngọc", "Từ chối", "Về\nĐại Hội\nVõ Thuật");
                                    }
                                } else {
                                    super.openBaseMenu(player);
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 50) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                ChangeMapService.gI().changeMap(player, 48, -1, 354, 240);
                                                break;
                                            case 1:
                                                ChangeMapService.gI().changeMap(player, 154, -1, 200, 312);
                                                break;
                                        }
                                    }
                                } else if (this.mapId == 52) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                if (MabuWar.gI().isTimeMabuWar()) {
                                                    ChangeMapService.gI().changeMap(player, 114, -1, 354, 240);
                                                } else {
                                                    ChangeMapService.gI().changeMap(player, 127, -1, 354, 240);
                                                }
                                                break;
                                        }
                                    }
                                } else if (this.mapId == 154) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                ChangeMapService.gI().changeMap(player, 50, -1, 318, 336);
                                                break;
                                            case 1:
                                                if (!Manager.gI().getGameConfig().isOpenPrisonPlanet()) {
                                                    Service.getInstance().sendThongBao(player,
                                                            "Lối vào hành tinh ngục tù chưa mở");
                                                    return;
                                                }
                                                if (player.nPoint.power < 60000000000L) {
                                                    Service.getInstance().sendThongBao(player,
                                                            "Yêu cầu tối thiếu 60tỷ sức mạnh");
                                                    return;
                                                }
                                                if (player.setClothes.setDHD != 5) {
                                                    Service.getInstance().sendThongBao(player,
                                                            "Yêu cầu mang set Đồ Hủy diệt");
                                                    return;
                                                }
                                                ChangeMapService.gI().changeMap(player, 155, -1, 111, 792);
                                                break;
                                        }
                                    }
                                } else if (this.mapId == 155) {
                                    if (player.iDMark.isBaseMenu()) {
                                        if (select == 0) {
                                            ChangeMapService.gI().changeMap(player, 154, -1, 200, 312);
                                        }
                                    }
                                } else if (this.mapId == 165) {
                                    if (player.iDMark.isBaseMenu()) {
                                        if (select == 0) {
                                            ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, -1, 250);
                                        }
                                    }
                                } else if (MapService.gI().isMapMabuWar(this.mapId) || MapService.gI().isMapMabuWar14H(this.mapId)) {
                                    if (player.iDMark.isBaseMenu()) {
                                        if (MabuWar.gI().isTimeMabuWar()) {
                                            switch (select) {
                                                case 0:
                                                    if (player.inventory.getGold() >= 50000000) {
                                                        Service.getInstance().changeFlag(player, 9);
                                                        player.inventory.subGold(50000000);

                                                    } else {
                                                        Service.getInstance().sendThongBao(player, "Không đủ vàng");
                                                    }
                                                    break;
                                                case 1:
                                                    if (player.zone.map.mapId == 120) {
                                                        ChangeMapService.gI().changeMapBySpaceShip(player,
                                                                player.gender + 21, -1, 250);
                                                    }
                                                    if (player.cFlag == 9) {
                                                        if (player.getPowerPoint() >= 20) {
                                                            if (!(player.zone.map.mapId == 119)) {
                                                                int idMapNextFloor = player.zone.map.mapId == 115
                                                                        ? player.zone.map.mapId + 2
                                                                        : player.zone.map.mapId + 1;
                                                                ChangeMapService.gI().changeMap(player, idMapNextFloor, -1,
                                                                        354, 240);
                                                            } else {
                                                                Zone zone = MabuWar.gI().getMapLastFloor(120);
                                                                if (zone != null) {
                                                                    ChangeMapService.gI().changeMap(player, zone, 354, 240);
                                                                } else {
                                                                    Service.getInstance().sendThongBao(player,
                                                                            "Trận đại chiến đã kết thúc, tàu vận chuyển sẽ đưa bạn về nhà");
                                                                }
                                                            }
                                                            player.resetPowerPoint();
                                                            player.sendMenuGotoNextFloorMabuWar = false;
                                                            Service.getInstance().sendPowerInfo(player, "%",
                                                                    player.getPowerPoint());
                                                            if (Util.isTrue(1, 30)) {
                                                                player.inventory.ruby += 1;
                                                                PlayerService.gI().sendInfoHpMpMoney(player);
                                                                Service.getInstance().sendThongBao(player,
                                                                        "Bạn nhận được 1 Hồng Ngọc");
                                                            } else {
                                                                Service.getInstance().sendThongBao(player,
                                                                        "Bạn đen vô cùng luôn nên không nhận được gì cả");
                                                            }
                                                        } else {
                                                            this.npcChat(player,
                                                                    "Ngươi cần có đủ điểm để xuống tầng tiếp theo");
                                                        }
                                                        break;
                                                    } else {
                                                        this.npcChat(player,
                                                                "Ngươi đang theo phe Babiđây,Hãy qua bên đó mà thể hiện");
                                                    }
                                            }
                                        } else if (MabuWar14h.gI().isTimeMabuWar()) {
                                            switch (select) {
                                                case 0:
                                                    if (player.effectSkin.isPhuHo) {
                                                        this.npcChat(player, "Con đã mang trong mình sức mạnh của thần Kaiô!");
                                                        return;
                                                    }
                                                    if (player.inventory.ruby < 55) {
                                                        Service.getInstance().sendThongBao(player, "Bạn không đủ hồng ngọc");
                                                    } else {
                                                        player.inventory.ruby -= 55;
                                                        player.effectSkin.isPhuHo = true;
                                                        Service.getInstance().point(player);
                                                        this.npcChat(player, "Ta đã phù hộ cho con hãy giúp ta tiêu diệt Mabư!");
                                                    }
                                                    break;
                                                case 2:
                                                    ChangeMapService.gI().changeMapBySpaceShip(player, 52, -1, 250);
                                                    break;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.BABIDAY:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (MapService.gI().isMapMabuWar(this.mapId)) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                            "Đừng vội xem thường Babyđây,ngay đến cha hắn là thần ma đạo sĩ\n"
                                            + "Bibiđây khi còn sống cũng phải sợ hắn đấy",
                                            "Yểm bùa\n50Tr Vàng",
                                            player.zone.map.mapId != 120 ? "Xuống\nTầng Dưới" : "Rời\nKhỏi đây");
                                } else {
                                    super.openBaseMenu(player);
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (MapService.gI().isMapMabuWar(this.mapId) && MabuWar.gI().isTimeMabuWar()) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                if (player.inventory.getGold() >= 50000000) {
                                                    Service.getInstance().changeFlag(player, 10);
                                                    player.inventory.subGold(50000000);
                                                } else {
                                                    Service.getInstance().sendThongBao(player, "Không đủ vàng");
                                                }
                                                break;
                                            case 1:
                                                if (player.zone.map.mapId == 120) {
                                                    ChangeMapService.gI().changeMapBySpaceShip(player,
                                                            player.gender + 21, -1, 250);
                                                }
                                                if (player.cFlag == 10) {
                                                    if (player.getPowerPoint() >= 20) {
                                                        if (!(player.zone.map.mapId == 119)) {
                                                            int idMapNextFloor = player.zone.map.mapId == 115
                                                                    ? player.zone.map.mapId + 2
                                                                    : player.zone.map.mapId + 1;
                                                            ChangeMapService.gI().changeMap(player, idMapNextFloor, -1,
                                                                    354, 240);
                                                        } else {
                                                            Zone zone = MabuWar.gI().getMapLastFloor(120);
                                                            if (zone != null) {
                                                                ChangeMapService.gI().changeMap(player, zone, 354, 240);
                                                            } else {
                                                                Service.getInstance().sendThongBao(player,
                                                                        "Trận đại chiến đã kết thúc, tàu vận chuyển sẽ đưa bạn về nhà");
                                                                ChangeMapService.gI().changeMapBySpaceShip(player,
                                                                        player.gender + 21, -1, 250);
                                                            }
                                                        }
                                                        player.resetPowerPoint();
                                                        player.sendMenuGotoNextFloorMabuWar = false;
                                                        Service.getInstance().sendPowerInfo(player, "TL",
                                                                player.getPowerPoint());
                                                        if (Util.isTrue(1, 30)) {
                                                            player.inventory.ruby += 1;
                                                            PlayerService.gI().sendInfoHpMpMoney(player);
                                                            Service.getInstance().sendThongBao(player,
                                                                    "Bạn nhận được 1 Hồng Ngọc");
                                                        } else {
                                                            Service.getInstance().sendThongBao(player,
                                                                    "Bạn đen vô cùng luôn nên không nhận được gì cả");
                                                        }
                                                    } else {
                                                        this.npcChat(player,
                                                                "Ngươi cần có đủ điểm để xuống tầng tiếp theo");
                                                    }
                                                    break;
                                                } else {
                                                    this.npcChat(player,
                                                            "Ngươi đang theo phe Ôsin,Hãy qua bên đó mà thể hiện");
                                                }
                                        }
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.LINH_CANH:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (player.clan == null) {
                                    this.createOtherMenu(player, ConstNpc.MENU_KHONG_CHO_VAO_DT,
                                            "Chỉ tiếp các bang hội, miễn tiếp khách vãng lai", "Đóng");
                                } else if (player.clan.getMembers().size() < 5) {
                                    // } else if (player.clan.getMembers().size() < 1) {
                                    this.createOtherMenu(player, ConstNpc.MENU_KHONG_CHO_VAO_DT,
                                            "Bang hội phải có ít nhất 5 thành viên mới có thể mở", "Đóng");
                                } else {
                                    ClanMember clanMember = player.clan.getClanMember((int) player.id);
                                    int days = (int) (((System.currentTimeMillis() / 1000) - clanMember.joinTime) / 60
                                            / 60 / 24);
                                    if (days < 2) {
                                        NpcService.gI().createTutorial(player, avartar,
                                                "Chỉ những thành viên gia nhập bang hội tối thiểu 2 ngày mới có thể tham gia");
                                        return;
                                    }
                                    if (!player.clan.haveGoneDoanhTrai && player.clan.timeOpenDoanhTrai != 0) {
                                        createOtherMenu(player, ConstNpc.MENU_VAO_DT,
                                                "Bang hội của ngươi đang đánh trại độc nhãn\n" + "Thời gian còn lại là "
                                                + TimeUtil.getSecondLeft(player.clan.timeOpenDoanhTrai,
                                                        DoanhTrai.TIME_DOANH_TRAI / 1000)
                                                + ". Ngươi có muốn tham gia không?",
                                                "Tham gia", "Không", "Hướng\ndẫn\nthêm");
                                    } else {
                                        List<Player> plSameClans = new ArrayList<>();
                                        List<Player> playersMap = player.zone.getPlayers();
                                        synchronized (playersMap) {
                                            for (Player pl : playersMap) {
                                                if (!pl.equals(player) && pl.clan != null
                                                        && pl.clan.id == player.clan.id && pl.location.x >= 1285
                                                        && pl.location.x <= 1645) {
                                                    plSameClans.add(pl);
                                                }

                                            }
                                        }
                                        // if (plSameClans.size() >= 0) {
                                        if (plSameClans.size() >= 2) {
                                            if (!player.isAdmin() && player.clanMember
                                                    .getNumDateFromJoinTimeToToday() < DoanhTrai.DATE_WAIT_FROM_JOIN_CLAN) {
                                                createOtherMenu(player, ConstNpc.MENU_KHONG_CHO_VAO_DT,
                                                        "Bang hội chỉ cho phép những người ở trong bang trên 1 ngày. Hẹn ngươi quay lại vào lúc khác",
                                                        "OK", "Hướng\ndẫn\nthêm");
                                            } else if (player.clan.haveGoneDoanhTrai) {
                                                createOtherMenu(player, ConstNpc.MENU_KHONG_CHO_VAO_DT,
                                                        "Bang hội của ngươi đã đi trại lúc "
                                                        + Util.formatTime(player.clan.timeOpenDoanhTrai)
                                                        + " hôm nay. Người mở\n" + "("
                                                        + player.clan.playerOpenDoanhTrai.name
                                                        + "). Hẹn ngươi quay lại vào ngày mai",
                                                        "OK", "Hướng\ndẫn\nthêm");

                                            } else {
                                                createOtherMenu(player, ConstNpc.MENU_CHO_VAO_DT,
                                                        "Hôm nay bang hội của ngươi chưa vào trại lần nào. Ngươi có muốn vào\n"
                                                        + "không?\nĐể vào, ta khuyên ngươi nên có 3-4 người cùng bang đi cùng",
                                                        "Vào\n(miễn phí)", "Không", "Hướng\ndẫn\nthêm");
                                            }
                                        } else {
                                            createOtherMenu(player, ConstNpc.MENU_KHONG_CHO_VAO_DT,
                                                    "Ngươi phải có ít nhất 2 đồng đội cùng bang đứng gần mới có thể\nvào\n"
                                                    + "tuy nhiên ta khuyên ngươi nên đi cùng với 3-4 người để khỏi chết.\n"
                                                    + "Hahaha.",
                                                    "OK", "Hướng\ndẫn\nthêm");
                                        }
                                    }
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 27) {
                                    switch (player.iDMark.getIndexMenu()) {
                                        case ConstNpc.MENU_KHONG_CHO_VAO_DT:
                                            if (select == 1) {
                                                NpcService.gI().createTutorial(player, this.avartar,
                                                        ConstNpc.HUONG_DAN_DOANH_TRAI);
                                            }
                                            break;
                                        case ConstNpc.MENU_CHO_VAO_DT:
                                            switch (select) {
                                                case 0:
                                                    DoanhTraiService.gI().openDoanhTrai(player);
                                                    break;
                                                case 2:
                                                    NpcService.gI().createTutorial(player, this.avartar,
                                                            ConstNpc.HUONG_DAN_DOANH_TRAI);
                                                    break;
                                            }
                                            break;
                                        case ConstNpc.MENU_VAO_DT:
                                            switch (select) {
                                                case 0:
                                                    ChangeMapService.gI().changeMap(player, 53, 0, 35, 432);
                                                    break;
                                                case 2:
                                                    NpcService.gI().createTutorial(player, this.avartar,
                                                            ConstNpc.HUONG_DAN_DOANH_TRAI);
                                                    break;
                                            }
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.QUA_TRUNG:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {

                        private final int COST_AP_TRUNG_NHANH = 1000000000;

                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                player.mabuEgg.sendMabuEgg();
                                if (player.mabuEgg.getSecondDone() != 0) {
                                    this.createOtherMenu(player, ConstNpc.CAN_NOT_OPEN_EGG, "Bư bư bư...",
                                            "Hủy bỏ\ntrứng",
                                            "Ấp nhanh\n" + Util.numberToMoney(COST_AP_TRUNG_NHANH) + " vàng", "Đóng");
                                } else {
                                    this.createOtherMenu(player, ConstNpc.CAN_OPEN_EGG, "Bư bư bư...", "Nở",
                                            "Hủy bỏ\ntrứng", "Đóng");
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                switch (player.iDMark.getIndexMenu()) {
                                    case ConstNpc.CAN_NOT_OPEN_EGG:
                                        if (select == 0) {
                                            this.createOtherMenu(player, ConstNpc.CONFIRM_DESTROY_EGG,
                                                    "Bạn có chắc chắn muốn hủy bỏ trứng Mabư?", "Đồng ý", "Từ chối");
                                        } else if (select == 1) {
                                            if (player.inventory.gold >= COST_AP_TRUNG_NHANH) {
                                                player.inventory.gold -= COST_AP_TRUNG_NHANH;
                                                player.mabuEgg.timeDone = 0;
                                                Service.getInstance().sendMoney(player);
                                                player.mabuEgg.sendMabuEgg();
                                            } else {
                                                Service.getInstance().sendThongBao(player,
                                                        "Bạn không đủ vàng để thực hiện, còn thiếu "
                                                        + Util.numberToMoney(
                                                                (COST_AP_TRUNG_NHANH - player.inventory.gold))
                                                        + " vàng");
                                            }
                                        }
                                        break;
                                    case ConstNpc.CAN_OPEN_EGG:
                                        switch (select) {
                                            case 0:
                                                this.createOtherMenu(player, ConstNpc.CONFIRM_OPEN_EGG,
                                                        "Bạn có chắc chắn cho trứng nở?\n"
                                                        + "Đệ tử của bạn sẽ được thay thế bằng đệ Mabư",
                                                        "Đệ mabư\nTrái Đất", "Đệ mabư\nNamếc", "Đệ mabư\nXayda",
                                                        "Từ chối");
                                                break;
                                            case 1:
                                                this.createOtherMenu(player, ConstNpc.CONFIRM_DESTROY_EGG,
                                                        "Bạn có chắc chắn muốn hủy bỏ trứng Mabư?", "Đồng ý",
                                                        "Từ chối");
                                                break;
                                        }
                                        break;
                                    case ConstNpc.CONFIRM_OPEN_EGG:
                                        switch (select) {
                                            case 0:
                                                player.mabuEgg.openEgg(ConstPlayer.TRAI_DAT);
                                                break;
                                            case 1:
                                                player.mabuEgg.openEgg(ConstPlayer.NAMEC);
                                                break;
                                            case 2:
                                                player.mabuEgg.openEgg(ConstPlayer.XAYDA);
                                                break;
                                            default:
                                                break;
                                        }
                                        break;
                                    case ConstNpc.CONFIRM_DESTROY_EGG:
                                        if (select == 0) {
                                            player.mabuEgg.destroyEgg();
                                        }
                                        break;
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.QUOC_VUONG:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {

                        @Override
                        public void openBaseMenu(Player player) {
                            this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                    "Con muốn nâng giới hạn sức mạnh cho bản thân hay đệ tử?", "Bản thân", "Đệ tử");
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (player.iDMark.isBaseMenu()) {
                                    switch (select) {
                                        case 0:
                                            if (player.nPoint.limitPower < NPoint.MAX_LIMIT) {
                                                this.createOtherMenu(player, ConstNpc.OPEN_POWER_MYSEFT,
                                                        "Ta sẽ truền năng lượng giúp con mở giới hạn sức mạnh của bản thân lên "
                                                        + Util.numberToMoney(player.nPoint.getPowerNextLimit()),
                                                        "Nâng ngay\n" + Util.numberToMoney(OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) + " vàng", "Đóng");
                                            } else {
                                                this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                        "Sức mạnh của con đã đạt tới giới hạn",
                                                        "Đóng");
                                            }
                                            break;
                                        case 1:
                                            if (player.pet != null) {
                                                if (player.pet.nPoint.limitPower < NPoint.MAX_LIMIT) {
                                                    this.createOtherMenu(player, ConstNpc.OPEN_POWER_PET,
                                                            "Ta sẽ truền năng lượng giúp con mở giới hạn sức mạnh của đệ tử lên "
                                                            + Util.numberToMoney(player.pet.nPoint.getPowerNextLimit()),
                                                            "Nâng ngay\n" + Util.numberToMoney(OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) + " vàng", "Đóng");
                                                } else {
                                                    this.createOtherMenu(player, ConstNpc.IGNORE_MENU,
                                                            "Sức mạnh của đệ con đã đạt tới giới hạn",
                                                            "Đóng");
                                                }
                                            } else {
                                                Service.getInstance().sendThongBao(player, "Không thể thực hiện");
                                            }
                                            // giới hạn đệ tử
                                            break;
                                    }
                                } else if (player.iDMark.getIndexMenu() == ConstNpc.OPEN_POWER_MYSEFT && player.nPoint.limitPower < NPoint.MAX_LIMIT) {
                                    switch (select) {
                                        case 0:
                                            if (player.inventory.gold >= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) {
                                                if (OpenPowerService.gI().openPowerSpeed(player)) {
                                                    player.inventory.gold -= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER;
                                                    Service.getInstance().sendMoney(player);
                                                }
                                            } else {
                                                Service.getInstance().sendThongBao(player,
                                                        "Bạn không đủ vàng để mở, còn thiếu "
                                                        + Util.numberToMoney((OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER - player.inventory.gold)) + " vàng");
                                            }
                                            break;
                                    }
                                } else if (player.iDMark.getIndexMenu() == ConstNpc.OPEN_POWER_PET && player.pet.nPoint.limitPower < NPoint.MAX_LIMIT) {
                                    if (select == 0) {
                                        if (player.inventory.gold >= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER) {
                                            if (OpenPowerService.gI().openPowerSpeed(player.pet)) {
                                                player.inventory.gold -= OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER;
                                                Service.getInstance().sendMoney(player);
                                            }
                                        } else {
                                            Service.getInstance().sendThongBao(player,
                                                    "Bạn không đủ vàng để mở, còn thiếu "
                                                    + Util.numberToMoney((OpenPowerService.COST_SPEED_OPEN_LIMIT_POWER - player.inventory.gold)) + " vàng");
                                        }
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.BARDOCK:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Tôi có thể giúp gì cho bạn??", "Đóng");
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (player.iDMark.isBaseMenu()) {
                                    switch (select) {
                                        case 0:
                                            break;
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.BERRY:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Tôi có thể giúp gì cho bạn??", "Đóng");
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (player.iDMark.isBaseMenu()) {
                                    switch (select) {
                                        case 0:
                                            break;
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.BUNMA_TL:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (!TaskService.gI().checkDoneTaskTalkNpc(player, this)) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Cậu bé muốn mua gì nào?",
                                            "Cửa hàng", "Đóng");
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (player.iDMark.isBaseMenu()) {
                                    if (select == 0) {
                                        ShopService.gI().openShopNormal(player, this, ConstNpc.SHOP_BUNMA_TL_0, 0,
                                                player.gender);
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.RONG_OMEGA:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                BlackBallWar.gI().setTime();
                                if (this.mapId == 24 || this.mapId == 25 || this.mapId == 26) {
                                    try {
                                        long now = System.currentTimeMillis();
                                        if (now > BlackBallWar.TIME_OPEN && now < BlackBallWar.TIME_CLOSE) {
                                            this.createOtherMenu(player, ConstNpc.MENU_OPEN_BDW,
                                                    "Đường đến với ngọc rồng sao đen đã mở, "
                                                    + "ngươi có muốn tham gia không?",
                                                    "Hướng dẫn\nthêm", "Tham gia", "Từ chối");
                                        } else {
                                            String[] optionRewards = new String[7];
                                            int index = 0;
                                            for (int i = 0; i < 7; i++) {
                                                if (player.rewardBlackBall.timeOutOfDateReward[i] > System
                                                        .currentTimeMillis()) {
                                                    optionRewards[index] = "Nhận thưởng\n" + (i + 1) + " sao";
                                                    index++;
                                                }
                                            }
                                            if (index != 0) {
                                                String[] options = new String[index + 1];
                                                for (int i = 0; i < index; i++) {
                                                    options[i] = optionRewards[i];
                                                }
                                                options[options.length - 1] = "Từ chối";
                                                this.createOtherMenu(player, ConstNpc.MENU_REWARD_BDW,
                                                        "Ngươi có một vài phần thưởng ngọc " + "rồng sao đen đây!",
                                                        options);
                                            } else {
                                                this.createOtherMenu(player, ConstNpc.MENU_NOT_OPEN_BDW,
                                                        "Phần Thưởng của Bang Hội chiến thắng..."
                                                        + "\n|1|1sao : 25% SD"
                                                        + "\n|1|2sao : 50% HP"
                                                        + "\n|1|3sao : 25% SD + 25% KI"
                                                        + "\n|1|4sao : 25% ST Chí Mạng"
                                                        + "\n|1|5sao : 25% Hút HP"
                                                        + "\n|1|6sao : 25% Hút KI"
                                                        + "\n|1|7sao : 25% Giáp",
                                                        "Hướng dẫn", "Từ chối");
                                            }
                                        }
                                    } catch (Exception ex) {
                                        Log.error("Lỗi mở menu rồng Omega");
                                    }
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                switch (player.iDMark.getIndexMenu()) {
                                    case ConstNpc.MENU_REWARD_BDW:
                                        player.rewardBlackBall.getRewardSelect((byte) select);
                                        break;
                                    case ConstNpc.MENU_OPEN_BDW:
                                        if (select == 0) {
                                            NpcService.gI().createTutorial(player, this.avartar,
                                                    ConstNpc.HUONG_DAN_BLACK_BALL_WAR);
                                        } else if (select == 1) {
                                            player.iDMark.setTypeChangeMap(ConstMap.CHANGE_BLACK_BALL);
                                            ChangeMapService.gI().openChangeMapTab(player);
                                        }
                                        break;
                                    case ConstNpc.MENU_NOT_OPEN_BDW:
                                        if (select == 0) {
                                            NpcService.gI().createTutorial(player, this.avartar,
                                                    ConstNpc.HUONG_DAN_BLACK_BALL_WAR);
                                        }
                                        break;
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.RONG_1S:
                case ConstNpc.RONG_2S:
                case ConstNpc.RONG_3S:
                case ConstNpc.RONG_4S:
                case ConstNpc.RONG_5S:
                case ConstNpc.RONG_6S:
                case ConstNpc.RONG_7S:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (player.isHoldBlackBall) {
                                    this.createOtherMenu(player, ConstNpc.MENU_PHU_HP, "Ta có thể giúp gì cho ngươi?",
                                            "Phù hộ", "Từ chối");
                                } else {
                                    this.createOtherMenu(player, ConstNpc.MENU_OPTION_GO_HOME,
                                            "Ta có thể giúp gì cho ngươi?", "Về nhà", "Từ chối");
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (player.iDMark.getIndexMenu() == ConstNpc.MENU_PHU_HP) {
                                    if (select == 0) {
                                        this.createOtherMenu(player, ConstNpc.MENU_OPTION_PHU_HP,
                                                "Ta sẽ giúp ngươi tăng HP lên mức kinh hoàng, ngươi chọn đi",
                                                "x3 HP\n" + Util.numberToMoney(BlackBallWar.COST_X3) + " vàng",
                                                "x5 HP\n" + Util.numberToMoney(BlackBallWar.COST_X5) + " vàng",
                                                "x7 HP\n" + Util.numberToMoney(BlackBallWar.COST_X7) + " vàng",
                                                "Từ chối");
                                    }
                                } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_GO_HOME) {
                                    if (select == 0) {
                                        ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, -1, 250);
                                    }
                                } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_PHU_HP) {
                                    switch (select) {
                                        case 0:
                                            BlackBallWar.gI().xHPKI(player, BlackBallWar.X3);
                                            break;
                                        case 1:
                                            BlackBallWar.gI().xHPKI(player, BlackBallWar.X5);
                                            break;
                                        case 2:
                                            BlackBallWar.gI().xHPKI(player, BlackBallWar.X7);
                                            break;
                                        case 3:
                                            this.npcChat(player, "Để ta xem ngươi trụ được bao lâu");
                                            break;
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.NPC_64:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                createOtherMenu(player, ConstNpc.BASE_MENU, "Ngươi muốn xem thông tin gì?",
                                        "Top\nsức mạnh", "Đóng");
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (player.iDMark.isBaseMenu()) {
                                    if (select == 0) {
                                        Service.getInstance().showTopPower(player, Service.getInstance().TOP_SUCMANH);
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.BILL:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (player == null || !canOpenNpc(player)) {
                                return;
                            }
                            if (this.mapId == 48) {
                                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                        "|7|SHOP ĐỒ HỦY DIỆT\n|6| Mang đủ 5 món đồ Thần linh và đem 99 Thức ăn đến cho ta. Ta sẽ bán đồ Hủy diệt cho ngươi",
                                        "SHOP HỦY DIỆT");//, getMenuSuKien(EVENT_SEVER), "Đóng");
                            } else if (this.mapId == 154) {
                                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                        "Ngươi cần gì ở ta?",
                                        "Vũ Trụ\nSiêu Cấp", "Đóng");
                            } else {
                                super.openBaseMenu(player);
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (player == null || !canOpenNpc(player)) {
                                return;
                            }
                            switch (this.mapId) {
                                case 48:
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                if (player.check99ThucAnHuyDiet()) {
                                                    if (player.setClothes.setDTL == 5) {
                                                        ShopService.gI().openShopSpecial(player, this, ConstNpc.SHOP_BILL_HUY_DIET_0, 0, -1);
                                                    } else {
                                                        createOtherMenu(player, ConstNpc.IGNORE_MENU, "Yêu cầu mặc 5 món Thần linh", "Đóng");
                                                    }
                                                } else {
                                                    createOtherMenu(player, ConstNpc.IGNORE_MENU, "Ngươi chưa đủ 99 thức ăn", "Đóng");
                                                }
                                                break;
                                            case 1:
                                                switch (EVENT_SEVER) {
                                                    case 2:
                                                        Attribute at = ServerManager.gI().getAttributeManager()
                                                                .find(ConstAttribute.SUC_DANH);
                                                        String text = "Sự kiện 20/11 chính thức tại Ngọc Rồng "
                                                                + Manager.SERVER_NAME + "\n"
                                                                + "Số điểm hiện tại của bạn là : "
                                                                + player.event.getEventPoint()
                                                                + "\nTổng số hoa đã tặng trên toàn máy chủ "
                                                                + EVENT_COUNT_THAN_HUY_DIET % 999 + "/999";
                                                        this.createOtherMenu(player, ConstNpc.MENU_OPEN_SUKIEN,
                                                                at != null && !at.isExpired() ? text
                                                                + "\nToàn bộ máy chủ được tăng 20% sức đánh, thời gian còn lại "
                                                                + at.getTime() / 60 + " phút."
                                                                : text + "\nKhi tặng đủ 999 bông toàn bộ máy chủ được tăng tăng 20% sức đánh trong 60 phút\n",
                                                                "Tặng 1\n Bông hoa", "Tặng\n10 Bông",
                                                                "Tặng\n99 Bông", "Đổi\nHộp quà");
                                                        break;
                                                    default:
                                                        createOtherMenu(player, 5656,
                                                                "|7|Npc này không liên quan đến Sự kiện\nVui lòng tìm Npc khác !!!", "Đóng");
                                                        break;
                                                }
                                                break;
                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPEN_SUKIEN) {
                                        openMenuSuKien(player, this, tempId, select);
                                    }
                                    break;
                                case 154:
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 145, -1, 745);
                                                break;
                                            case 1:
                                                this.npcChat(player, "Tạm biệt!");
                                                break;
                                        }
                                    }
                                    break;
                            }
                        }
                    };
                    break;
                case ConstNpc.WHIS:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 0 || this.mapId == 7 || this.mapId == 14 || this.mapId == 48) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Đã tìm đủ nguyên liệu cho tôi chưa?"
                                            + "\n Tôi sẽ giúp cậu mạnh lên kha khá đấy!"
                                            + "\n\b|7| Điều kiện học Tuyệt kỹ"
                                            + "\b|5| -Khi lần đầu học skill cần: x999 Bí kiếp tuyệt kỹ và SM trên 60 Tỷ"
                                            + "\n -Mỗi một cấp yêu cầu: x9999 Bí kiếp tuyệt kỹ và 100k Hồng ngọc", "Học\ntuyệt kĩ", "Từ Chối");
                                }
                                if (this.mapId == 154) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                            "|7|NÂNG CẤP ĐỒ THIÊN SỨ\n|6| Mang cho ta Công thức + Đá cầu vòng và 999 Mảnh thiên sứ ta sẽ chế tạo đồ Thiên sứ cho ngươi"
                                            + "\nĐồ Thiên sứ khi chế tạo sẽ random chỉ số 0-15%"
                                            + "\n|2|(Khi mang đủ 5 món Hủy diệt ngươi hãy theo Osin qua Hành tinh ngục tù tìm kiếm Mảnh thiên sứ và săn BOSS Thiên sứ để thu thập Đá cầu vòng)"
                                            + "\n|1| Ngươi có muốn nâng cấp không?",
                                            "Hướng dẫn", "Nâng Cấp \nĐồ Thiên Sứ", "Shop\n Thiên sứ", "Phân rã\nĐồ Thiên sứ", "Nâng SKH\nVIP");
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 154) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                NpcService.gI().createTutorial(player, this.avartar, ConstNpc.HUONG_DAN_DO_TS);
                                                break;
                                            case 1:
                                                CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_DO_TS);
                                                break;
                                            case 2:
                                                ShopService.gI().openShopWhisThienSu(player,
                                                        ConstNpc.SHOP_WHIS_THIEN_SU, 0);
                                                break;
                                            case 3: //Mở chỉ số bông tai
                                                CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.PHAN_RA_DO_TS);
                                                break;
                                            case 4:
                                                CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_SKH_TS);
                                                break;
                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                                        switch (player.combineNew.typeCombine) {
                                            case CombineServiceNew.NANG_CAP_DO_TS:
                                            case CombineServiceNew.NANG_CAP_SKH_TS:
                                            case CombineServiceNew.PHAN_RA_DO_TS:
                                                if (select == 0) {
                                                    CombineServiceNew.gI().startCombine(player);
                                                }
                                                break;
                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NANG_CAP_DO_TS) {
                                        if (select == 0) {
                                            CombineServiceNew.gI().startCombine(player);
                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_NANG_DO_SKH_TS) {
                                        if (select == 0) {
                                            CombineServiceNew.gI().startCombine(player);
                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_PHAN_RA_TS) {
                                        if (select == 0) {
                                            CombineServiceNew.gI().startCombine(player);
                                        }
                                    }
                                }

                                if (player.iDMark.isBaseMenu() && (this.mapId == 0 || this.mapId == 7 || this.mapId == 14 || this.mapId == 48)) {
                                    if (select == 0) {
                                        Message msg;
                                        try {
                                            Item sachTuyetki = null;
                                            try {
                                                sachTuyetki = InventoryService.gI().findItemBagByTemp(player, 1215);
                                            } catch (Exception e) {
                                            }
                                            if (player.gender == 0) {
                                                Skill curSkill = SkillUtil.getSkillbyId(player, Skill.SUPER_KAME);
                                                if (curSkill.point == 0) {
                                                    if (player.nPoint.power >= 60000000000L) {
                                                        if (sachTuyetki == null || sachTuyetki.quantity < 999) {
                                                            this.npcChat(player, "Bạn không đủ 999 bí kíp tuyệt kĩ");
                                                            return;
                                                        }
                                                        if (sachTuyetki.quantity >= 999) {
                                                            InventoryService.gI().subQuantityItemsBag(player, sachTuyetki, 999);
                                                            InventoryService.gI().sendItemBags(player);
                                                            curSkill = SkillUtil.createSkill(Skill.SUPER_KAME, 1);
                                                            SkillUtil.setSkill(player, curSkill);
                                                            msg = Service.getInstance().messageSubCommand((byte) 23);
                                                            msg.writer().writeShort(curSkill.skillId);
                                                            player.sendMessage(msg);
                                                            msg.cleanup();
                                                        } else {
                                                            Service.getInstance().sendThongBao(player, "Không đủ bí kíp tuyệt kĩ");
                                                        }
                                                    } else {
                                                        Service.getInstance().sendThongBao(player, "Yêu cầu sức mạnh trên 60 Tỷ");
                                                    }
                                                } else if (curSkill.point > 0 && curSkill.point < 9) {
                                                    if (sachTuyetki == null || sachTuyetki.quantity < 9999) {
                                                        this.npcChat(player, "Bạn không đủ 9999 bí kíp tuyệt kĩ");
                                                        return;
                                                    }
                                                    if (sachTuyetki.quantity >= 999) {// && curSkill.currLevel == 1000
                                                        if (player.inventory.ruby < 100000) {
                                                            this.npcChat(player, "Bạn không đủ 100k Hồng ngọc");
                                                            return;
                                                        }
                                                        player.inventory.ruby -= 100000;
                                                        Service.getInstance().sendMoney(player);
                                                        InventoryService.gI().subQuantityItemsBag(player, sachTuyetki, 9999);
                                                        InventoryService.gI().sendItemBags(player);
                                                        curSkill = SkillUtil.createSkill(Skill.SUPER_KAME, curSkill.point + 1);
                                                        SkillUtil.setSkill(player, curSkill);
                                                        msg = Service.getInstance().messageSubCommand((byte) 62);
                                                        msg.writer().writeShort(curSkill.skillId);
                                                        player.sendMessage(msg);
                                                        msg.cleanup();
                                                    } else {
                                                        Service.getInstance().sendThongBao(player, "Thông thạo của bạn chưa đủ 100%");
                                                    }
                                                } else {
                                                    Service.getInstance().sendThongBao(player, "Tuyệt kĩ của bạn đã đạt cấp tối đa");
                                                }
                                            }
                                            if (player.gender == 1) {
                                                Skill curSkill = SkillUtil.getSkillbyId(player, Skill.MA_PHONG_BA);
                                                if (curSkill.point == 0) {
                                                    if (player.nPoint.power >= 60000000000L) {
                                                        if (sachTuyetki == null || sachTuyetki.quantity < 999) {
                                                            this.npcChat(player, "Bạn không đủ 999 bí kíp tuyệt kĩ");
                                                            return;
                                                        }
                                                        if (sachTuyetki.quantity >= 999) {
                                                            InventoryService.gI().subQuantityItemsBag(player, sachTuyetki, 999);
                                                            InventoryService.gI().sendItemBags(player);
                                                            curSkill = SkillUtil.createSkill(Skill.MA_PHONG_BA, 1);
                                                            SkillUtil.setSkill(player, curSkill);
                                                            msg = Service.getInstance().messageSubCommand((byte) 23);
                                                            msg.writer().writeShort(curSkill.skillId);
                                                            player.sendMessage(msg);
                                                            msg.cleanup();
                                                        } else {
                                                            Service.getInstance().sendThongBao(player, "Không đủ bí kíp tuyệt kĩ");
                                                        }
                                                    } else {
                                                        Service.getInstance().sendThongBao(player, "Yêu cầu sức mạnh trên 60 Tỷ");
                                                    }
                                                } else if (curSkill.point > 0 && curSkill.point < 9) {
                                                    if (sachTuyetki == null || sachTuyetki.quantity < 9999) {
                                                        this.npcChat(player, "Bạn không đủ 9999 bí kíp tuyệt kĩ");
                                                        return;
                                                    }
                                                    if (sachTuyetki.quantity >= 999) {// && curSkill.currLevel == 1000
                                                        if (player.inventory.ruby < 100000) {
                                                            this.npcChat(player, "Bạn không đủ 100k Hồng ngọc");
                                                            return;
                                                        }
                                                        player.inventory.ruby -= 100000;
                                                        Service.getInstance().sendMoney(player);
                                                        InventoryService.gI().subQuantityItemsBag(player, sachTuyetki, 9999);
                                                        InventoryService.gI().sendItemBags(player);
                                                        curSkill = SkillUtil.createSkill(Skill.MA_PHONG_BA, curSkill.point + 1);
                                                        SkillUtil.setSkill(player, curSkill);
                                                        msg = Service.getInstance().messageSubCommand((byte) 62);
                                                        msg.writer().writeShort(curSkill.skillId);
                                                        player.sendMessage(msg);
                                                        msg.cleanup();
                                                    } else {
                                                        Service.getInstance().sendThongBao(player, "Thông thạo của bạn chưa đủ 100%");
                                                    }
                                                } else {
                                                    Service.getInstance().sendThongBao(player, "Tuyệt kĩ của bạn đã đạt cấp tối đa");
                                                }
                                            }
                                            if (player.gender == 2) {
                                                Skill curSkill = SkillUtil.getSkillbyId(player, Skill.LIEN_HOAN_CHUONG);
                                                if (curSkill.point == 0) {
                                                    if (player.nPoint.power >= 60000000000L) {
                                                        if (sachTuyetki == null || sachTuyetki.quantity < 999) {
                                                            this.npcChat(player, "Bạn không đủ 999 bí kíp tuyệt kĩ");
                                                            return;
                                                        }
                                                        if (sachTuyetki.quantity >= 999) {
                                                            InventoryService.gI().subQuantityItemsBag(player, sachTuyetki, 999);
                                                            InventoryService.gI().sendItemBags(player);
                                                            curSkill = SkillUtil.createSkill(Skill.LIEN_HOAN_CHUONG, 1);
                                                            SkillUtil.setSkill(player, curSkill);
                                                            msg = Service.getInstance().messageSubCommand((byte) 23);
                                                            msg.writer().writeShort(curSkill.skillId);
                                                            player.sendMessage(msg);
                                                            msg.cleanup();
                                                        } else {
                                                            Service.getInstance().sendThongBao(player, "Không đủ bí kíp tuyệt kĩ");
                                                        }
                                                    } else {
                                                        Service.getInstance().sendThongBao(player, "Yêu cầu sức mạnh trên 60 Tỷ");
                                                    }
                                                } else if (curSkill.point > 0 && curSkill.point < 9) {
                                                    if (sachTuyetki == null || sachTuyetki.quantity < 9999) {
                                                        this.npcChat(player, "Bạn không đủ 9999 bí kíp tuyệt kĩ");
                                                        return;
                                                    }
                                                    if (sachTuyetki.quantity >= 999) {
                                                        if (player.inventory.ruby < 100000) {
                                                            this.npcChat(player, "Bạn không đủ 100k Hồng ngọc");
                                                            return;
                                                        }
                                                        player.inventory.ruby -= 100000;
                                                        Service.getInstance().sendMoney(player);
                                                        InventoryService.gI().subQuantityItemsBag(player, sachTuyetki, 9999);
                                                        InventoryService.gI().sendItemBags(player);
                                                        curSkill = SkillUtil.createSkill(Skill.LIEN_HOAN_CHUONG, curSkill.point + 1);
                                                        SkillUtil.setSkill(player, curSkill);
                                                        msg = Service.getInstance().messageSubCommand((byte) 62);
                                                        msg.writer().writeShort(curSkill.skillId);
                                                        player.sendMessage(msg);
                                                        msg.cleanup();
                                                    } else {
                                                        Service.getInstance().sendThongBao(player, "Thông thạo của bạn chưa đủ 100%");
                                                    }
                                                } else {
                                                    Service.getInstance().sendThongBao(player, "Tuyệt kĩ của bạn đã đạt cấp tối đa");
                                                }
                                            }
                                        } catch (Exception ex) {
                                            ex.printStackTrace();
                                        }
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.BO_MONG:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 47 || this.mapId == 84) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "Xin chào, cậu muốn tôi giúp gì?",
                                            "Nhiệm vụ\nhàng ngày",
                                            "Nhập Code riêng",
                                            "Thưởng\nNhiệm Vụ",
                                            "Mua\nDanh Hiệu VIP",
                                            "Từ chối");
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 47 || this.mapId == 84) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                if (player.playerTask.sideTask.template != null) {
                                                    String npcSay = "Nhiệm vụ hiện tại: "
                                                            + player.playerTask.sideTask.getName() + " ("
                                                            + player.playerTask.sideTask.getLevel() + ")"
                                                            + "\nHiện tại đã hoàn thành: "
                                                            + player.playerTask.sideTask.count + "/"
                                                            + player.playerTask.sideTask.maxCount + " ("
                                                            + player.playerTask.sideTask.getPercentProcess()
                                                            + "%)\nSố nhiệm vụ còn lại trong ngày: "
                                                            + player.playerTask.sideTask.leftTask + "/"
                                                            + ConstTask.MAX_SIDE_TASK;
                                                    this.createOtherMenu(player, ConstNpc.MENU_OPTION_PAY_SIDE_TASK,
                                                            npcSay, "Trả nhiệm\nvụ", "Hủy nhiệm\nvụ");
                                                } else {
                                                    this.createOtherMenu(player, ConstNpc.MENU_OPTION_LEVEL_SIDE_TASK,
                                                            "|2|Khi hoàn thành các nhiệm vụ ngày nhận được các phần thưởng sau:"
                                                            + "\n|4|-Dễ : 1-10 " + ConstItem.NAME_XU_SERVER + " + 1 Máy dò Boss"
                                                            //                                                            + "\n-Bthường : 70TV  + 2000 Hngọc"
                                                            + "\n-Khó : 5-15 " + ConstItem.NAME_XU_SERVER + " + 1-2 Máy dò Boss"
                                                            + "\n-Siêu khó : 10-25 " + ConstItem.NAME_XU_SERVER + " + 1-3 Máy dò Boss"
                                                            + "\n|1|Tôi có vài nhiệm vụ theo cấp bậc, sức cậu có thể làm được cái nào?"
                                                            + "\n|1|Một ngày làm được 10 lần , Trả nhiệm vụ trước 00h00"
                                                            + "\n|1|Nếu quên hệ thống sẽ không REST nhiệm vụ ae lưu ý?",
                                                            "Dễ", "Khó", "Siêu Khó", "Từ chối");//, "Bình thường", "Khó", "Địa Ngục"
                                                }
                                                break;
                                            case 1:
                                                Input.gI().createGiftMember(player);
                                                break;
                                            case 2:
                                                if (TaskService.gI().getIdTask(player) < ConstTask.TASK_34_0) {
                                                    this.createOtherMenu(player, 64746456,
                                                            "Nhiệm vụ hiện tại của bạn là " + player.playerTask.taskMain.name
                                                            + "\n|7|Vui lòng hoàn thành tất cả nhiệm vụ để nhận quà!!!!",
                                                            "Hiểu rồi");
                                                    return;
                                                }
                                                this.createOtherMenu(player, 5701,
                                                        "Bạn đã hoàn thành tất cả nhiệm vụ"
                                                        + "\nTôi sẽ tặng bạn:"
                                                        + "\n|2|99 Phiếu Bé Ngoan"
                                                        + "\n|2|3k " + ConstItem.NAME_XU_SERVER
                                                        + "\n|2|100k Thỏi Vàng "
                                                        + "\n|2|1m Hồng Ngọc",
                                                        ((player.ThuongNhiemVu == 0) ? "(Chưa nhận)" : "(Đã nhận)"));
                                                break;
                                            case 3:
                                                this.createOtherMenu(player, 22222,
                                                        "|7|DANH HIỆU VĨNH VIỄN"
                                                        + "\n|1|Thợ Ngọc : Mua với giá 10Tr Hồng Ngọc"
                                                        + "\n|1|Ai Mà Xinh Thế : Cần 99 Mảnh Ai Mà Xinh Thế"
                                                        + "\n|5|(Cách Nhận : Quay Vòng Quay May Mắn)"
                                                        + "\n|1|Tuổi Thơ : Cần 99 Mảnh Tuổi Thơ"
                                                        + "\n|5|(Cách Nhận : Làm Full 34 Nhiệm Vụ)"
                                                        + "\n|1|Cần Thủ : Cần 999 Mảnh Cần Thủ"
                                                        + "\n|5|(Cách Nhận : Fam Quái Sự Kiện)"
                                                        + "\n|1|Diệt Thần : Cần 999 Mảnh Diệt Thần"
                                                        + "\n|1|(Danh hiệu không tốn ô hành trang)"
                                                        + "\n|1|Lệnh Xem Danh Hiệu : dh",
                                                        "Mua\nThợ Ngọc",
                                                        "Mua\nAi mà Xinh thế",
                                                        "Mua\nTuổi Thơ",
                                                        "Mua\nCần Thủ",
                                                        "Mua\nDiệt Thần");
                                                break;
                                            case 9:
                                                TaskService.gI().checkDoneAchivements(player);
                                                TaskService.gI().sendAchivement(player);
                                                break;
                                        }
                                    } else if (player.iDMark.getIndexMenu() == 5701) {
                                        switch (select) {
                                            case 0:
                                                if (TaskService.gI().getIdTask(player) < ConstTask.TASK_34_0) {
                                                    this.npcChat(player, "|7|Vui lòng hoàn thành tất cả nhiệm vụ chính");
                                                    return;
                                                }
                                                if (player.ThuongNhiemVu != 0) {
                                                    this.npcChat(player, "|7|Bạn đã nhận thưởng rồi mà !!!!");
                                                    return;
                                                }
                                                if (InventoryService.gI().getCountEmptyBag(player) < 3) {
                                                    this.npcChat(player, "Hành trang của bạn không đủ chỗ trống");
                                                    return;
                                                }
                                                if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_34_0 && player.ThuongNhiemVu == 0) {
                                                    player.ThuongNhiemVu++;
                                                    Item bengoan = ItemService.gI().createNewItem((short) 1223, 99);
                                                    Item thoivang = ItemService.gI().createNewItem((short) 1296, 200);
                                                    Item xunro = ItemService.gI().createNewItem((short) 1400, 3500);
                                                    Item ruby = ItemService.gI().createNewItem((short) 861, 1000000);
                                                    InventoryService.gI().addItemBag(player, thoivang, 1);
                                                    InventoryService.gI().addItemBag(player, ruby, 1);
                                                    InventoryService.gI().addItemBag(player, xunro, 1);
                                                    InventoryService.gI().addItemBag(player, bengoan, 1);
                                                    InventoryService.gI().sendItemBags(player);
                                                    this.npcChat(player, "Bạn nhận được 99 Mảnh Danh Hiệu Tuổi Thơ - 200 Máy Dò - 1 triệu Hồng Ngọc - 3k5 " + ConstItem.NAME_XU_SERVER);
                                                }
                                                break;
                                        }
                                    } else if (player.iDMark.getIndexMenu() == 22222) {
                                        switch (select) {
                                            case 0:
                                                if (player.DH_VV_4) {
                                                    Service.getInstance().sendThongBao(player, "Bạn đã nhận rồi mà !!!!!!");
                                                    return;
                                                }
                                                if (player.inventory.ruby < 10_000_000) {
                                                    Service.getInstance().sendThongBao(player, "Bạn chưa đủ 10Tr Hồng ngọc !!!!!!");
                                                    return;
                                                }
                                                player.inventory.ruby -= 10_000_000;
                                                player.isTitleUse4 = true;
                                                player.DH_VV_4 = true;
                                                Service.getInstance().point(player);
                                                Service.getInstance().sendTitle(player, 891);
                                                Service.getInstance().sendMoney(player);
                                                Service.getInstance().sendThongBao(player, "Nhận thành công danh hiệu Thợ Ngọc Vĩnh viễn");
                                                break;
                                            case 1:
                                                Item hoa = InventoryService.gI().findItemBagByTemp(player, 1221);
                                                if (player.DH_VV_5) {
                                                    Service.getInstance().sendThongBao(player, "Bạn đã nhận rồi mà !!!!!!");
                                                    return;
                                                }
                                                if (hoa == null || hoa.quantity < 99) {
                                                    Service.getInstance().sendThongBao(player, "Bạn chưa đủ 99 Mảnh Danh Hiệu Ai Mà Xinh Thế!");
                                                    return;
                                                }
                                                player.isTitleUse5 = true;
                                                player.DH_VV_5 = true;
                                                InventoryService.gI().subQuantityItemsBag(player, hoa, 99);
                                                Service.getInstance().point(player);
                                                Service.getInstance().sendTitle(player, 892);
                                                InventoryService.gI().sendItemBags(player);
                                                Service.getInstance().sendThongBao(player, "Nhận thành công danh hiệu Ai mà Xinh Thế Vĩnh viễn");
                                                break;
                                            case 2:
                                                Item phieu = InventoryService.gI().findItemBagByTemp(player, 1223);
                                                if (player.DH_VV_3) {
                                                    Service.getInstance().sendThongBao(player, "Bạn đã nhận rồi mà !!!!!!");
                                                    return;
                                                }
                                                if (phieu == null || phieu.quantity < 99) {
                                                    Service.getInstance().sendThongBao(player, "Bạn chưa đủ 99 Mảnh Danh Hiệu Tuổi Thơ!");
                                                    return;
                                                }
                                                player.isTitleUse3 = true;
                                                player.DH_VV_3 = true;
                                                InventoryService.gI().subQuantityItemsBag(player, phieu, 99);
                                                Service.getInstance().point(player);
                                                Service.getInstance().sendTitle(player, 890);
                                                InventoryService.gI().sendItemBags(player);
                                                Service.getInstance().sendThongBao(player, "Nhận thành công danh hiệu Tuổi Thơ Vĩnh viễn");
                                                break;
                                            case 3:
                                                Item xoCaVang = InventoryService.gI().findItemBagByTemp(player, 1222);
                                                if (player.DH_VV_2) {
                                                    Service.getInstance().sendThongBao(player, "Bạn đã nhận rồi mà !!!!!!");
                                                    return;
                                                }
                                                if (xoCaVang == null || xoCaVang.quantity < 999) {
                                                    this.npcChat(player, "Bạn chưa đủ 999 Mảnh Danh Hiệu Cần Thủ");
                                                    return;
                                                }
                                                player.isTitleUse2 = true;
                                                player.DH_VV_2 = true;
                                                InventoryService.gI().subQuantityItemsBag(player, xoCaVang, 999);
                                                Service.getInstance().point(player);
                                                Service.getInstance().sendTitle(player, 889);
                                                Service.getInstance().sendMoney(player);
                                                InventoryService.gI().sendItemBags(player);
                                                Service.getInstance().sendThongBao(player, "Nhận thành công danh hiệu Cần Thủ Vĩnh viễn");
                                                break;
                                            case 4:
                                                Item manhDietThan = InventoryService.gI().findItemBagByTemp(player, 1913);
                                                if (player.DH_VV_6) {
                                                    Service.getInstance().sendThongBao(player, "Bạn đã nhận rồi mà !!!!!!");
                                                    return;
                                                }
                                                if (manhDietThan == null || manhDietThan.quantity < 999) {
                                                    this.npcChat(player, "Bạn chưa đủ 999 Mảnh Diệt Thần");
                                                    return;
                                                }
                                                player.isTitleUse6 = true;
                                                player.DH_VV_6 = true;
                                                InventoryService.gI().subQuantityItemsBag(player, manhDietThan, 999);
                                                Service.getInstance().point(player);
                                                for (int i = 893; i <= 897; i++) {
                                                    Service.getInstance().sendTitle(player, i);
                                                }
                                                Service.getInstance().removeTitle(player);
                                                Service.getInstance().sendMoney(player);
                                                InventoryService.gI().sendItemBags(player);
                                                Service.getInstance().sendThongBao(player, "Nhận thành công danh hiệu Diệt Thần Vĩnh viễn");
                                                break;
                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_LEVEL_SIDE_TASK) {
                                        switch (select) {
                                            case 0:
                                            case 1:
                                            case 2:
//                                            case 3:
                                                TaskService.gI().changeSideTask(player, (byte) select);
                                                break;
                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_OPTION_PAY_SIDE_TASK) {
                                        switch (select) {
                                            case 0:
                                                TaskService.gI().paySideTask(player);
                                                break;
                                            case 1:
                                                TaskService.gI().removeSideTask(player);
                                                break;
                                        }
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.GOKU_SSJ:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 80) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                            "Xin chào, tôi có thể giúp gì cho cậu?", "Tới hành tinh\nYardart",
                                            "Từ chối");
                                } else if (this.mapId == 131) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                            "Xin chào, tôi có thể giúp gì cho cậu?", "Quay về", "Từ chối");
                                } else {
                                    super.openBaseMenu(player);
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                switch (player.iDMark.getIndexMenu()) {
                                    case ConstNpc.BASE_MENU:
                                        if (this.mapId == 80) {
                                            // if (select == 0) {
                                            // if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_24_0) {
                                            // ChangeMapService.gI().changeMapBySpaceShip(player, 160, -1, 168);
                                            // } else {
                                            // this.npcChat(player, "Xin lỗi, tôi chưa thể đưa cậu tới nơi đó lúc
                                            // này...");
                                            // }
                                            // } else
                                            if (select == 0) {
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 131, -1, 940);
                                            }
                                        } else if (this.mapId == 131) {
                                            if (select == 0) {
                                                ChangeMapService.gI().changeMapBySpaceShip(player, 80, -1, 870);
                                            }
                                        }
                                        break;
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.GOKU_SSJ_:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 133) {
                                    Item biKiep = InventoryService.gI().findItem(player.inventory.itemsBag, 590);
                                    int soLuong = 0;
                                    if (biKiep != null) {
                                        soLuong = biKiep.quantity;
                                    }
                                    if (soLuong >= 10000) {
                                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Bạn đang có " + soLuong
                                                + " bí kiếp.\n"
                                                + "Hãy kiếm đủ 10000 bí kiếp tôi sẽ dạy bạn cách dịch chuyển tức thời của người Yardart",
                                                "Học dịch\nchuyển", "Đóng");
                                    } else {
                                        this.createOtherMenu(player, ConstNpc.BASE_MENU, "Bạn đang có " + soLuong
                                                + " bí kiếp.\n"
                                                + "Hãy kiếm đủ 10000 bí kiếp tôi sẽ dạy bạn cách dịch chuyển tức thời của người Yardart",
                                                "Đóng");
                                    }
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 133) {
                                    Item biKiep = InventoryService.gI().findItem(player.inventory.itemsBag, 590);
                                    int soLuong = 0;
                                    if (biKiep != null) {
                                        soLuong = biKiep.quantity;
                                    }
                                    if (soLuong >= 10000 && InventoryService.gI().getCountEmptyBag(player) > 0) {
                                        Item yardart = ItemService.gI().createNewItem((short) (player.gender + 592));
                                        yardart.itemOptions.add(new ItemOption(47, 1000));
                                        yardart.itemOptions.add(new ItemOption(50, 50));
                                        yardart.itemOptions.add(new ItemOption(77, 50));
                                        yardart.itemOptions.add(new ItemOption(103, 50));
                                        yardart.itemOptions.add(new ItemOption(101, 1000));
                                        yardart.itemOptions.add(new ItemOption(108, 10));
                                        yardart.itemOptions.add(new ItemOption(30, 1));
                                        InventoryService.gI().addItemBag(player, yardart, 0);
                                        InventoryService.gI().subQuantityItemsBag(player, biKiep, 10000);
                                        InventoryService.gI().sendItemBags(player);
                                        Service.getInstance().sendThongBao(player,
                                                "Bạn vừa nhận được trang phục tộc Yardart");
                                    }
                                }
                            }
                        }
                    };
                    break;

                case ConstNpc.GHI_DANH:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        String[] menuselect = new String[]{};

                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == ConstMap.DAI_HOI_VO_THUAT) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                            "Chào mừng bạn đến với đại hội võ thuật", "Đại Hội\nVõ Thuật\nLần Thứ\n23", "Giải siêu hạng");
                                } else if (this.mapId == ConstMap.DAI_HOI_VO_THUAT_129) {
                                    int goldchallenge = player.goldChallenge;
                                    if (player.levelWoodChest == 0) {
                                        menuselect = new String[]{
                                            "Thi đấu\n" + goldchallenge + " Xu",
                                            "Về\nĐại Hội\nVõ Thuật"};
                                    } else {
                                        menuselect = new String[]{
                                            "Thi đấu\n" + goldchallenge + " xu",
                                            "Nhận thưởng\nRương cấp\n" + player.levelWoodChest,
                                            "Về\nĐại Hội\nVõ Thuật"};
                                    }
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                            "Đại hội võ thuật lần thứ 23\nDiễn ra bất kể ngày đêm,ngày nghỉ ngày lễ\nPhần thưởng vô cùng quý giá\nNhanh chóng tham gia nào",
                                            menuselect, "Từ chối");
                                } else {
                                    super.openBaseMenu(player);
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                switch (player.iDMark.getIndexMenu()) {
                                    case ConstNpc.BASE_MENU:
                                        if (this.mapId == ConstMap.DAI_HOI_VO_THUAT) {
                                            switch (select) {
                                                case 0:
                                                    ChangeMapService.gI().changeMapNonSpaceship(player,
                                                            ConstMap.DAI_HOI_VO_THUAT_129, player.location.x, 360);
                                                    break;
                                                case 1:
                                                    ChangeMapService.gI().changeMapNonSpaceship(player, 113, player.location.x, 360);
                                                    break;
                                            }
                                        } else if (this.mapId == ConstMap.DAI_HOI_VO_THUAT_129) {
                                            int goldchallenge = player.goldChallenge;
                                            if (player.levelWoodChest == 0) {
                                                switch (select) {
                                                    case 0:
                                                        if (InventoryService.gI().finditemWoodChest(player)) {
                                                            Item itemxu = InventoryService.gI().findItem(player, 1400, goldchallenge);
                                                            if (itemxu != null && itemxu.quantity >= goldchallenge) {
                                                                MartialCongressService.gI().startChallenge(player);
                                                                InventoryService.gI().subQuantityItemsBag(player, itemxu, goldchallenge);
                                                                InventoryService.gI().sendItemBags(player);
                                                                PlayerService.gI().sendInfoHpMpMoney(player);
                                                                player.goldChallenge += 20;
                                                            } else {
                                                                Service.getInstance().sendThongBao(player,
                                                                        "Không đủ xu");
                                                            }
                                                        } else {
                                                            Service.getInstance().sendThongBao(player,
                                                                    "Hãy mở rương báu vật trước");
                                                        }
                                                        break;
                                                    case 1:
                                                        ChangeMapService.gI().changeMapNonSpaceship(player,
                                                                ConstMap.DAI_HOI_VO_THUAT, player.location.x, 336);
                                                        break;
                                                }
                                            } else {
                                                switch (select) {
                                                    case 0:
                                                        if (InventoryService.gI().finditemWoodChest(player)) {
                                                            Item itemxu = InventoryService.gI().findItem(player, 1400, goldchallenge);
                                                            if (itemxu != null && itemxu.quantity >= goldchallenge) {
                                                                MartialCongressService.gI().startChallenge(player);
                                                                InventoryService.gI().subQuantityItemsBag(player, itemxu, goldchallenge);
                                                                InventoryService.gI().sendItemBags(player);
                                                                PlayerService.gI().sendInfoHpMpMoney(player);
                                                                player.goldChallenge += 20;
                                                            } else {
                                                                Service.getInstance().sendThongBao(player,
                                                                        "Không đủ xu");
                                                            }
                                                        } else {
                                                            Service.getInstance().sendThongBao(player,
                                                                    "Hãy mở rương báu vật trước");
                                                        }
                                                        break;
                                                    case 1:
                                                        if (!player.receivedWoodChest) {
                                                            if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                                                                Item it = ItemService.gI()
                                                                        .createNewItem((short) ConstItem.RUONG_GO);
                                                                it.itemOptions
                                                                        .add(new ItemOption(72, player.levelWoodChest));
                                                                it.itemOptions.add(new ItemOption(30, 0));
                                                                it.createTime = System.currentTimeMillis();
                                                                InventoryService.gI().addItemBag(player, it, 0);
                                                                InventoryService.gI().sendItemBags(player);

                                                                player.receivedWoodChest = true;
                                                                player.levelWoodChest = 0;
                                                                Service.getInstance().sendThongBao(player,
                                                                        "Bạn nhận được rương gỗ");
                                                            } else {
                                                                this.npcChat(player, "Hành trang đã đầy");
                                                            }
                                                        } else {
                                                            Service.getInstance().sendThongBao(player,
                                                                    "Mỗi ngày chỉ có thể nhận rương báu 1 lần");
                                                        }
                                                        break;
                                                    case 2:
                                                        ChangeMapService.gI().changeMapNonSpaceship(player,
                                                                ConstMap.DAI_HOI_VO_THUAT, player.location.x, 336);
                                                        break;
                                                }
                                            }
                                        }
                                        break;
                                }
                            }
                        }
                    };
                    break;

                case ConstNpc.POC_MUA_HE:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                this.createOtherMenu(player, ConstNpc.BASE_MENU, "Xin chào, sự kiện Tháng 8 đang được diễn ra"
                                        + "\nChúc Anh Em chơi sự kiện vui vẻ"
                                        + "\n|5|-Rương Sự kiện Thường: x49 Vỏ ốc, Vỏ sò, Con cua, Sao biển các loại và phí đổi rương 2 Thỏi vàng"
                                        + "\n-Rương Sự kiện VIP:  x99 Vỏ ốc, Vỏ sò, Con cua, Sao biển các loại và phí đổi rương 5 Thỏi vàng",
                                        "Rương sự kiện\nThường", "Rương sự kiện\nVIP", "Shop sự kiện");
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                switch (player.iDMark.getIndexMenu()) {
                                    case ConstNpc.BASE_MENU:
                                        switch (select) {
                                            case 0:
                                                Item vooc = InventoryService.gI().findItem(player, ConstItem.VO_OC, 49);
                                                Item voso = InventoryService.gI().findItem(player, ConstItem.VO_SO, 49);
                                                Item concua = InventoryService.gI().findItem(player, ConstItem.CON_CUA, 49);
                                                Item saobien = InventoryService.gI().findItem(player, ConstItem.SAO_BIEN, 49);
                                                Item tvang = InventoryService.gI().findItem(player, ConstItem.THOI_VANG, 2);
                                                if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                                                    if (vooc != null && voso != null && concua != null && saobien != null && tvang != null) {
                                                        InventoryService.gI().subQuantityItemsBag(player, vooc, 49);
                                                        InventoryService.gI().subQuantityItemsBag(player, voso, 49);
                                                        InventoryService.gI().subQuantityItemsBag(player, concua, 49);
                                                        InventoryService.gI().subQuantityItemsBag(player, saobien, 49);
                                                        InventoryService.gI().subQuantityItemsBag(player, tvang, 2);

                                                        Item ruongthuong = ItemService.gI().createNewItem((short) 1578);
                                                        ruongthuong.itemOptions.add(new ItemOption(74, 0));
                                                        InventoryService.gI().addItemBag(player, ruongthuong, 0);
                                                        InventoryService.gI().sendItemBags(player);
                                                        Service.getInstance().sendThongBao(player,
                                                                "Bạn nhận được " + ruongthuong.template.name);
                                                    } else {
                                                        Service.getInstance().sendThongBao(player, "Không đủ nguyên liệu");
                                                    }
                                                } else {
                                                    Service.getInstance().sendThongBao(player, "Hành trang đầy");
                                                }
                                                break;
                                            case 1:
                                                Item vooc1 = InventoryService.gI().findItem(player, ConstItem.VO_OC, 99);
                                                Item voso1 = InventoryService.gI().findItem(player, ConstItem.VO_SO, 99);
                                                Item concua1 = InventoryService.gI().findItem(player, ConstItem.CON_CUA, 99);
                                                Item saobien1 = InventoryService.gI().findItem(player, ConstItem.SAO_BIEN, 99);
                                                Item tvang1 = InventoryService.gI().findItem(player, ConstItem.THOI_VANG, 5);
                                                if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                                                    if (vooc1 != null && voso1 != null && concua1 != null && saobien1 != null && tvang1 != null) {
                                                        InventoryService.gI().subQuantityItemsBag(player, vooc1, 99);
                                                        InventoryService.gI().subQuantityItemsBag(player, voso1, 99);
                                                        InventoryService.gI().subQuantityItemsBag(player, concua1, 99);
                                                        InventoryService.gI().subQuantityItemsBag(player, saobien1, 99);
                                                        InventoryService.gI().subQuantityItemsBag(player, tvang1, 5);

                                                        Item ruongthuong = ItemService.gI().createNewItem((short) 1579);
                                                        ruongthuong.itemOptions.add(new ItemOption(74, 0));
                                                        InventoryService.gI().addItemBag(player, ruongthuong, 0);
                                                        InventoryService.gI().sendItemBags(player);
                                                        Service.getInstance().sendThongBao(player,
                                                                "Bạn nhận được " + ruongthuong.template.name);
                                                    } else {
                                                        Service.getInstance().sendThongBao(player, "Không đủ nguyên liệu");
                                                    }
                                                } else {
                                                    Service.getInstance().sendThongBao(player, "Hành trang đầy");
                                                }
                                                break;
                                            case 2: //Shop sự kiện
                                                ShopService.gI().openShopSpecial(player, this, ConstNpc.SHOP_POC_MUA_HE, 0, -1);
                                                break;
                                        }
                                        break;
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.ANDROID_AODAI:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                        "|7|Chúc mừng năm mới " + player.name
                                        + "\n|6|Sự kiện Tết 2025 đang diễn ra, hãy thu thập các vật phẩm để nhận những phần thưởng hấp dẫn sau"
                                        + "\n|5|-100 Thiệp Chúc Tết + 99 chữ (Vạn Sự Như Ý 2025) : 10.000 Thỏi vàng"
                                        + "\n-500 Thiệp Chúc Tết + 500 Chữ (Vạn Sự Như Ý 2025) + 1.000 " + ConstItem.NAME_XU_SERVER + ": Danh hiệu Đệ tử 'Đại Thần' 30% Chỉ số"
                                        + "\n-1.000 Thiệp Chúc Tết + 1.000 Chữ (Vạn Sự Như Ý 2025) + 2.000 " + ConstItem.NAME_XU_SERVER + ": Thú cưỡi 'Cá Vàng' 100% Chỉ số",
                                        //               + "\nCHÚC AE NGỌC RỒNG TITAN NĂM MỚI VUI VẺ...",
                                        "Đổi\nVật phẩm");
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                switch (player.iDMark.getIndexMenu()) {
                                    case ConstNpc.BASE_MENU:
                                        switch (select) {
                                            //         case 0:
                                            //              ShopService.gI().openShopSpecial(player, this, ConstNpc.SHOP_TET_2025, 0, -1);
                                            //              break;
                                            case 0:
                                                this.createOtherMenu(player, 22,
                                                        "|5|-100 Thiệp Chúc Tết + 99 chữ (Vạn Sự Như Ý 2025) : 10.000 Thỏi vàng"
                                                        + "\n-500 Thiệp Chúc Tết + 500 Chữ (Vạn Sự Như Ý 2025) + 1.000 " + ConstItem.NAME_XU_SERVER + ": Danh hiệu Đệ tử 'Đại Thần' 30% Chỉ số"
                                                        + "\n-1.000 Thiệp Chúc Tết + 1.000 Chữ (Vạn Sự Như Ý 2025) + 2.000 " + ConstItem.NAME_XU_SERVER + " : Thú cưỡi 'Cá Vàng' 100% Chỉ số",
                                                        "10.000\nThỏi vàng", "Đại Thần", "Cá Vàng");
                                                break;
                                        }
                                        break;
                                    case 22:
                                        switch (select) {
                                            case 0: {
                                                Item chuvanb = InventoryService.gI().findItem(player, ConstItem.CHU_VAN, 99);
                                                Item chusub = InventoryService.gI().findItem(player, ConstItem.CHU_SU, 99);
                                                Item chunhub = InventoryService.gI().findItem(player, ConstItem.CHU_NHU, 99);
                                                Item chuyb = InventoryService.gI().findItem(player, ConstItem.CHU_Y, 99);
                                                Item chu2025 = InventoryService.gI().findItem(player, ConstItem.CHU_2025, 99);
                                                Item thiepb = InventoryService.gI().findItem(player, ConstItem.THIEP_CHUC_TET_2024, 100);
                                                if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                                                    if (chuvanb != null && chusub != null && chunhub != null && chuyb != null && chu2025 != null && thiepb != null) {
                                                        InventoryService.gI().subQuantityItemsBag(player, chuvanb, 99);
                                                        InventoryService.gI().subQuantityItemsBag(player, chusub, 99);
                                                        InventoryService.gI().subQuantityItemsBag(player, chunhub, 99);
                                                        InventoryService.gI().subQuantityItemsBag(player, chuyb, 99);
                                                        InventoryService.gI().subQuantityItemsBag(player, chu2025, 99);
                                                        InventoryService.gI().subQuantityItemsBag(player, thiepb, 100);

                                                        Item thoivang = ItemService.gI().createNewItem((short) 457, 10000);
                                                        InventoryService.gI().addItemBag(player, thoivang, 0);
                                                        InventoryService.gI().sendItemBags(player);
                                                        Service.getInstance().sendThongBao(player, "Bạn nhận được 10.000 " + thoivang.template.name);
                                                    } else {
                                                        Service.getInstance().sendThongBao(player, "Không đủ nguyên liệu");
                                                    }
                                                } else {
                                                    Service.getInstance().sendThongBao(player, "Hành trang đầy.");
                                                }
                                                break;
                                            }
                                            case 1: {
                                                Item chuvanb = InventoryService.gI().findItem(player, ConstItem.CHU_VAN, 500);
                                                Item chusub = InventoryService.gI().findItem(player, ConstItem.CHU_SU, 500);
                                                Item chunhub = InventoryService.gI().findItem(player, ConstItem.CHU_NHU, 500);
                                                Item chuyb = InventoryService.gI().findItem(player, ConstItem.CHU_Y, 500);
                                                Item chu2025 = InventoryService.gI().findItem(player, ConstItem.CHU_2025, 500);
                                                Item thiepb = InventoryService.gI().findItem(player, ConstItem.THIEP_CHUC_TET_2024, 500);
                                                Item daTitan = InventoryService.gI().findItem(player, 1400, 1000);
                                                if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                                                    if (chuvanb != null && chusub != null && chunhub != null && chuyb != null && chu2025 != null && thiepb != null && daTitan != null) {
                                                        InventoryService.gI().subQuantityItemsBag(player, chuvanb, 500);
                                                        InventoryService.gI().subQuantityItemsBag(player, chusub, 500);
                                                        InventoryService.gI().subQuantityItemsBag(player, chunhub, 500);
                                                        InventoryService.gI().subQuantityItemsBag(player, chuyb, 500);
                                                        InventoryService.gI().subQuantityItemsBag(player, chu2025, 500);
                                                        InventoryService.gI().subQuantityItemsBag(player, thiepb, 500);
                                                        InventoryService.gI().subQuantityItemsBag(player, daTitan, 1000);

                                                        Item danhhieu = ItemService.gI().createNewItem((short) 1325, 1);
                                                        danhhieu.itemOptions.add(new ItemOption(50, 30));
                                                        danhhieu.itemOptions.add(new ItemOption(77, 40));
                                                        danhhieu.itemOptions.add(new ItemOption(103, 40));
                                                        danhhieu.itemOptions.add(new ItemOption(101, 500));
                                                        danhhieu.itemOptions.add(new ItemOption(5, 10));
                                                        danhhieu.itemOptions.add(new ItemOption(95, 10));
                                                        danhhieu.itemOptions.add(new ItemOption(96, 10));
                                                        danhhieu.itemOptions.add(new ItemOption(197, 1));
                                                        danhhieu.itemOptions.add(new ItemOption(30, 1));

                                                        InventoryService.gI().addItemBag(player, danhhieu, 0);
                                                        InventoryService.gI().sendItemBags(player);
                                                        Service.getInstance().sendThongBao(player, "Bạn nhận được " + danhhieu.template.name);
                                                    } else {
                                                        Service.getInstance().sendThongBao(player, "Không đủ nguyên liệu");
                                                    }
                                                } else {
                                                    Service.getInstance().sendThongBao(player, "Hành trang đầy.");
                                                }
                                                break;
                                            }
                                            case 2: {
                                                Item chuvanb = InventoryService.gI().findItem(player, ConstItem.CHU_VAN, 1000);
                                                Item chusub = InventoryService.gI().findItem(player, ConstItem.CHU_SU, 1000);
                                                Item chunhub = InventoryService.gI().findItem(player, ConstItem.CHU_NHU, 1000);
                                                Item chuyb = InventoryService.gI().findItem(player, ConstItem.CHU_Y, 1000);
                                                Item chu2025 = InventoryService.gI().findItem(player, ConstItem.CHU_2025, 1000);
                                                Item thiepb = InventoryService.gI().findItem(player, ConstItem.THIEP_CHUC_TET_2024, 1000);
                                                Item daTitan = InventoryService.gI().findItem(player, 1400, 2000);
                                                if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                                                    if (chuvanb != null && chusub != null && chunhub != null && chuyb != null && chu2025 != null && thiepb != null && daTitan != null) {
                                                        InventoryService.gI().subQuantityItemsBag(player, chuvanb, 1000);
                                                        InventoryService.gI().subQuantityItemsBag(player, chusub, 1000);
                                                        InventoryService.gI().subQuantityItemsBag(player, chunhub, 1000);
                                                        InventoryService.gI().subQuantityItemsBag(player, chuyb, 1000);
                                                        InventoryService.gI().subQuantityItemsBag(player, chu2025, 1000);
                                                        InventoryService.gI().subQuantityItemsBag(player, thiepb, 1000);
                                                        InventoryService.gI().subQuantityItemsBag(player, daTitan, 2000);

                                                        Item caVang = ItemService.gI().createNewItem((short) 1663, 1);
                                                        caVang.itemOptions.add(new ItemOption(50, 50));
                                                        caVang.itemOptions.add(new ItemOption(77, 50));
                                                        caVang.itemOptions.add(new ItemOption(103, 50));
                                                        caVang.itemOptions.add(new ItemOption(101, 1000));
                                                        caVang.itemOptions.add(new ItemOption(5, 10));
                                                        caVang.itemOptions.add(new ItemOption(95, 20));
                                                        caVang.itemOptions.add(new ItemOption(96, 20));
                                                        caVang.itemOptions.add(new ItemOption(197, 1));
                                                        caVang.itemOptions.add(new ItemOption(30, 1));

                                                        InventoryService.gI().addItemBag(player, caVang, 0);
                                                        InventoryService.gI().sendItemBags(player);
                                                        Service.getInstance().sendThongBao(player, "Bạn nhận được " + caVang.template.name);
                                                    } else {
                                                        Service.getInstance().sendThongBao(player, "Không đủ nguyên liệu");
                                                    }
                                                } else {
                                                    Service.getInstance().sendThongBao(player, "Hành trang đầy.");
                                                }
                                                break;
                                            }
                                        }
                                        break;
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.NOI_BANH:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                        "Xin chào " + player.name + "\nTôi là nồi nấu bánh\nTôi có thể giúp gì cho bạn"
                                        + "\n|5|-Làm Bánh Tét: (99)Thịt ba chỉ, Gạo nếp, Đỗ xanh, Lá chuối"
                                        + "\n-Làm Bánh Chưng: (99)Thịt Heo, Gạo nếp, Đỗ xanh, Lá dong"
                                        + "\n-Nấu Bánh Tét: (1)Bánh tét, Phụ gia tạo màu, Gia vị tổng hộp"
                                        + "\n-Nấu Bánh Chưng: (1)Bánh chưng, Phụ gia tạo màu, Gia vị tổng hộp"
                                        + "\n-Đổi Hộp quà: (5)Bánh chưng Chín, Bánh tét Chín"
                                        + "\n|3|(Làm bánh Thành công sẽ nhận 1 Điểm Sự kiện)",
                                        "Làm\nBánh Tét", "Làm\nBánh Chưng", getMenuLamBanh(player, 0),
                                        getMenuLamBanh(player, 1), "Đổi Hộp\nQuà Tết");
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                switch (player.iDMark.getIndexMenu()) {
                                    case ConstNpc.BASE_MENU:
                                        switch (select) {
                                            case 0:
                                                Item thitBaChi = InventoryService.gI().findItem(player,
                                                        ConstItem.THIT_BA_CHI, 99);
                                                Item gaoNep = InventoryService.gI().findItem(player, ConstItem.GAO_NEP,
                                                        99);
                                                Item doXanh = InventoryService.gI().findItem(player, ConstItem.DO_XANH,
                                                        99);
                                                Item laChuoi = InventoryService.gI().findItem(player,
                                                        ConstItem.LA_CHUOI, 99);
                                                if (thitBaChi != null && gaoNep != null && doXanh != null
                                                        && laChuoi != null) {
                                                    InventoryService.gI().subQuantityItemsBag(player, thitBaChi, 99);
                                                    InventoryService.gI().subQuantityItemsBag(player, gaoNep, 99);
                                                    InventoryService.gI().subQuantityItemsBag(player, doXanh, 99);
                                                    InventoryService.gI().subQuantityItemsBag(player, laChuoi, 99);
                                                    Item banhtet = ItemService.gI()
                                                            .createNewItem((short) ConstItem.BANH_TET_2023);
                                                    banhtet.itemOptions.add(new ItemOption(74, 0));
                                                    InventoryService.gI().addItemBag(player, banhtet, 0);
                                                    InventoryService.gI().sendItemBags(player);
                                                    Service.getInstance().sendThongBao(player,
                                                            "Bạn nhận được Bánh Tét");
                                                } else {
                                                    Service.getInstance().sendThongBao(player, "Không đủ nguyên liệu");
                                                }
                                                break;
                                            case 1:
                                                Item thitHeo1 = InventoryService.gI().findItem(player,
                                                        ConstItem.THIT_HEO_2023, 99);
                                                Item gaoNep1 = InventoryService.gI().findItem(player, ConstItem.GAO_NEP,
                                                        99);
                                                Item doXanh1 = InventoryService.gI().findItem(player, ConstItem.DO_XANH,
                                                        99);
                                                Item laDong1 = InventoryService.gI().findItem(player,
                                                        ConstItem.LA_DONG_2023, 99);
                                                if (thitHeo1 != null && gaoNep1 != null && doXanh1 != null
                                                        && laDong1 != null) {
                                                    InventoryService.gI().subQuantityItemsBag(player, thitHeo1, 99);
                                                    InventoryService.gI().subQuantityItemsBag(player, gaoNep1, 99);
                                                    InventoryService.gI().subQuantityItemsBag(player, doXanh1, 99);
                                                    InventoryService.gI().subQuantityItemsBag(player, laDong1, 99);
                                                    Item banhChung = ItemService.gI()
                                                            .createNewItem((short) ConstItem.BANH_CHUNG_2023);
                                                    banhChung.itemOptions.add(new ItemOption(74, 0));
                                                    InventoryService.gI().addItemBag(player, banhChung, 0);
                                                    InventoryService.gI().sendItemBags(player);
                                                    Service.getInstance().sendThongBao(player,
                                                            "Bạn nhận được Bánh Chưng");
                                                } else {
                                                    Service.getInstance().sendThongBao(player, "Không đủ nguyên liệu");
                                                }
                                                break;
                                            case 2:
                                                if (!player.event.isCookingTetCake()) {
                                                    Item banhTet2 = InventoryService.gI().findItem(player,
                                                            ConstItem.BANH_TET_2023, 1);
                                                    Item phuGiaTaoMau2 = InventoryService.gI().findItem(player,
                                                            ConstItem.PHU_GIA_TAO_MAU, 1);
                                                    Item giaVi2 = InventoryService.gI().findItem(player,
                                                            ConstItem.GIA_VI_TONG_HOP, 1);

                                                    if (banhTet2 != null && phuGiaTaoMau2 != null && giaVi2 != null) {
                                                        InventoryService.gI().subQuantityItemsBag(player, banhTet2, 1);
                                                        InventoryService.gI().subQuantityItemsBag(player, phuGiaTaoMau2,
                                                                1);
                                                        InventoryService.gI().subQuantityItemsBag(player, giaVi2, 1);
                                                        InventoryService.gI().sendItemBags(player);
                                                        player.event.setTimeCookTetCake(300);
                                                        player.event.setCookingTetCake(true);
                                                        Service.getInstance().sendThongBao(player,
                                                                "Bắt đầu nấu bánh,thời gian nấu bánh là 5 phút");
                                                    } else {
                                                        Service.getInstance().sendThongBao(player,
                                                                "Không đủ nguyên liệu");
                                                    }
                                                } else if (player.event.isCookingTetCake()
                                                        && player.event.getTimeCookTetCake() == 0) {
                                                    Item cake = ItemService.gI()
                                                            .createNewItem((short) ConstItem.BANH_TET_CHIN, 1);
                                                    cake.itemOptions.add(new ItemOption(77, 20));
                                                    cake.itemOptions.add(new ItemOption(103, 20));
                                                    cake.itemOptions.add(new ItemOption(74, 0));
                                                    InventoryService.gI().addItemBag(player, cake, 0);
                                                    InventoryService.gI().sendItemBags(player);
                                                    player.event.setCookingTetCake(false);
                                                    player.event.addEventPoint(1);
                                                    Service.getInstance().sendThongBao(player,
                                                            "Bạn nhận được Bánh Tét (đã chính) và 1 điểm sự kiện");
                                                }
                                                break;
                                            case 3:
                                                if (!player.event.isCookingChungCake()) {
                                                    Item banhChung3 = InventoryService.gI().findItem(player,
                                                            ConstItem.BANH_CHUNG_2023, 1);
                                                    Item phuGiaTaoMau3 = InventoryService.gI().findItem(player,
                                                            ConstItem.PHU_GIA_TAO_MAU, 1);
                                                    Item giaVi3 = InventoryService.gI().findItem(player,
                                                            ConstItem.GIA_VI_TONG_HOP, 1);

                                                    if (banhChung3 != null && phuGiaTaoMau3 != null && giaVi3 != null) {
                                                        InventoryService.gI().subQuantityItemsBag(player, banhChung3,
                                                                1);
                                                        InventoryService.gI().subQuantityItemsBag(player, phuGiaTaoMau3,
                                                                1);
                                                        InventoryService.gI().subQuantityItemsBag(player, giaVi3, 1);
                                                        InventoryService.gI().sendItemBags(player);
                                                        player.event.setTimeCookChungCake(300);
                                                        player.event.setCookingChungCake(true);
                                                        Service.getInstance().sendThongBao(player,
                                                                "Bắt đầu nấu bánh,thời gian nấu bánh là 5 phút");
                                                    } else {
                                                        Service.getInstance().sendThongBao(player,
                                                                "Không đủ nguyên liệu");
                                                    }
                                                } else if (player.event.isCookingChungCake()
                                                        && player.event.getTimeCookChungCake() == 0) {
                                                    Item cake = ItemService.gI()
                                                            .createNewItem((short) ConstItem.BANH_CHUNG_CHIN, 1);
                                                    cake.itemOptions.add(new ItemOption(50, 20));
                                                    cake.itemOptions.add(new ItemOption(5, 15));
                                                    cake.itemOptions.add(new ItemOption(74, 0));
                                                    InventoryService.gI().addItemBag(player, cake, 0);
                                                    InventoryService.gI().sendItemBags(player);
                                                    player.event.setCookingChungCake(false);
                                                    player.event.addEventPoint(1);
                                                    Service.getInstance().sendThongBao(player,
                                                            "Bạn nhận được Bánh Chưng (đã chín) và 1 điểm sự kiện");
                                                }
                                                break;
                                            case 4:
                                                Item tetCake = InventoryService.gI().findItem(player,
                                                        ConstItem.BANH_TET_CHIN, 5);
                                                Item chungCake = InventoryService.gI().findItem(player,
                                                        ConstItem.BANH_CHUNG_CHIN, 5);
                                                if (chungCake != null && tetCake != null) {
                                                    Item hopQua = ItemService.gI()
                                                            .createNewItem((short) ConstItem.HOP_QUA_TET_2023, 1);
                                                    hopQua.itemOptions.add(new ItemOption(30, 0));
                                                    hopQua.itemOptions.add(new ItemOption(74, 0));

                                                    InventoryService.gI().subQuantityItemsBag(player, tetCake, 5);
                                                    InventoryService.gI().subQuantityItemsBag(player, chungCake, 5);
                                                    InventoryService.gI().addItemBag(player, hopQua, 0);
                                                    InventoryService.gI().sendItemBags(player);
                                                    Service.getInstance().sendThongBao(player,
                                                            "Bạn nhận được Hộp quà tết");
                                                } else {
                                                    Service.getInstance().sendThongBao(player,
                                                            "Không đủ nguyên liệu để đổi");
                                                }
                                                break;
                                        }
                                        break;
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.CUA_HANG_KY_GUI:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                        "Của hàng chúng tôi chuyên bán hàng hiệu,hàng độc,nếu bạn không chê thì mại đzô",
                                        "Không có\nHướng dẫn", "Mua bán", "Danh sách\nHết Hạn", "Hủy");
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                switch (player.iDMark.getIndexMenu()) {
                                    case ConstNpc.BASE_MENU:
                                        switch (select) {
                                            case 1:
                                                if (!Manager.gI().getGameConfig().isOpenSuperMarket()) {
                                                    Service.getInstance().sendThongBao(player,
                                                            "Chức năng kí gửi chưa mở,vui lòng quay lại sau");
                                                    return;
                                                }
                                                ConsignmentShop.getInstance().show(player);
                                                break;
                                            case 2:
                                                ConsignmentShop.getInstance().showExpiringItems(player);
                                                break;
                                        }
                                        break;
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.CAU_CA:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 5) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                            "|7|ĐẾN KHU VỰC CÂU CÁ"
                                            + "\n\n|5|Nơi đây bạn sẽ dùng những Cần câu chuyên dụng để câu những loại cá quý hiếm khác nhau và đổi được các Vật phẩm siêu giá trị"
                                            + "\n|3|Yêu cầu để đến được nơi này khi Sức mạnh của bạn trên 100 Tỷ"
                                            + "\n|1|Bạn có chắc muốn đến Khu câu cá ?",
                                            "Khu\nCâu cá");
                                } else if (this.mapId == 174) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU, "|7|NÔNG DÂN"
                                            + "\n\n|5|Nơi đây có bán Cần câu, Mồi cho cá và Shop đổi cá"
                                            + "\n|3|Đổi Xô cá Xanh cần 4 loại Cá Thường khác nhau"
                                            + "\nĐổi Xô cá Vàng cần 4 loại Cá VIP khác nhau"
                                            + "\n|2|Đổi 1 xô cá xanh sẽ nhận được 10 Điểm câu cá"
                                            + "\n|2|Đổi 1 Xô cá vàng sẽ nhận được 20 Điểm câu cá",
                                            "Shop\nCần câu",
                                            "Shop\nQuy đổi",
                                            "Đổi\nXô cá Xanh",
                                            "Đổi\nXô cá Vàng",
                                            "Quay về");
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 5) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                if (player.isPl() && player.getSession().player.nPoint.power >= 100000000000L) {
                                                    ChangeMapService.gI().changeMap(player, 174, -1, 364, 408);
                                                } else {
                                                    this.npcChat(player, "Bạn chưa đủ 100 tỷ để vào");
                                                }
                                        }
                                    }
                                } else if (this.mapId == 174) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                ShopService.gI().openShopSpecial(player, this, ConstNpc.SHOP_CAU_CA_0, 0, -1);
                                                break;
                                            case 1:
                                                ShopService.gI().openShopSpecial(player, this, ConstNpc.SHOP_CAU_CA_1, 1, -1);
                                                break;
                                            case 2: {
                                                Item cathuong = null;
                                                Item cathuong1 = null;
                                                Item cathuong2 = null;
                                                Item cathuong3 = null;
                                                try {
                                                    cathuong = InventoryService.gI().findItemBagByTemp(player, 1364);
                                                    cathuong1 = InventoryService.gI().findItemBagByTemp(player, 1365);
                                                    cathuong2 = InventoryService.gI().findItemBagByTemp(player, 1366);
                                                    cathuong3 = InventoryService.gI().findItemBagByTemp(player, 1367);
                                                } catch (Exception e) {
                                                }
                                                if (cathuong == null || cathuong.quantity < 1) {
                                                    this.npcChat(player, "Bạn chưa có Cá trích");
                                                    return;
                                                }
                                                if (cathuong1 == null || cathuong1.quantity < 1) {
                                                    this.npcChat(player, "Bạn chưa có Cá đường xanh");
                                                    return;
                                                }
                                                if (cathuong2 == null || cathuong2.quantity < 1) {
                                                    this.npcChat(player, "Bạn chưa có Cá Green");
                                                    return;
                                                }
                                                if (cathuong3 == null || cathuong3.quantity < 1) {
                                                    this.npcChat(player, "Bạn chưa có Cá Blue");
                                                } else {
                                                    player.top_cauca += 10;
                                                    InventoryService.gI().subQuantityItemsBag(player, cathuong, 1);
                                                    InventoryService.gI().subQuantityItemsBag(player, cathuong1, 1);
                                                    InventoryService.gI().subQuantityItemsBag(player, cathuong2, 1);
                                                    InventoryService.gI().subQuantityItemsBag(player, cathuong3, 1);
                                                    Service.getInstance().sendMoney(player);
                                                    Item xoca = ItemService.gI().createNewItem((short) 1005);
                                                    InventoryService.gI().addItemBag(player, xoca, 1);
                                                    InventoryService.gI().sendItemBags(player);
                                                    this.npcChat(player, "|4|Bạn nhận được Xô cá Xanh");
                                                }
                                                break;
                                            }
                                            case 3: {
                                                Item cathuong = null;
                                                Item cathuong1 = null;
                                                Item cathuong2 = null;
                                                Item cathuong3 = null;
                                                try {
                                                    cathuong = InventoryService.gI().findItemBagByTemp(player, 1368);
                                                    cathuong1 = InventoryService.gI().findItemBagByTemp(player, 1369);
                                                    cathuong2 = InventoryService.gI().findItemBagByTemp(player, 1370);
                                                    cathuong3 = InventoryService.gI().findItemBagByTemp(player, 1371);
                                                } catch (Exception e) {
                                                }
                                                if (cathuong == null || cathuong.quantity < 1) {
                                                    this.npcChat(player, "Bạn chưa có Cá Clownfish");
                                                    return;
                                                }
                                                if (cathuong1 == null || cathuong1.quantity < 1) {
                                                    this.npcChat(player, "Bạn chưa có Cá heo");
                                                    return;
                                                }
                                                if (cathuong2 == null || cathuong2.quantity < 1) {
                                                    this.npcChat(player, "Bạn chưa có Cá voi");
                                                    return;
                                                }
                                                if (cathuong3 == null || cathuong3.quantity < 1) {
                                                    this.npcChat(player, "Bạn chưa có Cá Moorish");
                                                } else {
                                                    player.top_cauca += 20;
                                                    InventoryService.gI().subQuantityItemsBag(player, cathuong, 1);
                                                    InventoryService.gI().subQuantityItemsBag(player, cathuong1, 1);
                                                    InventoryService.gI().subQuantityItemsBag(player, cathuong2, 1);
                                                    InventoryService.gI().subQuantityItemsBag(player, cathuong3, 1);
                                                    Service.getInstance().sendMoney(player);
                                                    Item xoca = ItemService.gI().createNewItem((short) 1006);
                                                    InventoryService.gI().addItemBag(player, xoca, 1);
                                                    InventoryService.gI().sendItemBags(player);
                                                    this.npcChat(player, "|4|Bạn nhận được Xô cá Vàng");
                                                }
                                                break;
                                            }
                                            case 4:
                                                ChangeMapService.gI().changeMap(player, 5, -1, 643, 455);//con đường rắn độc
                                                break;
                                        }
                                    }
                                }
                            }

                        }
                    };
                    break;
                case ConstNpc.MAY_GAP_THU:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                this.createOtherMenu(player, ConstNpc.BASE_MENU, "|7|MÁY GẮP THÚ"
                                        + "\n\n|2| Sử dụng Ruby và " + ConstItem.NAME_XU_SERVER + " để gắp và nhận được Linh thú và PET chỉ số Ngẫu nhiên như sau :"
                                        + "\n|4| - Linh Thú : 1-100% chỉ số SD, HP, KI"
                                        + "\n - PET : 1-100% chỉ số SD, HP, KI"
                                        + "\n\n|3| *Gắp Linh Thú cần 50 Ruby 1 lượt"
                                        + "\n *Gắp PET cần 50 " + ConstItem.NAME_XU_SERVER + " 1 lượt"
                                        + "\n\n|1|Hãy tham gia và nhận các Linh Thú và PET quý hiếm nào !!!",
                                        "Shop\nVIP", "Gắp\nLinh Thú", "Gắp\nPET");
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (player.iDMark.isBaseMenu()) {
                                    switch (select) {
                                        case 0:
                                            ShopService.gI().openShopSpecial(player, this, ConstNpc.SHOP_VUI_0, 0, -1);
                                            break;
                                        case 1:
                                            Item ruby = null;
                                            try {
                                                ruby = InventoryService.gI().findItemBagByTemp(player, 1400);
                                            } catch (Exception e) {
                                            }
                                            if (ruby == null || ruby.quantity < 50) {
                                                Service.getInstance().sendThongBao(player, "Không đủ Ruby");
                                                return;
                                            }
                                            if (InventoryService.gI().getCountEmptyBag(player) == 0) {
                                                Service.getInstance().sendThongBao(player, "Hành trang không đủ chỗ trống");
                                            } else {
                                                int[] pet = new int[]{1274, 1275, 1276, 1277, 1278, 1279, 1280, 1281, 1282, 1283, 1284, 1285,
                                                    1286, 1287, 1288, 1289, 1290, 1291, 1292, 1293, 1294, 1295};
                                                int randomPet = new Random().nextInt(pet.length);
                                                Item linhThu = ItemService.gI().createNewItem((short) pet[randomPet]);
                                                linhThu.itemOptions.add(new ItemOption(50, Util.nextInt(1, 80)));
                                                linhThu.itemOptions.add(new ItemOption(77, Util.nextInt(1, 90)));
                                                linhThu.itemOptions.add(new ItemOption(103, Util.nextInt(1, 90)));
                                                linhThu.itemOptions.add(new ItemOption(101, Util.nextInt(10, 500)));
                                                linhThu.itemOptions.add(new ItemOption(5, Util.nextInt(1, 10)));
                                                if (Util.isTrue(95, 100)) {
                                                    linhThu.itemOptions.add(new ItemOption(93, Util.nextInt(1, 2)));
                                                    linhThu.itemOptions.add(new ItemOption(30, 1));
                                                }
                                                InventoryService.gI().subQuantityItemsBag(player, ruby, 50);
                                                InventoryService.gI().addItemBag(player, linhThu, 99);
                                                InventoryService.gI().sendItemBags(player);
                                                Service.getInstance().sendThongBao(player, "Chúc mừng bạn nhận được Linh thú " + linhThu.template.name);
                                            }
                                            break;
                                        case 2:
                                            Item xuVang = null;
                                            try {
                                                xuVang = InventoryService.gI().findItemBagByTemp(player, ConstItem.XU_SERVER);
                                            } catch (Exception e) {
                                            }
                                            if (xuVang == null || xuVang.quantity < 50) {
                                                Service.getInstance().sendThongBao(player, "Không đủ " + ConstItem.NAME_XU_SERVER);
                                                return;
                                            }
                                            if (InventoryService.gI().getCountEmptyBag(player) == 0) {
                                                Service.getInstance().sendThongBao(player, "Hành trang không đủ chỗ trống");
                                            } else {
                                                int[] pet = new int[]{2073, 2074, 2075, 2076, 2077};
                                                int randomPet = new Random().nextInt(pet.length);
                                                Item linhThu = ItemService.gI().createNewItem((short) pet[randomPet]);
                                                linhThu.itemOptions.add(new ItemOption(50, Util.nextInt(5, 100)));
                                                linhThu.itemOptions.add(new ItemOption(77, Util.nextInt(5, 110)));
                                                linhThu.itemOptions.add(new ItemOption(103, Util.nextInt(5, 110)));
                                                linhThu.itemOptions.add(new ItemOption(5, Util.nextInt(1, 30)));
                                                linhThu.itemOptions.add(new ItemOption(14, Util.nextInt(1, 10)));
                                                if (Util.isTrue(95, 100)) {
                                                    linhThu.itemOptions.add(new ItemOption(93, Util.nextInt(1, 3)));
                                                    linhThu.itemOptions.add(new ItemOption(30, 1));
                                                }

                                                //-------------Trừ--Item-------------
                                                if (!player.inventory.subTieuCoudSky(player, 50)) {
                                                    Service.getInstance().sendThongBaoOK(player, "Không đủ " + ConstItem.NAME_XU_SERVER);
                                                    return;
                                                }
                                                //------------------------------------

                                                InventoryService.gI().addItemBag(player, linhThu, 99);
                                                InventoryService.gI().sendItemBags(player);
                                                Service.getInstance().sendThongBao(player, "Chúc mừng bạn nhận được PET " + linhThu.template.name);
                                            }
                                            break;
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.VADOS:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                createOtherMenu(player, ConstNpc.BASE_MENU,
                                        "|7| BẢNG XẾP HẠNG\n|6|Ta Vừa Hack Map xem Được TOP Của Toàn Server\b|1|Người Muốn Xem TOP Gì?",
                                        "Top Sức mạnh", "Top Sức mạnh Đệ tử", "Top Nhiệm vụ", "Top Nạp");
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (player.iDMark.isBaseMenu()) {
                                    switch (select) {
                                        case 0:
                                            Service.getInstance().showTopPower(player, Service.getInstance().TOP_SUCMANH);
                                            break;
                                        case 1:
                                            Service.getInstance().showTopPower(player, Service.getInstance().TOP_DETU);
                                            break;
                                        case 2:
                                            Service.getInstance().showTopPower(player, Service.getInstance().TOP_NHIEMVU);
                                            break;
                                        case 3:
                                            Service.getInstance().showTopPower(player, Service.getInstance().TOP_NAP);
                                            break;
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.THAN_MAU:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 145) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                            "Ngươi cần gì ở ta?",
                                            //   "Shop\nVũ Trụ",
                                            "NC Chỉ Số\nSKH\nVũ Trụ",
                                            "Chuyển Đổi\nSKH\nVũ Trụ",
                                            "Về nhà"
                                    );
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (player == null || !canOpenNpc(player)) {
                                return;
                            }
                            switch (this.mapId) {
                                case 145:
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
//                                            case 0:
//                                                this.npcChat(player,"Đang update.");
//                                                break;
                                            case 0:
                                                CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.NANG_CAP_SKH_BILL);
                                                break;
                                            case 1:
                                                CombineServiceNew.gI().openTabCombine(player, CombineServiceNew.DOI_SKH_BILL);
                                                break;
                                            case 2:
                                                ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, -1, 250);
                                                break;
                                        }
                                    } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_START_COMBINE) {
                                        if (player.combineNew != null) {
                                            if (player.combineNew.typeCombine == CombineServiceNew.NANG_CAP_SKH_BILL
                                                    || player.combineNew.typeCombine == CombineServiceNew.DOI_SKH_BILL) {
                                                if (select == 0) {
                                                    CombineServiceNew.gI().startCombine(player);
                                                } else {
                                                    this.npcChat(player, "Lựa chọn không hợp lệ, hãy thử lại!");
                                                }
                                            } else {
                                                this.npcChat(player, "Lỗi chế tạo, hãy thử lại!");
                                            }
                                        } else {
                                            this.npcChat(player, "Lỗi chế tạo, hãy thử lại!");
                                        }
                                    } else {
                                        this.npcChat(player, "Lỗi menu, hãy thử lại!");
                                    }
                                    break;
                            }
                        }
                    };
                    break;
                case ConstNpc.CHUNG_SINH_BINH_DANG:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                switch (this.mapId) {
                                    case 7 // MÁP
                                    ->
                                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                                "Ngươi có dám bước vào đấu trường sinh tử, nơi chỉ kẻ mạnh mới sống sót?\n"
                                                + "|1|Số lượng người chơi [" + ChungSinhBinhDang.gI().getSoNguoiDangKy() + "/" + ChungSinhBinhDang.gI().getMaxPlayer() + "]\n"
                                                + "|7|Trạng thái trận đấu [" + switch (ChungSinhBinhDang.gI().getTrangThai()) {
                                            case CHO_DANG_KY ->
                                                "Đăng ký";
                                            case CHUAN_BI ->
                                                "Chuẩn bị";
                                            case DA_BAT_DAU ->
                                                "Đã bắt đầu";
                                            case DOI_CO ->
                                                "Đang chiến đấu";
                                            case KET_THUC ->
                                                "Kết thúc";
                                            default ->
                                                "Không xác định";
                                        } + "]\n\n"
                                                + "|0|Phần thưởng cho người chiến thắng:\n"
                                                + "|5|- Thỏi Vàng x25.000\n"
                                                + "|5|- Hồng Ngọc x25.000\n"
                                                + "|0|- Phần thưởng sẽ được gửi qua Hòm Thư!",
                                                "Chúng sinh\nbình đẳng",
                                                "Bảng xếp\nhạng",
                                                "Đóng"
                                        );
                                    case 226 ->
                                        this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                                "Ngươi có dám bước vào đấu trường sinh tử, nơi chỉ kẻ mạnh mới sống sót?\n"
                                                + "|1|Số lượng người chơi [" + ChungSinhBinhDang.gI().getSoNguoiDangKy() + "/" + ChungSinhBinhDang.gI().getMaxPlayer() + "]\n"
                                                + "|7|Trạng thái trận đấu [" + switch (ChungSinhBinhDang.gI().getTrangThai()) {
                                            case CHO_DANG_KY ->
                                                "Đăng ký";
                                            case CHUAN_BI ->
                                                "Chuẩn bị";
                                            case DA_BAT_DAU ->
                                                "Đã bắt đầu";
                                            case DOI_CO ->
                                                "Đang chiến đấu";
                                            case KET_THUC ->
                                                "Kết thúc";
                                            default ->
                                                "Không xác định";
                                        } + "]\n\n"
                                                + "|0|Phần thưởng cho người chiến thắng:\n"
                                                + "|5|- Thỏi Vàng x25.000\n"
                                                + "|5|- Hồng Ngọc x25.000\n"
                                                + "|0|- Phần thưởng sẽ được gửi qua Hòm Thư!",
                                                "Chúng sinh\nbình đẳng",
                                                "Bảng xếp\nhạng",
                                                "Đóng"
                                        );
                                    default -> {

                                    }
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                switch (this.mapId) {
                                    case 5 -> {
                                        if (player.iDMark.isBaseMenu()) {
                                            switch (select) {
                                                case 0 ->
                                                    createOtherMenu(player, ConstNpc.MENU_DAO_KAME, "Ta có thể giúp gì cho ngươi?",
                                                            "Đăng ký",
                                                            "Đóng");
                                                case 1 ->
                                                    this.npcChat(player, "Chưa update");
                                                case 2 ->
                                                    this.npcChat(player, Manager.DOMAIN);
                                            }

                                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_DAO_KAME) {
                                            switch (select) {
                                                case 0 ->
                                                    ChungSinhBinhDang.gI().dangKy(player, true);
                                            }
                                        }
                                    }
                                    case 226 -> {
                                        if (player.iDMark.isBaseMenu()) {
                                            switch (select) {
                                                case 0 ->
                                                    createOtherMenu(player, ConstNpc.MENU_PHONG_CHO, "Ta có thể giúp gì cho ngươi?",
                                                            "Hủy đăng ký",
                                                            "Đóng");
                                                case 1 ->
                                                    this.npcChat(player, "Chưa update");
                                                case 2 ->
                                                    this.npcChat(player, Manager.DOMAIN);
                                            }
                                        } else if (player.iDMark.getIndexMenu() == ConstNpc.MENU_PHONG_CHO) {
                                            switch (select) {
                                                case 0 ->
                                                    ChungSinhBinhDang.gI().dangKy(player, false);
                                            }
                                        }
                                    }
                                    default -> {
                                    }
                                }
                            }
                        }
                    };
                    break;
                case ConstNpc.LEO_THAP:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 175) {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                            "Ta là Thần Goku được cử xuống Trái đất để chiến đấu với các ngươi"
                                            + "\nHãy đánh bại ta và trở thành người mạnh mẽ nhất Trái Đất xem nào"
                                            + (player.levelKillThap > 0 && player.levelKillThap % 10 == 0 ? ("\n|2|Phần thưởng : 15 " + ConstItem.NAME_XU_SERVER) : ("\n|2|Phần thưởng : 1 " + ConstItem.NAME_XU_SERVER)),
                                            "Chiến\n[Tầng: " + player.levelKillThap + "]", "Top\nLeo tháp", "Quay về");
                                } else {
                                    this.createOtherMenu(player, ConstNpc.BASE_MENU,
                                            "|7|KHU VỰC LEO THÁP"
                                            + "\n|6|Bạn muốn đến khu vực Tháp Chiến Tranh?",
                                            "Đi đến");
                                }
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                if (this.mapId == 175) {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:// khiêu chiến NPC
                                                if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                                                    try {
                                                        player.lastTimeSwapThap = System.currentTimeMillis();
                                                        PlayerService.gI().savePlayer(player);

                                                        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

                                                        Runnable task = () -> {
                                                            hide_npc(player, (int) ConstNpc.LEO_THAP, 0);
                                                            PlayerService.gI().changeAndSendTypePK(player, ConstPlayer.PK_PVP);
                                                            PlayerService.gI().playerMove(player, 665, 504);
                                                            PlayerService.gI().setPos(player, 665, 504, 55);
                                                            scheduler.shutdown();
                                                        };
                                                        scheduler.schedule(task, 1, TimeUnit.SECONDS);

                                                        new Boss_Leo_Thap(Util.createIdRandomBoss((int) player.id), BossData.NPC_LEO_THAP, player.zone, this.cx, this.cy, player);
                                                        player.zone.load_Me_To_Another(player);
                                                    } catch (Exception ex) {
                                                        Logger.getLogger(NpcFactory.class.getName()).log(Level.SEVERE, null, ex);
                                                    }
                                                } else {
                                                    Service.getInstance().sendThongBao(player, "Cần 1 ô trống trong Hành trang");
                                                }
                                                break;
                                            case 1: // Top đánh NPC
                                                Service.getInstance().showToplevelLeoThap(player);
                                                break;
                                            case 2:
                                                ChangeMapService.gI().changeMapNonSpaceship(player, 0, 599, 432);
                                                break;
                                        }
                                    }
                                } else {
                                    if (player.iDMark.isBaseMenu()) {
                                        switch (select) {
                                            case 0:
                                                ChangeMapService.gI().changeMapNonSpaceship(player, 175, 665, 504);
                                                break;
                                        }
                                    }

                                }
                            }
                        }
                    };
                    break;
                default:
                    npc = new Npc(mapId, status, cx, cy, tempId, avartar) {
                        @Override
                        public void openBaseMenu(Player player) {
                            if (canOpenNpc(player)) {
                                super.openBaseMenu(player);
                            }
                        }

                        @Override
                        public void confirmMenu(Player player, int select) {
                            if (canOpenNpc(player)) {
                                // ShopService.gI().openShopNormal(player, this, ConstNpc.SHOP_BUNMA_TL_0, 0,
                                // player.gender);
                            }
                        }
                    };

            }

        } catch (Exception e) {
            Log.error(NpcFactory.class,
                    e, "Lỗi load npc");
        }
        return npc;
    }

    public static void handleXuLyVatPham(Npc npc, Player player, int select) {
        if (player.iDMark.getIndexMenu() == ConstNpc.XU_LY_VAT_PHAM) {
            switch (select) {
                case 0 ->
                    Input.gI().createFormTachItem(player);
                case 1 ->
                    Input.gI().createFormGopItem(player);
                case 2 -> {
                    npc.createOtherMenu(player, ConstNpc.XOA_VAT_PHAM,
                            "|2|Ta có thể giúp ngươi xử lý vật phẩm trong hành trang."
                            + "\n\n|7|**Lưu ý**"
                            + "\n|1|- Hành động này sẽ xóa vĩnh viễn vật phẩm trong túi đồ!"
                            + "\n|1|- Hãy chắc chắn trước khi thực hiện!",
                            "Xóa\nVật Phẩm [HSD]",
                            "Xóa\nVật Phẩm [Tất cả]",
                            "Xóa Hết\nVật Phẩm [Admin]");
                }
            }
        } else if (player.iDMark.getIndexMenu() == ConstNpc.XOA_VAT_PHAM) {
            switch (select) {
                case 0 ->
                    InventoryService.gI().removeAllItemsWithOption93(player); // HSD
                case 1 ->
                    InventoryService.gI().clearAllBagItems(player); // All
                case 2 -> {
                    if (!player.isAdmin()) {
                        Service.getInstance().sendThongBao(player, "Bạn không có quyền sử dụng chức năng này!");
                        return;
                    }
                    InventoryService.gI().clearAllBagItems(player);
                    boolean hasCurrency = player.inventory.gold > 0 || player.inventory.gem > 0 || player.inventory.ruby > 0;
                    if (!hasCurrency) {
                        Service.getInstance().sendThongBao(player, "Hành trang của bạn không tiền để xóa!");
                        return;
                    }
                    player.inventory.gold = 0;
                    player.inventory.gem = 0;
                    player.inventory.ruby = 0;
                    Service.getInstance().sendMoney(player);
                }
            }
        }
    }

    public static void createNpcRongThieng() {
        Npc npc = new Npc(-1, -1, -1, -1, ConstNpc.RONG_THIENG, -1) {
            @Override
            public void confirmMenu(Player player, int select) {
                switch (player.iDMark.getIndexMenu()) {
                    case ConstNpc.IGNORE_MENU:

                        break;
                    case ConstNpc.SHENRON_CONFIRM:
                        if (select == 0) {
                            SummonDragon.gI().confirmWish();
                        } else if (select == 1) {
                            SummonDragon.gI().reOpenShenronWishes(player);
                        }
                        break;
                    case ConstNpc.SHENRON_1_1:
                        if (player.iDMark.getIndexMenu() == ConstNpc.SHENRON_1_1
                                && select == SHENRON_1_STAR_WISHES_1.length - 1) {
                            NpcService.gI().createMenuRongThieng(player, ConstNpc.SHENRON_1_2, SHENRON_SAY,
                                    SHENRON_1_STAR_WISHES_2);
                            break;
                        }
                    case ConstNpc.SHENRON_1_2:
                        if (player.iDMark.getIndexMenu() == ConstNpc.SHENRON_1_2
                                && select == SHENRON_1_STAR_WISHES_2.length - 1) {
                            NpcService.gI().createMenuRongThieng(player, ConstNpc.SHENRON_1_1, SHENRON_SAY,
                                    SHENRON_1_STAR_WISHES_1);
                            break;
                        }
                    case ConstNpc.BLACK_SHENRON:
                        if (player.iDMark.getIndexMenu() == ConstNpc.BLACK_SHENRON
                                && select == BLACK_SHENRON_WISHES.length) {
                            NpcService.gI().createMenuRongThieng(player, ConstNpc.BLACK_SHENRON, BLACK_SHENRON_SAY,
                                    BLACK_SHENRON_WISHES);
                            break;
                        }
                    case ConstNpc.ICE_SHENRON:
                        if (player.iDMark.getIndexMenu() == ConstNpc.ICE_SHENRON
                                && select == ICE_SHENRON_WISHES.length) {
                            NpcService.gI().createMenuRongThieng(player, ConstNpc.ICE_SHENRON, ICE_SHENRON_SAY,
                                    ICE_SHENRON_WISHES);
                            break;
                        }
                    default:
                        SummonDragon.gI().showConfirmShenron(player, player.iDMark.getIndexMenu(), (byte) select);
                        break;
                }
            }
        };
    }

    public static void createNpcConMeo() {
        Npc npc = new Npc(-1, -1, -1, -1, ConstNpc.CON_MEO, 351) {
            @Override
            public void confirmMenu(Player player, int select) {
                switch (player.iDMark.getIndexMenu()) {
                    case ConstNpc.CONFIRM_DIALOG:
                        ConfirmDialog confirmDialog = player.getConfirmDialog();
                        if (confirmDialog != null) {
                            if (confirmDialog instanceof MenuDialog menu) {
                                menu.getRunable().setIndexSelected(select);
                                menu.run();
                                return;
                            }
                            if (select == 0) {
                                confirmDialog.run();
                            } else {
                                confirmDialog.cancel();
                            }
                            player.setConfirmDialog(null);
                        }
                        break;
                    case ConstNpc.UP_TOP_ITEM:

                        break;
                    case ConstNpc.MENU_OPTION_USE_ITEM1105:
                        if (select == 0) {
                            IntrinsicService.gI().sattd(player);
                        } else if (select == 1) {
                            IntrinsicService.gI().satnm(player);
                        } else if (select == 2) {
                            IntrinsicService.gI().setxd(player);
                        }
                        break;
                    case ConstNpc.MENU_OPTION_USE_ITEM1537:
                        if (select == 0) {
                            IntrinsicService.gI().SkhHdTD(player);
                        } else if (select == 1) {
                            IntrinsicService.gI().SkhHdNM(player);
                        } else if (select == 2) {
                            IntrinsicService.gI().SkhHdXD(player);
                        }
                        break;
                    case ConstNpc.MENU_OPTION_USE_ITEM1538:
                        if (select == 0) {
                            IntrinsicService.gI().SkhTlTD(player);
                        } else if (select == 1) {
                            IntrinsicService.gI().SkhTlNM(player);
                        } else if (select == 2) {
                            IntrinsicService.gI().SkhTlXD(player);
                        }
                        break;
                    case ConstNpc.MENU_TD:
                        switch (select) {
                            case 0:
                                try {
                                ItemService.gI().settaiyoken(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 1:
                                try {
                                ItemService.gI().setgenki(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 2:
                                try {
                                ItemService.gI().setkamejoko(player);
                            } catch (Exception e) {
                            }
                            break;
                        }
                        break;
                    case ConstNpc.MENU_NM:
                        switch (select) {
                            case 0:
                                try {
                                ItemService.gI().setgodki(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 1:
                                try {
                                ItemService.gI().setgoddam(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 2:
                                try {
                                ItemService.gI().setsummon(player);
                            } catch (Exception e) {
                            }
                            break;
                        }
                        break;

                    case ConstNpc.MENU_XD:
                        switch (select) {
                            case 0:
                                try {
                                ItemService.gI().setgodgalick(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 1:
                                try {
                                ItemService.gI().setmonkey(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 2:
                                try {
                                ItemService.gI().setgodhp(player);
                            } catch (Exception e) {
                            }
                            break;
                        }
                        break;
                    case ConstNpc.MENU_TD_HD:
                        switch (select) {
                            case 0:
                                try {
                                ItemService.gI().settaiyokenHD(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 1:
                                try {
                                ItemService.gI().setgenkiHD(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 2:
                                try {
                                ItemService.gI().setkamejokoHD(player);
                            } catch (Exception e) {
                            }
                            break;
                        }
                        break;
                    case ConstNpc.MENU_NM_HD:
                        switch (select) {
                            case 0:
                                try {
                                ItemService.gI().setgodkiHD(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 1:
                                try {
                                ItemService.gI().setgoddamHD(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 2:
                                try {
                                ItemService.gI().setsummonHD(player);
                            } catch (Exception e) {
                            }
                            break;
                        }
                        break;

                    case ConstNpc.MENU_XD_HD:
                        switch (select) {
                            case 0:
                                try {
                                ItemService.gI().setgodgalickHD(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 1:
                                try {
                                ItemService.gI().setmonkeyHD(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 2:
                                try {
                                ItemService.gI().setgodhpHD(player);
                            } catch (Exception e) {
                            }
                            break;
                        }
                        break;
                    case ConstNpc.MENU_TD_TL:
                        switch (select) {
                            case 0:
                                try {
                                ItemService.gI().settaiyokenTL(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 1:
                                try {
                                ItemService.gI().setgenkiTL(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 2:
                                try {
                                ItemService.gI().setkamejokoTL(player);
                            } catch (Exception e) {
                            }
                            break;
                        }
                        break;
                    case ConstNpc.MENU_NM_TL:
                        switch (select) {
                            case 0:
                                try {
                                ItemService.gI().setgodkiTL(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 1:
                                try {
                                ItemService.gI().setgoddamTL(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 2:
                                try {
                                ItemService.gI().setsummonTL(player);
                            } catch (Exception e) {
                            }
                            break;
                        }
                        break;

                    case ConstNpc.MENU_XD_TL:
                        switch (select) {
                            case 0:
                                try {
                                ItemService.gI().setgodgalickTL(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 1:
                                try {
                                ItemService.gI().setmonkeyTL(player);
                            } catch (Exception e) {
                            }
                            break;
                            case 2:
                                try {
                                ItemService.gI().setgodhpTL(player);
                            } catch (Exception e) {
                            }
                            break;
                        }
                        break;
                    case ConstNpc.TAIXIU:
                        String time = ((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) + " giây";
                        if (((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && player.goldTai == 0 && player.goldXiu == 0 && TaiXiu.gI().baotri == false) {
                            switch (select) {
                                case 0:
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.TAIXIU, 11039, "\n|7|---NHÀ CÁI TÀI XỈU---"
                                            + "\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z + TaiXiu.gI().ketqua
                                            + "\n|4|Kết quả 10 lần gần nhất:  " + TaiXiu.gI().InKetQua()
                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Thỏi vàng"
                                            + "\n\nTổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Thỏi vàng\n\n|5|Thời gian còn lại: " + time, "Cập nhập", "Theo TÀI", "Theo XỈU", "Đóng");
                                    break;
                                case 1:
                                    if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_22_0) {
                                        Input.gI().TAI_taixiu(player);
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Bạn chưa đủ điều kiện để chơi");
                                    }
                                    break;
                                case 2:
                                    if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_22_0) {
                                        Input.gI().XIU_taixiu(player);
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Bạn chưa đủ điều kiện để chơi");
                                    }
                                    break;
                            }
                        } else if (((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && player.goldTai > 0 && TaiXiu.gI().baotri == false) {
                            switch (select) {
                                case 0:
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.TAIXIU, 11039, "\n|7|---NHÀ CÁI TÀI XỈU---"
                                            + "\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z + TaiXiu.gI().ketqua
                                            + "\n|4|Kết quả 10 lần gần nhất:  " + TaiXiu.gI().InKetQua()
                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Thỏi vàng"
                                            + "\n\nTổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Thỏi vàng\n\n|5|Thời gian còn lại: " + time + "\n\n|7|Bạn đã cược Tài : " + Util.format(player.goldTai) + " Thỏi vàng", "Cập nhập", "Đóng");
                                    break;
                            }
                        } else if (((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && player.goldXiu > 0 && TaiXiu.gI().baotri == false) {
                            switch (select) {
                                case 0:
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.TAIXIU, 11039, "\n|7|---NHÀ CÁI TÀI XỈU---"
                                            + "\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z + TaiXiu.gI().ketqua
                                            + "\n|4|Kết quả 10 lần gần nhất:  " + TaiXiu.gI().InKetQua()
                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Thỏi vàng"
                                            + "\n\nTổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Thỏi vàng\n\n|5|Thời gian còn lại: " + time + "\n\n|7|Bạn đã cược Xỉu : " + Util.format(player.goldXiu) + " Thỏi vàng", "Cập nhập", "Đóng");
                                    break;
                            }
                        } else if (((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && player.goldTai > 0 && TaiXiu.gI().baotri == true) {
                            switch (select) {
                                case 0:
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.TAIXIU, 11039, "\n|7|---NHÀ CÁI TÀI XỈU---"
                                            + "\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z + TaiXiu.gI().ketqua
                                            + "\n|4|Kết quả 10 lần gần nhất:  " + TaiXiu.gI().InKetQua()
                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Thỏi vàng"
                                            + "\n\nTổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Thỏi vàng\n\n|5|Thời gian còn lại: " + time + "\n\n|7|Bạn đã cược Tài : " + Util.format(player.goldTai) + " Thỏi vàng" + "\n\n|7|Hệ thống sắp bảo trì", "Cập nhập", "Đóng");
                                    break;
                            }
                        } else if (((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && player.goldXiu > 0 && TaiXiu.gI().baotri == true) {
                            switch (select) {
                                case 0:
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.TAIXIU, 11039, "\n|7|---NHÀ CÁI TÀI XỈU---"
                                            + "\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z + TaiXiu.gI().ketqua
                                            + "\n|4|Kết quả 10 lần gần nhất:  " + TaiXiu.gI().InKetQua()
                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Thỏi vàng"
                                            + "\n\nTổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Thỏi vàng\n\n|5|Thời gian còn lại: " + time + "\n\n|7|Bạn đã cược Xỉu : " + Util.format(player.goldXiu) + " Thỏi vàng" + "\n\n|7|Hệ thống sắp bảo trì", "Cập nhập", "Đóng");
                                    break;
                            }
                        } else if (((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && player.goldXiu == 0 && player.goldTai == 0 && TaiXiu.gI().baotri == true) {
                            switch (select) {
                                case 0:
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.TAIXIU, 11039, "\n|7|---NHÀ CÁI TÀI XỈU---"
                                            + "\n\n|3|Kết quả kì trước:  " + TaiXiu.gI().x + " : " + TaiXiu.gI().y + " : " + TaiXiu.gI().z + TaiXiu.gI().ketqua
                                            + "\n|4|Kết quả 10 lần gần nhất:  " + TaiXiu.gI().InKetQua()
                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu.gI().goldTai) + " Thỏi vàng"
                                            + "\n\nTổng nhà XỈU: " + Util.format(TaiXiu.gI().goldXiu) + " Thỏi vàng\n\n|5|Thời gian còn lại: " + time + "\n\n|7|Hệ thống sắp bảo trì", "Cập nhập", "Đóng");
                                    break;
                            }
                        }
                        break;

                    case ConstNpc.TAIXIU_XU:
                        String time2 = ((TaiXiu_Xu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) + " giây";
                        if (((TaiXiu_Xu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && player.goldTai_xu == 0 && player.goldXiu_xu == 0 && TaiXiu_Xu.gI().baotri == false) {
                            switch (select) {
                                case 0:
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.TAIXIU_XU, 11039, "\n|7|---NHÀ CÁI TÀI XỈU---"
                                            + "\n\n|3|Kết quả kì trước:  " + TaiXiu_Xu.gI().x + " : " + TaiXiu_Xu.gI().y + " : " + TaiXiu_Xu.gI().z + TaiXiu_Xu.gI().ketqua
                                            + "\n|4|Kết quả 10 lần gần nhất:  " + TaiXiu_Xu.gI().InKetQua()
                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu_Xu.gI().goldTai) + " Hồng ngọc"
                                            + "\n\nTổng nhà XỈU: " + Util.format(TaiXiu_Xu.gI().goldXiu) + " Hồng ngọc\n\n|5|Thời gian còn lại: " + time2, "Cập nhập", "Theo TÀI", "Theo XỈU", "Đóng");
                                    break;
                                case 1:
                                    if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_22_0) {
                                        Input.gI().TAI_XU(player);
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Bạn chưa đủ điều kiện để chơi");
                                    }
                                    break;
                                case 2:
                                    if (TaskService.gI().getIdTask(player) >= ConstTask.TASK_22_0) {
                                        Input.gI().XIU_XU(player);
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Bạn chưa đủ điều kiện để chơi");
                                    }
                                    break;
                            }
                        } else if (((TaiXiu_Xu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && player.goldTai_xu > 0 && TaiXiu_Xu.gI().baotri == false) {
                            switch (select) {
                                case 0:
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.TAIXIU_XU, 11039, "\n|7|---NHÀ CÁI TÀI XỈU---"
                                            + "\n\n|3|Kết quả kì trước:  " + TaiXiu_Xu.gI().x + " : " + TaiXiu_Xu.gI().y + " : " + TaiXiu_Xu.gI().z + TaiXiu_Xu.gI().ketqua
                                            + "\n|4|Kết quả 10 lần gần nhất:  " + TaiXiu_Xu.gI().InKetQua()
                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu_Xu.gI().goldTai) + " Hồng ngọc"
                                            + "\n\nTổng nhà XỈU: " + Util.format(TaiXiu_Xu.gI().goldXiu) + " Hồng ngọc\n\n|5|Thời gian còn lại: " + time2 + "\n\n|7|Bạn đã cược Tài : " + Util.format(player.goldTai_xu) + " Hồng ngọc", "Cập nhập", "Đóng");
                                    break;
                            }
                        } else if (((TaiXiu_Xu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && player.goldXiu_xu > 0 && TaiXiu_Xu.gI().baotri == false) {
                            switch (select) {
                                case 0:
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.TAIXIU_XU, 11039, "\n|7|---NHÀ CÁI TÀI XỈU---"
                                            + "\n\n|3|Kết quả kì trước:  " + TaiXiu_Xu.gI().x + " : " + TaiXiu_Xu.gI().y + " : " + TaiXiu_Xu.gI().z + TaiXiu_Xu.gI().ketqua
                                            + "\n|4|Kết quả 10 lần gần nhất:  " + TaiXiu_Xu.gI().InKetQua()
                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu_Xu.gI().goldTai) + " Hồng ngọc"
                                            + "\n\nTổng nhà XỈU: " + Util.format(TaiXiu_Xu.gI().goldXiu) + " Hồng ngọc\n\n|5|Thời gian còn lại: " + time2 + "\n\n|7|Bạn đã cược Xỉu : " + Util.format(player.goldXiu_xu) + " Hồng ngọc", "Cập nhập", "Đóng");
                                    break;
                            }
                        } else if (((TaiXiu_Xu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && player.goldTai_xu > 0 && TaiXiu_Xu.gI().baotri == true) {
                            switch (select) {
                                case 0:
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.TAIXIU_XU, 11039, "\n|7|---NHÀ CÁI TÀI XỈU---"
                                            + "\n\n|3|Kết quả kì trước:  " + TaiXiu_Xu.gI().x + " : " + TaiXiu_Xu.gI().y + " : " + TaiXiu_Xu.gI().z + TaiXiu_Xu.gI().ketqua
                                            + "\n|4|Kết quả 10 lần gần nhất:  " + TaiXiu_Xu.gI().InKetQua()
                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu_Xu.gI().goldTai) + " Hồng ngọc"
                                            + "\n\nTổng nhà XỈU: " + Util.format(TaiXiu_Xu.gI().goldXiu) + " Hồng ngọc\n\n|5|Thời gian còn lại: " + time2 + "\n\n|7|Bạn đã cược Tài : " + Util.format(player.goldTai_xu) + " Hồng ngọc" + "\n\n|7|Hệ thống sắp bảo trì", "Cập nhập", "Đóng");
                                    break;
                            }
                        } else if (((TaiXiu_Xu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && player.goldXiu_xu > 0 && TaiXiu_Xu.gI().baotri == true) {
                            switch (select) {
                                case 0:
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.TAIXIU_XU, 11039, "\n|7|---NHÀ CÁI TÀI XỈU---"
                                            + "\n\n|3|Kết quả kì trước:  " + TaiXiu_Xu.gI().x + " : " + TaiXiu_Xu.gI().y + " : " + TaiXiu_Xu.gI().z + TaiXiu_Xu.gI().ketqua
                                            + "\n|4|Kết quả 10 lần gần nhất:  " + TaiXiu_Xu.gI().InKetQua()
                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu_Xu.gI().goldTai) + " Hồng ngọc"
                                            + "\n\nTổng nhà XỈU: " + Util.format(TaiXiu_Xu.gI().goldXiu) + " Hồng ngọc\n\n|5|Thời gian còn lại: " + time2 + "\n\n|7|Bạn đã cược Xỉu : " + Util.format(player.goldXiu_xu) + " Hồng ngọc" + "\n\n|7|Hệ thống sắp bảo trì", "Cập nhập", "Đóng");
                                    break;
                            }
                        } else if (((TaiXiu_Xu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) > 0 && player.goldXiu_xu == 0 && player.goldTai_xu == 0 && TaiXiu_Xu.gI().baotri == true) {
                            switch (select) {
                                case 0:
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.TAIXIU_XU, 11039, "\n|7|---NHÀ CÁI TÀI XỈU---"
                                            + "\n\n|3|Kết quả kì trước:  " + TaiXiu_Xu.gI().x + " : " + TaiXiu_Xu.gI().y + " : " + TaiXiu_Xu.gI().z + TaiXiu_Xu.gI().ketqua
                                            + "\n|4|Kết quả 10 lần gần nhất:  " + TaiXiu_Xu.gI().InKetQua()
                                            + "\n\n|6|Tổng nhà TÀI: " + Util.format(TaiXiu_Xu.gI().goldTai) + " Hồng ngọc"
                                            + "\n\nTổng nhà XỈU: " + Util.format(TaiXiu_Xu.gI().goldXiu) + " Hồng ngọc\n\n|5|Thời gian còn lại: " + time2 + "\n\n|7|Hệ thống sắp bảo trì", "Cập nhập", "Đóng");
                                    break;
                            }
                        }
                        break;

                    case ConstNpc.RUONG_GO:
                        int size = player.textRuongGo.size();
                        if (size > 0) {
                            String menuselect = "OK [" + (size - 1) + "]";
                            if (size == 1) {
                                menuselect = "OK";
                            }
                            NpcService.gI().createMenuConMeo(player, ConstNpc.RUONG_GO, -1,
                                    player.textRuongGo.get(size - 1), menuselect);
                            player.textRuongGo.remove(size - 1);
                        }
                        break;
                    case ConstNpc.MENU_MABU_WAR:
                        if (select == 0) {
                            if (player.zone.finishMabuWar) {
                                ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, -1, 250);
                            } else if (player.zone.map.mapId == 119) {
                                Zone zone = MabuWar.gI().getMapLastFloor(120);
                                if (zone != null) {
                                    ChangeMapService.gI().changeMap(player, zone, 354, 240);
                                } else {
                                    Service.getInstance().sendThongBao(player,
                                            "Trận đại chiến đã kết thúc, tàu vận chuyển sẽ đưa bạn về nhà");
                                    ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, -1, 250);
                                }
                            } else {
                                int idMapNextFloor = player.zone.map.mapId == 115 ? player.zone.map.mapId + 2
                                        : player.zone.map.mapId + 1;
                                ChangeMapService.gI().changeMap(player, idMapNextFloor, -1, 354, 240);
                            }
                            player.resetPowerPoint();
                            player.sendMenuGotoNextFloorMabuWar = false;
                            Service.getInstance().sendPowerInfo(player, "TL", player.getPowerPoint());
                            if (Util.isTrue(1, 30)) {
                                player.inventory.ruby += 1;
                                PlayerService.gI().sendInfoHpMpMoney(player);
                                Service.getInstance().sendThongBao(player, "Bạn nhận được 1 Hồng Ngọc");
                            } else {
                                Service.getInstance().sendThongBao(player,
                                        "Bạn đen vô cùng luôn nên không nhận được gì cả");
                            }
                        }
                        break;
                    case ConstNpc.IGNORE_MENU:

                        break;
                    case ConstNpc.MAKE_MATCH_PVP:
                        // PVP_old.gI().sendInvitePVP(player, (byte) select);
                        PVPServcice.gI().sendInvitePVP(player, (byte) select);
                        break;
                    case ConstNpc.MAKE_FRIEND:
                        if (select == 0) {
                            Object playerId = PLAYERID_OBJECT.get(player.id);
                            if (playerId != null) {
                                FriendAndEnemyService.gI().acceptMakeFriend(player,
                                        Integer.parseInt(String.valueOf(playerId)));
                            }
                        }
                        break;
                    case ConstNpc.REVENGE:
                        if (select == 0) {
                            PVPServcice.gI().acceptRevenge(player);
                        }
                        break;
                    case ConstNpc.TUTORIAL_SUMMON_DRAGON:
                        if (select == 0) {
                            NpcService.gI().createTutorial(player, -1, SummonDragon.SUMMON_SHENRON_TUTORIAL);
                        }
                        break;
                    case ConstNpc.SUMMON_SHENRON:
                        if (select == 0) {
                            NpcService.gI().createTutorial(player, -1, SummonDragon.SUMMON_SHENRON_TUTORIAL);
                        } else if (select == 1) {
                            SummonDragon.gI().summonShenron(player);
                        }
                        break;
                    case ConstNpc.SUMMON_BLACK_SHENRON:
                        if (select == 0) {
                            SummonDragon.gI().summonBlackShenron(player);
                        }
                        break;
                    case ConstNpc.SUMMON_ICE_SHENRON:
                        if (select == 0) {
                            SummonDragon.gI().summonIceShenron(player);
                        }
                        break;
                    case ConstNpc.INTRINSIC:
                        if (select == 0) {
                            IntrinsicService.gI().showAllIntrinsic(player);
                        } else if (select == 1) {
                            IntrinsicService.gI().showConfirmOpen(player);
                        } else if (select == 2) {
                            IntrinsicService.gI().showConfirmOpenVip(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_OPEN_INTRINSIC:
                        if (select == 0) {
                            IntrinsicService.gI().open(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_OPEN_INTRINSIC_VIP:
                        if (select == 0) {
                            IntrinsicService.gI().openVip(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_LEAVE_CLAN:
                        if (select == 0) {
                            ClanService.gI().leaveClan(player);
                        }
                        break;
                    case ConstNpc.CONFIRM_NHUONG_PC:
                        if (select == 0) {
                            ClanService.gI().phongPc(player, (int) PLAYERID_OBJECT.get(player.id));
                        }
                        break;
                    case ConstNpc.BAN_PLAYER:
                        if (select == 0) {
                            PlayerService.gI().banPlayer((Player) PLAYERID_OBJECT.get(player.id));
                            Service.getInstance().sendThongBao(player,
                                    "Ban người chơi " + ((Player) PLAYERID_OBJECT.get(player.id)).name + " thành công");
                        }
                        break;
                    case ConstNpc.BUFF_PET:
                        if (select == 0) {
                            Player pl = (Player) PLAYERID_OBJECT.get(player.id);
                            if (pl.pet == null) {
                                PetService.gI().createNormalPet(pl);
                                Service.getInstance().sendThongBao(player, "Phát đệ tử cho "
                                        + ((Player) PLAYERID_OBJECT.get(player.id)).name + " thành công");
                            }
                        }
                        break;
                    case ConstNpc.DUNG_NHIEU_TV:
                        Item thoivang = InventoryService.gI().findItemBagByTemp(player, 457);
                        switch (select) {
                            case 0:
                                if (thoivang == null || thoivang.quantity < 1) {
                                    Service.getInstance().sendThongBao(player, "Cần có đủ 1 Thỏi vàng để thực hiện");
                                    return;
                                }
                                if (player.inventory.gold + 500_000_000 > player.inventory.getGoldLimit()) {
                                    Service.getInstance().sendThongBao(player, "Vàng sau khi nhận vượt quá giới hạn");
                                } else {
                                    player.inventory.gold += 500_000_000;
                                    Service.getInstance().sendThongBao(player, "|4|Bạn nhận được 500 Triệu Vàng");
                                    InventoryService.gI().subQuantityItemsBag(player, thoivang, 1);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendMoney(player);
                                    break;
                                }
                                break;
                            case 1:
                                if (thoivang == null || thoivang.quantity < 5) {
                                    Service.getInstance().sendThongBao(player, "Cần có đủ 5 Thỏi vàng để thực hiện");
                                    return;
                                }
                                if (player.inventory.gold + 2_500_000_000L > player.inventory.getGoldLimit()) {
                                    Service.getInstance().sendThongBao(player, "Vàng sau khi nhận vượt quá giới hạn");
                                } else {
                                    player.inventory.gold += 2_500_000_000L;
                                    Service.getInstance().sendThongBao(player, "|4|Bạn nhận được 2,5 Tỷ Vàng");
                                    Service.getInstance().sendMoney(player);
                                    InventoryService.gI().subQuantityItemsBag(player, thoivang, 5);
                                    InventoryService.gI().sendItemBags(player);
                                    break;
                                }
                                break;
                            case 2:
                                if (thoivang == null || thoivang.quantity < 10) {
                                    Service.getInstance().sendThongBao(player, "Cần có đủ 10 Thỏi vàng để thực hiện");
                                    return;
                                }
                                if (player.inventory.gold + 5_000_000_000L > player.inventory.getGoldLimit()) {
                                    Service.getInstance().sendThongBao(player, "Vàng sau khi nhận vượt quá giới hạn");
                                } else {
                                    player.inventory.gold += 5_000_000_000L;
                                    Service.getInstance().sendThongBao(player, "|4|Bạn nhận được 5 Tỷ Vàng");
                                    Service.getInstance().sendMoney(player);
                                    InventoryService.gI().subQuantityItemsBag(player, thoivang, 10);
                                    InventoryService.gI().sendItemBags(player);
                                    break;
                                }
                                break;
                            case 3:
                                if (thoivang == null || thoivang.quantity < 98) {
                                    Service.getInstance().sendThongBao(player, "Cần có đủ 98 Thỏi vàng để thực hiện");
                                    return;
                                }
                                if (player.inventory.gold + 49_000_000_000L > player.inventory.getGoldLimit()) {
                                    Service.getInstance().sendThongBao(player, "Vàng sau khi nhận vượt quá giới hạn");
                                } else {
                                    player.inventory.gold += 49_000_000_000L;
                                    Service.getInstance().sendThongBao(player, "|4|Bạn nhận được 49 Tỷ Vàng");
                                    Service.getInstance().sendMoney(player);
                                    InventoryService.gI().subQuantityItemsBag(player, thoivang, 98);
                                    InventoryService.gI().sendItemBags(player);
                                    break;
                                }
                                break;
                            case 4:
                                if (thoivang == null || thoivang.quantity < 198) {
                                    Service.getInstance().sendThongBao(player, "Cần có đủ 198 Thỏi vàng để thực hiện");
                                    return;
                                }
                                if (player.inventory.gold + 99_000_000_000L > player.inventory.getGoldLimit()) {
                                    Service.getInstance().sendThongBao(player, "Vàng sau khi nhận vượt quá giới hạn");
                                } else {
                                    player.inventory.gold += 99_000_000_000L;
                                    Service.getInstance().sendThongBao(player, "|4|Bạn nhận được 99 Tỷ Vàng");
                                    Service.getInstance().sendMoney(player);
                                    InventoryService.gI().subQuantityItemsBag(player, thoivang, 198);
                                    InventoryService.gI().sendItemBags(player);
                                    break;
                                }
                                break;
                        }
                        break;
                    case ConstNpc.MENU_ADMIN:
                        switch (select) {
                            case 0:
                                switch (player.typePk) {
                                    case ConstPlayer.NON_PK -> {
                                        PlayerService.gI().changeAndSendTypePK(player, ConstPlayer.PK_ALL);
                                        Service.getInstance().sendThongBao(player, "Đã bật đồ sát");
                                    }
                                    case ConstPlayer.PK_ALL -> {
                                        PlayerService.gI().changeAndSendTypePK(player, ConstPlayer.NON_PK);
                                        Service.getInstance().sendThongBao(player, "Đã tắt đồ sát");
                                    }
                                    default ->
                                        Service.getInstance().sendThongBao(player, "Không thể chuyển chế độ PK trong trạng thái hiện tại");
                                }
                                break;

                            case 1:
                                for (int i = 14; i <= 20; i++) {
                                    Item item = ItemService.gI().createNewItem((short) i);
                                    item.quantity = 99;
                                    InventoryService.gI().addItemBag(player, item, 99);
                                }
                                InventoryService.gI().sendItemBags(player);
                                Service.getInstance().sendThongBao(player, "Nhận x99 Bộ Ngọc rồng thành công");
                                break;
                            case 2:
                                if (player.pet == null) {
                                    PetService.gI().createNormalPet(player);
                                } else {
                                    if (player.pet.typePet == 1) {
                                        PetService.gI().changeNormalPet(player);
                                    } else {
                                        PetService.gI().changeMabuPet(player);
                                    }
                                }
                                break;
                            case 3:
                                // PlayerService.gI().baoTri();
                                Maintenance.gI().start(30);
                                break;
                            case 4:
                                Input.gI().createFormFindPlayer(player);
                                break;
                            case 5:
                                NotiManager.getInstance().load();
                                NotiManager.getInstance().sendAlert(player);
                                NotiManager.getInstance().sendNoti(player);
                                Service.getInstance().chat(player, "Cập nhật thông báo thành công");
                                break;
                            case 6: // BUFF ITEM
                                Input.gI().createFormAddItem(player);
                                break;
                            case 7:
                                Input.gI().createFormBuffItemVip(player);
                                break;
                            case 8:
                                if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                                    if (player.inventory.itemsBody.get(0).quantity < 1
                                            && player.inventory.itemsBody.get(1).quantity < 1
                                            && player.inventory.itemsBody.get(2).quantity < 1
                                            && player.inventory.itemsBody.get(3).quantity < 1
                                            && player.inventory.itemsBody.get(4).quantity < 1) {
                                        player.gender += 1;
                                        if (player.gender > 2) {
                                            player.gender = 0;
                                        }
                                        short[] headtd = {30, 31, 64};
                                        short[] headnm = {9, 29, 32};
                                        short[] headxd = {27, 28, 6};
                                        player.playerSkill.skills.clear();
                                        for (Skill skill : player.playerSkill.skills) {
                                            skill.point = 1;
                                        }
                                        int[] skillsArr = player.gender == 0 ? new int[]{0, 1, 6, 9, 10, 20, 22, 24, 19, 27, 28, 29}
                                                : player.gender == 1 ? new int[]{2, 3, 7, 11, 12, 17, 18, 26, 19, 27, 28, 29}
                                                : new int[]{4, 5, 8, 13, 14, 21, 23, 25, 19, 27, 28, 29};
                                        for (int i = 0; i < skillsArr.length; i++) {
                                            if (skillsArr[i] == Skill.SUPER_KAME || skillsArr[i] == Skill.LIEN_HOAN_CHUONG || skillsArr[i] == Skill.MA_PHONG_BA) {
                                                player.playerSkill.skills.add(SkillUtil.createSkill(skillsArr[i], 1));
                                            } else if (skillsArr[i] == Skill.BIEN_HINH || skillsArr[i] == Skill.TAI_TAO_SUC_MANH) {
                                                player.playerSkill.skills.add(SkillUtil.createSkill(skillsArr[i], 1));
                                            } else {
                                                player.playerSkill.skills.add(SkillUtil.createSkill(skillsArr[i], 7));
                                            }
                                        }
                                        player.playerIntrinsic.intrinsic = IntrinsicService.gI().getIntrinsicById(0);
                                        player.playerIntrinsic.intrinsic.param1 = 0;
                                        player.playerIntrinsic.intrinsic.param2 = 0;
                                        player.playerIntrinsic.countOpen = 0;
                                        switch (player.gender) {
                                            case 0:
                                                player.head = headtd[Util.nextInt(headtd.length)];
                                                break;
                                            case 1:
                                                player.head = headnm[Util.nextInt(headnm.length)];
                                                break;
                                            case 2:
                                                player.head = headxd[Util.nextInt(headxd.length)];
                                                break;
                                            default:
                                                break;
                                        }
                                        Service.getInstance().sendThongBao(player, "|1|Đổi hành tinh thành công");
                                        Service.getInstance().player(player);
                                        player.zone.loadAnotherToMe(player);
                                        player.zone.load_Me_To_Another(player);
                                        Service.getInstance().sendFlagBag(player);
                                        Service.getInstance().Send_Caitrang(player);
                                        PlayerService.gI().sendInfoHpMpMoney(player);
                                        Service.getInstance().Send_Info_NV(player);
                                    } else {
                                        Service.getInstance().sendThongBao(player, "Tháo hết 5 món đầu đang mặc ra nha");
                                    }
                                } else {
                                    Service.getInstance().sendThongBao(player, "Balo đầy");
                                }
                                InventoryService.gI().sendItemBags(player);
                                Service.getInstance().sendMoney(player);
                                break;
                            case 9:
                                Input.gI().createFormBuffDanhHieu(player);
                                break;
                        }
                        break;
                    case ConstNpc.VAO_MAP_NGOAI_VUC:
                        switch (select) {
                            case 0:
                                ChangeMapService.gI().goToHallowen(player);
                                break;
                            case 1:
                                ChangeMapService.gI().goToHanhTinhBangGia(player);
                                break;
                            case 2:
                                ChangeMapService.gI().goToDiaNguc(player);
                                break;
                        }
                        break;
                    case ConstNpc.DE_TU_BERUS:
                        try {
                        switch (select) {
                            case 0:
                                Item item = InventoryService.gI().findItemBagByTemp(player, 1459);
                                if (item == null) {
                                    Service.getInstance().sendThongBao(player, "Không thể thực hiện");
                                    return;
                                }
                                InventoryService.gI().subQuantityItemsBag(player, item, 1);
                                InventoryService.gI().sendItemBags(player);
                                PetService.gI().changeBerusPet(player, 0);
                                break;
                            case 1:
                                Item item1 = InventoryService.gI().findItemBagByTemp(player, 1459);
                                if (item1 == null) {
                                    Service.getInstance().sendThongBao(player, "Không thể thực hiện");
                                    return;
                                }
                                InventoryService.gI().subQuantityItemsBag(player, item1, 1);
                                InventoryService.gI().sendItemBags(player);
                                PetService.gI().changeBerusPet(player, 1);
                                break;
                            case 2:
                                Item item2 = InventoryService.gI().findItemBagByTemp(player, 1459);
                                if (item2 == null) {
                                    Service.getInstance().sendThongBao(player, "Không thể thực hiện");
                                    return;
                                }
                                InventoryService.gI().subQuantityItemsBag(player, item2, 1);
                                InventoryService.gI().sendItemBags(player);
                                PetService.gI().changeBerusPet(player, 2);
                                break;
                        }
                        break;
                    } catch (Exception e) {
                        System.out.println("Loi mo de tu berus");
                    }
                    case ConstNpc.DE_TU_SON_TINH:
                        try {
                        switch (select) {
                            case 0:
                                Item item = InventoryService.gI().findItemBagByTemp(player, 1108);
                                if (item == null) {
                                    Service.getInstance().sendThongBao(player, "Không thể thực hiện");
                                    return;
                                }
                                InventoryService.gI().subQuantityItemsBag(player, item, 1);
                                InventoryService.gI().sendItemBags(player);
                                PetService.gI().changeBuuPet(player, 0);
                                break;
                            case 1:
                                Item item1 = InventoryService.gI().findItemBagByTemp(player, 1108);
                                if (item1 == null) {
                                    Service.getInstance().sendThongBao(player, "Không thể thực hiện");
                                    return;
                                }
                                InventoryService.gI().subQuantityItemsBag(player, item1, 1);
                                InventoryService.gI().sendItemBags(player);
                                PetService.gI().changeBuuPet(player, 1);
                                break;
                            case 2:
                                Item item2 = InventoryService.gI().findItemBagByTemp(player, 1108);
                                if (item2 == null) {
                                    Service.getInstance().sendThongBao(player, "Không thể thực hiện");
                                    return;
                                }
                                InventoryService.gI().subQuantityItemsBag(player, item2, 1);
                                InventoryService.gI().sendItemBags(player);
                                PetService.gI().changeBuuPet(player, 2);
                                break;
                        }
                        break;
                    } catch (Exception e) {
                        System.out.println("Loi mo de tu berus");
                    }
                    case ConstNpc.DE_TU_MABU:
                        try {
                        switch (select) {
                            case 0:
                                Item item = InventoryService.gI().findItemBagByTemp(player, 568);
                                if (item == null) {
                                    Service.getInstance().sendThongBao(player, "Không thể thực hiện");
                                    return;
                                }
                                InventoryService.gI().subQuantityItemsBag(player, item, 1);
                                InventoryService.gI().sendItemBags(player);
                                PetService.gI().changeMabuPet(player, 0);
                                break;
                            case 1:
                                Item item1 = InventoryService.gI().findItemBagByTemp(player, 568);
                                if (item1 == null) {
                                    Service.getInstance().sendThongBao(player, "Không thể thực hiện");
                                    return;
                                }
                                InventoryService.gI().subQuantityItemsBag(player, item1, 1);
                                InventoryService.gI().sendItemBags(player);
                                PetService.gI().changeMabuPet(player, 1);
                                break;
                            case 2:
                                Item item2 = InventoryService.gI().findItemBagByTemp(player, 568);
                                if (item2 == null) {
                                    Service.getInstance().sendThongBao(player, "Không thể thực hiện");
                                    return;
                                }
                                InventoryService.gI().subQuantityItemsBag(player, item2, 1);
                                InventoryService.gI().sendItemBags(player);
                                PetService.gI().changeMabuPet(player, 2);
                                break;
                        }
                        break;
                    } catch (Exception e) {
                        System.out.println("Loi mo de tu berus");
                    }
                    case ConstNpc.DE_TU_FIDE:
                        try {
                        switch (select) {
                            case 0:
                                Item item = InventoryService.gI().findItemBagByTemp(player, 1547);
                                if (item == null) {
                                    Service.getInstance().sendThongBao(player, "Không đủ yêu cầu");
                                    return;
                                }
                                InventoryService.gI().subQuantityItemsBag(player, item, 1);
                                InventoryService.gI().sendItemBags(player);
                                PetService.gI().changeFidePet(player, 0);
                                break;
                            case 1:
                                Item item1 = InventoryService.gI().findItemBagByTemp(player, 1547);
                                if (item1 == null) {
                                    Service.getInstance().sendThongBao(player, "Không đủ yêu cầu");
                                    return;
                                }
                                InventoryService.gI().subQuantityItemsBag(player, item1, 1);
                                InventoryService.gI().sendItemBags(player);
                                PetService.gI().changeFidePet(player, 1);
                                break;
                            case 2:
                                Item item2 = InventoryService.gI().findItemBagByTemp(player, 1547);
                                if (item2 == null) {
                                    Service.getInstance().sendThongBao(player, "Không đủ yêu cầu");
                                    return;
                                }
                                InventoryService.gI().subQuantityItemsBag(player, item2, 1);
                                InventoryService.gI().sendItemBags(player);
                                PetService.gI().changeFidePet(player, 2);
                                break;
                        }
                    } catch (Exception e) {
                        System.out.println("Loi mo de tu fide");
                    }
                    break;
                    case ConstNpc.DE_TU_XEN:
                        try {
                        switch (select) {
                            case 0:
                                Item item = InventoryService.gI().findItemBagByTemp(player, 1559);
                                if (item == null) {
                                    Service.getInstance().sendThongBao(player, "Không đủ yêu cầu");
                                    return;
                                }
                                InventoryService.gI().subQuantityItemsBag(player, item, 1);
                                InventoryService.gI().sendItemBags(player);
                                PetService.gI().changeXenPet(player, 0);
                                break;
                            case 1:
                                Item item1 = InventoryService.gI().findItemBagByTemp(player, 1559);
                                if (item1 == null) {
                                    Service.getInstance().sendThongBao(player, "Không đủ yêu cầu");
                                    return;
                                }
                                InventoryService.gI().subQuantityItemsBag(player, item1, 1);
                                InventoryService.gI().sendItemBags(player);
                                PetService.gI().changeXenPet(player, 1);
                                break;
                            case 2:
                                Item item2 = InventoryService.gI().findItemBagByTemp(player, 1559);
                                if (item2 == null) {
                                    Service.getInstance().sendThongBao(player, "Không đủ yêu cầu");
                                    return;
                                }
                                InventoryService.gI().subQuantityItemsBag(player, item2, 1);
                                InventoryService.gI().sendItemBags(player);
                                PetService.gI().changeXenPet(player, 2);
                                break;
                        }
                    } catch (Exception e) {
                        System.out.println("Loi mo de tu Xên");
                    }
                    break;
                    case ConstNpc.INFO_ALL:
                        switch (select) {
                            case 0:
                                NpcService.gI().createMenuConMeo(player, ConstNpc.INFO_ALL, 12713,
                                        "|7|THÔNG TIN NHÂN VẬT"
                                        + "\b|5|HP bản thân: " + Util.format(player.nPoint.hp) + "/" + Util.powerToString(player.nPoint.hpMax)
                                        + "\bKI bản thân: " + Util.format(player.nPoint.mp) + "/" + Util.powerToString(player.nPoint.mpMax)
                                        + "\bSức đánh: " + Util.format(player.nPoint.dame)
                                        + "\bGiáp: " + Util.format(player.nPoint.def)
                                        + "\b|8|-Vàng: " + Util.format(player.inventory.gold)
                                        + "   -Ngọc: " + Util.format(player.inventory.gem)
                                        + "   -H.Ngọc: " + Util.format(player.inventory.ruby)
                                        + "\b|5|Tổng vàng nhặt: " + Util.format(player.vangnhat)
                                        + "\b|3|Tổng Hồng ngọc nhặt: " + Util.format(player.hngocnhat),
                                        "Thông tin\n nhân vật", "Thông tin\nđệ tử", "Thông tin\nđồ mặc");
                                break;
                            case 1:
                                NpcService.gI().createMenuConMeo(player, ConstNpc.INFO_ALL, 12713,
                                        "|7|THÔNG TIN ĐỆ TỬ"
                                        + "\b\b|7|Hành tinh: " + Service.getInstance().get_HanhTinh(player.pet.gender)
                                        + "\b|5|HP ĐỆ TỬ: " + Util.format(player.pet.nPoint.hp) + "/" + Util.powerToString(player.pet.nPoint.hpMax)
                                        + "\bKI ĐỆ TỬ: " + Util.format(player.pet.nPoint.mp) + "/" + Util.powerToString(player.pet.nPoint.mpMax)
                                        + "\bSức đánh: " + Util.format(player.pet.nPoint.dame)
                                        + "\bGiáp: " + Util.format(player.pet.nPoint.def)
                                        + "\b|4|HP Gốc: " + Util.format(player.pet.nPoint.hpg)
                                        + "\bKI Gốc: " + Util.format(player.pet.nPoint.mpg)
                                        + "\bSức đánh Gốc: " + Util.format(player.pet.nPoint.dameg)
                                        + "\bGiáp Gốc: " + Util.format(player.pet.nPoint.defg),
                                        "Thông tin\n nhân vật", "Thông tin\nđệ tử", "Thông tin\nđồ mặc");
                                break;
                            case 2:
                                NpcService.gI().createMenuConMeo(player, ConstNpc.CHISODO, 12713,
                                        "|1|Bạn muốn xem chỉ số đồ bị giới hạn hiện thị:",
                                        "Chỉ số\nô 1", "Chỉ số\nô 2", "Chỉ số\nô 3",
                                        "Chỉ số\nô 4", "Chỉ số\nô 5", "Chỉ số\nô 6",
                                        "Chỉ số\nô 7", "Chỉ số\nô 8", "Chỉ số\nô 9",
                                        "Chỉ số\nô 10", "Chỉ số\nô 11", "Chỉ số\nô 12");
                                break;
                        }
                        break;
                    case ConstNpc.CHISODO: {
                        Item it = player.inventory.itemsBody.get(select);
                        if (it.quantity < 1) {
                            NpcService.gI().createMenuConMeo(player, ConstNpc.CHISODO, 12713,
                                    "|7|Ô này không có đồ!!!"
                                    + "\n|2|Bạn muốn xem chỉ số đồ bị giới hạn hiện thị:",
                                    "Chỉ số\nô 1", "Chỉ số\nô 2", "Chỉ số\nô 3",
                                    "Chỉ số\nô 4", "Chỉ số\nô 5", "Chỉ số\nô 6",
                                    "Chỉ số\nô 7", "Chỉ số\nô 8", "Chỉ số\nô 9",
                                    "Chỉ số\nô 10", "Chỉ số\nô 11", "Chỉ số\nô 12");
                            return;
                        }
                        NpcService.gI().createMenuConMeo(player, ConstNpc.CHISODO, 12713,
                                "|2|Tên Vật phẩm: " + it.template.name
                                + "\n|7|Chỉ số:"
                                + "\n|6|" + it.getInfo(),
                                "Chỉ số\nô 1", "Chỉ số\nô 2", "Chỉ số\nô 3",
                                "Chỉ số\nô 4", "Chỉ số\nô 5", "Chỉ số\nô 6",
                                "Chỉ số\nô 7", "Chỉ số\nô 8", "Chỉ số\nô 9",
                                "Chỉ số\nô 10", "Chỉ số\nô 11", "Chỉ số\nô 12");
                    }
                    break;
                    case ConstNpc.ADMIN_DANH_HIEU: {
                        switch (select) {
                            case 0:
                                if (player.lastTimeTitle1 == 0) {
                                    player.lastTimeTitle1 += System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 7);
                                } else {
                                    player.lastTimeTitle1 += (1000 * 60 * 60 * 24 * 7);
                                }
                                player.isTitleUse1 = true;
                                Service.getInstance().point(player);
                                Service.getInstance().sendTitle(player, 888);
                                Service.getInstance().sendThongBao(player, "Bạn nhận được 7 ngày Danh hiệu Đại Thần");
                                break;
                            case 1:
                                if (player.lastTimeTitle2 == 0) {
                                    player.lastTimeTitle2 += System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 7);
                                } else {
                                    player.lastTimeTitle2 += (1000 * 60 * 60 * 24 * 7);
                                }
                                player.isTitleUse2 = true;
                                Service.getInstance().point(player);
                                Service.getInstance().sendTitle(player, 889);
                                Service.getInstance().sendThongBao(player, "Bạn nhận được 7 ngày Danh hiệu Cần Thủ");
                                break;
                            case 2:
                                if (player.lastTimeTitle3 == 0) {
                                    player.lastTimeTitle3 += System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 7);
                                } else {
                                    player.lastTimeTitle3 += (1000 * 60 * 60 * 24 * 7);
                                }
                                player.isTitleUse3 = true;
                                Service.getInstance().point(player);
                                Service.getInstance().sendTitle(player, 890);
                                Service.getInstance().sendThongBao(player, "Bạn nhận được 7 ngày Danh hiệu Tuổi Thơ");
                                break;
                            case 3:
                                if (player.lastTimeTitle4 == 0) {
                                    player.lastTimeTitle4 += System.currentTimeMillis() + (1000 * 60 * 60 * 24 * 7);
                                } else {
                                    player.lastTimeTitle4 += (1000 * 60 * 60 * 24 * 7);
                                }
                                player.isTitleUse4 = true;
                                Service.getInstance().point(player);
                                Service.getInstance().sendTitle(player, 891);
                                Service.getInstance().sendThongBao(player, "Bạn nhận được 7 ngày Danh hiệu Thợ ngọc");
                                break;
                        }
                    }
                    break;

                    case ConstNpc.CHON_DANH_HIEU:
                        switch (select) {
                            case 0:
                                Service.getInstance().MenuDanhHieu1(player);
                                break;
                            case 1:
                                Service.getInstance().MenuDanhHieu2(player);
                                break;
                        }
                        break;
                    case ConstNpc.MENU_DANHHIEU_1: {
                        switch (select) {
                            case 0:
                                if (player.DH_VV_1 || player.lastTimeTitle1 > 0) {
                                    Service.getInstance().removeTitle(player);
                                    if (player.isTitleUse1) {
                                        player.isTitleUse1 = false;
                                    } else {
                                        player.isTitleUse1 = true;
                                    }
                                    Service.getInstance().point(player);
                                    Service.getInstance().sendThongBao(player, "Đã " + (player.isTitleUse1 == true ? "Bật" : "Tắt") + " Danh Hiệu!");
                                    for (int i = 888; i <= 892; i++) {
                                        Service.getInstance().sendTitle(player, i);
                                    }
                                    Service.getInstance().removeTitle(player);
                                    Service.getInstance().MenuDanhHieu1(player);
                                }
                                break;
                            case 1:
                                if (player.DH_VV_2 || player.lastTimeTitle2 > 0) {
                                    Service.getInstance().removeTitle(player);
                                    if (player.isTitleUse2) {
                                        player.isTitleUse2 = false;
                                    } else {
                                        player.isTitleUse2 = true;
                                    }
                                    Service.getInstance().point(player);
                                    Service.getInstance().sendThongBao(player, "Đã " + (player.isTitleUse2 == true ? "Bật" : "Tắt") + " Danh Hiệu!");
                                    for (int i = 888; i <= 892; i++) {
                                        Service.getInstance().sendTitle(player, i);
                                    }
                                    Service.getInstance().removeTitle(player);
                                    Service.getInstance().MenuDanhHieu1(player);
                                }
                                break;
                            case 2:
                                if (player.DH_VV_3 || player.lastTimeTitle3 > 0) {
                                    Service.getInstance().removeTitle(player);
                                    if (player.isTitleUse3) {
                                        player.isTitleUse3 = false;
                                    } else {
                                        player.isTitleUse3 = true;
                                    }
                                    Service.getInstance().point(player);
                                    Service.getInstance().sendThongBao(player, "Đã " + (player.isTitleUse3 == true ? "Bật" : "Tắt") + " Danh Hiệu!");
                                    for (int i = 888; i <= 892; i++) {
                                        Service.getInstance().sendTitle(player, i);
                                    }
                                    Service.getInstance().removeTitle(player);
                                    Service.getInstance().MenuDanhHieu1(player);
                                }
                                break;
                            case 3:
                                if (player.DH_VV_4 || player.lastTimeTitle4 > 0) {
                                    Service.getInstance().removeTitle(player);
                                    if (player.isTitleUse4) {
                                        player.isTitleUse4 = false;
                                    } else {
                                        player.isTitleUse4 = true;
                                    }
                                    Service.getInstance().point(player);
                                    Service.getInstance().sendThongBao(player, "Đã " + (player.isTitleUse4 == true ? "Bật" : "Tắt") + " Danh Hiệu!");
                                    for (int i = 888; i <= 892; i++) {
                                        Service.getInstance().sendTitle(player, i);
                                    }
                                    Service.getInstance().removeTitle(player);
                                    Service.getInstance().MenuDanhHieu1(player);
                                }
                                break;
                            case 4:
                                if (player.DH_VV_5 || player.lastTimeTitle5 > 0) {
                                    Service.getInstance().removeTitle(player);
                                    if (player.isTitleUse5) {
                                        player.isTitleUse5 = false;
                                    } else {
                                        player.isTitleUse5 = true;
                                    }
                                    Service.getInstance().point(player);
                                    Service.getInstance().sendThongBao(player, "Đã " + (player.isTitleUse5 == true ? "Bật" : "Tắt") + " Danh Hiệu!");
                                    for (int i = 888; i <= 892; i++) {
                                        Service.getInstance().sendTitle(player, i);
                                    }
                                    Service.getInstance().removeTitle(player);
                                    Service.getInstance().MenuDanhHieu1(player);
                                }
                                break;
                            case 5: {
                                if (player.isTitlePriority2) {
                                    Service.getInstance().sendThongBao(player, "Vui lòng tắt Ưu tiên Danh Hiệu Loại 2 trước!");
                                    break;
                                }
                                player.isTitlePriority1 = !player.isTitlePriority1;

                                Service.getInstance().sendThongBao(player,
                                        "Đã " + (player.isTitlePriority1 ? "Bật" : "Tắt") + " Ưu tiên Danh Hiệu Loại 1");
                                for (int i = 888; i <= 892; i++) {
                                    Service.getInstance().sendTitle(player, i);
                                }
                                Service.getInstance().removeTitle(player);
                                Service.getInstance().MenuDanhHieu1(player);
                                break;
                            }

                        }
                    }
                    break;
                    case ConstNpc.MENU_DANHHIEU_2: {
                        switch (select) {
                            case 0:
                                if (player.DH_VV_6) {
                                    Service.getInstance().removeTitle(player);
                                    if (player.isTitleUse6) {
                                        player.isTitleUse6 = false;
                                    } else {
                                        player.isTitleUse6 = true;
                                    }
                                    Service.getInstance().point(player);
                                    Service.getInstance().sendThongBao(player, "Đã " + (player.isTitleUse6 == true ? "Bật" : "Tắt") + " Danh Hiệu!");
                                    for (int i = 893; i <= 897; i++) {
                                        Service.getInstance().sendTitle(player, i);
                                    }
                                    Service.getInstance().removeTitle(player);
                                    Service.getInstance().MenuDanhHieu2(player);
                                }
                                break;
                            case 1:
                                if (player.DH_VV_7) {
                                    Service.getInstance().removeTitle(player);
                                    if (player.isTitleUse7) {
                                        player.isTitleUse7 = false;
                                    } else {
                                        player.isTitleUse7 = true;
                                    }
                                    Service.getInstance().point(player);
                                    Service.getInstance().sendThongBao(player, "Đã " + (player.isTitleUse7 == true ? "Bật" : "Tắt") + " Danh Hiệu!");
                                    for (int i = 893; i <= 897; i++) {
                                        Service.getInstance().sendTitle(player, i);
                                    }
                                    Service.getInstance().removeTitle(player);
                                    Service.getInstance().MenuDanhHieu2(player);
                                }
                                break;
                            case 2:
                                if (player.DH_VV_8) {
                                    Service.getInstance().removeTitle(player);
                                    if (player.isTitleUse8) {
                                        player.isTitleUse8 = false;
                                    } else {
                                        player.isTitleUse8 = true;
                                    }
                                    Service.getInstance().point(player);
                                    Service.getInstance().sendThongBao(player, "Đã " + (player.isTitleUse8 == true ? "Bật" : "Tắt") + " Danh Hiệu!");
                                    for (int i = 893; i <= 897; i++) {
                                        Service.getInstance().sendTitle(player, i);
                                    }
                                    Service.getInstance().removeTitle(player);
                                    Service.getInstance().MenuDanhHieu2(player);
                                }
                                break;
                            case 3:
                                if (player.DH_VV_9) {
                                    Service.getInstance().removeTitle(player);
                                    if (player.isTitleUse9) {
                                        player.isTitleUse9 = false;
                                    } else {
                                        player.isTitleUse9 = true;
                                    }
                                    Service.getInstance().point(player);
                                    Service.getInstance().sendThongBao(player, "Đã " + (player.isTitleUse9 == true ? "Bật" : "Tắt") + " Danh Hiệu!");
                                    for (int i = 893; i <= 897; i++) {
                                        Service.getInstance().sendTitle(player, i);
                                    }
                                    Service.getInstance().removeTitle(player);
                                    Service.getInstance().MenuDanhHieu2(player);
                                }
                                break;
                            case 4:
                                if (player.DH_VV_10) {
                                    Service.getInstance().removeTitle(player);
                                    if (player.isTitleUse10) {
                                        player.isTitleUse10 = false;
                                    } else {
                                        player.isTitleUse10 = true;
                                    }
                                    Service.getInstance().point(player);
                                    Service.getInstance().sendThongBao(player, "Đã " + (player.isTitleUse10 == true ? "Bật" : "Tắt") + " Danh Hiệu!");
                                    for (int i = 893; i <= 897; i++) {
                                        Service.getInstance().sendTitle(player, i);
                                    }
                                    Service.getInstance().removeTitle(player);
                                    Service.getInstance().MenuDanhHieu2(player);
                                }
                                break;
                            case 5: {
                                if (player.isTitlePriority1) {
                                    Service.getInstance().sendThongBao(player, "Vui lòng tắt Ưu tiên Danh Hiệu Loại 1 trước!");
                                    break;
                                }
                                player.isTitlePriority2 = !player.isTitlePriority2;
                                Service.getInstance().sendThongBao(player,
                                        "Đã " + (player.isTitlePriority2 ? "Bật" : "Tắt") + " Ưu tiên Danh Hiệu Loại 2");
                                for (int i = 893; i <= 897; i++) {
                                    Service.getInstance().sendTitle(player, i);
                                }
                                Service.getInstance().removeTitle(player);
                                Service.getInstance().MenuDanhHieu2(player);
                                break;
                            }
                        }
                    }
                    break;

                    case ConstNpc.CONFIRM_REMOVE_ALL_ITEM_LUCKY_ROUND:
                        if (select == 0) {
                            for (int i = 0; i < player.inventory.itemsBoxCrackBall.size(); i++) {
                                player.inventory.itemsBoxCrackBall.set(i, ItemService.gI().createItemNull());
                            }
                            Service.getInstance().sendThongBao(player, "Đã xóa hết vật phẩm trong rương");
                        }
                        break;
                    case ConstNpc.CONFIRM_REMOVE_ALL_ITEM_MAIL_BOX:
                        if (select == 0) {
                            for (int i = 0; i < player.inventory.itemsMailBox.size(); i++) {
                                player.inventory.itemsMailBox.set(i, ItemService.gI().createItemNull());
                            }
                            Service.getInstance().sendThongBao(player, "Đã xóa hết vật phẩm trong rương");
                        }
                        break;
                    case ConstNpc.MENU_FIND_PLAYER:
                        Player p = (Player) PLAYERID_OBJECT.get(player.id);
                        if (p != null) {
                            switch (select) {
                                case 0:
                                    if (p.zone != null) {
                                        ChangeMapService.gI().changeMapYardrat(player, p.zone, p.location.x,
                                                p.location.y);
                                    }
                                    break;
                                case 1:
                                    if (p.zone != null) {
                                        ChangeMapService.gI().changeMap(p, player.zone, player.location.x,
                                                player.location.y);
                                    }
                                    break;
                                case 2:
                                    if (p != null) {
                                        Input.gI().createFormChangeName(player, p);
                                    }
                                    break;
                                case 3:
                                    if (p != null) {
                                        String[] selects = new String[]{"Đồng ý", "Hủy"};
                                        NpcService.gI().createMenuConMeo(player, ConstNpc.BAN_PLAYER, -1,
                                                "Bạn có chắc chắn muốn ban " + p.name, selects, p);
                                    }
                                    break;
                            }
                        }
                        break;
                    case ConstNpc.MO_RONG_RUONG_SUU_TAM:
                        if (select == 0) {
                            Item thoiVang = InventoryService.gI().findItemBagByTemp(player, (short) RuongSuuTam.ID_TEMP);

                            List<Item> listItem = new ArrayList<>();
                            if (player.typeMoRuong == 0) {
                                listItem = player.ruongSuuTam.RuongCaiTrang;
                            } else if (player.typeMoRuong == 1) {
                                listItem = player.ruongSuuTam.RuongPhuKien;
                            } else if (player.typeMoRuong == 2) {
                                listItem = player.ruongSuuTam.RuongPet;
                            } else if (player.typeMoRuong == 3) {
                                listItem = player.ruongSuuTam.RuongLinhThu;
                            } else if (player.typeMoRuong == 4) {
                                listItem = player.ruongSuuTam.RuongThuCuoi;
                            }
                            if (listItem == null) {
                                Service.getInstance().sendThongBao(player, "Không thể thực hiện");
                                return;
                            }
                            if (listItem.size() >= RuongSuuTam.MAX_SIZE) {
                                Service.getInstance().sendThongBao(player, "Rương Sưu Tầm đã đạt giới hạn tối đa");
                                return;
                            }
                            if (thoiVang != null && thoiVang.quantity >= RuongSuuTam.QUATITY) {
                                listItem.add(ItemService.gI().createItemNull());
                                InventoryService.gI().subQuantityItemsBag(player, thoiVang, RuongSuuTam.QUATITY);
                                InventoryService.gI().sendItemBags(player);
                                RuongSuuTam.gI().SendAllRuong(player);
                                Service.getInstance().sendThongBao(player, "Mở thêm 1 ô " + RuongSuuTam.gI().nameRuong(player, player.typeMoRuong) + " Thành công");
                            } else {
                                Service.getInstance().sendThongBao(player, "Không đủ " + RuongSuuTam.QUATITY + " " + (thoiVang != null && thoiVang.template != null ? thoiVang.template.name : "vật phẩm") + "!");
                            }
                        }
                        break;
                    case ConstNpc.NANG_CAP_KHAM_NGOC:
                        if (select == 0) {
                            int nro = player.nroKhamNgoc;
                            int max_quatity = player.slItem;
                            int idItem = player.idTempNangCap;
                            KhamNgocPlayer manager = player.khamNgoc.get(nro);
                            Item item = InventoryService.gI().findItemBagByTemp(player, (short) idItem);
                            Item it = ItemService.gI().createNewItem((short) idItem);
                            if (item == null || item.quantity < max_quatity) {
                                Service.getInstance().sendThongBao(player, "Không đủ nguyên liệu. Còn thiếu " + (item == null ? max_quatity : (max_quatity - item.quantity)) + " " + it.template.name);
                                return;
                            }
                            InventoryService.gI().subQuantityItemsBag(player, item, max_quatity);
                            manager.levelNro++;
                            InventoryService.gI().sendItemBags(player);
                            player.nPoint.calPoint();
                            Service.getInstance().point(player);
                            KhamNgoc.gI().Send_KhamNgoc_Player(player);
                            if (manager.levelNro == 0) {
                                Service.getInstance().sendThongBao(player, "|2|Kích hoạt thành công Ngọc rồng " + (nro + 1) + " sao");
                            } else {
                                Service.getInstance().sendThongBao(player, "|2|Nâng thành công Ngọc rồng " + (nro + 1) + " sao lên Cấp " + manager.levelNro);
                            }
                        }
                        break;
                    case ConstNpc.DIEU_CHE:
                        if (select == 0) {
                            PhongThiNghiem ptn = PhongThiNghiem.PHONG_THI_NGHIEM.get(player.typeBinhDieuChe);
                            for (int i = 0; i < ptn.items.size(); i++) {
                                Item item = InventoryService.gI().findItemBagByTemp(player, (short) ptn.items.get(i).tempId);
                                InventoryService.gI().subQuantityItemsBag(player, item, ptn.items.get(i).quantity);
                            }
                            InventoryService.gI().sendItemBags(player);
                            player.phongThiNghiem.get(player.vitriBinhDieuChe).idBinh = player.typeBinhDieuChe;
                            player.phongThiNghiem.get(player.vitriBinhDieuChe).timeCheTao = System.currentTimeMillis() + (ptn.thoi_gian * 1000 * 60);
                            PhongThiNghiem.gI().Send_PhongThiNghiem_Player(player);
                        }
                        break;
                    case ConstNpc.MO_RONG_PHONG_THI_NGHIEM:
                        if (select == 0) {
                            Item thoiVang = InventoryService.gI().findItemBagByTemp(player, (short) PhongThiNghiem.ID_ITEM_MO_RONG);
                            if (player.phongThiNghiem.size() >= PhongThiNghiem.MAX_SIZE) {
                                Service.getInstance().sendThongBao(player, "Phòng Thí Nghiệm đã đạt giới hạn tối đa");
                                return;
                            }
                            if (thoiVang != null && thoiVang.quantity >= PhongThiNghiem.SO_LUONG) {
                                PhongThiNghiem_Player ptnPl = new PhongThiNghiem_Player();
                                ptnPl.idBinh = -1;
                                ptnPl.timeCheTao = 0;
                                player.phongThiNghiem.add(ptnPl);
                                InventoryService.gI().subQuantityItemsBag(player, thoiVang, PhongThiNghiem.SO_LUONG);
                                InventoryService.gI().sendItemBags(player);
                                PhongThiNghiem.gI().Send_PhongThiNghiem_Player(player);
                                Service.getInstance().sendThongBao(player, "Mở thêm 1 ô Thành công");
                            } else {
                                Service.getInstance().sendThongBao(player, "Không đủ " + PhongThiNghiem.SO_LUONG + " " + ConstItem.NAME_XU_SERVER);
                            }
                        }
                        break;
                    case ConstNpc.HUY_PTN:
                        if (select == 0) {
                            PhongThiNghiem ptn = PhongThiNghiem.PHONG_THI_NGHIEM.get(player.typeBinhDieuChe);
                            String text = "";
                            for (int i = 0; i < ptn.items.size(); i++) {
                                Item it = ItemService.gI().createNewItem((short) ptn.items.get(i).tempId);
                                it.quantity = ptn.items.get(i).quantity;
                                text += "|5|-" + it.quantity + " " + it.template.name + (i == (ptn.items.size() - 1) ? "" : "\n");
                                InventoryService.gI().addItemBag(player, it, 0);
                            }
                            InventoryService.gI().sendItemBags(player);
                            player.phongThiNghiem.get(player.vitriBinhDieuChe).idBinh = -1;
                            player.phongThiNghiem.get(player.vitriBinhDieuChe).timeCheTao = 0;
                            PhongThiNghiem.gI().Send_PhongThiNghiem_Player(player);
                            Service.getInstance().sendThongBao(player, "Hủy Thành công " + ptn.name_binh
                                    + "\n|3|Nhận lại vật phẩm:"
                                    + "\n" + text);
                        }
                        break;
                    case ConstNpc.TANG_TOC:
                        if (select == 0) {
                            Item item = InventoryService.gI().findItemBagByTemp(player, (short) PhongThiNghiem.ID_ITEM_TANG_TOC);
                            if (item == null || item.quantity < PhongThiNghiem.SO_LUONG_TANG_TOC) {
                                Service.getInstance().sendThongBao(player, "Không đủ nguyên liệu");
                                return;
                            }
                            InventoryService.gI().subQuantityItemsBag(player, item, PhongThiNghiem.SO_LUONG_TANG_TOC);
                            InventoryService.gI().sendItemBags(player);
                            PhongThiNghiem ptn = PhongThiNghiem.PHONG_THI_NGHIEM.get(player.typeBinhDieuChe);
                            player.phongThiNghiem.get(player.vitriBinhDieuChe).timeCheTao -= PhongThiNghiem.TIME_TANG_TOC;
                            PhongThiNghiem.gI().Send_PhongThiNghiem_Player(player);
                            Service.getInstance().sendThongBao(player, "Tăng tốc Thành công " + ptn.name_binh + ". Giảm " + Util.formatTimeString2(PhongThiNghiem.TIME_TANG_TOC) + " thởi gian điều chế");
                        }
                        break;
                    case ConstNpc.MENU_TU_TIEN:
                        if (player.isTuTien) {
                            switch (select) {
                                case 0:
                                    Service.getInstance().loadTuTien(player);
                                    break;
                                case 1:
                                    NpcService.gI().createMenuConMeo(player, ConstNpc.CHI_SO_TU_TIEN, 0,
                                            "|7|CHỈ SỐ TU TIÊN"
                                            + "\n|2|HP: +" + Service.getInstance().nPointTuTien(player.levelTuTien, player.tangTuTien) + "%"
                                            + "\nKI: +" + Service.getInstance().nPointTuTien(player.levelTuTien, player.tangTuTien) + "%"
                                            + "\nSD: +" + Service.getInstance().nPointTuTien(player.levelTuTien, player.tangTuTien) + "%"
                                            + "\n|1|Bonus: +" + player.bonusTuTien + "% HP, KI, SD",
                                            "Quay lại");
                                    break;
                                case 2:
                                    if (player.levelTuTien < 10) {
                                        if (player.tangTuTien == 10) {
                                            NpcService.gI().createMenuConMeo(player, ConstNpc.DOT_PHA_TU_TIEN, 0,
                                                    "|7|ĐỘt PHÁ TU TIÊN"
                                                    + "\nYêu cầu: Tầng 10 mỗi Cấp bậc"
                                                    + "\n|2|Cấp bậc hiện tại: " + Service.getInstance().bacTuTien(player.levelTuTien)
                                                    + "\n|1|Cấp bậc tiếp theo: " + Service.getInstance().bacTuTien(player.levelTuTien + 1)
                                                    + "\nTầng: " + (player.tangTuTien) + " (" + (player.expTuTien * 100) / Service.getInstance().expDotPhaTang(player.levelTuTien, player.tangTuTien) + "%)"
                                                    + "\n|4|Chỉ số hiện tại: +" + Service.getInstance().nPointTuTien(player.levelTuTien, player.tangTuTien) + "%HP, KI, SD"
                                                    + "\nChỉ số sau khi đột phá: +" + Service.getInstance().nPointTuTien(player.levelTuTien + 1, 1) + "%HP, KI, SD"
                                                    + "\n|2|-Nguyên liệu cần: "
                                                    + "\n|5|x" + Service.getInstance().daDotPhaTuTien(player.levelTuTien) + " Lôi Thần Thạch"
                                                    + "\n|5|x" + Service.getInstance().daDotPhaTuTien(player.levelTuTien) + " Thổ Thần Thạch"
                                                    + "\n|5|x" + Service.getInstance().daDotPhaTuTien(player.levelTuTien) + " Hoả Thần Thạch"
                                                    + "\n|3|(Sau khi Đột phá, sẽ tăng chỉ số, Exp về 0 và bắt đầu lại bậc 1 của Cấp bậc mới)",
                                                    "Đột phá");
                                        } else {
                                            Service.getInstance().sendThongBao(player, "Cần đạt Tầng 10 " + Service.getInstance().bacTuTien(player.levelTuTien));
                                            Service.getInstance().loadTuTien(player);
                                        }
                                    }
                                    break;
                                case 3:
                                    if (player.thongBaoExp) {
                                        player.thongBaoExp = false;
                                        Service.getInstance().sendThongBao(player, "|7|Đã Tắt thông báo Exp nhận được khi đánh quái");
                                    } else {
                                        player.thongBaoExp = true;
                                        Service.getInstance().sendThongBao(player, "|2|Đã Bật thông báo Exp nhận được khi đánh quái");
                                    }
                                    break;
                            }
                        } else {
                            switch (select) {
                                case 0:
                                    Item item = InventoryService.gI().findItemBagByTemp(player, (short) ConstItem.XU_SERVER);
                                    if (item == null || item.quantity < 10000) {
                                        Service.getInstance().sendThongBao(player, "Không đủ nguyên liệu");
                                        return;
                                    }
                                    player.isTuTien = true;
                                    player.tangTuTien = 1;
                                    InventoryService.gI().subQuantityItemsBag(player, item, 10000);
                                    InventoryService.gI().sendItemBags(player);
                                    Service.getInstance().sendThongBao(player, "Mở khoá thành công tính năng Tu Tiên");
                                    break;
                            }
                        }
                        break;
                    case ConstNpc.CHI_SO_TU_TIEN:
                        Service.getInstance().loadTuTien(player);
                        break;
                    case ConstNpc.DOT_PHA_TU_TIEN:
                        switch (select) {
                            case 0:
                                Item loi = InventoryService.gI().findItemBagByTemp(player, (short) 1919);
                                Item tho = InventoryService.gI().findItemBagByTemp(player, (short) 1920);
                                Item hoa = InventoryService.gI().findItemBagByTemp(player, (short) 1921);
                                if (loi == null || tho == null || hoa == null
                                        || loi.quantity < Service.getInstance().daDotPhaTuTien(player.levelTuTien)
                                        || tho.quantity < Service.getInstance().daDotPhaTuTien(player.levelTuTien)
                                        || hoa.quantity < Service.getInstance().daDotPhaTuTien(player.levelTuTien)) {
                                    Service.getInstance().sendThongBao(player, "Không đủ nguyên liệu");
                                    return;
                                }
                                if (player.tangTuTien != 10) {
                                    Service.getInstance().sendThongBao(player, "Cần đạt Tầng 10 " + Service.getInstance().bacTuTien(player.levelTuTien));
                                    return;
                                }
                                if (player.levelTuTien >= 10) {
                                    Service.getInstance().sendThongBao(player, "Đã đạt cấp tối đa");
                                    return;
                                }
                                player.levelTuTien++;
                                player.tangTuTien = 1;
                                player.expTuTien = 0;
                                InventoryService.gI().subQuantityItemsBag(player, loi, Service.getInstance().daDotPhaTuTien(player.levelTuTien));
                                InventoryService.gI().subQuantityItemsBag(player, tho, Service.getInstance().daDotPhaTuTien(player.levelTuTien));
                                InventoryService.gI().subQuantityItemsBag(player, hoa, Service.getInstance().daDotPhaTuTien(player.levelTuTien));
                                InventoryService.gI().sendItemBags(player);
                                Service.getInstance().point(player);
                                Service.getInstance().sendThongBao(player, "Đột phá thành công Cấp bậc " + Service.getInstance().bacTuTien(player.levelTuTien));
                                break;
                        }
                        break;
                }
            }
        };
    }

    public static void openMenuSuKien(Player player, Npc npc, int tempId, int select) {
        switch (Manager.EVENT_SEVER) {
            case 0:
                break;
            case 1:// hlw
                switch (select) {
                    case 0:
                        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                            Item keo = InventoryService.gI().finditemnguyenlieuKeo(player);
                            Item banh = InventoryService.gI().finditemnguyenlieuBanh(player);
                            Item bingo = InventoryService.gI().finditemnguyenlieuBingo(player);

                            if (keo != null && banh != null && bingo != null) {
                                Item GioBingo = ItemService.gI().createNewItem((short) 2016, 1);

                                // - Số item sự kiện có trong rương
                                InventoryService.gI().subQuantityItemsBag(player, keo, 10);
                                InventoryService.gI().subQuantityItemsBag(player, banh, 10);
                                InventoryService.gI().subQuantityItemsBag(player, bingo, 10);

                                GioBingo.itemOptions.add(new ItemOption(74, 0));
                                InventoryService.gI().addItemBag(player, GioBingo, 0);
                                InventoryService.gI().sendItemBags(player);
                                Service.getInstance().sendThongBao(player, "Đổi quà sự kiện thành công");
                            } else {
                                Service.getInstance().sendThongBao(player,
                                        "Vui lòng chuẩn bị x10 Nguyên Liệu Kẹo, Bánh Quy, Bí Ngô để đổi vật phẩm sự kiện");
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Hành trang đầy.");
                        }
                        break;
                    case 1:
                        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                            Item ve = InventoryService.gI().finditemnguyenlieuVe(player);
                            Item giokeo = InventoryService.gI().finditemnguyenlieuGiokeo(player);

                            if (ve != null && giokeo != null) {
                                Item Hopmaquy = ItemService.gI().createNewItem((short) 2017, 1);
                                // - Số item sự kiện có trong rương
                                InventoryService.gI().subQuantityItemsBag(player, ve, 3);
                                InventoryService.gI().subQuantityItemsBag(player, giokeo, 3);

                                Hopmaquy.itemOptions.add(new ItemOption(74, 0));
                                InventoryService.gI().addItemBag(player, Hopmaquy, 0);
                                InventoryService.gI().sendItemBags(player);
                                Service.getInstance().sendThongBao(player, "Đổi quà sự kiện thành công");
                            } else {
                                Service.getInstance().sendThongBao(player,
                                        "Vui lòng chuẩn bị x3 Vé đổi Kẹo và x3 Giỏ kẹo để đổi vật phẩm sự kiện");
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Hành trang đầy.");
                        }
                        break;
                    case 2:
                        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                            Item ve = InventoryService.gI().finditemnguyenlieuVe(player);
                            Item giokeo = InventoryService.gI().finditemnguyenlieuGiokeo(player);
                            Item hopmaquy = InventoryService.gI().finditemnguyenlieuHopmaquy(player);

                            if (ve != null && giokeo != null && hopmaquy != null) {
                                Item HopQuaHLW = ItemService.gI().createNewItem((short) 2012, 1);
                                // - Số item sự kiện có trong rương
                                InventoryService.gI().subQuantityItemsBag(player, ve, 3);
                                InventoryService.gI().subQuantityItemsBag(player, giokeo, 3);
                                InventoryService.gI().subQuantityItemsBag(player, hopmaquy, 3);

                                HopQuaHLW.itemOptions.add(new ItemOption(74, 0));
                                HopQuaHLW.itemOptions.add(new ItemOption(30, 0));
                                InventoryService.gI().addItemBag(player, HopQuaHLW, 0);
                                InventoryService.gI().sendItemBags(player);
                                Service.getInstance().sendThongBao(player,
                                        "Đổi quà hộp quà sự kiện Halloween thành công");
                            } else {
                                Service.getInstance().sendThongBao(player,
                                        "Vui lòng chuẩn bị x3 Hộp Ma Quỷ, x3 Vé đổi Kẹo và x3 Giỏ kẹo để đổi vật phẩm sự kiện");
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Hành trang đầy.");
                        }
                        break;
                }
                break;
            case 2:// 20/11
                switch (select) {
                    case 3:
                        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                            int evPoint = player.event.getEventPoint();
                            if (evPoint >= 999) {
                                Item HopQua = ItemService.gI().createNewItem((short) 2021, 1);
                                player.event.setEventPoint(evPoint - 999);

                                HopQua.itemOptions.add(new ItemOption(74, 0));
                                HopQua.itemOptions.add(new ItemOption(30, 0));
                                InventoryService.gI().addItemBag(player, HopQua, 0);
                                InventoryService.gI().sendItemBags(player);
                                Service.getInstance().sendThongBao(player, "Bạn nhận được Hộp Quà Teacher Day");
                            } else {
                                Service.getInstance().sendThongBao(player, "Cần 999 điểm tích lũy để đổi");
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Hành trang đầy.");
                        }
                        break;
                    // case 4:
                    // ShopService.gI().openShopSpecial(player, npc, ConstNpc.SHOP_HONG_NGOC, 0,
                    // -1);
                    // break;
                    default:
                        int n = 0;
                        switch (select) {
                            case 0:
                                n = 1;
                                break;
                            case 1:
                                n = 10;
                                break;
                            case 2:
                                n = 99;
                                break;
                        }

                        if (n > 0) {
                            Item bonghoa = InventoryService.gI().finditemBongHoa(player, n);
                            if (bonghoa != null) {
                                int evPoint = player.event.getEventPoint();
                                player.event.setEventPoint(evPoint + n);
                                ;
                                InventoryService.gI().subQuantityItemsBag(player, bonghoa, n);
                                Service.getInstance().sendThongBao(player, "Bạn nhận được " + n + " điểm sự kiện");
                                int pre;
                                int next;
                                String text = null;
                                AttributeManager am = ServerManager.gI().getAttributeManager();
                                switch (tempId) {
                                    case ConstNpc.THAN_MEO_KARIN:
                                        pre = EVENT_COUNT_THAN_MEO / 999;
                                        EVENT_COUNT_THAN_MEO += n;
                                        next = EVENT_COUNT_THAN_MEO / 999;
                                        if (pre != next) {
                                            am.setTime(ConstAttribute.TNSM, 3600);
                                            text = "Toàn bộ máy chủ tăng được 20% TNSM cho đệ tử khi đánh quái trong 60 phút.";
                                        }
                                        break;

                                    case ConstNpc.QUY_LAO_KAME:
                                        pre = EVENT_COUNT_QUY_LAO_KAME / 999;
                                        EVENT_COUNT_QUY_LAO_KAME += n;
                                        next = EVENT_COUNT_QUY_LAO_KAME / 999;
                                        if (pre != next) {
                                            am.setTime(ConstAttribute.VANG, 3600);
                                            text = "Toàn bộ máy chủ được tăng 100% vàng từ quái trong 60 phút.";
                                        }
                                        break;

                                    case ConstNpc.THUONG_DE:
                                        pre = EVENT_COUNT_THUONG_DE / 999;
                                        EVENT_COUNT_THUONG_DE += n;
                                        next = EVENT_COUNT_THUONG_DE / 999;
                                        if (pre != next) {
                                            am.setTime(ConstAttribute.KI, 3600);
                                            text = "Toàn bộ máy chủ được tăng 20% KI trong 60 phút.";
                                        }
                                        break;

                                    case ConstNpc.THAN_VU_TRU:
                                        pre = EVENT_COUNT_THAN_VU_TRU / 999;
                                        EVENT_COUNT_THAN_VU_TRU += n;
                                        next = EVENT_COUNT_THAN_VU_TRU / 999;
                                        if (pre != next) {
                                            am.setTime(ConstAttribute.HP, 3600);
                                            text = "Toàn bộ máy chủ được tăng 20% HP trong 60 phút.";
                                        }
                                        break;

                                    case ConstNpc.BILL:
                                        pre = EVENT_COUNT_THAN_HUY_DIET / 999;
                                        EVENT_COUNT_THAN_HUY_DIET += n;
                                        next = EVENT_COUNT_THAN_HUY_DIET / 999;
                                        if (pre != next) {
                                            am.setTime(ConstAttribute.SUC_DANH, 3600);
                                            text = "Toàn bộ máy chủ được tăng 20% Sức đánh trong 60 phút.";
                                        }
                                        break;
                                }
                                if (text != null) {
                                    Service.getInstance().sendThongBaoAllPlayer(text);
                                }

                            } else {
                                Service.getInstance().sendThongBao(player,
                                        "Cần ít nhất " + n + " bông hoa để có thể tặng");
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Cần ít nhất " + n + " bông hoa để có thể tặng");
                        }
                }
                break;
            case 3:
                if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                    Item keogiangsinh = InventoryService.gI().finditemKeoGiangSinh(player);

                    if (keogiangsinh != null && keogiangsinh.quantity >= 99) {
                        Item tatgiangsinh = ItemService.gI().createNewItem((short) 649, 1);
                        // - Số item sự kiện có trong rương
                        InventoryService.gI().subQuantityItemsBag(player, keogiangsinh, 99);

                        tatgiangsinh.itemOptions.add(new ItemOption(74, 0));
                        tatgiangsinh.itemOptions.add(new ItemOption(30, 0));
                        InventoryService.gI().addItemBag(player, tatgiangsinh, 0);
                        InventoryService.gI().sendItemBags(player);
                        Service.getInstance().sendThongBao(player, "Bạn nhận được Tất,vớ giáng sinh");
                    } else {
                        Service.getInstance().sendThongBao(player,
                                "Vui lòng chuẩn bị x99 kẹo giáng sinh để đổi vớ tất giáng sinh");
                    }
                } else {
                    Service.getInstance().sendThongBao(player, "Hành trang đầy.");
                }
                break;
            case 4:
                switch (select) {
                    case 0:
                        if (!player.event.isReceivedLuckyMoney()) {
                            Calendar cal = Calendar.getInstance();
                            int day = cal.get(Calendar.DAY_OF_MONTH);
                            if (day >= 22 && day <= 24) {
                                Item goldBar = ItemService.gI().createNewItem((short) ConstItem.THOI_VANG,
                                        Util.nextInt(1, 3));
                                player.inventory.ruby += Util.nextInt(10, 30);
                                goldBar.quantity = Util.nextInt(1, 3);
                                InventoryService.gI().addItemBag(player, goldBar, 99999);
                                InventoryService.gI().sendItemBags(player);
                                PlayerService.gI().sendInfoHpMpMoney(player);
                                player.event.setReceivedLuckyMoney(true);
                                Service.getInstance().sendThongBao(player,
                                        "Nhận lì xì thành công,chúc bạn năm mới dui dẻ");
                            } else if (day > 24) {
                                Service.getInstance().sendThongBao(player, "Hết tết rồi còn đòi lì xì");
                            } else {
                                Service.getInstance().sendThongBao(player, "Đã tết đâu mà đòi lì xì");
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Bạn đã nhận lì xì rồi");
                        }
                        break;
                    case 1:
                        ShopService.gI().openShopNormal(player, npc, ConstNpc.SHOP_SU_KIEN_TET, 0, -1);
                        break;
                }
                break;
            case ConstEvent.SU_KIEN_8_3:
                switch (select) {
                    case 3:
                        if (InventoryService.gI().getCountEmptyBag(player) > 0) {
                            int evPoint = player.event.getEventPoint();
                            if (evPoint >= 999) {
                                Item capsule = ItemService.gI().createNewItem((short) 2052, 1);
                                player.event.setEventPoint(evPoint - 999);

                                capsule.itemOptions.add(new ItemOption(74, 0));
                                capsule.itemOptions.add(new ItemOption(30, 0));
                                InventoryService.gI().addItemBag(player, capsule, 0);
                                InventoryService.gI().sendItemBags(player);
                                Service.getInstance().sendThongBao(player, "Bạn nhận được Capsule Hồng");
                            } else {
                                Service.getInstance().sendThongBao(player, "Cần 999 điểm tích lũy để đổi");
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Hành trang đầy.");
                        }
                        break;
                    default:
                        int n = 0;
                        switch (select) {
                            case 0:
                                n = 1;
                                break;
                            case 1:
                                n = 10;
                                break;
                            case 2:
                                n = 99;
                                break;
                        }

                        if (n > 0) {
                            Item bonghoa = InventoryService.gI().finditemBongHoa(player, n);
                            if (bonghoa != null) {
                                int evPoint = player.event.getEventPoint();
                                player.event.setEventPoint(evPoint + n);
                                InventoryService.gI().subQuantityItemsBag(player, bonghoa, n);
                                Service.getInstance().sendThongBao(player, "Bạn nhận được " + n + " điểm sự kiện");
                            } else {
                                Service.getInstance().sendThongBao(player,
                                        "Cần ít nhất " + n + " bông hoa để có thể tặng");
                            }
                        } else {
                            Service.getInstance().sendThongBao(player, "Cần ít nhất " + n + " bông hoa để có thể tặng");
                        }
                }
                break;
        }
    }

    public static String getMenuSuKien(int id) {
        switch (id) {
            case ConstEvent.KHONG_CO_SU_KIEN:
                return "Chưa có\n Sự Kiện";
            case ConstEvent.SU_KIEN_HALLOWEEN:
                return "Sự Kiện\nHaloween";
            case ConstEvent.SU_KIEN_20_11:
                return "Sự Kiện\n 20/11";
            case ConstEvent.SU_KIEN_NOEL:
                return "Sự Kiện\n Giáng Sinh";
            case ConstEvent.SU_KIEN_TET:
                return "Sự Kiện\n Tết Nguyên\nĐán 2024";
            case ConstEvent.SU_KIEN_8_3:
                return "Sự Kiện\n 8/3";
        }
        return "Chưa có\n Sự Kiện";
    }

    public static String getMenuLamBanh(Player player, int type) {
        switch (type) {
            case 0:// bánh tét
                if (player.event.isCookingTetCake()) {
                    int timeCookTetCake = player.event.getTimeCookTetCake();
                    if (timeCookTetCake == 0) {
                        return "Lấy bánh";
                    } else if (timeCookTetCake > 0) {
                        return "Đang nấu\nBánh Tét\nCòn " + TimeUtil.secToTime(timeCookTetCake);
                    }
                } else {
                    return "Nấu\nBánh Tét";
                }
                break;
            case 1:// bánh chưng
                if (player.event.isCookingChungCake()) {
                    int timeCookChungCake = player.event.getTimeCookChungCake();
                    if (timeCookChungCake == 0) {
                        return "Lấy bánh";
                    } else if (timeCookChungCake > 0) {
                        return "Đang nấu\nBánh Chưng\nCòn " + TimeUtil.secToTime(timeCookChungCake);
                    }
                } else {
                    return "Nấu\nBánh Chưng";
                }
                break;
        }
        return "";
    }
}
