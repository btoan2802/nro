package nro.models.player;

import java.sql.Timestamp;
import nro.card.Card;
import nro.card.CollectionBook;
import nro.consts.ConstAchive;
import nro.consts.ConstPlayer;
import nro.consts.ConstTask;
import nro.data.DataGame;
import nro.dialog.ConfirmDialog;
import nro.models.clan.Buff;
import nro.models.item.CaiTrang;
import nro.models.item.FlagBag;
import nro.models.boss.event.EscortedBoss;
import nro.models.clan.Clan;
import nro.models.clan.ClanMember;
import nro.models.intrinsic.IntrinsicPlayer;
import nro.models.item.Item;
import nro.models.item.ItemTime;
import nro.models.map.ItemMap;
import nro.models.map.TrapMap;
import nro.models.map.Zone;
import nro.models.map.war.BlackBallWar;
import nro.models.map.mabu.MabuWar;
import nro.models.map.war.NamekBallWar;
import nro.models.mob.MobMe;
import nro.models.npc.specialnpc.MabuEgg;
import nro.models.npc.specialnpc.MagicTree;
import nro.models.pvp.PVP;
import nro.models.skill.PlayerSkill;
import nro.models.task.TaskPlayer;
import nro.server.Client;
import nro.server.Manager;
import nro.server.io.Message;
import nro.server.io.Session;
import nro.services.*;
import nro.services.func.ChangeMapService;
import nro.services.func.CombineNew;
import nro.services.func.PVPServcice;
import nro.utils.Log;
import nro.utils.SkillUtil;
import nro.utils.Util;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import nro.models.item.ItemOption;
import nro.models.item.ItemTimeSieuCap;
import nro.models.map.ChungSinhBinhDang.ChungSinhBinhDang;
import nro.models.map.mabu.MabuWar14h;
import nro.models.mob.Mob;
import nro.models.phuban.DragonNamecWar.TranhNgoc;
import nro.models.phuban.DragonNamecWar.TranhNgocService;
import nro.models.skill.Skill;

/**
 *
 * @author Hoàng Việt - 0857853150
 * @copyright 💖 GirlkuN 💖
 *
 */
public class Player {
    public int bossKill;
    
    public boolean isTuTien;
    public boolean thongBaoExp;
    public int levelTuTien;
    public int tangTuTien;
    public int bonusTuTien;
    public long expTuTien;

    public int typeBinhDieuChe;
    public int vitriBinhDieuChe;
    public List<PhongThiNghiem_Player> phongThiNghiem = new ArrayList<>();

    public Date weekTimeLogin;

    public RuongSuuTamPlayer ruongSuuTam;
    public byte typeMoRuong;
    public byte active_ruong_suu_tam;

    public List<KhamNgocPlayer> khamNgoc = new ArrayList<>();
    public byte active_kham_ngoc;
    public byte nroKhamNgoc;
    public int idTempNangCap;
    public int slItem;

    public long diemdanh;

    public int bongtai;
    public int thiensu;

    public long lastTimeUpdateBallWar;
    public long lastTimeNotifyTimeHoldBlackBall;
    public long lastTimeHoldBlackBall;
    public int tempIdBlackBallHold = -1;
    public boolean isHoldBlackBall;
    public boolean isHoldNamecBall;
    public boolean isHoldNamecBallTranhDoat;
    public int tempIdNamecBallHoldTranhDoat = -1;

    public long timeoff = 0;
    public byte typetrain;
    public int expoff;
    public boolean istrain;

    public int sochon;
    public List<Integer> soMayMan = new ArrayList<>();
    public int rankSieuHang;
    public long timesieuhang;
    public boolean isnhanthuong1;
    public boolean isTransformPlayer;
    
    public boolean DH1 = false;
    public boolean DH2 = false;
    public boolean DH3 = false;
    public boolean DH4 = false;
    public boolean DH5 = false;

    public boolean isTitleUse1;
    public long lastTimeTitle1;
    public int IdDanhHieu_1;
    public boolean isTitleUse2;
    public long lastTimeTitle2;
    public int IdDanhHieu_2;
    public boolean isTitleUse3;
    public long lastTimeTitle3;
    public int IdDanhHieu_3;
    public boolean isTitleUse4;
    public long lastTimeTitle4;
    public int IdDanhHieu_4;
    public boolean isTitleUse5;
    public long lastTimeTitle5;
    public int IdDanhHieu_5;
    public boolean isTitleUse6;
    public long lastTimeTitle6;
    public int IdDanhHieu_6;
    public boolean isTitleUse7;
    public long lastTimeTitle7;
    public int IdDanhHieu_7;
    public boolean isTitleUse8;
    public long lastTimeTitle8;
    public int IdDanhHieu_8;
    public boolean isTitleUse9;
    public long lastTimeTitle9;
    public int IdDanhHieu_9;
    public boolean isTitleUse10;
    public long lastTimeTitle10;
    public int IdDanhHieu_10;
    public boolean isTitleUse11;
    public long lastTimeTitle11;
    public int IdDanhHieu_11;
    public boolean isTitleUse12;
    public long lastTimeTitle12;
    public int IdDanhHieu_12;
    public boolean isTitleUse13;
    public long lastTimeTitle13;
    public int IdDanhHieu_13;
    public boolean isTitleUse14;
    public long lastTimeTitle14;
    public int IdDanhHieu_14;
    public boolean isTitleUse15;
    public long lastTimeTitle15;
    public int IdDanhHieu_15;

    public boolean DH_VV_1;
    public boolean DH_VV_2;
    public boolean DH_VV_3;
    public boolean DH_VV_4;
    public boolean DH_VV_5;
    public boolean DH_VV_6;
    public boolean DH_VV_7;
    public boolean DH_VV_8;
    public boolean DH_VV_9;
    public boolean DH_VV_10;
    public boolean DH_VV_11;
    public boolean DH_VV_12;
    public boolean DH_VV_13;
    public boolean DH_VV_14;
    public boolean DH_VV_15;

    public boolean isTitlePriority1;
    public boolean isTitlePriority2;

    public boolean resetdame = false;
    public boolean hitdau = false;
    public long lastTimeDame;
    public double dametong = 0;
    
    public int chuyensinhsuphu;
    public int suphuchuaco1;
    public int suphuchuaco2;
    public int suphuchuaco3;
    public int suphuchuaco4;
    
    public int mot;
    public int hai;
    public int ba;
    public int bon;
    public int nam;

    public int sau;
    public int bay;
    public int tam;
    public int chin;
    public int muoi;

    public int ThuongNhiemVu;

    public int diem_tet;
    public int tongnap;
    public long lastTimeUseOption;
    public int goldTai;
    public int goldXiu;

    public int goldTai_xu;
    public int goldXiu_xu;
    public int topRuong;
    public int top_cauca;
    public int top_sanboss;
    public int MocVIP;

    public boolean dangGhepAn;
    public long lasttimeGhepAn;
    public short iconGhepAn;
    public long timeGhepAn;
    
    public boolean useCanCau;
    public long lasttimeCanCau;
    public short iconCancau;
    public long timeCauCa;
    
    
    public long lastEffectTimeKaioken = 0; // Time gửi hào quang Kaioken

