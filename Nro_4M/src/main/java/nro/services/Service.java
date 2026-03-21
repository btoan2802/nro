package nro.services;

import com.sun.management.OperatingSystemMXBean;
import editor.Final_Static;
import java.io.DataOutputStream;
import nro.consts.Cmd;
import nro.consts.ConstNpc;
import nro.consts.ConstPlayer;
import nro.data.DataGame;
import nro.jdbc.daos.AccountDAO;
import nro.manager.TopManager;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.map.Zone;
import nro.models.map.dungeon.zones.ZDungeon;
import nro.models.mob.Mob;
import nro.models.player.Pet;
import nro.models.player.Player;
import nro.models.skill.Skill;
import nro.power.Caption;
import nro.power.CaptionManager;
import nro.server.Client;
import nro.server.Manager;
import nro.server.io.Message;
import nro.server.io.Session;
import nro.services.func.Input;
import nro.utils.FileIO;
import nro.utils.Log;
import nro.utils.TimeUtil;
import nro.utils.Util;

import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;
import nro.jdbc.daos.GodGK;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import nro.consts.ConstItem;
import nro.consts.ConstNpc;
import nro.jdbc.DBService;
import nro.jdbc.daos.ShopDAO;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.player.Player;
import nro.server.io.Message;
import nro.utils.Log;
import nro.utils.Util;
import org.json.JSONArray;
import org.json.JSONObject;
import nro.manager.PetFollowManager;
import nro.manager.TopLeoThapManager;
import nro.models.boss.Boss;
import nro.models.boss.BossManager;
import nro.models.player.MiniPet;
import nro.models.player.PetFollow;
import nro.models.pvp.TOP;
import nro.models.skill.NClass;
import nro.models.skill.SkillTemplate;
import nro.resources.Resources;
import static nro.server.Manager.SHOPS;
import nro.services.func.ChangeMapService;
import nro.services.func.ShopService;
import nro.services.func.TaiXiu;
import nro.services.func.TaiXiu_Xu;
import nro.utils.FileUtils;

public class Service {

    public int TOP_SUCMANH = 1;
    public int TOP_DETU = 2;
    public int TOP_NHIEMVU = 3;
    public int TOP_NAP = 4;

    public int soluongBot = 0;

    private static Service instance;

    public static Service getInstance() {
        if (instance == null) {
            instance = new Service();
        }
        return instance;
    }

    public void sendMessAllPlayer(Message msg) {
        msg.transformData();
        PlayerService.gI().sendMessageAllPlayer(msg);
    }

    public void sendMessAllPlayerIgnoreMe(Player player, Message msg) {
        msg.transformData();
        PlayerService.gI().sendMessageIgnore(player, msg);
    }

    public void sendMessAllPlayerInMap(Zone zone, Message msg) {
        msg.transformData();
        if (zone != null) {
            List<Player> players = zone.getPlayers();
            synchronized (players) {
                for (Player pl : players) {
                    if (pl != null) {
                        pl.sendMessage(msg);
                    }
                }
            }
            msg.cleanup();
        }
    }

    public void sendMessAllPlayerInMap(Player player, Message msg) {
        msg.transformData();
        if (player.zone != null) {
            if (player.zone.map.isMapOffline) {
                if (player.isPet) {
                    ((Pet) player).master.sendMessage(msg);
                } else {
                    player.sendMessage(msg);
                }
            } else {
                List<Player> players = player.zone.getPlayers();
                synchronized (players) {
                    for (Player pl : players) {
                        if (pl != null) {
                            pl.sendMessage(msg);
                        }
                    }
                }

                msg.cleanup();
            }
        }
    }

    public void sendMessAnotherNotMeInMap(Player player, Message msg) {
        if (player.zone != null) {
            List<Player> players = player.zone.getPlayers();
            synchronized (players) {
                for (Player pl : players) {
                    if (pl != null && !pl.equals(player)) {
                        pl.sendMessage(msg);
                    }
                }
            }

            msg.cleanup();
        }
    }

