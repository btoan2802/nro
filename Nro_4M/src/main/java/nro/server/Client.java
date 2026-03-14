package nro.server;

import nro.login.LoginSession;
import nro.models.item.Item;
import nro.models.map.war.NamekBallWar;
import nro.models.player.Player;
import nro.models.pvp.PVP;
import nro.server.io.Session;
import nro.services.InventoryService;
import nro.services.ItemTimeService;
import nro.services.MapService;
import nro.services.PlayerService;
import nro.services.Service;
import nro.services.func.PVPServcice;
import nro.services.func.SummonDragon;
import nro.services.func.TransactionService;
import nro.utils.Log;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.Setter;
import nro.consts.ConstPlayer;
import nro.models.item.CaiTrang;
import nro.models.map.Zone;
import nro.models.phuban.DragonNamecWar.TranhNgoc;
import nro.models.player.Inventory;
import nro.models.player.Pet;
import nro.models.skill.PlayerSkill;
import nro.models.skill.Skill;
import nro.services.ItemService;
import nro.services.PetService;
import nro.utils.SkillUtil;
import nro.utils.Util;

/**
 *
 * @author Hoàng Việt - 0857853150
 * @copyright 💖 GirlkuN 💖
 *
 */
public class Client implements Runnable {

    private static Client i;

    @Getter
    private final List<Session> sessions = new ArrayList<>();
    private final Map<Integer, Session> sessions_id = new HashMap<Integer, Session>();
    private final Map<Long, Player> players_id = new HashMap<Long, Player>();
    private final Map<Integer, Player> players_userId = new HashMap<Integer, Player>();
    private final Map<String, Player> players_name = new HashMap<String, Player>();
    private final List<Player> players = new ArrayList<>();
    public final List<Player> bots = new ArrayList<>();
    public final List<Pet> pets = new ArrayList<>();
    private final String[] TenDau = {"Song", "Goku", "Vegeta", "Trung", "Nam", "Huy", "Bao", "Minh", "Long", "Kiet", "Khoa", "Tung", "Linh", "Duy", "Hoang",
        "Ken", "Dark", "Noob", "Pro", "Super", "Mega", "Solo", "Try", "Blue", "Red", "Black", "White", "Shadow", "Xx", "King",
        "Queen", "Anh", "Chi", "Boss", "Player", "Zoro", "Luffy", "Sabo", "Ace", "Zero", "God", "Devil", "Chien", "Yasuo",
        "Ten", "Zin", "Rin", "Kai", "Jay", "Leo", "Bin", "Kenji", "Ryo", "Shin", "Tan", "Tuan", "Phuc", "Dat", "Binh"
    };
    private final String[] TenSau = {"VIP", "Pro", "Cute", "TV", "VN", "God", "Official", "1999", "2003", "2010", "123", "456", "69", "99", "Xx",
        "Solo", "TryHard", "Gamer", "NoMercy", "SieuNhan", "SML", "TikTok", "YT", "Free", "Dark", "Master", "Legend", "Team",
        "TVN", "Mobile", "Live", "Gaming", "TV123", "HD", "King", "Super", "Dragon", "Wolf", "Knight", "Devil", "Godlike"};
    private boolean running = true;
    private int id = 1_000_000_000;

    private Client() {
        new Thread(this).start();
    }

    public List<Player> getPlayers() {
        synchronized (players) {
            return this.players.stream().collect(Collectors.toList());
        }
    }

    public static Client gI() {
        if (i == null) {
            i = new Client();
        }
        return i;
    }

    public void put(Session session) {
        synchronized (sessions) {
            if (!sessions_id.containsValue(session)) {
                this.sessions_id.put(session.id, session);
            }
            if (!sessions.contains(session)) {
                this.sessions.add(session);
            }
        }
    }

    public void put(Player player) {
        if (!players_id.containsKey(player.id)) {
            this.players_id.put(player.id, player);
        }
        if (!players_name.containsValue(player)) {
            this.players_name.put(player.name, player);
        }
        if (!players_userId.containsValue(player)) {
            this.players_userId.put(player.getSession().userId, player);
        }
        if (!players.contains(player)) {
            this.players.add(player);
        }

    }

    private void remove(Session session) {
        synchronized (sessions) {
            this.sessions_id.remove(session.id);
            this.sessions.remove(session);
            LoginSession login = ServerManager.gI().getLogin();
            if (login != null && login.isConnected()) {
                login.getService().logout(session.userId);
            }
            if (session.player != null) {
                this.remove(session.player);
                session.player.dispose();
            }
            if (session.loginSuccess && session.joinedGame) {
                session.loginSuccess = false;
                session.joinedGame = false;
            }
            ServerManager.gI().disconnect(session);
        }
    }

