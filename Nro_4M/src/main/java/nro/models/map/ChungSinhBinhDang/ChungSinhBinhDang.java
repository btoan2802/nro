package nro.models.map.ChungSinhBinhDang;

import editor.Final_Static;
import nro.models.player.Player;
import nro.services.Service;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import nro.consts.ConstItem;
import nro.models.boss.Boss;
import nro.models.item.Item;
import nro.models.item.ItemTime;
import nro.models.map.ChungSinhBinhDang.ChungSinhBinhDangService.TrangThaiTran;
import nro.models.map.ItemMap;
import nro.models.map.Zone;
import nro.server.io.Message;
import nro.services.ChatGlobalService;
import nro.services.InventoryService;
import nro.services.ItemService;
import nro.services.ItemTimeService;
import nro.services.MapService;
import nro.services.PlayerService;
import nro.services.func.ChangeMapService;
import nro.services.func.EffectMapService;
import nro.utils.FileUtils;
import nro.utils.Util;

public class ChungSinhBinhDang {

    public static final byte LOSE = 0;
    public static final byte WIN = 1;

    private static ChungSinhBinhDang instance;
    private TrangThaiTran trangThai = TrangThaiTran.KET_THUC;
    private final List<Player> danhSachDangKy = new ArrayList<>();
    private final List<Integer> danhSachMapBo = new ArrayList<>();
    private final List<Integer> danhSachMapDaBo = new ArrayList<>();
    private final List<Integer> DANH_SACH_MAP = List.of(219, 220, 221, 225, 222, 224, 223);
    private final Map<Player, Long> thoiGianThongBaoBo = new HashMap<>();
    public final List<Boss> bosses = new ArrayList<>();

    private long thoiGianBoLanCuoi = 0;
    private long thoiGianRoiItemLanCuoi = 0;
    private long thoiGianBatDau = 0;
    private long thoiGianThongBaoLanCuoi = 0;
    private int soLanBo = 0;

    private static final int MAX_PLAYER_THAM_GIA = 35; // số người chơi tham gia
    private static final int PHAN_TRAM_MAU_MOI_BO = 3; // Máu trừ bo %
    private static final int TIME_THU_BO = 3; //Thời gian bo thu tính phút
    private static final long TIME_ROI_ITEM = 30_000L; // cứ 30 giây rơi item giữa map
    private static final long THOI_GIAN_THONG_BAO_BO = 15_000L; // thời gian thông báo ngoài bo

    public static ChungSinhBinhDang gI() {
        if (instance == null) {
            instance = new ChungSinhBinhDang();
        }
        return instance;
    }

    public void dangKy(Player p, boolean isDangKy) {
        if (this.trangThai != TrangThaiTran.CHO_DANG_KY) {
            Service.getInstance().sendThongBao(p, "Hiện không phải thời gian đăng ký hoặc hủy.");
            return;
        }
        if (isDangKy) {
            if (this.danhSachDangKy.contains(p)) {
                Service.getInstance().sendThongBao(p, "Bạn đã đăng ký rồi!");
                return;
            }
            if (this.danhSachDangKy.size() >= MAX_PLAYER_THAM_GIA) {
                Service.getInstance().sendThongBao(p, "Sự kiện đã đủ " + MAX_PLAYER_THAM_GIA + " người tham gia!");
                return;
            }
            this.danhSachDangKy.add(p);
            ChangeMapService.gI().changeMap(p, 226, -1, Util.nextInt(150, 750), 336);
            Service.getInstance().sendThongBao(p, "Bạn đã đăng ký tham gia sự kiện thành công!");
        } else {
            if (this.danhSachDangKy.remove(p)) {
                ChangeMapService.gI().changeMapBySpaceShip(p, p.gender + 21, 0, -1);
                Service.getInstance().sendThongBao(p, "Bạn đã hủy đăng ký tham gia sự kiện.");
            } else {
                Service.getInstance().sendThongBao(p, "Bạn chưa đăng ký sự kiện.");
            }
        }
    }

