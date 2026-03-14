package nro.models.boss;

import nro.utils.Log;
import java.util.ArrayList;
import java.util.List;
import nro.models.boss.BossChungSinhBinhDang.Gogeta_SSJ_Csbd;
import nro.models.boss.BossTuTien.*;
import nro.models.boss.ThanBlue.*;
import nro.models.boss.ThanPink.*;
import nro.models.boss.ThanRed.*;
import nro.models.boss.bill.Boss_Leo_Thap;
import nro.models.boss.bossTheGioi.*;
import nro.models.boss.boss_ban_do_kho_bau.BossBanDoKhoBau;
import nro.models.boss.boss_doanh_trai.BossDoanhTrai;
import nro.models.boss.cdrd.CBoss;
import nro.models.boss.dhvt.BossDHVT;
import nro.models.boss.event.Qilin;
import nro.models.boss.event.SantaClaus;
import nro.models.boss.mabu_war.BossMabuWar;
import nro.models.item.Item;
import nro.models.map.Zone;
import nro.models.map.mabu.MabuWar;
import nro.models.player.Player;
import nro.server.io.Message;
import nro.services.InventoryService;
import nro.services.MapService;
import nro.services.Service;
import nro.services.func.ChangeMapService;
import nro.utils.Util;

/**
 *
 * @author Hoàng Việt - 0857853150
 * @copyright 💖 GirlkuN 💖
 *
 */
public class BossManager {

    public static final List<Boss> BOSSES_IN_GAME;
    private static BossManager intance;

    static {
        BOSSES_IN_GAME = new ArrayList<>();
    }

    public void updateAllBoss() {
        for (int i = BOSSES_IN_GAME.size() - 1; i >= 0; i--) {
            try {
                Boss boss = BOSSES_IN_GAME.get(i);
                if (boss != null) {
                    boss.update();
                }
            } catch (Exception e) {
                Log.error(BossManager.class, e);
            }
        }

    }

    private BossManager() {

    }

    public static BossManager gI() {
        if (intance == null) {
            intance = new BossManager();
        }
        return intance;
    }

    public Boss getBossById(int bossId) {
        for (int i = BOSSES_IN_GAME.size() - 1; i >= 0; i--) {
            if (BOSSES_IN_GAME.get(i).id == bossId) {
                return BOSSES_IN_GAME.get(i);
            }
        }
        return null;
    }

    public Boss getBossByIdRandom(int bossId) {
        for (int i = BOSSES_IN_GAME.size() - 1; i >= 0; i--) {
            if (BOSSES_IN_GAME.get(i).id == bossId) {
                return BOSSES_IN_GAME.get(i);
            }
        }
        return null;
    }

    public long getBossidlist(List<Boss> BOSSES_IN_GAME) {
        for (int i = BOSSES_IN_GAME.size() - 1; i >= 0; i--) {
            return BOSSES_IN_GAME.get(i).id;
        }
        return -1;
    }

    public void addBoss(Boss boss) {
        boolean have = false;
        for (Boss b : BOSSES_IN_GAME) {
            if (boss.equals(b)) {
                have = true;
                break;
            }
        }
        if (!have) {
            BOSSES_IN_GAME.add(boss);
        }
    }

    public List<Boss> getBosses() {
        return BossManager.BOSSES_IN_GAME;
    }

    public void removeBoss(Boss boss) {
        BOSSES_IN_GAME.remove(boss);
        boss.dispose();
    }

