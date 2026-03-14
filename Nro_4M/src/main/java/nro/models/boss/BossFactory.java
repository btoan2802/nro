package nro.models.boss;

import nro.consts.ConstEvent;
import nro.consts.ConstMap;
import nro.models.boss.BossGokuSky.*;
import nro.models.boss.BossHuyDiet.*;
import nro.models.boss.Doremon.*;
import nro.models.boss.bill.*;
import nro.models.boss.tramtau.*;
import nro.models.boss.bosstuonglai.*;
import nro.models.boss.broly.*;
import nro.models.boss.cell.*;
import nro.models.boss.chill.*;
import nro.models.boss.cold.*;
import nro.models.boss.event.HoaHong;
import nro.models.boss.event.Qilin;
import nro.models.boss.event.SantaClaus;
import nro.models.boss.fide.*;
import nro.models.boss.mabu_war.*;
import nro.models.boss.nappa.*;
import nro.models.boss.robotsatthu.*;
import nro.models.boss.tieudoisatthu.*;
import nro.models.boss.NguHanhSon.*;
import nro.models.boss.NgucTu.*;
import nro.models.boss.traidat.*;
import nro.models.boss.bossTheGioi.*;
import nro.models.boss.BossTet.*;
import nro.models.boss.BossTuTien.*;
import nro.models.boss.BossThanLinh.*;
import nro.models.boss.VoDaiVuTru.ThanMau;
import nro.models.boss.BossThienSu.*;
import nro.models.boss.BossYardrat.TapSuLoai1;
import nro.models.boss.BossYardrat.TapSuLoai2;
import nro.models.boss.BossYardrat.TapSuLoai3;
import nro.models.boss.BossYardrat.TapSuLoai4;
import nro.models.boss.SuKienHe.Frieren;
import nro.models.boss.SuKienHe.King_Kong_Sanh_Dieu;
import nro.models.boss.SuKienHe.Majin_Android_21;
import nro.models.boss.SuKienSanQuy.Inosuke;
import nro.models.boss.SuKienSanQuy.InosukeHashibira;
import nro.models.boss.SuKienSanQuy.Nezuko;
import nro.models.boss.SuKienSanQuy.Tanjiro;
import nro.models.boss.SuKienSanQuy.Zenitsu;
import nro.models.boss.ThanBlue.*;
import nro.models.boss.ThanPink.*;
import nro.models.boss.ThanRed.*;
import nro.models.map.Map;
import nro.models.map.Zone;
import nro.models.map.mabu.MabuWar;
import nro.models.map.mabu.MabuWar14h;
import nro.server.Manager;
import nro.services.MapService;
import org.apache.log4j.Logger;

import nro.utils.Util;

/**
 * @author Hoàng Việt - 0857853150
 * @copyright 💖 NROLOVE 💖
 */
public class BossFactory {

