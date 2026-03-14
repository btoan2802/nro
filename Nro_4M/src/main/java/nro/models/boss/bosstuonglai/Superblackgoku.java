package nro.models.boss.bosstuonglai;

import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.BossManager;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.services.Service;
import nro.services.SkillService;
import nro.utils.Util;
import nro.consts.ConstItem;
import nro.server.Manager;
import nro.services.RewardService;
import nro.services.TaskService;

public class Superblackgoku extends Boss {

    public Superblackgoku() {
        super(BossFactory.SUPERBLACKGOKU, BossData.SUPERBLACKGOKU);
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
        // do than 1/20
        int[] tempIds1 = new int[]{563, 565, 567};
        int tempId = -1;
        if (Util.isTrue(20, 20)) {
            tempId = 992;
        } else if (Util.isTrue(30, 30)) {
            tempId = tempIds1[Util.nextInt(0, tempIds1.length - 1)];
        }
        if (Manager.EVENT_SEVER == 4 && tempId == -1) {
            tempId = ConstItem.LIST_ITEM_NLSK_TET_2023[Util.nextInt(0, ConstItem.LIST_ITEM_NLSK_TET_2023.length - 1)];
        }
        if (tempId != -1) {
            ItemMap itemMap = new ItemMap(this.zone, tempId, 1,
                    pl.location.x, this.zone.map.yPhysicInTop(pl.location.x, pl.location.y - 24), pl.id);
            RewardService.gI().initBaseOptionClothes(itemMap.itemTemplate.id, itemMap.itemTemplate.type, itemMap.options);
            RewardService.gI().initStarOption(itemMap, new RewardService.RatioStar[]{
                new RewardService.RatioStar((byte) 1, 1, 2),
                new RewardService.RatioStar((byte) 2, 1, 3),
                new RewardService.RatioStar((byte) 3, 1, 4),
                new RewardService.RatioStar((byte) 4, 1, 5),
                new RewardService.RatioStar((byte) 5, 1, 6),
                new RewardService.RatioStar((byte) 6, 1, 7),
                new RewardService.RatioStar((byte) 7, 1, 8)
            });
            Service.getInstance().dropItemMap(this.zone, itemMap);
        }
        TaskService.gI().checkDoneTaskKillBoss(pl, this);
        generalRewards(pl);
    }

    @Override
    public void leaveMap() {
        BossFactory.createBoss(BossFactory.SUPERBLACKGOKU);
        super.leaveMap();
        BossManager.gI().removeBoss(this);
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

}
