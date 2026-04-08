package nro.models.player;

import editor.Final_Static;
import nro.attr.Attribute;
import nro.card.Card;
import nro.card.CollectionBook;
import nro.consts.ConstAttribute;
import nro.consts.ConstPlayer;
import nro.consts.ConstRatio;
import nro.models.clan.Buff;
import nro.models.intrinsic.Intrinsic;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.skill.Skill;
import nro.power.PowerLimit;
import nro.power.PowerLimitManager;
import nro.server.Manager;
import nro.server.ServerManager;
import nro.services.*;
import nro.utils.Log;
import nro.utils.SkillUtil;
import nro.utils.Util;

import java.util.ArrayList;
import java.util.List;
import nro.models.map.ChungSinhBinhDang.ChungSinhBinhDang;

public class NPoint {

    public static final byte MAX_LIMIT = 11;

    public Player player;
    public boolean isCrit;
    public boolean isCrit100;

    private Intrinsic intrinsic;
    private int percentDameIntrinsic;
    public int dameAfter;

    /*-----------------------Chỉ số cơ bản------------------------------------*/
    public byte numAttack;
    public short stamina, maxStamina;

    public byte limitPower;
    public long power;
    public long tiemNang;
    public double mpg, hpg;
    public double dameg;

    public double hp, hpMax;
    public double mp, mpMax;
    public double dame;
    public double def;
    public double defg;
    public int crit, critg;
    public byte speed = 5;

    public boolean teleport;

    /**
     * Chỉ số cộng thêm
     */
    public int hpAdd, mpAdd, dameAdd, defAdd, critAdd, hpHoiAdd, mpHoiAdd;

    /**
     * //+#% sức đánh chí mạng
     */
    public List<Integer> tlDameCrit;

    public boolean buffExpSatellite, buffDefenseSatellite;

    /**
     * Tỉ lệ hp, mp cộng thêm
     */
    public List<Integer> tlHp, tlMp;

    /**
     * Điểm tích lũy
     */
    public List<Integer> TichLuy;

    /**
     * Tỉ lệ giáp cộng thêm
     */
    public List<Integer> tlDef;

    /**
     * Tỉ lệ giáp cộng thêm
     */
    public List<Integer> tlxuyenDef;

    /**
     * Tỉ lệ sức đánh/ sức đánh khi đánh quái
     */
    public List<Integer> tlDame, tlDameAttMob;

    /**
     * Lượng hp, mp hồi mỗi 30s, mp hồi cho người khác
     */
    public double hpHoi, mpHoi, mpHoiCute;

    /**
     * Tỉ lệ hp, mp hồi cộng thêm
     */
    public short tlHpHoi, tlMpHoi;

    /**
     * Tỉ lệ hp, mp hồi bản thân và đồng đội cộng thêm
     */
    public short tlHpHoiBanThanVaDongDoi, tlMpHoiBanThanVaDongDoi;

    /**
     * Tỉ lệ hút hp, mp khi đánh, hp khi đánh quái
     */
    public short tlHutHp, tlHutMp, tlHutHpMob;

    /**
     * Tỉ lệ hút hp, mp xung quanh mỗi 5s
     */
    public short tlHutHpMpXQ;

    /**
     * Tỉ lệ phản sát thương
     */
    public short tlPST;

    /**
     * Tỉ lệ tiềm năng sức mạnh
     */
    public List<Integer> tlTNSM;
    public int tlTNSMPet;

    /**
     * Tỉ lệ vàng cộng thêm
     */
    public short tlGold;

    /**
     * Tỉ lệ né đòn
     */
    public short tlNeDon, tlchinhxac;

    /**
     * Tỉ lệ sức đánh đẹp cộng thêm cho bản thân và người xung quanh
     */
    public List<Integer> tlSDDep;

    /**
     * Tỉ lệ giảm sức đánh
     */
    public short tlSubSD;
    public List<Integer> tlSpeed;
    public int mstChuong;
    public int tlGiamst;
    public int tlxuyengiap;

    /*------------------------Effect skin-------------------------------------*/
    public Item trainArmor;
    public boolean wornTrainArmor;
    public boolean wearingTrainArmor;

    public boolean wearingVoHinh;
    public boolean isKhongLanh;
    public boolean isNgoaiHinh;

    public short tlHpGiamODo;

    public PowerLimit powerLimit;
    public boolean wearingDrabula;
    public boolean wearingMabu;
    public boolean wearingBuiBui;

    public boolean wearingNezuko;
    public boolean wearingTanjiro;
    public boolean wearingInosuke;
    public boolean wearingInoHashi;
    public boolean wearingZenitsu;
    public int tlDameChuong;
    public boolean xDameChuong;
    public boolean wearingYacon;
    public boolean wearingRedNoelHat;
    public boolean wearingGrayNoelHat;
    public boolean wearingBlueNoelHat;
    public boolean wearingNoelHat;

    public boolean TangLienHoan;
    public boolean TangGalick;
    public boolean TangKaioken;
    public boolean TangKame;

    public NPoint(Player player) {
        this.player = player;
        this.tlHp = new ArrayList<>();
        this.tlMp = new ArrayList<>();
        this.tlDef = new ArrayList<>();
        this.tlxuyenDef = new ArrayList<>();
        this.tlDame = new ArrayList<>();
        this.tlDameAttMob = new ArrayList<>();
        this.tlSDDep = new ArrayList<>();
        this.tlTNSM = new ArrayList<>();
        this.tlDameCrit = new ArrayList<>();
        this.tlSpeed = new ArrayList<>();
    }

    public void initPowerLimit() {
        powerLimit = PowerLimitManager.getInstance().get(limitPower);
    }

    public void setPointKhamNgoc() {
        if (this.player.active_kham_ngoc == 1) {
            for (int j = 0; j < KhamNgoc.KHAM_NGOC.size(); j++) {
                KhamNgoc manager = KhamNgoc.KHAM_NGOC.get(j);
                for (int k = 0; k < manager.khamNgocTemplates.size(); k++) {
                    KhamNgocTemplate template = manager.khamNgocTemplates.get(k);
                    if (this.player.khamNgoc.get(j).levelNro >= template.level) {
                        ItemOption io = template.options;
                        switch (io.optionTemplate.id) {
                            case 0: //Tấn công +#
                                this.player.nPoint.dameAdd += io.param;
                                break;
                            case 2: //HP, KI+#000
                                this.player.nPoint.hpAdd += io.param * 1000;
                                this.player.nPoint.mpAdd += io.param * 1000;
                                break;
                            case 3: // vô hiệu vả biến st chưởng thành ki
                                this.player.nPoint.mstChuong += io.param;
                                break;
                            case 5: //+#% sức đánh chí mạng
                                this.player.nPoint.tlDameCrit.add(io.param);
                                break;
                            case 6: //HP+#
                                this.player.nPoint.hpAdd += io.param;
                                break;
                            case 7: //KI+#
                                this.player.nPoint.mpAdd += io.param;
                                break;
                            case 8: //Hút #% HP, KI xung quanh mỗi 5 giây
                                this.player.nPoint.tlHutHpMpXQ += io.param;
                                break;
                            case 14: //Chí mạng+#%
                                this.player.nPoint.critAdd += io.param;
                                break;
                            case 18: // #% chính xác
                                this.player.nPoint.tlchinhxac += io.param;
                                break;
                            case 19: //Tấn công+#% khi đánh quái
                                this.player.nPoint.tlDameAttMob.add(io.param);
                                break;
                            case 22: //HP+#K
                                this.player.nPoint.hpAdd += io.param * 1000;
                                break;
                            case 23: //MP+#K
                                this.player.nPoint.mpAdd += io.param * 1000;
                                break;
                            case 24:
                                this.player.nPoint.wearingBuiBui = true;
                                break;
                            case 25:
                                this.player.nPoint.wearingYacon = true;
                                break;
                            case 26:
                                this.player.nPoint.wearingDrabula = true;
                                this.player.effectSkin.lastTimeDrabula = System.currentTimeMillis();
                                break;
                            case 29:
                                this.player.nPoint.wearingMabu = true;
                                break;
                            case 27: //+# HP/30s
                                this.player.nPoint.hpHoiAdd += io.param;
                                break;
                            case 28: //+# KI/30s
                                this.player.nPoint.mpHoiAdd += io.param;
                                break;
                            case 33: //dịch chuyển tức thời
                                this.player.nPoint.teleport = true;
                                break;
                            case 47: //Giáp+#
                                this.player.nPoint.defAdd += io.param;
                                break;
                            case 48: //HP/KI+#
                                this.player.nPoint.hpAdd += io.param;
                                this.player.nPoint.mpAdd += io.param;
                                break;
                            case 49: //Tấn công+#%
                            case 50: //Sức đánh+#%
                                this.player.nPoint.tlDame.add(io.param);
                                break;
                            case 77: //HP+#%
                                this.player.nPoint.tlHp.add(io.param);
                                break;
                            case 80: //HP+#%/30s
                                this.player.nPoint.tlHpHoi += io.param;
                                break;
                            case 81: //MP+#%/30s
                                this.player.nPoint.tlMpHoi += io.param;
                                break;
                            case 88: //Cộng #% exp khi đánh quái
                                this.player.nPoint.tlTNSM.add(io.param);
                                break;
                            case 94: //Giáp #%
                                this.player.nPoint.tlDef.add(io.param);
                                break;
                            case 95: //Biến #% tấn công thành HP
                                this.player.nPoint.tlHutHp += io.param;
                                break;
                            case 96: //Biến #% tấn công thành MP
                                this.player.nPoint.tlHutMp += io.param;
                                break;
                            case 97: //Phản #% sát thương
                                this.player.nPoint.tlPST += io.param;
                                break;
                            case 98: //xuyên Giáp #%
                                this.player.nPoint.tlxuyenDef.add(io.param);
                                break;
                            case 100: //+#% vàng từ quái
                                this.player.nPoint.tlGold += io.param;
                                break;
                            case 101: //+#% TN,SM
                                this.player.nPoint.tlTNSM.add(io.param);
                                break;
                            case 103: //KI +#%
                                this.player.nPoint.tlMp.add(io.param);
                                break;
                            case 104: //Biến #% tấn công quái thành HP
                                this.player.nPoint.tlHutHpMob += io.param;
                                break;
                            case 105: //Vô hình khi không đánh quái và boss
                                this.player.nPoint.wearingVoHinh = true;
                                break;
                            case 106: //Không ảnh hưởng bởi cái lạnh
                                this.player.nPoint.isKhongLanh = true;
                                break;
                            case 108: //#% Né đòn
                                this.player.nPoint.tlNeDon += io.param;
                                break;
                            case 109: //Hôi, giảm #% HP
                                this.player.nPoint.tlHpGiamODo += io.param;
                                break;
                            case 114:
                                this.player.nPoint.tlSpeed.add(io.param);
                                break;
                            case 117: //Đẹp +#% SĐ cho mình và người xung quanh
                                this.player.nPoint.tlSDDep.add(io.param);
                                break;
                            case 147: //+#% sức đánh
                                this.player.nPoint.tlDame.add(io.param);
                                break;
                            case 155: //Giảm 50% sức đánh, HP, KI và +#% SM, TN, vàng từ quái
                                this.player.nPoint.tlSubSD += 50;
                                this.player.nPoint.tlTNSM.add(io.param);
                                this.player.nPoint.tlGold += io.param;
                                break;
                            case 160:
                                this.player.nPoint.tlTNSMPet += io.param;
                                break;
                            case 162: //Cute hồi #% KI/s bản thân và xung quanh
                                this.player.nPoint.mpHoiCute += io.param;
                                break;
                            case 173: //Phục hồi #% HP và KI cho đồng đội
                                this.player.nPoint.tlHpHoiBanThanVaDongDoi += io.param;
                                this.player.nPoint.tlMpHoiBanThanVaDongDoi += io.param;
                                break;
                            case 189:
                                this.player.nPoint.wearingNezuko = true;
                                break;
                            case 190:
                                this.player.nPoint.wearingTanjiro = true;
                                break;
                            case 191:
                                this.player.nPoint.wearingInoHashi = true;
                                break;
                            case 192:
                                this.player.nPoint.wearingInosuke = true;
                                break;
                            case 193:
                                this.player.nPoint.wearingZenitsu = true;
                                break;
                            case 194:
                                this.player.nPoint.tlDameChuong = 3;
                                break;
                            case 195:
                                this.player.nPoint.tlDameChuong = 4;
                                break;
                        }
                    }
                }
            }
        }
    }

    public void setPointRuongSuuTam() {
        setPointRuongCaiTrang();
        setPointRuongPhuKien();
        setPointRuongPet();
        setPointRuongLinhThu();
        setPointRuongThuCuoi();
    }

