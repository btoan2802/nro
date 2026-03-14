package nro.models.boss.Doremon;

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
import nro.models.skill.Skill;
import nro.server.Manager;
import nro.services.RewardService;
import nro.services.Service;
import nro.utils.Util;

/**
 * @author Hoàng Việt - 0857853150
 * @copyright 💖 GirlkuN 💖
 */
public class Doremon extends Boss {

    public Doremon() {
        super(BossFactory.DOREMON, BossData.DOREMON);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
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
    public void rewards(Player pl) {
        if (Util.isTrue(1, 100)) {
            ItemMap itemMap = new ItemMap(this.zone, 900, 1, this.location.x - 10, this.location.y, pl.id);
            Service.getInstance().dropItemMap(zone, itemMap);
        } else if (Util.isTrue(9, 100)) {
            int a = 10;
            int soluong = Util.nextInt(1, 5);
            for (int i = 0; i < soluong; i++) {
                ItemMap itemMap1 = new ItemMap(this.zone, ConstItem.XU_DAC_BIET, 1, this.location.x + a, this.location.y, pl.id);
                Service.getInstance().dropItemMap(zone, itemMap1);
                a += 10;
            }
        } else if (Util.isTrue(20, 100)) {
            int[] set1 = {650, 651, 652, 653, 654, 655, 657, 658, 659, 660, 661, 662};
            ItemMap itemMap = ratiItemHuyDiet(zone, set1[Util.nextInt(0, set1.length - 1)], 1, this.location.x - 20, this.location.y, pl.id);
            Service.getInstance().dropItemMap(zone, itemMap);
        } else if (Util.isTrue(30, 100)) {
            int a = 10;
            int soluong = Util.nextInt(1, 10);
            for (int i = 0; i < soluong; i++) {
                ItemMap itemMap1 = new ItemMap(this.zone, 1400, 1, this.location.x + a, this.location.y, pl.id);
                Service.getInstance().dropItemMap(zone, itemMap1);
                a += 10;
            }
        } else {
            int a = 10;
            int soluong = Util.nextInt(1, 20);
            for (int i = 0; i < soluong; i++) {
                ItemMap itemMap2 = new ItemMap(this.zone, 674, 1, this.location.x + a, this.location.y, pl.id);
                Service.getInstance().dropItemMap(zone, itemMap2);
                a += 10;
            }
        }
        if (pl != null) {
            pl.diem_tet += 10;
            Service.getInstance().sendThongBao(pl, "Bạn nhận được 10 Điểm Boss");
        }
        generalRewards(pl);
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

    }

    @Override
    public void leaveMap() {
        BossFactory.createBoss(BossFactory.DOREMON).setJustRest();
        super.leaveMap();
        BossManager.gI().removeBoss(this);

    }
}
