package nro.models.boss.broly;

import nro.consts.ConstRatio;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.services.Service;
import nro.services.SkillService;
import nro.utils.Log;
import nro.utils.SkillUtil;
import nro.utils.Util;

/**
 *
 * @author Hoàng Việt - 0857853150
 * @copyright 💖 GirlkuN 💖
 *
 */
public class BrolyDetu extends Broly {

    public BrolyDetu() {
        super(BossFactory.BROLY_DETU - Util.randomBossId(), BossData.BROLY_DETU);
        this.nPoint.defg = (short) (this.nPoint.hpg / 1000);
        if (this.nPoint.defg < 0) {
            this.nPoint.defg = (short) -this.nPoint.defg;
        }
    }

    public BrolyDetu(byte id, BossData data) {
        super(id, data);
        this.nPoint.defg = (short) (this.nPoint.hpg / 1000);
        if (this.nPoint.defg < 0) {
            this.nPoint.defg = (short) -this.nPoint.defg;
        }
    }

    @Override
    public void attack() {
        try {
            if (!charge()) {
                Player pl = getPlayerAttack();
                if (pl != null) {
                    this.playerSkill.skillSelect = this.getSkillAttack();
                    if (Util.getDistance(this, pl) <= this.getRangeCanAttackWithSkillSelect()) {
                        if (Util.isTrue(15, ConstRatio.PER100) && SkillUtil.isUseSkillChuong(this)) {
                            goToXY(pl.location.x + (Util.getOne(-1, 1) * Util.nextInt(20, 80)),
                                    Util.nextInt(10) % 2 == 0 ? pl.location.y : pl.location.y - Util.nextInt(0, 50), false);
                        }
                        try {
                            SkillService.gI().useSkill(this, pl, null, null);
                        } catch (Exception e) {
                            Log.error(SuperBroly.class, e);
                        }
                    } else {
                        goToPlayer(pl, false);
                    }
                    if (Util.isTrue(5, ConstRatio.PER100)) {
                        this.changeIdle();
                    }
                }
            }
        } catch (Exception ex) {
            Log.error(SuperBroly.class, ex);
        }
    }

    @Override
    public Player getPlayerAttack() throws Exception {
        if (countChangePlayerAttack < targetCountChangePlayerAttack
                && plAttack != null && plAttack.zone != null && plAttack.zone.equals(this.zone)
                && !plAttack.effectSkin.isVoHinh) {
            if (!plAttack.isDie()) {
                this.countChangePlayerAttack++;
                return plAttack;
            } else {
                plAttack = null;
            }
        } else {
            this.targetCountChangePlayerAttack = Util.nextInt(10, 20);
            this.countChangePlayerAttack = 0;
            plAttack = this.zone.getRandomPlayerInMap();
        }
        return plAttack;
    }

    @Override
    public void leaveMap() {
        super.leaveMap();
    }

    @Override
    public void die() {
        this.secondTimeRestToNextTimeAppear = 900; //15p
        super.die();
    }

    @Override
    public void rewards(Player pl) {
        if (Util.isTrue(1, 100)) {
            ItemMap hop = new ItemMap(this.zone, 1547, 1,
                    this.location.x, this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id);
            Service.getInstance().dropItemMap(this.zone, hop);
        } else if (Util.isTrue(10, 100)) {
            ItemMap hop = new ItemMap(this.zone, 1479, 2,
                    this.location.x, this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id);
            Service.getInstance().dropItemMap(this.zone, hop);
        }  else if (Util.isTrue(17, 100)) {
            int a = 0;
            for (int i = 0; i < 10; i++) {
                ItemMap datitan = new ItemMap(this.zone, 1400, 1,
                        this.location.x + a, this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id);
                Service.getInstance().dropItemMap(this.zone, datitan);
                a += 5;
            }
        } else {
            int[] tv = {861}; // hồng ngọc sl 30k
            int[] dabv = {987}; // đá bảo vệ sl 3
            int[] nrbang = {925, 926, 927, 928, 929, 930, 931}; // ngọc rồng băng sl 1
            int[] manhTS = {1066, 1067, 1068, 1069, 1070}; // mảnh thiên sứ 60 mảnh
            int[][] random = {tv, dabv, nrbang, manhTS};
            int[] vatPham = random[Util.nextInt(0, random.length - 1)];
            int sl_1_vien;
            int so_vien;
            if (vatPham == tv) {
                sl_1_vien = 10000;
                so_vien = 3;
            } else if (vatPham == dabv) {
                sl_1_vien = 1;
                so_vien = 3;
            } else if (vatPham == nrbang) {
                sl_1_vien = 1;
                so_vien = 1;
            } else {
                sl_1_vien = 10;
                so_vien = 5;
            }
            int a = 0;
            for (int i = 0; i < so_vien; i++) {
                ItemMap datitan = new ItemMap(this.zone, vatPham[Util.nextInt(0, vatPham.length - 1)], sl_1_vien,
                        this.location.x + a, this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id);
                Service.getInstance().dropItemMap(this.zone, datitan);
                a += 5;
            }
        }
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

}