    public void showListBoss(Player player) {
        Message msg = new Message(-96);
        try {
            msg.writer().writeByte(0);
            msg.writer().writeUTF("List BOSS");
            int count = (int) BOSSES_IN_GAME.stream()
                    .filter(boss -> !(boss instanceof CBoss) && !(boss instanceof BossMabuWar) && !(boss instanceof Boss_Leo_Thap)
                    && !(boss instanceof BossDHVT) && !(boss instanceof SantaClaus)
                    && !(boss instanceof BossDoanhTrai) && !(boss instanceof BossBanDoKhoBau))
                    .count();
            msg.writer().writeByte(count > 120 ? 120 : count);
            for (int i = 0; i < BOSSES_IN_GAME.size(); i++) {
                Boss boss = BOSSES_IN_GAME.get(i);
                if ((boss instanceof CBoss) || (boss instanceof BossMabuWar)
                        || (boss instanceof BossDHVT) || (boss instanceof SantaClaus) || (boss instanceof BossDoanhTrai) || (boss instanceof Boss_Leo_Thap)
                        || (boss instanceof BossBanDoKhoBau)) {
                    continue;
                }
                msg.writer().writeInt((int) boss.id);
                msg.writer().writeInt((int) boss.id);
                msg.writer().writeShort(boss.data.outfit[0]);
                if (player.isVersionAbove(220)) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(boss.data.outfit[1]);
                msg.writer().writeShort(boss.data.outfit[2]);
                msg.writer().writeUTF(boss.data.name);
                msg.writer().writeUTF(boss.zone != null ? "Sống" : "Chết rồi");
                if (boss.zone != null) {
                    msg.writer().writeUTF("Map xuất hiện: " + boss.zone.map.mapName + " khu " + boss.zone.zoneId
                            + "\nMáu: " + Util.powerToStringnew(boss.nPoint.hp));
                } else {
                    msg.writer().writeUTF("Chưa xuất hiện: Chờ đi");
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showListBossMember(Player player) {
        Message msg = new Message(-96);
        try {
            msg.writer().writeByte(0);
            msg.writer().writeUTF("List BOSS"); // chặn boss vip trên máy dò thường
            int count = (int) BOSSES_IN_GAME.stream()
                    .filter(boss -> !(boss instanceof Red1)
                    && !(boss instanceof CBoss) && !(boss instanceof BossMabuWar)
                    && !(boss instanceof BossDHVT) && !(boss instanceof SantaClaus)
                    && !(boss instanceof BossDoanhTrai) && !(boss instanceof BossBanDoKhoBau)
                    && !(boss instanceof Gogeta_SSJ_Csbd) && !(boss instanceof Boss_Leo_Thap)
                    && !(boss instanceof Blue1) && !(boss instanceof Blue2) && !(boss instanceof Blue3)
                    && !(boss instanceof Blue4) && !(boss instanceof Blue5)
                    && !(boss instanceof TuTien1) && !(boss instanceof TuTien2) && !(boss instanceof TuTien3)
                    && !(boss instanceof Red1) && !(boss instanceof Red2) && !(boss instanceof Red3)
                    && !(boss instanceof Red4) && !(boss instanceof Red5)
                    && !(boss instanceof Pink1) && !(boss instanceof Pink2) && !(boss instanceof Pink3)
                    && !(boss instanceof Pink4) && !(boss instanceof Pink5)
                    )
                    .count();
            msg.writer().writeByte(count > 120 ? 120 : count);
            for (int i = 0; i < BOSSES_IN_GAME.size(); i++) {
                Boss boss = BOSSES_IN_GAME.get(i);
                if ((boss instanceof CBoss) || (boss instanceof BossMabuWar)
                        || (boss instanceof BossDHVT) || (boss instanceof SantaClaus)
                        || (boss instanceof BossDoanhTrai) || (boss instanceof BossBanDoKhoBau)
                        || (boss instanceof Gogeta_SSJ_Csbd) || (boss instanceof Boss_Leo_Thap)
                        || (boss instanceof Blue1) || (boss instanceof Blue2) || (boss instanceof Blue3)
                        || (boss instanceof Blue4) || (boss instanceof Blue5)
                        || (boss instanceof TuTien1) || (boss instanceof TuTien2) || (boss instanceof TuTien3)
                        || (boss instanceof Red1) || (boss instanceof Red2) || (boss instanceof Red3)
                        || (boss instanceof Red4) || (boss instanceof Red5)
                        || (boss instanceof Pink1) || (boss instanceof Pink2) || (boss instanceof Pink3)
                        || (boss instanceof Pink4) || (boss instanceof Pink5)) {
                    continue;
                }
                msg.writer().writeInt((int) boss.id);
                msg.writer().writeInt((int) boss.id);
                msg.writer().writeShort(boss.data.outfit[0]);
                if (player.isVersionAbove(220)) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(boss.data.outfit[1]);
                msg.writer().writeShort(boss.data.outfit[2]);
                msg.writer().writeUTF(boss.data.name);
                msg.writer().writeUTF(boss.zone != null ? "Sống" : "Chết rồi");
                if (boss.zone != null) {
                    msg.writer().writeUTF("Máu: " + Util.powerToStringnew(boss.nPoint.hp) + "\nDịch chuyển tới và pem nào!");
                } else {
                    msg.writer().writeUTF("Chưa xuất hiện: Chờ đi");
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showListBossMemberVIP(Player player) {
        Message msg = new Message(-96);
        try {
            msg.writer().writeByte(0);
            msg.writer().writeUTF("List BOSS");
            int count;
            count = (int) BOSSES_IN_GAME.stream() // thêm boss vào mấy dò VIP
                    .filter(boss
                            -> (boss instanceof Blue1) || (boss instanceof Blue2) || (boss instanceof Blue3)
                    || (boss instanceof Blue4) || (boss instanceof Blue5) || (boss instanceof Blue5)
                    || (boss instanceof TuTien1) || (boss instanceof TuTien2) || (boss instanceof TuTien3)
                    || (boss instanceof Red1) || (boss instanceof Red2) || (boss instanceof Red3)
                    || (boss instanceof Red4) || (boss instanceof Red5)
                    || (boss instanceof Pink1) || (boss instanceof Pink2) || (boss instanceof Pink3)
                    || (boss instanceof Pink4) || (boss instanceof Pink5)
                    )
                    .count();
            msg.writer().writeByte(count > 120 ? 120 : count);
            for (int i = 0; i < BOSSES_IN_GAME.size(); i++) {
                Boss boss = BOSSES_IN_GAME.get(i);
                if (!(boss instanceof Blue1) && !(boss instanceof Blue2) && !(boss instanceof Blue3)
                        && !(boss instanceof Blue4) && !(boss instanceof Blue5)
                        && !(boss instanceof TuTien1) && !(boss instanceof TuTien2) && !(boss instanceof TuTien3)
                        && !(boss instanceof Red1) && !(boss instanceof Red2) && !(boss instanceof Red3)
                        && !(boss instanceof Red4) && !(boss instanceof Red5)
                        && !(boss instanceof Pink1) && !(boss instanceof Pink2) && !(boss instanceof Pink3)
                        && !(boss instanceof Pink4) && !(boss instanceof Pink5)) {
                    continue;
                }
                msg.writer().writeInt((int) boss.id);
                msg.writer().writeInt((int) boss.id);
                msg.writer().writeShort(boss.data.outfit[0]);
                if (player.isVersionAbove(220)) {
                    msg.writer().writeShort(-1);
                }
                msg.writer().writeShort(boss.data.outfit[1]);
                msg.writer().writeShort(boss.data.outfit[2]);
                msg.writer().writeUTF(boss.data.name);
                msg.writer().writeUTF(boss.zone != null ? "Sống" : "Chết rồi");
                if (boss.zone != null) {
                    msg.writer().writeUTF("Máu: " + Util.powerToStringnew(boss.nPoint.hp) + "\nDịch chuyển tới và pem nào!");
                } else {
                    msg.writer().writeUTF("Chưa xuất hiện: Chờ đi");
                }
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void FindBoss(Player player, int id) {
        Boss boss = BossManager.gI().getBossById(id);
        if (boss == null || boss.isDie()) {
            Service.getInstance().sendThongBao(player, "Boss không tồn tại hoặc đã chết.");
            return;
        }
        Zone z = null;
        if (boss.zone != null && boss.zone.map != null) {
            z = MapService.gI().getMapCanJoin(player, boss.zone.map.mapId);
        }
        if (z == null || z.getNumOfPlayers() >= z.maxPlayer) {
            if (boss.zone != null && boss.zone.map != null) {
                Service.getInstance().sendThongBao(player, "Khu vực đang full.");
                Service.getInstance().sendThongBaoOK(player, boss.zone.map.mapName + "(" + boss.zone.map.mapId + ") khu " + boss.zone.zoneId + "");
            }
            return;
        }
        if (player.isAdmin()) {
            ChangeMapService.gI().changeMapYardrat(player, boss.zone, boss.location.x, boss.location.y);
            Service.getInstance().sendMoney(player);
        } else {
            Item maydo = InventoryService.gI().findItemBagByTemp(player, 1296);
            if (maydo == null) {
                Service.getInstance().sendThongBao(player, "Yêu cầu có Máy Dò Boss");
                return;
            }
            InventoryService.gI().subQuantityItemsBag(player, maydo, 1);
            InventoryService.gI().sendItemBags(player);
            ChangeMapService.gI().changeMapYardrat(player, boss.zone, boss.location.x, boss.location.y);
            Service.getInstance().sendMoney(player);
        }
    }

}
