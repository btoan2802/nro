package nro.models.boss;

import nro.consts.ConstPlayer;
import nro.models.skill.Skill;
import lombok.Builder;

/**
 * @author Hoàng Việt - 0857853150
 * @copyright 💖 GirlkuN 💖
 */
public class BossData {

    public static final int _0_GIAY = 0;
    public static final int _1_GIAY = 1;
    public static final int _5_GIAY = 5;
    public static final int _10_GIAY = 10;
    public static final int _30_GIAY = 30;
    public static final int _1_PHUT = 60;
    public static final int _5_PHUT = 300;
    public static final int _10_PHUT = 600;
    public static final int _15_PHUT = 900;
    public static final int _20_PHUT = 1200;
    public static final int _25_PHUT = 1500;
    public static final int _30_PHUT = 1800;
    public static final int _1_GIO = 3600;
    public static final int _2_GIO = 7200;
    public static final int _5_GIO = 18000;

    //--------------------------------------------------------------------------
    public String name;

    public byte gender;

    public byte typeDame;

    public byte typeHp;

    public int dame;

    public double[][] hp;

    public short[] outfit;

    public short[] mapJoin;

    public int[][] skillTemp;

    public int secondsRest;

    public boolean joinMapIdle;

    public int timeDelayLeaveMap = -1;

    @Builder
    public BossData(String name, byte gender, byte typeDame, byte typeHp, int dame, double[][] hp,
            short[] outfit, short[] mapJoin, int[][] skillTemp, int secondsRest) {
        this.name = name;
        this.gender = gender;
        this.typeDame = typeDame;
        this.typeHp = typeHp;
        this.dame = dame;
        this.hp = hp;
        this.outfit = outfit;
        this.mapJoin = mapJoin;
        this.skillTemp = skillTemp;
        this.secondsRest = secondsRest;
    }

    public BossData(String name, byte gender, byte typeDame, byte typeHp, int dame, double[][] hp,
            short[] outfit, short[] mapJoin, int[][] skillTemp, int secondsRest, boolean joinMapIdle) {
        this.name = name;
        this.gender = gender;
        this.typeDame = typeDame;
        this.typeHp = typeHp;
        this.dame = dame;
        this.hp = hp;
        this.outfit = outfit;
        this.mapJoin = mapJoin;
        this.skillTemp = skillTemp;
        this.secondsRest = secondsRest;
        this.joinMapIdle = joinMapIdle;
    }

    public BossData(String name, byte gender, byte typeDame, byte typeHp, int dame, double[][] hp,
            short[] outfit, short[] mapJoin, int[][] skillTemp, int secondsRest, int timeDelayLeaveMap) {
        this.name = name;
        this.gender = gender;
        this.typeDame = typeDame;
        this.typeHp = typeHp;
        this.dame = dame;
        this.hp = hp;
        this.outfit = outfit;
        this.mapJoin = mapJoin;
        this.skillTemp = skillTemp;
        this.secondsRest = secondsRest;
        this.timeDelayLeaveMap = timeDelayLeaveMap;
    }

    public BossData(String name, byte gender, byte typeDame, byte typeHp, int dame, double[][] hp,
            short[] outfit, short[] mapJoin, int[][] skillTemp, int secondsRest, boolean joinMapIdle, int timeDelayLeaveMap) {
        this.name = name;
        this.gender = gender;
        this.typeDame = typeDame;
        this.typeHp = typeHp;
        this.dame = dame;
        this.hp = hp;
        this.outfit = outfit;
        this.mapJoin = mapJoin;
        this.skillTemp = skillTemp;
        this.secondsRest = secondsRest;
        this.joinMapIdle = joinMapIdle;
        this.timeDelayLeaveMap = timeDelayLeaveMap;
    }

    //--------------------------------------------------------------------------Broly
    public static final BossData BROLY = new BossData(
            "Broly %1", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_PERCENT_HP_HUND, //type dame
            Boss.HP_NORMAL, //type hp
            1, //dame
            new double[][]{{100, 1000}, {1000, 100000}, {100000, 1000000}, {1000000, 2000000}}, //hp
            new short[]{291, 292, 293}, //outfit
            new short[]{5, 6, 27, 28, 29, 30, 13, 10, 31, 32, 33, 34, 20, 19, 35, 36, 37, 38}, //map join
            new int[][]{ //skill
                {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650}, {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
                {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800}, {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},},
            -1 //số giây nghỉ
    );

    public static final BossData SUPER_BROLY = new BossData(
            "Rồng Omega", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            10000, //dame
            new double[][]{{100_000_000_000L}}, //hp
            new short[]{1050, 1051, 1052}, //outfit
            new short[]{97, 98, 99, 100}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 7, 650},
                {Skill.KAMEJOKO, 7, 2000},
                {Skill.ANTOMIC, 7, 2000},
                {Skill.THAI_DUONG_HA_SAN, 7, 15000},},
            