    public void setPointRuongCaiTrang() {
        if (this.player.active_ruong_suu_tam == 1) {
            for (int i = 0; i < RuongSuuTam.listCaiTrang.size(); i++) {
                for (int k = 0; k < this.player.ruongSuuTam.RuongCaiTrang.size(); k++) {
                    Item listRuong = RuongSuuTam.listCaiTrang.get(i);
                    Item template = this.player.ruongSuuTam.RuongCaiTrang.get(k);
                    if (template.template != null && listRuong.template != null && listRuong.template.id == template.template.id) {
                        for (ItemOption io : listRuong.itemOptions) {
                            switch (io.optionTemplate.id) {
                                case 0: //Tấn công +#
                                    this.player.nPoint.dameAdd += io.param;
                                    break;
                                case 2: //HP, KI+#000
                                    this.player.nPoint.hpAdd += io.param * 1000;
                                    this.player.nPoint.mpAdd += io.param * 1000;
                                    break;
                                case 3: // vô hiệu vả biến st chưởng thành ki
                                    this.player.nPoint.mstChuong += io.param;
                                    break;
                                case 5: //+#% sức đánh chí mạng
                                    this.player.nPoint.tlDameCrit.add(io.param);
                                    break;
                                case 6: //HP+#
                                    this.player.nPoint.hpAdd += io.param;
                                    break;
                                case 7: //KI+#
                                    this.player.nPoint.mpAdd += io.param;
                                    break;
                                case 8: //Hút #% HP, KI xung quanh mỗi 5 giây
                                    this.player.nPoint.tlHutHpMpXQ += io.param;
                                    break;
                                case 14: //Chí mạng+#%
                                    this.player.nPoint.critAdd += io.param;
                                    break;
                                case 18: // #% chính xác
                                    this.player.nPoint.tlchinhxac += io.param;
                                    break;
                                case 19: //Tấn công+#% khi đánh quái
                                    this.player.nPoint.tlDameAttMob.add(io.param);
                                    break;
                                case 22: //HP+#K
                                    this.player.nPoint.hpAdd += io.param * 1000;
                                    break;
                                case 23: //MP+#K
                                    this.player.nPoint.mpAdd += io.param * 1000;
                                    break;
                                case 24:
                                    this.player.nPoint.wearingBuiBui = true;
                                    break;
                                case 25:
                                    this.player.nPoint.wearingYacon = true;
                                    break;
                                case 26:
                                    this.player.nPoint.wearingDrabula = true;
                                    this.player.effectSkin.lastTimeDrabula = System.currentTimeMillis();
                                    break;
                                case 29:
                                    this.player.nPoint.wearingMabu = true;
                                    break;
                                case 27: //+# HP/30s
                                    this.player.nPoint.hpHoiAdd += io.param;
                                    break;
                                case 28: //+# KI/30s
                                    this.player.nPoint.mpHoiAdd += io.param;
                                    break;
                                case 33: //dịch chuyển tức thời
                                    this.player.nPoint.teleport = true;
                                    break;
                                case 47: //Giáp+#
                                    this.player.nPoint.defAdd += io.param;
                                    break;
                                case 48: //HP/KI+#
                                    this.player.nPoint.hpAdd += io.param;
                                    this.player.nPoint.mpAdd += io.param;
                                    break;
                                case 49: //Tấn công+#%
                                case 50: //Sức đánh+#%
                                    this.player.nPoint.tlDame.add(io.param);
                                    break;
                                case 77: //HP+#%
                                    this.player.nPoint.tlHp.add(io.param);
                                    break;
                                case 80: //HP+#%/30s
                                    this.player.nPoint.tlHpHoi += io.param;
                                    break;
                                case 81: //MP+#%/30s
                                    this.player.nPoint.tlMpHoi += io.param;
                                    break;
                                case 88: //Cộng #% exp khi đánh quái
                                    this.player.nPoint.tlTNSM.add(io.param);
                                    break;
                                case 94: //Giáp #%
                                    this.player.nPoint.tlDef.add(io.param);
                                    break;
                                case 95: //Biến #% tấn công thành HP
                                    this.player.nPoint.tlHutHp += io.param;
                                    break;
                                case 96: //Biến #% tấn công thành MP
                                    this.player.nPoint.tlHutMp += io.param;
                                    break;
                                case 97: //Phản #% sát thương
                                    this.player.nPoint.tlPST += io.param;
                                    break;
                                case 98: //xuyên Giáp #%
                                    this.player.nPoint.tlxuyenDef.add(io.param);
                                    break;
                                case 100: //+#% vàng từ quái
                                    this.player.nPoint.tlGold += io.param;
                                    break;
                                case 101: //+#% TN,SM
                                    this.player.nPoint.tlTNSM.add(io.param);
                                    break;
                                case 103: //KI +#%
                                    this.player.nPoint.tlMp.add(io.param);
                                    break;
                                case 104: //Biến #% tấn công quái thành HP
                                    this.player.nPoint.tlHutHpMob += io.param;
                                    break;
                                case 105: //Vô hình khi không đánh quái và boss
                                    this.player.nPoint.wearingVoHinh = true;
                                    break;
                                case 106: //Không ảnh hưởng bởi cái lạnh
                                    this.player.nPoint.isKhongLanh = true;
                                    break;
                                case 108: //#% Né đòn
                                    this.player.nPoint.tlNeDon += io.param;
                                    break;
                                case 109: //Hôi, giảm #% HP
                                    this.player.nPoint.tlHpGiamODo += io.param;
                                    break;
                                case 114:
                                    this.player.nPoint.tlSpeed.add(io.param);
                                    break;
                                case 117: //Đẹp +#% SĐ cho mình và người xung quanh
                                    this.player.nPoint.tlSDDep.add(io.param);
                                    break;
                                case 147: //+#% sức đánh
                                    this.player.nPoint.tlDame.add(io.param);
                                    break;
                                case 155: //Giảm 50% sức đánh, HP, KI và +#% SM, TN, vàng từ quái
                                    this.player.nPoint.tlSubSD += 50;
                                    this.player.nPoint.tlTNSM.add(io.param);
                                    this.player.nPoint.tlGold += io.param;
                                    break;
                                case 160:
                                    this.player.nPoint.tlTNSMPet += io.param;
                                    break;
                                case 162: //Cute hồi #% KI/s bản thân và xung quanh
                                    this.player.nPoint.mpHoiCute += io.param;
                                    break;
                                case 173: //Phục hồi #% HP và KI cho đồng đội
                                    this.player.nPoint.tlHpHoiBanThanVaDongDoi += io.param;
                                    this.player.nPoint.tlMpHoiBanThanVaDongDoi += io.param;
                                    break;
                                case 189:
                                    this.player.nPoint.wearingNezuko = true;
                                    break;
                                case 190:
                                    this.player.nPoint.wearingTanjiro = true;
                                    break;
                                case 191:
                                    this.player.nPoint.wearingInoHashi = true;
                                    break;
                                case 192:
                                    this.player.nPoint.wearingInosuke = true;
                                    break;
                                case 193:
                                    this.player.nPoint.wearingZenitsu = true;
                                    break;
                                case 194:
                                    this.player.nPoint.tlDameChuong = 3;
                                    break;
                                case 195:
                                    this.player.nPoint.tlDameChuong = 4;
                                    break;
                            }
                        }
                    }
                }
            }
        }
    }

    public void setPointRuongPhuKien() {
        if (this.player.active_ruong_suu_tam == 1) {
            for (int i = 0; i < RuongSuuTam.listPhuKien.size(); i++) {
                for (int k = 0; k < this.player.ruongSuuTam.RuongPhuKien.size(); k++) {
                    Item listRuong = RuongSuuTam.listPhuKien.get(i);
                    Item template = this.player.ruongSuuTam.RuongPhuKien.get(k);
                    if (template.template != null && listRuong.template != null && listRuong.template.id == template.template.id) {
                        for (ItemOption io : listRuong.itemOptions) {
                            switch (io.optionTemplate.id) {
                                case 0: //Tấn công +#
                                    this.player.nPoint.dameAdd += io.param;
                                    break;
                                case 2: //HP, KI+#000
                                    this.player.nPoint.hpAdd += io.param * 1000;
                                    this.player.nPoint.mpAdd += io.param * 1000;
                                    break;
                                case 3: // vô hiệu vả biến st chưởng thành ki
                                    this.player.nPoint.mstChuong += io.param;
                                    break;
                                case 5: //+#% sức đánh chí mạng
                                    this.player.nPoint.tlDameCrit.add(io.param);
                                    break;
                                case 6: //HP+#
                                    this.player.nPoint.hpAdd += io.param;
                                    break;
                                case 7: //KI+#
                                    this.player.nPoint.mpAdd += io.param;
                                    break;
                                case 8: //Hút #% HP, KI xung quanh mỗi 5 giây
                                    this.player.nPoint.tlHutHpMpXQ += io.param;
                                    break;
                                case 14: //Chí mạng+#%
                                    this.player.nPoint.critAdd += io.param;
                                    break;
                                case 18: // #% chính xác
                                    this.player.nPoint.tlchinhxac += io.param;
                                    break;
                                case 19: //Tấn công+#% khi đánh quái
                                    this.player.nPoint.tlDameAttMob.add(io.param);
                                    break;
                                case 22: //HP+#K
                                    this.player.nPoint.hpAdd += io.param * 1000;
                                    break;
                                case 23: //MP+#K
                                    this.player.nPoint.mpAdd += io.param * 1000;
                                    break;
                                case 24:
                                    this.player.nPoint.wearingBuiBui = true;
                                    break;
                                case 25:
                                    this.player.nPoint.wearingYacon = true;
                                    break;
                                case 26:
                                    this.player.nPoint.wearingDrabula = true;
                                    this.player.effectSkin.lastTimeDrabula = System.currentTimeMillis();
                                    break;
                                case 29:
                                    this.player.nPoint.wearingMabu = true;
                                    break;
                                case 27: //+# HP/30s
                                    this.player.nPoint.hpHoiAdd += io.param;
                                    break;
                                case 28: //+# KI/30s
                                    this.player.nPoint.mpHoiAdd += io.param;
                                    break;
                                case 33: //dịch chuyển tức thời
                                    this.player.nPoint.teleport = true;
                                    break;
                                case 47: //Giáp+#
                                    this.player.nPoint.defAdd += io.param;
                                    break;
                                case 48: //HP/KI+#
                                    this.player.nPoint.hpAdd += io.param;
                                    this.player.nPoint.mpAdd += io.param;
                                    break;
                                case 49: //Tấn công+#%
                                case 50: //Sức đánh+#%
                                    this.player.nPoint.tlDame.add(io.param);
                                    break;
                                case 77: //HP+#%
                                    this.player.nPoint.tlHp.add(io.param);
                                    break;
                                case 80: //HP+#%/30s
                                    this.player.nPoint.tlHpHoi += io.param;
                                    break;
                                case 81: //MP+#%/30s
                                    this.player.nPoint.tlMpHoi += io.param;
                                    break;
                                case 88: //Cộng #% exp khi đánh quái
                                    this.player.nPoint.tlTNSM.add(io.param);
                                    break;
                                case 94: //Giáp #%
                                    this.player.nPoint.tlDef.add(io.param);
                                    break;
                                case 95: //Biến #% tấn công thành HP
                                    this.player.nPoint.tlHutHp += io.param;
                                    break;
                                case 96: //Biến #% tấn công thành MP
                                    this.player.nPoint.tlHutMp += io.param;
                                    break;
                                case 97: //Phản #% sát thương
                                    this.player.nPoint.tlPST += io.param;
                                    break;
                                case 98: //xuyên Giáp #%
                                    this.player.nPoint.tlxuyenDef.add(io.param);
                                    break;
                                case 100: //+#% vàng từ quái
                                    this.player.nPoint.tlGold += io.param;
                                    break;
                                case 101: //+#% TN,SM
                                    this.player.nPoint.tlTNSM.add(io.param);
                                    break;
                                case 103: //KI +#%
                                    this.player.nPoint.tlMp.add(io.param);
                                    break;
                                case 104: //Biến #% tấn công quái thành HP
                                    this.player.nPoint.tlHutHpMob += io.param;
                                    break;
                                case 105: //Vô hình khi không đánh quái và boss
                                    this.player.nPoint.wearingVoHinh = true;
                                    break;
                                case 106: //Không ảnh hưởng bởi cái lạnh
                                    this.player.nPoint.isKhongLanh = true;
                                    break;
                                case 108: //#% Né đòn
                                    this.player.nPoint.tlNeDon += io.param;
                                    break;
                                case 109: //Hôi, giảm #% HP
                                    this.player.nPoint.tlHpGiamODo += io.param;
                                    break;
                                case 114:
                                    this.player.nPoint.tlSpeed.add(io.param);
                                    break;
                                case 117: //Đẹp +#% SĐ cho mình và người xung quanh
                                    this.player.nPoint.tlSDDep.add(io.param);
                                    break;
                                case 147: //+#% sức đánh
                                    this.player.nPoint.tlDame.add(io.param);
                                    break;
                                case 155: //Giảm 50% sức đánh, HP, KI và +#% SM, TN, vàng từ quái
                                    this.player.nPoint.tlSubSD += 50;
                                    this.player.nPoint.tlTNSM.add(io.param);
                                    this.player.nPoint.tlGold += io.param;
                                    break;
                                case 160:
                                    this.player.nPoint.tlTNSMPet += io.param;
                                    break;
                                case 162: //Cute hồi #% KI/s bản thân và xung quanh
                                    this.player.nPoint.mpHoiCute += io.param;
                                    break;
                                case 173: //Phục hồi #% HP và KI cho đồng đội
                                    this.player.nPoint.tlHpHoiBanThanVaDongDoi += io.param;
                                    this.player.nPoint.tlMpHoiBanThanVaDongDoi += io.param;
                                    break;
                                case 189:
                                    this.player.nPoint.wearingNezuko = true;
                                    break;
                                case 190:
                                    this.player.nPoint.wearingTanjiro = true;
                                    break;
                                case 191:
                                    this.player.nPoint.wearingInoHashi = true;
                                    break;
                                case 192:
                                    this.player.nPoint.wearingInosuke = true;
                                    break;
                                case 193:
                                    this.player.nPoint.wearingZenitsu = true;
                                    break;
                                case 194:
                                    this.player.nPoint.tlDameChuong = 3;
                                    break;
                                case 195:
                                    this.player.nPoint.tlDameChuong = 4;
                                    break;
                            }
                        }
                    }
                }
            }
        }
    }

    public void setPointRuongPet() {
        if (this.player.active_ruong_suu_tam == 1) {
            for (int i = 0; i < RuongSuuTam.listPet.size(); i++) {
                for (int k = 0; k < this.player.ruongSuuTam.RuongPet.size(); k++) {
                    Item listRuong = RuongSuuTam.listPet.get(i);
                    Item template = this.player.ruongSuuTam.RuongPet.get(k);
                    if (template.template != null && listRuong.template != null && listRuong.template.id == template.template.id) {
                        for (ItemOption io : listRuong.itemOptions) {
                            switch (io.optionTemplate.id) {
                                case 0: //Tấn công +#
                                    this.player.nPoint.dameAdd += io.param;
                                    break;
                                case 2: //HP, KI+#000
                                    this.player.nPoint.hpAdd += io.param * 1000;
                                    this.player.nPoint.mpAdd += io.param * 1000;
                                    break;
                                case 3: // vô hiệu vả biến st chưởng thành ki
                                    this.player.nPoint.mstChuong += io.param;
                                    break;
                                case 5: //+#% sức đánh chí mạng
                                    this.player.nPoint.tlDameCrit.add(io.param);
                                    break;
                                case 6: //HP+#
                                    this.player.nPoint.hpAdd += io.param;
                                    break;
                                case 7: //KI+#
                                    this.player.nPoint.mpAdd += io.param;
                                    break;
                                case 8: //Hút #% HP, KI xung quanh mỗi 5 giây
                                    this.player.nPoint.tlHutHpMpXQ += io.param;
                                    break;
                                case 14: //Chí mạng+#%
                                    this.player.nPoint.critAdd += io.param;
                                    break;
                                case 18: // #% chính xác
                                    this.player.nPoint.tlchinhxac += io.param;
                                    break;
                                case 19: //Tấn công+#% khi đánh quái
                                    this.player.nPoint.tlDameAttMob.add(io.param);
                                    break;
                                case 22: //HP+#K
                                    this.player.nPoint.hpAdd += io.param * 1000;
                                    break;
                                case 23: //MP+#K
                                    this.player.nPoint.mpAdd += io.param * 1000;
                                    break;
                                case 24:
                                    this.player.nPoint.wearingBuiBui = true;
                                    break;
                                case 25:
                                    this.player.nPoint.wearingYacon = true;
                                    break;
                                case 26:
                                    this.player.nPoint.wearingDrabula = true;
                                    this.player.effectSkin.lastTimeDrabula = System.currentTimeMillis();
                                    break;
                                case 29:
                                    this.player.nPoint.wearingMabu = true;
                                    break;
                                case 27: //+# HP/30s
                                    this.player.nPoint.hpHoiAdd += io.param;
                                    break;
                                case 28: //+# KI/30s
                                    this.player.nPoint.mpHoiAdd += io.param;
                                    break;
                                case 33: //dịch chuyển tức thời
                                    this.player.nPoint.teleport = true;
                                    break;
                                case 47: //Giáp+#
                                    this.player.nPoint.defAdd += io.param;
                                    break;
                                case 48: //HP/KI+#
                                    this.player.nPoint.hpAdd += io.param;
                                    this.player.nPoint.mpAdd += io.param;
                                    break;
                                case 49: //Tấn công+#%
                                case 50: //Sức đánh+#%
                                    this.player.nPoint.tlDame.add(io.param);
                                    break;
                                case 77: //HP+#%
                                    this.player.nPoint.tlHp.add(io.param);
                                    break;
                                case 80: //HP+#%/30s
                                    this.player.nPoint.tlHpHoi += io.param;
                                    break;
                                case 81: //MP+#%/30s
                                    this.player.nPoint.tlMpHoi += io.param;
                                    break;
                                case 88: //Cộng #% exp khi đánh quái
                                    this.player.nPoint.tlTNSM.add(io.param);
                                    break;
                                case 94: //Giáp #%
                                    this.player.nPoint.tlDef.add(io.param);
                                    break;
                                case 95: //Biến #% tấn công thành HP
                                    this.player.nPoint.tlHutHp += io.param;
                                    break;
                                case 96: //Biến #% tấn công thành MP
                                    this.player.nPoint.tlHutMp += io.param;
                                    break;
                                case 97: //Phản #% sát thương
                                    this.player.nPoint.tlPST += io.param;
                                    break;
                                case 98: //xuyên Giáp #%
                                    this.player.nPoint.tlxuyenDef.add(io.param);
                                    break;
                                case 100: //+#% vàng từ quái
                                    this.player.nPoint.tlGold += io.param;
                                    break;
                                case 101: //+#% TN,SM
                                    this.player.nPoint.tlTNSM.add(io.param);
                                    break;
                                case 103: //KI +#%
                                    this.player.nPoint.tlMp.add(io.param);
                                    break;
                                case 104: //Biến #% tấn công quái thành HP
                                    this.player.nPoint.tlHutHpMob += io.param;
                                    break;
                                case 105: //Vô hình khi không đánh quái và boss
                                    this.player.nPoint.wearingVoHinh = true;
                                    break;
                                case 106: //Không ảnh hưởng bởi cái lạnh
                                    this.player.nPoint.isKhongLanh = true;
                                    break;
                                case 108: //#% Né đòn
                                    this.player.nPoint.tlNeDon += io.param;
                                    break;
                                case 109: //Hôi, giảm #% HP
                                    this.player.nPoint.tlHpGiamODo += io.param;
                                    break;
                                case 114:
                                    this.player.nPoint.tlSpeed.add(io.param);
                                    break;
                                case 117: //Đẹp +#% SĐ cho mình và người xung quanh
                                    this.player.nPoint.tlSDDep.add(io.param);
                                    break;
                                case 147: //+#% sức đánh
                                    this.player.nPoint.tlDame.add(io.param);
                                    break;
                                case 155: //Giảm 50% sức đánh, HP, KI và +#% SM, TN, vàng từ quái
                                    this.player.nPoint.tlSubSD += 50;
                                    this.player.nPoint.tlTNSM.add(io.param);
                                    this.player.nPoint.tlGold += io.param;
                                    break;
                                case 160:
                                    this.player.nPoint.tlTNSMPet += io.param;
                                    break;
                                case 162: //Cute hồi #% KI/s bản thân và xung quanh
                                    this.player.nPoint.mpHoiCute += io.param;
                                    break;
                                case 173: //Phục hồi #% HP và KI cho đồng đội
                                    this.player.nPoint.tlHpHoiBanThanVaDongDoi += io.param;
                                    this.player.nPoint.tlMpHoiBanThanVaDongDoi += io.param;
                                    break;
                                case 189:
                                    this.player.nPoint.wearingNezuko = true;
                                    break;
                                case 190:
                                    this.player.nPoint.wearingTanjiro = true;
                                    break;
                                case 191:
                                    this.player.nPoint.wearingInoHashi = true;
                                    break;
                                case 192:
                                    this.player.nPoint.wearingInosuke = true;
                                    break;
                                case 193:
                                    this.player.nPoint.wearingZenitsu = true;
                                    break;
                                case 194:
                                    this.player.nPoint.tlDameChuong = 3;
                                    break;
                                case 195:
                                    this.player.nPoint.tlDameChuong = 4;
                                    break;
                            }
                        }
                    }
                }
            }
        }
    }

