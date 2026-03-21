package nro.models.player;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import nro.consts.ConstPlayer;
import nro.models.item.CaiTrang;
import nro.models.map.ChungSinhBinhDang.ChungSinhBinhDang;
import nro.models.mob.Mob;
import nro.models.pvp.PVP;
import nro.models.skill.Skill;
import nro.server.Manager;
import nro.server.io.Message;
import nro.services.*;
import nro.services.func.PVPServcice;
import nro.utils.SkillUtil;
import nro.utils.TimeUtil;
import nro.utils.Util;

/**
 * @author Hoàng Việt - 0857853150
 * @copyright 💖 GirlkuN 💖
 */
public class Pet extends Player {

    private static final short ARANGE_CAN_ATTACK = 200;
    private static final short ARANGE_ATT_SKILL1 = 50;
    public List<Player> enemies2 = new ArrayList<>();

    private static final short[][] PET_ID = {{285, 286, 287}, {288, 289, 290}, {282, 283, 284}, {304, 305, 303}};

    public static final byte FOLLOW = 0;
    public static final byte PROTECT = 1;
    public static final byte ATTACK = 2;
    public static final byte GOHOME = 3;
    public static final byte FUSION = 4;
    public static boolean ANGRY;

    public Player master;
    public byte status = 0;

    public byte typePet;
    public boolean isTransform;

    public long lastTimeDie;

    private boolean goingHome;

    private Mob mobAttack;
    private Player playerAttack;

    private static final int TIME_WAIT_AFTER_UNFUSION = 5000;
    private long lastTimeUnfusion;

    public byte getStatus() {
        return this.status;
    }

    @Override
    public int version() {
        return 214;
    }

    public Pet(Player master) {
        this.master = master;
        this.isPet = true;
    }

    public void changeStatus(byte status) {
        if (goingHome || master.fusion.typeFusion != 0 || (this.isDie() && status == FUSION)) {
            Service.getInstance().sendThongBao(master, "Không thể thực hiện");
            return;
        }
        if (master.zone != null && master.zone.map != null
                && MapService.gI().isMapChungSinhBinhDang(master.zone.map.mapId)
                && ChungSinhBinhDang.gI().isPlayerDangKy(master)) {
            Service.getInstance().sendThongBao(master, "Không thể thực hiện thao tác này trong Chúng Sinh Bình Đẳng");
            return;
        }
        Service.getInstance().chatJustForMe(master, this, getTextStatus(status));
        if (status == GOHOME) {
            goHome();
        } else if (status == FUSION) {
            fusion(false);
        }
        this.status = status;
    }

    public void joinMapMaster() {
        if (master != null && master.zone != null && master.zone.map != null) {
            if (!MapService.gI().isMapVS(master.zone.map.mapId)) {
                if (status != GOHOME && status != FUSION && !isDie()) {
                    this.location.x = master.location.x + Util.nextInt(-10, 10);
                    this.location.y = master.location.y;
                    MapService.gI().goToMap(this, master.zone);
                    this.zone.load_Me_To_Another(this);
                }
            }
        }
    }

    public void goHome() {
        if (this.status == GOHOME) {
            return;
        }
        goingHome = true;
        new Thread(() -> {
            try {
                Pet.this.status = Pet.ATTACK;
                Thread.sleep(2000);
            } catch (Exception e) {
            }
            MapService.gI().goToMap(this, MapService.gI().getMapCanJoin(this, master.gender + 21));
            this.zone.load_Me_To_Another(this);
            Pet.this.status = Pet.GOHOME;
            goingHome = false;
        }).start();
    }

    private String getTextStatus(byte status) {
        switch (status) {
            case FOLLOW:
                return "Ok con theo sư phụ";
            case PROTECT:
                return "Ok con sẽ bảo vệ sư phụ";
            case ATTACK:
                return "Ok sư phụ để con lo cho";
            case GOHOME:
                return "Ok con về, bibi sư phụ";
            default:
                return "";
        }
    }

    public void fusion(boolean porata) {
        if (this.isDie()) {
            Service.getInstance().sendThongBao(master, "Không thể thực hiện");
            return;
        }
        if (Util.canDoWithTime(lastTimeUnfusion, TIME_WAIT_AFTER_UNFUSION)) {
            if (porata) {
                master.fusion.typeFusion = ConstPlayer.HOP_THE_PORATA;
            } else {
                master.fusion.lastTimeFusion = System.currentTimeMillis();
                master.fusion.typeFusion = ConstPlayer.LUONG_LONG_NHAT_THE;
                ItemTimeService.gI().sendItemTime(master, master.gender == ConstPlayer.NAMEC ? 3901 : 3790, Fusion.TIME_FUSION / 1000);
            }
            this.status = FUSION;
            exitMapFusion();
            fusionEffect(master.fusion.typeFusion);
            Service.getInstance().Send_Caitrang(master);
            master.nPoint.calPoint();
            master.nPoint.setFullHpMp();
            Service.getInstance().point(master);
        } else {
            Service.getInstance().sendThongBao(this.master, "Vui lòng đợi "
                    + TimeUtil.getTimeLeft(lastTimeUnfusion, TIME_WAIT_AFTER_UNFUSION / 1000) + " nữa");
        }
    }