            _5_PHUT
    );

    public static final BossData BROLY_DETU = new BossData(
            "Berus BaBy", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            1000000000, //dame
            new double[][]{{30_000_000_000_000L}}, //hp
            new short[]{1631, 1632, 1633}, //outfit
            new short[]{29, 30, 31, 32, 33, 34, 35, 36, 37, 38}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 7, 650},
                {Skill.KAMEJOKO, 7, 2000},
                {Skill.ANTOMIC, 7, 2000},
                {Skill.THAI_DUONG_HA_SAN, 7, 15000},},
            
            _10_PHUT
    );

    public static final BossData SUPER_BROLY_RED = new BossData(
            "Super Broly Love %1", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            300000, //dame
            new double[][]{{1000000000}}, //hp
            new short[]{294, 295, 296}, //outfit
            new short[]{5, 6, 27, 28, 29, 30, 13, 10, 31, 32, 33, 34, 20, 19, 35, 36, 37, 38}, //map join
            //            new short[]{14}, //map join
            new int[][]{ //skill
                {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650}, {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
                {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800}, {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
                {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700}, {Skill.ANTOMIC, 7, 2000},
                {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300}, {Skill.MASENKO, 6, 1500},
                {Skill.TAI_TAO_NANG_LUONG, 1, 15000}, {Skill.TAI_TAO_NANG_LUONG, 3, 25000}, {Skill.TAI_TAO_NANG_LUONG, 5, 25000},
                {Skill.TAI_TAO_NANG_LUONG, 6, 30000}, {Skill.TAI_TAO_NANG_LUONG, 7, 50000}
            },
            _10_PHUT
    );
    //--------------------------------------------------------------------------Boss hải tặc

    public static final BossData LUFFY = new BossData(
            "Luffy", //name
            ConstPlayer.TRAI_DAT, //gender
            Boss.DAME_PERCENT_HP_THOU, //type dame
            Boss.HP_NORMAL, //type hp
            5, //dame
            new double[][]{{12000000}}, //hp
            new short[]{582, 583, 584}, //outfit
            new short[]{137}, //map join
            new int[][]{ //skill
                {Skill.GALICK, 7, 1000}, {Skill.GALICK, 6, 1000}, {Skill.GALICK, 5, 1000}, {Skill.GALICK, 4, 1000}
            },
            _1_PHUT, true
    );

    public static final BossData ZORO = new BossData(
            "Zoro", //name
            ConstPlayer.TRAI_DAT, //gender
            Boss.DAME_PERCENT_HP_THOU, //type dame
            Boss.HP_NORMAL, //type hp
            5, //dame
            new double[][]{{11000000}}, //hp
            new short[]{585, 586, 587}, //outfit
            new short[]{137}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 7, 1000}, {Skill.DRAGON, 6, 1000}, {Skill.DRAGON, 5, 1000}, {Skill.DRAGON, 4, 1000}
            },
            _1_PHUT, true
    );

    public static final BossData SANJI = new BossData(
            "Sanji", //name
            ConstPlayer.TRAI_DAT, //gender
            Boss.DAME_PERCENT_HP_THOU, //type dame
            Boss.HP_NORMAL, //type hp
            5, //dame
            new double[][]{{10000000}}, //hp
            new short[]{588, 589, 590}, //outfit
            new short[]{137}, //map join
            new int[][]{ //skill
                {Skill.DEMON, 7, 1000}, {Skill.DEMON, 6, 1000}, {Skill.DEMON, 5, 1000}, {Skill.DEMON, 4, 1000}
            },
            _1_PHUT, true
    );

    public static final BossData USOPP = new BossData(
            "Usopp", //name
            ConstPlayer.TRAI_DAT, //gender
            Boss.DAME_PERCENT_HP_THOU, //type dame
            Boss.HP_NORMAL, //type hp
            5, //dame
            new double[][]{{7000000}}, //hp
            new short[]{597, 598, 599}, //outfit
            new short[]{136}, //map join
            new int[][]{ //skill
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 1, 1000},
                {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 1, 1000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 1, 1000},},
            _1_PHUT, true
    );

    public static final BossData FRANKY = new BossData(
            "Franky", //name
            ConstPlayer.TRAI_DAT, //gender
            Boss.DAME_PERCENT_HP_THOU, //type dame
            Boss.HP_NORMAL, //type hp
            5, //dame
            new double[][]{{8000000}}, //hp
            new short[]{594, 595, 596}, //outfit
            new short[]{136}, //map join
            new int[][]{ //skill
                {Skill.DEMON, 7, 1000}, {Skill.DEMON, 6, 1000}, {Skill.DEMON, 5, 1000}, {Skill.DEMON, 4, 1000},
                {Skill.ANTOMIC, 7, 5000}
            },
            _1_PHUT, true
    );

    public static final BossData BROOK = new BossData(
            "Brook", //name
            ConstPlayer.TRAI_DAT, //gender
            Boss.DAME_PERCENT_HP_THOU, //type dame
            Boss.HP_NORMAL, //type hp
            5, //dame
            new double[][]{{9000000}}, //hp
            new short[]{591, 592, 593}, //outfit
            new short[]{136}, //map join
            new int[][]{ //skill
                {Skill.DEMON, 7, 1000}, {Skill.DEMON, 6, 1000}, {Skill.DEMON, 5, 1000}, {Skill.DEMON, 4, 1000}
            },
            _1_PHUT, true
    );

    public static final BossData NAMI = new BossData(
            "Nami", //name
            ConstPlayer.TRAI_DAT, //gender
            Boss.DAME_PERCENT_HP_THOU, //type dame
            Boss.HP_NORMAL, //type hp
            5, //dame
            new double[][]{{4000000}}, //hp
            new short[]{600, 601, 602}, //outfit
            new short[]{138}, //map join
            new int[][]{ //skill
                {Skill.DEMON, 7, 1000}, {Skill.DEMON, 6, 1000}, {Skill.DEMON, 5, 1000}, {Skill.DEMON, 4, 1000}
            },
            _1_PHUT, true
    );

    public static final BossData CHOPPER = new BossData(
            "Chopper", //name
            ConstPlayer.TRAI_DAT, //gender
            Boss.DAME_PERCENT_HP_THOU, //type dame
            Boss.HP_NORMAL, //type hp
            5, //dame
            new double[][]{{5000000}}, //hp
            new short[]{606, 607, 608}, //outfit
            new short[]{138}, //map join
            new int[][]{ //skill
                {Skill.DEMON, 7, 1000}, {Skill.DEMON, 6, 1000}, {Skill.DEMON, 5, 1000}, {Skill.DEMON, 4, 1000}
            },
            _1_PHUT, true
    );

    public static final BossData TRUNG_UY_XANH_LO_2 = new BossData(
            "Trung uý Xanh Lơ", //name
            ConstPlayer.TRAI_DAT, //gender
            Boss.DAME_PERCENT_HP_THOU, //type dame
            Boss.HP_NORMAL, //type hp
            10, //dame
            new double[][]{{100000000L}}, //hp
            new short[]{135, 136, 137}, //outfit
            new short[]{62}, //map join
            new int[][]{ //skill
                {Skill.DEMON, 1, 520}, {Skill.DEMON, 2, 500}, {Skill.DEMON, 3, 480}, {Skill.DEMON, 4, 460}, {Skill.DEMON, 5, 440}, {Skill.DEMON, 6, 420}, {Skill.DEMON, 7, 400},
                {Skill.KAMEJOKO, 2, 1500},
                {Skill.THAI_DUONG_HA_SAN, 3, 15000}, {Skill.THAI_DUONG_HA_SAN, 7, 30000}
            },
            _1_PHUT
    );

    public static final BossData ROBIN = new BossData(
            "Robin", //name
            ConstPlayer.TRAI_DAT, //gender
            Boss.DAME_PERCENT_HP_THOU, //type dame
            Boss.HP_NORMAL, //type hp
            5, //dame
            new double[][]{{6000000}}, //hp
            new short[]{603, 604, 605}, //outfit
            new short[]{138}, //map join
            new int[][]{ //skill
                {Skill.DEMON, 7, 1000}, {Skill.DEMON, 6, 1000}, {Skill.DEMON, 5, 1000}, {Skill.DEMON, 4, 1000}
            },
            _1_PHUT, true
    );

    //--------------------------------------------------------------------------Boss doanh trại
    public static final BossData TRUNG_UY_TRANG = new BossData(
            "Trung uý Trắng", //name
            ConstPlayer.TRAI_DAT, //gender
            Boss.DAME_TIME_PLAYER_WITH_HIGHEST_HP_IN_CLAN, //type dame
            Boss.HP_TIME_PLAYER_WITH_HIGHEST_DAME_IN_CLAN, //type hp
            50, //dame
            new double[][]{{50}}, //hp
            new short[]{141, 142, 143}, //outfit
            new short[]{59}, //map join
            new int[][]{ //skill
                {Skill.DEMON, 1, 520}, {Skill.DEMON, 2, 500}, {Skill.DEMON, 3, 480}, {Skill.DEMON, 4, 460}, {Skill.DEMON, 5, 440}, {Skill.DEMON, 6, 420}, {Skill.DEMON, 7, 400}
            },
            _1_PHUT
    );

    public static final BossData TRUNG_UY_XANH_LO = new BossData(
            "Trung uý Xanh Lơ", //name
            ConstPlayer.TRAI_DAT, //gender
            Boss.DAME_TIME_PLAYER_WITH_HIGHEST_HP_IN_CLAN, //type dame
            Boss.HP_TIME_PLAYER_WITH_HIGHEST_DAME_IN_CLAN, //type hp
            20, //dame
            new double[][]{{30}}, //hp
            new short[]{135, 136, 137}, //outfit
            new short[]{62}, //map join
            new int[][]{ //skill
                {Skill.DEMON, 1, 520}, {Skill.DEMON, 2, 500}, {Skill.DEMON, 3, 480}, {Skill.DEMON, 4, 460}, {Skill.DEMON, 5, 440}, {Skill.DEMON, 6, 420}, {Skill.DEMON, 7, 400},
                {Skill.KAMEJOKO, 2, 1500},
                {Skill.THAI_DUONG_HA_SAN, 3, 15000}, {Skill.THAI_DUONG_HA_SAN, 7, 30000}
            },
            _1_PHUT
    );

    public static final BossData TRUNG_UY_THEP = new BossData(
            "Trung uý Thép", //name
            ConstPlayer.TRAI_DAT, //gender
            Boss.DAME_TIME_PLAYER_WITH_HIGHEST_HP_IN_CLAN, //type dame
            Boss.HP_TIME_PLAYER_WITH_HIGHEST_DAME_IN_CLAN, //type hp
            100, //dame
            new double[][]{{300}}, //hp
            new short[]{129, 130, 131}, //outfit
            new short[]{55}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 300}, {Skill.DRAGON, 3, 500},
                {Skill.DEMON, 1, 100}, {Skill.DEMON, 2, 300}, {Skill.DEMON, 3, 500},
                {Skill.GALICK, 1, 100},
                {Skill.MASENKO, 1, 100}, {Skill.MASENKO, 2, 100}
            },
            _1_PHUT
    );

    public static final BossData NINJA_AO_TIM = new BossData(
            "Ninja áo tím", //name
            ConstPlayer.TRAI_DAT, //gender
            Boss.DAME_TIME_PLAYER_WITH_HIGHEST_HP_IN_CLAN, //type dame
            Boss.HP_TIME_PLAYER_WITH_HIGHEST_DAME_IN_CLAN, //type hp
            40, //dame
            new double[][]{{150}}, //hp
            new short[]{123, 124, 125}, //outfit
            new short[]{54}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 1, 100},
                {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200}, {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
                {Skill.GALICK, 1, 100}
            },
            _1_PHUT
    );

    public static final BossData NINJA_AO_TIM_FAKE = new BossData(
            "Ninja áo tím", //name
            ConstPlayer.TRAI_DAT, //gender
            Boss.DAME_TIME_PLAYER_WITH_HIGHEST_HP_IN_CLAN, //type dame
            Boss.HP_TIME_PLAYER_WITH_HIGHEST_DAME_IN_CLAN, //type hp
            75, //dame
            new double[][]{{100}}, //hp
            new short[]{123, 124, 125}, //outfit
            new short[]{54}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 1, 100},
                {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200}, {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
                {Skill.GALICK, 1, 100}
            },
            _1_PHUT
    );

    public static final BossData ROBOT_VE_SI = new BossData(
            "Rôbốt Vệ Sĩ", //name
            ConstPlayer.TRAI_DAT, //gender
            Boss.DAME_TIME_PLAYER_WITH_HIGHEST_HP_IN_CLAN, //type dame
            Boss.HP_TIME_PLAYER_WITH_HIGHEST_DAME_IN_CLAN, //type hp
            50, //dame
            new double[][]{{120}}, //hp
            new short[]{138, 139, 140}, //outfit
            new short[]{57}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300}, {Skill.DRAGON, 7, 700},
                {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200}, {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
                {Skill.GALICK, 1, 100}
            },
            _1_PHUT
    );

    //--------------------------------------------------------------------------Boss xên ginder
    public static final BossData XEN_BO_HUNG_1 = new BossData(
            "Xên bọ hung 1", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            12000000, //dame
            new double[][]{{50_000_000_000L}}, //hp
            new short[]{228, 229, 230}, //outfit
            new short[]{100}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300}, {Skill.DRAGON, 7, 700},
                {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200}, {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
                {Skill.GALICK, 1, 100}
            },
            _5_PHUT
    );

    public static final BossData XEN_BO_HUNG_2 = new BossData(
            "Xên bọ hung 2", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            16000000, //dame
            new double[][]{{70_000_000_000L}}, //hp
            new short[]{231, 232, 233}, //outfit
            new short[]{100}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300}, {Skill.DRAGON, 7, 700},
                {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200}, {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
                {Skill.GALICK, 1, 100}
            },
            _5_GIAY
    );

    public static final BossData XEN_BO_HUNG_HOAN_THIEN = new BossData(
            "Xên hoàn thiện", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            200000000, //dame
            new double[][]{{1_000_000_000_000D}}, //hp
            new short[]{234, 235, 236}, //outfit
            new short[]{100}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300}, {Skill.DRAGON, 7, 700},
                {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200}, {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
                {Skill.GALICK, 1, 100}
            },
            _5_GIAY
    );

    //--------------------------------------------------------------------------Boss xên võ đài
    public static final BossData XEN_BO_HUNG = new BossData(
            "Xên bọ hung", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            200000000, //dame
            new double[][]{{2_000_000_000_000D}}, //hp
            new short[]{234, 235, 236}, //outfit
            new short[]{103}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300}, {Skill.DRAGON, 7, 700},
                {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200}, {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
                {Skill.GALICK, 1, 100},
                {Skill.THAI_DUONG_HA_SAN, 5, 45000},
                {Skill.TU_SAT, 7, 100}
            },
            _5_PHUT, true
    );

    public static final BossData XEN_CON = new BossData(
            "Xên con", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            15000000, //dame
            new double[][]{{100_000_000_000L}}, //hp
            new short[]{264, 265, 266}, //outfit
            new short[]{103}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300}, {Skill.DRAGON, 7, 700},
                {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200}, {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
                {Skill.GALICK, 1, 100}
            },
            _5_GIAY
    );

    public static final BossData SIEU_BO_HUNG = new BossData(
            "Siêu bọ hung", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            25000000, //dame
            new double[][]{{5_000_000_000_000L}}, //hp
            new short[]{234, 235, 236}, //outfit
            new short[]{103}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300}, {Skill.DRAGON, 7, 700},
                {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200}, {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
                {Skill.GALICK, 1, 100}
            },
            _5_GIAY
    );

    //--------------------------------------------------------------------------Boss nappa
    public static final BossData KUKU = new BossData(
            "Kuku", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            10000, //dame
            new double[][]{{100000000}}, //hp
            new short[]{159, 160, 161}, //outfit
            new short[]{68, 69, 70, 71, 72}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000}, {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200}, {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _1_PHUT
    );
    public static final BossData MAP_DAU_DINH = new BossData(
            "Mập đầu đinh", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            15000, //dame
            new double[][]{{300_000_000L}}, //hp
            new short[]{165, 166, 167}, //outfit
            new short[]{64, 65, 63, 66, 67}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000}, {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200}, {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _1_PHUT
    );
    public static final BossData RAMBO = new BossData(
            "Rambo", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            20000, //dame
            new double[][]{{500_000_000L}}, //hp
            new short[]{162, 163, 164}, //outfit
            new short[]{73, 74, 75, 76, 77}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000}, {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200}, {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200}, {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _1_PHUT
    );

    //--------------------------------------------------------------------------Boss cold
    public static final BossData COOLER = new BossData(
            "Cooler", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            120000000, //dame
            new double[][]{{100_000_000_000L}}, //hp
            new short[]{317, 318, 319}, //outfit
            new short[]{110}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000}, {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200}, {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200}, {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _5_PHUT
    );

    public static final BossData COOLER2 = new BossData(
            "Cooler 2", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            150000000, //dame
            new double[][]{{200_000_000_000L}}, //hp
            new short[]{320, 321, 322}, //outfit
            new short[]{110}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000}, {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200}, {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200}, {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _5_GIAY
    );

    //--------------------------------------------------------------------------Tiểu đội sát thủ
    public static final BossData SO4 = new BossData(
            "Số 4", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            300000, //dame
            new double[][]{{1_000_000_000}}, //hp
            new short[]{168, 169, 170}, //outfit
            new short[]{82, 83, 79}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000}, {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200}, {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200}, {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _30_GIAY
    );
    public static final BossData SO3 = new BossData(
            "Số 3", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            350000, //dame
            new double[][]{{3_000_000_000L}}, //hp
            new short[]{174, 175, 176}, //outfit
            new short[]{82, 83, 79}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000}, {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200}, {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200}, {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _30_GIAY
    );
    public static final BossData SO2 = new BossData(
            "Số 2", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            400000, //dame
            new double[][]{{5_000_000_000L}}, //hp
            new short[]{171, 172, 173}, //outfit
            new short[]{82, 83, 79}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000}, {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200}, {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200}, {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _30_GIAY
    );
    public static final BossData SO1 = new BossData(
            "Số 1", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            400000, //dame
            new double[][]{{7_000_000_000L}}, //hp
            new short[]{177, 178, 179}, //outfit
            new short[]{82, 83, 79}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000}, {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200}, {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200}, {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _30_GIAY
    );
    public static final BossData TIEU_DOI_TRUONG = new BossData(
            "Tiểu đội trưởng", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            500000, //dame
            new double[][]{{10_000_000_000L}}, //hp
            new short[]{180, 181, 182}, //outfit
            new short[]{82, 83, 79}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000}, {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200}, {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200}, {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _5_PHUT
    );

    //--------------------------------------------------------------------------Fide đại ca
    public static final BossData FIDE_DAI_CA_1 = new BossData(
            "Fide đại ca 1", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            500000, //dame
            new double[][]{{5_000_000_000L}}, //hp
            new short[]{183, 184, 185}, //outfit
            new short[]{80}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000}, {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200}, {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200}, {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _5_PHUT
    );

    public static final BossData FIDE_DAI_CA_2 = new BossData(
            "Fide đại ca 2", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            500000, //dame
            new double[][]{{7_000_000_000L}}, //hp
            new short[]{186, 187, 188}, //outfit
            new short[]{80}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000}, {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200}, {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200}, {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _5_GIAY
    );

    public static final BossData FIDE_DAI_CA_3 = new BossData(
            "Fide đại ca 3", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            600000, //dame
            new double[][]{{10_000_000_000L}}, //hp
            new short[]{189, 190, 191}, //outfit
            new short[]{80}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000}, {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200}, {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200}, {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _5_GIAY
    );

    //--------------------------------------------------------------------------
    public static final BossData ANDROID_19 = new BossData(
            "Android 19", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            3000000, //dame
            new double[][]{{3_000_000_000L}}, //hp
            new short[]{249, 250, 251}, //outfit
            new short[]{93, 94, 96}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000}, {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200}, {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200}, {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _1_PHUT
    );

    public static final BossData ANDROID_20 = new BossData(
            "Dr.Kôrê", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            3000000, //dame
            new double[][]{{7_000_000_000L}}, //hp
            new short[]{255, 256, 257}, //outfit
            new short[]{93, 94, 96}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000}, {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200}, {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200}, {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _5_PHUT,
            true
    );
    public static final BossData ANDROID_13 = new BossData(
            "Android 13", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            2500000, //dame
            new double[][]{{5_000_000_000L}}, //hp
            new short[]{252, 253, 254}, //outfit
            new short[]{82, 83, 79}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000}, {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200}, {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200}, {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _1_PHUT, true
    );

    public static final BossData ANDROID_14 = new BossData(
            "Android 14", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            2500000, //dame
            new double[][]{{4_000_000_000L}}, //hp
            new short[]{246, 247, 248}, //outfit
            new short[]{82, 83, 79}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000}, {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200}, {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200}, {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _1_PHUT, true
    );
    public static final BossData ANDROID_15 = new BossData(
            "Android 15", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            2500000, //dame
            new double[][]{{7_000_000_000L}}, //hp
            new short[]{261, 262, 263}, //outfit
            new short[]{82, 83, 79}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000}, {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200}, {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200}, {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _1_PHUT, true
    );
    public static final BossData PIC = new BossData(
            "Pic", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            13000000, //dame
            new double[][]{{3_000_000_000L}}, //hp
            new short[]{237, 238, 239}, //outfit
            new short[]{82, 83, 79}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000}, {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200}, {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200}, {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _1_PHUT, true
    );
    public static final BossData POC = new BossData(
            "Poc", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            12500000, //dame
            new double[][]{{7_000_000_000L}}, //hp
            new short[]{240, 241, 242}, //outfit
            new short[]{82, 83, 79}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000}, {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200}, {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200}, {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _1_PHUT
    );

    public static final BossData KINGKONG = new BossData(
            "King Kong", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            13500000, //dame
            new double[][]{{5_000_000_000L}}, //hp
            new short[]{243, 244, 245}, //outfit
            new short[]{97, 98, 99}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000}, {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200}, {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200}, {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _5_PHUT, true
    );

    //--------------------------------------------------------------------------Boss berus
    public static final BossData WHIS = new BossData(
            "Thần Thiên Sứ", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            250000000, //dame
            new double[][]{{2_000_000_000_000L}}, //hp
            new short[]{838, 839, 840}, //outfit
            new short[]{154}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000}, {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200}, {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200}, {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _15_PHUT
    );

    public static final BossData BILL = new BossData(
            "Thần Hủy Diệt", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            300000000, //dame
            new double[][]{{5_000_000_000_000L}}, //hp
            new short[]{508, 509, 510}, //outfit
            new short[]{166}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000}, {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200}, {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200}, {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _15_PHUT
    );
    //--------------------------------------------------------------------------Boss berus
    public static final BossData VADOS = new BossData(
            "Thần Thiên Sứ Vados", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            250000000, //dame
            new double[][]{{5_000_000_000_000L}}, //hp
            new short[]{530, 531, 532}, //outfit
            new short[]{167}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000}, {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200}, {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200}, {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _10_PHUT
    );

    public static final BossData CHAMPA = new BossData(
            "Thần Hủy Diệt Champa", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            300000000, //dame
            new double[][]{{5_000_000_000_000L}}, //hp
            new short[]{511, 512, 513}, //outfit
            new short[]{168}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000}, {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200}, {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200}, {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _10_PHUT
    );
    //doremon
    public static final BossData XEKO = new BossData(
            "Sói Basil (SKiện)", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            2500, //dame
            new double[][]{{2_000_000_000L}}, //hp
            new short[]{745, 746, 747}, //outfit
            new short[]{1, 2, 4, 5, 6, 7, 8, 9, 10, 11, 13, 14, 15, 16, 17, 18, 19, 20, 27, 28, 29, 30, 31, 32, 33, 34, 35}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000}, {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200}, {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200}, {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _10_PHUT
    );
    public static final BossData CHAIEN = new BossData(
            "Sói Lavender (SKiện)", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            2500, //dame
            new double[][]{{2_000_000_000L}}, //hp
            new short[]{748, 749, 750}, //outfit
            new short[]{1, 2, 4, 5, 6, 7, 8, 9, 10, 11, 13, 14, 15, 16, 17, 18, 19, 20, 27, 28, 29, 30, 31, 32, 33, 34, 35}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000}, {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200}, {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200}, {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _10_PHUT
    );
    public static final BossData XUKA = new BossData(
            "Sói Bergamo (SKiện)", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            2500, //dame
            new double[][]{{2_000_000_000L}}, //hp
            new short[]{751, 752, 753}, //outfit
            new short[]{1, 2, 4, 5, 6, 7, 8, 9, 10, 11, 13, 14, 15, 16, 17, 18, 19, 20, 27, 28, 29, 30, 31, 32, 33, 34, 35}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000}, {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200}, {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200}, {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _10_PHUT
    );
    public static final BossData NOBITA = new BossData(
            "Hashibirao (SKiện)", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            2500, //dame
            new double[][]{{1, 2, 4, 5, 6, 7, 8, 9, 10, 11, 13, 14, 15, 16, 17, 18, 19, 20, 27, 28, 29, 30, 31, 32, 33, 34, 35}}, //hp
            new short[]{1122, 1123, 1124}, //outfit
            new short[]{17}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000}, {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200}, {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200}, {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _10_PHUT
    );

    public static final BossData DOREMON = new BossData(
            "Nezuko(SKiện)", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            3000, //dame
            new double[][]{{2_000_000_000L}}, //hp
            new short[]{1128, 1129, 1130}, //outfit
            new short[]{1, 2, 4, 5, 6, 7, 8, 9, 10, 11, 13, 14, 15, 16, 17, 18, 19, 20, 27, 28, 29, 30, 31, 32, 33, 34, 35}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000}, {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200}, {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200}, {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _10_PHUT
    );