    //id boss
    public static final int BROLY_DETU = -2222;
    public static final byte BROLY = -1;
    public static final byte SUPER_BROLY = -2;
    public static final byte TRUNG_UY_TRANG = -3;
    public static final byte TRUNG_UY_XANH_LO = -4;
    public static final byte TRUNG_UD_THEP = -5;
    public static final byte NINJA_AO_TIM = -6;
    public static final byte NINJA_AO_TIM_FAKE_1 = -7;
    public static final byte NINJA_AO_TIM_FAKE_2 = -8;
    public static final byte NINJA_AO_TIM_FAKE_3 = -9;
    public static final byte NINJA_AO_TIM_FAKE_4 = -10;
    public static final byte NINJA_AO_TIM_FAKE_5 = -11;
    public static final byte NINJA_AO_TIM_FAKE_6 = -12;
    public static final byte ROBOT_VE_SI_1 = -13;
    public static final byte ROBOT_VE_SI_2 = -14;
    public static final byte ROBOT_VE_SI_3 = -15;
    public static final byte ROBOT_VE_SI_4 = -16;
    public static final byte XEN_BO_HUNG_1 = -17;
    public static final byte XEN_BO_HUNG_2 = -18;
    public static final byte XEN_BO_HUNG_HOAN_THIEN = -19;
    public static final byte XEN_BO_HUNG = -20;
    public static final byte XEN_CON = -21;
    public static final byte SIEU_BO_HUNG = -22;
    public static final byte KUKU = -23;
    public static final byte MAP_DAU_DINH = -24;
    public static final byte RAMBO = -25;
    public static final byte COOLER = -26;
    public static final byte COOLER2 = -27;
    public static final byte SO4 = -28;
    public static final byte SO3 = -29;
    public static final byte SO2 = -30;
    public static final byte SO1 = -31;
    public static final byte TIEU_DOI_TRUONG = -32;
    public static final byte FIDE_DAI_CA_1 = -33;
    public static final byte FIDE_DAI_CA_2 = -34;
    public static final byte FIDE_DAI_CA_3 = -35;
    public static final byte ANDROID_19 = -36;
    public static final byte ANDROID_20 = -37;
    public static final byte ANDROID_13 = -38;
    public static final byte ANDROID_14 = -39;
    public static final byte ANDROID_15 = -40;
    public static final byte PIC = -41;
    public static final byte POC = -42;
    public static final byte KINGKONG = -43;
    public static final byte SUPER_BROLY_RED = -44;
    public static final byte LUFFY = -45;
    public static final byte ZORO = -46;
    public static final byte SANJI = -47;
    public static final byte USOPP = -48;
    public static final byte FRANKY = -49;
    public static final byte BROOK = -50;
    public static final byte NAMI = -51;
    public static final byte CHOPPER = -52;
    public static final byte ROBIN = -53;
    public static final byte WHIS = -54;
    public static final byte BILL = -55;
    public static final byte CHILL = -56;
    public static final byte CHILL2 = -57;
    public static final byte BULMA = -58;
    public static final byte POCTHO = -59;
    public static final byte CHICHITHO = -60;
    public static final byte BLACKGOKU = -61;
    public static final byte SUPERBLACKGOKU = -62;
    public static final byte SANTA_CLAUS = -63;
    public static final byte MABU_MAP = -64;
    public static final byte SUPER_BU = -65;
    public static final byte BU_TENK = -66;
    public static final byte DRABULA_TANG1 = -67;
    public static final byte BUIBUI_TANG2 = -68;
    public static final byte BUIBUI_TANG3 = -69;
    public static final byte YACON_TANG4 = -70;
    public static final byte DRABULA_TANG5 = -71;
    public static final byte GOKU_TANG5 = -72;
    public static final byte CADIC_TANG5 = -73;
    public static final byte DRABULA_TANG6 = -74;
    public static final byte XEN_MAX = -75;
    public static final byte HOA_HONG = -76;
    public static final byte SOI_HEC_QUYN = -77;
    public static final byte O_DO = -78;
    public static final byte XINBATO = -79;
    public static final byte CHA_PA = -80;
    public static final byte PON_PUT = -81;
    public static final byte CHAN_XU = -82;
    public static final byte TAU_PAY_PAY = -83;
    public static final byte YAMCHA = -84;
    public static final byte JACKY_CHUN = -85;
    public static final byte THIEN_XIN_HANG = -86;
    public static final byte LIU_LIU = -87;
    public static final byte THIEN_XIN_HANG_CLONE = -88;
    public static final byte THIEN_XIN_HANG_CLONE1 = -89;
    public static final byte THIEN_XIN_HANG_CLONE2 = -90;
    public static final byte THIEN_XIN_HANG_CLONE3 = -91;
    public static final byte QILIN = -92;
    public static final byte NGO_KHONG = -93;
    public static final byte BAT_GIOI = -94;
    public static final byte FIDEGOLD = -95;
    public static final byte CUMBER = -96;
    public static final byte CUMBER2 = -97;
    public static final byte SUPER_BLACK_ROSE = -98;
    public static final byte ZAMAS_TOI_THUONG = -99;
    public static final byte WHIS_DETU = -100;
    public static final byte ZENO = -101;
    public static final byte RONG_DEN = -102;
    public static final byte GOKU_SUPER = -103;
    public static final byte BONG_BANG = -104;
    public static final byte SOI_BASIL = -105;
    public static final byte VADOS = -106;
    public static final byte CHAMPA = -107;
    public static final byte ITACHI = -108;
    public static final byte ZAMAS_ZOMBIE = -109;
    public static final byte LUFFY_THAN_NIKA = -110;
    public static final byte KID_BU = -111;
    public static final byte BU_HAN = -112;
    public static final byte LUFFY_GEAR5 = -113;
    public static final byte KAIDO = -114;
    public static final byte NGUYET_THAN = -115;
    public static final byte MIHAWK = -116;
    public static final byte JIREN = -117;
    public static final byte TOPPO = -118;
    public static final byte NHAT_THAN = -119;
    public static final int NHAT_VI = -120;
    public static final int NHI_VI = -121;
    public static final int TAM_VI = -122;
    public static final int TU_VI = -123;
    public static final int NGU_VI = -124;
    public static final int LUC_VI = -125;
    public static final int THAT_VI = -126;
    public static final int BAT_VI = -127;
    public static final int CUU_VI = -128;
    public static final int THAP_VI = -129;
    public static final int DOREMON = -130;
    public static final int NOBITA = -131;
    public static final int XUKA = -132;
    public static final int CHAIEN = -133;
    public static final int XEKO = -134;
    public static final int GOKU_THAN_TAI = -135;
    public static final int PICOLO_THAN_TAI = -136;
    public static final int VEGETA_THAN_TAI = -137;
    public static final int JIREN_HOAN_MY = -138;
    public static final int SON_TINH = -139;
    public static final int THUY_TINH = -140;
    public static final int VUA_HUNG = -141;
    public static final int RONG_1SAO = -142;
    public static final int RONG_2SAO = -143;
    public static final int RONG_3SAO = -144;
    public static final int RONG_4SAO = -145;
    public static final int RONG_5SAO = -146;
    public static final int RONG_6SAO = -147;
    public static final int RONG_7SAO = -148;
    public static final int ADMIN_1 = -149;
    public static final int ADMIN_2 = -150;
    public static final int HUY_DIET1 = -161;
    public static final int HUY_DIET2 = -162;
    public static final int HUY_DIET3 = -163;
    public static final int HUY_DIET4 = -164;
    public static final int HUY_DIET5 = -165;
    public static final int THAN_LINH1 = -171;
    public static final int THAN_LINH2 = -172;
    public static final int THAN_LINH3 = -173;
    public static final int THAN_MAU = -181;
    public static final int BI_KIEP_1 = -183;
    public static final int BI_KIEP_2 = -184;
    public static final int BI_KIEP_3 = -185;
    public static final int BI_KIEP_4 = -186;
    public static final int FRIEREN = -187;
    public static final int MAJIN_ANDROID_21 = -188;
    public static final int KING_KONG_SANH_DIEU = -189;
    public static final int INOSUKE_BOSS = -190;
    public static final int ZENITSU_BOSS = -191;
    public static final int INOSUKE_HASHIBIRA_BOSS = -192;
    public static final int TANJIRO_BOSS = -193;
    public static final int NEZUKO_BOSS = -194;
    public static final int GOKU_SKY_1 = -195;
    public static final int GOKU_SKY_2 = -196;
    public static final int GOKU_SKY_3 = -197;
    public static final int GOKU_SKY_4 = -198;
    public static final int GOKU_SKY_5 = -199;
    public static final int THAN_BLUE1 = -200;
    public static final int THAN_BLUE2 = -201;
    public static final int THAN_BLUE3 = -202;
    public static final int THAN_BLUE4 = -203;
    public static final int THAN_BLUE5 = -204;
    public static final int THAN_RED1 = -210;
    public static final int THAN_RED2 = -211;
    public static final int THAN_RED3 = -212;
    public static final int THAN_RED4 = -213;
    public static final int THAN_RED5 = -214;
    public static final int THAN_PINK1 = -220;
    public static final int THAN_PINK2 = -221;
    public static final int THAN_PINK3 = -222;
    public static final int THAN_PINK4 = -223;
    public static final int THAN_PINK5 = -224;
    public static final int THIEN_SU1 = -230;
    public static final int THIEN_SU2 = -231;
    public static final int THIEN_SU3 = -232;
    public static final int THIEN_SU4 = -233;
    public static final int THIEN_SU5 = -234;
    
