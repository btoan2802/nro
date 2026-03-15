package nro.models.boss.BossHuyDiet;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import nro.consts.ConstRatio;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.services.Service;
import nro.utils.Util;

import nro.models.boss.BossManager;
import nro.models.map.Zone;
import nro.services.SkillService;
import nro.utils.SkillUtil;

/**
 * @author Hoàng Việt - 0857853150
 * @copyright 💖 GirlkuN 💖
 */
public class HuyDiet1 extends Boss {

    public HuyDiet1() {
        super(BossFactory.HUY_DIET1, BossData.HUY_DIET1);
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
    int a = 0;

    // rơi set Hủy Diệt
    int[] set1 = {657, 659, 661, 656, 650, 651, 652, 653, 654, 655, 658, 660, 662};
    itemMap = ratiItemHuyDiet(zone, set1[Util.nextInt(0, set1.length - 1)], 1,
            this.location.x - 20, this.location.y, pl.id);
    Service.getInstance().dropItemMap(this.zone, itemMap);

    // luôn rơi 1 item 1547
    itemMap = new ItemMap(this.zone, 1547, 1,
            this.location.x + 5, this.location.y, pl.id);
    // thêm option không thể giao dịch
    itemMap.options.add(new ItemOption(30, 0));
    Service.getInstance().dropItemMap(this.zone, itemMap);
    

    // rơi vàng 861 (random 1 -> 10 cục)
    int sluong = Util.nextInt(1, 10);
    for (int i = 0; i < sluong; i++) {
        itemMap = new ItemMap(this.zone, 861, 2000,
                this.location.x + a, this.location.y, pl.id);
        Service.getInstance().dropItemMap(this.zone, itemMap);
        a += 5;
    }

    // rơi capsule
    int[] capsule = {14, 15, 16, 17, 18, 19, 20};
    itemMap = new ItemMap(this.zone,
            capsule[Util.nextInt(0, capsule.length - 1)],
            1, this.location.x, this.location.y, pl.id);
    Service.getInstance().dropItemMap(this.zone, itemMap);

    if (pl != null) {
        pl.top_sanboss += 2;
    }
}
    public static ItemMap ratiItemHuyDiet(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, y, playerId);
        List<Integer> ao = Arrays.asList(650, 652, 654);
        List<Integer> quan = Arrays.asList(651, 653, 655);
        List<Integer> gang = Arrays.asList(657, 659, 661);
        List<Integer> giay = Arrays.asList(658, 660, 662);
        int nhd = 656;
        if (ao.contains(tempId)) {
            it.options.add(new ItemOption(47, Util.highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(1001) + 1800))); // áo từ 1800-2800 giáp
        }
        if (quan.contains(tempId)) {
            it.options.add(new ItemOption(22, Util.highlightsItem(it.itemTemplate.gender == 0, new Random().nextInt(16) + 85))); // hp 85-100k
        }
        if (gang.contains(tempId)) {
            it.options.add(new ItemOption(0, Util.highlightsItem(it.itemTemplate.gender == 2, new Random().nextInt(1500) + 8500))); // 8500-10000
        }
        if (giay.contains(tempId)) {
            it.options.add(new ItemOption(23, Util.highlightsItem(it.itemTemplate.gender == 1, new Random().nextInt(11) + 80))); // ki 80-90k
        }
        if (nhd == tempId) {
            it.options.add(new ItemOption(14, new Random().nextInt(3) + 17));
        }
        it.options.add(new ItemOption(21, 80));
        return it;
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
        BossFactory.createBoss(BossFactory.HUY_DIET1).setJustRest();
        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }

}
