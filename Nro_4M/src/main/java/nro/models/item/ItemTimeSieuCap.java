package nro.models.item;

import nro.models.player.NPoint;
import nro.models.player.Player;
import nro.services.EffectSkillService;
import nro.services.ItemTimeService;
import nro.services.Service;
import nro.utils.Util;

public class ItemTimeSieuCap {

    //id item text
    public static final int TIME_ITEM_SC_10P = 600000;
    public static final int TIME_ITEM_SC_30P = 1800000;
    public static final int TIME_TRUNGTHU = 3600000;
    public static final int TIME_ITEM_SC_1_DAY = 21600000;

    private Player player;

    public boolean isRongHoangKim;
    public long lastTimeRongHoangKim;

    public boolean isDuoikhi;
    public boolean isDaNgucTu;
    public boolean isRongSieuCap;
    public boolean isRongBang;
    public boolean isUseCaRot;

    public boolean isCuongBao;
    public long lastTimeCuongBao;

    public long lastTimeDuoikhi;
    public long lastTimeDaNgucTu;
    public long lastTimeRongSieuCap;
    public boolean isRongVutrusd;
    public boolean isRongVutruhp;
    public boolean isRongVutruki;
    public boolean isRongVutrucm;
    public boolean isRongVutrutnsm;
    public long lastTimeRongBang;
    public long lastTimeCaRot;

    public boolean isUseXiMuoi;
    public long lastTimeUseXiMuoi;
    public long lastTimeRongVutrusd;
    public long lastTimeRongVutruhp;
    public long lastTimeRongVutruki;
    public long lastTimeRongVutrucm;
    public long lastTimeRongVutrutnsm;
    public boolean isUseTrungThu;
    public long lastTimeUseBanh;
    public int iconBanh;

    public boolean isKeo;
    public long lastTimeKeo;

    public boolean isTraiDua;
    public long lastTimeTraiDua;
    public int iconMeal;
    
    public boolean isSuPhuSC;
    public long lastTimeSuPhuSC;
    
    public boolean isDeTuSC;
    public long lastTimeDeTuSC;

    public ItemTimeSieuCap(Player player) {
        this.player = player;
    }

    public void update() {
        if (isDuoikhi) {
            if (Util.canDoWithTime(lastTimeDuoikhi, TIME_ITEM_SC_10P)) {
                isDuoikhi = false;
                Service.getInstance().point(player);
            }
        }

        if (isDaNgucTu) {
            if (Util.canDoWithTime(lastTimeDaNgucTu, TIME_ITEM_SC_10P)) {
                isDaNgucTu = false;
                Service.getInstance().point(player);
            }
        }
        if (isUseCaRot) {
            if (Util.canDoWithTime(lastTimeCaRot, TIME_ITEM_SC_10P)) {
                isUseCaRot = false;
                Service.getInstance().point(player);
            }
        }

        if (isKeo) {
            if (Util.canDoWithTime(lastTimeKeo, TIME_ITEM_SC_30P)) {
                isKeo = false;
                Service.getInstance().point(player);
            }
        }
        if (isUseXiMuoi) {
            if (Util.canDoWithTime(lastTimeUseXiMuoi, TIME_ITEM_SC_10P)) {
                isUseXiMuoi = false;
                Service.getInstance().point(player);
            }
        }
        if (isUseTrungThu) {
            if (Util.canDoWithTime(lastTimeUseBanh, TIME_TRUNGTHU)) {
                isUseTrungThu = false;
                Service.getInstance().point(player);
            }
        }
        if (isCuongBao) {
            if (Util.canDoWithTime(lastTimeCuongBao, TIME_ITEM_SC_10P)) {
                isCuongBao = false;
//                EffectSkillService.gI().removeCuongBao(player);
                Service.getInstance().point(player);
            }
        }
        if (isTraiDua) {
            if (Util.canDoWithTime(lastTimeTraiDua, TIME_ITEM_SC_30P)) {
                isTraiDua = false;
                Service.getInstance().point(player);
            }
        }
        if (isRongSieuCap) {
            if (Util.canDoWithTime(lastTimeRongSieuCap, TIME_ITEM_SC_30P)) {
                isRongSieuCap = false;
                Service.getInstance().point(player);
            }
        }
        if (isRongBang) {
            if (Util.canDoWithTime(lastTimeRongBang, TIME_ITEM_SC_30P)) {
                isRongBang = false;
                Service.getInstance().point(player);
            }
        }
        if (this.player.useCanCau) {
            if (Util.canDoWithTime(this.player.lasttimeCanCau, player.timeCauCa)) {
                this.player.useCanCau = false;
            }
        }
        if (this.player.dangGhepAn) {
            if (Util.canDoWithTime(this.player.lasttimeGhepAn, player.timeGhepAn)) {
                this.player.dangGhepAn = false;
            }
        }
        if (isRongHoangKim) {
            if (Util.canDoWithTime(lastTimeRongHoangKim, TIME_ITEM_SC_1_DAY)) {
                isRongHoangKim = false;
                Service.getInstance().point(player);
            }
        }
        if (isRongVutrusd) {
            if (Util.canDoWithTime(lastTimeRongVutrusd, TIME_ITEM_SC_30P)) {
                isRongVutrusd = false;
                Service.getInstance().point(player);
            }
        }
        if (isRongVutruhp) {
            if (Util.canDoWithTime(lastTimeRongVutruhp, TIME_ITEM_SC_30P)) {
                isRongVutruhp = false;
                Service.getInstance().point(player);
            }
        }
        if (isRongVutruki) {
            if (Util.canDoWithTime(lastTimeRongVutruki, TIME_ITEM_SC_30P)) {
                isRongVutruki = false;
                Service.getInstance().point(player);
            }
        }
        if (isRongVutrucm) {
            if (Util.canDoWithTime(lastTimeRongVutrucm, TIME_ITEM_SC_30P)) {
                isRongVutrucm = false;
                Service.getInstance().point(player);
            }
        }
        if (isRongVutrutnsm) {
            if (Util.canDoWithTime(lastTimeRongVutrutnsm, TIME_ITEM_SC_30P)) {
                isRongVutrutnsm = false;
                Service.getInstance().point(player);
            }
        }
        if (isSuPhuSC) {
            if (Util.canDoWithTime(lastTimeSuPhuSC, TIME_ITEM_SC_10P)) {
                isSuPhuSC = false;
                Service.getInstance().point(player);
            }
        }
        if (isDeTuSC) {
            if (Util.canDoWithTime(lastTimeDeTuSC, TIME_ITEM_SC_10P)) {
                isDeTuSC = false;
                Service.getInstance().point(player);
            }
        }
    }

    public void dispose() {
        this.player = null;
    }
}
