package nro.models.boss.robotsatthu;

import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.BossManager;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.services.Service;
import nro.services.TaskService;
import nro.utils.Util;

/**
 *
 * @author ❤Girlkun75❤
 * @copyright ❤Trần Lại❤
 */
public class Poc extends Boss {

    public Poc() {
        super(BossFactory.POC, BossData.POC);
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
    }

    @Override
    public void leaveMap() {
        super.leaveMap();
        BossManager.gI().removeBoss(this);
        BossManager.gI().getBossById(BossFactory.PIC).changeToAttack();
    }

}
