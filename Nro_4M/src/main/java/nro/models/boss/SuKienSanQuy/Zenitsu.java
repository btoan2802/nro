package nro.models.boss.SuKienSanQuy;

import nro.consts.ConstItem;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.BossManager;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.models.skill.Skill;
import nro.services.Service;
import nro.utils.Util;

public class Zenitsu extends Boss {

    public Zenitsu() {
        super(BossFactory.ZENITSU_BOSS, BossData.ZENITSU_BOSS);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public void rewards(Player pl) {
        pl.event.addEventPointSK(5);
        int[] Itemroi = {1871};
        int soluongroi = 1;
        int a = 20;
        for (int i = 0; i < soluongroi; i++) {
            ItemMap itemMap = new ItemMap(this.zone, Itemroi[Util.nextInt(0, Itemroi.length - 1)], 1,
                    this.location.x + a, this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id);
            Service.getInstance().dropItemMap(this.zone, itemMap);
            a += 10;
        }
        if (Util.isTrue(1, 100)) {
            Service.getInstance().dropItemMap(this.zone, new ItemMap(
                    this.zone,
                    ConstItem.NGOC_RONG_SIEU_CAP,
                    1,
                    this.location.x + a,
                    this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24),
                    pl.id));
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
    }

    @Override
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (plAtt != null) {
            switch ((plAtt.playerSkill != null && plAtt.playerSkill.skillSelect != null) ? plAtt.playerSkill.skillSelect.template.id : -1) {
                case Skill.SUPER_KAME, Skill.LIEN_HOAN_CHUONG, Skill.MA_PHONG_BA, Skill.KAMEJOKO, Skill.MASENKO, Skill.ANTOMIC -> {
                    damage = 1.0;
                    Service.getInstance().chat(plAtt, "Trời ơi, " + plAtt.playerSkill.skillSelect.template.name + " hoàn toàn vô hiệu lực với hắn..");
                }
                default -> {
                    if (plAtt.isPl()) {
                        int damevaohpboss = 18; // 18% máu hiện tại
                        double maxPercentDamage = this.nPoint.hp * damevaohpboss / 100.0;
                        if (damage > maxPercentDamage) {
                            damage = maxPercentDamage;
                        }
                        damage = Math.max(damage, 1.0);
                    } else {
                        damage = 1.0;
                    }
                }
            }
        } else {
            damage = 1.0;
        }
        return super.injured(plAtt, damage, piercing, isMobAttack);
    }

    @Override
    public void leaveMap() {
        super.leaveMap();
        BossManager.gI().removeBoss(this);
        BossManager.gI().getBossById(BossFactory.INOSUKE_HASHIBIRA_BOSS).changeToAttack();
    }
}
