package nro.models.boss.bill;

import nro.consts.ConstItem;
import nro.consts.ConstPlayer;
import nro.consts.ConstRatio;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossManager;
import nro.models.boss.FutureBoss;
import nro.models.item.Item;
import nro.models.map.Zone;
import nro.models.player.Player;
import nro.models.skill.Skill;
import nro.services.InventoryService;
import nro.services.ItemService;
import nro.services.PlayerService;
import nro.services.Service;
import nro.services.SkillService;
import nro.services.func.ChangeMapService;
import nro.utils.Log;
import nro.utils.SkillUtil;
import nro.utils.Util;

/**
 * @copyright VIET
 */
public class Boss_Leo_Thap extends FutureBoss {

    public Boss_Leo_Thap(int bossID, BossData bossData, Zone zone, int x, int y, Player player) throws Exception {
        super(bossID, bossData);
        this.zone = zone;
        this.location.x = x;
        this.location.y = y;
        this.nPoint.hpMax += player.levelKillThap * 10_000_000_000D;
        this.nPoint.hpg += player.levelKillThap * 10_000_000_000D;
        this.nPoint.mpg += player.levelKillThap * 10_000_000_000D;
        this.nPoint.mpMax += player.levelKillThap * 10_000_000_000D;
        this.nPoint.dame += player.levelKillThap * 100_000_000D;
        this.name = this.name + " [Tầng:" + player.levelKillThap + "]";
        Service.getInstance().hsChar(this, this.nPoint.hpMax, this.nPoint.mpMax);
        this.idPlayerForNPC = player.idPlayerForNPC;
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public void rewards(Player pl) {
    }

    @Override
    public void idle() {
    }

    @Override
    public void checkPlayerDie(Player pl) {
        PlayerService.gI().changeAndSendTypePK(this, ConstPlayer.PK_PVP);
        if (pl.isDie()) {
            goToXY(pl.location.x + 2, pl.location.y, false);
            goToXY(pl.location.x - 2, pl.location.y, false);
            this.chat("Luyện tập thêm đi");
            try {
                Thread.sleep(5000);
                leaveMap();
                hide_npc(pl, 105, 1);
            } catch (InterruptedException ex) {
            }
        }
    }

    @Override
    public void joinMap() {
        if (this.zone != null) {
            ChangeMapService.gI().changeMap(this, 175, this.zone.zoneId, 814, 504);
//            System.out.println("BOSS " + this.name + " (" + this.id + ")" + ": " + this.zone.map.mapName + " khu vực " + this.zone.zoneId + "(" + this.zone.map.mapId + ")");
        }
    }

    @Override
    public void attack() {
        try {
            if (this.zone == null) {
                leaveMap();
            } else {
                Player pl = super.zone.findPlayerByID(idPlayerForNPC);
                if (pl != null && !pl.isDie() && !pl.isMiniPet) {

                    this.playerSkill.skillSelect = this.getSkillAttack();

                    // Kiểm tra null cho playerSkill và playerSkill.skillSelect
                    if (this.playerSkill != null && this.playerSkill.skillSelect != null) {

                        double distance = Util.getDistance(this, pl);
                        double range = this.getRangeCanAttackWithSkillSelect();

                        // Kiểm tra distance và range
                        if (distance <= range) {
                            if (Util.isTrue(15, ConstRatio.PER100)) {
                                if (SkillUtil.isUseSkillChuong(this)) {
                                    goToXY(pl.location.x + (Util.getOne(-1, 1) * Util.nextInt(5, 15)),
                                            Util.nextInt(3) % 2 == 0 ? pl.location.y : pl.location.y, false);
                                } else {
                                    goToXY(pl.location.x + (Util.getOne(-1, 1) * Util.nextInt(5, 15)),
                                            Util.nextInt(3) % 2 == 0 ? pl.location.y : pl.location.y, false);
                                }
                            }
                            SkillService skillService = SkillService.gI();

                            // Kiểm tra null cho skillService
                            if (skillService != null) {
                                skillService.useSkill(this, pl, null, null);
                                checkPlayerDie(pl);
                            } else {
                                // Xử lý khi skillService là null
                                Log.error("SkillService is null");
                            }
                        } else {
                            goToPlayer(pl, false);
                        }
                    } else {
                        // Xử lý khi playerSkill hoặc playerSkill.skillSelect là null
                        Log.error("playerSkill or playerSkill.skillSelect is null");
                    }
                } else {
                    leaveMap();
                }
            }
        } catch (Exception ex) {
            Log.error(Boss.class, ex);
        }
    }

    @Override
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        if (plAtt.id == idPlayerForNPC) {
            double dame = 0;
            if (this.isDie()) {
                return dame;
            } else {
                if (Util.isTrue(1, 5)) {
                    switch (plAtt.playerSkill.skillSelect.template.id) {
                        case Skill.TU_SAT:
                        case Skill.QUA_CAU_KENH_KHI:
                        case Skill.MAKANKOSAPPO:
                            break;
                        default:
                            return 0;
                    }
                }
                dame = super.injured(plAtt, damage, piercing, isMobAttack);
                if (this.isDie()) {
                    Item item = ItemService.gI().createNewItem((short) ConstItem.XU_SERVER);
                    if (plAtt.levelKillThap > 0 && plAtt.levelKillThap % 10 == 0) {
                        item.quantity = 15;
                        InventoryService.gI().addItemBag(plAtt, item, 1);
                        InventoryService.gI().sendItemBags(plAtt);
                        Service.getInstance().sendThongBao(plAtt, "Nhận 15 " + ConstItem.NAME_XU_SERVER);
                    } else {
                        item.quantity = 1;
                        InventoryService.gI().addItemBag(plAtt, item, 1);
                        InventoryService.gI().sendItemBags(plAtt);
                        Service.getInstance().sendThongBao(plAtt, "Nhận 1 " + ConstItem.NAME_XU_SERVER);
                    }
                    rewards(plAtt);
                    notifyPlayeKill(plAtt);
                    plAtt.levelKillThap++;

                    plAtt.lastTimeKillThap = System.currentTimeMillis();
                    plAtt.levelKillThapDone = plAtt.levelKillThap;
                    long timekillThap = plAtt.lastTimeKillThap - plAtt.lastTimeSwapThap;
                    if (timekillThap > 0) {
                        plAtt.timeKillThap = timekillThap;
                    } else {
                        System.out.println("Giá trị bị lỗi");
                    }
                    PlayerService.gI().savePlayer(plAtt);
                    PlayerService.gI().changeAndSendTypePK(plAtt, ConstPlayer.NON_PK);
                    die();
                }
                return dame;
            }
        }
        return 0;
    }

    @Override
    public void initTalk() {
        this.textTalkBefore = new String[]{"Ta sẽ dạy ngươi vài chiêu"};
        this.textTalkMidle = new String[]{"Haizzzzz", "Xem đây", "Hahaha", "ai da"};
        this.textTalkAfter = new String[]{"OK ta chịu thua"};
    }

    @Override
    public void leaveMap() {
        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }
}
