package nro.models.map.ChungSinhBinhDang;

import java.util.logging.Level;
import java.util.logging.Logger;
import nro.models.boss.BossChungSinhBinhDang.Gogeta_SSJ_Csbd;
import nro.models.item.Item;
import nro.models.player.NPointClone;
import nro.models.player.Player;
import nro.services.InventoryService;
import nro.services.MapService;
import nro.services.PlayerService;
import nro.services.Service;
import nro.utils.Util;

public class ChungSinhBinhDangUseItem {

    private static ChungSinhBinhDangUseItem instance;

    public static ChungSinhBinhDangUseItem gI() {
        if (instance == null) {
            instance = new ChungSinhBinhDangUseItem();
        }
        return instance;
    }

    public void ChungSinhBinhDangUseItem(Player pl, Item item) {
        int mapid = (pl != null && pl.zone != null && pl.zone.map != null) ? pl.zone.map.mapId : -1;
        if (pl != null && pl.nPoint instanceof NPointClone n
                && MapService.gI().isMapChungSinhBinhDang(mapid)
                && ChungSinhBinhDang.gI().isDangChienVaThamGia(pl)) {
            switch (item.template.id) {
                case 1849: { // Đậu thần xanh - hồi 30% HP/MP
                    n.setHp(n.hp + (n.hpMax * 0.3));
                    n.setMp(n.mp + (n.mpMax * 0.3));
                    PlayerService.gI().sendInfoHpMp(pl);
                    Service.getInstance().sendInfoPlayerEatPea(pl);
                    InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                    Service.getInstance().sendThongBao(pl, "Bạn đã phục hồi 30% HP/MP!");
                    break;
                }
                case 1850: { // Đậu thần tím - hồi 60% HP/MP
                    n.setHp(n.hp + (n.hpMax * 0.6));
                    n.setMp(n.mp + (n.mpMax * 0.6));
                    PlayerService.gI().sendInfoHpMp(pl);
                    Service.getInstance().sendInfoPlayerEatPea(pl);
                    InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                    Service.getInstance().sendThongBao(pl, "Bạn đã phục hồi 60% HP/MP!");
                    break;
                }
                case 1851: { // Đậu thần đỏ - hồi 100% HP/MP
                    n.setHp(n.hpMax);
                    n.setMp(n.mpMax);
                    PlayerService.gI().sendInfoHpMp(pl);
                    Service.getInstance().sendInfoPlayerEatPea(pl);
                    InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                    Service.getInstance().sendThongBao(pl, "Bạn đã phục hồi 100% HP/MP!");
                    break;
                }
                case 1852: { // Thẻ tăng sức đánh level 1
                    double valueAdd = calPercent(ChungSinhBinhDangService.SUC_DANH, 100);
                    double maxBuff = ChungSinhBinhDangService.MAX_DAME_GOC - ChungSinhBinhDangService.SUC_DANH;
                    if (n.buffDame >= maxBuff) {
                        Service.getInstance().sendThongBaoOK(pl, "Đã đạt giới hạn Sức đánh gốc: " + Util.format(ChungSinhBinhDangService.MAX_DAME_GOC));
                    } else {
                        if (n.buffDame + valueAdd > maxBuff) {
                            n.buffDame = maxBuff;
                        } else {
                            n.buffDame += valueAdd;
                        }
                        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                        n.calPoint();
                        Service.getInstance().sendThongBao(pl, "Đã tăng thêm 100% sức đánh gốc!");
                    }
                    break;
                }
                case 1853: { // Thẻ tăng sức đánh level 2
                    double valueAdd = calPercent(ChungSinhBinhDangService.SUC_DANH, 300);
                    double maxBuff = ChungSinhBinhDangService.MAX_DAME_GOC - ChungSinhBinhDangService.SUC_DANH;
                    if (n.buffDame >= maxBuff) {
                        Service.getInstance().sendThongBaoOK(pl, "Đã đạt giới hạn Sức đánh gốc: " + Util.format(ChungSinhBinhDangService.MAX_DAME_GOC));
                    } else {
                        if (n.buffDame + valueAdd > maxBuff) {
                            n.buffDame = maxBuff;
                        } else {
                            n.buffDame += valueAdd;
                        }
                        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                        n.calPoint();
                        Service.getInstance().sendThongBao(pl, "Đã tăng thêm 300% sức đánh gốc!");
                    }
                    break;
                }
                case 1854: { // Thẻ tăng sức đánh level 3
                    double valueAdd = calPercent(ChungSinhBinhDangService.SUC_DANH, 500);
                    double maxBuff = ChungSinhBinhDangService.MAX_DAME_GOC - ChungSinhBinhDangService.SUC_DANH;
                    if (n.buffDame >= maxBuff) {
                        Service.getInstance().sendThongBaoOK(pl, "Đã đạt giới hạn Sức đánh gốc: " + Util.format(ChungSinhBinhDangService.MAX_DAME_GOC));
                    } else {
                        if (n.buffDame + valueAdd > maxBuff) {
                            n.buffDame = maxBuff;
                        } else {
                            n.buffDame += valueAdd;
                        }
                        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                        n.calPoint();
                        Service.getInstance().sendThongBao(pl, "Đã tăng thêm 500% sức đánh gốc!");
                    }
                    break;
                }
                case 1855: { // Thẻ tăng HP
                    double valueAdd = calPercent(ChungSinhBinhDangService.HP, 500);
                    double maxBuff = ChungSinhBinhDangService.MAX_HP_MP_GOC - ChungSinhBinhDangService.HP;
                    if (n.buffHp >= maxBuff) {
                        Service.getInstance().sendThongBaoOK(pl, "Đã đạt giới hạn HP gốc: " + Util.format(ChungSinhBinhDangService.MAX_HP_MP_GOC));
                    } else {
                        if (n.buffHp + valueAdd > maxBuff) {
                            n.buffHp = maxBuff;
                        } else {
                            n.buffHp += valueAdd;
                        }
                        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                        n.calPoint();
                        Service.getInstance().sendThongBao(pl, "Đã tăng thêm 500% HP gốc!");
                    }
                    break;
                }
                case 1856: { // Thẻ tăng KI
                    double valueAdd = calPercent(ChungSinhBinhDangService.KI, 500);
                    double maxBuff = ChungSinhBinhDangService.MAX_HP_MP_GOC - ChungSinhBinhDangService.KI;
                    if (n.buffMp >= maxBuff) {
                        Service.getInstance().sendThongBaoOK(pl, "Đã đạt giới hạn KI gốc: " + Util.format(ChungSinhBinhDangService.MAX_HP_MP_GOC));
                    } else {
                        if (n.buffMp + valueAdd > maxBuff) {
                            n.buffMp = maxBuff;
                        } else {
                            n.buffMp += valueAdd;
                        }
                        InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                        n.calPoint();
                        Service.getInstance().sendThongBao(pl, "Đã tăng thêm 500% KI gốc!");
                    }
                    break;
                }
                case 1857: { // Gọi boss
                    if (pl.zone != null && pl.zone.map != null) {
                        try {
                            Gogeta_SSJ_Csbd boss = new Gogeta_SSJ_Csbd(pl, pl.zone, (int) -pl.id - Util.randomBossId(), (short) pl.zone.map.mapId);
                            ChungSinhBinhDang.gI().bosses.add(boss);
                            InventoryService.gI().subQuantityItemsBag(pl, item, 1);
                        } catch (Exception ex) {
                            Logger.getLogger(Service.class.getName()).log(Level.SEVERE, null, ex);
                        }
                    } else {
                        Service.getInstance().sendThongBao(pl, "Không thể gọi boss khi chưa có vị trí hợp lệ!");
                    }
                    break;
                }
            }
            InventoryService.gI().sendItemBags(pl);
            Service.getInstance().point(pl);
        } else {
            Service.getInstance().sendThongBao(pl, "Không thể thực hiện khi không tham gia chúng sinh bình đẳng");
        }
    }

    public double calPercent(double param, double percent) {
        return param * percent / 100;
    }
}
