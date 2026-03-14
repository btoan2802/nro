package nro.models.dragon;

import nro.dialog.MenuDialog;
import nro.dialog.MenuRunable;
import nro.manager.NamekBallManager;
import nro.models.clan.Buff;
import nro.models.clan.Clan;
import nro.models.map.Zone;
import nro.models.player.Player;
import nro.server.io.Message;
import nro.services.NpcService;
import nro.services.Service;

import java.io.DataOutputStream;
import nro.models.item.Item;
import nro.server.Client;
import nro.services.InventoryService;
import nro.services.ItemService;
import nro.services.ItemTimeService;

/**
 * @author Hoàng Việt - 0857853150
 */
public class Poruga extends AbsDragon {

    private static Poruga instance;

    public Poruga(Player player) {
        super(player);
        this.setWishes(new String[]{"100 Điểm và 10 Xu Bang hội",
            "Tăng 50%\nsức đánh",
            "Tăng 50% HP",
            "Tăng 50% KI",
            "Tăng 30%\nchí mạng",
            "Tăng 500%\nTNSM"});
        this.setTutorial("");
        this.setContent("Ta sẽ ban cho ngươi điều ước,ngươi có 5 phút,hãy suy nghĩ thật kĩ trước khi quyết định,tác dụng của chúc phúc sẽ có hiệu lực đến 6h AM");
        this.setName("Rồng thần Namek");
        instance = this;
    }

    public static Poruga gI() {
        return instance;
    }

    @Override
    public void openMenu() {

    }

    @Override
    public void summon() {
        setAppear(true);
        callDragon();
        showWishes();
        setLastTimeAppear(System.currentTimeMillis());
        new Thread(this).start();
        sendNotify();
    }

    @Override
    public void reSummon() {

    }

    @Override
    public void showWishes() {
        Clan clan = getSummoner().clan;
        MenuDialog menu = new MenuDialog(getContent(), getWishes(), new MenuRunable() {
            @Override
            public void run() {
                switch (this.getIndexSelected()) {
                    case 0:
                        getSummoner().clan.members.forEach(m -> {
                            if (Client.gI().getPlayer(m.id) != null) {
                                Player p = Client.gI().getPlayer(m.id);
                                Item it = ItemService.gI().createNewItem((short) 1797);
                                it.quantity = 10;
                                InventoryService.gI().addItemBag(p, it, 0);
                                InventoryService.gI().sendItemBags(p);
                            }
                        });
                        getSummoner().clan.diemsukienbang += 100;
                        getSummoner().clan.update();
                        break;
                    case 1:
                        clan.setBuff(Buff.BUFF_ATK);
                        for (Player player : clan.membersInGame) {
                            player.itemTimesieucap.lastTimeRongVutrusd = System.currentTimeMillis();
                            player.itemTimesieucap.isRongVutrusd = true;
                            player.setBuff(clan.getBuff());
                            Service.getInstance().point(player);
                            ItemTimeService.gI().sendAllItemTime(player);
                            Service.getInstance().sendThongBao(player, "Bạn vừa nhận được chúc phúc của rồng thần Vũ trụ");
                        }
                        break;
                    case 2:
                        clan.setBuff(Buff.BUFF_HP);
                        for (Player player : clan.membersInGame) {
                            player.itemTimesieucap.lastTimeRongVutruhp = System.currentTimeMillis();
                            player.itemTimesieucap.isRongVutruhp = true;
                            player.setBuff(clan.getBuff());
                            Service.getInstance().point(player);
                            ItemTimeService.gI().sendAllItemTime(player);
                            Service.getInstance().sendThongBao(player, "Bạn vừa nhận được chúc phúc của rồng thần Vũ trụ");
                        }
                        break;
                    case 3:
                        clan.setBuff(Buff.BUFF_KI);
                        for (Player player : clan.membersInGame) {
                            player.itemTimesieucap.lastTimeRongVutruki = System.currentTimeMillis();
                            player.itemTimesieucap.isRongVutruki = true;
                            player.setBuff(clan.getBuff());
                            Service.getInstance().point(player);
                            ItemTimeService.gI().sendAllItemTime(player);
                            Service.getInstance().sendThongBao(player, "Bạn vừa nhận được chúc phúc của rồng thần Vũ trụ");
                        }
                        break;
                    case 4:
                        clan.setBuff(Buff.BUFF_CRIT);
                        for (Player player : clan.membersInGame) {
                            player.itemTimesieucap.lastTimeRongVutrucm = System.currentTimeMillis();
                            player.itemTimesieucap.isRongVutrucm = true;
                            player.setBuff(clan.getBuff());
                            Service.getInstance().point(player);
                            ItemTimeService.gI().sendAllItemTime(player);
                            Service.getInstance().sendThongBao(player, "Bạn vừa nhận được chúc phúc của rồng thần Vũ trụ");
                        }
                        break;
                    case 5:
                        clan.setBuff(Buff.BUFF_TNSM);
                        for (Player player : clan.membersInGame) {
                            player.itemTimesieucap.lastTimeRongVutrutnsm = System.currentTimeMillis();
                            player.itemTimesieucap.isRongVutrutnsm = true;
                            player.setBuff(clan.getBuff());
                            Service.getInstance().point(player);
                            ItemTimeService.gI().sendAllItemTime(player);
                            Service.getInstance().sendThongBao(player, "Bạn vừa nhận được chúc phúc của rồng thần Vũ trụ");
                        }
                        break;
                }

                leave();
            }
        });
        menu.show(getSummoner());
    }

    @Override
    public void callDragon() {
        Message msg = new Message(-83);
        DataOutputStream ds = msg.writer();
        try {
            ds.writeByte(isAppear() ? 0 : (byte) 1);
            if (isAppear()) {
                Zone z = getSummoner().zone;
                ds.writeShort(z.map.mapId);
                ds.writeShort(z.map.bgId);
                ds.writeByte(z.zoneId);
                ds.writeInt((int) getSummonerID());
                ds.writeUTF("");
                ds.writeShort(getSummoner().location.x);
                ds.writeShort(getSummoner().location.y);
                ds.writeByte(1);
                getSummoner().zone.effDragon = 50;
            }
            ds.flush();
            Service.getInstance().sendMessAllPlayer(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    @Override
    public void leave() {
        NpcService.gI().createTutorial(getSummoner(), -1, "Điều ước của ngươi đã trở thành sự thật\nHẹn gặp ngươi lần sau, ta đi ngủ đây, bái bai");
        setAppear(false);
        callDragon();
        NamekBallManager.gI().initFossil();
    }
}