    public static final int TU_TIEN_1 = -240;
    public static final int TU_TIEN_2 = -241;
    public static final int TU_TIEN_3 = -242;
   
    

    public static final int SIEU_TROM = -1342578;

    private static final Logger logger = Logger.getLogger(BossFactory.class);

    public static final int[] MAP_APPEARED_QILIN = {
        ConstMap.LANG_ARU, ConstMap.LANG_MORI, ConstMap.LANG_KAKAROT, ConstMap.DOI_HOA_CUC, ConstMap.DOI_NAM_TIM, ConstMap.DOI_HOANG,
        ConstMap.LANG_PLANT, ConstMap.RUNG_NGUYEN_SINH,
        ConstMap.RUNG_CO, ConstMap.RUNG_THONG_XAYDA, ConstMap.RUNG_DA, ConstMap.THUNG_LUNG_DEN, ConstMap.BO_VUC_DEN, ConstMap.THANH_PHO_VEGETA,
        ConstMap.THUNG_LUNG_TRE, ConstMap.RUNG_NAM, ConstMap.RUNG_BAMBOO, ConstMap.RUNG_XUONG, ConstMap.RUNG_DUONG_XI, ConstMap.NAM_KAME,
        ConstMap.DAO_BULONG, ConstMap.DONG_KARIN, ConstMap.THI_TRAN_MOORI, ConstMap.THUNG_LUNG_MAIMA, ConstMap.NUI_HOA_TIM, ConstMap.NUI_HOA_VANG
    };

