package nro.models.map.war;

import lombok.Getter;
import nro.consts.ConstItem;
import nro.consts.ConstPlayer;
import nro.manager.NamekBallManager;
import nro.models.clan.Clan;
import nro.models.dragon.Poruga;
import nro.models.map.Map;
import nro.models.map.NamekBall;
import nro.models.map.Zone;
import nro.models.npc.Npc;
import nro.models.player.Player;
import nro.services.MapService;
import nro.services.PlayerService;
import nro.services.Service;
import nro.utils.Util;

public class NamekBallWar {

    private static final NamekBallWar INSTANCE = new NamekBallWar();
    @Getter
    private Player[] holders = new Player[7];

    public static NamekBallWar gI() {
        return INSTANCE;
    }

    public void pickBall(Player player, NamekBall item) {
        if (player.isHoldNamecBall) {
            dropBall(player);
        }
        if (item.isStone()) {
            Service.getInstance().sendThongBao(player, "Chỉ là cục đá, vác chi cho nặng!");
            return;
        }
        player.isHoldNamecBall = true;
        Service.getInstance().sendFlagBag(player);
        PlayerService.gI().changeAndSendTypePK(player, ConstPlayer.PK_ALL);
        item.setHolding(true);
        holders[item.getIndex()] = player;
        Service.getInstance().sendFlagBag(player);
        item.reAppearItem();
        item.setHolderName(player.name);
        Service.getInstance().sendThongBao(player, "Bạn đang giữ viên ngọc rồng Namek " + (item.getIndex() + 1) + " sao");
    }

    public void summonDragon(Player player, Npc npc) {
        Clan clan = player.clan;
        if (clan == null) {
            npc.npcChat(player, "Chưa có bang hội");
            return;
        }
        int clanID = clan.id;
        if (holders[0] != player) {
            npc.npcChat(player, "Hãy đem viên ngọc một sao tới gặp tao để có thể triệu hồi rồng thần");
            return;
        }
        int valid = 0;
        for (Player holder : holders) {
            if (holder != null && holder.clan.id == clanID && holder.zone == player.zone) {
                valid++;
            }
        }
        if (valid < 7) {
            Service.getInstance().sendThongBao(player,
                    "Hiện tại mới có " + valid + "/7 người đúng điều kiện (cùng bang và cùng khu). Không thể triệu hồi rồng.");
            return;
        }
        NamekBall oneStar = NamekBallManager.gI().findByID(ConstItem.NGOC_RONG_NAMEK_1_SAO);
        if (!oneStar.isCleaning()) {
            oneStar.setCleaning(true);
            oneStar.setCleaningTime(1 * 60);
            npc.npcChat(player, "Hãy đợi 1 phút tao lau sạch ngọc rồng,trong thời gian lau ngọc nếu một trong các viên ngọc bị rơi sẽ làm mới thời gian lau");
            return;
        }
        if (oneStar.isCleaning() && oneStar.getCleaningTime() == 0) {
            new Poruga(player).summon();
        } else {
            int secondsLeft = (int) oneStar.getCleaningTime();
            int minutes = secondsLeft / 60;
            int seconds = secondsLeft % 60;
            String timeLeft = (minutes > 0 ? minutes + " phút " : "") + seconds + " giây";
            npc.npcChat(player, "Chưa lau xong ngọc rồng. Thời gian còn lại: " + timeLeft);
        }
    }

    public void dropBall(Player player) {
        int index = -1;

        for (int i = 0; i < holders.length; i++) {
            if (holders[i] == player) {
                holders[i] = null;
                index = i;
                break;
            }
        }

        if (index == -1 && player == null) {
            for (int i = 0; i < holders.length; i++) {
                if (holders[i] == null) {
                    continue;
                }
                if (holders[i] != null && (holders[i].session == null || !holders[i].session.connected)) {
                    index = i;
                    holders[i] = null;
                    break;
                }
            }
        }

        if (index != -1) {
            NamekBall ball = NamekBallManager.gI().findByIndex(index);
            if (ball != null) {
                ball.setCleaning(false);
                ball.setHolding(false);
                ball.setHolderName("");
                if (player != null) {
                    player.isHoldNamecBall = false;
                    ball.setZone(player.zone);
                    ball.x = player.location.x;
                    ball.y = player.location.y;
                    ball.reAppearItem();
                    Service.getInstance().point(player);
                    Service.getInstance().sendFlagBag(player);
                    PlayerService.gI().changeAndSendTypePK(player, ConstPlayer.NON_PK);
                } else {
                    int[] validMapIds = {
                        0, 1, 2, 3, 4, 5, 6, 7, 8, 9,
                        10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20,
                        24, 25, 26, 27, 28, 29, 30, 31, 32, 33, 34,
                        35, 36, 37, 38, 42, 43, 44
                    };
                    int mapId = validMapIds[Util.nextInt(0, validMapIds.length - 1)];
                    Map m = MapService.gI().getMapById(mapId);
                    Zone z = m.zones.get(Util.nextInt(0, m.zones.size() - 1));
                    int x = m.mapWidth / 2;
                    int y = m.yPhysicInTop(m.mapWidth / 2, m.mapHeight / 2);

                    ball.setZone(z);
                    ball.x = x;
                    ball.y = y;
                    Service.getInstance().dropItemMap(z, ball);
                }
            }
        }
    }

//    public void dropBall(Player player) {
//        int index = -1;
//        for (int i = 0; i < holders.length; i++) {
//            if (holders[i] == player) {
//                holders[i] = null;
//                index = i;
//            }
//        }
//        if (index != -1) {
//            player.isHoldNamecBall = false;
//            NamekBall ball = NamekBallManager.gI().findByIndex(index);
//            if (ball.getIndex() != 0) {
//                NamekBall oneStar = NamekBallManager.gI().findByIndex(0);
//                oneStar.setCleaning(false);
//            }
//            ball.setCleaning(false);
//            ball.setZone(player.zone);
//            ball.x = player.location.x;
//            ball.y = player.location.y;
//            ball.setHolding(false);
//            ball.reAppearItem();
//            ball.setHolderName("");
//            Service.getInstance().point(player);
//            Service.getInstance().sendFlagBag(player);
//        }
//        PlayerService.gI().changeAndSendTypePK(player, ConstPlayer.NON_PK);
//    }
}
