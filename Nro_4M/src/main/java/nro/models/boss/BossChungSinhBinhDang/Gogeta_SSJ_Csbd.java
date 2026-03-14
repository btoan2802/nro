package nro.models.boss.BossChungSinhBinhDang;

import editor.Final_Static;
import editor.Final_Static;
import nro.consts.ConstPlayer;
import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossManager;
import nro.models.map.ItemMap;
import nro.models.map.Zone;
import nro.models.player.Player;
import nro.models.skill.Skill;
import nro.services.Service;
import nro.services.func.ChangeMapService;
import static nro.services.func.ChangeMapService.NON_SPACE_SHIP;
import nro.utils.Util;

public class Gogeta_SSJ_Csbd extends Boss {

    private final int x;
    private final int y;

    public Gogeta_SSJ_Csbd(Player player, Zone zone, int id, short idMap) throws Exception {
        super(id, new BossData(
                "Gogeta SSJ", //name
                ConstPlayer.XAYDA, //gender
                Boss.DAME_NORMAL, //type dame
                Boss.HP_NORMAL, //type hp
                1_000, //dame
                new double[][]{{100_000_000_000D}}, //hp
                new short[]{1806, 1807, 1808}, //outfit {head, body, leg}
                new short[]{idMap}, //map join
                new int[][]{
                    {Skill.DEMON, 3, 1}, {Skill.DEMON, 6, 2}, {Skill.DRAGON, 7, 3}, {Skill.DRAGON, 1, 4}, {Skill.GALICK, 5, 5},
                    {Skill.KAMEJOKO, 7, 6}, {Skill.KAMEJOKO, 6, 7}, {Skill.KAMEJOKO, 5, 8}, {Skill.KAMEJOKO, 4, 9}, {Skill.KAMEJOKO, 3, 10}, {Skill.KAMEJOKO, 2, 11}, {Skill.KAMEJOKO, 1, 12},
                    {Skill.ANTOMIC, 1, 13}, {Skill.ANTOMIC, 2, 14}, {Skill.ANTOMIC, 3, 15}, {Skill.ANTOMIC, 4, 16}, {Skill.ANTOMIC, 5, 17}, {Skill.ANTOMIC, 6, 19}, {Skill.ANTOMIC, 7, 20},
                    {Skill.MASENKO, 1, 21}, {Skill.MASENKO, 5, 22}, {Skill.MASENKO, 6, 23},
                    {Skill.KAMEJOKO, 7, 1000},},
                0
        ));
        this.x = player.location.x + 50;
        this.y = player.location.y;
        this.zone = zone;
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public void joinMap() {
        if (this.zone == null) {
            this.zone = getMapCanJoin(mapJoin[Util.nextInt(0, mapJoin.length - 1)]);
        }
        if (this.zone != null) {
            ChangeMapService.gI().changeMap(this, this.zone, -1, -1, this.x, this.y, NON_SPACE_SHIP);
        }
    }

    @Override
    public void rewards(Player plKill) {
        for (int i = 0; i < 3; i++) {
            int itemId = Final_Static.ItemRoiChungSinhBinhDang[Util.nextInt(0, Final_Static.ItemRoiChungSinhBinhDang.length - 1)];
            ItemMap itemcsbd = Util.khongthegiaodichchungsinhbinhdang(
                    this.zone,
                    itemId,
                    1,
                    this.location.x + Util.nextInt(-50, 50), // lệch nhẹ mỗi item
                    this.zone.map.yPhysicInTop(x, this.location.y - 24),
                    plKill.id
            );
            Service.getInstance().dropItemMap(this.zone, itemcsbd);
        }
    }

    @Override
    public void active() {
        super.active();
    }

    @Override
    public void leaveMap() {
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