    private BossFactory() {

    }

    public static void initBoss() {
        new Thread(() -> {
            try {

//                createBoss(NGO_KHONG);

//                createBoss(WHIS);// BOS HỦY DIỆT
//                createBoss(VADOS);// BOS HỦY DIỆT
//                createBoss(BILL);// BOS HỦY DIỆT
//                createBoss(CHAMPA);// BOS HỦY DIỆT

//                createBoss(SIEU_TROM);
//                createBoss(CUMBER);

//                createBoss(BULMA);
//                createBoss(CHICHITHO);
//                createBoss(POCTHO);
                
                createBoss(BLACKGOKU);         // BOS NHIỆM VỤ
                createBoss(SUPERBLACKGOKU);    // BOS NHIỆM VỤ
                createBoss(CHILL);             // BOS NHIỆM VỤ

                createBoss(COOLER);            // BOS NHIỆM VỤ
                createBoss(XEN_BO_HUNG);       // BOS NHIỆM VỤ
                createBoss(KUKU);              // BOS NHIỆM VỤ
                createBoss(MAP_DAU_DINH);      // BOS NHIỆM VỤ
                createBoss(RAMBO);             // BOS NHIỆM VỤ
                createBoss(TIEU_DOI_TRUONG);   // BOS NHIỆM VỤ
                createBoss(SO1);   // BOS NHIỆM VỤ
                createBoss(SO2);   // BOS NHIỆM VỤ
                createBoss(SO3);   // BOS NHIỆM VỤ
                createBoss(SO4);   // BOS NHIỆM VỤ
                createBoss(FIDE_DAI_CA_1);     // BOS NHIỆM VỤ
                createBoss(ANDROID_20);        // BOS NHIỆM VỤ
                createBoss(KINGKONG);          // BOS NHIỆM VỤ
                createBoss(XEN_BO_HUNG_1);     // BOS NHIỆM VỤ
                createBoss(SUPER_BLACK_ROSE);  // BOS NHIỆM VỤ
                createBoss(ZAMAS_TOI_THUONG);  // BOS NHIỆM VỤ
                createBoss(XEN_MAX);           // BOS NHIỆM VỤ
//                createBoss(BONG_BANG);
//                createBoss(SOI_BASIL);
//               createBoss(SANTA_CLAUS);
//               createBoss(WHIS_DETU);
//                createBoss(RONG_DEN);
//                createBoss(ZENO);          // 1tr tỷ
//                createBoss(GOKU_SUPER);
//                createBoss(ITACHI);        // 1tr tỷ
//                createBoss(ZAMAS_ZOMBIE);
//                createBoss(LUFFY_GEAR5);
//                createBoss(KAIDO);
//                createBoss(LUFFY_THAN_NIKA);
//                createBoss(NGUYET_THAN);
//                createBoss(MIHAWK);
//                createBoss(NHAT_THAN);

//                createBoss(NHAT_VI);      //2 tỷ tỷ
//                createBoss(NHI_VI);       //2 tỷ tỷ
//                createBoss(TAM_VI);       //2 tỷ tỷ
//                createBoss(TU_VI);        //2 tỷ tỷ
//                createBoss(NGU_VI);       //2 tỷ tỷ

//                createBoss(LUC_VI);       // 100K TY
//                createBoss(THAT_VI);      // 100K TY
//                createBoss(BAT_VI);       // 100K TY
//                createBoss(CUU_VI);       // 100K TY
//                createBoss(THAP_VI);      // 100K TY

//                createBoss(GOKU_SKY_1);     // 10K TY
//                createBoss(GOKU_SKY_2);     // 10K TY
//                createBoss(GOKU_SKY_3);     // 10K TY
//                createBoss(GOKU_SKY_4);     // 10K TY
//                createBoss(GOKU_SKY_5);     // 10K TY

//                createBoss(XEKO);        // BOSS SỰ KIỆN +10 ĐIỂM
//                createBoss(CHAIEN);      // BOSS SỰ KIỆN +10 ĐIỂM
//                createBoss(NOBITA);      // BOSS SỰ KIỆN +10 ĐIỂM
//                createBoss(XUKA);        // BOSS SỰ KIỆN +10 ĐIỂM
//                createBoss(DOREMON);     // BOSS SỰ KIỆN +10 ĐIỂM

//                createBoss(GOKU_THAN_TAI);    // BOSS CÀY CHAY
//                createBoss(PICOLO_THAN_TAI);  // BOSS CÀY CHAY
//                createBoss(VEGETA_THAN_TAI);  // BOSS CÀY CHAY

//                createBoss(SON_TINH);
//                createBoss(THUY_TINH);
//                createBoss(VUA_HUNG);

                createBoss(RONG_1SAO); // 1_000_000_000_000D
                createBoss(RONG_2SAO); // 2_000_000_000_000D
                createBoss(RONG_3SAO); // 3_000_000_000_000D
//                createBoss(RONG_4SAO); 
//                createBoss(RONG_5SAO); 
//                createBoss(RONG_6SAO); 
//                createBoss(RONG_7SAO); 

//                createBoss(ADMIN_1);
//                createBoss(ADMIN_2);

                createBoss(HUY_DIET1);  // 30_000_000_000D
                createBoss(HUY_DIET2);
                createBoss(HUY_DIET3);
//                createBoss(HUY_DIET4);
//                createBoss(HUY_DIET5);

                createBoss(THAN_LINH1);  // 10_000_000_000D
                createBoss(THAN_LINH2);
//                createBoss(THAN_LINH3);

                createBoss(THIEN_SU1);  // 100_000_000_000D
//                createBoss(THIEN_SU2);
//                createBoss(THIEN_SU3);
//                createBoss(THIEN_SU4);
//                createBoss(THIEN_SU5);

//                createBoss(THAN_MAU);

//                createBoss(BI_KIEP_1);
//                createBoss(BI_KIEP_2);
//                createBoss(BI_KIEP_3);
//                createBoss(BI_KIEP_4);

                createBoss(THAN_BLUE1);
//                createBoss(THAN_BLUE2);
//                createBoss(THAN_BLUE3);
//                createBoss(THAN_BLUE4);
//                createBoss(THAN_BLUE5);
                
                createBoss(THAN_RED1);
//                createBoss(THAN_RED2);
//                createBoss(THAN_RED3);
//                createBoss(THAN_RED4);
//                createBoss(THAN_RED5);
                
                createBoss(THAN_PINK1);
//                createBoss(THAN_PINK2);
//                createBoss(THAN_PINK3);
//                createBoss(THAN_PINK4);
//                createBoss(THAN_PINK5);
                createBoss(TU_TIEN_1);
                createBoss(TU_TIEN_2);
                createBoss(TU_TIEN_3);
                
                
                
                if (Manager.EVENT_SEVER == ConstEvent.SIEU_ANH_HUNG) {
                    createBoss(TOPPO);
                    createBoss(JIREN);
                    createBoss(JIREN_HOAN_MY);
                }
                for (int i = 0; i < 3; i++) {
                     createBoss(BROLY_DETU);  // 500 TỶ RƠI ĐỆ
                }
                for (int i = 0; i < 3; i++) {
                    createBoss(SUPER_BROLY); // 100 TỶ BOSS RƠI NRO 1S-7S
                }
                for (Map map : Manager.MAPS) {
                    if (map != null && !map.zones.isEmpty()) {
                        if (!map.isMapOffline && map.type == ConstMap.MAP_NORMAL
                                && map.tileId > 0 && !MapService.gI().isMapVS(map.mapId)) {
                            if (map.mapWidth > 50 && map.mapHeight > 50) {
                                if (Manager.EVENT_SEVER == ConstEvent.SU_KIEN_20_11) {
                                    new HoaHong(map.mapId);
                                }
                                if (Manager.EVENT_SEVER == ConstEvent.SU_KIEN_NOEL) {
                                    new SantaClaus(map.mapId);
                                }
                            }
                        }
                    }
                }
                if (Manager.EVENT_SEVER == ConstEvent.SU_KIEN_TET) {
                    for (int mapID : MAP_APPEARED_QILIN) {
                        new Qilin(mapID);
                    }
                }

//                createBoss(FRIEREN);
//                createBoss(MAJIN_ANDROID_21);
//                createBoss(KING_KONG_SANH_DIEU);
//                createBoss(NEZUKO_BOSS);

            } catch (Exception e) {
                logger.error("Err initboss", e);
            }
        }).start();
    }

