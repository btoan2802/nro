package nro.models.boss.BossTet;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import nro.consts.ConstEvent;
import nro.consts.ConstItem;
import nro.consts.ConstRatio;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.server.Manager;
import nro.services.RewardService;
import nro.services.Service;
import nro.utils.Util;

import nro.models.boss.BossManager;
import nro.models.map.Zone;
import nro.models.skill.Skill;
import nro.services.SkillService;
import nro.services.TaskService;
import nro.utils.SkillUtil;

/**
 * @author Hoàng Việt - 0857853150
 * @copyright 💖 GirlkuN 💖
 */
public class VuaHung extends Boss {

    public VuaHung() {
        super(BossFactory.VUA_HUNG, BossData.VUA_HUNG);
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
        if (plAtt != null) {
            int skill = plAtt.playerSkill.skillSelect.template.id;
            if (skill == Skill.SUPER_KAME || skill == Skill.LIEN_HOAN_CHUONG || skill == Skill.MA_PHONG_BA) {
                damage = 1;
                Service.getInstance().chat(plAtt, "Trời ơi, chưởng hoàn toàn vô hiệu lực với hắn..");
            }
        }
        if (plAtt != null && !plAtt.isPl()) {
            damage = 1;
        }
        if (damage >= 10_000_000) {
            damage = 10_000_000;
        }
        return super.injured(plAtt, damage, piercing, isMobAttack);
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
        int a = 0;
        int soluong = Util.nextInt(1, 10);
        for (int i = 0; i < soluong; i++) {
            ItemMap datitan = new ItemMap(this.zone, 1400, 1,
                    this.location.x + a, this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id);
            Service.getInstance().dropItemMap(this.zone, datitan);
            a += 5;
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
        this.textTalkMidle = new String[]{"Ta gã con gái ?", "Nếu các ngươi hạ được ta",
            "Chán", "Các ngươi quá yếu"};

    }

    @Override
    public void leaveMap() {
        BossFactory.createBoss(BossFactory.VUA_HUNG).setJustRest();
        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }

}