    public void setPointRuongLinhThu() {
        if (this.player.active_ruong_suu_tam == 1) {
            for (int i = 0; i < RuongSuuTam.listLinhThu.size(); i++) {
                for (int k = 0; k < this.player.ruongSuuTam.RuongLinhThu.size(); k++) {
                    Item listRuong = RuongSuuTam.listLinhThu.get(i);
                    Item template = this.player.ruongSuuTam.RuongLinhThu.get(k);
                    if (template.template != null && listRuong.template != null && listRuong.template.id == template.template.id) {
                        for (ItemOption io : listRuong.itemOptions) {
                            switch (io.optionTemplate.id) {
                                case 0: //Tấn công +#
                                    this.player.nPoint.dameAdd += io.param;
                                    break;
                                case 2: //HP, KI+#000
                                    this.player.nPoint.hpAdd += io.param * 1000;
                                    this.player.nPoint.mpAdd += io.param * 1000;
                                    break;
                                case 3: // vô hiệu vả biến st chưởng thành ki
                                    this.player.nPoint.mstChuong += io.param;
                                    break;
                                case 5: //+#% sức đánh chí mạng
                                    this.player.nPoint.tlDameCrit.add(io.param);
                                    break;
                                case 6: //HP+#
                                    this.player.nPoint.hpAdd += io.param;
                                    break;
                                case 7: //KI+#
                                    this.player.nPoint.mpAdd += io.param;
                                    break;
                                case 8: //Hút #% HP, KI xung quanh mỗi 5 giây
                                    this.player.nPoint.tlHutHpMpXQ += io.param;
                                    break;
                                case 14: //Chí mạng+#%
                                    this.player.nPoint.critAdd += io.param;
                                    break;
                                case 18: // #% chính xác
                                    this.player.nPoint.tlchinhxac += io.param;
                                    break;
                                case 19: //Tấn công+#% khi đánh quái
                                    this.player.nPoint.tlDameAttMob.add(io.param);
                                    break;
                                case 22: //HP+#K
                                    this.player.nPoint.hpAdd += io.param * 1000;
                                    break;
                                case 23: //MP+#K
                                    this.player.nPoint.mpAdd += io.param * 1000;
                                    break;
                                case 24:
                                    this.player.nPoint.wearingBuiBui = true;
                                    break;
                                case 25:
                                    this.player.nPoint.wearingYacon = true;
                                    break;
                                case 26:
                                    this.player.nPoint.wearingDrabula = true;
                                    this.player.effectSkin.lastTimeDrabula = System.currentTimeMillis();
                                    break;
                                case 29:
                                    this.player.nPoint.wearingMabu = true;
                                    break;
                                case 27: //+# HP/30s
                                    this.player.nPoint.hpHoiAdd += io.param;
                                    break;
                                case 28: //+# KI/30s
                                    this.player.nPoint.mpHoiAdd += io.param;
                                    break;
                                case 33: //dịch chuyển tức thời
                                    this.player.nPoint.teleport = true;
                                    break;
                                case 47: //Giáp+#
                                    this.player.nPoint.defAdd += io.param;
                                    break;
                                case 48: //HP/KI+#
                                    this.player.nPoint.hpAdd += io.param;
                                    this.player.nPoint.mpAdd += io.param;
                                    break;
                                case 49: //Tấn công+#%
                                case 50: //Sức đánh+#%
                                    this.player.nPoint.tlDame.add(io.param);
                                    break;
                                case 77: //HP+#%
                                    this.player.nPoint.tlHp.add(io.param);
                                    break;
                                case 80: //HP+#%/30s
                                    this.player.nPoint.tlHpHoi += io.param;
                                    break;
                                case 81: //MP+#%/30s
                                    this.player.nPoint.tlMpHoi += io.param;
                                    break;
                                case 88: //Cộng #% exp khi đánh quái
                                    this.player.nPoint.tlTNSM.add(io.param);
                                    break;
                                case 94: //Giáp #%
                                    this.player.nPoint.tlDef.add(io.param);
                                    break;
                                case 95: //Biến #% tấn công thành HP
                                    this.player.nPoint.tlHutHp += io.param;
                                    break;
                                case 96: //Biến #% tấn công thành MP
                                    this.player.nPoint.tlHutMp += io.param;
                                    break;
                                case 97: //Phản #% sát thương
                                    this.player.nPoint.tlPST += io.param;
                                    break;
                                case 98: //xuyên Giáp #%
                                    this.player.nPoint.tlxuyenDef.add(io.param);
                                    break;
                                case 100: //+#% vàng từ quái
                                    this.player.nPoint.tlGold += io.param;
                                    break;
                                case 101: //+#% TN,SM
                                    this.player.nPoint.tlTNSM.add(io.param);
                                    break;
                                case 103: //KI +#%
                                    this.player.nPoint.tlMp.add(io.param);
                                    break;
                                case 104: //Biến #% tấn công quái thành HP
                                    this.player.nPoint.tlHutHpMob += io.param;
                                    break;
                                case 105: //Vô hình khi không đánh quái và boss
                                    this.player.nPoint.wearingVoHinh = true;
                                    break;
                                case 106: //Không ảnh hưởng bởi cái lạnh
                                    this.player.nPoint.isKhongLanh = true;
                                    break;
                                case 108: //#% Né đòn
                                    this.player.nPoint.tlNeDon += io.param;
                                    break;
                                case 109: //Hôi, giảm #% HP
                                    this.player.nPoint.tlHpGiamODo += io.param;
                                    break;
                                case 114:
                                    this.player.nPoint.tlSpeed.add(io.param);
                                    break;
                                case 117: //Đẹp +#% SĐ cho mình và người xung quanh
                                    this.player.nPoint.tlSDDep.add(io.param);
                                    break;
                                case 147: //+#% sức đánh
                                    this.player.nPoint.tlDame.add(io.param);
                                    break;
                                case 155: //Giảm 50% sức đánh, HP, KI và +#% SM, TN, vàng từ quái
                                    this.player.nPoint.tlSubSD += 50;
                                    this.player.nPoint.tlTNSM.add(io.param);
                                    this.player.nPoint.tlGold += io.param;
                                    break;
                                case 160:
                                    this.player.nPoint.tlTNSMPet += io.param;
                                    break;
                                case 162: //Cute hồi #% KI/s bản thân và xung quanh
                                    this.player.nPoint.mpHoiCute += io.param;
                                    break;
                                case 173: //Phục hồi #% HP và KI cho đồng đội
                                    this.player.nPoint.tlHpHoiBanThanVaDongDoi += io.param;
                                    this.player.nPoint.tlMpHoiBanThanVaDongDoi += io.param;
                                    break;
                                case 189:
                                    this.player.nPoint.wearingNezuko = true;
                                    break;
                                case 190:
                                    this.player.nPoint.wearingTanjiro = true;
                                    break;
                                case 191:
                                    this.player.nPoint.wearingInoHashi = true;
                                    break;
                                case 192:
                                    this.player.nPoint.wearingInosuke = true;
                                    break;
                                case 193:
                                    this.player.nPoint.wearingZenitsu = true;
                                    break;
                                case 194:
                                    this.player.nPoint.tlDameChuong = 3;
                                    break;
                                case 195:
                                    this.player.nPoint.tlDameChuong = 4;
                                    break;
                            }
                        }
                    }
                }
            }
        }
    }

    public void setPointRuongThuCuoi() {
        if (this.player.active_ruong_suu_tam == 1) {
            for (int i = 0; i < RuongSuuTam.listThuCuoi.size(); i++) {
                for (int k = 0; k < this.player.ruongSuuTam.RuongThuCuoi.size(); k++) {
                    Item listRuong = RuongSuuTam.listThuCuoi.get(i);
                    Item template = this.player.ruongSuuTam.RuongThuCuoi.get(k);
                    if (template.template != null && listRuong.template != null && listRuong.template.id == template.template.id) {
                        for (ItemOption io : listRuong.itemOptions) {
                            switch (io.optionTemplate.id) {
                                case 0: //Tấn công +#
                                    this.player.nPoint.dameAdd += io.param;
                                    break;
                                case 2: //HP, KI+#000
                                    this.player.nPoint.hpAdd += io.param * 1000;
                                    this.player.nPoint.mpAdd += io.param * 1000;
                                    break;
                                case 3: // vô hiệu vả biến st chưởng thành ki
                                    this.player.nPoint.mstChuong += io.param;
                                    break;
                                case 5: //+#% sức đánh chí mạng
                                    this.player.nPoint.tlDameCrit.add(io.param);
                                    break;
                                case 6: //HP+#
                                    this.player.nPoint.hpAdd += io.param;
                                    break;
                                case 7: //KI+#
                                    this.player.nPoint.mpAdd += io.param;
                                    break;
                                case 8: //Hút #% HP, KI xung quanh mỗi 5 giây
                                    this.player.nPoint.tlHutHpMpXQ += io.param;
                                    break;
                                case 14: //Chí mạng+#%
                                    this.player.nPoint.critAdd += io.param;
                                    break;
                                case 18: // #% chính xác
                                    this.player.nPoint.tlchinhxac += io.param;
                                    break;
                                case 19: //Tấn công+#% khi đánh quái
                                    this.player.nPoint.tlDameAttMob.add(io.param);
                                    break;
                                case 22: //HP+#K
                                    this.player.nPoint.hpAdd += io.param * 1000;
                                    break;
                                case 23: //MP+#K
                                    this.player.nPoint.mpAdd += io.param * 1000;
                                    break;
                                case 24:
                                    this.player.nPoint.wearingBuiBui = true;
                                    break;
                                case 25:
                                    this.player.nPoint.wearingYacon = true;
                                    break;
                                case 26:
                                    this.player.nPoint.wearingDrabula = true;
                                    this.player.effectSkin.lastTimeDrabula = System.currentTimeMillis();
                                    break;
                                case 29:
                                    this.player.nPoint.wearingMabu = true;
                                    break;
                                case 27: //+# HP/30s
                                    this.player.nPoint.hpHoiAdd += io.param;
                                    break;
                                case 28: //+# KI/30s
                                    this.player.nPoint.mpHoiAdd += io.param;
                                    break;
                                case 33: //dịch chuyển tức thời
                                    this.player.nPoint.teleport = true;
                                    break;
                                case 47: //Giáp+#
                                    this.player.nPoint.defAdd += io.param;
                                    break;
                                case 48: //HP/KI+#
                                    this.player.nPoint.hpAdd += io.param;
                                    this.player.nPoint.mpAdd += io.param;
                                    break;
                                case 49: //Tấn công+#%
                                case 50: //Sức đánh+#%
                                    this.player.nPoint.tlDame.add(io.param);
                                    break;
                                case 77: //HP+#%
                                    this.player.nPoint.tlHp.add(io.param);
                                    break;
                                case 80: //HP+#%/30s
                                    this.player.nPoint.tlHpHoi += io.param;
                                    break;
                                case 81: //MP+#%/30s
                                    this.player.nPoint.tlMpHoi += io.param;
                                    break;
                                case 88: //Cộng #% exp khi đánh quái
                                    this.player.nPoint.tlTNSM.add(io.param);
                                    break;
                                case 94: //Giáp #%
                                    this.player.nPoint.tlDef.add(io.param);
                                    break;
                                case 95: //Biến #% tấn công thành HP
                                    this.player.nPoint.tlHutHp += io.param;
                                    break;
                                case 96: //Biến #% tấn công thành MP
                                    this.player.nPoint.tlHutMp += io.param;
                                    break;
                                case 97: //Phản #% sát thương
                                    this.player.nPoint.tlPST += io.param;
                                    break;
                                case 98: //xuyên Giáp #%
                                    this.player.nPoint.tlxuyenDef.add(io.param);
                                    break;
                                case 100: //+#% vàng từ quái
                                    this.player.nPoint.tlGold += io.param;
                                    break;
                                case 101: //+#% TN,SM
                                    this.player.nPoint.tlTNSM.add(io.param);
                                    break;
                                case 103: //KI +#%
                                    this.player.nPoint.tlMp.add(io.param);
                                    break;
                                case 104: //Biến #% tấn công quái thành HP
                                    this.player.nPoint.tlHutHpMob += io.param;
                                    break;
                                case 105: //Vô hình khi không đánh quái và boss
                                    this.player.nPoint.wearingVoHinh = true;
                                    break;
                                case 106: //Không ảnh hưởng bởi cái lạnh
                                    this.player.nPoint.isKhongLanh = true;
                                    break;
                                case 108: //#% Né đòn
                                    this.player.nPoint.tlNeDon += io.param;
                                    break;
                                case 109: //Hôi, giảm #% HP
                                    this.player.nPoint.tlHpGiamODo += io.param;
                                    break;
                                case 114:
                                    this.player.nPoint.tlSpeed.add(io.param);
                                    break;
                                case 117: //Đẹp +#% SĐ cho mình và người xung quanh
                                    this.player.nPoint.tlSDDep.add(io.param);
                                    break;
                                case 147: //+#% sức đánh
                                    this.player.nPoint.tlDame.add(io.param);
                                    break;
                                case 155: //Giảm 50% sức đánh, HP, KI và +#% SM, TN, vàng từ quái
                                    this.player.nPoint.tlSubSD += 50;
                                    this.player.nPoint.tlTNSM.add(io.param);
                                    this.player.nPoint.tlGold += io.param;
                                    break;
                                case 160:
                                    this.player.nPoint.tlTNSMPet += io.param;
                                    break;
                                case 162: //Cute hồi #% KI/s bản thân và xung quanh
                                    this.player.nPoint.mpHoiCute += io.param;
                                    break;
                                case 173: //Phục hồi #% HP và KI cho đồng đội
                                    this.player.nPoint.tlHpHoiBanThanVaDongDoi += io.param;
                                    this.player.nPoint.tlMpHoiBanThanVaDongDoi += io.param;
                                    break;
                                case 189:
                                    this.player.nPoint.wearingNezuko = true;
                                    break;
                                case 190:
                                    this.player.nPoint.wearingTanjiro = true;
                                    break;
                                case 191:
                                    this.player.nPoint.wearingInoHashi = true;
                                    break;
                                case 192:
                                    this.player.nPoint.wearingInosuke = true;
                                    break;
                                case 193:
                                    this.player.nPoint.wearingZenitsu = true;
                                    break;
                                case 194:
                                    this.player.nPoint.tlDameChuong = 3;
                                    break;
                                case 195:
                                    this.player.nPoint.tlDameChuong = 4;
                                    break;
                            }
                        }
                    }
                }
            }
        }
    }