    public static void initBossMabuWar14H() {
        new Thread(() -> {
            Map map = MapService.gI().getMapById(127);
            for (Zone zone : map.zones) {
                Boss boss = new Mabu_14H(127, zone.zoneId);
                MabuWar14h.gI().bosses.add(boss);
            }
            map = MapService.gI().getMapById(128);
            for (Zone zone : map.zones) {
                Boss boss = new SuperBu_14H(128, zone.zoneId);
                MabuWar14h.gI().bosses.add(boss);
            }
        }).start();
    }

    public static void initBossMabuWar() {
        new Thread(() -> {
            for (short mapid : BossData.DRABULA_TANG1.mapJoin) {
                Map map = MapService.gI().getMapById(mapid);
                for (Zone zone : map.zones) {
                    Boss boss = new Drabula_Tang1(mapid, zone.zoneId);
                    MabuWar.gI().bosses.add(boss);
                }
            }
            for (short mapid : BossData.DRABULA_TANG6.mapJoin) {
                Map map = MapService.gI().getMapById(mapid);
                for (Zone zone : map.zones) {
                    Boss boss = new Drabula_Tang6(mapid, zone.zoneId);
                    MabuWar.gI().bosses.add(boss);
                }
            }
            for (short mapid : BossData.GOKU_TANG5.mapJoin) {
                Map map = MapService.gI().getMapById(mapid);
                for (Zone zone : map.zones) {
                    Boss boss = new Goku_Tang5(mapid, zone.zoneId);
                    MabuWar.gI().bosses.add(boss);
                }
            }
            for (short mapid : BossData.CALICH_TANG5.mapJoin) {
                Map map = MapService.gI().getMapById(mapid);
                for (Zone zone : map.zones) {
                    Boss boss = new Calich_Tang5(mapid, zone.zoneId);
                    MabuWar.gI().bosses.add(boss);
                }
            }
            for (short mapid : BossData.BUIBUI_TANG2.mapJoin) {
                Map map = MapService.gI().getMapById(mapid);
                for (Zone zone : map.zones) {
                    Boss boss = new BuiBui_Tang2(mapid, zone.zoneId);
                    MabuWar.gI().bosses.add(boss);
                }
            }
            for (short mapid : BossData.BUIBUI_TANG3.mapJoin) {
                Map map = MapService.gI().getMapById(mapid);
                for (Zone zone : map.zones) {
                    Boss boss = new BuiBui_Tang3(mapid, zone.zoneId);
                    MabuWar.gI().bosses.add(boss);
                }
            }
            for (short mapid : BossData.YACON_TANG4.mapJoin) {
                Map map = MapService.gI().getMapById(mapid);
                for (Zone zone : map.zones) {
                    Boss boss = new Yacon_Tang4(mapid, zone.zoneId);
                    MabuWar.gI().bosses.add(boss);
                }
            }
        }).start();
    }