    public long vangnhat = 0;
    public long hngocnhat = 0;
    public String Hppl = "\n";

    public int server;
    public byte[] buyLimit;

    public Timestamp lastimelogin;

    public long idPlayerForNPC;

    public long lastTimeSwapThap;
    public long lastTimeKillThap;
    public int levelKillThap;

    public int levelKillThapDone;
    public long timeKillThap;

    public int idPhucLoi;
    public List<Integer> listNhan = new ArrayList<>();
    public int[] checkNhan;
    public int phutOnline;
    public long lastTimeOnline = System.currentTimeMillis();
    public List<Integer> listOnline = new ArrayList<>();
    public List<Integer> listDiemDanh = new ArrayList<>();

    public int idTamBao;
    public List<Integer> listNhan_TamBao = new ArrayList<>();
    public int[] checkNhan_TamBao;
    public int diem_quay;
    public int[] list_id_nhan = new int[14];

    public PlayerEvent event;
    public List<String> textRuongGo = new ArrayList<>();
    public boolean receivedWoodChest;
    public int goldChallenge;
    public int levelWoodChest;
    public boolean isInvisible;
    public boolean sendMenuGotoNextFloorMabuWar;
    public long lastTimeBabiday;
    public long lastTimeChangeZone;
    public long lastTimeChatGlobal;
    public long lastTimeChatPrivate;
    public long lastTimeChangeMap;
    public Date firstTimeLogin;
    public Session session;
    public byte countSaveFail;
    public boolean beforeDispose;

    public long timeFixInventory;
    public boolean isPet;
    public boolean isBoss;
    public boolean isMiniPet;
    public boolean isBot = false;

    public PlayerPhanThan phanthan;
    public boolean isPhanThan;

    public int playerTradeId = -1;
    public Player playerTrade;

    public int mapIdBeforeLogout;
    public List<Zone> mapBlackBall;
    public Zone zone;
    public Zone mapBeforeCapsule;
    public List<Zone> mapCapsule;
    public Pet pet;
    public MiniPet minipet;

    public MobMe mobMe;
    public Location location;
    public SetClothes setClothes;
    public EffectSkill effectSkill;
    public MabuEgg mabuEgg;
    public TaskPlayer playerTask;
    public ItemTime itemTime;
    public ItemTimeSieuCap itemTimesieucap;
    public Fusion fusion;
    public MagicTree magicTree;
    public IntrinsicPlayer playerIntrinsic;
    public Inventory inventory;
    public PlayerSkill playerSkill;
    public CombineNew combineNew;
    public IDMark iDMark;
    public Charms charms;
    public EffectSkin effectSkin;
    public Gift gift;
    public NPoint nPoint;
    public NPoint nPointClone;
    public RewardBlackBall rewardBlackBall;
    public EffectFlagBag effectFlagBag;

    public Clan clan;
    public ClanMember clanMember;

    public ListFriendEnemy<Friend> friends;
    public ListFriendEnemy<Enemy> enemies;

    protected boolean actived = false;
    public boolean loaded;

    public long id;
    public String name;
    public byte gender;
    public boolean isNewMember = true;
    public short head;

    public byte typePk;

    public long lastHoiSinhTime = 0;

    public long lastTimePickTranhNgoc;
    public int tempIdPickTranhNgoc = -1;
    public boolean isPickTranhNgoc;

    public byte cFlag;
    public long lastTimeChangeFlag;
    public long lastTimeTrade;

    public boolean haveTennisSpaceShip;
    private byte useSpaceShip;

    public boolean isGoHome;

    public boolean justRevived;
    public long lastTimeRevived;
    public boolean immortal;

    public long lastTimeBan;
    public long lastTimeUpdate;
    public boolean isBan;

    public SkillSpecial skillSpecial;
    public boolean isGotoFuture;
    public long lastTimeGoToFuture;
    public boolean isgotoPrimaryForest;
    public long lastTimePrimaryForest;

    public boolean isgotoHanhTinhBang;
    public long lastTimeHanhTinhBang;

    public boolean isgotoDiaNguc;
    public long lastTimeDiaNguc;

    public boolean isgotoHallowen;
    public long lastTimeHallowen;

    public boolean isGoToBDKB;
    public long lastTimeGoToBDKB;
    public long lastTimeAnXienTrapBDKB;
    private short powerPoint;
    private short percentPowerPont;

    public long lastTimeAnXienTrapCSBD;

    public boolean PorataVIP;
    public boolean PorataVIP_2;
    public boolean PorataVIP_3;
    public boolean PorataVIP_4;
    public boolean PorataVIP_5;

    public long lastTimePickItem;
    @Setter
    @Getter
    private CollectionBook collectionBook;
    @Getter
    @Setter
    private boolean isSaving, isDisposed;
    @Getter
    @Setter
    private boolean interactWithKarin;
    @Getter
    @Setter
    private EscortedBoss escortedBoss;
    @Setter
    @Getter
    private ConfirmDialog confirmDialog;
    @Getter
    @Setter
    public byte[] rewardLimit;
    @Setter
    @Getter
    private PetFollow petFollow;
    @Setter
    @Getter
    private Buff buff;

    public Player() {
        ruongSuuTam = new RuongSuuTamPlayer();
        lastTimeUseOption = System.currentTimeMillis();
        location = new Location();
        nPoint = new NPoint(this);
        inventory = new Inventory(this);
        playerSkill = new PlayerSkill(this);
        setClothes = new SetClothes(this);
        effectSkill = new EffectSkill(this);
        fusion = new Fusion(this);
        playerIntrinsic = new IntrinsicPlayer(this);
        rewardBlackBall = new RewardBlackBall(this);
        effectFlagBag = new EffectFlagBag(this);
        //----------------------------------------------------------------------
        iDMark = new IDMark();
        combineNew = new CombineNew();
        playerTask = new TaskPlayer(this);
        friends = new ListFriendEnemy<>(this);
        enemies = new ListFriendEnemy<>(this);
        itemTime = new ItemTime(this);
        itemTimesieucap = new ItemTimeSieuCap(this);
        charms = new Charms(this);
        gift = new Gift(this);
        effectSkin = new EffectSkin(this);
        skillSpecial = new SkillSpecial(this);
        event = new PlayerEvent(this);
        buyLimit = new byte[13];
        buff = Buff.NONE;
    }

    //--------------------------------------------------------------------------
    public short getPowerPoint() {
        return powerPoint;
    }

    public void addPowerPoint(int value) {
        powerPoint += value;
    }

    public short getPercentPowerPont() {
        return percentPowerPont;
    }

    public void addPercentPowerPoint(int value) {
        percentPowerPont += value;
    }

    public void resetPowerPoint() {
        percentPowerPont = 0;
        powerPoint = 0;
    }

    public void setUseSpaceShip(byte useSpaceShip) {
        // 0 - không dùng
        // 1 - tàu vũ trụ theo hành tinh
        // 2 - dịch chuyển tức thời
        // 3 - tàu tenis
        this.useSpaceShip = useSpaceShip;
    }

    public byte getUseSpaceShip() {
        return this.useSpaceShip;
    }

    public boolean isDie() {
        if (this.nPoint != null) {
            return this.nPoint.hp < 1;
        } else {
            return true;
        }
    }

