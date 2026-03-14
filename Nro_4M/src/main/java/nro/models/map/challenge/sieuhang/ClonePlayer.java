package nro.models.map.challenge.sieuhang;

import nro.models.boss.BossData;
import nro.models.boss.dhvt.BossDHVT;
import nro.models.player.Player;
import nro.utils.Util;

public class ClonePlayer extends BossDHVT {

    public ClonePlayer(Player player, BossData data, int id) throws Exception {
        super(Util.randomBossId(), data);
        this.playerAtt = player;
        this.idPlayer = id;
    }

}
