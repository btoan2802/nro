package nro.models.boss.BossTuTien;

import nro.consts.ConstRatio;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.services.Service;
import nro.utils.Util;
import nro.models.boss.BossManager;
import nro.services.SkillService;
import nro.utils.SkillUtil;

/**
 * @author Hoàng Việt - 0857853150
 * @copyright 💖 GirlkuN 💖
 */
public class TuTien3 extends Boss {

    public TuTien3() {
        super(BossFactory.TU_TIEN_3, BossData.TU_TIEN_3);
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

        // nếu boss chưa có target thì lấy player đầu trong map
        if (pl == null && this.zone != null && !this.zone.getPlayers().isEmpty()) {
            pl = this.zone.getPlayers().get(0);
        }

        if (pl == null) {
            return;
        }

        if (!useSpecialSkill()) {

            this.playerSkill.skillSelect = this.getSkillAttack();

            int distance = Util.getDistance(this, pl);

            // nếu ở xa thì di chuyển lại gần
            if (distance > 250) {
                goToPlayer(pl, false);
                return;
            }

            // nếu đủ tầm thì đánh
            if (Util.isTrue(15, ConstRatio.PER100) && SkillUtil.isUseSkillChuong(this)) {
                goToXY(pl.location.x + (Util.getOne(-1, 1) * Util.nextInt(20, 80)),
                        Util.nextInt(10) % 2 == 0 ? pl.location.y : pl.location.y - Util.nextInt(0, 50), false);
            }

            SkillService.gI().useSkill(this, pl, null, null);
            checkPlayerDie(pl);
        }

    } catch (Exception e) {
        e.printStackTrace();
    }
}

    @Override
    public void rewards(Player pl) {
        if (Util.isTrue(10, 100)) {
            ItemMap itemMap = new ItemMap(this.zone, 1922, 30, this.location.x, this.location.y, pl.id);
            Service.getInstance().dropItemMap(this.zone, itemMap);
        } else {
            int a = 0;
            int[] set1 = {1919, 1920, 1921};
            for (int i = 0; i < 3; i++) {
                ItemMap itemMap1 = new ItemMap(this.zone, set1[Util.nextInt(0, set1.length - 1)], 30, this.location.x + a, this.location.y, pl.id);
                Service.getInstance().dropItemMap(this.zone, itemMap1);
                a += 5;
            }
        }
    }

    @Override
    public void idle() {

    }

    @Override
    public void checkPlayerDie(Player pl) {

    }

    @Override
    public void initTalk() {
        this.textTalkMidle = new String[]{"Oải rồi Hả ku?", "Ê cố lên nhóc",
            "Chán", "Lũ Rác Rưỡi"};
    }

    @Override
    public void leaveMap() {
        BossFactory.createBoss(BossFactory.TU_TIEN_3).setJustRest();
        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }

}