    public boolean check99ThucAnHuyDiet() {
        for (Item item : this.inventory.itemsBag) {
            if (item != null && item.template != null && item.template.id >= 663 && item.template.id <= 667 && item.quantity >= 99) {
                return true;
            }
        }
        return false;
    }

    //--------------------------------------------------------------------------
    public void setSession(Session session) {
        this.session = session;
    }

    public void sendMessage(Message msg) {
        if (this.session != null) {
            session.sendMessage(msg);
        }
    }

    public Session getSession() {
        return this.session;
    }

    public int version() {
        return session != null ? session.version : 0;
    }

    public boolean isVersionAbove(int version) {
        return version() >= version;
    }

    public void update() {
        if (this.isBot) {
            active();
        }
        if (this != null && this.name != null && !this.beforeDispose && !isBot) {
            try {
                if (this.istrain && !MapService.gI().isMapTrainOff(this, this.zone.map.mapId)) {
                    ChangeMapService.gI().changeMapBySpaceShip(this, MapService.gI().getMapTrainOff(this), -1, 250);
                    congExpOff();
                    this.timeoff = 0;
                }
                if (!isBan) {
                    if (!this.isBoss) {
                        this.idPlayerForNPC = this.id;
                    }
                    if (nPoint != null) {
                        nPoint.update();
                    }
                    if (fusion != null) {
                        fusion.update();
                    }
                    if (effectSkin != null) {
                        effectSkill.update();
                    }
                    if (mobMe != null) {
                        mobMe.update();
                    }
                    if (effectSkin != null) {
                        effectSkin.update();
                    }
                    if (pet != null) {
                        pet.update();
                    }
                    if (minipet != null) {
                        minipet.update();
                    }
                    if (checkphanthan()) {
                        phanthan.update();
                    }
                    if (magicTree != null) {
                        magicTree.update();
                    }
                    if (itemTime != null) {
                        itemTime.update();
                    }
                    if (itemTimesieucap != null) {
                        itemTimesieucap.update();
                    }
                    if (event != null) {
                        event.update();
                    }
                    if (this.isPl()) {
                        MabuWar.gI().update(this);
                        MabuWar14h.gI().update(this);
                        TranhNgoc.gI().update(this);
                        ChungSinhBinhDang.gI().kickmap(this);
                    }
//                    if (!this.isBoss && !this.isMiniPet && !this.isPhanThan) {
//                        if (pet != null && this.inventory.itemsBody.get(5).isNotNullItem() && this.pet.inventory.itemsBody.get(5).isNotNullItem()) {
//                            if ((this.inventory.itemsBody.get(5).template.id == 2032 && this.pet.inventory.itemsBody.get(5).template.id == 1409)
//                                    || (this.inventory.itemsBody.get(5).template.id == 1409 && this.pet.inventory.itemsBody.get(5).template.id == 2032)) {
//                                this.PorataVIP = true;
//                            } else {
//                                this.PorataVIP = false;
//                            }
//                        } else {
//                            this.PorataVIP = false;
//                        }
//                    }
//                    if (!this.isBoss && !this.isMiniPet && !this.isPhanThan) {
//                        if (pet != null && this.inventory.itemsBody.get(5).isNotNullItem() && this.pet.inventory.itemsBody.get(5).isNotNullItem()) {
//                            if ((this.inventory.itemsBody.get(5).template.id == 2035 && this.pet.inventory.itemsBody.get(5).template.id == 2036)//porata vip 2
//                                    || (this.inventory.itemsBody.get(5).template.id == 2036 && this.pet.inventory.itemsBody.get(5).template.id == 2035)) {//porata vip 2
//                                this.PorataVIP_2 = true;
//                            } else {
//                                this.PorataVIP_2 = false;
//                            }
//                        } else {
//                            this.PorataVIP_2 = false;
//                        }
//                    }
//                    if (!this.isBoss && !this.isMiniPet && !this.isPhanThan) {
//                        if (pet != null && this.inventory.itemsBody.get(5).isNotNullItem() && this.pet.inventory.itemsBody.get(5).isNotNullItem()) {
//                            if ((this.inventory.itemsBody.get(5).template.id == 1980 && this.pet.inventory.itemsBody.get(5).template.id == 1979)//porata vip 2
//                                    || (this.inventory.itemsBody.get(5).template.id == 1979 && this.pet.inventory.itemsBody.get(5).template.id == 1980)) {//porata vip 2
//                                this.PorataVIP_3 = true;
//                            } else {
//                                this.PorataVIP_3 = false;
//                            }
//                        } else {
//                            this.PorataVIP_3 = false;
//                        }
//                    }
//                    if (!this.isBoss && !this.isMiniPet && !this.isPhanThan) {
//                        if (pet != null && this.inventory.itemsBody.get(5).isNotNullItem() && this.pet.inventory.itemsBody.get(5).isNotNullItem()) {
//                            if ((this.inventory.itemsBody.get(5).template.id == 1997 && this.pet.inventory.itemsBody.get(5).template.id == 1998)//porata vip 2
//                                    || (this.inventory.itemsBody.get(5).template.id == 1998 && this.pet.inventory.itemsBody.get(5).template.id == 1997)) {//porata vip 2
//                                this.PorataVIP_4 = true;
//                            } else {
//                                this.PorataVIP_4 = false;
//                            }
//                        } else {
//                            this.PorataVIP_4 = false;
//                        }
//                    }
//                    if (!this.isBoss && !this.isMiniPet && !this.isPhanThan) {
//                        if (pet != null && this.inventory.itemsBody.get(5).isNotNullItem() && this.pet.inventory.itemsBody.get(5).isNotNullItem()) {
//                            if ((this.inventory.itemsBody.get(5).template.id == 2058 && this.pet.inventory.itemsBody.get(5).template.id == 2062)//porata vip 2
//                                    || (this.inventory.itemsBody.get(5).template.id == 2062 && this.pet.inventory.itemsBody.get(5).template.id == 2058)) {//porata vip 2
//                                this.PorataVIP_5 = true;
//                            } else {
//                                this.PorataVIP_5 = false;
//                            }
//                        } else {
//                            this.PorataVIP_5 = false;
//                        }
//                    }
                    BlackBallWar.gI().update(this);
                    if (!this.isBoss && !this.isPet && !this.isMiniPet && !this.isPhanThan) {
                        if (Manager.SERVER != 0 && this.server != Manager.SERVER && !this.isAdmin()) {
                            PlayerService.gI().banPlayer(this);
                        }
                        if (Util.canDoWithTime(lastTimeUpdate, 60000)) {
                            this.playerTask.achivements.get(ConstAchive.HOAT_DONG_CHAM_CHI).count++;
                            this.lastTimeUpdate = System.currentTimeMillis();
                        }
                        if (Util.canDoWithTime(lastTimeOnline, 60000)) {
                            this.phutOnline++;
                            this.lastTimeOnline = System.currentTimeMillis();
                        }
                    }
                    if (isGotoFuture && Util.canDoWithTime(lastTimeGoToFuture, 6000)) {
                        ChangeMapService.gI().changeMapBySpaceShip(this, 102, -1, Util.nextInt(60, 200));
                        this.isGotoFuture = false;
                    }
                    if (isGoToBDKB && Util.canDoWithTime(lastTimeGoToBDKB, 6000)) {
                        ChangeMapService.gI().changeMapBySpaceShip(this, 135, -1, 35);
                        this.isGoToBDKB = false;
                    }
                    if (isgotoPrimaryForest && Util.canDoWithTime(lastTimePrimaryForest, 6000)) {
                        ChangeMapService.gI().changeMap(this, 161, -1, 169, 312);
                        this.isgotoPrimaryForest = false;
                    }
                    if (isgotoHanhTinhBang && Util.canDoWithTime(lastTimeHanhTinhBang, 6000)) {
                        ChangeMapService.gI().changeMap(this, 211, -1, 290, 768);
                        this.isgotoHanhTinhBang = false;
                    }
                    if (isgotoDiaNguc && Util.canDoWithTime(lastTimeDiaNguc, 6000)) {
                        ChangeMapService.gI().changeMap(this, 212, -1, 110, 480);
                        this.isgotoDiaNguc = false;
                    }
                    if (isgotoHallowen && Util.canDoWithTime(lastTimeHallowen, 6000)) {
                        ChangeMapService.gI().changeMap(this, 210, -1, 258, 192);
                        this.isgotoHallowen = false;
                    }
                    if (this.zone != null) {
                        TrapMap trap = this.zone.isInTrap(this);
                        if (trap != null) {
                            trap.doPlayer(this);
                        }
                    }
                    if (Util.canDoWithTime(this.lastTimeDame, 5000) && this.dametong != 0 && this.hitdau == true) {
                        Service.getInstance().sendThongBao(this, "\n|1|DAME/5giây: \b|7|" + Util.powerToStringnew(this.dametong));
                        this.dametong = 0;
                        this.resetdame = true;
                        this.hitdau = false;
                    }
                } else {
                    if (Util.canDoWithTime(lastTimeBan, 5000)) {
                        Client.gI().kickSession(session);
                    }
                }
            } catch (Exception e) {
                Log.error(Player.class, e, "Lỗi tại player: " + this.name);
            }
        }
    }

