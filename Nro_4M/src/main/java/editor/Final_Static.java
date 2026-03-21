package editor;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import nro.models.skill.Skill;

public class Final_Static {

    public static final boolean EFF_MAP = false; // true là mở effmap - false là tắt effmap
    public static final boolean LOCK_MAP_CHUNGSINHBINHDANG = true; // true là khóa - false là mở
    public static final short[] VatPhamRoiChungSinhBinhDang = { 1986, 1987, 1988, 1989, 1990, 1991, 1992, 1993, 1994 }; // Vật
                                                                                                                        // phẩm
                                                                                                                        // map
                                                                                                                        // chúng
                                                                                                                        // sinh
                                                                                                                        // bình
                                                                                                                        // đẳng
    public static final short[] ItemRoiChungSinhBinhDang = { 1849, 1850, 1851, 1852, 1853, 1854, 1855, 1856, 1857 }; // Item
                                                                                                                     // map
                                                                                                                     // chúng
                                                                                                                     // sinh
                                                                                                                     // bình
                                                                                                                     // đẳng
    public static final Set<Byte> LOAI_SKILL_CSBD = new HashSet<>(Arrays.asList(
            Skill.BIEN_HINH,
            Skill.PHAN_THAN,
            Skill.TAI_TAO_SUC_MANH,
            Skill.MA_PHONG_BA,
            Skill.LIEN_HOAN_CHUONG,
            Skill.SUPER_KAME,
            Skill.KHIEN_NANG_LUONG,
            Skill.MASENKO,
            Skill.TRI_THUONG,
            Skill.MAKANKOSAPPO,
            Skill.DE_TRUNG,
            Skill.LIEN_HOAN,
            Skill.SOCOLA,
            Skill.ANTOMIC,
            Skill.TAI_TAO_NANG_LUONG,
            Skill.BIEN_KHI,
            Skill.TU_SAT,
            Skill.HUYT_SAO,
            Skill.TROI,
            Skill.KAMEJOKO,
            Skill.THAI_DUONG_HA_SAN,
            Skill.KAIOKEN,
            Skill.QUA_CAU_KENH_KHI,
            Skill.DICH_CHUYEN_TUC_THOI,
            Skill.THOI_MIEN));
    public static final Set<Integer> ITEM_ID_DE_TU = Set.of(
            1596, 1593, 1590, 1587, 1584, // trái đất ( Đệ Tử )
            1598, 1595, 1592, 1589, 1586, // xayda ( Đệ Tử )
            1597, 1594, 1591, 1588, 1585, // namek ( Đệ Tử )
            1401, 1402, 1403, 1404, 1405 // Thánh Tôn ( Đệ Tử )
    );
    public static final Set<Integer> ITEM_ID_CAI_TRANG_SHOP_CHO_GO_SPL = Set.of(
            2058, 2062, // Shop hồng ngọc
            1834, 1836, 1837, 1838, // Shop xu sky
            1998, 2035, 2036 // Shop xu sky
    );
    public static final Set<Integer> ITEM_ID_CAI_TRANG_PHUC_LOI_CHO_GO_SPL = Set.of(
            1844, 1845);
    public static final short[] ALLOW_ITEMS = { 457, 225 };

    public static boolean isAllowTachItem(int itemId) {
        for (short id : ALLOW_ITEMS) {
            if (id == itemId) {
                return true;
            }
        }
        return false;
    }

    private static class ________________________Chỉ_số_hợp_thể_đệ______________________ {
    }

    public static short hpPettype1 = 10; // đệ bát giới mập
    public static short mpPettype1 = 10;
    public static short damePettype1 = 10;

    public static short hpPettype2 = 30; // Bư Teen
    public static short mpPettype2 = 30;
    public static short damePettype2 = 30;

    public static short hpPettype3 = 60; // Thần Hủy Diệt
    public static short mpPettype3 = 60;
    public static short damePettype3 = 60;

    public static short hpPettype4 = 90; // Thần Vũ Trụ
    public static short mpPettype4 = 90;
    public static short damePettype4 = 90;

    public static short hpPettype5 = 170; // Chiến Thần
    public static short mpPettype5 = 170;
    public static short damePettype5 = 170;

    public static short hpPettype6 = 120; // Obito Lục Đạo
    public static short mpPettype6 = 120;
    public static short damePettype6 = 120;

    public static short hpPettype7 = 70;
    public static short mpPettype7 = 70;
    public static short damePettype7 = 70;

    public static short hpPettype8 = 70;
    public static short mpPettype8 = 70;
    public static short damePettype8 = 70;

    public static short hpPettype9 = 150;
    public static short mpPettype9 = 150;
    public static short damePettype9 = 150;

    public static short hpPettype10 = 250;
    public static short mpPettype10 = 250;
    public static short damePettype10 = 250;

    public static short hpPettype11 = 500;
    public static short mpPettype11 = 500;
    public static short damePettype11 = 500;

}