    public void batDauChuanBi() {
        this.thoiGianThongBaoLanCuoi = 0;
        for (Player p : getDangKyHopLe()) {
            Service.getInstance().sendThongBao(p, "Chuẩn bị chiến đấu, bạn có vài phút chuẩn bị!");
        }
    }

    public void xuLyTrongThoiGianChuanBi(long now, long endChuanBi) {
        this.danhSachDangKy.removeIf(p -> !isPlayerHopLe(p));
        long conLai = endChuanBi - now;
        if (conLai <= 2 * 60_000L) {
            if (now - this.thoiGianThongBaoLanCuoi >= 30_000L) {
                this.thoiGianThongBaoLanCuoi = now;
                for (Player p : this.danhSachDangKy) {
                    Service.getInstance().sendThongBao(p, "Trận đấu sẽ bắt đầu trong " + (conLai / 1000) + " giây nữa!");
                }
            }
        }
    }

    public void batDauTranDau() {
        List<Player> danhSachThamChien = new ArrayList<>();
        for (Player p : this.getDangKyHopLe()) {
            danhSachThamChien.add(p);
        }
        if (danhSachThamChien.size() < 2) {
            for (Player p : danhSachThamChien) {
                ChangeMapService.gI().changeMapBySpaceShip(p, p.gender + 21, 0, -1);
                Service.getInstance().sendThongBao(p, "Không đủ người tham gia, trận đấu bị hủy.");
            }
            this.dispose();
            this.setTrangThai(TrangThaiTran.KET_THUC);
            return;
        }
        for (Player p : danhSachThamChien) {
            try {
                int randomMapId = this.DANH_SACH_MAP.get(Util.nextInt(this.DANH_SACH_MAP.size()));
                ChangeMapService.gI().changeMapInYard(p, randomMapId, -1, -1);
                Service.getInstance().changeFlag(p, 0);
                Service.getInstance().sendThongBao(p, "Trận chiến đã bắt đầu! Bạn được đưa vào khu vực ngẫu nhiên.");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.dispose();
        this.danhSachDangKy.addAll(danhSachThamChien);
        this.danhSachMapBo.addAll(DANH_SACH_MAP);
        this.thoiGianBatDau = System.currentTimeMillis();
        this.sendTextChunhSinhBinhDang();
    }

    public void xuLyVongBo() {
        long now = System.currentTimeMillis();
        if (this.soLanBo < this.DANH_SACH_MAP.size() && now - this.thoiGianBoLanCuoi >= TIME_THU_BO * 60_000L) {
            this.thoiGianBoLanCuoi = now;
            int mapBo = this.DANH_SACH_MAP.get(this.soLanBo);
            this.danhSachMapBo.remove((Integer) mapBo);
            danhSachMapDaBo.add(mapBo);
            for (Player p : this.getDangKyHopLe()) {
                Service.getInstance().sendThongBao(p, "Vòng bo đã thu hẹp!\nCác sky vào khu vực này an toàn ngay.");
            }
            this.soLanBo++;
            this.sendTextChunhSinhBinhDang();
        }
    }

    public void xuLyTruMauBoChoToanBo() {
        long now = System.currentTimeMillis();
        for (Player p : new ArrayList<>(this.danhSachDangKy)) {
            if (!isPlayerHopLe(p)) {
                continue;
            }
            if (p.isDie()) {
                duaVeNha(p, "Bạn đã tử vong và được đưa về nhà.");
                sendWinAndLosePlayer(p, (byte) LOSE);
                continue;
            }
            if (this.danhSachMapDaBo.contains(p.zone.map.mapId)) {
                double damage = (p.nPoint.hpMax * soLanBo * PHAN_TRAM_MAU_MOI_BO / 100);
                if (Util.canDoWithTime(p.lastTimeAnXienTrapCSBD, 1000)) {
                    p.nPoint.subHP(damage + (Util.nextInt(-10, 10)));
                    PlayerService.gI().sendInfoHp(p);
                    EffectMapService.gI().sendEffectMapToAllInMap(p.zone, 49, 2, 1, p.location.x - 32, p.location.y + 24, 1);
                    EffectMapService.gI().sendEffectMapToAllInMap(p.zone, 37, 3, 1, p.location.x, p.location.y + 24, 2);
                    p.lastTimeAnXienTrapCSBD = now;
                }
                long lastNotify = this.thoiGianThongBaoBo.getOrDefault(p, 0L);
                if (now - lastNotify >= THOI_GIAN_THONG_BAO_BO) {
                    Service.getInstance().sendThongBao(p, "Bạn đang ở ngoài vùng an toàn! -" + Util.format(damage) + " HP (" + soLanBo * PHAN_TRAM_MAU_MOI_BO + "%)");
                    this.thoiGianThongBaoBo.put(p, now);
                }
                if (p.isDie()) {
                    duaVeNha(p, "Bạn đã tử vong vì ở ngoài vòng bo!");
                    sendWinAndLosePlayer(p, (byte) LOSE);
                }
            } else {
                this.thoiGianThongBaoBo.remove(p);
            }
        }
    }

    private void duaVeNha(Player p, String thongBao) {
        if (isPlayerHopLe(p)) {
            Service.getInstance().sendThongBao(p, thongBao);
            p.isGoHome = true;
            ChangeMapService.gI().changeMapBySpaceShip(p, p.gender + 21, 0, -1);
            p.isGoHome = false;
            ItemTimeService.gI().removeTextTime(p, ItemTime.CHUNG_SINH_BINH_DANG);
            this.danhSachDangKy.remove(p);
            this.sendTextChunhSinhBinhDang();
        }
    }

    public void kiemTraNguoiSongSot() {
        List<Player> songSot = new ArrayList<>();
        for (Player p : new ArrayList<>(this.danhSachDangKy)) {
            if (!isPlayerHopLe(p)) {
                this.danhSachDangKy.remove(p);
            } else if (p.isDie()) {
                duaVeNha(p, "Bạn đã tử vong!");
                sendWinAndLosePlayer(p, (byte) LOSE);
            } else {
                songSot.add(p);
            }
        }
        if (songSot.size() == 1) {
            Player winner = songSot.get(0);
            ChatGlobalService.gI().chatnew(winner, "Chúc Mừng " + winner.name + " – Kẻ chiến thắng chúng sinh bình đẳng!");
            ketThucTranDau();
            sendWinAndLosePlayer(winner, (byte) WIN);
            sendMailRewardForWinner(winner);
        } else if (songSot.isEmpty()) {
            ketThucTranDau();
        }
    }

    public void sendMailRewardForWinner(Player winner) {
        if (winner != null) {
            List<Item> rewards = new ArrayList<>();
            
            Item thoiVang = ItemService.gI().createNewItem((short) ConstItem.THOI_VANG);
            thoiVang.quantity = 25000;
            winner.inventory.itemsMailBox.add(thoiVang);
            rewards.add(thoiVang);

            Item hongNgoc = ItemService.gI().createNewItem((short) ConstItem.HONG_NGOC);
            hongNgoc.quantity = 25000;
            winner.inventory.itemsMailBox.add(hongNgoc);
            rewards.add(hongNgoc);

            FileUtils.logRewardItems(winner, rewards, "Phần thưởng TOP 1 Chúng Sinh Bình Đẳng");

            Service.getInstance().sendThongBao(winner, "Phần thưởng được gửi vào trong hòm thư");
        }
    }

    public List<Player> getNguoiSongSot() {
        List<Player> songSot = new ArrayList<>();
        for (Player p : this.danhSachDangKy) {
            if (isPlayerHopLe(p) && !p.isDie()) {
                songSot.add(p);
            }
        }
        return songSot;
    }

    public void ketThucTranDau() {
        for (Player p : new ArrayList<>(this.danhSachDangKy)) {
            duaVeNha(p, "Sự kiện Chúng Sinh Bình Đẳng đã kết thúc.");
        }
        this.dispose();
        setTrangThai(TrangThaiTran.KET_THUC);
    }

    public void xuLyRoiItem() {
        long now = System.currentTimeMillis();
        if (this.trangThai == TrangThaiTran.DA_BAT_DAU && now - this.thoiGianRoiItemLanCuoi >= TIME_ROI_ITEM) {
            this.thoiGianRoiItemLanCuoi = now;

            for (Integer mapId : this.danhSachMapBo) {
                Zone zone = MapService.gI().getZoneJoinByMapIdAndZoneId(null, mapId, 0);
                if (zone != null) {
                    int x = 1500;
                    int y = zone.map.yPhysicInTop(x, 216);
                    switch (zone.map.mapId) {
                        case 219 -> {
                            x = (short) 1441;
                            y = (short) 360;
                        }
                        case 220 -> {
                            x = (short) 1453;
                            y = (short) 408;
                        }
                        case 221 -> {
                            x = (short) 1441;
                            y = (short) 360;
                        }
                        case 222 -> {
                            x = (short) 1486;
                            y = (short) 216;
                        }
                        case 223 -> {
                            x = (short) 1441;
                            y = (short) 360;
                        }
                        case 224 -> {
                            x = (short) 1486;
                            y = (short) 216;
                        }
                        case 225 -> {
                            x = (short) 1514;
                            y = (short) 408;
                        }
                        default -> {
                        }
                    }

                    ItemMap itemMap = Util.Randomchisochungsinhbinhdang(zone, Final_Static.VatPhamRoiChungSinhBinhDang[Util.nextInt(0, Final_Static.VatPhamRoiChungSinhBinhDang.length - 1)], 1, x, y, -1);
                    Service.getInstance().dropItemMap(zone, itemMap);

                    for (Player p : zone.getPlayers()) {
                        if (isPlayerHopLe(p) && !p.isDie()) {
                            Service.getInstance().sendThongBao(p, "Một số vật phẩm quý giá vừa xuất hiện ở giữa bản đồ!");
                        }
                    }
                }
            }
        }
    }

    public void ChangeFlagAll() {
        for (Player p : getDangKyHopLe()) {
            Service.getInstance().changeFlag(p, 8);
            Service.getInstance().sendThongBao(p, "Cuộc chiến bắt đầu!");
        }
    }

    public void dispose() {
        for (Player p : this.danhSachDangKy) {
            Service.getInstance().clearSpecificItems(p);
            ItemTimeService.gI().removeTextTime(p, ItemTime.CHUNG_SINH_BINH_DANG);
        }
        this.removeAllBoss();
        this.danhSachDangKy.clear();
        this.danhSachMapBo.clear();
        this.danhSachMapDaBo.clear();
        this.soLanBo = 0;
        this.thoiGianBoLanCuoi = 0;
        this.thoiGianThongBaoLanCuoi = 0;
        this.thoiGianThongBaoBo.clear();
        this.thoiGianBatDau = 0;
    }

    public void removeAllBoss() {
        for (Boss boss : bosses) {
            boss.leaveMap();
        }
        this.bosses.clear();
    }

    public void setDie_dropItem(Player plDie, Player plAtt) {
        if (isPlayerHopLe(plDie)) {
            for (int u = 0; u < plDie.inventory.itemsBodyTemp.size(); u++) {
                Item itemThrow = plDie.inventory.itemsBodyTemp.get(u);
                if (itemThrow.isNotNullItem()) {
                    ItemMap itemMap = new ItemMap(plDie.zone, itemThrow.template.id,
                            itemThrow.quantity, plDie.location.x + Util.nextInt(-10, 10), plDie.location.y, -1);
                    itemMap.options = itemThrow.itemOptions;
                    Service.getInstance().dropItemMap(plDie.zone, itemMap);

                    // XÓA ITEM SAU KHI RƠI
                    InventoryService.gI().subQuantityItem(plDie.inventory.itemsBodyTemp, itemThrow, itemThrow.quantity);
                }
            }

            for (int k = 0; k < plDie.inventory.itemsBagTemp.size(); k++) {
                Item itemThrow = plDie.inventory.itemsBagTemp.get(k);
                if (itemThrow.isNotNullItem()) {
                    ItemMap itemMap = new ItemMap(plDie.zone, itemThrow.template.id,
                            itemThrow.quantity, plDie.location.x + Util.nextInt(-10, 10), plDie.location.y, -1);
                    itemMap.options = itemThrow.itemOptions;
                    Service.getInstance().dropItemMap(plDie.zone, itemMap);

                    // XÓA ITEM SAU KHI RƠI
                    InventoryService.gI().subQuantityItem(plDie.inventory.itemsBagTemp, itemThrow, itemThrow.quantity);
                }
            }
            InventoryService.gI().sendItemBags(plDie);
            InventoryService.gI().sendItemBody(plDie);
            Service.getInstance().charDie(plDie);
        }
    }

    private void sendTextChunhSinhBinhDang() {
        for (Player p : getDangKyHopLe()) {
            ItemTimeService.gI().sendTextChungSinhBinhDang(p);
        }
    }

    private List<Player> getDangKyHopLe() {
        List<Player> result = new ArrayList<>();
        for (Player p : this.danhSachDangKy) {
            if (isPlayerHopLe(p)) {
                result.add(p);
            }
        }
        return result;
    }

    private boolean isPlayerHopLe(Player p) {
        return p != null && p.session != null && p.zone != null && p.zone.map != null && MapService.gI().isMapChungSinhBinhDang(p.zone.map.mapId);
    }

    public void kickmap(Player player) {
        if (player.zone != null && MapService.gI().isMapChungSinhBinhDang(player.zone.map.mapId)) {
            if (this.trangThai == TrangThaiTran.KET_THUC && Final_Static.LOCK_MAP_CHUNGSINHBINHDANG) {
                Service.getInstance().sendThongBao(player, "Sự kiện đã kết thúc! Hẹn gặp lại ở giờ tiếp theo.");
                player.isGoHome = true;
                ChangeMapService.gI().changeMapBySpaceShip(player, player.gender + 21, 0, -1);
                player.isGoHome = false;
            }
        }
    }

    public void sendWinAndLosePlayer(Player player, byte type) {//thua 0, thắng 1
        Message msg;
        try {
            msg = new Message(20);
            msg.writer().writeByte(0);
            msg.writer().writeByte(2);
            msg.writer().writeByte(type);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public TrangThaiTran getTrangThai() {
        return this.trangThai;
    }

    public int getMaxPlayer() {
        return MAX_PLAYER_THAM_GIA;
    }

    public void setTrangThai(TrangThaiTran trangThai) {
        this.trangThai = trangThai;
    }

    public long getThoiGianBatDau() {
        return thoiGianBatDau;
    }

    public int getSoNguoiDangKy() {
        return this.danhSachDangKy.size();
    }

    public int getSoLanBo() {
        return this.soLanBo;
    }

    public int getTongSoLanBo() {
        return this.DANH_SACH_MAP.size();
    }

    public boolean isPlayerDangKy(Player player) {
        return danhSachDangKy.contains(player);
    }

    public boolean isTrangThaiDangChien() {
        return !(trangThai == TrangThaiTran.CHO_DANG_KY
                || trangThai == TrangThaiTran.CHUAN_BI
                || trangThai == TrangThaiTran.KET_THUC);
    }

    public boolean isDangChienVaThamGia(Player player) {
        return isTrangThaiDangChien() && isPlayerDangKy(player);
    }

}