    public boolean isPl() {
        return !isPet // Không phải (Pet)
                && !isBoss // Không phải Boss
                && !isMiniPet // Không phải thú cưng Mini Pet
                && !isBot // Không phải bot
                && !isPhanThan // Không phải bản sao (Clone)
                && !(this instanceof Referee); // Không phải Trọng tài (Referee)
    }

    //--------------------------------------------------------------------------
    /*
     * {380, 381, 382}: ht lưỡng long nhất thể xayda trái đất
     * {383, 384, 385}: ht porata xayda trái đất
     * {391, 392, 393}: ht namếc
     * {870, 871, 872}: ht c2 trái đất
     * {873, 874, 875}: ht c2 namếc
     * {867, 878, 869}: ht c2 xayda
     */
    private static final short[][] idOutfitFusion = {
        {380, 381, 382}, {383, 384, 385}, {391, 392, 393},// btc1 td,nm,xd
        {870, 871, 872}, {873, 874, 875}, {867, 868, 869}, //btc2 td,nm,xd
        {870, 871, 872}, {873, 874, 875}, {867, 868, 869}, //btc3 td,nm,xd
        {870, 871, 872}, {873, 874, 875}, {867, 868, 869}, //btc4 td,nm,xd
        {870, 871, 872}, {873, 874, 875}, {867, 868, 869}, //btc5 td,nm,xd
    };

    private static final short[][] HEAD_BIEN_HINH = {
        {1880, 1880, 1883, 1886, 1889, 1892},//Trái đất
        {1897, 1900, 1903, 1906, 1909, 1912},//Namec
        {1917, 1917, 1920, 1923, 1926, 1929}//Xayda
    };

    private static final byte[][] AURA_BIENHINH = {
        {30, 31, 31, 32, 33, 35},//Trái đất
        {36, 37, 37, 32, 38, 39},//Namec
        {30, 31, 31, 32, 33, 34}//Xayda
    };

    private static final short[][] BODY_LEG_BIEN_HINH = {
        {1895, 1896},//Trái đất
        {1915, 1916},//Namec
        {1932, 1933}//Xayda
    };

    public byte getAura() {
        if (!this.isPl() || this.isBot || (this instanceof TestDame)) {
            return -1;
        }
        if (effectSkill != null && effectSkill.isBienHinh) {
            return AURA_BIENHINH[this.gender][effectSkill.levelBienHinh - 1];
        }
        Item item = this.inventory.getCurrentBody().get(5);
        if (this.isPl() && this.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
            return 100;
        }
        if (!item.isNotNullItem()) {
            return -1;
        }
        switch (item.template.id) {
             // cải trang Thỏi Vàng
            case 2035:
                return 116;
            case 2036:
                return 116;
            case 1997:
                return 116;
            case 1998:
                return 116;
            case 2062:
                return 116;
            case 2058:
                return 116;
            case 1744:
                return 116;
            case 1858:
                return 116;
            case 1883:
                return 117;
            case 1884:
                return 119;
            case 1885:
                return 107;
            case 1879:
                return 118;
            case 1894:
                return 120;
            case 1834:
                return 119;
            case 1838:
                return 89;
            case 1887:
                return 107;
               // cải trang ước rồng 
            case 227:
            case 228:
            case 229:
                return 1;
             // cải trang mốc nạp   
            case 1844:
                return 24;
            case 1907:
                return 90;
            case 1908:
                return 90;
            case 1909:
                return 90;
                
        }
        if (Manager.TOP_PLAYERS.contains(this.name)) {
            return 1;
        }
        CollectionBook book = getCollectionBook();
        if (book != null) {
            Card card = book.getCards().stream().filter(t -> t.isUse() && t.getCardTemplate().getAura() != -1).findAny().orElse(null);
            if (card != null) {
                return (byte) card.getCardTemplate().getAura();
            }
        }
        return -1;
    }

    public boolean checkSkinFusion() {
        if (inventory != null && inventory.getCurrentBody().get(5).isNotNullItem()) {
            Short idct = inventory.getCurrentBody().get(5).template.id;
            // Cải trang map ngoài
            if (idct >= 601 && idct <= 2101) {
                return true;
            }
        }
        return false;
    }

