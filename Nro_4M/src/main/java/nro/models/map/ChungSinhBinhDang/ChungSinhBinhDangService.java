package nro.models.map.ChungSinhBinhDang;

import nro.services.Service;
import nro.utils.TimeUtil;

public class ChungSinhBinhDangService {

    private static ChungSinhBinhDangService i;
    // ====== Các mốc thời gian tính theo phút ======
    private static final int PHUT_DANG_KY_BAT_DAU = 0;
    private static final int PHUT_DANG_KY_KET_THUC = 5;

    private static final int PHUT_CHUAN_BI_BAT_DAU = 5;
    private static final int PHUT_CHUAN_BI_KET_THUC = 10;

    private static final int PHUT_TRAN_DAU_BAT_DAU = 10; // Vào map chính
    private static final int PHUT_DOI_CO = 15; // phút 15 đổi cờ all player
    private static final int PHUT_BAT_DAU_THU_BO = 17; // phút 17 sẽ bắt đầu thu bo
    private static final int PHUT_TRAN_DAU_KET_THUC = 50; // kết thúc

    // ====== Chỉ số cơ bản ======
    public static final double HP = 50_000_000;
    public static final double KI = 50_000_000;
    public static final double SUC_DANH = 500_000;
    public static final double GIAP = 50_000;
    public static final int CHI_MANG = 1;

    // ====== Giới hạn cộng gốc ======
    public static final double MAX_HP_MP_GOC = 10_000_000_000D;
    public static final double MAX_DAME_GOC = 1_000_000_000D;

    public static ChungSinhBinhDangService gI() {
        if (i == null) {
            i = new ChungSinhBinhDangService();
        }
        return i;
    }

    public enum TrangThaiTran {
        CHO_DANG_KY,
        CHUAN_BI,
        DA_BAT_DAU,
        DOI_CO,
        KET_THUC
    }

    public void update() {
        long now = System.currentTimeMillis();
        long timeStartHour = TimeUtil.getTimeStartOfHour(now);
        ChungSinhBinhDang csbd = ChungSinhBinhDang.gI();

        xuLyDangKy(csbd, now, timeStartHour);

        if (csbd.getTrangThai() != TrangThaiTran.KET_THUC) {
            xuLyTranChung(csbd, now, timeStartHour);
            xuLyLienTucTrongTran(csbd, now, timeStartHour);
        }

    }

    private void xuLyDangKy(ChungSinhBinhDang csbd, long now, long startHour) {
        if (now >= TimeUtil.timeAt(PHUT_DANG_KY_BAT_DAU, startHour)
                && now < TimeUtil.timeAt(PHUT_DANG_KY_KET_THUC, startHour)) {
            if (csbd.getTrangThai() != TrangThaiTran.CHO_DANG_KY) {
                csbd.setTrangThai(TrangThaiTran.CHO_DANG_KY);
                Service.getInstance().sendThongBaoAllPlayer("Sự kiện Chúng Sinh Bình Đẳng đã mở đăng ký! Tham gia ngay!");
            }
        }
    }

    private void xuLyTranChung(ChungSinhBinhDang csbd, long now, long startHour) {
        if (now >= TimeUtil.timeAt(PHUT_CHUAN_BI_BAT_DAU, startHour)
                && now < TimeUtil.timeAt(PHUT_CHUAN_BI_KET_THUC, startHour)) {
            if (csbd.getTrangThai() != TrangThaiTran.CHUAN_BI) {
                csbd.batDauChuanBi();
                csbd.setTrangThai(TrangThaiTran.CHUAN_BI);
            }
            // Chạy liên tục từ phút PHUT_CHUAN_BI_BAT_DAU đến PHUT_CHUAN_BI_KET_THUC
            csbd.xuLyTrongThoiGianChuanBi(now, TimeUtil.timeAt(PHUT_CHUAN_BI_KET_THUC, startHour)); // Liên tục
        } else if (now >= TimeUtil.timeAt(PHUT_TRAN_DAU_BAT_DAU, startHour)
                && now < TimeUtil.timeAt(PHUT_DOI_CO, startHour)) {
            if (csbd.getTrangThai() != TrangThaiTran.DA_BAT_DAU) {
                csbd.batDauTranDau();
                csbd.setTrangThai(TrangThaiTran.DA_BAT_DAU);
            }
        } else if (now >= TimeUtil.timeAt(PHUT_DOI_CO, startHour)
                && now < TimeUtil.timeAt(PHUT_BAT_DAU_THU_BO, startHour)) {
            if (csbd.getTrangThai() != TrangThaiTran.DOI_CO) {
                csbd.ChangeFlagAll();
                csbd.setTrangThai(TrangThaiTran.DOI_CO);
            }
        } else if (now >= TimeUtil.timeAt(PHUT_TRAN_DAU_KET_THUC, startHour)) {
            if (csbd.getTrangThai() != TrangThaiTran.KET_THUC) {
                csbd.ketThucTranDau();
                csbd.setTrangThai(TrangThaiTran.KET_THUC);
            }
        }
    }

    private void xuLyLienTucTrongTran(ChungSinhBinhDang csbd, long now, long startHour) {
        // Chạy liên tục từ phút PHUT_BAT_DAU_THU_BO đến PHUT_TRAN_DAU_KET_THUC
        if (now >= TimeUtil.timeAt(PHUT_BAT_DAU_THU_BO, startHour)
                && now < TimeUtil.timeAt(PHUT_TRAN_DAU_KET_THUC, startHour)) {
            csbd.xuLyVongBo();
            csbd.xuLyTruMauBoChoToanBo();
        }
        // Chạy liên tục từ phút PHUT_TRAN_DAU_BAT_DAU đến PHUT_TRAN_DAU_KET_THUC
        if (now >= TimeUtil.timeAt(PHUT_TRAN_DAU_BAT_DAU, startHour)
                && now < TimeUtil.timeAt(PHUT_TRAN_DAU_KET_THUC, startHour)) {
            csbd.xuLyRoiItem();
            csbd.kiemTraNguoiSongSot();
        }
    }

    public int getTongThoiGianTranMinute() {
        return PHUT_TRAN_DAU_KET_THUC - PHUT_TRAN_DAU_BAT_DAU;
    }

}
