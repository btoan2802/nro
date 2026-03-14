package nro.models.boss.BossThanLinh;

import nro.consts.ConstRatio;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.services.RewardService;
import nro.services.Service;
import nro.utils.Util;

import nro.models.boss.BossManager;
import nro.services.SkillService;
import nro.utils.SkillUtil;

/**
 * @author Hoàng Việt - 0857853150
 * @copyright 💖 GirlkuN 💖
 */
public class ThanLinh3 extends Boss {

    public ThanLinh3() {
        super(BossFactory.THAN_LINH3, BossData.THAN_LINH3);
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
                if (!useSpecialSkill()) {
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
            }
        } catch (Exception ex) {
        }
    }

    @Override
    public void rewards(Player pl) {
        ItemMap itemMap = null;
        if (Util.isTrue(5, 100)) {
            int[] set1 = {562, 564, 566, 561,555, 556, 563, 557, 558, 565, 559, 567, 560};
            itemMap = new ItemMap(this.zone, set1[Util.nextInt(0, set1.length - 1)], 1, this.location.x, this.location.y, pl.id);
            RewardService.gI().initBaseOptionClothes(itemMap.itemTemplate.id, itemMap.itemTemplate.type, itemMap.options);
            Service.getInstance().dropItemMap(this.zone, itemMap);
        } else if (Util.isTrue(10, 100)) {
            int sluong = Util.nextInt(1, 5);
            int a = 0;
            for (int i = 0; i < sluong; i++) {
                itemMap = new ItemMap(this.zone, 1400, 1, this.location.x + a, this.location.y, pl.id);
                Service.getInstance().dropItemMap(this.zone, itemMap);
                a += 5;
            }
        } else if (Util.isTrue(20, 100)) {
            int sluong = Util.nextInt(1, 10);
            int a = 0;
            for (int i = 0; i < sluong; i++) {
                itemMap = new ItemMap(this.zone, 861, 2000, this.location.x + a, this.location.y, pl.id);
                Service.getInstance().dropItemMap(this.zone, itemMap);
                a += 5;
            }
        } else {
            int[] set1 = {14, 15, 16, 17, 18, 19, 20};
            itemMap = new ItemMap(this.zone, set1[Util.nextInt(0, set1.length - 1)], 1, this.location.x, this.location.y, pl.id);
            Service.getInstance().dropItemMap(this.zone, itemMap);
        }
        if (pl != null) {
            pl.top_sanboss += 2;
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
        this.textTalkMidle = new String[]{"Oải rồi hả?", "Ê cố lên nhóc",
            "Chán", "Ta có nhầm không nhỉ"};

    }

    @Override
    public void leaveMap() {
        BossFactory.createBoss(BossFactory.THAN_LINH3).setJustRest();
        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }

}
