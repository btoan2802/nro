package nro.models.boss.bossTheGioi;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import nro.consts.ConstItem;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.BossManager;
import nro.models.boss.FutureBoss;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.map.Zone;
import nro.models.player.Player;
import nro.server.Manager;
import nro.services.RewardService;
import nro.services.Service;
import nro.services.SkillService;
import nro.services.func.ChangeMapService;
import nro.utils.Util;

/**
 * @author Hoàng Việt - 0857853150
 * @copyright 💖 GirlkuN 💖
 */
public class TuVi extends FutureBoss {

    public TuVi() {
        super(BossFactory.TU_VI, BossData.TU_VI);
    }

    @Override
    protected boolean useSpecialSkill() {
        this.playerSkill.skillSelect = this.getSkillSpecial();
        if (SkillService.gI().canUseSkillWithCooldown(this)) {
            SkillService.gI().useSkill(this, null, null, null);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void rewards(Player pl) {
        if (Util.isTrue(1, 100)) {
            ItemMap hop = new ItemMap(this.zone, 1407, 1,
                    this.location.x, this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id);
            Service.getInstance().dropItemMap(this.zone, hop);
        } else if (Util.isTrue(1, 100)) {
            ItemMap ngocrongsc = new ItemMap(this.zone, ConstItem.NGOC_RONG_SIEU_CAP, 1,
                    this.location.x, this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id);
            Service.getInstance().dropItemMap(this.zone, ngocrongsc);
        } else if (Util.isTrue(5, 100)) {
            ItemMap hop = new ItemMap(this.zone, 1479, 2,
                    this.location.x, this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id);
            Service.getInstance().dropItemMap(this.zone, hop);
        } else if (Util.isTrue(10, 100)) {
            ItemMap hop = new ItemMap(this.zone, ConstItem.XU_DAC_BIET, 5,
                    this.location.x, this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id);
            Service.getInstance().dropItemMap(this.zone, hop);
        } else if (Util.isTrue(13, 100)) {
            int a = 0;
            for (int i = 0; i < 20; i++) {
                ItemMap datitan = new ItemMap(this.zone, 1400, 1,
                        this.location.x + a, this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id);
                Service.getInstance().dropItemMap(this.zone, datitan);
                a += 5;
            }
        } else if (Util.isTrue(17, 100)) {
            int a = 0;
            for (int i = 0; i < 5; i++) {
                ItemMap datitan = new ItemMap(this.zone, 1791, 1,
                        this.location.x + a, this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id);
                Service.getInstance().dropItemMap(this.zone, datitan);
                a += 5;
            }
        } else {
            int[] tv = {987};
            int[] dabv = {1787};
            int[] nrbang = {674};
            int[] manhTS = {1257, 1258, 1259, 859, 956};
            int[][] random = {tv, dabv, nrbang, manhTS};
            int[] vatPham = random[Util.nextInt(0, random.length - 1)];
            int sl_1_vien;
            int so_vien;
            if (vatPham == tv) {
                sl_1_vien = 1;
                so_vien = 3;
            } else if (vatPham == dabv) {
                sl_1_vien = 1;
                so_vien = 1;
            } else if (vatPham == nrbang) {
                sl_1_vien = 1;
                so_vien = 5;
            } else {
                sl_1_vien = 5;
                so_vien = 10;
            }
            int a = 0;
            for (int i = 0; i < so_vien; i++) {
                ItemMap datitan = new ItemMap(this.zone, vatPham[Util.nextInt(0, vatPham.length - 1)], sl_1_vien,
                        this.location.x + a, this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id);
                Service.getInstance().dropItemMap(this.zone, datitan);
                a += 5;
            }
        }
        if (pl != null) {
            pl.top_sanboss += 10;
        }
        generalRewards(pl);
    }

    @Override
    public void idle() {

    }

    @Override
    public void checkPlayerDie(Player pl) {

    }

    @Override
    public void initTalk() {

    }

    @Override
    public void leaveMap() {
//        if (this.zone == null) {
//            this.zone = getMapCanJoin(mapJoin[Util.nextInt(0, mapJoin.length - 1)]);
//        }
//        if (this.zone != null) {
        BossFactory.createBoss(BossFactory.TU_VI).setJustRest();
//            ChangeMapService.gI().changeMapBySpaceShip(this, this.zone, ChangeMapService.TENNIS_SPACE_SHIP);
//        }
        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }
}