    public void clear() {
        if (!bots.isEmpty()) {
            MapService.gI().exitMap(bots.get(0));
            MapService.gI().exitMap(bots.get(0).pet);
            pets.remove(0);
            players.remove(bots.get(0));
            this.players_id.remove(bots.get(0).id);
            this.players_name.remove(bots.get(0).name);
            bots.remove(0);
        }
    }

    public void createBot() {
        try {
            String[] name1 = TenDau;
            String[] name2 = TenSau;
            Player pl = new Player();
            System.out.println("Creat Bot:" + "[" + id + "]");
            pl.id = id;
            id++;
            int vip = Util.nextInt(1, 4);
            String tenDau = name1[Util.nextInt(name1.length)];
            String tenSau = name2[Util.nextInt(name2.length)];
            if (Util.nextInt(100) < 20) {
                pl.name = "[VIP " + vip + "] " + tenDau + tenSau;
            } else {
                pl.name = tenDau + tenSau;
            }
            pl.gender = (byte) Util.nextInt(0, 2);
            pl.isBot = true;
            pl.isBoss = false;
            pl.isMiniPet = false;
            pl.isPet = false;
            pl.nPoint.power = Util.nextLong(100_000_000_000L, 500_000_000_000L);
            pl.nPoint.power *= Util.nextInt(1, 2);
            pl.nPoint.hpg = Util.nextInt(1000000, 10000000);
            pl.nPoint.hpMax = Util.nextInt(1000000, 10000000);
            pl.nPoint.hp = pl.nPoint.hpMax / 2;
            pl.nPoint.mpMax = Util.nextInt(1000, 10000);
            pl.nPoint.dame = Util.nextInt(1000, 10000);
            pl.nPoint.stamina = 32000;
            Pet pet = new Pet(pl);
            PetService.gI().createPetIsBot(pl, (byte) 0);
            pet.changeStatus(Pet.FOLLOW);
            pet.isPet = true;
            pl.itemTime.isUseTDLT = true;
            pl.lastTimeMap = System.currentTimeMillis();
            ItemTimeService.gI().sendCanAutoPlay(pl);
            pl.typePk = ConstPlayer.NON_PK;
            //skill
            int[] skillsArr = pl.gender == 0 ? new int[]{0, 1, 6, 20, 22}
                    : pl.gender == 1 ? new int[]{17, 3, 7, 2, 18, 12}
                    : new int[]{4, 5, 8};
            pl.playerSkill = new PlayerSkill(pl);
            for (int j = 0; j < skillsArr.length; j++) {
                Skill skill = SkillUtil.createSkill(skillsArr[j], Util.nextInt(2, 7));
                pl.playerSkill.skills.add(skill);
            }
            pl.inventory = new Inventory(pl);
            for (int i = 0; i < 13; i++) {
                pl.inventory.itemsBody.add(ItemService.gI().createItemNull());
            }
            pl.inventory.gold = 2000000000;
            pl.inventory.itemsBody.set(5, Manager.CT.get(Util.nextInt(0, Manager.CT.size() - 1)));
            pl.inventory.itemsBody.set(8, Manager.FLAG.get(Util.nextInt(0, Manager.FLAG.size() - 1)));
            Service.getInstance().sendFlagBag(pl);
            Service.getInstance().Send_Caitrang(pl);
            pl.location.y = 50;
            pets.add(pet);
            bots.add(pl);
            Zone z = MapService.gI().getMapCanJoin(pl, Util.nextInt(150));
            while (z != null && !z.mobs.isEmpty()) {
                z = MapService.gI().getMapCanJoin(pl, Util.nextInt(150));
            }
            pl.zone = MapService.gI().getMapCanJoin(pl, Util.nextInt(150));
            if (pl.zone == null) {
                return;
            }
            if (pl.zone.map == null) {
                return;
            }
            pl.location.x = Util.nextInt(20, pl.zone.map.mapWidth - 20);
            pl.fusion.typeFusion = ConstPlayer.HOP_THE_PORATA5;
            pl.zone.addPlayer(pl);
            pl.zone.load_Me_To_Another(pl);
            pl.zone.loadAnotherToMe(pl);
            Client.gI().put(pl);
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    private void remove(Player player) {
        this.players_id.remove(player.id);
        this.players_name.remove(player.name);
        this.players_userId.remove(player.getSession().userId);
        this.players.remove(player);
        dispose(player);
    }

    public void dispose(Player player) {
        if (!player.beforeDispose) {
            if (player.isHoldNamecBall) {
                NamekBallWar.gI().dropBall(player);
            }
            TranhNgoc.gI().removePlayersCadic(player);
            TranhNgoc.gI().removePlayersFide(player);
            player.beforeDispose = true;
            player.mapIdBeforeLogout = player.zone.map.mapId;
            MapService.gI().exitMap(player);
            TransactionService.gI().cancelTrade(player);
            PVPServcice.gI().finishPVP(player, PVP.TYPE_LEAVE_MAP);
            if (player.clan != null) {
                player.clan.removeMemberOnline(null, player);
            }
            if (player.itemTime != null && player.itemTime.isUseTDLT) {
                Item tdlt = InventoryService.gI().findItemBagByTemp(player, 521);
                if (tdlt != null) {
                    ItemTimeService.gI().turnOffTDLT(player, tdlt);
                }
            }
            if (SummonDragon.gI().playerSummonShenron != null
                    && SummonDragon.gI().playerSummonShenron.id == player.id) {
                SummonDragon.gI().isPlayerDisconnect = true;
            }
            if (player.mobMe != null) {
                player.mobMe.mobMeDie();
            }
            if (player.checkphanthan()) {
                player.phanthan.dispose();
            }
            if (player.pet != null) {
                if (player.pet.mobMe != null) {
                    player.pet.mobMe.mobMeDie();
                }
                MapService.gI().exitMap(player.pet);
            }
            if (player.minipet != null) {
                MapService.gI().exitMap(player.minipet);
            }
            if (player.nPoint != null && player.nPointClone != null) {
                player.nPoint = player.nPointClone;
            }
            if (player.playerSkill.tempSkills != null) {
                if (player.playerSkill.skillSelect != null
                        && player.playerSkill.tempSkills != null
                        && player.playerSkill.tempSkills.contains(player.playerSkill.skillSelect)) {
                    player.playerSkill.skillSelect = null;
                }
                player.playerSkill.tempSkills = null;
                if (!player.playerSkill.skills.isEmpty()) {
                    player.playerSkill.skillSelect = player.playerSkill.skills.get(0);
                }
            }
            PlayerService.gI().savePlayer(player);
        }
    }

    public void kickSession(Session session) {
        if (session != null) {
            this.remove(session);
            session.disconnect();
        }
    }

    public Player getPlayer(long playerId) {
        return this.players_id.get(playerId);
    }

    public Player getPlayerByUser(int userId) {
        return this.players_userId.get(userId);
    }

    public Session getSession(Session session) {
        synchronized (sessions) {
            for (Session se : sessions) {
                if (se != session && se.userId == session.userId) {
                    return se;
                }
            }
        }
        return null;
    }

    public Player getPlayer(String name) {
        return this.players_name.get(name);
    }

    public Player findPlayerByAddingVipPrefix(String nameWithoutVip) {
        for (Player p : Client.gI().getPlayers()) {
            if (p.name != null) {
                // Kiểm tra tên có dạng [VIP x] Tên
                if (p.name.matches("^\\s*\\[VIP \\d+\\]\\s+.*")) {
                    // Lấy phần tên sau [VIP x]
                    String realName = p.name.replaceFirst("^\\s*\\[VIP \\d+\\]\\s*", "").trim();
                    if (realName.equalsIgnoreCase(nameWithoutVip)) {
                        return p;
                    }
                } else {
                    // Trường hợp không có VIP
                    if (p.name.equalsIgnoreCase(nameWithoutVip)) {
                        return p;
                    }
                }
            }
        }
        return null;
    }

    public Session getSession(int sessionId) {
        return this.sessions_id.get(sessionId);
    }

    public void close() {
        Log.log("BEGIN KICK OUT SESSION...............................");
        synchronized (sessions) {
            while (!this.sessions.isEmpty()) {
                Log.log("LEFT PLAYER: " + this.players.size() + ".........................");
                this.kickSession(this.sessions.remove(0));
            }
        }
    }

    private void update() {
        synchronized (sessions) {
            for (Session session : sessions) {
                if (session.timeWait > 0) {
                    session.timeWait--;
                    if (session.timeWait == 0) {
                        kickSession(session);
                    }
                }
            }
        }
    }

    @Override
    public void run() {
        while (ServerManager.isRunning) {
            try {
                long st = System.currentTimeMillis();
                update();
                Thread.sleep(800 - (System.currentTimeMillis() - st));
            } catch (Exception e) {
            }
        }
    }

    public void show(Player player) {
        String txt = "";
        txt += "sessions: " + sessions.size() + "\n";
        txt += "sessions_id: " + sessions_id.size() + "\n";
        txt += "players_id: " + players_id.size() + "\n";
        txt += "players_userId: " + players_userId.size() + "\n";
        txt += "players_name: " + players_name.size() + "\n";
        txt += "players: " + players.size() + "\n";
        Service.getInstance().sendThongBao(player, txt);
    }
}
