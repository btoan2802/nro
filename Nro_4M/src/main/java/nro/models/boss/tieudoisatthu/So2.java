package nro.models.boss.tieudoisatthu;

import nro.models.boss.*;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.services.Service;
import nro.services.TaskService;
import nro.utils.Util;

/**
 *
 * @author Hoàng Việt - 0857853150
 * @copyright 💖 GirlkuN 💖
 *
 */
public class So2 extends FutureBoss {

    public So2() {
        super(BossFactory.SO2, BossData.SO2);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }

    @Override
    public void rewards(Player pl) {
        int[] random = {457, 1099, 1100, 1101, 1102};
        int vpham = random[Util.nextInt(0, random.length - 1)];
        int soluong;
        if (vpham == 457) {
            soluong = 10;
        } else {
            soluong = 1;
        }
        int a = 0;
        for (int i = 0; i < soluong; i++) {
            ItemMap vp = new ItemMap(this.zone, vpham, 1,
                    this.location.x + a, this.zone.map.yPhysicInTop(this.location.x, this.location.y - 24), pl.id);
            Service.getInstance().dropItemMap(this.zone, vp);
            a += 5;
        }
        TaskService.gI().checkDoneTaskKillBoss(pl, this);
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
            "Chán", "Đại ca Fide có nhầm không nhỉ"};

    }

    @Override
    public void leaveMap() {
        BossFactory.createBoss(BossFactory.SO2).setJustRest();
        super.leaveMap();
        BossManager.gI().removeBoss(this);
    }

}