//siêu trộm------------------------------------
    public static final BossData SIEU_TROM = new BossData(
            "King Kong Sành điệu", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            100, //dame
            new double[][]{{99}}, //hp
            new short[]{1327, 1328, 1329},
            new short[]{174}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 5, 500},
                {Skill.THAI_DUONG_HA_SAN, 5, 10000}
            },
            _10_PHUT
    );

    //--------------------------------------------------------------------------Boss CHILLED
    public static final BossData CHILL = new BossData(
            "Chilled", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            25000000, //dame
            new double[][]{{200_000_000_000L}}, //hp
            new short[]{1024, 1025, 1026}, //outfit
            new short[]{163}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000}, {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200}, {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200}, {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _15_PHUT
    );

    public static final BossData CHILL2 = new BossData(
            "Chilled 2", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            30000000, //dame
            new double[][]{{500_000_000_000L}}, //hp
            new short[]{1021, 1022, 1023}, //outfit
            new short[]{163}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000}, {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200}, {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200}, {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _1_PHUT
    );

    public static final BossData BULMA = new BossData(
            "Fide Baby (Đệ Tử)", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            30000, //dame
            new double[][]{{100_000_000_000L}}, //hp
            new short[]{1720, 1721, 1722}, //outfit
            new short[]{7, 8}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000}, {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200}, {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200}, {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _10_PHUT
    );

    public static final BossData POCTHO = new BossData(
            "Xên Baby (Đệ Tử)", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            300000, //dame
            new double[][]{{100_000_000_000L}}, //hp
            new short[]{1717, 1718, 1719}, //outfit
            new short[]{0, 1}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000}, {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200}, {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200}, {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _10_PHUT
    );

    public static final BossData CHICHITHO = new BossData(
            "Bư Baby (Đệ Tử)", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            300000, //dame
            new double[][]{{100_000_000_000L}}, //hp
            new short[]{1723, 1724, 1725}, //outfit
            new short[]{14, 15}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000}, {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200}, {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200}, {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.GALICK, 1, 1000}
            },
            _10_PHUT
    );

    public static final BossData SUPER_BLACK_ROSE = new BossData(
            " Goku SJJ3 Rose", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            300000000, //dame
            new double[][]{{5_000_000_000_000L}}, //hp
            new short[]{793, 794, 795}, //outfit
            new short[]{105, 106, 107, 108, 109, 110}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 7, 5000},
                {Skill.THAI_DUONG_HA_SAN, 5, 50000},
                {Skill.ANTOMIC, 7, 2000},
                {Skill.GALICK, 1, 1000}
            },
            _5_PHUT
    );
    public static final BossData ZAMAS_TOI_THUONG = new BossData(
            "Zamas Kaioshin", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            2000000000, //dame
            new double[][]{{1_000_000_000_000L}}, //hp
            new short[]{903, 904, 905}, //outfit
            new short[]{105, 106, 107, 5}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 7, 3000},
                {Skill.THAI_DUONG_HA_SAN, 5, 50000},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.GALICK, 1, 1000}
            },
            _5_PHUT
    );

    public static final BossData BONG_BANG = new BossData(
            " Black Rose 5.0", //name
            ConstPlayer.TRAI_DAT, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            1000000000, //dame
            new double[][]{{5_000_000_000_000_000L}}, //hp
            new short[]{1804, 1801, 1802}, //outfit
            new short[]{156}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 7, 3000},
                {Skill.THAI_DUONG_HA_SAN, 5, 50000},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.GALICK, 1, 1000}
            },
            _30_PHUT
    );

    public static final BossData SOI_BASIL = new BossData(
            "Majin Form Siêu Cấp", //name
            ConstPlayer.TRAI_DAT, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            10000, //dame
            new double[][]{{10_000_000_000_000_000L}}, //hp
            new short[]{1832, 1835, 1836}, //outfit
            new short[]{212}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 5, 300},
                {Skill.THAI_DUONG_HA_SAN, 5, 50000},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.GALICK, 1, 1000}
            },
            _30_PHUT
    );

    public static final BossData ITACHI = new BossData(
            "Itachi", //name
            ConstPlayer.TRAI_DAT, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            30000, //dame
            new double[][]{{1_000_000_000_000_000L}}, //hp
            new short[]{1472, 1473, 1474}, //outfit
            new short[]{214}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 5, 300},
                {Skill.THAI_DUONG_HA_SAN, 5, 50000},
                //                    {Skill.TAI_TAO_NANG_LUONG, 5, 10000},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.GALICK, 1, 1000}
            },
            _30_PHUT
    );
    // 3 con bosss sự kiện
    public static final BossData JIREN = new BossData(
            "Thần Chiến Tranh", //name
            ConstPlayer.TRAI_DAT, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            50000, //dame
            new double[][]{{200_000_000_000_000L}}, //hp
            new short[]{1252, 1253, 1254}, //outfit
            new short[]{5, 6, 27, 28, 29, 30, 13, 10, 31, 32, 33, 34, 20, 19, 35, 36, 37, 38}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 5, 300},
                {Skill.THAI_DUONG_HA_SAN, 5, 20000},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.GALICK, 1, 1000}
            },
            _10_PHUT
    );

    public static final BossData TOPPO = new BossData(
            "Thần Mẫu", //name
            ConstPlayer.TRAI_DAT, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            50000, //dame
            new double[][]{{200_000_000_000_000L}}, //hp
            new short[]{1469, 1470, 1471}, //outfit
            new short[]{5, 6, 27, 28, 29, 30, 13, 10, 31, 32, 33, 34, 20, 19, 35, 36, 37, 38}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 5, 300},
                {Skill.THAI_DUONG_HA_SAN, 5, 20000},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.GALICK, 1, 1000}
            },
            _10_PHUT
    );

    public static final BossData JIREN_HOAN_MY = new BossData(
            "Namek Hủy Diệt", //name
            ConstPlayer.TRAI_DAT, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            50000, //dame
            new double[][]{{200_000_000_000_000L}}, //hp
            new short[]{633, 634, 635}, //outfit
            new short[]{5, 6, 27, 28, 29, 30, 13, 10, 31, 32, 33, 34, 20, 19, 35, 36, 37, 38}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 5, 300},
                {Skill.THAI_DUONG_HA_SAN, 5, 20000},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.GALICK, 1, 1000}
            },
            _10_PHUT
    );

    public static final BossData NGUYET_THAN = new BossData(
            "Zeno Tối Thượng", //name
            ConstPlayer.TRAI_DAT, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            70000, //dame
            new double[][]{{5_000_000_000_000L}}, //
            new short[]{1427, 1428, 1429}, //outfit
            new short[]{215}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 5, 300},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.THAI_DUONG_HA_SAN, 7, 50000},
                {Skill.GALICK, 1, 1000}
            },
            _30_PHUT
    );

    public static final BossData MIHAWK = new BossData(
            "CT Super Broly 7.0", //name
            ConstPlayer.TRAI_DAT, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            9000000, //dame
            new double[][]{{5_000_000_000_000L}}, //
            new short[]{1864, 1867, 1868}, //outfit
            new short[]{216}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 5, 300},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.THAI_DUONG_HA_SAN, 7, 50000},
                {Skill.GALICK, 1, 1000}
            },
            _30_PHUT
    );

    public static final BossData ZAMAS_ZOMBIE = new BossData(
            "Zamas Zombie", //name
            ConstPlayer.TRAI_DAT, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            2100000000, //dame
            new double[][]{{5_000_000_000_000L}}, //hp
            new short[]{1541, 1542, 1543}, //outfit
            new short[]{5}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 5, 300},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.THAI_DUONG_HA_SAN, 7, 90000},
                {Skill.GALICK, 1, 1000}
            },
            _5_PHUT
    );

    public static final BossData LUFFY_THAN_NIKA = new BossData(
            "Android 21 Tiến Sĩ", //name
            ConstPlayer.TRAI_DAT, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            10000, //dame
            new double[][]{{5_000_000_000_000L}}, //hp    100k tỷ
            new short[]{1844, 1845, 1846}, //outfit
            new short[]{218}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 5, 300},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.THAI_DUONG_HA_SAN, 5, 50000},
                {Skill.GALICK, 1, 1000}
            },
            _30_PHUT
    );

    public static final BossData LUFFY_GEAR5 = new BossData(
            "Majin Form", //name
            ConstPlayer.TRAI_DAT, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            10000, //dame
            new double[][]{{5_000_000_000_000L}}, //hp  100k tỷ
            new short[]{1837, 1840, 1841}, //outfit
            new short[]{159}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 5, 300},
                {Skill.THAI_DUONG_HA_SAN, 5, 20000},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.GALICK, 1, 1000}
            },
            _30_PHUT
    );

    public static final BossData KAIDO = new BossData(
            "Black Goku 5.0", //name
            ConstPlayer.TRAI_DAT, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            1000000000, //dame
            new double[][]{{5_000_000_000_000L}}, //hp   100k tỷ
            new short[]{1805, 1801, 1802}, //outfit
            new short[]{153}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 5, 300},
                {Skill.THAI_DUONG_HA_SAN, 5, 30000},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.GALICK, 1, 1000}
            },
            _10_PHUT
    );

    public static final BossData NHAT_THAN = new BossData(
            "CT Broly 7.0", //name
            ConstPlayer.TRAI_DAT, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            4000000, //dame
            new double[][]{{500_000_000_000_000L}}, //hp 500k tỷ bos đb
            new short[]{1853, 1856, 1857}, //outfit
            new short[]{217}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 5, 300},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.THAI_DUONG_HA_SAN, 7, 50000},
                {Skill.GALICK, 1, 1000}
            },
            _30_PHUT
    );

    public static final BossData WHIS_DETU = new BossData(
            "Majin Form ", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            1500000, //dame
            new double[][]{{5_000_000_000_000L}}, //hp   5k tỷ
            new short[]{1646, 1647, 1648}, //outfit
            new short[]{52}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000}, {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200}, {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200}, {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 5, 1700},
                {Skill.THAI_DUONG_HA_SAN, 1, 30000},
                {Skill.GALICK, 1, 1000}
            },
            _10_PHUT
    );
    public static final BossData ZENO = new BossData(
            "Thần Zeno Cấp 2", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            25000, //dame
            new double[][]{{1_000_000_000_000_000L}}, //hp
            new short[]{1219, 1220, 1221}, //outfit
            new short[]{217}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 6, 300},
                {Skill.ANTOMIC, 5, 1700},
                {Skill.THAI_DUONG_HA_SAN, 5, 50000},
                {Skill.MASENKO, 5, 1700}
            },
            _10_PHUT
    );
    public static final BossData RONG_DEN = new BossData(
            "Rồng Đen ", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            1500000, //dame
            new double[][]{{70_000_000_000L}}, //hp
            new short[]{1050, 1051, 1052}, //outfit
            new short[]{92, 93, 94}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 3, 500}, {Skill.LIEN_HOAN, 4, 1000}, {Skill.LIEN_HOAN, 5, 1500},
                {Skill.KAMEJOKO, 4, 3000}, {Skill.KAMEJOKO, 5, 4000}, {Skill.KAMEJOKO, 7, 7000},
                {Skill.DICH_CHUYEN_TUC_THOI, 4, 10000},
                {Skill.THAI_DUONG_HA_SAN, 5, 30000}
            },
            _5_PHUT
    );
    public static final BossData GOKU_SUPER = new BossData(
            "Goku Super Saiyan 4", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            3000000, //dame
            new double[][]{{50_000_000_000L}}, //hp
            new short[]{1074, 1075, 1076}, //outfit
            new short[]{155}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 7, 500},
                {Skill.THAI_DUONG_HA_SAN, 5, 15000},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.GALICK, 1, 1000}
            },
            _15_PHUT
    );

    //
