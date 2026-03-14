package nro.models.map.challenge.sieuhang;

import nro.jdbc.daos.GodGK;
import nro.models.boss.BossData;
import nro.models.map.Zone;
import nro.models.player.Player;
import nro.models.skill.Skill;
import nro.server.io.Message;
import nro.services.MapService;
import nro.services.Service;
import nro.services.func.ChangeMapService;
import nro.utils.Util;

import java.util.ArrayList;
import java.util.List;

import nro.models.boss.Boss;

/**
 * @author Béo Mập :3
 */
public class SieuHangService {

    private static SieuHangService i;

    public static SieuHangService gI() {
        if (i == null) {
            i = new SieuHangService();
        }
        return i;
    }

    public boolean checkDangThiDau(Player player1, Player player2) {
        for (SieuHang sieuHang : SieuHangManager.gI().list) {
            if (player1.name.equals(sieuHang.getPlayer().name) || player1.name.equals(sieuHang.getBoss().name) || player2.name.equals(sieuHang.getPlayer().name) || player2.name.equals(sieuHang.getBoss().name)) {
                return true;
            }
        }
        return false;
    }

    public void startChallenge(Player player, int id) throws Exception {
        if (player.zone.map.mapId != 113) {
            return;
        }
        Player pl = GodGK.loadPlayerbyId(id);
        if (pl != null) {
            pl.nPoint.calPoint();
            if (player.rankSieuHang > 11 && pl.rankSieuHang <= 10) {
                Service.getInstance().sendThongBao(player, "Chưa thể khiêu chiến đối thử trong top 10");
                return;
            }
            if (player.rankSieuHang < 2 && pl.rankSieuHang <= 10) {
                Service.getInstance().sendThongBao(player, "Không thể khiêu chiến đối thủ quá 2 hạng");
                return;
            }
            if (checkDangThiDau(player, pl)) {
                Service.getInstance().sendThongBao(player, "Đối thủ đang thi đấu");
                return;
            }

            List<Skill> skillList = new ArrayList<>();
            for (byte i = 0; i < pl.playerSkill.skills.size(); i++) {
                Skill skill = pl.playerSkill.skills.get(i);
                if (skill.point > 0
                        && skill.template.id != Skill.TU_SAT
                        && skill.template.id != Skill.MAKANKOSAPPO
                        && skill.template.id != Skill.QUA_CAU_KENH_KHI
                        && skill.template.id != Skill.LIEN_HOAN_CHUONG
                        && skill.template.id != Skill.SUPER_KAME
                        && skill.template.id != Skill.MA_PHONG_BA
                        && skill.template.id != Skill.BIEN_HINH
                        && skill.template.id != Skill.PHAN_THAN) {
                    skillList.add(skill);
                }
            }
            int[][] skillTemp = new int[skillList.size()][5];
            for (byte i = 0; i < skillList.size(); i++) {
                Skill skill = skillList.get(i);
                if (skill.point > 0) {
                    skillTemp[i][0] = skill.template.id;
                    skillTemp[i][1] = skill.point;
                    skillTemp[i][2] = skill.coolDown;
                }
            }

            BossData data = new BossData(
                    pl.name,
                    pl.gender,
                    Boss.DAME_NORMAL, //type dame
                    Boss.HP_NORMAL, //type hp
                    Util.DoubleGioihan(pl.nPoint.dame),
                    new double[][]{{pl.nPoint.hpMax}},
                    new short[]{pl.getHead(), pl.getBody(), pl.getLeg()},
                    new short[]{113},
                    skillTemp,
                    5
            );

            ClonePlayer boss = new ClonePlayer(player, data, (int) pl.id);
            boss.rankSieuHang = pl.rankSieuHang;
            Zone zone = getMapChalllenge(113);
            if (zone != null) {
                ChangeMapService.gI().changeMap(player, zone, player.location.x, 360);
                Util.setTimeout(() -> {
                    SieuHang mc = new SieuHang();
                    mc.setPlayer(player);
                    mc.toTheNextRound(boss);
                    SieuHangManager.gI().add(mc);
                }, 500);
            }
        }
    }

    public void moveFast(Player pl, int x, int y) {
        Message msg;
        try {
            msg = new Message(58);
            msg.writer().writeInt((int) pl.id);
            msg.writer().writeShort(x);
            msg.writer().writeShort(y);
            msg.writer().writeInt((int) pl.id);
            pl.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendTypePK(Player player, Player boss) {
        Message msg;
        try {
            msg = Service.getInstance().messageSubCommand((byte) 35);
            msg.writer().writeInt((int) boss.id);
            msg.writer().writeByte(3);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public Zone getMapChalllenge(int mapId) {
        Zone map = MapService.gI().getMapWithRandZone(mapId);
        if (map.getNumOfBosses() < 1) {
            return map;
        }
        return null;
    }
}