    /*-------------------------------------------------------------------------*/
    /**
     * Tính toán mọi chỉ số sau khi có thay đổi
     */
    public void calPoint() {
        try {
            if (this.player.pet != null) {
                this.player.pet.nPoint.setPointWhenWearClothes();
            }
            this.setPointWhenWearClothes();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setPoint(ItemOption io) {
        switch (io.optionTemplate.id) {
            case 0: //Tấn công +#
                this.dameAdd += io.param;
                break;
            case 2: //HP, KI+#000
                this.hpAdd += io.param * 1000;
                this.mpAdd += io.param * 1000;
                break;
            case 3: // vô hiệu vả biến st chưởng thành ki
                this.mstChuong += io.param;
                break;
            case 5: //+#% sức đánh chí mạng
                this.tlDameCrit.add(io.param);
                break;
            case 6: //HP+#
                this.hpAdd += io.param;
                break;
            case 7: //KI+#
                this.mpAdd += io.param;
                break;
            case 8: //Hút #% HP, KI xung quanh mỗi 5 giây
                this.tlHutHpMpXQ += io.param;
                break;
            case 14: //Chí mạng+#%
                this.critAdd += io.param;
                break;
            case 19: //Tấn công+#% khi đánh quái
                this.tlDameAttMob.add(io.param);
                break;
            case 22: //HP+#K
                this.hpAdd += io.param * 1000;
                break;
            case 23: //MP+#K
                this.mpAdd += io.param * 1000;
                break;
            case 24:
                this.wearingBuiBui = true;
                break;
            case 25:
                this.wearingYacon = true;
                break;
            case 26:
                this.wearingDrabula = true;
                this.player.effectSkin.lastTimeDrabula = System.currentTimeMillis();
                break;
            case 29:
                this.wearingMabu = true;
                break;
            case 27: //+# HP/30s
                this.hpHoiAdd += io.param;
                break;
            case 28: //+# KI/30s
                this.mpHoiAdd += io.param;
                break;
            case 33: //dịch chuyển tức thời
                this.teleport = true;
                break;
            case 47: //Giáp+#
                this.defAdd += io.param;
                break;
            case 48: //HP/KI+#
                this.hpAdd += io.param;
                this.mpAdd += io.param;
                break;
            case 49: //Tấn công+#%
            case 50: //Sức đánh+#%
                this.tlDame.add(io.param);
                break;
            case 77: //HP+#%
                this.tlHp.add(io.param);
                break;
            case 80: //HP+#%/30s
                this.tlHpHoi += io.param;
                break;
            case 81: //MP+#%/30s
                this.tlMpHoi += io.param;
                break;
            case 88: //Cộng #% exp khi đánh quái
                this.tlTNSM.add(io.param);
                break;
            case 94: //Giáp #%
                this.tlDef.add(io.param);
                break;
            case 95: //Biến #% tấn công thành HP
                this.tlHutHp += io.param;
                break;
            case 96: //Biến #% tấn công thành MP
                this.tlHutMp += io.param;
                break;
            case 97: //Phản #% sát thương
                this.tlPST += io.param;
                break;
            case 98: //Xuyên Giáp #%
                this.tlxuyenDef.add(io.param);
                break;
            case 100: //+#% vàng từ quái
                this.tlGold += io.param;
                break;
            case 101: //+#% TN,SM
                this.tlTNSM.add(io.param);
                break;
            case 103: //KI +#%
                this.tlMp.add(io.param);
                break;
            case 104: //Biến #% tấn công quái thành HP
                this.tlHutHpMob += io.param;
                break;
            case 105: //Vô hình khi không đánh quái và boss
                this.wearingVoHinh = true;
                break;
            case 106: //Không ảnh hưởng bởi cái lạnh
                this.isKhongLanh = true;
                break;
            case 108: //#% Né đòn
                this.tlNeDon += io.param;
                break;
            case 18: // #% chính xác
                this.tlchinhxac += io.param;
                break;
            case 109: //Hôi, giảm #% HP
                this.tlHpGiamODo += io.param;
                break;
            case 114:
                this.tlSpeed.add(io.param);
                break;
            case 117: //Đẹp +#% SĐ cho mình và người xung quanh
                this.tlSDDep.add(io.param);
                break;
            case 147: //+#% sức đánh
                this.tlDame.add(io.param);
                break;
            case 156: //Giảm 50% sức đánh, HP, KI và +#% SM, TN, vàng từ quái
                this.tlSubSD += 50;
                this.tlTNSM.add(io.param);
                this.tlGold += io.param;
                break;
            case 160:
                this.tlTNSMPet += io.param;
                break;
            case 162: //Cute hồi #% KI/s bản thân và xung quanh
                this.mpHoiCute += io.param;
                break;
            case 173: //Phục hồi #% HP và KI cho đồng đội
                this.tlHpHoiBanThanVaDongDoi += io.param;
                this.tlMpHoiBanThanVaDongDoi += io.param;
                break;
            case 189:
                this.wearingNezuko = true;
                break;
            case 190:
                this.wearingTanjiro = true;
                break;
            case 191:
                this.wearingInoHashi = true;
                break;
            case 192:
                this.wearingInosuke = true;
                break;
            case 193:
                this.wearingZenitsu = true;
                break;
            case 194:
                this.tlDameChuong = 3;
                break;
            case 195:
                this.tlDameChuong = 4;
                break;
            case 210:
                this.TangLienHoan = true;
                break;
            case 211:
                this.TangGalick = true;
                break;
            case 212:
                this.TangKaioken = true;
                break;
            case 213:
                this.TangKame = true;
                break;
            case 229: //HP pháp sư +#%
                this.tlHp.add(io.param);
                break;
            case 230: //KI pháp sư +#%
                this.tlMp.add(io.param);
                break;
            case 231: //Sức đánh pháp sư +#%
                this.tlDame.add(io.param);
                break;
            case 232: //Sức đánh chí mạng pháp sư +#%
                this.tlDameCrit.add(io.param);
                break;
            case 240: //HP pháp sư +#%
                this.TichLuy.add(io.param);
                break;
        }
    }

    protected void setPointWhenWearClothes() {
        resetPoint();
        for (Item item : this.player.inventory.itemsBody) {
            if (item.isNotNullItem()) {
                int tempID = item.template.id;
                if (tempID >= 592 && tempID <= 594) {
                    teleport = true;
                }
                for (ItemOption io : item.itemOptions) {
                    setPoint(io);
                }
            }
        }
        List<Item> itemsBody = player.inventory.itemsBody;
        if (this.player.isPl()) {
            if (player.inventory.itemsBody.get(1).isNotNullItem()) {
                Item pants = itemsBody.get(1);
                if (pants.isNotNullItem() && pants.getId() >= 691 && pants.getId() <= 693) {
                    player.event.setUseQuanHoa(true);
                }
            }
            if (player.inventory.itemsBody.get(5).isNotNullItem()) {
                Item pants = itemsBody.get(5);
                if (pants.isNotNullItem() && pants.getId() == 451) {
                    player.event.setUseCaiTrangQuy(true);
                }
            }
        }
        if (Manager.EVENT_SEVER == 3) {
            if (!this.player.isBoss && !this.player.isMiniPet) {
                if (itemsBody.get(5).isNotNullItem()) {
                    int tempID = itemsBody.get(5).getId();
                    switch (tempID) {
                        case 386:
                        case 389:
                        case 392:
                            wearingGrayNoelHat = true;
                            wearingNoelHat = true;
                            break;
                        case 387:
                        case 390:
                        case 393:
                            wearingRedNoelHat = true;
                            wearingNoelHat = true;
                            break;
                        case 388:
                        case 391:
                        case 394:
                            wearingBlueNoelHat = true;
                            wearingNoelHat = true;
                            break;
                        default:
                            wearingRedNoelHat = false;
                            wearingBlueNoelHat = false;
                            wearingGrayNoelHat = false;
                            wearingNoelHat = false;
                    }
                }
            }
        }
        CollectionBook book = player.getCollectionBook();

        if (book != null) {
            List<Card> cards = book.getCards();
            if (cards != null) {
                for (Card c : cards) {
                    if (c.getLevel() > 0) {
                        int index = 0;
                        for (ItemOption o : c.getCardTemplate().getOptions()) {
                            if ((index == 0 || c.isUse()) && c.getLevel() >= o.activeCard) {
                                setPoint(o);
                            }
                            index++;
                        }
                    }
                }
            }
        }
        setChisoPorata();
        setDameTrainArmor();
        setPointKhamNgoc();
        setPointRuongSuuTam();
        setBasePoint();
    }

    private void setChisoPorata() {
        if (this.player.isPl() && this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2) {
            for (Item item : this.player.inventory.itemsBag) {
                if (item.isNotNullItem() && item.template.id == 921) {
                    for (ItemOption io : item.itemOptions) {
                        switch (io.optionTemplate.id) {
                            case 14: //Chí mạng+#%
                                this.critAdd += io.param;
                                break;
                            case 50: //Sức đánh+#%
                                this.tlDame.add(io.param);
                                break;
                            case 77: //HP+#%
                                this.tlHp.add(io.param);
                                break;
                            case 80: //HP+#%/30s
                                this.tlHpHoi += io.param;
                                break;
                            case 81: //MP+#%/30s
                                this.tlMpHoi += io.param;
                                break;
                            case 94: //Giáp #%
                                this.tlDef.add(io.param);
                                break;
                            case 98: //Xuyên Giáp #%
                                this.tlxuyenDef.add(io.param);
                                break;
                            case 103: //KI +#%
                                this.tlMp.add(io.param);
                                break;
                            case 108: //#% Né đòn
                                this.tlNeDon += io.param;
                                break;
                            case 18: // #% chính xác
                                this.tlchinhxac += io.param;
                                break;
                        }
                    }
                    break;
                }
            }
        }
        if (this.player.isPl() && this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3) {
            for (Item item : this.player.inventory.itemsBag) {
                if (item.isNotNullItem() && item.template.id == 1165) {
                    for (ItemOption io : item.itemOptions) {
                        switch (io.optionTemplate.id) {
                            case 14: //Chí mạng+#%
                                this.critAdd += io.param;
                                break;
                            case 50: //Sức đánh+#%
                                this.tlDame.add(io.param);
                                break;
                            case 77: //HP+#%
                                this.tlHp.add(io.param);
                                break;
                            case 80: //HP+#%/30s
                                this.tlHpHoi += io.param;
                                break;
                            case 81: //MP+#%/30s
                                this.tlMpHoi += io.param;
                                break;
                            case 94: //Giáp #%
                                this.tlDef.add(io.param);
                                break;
                            case 98: //Xuyên Giáp #%
                                this.tlxuyenDef.add(io.param);
                                break;
                            case 103: //KI +#%
                                this.tlMp.add(io.param);
                                break;
                            case 108: //#% Né đòn
                                this.tlNeDon += io.param;
                                break;
                            case 18: // #% chính xác
                                this.tlchinhxac += io.param;
                                break;
                        }
                    }
                    break;
                }
            }
        }
        if (this.player.isPl() && this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4) {
            for (Item item : this.player.inventory.itemsBag) {
                if (item.isNotNullItem() && item.template.id == 1129) {
                    for (ItemOption io : item.itemOptions) {
                        switch (io.optionTemplate.id) {
                            case 14: //Chí mạng+#%
                                this.critAdd += io.param;
                                break;
                            case 50: //Sức đánh+#%
                                this.tlDame.add(io.param);
                                break;
                            case 77: //HP+#%
                                this.tlHp.add(io.param);
                                break;
                            case 80: //HP+#%/30s
                                this.tlHpHoi += io.param;
                                break;
                            case 81: //MP+#%/30s
                                this.tlMpHoi += io.param;
                                break;
                            case 94: //Giáp #%
                                this.tlDef.add(io.param);
                                break;
                            case 98: //Xuyên Giáp #%
                                this.tlxuyenDef.add(io.param);
                                break;
                            case 103: //KI +#%
                                this.tlMp.add(io.param);
                                break;
                            case 108: //#% Né đòn
                                this.tlNeDon += io.param;
                                break;
                            case 18: // #% chính xác
                                this.tlchinhxac += io.param;
                                break;
                        }
                    }
                    break;
                }
            }
        }
        if (this.player.isPl() && this.player.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5) {
            for (Item item : this.player.inventory.itemsBag) {
                if (item.isNotNullItem() && (
                        item.template.id == 1550
                        || item.template.id == 1999
                        || item.template.id == 1944
                        || item.template.id == 1945
                        || item.template.id == 1946)) {
                    for (ItemOption io : item.itemOptions) {
                        switch (io.optionTemplate.id) {
                            case 14: //Chí mạng+#%
                                this.critAdd += io.param;
                                break;
                            case 50: //Sức đánh+#%
                                this.tlDame.add(io.param);
                                break;
                            case 77: //HP+#%
                                this.tlHp.add(io.param);
                                break;
                            case 80: //HP+#%/30s
                                this.tlHpHoi += io.param;
                                break;
                            case 81: //MP+#%/30s
                                this.tlMpHoi += io.param;
                                break;
                            case 94: //Giáp #%
                                this.tlDef.add(io.param);
                                break;
                            case 98: //Xuyên Giáp #%
                                this.tlxuyenDef.add(io.param);
                                break;
                            case 103: //KI +#%
                                this.tlMp.add(io.param);
                                break;
                            case 108: //#% Né đòn
                                this.tlNeDon += io.param;
                                break;
                            case 18: // #% chính xác
                                this.tlchinhxac += io.param;
                                break;
                        }
                    }
                    break;
                }
            }
        }
    }

    private void setDameTrainArmor() {
//        if (!this.player.isPet && !this.player.isBoss && !this.player.isMiniPet) {
//            try {
//                Item gtl = this.player.inventory.itemsBody.get(6);
//                if (gtl.isNotNullItem()) {
//                    this.wearingTrainArmor = true;
//                    this.wornTrainArmor = true;
//                    this.player.inventory.trainArmor = gtl;
//                    this.tlSubSD += ItemService.gI().getPercentTrainArmor(gtl);
//                } else {
//                    if (this.wornTrainArmor) {
//                        this.wearingTrainArmor = false;
//                        for (ItemOption io : this.player.inventory.trainArmor.itemOptions) {
//                            if (io.optionTemplate.id == 9 && io.param > 0) {
//                                this.tlDame.add(ItemService.gI().getPercentTrainArmor(this.player.inventory.trainArmor));
//                                break;
//                            }
//                        }
//                    }
//                }
//            } catch (Exception e) {
//                Log.error("Lỗi get giáp tập luyện " + this.player.name);
//            }
//        }
    }

    private void setNeDon() {
        //ngọc rồng đen 6 sao
        if (this.player.rewardBlackBall.timeOutOfDateReward[5] > System.currentTimeMillis()) {
            this.tlHutMp += RewardBlackBall.R6S;
        }
    }

    private void setHpHoi() {
        this.hpHoi = calPercent(this.hpMax, 1);
        this.hpHoi += this.hpHoiAdd;
        this.hpHoi += calPercent(this.hpMax, this.tlHpHoi);
        this.hpHoi += calPercent(this.hpMax, this.tlHpHoiBanThanVaDongDoi);
        if (this.player.effectSkin.isNezuko) {
            this.hpHoi += calPercent(this.hpMax, 3);
        }
    }

    private void setMpHoi() {
        this.mpHoi = calPercent(this.mpMax, 1);
        this.mpHoi += this.mpHoiAdd;
        this.mpHoi += calPercent(this.mpMax, this.tlMpHoi);
        this.mpHoi += calPercent(this.mpMax, this.tlMpHoiBanThanVaDongDoi);
        if (this.player.effectSkin.isNezuko) {
            this.mpHoi += calPercent(this.mpMax, 3);
        }
    }

    protected void setHpMax() {
        this.hpMax = this.hpg;
        this.hpMax += this.hpAdd;
        //đồ
        for (Integer tl : this.tlHp) {
            this.hpMax += calPercent(this.hpMax, tl);
        }
        //Phân thân
        if (this.player.isPhanThan) {
            this.hpMax = calPercent(((PlayerPhanThan) this.player).master.nPoint.hpMax, SkillUtil.getPercentPhanThan(((PlayerPhanThan) this.player).master.effectSkill.levelPhanThan));
            return;
        }
        //set nhật ấn hp
        if (this.player.setClothes.nhatan == 5) {
            if (this.player.setClothes.setDTS == 5) {
                this.hpMax += calPercent(this.hpMax, 20);
            } else if (this.player.setClothes.setDTT == 5) {
                this.hpMax += calPercent(this.hpMax, 50);
            }
        }
        //set nappa
        if (this.player.setClothes.nappa == 5) {
            this.hpMax += calPercent(this.hpMax, 100);
        }
        if (this.player.setClothes.thienhoangHP_1 == 1 && this.player.setClothes.nappa == 4
                || this.player.setClothes.thienhoangHP_1 == 2 && this.player.setClothes.nappa == 3
                || this.player.setClothes.thienhoangHP_1 == 3 && this.player.setClothes.nappa == 2
                || this.player.setClothes.thienhoangHP_1 == 4 && this.player.setClothes.nappa == 1) {
            this.hpMax += calPercent(this.hpMax, 100);
        }
        if (this.player.setClothes.thienhoangHP_1 == 5) {
            this.hpMax += calPercent(this.hpMax, 200);
        }
        if (this.player.setClothes.thienhoangdbHP == 5) {
            this.hpMax += calPercent(this.hpMax, 700);
        }

        //ngọc rồng đen 2 sao
        if (this.player.rewardBlackBall.timeOutOfDateReward[1] > System.currentTimeMillis()) {
            this.hpMax += calPercent(this.hpMax, RewardBlackBall.R2S);
        }
        //khỉ
        if (this.player.effectSkill.isMonkey) {
            if (!this.player.isPet || (this.player.isPet
                    && ((Pet) this.player).status != Pet.FUSION)) {
                int percent = SkillUtil.getPercentHpMonkey(player.effectSkill.levelMonkey);
                this.hpMax += calPercent(this.hpMax, percent);
            }
        }
        //chỉ số pet khi hợp thể
        if (this.player.isPet && isFusion()) {
            if (((Pet) this.player).typePet == 1) {//mabu
                this.hpMax += calPercent(this.hpMax, Final_Static.hpPettype1);
            } else if (((Pet) this.player).typePet == 2) {// Bư Teen 30%
                this.hpMax += calPercent(this.hpMax, Final_Static.hpPettype2);
            } else if (((Pet) this.player).typePet == 3) {// Thần Hủy Diệt 60%
                this.hpMax += calPercent(this.hpMax, Final_Static.hpPettype3);
            } else if (((Pet) this.player).typePet == 4) {// Thần Zeno 90%
                this.hpMax += calPercent(this.hpMax, Final_Static.hpPettype4);
            } else if (((Pet) this.player).typePet == 5) {// Siêu Thần 170%
                this.hpMax += calPercent(this.hpMax, Final_Static.hpPettype5);
            } else if (((Pet) this.player).typePet == 6) {
                this.hpMax += calPercent(this.hpMax, Final_Static.hpPettype6);
            } else if (((Pet) this.player).typePet == 7) {
                this.hpMax += calPercent(this.hpMax, Final_Static.hpPettype7);
            } else if (((Pet) this.player).typePet == 8) {
                this.hpMax += calPercent(this.hpMax, Final_Static.hpPettype8);
            } else if (((Pet) this.player).typePet == 9) {
                this.hpMax += calPercent(this.hpMax, Final_Static.hpPettype9);
            } else if (((Pet) this.player).typePet == 10) {
                this.hpMax += calPercent(this.hpMax, Final_Static.hpPettype10);
            } else if (((Pet) this.player).typePet == 11) {
                this.hpMax += calPercent(this.hpMax, Final_Static.hpPettype11);
            }
        }
        if (this.player.isPet && ((Pet) this.player).master.PorataVIP == true && isFusion()) {
            this.hpMax += calPercent(this.hpMax, 1);
        }
        if (this.player.isPet && ((Pet) this.player).master.PorataVIP_2 == true && isFusion()) {//porata vip 2
            this.hpMax += calPercent(this.hpMax, 1);
        }
        if (this.player.isPet && ((Pet) this.player).master.PorataVIP_3 == true && isFusion()) {//porata vip 2
            this.hpMax += calPercent(this.hpMax, 100);
        }
        if (this.player.isPet && ((Pet) this.player).master.PorataVIP_4 == true && isFusion()) {//porata vip 2
            this.hpMax += calPercent(this.hpMax, 1);
        }
        if (this.player.isPet && ((Pet) this.player).master.PorataVIP_5 == true && isFusion()) {//porata vip 2
            this.hpMax += calPercent(this.hpMax, 1);
        }
        //phù
        if (this.player.zone != null && MapService.gI().isMapBlackBallWar(this.player.zone.map.mapId)) {
            this.hpMax *= this.player.effectSkin.xHPKI;
        }
        //phù mabu 14h
        if (this.player.zone != null && MapService.gI().isMapMabuWar14H(this.player.zone.map.mapId)) {
            this.hpMax += 1000000;
        }
        //+hp đệ
        if (this.player.fusion.typeFusion != ConstPlayer.NON_FUSION) {
            this.hpMax += this.player.pet.nPoint.hpMax;
        }
        //huýt sáo
        if (!this.player.isPet
                || (this.player.isPet
                && ((Pet) this.player).status != Pet.FUSION)) {
            if (this.player.effectSkill.tiLeHPHuytSao != 0) {
                this.hpMax += calPercent(this.hpMax, this.player.effectSkill.tiLeHPHuytSao);
            }
        }
        //bổ huyết
        if (this.player.itemTime != null && this.player.itemTime.isUseBoHuyet) {
            this.hpMax *= 2;
        }
        //bổ huyết 2
        if (this.player.itemTime != null && this.player.itemTime.isUseBoHuyet2) {
            this.hpMax += calPercent(hpMax, 120);
        }
        if (this.player.zone != null && MapService.gI().isMapCold(this.player.zone.map)
                && !this.isKhongLanh) {
            this.hpMax /= 2;
        }
        if (!player.isBoss) {
            Attribute at = ServerManager.gI().getAttributeManager().find(ConstAttribute.HP);
            if (at != null && !at.isExpired()) {
                hpMax += calPercent(hpMax, at.getValue());
            }
        }
        if (this.player.itemTime != null) {
            if (this.player.itemTime.isUseBanhTet) {
                hpMax += calPercent(hpMax, 20);
            }
        }
        if (player.getBuff() == Buff.BUFF_HP) {
            if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isRongVutruhp) {
                hpMax += calPercent(hpMax, 50);
            }
        }
        //đuôi khỉ
        if (!this.player.isPet && this.player.itemTimesieucap.isDuoikhi
                || this.player.isPet && ((Pet) this.player).master.itemTimesieucap.isDuoikhi) {
            this.hpMax += calPercent(hpMax, 5);
        }
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isDaNgucTu) {
            this.hpMax += calPercent(hpMax, 10);
        }
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isCuongBao) {
            this.hpMax += calPercent(hpMax, 20);
        }
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseXiMuoi) {
            this.hpMax += calPercent(hpMax, 50);
        }
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseTrungThu && this.player.itemTimesieucap.iconBanh == 4042) {
            this.hpMax += calPercent(hpMax, 10);
        }
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseTrungThu && this.player.itemTimesieucap.iconBanh == 4043) {
            this.hpMax += calPercent(hpMax, 20);
        }
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseTrungThu && this.player.itemTimesieucap.iconBanh == 4125) {
            this.hpMax += calPercent(hpMax, 30);
        }
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseTrungThu && this.player.itemTimesieucap.iconBanh == 4126) {
            this.hpMax += calPercent(hpMax, 40);
        }
        if (this.player.chuyensinhsuphu > 0) {
            this.hpMax += calPercent(hpMax, this.player.chuyensinhsuphu);
        }
        if (this.player.isPl() && (this.player.isTitleUse10 == true && this.player.DH_VV_10)) {
            this.hpMax += calPercent(hpMax, 200);
        }
        if (this.player.isPl() && (this.player.isTitleUse9 == true && this.player.DH_VV_9)) {
            this.hpMax += calPercent(hpMax, 200);
        }
        if (this.player.isPl() && (this.player.isTitleUse8 == true && this.player.DH_VV_8)) {
            this.hpMax += calPercent(hpMax, 200);
        }
        if (this.player.isPl() && (this.player.isTitleUse7 == true && this.player.DH_VV_7)) {
            this.hpMax += calPercent(hpMax, 100);
        }
        if (this.player.isPl() && (this.player.isTitleUse6 == true && this.player.DH_VV_6)) {
            this.hpMax += calPercent(hpMax, 50);
        }
        if (this.player.isPl() && (this.player.isTitleUse5 == true && this.player.lastTimeTitle5 > 0 || (this.player.isTitleUse5 == true && this.player.DH_VV_5))) {
            this.hpMax += calPercent(hpMax, 100);
        }
        if (this.player.isPl() && (this.player.isTitleUse4 == true && this.player.lastTimeTitle4 > 0 || (this.player.isTitleUse4 == true && this.player.DH_VV_4))) {
            this.hpMax += calPercent(hpMax, 100);
        }
        if (this.player.isPl() && (this.player.isTitleUse3 == true && this.player.lastTimeTitle3 > 0 || (this.player.isTitleUse3 == true && this.player.DH_VV_3))) {
            this.hpMax += calPercent(hpMax, 30);
        }
        if (this.player.isPl() && (this.player.isTitleUse2 == true && this.player.lastTimeTitle2 > 0 || (this.player.isTitleUse2 == true && this.player.DH_VV_2))) {
            this.hpMax += calPercent(hpMax, 50);
        }
        if (this.player.isPl() && (this.player.isTitleUse1 == true && this.player.lastTimeTitle1 > 0 || (this.player.isTitleUse1 == true && this.player.DH_VV_1))) {
            this.hpMax += calPercent(hpMax, 50);
        }
        //rồng băng
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isRongBang) {
            this.hpMax += calPercent(hpMax, 30);
        }
        //rồng siêu cấp
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isRongSieuCap) {
            this.hpMax += calPercent(hpMax, 50);
        }
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isRongHoangKim) {
            this.hpMax += calPercent(hpMax, 200);
        }
        //skill biến hình
        if (this.player.effectSkill.isBienHinh) {
            this.hpMax += calPercent(hpMax, EffectSkillService.gI().PointBienHinh(this.player));
        }
        //cà rốt
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseCaRot) {
            this.hpMax += calPercent(hpMax, 50);
        }
        //rồng siêu cấp
        if (this.player.isPet && ((Pet) this.player).master.itemTimesieucap.isDeTuSC) {
            this.hpMax += calPercent(this.hpMax, 100);
        }
        if (!this.player.isPet && this.player.itemTimesieucap.isSuPhuSC) {
            this.hpMax += calPercent(this.hpMax, 100);
        }
        if (this.player.isPl() && this.player.isTuTien){
            this.hpMax += calPercent(this.hpMax, (this.player.bonusTuTien + Service.getInstance().nPointTuTien(this.player.levelTuTien, this.player.tangTuTien)));
        }
    }

    // (hp sư phụ + hp đệ tử ) + 15%
    // (hp sư phụ + 15% +hp đệ tử)
    private void setHp() {
        if (this.hp > this.hpMax) {
            this.hp = this.hpMax;
        }
    }

    protected void setMpMax() {
        this.mpMax = this.mpg;
        this.mpMax += this.mpAdd;
        //đồ
        for (Integer tl : this.tlMp) {
            this.mpMax += calPercent(this.mpMax, tl);
        }
        if (this.player.isPhanThan) {
            this.mpMax = calPercent(((PlayerPhanThan) this.player).master.nPoint.mpMax, SkillUtil.getPercentPhanThan(((PlayerPhanThan) this.player).master.effectSkill.levelPhanThan));
            return;
        }
        //set tinh ấn tăng ki
        if (this.player.setClothes.tinhan == 5) {
            if (this.player.setClothes.setDTS == 5) {
                this.mpMax += calPercent(this.mpMax, 20);
            } else if (this.player.setClothes.setDTT == 5) {
                this.mpMax += calPercent(this.mpMax, 50);
            }
        }
        if (this.player.setClothes.picolo == 5) {
            this.mpMax *= 2;
        }
        if ((this.player.setClothes.mahoangKI_1 == 1 && this.player.setClothes.picolo == 4)
        || (this.player.setClothes.mahoangKI_1 == 2 && this.player.setClothes.picolo == 3)
        || (this.player.setClothes.mahoangKI_1 == 3 && this.player.setClothes.picolo == 2)
        || (this.player.setClothes.mahoangKI_1 == 4 && this.player.setClothes.picolo == 1)) {
    this.mpMax *= 2;
}

        if (this.player.setClothes.mahoangKI_1 == 5) {
            this.mpMax *= 3;
        }

        //ngọc rồng đen 3 sao
        if (this.player.rewardBlackBall.timeOutOfDateReward[2] > System.currentTimeMillis()) {
            this.mpMax += calPercent(this.mpMax, RewardBlackBall.R3S);
        }
        //chỉ số pet khi hợp thể
        if (this.player.isPet && (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5)) {
            if (((Pet) this.player).typePet == 1) {//mabu
                this.mpMax += calPercent(this.mpMax, Final_Static.mpPettype1);
            } else if (((Pet) this.player).typePet == 2) {//bư nhí
                this.mpMax += calPercent(this.mpMax, Final_Static.mpPettype2);
            } else if (((Pet) this.player).typePet == 3) {//kid bư
                this.mpMax += calPercent(this.mpMax, Final_Static.mpPettype3);
            } else if (((Pet) this.player).typePet == 4) {//berus
                this.mpMax += calPercent(this.mpMax, Final_Static.mpPettype4);
            } else if (((Pet) this.player).typePet == 5) {//zeno
                this.mpMax += calPercent(this.mpMax, Final_Static.mpPettype5);
            } else if (((Pet) this.player).typePet == 6) {//goku
                this.mpMax += calPercent(this.mpMax, Final_Static.mpPettype6);
            } else if (((Pet) this.player).typePet == 7) {//sontinh
                this.mpMax += calPercent(this.mpMax, Final_Static.mpPettype7);
            } else if (((Pet) this.player).typePet == 8) {//thuytinh
                this.mpMax += calPercent(this.mpMax, Final_Static.mpPettype8);
            } else if (((Pet) this.player).typePet == 9) {//bunhi
                this.mpMax += calPercent(this.mpMax, Final_Static.mpPettype9);
            } else if (((Pet) this.player).typePet == 10) {//sennhi
                this.mpMax += calPercent(this.mpMax, Final_Static.mpPettype10);
            } else if (((Pet) this.player).typePet == 11) {//fidenhi
                this.mpMax += calPercent(this.mpMax, Final_Static.mpPettype11);
            }
        }
        if (this.player.isPet && ((Pet) this.player).master.PorataVIP == true && isFusion()) {
            this.mpMax += calPercent(this.mpMax, 1);
        }
        if (this.player.isPet && ((Pet) this.player).master.PorataVIP_2 == true && isFusion()) {//porata vip 2
            this.mpMax += calPercent(this.mpMax, 1);
        }
        if (this.player.isPet && ((Pet) this.player).master.PorataVIP_3 == true && isFusion()) {//porata vip 2
            this.mpMax += calPercent(this.mpMax, 100);
        }
        if (this.player.isPet && ((Pet) this.player).master.PorataVIP_4 == true && isFusion()) {//porata vip 2
            this.mpMax += calPercent(this.mpMax, 1);
        }
        if (this.player.isPet && ((Pet) this.player).master.PorataVIP_5 == true && isFusion()) {//porata vip 2
            this.mpMax += calPercent(this.mpMax, 1);
        }
        //hợp thể
        if (this.player.fusion.typeFusion != 0) {
            this.mpMax += this.player.pet.nPoint.mpMax;
        }
        //bổ khí
        if (this.player.itemTime != null && this.player.itemTime.isUseBoKhi) {
            this.mpMax *= 2;
        }
        //bổ khí 2
        if (this.player.itemTime != null && this.player.itemTime.isUseBoKhi2) {
            this.mpMax += calPercent(mpMax, 120);
        }
        //phù
        if (this.player.zone != null && MapService.gI().isMapBlackBallWar(this.player.zone.map.mapId)) {
            this.mpMax *= this.player.effectSkin.xHPKI;
        }
        //phù mabu 14h
        if (this.player.zone != null && MapService.gI().isMapMabuWar14H(this.player.zone.map.mapId)) {
            this.mpMax += 1000000;
        }
        //xiên cá
        if (this.player.effectFlagBag.useXienCa) {
            this.mpMax += calPercent(this.mpMax, 15);
        }
        //Kiem z
        if (this.player.effectFlagBag.useKiemz) {
            this.mpMax += calPercent(this.mpMax, 20);
        }
        if (this.player.effectFlagBag.useDieuRong) {
            this.mpMax += calPercent(this.mpMax, 30);
        }
        if (this.player.effectFlagBag.useHoaVang || this.player.effectFlagBag.useHoaHong) {
            this.mpMax += calPercent(this.mpMax, 20);
        }
        if (!player.isBoss) {
            Attribute at = ServerManager.gI().getAttributeManager().find(ConstAttribute.KI);
            if (at != null && !at.isExpired()) {
                mpMax += calPercent(mpMax, at.getValue());
            }
        }
        if (this.player.itemTime != null) {
            if (this.player.itemTime.isUseBanhTet) {
                mpMax += calPercent(mpMax, 20);
            }
        }
        if (player.getBuff() == Buff.BUFF_KI) {
            if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isRongVutruki) {
                mpMax += calPercent(mpMax, 50);
            }
        }
        //đuôi khỉ
        if (!this.player.isPet && this.player.itemTimesieucap.isDuoikhi
                || this.player.isPet && ((Pet) this.player).master.itemTimesieucap.isDuoikhi) {
            this.mpMax += calPercent(mpMax, 5);
        }
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isDaNgucTu) {
            this.mpMax += calPercent(mpMax, 10);
        }
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isCuongBao) {
            this.mpMax += calPercent(mpMax, 20);
        }
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseXiMuoi) {
            this.mpMax += calPercent(mpMax, 50);
        }
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseTrungThu && this.player.itemTimesieucap.iconBanh == 4042) {
            this.mpMax += calPercent(mpMax, 10);
        }
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseTrungThu && this.player.itemTimesieucap.iconBanh == 4043) {
            this.mpMax += calPercent(mpMax, 20);
        }
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseTrungThu && this.player.itemTimesieucap.iconBanh == 4125) {
            this.mpMax += calPercent(mpMax, 30);
        }
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseTrungThu && this.player.itemTimesieucap.iconBanh == 4126) {
            this.mpMax += calPercent(mpMax, 40);
        }
        if (this.player.chuyensinhsuphu > 0) {
            this.mpMax += calPercent(mpMax, this.player.chuyensinhsuphu);
        }
        if (this.player.isPl() && (this.player.isTitleUse10 == true && this.player.DH_VV_10)) {
            this.mpMax += calPercent(mpMax, 200);
        }
        if (this.player.isPl() && (this.player.isTitleUse9 == true && this.player.DH_VV_9)) {
            this.mpMax += calPercent(mpMax, 200);
        }
        if (this.player.isPl() && (this.player.isTitleUse8 == true && this.player.DH_VV_8)) {
            this.mpMax += calPercent(mpMax, 200);
        }
        if (this.player.isPl() && (this.player.isTitleUse7 == true && this.player.DH_VV_7)) {
            this.mpMax += calPercent(mpMax, 100);
        }
        if (this.player.isPl() && (this.player.isTitleUse6 == true && this.player.DH_VV_6)) {
            this.mpMax += calPercent(mpMax, 50);
        }
        if (this.player.isPl() && (this.player.isTitleUse5 == true && this.player.lastTimeTitle5 > 0 || (this.player.isTitleUse5 == true && this.player.DH_VV_5))) {
            this.mpMax += calPercent(mpMax, 100);
        }
        if (this.player.isPl() && (this.player.isTitleUse4 == true && this.player.lastTimeTitle4 > 0 || (this.player.isTitleUse4 == true && this.player.DH_VV_4))) {
            this.mpMax += calPercent(mpMax, 100);
        }
        if (this.player.isPl() && (this.player.isTitleUse3 == true && this.player.lastTimeTitle3 > 0 || (this.player.isTitleUse3 == true && this.player.DH_VV_3))) {
            this.mpMax += calPercent(mpMax, 30);
        }
        if (this.player.isPl() && (this.player.isTitleUse2 == true && this.player.lastTimeTitle2 > 0 || (this.player.isTitleUse2 == true && this.player.DH_VV_2))) {
            this.mpMax += calPercent(mpMax, 50);
        }
        if (this.player.isPl() && (this.player.isTitleUse1 == true && this.player.lastTimeTitle1 > 0 || (this.player.isTitleUse1 == true && this.player.DH_VV_1))) {
            this.mpMax += calPercent(mpMax, 50);
        }
        //rồng băng
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isRongBang) {
            this.mpMax += calPercent(mpMax, 30);
        }
        //rồng siêu cấp
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isRongSieuCap) {
            this.mpMax += calPercent(mpMax, 50);
        }
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isRongHoangKim) {
            this.mpMax += calPercent(mpMax, 200);
        }
        //skill biến hình
        if (this.player.effectSkill.isBienHinh) {
            this.mpMax += calPercent(mpMax, EffectSkillService.gI().PointBienHinh(this.player));
        }
        //cà rốt
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseCaRot) {
            this.mpMax += calPercent(mpMax, 50);
        }
        //rồng siêu cấp
        if (this.player.isPet && ((Pet) this.player).master.itemTimesieucap.isDeTuSC) {
            this.mpMax += calPercent(this.mpMax, 100);
        }
        if (!this.player.isPet && this.player.itemTimesieucap.isSuPhuSC) {
            this.mpMax += calPercent(this.mpMax, 100);
        }
        if (this.player.isPl() && this.player.isTuTien){
            this.mpMax += calPercent(this.mpMax, (this.player.bonusTuTien + Service.getInstance().nPointTuTien(this.player.levelTuTien, this.player.tangTuTien)));
        }
    }

    private void setMp() {
        if (this.mp > this.mpMax) {
            this.mp = this.mpMax;
        }
    }

    protected void setDame() {
        this.dame = this.dameg;
        this.dame += this.dameAdd;
        //đồ
        for (Integer tl : this.tlDame) {
            this.dame += calPercent(this.dame, tl);
        }
        if (this.player.isPhanThan) {
            this.dame = calPercent(((PlayerPhanThan) this.player).master.nPoint.dame, SkillUtil.getPercentPhanThan(((PlayerPhanThan) this.player).master.effectSkill.levelPhanThan));
            return;
        }
        for (Integer tl : this.tlSDDep) {
            this.dame += calPercent(this.dame, tl);
        }
        //set nguyệt ấn sđ
        if (this.player.setClothes.nguyetan == 5) {
            if (this.player.setClothes.setDTS == 5) {
                this.dame += calPercent(this.dame, 20);
            } else if (this.player.setClothes.setDTT == 5) {
                this.dame += calPercent(this.dame, 50);
            }
        }
        //chỉ số pet khi hợp thể
        if (this.player.isPet && isFusion()) {
            if (((Pet) this.player).typePet == 1) {//mabu
                this.dame += calPercent(this.dame, Final_Static.damePettype1);
            } else if (((Pet) this.player).typePet == 2) {//bư nhí
                this.dame += calPercent(this.dame, Final_Static.damePettype2);
            } else if (((Pet) this.player).typePet == 3) {//kid bư
                this.dame += calPercent(this.dame, Final_Static.damePettype3);
            } else if (((Pet) this.player).typePet == 4) {//berus
                this.dame += calPercent(this.dame, Final_Static.damePettype4);
            } else if (((Pet) this.player).typePet == 5) {//zeno
                this.dame += calPercent(this.dame, Final_Static.damePettype5);
            } else if (((Pet) this.player).typePet == 6) {//goku
                this.dame += calPercent(this.dame, Final_Static.damePettype6);
            } else if (((Pet) this.player).typePet == 7) {//sontinh
                this.dame += calPercent(this.dame, Final_Static.damePettype7);
            } else if (((Pet) this.player).typePet == 8) {//thuytinh
                this.dame += calPercent(this.dame, Final_Static.damePettype8);
            } else if (((Pet) this.player).typePet == 9) {//bunhi
                this.dame += calPercent(this.dame, Final_Static.damePettype9);
            } else if (((Pet) this.player).typePet == 10) {//sennhi
                this.dame += calPercent(this.dame, Final_Static.damePettype10);
            } else if (((Pet) this.player).typePet == 11) {//fidenhi
                this.dame += calPercent(this.dame, Final_Static.damePettype11);
            }
        }
        if (this.player.isPet && isFusion()) {
            this.dame += calPercent(this.dame, 1);
        }
        if (this.player.isPet && ((Pet) this.player).master.PorataVIP_2 == true && isFusion()) {//porata vip 2
            this.dame += calPercent(this.dame, 1);
        }
        if (this.player.isPet && ((Pet) this.player).master.PorataVIP_3 == true && isFusion()) {//porata vip 2
            this.dame += calPercent(this.dame, 100);
        }
        if (this.player.isPet && ((Pet) this.player).master.PorataVIP_4 == true && isFusion()) {//porata vip 2
            this.dame += calPercent(this.dame, 1);
        }
        if (this.player.isPet && ((Pet) this.player).master.PorataVIP_5 == true && isFusion()) {//porata vip 2
            this.dame += calPercent(this.dame, 1);
        }
        //thức ăn
        if (!this.player.isPet && this.player.itemTime.isEatMeal
                || this.player.isPet && ((Pet) this.player).master.itemTime.isEatMeal) {
            this.dame += calPercent(this.dame, 10);
        }
        //hợp thể
        if (this.player.fusion.typeFusion != 0) {
            this.dame += this.player.pet.nPoint.dame;
        }
        //cuồng nộ
        if (this.player.itemTime != null && this.player.itemTime.isUseCuongNo) {
            this.dame *= 2;
        }
        //cuồng nộ 2
        if (this.player.itemTime != null && this.player.itemTime.isUseCuongNo2) {
            this.dame += calPercent(dame, 120);
        }
        //phù mabu 14h
        if (this.player.zone != null && MapService.gI().isMapMabuWar14H(this.player.zone.map.mapId)) {
            this.dame += 10000;
        }
        //giảm dame
        this.dame -= calPercent(this.dame, tlSubSD);
        //map cold
        if (this.player.zone != null && MapService.gI().isMapCold(this.player.zone.map)
                && !this.isKhongLanh) {
            this.dame /= 2;
        }
        //ngọc rồng đen 1 sao
        if (this.player.rewardBlackBall.timeOutOfDateReward[0] > System.currentTimeMillis()) {
            this.dame += calPercent(this.dame, RewardBlackBall.R1S);
        }
        //ngọc rồng đen 3 sao
        if (this.player.rewardBlackBall.timeOutOfDateReward[2] > System.currentTimeMillis()) {
            this.mpMax += calPercent(this.mpMax, 25);
        }
        if (!player.isBoss) {
            Attribute at = ServerManager.gI().getAttributeManager().find(ConstAttribute.SUC_DANH);
            if (at != null && !at.isExpired()) {
                this.dame += calPercent(dame, at.getValue());
            }
        }
        if (this.player.itemTime != null) {
            if (this.player.itemTime.isUseBanhChung) {
                dame += calPercent(dame, 20);
            }
        }
        if (player.getBuff() == Buff.BUFF_ATK) {
            if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isRongVutrusd) {
                dame += calPercent(dame, 50);
            }
        }
        if (this.player.effectSkin.isNezuko) {
            this.dame += calPercent(this.dame, 15);
        }
        //khỉ
        if (this.player.effectSkill.isMonkey) {
            if (!this.player.isPet || (this.player.isPet
                    && ((Pet) this.player).status != Pet.FUSION)) {
                int percent = SkillUtil.getPercentDameMonkey(player.effectSkill.levelMonkey);
                this.dame += calPercent(dame, percent);
            }
        }
        //thức ăn
        if (!this.player.isPet && this.player.itemTime.isEatMeal
                || this.player.isPet && ((Pet) this.player).master.itemTime.isEatMeal) {
            this.dame += calPercent(dame, 10);
        }

        //đuôi khỉ
        if (!this.player.isPet && this.player.itemTimesieucap.isDuoikhi
                || this.player.isPet && ((Pet) this.player).master.itemTimesieucap.isDuoikhi) {
            this.dame += calPercent(dame, 5);
        }
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isDaNgucTu) {
            this.dame += calPercent(dame, 10);
        }
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isCuongBao) {
            this.dame += calPercent(dame, 20);
        }
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseXiMuoi) {
            this.dame += calPercent(dame, 50);
        }
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseTrungThu && this.player.itemTimesieucap.iconBanh == 4042) {
            this.dame += calPercent(dame, 10);
        }
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseTrungThu && this.player.itemTimesieucap.iconBanh == 4043) {
            this.dame += calPercent(dame, 20);
        }
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseTrungThu && this.player.itemTimesieucap.iconBanh == 4125) {
            this.dame += calPercent(dame, 30);
        }
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseTrungThu && this.player.itemTimesieucap.iconBanh == 4126) {
            this.dame += calPercent(dame, 40);
        }
        if (this.player.chuyensinhsuphu > 0) {
            this.dame += calPercent(dame, this.player.chuyensinhsuphu);
        }
        if (this.player.isPl() && (this.player.isTitleUse10 == true && this.player.DH_VV_10)) {
            this.dame += calPercent(dame, 200);
        }
        if (this.player.isPl() && (this.player.isTitleUse9 == true && this.player.DH_VV_9)) {
            this.dame += calPercent(dame, 200);
        }
        if (this.player.isPl() && (this.player.isTitleUse8 == true && this.player.DH_VV_8)) {
            this.dame += calPercent(dame, 200);
        }
        if (this.player.isPl() && (this.player.isTitleUse7 == true && this.player.DH_VV_7)) {
            this.dame += calPercent(dame, 100);
        }
        if (this.player.isPl() && (this.player.isTitleUse6 == true && this.player.DH_VV_6)) {
            this.dame += calPercent(dame, 50);
        }
        if (this.player.isPl() && (this.player.isTitleUse5 == true && this.player.lastTimeTitle5 > 0 || (this.player.isTitleUse5 == true && this.player.DH_VV_5))) {
            this.dame += calPercent(dame, 100);
        }
        if (this.player.isPl() && (this.player.isTitleUse4 == true && this.player.lastTimeTitle4 > 0 || (this.player.isTitleUse4 == true && this.player.DH_VV_4))) {
            this.dame += calPercent(dame, 100);
        }
        if (this.player.isPl() && (this.player.isTitleUse3 == true && this.player.lastTimeTitle3 > 0 || (this.player.isTitleUse3 == true && this.player.DH_VV_3))) {
            this.dame += calPercent(dame, 30);
        }
        if (this.player.isPl() && (this.player.isTitleUse2 == true && this.player.lastTimeTitle2 > 0 || (this.player.isTitleUse2 == true && this.player.DH_VV_2))) {
            this.dame += calPercent(dame, 50);
        }
        if (this.player.isPl() && (this.player.isTitleUse1 == true && this.player.lastTimeTitle1 > 0 || (this.player.isTitleUse1 == true && this.player.DH_VV_1))) {
            this.dame += calPercent(dame, 50);
        }
        //rồng băng
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isRongBang) {
            this.dame += calPercent(dame, 30);
        }
        //rồng băng
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isRongSieuCap) {
            this.dame += calPercent(dame, 50);
        }
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isRongHoangKim) {
            this.dame += calPercent(dame, 200);
        }
        //skill biến hình
        if (this.player.effectSkill.isBienHinh) {
            this.dame += calPercent(dame, EffectSkillService.gI().PointBienHinh(this.player));
        }
        //cà rốt
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isUseCaRot) {
            this.dame += calPercent(dame, 50);
        }
        //rồng siêu cấp
        if (this.player.isPet && ((Pet) this.player).master.itemTimesieucap.isDeTuSC) {
            this.dame += calPercent(this.dame, 100);
        }
        if (!this.player.isPet && this.player.itemTimesieucap.isSuPhuSC) {
            this.dame += calPercent(this.dame, 100);
        }
        if (this.player.isPl() && this.player.isTuTien){
            this.dame += calPercent(this.dame, (this.player.bonusTuTien + Service.getInstance().nPointTuTien(this.player.levelTuTien, this.player.tangTuTien)));
        }
    }

    protected void setDef() {
        this.def = this.defg * 4;
        this.def += this.defAdd;
        //ngọc rồng đen 4 sao
        if (this.player.rewardBlackBall.timeOutOfDateReward[6] > System.currentTimeMillis()) {
            this.tlDef.add(RewardBlackBall.R7S);
        }
        //đồ
        for (Integer tl : this.tlDef) {
            this.tlGiamst += tl;
        }
        if (tlGiamst > 90) {
            tlGiamst = 90;
        }
        //ngọc rồng đen 5 sao
        if (this.player.rewardBlackBall.timeOutOfDateReward[4] > System.currentTimeMillis()) {
            this.tlHutHp += RewardBlackBall.R5S;
        }
        if (this.player.effectSkin.isInosuke) {
            this.def += calPercent(this.def, 50);
        }
        if (this.player.effectSkin.isInoHashi) {
            this.def += calPercent(this.def, 60);
        }
    }

    private void setxuyenDef() {
        //đồ
        for (Integer tl : this.tlxuyenDef) {
            this.tlxuyengiap += tl;
        }
        if (tlxuyengiap > 90) {
            tlxuyengiap = 90;
        }
    }

    protected void setCrit() {
        this.crit = this.critg;
        this.crit += this.critAdd;
        //biến khỉ
        if (this.player.effectSkill.isMonkey) {
            this.crit = 110;
        }
        if (player.getBuff() == Buff.BUFF_CRIT) {
            if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isRongVutrucm) {
                crit += 30;
            }
        }
        if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isCuongBao) {
            this.crit = 110;
        }
    }

    private void setCritDame() {
        if (this.player.effectSkin.isTanjiro) {
            this.tlDameCrit.add(30);
        }
        if (this.player.itemTime != null) {
            if (this.player.itemTime.isUseBanhChung) {
                this.tlDameCrit.add(15);
            }
        }
        if (this.player.setClothes.SetbillSdcm == 5) {
            this.tlDameCrit.add(50); // Tang 50% ti le dame chi mang
        }
        //ngọc rồng đen 4 sao
        if (this.player.rewardBlackBall.timeOutOfDateReward[3] > System.currentTimeMillis()) {
            this.tlDameCrit.add(RewardBlackBall.R4S);
        }
    }

    private void setSpeed() {
        for (Integer tl : this.tlSpeed) {
            this.speed += calPercent(this.speed, tl);
        }
        if (this.player.effectSkin.isSlow) {
            this.speed = 1;
        }
    }

    protected void resetPoint() {
        this.hpAdd = 0;
        this.mpAdd = 0;
        this.dameAdd = 0;
        this.defAdd = 0;
        this.critAdd = 0;
        this.tlHp.clear();
        this.tlMp.clear();
        this.tlDef.clear();
        this.tlxuyenDef.clear();
        this.tlDame.clear();
        this.tlDameAttMob.clear();
        this.tlDameCrit.clear();
        this.tlHpHoiBanThanVaDongDoi = 0;
        this.tlMpHoiBanThanVaDongDoi = 0;
        this.hpHoi = 0;
        this.mpHoi = 0;
        this.mpHoiCute = 0;
        this.tlHpHoi = 0;
        this.tlMpHoi = 0;
        this.tlHutHp = 0;
        this.tlHutMp = 0;
        this.tlHutHpMob = 0;
        this.tlHutHpMpXQ = 0;
        this.tlPST = 0;
        this.tlTNSM.clear();
        this.tlDameAttMob.clear();
        this.tlDameCrit.clear();
        this.tlGold = 0;
        this.tlNeDon = 0;
        this.tlchinhxac = 0;
        this.tlSDDep.clear();
        this.tlSubSD = 0;
        this.tlHpGiamODo = 0;
        this.teleport = false;
        this.tlSpeed.clear();
        this.speed = 5;
        this.mstChuong = 0;
        this.tlGiamst = 0;
        this.tlxuyengiap = 0;
        this.tlTNSMPet = 0;

        this.wearingVoHinh = false;
        this.isKhongLanh = false;
        this.wearingDrabula = false;
        this.wearingNezuko = false;
        this.wearingZenitsu = false;
        this.wearingInosuke = false;
        this.wearingInoHashi = false;
        this.wearingTanjiro = false;
        this.wearingMabu = false;
        this.wearingBuiBui = false;
        this.xDameChuong = false;
        this.wearingYacon = false;
        this.isNgoaiHinh = false;

        this.TangLienHoan = false;
        this.TangGalick = false;
        this.TangKaioken = false;
        this.TangKame = false;
    }

    public void addHp(double hp) {
        this.hp += hp;
        if (this.hp > this.hpMax) {
            this.hp = this.hpMax;
        }
    }

    public void addMp(double mp) {
        this.mp += mp;
        if (this.mp > this.mpMax) {
            this.mp = this.mpMax;
        }
    }

    public void setHp(double hp) {
        if (hp > this.hpMax) {
            this.hp = this.hpMax;
        } else {
            this.hp = (double) hp;
        }
    }

    public void setMp(double mp) {
        if (mp > this.mpMax) {
            this.mp = this.mpMax;
        } else {
            this.mp = (double) mp;
        }
    }

    private void setIsCrit() {
        if (intrinsic != null && intrinsic.id == 25
                && this.getCurrPercentHP() <= intrinsic.param1) {
            isCrit = true;
        } else if (isCrit100) {
            isCrit100 = false;
            isCrit = true;
        } else {
            isCrit = Util.isTrue(this.crit, ConstRatio.PER100);
        }
    }

    public double getDameAttack(boolean isAttackMob) {
        setIsCrit();
        double dameAttack = this.dame;
        intrinsic = this.player.playerIntrinsic.intrinsic;
        percentDameIntrinsic = 0;
        double percentDameSkill = 0;
        long percentXDame = 0;
       Skill skillSelect = player.playerSkill.skillSelect;
        switch (skillSelect.template.id) {
            case Skill.DRAGON:
                if (intrinsic.id == 1) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                break;
            case Skill.KAMEJOKO:
                if (intrinsic.id == 2) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                if (this.player.setClothes.songoku == 5) {
                    percentXDame = 80;
                }
                if (this.player.setClothes.nhanhoangKM == 1 && this.player.setClothes.songoku == 4) {
                    percentXDame = 100;
                }
                if (this.player.setClothes.nhanhoangKM == 5) {
                    percentXDame = 150;
                }
                if (this.player.setClothes.nhanhoangKM_1 == 1 && this.player.setClothes.songoku == 4
                        || this.player.setClothes.nhanhoangKM_1 == 2 && this.player.setClothes.songoku == 3
                        || this.player.setClothes.nhanhoangKM_1 == 3 && this.player.setClothes.songoku == 2
                        || this.player.setClothes.nhanhoangKM_1 == 4 && this.player.setClothes.songoku == 1) {
                    percentXDame = 100;
                }
                if (this.player.setClothes.nhanhoangKM_1 == 5) {
                    percentXDame = 200;
                }

                if (this.TangKame == true) {
                    percentXDame += 30;
                }
                break;
            case Skill.GALICK:
                if (intrinsic.id == 16) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                if (this.player.setClothes.kakarot == 5) {
                    percentXDame = 80;
                }
                if (this.player.setClothes.thienhoangGL_1 == 1 && this.player.setClothes.kakarot == 4
                        || this.player.setClothes.thienhoangGL_1 == 2 && this.player.setClothes.kakarot == 3
                        || this.player.setClothes.thienhoangGL_1 == 3 && this.player.setClothes.kakarot == 2
                        || this.player.setClothes.thienhoangGL_1 == 4 && this.player.setClothes.kakarot == 1) {
                    percentXDame = 100;
                }
                if (this.player.setClothes.thienhoangGL_1 == 5) {
                    percentXDame = 200;
                }
                if (this.player.setClothes.thienhoangdbGL == 5) {
                    percentXDame = 700;
                }

                if (this.TangGalick == true) {
                    percentXDame += 30;
                }
                break;
            case Skill.ANTOMIC:
                if (intrinsic.id == 17) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
//                if (this.player.effectSkin.xDameChuong) {
//                    percentXDame += tlDameChuong;
//                    this.player.effectSkin.xDameChuong = false;
//                }
                break;
            case Skill.DEMON:
                if (intrinsic.id == 8) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                break;
            case Skill.MASENKO:
                if (intrinsic.id == 9) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
//                if (this.player.effectSkin.xDameChuong) {
//                    percentXDame += tlDameChuong * 100;
//                    this.player.effectSkin.xDameChuong = false;
//                }
                break;
            case Skill.KAIOKEN:
                if (intrinsic.id == 26) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                if (this.player.setClothes.thienXinHang == 5) {
                    percentXDame = 100;
                }
                if (this.player.setClothes.nhanhoangKOK == 1 && this.player.setClothes.thienXinHang == 4) {
                    percentXDame = 100;
                }
                if (this.player.setClothes.nhanhoangKOK == 5) {
                    percentXDame = 150;
                }
                if (this.player.setClothes.nhanhoangKOK_1 == 1 && this.player.setClothes.thienXinHang == 4
                        || this.player.setClothes.nhanhoangKOK_1 == 2 && this.player.setClothes.thienXinHang == 3
                        || this.player.setClothes.nhanhoangKOK_1 == 3 && this.player.setClothes.thienXinHang == 2
                        || this.player.setClothes.nhanhoangKOK_1 == 4 && this.player.setClothes.thienXinHang == 1) {
                    percentXDame = 100;
                }
                if (this.player.setClothes.nhanhoangKOK_1 == 5) {
                    percentXDame = 200;
                }

                if (this.TangKaioken == true) {
                    percentXDame += 30;
                }
                break;
            case Skill.LIEN_HOAN:
                if (intrinsic.id == 13) {
                    percentDameIntrinsic = intrinsic.param1;
                }
                percentDameSkill = skillSelect.damage;
                if (this.player.setClothes.ocTieu == 5) {
                    percentXDame = 80;
                }
                if (this.player.setClothes.mahoangLH == 1 && this.player.setClothes.ocTieu == 4) {
                    percentXDame = 100;
                }
                if (this.player.setClothes.mahoangLH == 5) {
                    percentXDame = 150;
                }
                if (this.player.setClothes.mahoangLH_1 == 1 && this.player.setClothes.ocTieu == 4
                        || this.player.setClothes.mahoangLH_1 == 2 && this.player.setClothes.ocTieu == 3
                        || this.player.setClothes.mahoangLH_1 == 3 && this.player.setClothes.ocTieu == 2
                        || this.player.setClothes.mahoangLH_1 == 4 && this.player.setClothes.ocTieu == 1) {
                    percentXDame = 100;
                }
                if (this.player.setClothes.mahoangLH_1 == 5) {
                    percentXDame = 200;
                }

                if (this.TangLienHoan == true) {
                    percentXDame += 30;
                }
                break;
            case Skill.DICH_CHUYEN_TUC_THOI:
                dameAttack *= 2;
                dameAttack = Util.nextdame((double) (dameAttack - calPercent(dameAttack, 5)),
                        (double) (dameAttack + calPercent(dameAttack, 5)));
                return dameAttack;
            case Skill.MAKANKOSAPPO:
                percentDameSkill = skillSelect.damage;
                double dameSkill = this.mpMax * percentDameSkill;
                return dameSkill;
            case Skill.QUA_CAU_KENH_KHI:
                long totalHP = 0;
                if (player.zone != null) {
                    totalHP = player.zone.getTotalHP();
                }

                double damage = totalHP + (this.dame * 50);
                damage = calPercent(damage, skillSelect.damage);

                if (this.player.setClothes.kirin == 5) {
                    damage *= 3;
                }

                return damage;
            }
        if (intrinsic.id == 18 && this.player.effectSkill.isMonkey) {
            percentDameIntrinsic = intrinsic.param1;
        }
        if (percentDameSkill != 0) {
            dameAttack = calPercent(dameAttack, percentDameSkill);
        }
        dameAttack += calPercent(dameAttack, percentDameIntrinsic);
        dameAttack += calPercent(dameAttack, dameAfter);

        if (isAttackMob) {
            for (Integer tl : this.tlDameAttMob) {
                dameAttack += calPercent(dameAttack, tl);
            }
        }
        dameAfter = 0;
        if (this.player.isPet && ((Pet) this.player).master.charms.tdDeTu > System.currentTimeMillis()) {
            dameAttack *= 2;
        }
        if (isCrit) {
            dameAttack *= 2;
            for (Integer tl : this.tlDameCrit) {
                dameAttack += calPercent(dameAttack, tl);
            }
        }
        dameAttack += calPercent((double) dameAttack, (double) percentXDame);
//        System.out.println(dameAttack);
        dameAttack = Util.nextdame((double) (dameAttack - calPercent(dameAttack, 5)), (double) (dameAttack + calPercent(dameAttack, 5)));

        if (player.isPl()) {
            if (player.inventory.haveOption(player.inventory.itemsBody, 5, 159)) {
                if (Util.canDoWithTime(player.lastTimeUseOption, 60000) && (player.playerSkill.skillSelect.template.id == Skill.KAMEJOKO || player.playerSkill.skillSelect.template.id == Skill.ANTOMIC || player.playerSkill.skillSelect.template.id == Skill.MASENKO)) {
                    dameAttack *= player.inventory.getParam(player.inventory.itemsBody.get(5), 159);
                    player.lastTimeUseOption = System.currentTimeMillis();
                    Service.getInstance().sendThongBao(player, "|1|Bạn vừa gây ra x" + player.inventory.getParam(player.inventory.itemsBody.get(5), 159) + " Sát thương Chưởng");
                }
            }
        }

        //check activation set
        return dameAttack;
    }

    public double getDameAttackSkillNotFocus() {
        setIsCrit();
        double dameAttack = this.dame;
        intrinsic = this.player.playerIntrinsic.intrinsic;
        percentDameIntrinsic = 0;
        int percentDameSkill = 0;
        int percentXDame = 0;
        Skill skillSelect = player.playerSkill.skillSelect;
        switch (skillSelect.template.id) {

        }
        if (intrinsic.id == 18 && this.player.effectSkill.isMonkey) {
            percentDameIntrinsic = intrinsic.param1;
        }
        if (percentDameSkill != 0) {
            dameAttack = calPercent(dameAttack, percentDameSkill);
        }
        dameAttack += calPercent(dameAttack, percentDameIntrinsic);
        dameAttack += calPercent(dameAttack, dameAfter);
        dameAfter = 0;
        if (this.player.isPet && ((Pet) this.player).master.charms.tdDeTu > System.currentTimeMillis()) {
            dameAttack *= 2;
        }
        if (isCrit) {
            dameAttack *= 2;
            for (Integer tl : this.tlDameCrit) {
                dameAttack += calPercent(dameAttack, tl);
            }
        }
        dameAttack += calPercent(dameAttack, percentXDame);
        dameAttack = Util.nextdame(
    (long)Math.max(0, dameAttack - calPercent(dameAttack, 5)),
    (long)(dameAttack + calPercent(dameAttack, 5)));
        return (double) dameAttack;
    }

    public double getCurrPercentHP() {
        if (this.hpMax == 0) {
            return 100;
        }
        return this.hp * 100 / this.hpMax;
    }

    public double getCurrPercentMP() {
        return this.mp * 100 / this.mpMax;
    }

    public void setFullHpMp() {
        this.hp = this.hpMax;
        this.mp = this.mpMax;
    }

    public void subHP(double sub) {
        this.hp -= sub;
        if (this.hp < 0) {
            this.hp = 0;
        }
    }

    public void subMP(long sub) {
        this.mp -= sub;
        if (this.mp < 0) {
            this.mp = 0;
        }
    }

    public long calSucManhTiemNang(long tiemNang) {
        if (power < getPowerLimit()) {
            for (Integer tl : this.tlTNSM) {
                tiemNang += calPercent(tiemNang, tl);
            }
            if (player.getBuff() == Buff.BUFF_TNSM) {
                if (this.player.itemTimesieucap != null && this.player.itemTimesieucap.isRongVutrutnsm) {
                    tiemNang += calPercent(tiemNang, 500);
                }
            }
            if (this.player.cFlag != 0) {
                if (this.player.cFlag == 8) {
                    tiemNang += calPercent(tiemNang, 10);
                } else {
                    tiemNang += calPercent(tiemNang, 5);
                }
            }
            if (buffExpSatellite) {
                tiemNang += calPercent(tiemNang, 20);
            }
            if (player.isPet) {
                Attribute at = ServerManager.gI().getAttributeManager().find(ConstAttribute.TNSM);
                if (at != null && !at.isExpired()) {
                    tiemNang += calPercent(tiemNang, at.getValue());
                }
            }
            if (this.player.isPet) {
                int tltnsm = ((Pet) this.player).master.nPoint.tlTNSMPet;
                if (tltnsm > 0) {
                    tiemNang += calPercent(tiemNang, tltnsm);
                }
            }
            long tn = tiemNang;
            if (this.player.charms.tdTriTue > System.currentTimeMillis()) {
                tiemNang += tn;
            }
            if (this.player.charms.tdTriTue3 > System.currentTimeMillis()) {
                tiemNang += tn * 2;
            }
            if (this.player.charms.tdTriTue4 > System.currentTimeMillis()) {
                tiemNang += tn * 3;
            }
            if (!this.player.isPet && this.player.itemTimesieucap.isDuoikhi
                    || this.player.isPet && ((Pet) this.player).master.itemTimesieucap.isDuoikhi) {
                tiemNang += tn * 3;
            }
            if (!this.player.isPet && this.player.itemTimesieucap.isKeo
                    || this.player.isPet && ((Pet) this.player).master.itemTimesieucap.isKeo) {
                tiemNang += tn * 2;
            }
            if (!this.player.isPet && this.player.itemTimesieucap.isUseXiMuoi
                    || this.player.isPet && ((Pet) this.player).master.itemTimesieucap.isUseXiMuoi) {
                tiemNang += calPercent(tiemNang, 50);
            }
            if (this.intrinsic != null && this.intrinsic.id == 24) {
                tiemNang += calPercent(tiemNang, this.intrinsic.param1);
            }
            if (this.power >= 60000000000L) {
                tiemNang -= calPercent(tiemNang, 70);
            }
            if (this.player.isPet) {
                if (((Pet) this.player).master.charms.tdDeTu > System.currentTimeMillis()) {
                    tiemNang += tn * 2;
                }
            }

            // Thêm các điều kiện sau dòng này thì sẽ tính đúng các phép cộng trước đó.
            tiemNang *= Manager.RATE_EXP_SERVER;
            tiemNang = calSubTNSM(tiemNang);
            if (tiemNang <= 0) {
                tiemNang = 1;
            }
        } else {
            tiemNang = 10;
        }
        return tiemNang;
    }

    public long calSubTNSM(long tiemNang) {
        if (this.player.zone != null && MapService.gI().isMapChungSinhBinhDang(this.player.zone.map.mapId)) {
            return 0;
        }
        if (this.player.nPoint.power < 16_000_000_000L
                && this.player.zone != null && MapService.gI().isMapNguHanhSon(this.player.zone.map.mapId)) {
            tiemNang *= 2;
        }
        if (this.player.zone != null && MapService.gI().isMapBanDoKhoBau(this.player.zone.map.mapId)) {
            tiemNang *= 2;
        }
        if (power >= 350000000000L) {
            tiemNang -= calPercent(tiemNang, 98);
        } else if (power >= 210000000000L) {
            tiemNang -= calPercent(tiemNang, 95);
        } else if (power >= 110000000000L) {
            tiemNang -= calPercent(tiemNang, 90);
        } else if (power >= 100000000000L) {
            tiemNang -= calPercent(tiemNang, 85);
        } else if (power >= 90000000000L) {
            tiemNang -= calPercent(tiemNang, 80);
        } else if (power >= 80000000000L) {
            tiemNang -= calPercent(tiemNang, 75);
        }
        if (tiemNang > 10000000000L) {
            tiemNang = 10000000000L;
        }
        return tiemNang;
    }

    public short getTileHutHp(boolean isMob) {
        if (isMob) {
            return (short) (this.tlHutHp + this.tlHutHpMob);
        } else {
            return this.tlHutHp;
        }
    }

    public short getTiLeHutMp() {
        return this.tlHutMp;
    }

    public double subDameInjureWithDeff(double dame) {
        double def = this.def;
        dame -= def;
        if (this.player.itemTime.isUseGiapXen) {
            dame /= 2;
        }
        if (this.player.itemTime.isUseGiapXen2) {
            dame -= calPercent(dame, 60);
        }
        if (dame < 0) {
            dame = 1;
        }
        return dame;
    }

    /*------------------------------------------------------------------------*/
    public boolean canOpenPower() {
        return this.power >= getPowerLimit();
    }

    public long getPowerLimit() {
        if (powerLimit != null) {
            return powerLimit.getPower();
        }
        return 0;
    }

    public long getPowerNextLimit() {
        PowerLimit powerLimit = PowerLimitManager.getInstance().get(limitPower + 1);
        if (powerLimit != null) {
            return powerLimit.getPower();
        }
        return 0;
    }

    //**************************************************************************
    //POWER - TIEM NANG
    public void powerUp(long power) {
        this.power += power;
        TaskService.gI().checkDoneTaskPower(player, this.power);
    }

    public void tiemNangUp(long tiemNang) {
        this.tiemNang += tiemNang;
    }

    public void increasePoint(byte type, short point) {
        if (this.player.zone == null || MapService.gI().isMapChungSinhBinhDang(this.player.zone.map.mapId) || ChungSinhBinhDang.gI().isPlayerDangKy(player)) {
            Service.getInstance().sendThongBaoOK(player, "Không thể thực hiện tại map này");
            return;
        }
        if (powerLimit == null) {
            return;
        }
        if (point <= 0) {
            return;
        }
        boolean updatePoint = false;
        double tiemNangUse = 0;
        if (type == 0) {
            int pointHp = point * 20;
            tiemNangUse = point * (2 * (this.hpg + 1000) + pointHp - 20) / 2;
            if ((this.hpg + pointHp) <= powerLimit.getHp()) {
                if (doUseTiemNang(tiemNangUse)) {
                    hpg += pointHp;
                    updatePoint = true;
                }
            } else {
                Service.getInstance().sendThongBaoOK(player, "Vui lòng mở giới hạn sức mạnh");
                return;
            }
        }
        if (type == 1) {
            int pointMp = point * 20;
            tiemNangUse = point * (2 * (this.mpg + 1000) + pointMp - 20) / 2;
            if ((this.mpg + pointMp) <= powerLimit.getMp()) {
                if (doUseTiemNang(tiemNangUse)) {
                    mpg += pointMp;
                    updatePoint = true;
                }
            } else {
                Service.getInstance().sendThongBaoOK(player, "Vui lòng mở giới hạn sức mạnh");
                return;
            }
        }
        if (type == 2) {
            tiemNangUse = point * (2 * this.dameg + point - 1) / 2 * 100;
            if ((this.dameg + point) <= powerLimit.getDamage()) {
                if (doUseTiemNang(tiemNangUse)) {
                    dameg += point;
                    updatePoint = true;
                }
            } else {
                Service.getInstance().sendThongBaoOK(player, "Vui lòng mở giới hạn sức mạnh");
                return;
            }
        }
        if (type == 3) {
            tiemNangUse = point * 2 * (this.defg + 5) / 2 * 100000;
            if ((this.defg + point) <= powerLimit.getDefense()) {
                if (doUseTiemNang(tiemNangUse)) {
                    defg += point;
                    updatePoint = true;
                }
            } else {
                Service.getInstance().sendThongBaoOK(player, "Vui lòng mở giới hạn sức mạnh");
                return;
            }
        }
        if (type == 4) {
            tiemNangUse = 50000000L;
            for (int i = 0; i < this.critg; i++) {
                tiemNangUse *= 5L;
            }
            if ((this.critg + point) <= powerLimit.getCritical()) {
                if (doUseTiemNang(tiemNangUse)) {
                    critg += point;
                    updatePoint = true;
                }
            } else {
                Service.getInstance().sendThongBaoOK(player, "Vui lòng mở giới hạn sức mạnh");
                return;
            }
        }
        if (updatePoint) {
            Service.getInstance().point(player);
        }
    }

    private boolean doUseTiemNang(double tiemNang) {
        if (this.tiemNang < tiemNang) {
            Service.getInstance().sendThongBaoOK(player, "Bạn không đủ tiềm năng");
            return false;
        }
        if (this.tiemNang >= tiemNang) {
            this.tiemNang -= tiemNang;
            TaskService.gI().checkDoneTaskUseTiemNang(player);
            return true;
        }
        return false;
    }

    //--------------------------------------------------------------------------
    private long lastTimeHoiPhuc;
    private long lastTimeHoiStamina;
    public int maxCountCharge = 10;
    public int delayTTNL = 1000;

    public void update() {
        if (player != null && player.effectSkill != null) {
            if (player.effectSkill.isCharging && player.effectSkill.countCharging < 10) {
                long tiLeHoiPhuc = SkillUtil.getPercentCharge(player.playerSkill.skillSelect.point);
                if (player.effectSkill.isCharging && !player.isDie() && !player.effectSkill.isHaveEffectSkill()
                        && (hp < hpMax || mp < mpMax)) {
                    PlayerService.gI().hoiPhuc(player, calPercent(hpMax, tiLeHoiPhuc), calPercent(mpMax, tiLeHoiPhuc));
                    if (player.effectSkill.countCharging % 3 == 0) {
                        Service.getInstance().chat(player, "Phục hồi năng lượng " + Util.LongGioihan(getCurrPercentHP()) + "%");
                    }
                } else {
                    EffectSkillService.gI().stopCharge(player);
                }
                if (++player.effectSkill.countCharging >= 10) {
                    EffectSkillService.gI().stopCharge(player);
                }
            }
            if (Util.canDoWithTime(lastTimeHoiPhuc, 30000)) {
                PlayerService.gI().hoiPhuc(this.player, hpHoi, mpHoi);
                this.lastTimeHoiPhuc = System.currentTimeMillis();
            }
            if (Util.canDoWithTime(lastTimeHoiStamina, 60000) && this.stamina < this.maxStamina) {
                this.stamina++;
                this.lastTimeHoiStamina = System.currentTimeMillis();
                if (!this.player.isBoss && !this.player.isPet) {
                    PlayerService.gI().sendCurrentStamina(this.player);
                }
            }
        }
        //hồi phục 30s
        //hồi phục thể lực
    }

    protected void setBasePoint() {
        setHpMax();
        setMpMax();
        setDame();
        setDef();
        setxuyenDef();
        setCrit();
        setHpHoi();
        setMpHoi();
        setNeDon();
        setCritDame();
        setSpeed();
        setAttributeOverLimit();
    }

    public void setAttributeOverLimit() {
        int max = Integer.MAX_VALUE;
        int min = -100000000;
        if (this.hpMax < 0) {
            if (this.hpMax < min) {
                this.hpMax = max;
            } else {
                this.hpMax = 1;
            }
        }
        if (this.mpMax < 0) {
            if (this.mpMax < min) {
                this.mpMax = max;
            } else {
                this.mpMax = 1;
            }
        }
        if (this.dame < 0) {
            if (this.dame < min) {
                this.dame = max;
            } else {
                this.dame = 1;
            }
        }
        if (this.def < 0) {
            if (this.def < min) {
                this.def = max;
            } else {
                this.def = 1;
            }
        }
        if (this.crit < 0) {
            if (this.crit < min) {
                this.crit = max;
            } else {
                this.crit = 1;
            }
        }
        setHp();
        setMp();
        if (this.tlNeDon > 100) {
            this.tlNeDon = 100;
        }
        if (this.tlchinhxac > 100) {
            this.tlchinhxac = 100;
        }
    }

    public double calPercent(double param, double percent) {
        return param * percent / 100;
    }

    public void dispose() {
        this.intrinsic = null;
        this.player = null;
        this.tlHp = null;
        this.tlMp = null;
        this.tlDef = null;
        this.tlxuyenDef = null;
        this.tlDame = null;
        this.tlDameAttMob = null;
        this.tlSDDep = null;
        this.tlTNSM = null;
        this.tlDameCrit = null;
        this.tlSpeed = null;
    }

    private boolean isFusion() {
        return (((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA2
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA3
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA4
                || ((Pet) this.player).master.fusion.typeFusion == ConstPlayer.HOP_THE_PORATA5);
    }
}