//    public static final BossData BROLYDEN = new BossData(
//            "S.Broly Black", //name
//            ConstPlayer.XAYDA, //gender
//            Boss.DAME_NORMAL, //type dame
//            Boss.HP_NORMAL, //type hp
//            300000, //dame
//           new double[][]{{1000000000}}, //hp
//            new short[]{1080, 1081, 1082}, //outfit
//            new short[]{14}, //map join
//            new int[][]{ //skill
//                {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650}, {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
//                {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800}, {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
//                {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700}, {Skill.ANTOMIC, 7, 2000},
//                {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300}, {Skill.MASENKO, 6, 1500},
//                {Skill.TAI_TAO_NANG_LUONG, 1, 5000}, {Skill.TAI_TAO_NANG_LUONG, 3, 10000}, {Skill.TAI_TAO_NANG_LUONG, 5, 25000},
//                {Skill.TAI_TAO_NANG_LUONG, 6, 30000}, {Skill.TAI_TAO_NANG_LUONG, 7, 50000}
//            },
//            _5_PHUT
//    );
//    
//    public static final BossData BROLYXANH = new BossData(
//            "S.Broly SNamếc", //name
//            ConstPlayer.XAYDA, //gender
//            Boss.DAME_NORMAL, //type dame
//            Boss.HP_NORMAL, //type hp
//            300000, //dame
//           new double[][]{{1000000000}}, //hp
//            new short[]{1086, 1087, 1088}, //outfit
//            new short[]{14}, //map join
//            new int[][]{ //skill
//                {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650}, {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
//                {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800}, {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
//                {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700}, {Skill.ANTOMIC, 7, 2000},
//                {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300}, {Skill.MASENKO, 6, 1500},
//                {Skill.TAI_TAO_NANG_LUONG, 1, 5000}, {Skill.TAI_TAO_NANG_LUONG, 3, 10000}, {Skill.TAI_TAO_NANG_LUONG, 5, 25000},
//                {Skill.TAI_TAO_NANG_LUONG, 6, 30000}, {Skill.TAI_TAO_NANG_LUONG, 7, 50000}
//            },
//            _5_PHUT
//    ); 
//    
//    public static final BossData BROLYVANG = new BossData(
//            "S.Broly SSJ", //name
//            ConstPlayer.XAYDA, //gender
//            Boss.DAME_NORMAL, //type dame
//            Boss.HP_NORMAL, //type hp
//            300000, //dame
//           new double[][]{{1000000000}}, //hp
//            new short[]{1083, 1084, 1085}, //outfit
//            new short[]{14}, //map join
//            new int[][]{ //skill
//                {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650}, {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
//                {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800}, {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
//                {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700}, {Skill.ANTOMIC, 7, 2000},
//                {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300}, {Skill.MASENKO, 6, 1500},
//                {Skill.TAI_TAO_NANG_LUONG, 1, 5000}, {Skill.TAI_TAO_NANG_LUONG, 3, 10000}, {Skill.TAI_TAO_NANG_LUONG, 5, 25000},
//                {Skill.TAI_TAO_NANG_LUONG, 6, 30000}, {Skill.TAI_TAO_NANG_LUONG, 7, 50000}
//            },
//            _5_PHUT
//    ); 
//    
    public static final BossData BLACKGOKU = new BossData(
            "Black Goku", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            200000000, //dame
            new double[][]{{2_000_000_000_000L}}, //hp
            new short[]{550, 551, 552}, //outfit
            new short[]{92, 93, 94}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 7, 650},
                {Skill.KAMEJOKO, 7, 2000},
                {Skill.ANTOMIC, 7, 2000},
                {Skill.THAI_DUONG_HA_SAN, 7, 15000},},
            _5_PHUT
    );

    public static final BossData SUPERBLACKGOKU = new BossData(
            "Super Black Goku", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            800000000, //dame
            new double[][]{{3_000_000_000_000L}}, //hp
            new short[]{553, 551, 552}, //outfit
            new short[]{92, 93, 94}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 7, 650},
                {Skill.KAMEJOKO, 7, 2000},
                {Skill.ANTOMIC, 7, 2000},
                {Skill.THAI_DUONG_HA_SAN, 7, 15000},},
            _5_PHUT
    );

    public static final BossData HOA_HONG = BossData.builder()
            .name("Hoa Hồng")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(0)
            .hp(new double[][]{{100}})
            .outfit(new short[]{706, 707, 708})
            .mapJoin(new short[]{})
            .skillTemp(new int[][]{})
            .secondsRest(_5_PHUT)
            .build();

    public static final BossData SANTA_CLAUS = BossData.builder()
            .name("Ông già Nôen")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(0)
            .hp(new double[][]{{500000}})
            .outfit(new short[]{657, 658, 659})
            .mapJoin(new short[]{})
            .skillTemp(new int[][]{})
            .secondsRest(_5_PHUT)
            .build();

    public static final BossData QILIN = BossData.builder()
            .name("Lân con")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(0)
            .hp(new double[][]{{5000000}})
            .outfit(new short[]{763, 764, 765})
            .mapJoin(new short[]{})
            .skillTemp(new int[][]{})
            .secondsRest(_5_PHUT)
            .build();

    public static final BossData MABU_MAP = BossData.builder()
            .name("Mabư")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(7500000)
            .hp(new double[][]{{2000000000000L}})
            .outfit(new short[]{297, 298, 299})
            .mapJoin(new short[]{52})
            .skillTemp(new int[][]{
        {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650}, {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
        {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800}, {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
        {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700}, {Skill.ANTOMIC, 7, 2000},
        {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300}, {Skill.MASENKO, 6, 1500},
        {Skill.TAI_TAO_NANG_LUONG, 1, 5000}, {Skill.TAI_TAO_NANG_LUONG, 3, 10000}, {Skill.TAI_TAO_NANG_LUONG, 5, 25000},
        {Skill.TAI_TAO_NANG_LUONG, 6, 30000}, {Skill.TAI_TAO_NANG_LUONG, 7, 50000}
    })
            .secondsRest(_5_PHUT)
            .build();

    public static final BossData MABU_MAP2 = BossData.builder()
            .name("Bư Mập")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(750000)
            .hp(new double[][]{{100000000}})
            .outfit(new short[]{297, 298, 299})
            .mapJoin(new short[]{})
            .skillTemp(new int[][]{
        {Skill.DEMON, 3, 450}, {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
        {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800}, {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
        {Skill.ANTOMIC, 3, 1200},
        {Skill.TAI_TAO_NANG_LUONG, 1, 25000}
    })
            .secondsRest(_5_PHUT)
            .build();

    public static final BossData SUPER_BU = BossData.builder()
            .name("Super Bư")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(0)
            .hp(new double[][]{{5000000000L}})
            .outfit(new short[]{427, 428, 429})
            .mapJoin(new short[]{127})
            .skillTemp(new int[][]{
        {Skill.DEMON, 1, 5000},
        {Skill.TAI_TAO_NANG_LUONG, 1, 5000}, {Skill.TAI_TAO_NANG_LUONG, 3, 10000}, {Skill.TAI_TAO_NANG_LUONG, 5, 25000},
        {Skill.TAI_TAO_NANG_LUONG, 6, 30000}, {Skill.TAI_TAO_NANG_LUONG, 7, 50000}
    })
            .secondsRest(_30_GIAY)
            .build();

    public static final BossData KID_BU = BossData.builder()
            .name("Kid Bư")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(1200000)
            .hp(new double[][]{{7000000000L}})
            .outfit(new short[]{439, 440, 441})
            .mapJoin(new short[]{})
            .skillTemp(new int[][]{
        {Skill.DEMON, 3, 450}, {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
        {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800}, {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
        {Skill.ANTOMIC, 3, 1200},
        {Skill.TAI_TAO_NANG_LUONG, 1, 25000}
    })
            .secondsRest(_30_GIAY)
            .build();

    public static final BossData BU_TENK = BossData.builder()
            .name("Bư Tênk")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(0)
            .hp(new double[][]{{8000000000L}})
            .outfit(new short[]{439, 440, 441})
            .mapJoin(new short[]{127})
            .skillTemp(new int[][]{
        {Skill.DEMON, 1, 5000},
        {Skill.TAI_TAO_NANG_LUONG, 1, 5000}, {Skill.TAI_TAO_NANG_LUONG, 3, 10000}, {Skill.TAI_TAO_NANG_LUONG, 5, 25000},
        {Skill.TAI_TAO_NANG_LUONG, 6, 30000}, {Skill.TAI_TAO_NANG_LUONG, 7, 50000}
    })
            .secondsRest(_30_GIAY)
            .build();

    public static final BossData BU_HAN = BossData.builder()
            .name("Bư Han")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(1200000)
            .hp(new double[][]{{9000000000L}})
            .outfit(new short[]{427, 428, 429})
            .mapJoin(new short[]{})
            .skillTemp(new int[][]{
        {Skill.DEMON, 3, 450}, {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
        {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800}, {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
        {Skill.ANTOMIC, 3, 1200},
        {Skill.QUA_CAU_KENH_KHI, 7, 1200},
        {Skill.TAI_TAO_NANG_LUONG, 1, 25000}
    })
            .secondsRest(_30_GIAY)
            .build();

    public static final BossData DRABULA_TANG1 = BossData.builder()
            .name("Drabula")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(250000)
            .hp(new double[][]{{500000000}})
            .outfit(new short[]{418, 419, 420})
            .mapJoin(new short[]{114})
            .skillTemp(new int[][]{
        {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650}, {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
        {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800}, {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
        {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700}, {Skill.ANTOMIC, 7, 2000},
        {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300}, {Skill.MASENKO, 6, 1500},
        {Skill.TAI_TAO_NANG_LUONG, 1, 5000}, {Skill.TAI_TAO_NANG_LUONG, 3, 10000}, {Skill.TAI_TAO_NANG_LUONG, 5, 25000},
        {Skill.TAI_TAO_NANG_LUONG, 6, 30000}, {Skill.TAI_TAO_NANG_LUONG, 7, 50000}
    })
            .secondsRest(_30_GIAY)
            .build();

    public static final BossData DRABULA_TANG5 = BossData.builder()
            .name("Drabula")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(500000)
            .hp(new double[][]{{500000000}})
            .outfit(new short[]{418, 419, 420})
            .mapJoin(new short[]{119})
            .skillTemp(new int[][]{
        {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650}, {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
        {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800}, {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
        {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700}, {Skill.ANTOMIC, 7, 2000},
        {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300}, {Skill.MASENKO, 6, 1500},
        {Skill.TAI_TAO_NANG_LUONG, 1, 5000}, {Skill.TAI_TAO_NANG_LUONG, 3, 10000}, {Skill.TAI_TAO_NANG_LUONG, 5, 25000},
        {Skill.TAI_TAO_NANG_LUONG, 6, 30000}, {Skill.TAI_TAO_NANG_LUONG, 7, 50000}
    })
            .secondsRest(_30_GIAY)
            .build();

    public static final BossData DRABULA_TANG6 = BossData.builder()
            .name("Drabula")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(500000)
            .hp(new double[][]{{1000000000}})
            .outfit(new short[]{418, 419, 420})
            .mapJoin(new short[]{120})
            .skillTemp(new int[][]{
        {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650}, {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
        {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800}, {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
        {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700}, {Skill.ANTOMIC, 7, 2000},
        {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300}, {Skill.MASENKO, 6, 1500},
        {Skill.TAI_TAO_NANG_LUONG, 1, 5000}, {Skill.TAI_TAO_NANG_LUONG, 3, 10000}, {Skill.TAI_TAO_NANG_LUONG, 5, 25000},
        {Skill.TAI_TAO_NANG_LUONG, 6, 30000}, {Skill.TAI_TAO_NANG_LUONG, 7, 50000}
    })
            .secondsRest(_5_PHUT)
            .build();

    public static final BossData BUIBUI_TANG2 = BossData.builder()
            .name("BuiBui")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(250000)
            .hp(new double[][]{{500000000}})
            .outfit(new short[]{451, 452, 453})
            .mapJoin(new short[]{115})
            .skillTemp(new int[][]{
        {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650}, {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
        {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800}, {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
        {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700}, {Skill.ANTOMIC, 7, 2000},
        {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300}, {Skill.MASENKO, 6, 1500},
        {Skill.TAI_TAO_NANG_LUONG, 1, 5000}, {Skill.TAI_TAO_NANG_LUONG, 3, 10000}, {Skill.TAI_TAO_NANG_LUONG, 5, 25000},
        {Skill.TAI_TAO_NANG_LUONG, 6, 30000}, {Skill.TAI_TAO_NANG_LUONG, 7, 50000}
    })
            .secondsRest(_30_GIAY)
            .build();

    public static final BossData BUIBUI_TANG3 = BossData.builder()
            .name("BuiBui")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(250000)
            .hp(new double[][]{{500000000}})
            .outfit(new short[]{451, 452, 453})
            .mapJoin(new short[]{117})
            .skillTemp(new int[][]{
        {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650}, {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
        {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800}, {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
        {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700}, {Skill.ANTOMIC, 7, 2000},
        {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300}, {Skill.MASENKO, 6, 1500},
        {Skill.TAI_TAO_NANG_LUONG, 1, 5000}, {Skill.TAI_TAO_NANG_LUONG, 3, 10000}, {Skill.TAI_TAO_NANG_LUONG, 5, 25000},
        {Skill.TAI_TAO_NANG_LUONG, 6, 30000}, {Skill.TAI_TAO_NANG_LUONG, 7, 50000}
    })
            .secondsRest(_30_GIAY)
            .build();

    public static final BossData CALICH_TANG5 = BossData.builder()
            .name("Ca Đíc")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(350000)
            .hp(new double[][]{{500000000}})
            .outfit(new short[]{103, 16, 17})
            .mapJoin(new short[]{119})
            .skillTemp(new int[][]{
        {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650}, {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
        {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800}, {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
        {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700}, {Skill.ANTOMIC, 7, 2000},
        {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300}, {Skill.MASENKO, 6, 1500},
        {Skill.TAI_TAO_NANG_LUONG, 1, 5000}, {Skill.TAI_TAO_NANG_LUONG, 3, 10000}, {Skill.TAI_TAO_NANG_LUONG, 5, 25000},
        {Skill.TAI_TAO_NANG_LUONG, 6, 30000}, {Skill.TAI_TAO_NANG_LUONG, 7, 50000}
    })
            .secondsRest(_30_GIAY)
            .build();

    public static final BossData GOKU_TANG5 = BossData.builder()
            .name("Gôku")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(350000)
            .hp(new double[][]{{500000000}})
            .outfit(new short[]{101, 1, 2})
            .mapJoin(new short[]{119})
            .skillTemp(new int[][]{
        {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650}, {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
        {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800}, {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
        {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700}, {Skill.ANTOMIC, 7, 2000},
        {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300}, {Skill.MASENKO, 6, 1500},
        {Skill.TAI_TAO_NANG_LUONG, 1, 5000}, {Skill.TAI_TAO_NANG_LUONG, 3, 10000}, {Skill.TAI_TAO_NANG_LUONG, 5, 25000},
        {Skill.TAI_TAO_NANG_LUONG, 6, 30000}, {Skill.TAI_TAO_NANG_LUONG, 7, 50000}
    })
            .secondsRest(_30_GIAY)
            .build();

    public static final BossData YACON_TANG4 = BossData.builder()
            .name("Yacôn")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(350000)
            .hp(new double[][]{{500000000}})
            .outfit(new short[]{415, 416, 417})
            .mapJoin(new short[]{118})
            .skillTemp(new int[][]{
        {Skill.DEMON, 3, 450}, {Skill.DEMON, 6, 400}, {Skill.DRAGON, 7, 650}, {Skill.DRAGON, 1, 500}, {Skill.GALICK, 5, 480},
        {Skill.KAMEJOKO, 7, 2000}, {Skill.KAMEJOKO, 6, 1800}, {Skill.KAMEJOKO, 4, 1500}, {Skill.KAMEJOKO, 2, 1000},
        {Skill.ANTOMIC, 3, 1200}, {Skill.ANTOMIC, 5, 1700}, {Skill.ANTOMIC, 7, 2000},
        {Skill.MASENKO, 1, 800}, {Skill.MASENKO, 5, 1300}, {Skill.MASENKO, 6, 1500},
        {Skill.TAI_TAO_NANG_LUONG, 1, 5000}, {Skill.TAI_TAO_NANG_LUONG, 3, 10000}, {Skill.TAI_TAO_NANG_LUONG, 5, 25000},
        {Skill.TAI_TAO_NANG_LUONG, 6, 30000}, {Skill.TAI_TAO_NANG_LUONG, 7, 50000}
    })
            .secondsRest(_30_GIAY)
            .build();

    public static final BossData XEN_MAX = BossData.builder()
        .name("Thần Zamas Tối Thượng")
        .gender(ConstPlayer.XAYDA)
        .typeDame(Boss.DAME_NORMAL)
        .typeHp(Boss.HP_NORMAL)
        .dame(2100000000) // 500 nghìn tỷ dame
        .hp(new double[][]{{10_000_000_000_000_000D}}) // 1e18 HP (1 quintillion)
        .outfit(new short[]{1312, 1313, 1314})
        .mapJoin(new short[]{5, 108, 109, 110})
        .skillTemp(new int[][]{
        {Skill.GALICK, 7, 100},
        {Skill.KAMEJOKO, 7, 1000},
        {Skill.ANTOMIC, 7, 1000},
        {Skill.THAI_DUONG_HA_SAN, 7, 30000},
        {Skill.QUA_CAU_KENH_KHI, 7, 5000},
        {Skill.TAI_TAO_NANG_LUONG, 7, 100000},
        {Skill.BIEN_KHI, 7, 50000},
        {Skill.KHIEN_NANG_LUONG, 7, 70000},
        {Skill.BIEN_HINH, 6, 50000},
        {Skill.TAI_TAO_SUC_MANH, 1, 100},
        {Skill.HUYT_SAO, 7, 30000}

        })
            .secondsRest(_10_PHUT)
            .build();

    public static final BossData SOI_HEC_QUYN = BossData.builder()
            .name("Sói Hẹc Quyn")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(400000)
            .hp(new double[][]{{500000000}})
            .outfit(new short[]{394, 395, 396})
            .mapJoin(new short[]{129})
            .skillTemp(new int[][]{
        {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300}, {Skill.DRAGON, 7, 700},
        {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200}, {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
        {Skill.GALICK, 1, 100}
    })
            .secondsRest(_1_PHUT)
            .build();

    public static final BossData O_DO = BossData.builder()
            .name("Ở Dơ")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(500000)
            .hp(new double[][]{{1000000000}})
            .outfit(new short[]{400, 401, 402})
            .mapJoin(new short[]{129})
            .skillTemp(new int[][]{
        {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300}, {Skill.DRAGON, 7, 700},
        {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200}, {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
        {Skill.GALICK, 1, 100}
    })
            .secondsRest(_1_PHUT)
            .build();

    public static final BossData XINBATO = BossData.builder()
            .name("Xinbatô")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(600000)
            .hp(new double[][]{{1000000000}})
            .outfit(new short[]{359, 360, 361})
            .mapJoin(new short[]{129})
            .skillTemp(new int[][]{
        {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300}, {Skill.DRAGON, 7, 700},
        {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200}, {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
        {Skill.GALICK, 1, 100}
    })
            .secondsRest(_1_PHUT)
            .build();

    public static final BossData CHA_PA = BossData.builder()
            .name("Cha pa")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(650000)
            .hp(new double[][]{{1000000000}})
            .outfit(new short[]{362, 363, 364})
            .mapJoin(new short[]{129})
            .skillTemp(new int[][]{
        {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300}, {Skill.DRAGON, 7, 700},
        {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200}, {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
        {Skill.GALICK, 1, 100}
    })
            .secondsRest(_1_PHUT)
            .build();

    public static final BossData PON_PUT = BossData.builder()
            .name("Pon put")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(700000)
            .hp(new double[][]{{5000000000L}})
            .outfit(new short[]{365, 366, 367})
            .mapJoin(new short[]{129})
            .skillTemp(new int[][]{
        {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300}, {Skill.DRAGON, 7, 700},
        {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200}, {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
        {Skill.GALICK, 1, 100}
    })
            .secondsRest(_1_PHUT)
            .build();

    public static final BossData CHAN_XU = BossData.builder()
            .name("Chan xư")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(705000)
            .hp(new double[][]{{10000000000L}})
            .outfit(new short[]{371, 372, 373})
            .mapJoin(new short[]{129})
            .skillTemp(new int[][]{
        {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300}, {Skill.DRAGON, 7, 700},
        {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200}, {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
        {Skill.GALICK, 1, 100}
    })
            .secondsRest(_1_PHUT)
            .build();

    public static final BossData TAU_PAY_PAY = BossData.builder()
            .name("Tàu Pảy Pảy")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(800000)
            .hp(new double[][]{{20000000000L}})
            .outfit(new short[]{92, 93, 94})
            .mapJoin(new short[]{129})
            .skillTemp(new int[][]{
        {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300}, {Skill.DRAGON, 7, 700},
        {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200}, {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
        {Skill.GALICK, 1, 100}
    })
            .secondsRest(_1_PHUT)
            .build();

    public static final BossData YAMCHA = BossData.builder()
            .name("Yamcha")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(900000)
            .hp(new double[][]{{30000000000L}})
            .outfit(new short[]{374, 375, 376})
            .mapJoin(new short[]{129})
            .skillTemp(new int[][]{
        {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300}, {Skill.DRAGON, 7, 700},
        {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200}, {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
        {Skill.GALICK, 1, 100}
    })
            .secondsRest(_1_PHUT)
            .build();

    public static final BossData JACKY_CHUN = BossData.builder()
            .name("Jacky Chun")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(1000000)
            .hp(new double[][]{{50000000000L}})
            .outfit(new short[]{356, 357, 358})
            .mapJoin(new short[]{129})
            .skillTemp(new int[][]{
        {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300}, {Skill.DRAGON, 7, 700},
        {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200}, {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
        {Skill.GALICK, 1, 100}
    })
            .secondsRest(_1_PHUT)
            .build();

    public static final BossData THIEN_XIN_HANG = BossData.builder()
            .name("Thiên Xin Hăng")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(1500000)
            .hp(new double[][]{{80000000000L}})
            .outfit(new short[]{368, 369, 370})
            .mapJoin(new short[]{129})
            .skillTemp(new int[][]{
        {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300}, {Skill.DRAGON, 7, 700},
        {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200}, {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
        {Skill.THAI_DUONG_HA_SAN, 1, 15000}
    })
            .secondsRest(_1_PHUT)
            .build();

    public static final BossData THIEN_XIN_HANG_CLONE = BossData.builder()
            .name("Thiên Xin Hăng")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(75000)
            .hp(new double[][]{{200000000}})
            .outfit(new short[]{368, 369, 370})
            .mapJoin(new short[]{129})
            .skillTemp(new int[][]{
        {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300}, {Skill.DRAGON, 7, 700},
        {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200}, {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
        {Skill.THAI_DUONG_HA_SAN, 1, 15000}
    })
            .secondsRest(_1_PHUT)
            .build();
    public static final BossData LIU_LIU = BossData.builder()
            .name("Liu Liu")
            .gender(ConstPlayer.TRAI_DAT)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(2000000)
            .hp(new double[][]{{100000000000L}})
            .outfit(new short[]{397, 398, 399})
            .mapJoin(new short[]{129})
            .skillTemp(new int[][]{
        {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300}, {Skill.DRAGON, 7, 700},
        {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200}, {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
        {Skill.GALICK, 1, 100}
    })
            .secondsRest(_1_PHUT)
            .build();

    public static final BossData NGO_KHONG = new BossData(
            "Tôn Ngộ Không", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            500000, //dame
            new double[][]{{500000000}}, //hp
            new short[]{462, 463, 464}, //outfit
            new short[]{124}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300}, {Skill.DRAGON, 7, 700},
                {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200}, {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
                {Skill.GALICK, 1, 100}
            },
            _5_PHUT
    );

    public static final BossData BAT_GIOI = BossData.builder()
            .name("Chư Bát Giới")
            .gender(ConstPlayer.XAYDA)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(500000)
            .hp(new double[][]{{500000000}})
            .outfit(new short[]{465, 466, 467})
            .mapJoin(new short[]{124})
            .skillTemp(new int[][]{
        {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300}, {Skill.DRAGON, 7, 700},
        {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200}, {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
        {Skill.GALICK, 1, 100}
    })
            .secondsRest(_5_PHUT)
            .build();

    public static final BossData FIDEGOLD = BossData.builder()
            .name("Fide Vàng  %1")
            .gender(ConstPlayer.XAYDA)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(500000)
            .hp(new double[][]{{1000000000}})
            .outfit(new short[]{502, 503, 504})
            .mapJoin(new short[]{6})
            .skillTemp(new int[][]{
        {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300}, {Skill.DRAGON, 7, 700},
        {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200}, {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
        {Skill.GALICK, 1, 100}
    })
            .secondsRest(_5_PHUT)
            .build();

    public static final BossData CUMBER = BossData.builder()
            .name("Cumber")
            .gender(ConstPlayer.XAYDA)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(500000)
            .hp(new double[][]{{20_000_000_000L}})
            .outfit(new short[]{2024, 2025, 2026})
            .mapJoin(new short[]{155})
            .skillTemp(new int[][]{
        {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300}, {Skill.DRAGON, 7, 700},
        {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200}, {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
        {Skill.GALICK, 1, 100}
    })
            .secondsRest(_10_PHUT)
            .build();

    public static final BossData CUMBER2 = BossData.builder()
            .name("Super Cumber")
            .gender(ConstPlayer.XAYDA)
            .typeDame(Boss.DAME_NORMAL)
            .typeHp(Boss.HP_NORMAL)
            .dame(500000)
            .hp(new double[][]{{30_000_000_000L}})
            .outfit(new short[]{2027, 2028, 2029})
            .mapJoin(new short[]{155})
            .skillTemp(new int[][]{
        {Skill.DRAGON, 1, 100}, {Skill.DRAGON, 2, 200}, {Skill.DRAGON, 3, 300}, {Skill.DRAGON, 7, 700},
        {Skill.KAMEJOKO, 1, 1000}, {Skill.KAMEJOKO, 2, 1200}, {Skill.KAMEJOKO, 5, 1500}, {Skill.KAMEJOKO, 7, 1700},
        {Skill.GALICK, 1, 100}
    })
            .secondsRest(_1_PHUT)
            .build();

    //--------------------------------------------------------------------------Boss berus
    public static final BossData NHAT_VI = new BossData(
            "Trunks Tương Lai [1]", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            10000, //dame
            new double[][]{{2_000_000_000_000_000_000L}}, //hp
            new short[]{2093, 2097, 2098, 39, 141}, // Head, Body, Leg, Aura, Flag
            new short[]{6, 27, 28, 29, 30, 13, 31, 32, 33, 34, 10, 19, 20, 35, 36, 37, 38}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000}, {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200}, {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 7, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200}, {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 7, 1700},
                {Skill.THAI_DUONG_HA_SAN, 5, 70000},
                {Skill.GALICK, 1, 1000}
            },
            _1_PHUT
    );

    public static final BossData NHI_VI = new BossData(
            "Trunks Tương Lai [2]", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            10000, //dame
            new double[][]{{2_000_000_000_000_000_000L}}, //hp
            new short[]{2093, 2097, 2098, 39, 141}, // Head, Body, Leg, Aura, Flag
            new short[]{6, 27, 28, 29, 30, 13, 31, 32, 33, 34, 10, 19, 20, 35, 36, 37, 38}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000}, {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200}, {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 7, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200}, {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 7, 1700},
                {Skill.THAI_DUONG_HA_SAN, 5, 70000},
                {Skill.GALICK, 1, 1000}
            },
            _1_PHUT
    );

    public static final BossData TAM_VI = new BossData(
            "Trunks Tương Lai [3]", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            10000, //dame
            new double[][]{{2_000_000_000_000_000_000L}}, //hp
            new short[]{2093, 2097, 2098, 39, 141}, // Head, Body, Leg, Aura, Flag
            new short[]{6, 27, 28, 29, 30, 13, 31, 32, 33, 34, 10, 19, 20, 35, 36, 37, 38}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000}, {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200}, {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 7, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200}, {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 7, 1700},
                {Skill.THAI_DUONG_HA_SAN, 5, 70000},
                {Skill.GALICK, 1, 1000}
            },
            _30_PHUT
    );

    public static final BossData TU_VI = new BossData(
            "Trunks Tương Lai [4]", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            10000, //dame
            new double[][]{{2_000_000_000_000_000_000L}}, //hp
            new short[]{2093, 2097, 2098, 39, 141}, // Head, Body, Leg, Aura, Flag
            new short[]{6, 27, 28, 29, 30, 13, 31, 32, 33, 34, 10, 19, 20, 35, 36, 37, 38}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000}, {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200}, {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 7, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200}, {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 7, 1700},
                {Skill.THAI_DUONG_HA_SAN, 5, 70000},
                {Skill.GALICK, 1, 1000}
            },
            _30_PHUT
    );

    public static final BossData NGU_VI = new BossData(
            "Trunks Tương Lai [5]", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            10000, //dame
            new double[][]{{2_000_000_000_000_000_000L}}, //hp
            new short[]{2093, 2097, 2098, 39, 141}, // Head, Body, Leg, Aura, Flag
            new short[]{6, 27, 28, 29, 30, 13, 31, 32, 33, 34, 10, 19, 20, 35, 36, 37, 38}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000}, {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200}, {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 7, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200}, {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 7, 1700},
                {Skill.THAI_DUONG_HA_SAN, 5, 70000},
                {Skill.GALICK, 1, 1000}
            },
            _30_PHUT
    );

    public static final BossData LUC_VI = new BossData(
            "Trunks SSJ 1 [1]", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            300000, //dame
            new double[][]{{2_000_000_000_000_000_000_000_000D}}, //hp
            new short[]{2090, 2091, 2092, 31, 141}, // Head, Body, Leg, Aura, Flag
            new short[]{6, 27, 28, 29, 30, 13, 31, 32, 33, 34, 10, 19, 20, 35, 36, 37, 38}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000}, {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200}, {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 7, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200}, {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 7, 1700},
                {Skill.THAI_DUONG_HA_SAN, 1, 30000},
                {Skill.GALICK, 1, 1000}
            },
            _30_PHUT
    );

    public static final BossData THAT_VI = new BossData(
            "Trunks SSJ 1 [2]", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            350000, //dame
            new double[][]{{2_000_000_000_000_000_000_000_000D}}, //hp
            new short[]{2090, 2091, 2092, 31, 141}, // Head, Body, Leg, Aura, Flag
            new short[]{6, 27, 28, 29, 30, 13, 31, 32, 33, 34, 10, 19, 20, 35, 36, 37, 38}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000}, {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200}, {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 7, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200}, {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 7, 1700},
                {Skill.THAI_DUONG_HA_SAN, 1, 30000},
                {Skill.GALICK, 1, 1000}
            },
            _30_PHUT
    );

    public static final BossData BAT_VI = new BossData(
            "Trunks SSJ 1 [3]", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            400000, //dame
            new double[][]{{2_000_000_000_000_000_000_000_000D}}, //hp
            new short[]{2090, 2091, 2092, 31, 141}, // Head, Body, Leg, Aura, Flag
            new short[]{6, 27, 28, 29, 30, 13, 31, 32, 33, 34, 10, 19, 20, 35, 36, 37, 38}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000}, {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200}, {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 7, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200}, {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 7, 1700},
                {Skill.THAI_DUONG_HA_SAN, 5, 30000},
                {Skill.GALICK, 1, 1000}
            },
            _30_PHUT
    );

    public static final BossData CUU_VI = new BossData(
            "Trunks SSJ 1 [4]", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            450000, //dame
            new double[][]{{2_000_000_000_000_000_000_000_000D}}, //hp
            new short[]{2090, 2091, 2092, 31, 141}, // Head, Body, Leg, Aura, Flag
            new short[]{6, 27, 28, 29, 30, 13, 31, 32, 33, 34, 10, 19, 20, 35, 36, 37, 38}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000}, {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200}, {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 7, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200}, {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 7, 1700},
                {Skill.THAI_DUONG_HA_SAN, 5, 30000},
                {Skill.GALICK, 1, 1000}
            },
            _30_PHUT
    );

    public static final BossData THAP_VI = new BossData(
            "Trunks SSJ 1 [5]", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            600000, //dame
            new double[][]{{2_000_000_000_000_000_000_000_000D}}, //hp
            new short[]{2090, 2091, 2092, 31, 141}, // Head, Body, Leg, Aura, Flag
            new short[]{6, 27, 28, 29, 30, 13, 31, 32, 33, 34, 10, 19, 20, 35, 36, 37, 38}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000}, {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200}, {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 7, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200}, {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 7, 1700},
                {Skill.THAI_DUONG_HA_SAN, 5, 30000},
                {Skill.GALICK, 1, 1000}
            },
            _30_PHUT
    );

    public static final BossData GOKU_SKY_1 = new BossData(
            "Goku Sky [1]", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            300000, //dame
            new double[][]{{100_000_000_000_000_000_000_000_000D}}, //hp
            new short[]{1741, 1742, 1743, 89, 132}, // Head, Body, Leg, Aura, Flag
            new short[]{6, 27, 28, 29, 30, 13, 31, 32, 33, 34, 10, 19, 20, 35, 36, 37, 38}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000}, {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200}, {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 7, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200}, {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 7, 1700},
                {Skill.THAI_DUONG_HA_SAN, 1, 30000},
                {Skill.GALICK, 1, 1000}
            },
            _30_PHUT
    );

    public static final BossData GOKU_SKY_2 = new BossData(
            "Goku Sky [2]", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            300000, //dame
            new double[][]{{100_000_000_000_000_000_000_000_000D}}, //hp
            new short[]{1741, 1742, 1743, 89, 132}, // Head, Body, Leg, Aura, Flag
            new short[]{6, 27, 28, 29, 30, 13, 31, 32, 33, 34, 10, 19, 20, 35, 36, 37, 38}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000}, {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200}, {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 7, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200}, {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 7, 1700},
                {Skill.THAI_DUONG_HA_SAN, 1, 30000},
                {Skill.GALICK, 1, 1000}
            },
            _30_PHUT
    );

    public static final BossData GOKU_SKY_3 = new BossData(
            "Goku Sky [3]", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            300000, //dame
            new double[][]{{100_000_000_000_000_000_000_000_000D}}, //hp
            new short[]{1741, 1742, 1743, 89, 132}, // Head, Body, Leg, Aura, Flag
            new short[]{6, 27, 28, 29, 30, 13, 31, 32, 33, 34, 10, 19, 20, 35, 36, 37, 38}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000}, {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200}, {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 7, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200}, {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 7, 1700},
                {Skill.THAI_DUONG_HA_SAN, 1, 30000},
                {Skill.GALICK, 1, 1000}
            },
            _30_PHUT
    );

    public static final BossData GOKU_SKY_4 = new BossData(
            "Goku Sky [4]", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            300000, //dame
            new double[][]{{100_000_000_000_000_000_000_000_000D}}, //hp
            new short[]{1741, 1742, 1743, 89, 132}, // Head, Body, Leg, Aura, Flag
            new short[]{6, 27, 28, 29, 30, 13, 31, 32, 33, 34, 10, 19, 20, 35, 36, 37, 38}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000}, {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200}, {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 7, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200}, {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 7, 1700},
                {Skill.THAI_DUONG_HA_SAN, 1, 30000},
                {Skill.GALICK, 1, 1000}
            },
            _30_PHUT
    );

    public static final BossData GOKU_SKY_5 = new BossData(
            "Goku Sky [5]", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            300000, //dame
            new double[][]{{100_000_000_000_000_000_000_000_000D}}, //hp
            new short[]{1741, 1742, 1743, 89, 132}, // Head, Body, Leg, Aura, Flag
            new short[]{6, 27, 28, 29, 30, 13, 31, 32, 33, 34, 10, 19, 20, 35, 36, 37, 38}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000}, {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200}, {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 7, 1700},
                {Skill.MASENKO, 1, 1000}, {Skill.MASENKO, 2, 1200}, {Skill.MASENKO, 4, 1500}, {Skill.MASENKO, 7, 1700},
                {Skill.THAI_DUONG_HA_SAN, 1, 30000},
                {Skill.GALICK, 1, 1000}
            },
            _30_PHUT
    );

    public static final BossData GOKU_THAN_TAI = new BossData(
            "Rồng Omega( GH Đam )", //name
            ConstPlayer.TRAI_DAT, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            1000, //dame
            new double[][]{{1_000_000_000L}}, //hp
            new short[]{1050, 1051, 1052}, //outfit
            new short[]{104, 102}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 5, 300},
                {Skill.THAI_DUONG_HA_SAN, 5, 30000},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.GALICK, 1, 1000}
            },
            _5_PHUT
    );

    public static final BossData PICOLO_THAN_TAI = new BossData(
            "Thần Tài", //name
            ConstPlayer.TRAI_DAT, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            1000, //dame
            new double[][]{{2_000_000_000L}}, //hp
            new short[]{1499, 1500, 1501}, //outfit
            new short[]{0, 7, 14}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 5, 300},
                {Skill.THAI_DUONG_HA_SAN, 5, 30000},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.GALICK, 1, 1000}
            },
            _15_PHUT
    );

    public static final BossData VEGETA_THAN_TAI = new BossData(
            "Thần Tài ( Hồn Zeno )", //name
            ConstPlayer.TRAI_DAT, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            1000, //dame
            new double[][]{{3_000_000_000L}}, //hp
            new short[]{1496, 1497, 1498}, //outfit
            new short[]{0, 7, 14}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 5, 300},
                {Skill.THAI_DUONG_HA_SAN, 5, 30000},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.GALICK, 1, 1000}
            },
            _15_PHUT
    );

    public static final BossData SON_TINH = new BossData(
            "Sơn Tinh", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            1000, //dame
            new double[][]{{1_000L}}, //hp
            new short[]{1976, 1977, 1978}, //outfit
            new short[]{0}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 5, 300},
                {Skill.THAI_DUONG_HA_SAN, 5, 30000},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.GALICK, 1, 1000}
            },
            _1_GIAY
    );

    public static final BossData THUY_TINH = new BossData(
            "Thủy Tinh", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            1000, //dame
            new double[][]{{1_000L}}, //hp
            new short[]{1796, 1797, 1798}, //outfit
            new short[]{0}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 5, 300},
                {Skill.THAI_DUONG_HA_SAN, 5, 30000},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.GALICK, 1, 1000}
            },
            _1_GIAY
    );

    public static final BossData VUA_HUNG = new BossData(
            "Vua Hùng", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            1000, //dame
            new double[][]{{1_000L}}, //hp
            new short[]{1793, 1794, 1795}, //outfit
            new short[]{0}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 5, 300},
                {Skill.THAI_DUONG_HA_SAN, 5, 30000},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.GALICK, 1, 1000}
            },
            _1_GIAY
    );

    public static final BossData RONG_1SAO = new BossData(
            "Chiến Binh Pink [1]", //name
            ConstPlayer.TRAI_DAT, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            400000000, //dame
            new double[][]{{10_000_000_000_000D}},
            new short[]{1574, 1577, 1578}, //outfit
            new short[]{96, 97, 98, 106, 107, 108, 161, 162, 163}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 7, 300},
                {Skill.ANTOMIC, 7, 300},
                {Skill.THAI_DUONG_HA_SAN, 3, 50000},
                {Skill.BIEN_KHI, 7, 50000},
                {Skill.KAMEJOKO, 7, 300}
            },
            _15_PHUT
    );

    public static final BossData RONG_2SAO = new BossData(
            "Chiến Binh Pink [2]", //name
            ConstPlayer.TRAI_DAT, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            40000000, //dame
            new double[][]{{10_000_000_000_000D}},
            new short[]{1574, 1577, 1578}, //outfit
            new short[]{96, 97, 98, 106, 107, 108, 161, 162, 163}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 7, 300},
                {Skill.ANTOMIC, 7, 300},
                {Skill.THAI_DUONG_HA_SAN, 3, 50000},
                {Skill.BIEN_KHI, 7, 50000},
                {Skill.KAMEJOKO, 7, 300}
            },
            _15_PHUT
    );

    public static final BossData RONG_3SAO = new BossData(
            "Chiến Binh Pink [3]", //name
            ConstPlayer.TRAI_DAT, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            40000000, //dame
            new double[][]{{10_000_000_000_000D}},
            new short[]{1574, 1577, 1578}, //outfit
            new short[]{96, 97, 98, 106, 107, 108, 161, 162, 163}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 7, 300},
                {Skill.ANTOMIC, 7, 300},
                {Skill.THAI_DUONG_HA_SAN, 3, 50000},
                {Skill.BIEN_KHI, 7, 50000},
                {Skill.KAMEJOKO, 7, 300}
            },
            _15_PHUT
    );

    public static final BossData RONG_4SAO = new BossData(
            "Bé Rồng 4 Sao", //name
            ConstPlayer.TRAI_DAT, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            4000, //dame
            new double[][]{{1_000_000L}},
            new short[]{1973, 1974, 1975}, //outfit
            new short[]{217}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 5, 300},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.THAI_DUONG_HA_SAN, 7, 50000},
                {Skill.GALICK, 1, 1000}
            },
            _1_GIAY
    );

    public static final BossData RONG_5SAO = new BossData(
            "Bé Rồng 5 Sao", //name
            ConstPlayer.TRAI_DAT, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            4000, //dame
            new double[][]{{1_000_000L}},
            new short[]{1952, 1953, 1954}, //outfit
            new short[]{217}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 5, 300},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.THAI_DUONG_HA_SAN, 7, 50000},
                {Skill.GALICK, 1, 1000}
            },
            _1_GIAY
    );

    public static final BossData RONG_6SAO = new BossData(
            "Bé Rồng 6 Sao", //name
            ConstPlayer.TRAI_DAT, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            4000, //dame
            new double[][]{{1_000_000L}},
            new short[]{1877, 1878, 1879}, //outfit
            new short[]{217}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 5, 300},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.THAI_DUONG_HA_SAN, 7, 50000},
                {Skill.GALICK, 1, 1000}
            },
            _1_GIAY
    );

    public static final BossData RONG_7SAO = new BossData(
            "Bé Rồng 7 Sao", //name
            ConstPlayer.TRAI_DAT, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            4000, //dame
            new double[][]{{1_000_000L}},
            new short[]{1790, 1791, 1792}, //outfit
            new short[]{217}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 5, 300},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.THAI_DUONG_HA_SAN, 7, 50000},
                {Skill.GALICK, 1, 1000}
            },
            _1_GIAY
    );

    public static final BossData ADMIN_1 = new BossData(
            "Admin ( VIP1 )", //name
            ConstPlayer.TRAI_DAT, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            100000, //dame
            new double[][]{{1_000_000_000_000_000_000_000_000D}},
            new short[]{1436, 1437, 1438}, //outfit
            new short[]{139, 140}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 5, 300},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.THAI_DUONG_HA_SAN, 7, 50000},
                {Skill.TAI_TAO_NANG_LUONG, 7, 15000},
                {Skill.GALICK, 7, 1000}, {Skill.BIEN_KHI, 7, 1200000}
            },
            _30_PHUT
    );

    public static final BossData ADMIN_2 = new BossData(
            "Admin ( VIP2 )", //name
            ConstPlayer.TRAI_DAT, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            100000, //dame
            new double[][]{{1_000_000_000_000_000_000_000_000D}},
            new short[]{1436, 1437, 1438}, //outfit
            new short[]{139, 140}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 5, 300},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.THAI_DUONG_HA_SAN, 7, 50000},
                {Skill.TAI_TAO_NANG_LUONG, 7, 15000},
                {Skill.GALICK, 7, 1000}, {Skill.BIEN_KHI, 7, 1200000}
            },
            _30_PHUT
    );

    public static final BossData VI_THU1 = new BossData(
            "Nhất Vĩ", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            20000, //dame
            new double[][]{{1000}}, //hp
            new short[]{1625, 1626, 1627}, //outfit
            new short[]{126}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 5, 1000},
                {Skill.GALICK, 7, 1000},
                {Skill.ANTOMIC, 7, 1000}
            },
            1
    );

    public static final BossData VI_THU2 = new BossData(
            "Nhị Vĩ", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            20000, //dame
            new double[][]{{1000}}, //hp
            new short[]{1628, 1629, 1630}, //outfit
            new short[]{126}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 5, 1000},
                {Skill.GALICK, 7, 1000},
                {Skill.ANTOMIC, 7, 1000}
            },
            _10_GIAY
    );

    public static final BossData VI_THU3 = new BossData(
            "Tam Vĩ", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            20000, //dame
            new double[][]{{1000}}, //hp
            new short[]{1673, 1674, 1675}, //outfit
            new short[]{126}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 5, 1000},
                {Skill.GALICK, 7, 1000},
                {Skill.ANTOMIC, 7, 1000}
            },
            _10_GIAY
    );

    public static final BossData VI_THU4 = new BossData(
            "Tứ Vĩ", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            20000, //dame
            new double[][]{{1000}}, //hp
            new short[]{1676, 1677, 1678}, //outfit
            new short[]{126}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 5, 1000},
                {Skill.GALICK, 7, 1000},
                {Skill.ANTOMIC, 7, 1000}
            },
            _10_GIAY
    );

    public static final BossData VI_THU5 = new BossData(
            "Ngũ Vĩ", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            20000, //dame
            new double[][]{{1000}}, //hp
            new short[]{1679, 1680, 1681}, //outfit
            new short[]{126}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 5, 1000},
                {Skill.GALICK, 7, 1000},
                {Skill.ANTOMIC, 7, 1000}
            },
            _10_GIAY
    );

    public static final BossData VI_THU6 = new BossData(
            "Lục Vĩ", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            20000, //dame
            new double[][]{{1000}}, //hp
            new short[]{1682, 1683, 1684}, //outfit
            new short[]{126}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 5, 1000},
                {Skill.GALICK, 7, 1000},
                {Skill.ANTOMIC, 7, 1000}
            },
            _10_GIAY
    );

    public static final BossData VI_THU7 = new BossData(
            "Thất Vĩ", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            20000, //dame
            new double[][]{{1000}}, //hp
            new short[]{1685, 1686, 1687}, //outfit
            new short[]{126}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 5, 1000},
                {Skill.GALICK, 7, 1000},
                {Skill.ANTOMIC, 7, 1000}
            },
            _10_GIAY
    );

    public static final BossData VI_THU8 = new BossData(
            "Bát Vĩ", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            20000, //dame
            new double[][]{{1000}}, //hp
            new short[]{1688, 1689, 1690}, //outfit
            new short[]{126}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 5, 1000},
                {Skill.GALICK, 7, 1000},
                {Skill.ANTOMIC, 7, 1000}
            },
            _10_GIAY
    );

    public static final BossData VI_THU9 = new BossData(
            "Cửu Vĩ", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            20000, //dame
            new double[][]{{1000}}, //hp
            new short[]{1544, 1545, 1546}, //outfit
            new short[]{126}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 5, 1000},
                {Skill.GALICK, 7, 1000},
                {Skill.ANTOMIC, 7, 1000}
            },
            _10_GIAY
    );

    public static final BossData VI_THU99 = new BossData(
            "Thập Vĩ", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            20000, //dame
            new double[][]{{1000}}, //hp
            new short[]{1622, 1623, 1624}, //outfit
            new short[]{126}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 5, 1000},
                {Skill.GALICK, 7, 1000},
                {Skill.ANTOMIC, 7, 1000}
            },
            _10_GIAY
    );

    public static final BossData HUY_DIET1 = new BossData(
            "Thần Hủy Diệt [1] ", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            2000000000, //dame
            new double[][]{{30_000_000_000_000L}}, //hp
            new short[]{508, 509, 510}, //outfit
            new short[]{166}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 7, 300},
                {Skill.KAMEJOKO, 7, 300},
                {Skill.THAI_DUONG_HA_SAN, 3, 50000},
                {Skill.BIEN_KHI, 7, 50000},
                {Skill.GALICK, 7, 300}
            },
            _5_PHUT
    );

    public static final BossData HUY_DIET2 = new BossData(
            "Thần Hủy Diệt [2]", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            2000000000, //dame
            new double[][]{{30_000_000_000_000L}}, //hp
            new short[]{508, 509, 510}, //outfit
            new short[]{167}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 7, 300},
                {Skill.KAMEJOKO, 7, 300},
                {Skill.THAI_DUONG_HA_SAN, 3, 50000},
                {Skill.BIEN_KHI, 7, 50000},
                {Skill.GALICK, 7, 300}
            },
            _5_PHUT
    );

    public static final BossData HUY_DIET3 = new BossData(
            "Thần Hủy Diệt [3]", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            2000000000, //dame
            new double[][]{{30_000_000_000_000L}}, //hp
            new short[]{508, 509, 510}, //outfit
            new short[]{168}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 7, 300},
                {Skill.KAMEJOKO, 7, 300},
                {Skill.THAI_DUONG_HA_SAN, 3, 50000},
                {Skill.BIEN_KHI, 7, 50000},
                {Skill.GALICK, 7, 300}
            },
            _5_PHUT
    );

    public static final BossData HUY_DIET4 = new BossData(
            "Thần Hủy Diệt [4] ", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            2000000000, //dame
            new double[][]{{30_000_000_000_000L}}, //hp
            new short[]{508, 509, 510}, //outfit
            new short[]{166}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 7, 300},
                {Skill.KAMEJOKO, 7, 300},
                {Skill.THAI_DUONG_HA_SAN, 3, 50000},
                {Skill.BIEN_KHI, 7, 50000},
                {Skill.GALICK, 7, 300}
            },
            _5_PHUT
    );

    public static final BossData HUY_DIET5 = new BossData(
            "Thần Hủy Diệt [5] ", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            2000000000, //dame
            new double[][]{{30_000_000_000_000L}}, //hp
            new short[]{508, 509, 510}, //outfit
            new short[]{166}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 7, 300},
                {Skill.KAMEJOKO, 7, 300},
                {Skill.THAI_DUONG_HA_SAN, 3, 50000},
                {Skill.BIEN_KHI, 7, 50000},
                {Skill.GALICK, 7, 300}
            },
            _5_PHUT
    );

    public static final BossData THAN_LINH1 = new BossData(
            "Thần Linh [1]", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            20000, //dame
            new double[][]{{10_000_000_000L}}, //hp
            new short[]{627, 628, 629}, //outfit
            new short[]{27, 28, 31, 32, 33, 35, 36, 37, 38}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 7, 300},
                {Skill.KAMEJOKO, 7, 300},
                {Skill.THAI_DUONG_HA_SAN, 3, 50000},
                {Skill.BIEN_KHI, 4, 50000},
                {Skill.GALICK, 7, 300}
            },
            _5_PHUT
    );

    public static final BossData THAN_LINH2 = new BossData(
            "Thần Linh [2]", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            20000, //dame
            new double[][]{{10_000_000_000L}}, //hp
            new short[]{627, 628, 629}, //outfit
            new short[]{27, 28, 31, 32, 33, 35, 36, 37, 38}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 7, 300},
                {Skill.KAMEJOKO, 7, 300},
                {Skill.THAI_DUONG_HA_SAN, 3, 50000},
                {Skill.BIEN_KHI, 4, 50000},
                {Skill.GALICK, 7, 300}
            },
            _5_PHUT
    );

    public static final BossData THAN_LINH3 = new BossData(
            "Thần Linh [3]", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            20000, //dame
            new double[][]{{10_000_000_000L}}, //hp
            new short[]{627, 628, 629}, //outfit
            new short[]{27, 28, 31, 32, 33, 35, 36, 37, 38}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 7, 300},
                {Skill.KAMEJOKO, 7, 300},
                {Skill.THAI_DUONG_HA_SAN, 3, 50000},
                {Skill.BIEN_KHI, 4, 50000},
                {Skill.GALICK, 7, 300}
            },
            _5_PHUT
    );
    public static final BossData THAN_MAU = new BossData(
            "Thần Mẫu", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            20000, //dame
            new double[][]{{1_000_000_000_000L}}, //hp
            new short[]{1469, 1470, 1471}, //outfit
            new short[]{145}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 5, 300},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.THAI_DUONG_HA_SAN, 7, 50000},
                {Skill.GALICK, 7, 1000}
            },
            _2_GIO
    );
    public static final BossData BI_KIEP_1 = new BossData(
            "Tập Sự 1", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            200000, //dame
            new double[][]{{1_000_000_000L}}, //hp
            new short[]{526, 523, 524}, //outfit
            new short[]{131, 132, 133}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 5, 300},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.THAI_DUONG_HA_SAN, 2, 5000},
                {Skill.GALICK, 7, 1000}
            },
            _30_GIAY
    );
    public static final BossData BI_KIEP_2 = new BossData(
            "Tập Sự 2", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            200000, //dame
            new double[][]{{1_000_000_000L}}, //hp
            new short[]{527, 523, 524}, //outfit
            new short[]{131, 132, 133}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 5, 300},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.THAI_DUONG_HA_SAN, 2, 5000},
                {Skill.GALICK, 7, 1000}
            },
            _30_GIAY
    );
    public static final BossData BI_KIEP_3 = new BossData(
            "Tập Sự 3", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            200000, //dame
            new double[][]{{1_000_000_000L}}, //hp
            new short[]{528, 523, 524}, //outfit
            new short[]{131, 132, 133}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 5, 300},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.THAI_DUONG_HA_SAN, 2, 5000},
                {Skill.GALICK, 7, 1000}
            },
            _30_GIAY
    );
    public static final BossData BI_KIEP_4 = new BossData(
            "Tập Sự 4", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            200000, //dame
            new double[][]{{1_000_000_000L}}, //hp
            new short[]{529, 523, 524}, //outfit
            new short[]{131, 132, 133}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 5, 300},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.THAI_DUONG_HA_SAN, 2, 5000},
                {Skill.GALICK, 7, 1000}
            },
            _30_GIAY
    );

    public static final BossData FRIEREN = new BossData(
            "Frieren [Sự kiện hè]", //name
            ConstPlayer.TRAI_DAT, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            2000, //dame
            new double[][]{{300_000L}}, //hp
            new short[]{1634, 1635, 1636}, //outfit
            new short[]{6, 27, 28, 29, 30, 13, 31, 32, 33, 34, 10, 19, 20, 35, 36, 37, 38}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 5, 300},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.THAI_DUONG_HA_SAN, 7, 50000},
                {Skill.GALICK, 7, 1000}, {Skill.THOI_MIEN, 7, 100000}
            },
            _30_GIAY
    );
    public static final BossData MAJIN_ANDROID_21 = new BossData(
            "Majin Android 21 [Sự kiện hè]", //name
            ConstPlayer.TRAI_DAT, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            2000, //dame
            new double[][]{{300_000L}}, //hp
            new short[]{1726, 1727, 1728}, //outfit
            new short[]{6, 27, 28, 29, 30, 13, 31, 32, 33, 34, 10, 19, 20, 35, 36, 37, 38}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 5, 300},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.THAI_DUONG_HA_SAN, 7, 50000},
                {Skill.GALICK, 7, 1000}, {Skill.THOI_MIEN, 7, 100000}
            },
            _30_GIAY
    );
    public static final BossData KING_KONG_SANH_DIEU = new BossData(
            "King Kong Sành điệu [Sự kiện hè]", //name
            ConstPlayer.TRAI_DAT, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            2000, //dame
            new double[][]{{300_000L}}, //hp
            new short[]{1327, 1328, 1329}, //outfit
            new short[]{6, 27, 28, 29, 30, 13, 31, 32, 33, 34, 10, 19, 20, 35, 36, 37, 38}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 5, 300},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.THAI_DUONG_HA_SAN, 7, 50000},
                {Skill.GALICK, 7, 1000}, {Skill.THOI_MIEN, 7, 100000}
            },
            _30_GIAY
    );

    public static final BossData INOSUKE_BOSS = new BossData(
            "Inosuke [Diệt Quỷ]",
            ConstPlayer.TRAI_DAT,
            Boss.DAME_NORMAL,
            Boss.HP_NORMAL,
            100000,
            new double[][]{{1_100_000L}},
            new short[]{1131, 1132, 1133, 33, 140}, // Head, Body, Leg, Aura, Flag
            new short[]{6, 27, 28, 29, 30, 13, 31, 32, 33, 34, 10, 19, 20, 35, 36, 37, 38}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 5, 300},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.THAI_DUONG_HA_SAN, 7, 50000},
                {Skill.GALICK, 7, 1000}, {Skill.THOI_MIEN, 7, 100000}
            },
            _30_GIAY
    );

    public static final BossData ZENITSU_BOSS = new BossData(
            "Zenitsu [Diệt Quỷ]",
            ConstPlayer.TRAI_DAT,
            Boss.DAME_NORMAL,
            Boss.HP_NORMAL,
            100000,
            new double[][]{{1_300_000L}},
            new short[]{1125, 1126, 1127, 31, 140}, // Head, Body, Leg, Aura, Flag
            new short[]{6, 27, 28, 29, 30, 13, 31, 32, 33, 34, 10, 19, 20, 35, 36, 37, 38}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 5, 300},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.THAI_DUONG_HA_SAN, 7, 50000},
                {Skill.GALICK, 7, 1000}, {Skill.THOI_MIEN, 7, 100000}
            },
            _30_GIAY, true
    );

    public static final BossData INOSUKE_HASHIBIRA_BOSS = new BossData(
            "Inosuke Hashibira [Diệt Quỷ]",
            ConstPlayer.TRAI_DAT,
            Boss.DAME_NORMAL,
            Boss.HP_NORMAL,
            100000,
            new double[][]{{1_200_000L}},
            new short[]{1122, 1123, 1124, 33, 140}, // Head, Body, Leg, Aura, Flag
            new short[]{6, 27, 28, 29, 30, 13, 31, 32, 33, 34, 10, 19, 20, 35, 36, 37, 38}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 5, 300},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.THAI_DUONG_HA_SAN, 7, 50000},
                {Skill.GALICK, 7, 1000}, {Skill.THOI_MIEN, 7, 100000}
            },
            _30_GIAY, true
    );
    public static final BossData TANJIRO_BOSS = new BossData(
            "Tanjiro [Diệt Quỷ]",
            ConstPlayer.TRAI_DAT,
            Boss.DAME_NORMAL,
            Boss.HP_NORMAL,
            100000000,
            new double[][]{{1_000_000L}},
            new short[]{1119, 1120, 1121, 38, 141}, // Head, Body, Leg, Aura, Flag
            new short[]{6, 27, 28, 29, 30, 13, 31, 32, 33, 34, 10, 19, 20, 35, 36, 37, 38}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 5, 300},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.THAI_DUONG_HA_SAN, 7, 50000},
                {Skill.GALICK, 7, 1000}, {Skill.THOI_MIEN, 7, 100000}
            },
            _30_GIAY
    );

    public static final BossData NEZUKO_BOSS = new BossData(
            "Nezuko [Diệt Quỷ]",
            ConstPlayer.TRAI_DAT,
            Boss.DAME_NORMAL,
            Boss.HP_NORMAL,
            1000000000,
            new double[][]{{1_500_000_000_000_000D}},
            new short[]{1128, 1129, 1130, 14, 141}, // Head, Body, Leg, Aura, Flag
            new short[]{5}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 5, 300},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.THAI_DUONG_HA_SAN, 7, 50000},
                {Skill.GALICK, 7, 1000}, {Skill.THOI_MIEN, 7, 100000}
            },
            _5_PHUT
    );

    public static final BossData THAN_BLUE1 = new BossData(
            "Thần Blue [PRO 1]",
            ConstPlayer.TRAI_DAT,
            Boss.DAME_NORMAL,
            Boss.HP_NORMAL,
            2000000000,
            new double[][]{{1_000_000_000_000_000_000D}},
            new short[]{1586, 1595, 1596, 118, 161}, // Head, Body, Leg, Aura, Flag
            new short[]{5, 33, 42, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75,
                76, 77, 78, 79, 80, 81, 82, 83, 104, 105, 106, 107, 108, 109, 110}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 5, 300},
                {Skill.KAMEJOKO, 7, 300},
                {Skill.PHAN_THAN, 7, 300},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.BIEN_HINH, 6, 300000},
                {Skill.TAI_TAO_SUC_MANH, 1, 100},
                {Skill.BIEN_KHI, 7, 50000},
                {Skill.THOI_MIEN, 7, 30000},
                {Skill.GALICK, 7, 1000}, {Skill.THOI_MIEN, 7, 100000},
                {Skill.KHIEN_NANG_LUONG, 7, 200000}
            },
            _5_PHUT
    );

    public static final BossData THAN_BLUE2 = new BossData(
            "Thần Blue [PRO 2]",
            ConstPlayer.TRAI_DAT,
            Boss.DAME_NORMAL,
            Boss.HP_NORMAL,
            2000000000,
            new double[][]{{1_000_000_000_000_000_000D}},
            new short[]{1586, 1595, 1596, 118, 161}, // Head, Body, Leg, Aura, Flag
            new short[]{5, 33, 42,63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75,
                76, 77, 78, 79, 80, 81, 82, 83, 84, 104, 105, 106, 107, 108, 109, 110}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 5, 300},
                {Skill.BIEN_KHI, 7, 50000},
                {Skill.KAMEJOKO, 7, 300},
                {Skill.PHAN_THAN, 7, 300},
                {Skill.BIEN_HINH, 6, 300000},
                {Skill.TAI_TAO_SUC_MANH, 1, 100},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.THOI_MIEN, 7, 30000},
                {Skill.GALICK, 7, 1000}, {Skill.THOI_MIEN, 7, 100000},
                {Skill.KHIEN_NANG_LUONG, 7, 200000}
            },
            _5_PHUT
    );

    public static final BossData THAN_BLUE3 = new BossData(
            "Thần Blue [PRO 3]",
            ConstPlayer.TRAI_DAT,
            Boss.DAME_NORMAL,
            Boss.HP_NORMAL,
            2000000000,
            new double[][]{{1_000_000_000_000_000_000D}},
            new short[]{1586, 1595, 1596, 118, 161}, // Head, Body, Leg, Aura, Flag
            new short[]{5, 33, 42, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75,
                76, 77, 78, 79, 80, 81, 82, 83, 84, 104, 105, 106, 107, 108, 109, 110}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 5, 300},
                {Skill.BIEN_KHI, 7, 50000},
                {Skill.KAMEJOKO, 7, 300},
                {Skill.PHAN_THAN, 7, 300},

                {Skill.BIEN_HINH, 6, 300000},
                {Skill.TAI_TAO_SUC_MANH, 1, 100},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.THOI_MIEN, 7, 30000},
                {Skill.GALICK, 7, 1000}, {Skill.THOI_MIEN, 7, 100000},
                {Skill.KHIEN_NANG_LUONG, 7, 200000}
            },
            _5_PHUT
    );

    public static final BossData THAN_BLUE4 = new BossData(
            "Thần Blue [PRO 4]",
            ConstPlayer.TRAI_DAT,
            Boss.DAME_NORMAL,
            Boss.HP_NORMAL,
            2000000000,
            new double[][]{{1_000_000_000_000_000_000D}},
            new short[]{1586, 1595, 1596, 118, 161}, // Head, Body, Leg, Aura, Flag
            new short[]{5, 33, 42,63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75,
                76, 77, 78, 79, 80, 81, 82, 83, 84, 104, 105, 106, 107, 108, 109, 110}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 5, 300},
                {Skill.BIEN_KHI, 7, 50000},
                {Skill.KAMEJOKO, 7, 300},
                {Skill.PHAN_THAN, 7, 300},
                {Skill.BIEN_HINH, 6, 300000},
                {Skill.TAI_TAO_SUC_MANH, 1, 100},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.THOI_MIEN, 7, 30000},
                {Skill.GALICK, 7, 1000}, {Skill.THOI_MIEN, 7, 100000},
                {Skill.KHIEN_NANG_LUONG, 7, 200000}
            },
            _5_PHUT
    );

    public static final BossData THAN_BLUE5 = new BossData(
            "Thần Blue [PRO 5]",
            ConstPlayer.TRAI_DAT,
            Boss.DAME_NORMAL,
            Boss.HP_NORMAL,
            2000000000,
            new double[][]{{1_000_000_000_000_000_000D}},
            new short[]{1586, 1595, 1596, 118, 161}, // Head, Body, Leg, Aura, Flag
            new short[]{5, 33, 42, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75,
                76, 77, 78, 79, 80, 81, 82, 83, 84, 104, 105, 106, 107, 108, 109, 110}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 5, 300},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.KAMEJOKO, 7, 300},
                {Skill.PHAN_THAN, 7, 300},

                {Skill.BIEN_HINH, 6, 300000},
                {Skill.TAI_TAO_SUC_MANH, 1, 100},
                {Skill.BIEN_KHI, 7, 50000},
                {Skill.THOI_MIEN, 7, 30000},
                {Skill.GALICK, 7, 1000}, {Skill.THOI_MIEN, 7, 100000},
                {Skill.KHIEN_NANG_LUONG, 7, 200000}
            },
            _5_PHUT
    );

    public static final BossData THAN_RED1 = new BossData(
            "Thần Red [PRO 1]",
            ConstPlayer.TRAI_DAT,
            Boss.DAME_NORMAL,
            Boss.HP_NORMAL,
            2000000000,
            new double[][]{{100_000_000_000_000_000D}},
            new short[]{1640, 1643, 1644, 115, 165}, // Head, Body, Leg, Aura, Flag
            new short[]{5, 33, 42,63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75,
                76, 77, 78, 79, 80, 81, 82, 83, 84, 104, 105, 106, 107, 108, 109, 110}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 5, 300},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.KAMEJOKO, 7, 300},
                {Skill.PHAN_THAN, 7, 300},
                {Skill.BIEN_HINH, 6, 300000},
                {Skill.TAI_TAO_SUC_MANH, 1, 100},
                {Skill.BIEN_KHI, 7, 50000},
                {Skill.BIEN_KHI, 7, 50000},
                {Skill.THAI_DUONG_HA_SAN, 7, 50000},
                {Skill.GALICK, 7, 1000}, {Skill.THOI_MIEN, 7, 100000}
            },
            _5_PHUT
    );

    public static final BossData THAN_RED2 = new BossData(
            "Thần Red [PRO 2]",
            ConstPlayer.TRAI_DAT,
            Boss.DAME_NORMAL,
            Boss.HP_NORMAL,
            2000000000,
            new double[][]{{100_000_000_000_000_000D}},
            new short[]{1640, 1643, 1644, 115, 165}, // Head, Body, Leg, Aura, Flag
            new short[]{5, 33, 42,63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75,
                76, 77, 78, 79, 80, 81, 82, 83, 84, 104, 105, 106, 107, 108, 109, 110}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 5, 300},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.KAMEJOKO, 7, 300},
                {Skill.PHAN_THAN, 7, 300},
                {Skill.BIEN_HINH, 6, 300000},
                {Skill.TAI_TAO_SUC_MANH, 1, 100},
                {Skill.BIEN_KHI, 7, 50000},
                {Skill.THAI_DUONG_HA_SAN, 7, 50000},
                {Skill.GALICK, 7, 1000}, {Skill.THOI_MIEN, 7, 100000}
            },
            _5_PHUT
    );

    public static final BossData THAN_RED3 = new BossData(
            "Thần Red [PRO 3]",
            ConstPlayer.TRAI_DAT,
            Boss.DAME_NORMAL,
            Boss.HP_NORMAL,
            2000000000,
            new double[][]{{100_000_000_000_000_000D}},
            new short[]{1640, 1643, 1644, 115, 165}, // Head, Body, Leg, Aura, Flag
            new short[]{5, 33, 42,63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75,
                76, 77, 78, 79, 80, 81, 82, 83, 84, 104, 105, 106, 107, 108, 109, 110}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 5, 300},
                {Skill.BIEN_KHI, 7, 50000},
                {Skill.KAMEJOKO, 7, 300},
                {Skill.PHAN_THAN, 7, 300},
                {Skill.BIEN_HINH, 6, 300000},
                {Skill.TAI_TAO_SUC_MANH, 1, 100},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.THAI_DUONG_HA_SAN, 7, 50000},
                {Skill.GALICK, 7, 1000}, {Skill.THOI_MIEN, 7, 100000}
            },
            _5_PHUT
    );

    public static final BossData THAN_RED4 = new BossData(
            "Thần Red [PRO 4]",
            ConstPlayer.TRAI_DAT,
            Boss.DAME_NORMAL,
            Boss.HP_NORMAL,
            2000000000,
            new double[][]{{100_000_000_000_000_000D}},
            new short[]{1640, 1643, 1644, 115, 165}, // Head, Body, Leg, Aura, Flag
            new short[]{5, 33, 42,63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75,
                76, 77, 78, 79, 80, 81, 82, 83, 84, 104, 105, 106, 107, 108, 109, 110}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 5, 300},
                {Skill.BIEN_KHI, 7, 50000},
                {Skill.KAMEJOKO, 7, 300},
                {Skill.PHAN_THAN, 7, 300},
                {Skill.BIEN_HINH, 6, 300000},
                {Skill.TAI_TAO_SUC_MANH, 1, 100},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.THAI_DUONG_HA_SAN, 7, 50000},
                {Skill.GALICK, 7, 1000}, {Skill.THOI_MIEN, 7, 100000}
            },
            _5_PHUT
    );

    public static final BossData THAN_RED5 = new BossData(
            "Thần Red [PRO 5]",
            ConstPlayer.TRAI_DAT,
            Boss.DAME_NORMAL,
            Boss.HP_NORMAL,
            2000000000,
            new double[][]{{100_000_000_000_000_000D}},
            new short[]{1640, 1643, 1644, 115, 165}, // Head, Body, Leg, Aura, Flag
            new short[]{5, 33, 42,63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75,
                76, 77, 78, 79, 80, 81, 82, 83, 84, 104, 105, 106, 107, 108, 109, 110}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 5, 300},
                {Skill.BIEN_HINH, 6, 300000},
                {Skill.KAMEJOKO, 7, 300},
                {Skill.PHAN_THAN, 7, 300},
                {Skill.TAI_TAO_SUC_MANH, 1, 100},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.THAI_DUONG_HA_SAN, 7, 50000},
                {Skill.GALICK, 7, 1000}, {Skill.THOI_MIEN, 7, 100000}
            },
            _5_PHUT
    );

    public static final BossData THAN_PINK1 = new BossData(
            "Thần Yellow [PRO 1]",
            ConstPlayer.TRAI_DAT,
            Boss.DAME_NORMAL,
            Boss.HP_NORMAL,
            2000000000,
            new double[][]{{100_000_000_000_000_000D}},
            new short[]{1598, 1580, 1581, 107, 155}, // Head, Body, Leg, Aura, Flag
            new short[]{5, 33, 42, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75,
                76, 77, 78, 79, 80, 81, 82, 83, 84, 104, 105, 106, 107, 108, 109, 110}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 5, 300},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.BIEN_HINH, 6, 300000},
                {Skill.TAI_TAO_SUC_MANH, 1, 100},
                {Skill.THAI_DUONG_HA_SAN, 7, 50000},
                {Skill.GALICK, 7, 1000}, {Skill.THOI_MIEN, 7, 100000}
            },
            _5_PHUT
    );

    public static final BossData THAN_PINK2 = new BossData(
            "Thần Yellow [PRO 2]",
            ConstPlayer.TRAI_DAT,
            Boss.DAME_NORMAL,
            Boss.HP_NORMAL,
            2000000000,
            new double[][]{{100_000_000_000_000_000D}},
            new short[]{1598, 1580, 1581, 107, 155}, // Head, Body, Leg, Aura, Flag
            new short[]{5, 33, 42, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75,
                76, 77, 78, 79, 80, 81, 82, 83, 84, 104, 105, 106, 107, 108, 109, 110}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 5, 300},
                {Skill.BIEN_HINH, 6, 300000},
                {Skill.KAMEJOKO, 7, 300},
                {Skill.PHAN_THAN, 7, 300},
                {Skill.TAI_TAO_SUC_MANH, 1, 100},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.THAI_DUONG_HA_SAN, 7, 50000},
                {Skill.GALICK, 7, 1000}, {Skill.THOI_MIEN, 7, 100000}
            },
            _5_PHUT
    );

    public static final BossData THAN_PINK3 = new BossData(
            "Thần Yellow [PRO 3]",
            ConstPlayer.TRAI_DAT,
            Boss.DAME_NORMAL,
            Boss.HP_NORMAL,
            2000000000,
            new double[][]{{100_000_000_000_000_000D}},
            new short[]{1598, 1580, 1581, 107, 155}, // Head, Body, Leg, Aura, Flag
            new short[]{5, 33, 42,63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75,
                76, 77, 78, 79, 80, 81, 82, 83, 84, 104, 105, 106, 107, 108, 109, 110}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 5, 300},
                {Skill.BIEN_HINH, 6, 300000},
                {Skill.KAMEJOKO, 7, 300},
                {Skill.PHAN_THAN, 7, 300},
                {Skill.TAI_TAO_SUC_MANH, 1, 100},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.THAI_DUONG_HA_SAN, 7, 50000},
                {Skill.GALICK, 7, 1000}, {Skill.THOI_MIEN, 7, 100000}
            },
            _5_PHUT
    );

    public static final BossData THAN_PINK4 = new BossData(
            "Thần Yellow [PRO 4]",
            ConstPlayer.TRAI_DAT,
            Boss.DAME_NORMAL,
            Boss.HP_NORMAL,
            2000000000,//dame
            new double[][]{{100_000_000_000_000_000D}},
            new short[]{1598, 1580, 1581, 107, 155}, // Head, Body, Leg, Aura, Flag
            new short[]{5, 33, 42,63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75,
                76, 77, 78, 79, 80, 81, 82, 83, 84, 104, 105, 106, 107, 108, 109, 110}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 5, 300},
                {Skill.BIEN_HINH, 6, 300000},
                {Skill.KAMEJOKO, 7, 300},
                {Skill.PHAN_THAN, 7, 300},
                {Skill.TAI_TAO_SUC_MANH, 1, 100},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.THAI_DUONG_HA_SAN, 7, 50000},
                {Skill.GALICK, 7, 1000}, {Skill.THOI_MIEN, 7, 100000}
            },
            _5_PHUT
    );

    public static final BossData THAN_PINK5 = new BossData(
            "Thần Yellow [PRO 5]",
            ConstPlayer.TRAI_DAT,
            Boss.DAME_NORMAL,
            Boss.HP_NORMAL,
            2000000000,
            new double[][]{{100_000_000_000_000_000D}},
            new short[]{1598, 1580, 1581, 107, 155}, // Head, Body, Leg, Aura, Flag
            new short[]{5, 33, 42, 63, 64, 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75,
                76, 77, 78, 79, 80, 81, 82, 83, 84, 104, 105, 106, 107, 108, 109, 110}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 5, 300},
                {Skill.ANTOMIC, 7, 1000},
                {Skill.BIEN_HINH, 6, 300000},
                {Skill.KAMEJOKO, 7, 300},
                {Skill.PHAN_THAN, 7, 300},
                {Skill.TAI_TAO_SUC_MANH, 1, 100},
                {Skill.BIEN_KHI, 7, 50000},
                {Skill.THAI_DUONG_HA_SAN, 7, 50000},
                {Skill.GALICK, 7, 1000}, {Skill.THOI_MIEN, 7, 100000}
            },
            _5_PHUT
    );
    public static final BossData THIEN_SU1 = new BossData(
            "Thiên Sứ [1]", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            200000000, //dame
            new double[][]{{100_000_000_000_000D}}, //hp
            new short[]{838, 839, 840}, //outfit
            new short[]{2, 3, 4, 8, 9, 10, 15, 16, 17, 154}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 7, 300},
                {Skill.KAMEJOKO, 7, 300},
                {Skill.THAI_DUONG_HA_SAN, 3, 50000},
                {Skill.BIEN_KHI, 7, 50000},
                {Skill.GALICK, 7, 300}
            },
            _10_PHUT
    );
    public static final BossData THIEN_SU2 = new BossData(
            "Thiên Sứ [2]", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            20000000, //dame
            new double[][]{{100_000_000_000_000D}}, //hp
            new short[]{838, 839, 840}, //outfit
            new short[]{2, 3, 4, 8, 9, 10, 15, 16, 17, 154}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 7, 300},
                {Skill.KAMEJOKO, 7, 300},
                {Skill.THAI_DUONG_HA_SAN, 3, 50000},
                {Skill.BIEN_KHI, 6, 50000},
                {Skill.GALICK, 7, 300}
            },
            _10_PHUT
    );
    public static final BossData THIEN_SU3 = new BossData(
            "Thiên Sứ [3]", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            20000000, //dame
            new double[][]{{100_000_000_000_000D}}, //hp
            new short[]{838, 839, 840}, //outfit
            new short[]{2, 3, 4, 8, 9, 10, 15, 16, 17, 154}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 7, 300},
                {Skill.KAMEJOKO, 7, 300},
                {Skill.THAI_DUONG_HA_SAN, 3, 50000},
                {Skill.BIEN_KHI, 6, 50000},
                {Skill.GALICK, 7, 300}
            },
            _10_PHUT
    );
    public static final BossData THIEN_SU4 = new BossData(
            "Thiên Sứ [4]", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            20000000, //dame
            new double[][]{{1_000_000_000_000_000D}}, //hp
            new short[]{838, 839, 840}, //outfit
            new short[]{2, 3, 4, 8, 9, 10, 15, 16, 17, 154}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 7, 300},
                {Skill.KAMEJOKO, 7, 300},
                {Skill.THAI_DUONG_HA_SAN, 3, 50000},
                {Skill.BIEN_KHI, 6, 50000},
                {Skill.GALICK, 7, 300}
            },
            _10_PHUT
    );
    public static final BossData THIEN_SU5 = new BossData(
            "Thiên Sứ [5]", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            20000000, //dame
            new double[][]{{1_000_000_000_000D}}, //hp
            new short[]{838, 839, 840}, //outfit
            new short[]{2, 3, 4, 8, 9, 10, 15, 16, 17, 154}, //map join
            new int[][]{ //skill
                {Skill.LIEN_HOAN, 7, 300},
                {Skill.KAMEJOKO, 7, 300},
                {Skill.THAI_DUONG_HA_SAN, 3, 50000},
                {Skill.BIEN_KHI, 6, 50000},
                {Skill.GALICK, 7, 300}
            },
            _10_PHUT
    );

    public static final BossData NPC_LEO_THAP = new BossData(
            "Boss Tháp", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            2000, //dame
            new double[][]{{550000}}, //hp
            new short[]{1586, 1595, 1596}, //outfit
            new short[]{175}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000}, {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200}, {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.GALICK, 1, 1000}},
            _1_GIAY
    );

    public static final BossData TU_TIEN_1 = new BossData(
            "Cađíc SSJ", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            2000000000, //dame
            new double[][]{{50_000_000_000_000_000_000D}}, //hp
            new short[]{1816, 1817, 1818}, //outfit
            new short[]{106}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000}, {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200}, {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.GALICK, 1, 1000}},
            _10_PHUT
    );

    public static final BossData TU_TIEN_2 = new BossData(
            "Cađíc SSJ 2", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            2000000, //dame
            new double[][]{{50_000_000_000_000_000_000D}}, //hp
            new short[]{1819, 1820, 1821}, //outfit
            new short[]{108}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000}, {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200}, {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.GALICK, 1, 1000}},
            _10_PHUT
    );

    public static final BossData TU_TIEN_3 = new BossData(
            "Cađíc SSJ Blue", //name
            ConstPlayer.XAYDA, //gender
            Boss.DAME_NORMAL, //type dame
            Boss.HP_NORMAL, //type hp
            2000000000, //dame
            new double[][]{{500_000_000_000_000_000_000D}}, //hp
            new short[]{1813, 1814, 1815}, //outfit
            new short[]{107}, //map join
            new int[][]{ //skill
                {Skill.DRAGON, 1, 1000}, {Skill.DRAGON, 2, 2000}, {Skill.DRAGON, 3, 3000}, {Skill.DRAGON, 7, 7000},
                {Skill.ANTOMIC, 1, 1000}, {Skill.ANTOMIC, 2, 1200}, {Skill.ANTOMIC, 4, 1500}, {Skill.ANTOMIC, 5, 1700},
                {Skill.GALICK, 1, 1000}},
            _10_PHUT
    );
}
