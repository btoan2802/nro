package nro.models.boss.SuKienHe;

import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.BossManager;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.models.skill.Skill;
import nro.services.Service;
import nro.services.SkillService;

import nro.utils.Util;

public class King_Kong_Sanh_Dieu extends Boss {

    public King_Kong_Sanh_Dieu() {
        super(BossFactory.KING_KONG_SANH_DIEU, BossData.KING_KONG_SANH_DIEU);
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
        int[] Itemroi = {1842, 1843};
        int soluongroi = 10;
        int a = 20;
        for (int i = 0; i < soluongroi; i++) {
            ItemMap itemMap = new ItemMap(this.zone, Itemroi[Util.nextInt(0, Itemroi.length - 1)], 5,
                    this.location.x + a, this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id);
            Service.getInstance().dropItemMap(this.zone, itemMap);
            a += 10;
        }
    }

    @Override
    public void idle() {

    }

    @Override
    public void checkPlayerDie(Player pl) {

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
                        int damevaohpboss = 20; // 20% máu hiện tại
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
    public void initTalk() {

    }

    @Override
    public void leaveMap() {
        BossFactory.createBoss(BossFactory.KING_KONG_SANH_DIEU).setJustRest();
        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }
}
