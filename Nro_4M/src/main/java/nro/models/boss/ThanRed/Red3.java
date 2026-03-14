package nro.models.boss.ThanRed;


import java.util.Arrays;
import java.util.List;
import java.util.Random;
import nro.consts.ConstRatio;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.services.Service;
import nro.utils.Util;

import nro.models.boss.BossManager;
import nro.models.item.ItemOption;
import nro.models.map.Zone;
import nro.services.SkillService;
import nro.utils.SkillUtil;

/**
 * @author Hoàng Việt - 0857853150
 * @copyright 💖 GirlkuN 💖
 */
public class Red3 extends Boss {

    public Red3() {
        super(BossFactory.THAN_RED3, BossData.THAN_RED3);
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
        if (Util.isTrue(10, 100)) {
            ItemMap hop = new ItemMap(this.zone, 1407, 1,
                    this.location.x, this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id);
            Service.getInstance().dropItemMap(this.zone, hop);
        } else if (Util.isTrue(15, 100)) {
            ItemMap hop = new ItemMap(this.zone, 1769, 1,
                    this.location.x, this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id);
            Service.getInstance().dropItemMap(this.zone, hop);
        }  else if (Util.isTrue(20, 100)) {
            int[] set1 = {1054, 1055, 1056, 1060, 1061, 1062, 1048, 1049, 1050, 1051, 1052, 1053, 1057, 1058, 1059};
            ItemMap itemMap = ratiItemThienSu(zone, set1[Util.nextInt(0, set1.length - 1)], 1, this.location.x - 20, this.location.y, pl.id);
            Service.getInstance().dropItemMap(this.zone, itemMap);
        } else if (Util.isTrue(25, 100)) {
            int a = 0;
            for (int i = 0; i < 5; i++) {
                ItemMap datitan = new ItemMap(this.zone, 1400, 5,
                        this.location.x + a, this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id);
                Service.getInstance().dropItemMap(this.zone, datitan);
                a += 5;
            }
        } else {
            int[] tv = {861}; // 100k
            int[] dabv = {1787}; // xu vòng quay
            int[] nrbang = {1015}; // ngọc rồng siêu cấp
            int[] manhTS = {1874, 1875, 1876, 1877, 1878}; // mảnh siêu thần 50
            int[][] random = {tv, dabv, nrbang, manhTS};
            int[] vatPham = random[Util.nextInt(0, random.length - 1)];
            int sl_1_vien;
            int so_vien;
            if (vatPham == tv) {
                sl_1_vien = 20000;
                so_vien = 5;
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
        this.textTalkMidle = new String[]{"Oải rồi hả?", "Ê cố lên Người Anh Em",
            "Giết Ta Đi Nào", "Ta Sẽ Giết Lũ Súc Vật Hạ Đẳng Các Ngươi","Chán"};

    }

    @Override
    public void leaveMap() {
        BossFactory.createBoss(BossFactory.THAN_RED3).setJustRest();
        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }

}
