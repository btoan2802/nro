package nro.models.boss.cell;

import nro.consts.ConstRatio;
import nro.consts.ConstTask;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.FutureBoss;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.services.Service;
import nro.services.SkillService;
import nro.services.TaskService;
import nro.utils.Log;
import nro.utils.SkillUtil;
import nro.utils.Util;

/**
 * @author Hoàng Việt - 0857853150
 */
public class XenMax extends FutureBoss {

    public XenMax() {
        super(BossFactory.XEN_MAX, BossData.XEN_MAX);
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
    public void attack() {
        try {
            Player pl = getPlayerAttack();
            if (pl != null) {
                this.playerSkill.skillSelect = this.getSkillAttack();
                if (Util.getDistance(this, pl) <= this.getRangeCanAttackWithSkillSelect()) {
                    if (Util.isTrue(15, ConstRatio.PER100) && SkillUtil.isUseSkillChuong(this)) {
                        goToXY(pl.location.x + (Util.getOne(-1, 1) * Util.nextInt(20, 80)),
                                Util.nextInt(10) % 2 == 0 ? pl.location.y : pl.location.y - Util.nextInt(0, 50), false);
                    }
                    SkillService.gI().useSkill(this, pl, null, null);
                    checkPlayerDie(pl);
                } else {
                    goToPlayer(pl, false);
                }
            }
        } catch (Exception ex) {
            Log.error(XenMax.class, ex);
        }
    }

    @Override
    public void idle() {
    }

    @Override
    public void rewards(Player pl) {
        if (Util.isTrue(10, 100)) {
            ItemMap hop = new ItemMap(this.zone, 1547, 1,
                    this.location.x, this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id);
            Service.getInstance().dropItemMap(this.zone, hop);
        } else if (Util.isTrue(25, 100)) {
            ItemMap hop = new ItemMap(this.zone, 1479, 2,
                    this.location.x, this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id);
            Service.getInstance().dropItemMap(this.zone, hop);
        } else if (Util.isTrue(17, 100)) {
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
        //nhiệm vụ siêu thần thủy
        if (TaskService.gI().getIdTask(pl) == ConstTask.TASK_31_3) {
            if (Util.isTrue(10, 100)) {
                ItemMap sieuThanThuy = new ItemMap(this.zone, 725, 1,
                        this.location.x, this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id);
                Service.getInstance().dropItemMap(this.zone, sieuThanThuy);
            }
        }
    }

    @Override
    public void checkPlayerDie(Player pl) {

    }

    @Override
    public void initTalk() {
        this.textTalkBefore = new String[]{};
        this.textTalkMidle = new String[]{"Kame Kame Haaaaa!!", "Mi khá đấy nhưng so với ta chỉ là hạng tôm tép",
            "Tất cả nhào vô hết đi", "Cứ chưởng tiếp đi. haha", "Các ngươi yếu thế này sao hạ được ta đây. haha",
            "Khi công pháo!!", "Cho mi biết sự lợi hại của ta"};
        this.textTalkAfter = new String[]{"Các ngươi được lắm", "Hãy đợi đấy thời gian tới ta sẽ quay lại.."};
    }
}