    public static Boss createBoss(int bossId) {
        Boss boss = null;
        try {
            switch (bossId) {
                case SIEU_TROM:
                    boss = new SieuTrom();
                    break;
                case BROLY:
                    boss = new Broly();
                    break;
                case SUPER_BROLY:
                    boss = new SuperBroly();
                    break;
                case BROLY_DETU:
                    boss = new BrolyDetu();
                    break;
                case XEN_BO_HUNG_1:
                    boss = new XenBoHung1();
                    break;
                case XEN_BO_HUNG_2:
                    boss = new XenBoHung2();
                    break;
                case XEN_BO_HUNG_HOAN_THIEN:
                    boss = new XenBoHungHoanThien();
                    break;
                case XEN_BO_HUNG:
                    boss = new XenBoHung();
                    break;
                case XEN_CON:
                    boss = new XenCon();
                    break;
                case SIEU_BO_HUNG:
                    boss = new SieuBoHung();
                    break;
                case KUKU:
                    boss = new Kuku();
                    break;
                case MAP_DAU_DINH:
                    boss = new MapDauDinh();
                    break;
                case RAMBO:
                    boss = new Rambo();
                    break;
                case COOLER:
                    boss = new Cooler();
                    break;
                case COOLER2:
                    boss = new Cooler2();
                    break;
                case SO4:
                    boss = new So4();
                    break;
                case SO3:
                    boss = new So3();
                    break;
                case SO2:
                    boss = new So2();
                    break;
                case SO1:
                    boss = new So1();
                    break;
                case TIEU_DOI_TRUONG:
                    boss = new TieuDoiTruong();
                    break;
                case FIDE_DAI_CA_1:
                    boss = new FideDaiCa1();
                    break;
                case FIDE_DAI_CA_2:
                    boss = new FideDaiCa2();
                    break;
                case FIDE_DAI_CA_3:
                    boss = new FideDaiCa3();
                    break;
                case ANDROID_19:
                    boss = new Android19();
                    break;
                case ANDROID_20:
                    boss = new Android20();
                    break;
                case SUPER_BROLY_RED:
                    boss = new SuperBrolyRed();
                    break;
                case POC:
                    boss = new Poc();
                    break;
                case PIC:
                    boss = new Pic();
                    break;
                case KINGKONG:
                    boss = new KingKong();
                    break;
                case WHIS:
                    boss = new Whis();
                    break;
                case BILL:
                    boss = new Bill();
                    break;
                case VADOS:
                    boss = new Vados();
                    break;
                case CHAMPA:
                    boss = new Champa();
                    break;
                case CHILL:
                    boss = new Chill();
                    break;
                case CHILL2:
                    boss = new Chill2();
                    break;
                case BULMA:
                    boss = new BULMA();
                    break;
                case POCTHO:
                    boss = new POCTHO();
                    break;
                case CHICHITHO:
                    boss = new CHICHITHO();
                    break;
                case SUPER_BLACK_ROSE:
                    boss = new BLACKROSE();
                    break;
                case ZAMAS_TOI_THUONG:
                    boss = new ZamasToiThuong();
                    break;
                case BONG_BANG:
                    boss = new BongBang();
                    break;
                case JIREN:
                    boss = new Jiren();
                    break;
                case TOPPO:
                    boss = new Toppo();
                    break;
                case JIREN_HOAN_MY:
                    boss = new JirenHoanMy();
                    break;
                case SOI_BASIL:
                    boss = new SoiBasil();
                    break;
                case ITACHI:
                    boss = new Itachi();
                    break;
                case NGUYET_THAN:
                    boss = new nguyetthan();
                    break;
                case MIHAWK:
                    boss = new Mihawk();
                    break;
                case ZAMAS_ZOMBIE:
                    boss = new ZamasZombie();
                    break;
                case LUFFY_GEAR5:
                    boss = new LuffyGear5();
                    break;
                case KAIDO:
                    boss = new Kaido();
                    break;
                case NHAT_THAN:
                    boss = new nhatthan();
                    break;
                case LUFFY_THAN_NIKA:
                    boss = new LuffyThanNika();
                    break;
                case GOKU_SUPER:
                    boss = new GokuSuper();
                    break;
                case WHIS_DETU:
                    boss = new WhisDetu();
                    break;
                case ZENO:
                    boss = new ZenoDetu();
                    break;
                case RONG_DEN:
                    boss = new RongDen();
                    break;
//             case BROLYDEN:
//                boss = new Brolyden();
//                break;
//             case BROLYXANH:
//                boss = new Brolyxanh();
//                break;
//             case BROLYVANG:
//                boss = new Brolyvang();
//                break;
                case BLACKGOKU:
                    boss = new Blackgoku();
                    break;
                case SUPERBLACKGOKU:
                    boss = new Superblackgoku();
                    break;
                case MABU_MAP:
                    boss = new Mabu_14H(52, Util.nextInt(0, 10));
                    break;
                case XEN_MAX:
                    boss = new XenMax();
                    break;
                case NGO_KHONG:
                    boss = new NgoKhong();
                    break;
                case BAT_GIOI:
                    boss = new BatGioi();
                    break;
                case FIDEGOLD:
                    boss = new FideGold();
                    break;
                case CUMBER:
                    boss = new Cumber();
                    break;
                case CUMBER2:
                    boss = new SuperCumber();
                    break;
                case NHAT_VI:
                    boss = new NhatVi();
                    break;
                case NHI_VI:
                    boss = new NhiVi();
                    break;
                case TAM_VI:
                    boss = new TamVi();
                    break;
                case TU_VI:
                    boss = new TuVi();
                    break;
                case NGU_VI:
                    boss = new NguVi();
                    break;
                case LUC_VI:
                    boss = new LucVi();
                    break;
                case THAT_VI:
                    boss = new ThatVi();
                    break;
                case BAT_VI:
                    boss = new BatVi();
                    break;
                case CUU_VI:
                    boss = new CuuVi();
                    break;
                case THAP_VI:
                    boss = new ThapVi();
                    break;
                case GOKU_SKY_1:
                    boss = new GoKuSky1();
                    break;
                case GOKU_SKY_2:
                    boss = new GoKuSky2();
                    break;
                case GOKU_SKY_3:
                    boss = new GoKuSky3();
                    break;
                case GOKU_SKY_4:
                    boss = new GoKuSky4();
                    break;
                case GOKU_SKY_5:
                    boss = new GoKuSky5();
                    break;
                case DOREMON:
                    boss = new Doremon();
                    break;
                case NOBITA:
                    boss = new Nobita();
                    break;
                case XUKA:
                    boss = new Xuka();
                    break;
                case CHAIEN:
                    boss = new Chaien();
                    break;
                case XEKO:
                    boss = new Xeko();
                    break;
                case GOKU_THAN_TAI:
                    boss = new GokuThanTai();
                    break;
                case PICOLO_THAN_TAI:
                    boss = new PicoloThanTai();
                    break;
                case VEGETA_THAN_TAI:
                    boss = new VegetaThanTai();
                    break;
                case SON_TINH:
                    boss = new SonTinh();
                    break;
                case THUY_TINH:
                    boss = new ThuyTinh();
                    break;
                case VUA_HUNG:
                    boss = new VuaHung();
                    break;
                case RONG_1SAO:
                    boss = new Rong1sao();
                    break;
                case RONG_2SAO:
                    boss = new Rong2sao();
                    break;
                case RONG_3SAO:
                    boss = new Rong3sao();
                    break;
                case RONG_4SAO:
                    boss = new Rong4sao();
                    break;
                case RONG_5SAO:
                    boss = new Rong5sao();
                    break;
                case RONG_6SAO:
                    boss = new Rong6sao();
                    break;
                case RONG_7SAO:
                    boss = new Rong7sao();
                    break;
                case ADMIN_1:
                    boss = new Admin1();
                    break;
                case ADMIN_2:
                    boss = new Admin2();
                    break;
                case HUY_DIET1:
                    boss = new HuyDiet1();
                    break;
                case HUY_DIET2:
                    boss = new HuyDiet2();
                    break;
                case HUY_DIET3:
                    boss = new HuyDiet3();
                    break;
                case HUY_DIET4:
                    boss = new HuyDiet4();
                    break;
                case HUY_DIET5:
                    boss = new HuyDiet5();
                    break;
                case THAN_LINH1:
                    boss = new ThanLinh1();
                    break;
                case THAN_LINH2:
                    boss = new ThanLinh2();
                    break;
                case THAN_LINH3:
                    boss = new ThanLinh3();
                    break;
                case THAN_MAU:
                    boss = new ThanMau();
                    break;
                case THIEN_SU1:
                    boss = new ThienSu1();
                    break;
                case BI_KIEP_1:
                    boss = new TapSuLoai1();
                    break;
                case BI_KIEP_2:
                    boss = new TapSuLoai2();
                    break;
                case BI_KIEP_3:
                    boss = new TapSuLoai3();
                    break;
                case BI_KIEP_4:
                    boss = new TapSuLoai4();
                    break;
                case FRIEREN:
                    boss = new Frieren();
                    break;
                case MAJIN_ANDROID_21:
                    boss = new Majin_Android_21();
                    break;
                case KING_KONG_SANH_DIEU:
                    boss = new King_Kong_Sanh_Dieu();
                    break;
                case INOSUKE_BOSS:
                    boss = new Inosuke();
                    break;
                case ZENITSU_BOSS:
                    boss = new Zenitsu();
                    break;
                case INOSUKE_HASHIBIRA_BOSS:
                    boss = new InosukeHashibira();
                    break;
                case TANJIRO_BOSS:
                    boss = new Tanjiro();
                    break;
                case NEZUKO_BOSS:
                    boss = new Nezuko();
                    break;
                case THAN_BLUE1:
                    boss = new Blue1();
                    break;
                case THAN_BLUE2:
                    boss = new Blue2();
                    break;
                case THAN_BLUE3:
                    boss = new Blue3();
                    break;
                case THAN_BLUE4:
                    boss = new Blue4();
                    break;
                case THAN_BLUE5:
                    boss = new Blue5();
                    break;
                case THAN_RED1:
                    boss = new Red1();
                    break;
                case THAN_RED2:
                    boss = new Red2();
                    break;
                case THAN_RED3:
                    boss = new Red3();
                    break;
                case THAN_RED4:
                    boss = new Red4();
                    break;
                case THAN_RED5:
                    boss = new Red5();
                    break;
                case THAN_PINK1:
                    boss = new Pink1();
                    break;
                case THAN_PINK2:
                    boss = new Pink2();
                    break;
                case THAN_PINK3:
                    boss = new Pink3();
                    break;
                case THAN_PINK4:
                    boss = new Pink4();
                    break;
                case THAN_PINK5:
                    boss = new Pink5();
                    break;
                case THIEN_SU2:
                    boss = new ThienSu2();
                    break;
                case THIEN_SU3:
                    boss = new ThienSu3();
                    break;
                case THIEN_SU4:
                    boss = new ThienSu4();
                    break;
                case THIEN_SU5:
                    boss = new ThienSu5();
                    break;
                case TU_TIEN_1:
                    boss = new TuTien1();
                    break;
                case TU_TIEN_2:
                    boss = new TuTien2();
                    break;
                case TU_TIEN_3:
                    boss = new TuTien3();
                    break;

            }

        } catch (Exception e) {
        }
        return boss;
    }

}