    public void fusion2(boolean porata) {
        if (this.isDie()) {
            Service.getInstance().sendThongBao(master, "Không thể thực hiện");
            return;
        }
        if (Util.canDoWithTime(lastTimeUnfusion, TIME_WAIT_AFTER_UNFUSION)) {
            if (porata) {
                master.fusion.typeFusion = ConstPlayer.HOP_THE_PORATA2;
            }
            this.status = FUSION;
            exitMapFusion();
            fusionEffect(master.fusion.typeFusion);
            Service.getInstance().Send_Caitrang(master);
            master.nPoint.calPoint();
            master.nPoint.setFullHpMp();
            Service.getInstance().point(master);
        } else {
            Service.getInstance().sendThongBao(this.master, "Vui lòng đợi "
                    + TimeUtil.getTimeLeft(lastTimeUnfusion, TIME_WAIT_AFTER_UNFUSION / 1000) + " nữa");
        }
    }

    public void fusion3(boolean porata) {
        if (this.isDie()) {
            Service.getInstance().sendThongBao(master, "Không thể thực hiện");
            return;
        }
        if (Util.canDoWithTime(lastTimeUnfusion, TIME_WAIT_AFTER_UNFUSION)) {
            if (porata) {
                master.fusion.typeFusion = ConstPlayer.HOP_THE_PORATA3;
            }
            this.status = FUSION;
            exitMapFusion();
            fusionEffect(master.fusion.typeFusion);
            Service.getInstance().Send_Caitrang(master);
            master.nPoint.calPoint();
            master.nPoint.setFullHpMp();
            Service.getInstance().point(master);
        } else {
            Service.getInstance().sendThongBao(this.master, "Vui lòng đợi "
                    + TimeUtil.getTimeLeft(lastTimeUnfusion, TIME_WAIT_AFTER_UNFUSION / 1000) + " nữa");
        }
    }

    public void fusion4(boolean porata) {
        if (this.isDie()) {
            Service.getInstance().sendThongBao(master, "Không thể thực hiện");
            return;
        }
        if (Util.canDoWithTime(lastTimeUnfusion, TIME_WAIT_AFTER_UNFUSION)) {
            if (porata) {
                master.fusion.typeFusion = ConstPlayer.HOP_THE_PORATA4;
            }
            this.status = FUSION;
            exitMapFusion();
            fusionEffect(master.fusion.typeFusion);
            Service.getInstance().Send_Caitrang(master);
            master.nPoint.calPoint();
            master.nPoint.setFullHpMp();
            Service.getInstance().point(master);
        } else {
            Service.getInstance().sendThongBao(this.master, "Vui lòng đợi "
                    + TimeUtil.getTimeLeft(lastTimeUnfusion, TIME_WAIT_AFTER_UNFUSION / 1000) + " nữa");
        }
    }

    public void fusion5(boolean porata) {
        if (this.isDie()) {
            Service.getInstance().sendThongBao(master, "Không thể thực hiện");
            return;
        }
        if (Util.canDoWithTime(lastTimeUnfusion, TIME_WAIT_AFTER_UNFUSION)) {
            if (porata) {
                master.fusion.typeFusion = ConstPlayer.HOP_THE_PORATA5;
            }
            this.status = FUSION;
            exitMapFusion();
            fusionEffect(master.fusion.typeFusion);
            Service.getInstance().Send_Caitrang(master);
            master.nPoint.calPoint();
            master.nPoint.setFullHpMp();
            Service.getInstance().point(master);
        } else {
            Service.getInstance().sendThongBao(this.master, "Vui lòng đợi "
                    + TimeUtil.getTimeLeft(lastTimeUnfusion, TIME_WAIT_AFTER_UNFUSION / 1000) + " nữa");
        }
    }

    public void unFusion() {
        master.fusion.typeFusion = 0;
        this.status = PROTECT;
        Service.getInstance().point(master);
        joinMapMaster();
        fusionEffect(master.fusion.typeFusion);
        Service.getInstance().Send_Caitrang(master);
        Service.getInstance().point(master);
        this.lastTimeUnfusion = System.currentTimeMillis();
    }