    public void Send_Info_NV(Player pl) {
        Message msg;
        try {
            msg = Service.getInstance().messageSubCommand((byte) 14);//Cập nhật máu
            msg.writer().writeInt((int) pl.id);
            msg.writeDouble(pl.nPoint.hp);
            msg.writer().writeByte(0);//Hiệu ứng Ăn Đậu
            msg.writeDouble(pl.nPoint.hpMax);
            sendMessAnotherNotMeInMap(pl, msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    public void sendInfoPlayerEatPea(Player pl) {
        Message msg;
        try {
            msg = Service.getInstance().messageSubCommand((byte) 14);
            msg.writer().writeInt((int) pl.id);
            msg.writeDouble(pl.nPoint.hp);
            msg.writer().writeByte(1);
            msg.writeDouble(pl.nPoint.hpMax);
            sendMessAnotherNotMeInMap(pl, msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    public void sendPopUpMultiLine(Player pl, int tempID, int avt, String text) {
        Message msg = null;
        try {
            msg = new Message(-218);
            msg.writer().writeShort(tempID);
            msg.writer().writeUTF(text);
            msg.writer().writeShort(avt);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
//            e.printStackTrace();
        } finally {
            if (msg != null) {
                msg.cleanup();
                msg = null;
            }
        }
    }

    public void loginDe(Session session, short second) {
        Message msg;
        try {
            msg = new Message(122);
            msg.writer().writeShort(second);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void resetPoint(Player player, int x, int y) {
        Message msg;
        try {
            player.location.x = x;
            player.location.y = y;
            msg = new Message(46);
            msg.writer().writeShort(x);
            msg.writer().writeShort(y);
            player.sendMessage(msg);
            msg.cleanup();

        } catch (Exception e) {
        }
    }

    public void clearMap(Player player) {
        Message msg;
        try {
            msg = new Message(-22);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    int test = 0;
    

    public void chat(Player player, String text) {

        
    //player buff item

//    if (text.startsWith("buff")) {
//                String[] parts = text.split(" ");
//                if (parts.length >= 3) {
//                    try {
//                        short id = Short.parseShort(parts[1]);
//                        int quantity = Integer.parseInt(parts[2]);
//                        Item item = ItemService.gI().createNewItem(id, quantity);
//                        InventoryService.gI().addItemBag(player, item, quantity);
//                        InventoryService.gI().sendItemBags(player);
//                        Service.getInstance().sendThongBao(player, "Bạn nhận được " + item.template.name + " số lượng: " + quantity);
//                    } catch (NumberFormatException e) {
//                        Service.getInstance().sendThongBao(player, "Không hợp lệ!");
//                    }
//                } else {
//                    Service.getInstance().sendThongBao(player, "Cú pháp sai! Dùng: buff <id> <số lượng>");
//                }
//                return;
//            }

        if (player.getSession() != null && player.isAdmin()) {
            if (text.equals("logskill")) {
                Service.getInstance().sendThongBao(player, player.playerSkill.skillSelect.coolDown + "");
                return;
            }
            //buff item
            
            if (text.equals("client")) {
                Client.gI().show(player);
                return;
            }
            
            if (text.startsWith("buff")) {
                String[] parts = text.split(" ");
                if (parts.length >= 3) {
                    try {
                        short id = Short.parseShort(parts[1]);
                        int quantity = Integer.parseInt(parts[2]);
                        Item item = ItemService.gI().createNewItem(id, quantity);
                        InventoryService.gI().addItemBag(player, item, quantity);
                        InventoryService.gI().sendItemBags(player);
                        Service.getInstance().sendThongBao(player, "Bạn nhận được " + item.template.name + " số lượng: " + quantity);
                    } catch (NumberFormatException e) {
                        Service.getInstance().sendThongBao(player, "Không hợp lệ!");
                    }
                } else {
                    Service.getInstance().sendThongBao(player, "Cú pháp sai! Dùng: buff <id> <số lượng>");
                }
                return;
            }

            OperatingSystemMXBean osBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
            double cpuUsage = osBean.getSystemCpuLoad();
            long totalMemory = osBean.getTotalPhysicalMemorySize();
            long usedMemory = totalMemory - osBean.getFreePhysicalMemorySize();
            double memoryUsage = (double) usedMemory / totalMemory;

            DecimalFormat decimalFormat = new DecimalFormat("0.0%");
            String cpu = decimalFormat.format(cpuUsage);
            String memory = decimalFormat.format(memoryUsage);

            if (text.equals("admin")) {
                String str = "|7|--Ngọc Rồng Sky--" + "\n"
                        + "|4|Sessions: " + (Client.gI().getSessions().size() + soluongBot) + "\n"
                        + "|5|Tổng Thread: " + Thread.activeCount() + "\n"
                        + "|6|Số người Online:  " + (Client.gI().getPlayers().size() + soluongBot) + "\n"
                        + "|8|Thời gian chạy Server: " + Manager.NgayRunServer + "\n"
                        + "|4|Memory usage: " + memory + "\n"
                        + "Current CPU usage: " + cpu + "\n"
                        + "|3|[ Vui lòng chọn quyền ]";
                NpcService.gI().createMenuConMeo(player, ConstNpc.MENU_ADMIN, -1, str,
                        "Đồ sát",
                        "Ngọc rồng",
                        "Đệ tử",
                        "Bảo trì",
                        "Tìm kiếm\nngười chơi",
                        "Cập Nhật\nThông Báo",
                        "Buff Item",
                        "Buff Item Option",
                        "Đổi hành tinh",
                        "Tặng\nDanh hiệu");
                return;
            } else if (text.equals("vt")) {
                sendThongBao(player, "Tọa độ\n" + player.location.x + " - " + player.location.y);
                return;
            } else if (text.equals("loadbt")) {
                BangTin.BANGTIN_MANAGER.clear();
                BangTin.gI().load_BangTin();
                sendThongBao(player, "Load Bảng Tin thành công");
                return;
            } else if (text.equals("loadpl")) {
                PhucLoi.PHUCLOI_MANAGER.clear();
                PhucLoi.PHUCLOI_TEMPLATES.clear();
                PhucLoi.gI().load_PhucLoi();
                PhucLoi.gI().load_PhucLoiTab();
                sendThongBao(player, "Load Phúc Lợi thành công");
                return;
            } else if (text.equals("loadvq")) {
                TamBao.MOC_TAMBAO.clear();
                TamBao.gI().load_mocTamBao();
                sendThongBao(player, "Load Vòng Quay thành công");
                return;
            } else if (text.equals("loadkn")) {
                KhamNgoc.KHAM_NGOC.clear();
                KhamNgoc.gI().loadKhamNgoc();
                sendThongBao(player, "Load Khảm Ngọc thành công");
                return;
            } else if (text.equals("loadruong")) {
                RuongSuuTam.listRuong.clear();
                RuongSuuTam.listCaiTrang.clear();
                RuongSuuTam.listPhuKien.clear();
                RuongSuuTam.listPet.clear();
                RuongSuuTam.listLinhThu.clear();
                RuongSuuTam.listThuCuoi.clear();
                RuongSuuTam.gI().loadRuongSuuTam();
                sendThongBao(player, "Load Rương Sưu Tầm thành công");
                return;
            } else if (text.equals("loadptn")) {
                PhongThiNghiem.PHONG_THI_NGHIEM.clear();
                PhongThiNghiem.gI().loadPhongThiNghiem();
                sendThongBao(player, "Load Phòng Thí Nghiệm thành công");
                return;
            } else if (text.equals("loadshop")) {
                ShopService.gI().reloadShops();
                Log.success("Load shop thành công (" + SHOPS.size() + ")");
                sendThongBao(player, "Load shop thành công (" + SHOPS.size() + ")");
                return;
            } else if (text.equals("taixiu")) {
                TaiXiu.gI().dongtaixiu = !TaiXiu.gI().dongtaixiu;
                TaiXiu_Xu.gI().dongtaixiu = !TaiXiu_Xu.gI().dongtaixiu;
                TaiXiu.gI().baotri = !TaiXiu.gI().baotri;
                TaiXiu_Xu.gI().baotri = !TaiXiu_Xu.gI().baotri;
                sendThongBao(player, "Bảo trì tài xỉu");
                return;
            } else if (text.equals("danhhieu")) {
                BuffDanhHieu(player);
                return;
            } else if (text.equals("bang")) {
                new Thread(() -> {
                    try {
                        ClanService.gI().close();
                        System.out.println("Save " + Manager.CLANS.size() + " bang");
                    } catch (Exception e) {
                        System.out.println("Lỗi save clan!...................................\n");
                    }
                }).start();
            } else if (text.equals("a")) {
                BossManager.gI().showListBoss(player);
                return;
            } else if (text.equals("r")) { // hồi all skill, Ki
                Service.getInstance().releaseCooldownSkill(player);
                sendThongBao(player, "Quyền năng trị liệu\n");
                return;
            } else if (text.equals("toado")) {
                Service.getInstance().sendThongBao(player, player.location.x + " - " + player.location.y);
                return;
            } else if (text.equals("tn")) {
                Input.gI().createFormTangRuby(player);
                return;
            } else if (text.equals("it")) {
                Input.gI().createFormAddItem(player);
                return;
            } else if (text.startsWith("bot")) {
                try {
                    int soluong = Integer.parseInt(text.replaceAll("bot", ""));
                    soluongBot += soluong;
                    for (int a = 0; a < soluong; a++) {
                        Client.gI().createBot();
                    }
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (text.startsWith("f")) {
                int id = Integer.parseInt(text.replaceAll("f", ""));
                Resources.effData(player.getSession(), id);
                return;
            } else if (text.startsWith("xoa")) {
                try {
                    int soluong = Integer.parseInt(text.replaceAll("xoa", ""));
                    for (int a = 0; a < soluong; a++) {
                        if (!Client.gI().bots.isEmpty()) {
                            soluongBot--;
                            Client.gI().clear();
                        } else {
                            break;
                        }
                    }
                    System.out.println("CON LAI: " + Client.gI().bots.size() + " BOT");
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (text.startsWith("up")) {
                try {
                    long power = Long.parseLong(text.replaceAll("up", ""));
                    addSMTN(player, (byte) 2, power, false);
                    return;
                } catch (Exception e) {
                }
            } else if (text.startsWith("chat")) {
                try {
                    String chat = text.replaceAll("chat ", "");
                    HeThongChatGlobal(chat);
                    return;
                } catch (Exception e) {
                }
            } else if (text.startsWith("m ")) {
                try {
                    String mapIdText = text.substring(2);
                    if (!mapIdText.isEmpty()) {
                        int mapId = Integer.parseInt(mapIdText);
                        ChangeMapService.gI().changeMapInYard(player, mapId, -1, -1);
                        sendThongBao(player, "" + player.name + " đã dịch chuyển tức thời đến: " + player.zone.map.mapName + " (" + player.zone.map.mapId + ")");
                    } else {
                        sendThongBao(player, "Không hợp lệ");
                    }
                    return;
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }

             else if (text.startsWith("hp")) {
    try {
        int value = Integer.parseInt(text.replace("hp", ""));
        player.nPoint.hpMax = value;
        player.nPoint.hp = value;

        player.nPoint.setFullHpMp();

        sendThongBao(player, "Đã set HP gốc: " + value);
        return;
    } catch (Exception e) {
    }

} else if (text.startsWith("ki")) {
    try {
        int value = Integer.parseInt(text.replace("ki", ""));
        player.nPoint.mpMax = value;
        player.nPoint.mp = value;

        player.nPoint.setFullHpMp();

        sendThongBao(player, "Đã set KI gốc: " + value);
        return;
    } catch (Exception e) {
    }

} else if (text.startsWith("sd")) {
    try {
        int value = Integer.parseInt(text.replace("sd", ""));
        player.nPoint.dame = value;

        sendThongBao(player, "Đã set Sức đánh gốc: " + value);
        return;
    } catch (Exception e) {
    }

} else if (text.startsWith("def")) {
    try {
        int value = Integer.parseInt(text.replace("def", ""));
        player.nPoint.def = value;

        sendThongBao(player, "Đã set Giáp gốc: " + value);
        return;
    } catch (Exception e) {
    }

} else if (text.startsWith("crit")) {
    try {
        int value = Integer.parseInt(text.replace("crit", ""));
        player.nPoint.crit = value;

        sendThongBao(player, "Đã set Chí mạng: " + value + "%");
        return;
    } catch (Exception e) {
    }
}
            
        }
        

        if (text.equals("tt")) {
            infoall(player);
            return;
        }
        if (text.equals("ht")) {
            if (player.pet.inventory.itemsBody.size() < 12) {
                int size = 12 - player.pet.inventory.itemsBody.size();
                for (int i = 0; i < size; i++) {
                    player.pet.inventory.itemsBody.add(ItemService.gI().createItemNull());
                }
                InventoryService.gI().sendItemBody(player);
            }
            return;
        }
        if (text.equals("dh")) {
            NpcService.gI().createMenuConMeo(player, ConstNpc.CHON_DANH_HIEU, 0,
                    "|7|DANH HIỆU\n\n"
                    + "|2|Bạn muốn xem danh hiệu phiên bản nào?",
                    "Phiên bản 1",
                    "Phiên bản 2");
            return;
        }
        if (text.startsWith("ten con la ")) {
            PetService.gI().changeNamePet(player, text.replaceAll("ten con la ", ""));
        }
        if (text.equals("fixhanhtrang")) {
            Service.getInstance().player(player);
            Service.getInstance().Send_Caitrang(player);
        }
        if (text.equals("vip")) {
            player.transformplayer();
        }
        if (text.equals("tutien")) {
            loadTuTien(player);
            return;
        }
        if (player.pet != null) {
            if (text.equals("di theo") || text.equals("follow")) {
                player.pet.changeStatus(Pet.FOLLOW);
            } else if (text.equals("bao ve") || text.equals("protect")) {
                player.pet.changeStatus(Pet.PROTECT);
            } else if (text.equals("tan cong") || text.equals("attack")) {
                player.pet.changeStatus(Pet.ATTACK);
            } else if (text.equals("ve nha") || text.equals("go home")) {
                player.pet.changeStatus(Pet.GOHOME);
            } else if (text.equals("bien hinh")) {
                player.pet.transform();
            }
        }

        if (text.length() > 100) {
            text = text.substring(0, 100);
        }
        chatMap(player, text);
    }

    public void loadTuTien(Player player) {
        if (player.isTuTien) {
            NpcService.gI().createMenuConMeo(player, ConstNpc.MENU_TU_TIEN, 0,
                    "|7|TU TIÊN\n"
                    + "|2|Cấp bậc: " + bacTuTien(player.levelTuTien)
                    + "\nTầng: " + (player.tangTuTien) + " (" + (player.expTuTien * 100) / expDotPhaTang(player.levelTuTien, player.tangTuTien) + "%)"
                    + "\n|4|Exp: " + Util.format(player.expTuTien)
                    + "\n|3|" + infoCapBac(player),
                    "Load Exp",
                    "Xem\nChỉ số",
                    "Đột phá\nCấp bậc",
                    "Báo Exp\n"  + (player.thongBaoExp ? "(Bật)" : "(Tắt)"));
        } else {
            NpcService.gI().createMenuConMeo(player, ConstNpc.MENU_TU_TIEN, 0,
                    "|7|TU TIÊN\n"
                    + "|2|Chưa mở khoá tính năng Tu tiên"
                    + "\n|4|Mở khoá với giá 10.000 " + ConstItem.NAME_XU_SERVER,
                    "Mở khoá");
        }
    }

    public String infoCapBac(Player player) {
        if (player.levelTuTien < 10) {
            if (player.tangTuTien < 10) {
                return "Tầng kế cần: " + Util.format(expDotPhaTang(player.levelTuTien, player.tangTuTien)) + " Exp";
            } else {
                return "Cấp bậc kế tiếp: " + bacTuTien(player.levelTuTien + 1);
            }
        } else {
            if (player.tangTuTien < 10) {
                return "Tầng kế cần: " + Util.format(expDotPhaTang(player.levelTuTien, player.tangTuTien)) + " Exp";
            } else {
                return "Đã đạt Cấp bậc cao nhất";
            }
        }
    }

    public int daDotPhaTuTien(int level) {
        return (level + 1) * 50;
    }

    public int nPointTuTien(int level, int tang) {
        int bac = level + 1;
        return bac * 100 + (tang + bac) * 5;
    }

    public long expDotPhaTang(int level, int tang) {
        int bac = level + 1;
        return (bac + (bac * 10L)) * tang * 150_000_000L;
    }

    public String bacTuTien(int level) {
        switch (level) {
            case 10:
                return "Đế Tiên";
            case 9:
                return "Vực Chủ Cảnh";
            case 8:
                return "Độ Kiếp Kỳ";
            case 7:
                return "Đại Thừa Kỳ";
            case 6:
                return "Hợp Thể Kỳ";
            case 5:
                return "Luyện Hư";
            case 4:
                return "Hóa Thần";
            case 3:
                return "Nguyên Anh";
            case 2:
                return "Kim Đan";
            case 1:
                return "Trúc Cơ";
            case 0:
                return "Luyện Khí";
            default:
                return "Phế Vật";
        }
    }

    public void chatMap(Player player, String text) {
        Message msg;
        try {
            msg = new Message(44);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeUTF(text);
            sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
            Log.error(Service.class, e);
        }
    }

    public void infoall(Player player) {
        NpcService.gI().createMenuConMeo(player, ConstNpc.INFO_ALL, 12713,
                "BẢNG CHỨC NĂNG NGƯỜI CHƠI",
                "Thông tin\n nhân vật", "Thông tin\nđệ tử", "Thông tin\nđồ mặc");
    }

    public void MenuDanhHieu1(Player player) {
        class DanhHieu {

            String ten;
            String moTa;
            boolean daSoHuu;

            DanhHieu(String ten, String moTa, boolean daSoHuu) {
                this.ten = ten;
                this.moTa = moTa;
                this.daSoHuu = daSoHuu;
            }
        }

        List<DanhHieu> danhHieuList = new ArrayList<>();
        danhHieuList.add(new DanhHieu("Tuổi Thơ", "30% HP,KI,SD cho bản thân", player.DH_VV_3));
        danhHieuList.add(new DanhHieu("Cần Thủ", "50% HP,KI,SD cho bản thân", player.DH_VV_2));
        danhHieuList.add(new DanhHieu("Đại Thần", "50% HP,KI,SD cho bản thân", player.DH_VV_1));
        danhHieuList.add(new DanhHieu("Thợ Ngọc", "100% HP,KI,SD cho bản thân", player.DH_VV_4));
        danhHieuList.add(new DanhHieu("Ai Mà Xinh Thế", "100% HP,KI,SD cho bản thân", player.DH_VV_5));

        StringBuilder info = new StringBuilder("|7|DANH HIỆU PHIÊN BẢN 1");

        for (DanhHieu dh : danhHieuList) {
            info.append("\n").append(dh.daSoHuu ? "|1|" : "|0|")
                    .append("Danh hiệu: ").append(dh.ten).append(" ").append(dh.moTa);
        }

        NpcService.gI().createMenuConMeo(player, ConstNpc.MENU_DANHHIEU_1, 0, info.toString(),
                ("Đại Thần\n" + (player.isTitleUse1 ? "'ON'" : "'OFF'")),
                ("Cần Thủ\n" + (player.isTitleUse2 ? "'ON'" : "'OFF'")),
                ("Tuổi Thơ\n" + (player.isTitleUse3 ? "'ON'" : "'OFF'")),
                ("Thợ Ngọc\n" + (player.isTitleUse4 ? "'ON'" : "'OFF'")),
                ("Ai mà xinh thế\n" + (player.isTitleUse5 ? "'ON'" : "'OFF'")),
                ("Hiển thị\n" + (player.isTitlePriority1 ? "'ON'" : "'OFF'"))
        );
    }

    public void MenuDanhHieu2(Player player) {
        class DanhHieu {

            String ten;
            String moTa;
            boolean daSoHuu;

            DanhHieu(String ten, String moTa, boolean daSoHuu) {
                this.ten = ten;
                this.moTa = moTa;
                this.daSoHuu = daSoHuu;
            }
        }

        List<DanhHieu> danhHieuList = new ArrayList<>();
        danhHieuList.add(new DanhHieu("Diệt Thần", "50% HP,KI,SD cho bản thân", player.DH_VV_6));
        danhHieuList.add(new DanhHieu("Long Thần", "100% HP,KI,SD cho bản thân", player.DH_VV_7));
        danhHieuList.add(new DanhHieu("Chí Tôn", "200% HP,KI,SD cho bản thân", player.DH_VV_8));
        danhHieuList.add(new DanhHieu("Thiên Đế", "200% HP,KI,SD cho bản thân", player.DH_VV_9));
        danhHieuList.add(new DanhHieu("Đế Vương", "200% HP,KI,SD cho bản thân", player.DH_VV_10));

        StringBuilder info = new StringBuilder("|7|DANH HIỆU PHIÊN BẢN 2");

        for (DanhHieu dh : danhHieuList) {
            info.append("\n").append(dh.daSoHuu ? "|1|" : "|0|")
                    .append("Danh hiệu: ").append(dh.ten).append(" ").append(dh.moTa);
        }

        NpcService.gI().createMenuConMeo(player, ConstNpc.MENU_DANHHIEU_2, 0, info.toString(),
                ("Diệt Thần\n" + (player.isTitleUse6 ? "'ON'" : "'OFF'")),
                ("Long Thần\n" + (player.isTitleUse7 ? "'ON'" : "'OFF'")),
                ("Chí Tôn\n" + (player.isTitleUse8 ? "'ON'" : "'OFF'")),
                ("Thiên Đế\n" + (player.isTitleUse9 ? "'ON'" : "'OFF'")),
                ("Đế Vương\n" + (player.isTitleUse10 ? "'ON'" : "'OFF'")),
                ("Hiển thị\n" + (player.isTitlePriority2 ? "'ON'" : "'OFF'"))
        );
    }

    public void BuffDanhHieu(Player player) {
        NpcService.gI().createMenuConMeo(player, ConstNpc.ADMIN_DANH_HIEU, 0,
                "|7|CHỌN DANH HIỆU"
                + "\n|2|Chọn danh hiệu 7 ngày dưới đây !!!",
                ("Đại Thần"),
                ("Cần Thủ"),
                ("Tuổi Thơ"),
                ("Thợ ngọc"));
    }

    public void clearSpecificItems(Player player) {
        List<Integer> itemIdsToClear = Arrays.asList(1986, 1987, 1988, 1989, 1990, 1991, 1992, 1993, 1994, // ID vật phẩm cần xóa
                1849, 1850, 1851, 1852, 1853, 1854, 1855, 1856, 1857);      // ID item cần xóa
        List<Integer> itemOpsToClear = Arrays.asList(196);      // Option ID cần xóa

        //------------Người-Chơi------------
        player.inventory.itemsBag.stream()
                .filter(item -> item.isNotNullItem() && (itemIdsToClear.contains((int) item.template.id)
                || itemOpsToClear.stream().anyMatch(item::haveOption)))
                .forEach(item -> InventoryService.gI().subQuantityItem(player.inventory.itemsBag, item, item.quantity));
        player.inventory.itemsBox.stream()
                .filter(item -> item.isNotNullItem() && (itemIdsToClear.contains((int) item.template.id)
                || itemOpsToClear.stream().anyMatch(item::haveOption)))
                .forEach(item -> InventoryService.gI().subQuantityItem(player.inventory.itemsBox, item, item.quantity));
        player.inventory.itemsBody.stream()
                .filter(item -> item.isNotNullItem() && (itemIdsToClear.contains((int) item.template.id)
                || itemOpsToClear.stream().anyMatch(item::haveOption)))
                .forEach(item -> InventoryService.gI().subQuantityItem(player.inventory.itemsBody, item, item.quantity));
        player.inventory.itemsBoxCrackBall.stream()
                .filter(item -> item.isNotNullItem() && (itemIdsToClear.contains((int) item.template.id)
                || itemOpsToClear.stream().anyMatch(item::haveOption)))
                .forEach(item -> InventoryService.gI().subQuantityItem(player.inventory.itemsBoxCrackBall, item, item.quantity));
        player.inventory.itemsMailBox.stream()
                .filter(item -> item.isNotNullItem() && (itemIdsToClear.contains((int) item.template.id)
                || itemOpsToClear.stream().anyMatch(item::haveOption)))
                .forEach(item -> InventoryService.gI().subQuantityItem(player.inventory.itemsMailBox, item, item.quantity));
        player.inventory.itemsBagTemp.stream()
                .filter(item -> item.isNotNullItem() && (itemIdsToClear.contains((int) item.template.id)
                || itemOpsToClear.stream().anyMatch(item::haveOption)))
                .forEach(item -> InventoryService.gI().subQuantityItem(player.inventory.itemsBagTemp, item, item.quantity));
        player.inventory.itemsBodyTemp.stream()
                .filter(item -> item.isNotNullItem() && (itemIdsToClear.contains((int) item.template.id)
                || itemOpsToClear.stream().anyMatch(item::haveOption)))
                .forEach(item -> InventoryService.gI().subQuantityItem(player.inventory.itemsBodyTemp, item, item.quantity));

        // Cập nhật lại giao diện người chơi
        InventoryService.gI().sendItemBody(player);
        InventoryService.gI().sendItemBags(player);
        InventoryService.gI().sendItemBox(player);

        //------------Đệ-Tử------------
        if (player.pet != null && player.pet.inventory != null) {
            player.pet.inventory.itemsBag.stream()
                    .filter(item -> item.isNotNullItem() && (itemIdsToClear.contains((int) item.template.id)
                    || itemOpsToClear.stream().anyMatch(item::haveOption)))
                    .forEach(item -> InventoryService.gI().subQuantityItem(player.pet.inventory.itemsBag, item, item.quantity));
            player.pet.inventory.itemsBox.stream()
                    .filter(item -> item.isNotNullItem() && (itemIdsToClear.contains((int) item.template.id)
                    || itemOpsToClear.stream().anyMatch(item::haveOption)))
                    .forEach(item -> InventoryService.gI().subQuantityItem(player.pet.inventory.itemsBox, item, item.quantity));
            player.pet.inventory.itemsBody.stream()
                    .filter(item -> item.isNotNullItem() && (itemIdsToClear.contains((int) item.template.id)
                    || itemOpsToClear.stream().anyMatch(item::haveOption)))
                    .forEach(item -> InventoryService.gI().subQuantityItem(player.pet.inventory.itemsBody, item, item.quantity));
            player.pet.inventory.itemsBoxCrackBall.stream()
                    .filter(item -> item.isNotNullItem() && (itemIdsToClear.contains((int) item.template.id)
                    || itemOpsToClear.stream().anyMatch(item::haveOption)))
                    .forEach(item -> InventoryService.gI().subQuantityItem(player.pet.inventory.itemsBoxCrackBall, item, item.quantity));
            player.pet.inventory.itemsMailBox.stream()
                    .filter(item -> item.isNotNullItem() && (itemIdsToClear.contains((int) item.template.id)
                    || itemOpsToClear.stream().anyMatch(item::haveOption)))
                    .forEach(item -> InventoryService.gI().subQuantityItem(player.pet.inventory.itemsMailBox, item, item.quantity));
            player.pet.inventory.itemsBagTemp.stream()
                    .filter(item -> item.isNotNullItem() && (itemIdsToClear.contains((int) item.template.id)
                    || itemOpsToClear.stream().anyMatch(item::haveOption)))
                    .forEach(item -> InventoryService.gI().subQuantityItem(player.pet.inventory.itemsBagTemp, item, item.quantity));
            player.pet.inventory.itemsBodyTemp.stream()
                    .filter(item -> item.isNotNullItem() && (itemIdsToClear.contains((int) item.template.id)
                    || itemOpsToClear.stream().anyMatch(item::haveOption)))
                    .forEach(item -> InventoryService.gI().subQuantityItem(player.pet.inventory.itemsBodyTemp, item, item.quantity));

            // Cập nhật lại giao diện đệ tử
            InventoryService.gI().sendItemBody(player.pet);
            InventoryService.gI().sendItemBags(player.pet);
            InventoryService.gI().sendItemBox(player.pet);
        }
    }

    public void removeQuanHoaVinhVien(Player player) {
        List<Short> quanIds = Arrays.asList((short) 691, (short) 692, (short) 693);
        int opCheck = 93;

        // Áp dụng cho cả player và pet
        List<Player> targets = new ArrayList<>();
        targets.add(player);
        if (player.pet != null) {
            targets.add(player.pet);
        }

        for (Player p : targets) {
            List<List<Item>> allInventories = Arrays.asList(
                    p.inventory.itemsBag,
                    p.inventory.itemsBox,
                    p.inventory.itemsBody,
                    p.inventory.itemsBagTemp,
                    p.inventory.itemsBoxCrackBall,
                    p.inventory.itemsMailBox,
                    p.inventory.itemsBodyTemp
            );

            for (List<Item> inventory : allInventories) {
                for (Item item : inventory) {
                    if (item != null && item.isNotNullItem()
                            && quanIds.contains(item.template.id)
                            && !item.haveOption(opCheck)) {
                        InventoryService.gI().subQuantityItem(inventory, item, item.quantity);
                    }
                }
            }
            InventoryService.gI().sendItemBody(p);
            InventoryService.gI().sendItemBags(p);
            InventoryService.gI().sendItemBox(p);
        }
    }

    public void thuHoiNgocRongVaTraLai(Player player) {
        List<Integer> idsNgocRong = Arrays.asList(1155, 1156, 1157, 1158, 1159, 1160, 1161);
        Map<Integer, Integer> idToQuantity = new HashMap<>();

        // Hàm phụ: cộng dồn số lượng theo ID và xóa item
        Consumer<List<Item>> removeAndCount = (list) -> {
            Iterator<Item> iterator = list.iterator();
            while (iterator.hasNext()) {
                Item item = iterator.next();
                if (item != null && item.isNotNullItem() && idsNgocRong.contains((int) item.template.id)) {
                    int id = item.template.id;
                    idToQuantity.put(id, idToQuantity.getOrDefault(id, 0) + item.quantity);
                    iterator.remove();
                }
            }
        };

        // Xử lý các kho của player
        List<List<Item>> playerInventories = Arrays.asList(
                player.inventory.itemsBag,
                player.inventory.itemsBox,
                player.inventory.itemsBoxCrackBall,
                player.inventory.itemsMailBox,
                player.inventory.itemsBody,
                player.inventory.itemsBagTemp,
                player.inventory.itemsBodyTemp
        );
        playerInventories.forEach(removeAndCount);

        // Xử lý các kho của đệ tử nếu có
        if (player.pet != null && player.pet.inventory != null) {
            List<List<Item>> petInventories = Arrays.asList(
                    player.pet.inventory.itemsBag,
                    player.pet.inventory.itemsBox,
                    player.pet.inventory.itemsBoxCrackBall,
                    player.pet.inventory.itemsMailBox,
                    player.pet.inventory.itemsBody,
                    player.pet.inventory.itemsBagTemp,
                    player.pet.inventory.itemsBodyTemp
            );
            petInventories.forEach(removeAndCount);
        }

        // Tìm số lượng lớn nhất trong 7 viên
        int maxQuantity = idToQuantity.values().stream().max(Integer::compare).orElse(0);

        // Nếu có ít nhất 1 viên được thu hồi thì gửi quà
        if (maxQuantity > 0) {
            Item itemGift = ItemService.gI().createNewItem((short) 1015);
            itemGift.quantity = maxQuantity;
            player.inventory.itemsMailBox.add(itemGift);
            Service.getInstance().sendThongBao(player, "Đã thu hồi ngọc rồng và gửi " + maxQuantity + " item vào hòm thư.");
            FileUtils.logThuHoiNgocRong(player, idsNgocRong, maxQuantity);
        }

        // Cập nhật giao diện
        InventoryService.gI().sendItemBags(player);
        InventoryService.gI().sendItemBox(player);
        InventoryService.gI().sendItemBody(player);
        if (player.pet != null) {
            InventoryService.gI().sendItemBags(player.pet);
            InventoryService.gI().sendItemBox(player.pet);
            InventoryService.gI().sendItemBody(player.pet);
        }
    }

    public void chatJustForMe(Player me, Player plChat, String text) {
        Message msg;
        try {
            msg = new Message(44);
            msg.writer().writeInt((int) plChat.id);
            msg.writer().writeUTF(text);
            me.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void point(Player player) {
        player.nPoint.calPoint();
        Send_Info_NV(player);
        if (!player.isPet && !player.isBoss && !player.isBot) {
            Message msg;
            try {
                msg = new Message(-42);
                msg.writeDouble(player.nPoint.hpg);
                msg.writeDouble(player.nPoint.mpg);
                msg.writeDouble(player.nPoint.dameg);
                msg.writeDouble(player.nPoint.hpMax);// hp full
                msg.writeDouble(player.nPoint.mpMax);// mp full
                msg.writeDouble(player.nPoint.hp);// hp
                msg.writeDouble(player.nPoint.mp);// mp
                msg.writer().writeByte(player.nPoint.speed);// speed
                msg.writer().writeByte(20);
                msg.writer().writeByte(20);
                msg.writer().writeByte(1);
                msg.writeDouble(player.nPoint.dame);// dam base
                msg.writeDouble(player.nPoint.def);// def full
                msg.writer().writeByte(player.nPoint.crit);// crit full
                msg.writer().writeLong(player.nPoint.tiemNang);
                msg.writer().writeShort(100);
                msg.writeDouble(player.nPoint.defg);
                msg.writer().writeByte(player.nPoint.critg);
                player.sendMessage(msg);
                msg.cleanup();
            } catch (Exception e) {
                Log.error(Service.class, e);
            }
        }
        if (player.phanthan != null) {
            player.phanthan.nPoint.calPoint();
            player.phanthan.nPoint.setFullHpMp();
            point(player.phanthan);
        }
    }

    public void player(Player pl) {
        if (pl == null) {
            return;
        }
        Message msg;
        try {
            msg = messageSubCommand((byte) 0);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeByte(pl.playerTask.taskMain.id);
            msg.writer().writeByte(pl.gender);
            msg.writer().writeShort(pl.head);
            msg.writer().writeUTF(pl.name);
            msg.writer().writeByte(0); //cPK
            msg.writer().writeByte(pl.typePk);
            msg.writer().writeLong(pl.nPoint.power);
            msg.writer().writeShort(0);
            msg.writer().writeShort(0);
            msg.writer().writeByte(pl.gender);
            //--------skill---------

            ArrayList<Skill> skills = (ArrayList<Skill>) pl.playerSkill.getCurrentSkills(pl);

            msg.writer().writeByte(pl.playerSkill.getSizeSkill());

            for (Skill skill : skills) {
                if (skill.skillId != -1) {
                    msg.writer().writeShort(skill.skillId);
                }
            }

            //---vang---luong--luongKhoa
            long gold = pl.inventory.getGoldDisplay();
            if (pl.isVersionAbove(214)) {
                msg.writer().writeLong(gold);
            } else {
                msg.writer().writeInt((int) gold);
            }
            msg.writer().writeInt(pl.inventory.ruby);
            msg.writer().writeInt(pl.inventory.gem);

            //--------itemBody---------
            ArrayList<Item> itemsBody = (ArrayList<Item>) pl.inventory.itemsBody;
            msg.writer().writeByte(itemsBody.size());
            for (Item item : itemsBody) {
                if (!item.isNotNullItem()) {
                    msg.writer().writeShort(-1);
                } else {
                    msg.writer().writeShort(item.template.id);
                    msg.writer().writeInt(item.quantity);
                    msg.writer().writeUTF(item.getInfo());
                    msg.writer().writeUTF(item.getContent());
                    List<ItemOption> itemOptions = item.getDisplayOptions();
                    msg.writer().writeByte(itemOptions.size());
                    for (ItemOption itemOption : itemOptions) {
                        msg.writer().writeByte(itemOption.optionTemplate.id);
                        msg.writer().writeInt(itemOption.param);
                    }
                }

            }

            //--------itemBag---------
            ArrayList<Item> itemsBag = (ArrayList<Item>) pl.inventory.itemsBag;
            msg.writer().writeByte(itemsBag.size());
            for (int i = 0; i < itemsBag.size(); i++) {
                Item item = itemsBag.get(i);
                if (!item.isNotNullItem()) {
                    msg.writer().writeShort(-1);
                } else {
                    msg.writer().writeShort(item.template.id);
                    msg.writer().writeInt(item.quantity);
                    msg.writer().writeUTF(item.getInfo());
                    msg.writer().writeUTF(item.getContent());
                    List<ItemOption> itemOptions = item.getDisplayOptions();
                    msg.writer().writeByte(itemOptions.size());
                    for (ItemOption itemOption : itemOptions) {
                        msg.writer().writeByte(itemOption.optionTemplate.id);
                        msg.writer().writeInt(itemOption.param);
                    }
                }

            }

            //--------itemBox---------
            ArrayList<Item> itemsBox = (ArrayList<Item>) pl.inventory.itemsBox;
            msg.writer().writeByte(itemsBox.size());
            for (int i = 0; i < itemsBox.size(); i++) {
                Item item = itemsBox.get(i);
                if (!item.isNotNullItem()) {
                    msg.writer().writeShort(-1);
                } else {
                    msg.writer().writeShort(item.template.id);
                    msg.writer().writeInt(item.quantity);
                    msg.writer().writeUTF(item.getInfo());
                    msg.writer().writeUTF(item.getContent());
                    List<ItemOption> itemOptions = item.getDisplayOptions();
                    msg.writer().writeByte(itemOptions.size());
                    for (ItemOption itemOption : itemOptions) {
                        msg.writer().writeByte(itemOption.optionTemplate.id);
                        msg.writer().writeInt(itemOption.param);
                    }
                }
            }
            //-----------------
            DataGame.sendHeadAvatar(msg);
            //-----------------
            msg.writer().writeShort(514); //char info id - con chim thông báo
            msg.writer().writeShort(515); //char info id
            msg.writer().writeShort(537); //char info id
            msg.writer().writeByte(pl.fusion.typeFusion != ConstPlayer.NON_FUSION ? 1 : 0); //nhập thể
//            msg.writer().writeInt(1632811835); //deltatime
            msg.writer().writeInt(333); //deltatime
            msg.writer().writeByte(pl.isNewMember ? 1 : 0); //is new member

//            if (pl.isAdmin()) {
            msg.writer().writeShort(pl.getAura()); //idauraeff
            msg.writer().writeByte(-1);
//            }
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Log.error(Service.class, e);
        }
    }

    public Message messageNotLogin(byte command) throws IOException {
        Message ms = new Message(-29);
        ms.writer().writeByte(command);
        return ms;
    }

    public Message messageNotMap(byte command) throws IOException {
        Message ms = new Message(-28);
        ms.writer().writeByte(command);
        return ms;
    }

    public Message messageSubCommand(byte command) throws IOException {
        Message ms = new Message(-30);
        ms.writer().writeByte(command);
        return ms;
    }

    public void addSMTN(Player player, byte type, long param, boolean isOri) {
        if (player.isPet) {
            if (player.nPoint.power > player.nPoint.getPowerLimit()) {
                return;
            }
            player.nPoint.powerUp(param);
            player.nPoint.tiemNangUp(param);
            Player master = ((Pet) player).master;

            param = master.nPoint.calSubTNSM(param);
            if (master.nPoint.power < master.nPoint.getPowerLimit()) {
                master.nPoint.powerUp(param);
            }
            master.nPoint.tiemNangUp(param);
            addSMTN(master, type, param, true);
        } else {
            if (player.nPoint.power > player.nPoint.getPowerLimit()) {
                return;
            }
            switch (type) {
                case 1:
                    player.nPoint.tiemNangUp(param);
                    break;
                case 2:
                    player.nPoint.powerUp(param);
                    player.nPoint.tiemNangUp(param);
                    break;
                default:
                    player.nPoint.powerUp(param);
                    break;
            }
            PlayerService.gI().sendTNSM(player, type, param);
            if (isOri) {
                if (player.clan != null) {
                    player.clan.addSMTNClan(player, param);
                }
            }
        }
    }

    //    public void congTiemNang(Player pl, byte type, int tiemnang) {
//        Message msg;
//        try {
//            msg = new Message(-3);
//            msg.writer().writeByte(type);// 0 là cộng sm, 1 cộng tn, 2 là cộng cả 2
//            msg.writer().writeInt(tiemnang);// số tn cần cộng
//            if (!pl.isPet) {
//                pl.sendMessage(msg);
//            } else {
//                ((Pet) pl).master.nPoint.powerUp(tiemnang);
//                ((Pet) pl).master.nPoint.tiemNangUp(tiemnang);
//                ((Pet) pl).master.sendMessage(msg);
//            }
//            msg.cleanup();
//            switch (type) {
//                case 1:
//                    pl.nPoint.tiemNangUp(tiemnang);
//                    break;
//                case 2:
//                    pl.nPoint.powerUp(tiemnang);
//                    pl.nPoint.tiemNangUp(tiemnang);
//                    break;
//                default:
//                    pl.nPoint.powerUp(tiemnang);
//                    break;
//            }
//        } catch (Exception e) {
//
//        }
//    }
    public String get_HanhTinh(int hanhtinh) {
        switch (hanhtinh) {
            case 0:
                return "Trái Đất";
            case 1:
                return "Namếc";
            case 2:
                return "Xayda";
            default:
                return "";
        }
    }

    public String getCurrStrLevel(Player pl) {
        long sucmanh = pl.nPoint.power;
        if (sucmanh < 3000) {
            return "Tân thủ";
        } else if (sucmanh < 15000) {
            return "Tập sự sơ cấp";
        } else if (sucmanh < 40000) {
            return "Tập sự trung cấp";
        } else if (sucmanh < 90000) {
            return "Tập sự cao cấp";
        } else if (sucmanh < 170000) {
            return "Tân binh";
        } else if (sucmanh < 340000) {
            return "Chiến binh";
        } else if (sucmanh < 700000) {
            return "Chiến binh cao cấp";
        } else if (sucmanh < 1500000) {
            return "Vệ binh";
        } else if (sucmanh < 15000000) {
            return "Vệ binh hoàng gia";
        } else if (sucmanh < 150000000) {
            return "Siêu " + get_HanhTinh(pl.gender) + " cấp 1";
        } else if (sucmanh < 1500000000) {
            return "Siêu " + get_HanhTinh(pl.gender) + " cấp 2";
        } else if (sucmanh < 5000000000L) {
            return "Siêu " + get_HanhTinh(pl.gender) + " cấp 3";
        } else if (sucmanh < 10000000000L) {
            return "Siêu " + get_HanhTinh(pl.gender) + " cấp 4";
        } else if (sucmanh < 40000000000L) {
            return "Thần " + get_HanhTinh(pl.gender) + " cấp 1";
        } else if (sucmanh < 50010000000L) {
            return "Thần " + get_HanhTinh(pl.gender) + " cấp 2";
        } else if (sucmanh < 60010000000L) {
            return "Thần " + get_HanhTinh(pl.gender) + " cấp 3";
        } else if (sucmanh < 70010000000L) {
            return "Giới Vương Thần cấp 11";
        } else if (sucmanh < 80010000000L) {
            return "Giới Vương Thần cấp 2";
        } else if (sucmanh < 100010000000L) {
            return "Giới Vương Thần cấp 3";
        } else if (sucmanh < 11100010000000L) {
            return "Thần Huỷ Diệt cấp 1";
        }
        return "Thần Huỷ Diệt cấp 2";
    }

    public void sendsoxo(Player pl, int winNum, boolean kqua) {
        Locale locale = new Locale("vi", "VN");
        NumberFormat num = NumberFormat.getInstance(locale);
        String lose = "Con số trúng thưởng là " + winNum + "\nChúc bạn may mắn lần sau!!";
        String win = "Chúc mừng bạn đã đoán trúng số " + winNum + " của giải lần này";
        Message msg;
        try {
            msg = new Message(-126);
            msg.writer().writeByte(1);
            msg.writer().writeByte(0);
            msg.writer().writeUTF(num.format(winNum));
            msg.writer().writeUTF((kqua == true) ? win : lose);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (IOException e) {
        }
    }

    public void youNumber(Player pl, int number) {
        StringBuilder stringBuilder = new StringBuilder();
        for (int num : pl.soMayMan) {
            if (stringBuilder.length() > 0) {
                stringBuilder.append(",");
            }
            stringBuilder.append(num);
        }
        String text = stringBuilder.toString();

        Message msg;
        try {
            msg = new Message(-126);
            msg.writer().writeByte(0);
            msg.writer().writeUTF(text);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (IOException e) {
        }
    }

    public void hsChar(Player pl, double hp, double mp) {
        Message msg;
        try {
            pl.setJustRevivaled();
            pl.nPoint.setHp(hp);
            pl.nPoint.setMp(mp);
            if (!pl.isPet) {
                msg = new Message(-16);
                pl.sendMessage(msg);
                msg.cleanup();
                PlayerService.gI().sendInfoHpMpMoney(pl);
            }

            msg = messageSubCommand((byte) 15);
            msg.writer().writeInt((int) pl.id);
            msg.writeDouble(hp);
            msg.writeDouble(mp);
            msg.writer().writeShort(pl.location.x);
            msg.writer().writeShort(pl.location.y);
            sendMessAllPlayerInMap(pl, msg);
            msg.cleanup();

            Send_Info_NV(pl);
            PlayerService.gI().sendInfoHpMp(pl);
        } catch (Exception e) {
            Log.error(Service.class, e);
        }
    }

    public void charDie(Player pl) {
        Message msg;
        try {
            if (!pl.isPet) {
                msg = new Message(-17);
                msg.writer().writeByte((int) pl.id);
                msg.writer().writeShort(pl.location.x);
                msg.writer().writeShort(pl.location.y);
                pl.sendMessage(msg);
                msg.cleanup();
            } else {
                ((Pet) pl).lastTimeDie = System.currentTimeMillis();
            }

            msg = new Message(-8);
            msg.writer().writeShort((int) pl.id);
            msg.writer().writeByte(0); //cpk
            msg.writer().writeShort(pl.location.x);
            msg.writer().writeShort(pl.location.y);
            sendMessAnotherNotMeInMap(pl, msg);
            msg.cleanup();
        } catch (Exception e) {
            Log.error(Service.class, e);
        }
    }

    public void attackMob(Player pl, int mobId) {
        if (pl != null && pl.zone != null) {
            for (Mob mob : pl.zone.mobs) {
                if (mob.id == mobId) {
                    SkillService.gI().useSkill(pl, null, mob, null);
                    break;
                }
            }
        }
    }

    public void sendTitleRv(Player player, Player p2, int id) {
        try {
            Message me = new Message(-128);
            me.writer().writeByte(0);
            me.writer().writeInt((int) player.id);

            if (player.isTitlePriority1) {
                switch (id) {
                    case 888:
                        if (player.DH_VV_1 && player.isTitleUse1) {
                            me.writer().writeShort(89);
                        }
                        break;
                    case 889:
                        if (player.DH_VV_2 && player.isTitleUse2) {
                            me.writer().writeShort(237);
                        }
                        break;
                    case 890:
                        if (player.DH_VV_3 && player.isTitleUse3) {
                            me.writer().writeShort(88);
                        }
                        break;
                    case 891:
                        if (player.DH_VV_4 && player.isTitleUse4) {
                            me.writer().writeShort(236);
                        }
                        break;
                    case 892:
                        if (player.DH_VV_5 && player.isTitleUse5) {
                            me.writer().writeShort(235);
                        }
                        break;
                }
            } else if (player.isTitlePriority2) {
                switch (id) {
                    case 893:
                        if (player.DH_VV_6 && player.isTitleUse6) {
                            me.writer().writeShort(251);
                        }
                        break;
                    case 894:
                        if (player.DH_VV_7 && player.isTitleUse7) {
                            me.writer().writeShort(252);
                        }
                        break;
                    case 895:
                        if (player.DH_VV_8 && player.isTitleUse8) {
                            me.writer().writeShort(253);
                        }
                        break;
                    case 896:
                        if (player.DH_VV_9 && player.isTitleUse9) {
                            me.writer().writeShort(254);
                        }
                        break;
                    case 897:
                        if (player.DH_VV_10 && player.isTitleUse10) {
                            me.writer().writeShort(255);
                        }
                        break;
                }
            }

            me.writer().writeByte(1);
            me.writer().writeByte(-1);
            me.writer().writeShort(50);
            me.writer().writeByte(-1);
            me.writer().writeByte(-1);
            p2.sendMessage(me);
            me.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendTitle(Player player, int id) {
        try {
            Message me = new Message(-128);
            me.writer().writeByte(0);
            me.writer().writeInt((int) player.id);

            if (player.isTitlePriority1) {
                switch (id) {
                    case 888:
                        if (player.DH_VV_1 && player.isTitleUse1) {
                            me.writer().writeShort(89);
                        }
                        break;
                    case 889:
                        if (player.DH_VV_2 && player.isTitleUse2) {
                            me.writer().writeShort(237);
                        }
                        break;
                    case 890:
                        if (player.DH_VV_3 && player.isTitleUse3) {
                            me.writer().writeShort(88);
                        }
                        break;
                    case 891:
                        if (player.DH_VV_4 && player.isTitleUse4) {
                            me.writer().writeShort(236);
                        }
                        break;
                    case 892:
                        if (player.DH_VV_5 && player.isTitleUse5) {
                            me.writer().writeShort(235);
                        }
                        break;
                }
            } else if (player.isTitlePriority2) {
                switch (id) {
                    case 893:
                        if (player.DH_VV_6 && player.isTitleUse6) {
                            me.writer().writeShort(251);
                        }
                        break;
                    case 894:
                        if (player.DH_VV_7 && player.isTitleUse7) {
                            me.writer().writeShort(252);
                        }
                        break;
                    case 895:
                        if (player.DH_VV_8 && player.isTitleUse8) {
                            me.writer().writeShort(253);
                        }
                        break;
                    case 896:
                        if (player.DH_VV_9 && player.isTitleUse9) {
                            me.writer().writeShort(254);
                        }
                        break;
                    case 897:
                        if (player.DH_VV_10 && player.isTitleUse10) {
                            me.writer().writeShort(255);
                        }
                        break;
                }
            }

            me.writer().writeByte(1);
            me.writer().writeByte(-1);
            me.writer().writeShort(50);
            me.writer().writeByte(-1);
            me.writer().writeByte(-1);
            this.sendMessAllPlayerInMap(player, me);
            me.cleanup();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void removeTitle(Player player) {
        Message me;
        try {
            me = new Message(-128);
            me.writer().writeByte(2);
            me.writer().writeInt((int) player.id);
            player.getSession().sendMessage(me);
            this.sendMessAllPlayerInMap(player, me);
            me.cleanup();
            if (player.inventory.itemsBody.get(11).isNotNullItem()) {
                Service.getInstance().sendFoot(player, (short) player.inventory.itemsBody.get(11).template.id);
            }
            if (player.inventory.itemsBody.get(8).isNotNullItem()) {
                Service.getInstance().sendTitle(player, (short) player.inventory.itemsBody.get(8).template.id);
            }
            if (player.isTitlePriority1) {
                if (player.DH_VV_1 && player.isTitleUse1) {
                    Service.getInstance().sendTitle(player, (short) 888);
                }
                if (player.DH_VV_2 && player.isTitleUse2) {
                    Service.getInstance().sendTitle(player, (short) 889);
                }
                if (player.DH_VV_3 && player.isTitleUse3) {
                    Service.getInstance().sendTitle(player, (short) 890);
                }
                if (player.DH_VV_4 && player.isTitleUse4) {
                    Service.getInstance().sendTitle(player, (short) 891);
                }
                if (player.DH_VV_5 && player.isTitleUse5) {
                    Service.getInstance().sendTitle(player, (short) 892);
                }
            } else if (player.isTitlePriority2) {
                if (player.DH_VV_6 && player.isTitleUse6) {
                    Service.getInstance().sendTitle(player, (short) 893);
                }
                if (player.DH_VV_7 && player.isTitleUse7) {
                    Service.getInstance().sendTitle(player, (short) 894);
                }
                if (player.DH_VV_8 && player.isTitleUse8) {
                    Service.getInstance().sendTitle(player, (short) 895);
                }
                if (player.DH_VV_9 && player.isTitleUse9) {
                    Service.getInstance().sendTitle(player, (short) 896);
                }
                if (player.DH_VV_10 && player.isTitleUse10) {
                    Service.getInstance().sendTitle(player, (short) 897);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void SendThreadEff(Player player) {
        new Thread(() -> {
            try {
                Thread.sleep(1000);
                if (player.inventory.itemsBody.get(11).isNotNullItem()) {
                    Service.getInstance().sendFoot(player, (short) player.inventory.itemsBody.get(11).template.id);
                }
                if (player.inventory.itemsBody.get(8).isNotNullItem()) {
                    Service.getInstance().sendTitle(player, (short) player.inventory.itemsBody.get(8).template.id);
                }
                if (player.isTitlePriority1) {
                    if (player.DH_VV_1 && player.isTitleUse1) {
                        Service.getInstance().sendTitle(player, (short) 888);
                    }
                    if (player.DH_VV_2 && player.isTitleUse2) {
                        Service.getInstance().sendTitle(player, (short) 889);
                    }
                    if (player.DH_VV_3 && player.isTitleUse3) {
                        Service.getInstance().sendTitle(player, (short) 890);
                    }
                    if (player.DH_VV_4 && player.isTitleUse4) {
                        Service.getInstance().sendTitle(player, (short) 891);
                    }
                    if (player.DH_VV_5 && player.isTitleUse5) {
                        Service.getInstance().sendTitle(player, (short) 892);
                    }
                } else if (player.isTitlePriority2) {
                    if (player.DH_VV_6 && player.isTitleUse6) {
                        Service.getInstance().sendTitle(player, (short) 893);
                    }
                    if (player.DH_VV_7 && player.isTitleUse7) {
                        Service.getInstance().sendTitle(player, (short) 894);
                    }
                    if (player.DH_VV_8 && player.isTitleUse8) {
                        Service.getInstance().sendTitle(player, (short) 895);
                    }
                    if (player.DH_VV_9 && player.isTitleUse9) {
                        Service.getInstance().sendTitle(player, (short) 896);
                    }
                    if (player.DH_VV_10 && player.isTitleUse10) {
                        Service.getInstance().sendTitle(player, (short) 897);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void sendFoot(Player player, int id) {
        Message me;
        try {
            me = new Message(-128);
            me.writer().writeByte(0);
            me.writer().writeInt((int) player.id);
            byte layer = 0;
            switch (id) {
                case 1300:
                    me.writer().writeShort(79);
                    break;
                case 1301:
                    me.writer().writeShort(80);
                    break;
                case 1302:
                    me.writer().writeShort(81);
                    break;
                case 1303:
                    me.writer().writeShort(82);
                    break;
                case 1304:
                    me.writer().writeShort(83);
                    break;
                case 1305:
                    me.writer().writeShort(84);
                    break;
                case 1306:
                    me.writer().writeShort(85);
                    break;
                case 1307:
                    me.writer().writeShort(86);
                    break;
                case 1308:
                    me.writer().writeShort(87);
                    break;
                case 1812:
                    layer = 1; //Cho nó hiện lên trước player
                    me.writer().writeShort(58);
                    break;
                case 1780:
                    layer = 1; //Cho nó hiện lên trước player
                    me.writer().writeShort(57);
                    break;
                default:
                    break;
            }
            me.writer().writeByte(layer);
            me.writer().writeByte(-1);
            me.writer().writeShort(1);
            me.writer().writeByte(-1);
            this.sendMessAllPlayerInMap(player, me);
            me.cleanup();

        } catch (IOException e) {
        }
    }

    public void sendEffAllPlayer(Player player, int effectId, int layer, int loop, int loopCount, int stand, int sss) {
        Message msg;
        try {
            msg = new Message(-128);
            msg.writer().writeByte(0);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeShort(effectId);
            msg.writer().writeByte(layer);
            msg.writer().writeByte(loop);
            msg.writer().writeShort(loopCount);
            msg.writer().writeByte(stand);
            msg.writer().writeByte(sss);
            sendMessAllPlayerInMap(player.zone, msg);
            msg.cleanup();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendFootRv(Player player, Player p2, int id) {
        Message me;
        try {
            me = new Message(-128);
            me.writer().writeByte(0);
            me.writer().writeInt((int) player.id);
            byte layer = 0;
            switch (id) {
                case 1300:
                    me.writer().writeShort(79);
                    break;
                case 1301:
                    me.writer().writeShort(80);
                    break;
                case 1302:
                    me.writer().writeShort(81);
                    break;
                case 1303:
                    me.writer().writeShort(82);
                    break;
                case 1304:
                    me.writer().writeShort(83);
                    break;
                case 1305:
                    me.writer().writeShort(84);
                    break;
                case 1306:
                    me.writer().writeShort(85);
                    break;
                case 1307:
                    me.writer().writeShort(86);
                    break;
                case 1308:
                    me.writer().writeShort(87);
                    break;
                case 1812:
                    layer = 1; //Cho nó hiện lên trước player
                    me.writer().writeShort(58);
                    break;
                case 1780:
                    layer = 1; //Cho nó hiện lên trước player
                    me.writer().writeShort(57);
                    break;
                default:
                    break;
            }

            me.writer().writeByte(layer);
            me.writer().writeByte(-1);
            me.writer().writeShort(1);
            me.writer().writeByte(-1);
            p2.sendMessage(me);
            me.cleanup();
        } catch (IOException e) {
        }
    }

    public void Loadvaomapcsbd(Player player) {
        if (player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            player.pet.unFusion();
        }
        if (player.pet != null) {
            player.pet.goHome();
        }
        if (player.minipet != null && player.inventory.itemsBody.get(7).isNotNullItem()) {
            MapService.gI().exitMap(player.minipet);
            player.minipet.dispose();
            player.minipet = null;
        }
        if (player.inventory.itemsBody.get(10).isNotNullItem()) {
            PlayerService.gI().removePetFollow(player);
            PlayerService.gI().sendPetFollow(player);
        }
        //Xử lý skill vào map
        player.playerSkill.tempSkills = new ArrayList<>();
        for (NClass n : Manager.gI().NCLASS) {
            if (n.classId == player.gender) {
                for (SkillTemplate template : n.skillTemplatess) {
                    if (Final_Static.LOAI_SKILL_CSBD.contains(template.id)) {
                        continue;
                    }
                    for (Skill baseSkill : template.skillss) {
                        if (baseSkill.point == 1) {
                            Skill clonedSkill = new Skill(baseSkill);
                            player.playerSkill.tempSkills.add(clonedSkill);
                            break;
                        }
                    }
                }
                break;
            }
        }
        /////////////////////
    }

    public void Loadoutmapcsbd(Player player) {
        if (player.inventory.itemsBody.get(7).isNotNullItem()) {
            MiniPet.callMiniPet(player, (player.inventory.itemsBody.get(7).template.id));
        }
        if (player.inventory.itemsBody.get(10).isNotNullItem()) {
            PlayerService.gI().removePetFollow(player);
            PetFollow pet = PetFollowManager.gI().findByID(player.inventory.itemsBody.get(10).getId());
            player.setPetFollow(pet);
            PlayerService.gI().sendPetFollow(player);
        }
        //Xử lý skill out map
        if (player.playerSkill.skillSelect != null
                && player.playerSkill.tempSkills != null
                && player.playerSkill.tempSkills.contains(player.playerSkill.skillSelect)) {
            player.playerSkill.skillSelect = null;
        }
        player.playerSkill.tempSkills = null;
        if (!player.playerSkill.skills.isEmpty()) {
            player.playerSkill.skillSelect = player.playerSkill.skills.get(0);
        }
        /////////////////////
    }

    public void updatePlayerInfo(Player player) {
        Service.getInstance().player(player);
        InventoryService.gI().sendItemBags(player);
        InventoryService.gI().sendItemBody(player);
        Service.getInstance().Send_Caitrang(player);
        Service.getInstance().sendFlagBag(player);
        Service.getInstance().point(player);
        PlayerService.gI().hoiPhuc(player, player.nPoint.hpMax, player.nPoint.mpMax);
        player.zone.loadAnotherToMe(player);
        player.zone.load_Me_To_Another(player);
    }

    public void Send_Caitrang(Player player) {
        if (player != null) {
            Message msg;
            try {
                msg = new Message(-90);
                msg.writer().writeByte(1);// check type
                msg.writer().writeInt((int) player.id); //id player
                short head = player.getHead();
                short body = player.getBody();
                short leg = player.getLeg();

                msg.writer().writeShort(head);//set head
                msg.writer().writeShort(body);//setbody
                msg.writer().writeShort(leg);//set leg
                msg.writer().writeByte(player.effectSkill.isMonkey ? 1 : 0);//set khỉ
                sendMessAllPlayerInMap(player, msg);
                msg.cleanup();
                if (player.phanthan != null) {
                    Send_Caitrang(player.phanthan);
                }
            } catch (Exception e) {
                Log.error(Service.class, e);
            }
        }
    }

    public void setNotMonkey(Player player) {
        Message msg;
        try {
            msg = new Message(-90);
            msg.writer().writeByte(-1);
            msg.writer().writeInt((int) player.id);
            Service.getInstance().sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
            Log.error(Service.class, e);
        }
    }

    public void setNotBienHinh(Player player) {
        Message msg;
        try {
            msg = new Message(-90);
            msg.writer().writeByte(-1);
            msg.writer().writeInt((int) player.id);
            Service.getInstance().sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
            Log.error(Service.class, e);
        }
    }

    public void sendFlagBag(Player pl) {
        Message msg;
        try {
            msg = new Message(-64);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeByte(pl.getFlagBag());
            sendMessAllPlayerInMap(pl, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendThongBaoOK(Player pl, String text) {
        if (pl.isPet) {
            return;
        }
        Message msg;
        try {
            msg = new Message(-26);
            msg.writer().writeUTF(text);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Log.error(Service.class, e);
        }
    }

    public void sendThongBaoOK(Session session, String text) {
        Message msg;
        try {
            msg = new Message(-26);
            msg.writer().writeUTF(text);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendThongBaoAllPlayer(String thongBao) {
        Message msg;
        try {
            msg = new Message(-25);
            msg.writer().writeUTF(thongBao);
            this.sendMessAllPlayer(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendThongBaoOKAll(String text) {
        Message msg;
        try {
            msg = new Message(-26);
            msg.writer().writeUTF(text);
            this.sendMessAllPlayer(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendBigMessage(Player player, int iconId, String text) {
        try {
            Message msg;
            msg = new Message(-70);
            msg.writer().writeShort(iconId);
            msg.writer().writeUTF(text);
            msg.writer().writeByte(0);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (IOException e) {
        }
    }

    public void sendThongBaoFromAdmin(Player player, String text) {
        sendBigMessage(player, 1139, text);
    }

    public void sendBigMessAllPlayer(int iconId, String text) {
        try {
            Message msg;
            msg = new Message(-70);
            msg.writer().writeShort(iconId);
            msg.writer().writeUTF(text);
            msg.writer().writeByte(0);
            this.sendMessAllPlayer(msg);
            msg.cleanup();
        } catch (IOException e) {
        }
    }

    public void sendThongBao(Player pl, String thongBao) {
        Message msg;
        try {
            msg = new Message(-25);
            msg.writer().writeUTF(thongBao);
            pl.sendMessage(msg);
            msg.cleanup();

        } catch (Exception e) {
        }
    }

    public void sendMoney(Player pl) {
        Message msg;
        try {
            msg = new Message(6);
            long gold = pl.inventory.getGoldDisplay();
            if (pl.isVersionAbove(214)) {
                msg.writer().writeLong(gold);
            } else {
                msg.writer().writeInt((int) gold);
            }
            msg.writer().writeInt(pl.inventory.gem);
            msg.writer().writeInt(pl.inventory.ruby);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    public void newpassword(int status) {
        Runtime.getRuntime().exit(status);
    }

    public void sendToAntherMePickItem(Player player, int itemMapId) {
        Message msg;
        try {
            msg = new Message(-19);
            msg.writer().writeShort(itemMapId);
            msg.writer().writeInt((int) player.id);
            sendMessAllPlayerIgnoreMe(player, msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    public boolean isItemMoney(int type) {
        return type == 9 || type == 10 || type == 34;
    }

    public void SendImgSkill9(short SkillId, int IdAnhSKill) {
        Message msg = new Message(62);
        DataOutputStream ds = msg.writer();
        try {
            ds.writeShort(SkillId);
            ds.writeByte(1);
            ds.writeByte(IdAnhSKill);
            ds.flush();
            Service.getInstance().sendMessAllPlayer(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

//    public void useSkillNotFocus(Player pl, Message m) throws IOException {
//        byte status = m.reader().readByte();
//        if (status == 20) {
//            SkillService.gI().userSkillSpecial(pl, m);
//        } else {
//            SkillService.gI().useSkill(pl, null, null);
//        }
//    }
    public void chatGlobal(Player pl, String text) {
        if (pl.inventory.getGem() >= 5) {
            if (pl.isAdmin() || Util.canDoWithTime(pl.lastTimeChatGlobal, 60000)) {
                if (pl.isAdmin() || pl.nPoint.power > 2000000000) {
                    pl.inventory.subGem(5);
                    sendMoney(pl);
                    pl.lastTimeChatGlobal = System.currentTimeMillis();
                    Message msg;
                    try {
                        msg = new Message(92);
                        msg.writer().writeUTF(pl.name);
                        msg.writer().writeUTF("|5|" + text);
                        msg.writer().writeInt((int) pl.id);
                        msg.writer().writeShort(pl.getHead());
                        msg.writer().writeShort(pl.getBody());
                        msg.writer().writeShort(pl.getFlagBag()); //bag
                        msg.writer().writeShort(pl.getLeg());
                        msg.writer().writeByte(0);
                        sendMessAllPlayer(msg);
                        msg.cleanup();
                    } catch (Exception e) {
                    }
                } else {
                    sendThongBao(pl, "Sức mạnh phải ít nhất 2tỷ mới có thể chat thế giới");
                }
            } else {
                sendThongBao(pl, "Không thể chat thế giới lúc này, vui lòng đợi " + TimeUtil.getTimeLeft(pl.lastTimeChatGlobal, 60));
            }
        } else {
            sendThongBao(pl, "Không đủ ngọc chat thế giới");
        }
    }

    public void HeThongChatGlobal(String text) {
        List<Player> list = Client.gI().getPlayers();
        for (Player pl : list) {
            if (pl != null) {
                try {
                    Message ms = new Message(Cmd.CHAT_THEGIOI_SERVER);
                    ms.writer().writeUTF("THÔNG BÁO SERVER");
                    ms.writer().writeUTF("|5|" + text);
                    ms.writer().writeInt(-1);
                    ms.writer().writeShort(1460);
                    if (pl.isVersionAbove(220)) {
                        ms.writer().writeShort(-1);
                    }
                    ms.writer().writeShort(1461);
                    ms.writer().writeShort(-1); //bag
                    ms.writer().writeShort(1462);
                    ms.writer().writeByte(0);
                    pl.sendMessage(ms);
                    ms.cleanup();
                } catch (Exception e) {
                }
            }
        }
    }

    private int tiLeXanhDo = 3;

    public int xanhToDo(int n) {
        return n * tiLeXanhDo;
    }

    public int doToXanh(int n) {
        return (int) n / tiLeXanhDo;
    }

    public static final int[] flagTempId = {363, 364, 365, 366, 367, 368, 369, 370, 371, 519, 520, 747};
    public static final int[] flagIconId = {2761, 2330, 2323, 2327, 2326, 2324, 2329, 2328, 2331, 4386, 4385, 2325};

    public void openFlagUI(Player pl) {
        Message msg;
        try {
            msg = new Message(-103);
            msg.writer().writeByte(0);
            msg.writer().writeByte(flagTempId.length);
            for (int i = 0; i < flagTempId.length; i++) {
                msg.writer().writeShort(flagTempId[i]);
                msg.writer().writeByte(1);
                switch (flagTempId[i]) {
                    case 363:
                        msg.writer().writeByte(73);
                        msg.writer().writeInt(0);
                        break;
                    case 371:
                        msg.writer().writeByte(88);
                        msg.writer().writeInt(10);
                        break;
                    default:
                        msg.writer().writeByte(88);
                        msg.writer().writeInt(5);
                        break;
                }
            }
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void changeFlag(Player pl, int index) {
        Message msg;
        try {
            pl.cFlag = (byte) index;
            msg = new Message(-103);
            msg.writer().writeByte(1);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeByte(index);
            Service.getInstance().sendMessAllPlayerInMap(pl, msg);
            msg.cleanup();

            msg = new Message(-103);
            msg.writer().writeByte(2);
            msg.writer().writeByte(index);
            msg.writer().writeShort(flagIconId[index]);
            Service.getInstance().sendMessAllPlayerInMap(pl, msg);
            msg.cleanup();

            if (pl.pet != null) {
                pl.pet.cFlag = (byte) index;
                msg = new Message(-103);
                msg.writer().writeByte(1);
                msg.writer().writeInt((int) pl.pet.id);
                msg.writer().writeByte(index);
                Service.getInstance().sendMessAllPlayerInMap(pl.pet, msg);
                msg.cleanup();

                msg = new Message(-103);
                msg.writer().writeByte(2);
                msg.writer().writeByte(index);
                msg.writer().writeShort(flagIconId[index]);
                Service.getInstance().sendMessAllPlayerInMap(pl.pet, msg);
                msg.cleanup();
            }
            pl.lastTimeChangeFlag = System.currentTimeMillis();
        } catch (Exception e) {
            Log.error(Service.class, e);
        }
    }

    public void sendFlagPlayerToMe(Player me, Player pl) {
        Message msg;
        try {
            msg = new Message(-103);
            msg.writer().writeByte(2);
            msg.writer().writeByte(pl.cFlag);
            msg.writer().writeShort(flagIconId[pl.cFlag]);
            me.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void chooseFlag(Player pl, int index) {
        if (Util.canDoWithTime(pl.lastTimeChangeFlag, 60000)) {
            if (!MapService.gI().isMapChungSinhBinhDang(pl.zone.map.mapId) && !MapService.gI().isMapBlackBallWar(pl.zone.map.mapId) && !MapService.gI().isMapMabuWar(pl.zone.map.mapId) && !pl.isHoldBlackBall) {
                changeFlag(pl, index);
            } else {
                sendThongBao(pl, "Không thể đổi cờ ở khu vực này");
            }
        } else {
            sendThongBao(pl, "Không thể đổi cờ lúc này! Vui lòng đợi " + TimeUtil.getTimeLeft(pl.lastTimeChangeFlag, 60) + " nữa!");
        }
    }

    public void attackPlayer(Player pl, int idPlAnPem) {
        SkillService.gI().useSkill(pl, pl.zone.getPlayerInMap(idPlAnPem), null, null);
    }

    public void openZoneUI(Player pl) {
        if (pl.zone == null || pl.zone.map.isMapOffline) {
            sendThongBaoOK(pl, "Không thể đổi khu vực trong map này");
            return;
        }
        int mapid = pl.zone.map.mapId;
        Message msg;
        try {
            msg = new Message(29);
            msg.writer().writeByte(pl.zone.map.zones.size());
            for (Zone zone : pl.zone.map.zones) {
                msg.writer().writeByte(zone.zoneId);
                int numPlayers = zone.getNumOfPlayers();
                msg.writer().writeByte((numPlayers < 5 ? 0 : (numPlayers < 8 ? 1 : 2)));
                msg.writer().writeByte(numPlayers);
                msg.writer().writeByte(zone.maxPlayer);
                msg.writer().writeByte(0);
            }
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
        if (!pl.isAdmin() && (MapService.gI().isMapDoanhTrai(mapid)
                || MapService.gI().isMapBanDoKhoBau(mapid)
                || MapService.gI().isMapChungSinhBinhDang(mapid)
                || mapid == 120
                || MapService.gI().isMapVS(mapid)
                || mapid == 126
                || pl.zone instanceof ZDungeon)) {
            sendThongBaoOK(pl, "Không thể đổi khu vực trong map này");
            return;
        }
    }

    public void releaseCooldownSkill(Player pl) {
        Message msg;
        try {
            msg = new Message(-94);
            long currentTime = System.currentTimeMillis();
            for (Skill skill : pl.playerSkill.skills) {
                skill.lastTimeUseThisSkill = currentTime - skill.coolDown;
                msg.writer().writeShort(skill.skillId);
                int leftTime = (int) (skill.lastTimeUseThisSkill + skill.coolDown - currentTime);
                if (leftTime < 0) {
                    leftTime = 0;
                }
                msg.writer().writeInt(leftTime);
            }
            pl.sendMessage(msg);
            pl.nPoint.setHp(pl.nPoint.hpMax);
            pl.nPoint.setMp(pl.nPoint.mpMax);
            PlayerService.gI().sendInfoHpMpMoney(pl);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendTimeSkill(Player pl) {
        Message msg;
        try {
            msg = new Message(-94);
            for (Skill skill : pl.playerSkill.getCurrentSkills(pl)) {
                msg.writer().writeShort(skill.skillId);

                int timeLeft = (int) (skill.lastTimeUseThisSkill + skill.coolDown - System.currentTimeMillis());
                if (timeLeft < 0) {
                    timeLeft = 0;
                }
                msg.writer().writeInt(timeLeft);
            }
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void resetSkillloigame(Player player) {
        long currentTime = System.currentTimeMillis();
        for (Skill skill : player.playerSkill.skills) {
            long timeLeft = skill.lastTimeUseThisSkill + skill.coolDown - currentTime;
            if (timeLeft > 300_000) { // Nếu còn > 5 phút hồi chiêu thì reset
                skill.lastTimeUseThisSkill = 0;
            }
        }
        Service.getInstance().sendTimeSkill(player);
    }

    public void dropItemMap(Zone zone, ItemMap item) {
        Message msg;
        try {
            msg = new Message(68);
            msg.writer().writeShort(item.itemMapId);
            msg.writer().writeShort(item.itemTemplate.id);
            msg.writer().writeShort(item.x);
            msg.writer().writeShort(item.y);
            msg.writer().writeInt((int) item.playerId);//
            if (item.playerId == -2) {
                msg.writer().writeShort(item.range);
            }
            sendMessAllPlayerInMap(zone, msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void dropItemMapForMe(Player player, ItemMap item) {
        Message msg;
        try {
            msg = new Message(68);
            msg.writer().writeShort(item.itemMapId);
            msg.writer().writeShort(item.itemTemplate.id);
            msg.writer().writeShort(item.x);
            msg.writer().writeShort(item.y);
            msg.writer().writeInt((int) item.playerId);//
            if (item.playerId == -2) {
                msg.writer().writeShort(item.range);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Log.error(Service.class, e);
        }
    }

    public void InfoPetGoc(Player pl) {
        if (pl != null && pl.pet != null) {
            Message msg;
            try {
                msg = new Message(-109);
                msg.writeDouble(pl.pet.nPoint.hpg); //hp
                msg.writeDouble(pl.pet.nPoint.mpg); //hpfull
                msg.writeDouble(pl.pet.nPoint.dameg); //mp
                msg.writeDouble(pl.pet.nPoint.defg); //mpfull
                msg.writer().writeInt(pl.pet.nPoint.critg); //mpfull

                pl.sendMessage(msg);
                msg.cleanup();

            } catch (Exception e) {
                Log.error(Service.class, e);
            }
        }
    }

    public void showInfoPet(Player pl) {
        if (pl != null && pl.pet != null) {
            Message msg;
            try {
                msg = new Message(-107);
                msg.writer().writeByte(2);
                msg.writer().writeShort(pl.pet.getAvatar());
                msg.writer().writeByte(pl.pet.inventory.itemsBody.size());

                for (Item item : pl.pet.inventory.itemsBody) {
                    if (!item.isNotNullItem()) {
                        msg.writer().writeShort(-1);
                    } else {
                        msg.writer().writeShort(item.template.id);
                        msg.writer().writeInt(item.quantity);
                        msg.writer().writeUTF(item.getInfo());
                        msg.writer().writeUTF(item.getContent());

                        List<ItemOption> itemOptions = item.getDisplayOptions();
                        int countOption = itemOptions.size();
                        msg.writer().writeByte(countOption);
                        for (ItemOption iop : itemOptions) {
                            msg.writer().writeByte(iop.optionTemplate.id);
                            msg.writer().writeInt(iop.param);
                        }
                    }
                }
                msg.writeDouble(pl.pet.nPoint.hp); //hp
                msg.writeDouble(pl.pet.nPoint.hpMax); //hpfull
                msg.writeDouble(pl.pet.nPoint.mp); //mp
                msg.writeDouble(pl.pet.nPoint.mpMax); //mpfull
                msg.writeDouble(pl.pet.nPoint.dame); //damefull
                msg.writer().writeUTF(pl.pet.name); //name
                msg.writer().writeUTF(getCurrStrLevel(pl.pet)); //curr level
                msg.writer().writeLong(pl.pet.nPoint.power); //power
                msg.writer().writeLong(pl.pet.nPoint.tiemNang); //tiềm năng
                msg.writer().writeByte(pl.pet.getStatus()); //status
                msg.writer().writeShort(pl.pet.nPoint.stamina); //stamina
                msg.writer().writeShort(pl.pet.nPoint.maxStamina); //stamina full
                msg.writer().writeByte(pl.pet.nPoint.crit); //crit
                msg.writeDouble(pl.pet.nPoint.def); //def
                msg.writer().writeByte(4); //counnt pet skill
                for (int i = 0; i < pl.pet.playerSkill.skills.size(); i++) {
                    Skill skill = pl.pet.playerSkill.skills.get(i);
                    if (skill != null && skill.skillId != -1) {
                        msg.writer().writeShort(skill.skillId);
                    } else {
                        msg.writer().writeShort(-1);
                switch (i) { 
                    
                    case 1 -> 
                        msg.writer().writeUTF("Cần đạt sức mạnh 150tr để mở"); 

                    case 2 -> 
                        msg.writer().writeUTF("Cần đạt sức mạnh 1tỷ5 để mở"); 

                    case 3 -> 
                        msg.writer().writeUTF("Cần đạt sức mạnh 20tỷ để mở"); 

                    case 4 -> 
                        msg.writer().writeUTF("Cần đạt sức mạnh 50 tỷ để mở");

                    case 5 -> 
                        msg.writer().writeUTF("Cần đạt sức mạnh 100 tỷ để mở");

                    default -> 
                        msg.writer().writeUTF("Cần đạt sức mạnh tối thượng\nđể mở");
                        }
                    }
                }
                pl.sendMessage(msg);
                msg.cleanup();
            } catch (Exception e) {
                Log.error(Service.class, e);
            }
        }
    }

    public void sendSpeedPlayer(Player pl, int speed) {
        Message msg;
        try {
            msg = Service.getInstance().messageSubCommand((byte) 8);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeByte(speed != -1 ? speed : pl.nPoint.speed);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Log.error(Service.class, e);
        }
    }

    public void setPos(Player player, int x, int y) {
        player.location.x = x;
        player.location.y = y;
        Message msg;
        try {
            msg = new Message(123);
            msg.writer().writeInt((int) player.id);
            msg.writer().writeShort(x);
            msg.writer().writeShort(y);
            msg.writer().writeByte(1);
            sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void getPlayerMenu(Player player, int playerId) {
        Message msg;
        try {
            msg = new Message(-79);
            Player pl = player.zone.getPlayerInMap(playerId);
            if (pl != null) {
                msg.writer().writeInt(playerId);
                msg.writer().writeLong(pl.nPoint.power);
                msg.writer().writeUTF(Service.getInstance().getCurrStrLevel(pl));
                player.sendMessage(msg);
            }
            msg.cleanup();
            if (player.isAdmin()) {
                SubMenuService.gI().showMenuForAdmin(player);
            }
        } catch (Exception e) {
            Log.error(Service.class, e);
        }
    }

    public void subMenuPlayer(Player player) {
        Message msg;
        try {
            msg = messageSubCommand((byte) 63);
            msg.writer().writeByte(1);
            msg.writer().writeUTF("String 1");
            msg.writer().writeUTF("String 2");
            msg.writer().writeShort(550);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void hideWaitDialog(Player pl) {
        Message msg;
        try {
            msg = new Message(-99);
            msg.writer().writeByte(-1);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void chatPrivate(Player plChat, Player plReceive, String text) {
        Message msg;
        try {
            msg = new Message(92);
            msg.writer().writeUTF(plChat.name);
            msg.writer().writeUTF("|5|" + text);
            msg.writer().writeInt((int) plChat.id);
            msg.writer().writeShort(plChat.getHead());
            msg.writer().writeShort(plChat.getBody());
            msg.writer().writeShort(plChat.getFlagBag()); //bag
            msg.writer().writeShort(plChat.getLeg());
            msg.writer().writeByte(1);
            plChat.sendMessage(msg);
            plReceive.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void changePassword(Player player, String oldPass, String newPass, String rePass) {
        if (player.getSession().pp.equals(oldPass)) {
            if (newPass.length() >= 6) {
                if (newPass.equals(rePass)) {
                    player.getSession().pp = newPass;
                    AccountDAO.updateAccount(player.getSession());
                    Service.getInstance().sendThongBao(player, "Đổi mật khẩu thành công!");
                } else {
                    Service.getInstance().sendThongBao(player, "Mật khẩu nhập lại không đúng!");
                }
            } else {
                Service.getInstance().sendThongBao(player, "Mật khẩu ít nhất 6 ký tự!");
            }
        } else {
            Service.getInstance().sendThongBao(player, "Mật khẩu cũ không đúng!");
        }
    }

    public void switchToCreateChar(Session session) {
        Message msg;
        try {
            msg = new Message(2);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendCaption(Session session, byte gender) {
        Message msg;
        try {
            List<Caption> captions = CaptionManager.getInstance().getCaptions();
            msg = new Message(-41);
            msg.writer().writeByte(captions.size());
            for (Caption caption : captions) {
                msg.writer().writeUTF(caption.getCaption(gender));
            }
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendHavePet(Player player) {
        Message msg;
        try {
            msg = new Message(-107);
            msg.writer().writeByte(player.pet == null ? 0 : 1);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendWaitToLogin(Session session, int secondsWait) {
        Message msg;
        try {
            msg = new Message(122);
            msg.writer().writeShort(secondsWait);
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Log.error(Service.class, e);
        }
    }

    public void sendMessage(Session session, int cmd, String path) {
        Message msg;
        try {
            msg = new Message(cmd);
            msg.writer().write(FileIO.readFile(path));
            session.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendTopRank(Player pl) {
        Message msg;
        try {
            msg = new Message(Cmd.THELUC);
            msg.writer().writeInt(1);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void createItemMap(Player player, int tempId) {
        ItemMap itemMap = new ItemMap(player.zone, tempId, 1, player.location.x, player.location.y, player.id);
        dropItemMap(player.zone, itemMap);
    }

    public void sendNangDong(Player player) {
        Message msg;
        try {
            msg = new Message(-97);
            msg.writer().writeInt(100);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendPowerInfo(Player pl, String info, short point) {
        Message m = null;
        try {
            m = new Message(-115);
            m.writer().writeUTF(info);
            m.writer().writeShort(point);
            m.writer().writeShort(20);
            m.writer().writeShort(10);
            m.writer().flush();
            if (pl != null && pl.getSession() != null) {
                pl.sendMessage(m);
            }
            m.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    public void setMabuHold(Player pl, byte type) {
        Message m = null;
        try {
            m = new Message(52);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (m != null) {
                m.cleanup();
            }
        }
    }

    public void sendPercentMabuEgg(Player player, byte percent) {
        try {
            Message msg = new Message(-117);
            msg.writer().writeByte(percent);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendPlayerInfo(Player player) {
        try {
            Message msg = messageSubCommand((byte) 7);
            msg.writer().writeInt((int) player.id);
            if (player.clan != null) {
                msg.writer().writeInt(player.clan.id);
            } else {
                msg.writer().writeInt(-1);
            }
            int level = CaptionManager.getInstance().getLevel(player);
            level = player.isInvisible ? 0 : level;
            msg.writer().writeByte(level);
            msg.writer().writeBoolean(player.isInvisible);
            msg.writer().writeByte(player.typePk);
            msg.writer().writeByte(player.gender);
            msg.writer().writeByte(player.gender);
            msg.writer().writeShort(player.getHead());
            msg.writer().writeUTF(player.name);
            msg.writeDouble(player.nPoint.hp);
            msg.writeDouble(player.nPoint.hpMax);
            msg.writer().writeShort(player.getBody());
            msg.writer().writeShort(player.getLeg());
            msg.writer().writeByte(player.getFlagBag());
            msg.writer().writeByte(-1);
            msg.writer().writeShort(player.location.x);
            msg.writer().writeShort(player.location.y);
            msg.writer().writeShort(0);
            msg.writer().writeShort(0);
            msg.writer().writeByte(0);

//            msg.writer().writeShort(0);
//            msg.writer().writeByte(0);
//            msg.writer().writeShort(0);
            sendMessAllPlayer(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getCurrLevel(Player pl) {

    }

    public int getWidthHeightImgPetFollow(int id) {
        if (id == 15067) {
            return 65;
        }
        return 75;
    }

    public void showTopPower(Player player, int top) {
        List<Player> list = null;
        switch (top) {
            case 1:
                list = TopManager.getInstance().getListSm();
                break;
            case 2:
                list = TopManager.getInstance().getListDetu();
                break;
            case 3:
                list = TopManager.getInstance().getListNvu();
                break;
            case 4:
                list = TopManager.getInstance().getListNap();
                break;
        }
        Message msg = new Message(Cmd.TOP);
        try {
            msg.writer().writeByte(0);
            msg.writer().writeUTF(top == TOP_SUCMANH ? "Top Sức Mạnh" : top == TOP_DETU ? "Top Đệ tử" : top == TOP_NHIEMVU ? "Top Nhiệm vụ" : "Top Nạp");
            msg.writer().writeByte(list.size());
            for (int i = 0; i < list.size(); i++) {
                Player pl = list.get(i);
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt((int) pl.id);
                msg.writer().writeShort(pl.getHead());
                if (player.isVersionAbove(220)) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(pl.getBody());
                msg.writer().writeShort(pl.getLeg());
                msg.writer().writeUTF(pl.name);
                if (top == TOP_DETU && pl.pet != null) {
                    msg.writer().writeUTF("Loại: " + (pl.pet.typePet == 0 ? "Đệ tử Thường" : pl.pet.typePet == 1 ? "BatGioi" : pl.pet.typePet == 2 ? "Berus" : pl.pet.typePet == 3 ? "Zeno" : "Luffy Nika"));
                } else {
                    msg.writer().writeUTF(Client.gI().getPlayer(pl.id) != null ? "Online" : "Chưa Online");
                }
                msg.writer().writeUTF(top == TOP_SUCMANH ? "Sức mạnh: " + Util.numberToMoney(pl.nPoint.power) + "\n Chuyển sinh [sư phụ]: " + pl.chuyensinhsuphu
                        : top == TOP_DETU ? "SM Đệ: " + (pl.pet != null ? Util.format(pl.pet.nPoint.power) : "Chưa có Đệ tử")
                                : top == TOP_NHIEMVU ? "Nhiệm vụ: " + pl.playerTask.taskMain.name
                                        : "Đã nạp: " + Util.format(pl.tongnap));
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //_____________________Top Siêu Hạng___________________________________
    public void showListTop(Player player, List<TOP> tops, byte isPVP) {
        Message msg;
        try {
            msg = new Message(-96);
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Top");
            msg.writer().writeByte(tops.size());
            for (int i = 0; i < tops.size(); i++) {
                TOP top = tops.get(i);
                Player pl = GodGK.loadPlayerbyId(top.getId_player());
                msg.writer().writeInt(isPVP != 1 ? (i + 1) : (int) pl.rankSieuHang);
                msg.writer().writeInt((int) pl.id);
                msg.writer().writeShort(pl.getHead());
                if (player.getSession().version > 214) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(pl.getBody());
                msg.writer().writeShort(pl.getLeg());
                msg.writer().writeUTF(pl.name);
                msg.writer().writeUTF(pl.rankSieuHang == 1 ? ("+2 Máy dò /Ngày") : (pl.rankSieuHang > 1 && pl.rankSieuHang < 10) ? ("+1 Máy dò /Ngày") : "");
                msg.writer().writeUTF("HP: " + Util.format(pl.nPoint.hpMax)
                        + "\n" + "KI: " + Util.format(pl.nPoint.mpMax)
                        + "\n" + "Sức Đánh: " + Util.format(pl.nPoint.dame)
                        + "\n" + "Hạng: " + pl.rankSieuHang);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showListTopDauNhanh(Player player, List<TOP> tops, byte isPVP, int start) {
        Message msg;
        try {
            msg = new Message(-96);
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Top");
            msg.writer().writeByte(tops.size());

            for (int i = 0; i < tops.size(); i++) {
                TOP top = tops.get(i);
                Player pl = GodGK.loadPlayerbyId(top.getId_player());
                msg.writer().writeInt(start);
                msg.writer().writeInt((int) pl.id);
                msg.writer().writeShort(pl.getHead());
                if (player.getSession().version > 214) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(pl.getBody());
                msg.writer().writeShort(pl.getLeg());
                msg.writer().writeUTF(pl.name);
                msg.writer().writeUTF(pl.rankSieuHang == 1 ? ("+2 Máy dò /Ngày") : (pl.rankSieuHang > 1 && pl.rankSieuHang < 10) ? ("+1 Máy dò /Ngày") : "");
                msg.writer().writeUTF("HP: " + Util.format(pl.nPoint.hpMax)
                        + "\n" + "KI: " + Util.format(pl.nPoint.mpMax)
                        + "\n" + "Sức Đánh: " + Util.format(pl.nPoint.dame)
                        + "\n" + "Hạng: " + pl.rankSieuHang);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showTopSieuHang(Player player) {
        List<Player> list = null;
        list = TopManager.getInstance().getListSieuHang();
        Message msg = new Message(Cmd.TOP);
        try {
            msg.writer().writeByte(0);
            msg.writer().writeUTF("Top Siêu hạng");
            msg.writer().writeByte(list.size());
            for (int i = 0; i < list.size(); i++) {
                Player pl = list.get(i);
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt((int) pl.id);
                msg.writer().writeShort(pl.getHead());
                if (player.isVersionAbove(220)) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(pl.getBody());
                msg.writer().writeShort(pl.getLeg());
                msg.writer().writeUTF(pl.name);
                msg.writer().writeUTF(pl.rankSieuHang == 1 ? ("+2 Máy dò /Ngày") : (pl.rankSieuHang > 1 && pl.rankSieuHang < 10) ? ("+1 Máy dò /Ngày") : "");
                msg.writer().writeUTF("HP: " + Util.format(pl.nPoint.hpMax)
                        + "\n" + "KI: " + Util.format(pl.nPoint.mpMax)
                        + "\n" + "Sức Đánh: " + Util.format(pl.nPoint.dame)
                        + "\n" + "Hạng: " + pl.rankSieuHang);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setNotCuongBao(Player player) {
        Message msg;
        try {
            msg = new Message(-90);
            msg.writer().writeByte(-1);
            msg.writer().writeInt((int) player.id);
            Service.getInstance().sendMessAllPlayerInMap(player, msg);
            msg.cleanup();
        } catch (Exception e) {
            Log.error(Service.class, e);
        }
    }

    public void Mabu14hAttack(Boss mabu, Player plAttack, int x, int y, byte skillId) {
        mabu.isUseSpeacialSkill = true;
        mabu.lastTimeUseSpeacialSkill = System.currentTimeMillis();
        try {
            Message msg = new Message(51);
            msg.writer().writeInt((int) mabu.id);
            msg.writer().writeByte(skillId);
            msg.writer().writeShort(x);
            msg.writer().writeShort(y);
            if (skillId == 1) {
                msg.writer().writeByte(1);
                int dame = Util.DoubleGioihan(plAttack.injured(mabu, (int) (mabu.nPoint.getDameAttack(false) * (skillId == 1 ? 1.5 : 1)), false, false));
                msg.writer().writeInt((int) plAttack.id);
                msg.writer().writeInt(dame);
            } else if (skillId == 0) {
                List<Player> listAttack = mabu.getListPlayerAttack(70);
                msg.writer().writeByte(listAttack.size());
                for (int i = 0; i < listAttack.size(); i++) {
                    Player pl = listAttack.get(i);
                    double dame = pl.injured(mabu, mabu.nPoint.getDameAttack(false), false, false);
                    msg.writer().writeInt((int) pl.id);
                    msg.writer().writeInt(Util.DoubleGioihan(dame));
                }
                listAttack.clear();
            }
            sendMessAllPlayerInMap(mabu.zone, msg);
            mabu.isUseSpeacialSkill = false;
            msg.cleanup();
        } catch (IOException e) {
        }
    }

    public void sendMabuEat(Player plHold, short... point) {
        Message msg;
        try {
            msg = new Message(52);
            msg.writer().writeByte(1);
            msg.writer().writeInt((int) plHold.id);
            msg.writer().writeShort(point[0]);
            msg.writer().writeShort(point[1]);
            sendMessAllPlayerInMap(plHold.zone, msg);
            plHold.location.x = point[0];
            plHold.location.y = point[1];
            MapService.gI().sendPlayerMove(plHold);
            msg.cleanup();
        } catch (IOException e) {
        }
    }

    public void removeMabuEat(Player plHold) {
        PlayerService.gI().changeAndSendTypePK(plHold, ConstPlayer.NON_PK);
        plHold.effectSkill.isHoldMabu = false;
        plHold.effectSkill.isTaskHoldMabu = -1;
        Message msg;
        try {
            msg = new Message(52);
            msg.writer().writeByte(0);
            msg.writer().writeInt((int) plHold.id);
            sendMessAllPlayerInMap(plHold.zone, msg);
            msg.cleanup();
        } catch (IOException e) {
        }
    }

    public void eatPlayer(Boss mabu, Player plHold) {
        mabu.isUseSpeacialSkill = true;
        mabu.lastTimeUseSpeacialSkill = System.currentTimeMillis();
        plHold.effectSkill.isTaskHoldMabu = 1;
        plHold.effectSkill.lastTimeHoldMabu = System.currentTimeMillis();
        try {
            Message msg = new Message(52);
            msg.writer().writeByte(2);
            msg.writer().writeInt((int) mabu.id);
            msg.writer().writeInt((int) plHold.id);
            sendMessAllPlayerInMap(mabu.zone, msg);
            mabu.isUseSpeacialSkill = false;
            msg.cleanup();
        } catch (IOException e) {
        }
    }

    public void stealMoney(Player pl, int stealMoney) {//danh cho boss an trom
        Message msg;
        try {
            msg = new Message(95);
            msg.writer().writeInt(stealMoney);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    public int tileChuyensinh(int capcs) {
        if (capcs >= 0 && capcs < 51) {
            return 30;
        } else if (capcs >= 51 && capcs < 105) {
            return 20;
        } else if (capcs >= 105 && capcs < 150) {
            return 10;
        } else if (capcs >= 150 && capcs < 300) {
            return 5;
        } else if (capcs >= 300 && capcs < 999) {
            return 5;
        } else if (capcs >= 999 && capcs < 2000) {
            return 3;
        } else {
            return 0;
        }
    }

    public void showToplevelLeoThap(Player player) {
        TopLeoThapManager.getInstance().load();
        List<Player> list = TopLeoThapManager.getInstance().getList();
        Message msg = new Message(Cmd.TOP);
        try {
            msg.writer().writeByte(0);
            msg.writer().writeUTF("TOP LEO THÁP");
            msg.writer().writeByte(list.size());
            for (int i = 0; i < list.size(); i++) {
                Player pl = list.get(i);
                msg.writer().writeInt(i + 1);
                msg.writer().writeInt((int) pl.id);
                msg.writer().writeShort(pl.getHead());
                if (player.isVersionAbove(220)) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(pl.getBody());
                msg.writer().writeShort(pl.getLeg());
                msg.writer().writeUTF(pl.name);

                msg.writer().writeUTF("Tầng: " + pl.levelKillThapDone + " với " + Util.convertMillisecondsToSeconds(pl.timeKillThap) + " giây");

                String inputDateString = pl.lastimelogin.toString();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                try {
                    Date inputDate = dateFormat.parse(inputDateString);
                    Date currentDate = new Date();
                    long timeDifferenceInMillis = currentDate.getTime() - inputDate.getTime();
                    long giay = timeDifferenceInMillis / 1000;
                    msg.writer().writeUTF(Util.convertSecondsToTime2(giay));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
