package nro.models.boss.nappa;

import nro.models.boss.Boss;
import nro.models.boss.BossData;
import nro.models.boss.BossFactory;
import nro.models.boss.FutureBoss;
import nro.models.map.ItemMap;
import nro.models.player.Player;
import nro.services.RewardService;
import nro.services.Service;
import nro.services.TaskService;
import nro.utils.Util;

/**
 *
 * @author Hoàng Việt - 0857853150
 * @copyright 💖 GirlkuN 💖
 *
 */
public class MapDauDinh extends FutureBoss {

    public MapDauDinh() {
        super(BossFactory.MAP_DAU_DINH, BossData.MAP_DAU_DINH);
    }

    @Override
    protected boolean useSpecialSkill() {
        return false;
    }


    @Override
    public void rewards(Player pl) {
        int[] random = {457, 381, 382, 383, 384};
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

}