    private void fusionEffect(int type) {
        Message msg;
        try {
            msg = new Message(125);
            msg.writer().writeByte(type);
            msg.writer().writeInt((int) master.id);
            Service.getInstance().sendMessAllPlayerInMap(master, msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    private void exitMapFusion() {
        if (this.zone != null) {
            MapService.gI().exitMap(this);
        }
    }

    public long lastTimeMoveIdle;
    private int timeMoveIdle;
    public boolean idle;

    private void moveIdle() {
        if (status == GOHOME || status == FUSION) {
            return;
        }
        if (idle && Util.canDoWithTime(lastTimeMoveIdle, timeMoveIdle)) {
            int dir = this.location.x - master.location.x <= 0 ? -1 : 1;
            PlayerService.gI().playerMove(this, master.location.x
                    + Util.nextInt(dir == -1 ? 30 : -50, dir == -1 ? 50 : 30), master.location.y);
            lastTimeMoveIdle = System.currentTimeMillis();
            timeMoveIdle = Util.nextInt(5000, 8000);
        }
    }

    private long lastTimeMoveAtHome;
    private byte directAtHome = -1;

    public boolean canAttackPlayer(Player pl1, Player pl2) {
        if (pl2 != null && !pl1.isDie() && !pl2.isDie()) {
            if (pl1.typePk > 0 || pl2.typePk > 0) {
                return true;
            }
            if ((pl1.cFlag != 0 && pl2.cFlag != 0)
                    && (pl1.cFlag == 8 || pl2.cFlag == 8 || pl1.cFlag != pl2.cFlag)) {
                return true;
            }
            PVP pvp = PVPServcice.gI().findPvp(pl1);
            if (pvp != null) {
                if ((pvp.player1.equals(pl1) && pvp.player2.equals(pl2)
                        || (pvp.player1.equals(pl2) && pvp.player2.equals(pl1)))) {
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    @Override
    public void update() {
        try {
            super.update();
            increasePoint(); //cộng chỉ số
            updatePower(); //check mở skill...
            if (isDie()) {
                if (System.currentTimeMillis() - lastTimeDie > 50000) {
                    if (this.nPoint != null) {
                        Service.getInstance().hsChar(this, nPoint.hpMax, nPoint.mpMax);
                    }
                } else {
                    return;
                }
            }

            if (justRevived && this.zone == master.zone) {
                Service.getInstance().chatJustForMe(master, this, "Sư phụ ơi, con đây nè!");
                justRevived = false;
            }

            if (this.zone == null || this.zone != master.zone) {
                joinMapMaster();
            }
            if (this.master != null && master.isDie() || this.isDie() || effectSkill.isHaveEffectSkill()) {
                return;
            }

            moveIdle();
            switch (status) {
                case FOLLOW:
                    followMaster(60);
                    break;
                case PROTECT:
                if (useSkill3() || useSkill4() || useSkill5() || useSkill6()) {
                    break;
                }
                    boolean attackPl = false;
                    if (this.zone != null) {
                        List<Player> plist = new ArrayList<>(this.zone.getHumanoids());

                        synchronized (plist) {
                            for (Player pl : plist) {
                                if (pl != null) {
                                    synchronized (pl) {
                                        if (pl != null && this.enemies2.contains(pl) && !pl.isMiniPet) {
                                            if (pl.isDie()) {

                                            } else {
                                                int dis = ARANGE_CAN_ATTACK;
                                                int d = Util.getDistance(this, pl);
                                                if (d <= dis) {
                                                    dis = d;
                                                    if (canAttackPlayer(this, pl)) {
                                                        attackPl = true;
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (attackPl) {
                        List<Player> kethu = new ArrayList<>(this.enemies2);
                        if (kethu != null && kethu.size() > 0) {

                            Player kethugannhat = getPlayerEnimeInMap();
                            if (kethugannhat != null) {

                                int disToMob = Util.getDistance(this, kethugannhat);
                                if (disToMob <= ARANGE_ATT_SKILL1) {
                                    //đấm
                                    this.playerSkill.skillSelect = getSkill(1);
                                    if (SkillService.gI().canUseSkillWithCooldown(this)) {
                                        if (SkillService.gI().canUseSkillWithMana(this)) {
                                            PlayerService.gI().playerMove(this, kethugannhat.location.x + Util.nextInt(-40, 40), kethugannhat.location.y);

                                            SkillService.gI().useSkill(this, kethugannhat, null, null);
                                        } else {
                                            askPea();
                                        }
                                    }
                                } else {
                                    //chưởng
                                    this.playerSkill.skillSelect = getSkill(2);

                                    if (this.playerSkill.skillSelect.skillId != -1) {
                                        if (SkillService.gI().canUseSkillWithCooldown(this)) {
                                            if (SkillService.gI().canUseSkillWithMana(this)) {
                                                SkillService.gI().useSkill(this, kethugannhat, mobAttack, null);
                                            } else {
                                                askPea();
                                            }
                                        }
                                    }
                                }
                            } else {
                                idle = true;
                            }
                        }
                    } else {
                        mobAttack = findMobAttack();
                        if (mobAttack != null) {
                            int disToMob = Util.getDistance(this, mobAttack);
                            if (disToMob <= ARANGE_ATT_SKILL1) {
                                //đấm
                                this.playerSkill.skillSelect = getSkill(1);
                                if (SkillService.gI().canUseSkillWithCooldown(this)) {
                                    if (SkillService.gI().canUseSkillWithMana(this)) {
                                        PlayerService.gI().playerMove(this, mobAttack.location.x + Util.nextInt(-20, 20), mobAttack.location.y);
                                        SkillService.gI().useSkill(this, playerAttack, mobAttack, null);
                                    } else {
                                        askPea();
                                    }
                                }
                            } else {
                                //chưởng
                                this.playerSkill.skillSelect = getSkill(2);

                                if (this.playerSkill.skillSelect.skillId != -1) {
                                    if (SkillService.gI().canUseSkillWithCooldown(this)) {
                                        if (SkillService.gI().canUseSkillWithMana(this)) {

                                            SkillService.gI().useSkill(this, playerAttack, mobAttack, null);
                                        } else {
                                            askPea();
                                        }
                                    }
                                }
                            }

                        } else {
                            idle = true;
                        }
                    }
                    break;
                case ATTACK:
                    if (useSkill3() || useSkill4()) {
                        break;
                    }
                    boolean attackPl2 = false;
                    if (this.zone != null) {
                        List<Player> plist2 = new ArrayList<>(this.zone.getHumanoids());
                        synchronized (plist2) {
                            for (Player pl : plist2) {
                                if (pl != null) {
                                    synchronized (pl) {
                                        if (pl != null && this.enemies2.contains(pl) && !pl.isMiniPet) {
                                            if (pl.isDie()) {

                                            } else {
                                                //  followMaster(60);
                                                int dis = ARANGE_CAN_ATTACK;
                                                int d = Util.getDistance(this, pl);
                                                if (d <= dis) {
                                                    dis = d;
                                                    if (SkillService.gI().canAttackPlayer(this, pl)) {
                                                        attackPl2 = true;
                                                    }

                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    if (attackPl2) {
                        List<Player> kethu = new ArrayList<>(this.enemies2);
                        if (kethu != null) {
                            Player kethugannhat = getPlayerEnimeInMap();
                            if (kethugannhat != null) {
                                int disToMob = 0;
                                if (mobAttack != null) {
                                    disToMob = Util.getDistance(this, mobAttack);
                                }
                                if (disToMob <= ARANGE_ATT_SKILL1) {
                                    this.playerSkill.skillSelect = getSkill(1);
                                    if (SkillService.gI().canUseSkillWithCooldown(this)) {
                                        if (SkillService.gI().canUseSkillWithMana(this)) {
                                            PlayerService.gI().playerMove(this, kethugannhat.location.x + Util.nextInt(-20, 20), kethugannhat.location.y);
                                            SkillService.gI().useSkill(this, kethugannhat, null, null);
                                        } else {
                                            askPea();
                                        }
                                    }
                                } else {
                                    this.playerSkill.skillSelect = getSkill(2);
                                    if (this.playerSkill.skillSelect.skillId != -1) {
                                        if (SkillService.gI().canUseSkillWithMana(this)) {
                                            SkillService.gI().useSkill(this, kethugannhat, null, null);
                                        }
                                    } else {
                                        this.playerSkill.skillSelect = getSkill(1);
                                        if (SkillService.gI().canUseSkillWithCooldown(this)) {
                                            if (SkillService.gI().canUseSkillWithMana(this)) {
                                                PlayerService.gI().playerMove(this, kethugannhat.location.x + Util.nextInt(-20, 20), kethugannhat.location.y);
                                                SkillService.gI().useSkill(this, kethugannhat, null, null);
                                            } else {
                                                askPea();
                                            }
                                        }
                                    }
                                }
                            } else {
                                idle = true;
                            }
                        }
                    } else {
                        mobAttack = findMobAttack();
                        if (mobAttack != null) {
                            int disToMob = Util.getDistance(this, mobAttack);
                            if (disToMob <= ARANGE_ATT_SKILL1) {
                                this.playerSkill.skillSelect = getSkill(1);
                                if (SkillService.gI().canUseSkillWithCooldown(this)) {
                                    if (SkillService.gI().canUseSkillWithMana(this)) {
                                        PlayerService.gI().playerMove(this, mobAttack.location.x + Util.nextInt(-20, 20), mobAttack.location.y);
                                        SkillService.gI().useSkill(this, playerAttack, mobAttack, null);
                                    } else {
                                        askPea();
                                    }
                                }
                            } else {
                                this.playerSkill.skillSelect = getSkill(2);
                                if (this.playerSkill.skillSelect.skillId != -1) {
                                    if (SkillService.gI().canUseSkillWithMana(this)) {
                                        SkillService.gI().useSkill(this, playerAttack, mobAttack, null);
                                    }
                                } else {
                                    this.playerSkill.skillSelect = getSkill(1);
                                    if (SkillService.gI().canUseSkillWithCooldown(this)) {
                                        if (SkillService.gI().canUseSkillWithMana(this)) {
                                            PlayerService.gI().playerMove(this, mobAttack.location.x + Util.nextInt(-20, 20), mobAttack.location.y);
                                            SkillService.gI().useSkill(this, playerAttack, mobAttack, null);
                                        } else {
                                            askPea();
                                        }
                                    }
                                }
                            }

                        } else {
                            idle = true;
                        }
                    }
                    break;
                case GOHOME:
                    if (this.zone != null && (this.zone.map.mapId == 21 || this.zone.map.mapId == 22 || this.zone.map.mapId == 23)) {
                        if (System.currentTimeMillis() - lastTimeMoveAtHome <= 5000) {
                            return;
                        } else {
                            if (this.zone.map.mapId == 21) {
                                if (directAtHome == -1) {

                                    PlayerService.gI().playerMove(this, 250, 336);
                                    directAtHome = 1;
                                } else {
                                    PlayerService.gI().playerMove(this, 200, 336);
                                    directAtHome = -1;
                                }
                            } else if (this.zone.map.mapId == 22) {
                                if (directAtHome == -1) {
                                    PlayerService.gI().playerMove(this, 500, 336);
                                    directAtHome = 1;
                                } else {
                                    PlayerService.gI().playerMove(this, 452, 336);
                                    directAtHome = -1;
                                }
                            } else if (this.zone.map.mapId == 23) {
                                if (directAtHome == -1) {
                                    PlayerService.gI().playerMove(this, 250, 336);
                                    directAtHome = 1;
                                } else {
                                    PlayerService.gI().playerMove(this, 200, 336);
                                    directAtHome = -1;
                                }
                            }
                            Service.getInstance().chatJustForMe(master, this, "Bibi sư phụ !");
                            lastTimeMoveAtHome = System.currentTimeMillis();
                        }
                    }
                    break;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private long lastTimeAskPea;

    public void askPea() {
        if (this.typePet >= 1 && master.charms.tdDeTuMabu > System.currentTimeMillis()) {
            InventoryService.gI().eatPea(master);
        } else if (Util.canDoWithTime(lastTimeAskPea, 10000)) {
            Service.getInstance().chatJustForMe(master, this, "Sư phụ ơi cho con đậu thần");
            lastTimeAskPea = System.currentTimeMillis();
        }
    }

    private int countTTNL;

    private boolean useSkill3() {
        try {
            playerSkill.skillSelect = getSkill(3);
            if (playerSkill.skillSelect.skillId == -1) {
                return false;
            }
            switch (this.playerSkill.skillSelect.template.id) {
                case Skill.THAI_DUONG_HA_SAN:
                    if (SkillService.gI().canUseSkillWithCooldown(this) && SkillService.gI().canUseSkillWithMana(this)) {
                        SkillService.gI().useSkill(this, null, null, null);
                        Service.getInstance().chatJustForMe(master, this, "Thái dương hạ san");
                        return true;
                    }
                    return false;
                case Skill.TAI_TAO_NANG_LUONG:
                    if (this.effectSkill.isCharging && this.countTTNL < Util.nextInt(3, 5)) {
                        this.countTTNL++;
                        return true;
                    }
                    if (SkillService.gI().canUseSkillWithCooldown(this) && SkillService.gI().canUseSkillWithMana(this)
                            && (this.nPoint.getCurrPercentHP() <= 20 || this.nPoint.getCurrPercentMP() <= 20)) {
                        SkillService.gI().useSkill(this, null, null, null);
                        this.countTTNL = 0;
                        return true;
                    }
                    return false;
                case Skill.KAIOKEN:
                    if (SkillService.gI().canUseSkillWithCooldown(this) && SkillService.gI().canUseSkillWithMana(this)) {
                        mobAttack = this.findMobAttack();
                        if (mobAttack == null) {
                            return false;
                        }
                        int dis = Util.getDistance(this, mobAttack);
                        if (dis > ARANGE_ATT_SKILL1) {
                            PlayerService.gI().playerMove(this, mobAttack.location.x, mobAttack.location.y);
                        } else {
                            if (SkillService.gI().canUseSkillWithCooldown(this) && SkillService.gI().canUseSkillWithMana(this)) {
                                PlayerService.gI().playerMove(this, mobAttack.location.x + Util.nextInt(-20, 20), mobAttack.location.y);
                            }
                        }
                        SkillService.gI().useSkill(this, playerAttack, mobAttack, null);
                        getSkill(1).lastTimeUseThisSkill = System.currentTimeMillis();
                        return true;
                    }
                    return false;
                default:
                    return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    private boolean useSkill4() {
        try {
            this.playerSkill.skillSelect = getSkill(4);
            if (this.playerSkill.skillSelect.skillId == -1) {
                return false;
            }
            switch (this.playerSkill.skillSelect.template.id) {
                case Skill.BIEN_KHI:
                    if (!this.effectSkill.isMonkey && SkillService.gI().canUseSkillWithCooldown(this) && SkillService.gI().canUseSkillWithMana(this)) {
                        SkillService.gI().useSkill(this, null, null, null);
                        return true;
                    }
                    return false;
                case Skill.KHIEN_NANG_LUONG:
                    if (!this.effectSkill.isShielding && SkillService.gI().canUseSkillWithCooldown(this) && SkillService.gI().canUseSkillWithMana(this)) {
                        SkillService.gI().useSkill(this, null, null, null);
                        return true;
                    }
                    return false;
                case Skill.DE_TRUNG:
                    if (this.mobMe == null && SkillService.gI().canUseSkillWithCooldown(this) && SkillService.gI().canUseSkillWithMana(this)) {
                        SkillService.gI().useSkill(this, null, null, null);
                        return true;
                    }
                    return false;
                default:
                    return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
    private boolean useSkill5() {
    try {
        this.playerSkill.skillSelect = getSkill(5);
        if (this.playerSkill.skillSelect.skillId == -1) {
            return false;
        }

        if (SkillService.gI().canUseSkillWithCooldown(this)
                && SkillService.gI().canUseSkillWithMana(this)) {

            SkillService.gI().useSkill(this, playerAttack, mobAttack, null);
            return true;
        }

        return false;
    } catch (Exception e) {
        return false;
    }
}
private boolean useSkill6() {
    try {
        this.playerSkill.skillSelect = getSkill(6);
        if (this.playerSkill.skillSelect.skillId == -1) {
            return false;
        }

        if (SkillService.gI().canUseSkillWithCooldown(this)
                && SkillService.gI().canUseSkillWithMana(this)) {

            SkillService.gI().useSkill(this, playerAttack, mobAttack, null);
            return true;
        }

        return false;
    } catch (Exception e) {
        return false;
    }
}

    private long lastTimeIncreasePoint;

    private void increasePoint() {
        if (this.nPoint != null && Util.canDoWithTime(lastTimeIncreasePoint, 10)) {
            if (status != FUSION) {
                if (Util.isTrue(1, 100)) {
                    this.nPoint.increasePoint((byte) 3, (short) 1);
                } else {
                    if (this.nPoint.tiemNang > 2000000000) {
                        this.nPoint.increasePoint((byte) Util.nextInt(0, 2), (short) 10);
                    } else {
                        this.nPoint.increasePoint((byte) Util.nextInt(0, 2), (short) 1);
                    }
                }
                lastTimeIncreasePoint = System.currentTimeMillis();
            }
        }
    }

    public void followMaster() {
        if (this.isDie() || effectSkill.isHaveEffectSkill()) {
            return;
        }
        switch (this.status) {
            case ATTACK:
                if (ANGRY) {
                    followMaster(80);
                } else {
                    if ((mobAttack != null && Util.getDistance(this, master) <= 500)) {
                        break;
                    }
                }
            case FOLLOW:
            case PROTECT:
                followMaster(60);
                break;
        }
    }

    public Player getPlayerEnimeInMap() {
        int dis = ARANGE_CAN_ATTACK;
        Player mobAtt = null;
        for (Player pl : zone.getHumanoids()) {
            if (pl.isDie()) {
                continue;
            }
            if (enemies2.contains(pl)) {
                int d = Util.getDistance(this, pl);
                if (d <= dis) {
                    dis = d;
                    mobAtt = pl;
                }
            }
        }
        return mobAtt;
    }

    private void followMaster(int dis) {
        int mX = master.location.x;
        int mY = master.location.y;
        int disX = this.location.x - mX;
        if (Math.sqrt(Math.pow(mX - this.location.x, 2) + Math.pow(mY - this.location.y, 2)) >= dis) {
            if (disX < 0) {
                this.location.x = mX - Util.nextInt(0, dis);
            } else {
                this.location.x = mX + Util.nextInt(0, dis);
            }
            this.location.y = mY;
            PlayerService.gI().playerMove(this, this.location.x, this.location.y);
        }
    }

    public short getAvatar() {
        if (this.typePet == 1) {
            return 2057;
        } else if (this.typePet == 2) {  //buten 30%
            return 1726;
            
        } else if (this.typePet == 3) {  //Beerus 60%
            return 1631;
            
        } else if (this.typePet == 4) {  //zeno  90%
            return 1427;
            
        } else if (this.typePet == 5) {  //Siêu Thần 170%
            return 2063;
            
        } else if (this.typePet == 6) {  // Obito Lục Đạo 120%
            return 1243;
            
        } else if (this.typePet == 7) {  
            return 1631;
        } else if (this.typePet == 8) { 
            return 1258;
        } else if (this.typePet == 9) { 
            return 2063;
        } else if (this.typePet == 10) {
            return 1427;
        } else if (this.typePet == 11) {
            return 1783;
        } else {
            return PET_ID[3][this.gender];
        }
    }

    @Override
    public short getHead() {
        if (effectSkill.isMonkey) {
            return (short) ConstPlayer.HEADMONKEY[effectSkill.levelMonkey - 1];
        } else if (effectSkill.isSocola || effectSkin.isSocola) {
            return 412;
        } else if (effectSkin != null && effectSkin.isHoaDa) {
            return 454;
        } else if (this.typePet == 1 && !this.isTransform) {
            return 2057;
            
        } else if (this.typePet == 2 && !this.isTransform) {  //buten 30%
            return 1726;
            
        } else if (this.typePet == 3 && !this.isTransform) {  //Beerus 60%
            return 1631;
            
        } else if (this.typePet == 4 && !this.isTransform) {  //zeno  90%
            return 1427;
            
        } else if (this.typePet == 5 && !this.isTransform) {  //Siêu Thần 170%
            return 2063;
            
        } else if (this.typePet == 6 && !this.isTransform) {  // Obito Lục Đạo 120%
            return 1243;
            
        } else if (this.typePet == 7 && !this.isTransform) {
            return 1631;
        } else if (this.typePet == 8 && !this.isTransform) { 
            return 1258;
        } else if (this.typePet == 9 && !this.isTransform) { 
            return 2063;
        } else if (this.typePet == 10 && !this.isTransform) {
            return 1427;
        } else if (this.typePet == 11 && !this.isTransform) { 
            return 1783;
        } else if (effectSkill.isBinh) {
            return 1321;
        } else if (inventory.itemsBody.get(5).isNotNullItem()) {
            CaiTrang ct = Manager.gI().getCaiTrangByItemId(inventory.itemsBody.get(5).template.id);
            if (ct != null) {
                return (short) ((short) ct.getID()[0] != -1 ? ct.getID()[0] : inventory.itemsBody.get(5).template.part);
            }
        }
        if (this.nPoint.power < 1500000) {
            return PET_ID[this.gender][0];
        } else {
            return PET_ID[3][this.gender];
        }
    }

    @Override
    public short getBody() {
        if (effectSkill.isMonkey) {
            return 193;
        } else if (effectSkill.isSocola || effectSkin.isSocola) {
            return 413;
        } else if (effectSkin != null && effectSkin.isHoaDa) {
            return 455;
        } else if (this.typePet == 1 && !this.isTransform) {
            return 2058;
            
        } else if (this.typePet == 2 && !this.isTransform) {  //buten 30%
            return 1727;
            
        } else if (this.typePet == 3 && !this.isTransform) {  //Beerus 60%
            return 1632;
            
        } else if (this.typePet == 4 && !this.isTransform) {  //zeno  90%
            return 1428;
            
        } else if (this.typePet == 5 && !this.isTransform) {  //Siêu Thần 170%
            return 2064;
            
        } else if (this.typePet == 6 && !this.isTransform) {  // Obito Lục Đạo 120%
            return 1244;
            
        } else if (this.typePet == 7 && !this.isTransform) {
            return 1632;
        } else if (this.typePet == 8 && !this.isTransform) {  
            return 1259;
        } else if (this.typePet == 9 && !this.isTransform) { 
            return 2064;
        } else if (this.typePet == 10 && !this.isTransform) { 
            return 1428;
        } else if (this.typePet == 11 && !this.isTransform) { 
            return 1784;
        } else if (effectSkill.isBinh) {
            return 1322;
        } else if (inventory.itemsBody.get(5).isNotNullItem()) {
            CaiTrang ct = Manager.gI().getCaiTrangByItemId(inventory.itemsBody.get(5).template.id);
            if (ct != null && ct.getID()[1] != -1) {
                return (short) ct.getID()[1];
            }
        }
        if (inventory.itemsBody.get(0).isNotNullItem()) {
            return inventory.itemsBody.get(0).template.part;
        }
        if (this.nPoint.power < 1500000) {
            return PET_ID[this.gender][1];
        } else {
            return (short) (gender == ConstPlayer.NAMEC ? 59 : 57);
        }
    }

    @Override
    public short getLeg() {
        if (effectSkill.isMonkey) {
            return 194;
        } else if (effectSkill.isSocola || effectSkin.isSocola) {
            return 414;
        } else if (effectSkin != null && effectSkin.isHoaDa) {
            return 456;
        } else if (this.typePet == 1 && !this.isTransform) {
            return 2059;
            
        } else if (this.typePet == 2 && !this.isTransform) {  //buten 30%
            return 1728;
            
        } else if (this.typePet == 3 && !this.isTransform) {  //Beerus 60%
            return 1633;
            
        } else if (this.typePet == 4 && !this.isTransform) {  //zeno  90%
            return 1429;
            
        } else if (this.typePet == 5 && !this.isTransform) {  //Siêu Thần 170%
            return 2065;
            
        } else if (this.typePet == 6 && !this.isTransform) {  // Obito Lục Đạo 120%
            return 1245; 
            
        } else if (this.typePet == 7 && !this.isTransform) {  
            return 1633;
        } else if (this.typePet == 8 && !this.isTransform) { 
            return 1260;
        } else if (this.typePet == 9 && !this.isTransform) { 
            return 2065;
        } else if (this.typePet == 10 && !this.isTransform) { 
            return 1429;
        } else if (this.typePet == 11 && !this.isTransform) { 
            return 1785;
        } else if (effectSkill.isBinh) {
            return 1323;
        } else if (inventory.itemsBody.get(5).isNotNullItem()) {
            CaiTrang ct = Manager.gI().getCaiTrangByItemId(inventory.itemsBody.get(5).template.id);
            if (ct != null && ct.getID()[2] != -1) {
                return (short) ct.getID()[2];
            }
        }
        if (inventory.itemsBody.get(1).isNotNullItem()) {
            return inventory.itemsBody.get(1).template.part;
        }

        if (this.nPoint.power < 1500000) {
            return PET_ID[this.gender][2];
        } else {
            return (short) (gender == ConstPlayer.NAMEC ? 60 : 58);
        }
    }

    private Mob findMobAttack() {
        int dis = ARANGE_CAN_ATTACK;
        Mob mobAtt = null;
        for (Mob mob : zone.mobs) {
            if (mob.isDie()) {
                continue;
            }
            int d = Util.getDistance(this, mob);
            if (d <= dis) {
                dis = d;
                mobAtt = mob;
            }
        }
        return mobAtt;
    }

    switch (this.playerSkill.getSizeSkill()) {
        case 1:
            if (this.nPoint.power >= 150_000_000) {
                openSkill2();
            }
            break;
        case 2:
            if (this.nPoint.power >= 1_500_000_000) {
                openSkill3();
            }
            break;
        case 3:
            if (this.nPoint.power >= 20_000_000_000L) {
                openSkill4();
            }
            break;
        case 4:
            if (this.nPoint.power >= 50_000_000_000L) {
                openSkill5();
            }
            break;
        case 5:
            if (this.nPoint.power >= 100_000_000_000L) {
                openSkill6();
            }
            break;
    }

    public void openSkill2() {
        Skill skill = null;
        int tiLeKame = 30;
        int tiLeMasenko = 25;
        int tiLeAntomic = 45;

        int rd = Util.nextInt(1, 100);
        if (rd <= tiLeKame) {
            skill = SkillUtil.createSkill(Skill.KAMEJOKO, 1);
        } else if (rd <= tiLeKame + tiLeMasenko) {
            skill = SkillUtil.createSkill(Skill.MASENKO, 1);
        } else if (rd <= tiLeKame + tiLeMasenko + tiLeAntomic) {
            skill = SkillUtil.createSkill(Skill.ANTOMIC, 1);
        }
        skill.coolDown = 1000;
        this.playerSkill.skills.set(1, skill);
    }

    public void openSkill3() {
        Skill skill = null;
        int tiLeTDHS = 30;
        int tiLeTTNL = 50;
        int tiLeKOK = 20;

        int rd = Util.nextInt(1, 100);
        if (rd <= tiLeTDHS) {
            skill = SkillUtil.createSkill(Skill.THAI_DUONG_HA_SAN, 1);
        } else if (rd <= tiLeTDHS + tiLeTTNL) {
            skill = SkillUtil.createSkill(Skill.TAI_TAO_NANG_LUONG, 1);
        } else if (rd <= tiLeTDHS + tiLeTTNL + tiLeKOK) {
            skill = SkillUtil.createSkill(Skill.KAIOKEN, 1);
        }
        this.playerSkill.skills.set(2, skill);
    }

    public void openSkill4() {
        Skill skill = null;
        int tiLeBienKhi = 30;
        int tiLeDeTrung = 40;
        int tiLeKNL = 20;

        int rd = Util.nextInt(1, 100);
        if (rd <= tiLeBienKhi) {
            skill = SkillUtil.createSkill(Skill.BIEN_KHI, 1);
        } else if (rd <= tiLeBienKhi + tiLeDeTrung) {
            skill = SkillUtil.createSkill(Skill.DE_TRUNG, 1);
        } else if (rd <= tiLeBienKhi + tiLeDeTrung + tiLeKNL) {
            skill = SkillUtil.createSkill(Skill.KHIEN_NANG_LUONG, 1);
        }
        this.playerSkill.skills.set(3, skill);
    }

    public void openSkill5() {
    Skill skill = null;

    int rd = Util.nextInt(1, 100);

    if (rd <= 33) {
        skill = SkillUtil.createSkill(Skill.LIEN_HOAN_CHUONG, 1);
    } else if (rd <= 66) {
        skill = SkillUtil.createSkill(Skill.MAKANKOSAPPO, 1);
    } else {
        skill = SkillUtil.createSkill(Skill.SUPER_KAME, 1);
    }

    if (this.playerSkill.skills.size() > 4) {
        this.playerSkill.skills.set(4, skill);
    }
}
public void openSkill6() {
    Skill skill = null;

    int rd = Util.nextInt(1, 100);

    if (rd <= 50) {
        skill = SkillUtil.createSkill(Skill.HUYT_SAO, 1);
    } else {
        skill = SkillUtil.createSkill(Skill.QUA_CAU_KENH_KHI, 1);
    }

    if (this.playerSkill.skills.size() > 5) {
        this.playerSkill.skills.set(5, skill);
    }
}

    private Skill getSkill(int indexSkill) {
        return this.playerSkill.skills.get(indexSkill - 1);
    }

    public void transform() {
        if (this.typePet == 1) {
            this.isTransform = !this.isTransform;
            Service.getInstance().Send_Caitrang(this);
            Service.getInstance().chat(this, "Bát Giới Biến Hình....");
        } else if (this.typePet == 2) {
            this.isTransform = !this.isTransform;
            Service.getInstance().Send_Caitrang(this);
            Service.getInstance().chat(this, "Buten Xinh Đẹp Biến Hình....");
        }
        if (this.typePet == 3) {
            this.isTransform = !this.isTransform;
            Service.getInstance().Send_Caitrang(this);
            Service.getInstance().chat(this, "Thần Hủy Diệt Biến Hình  ....");
        }
        if (this.typePet == 4) {
            this.isTransform = !this.isTransform;
            Service.getInstance().Send_Caitrang(this);
            Service.getInstance().chat(this, "Thần Zeno Biến Hình ....");
        }
        if (this.typePet == 5) {
            this.isTransform = !this.isTransform;
            Service.getInstance().Send_Caitrang(this);
            Service.getInstance().chat(this, "Chiến Thần Biến Hình....");
        }
        if (this.typePet == 6) {
            this.isTransform = !this.isTransform;
            Service.getInstance().Send_Caitrang(this);
            Service.getInstance().chat(this, "Obito Lục Đạo Hồi Sinh....");
        }
        if (this.typePet == 7) {
            this.isTransform = !this.isTransform;
            Service.getInstance().Send_Caitrang(this);
            Service.getInstance().chat(this, "Thần Hủy Diệt Biến Hình....");
        }
        if (this.typePet == 8) {
            this.isTransform = !this.isTransform;
            Service.getInstance().Send_Caitrang(this);
            Service.getInstance().chat(this, "Gojo Biến Hình....");
        }
        if (this.typePet == 9) {
            this.isTransform = !this.isTransform;
            Service.getInstance().Send_Caitrang(this);
            Service.getInstance().chat(this, "Goku Premium Biến Hình....");
        }
        if (this.typePet == 10) {
            this.isTransform = !this.isTransform;
            Service.getInstance().Send_Caitrang(this);
            Service.getInstance().chat(this, "Thần Zeno Biến Hình....");
        }
        if (this.typePet == 11) {
            this.isTransform = !this.isTransform;
            Service.getInstance().Send_Caitrang(this);
            Service.getInstance().chat(this, "Khỉ Đại Ka Biến Hình....");
        }
    }

    public void angry(Player plAtt) {
        ANGRY = true;
        if (plAtt != null) {
            this.playerAttack = plAtt;
            Service.getInstance().chatJustForMe(master, this, "Mi làm ta nổi giận rồi " + playerAttack.name
                    .replace("$", ""));
        }
    }
}
