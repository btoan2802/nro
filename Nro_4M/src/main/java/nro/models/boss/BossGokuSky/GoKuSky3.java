package nro.models.boss.BossGokuSky;

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

public class GoKuSky3 extends FutureBoss {

    public GoKuSky3() {
        super(BossFactory.GOKU_SKY_3, BossData.GOKU_SKY_3);
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
        if (Util.isTrue(5, 100)) {
            int sluong = Util.nextInt(1, 2);
            int a = 0;
            for (int i = 0; i < sluong; i++) {
                ItemMap itemroi = new ItemMap(this.zone, 1769, 1, //Rương Sao pha lê Đặc biệt
                        this.location.x + a, this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id);
                Service.getInstance().dropItemMap(this.zone, itemroi);
                a += 5;
            }
        } else if (Util.isTrue(5, 100)) {
            int sluong = Util.nextInt(1, 10);
            int a = 0;
            for (int i = 0; i < sluong; i++) {
                ItemMap itemroi = new ItemMap(this.zone, 1791, 1, //Đá Vũ Trụ
                        this.location.x + a, this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id);
                Service.getInstance().dropItemMap(this.zone, itemroi);
                a += 5;
            }
        } else if (Util.isTrue(10, 100)) {
            int sluong = Util.nextInt(1, 30);
            int a = 0;
            for (int i = 0; i < sluong; i++) {
                ItemMap itemroi = new ItemMap(this.zone, 1400, 1, //Xu Sky
                        this.location.x + a, this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id);
                Service.getInstance().dropItemMap(this.zone, itemroi);
                a += 5;
            }
        } else if (Util.isTrue(5, 100)) {
            int sluong = Util.nextInt(1, 10);
            int a = 0;
            for (int i = 0; i < sluong; i++) {
                ItemMap itemroi = new ItemMap(this.zone, ConstItem.XU_DAC_BIET, 1,
                        this.location.x + a, this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id);
                Service.getInstance().dropItemMap(this.zone, itemroi);
                a += 5;
            }
        } 
        else if (Util.isTrue(5, 100)) {
    ItemMap itemMap1155 = new ItemMap(this.zone, 1155, 1,
            this.location.x, this.location.y, pl.id);
    Service.getInstance().dropItemMap(this.zone, itemMap1155);
} else {
            int[] set1 = {1156, 1157, 1158, 1159, 1160, 1161};
            ItemMap itemMap4 = new ItemMap(this.zone, set1[Util.nextInt(0, set1.length - 1)], 1, this.location.x, this.location.y, pl.id);
            Service.getInstance().dropItemMap(this.zone, itemMap4);
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
        BossFactory.createBoss(BossFactory.GOKU_SKY_3).setJustRest();
        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }
}
