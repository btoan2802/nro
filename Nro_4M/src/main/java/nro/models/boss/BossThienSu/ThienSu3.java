package nro.models.boss.BossThienSu;

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
public class ThienSu3 extends Boss {

    public ThienSu3() {
        super(BossFactory.THIEN_SU3, BossData.THIEN_SU3);
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
        if (Util.isTrue(1, 100)) {
            int[] set1 = {1054, 1055, 1056, 1060, 1061, 1062, 1048, 1049, 1050, 1051, 1052, 1053, 1057, 1058, 1059};
            itemMap = ratiItemThienSu(zone, set1[Util.nextInt(0, set1.length - 1)], 1, this.location.x - 20, this.location.y, pl.id);
            Service.getInstance().dropItemMap(this.zone, itemMap);
        }  else if (Util.isTrue(20, 100)) {
            int sluong = Util.nextInt(1, 5);
            int a = 0;
            for (int i = 0; i < sluong; i++) {
                itemMap = new ItemMap(this.zone, 1400, 1, this.location.x + a, this.location.y, pl.id);
                Service.getInstance().dropItemMap(this.zone, itemMap);
                a += 5;
            }
        } else if (Util.isTrue(30, 100)) {
            int sluong = Util.nextInt(1, 10);
            int a = 0;
            for (int i = 0; i < sluong; i++) {
                itemMap = new ItemMap(this.zone, 861, 3000, this.location.x + a, this.location.y, pl.id);
                Service.getInstance().dropItemMap(this.zone, itemMap);
                a += 5;
            }
        } else {
            int[] set1 = {1066, 1067, 1068, 1069, 1070};
            itemMap = new ItemMap(this.zone, set1[Util.nextInt(0, set1.length - 1)], 30, this.location.x, this.location.y, pl.id);
            Service.getInstance().dropItemMap(this.zone, itemMap);
        }
        if (pl != null) {
            pl.top_sanboss += 2;
        }
    }

    public static ItemMap ratiItemThienSu(Zone zone, int itemId, int quantity, int x, int y, long playerId) {
        ItemMap dots = new ItemMap(zone, itemId, quantity, x, y, playerId);
        List<Integer> ao = Arrays.asList(1048, 1049, 1050);
        List<Integer> quan = Arrays.asList(1051, 1052, 1053);
        List<Integer> gang = Arrays.asList(1054, 1055, 1056);
        List<Integer> giay = Arrays.asList(1057, 1058, 1059);
        List<Integer> nhan = Arrays.asList(1060, 1061, 1062);
        //áo
        if (ao.contains(itemId)) {
            dots.options.add(new ItemOption(47, Util.highlightsItem(dots.itemTemplate.gender == 2, new Random().nextInt(1201) + 2800))); // áo từ 2800-4000 giáp
        }
        //quần
        if (Util.isTrue(60, 100)) {
            if (quan.contains(itemId)) {
                dots.options.add(new ItemOption(22, Util.highlightsItem(dots.itemTemplate.gender == 0, new Random().nextInt(11) + 120))); // hp 120k-130k
            }
        } else {
            if (quan.contains(itemId)) {
                dots.options.add(new ItemOption(22, Util.highlightsItem(dots.itemTemplate.gender == 0, new Random().nextInt(51) + 130))); // hp 130-180k 15%
            }
        }
        //găng
        if (Util.isTrue(60, 100)) {
            if (gang.contains(itemId)) {
                dots.options.add(new ItemOption(0, Util.highlightsItem(dots.itemTemplate.gender == 2, new Random().nextInt(7651) + 11000))); // 11000-18600
            }
        } else {
            if (gang.contains(itemId)) {
                dots.options.add(new ItemOption(0, Util.highlightsItem(dots.itemTemplate.gender == 2, new Random().nextInt(7001) + 12000))); // gang 15% 12-19k -xayda 12k1
            }
        }
        //giày
        if (Util.isTrue(60, 100)) {
            if (giay.contains(itemId)) {
                dots.options.add(new ItemOption(23, Util.highlightsItem(dots.itemTemplate.gender == 1, new Random().nextInt(21) + 120))); // ki 90-110k
            }
        } else {
            if (giay.contains(itemId)) {
                dots.options.add(new ItemOption(23, Util.highlightsItem(dots.itemTemplate.gender == 1, new Random().nextInt(21) + 130))); // ki 110-130k
            }
        }

        if (nhan.contains(itemId)) {
            dots.options.add(new ItemOption(14, Util.highlightsItem(dots.itemTemplate.gender == 1, new Random().nextInt(3) + 18))); // nhẫn 18-20%
        }
        dots.options.add(new ItemOption(21, 120));
        return dots;
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
        BossFactory.createBoss(BossFactory.THIEN_SU3).setJustRest();
        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }

}