    public short getHead() {
        if (this.id == 1000000) {
            return 412;
        }
        if (effectSkill != null && effectSkill.isMonkey) {
            return (short) ConstPlayer.HEADMONKEY[effectSkill.levelMonkey - 1];
        } else if (effectSkill != null && effectSkill.isBienHinh && !this.isTransformPlayer) {
            return (short) HEAD_BIEN_HINH[this.gender][effectSkill.levelBienHinh - 1];
        } else if (effectSkill != null && effectSkill.isSocola) {
            return 412;
        } else if (effectSkill != null && effectSkill.isBinh) {
            return 1321;
        } else if (effectSkin != null && effectSkin.isHoaDa) {
            return 454;
        } else if (fusion != null && fusion.typeFusion != ConstPlayer.NON_FUSION && this.nPoint.isNgoaiHinh == false) {
            if (checkSkinFusion()) {
                CaiTrang ct = Manager.getCaiTrangByItemId(inventory.getCurrentBody().get(5).template.id);
                return (short) (ct != null && ct.getID()[0] != -1 ? ct.getID()[0] : inventory.getCurrentBody().get(5).template.part);
            } else if (fusion.typeFusion == ConstPlayer.LUONG_LONG_NHAT_THE) {
                return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 0][0];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA && this.PorataVIP == false && this.PorataVIP_2 == false && this.PorataVIP_3 == false && this.PorataVIP_4 == false && this.PorataVIP_5 == false && this.nPoint.isNgoaiHinh == false) {
                return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 1][0];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2 && this.PorataVIP == false && this.PorataVIP_2 == false && this.PorataVIP_3 == false && this.PorataVIP_4 == false && this.PorataVIP_5 == false && this.nPoint.isNgoaiHinh == false) {
                return idOutfitFusion[3 + this.gender][0];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3 && this.PorataVIP == false && this.PorataVIP_2 == false && this.PorataVIP_3 == false && this.PorataVIP_4 == false && this.PorataVIP_5 == false && this.nPoint.isNgoaiHinh == false) {
                return idOutfitFusion[6 + this.gender][0];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4 && this.PorataVIP == false && this.PorataVIP_2 == false && this.PorataVIP_3 == false && this.PorataVIP_4 == false && this.PorataVIP_5 == false && this.nPoint.isNgoaiHinh == false) {
                return idOutfitFusion[9 + this.gender][0];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5 && this.PorataVIP == false && this.PorataVIP_2 == false && this.PorataVIP_3 == false && this.PorataVIP_4 == false && this.PorataVIP_5 == false && this.nPoint.isNgoaiHinh == false) {
                return idOutfitFusion[12 + this.gender][0];
            } else if (this.PorataVIP == true && this.nPoint.isNgoaiHinh == false) {
                return 1987;//porata vip 1
            } else if (this.PorataVIP_2 == true && this.nPoint.isNgoaiHinh == false) {
                return 1847;//porata vip 2
            } else if (this.PorataVIP_3 == true && this.nPoint.isNgoaiHinh == false) {
                return 1864;//porata vip 3
            } else if (this.PorataVIP_4 == true && this.nPoint.isNgoaiHinh == false) {
                return 1806;//porata vip 4
            } else if (this.PorataVIP_5 == true && this.nPoint.isNgoaiHinh == false) {
                return 1813;//porata vip 4
            }
        } else if (inventory != null && inventory.getCurrentBody().size() > 5 && inventory.getCurrentBody().get(5).isNotNullItem()) {
            CaiTrang ct = Manager.getCaiTrangByItemId(inventory.getCurrentBody().get(5).template.id);
            if (!checkfusioncsbd() && checkSkinFusion()) {
                return this.head;
            }
            return (short) (ct != null && ct.getID()[0] != -1 ? ct.getID()[0] : inventory.getCurrentBody().get(5).template.part);
        }
        return this.head;
    }

    public short getBody() {
        if (this.id == 1000000) {
            return 413;
        }
        if (effectSkill != null && effectSkill.isMonkey) {
            return 193;
        } else if (effectSkill != null && effectSkill.isBienHinh && !this.isTransformPlayer) {
            return (short) BODY_LEG_BIEN_HINH[this.gender][0];
        } else if (effectSkill != null && effectSkill.isSocola) {
            return 413;
        } else if (effectSkill != null && effectSkill.isBinh) {
            return 1322;
        } else if (effectSkin != null && effectSkin.isHoaDa) {
            return 455;
        } else if (fusion != null && fusion.typeFusion != ConstPlayer.NON_FUSION && this.nPoint.isNgoaiHinh == false) {
            if (checkSkinFusion()) {
                CaiTrang ct = Manager.getCaiTrangByItemId(inventory.getCurrentBody().get(5).template.id);
                return (short) ct.getID()[1];
            }
            if (fusion.typeFusion == ConstPlayer.LUONG_LONG_NHAT_THE) {
                return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 0][1];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA && this.PorataVIP == false && this.PorataVIP_2 == false && this.PorataVIP_3 == false && this.PorataVIP_4 == false && this.PorataVIP_5 == false && this.nPoint.isNgoaiHinh == false) {
                return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 1][1];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2 && this.PorataVIP == false && this.PorataVIP_2 == false && this.PorataVIP_3 == false && this.PorataVIP_4 == false && this.PorataVIP_5 == false && this.nPoint.isNgoaiHinh == false) {
                return idOutfitFusion[3 + this.gender][1];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3 && this.PorataVIP == false && this.PorataVIP_2 == false && this.PorataVIP_3 == false && this.PorataVIP_4 == false && this.PorataVIP_5 == false && this.nPoint.isNgoaiHinh == false) {
                return idOutfitFusion[6 + this.gender][1];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4 && this.PorataVIP == false && this.PorataVIP_2 == false && this.PorataVIP_3 == false && this.PorataVIP_4 == false && this.PorataVIP_5 == false && this.nPoint.isNgoaiHinh == false) {
                return idOutfitFusion[9 + this.gender][1];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5 && this.PorataVIP == false && this.PorataVIP_2 == false && this.PorataVIP_3 == false && this.PorataVIP_4 == false && this.PorataVIP_5 == false && this.nPoint.isNgoaiHinh == false) {
                return idOutfitFusion[12 + this.gender][1];
            } else if (this.PorataVIP == true && this.nPoint.isNgoaiHinh == false) {
                return 1988; //porata vip 1
            } else if (this.PorataVIP_2 == true && this.nPoint.isNgoaiHinh == false) {
                return 1850;//porata vip 2
            } else if (this.PorataVIP_3 == true && this.nPoint.isNgoaiHinh == false) {
                return 1867;//porata vip 3
            } else if (this.PorataVIP_4 == true && this.nPoint.isNgoaiHinh == false) {
                return 1807;//porata vip 4
            } else if (this.PorataVIP_5 == true && this.nPoint.isNgoaiHinh == false) {
                return 1814;//porata vip 4
            }
        } else if (inventory != null && inventory.getCurrentBody().size() > 5 && inventory.getCurrentBody().get(5).isNotNullItem()) {
            CaiTrang ct = Manager.getCaiTrangByItemId(inventory.getCurrentBody().get(5).template.id);
            if (!checkfusioncsbd() && checkSkinFusion()) {
                if (inventory != null && inventory.getCurrentBody().get(0).isNotNullItem()) {
                    if (inventory != null && inventory.getCurrentBody().get(0).isNotNullItem()) {
                        return inventory.getCurrentBody().get(0).template.part;
                    }
                } else {
                    return (short) (gender == ConstPlayer.NAMEC ? 59 : 57);
                }
            }
            if (ct != null && ct.getID()[1] != -1) {
                return (short) ct.getID()[1];
            }
        }
        if (inventory != null && inventory.getCurrentBody().get(0).isNotNullItem()) {
            return inventory.getCurrentBody().get(0).template.part;
        }
        return (short) (gender == ConstPlayer.NAMEC ? 59 : 57);
    }

    public short getLeg() {
        if (this.id == 1000000) {
            return 414;
        }
        if (effectSkill != null && effectSkill.isMonkey) {
            return 194;
        } else if (effectSkill != null && effectSkill.isBienHinh && !this.isTransformPlayer) {
            return (short) BODY_LEG_BIEN_HINH[this.gender][1];
        } else if (effectSkill != null && effectSkill.isSocola) {
            return 414;
        } else if (effectSkill != null && effectSkill.isBinh) {
            return 1323;
        } else if (effectSkin != null && effectSkin.isHoaDa) {
            return 456;
        } else if (fusion != null && fusion.typeFusion != ConstPlayer.NON_FUSION && this.nPoint.isNgoaiHinh == false) {
            if (checkSkinFusion()) {
                CaiTrang ct = Manager.getCaiTrangByItemId(inventory.getCurrentBody().get(5).template.id);
                return (short) ct.getID()[2];
            }
            if (fusion.typeFusion == ConstPlayer.LUONG_LONG_NHAT_THE) {
                return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 0][2];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA && this.PorataVIP == false && this.PorataVIP_2 == false && this.PorataVIP_3 == false && this.PorataVIP_4 == false && this.PorataVIP_5 == false && this.nPoint.isNgoaiHinh == false) {
                return idOutfitFusion[this.gender == ConstPlayer.NAMEC ? 2 : 1][2];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2 && this.PorataVIP == false && this.PorataVIP_2 == false && this.PorataVIP_3 == false && this.PorataVIP_4 == false && this.PorataVIP_5 == false && this.nPoint.isNgoaiHinh == false) {
                return idOutfitFusion[3 + this.gender][2];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3 && this.PorataVIP == false && this.PorataVIP_2 == false && this.PorataVIP_3 == false && this.PorataVIP_4 == false && this.PorataVIP_5 == false && this.nPoint.isNgoaiHinh == false) {
                return idOutfitFusion[6 + this.gender][2];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4 && this.PorataVIP == false && this.PorataVIP_2 == false && this.PorataVIP_3 == false && this.PorataVIP_4 == false && this.PorataVIP_5 == false && this.nPoint.isNgoaiHinh == false) {
                return idOutfitFusion[9 + this.gender][2];
            } else if (fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5 && this.PorataVIP == false && this.PorataVIP_2 == false && this.PorataVIP_3 == false && this.PorataVIP_4 == false && this.PorataVIP_5 == false && this.nPoint.isNgoaiHinh == false) {
                return idOutfitFusion[12 + this.gender][2];
            } else if (this.PorataVIP == true && this.nPoint.isNgoaiHinh == false) {
                return 1989;//porata vip 1
            } else if (this.PorataVIP_2 == true && this.nPoint.isNgoaiHinh == false) {
                return 1851;//porata vip 2
            } else if (this.PorataVIP_3 == true && this.nPoint.isNgoaiHinh == false) {
                return 1868;//porata vip 3
            } else if (this.PorataVIP_4 == true && this.nPoint.isNgoaiHinh == false) {
                return 1808;//porata vip 4
            } else if (this.PorataVIP_5 == true && this.nPoint.isNgoaiHinh == false) {
                return 1815;//porata vip 5
            }
        } else if (inventory != null && inventory.getCurrentBody().size() > 5 && inventory.getCurrentBody().get(5).isNotNullItem()) {
            if (!checkfusioncsbd() && checkSkinFusion()) {
                if (inventory != null && inventory.getCurrentBody().get(1).isNotNullItem()) {
                    return inventory.getCurrentBody().get(1).template.part;
                } else {
                    return (short) (gender == ConstPlayer.NAMEC ? 60 : 58);
                }
            }
            CaiTrang ct = Manager.getCaiTrangByItemId(inventory.getCurrentBody().get(5).template.id);
            if (ct != null && ct.getID()[2] != -1) {
                return (short) ct.getID()[2];
            }
        }
        if (inventory != null && inventory.getCurrentBody().get(1).isNotNullItem()) {
            return inventory.getCurrentBody().get(1).template.part;
        }
        return (short) (gender == 1 ? 60 : 58);
    }

    public short getFlagBag() {
        if (this.isHoldBlackBall) {
            return 31;
        } else if (this.isHoldNamecBall) {
            return 30;
        }
        if (this.inventory.getCurrentBody().size() == 13) {
            if (this.inventory.getCurrentBody().get(8).isNotNullItem()) {
                FlagBag f = FlagBagService.gI().getFlagBagByName(this.inventory.getCurrentBody().get(8).template.name);
                if (f != null) {
                    return (short) f.id;
                }
            }
        }
        if (this.isPet) {
            if (this.inventory.itemsBody.get(7).isNotNullItem()) {
                FlagBag ff = FlagBagService.gI().getFlagBagByName(this.inventory.itemsBody.get(7).template.name);
                if (ff != null) {
                    return (short) ff.id;
                }
            }
        }
        if (TaskService.gI().getIdTask(this) == ConstTask.TASK_3_2) {
            return 28;
        }
        if (this.clan != null) {
            return (short) this.clan.imgId;
        }
        return -1;
    }

    public short getMount() {
        if (!this.isBot && this.isVersionAbove(220)) {
            for (Item item : inventory.getCurrentBody()) {
                if (item.isNotNullItem()) {
                    if (item.template.type == 24) {
                        if (item.template.gender == 3 || item.template.gender == this.gender) {
                            return item.template.id;
                        } else {
                            return -1;
                        }
                    }
                    if (item.template.type == 23) {
                        if (item.template.id < 500) {
                            return item.template.id;
                        } else {
                            Object mount = DataGame.MAP_MOUNT_NUM.get(String.valueOf(item.template.id));
                            if (mount == null) {
                                return -1;
                            }
                            return (short) mount;
                        }
                    }
                }
            }
        } else {
            for (Item item : inventory.getCurrentBag()) {
                if (item.isNotNullItem()) {
                    if (item.template.type == 24) {
                        if (item.template.gender == 3 || item.template.gender == this.gender) {
                            return item.template.id;
                        } else {
                            return -1;
                        }
                    }
                    if (item.template.type == 23) {
                        if (item.template.id < 500) {
                            return item.template.id;
                        } else {
                            Object mount = DataGame.MAP_MOUNT_NUM.get(String.valueOf(item.template.id));
                            if (mount == null) {
                                return -1;
                            }
                            return (short) mount;
                        }
                    }
                }
            }
        }
        return -1;
    }

    public short getThuCung() {
        if (this.isPl() && this.inventory != null && this.inventory.getCurrentBody().size() > 7 && this.inventory.getCurrentBody().get(7).isNotNullItem()) {
            return (short) (this.inventory.getCurrentBody().get(7).template.id);
        }
        return -1;
    }

    public short getLinhThu() {
        if (this.isPl() && this.inventory != null && this.inventory.getCurrentBody().size() > 10 && this.inventory.getCurrentBody().get(10).isNotNullItem()) {
            return (short) (this.inventory.getCurrentBody().get(10).template.iconID - 1);
        }
        return 0;
    }
    public Mob mobTarget;

    public long lastTimeTargetMob;

    public long timeTargetMob;

    public long lastTimeAttack;
    public long lastTimeMap;
    public Player isBotAttckBoss;
    public PVP pvp;
    private long lastTimeTargetPlayer;
    private int timeTargetPlayer;

    public void moveTo(int x, int y) {
        byte dir = (byte) (this.location.x - x < 0 ? 1 : -1);
        byte move = (byte) Util.nextInt(40, 60);
        if (isBot) {
            move = (byte) (move * (byte) 2);
        }
        PlayerService.gI().playerMove(this, this.location.x + (dir == 1 ? move : -move), y + (Util.isTrue(3, 10) ? -50 : 0));
    }

    public Mob getMobAttack() {
        if (this.mobTarget != null && (this.mobTarget.isDie() || !this.zone.equals(this.mobTarget.zone))) {
            this.mobTarget = null;
        }
        if (this.mobTarget == null && Util.canDoWithTime(lastTimeTargetMob, timeTargetMob)) {
            this.mobTarget = this.zone.getRandomMobInMap();
            this.lastTimeTargetMob = System.currentTimeMillis();
            this.timeTargetMob = 500;
        }
        return this.mobTarget;
    }

    public void active() {
        if (this.isBot) {
            if (this.pet != null) {
                this.pet.update();
            }
            if (this.isDie()) {
                Service.getInstance().sendMoney(this);
                PlayerService.gI().hoiSinh(this);
                Service.getInstance().hsChar(this, this.nPoint.hpMax, this.nPoint.mpMax);
                PlayerService.gI().sendInfoHpMp(this);
            }
            if (this.nPoint.mp <= this.nPoint.mpMax * 30 / 100) {
                this.nPoint.mp = this.nPoint.mpMax;
            }
            this.BayCs();
            this.AttackBoss();
            this.attack();
        }
    }

    public int getRangeCanAttackWithSkillSelect() {
        int skillId = this.playerSkill.skillSelect.template.id;
        if (skillId == Skill.KAMEJOKO || skillId == Skill.MASENKO || skillId == Skill.ANTOMIC) {
            return Skill.RANGE_ATTACK_CHIEU_CHUONG;
        } else {
            return Skill.RANGE_ATTACK_CHIEU_DAM;
        }
    }

    public void BayCs() {
        if (this.isBot) {
            if (Util.canDoWithTime(lastTimeMap, Util.nextInt(60000, 120000))) {
                byte RandomMap = (byte) new Random().nextInt(0, Manager.IdMapSpam.length - 1);
                try {
                    this.lastTimeMap = System.currentTimeMillis();
                    ChangeMapService.gI().changeMapInYard(this, Manager.IdMapSpam[RandomMap], -1, -1);
                } catch (Exception e) {
                }
            }
        }
    }

    public void AttackBoss() {
        try {
            if (this.isBot) {
                List<Player> playersMap = null;
                playersMap = new ArrayList<>(this.zone.getHumanoids()); // Tạo một bản sao của danh sách playersMap

                if (!this.zone.map.isMapOffline) {
                    Iterator<Player> iterator = playersMap.iterator();
                    while (iterator.hasNext()) {
                        Player pl = iterator.next();
                        if (pl != null && pl != this && pl.isBoss && pl.location != null) {
                            this.playerSkill.skillSelect = this.playerSkill.skills.get(1);
                            if (SkillService.gI().canUseSkillWithCooldown(this)) {
                                PlayerService.gI().playerMove(this, pl.location.x + Util.nextInt(-60, 60),
                                        pl.location.y);
                                SkillService.gI().useSkill(this, pl, null, null);
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
        }
    }

    public Player getBotAttackBOSS() {
        if (this.isBotAttckBoss != null && (this.isBotAttckBoss.isDie() || !this.zone.equals(this.isBotAttckBoss.zone)
                || (this.isBotAttckBoss.pvp == null || this.pvp == null)
                || (this.isBotAttckBoss.typePk != ConstPlayer.PK_ALL || this.typePk != ConstPlayer.PK_ALL)
                || ((this.isBotAttckBoss.cFlag == 0 && this.cFlag == 0)
                && (this.isBotAttckBoss.cFlag != 8 || this.cFlag == this.isBotAttckBoss.cFlag)))
                || this.isBotAttckBoss == this) {
            this.isBotAttckBoss = null;
        }
        if (this.zone != null
                && (this.isBotAttckBoss == null || this.isBotAttckBoss == this)
                || Util.canDoWithTime(this.lastTimeTargetPlayer, this.timeTargetPlayer)) {
            this.isBotAttckBoss = this.zone.PlayerPKinmap();
            this.lastTimeTargetPlayer = System.currentTimeMillis();
            this.timeTargetPlayer = Util.nextInt(40000, 45000);
        }
        return this.isBotAttckBoss;
    }

    public void transformplayer() {
        if (this.effectSkill != null && this.effectSkill.isBienHinh) {
            this.isTransformPlayer = !this.isTransformPlayer;
            Service.getInstance().Send_Caitrang(this);
            Service.getInstance().chat(this, "Biến hình nguyên trạng....");
        }
    }

    public void attack() {
        try {
            if (this.isBot) {
                //this.mobTarget = this.getMobAttack();
                if (Util.canDoWithTime(lastTimeAttack, 100) && this.mobTarget != null) {

                    this.lastTimeAttack = System.currentTimeMillis();

                    Mob m = this.getMobAttack();
                    if (m == null || m.isDie()) {
                        return;
                    }
                    this.playerSkill.skillSelect = this.playerSkill.skills.get(Util.nextInt(0, this.playerSkill.skills.size() - 1));
                    //System.out.println(m.name);
                    if (Util.isTrue(80, 100)) {
                        this.playerSkill.skillSelect = this.playerSkill.skills.get(0);
                    }
                    if (Util.getDistance(this, m) <= this.getRangeCanAttackWithSkillSelect()) {
                        if (Util.isTrue(5, 20)) {
                            if (SkillUtil.isUseSkillChuong(this)) {
                                this.moveTo(m.location.x + (Util.getOne(-1, 1) * Util.nextInt(20, 200)),
                                        Util.nextInt(10) % 2 == 0 ? m.location.y : m.location.y - Util.nextInt(0, 50));
                            } else {
                                this.moveTo(m.location.x + (Util.getOne(-1, 1) * Util.nextInt(10, 40)),
                                        Util.nextInt(10) % 2 == 0 ? m.location.y : m.location.y - Util.nextInt(0, 50));
                            }
                        }
                        SkillService.gI().useSkill(this, null, m, null);
                    } else {
                        this.moveTo(m.location.x, m.location.y);
                    }
                } else {
                    this.mobTarget = getMobAttack();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //--------------------------------------------------------------------------
    public double injured(Player plAtt, double damage, boolean piercing, boolean isMobAttack) {
        int mstChuong = this.nPoint.mstChuong;
        int giamst = this.nPoint.tlGiamst;
        int TileChinhxac = 0;
        int xuyenDef = plAtt != null ? plAtt.nPoint.tlxuyengiap : 0;
        if (!this.isDie()) {
            if (this.isMiniPet) {
                return 0;
            }
            if (plAtt != null) {
                TileChinhxac = plAtt.nPoint.tlchinhxac;
                if (!this.isBoss && plAtt.nPoint.xDameChuong && SkillUtil.isUseSkillChuong(plAtt)) {
                    damage = plAtt.nPoint.tlDameChuong * damage;
                    plAtt.nPoint.xDameChuong = false;
                }
                if (mstChuong > 0 && SkillUtil.isUseSkillChuong(plAtt)) {
                    PlayerService.gI().hoiPhuc(this, 0, damage * mstChuong / 100);
                    damage = 0;
                }
            }
            if (!SkillUtil.isUseSkillBoom(plAtt)) {
                if (TileChinhxac > 0) {
                    if (!piercing && Util.isTrue(this.nPoint.tlNeDon - TileChinhxac, 100)) {
                        return 0;
                    }
                } else {
                    if (!piercing && Util.isTrue(this.nPoint.tlNeDon, 100)) {
                        return 0;
                    }
                }
            }
            if (Util.isTrue(xuyenDef, 100)) {
            } else {
                damage = this.nPoint.subDameInjureWithDeff(damage);
            }
            if (!piercing && effectSkill.isShielding) {
                if (damage > nPoint.hpMax) {
                    EffectSkillService.gI().breakShield(this);
                }
                damage = 1;
            }
            if (isMobAttack && this.charms.tdBatTu > System.currentTimeMillis() && damage >= this.nPoint.hp) {
                damage = this.nPoint.hp - 1;
            }
            if (giamst > 0) {
                damage -= nPoint.calPercent(damage, giamst);
            }
            if (this.effectSkill.isHoldMabu) {
                damage = 1;
            }
            if (this.effectSkill.isHoldMabu && Util.isTrue(30, 150)) {
                Service.getInstance().removeMabuEat(this);
            }
            if (this.id == 3 && plAtt != null && plAtt.id != 3) {
                damage = 1;
            }
            this.nPoint.subHP(damage);
            if (isDie()) {
                if (plAtt != null) {
                    if (MapService.gI().isMapMabuWar(plAtt.zone.map.mapId)) {
                        plAtt.addPowerPoint(5);
                        Service.getInstance().sendPowerInfo(plAtt, "TL", plAtt.getPowerPoint());
                    }
                }
                setDie(plAtt);
                if (this.isPl() && this.zone.map.mapId == 126) {
                    ChangeMapService.gI().changeMap(this, 21 + this.gender, 0, 200, 336);
                    Service.getInstance().hsChar(this, this.nPoint.hpMax, this.nPoint.mpMax);
                    PlayerService.gI().sendInfoHpMp(this);
                }
            }
            return damage;
        } else {
            return 0;
        }
    }

    public void setDie(Player plAtt) {
        //xóa phù
        if (this.effectSkin.xHPKI > 1) {
            this.effectSkin.xHPKI = 1;
            Service.getInstance().point(this);
        }
        //xóa tụ skill đặc biệt
        this.playerSkill.prepareQCKK = false;
        this.playerSkill.prepareLaze = false;
        this.playerSkill.prepareTuSat = false;
        //xóa hiệu ứng skill
        this.effectSkill.removeSkillEffectWhenDie();
        //
        nPoint.setHp(0);
        nPoint.setMp(0);
        //xóa trứng
        if (this.mobMe != null) {
            this.mobMe.mobMeDie();
        }
        Service.getInstance().charDie(this);
        //add kẻ thù
        if (!this.isPet && !this.isBoss && plAtt != null && !plAtt.isPet && !plAtt.isBoss) {
            if (!plAtt.itemTime.isUseAnDanh) {
                FriendAndEnemyService.gI().addEnemy(this, plAtt);
            }
        }
        if (this.effectSkin.isSocola) {
            reward(plAtt);
        }
        if (this.zone != null && this.zone.map != null && MapService.gI().isMapMabuWar(this.zone.map.mapId)) {
            if (this.powerPoint < 20) {
                this.powerPoint = 0;
            }
            if (this.percentPowerPont < 100) {
                this.percentPowerPont = 0;
            }
        }
        //kết thúc pk
        PVPServcice.gI().finishPVP(this, PVP.TYPE_DIE);
        BlackBallWar.gI().dropBlackBall(this);
        if (isHoldNamecBall) {
            NamekBallWar.gI().dropBall(this);
            Service.getInstance().sendFlagBag(this);
        }
        if (isHoldNamecBallTranhDoat) {
            TranhNgocService.getInstance().dropBall(this, (byte) -1);
            TranhNgocService.getInstance().sendUpdateLift(this);
        }
        if (this.phanthan != null) {
            this.phanthan.setDie(plAtt);
        }
        ChungSinhBinhDang.gI().setDie_dropItem(this, plAtt);
    }

    public void reward(Player pl) {
        if (pl != null) {
            int x = this.location.x;
            int y = this.zone.map.yPhysicInTop(x, this.location.y - 24);
            ItemMap itemMap = new ItemMap(this.zone, 516, 1, x, y, pl.id);
            RewardService.gI().initBaseOptionClothes(itemMap.itemTemplate.id, itemMap.itemTemplate.type, itemMap.options);
            if (itemMap != null) {
                Service.getInstance().dropItemMap(zone, itemMap);
            }
        }
    }

    public String tenskill9(int gender) {
        switch (gender) {
            case 2:
                return "Cadic Liên hoàn chưởng";
            case 1:
                return "Ma Phong Ba";
            case 0:
                return "Super Kamejoko";
            default:
                return "";
        }
    }

    //--------------------------------------------------------------------------
    public void setClanMember() {
        if (this.clanMember != null) {
            this.clanMember.powerPoint = this.nPoint.power;
            this.clanMember.head = this.getHead();
            this.clanMember.body = this.getBody();
            this.clanMember.leg = this.getLeg();
        }
    }

    public void congExpOff() {
        long exp = this.getexp() * this.timeoff;
        Service.getInstance().addSMTN(this, (byte) 2, exp, false);
        NpcService.gI().createTutorial(this, 536, "Bạn tăng được " + exp + " sức mạnh trong thời gian " + this.timeoff + " phút tập luyện Offline");
    }

    public boolean isAdmin() {
        return this.session.isAdmin;
    }

    public void setJustRevivaled() {
        this.justRevived = true;
        this.lastTimeRevived = System.currentTimeMillis();
        this.immortal = true;
    }

    public void dispose() {
        if (escortedBoss != null) {
            escortedBoss.stopEscorting();
        }
        if (skillSpecial != null) {
            skillSpecial.dispose();
            skillSpecial = null;
        }
        if (phanthan != null) {
            phanthan.dispose();
            phanthan = null;
        }
        isDisposed = true;
    }

    public void TangDiem(Player player) {
        try {
            Item item = InventoryService.gI().findItemBagByTemp(player, 1459);
            if (item == null) {
                return;
            }
            for (ItemOption io : item.itemOptions) {
                if (io.optionTemplate.id == 240 && io.param < 10) {
                    io.param++;
                    break;
                }
            }
        } catch (Exception e) {
            System.out.println("Loi tang diem boss");
        }
    }

    public int getexp() {
        int[] expTable = {5000, 10000, 20000, 40000, 80000, 120000, 240000, 500000};
        if (this.typetrain >= 0 && this.typetrain < expTable.length) {
            return expTable[this.typetrain];
        } else {
            return 0;
        }
    }

    public boolean checkphanthan() {
        return this.phanthan != null && this.phanthan != this;
    }

    public boolean checkfusioncsbd() {
        return this.zone != null
                && this.zone.map != null
                && MapService.gI().isMapChungSinhBinhDang(this.zone.map.mapId)
                && ChungSinhBinhDang.gI().isDangChienVaThamGia(this);
    }

}
