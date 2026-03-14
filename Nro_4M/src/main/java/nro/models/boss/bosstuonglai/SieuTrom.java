package nro.models.boss.bosstuonglai;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import nro.consts.ConstItem;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.BossManager;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.map.Zone;
import nro.models.player.Player;
import nro.server.Manager;
import nro.services.RewardService;
import nro.services.Service;
import nro.services.SkillService;
import nro.utils.Util;

/**
 * @author Hoàng Việt - 0857853150
 * @copyright 💖 GirlkuN 💖
 */
public class SieuTrom extends Boss {

    public SieuTrom() {
        super(BossFactory.SIEU_TROM, BossData.SIEU_TROM);
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
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        damage = 1;
        return super.injured(plAtt, damage, piercing, isMobAttack);
    }

    @Override
    public void rewards(Player pl) {
        int[] set2 = {1359, 1360, 1361, 1362, 1363, ConstItem.XU_DAC_BIET , 1400 , 457};
        ItemMap itemMap = new ItemMap(this.zone, set2[Util.nextInt(0, set2.length - 1)], 1,
                this.location.x - 10, this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id);
        Service.getInstance().dropItemMap(this.zone, itemMap);
        
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
        BossFactory.createBoss(BossFactory.SIEU_TROM).setJustRest();
        super.leaveMap();
        BossManager.gI().removeBoss(this);

    }
}
