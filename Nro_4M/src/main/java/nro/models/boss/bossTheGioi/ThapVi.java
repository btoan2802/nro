package nro.models.boss.bossTheGioi;

import nro.consts.ConstItem;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.BossManager;
import nro.models.boss.FutureBoss;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.services.Service;
import nro.services.SkillService;
import nro.utils.Util;

public class ThapVi extends FutureBoss {

    public ThapVi() {
        super(BossFactory.THAP_VI, BossData.THAP_VI);
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
        if (Util.isTrue(10, 100)) {
            ItemMap itemroi = new ItemMap(this.zone, 1407, 1, //Hộp Đồ SKH Thánh Tôn
                    this.location.x, this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id);
            Service.getInstance().dropItemMap(this.zone, itemroi);
        } else if (Util.isTrue(5, 100)) {
            int sluong = Util.nextInt(1, 3);
            int a = 0;
            for (int i = 0; i < sluong; i++) {
                ItemMap itemroi = new ItemMap(this.zone, 1479, 1, //Hộp Sao pha lê Vip
                        this.location.x + a, this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id);
                Service.getInstance().dropItemMap(this.zone, itemroi);
                a += 5;
            }
        } else if (Util.isTrue(5, 100)) {
            int sluong = Util.nextInt(3, 5);
            int a = 0;
            for (int i = 0; i < sluong; i++) {
                ItemMap itemroi = new ItemMap(this.zone, 1791, 1, //Đá Vũ Trụ
                        this.location.x + a, this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id);
                Service.getInstance().dropItemMap(this.zone, itemroi);
                a += 5;
            }
        } else if (Util.isTrue(10, 100)) {
            int sluong = Util.nextInt(1, 20);
            int a = 0;
            for (int i = 0; i < sluong; i++) {
                ItemMap itemroi = new ItemMap(this.zone, 1400, 1, //Xu Sky
                        this.location.x + a, this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id);
                Service.getInstance().dropItemMap(this.zone, itemroi);
                a += 5;
            }
        } else if (Util.isTrue(5, 100)) {
            int sluong = Util.nextInt(1, 5);
            int a = 0;
            for (int i = 0; i < sluong; i++) {
                ItemMap itemroi = new ItemMap(this.zone, ConstItem.XU_DAC_BIET, 1,
                        this.location.x + a, this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id);
                Service.getInstance().dropItemMap(this.zone, itemroi);
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
        BossFactory.createBoss(BossFactory.THAP_VI).setJustRest();
        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }
}
