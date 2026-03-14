package nro.services;

import nro.consts.ConstPlayer;
import nro.data.DataGame;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.map.phoban.BanDoKhoBau;
import nro.models.map.phoban.DoanhTrai;
import nro.models.player.Fusion;
import nro.models.player.Player;
import nro.server.io.Message;
import nro.utils.Log;

import static nro.models.item.ItemTime.*;
import static nro.models.item.ItemTimeSieuCap.*;
import nro.models.map.ChungSinhBinhDang.ChungSinhBinhDang;
import nro.models.map.ChungSinhBinhDang.ChungSinhBinhDangService;
import nro.utils.TimeUtil;

/**
 *
 * @author Hoàng Việt - 0857853150
 * @copyright 💖 GirlkuN 💖
 *
 */
public class ItemTimeService {

    private static ItemTimeService i;

    public static ItemTimeService gI() {
        if (i == null) {
            i = new ItemTimeService();
        }
        return i;
    }

    //gửi cho client
    public void sendAllItemTime(Player player) {
        sendTextRongSieuCap(player);
        sendTextRongBang(player);
        sendTextDoanhTrai(player);
        sendTextBanDoKhoBau(player);
        sendTextRongHoangKim(player);
        sendTextRongVutrusd(player);
        sendTextRongVutruhp(player);
        sendTextRongVutruki(player);
        sendTextRongVutrucm(player);
        sendTextRongVutrutnsm(player);
        sendTextSuPhuSC(player);
        sendTextDeTuSC(player);
        if (player.fusion.typeFusion == ConstPlayer.LUONG_LONG_NHAT_THE) {
            sendItemTime(player, player.gender == ConstPlayer.NAMEC ? 3901 : 3790,
                    (int) ((Fusion.TIME_FUSION - (System.currentTimeMillis() - player.fusion.lastTimeFusion)) / 1000));
        }
        if (player.useCanCau) {
            sendItemTime(player, player.iconCancau, (int) ((player.timeCauCa - (System.currentTimeMillis() - player.lasttimeCanCau)) / 1000));
        }
        if (player.dangGhepAn) {
            sendItemTime(player, player.iconGhepAn, (int) ((player.timeGhepAn - (System.currentTimeMillis() - player.lasttimeGhepAn)) / 1000));
        }
        if (player.itemTime.isUseBoHuyet) {
            sendItemTime(player, 2755, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBoHuyet)) / 1000));
        }
        if (player.itemTime.isUseBoKhi) {
            sendItemTime(player, 2756, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBoKhi)) / 1000));
        }
        if (player.itemTime.isUseGiapXen) {
            sendItemTime(player, 2757, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeGiapXen)) / 1000));
        }
        if (player.itemTime.isUseCuongNo) {
            sendItemTime(player, 2754, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeCuongNo)) / 1000));
        }
        if (player.itemTime.isUseAnDanh) {
            sendItemTime(player, 2760, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeAnDanh)) / 1000));
        }
        if (player.itemTime.isOpenPower) {
            sendItemTime(player, 3783, (int) ((TIME_OPEN_POWER - (System.currentTimeMillis() - player.itemTime.lastTimeOpenPower)) / 1000));
        }
        if (player.itemTime.isUseMayDo) {
            sendItemTime(player, 2758, (int) ((TIME_MAY_DO - (System.currentTimeMillis() - player.itemTime.lastTimeUseMayDo)) / 1000));
        }
        if (player.itemTime.isUseVotCaThuong) {
            sendItemTime(player, 9662, (int) ((TIME_MAY_DO - (System.currentTimeMillis() - player.itemTime.lastTimeUseVotCaThuong)) / 1000));
        }
        if (player.itemTime.isUseVotCaVip) {
            sendItemTime(player, 9663, (int) ((TIME_MAY_DO - (System.currentTimeMillis() - player.itemTime.lastTimeUseVotCaVip)) / 1000));
        }
        if (player.itemTime.isEatMeal) {
            sendItemTime(player, player.itemTime.iconMeal, (int) ((TIME_EAT_MEAL - (System.currentTimeMillis() - player.itemTime.lastTimeEatMeal)) / 1000));
        }
        if (player.itemTime.isUseBanhChung) {
            sendItemTime(player, 15528, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBanhChung)) / 1000));
        }
        if (player.itemTime.isUseBanhTet) {
            sendItemTime(player, 15526, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBanhTet)) / 1000));
        }
        if (player.itemTime.isUseBoHuyet2) {
            sendItemTime(player, 10714, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBoHuyet2)) / 1000));
        }
        if (player.itemTime.isUseBoKhi2) {
            sendItemTime(player, 10715, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeBoKhi2)) / 1000));
        }
        if (player.itemTime.isUseGiapXen2) {
            sendItemTime(player, 10712, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeGiapXen2)) / 1000));
        }
        if (player.itemTime.isUseCuongNo2) {
            sendItemTime(player, 10716, (int) ((TIME_ITEM - (System.currentTimeMillis() - player.itemTime.lastTimeCuongNo2)) / 1000));
        }
        if (player.itemTimesieucap.isDaNgucTu) {
            sendItemTime(player, 11274, (int) ((TIME_ITEM_SC_10P - (System.currentTimeMillis() - player.itemTimesieucap.lastTimeDaNgucTu)) / 1000));
        }
        if (player.itemTimesieucap.isDuoikhi) {
            sendItemTime(player, 5072, (int) ((TIME_ITEM_SC_10P - (System.currentTimeMillis() - player.itemTimesieucap.lastTimeDuoikhi)) / 1000));
        }
        if (player.itemTimesieucap.isUseCaRot) {
            sendItemTime(player, 16128, (int) ((TIME_ITEM_SC_10P - (System.currentTimeMillis() - player.itemTimesieucap.lastTimeCaRot)) / 1000));
        }
        if (player.itemTimesieucap.isKeo) {
            sendItemTime(player, 8243, (int) ((TIME_ITEM_SC_30P - (System.currentTimeMillis() - player.itemTimesieucap.lastTimeKeo)) / 1000));
        }
        if (player.itemTimesieucap.isUseXiMuoi) {
            sendItemTime(player, 10904, (int) ((TIME_ITEM_SC_10P - (System.currentTimeMillis() - player.itemTimesieucap.lastTimeUseXiMuoi)) / 1000));
        }
        if (player.itemTimesieucap.isUseTrungThu) {
            sendItemTime(player, player.itemTimesieucap.iconBanh, (int) ((TIME_TRUNGTHU - (System.currentTimeMillis() - player.itemTimesieucap.lastTimeUseBanh)) / 1000));
        }
        if (player.itemTimesieucap.isCuongBao) {
            sendItemTime(player, 22071, (int) ((TIME_ITEM_SC_10P - (System.currentTimeMillis() - player.itemTimesieucap.lastTimeCuongBao)) / 1000));
        }
        if (player.effectSkill != null && player.effectSkill.isBienHinh) {
            sendItemTime(player, DataGame.ICON_BIENHINH[player.gender][player.effectSkill.levelBienHinh - 1], (int) ((player.effectSkill.timeBienHinh - (System.currentTimeMillis() - player.effectSkill.lastTimeUpBienHinh)) / 1000));
        }
        if (player.itemTimesieucap.isTraiDua) {
            sendItemTime(player, 6574, (int) ((TIME_ITEM_SC_30P - (System.currentTimeMillis() - player.itemTimesieucap.lastTimeTraiDua)) / 1000));
        }
    }

    //bật tđlt
    public void turnOnTDLT(Player player, Item item) {
        int min = 0;
        for (ItemOption io : item.itemOptions) {
            if (io.optionTemplate.id == 1) {
                min = io.param;
                io.param = 0;
                break;
            }
        }
        player.itemTime.isUseTDLT = true;
        player.itemTime.timeTDLT = min * 60 * 1000;
        player.itemTime.lastTimeUseTDLT = System.currentTimeMillis();
        sendCanAutoPlay(player);
        sendItemTime(player, 4387, player.itemTime.timeTDLT / 1000);
        InventoryService.gI().sendItemBags(player);
    }

    //tắt tđlt
    public void turnOffTDLT(Player player, Item item) {
        player.itemTime.isUseTDLT = false;
        for (ItemOption io : item.itemOptions) {
            if (io.optionTemplate.id == 1) {
                io.param = (short) ((player.itemTime.timeTDLT - (System.currentTimeMillis() - player.itemTime.lastTimeUseTDLT)) / 60 / 1000);
                break;
            }
        }
        sendCanAutoPlay(player);
        removeItemTime(player, 4387);
        InventoryService.gI().sendItemBags(player);
    }

    public void sendCanAutoPlay(Player player) {
        Message msg;
        try {
            msg = new Message(-116);
            msg.writer().writeByte(player.itemTime.isUseTDLT ? 1 : 0);
            player.sendMessage(msg);
        } catch (Exception e) {
            Log.error(ItemTimeService.class, e);
        }
    }

    public void sendTextRongBang(Player player) {
        if (player.itemTimesieucap.isRongBang) {
            int secondsLeft = (int) ((TIME_ITEM_SC_30P - (System.currentTimeMillis() - player.itemTimesieucap.lastTimeRongBang)) / 1000);
            sendTextTime(player, RONG_BANG, "Rồng Băng: +30%HP,KI,SD còn", secondsLeft);
        }
    }

    public void sendTextRongSieuCap(Player player) {
        if (player.itemTimesieucap.isRongSieuCap) {
            int secondsLeft = (int) ((TIME_ITEM_SC_30P - (System.currentTimeMillis() - player.itemTimesieucap.lastTimeRongSieuCap)) / 1000);
            sendTextTime(player, RONG_SIEU_CAP, "Rồng Siêu Cấp: +50%HP,KI,SD còn", secondsLeft);
        }
    }

    public void sendTextRongHoangKim(Player player) {
        if (player.itemTimesieucap.isRongHoangKim) {
            int secondsLeft = (int) ((TIME_ITEM_SC_1_DAY - (System.currentTimeMillis() - player.itemTimesieucap.lastTimeRongHoangKim)) / 1000);
            sendTextTime(player, RONG_HOANG_KIM, "Rồng Hoàng Kim: +200%HP,KI,SD còn", secondsLeft);
        }
    }

    public void sendTextRongVutrusd(Player player) {
        if (player.itemTimesieucap.isRongVutrusd) {
            int secondsLeft = (int) ((TIME_ITEM_SC_30P - (System.currentTimeMillis() - player.itemTimesieucap.lastTimeRongVutrusd)) / 1000);
            sendTextTime(player, RONG_VU_TRU, "Rồng Vũ Trụ: +50% SĐ còn", secondsLeft);
        }
    }

    public void sendTextRongVutruhp(Player player) {
        if (player.itemTimesieucap.isRongVutruhp) {
            int secondsLeft = (int) ((TIME_ITEM_SC_30P - (System.currentTimeMillis() - player.itemTimesieucap.lastTimeRongVutruhp)) / 1000);
            sendTextTime(player, RONG_VU_TRU, "Rồng Vũ Trụ: +50% HP còn", secondsLeft);
        }
    }

    public void sendTextRongVutruki(Player player) {
        if (player.itemTimesieucap.isRongVutruki) {
            int secondsLeft = (int) ((TIME_ITEM_SC_30P - (System.currentTimeMillis() - player.itemTimesieucap.lastTimeRongVutruki)) / 1000);
            sendTextTime(player, RONG_VU_TRU, "Rồng Vũ Trụ: +50% KI còn", secondsLeft);
        }
    }

    public void sendTextRongVutrucm(Player player) {
        if (player.itemTimesieucap.isRongVutrucm) {
            int secondsLeft = (int) ((TIME_ITEM_SC_30P - (System.currentTimeMillis() - player.itemTimesieucap.lastTimeRongVutrucm)) / 1000);
            sendTextTime(player, RONG_VU_TRU, "Rồng Vũ Trụ: +30% Chí Mạng còn", secondsLeft);
        }
    }

    public void sendTextRongVutrutnsm(Player player) {
        if (player.itemTimesieucap.isRongVutrutnsm) {
            int secondsLeft = (int) ((TIME_ITEM_SC_30P - (System.currentTimeMillis() - player.itemTimesieucap.lastTimeRongVutrutnsm)) / 1000);
            sendTextTime(player, RONG_VU_TRU, "Rồng Vũ Trụ: +500% TN, SM còn", secondsLeft);
        }
    }

    public void sendTextDoanhTrai(Player player) {
        if (player.clan != null && !player.clan.haveGoneDoanhTrai
                && player.clan.timeOpenDoanhTrai != 0) {
            int secondPassed = (int) ((System.currentTimeMillis() - player.clan.timeOpenDoanhTrai) / 1000);
            int secondsLeft = (DoanhTrai.TIME_DOANH_TRAI / 1000) - secondPassed;
            sendTextTime(player, DOANH_TRAI, "Doanh trại độc nhãn", secondsLeft);
        }
    }

    public void sendTextBanDoKhoBau(Player player) {
        if (player.clan != null
                && player.clan.timeOpenBanDoKhoBau != 0) {
            int secondPassed = (int) ((System.currentTimeMillis() - player.clan.timeOpenBanDoKhoBau) / 1000);
            int secondsLeft = (BanDoKhoBau.TIME_BAN_DO_KHO_BAU / 1000) - secondPassed;
            sendTextTime(player, BAN_DO_KHO_BAU, "Bản đồ kho báu", secondsLeft);
        }
    }

    public void sendTextSuPhuSC(Player player) {
        if (player.itemTimesieucap.isSuPhuSC) {
            int secondsLeft = (int) ((TIME_ITEM_SC_10P - (System.currentTimeMillis() - player.itemTimesieucap.lastTimeSuPhuSC)) / 1000);
            sendTextTime(player, SU_PHU_SC, "100% SD.HP.KI Sư Phụ còn", secondsLeft);
        }
    }

    public void sendTextDeTuSC(Player player) {
        if (player.itemTimesieucap.isDeTuSC) {
            int secondsLeft = (int) ((TIME_ITEM_SC_10P - (System.currentTimeMillis() - player.itemTimesieucap.lastTimeDeTuSC)) / 1000);
            sendTextTime(player, DE_TU_SC, "100% SD.HP.KI Đệ Tử còn", secondsLeft);
        }
    }

    public void sendTextChungSinhBinhDang(Player player) {
        ChungSinhBinhDangService.TrangThaiTran trangThai = ChungSinhBinhDang.gI().getTrangThai();
        if (trangThai != ChungSinhBinhDangService.TrangThaiTran.KET_THUC) {
            ChungSinhBinhDang csbd = ChungSinhBinhDang.gI();
            long now = System.currentTimeMillis();
            long timeStart = csbd.getThoiGianBatDau();
            int timeMatchMinutes = ChungSinhBinhDangService.gI().getTongThoiGianTranMinute();
            long totalMatchMillis = timeMatchMinutes * 60_000L;
            long elapsedMillis = now - timeStart;
            long timeLeftMillis = Math.max(totalMatchMillis - elapsedMillis, 0);
            int timeLeftMinutes = (int) (timeLeftMillis / 60_000);
            int timeLeftSeconds = (int) ((timeLeftMillis % 60_000) / 1000);
            int songSot = csbd.getNguoiSongSot().size();
            int soLanBo = csbd.getSoLanBo();
            int tongSoLanBo = csbd.getTongSoLanBo();
            String text = "Còn lại: " + songSot + " người\n"
                    + "Vòng bo: " + soLanBo + "/" + tongSoLanBo + "\n"
                    + "Thời gian còn lại: ";
            sendTextTime(player, CHUNG_SINH_BINH_DANG, text, timeLeftMinutes * 60 + timeLeftSeconds);
        }
    }

    public void removeTextDoanhTrai(Player player) {
        removeTextTime(player, DOANH_TRAI);
    }

    public void removeTextTime(Player player, byte id) {
        sendTextTime(player, id, "", 0);
    }

    private void sendTextTime(Player player, byte id, String text, int seconds) {
        Message msg;
        try {
            msg = new Message(65);
            msg.writer().writeByte(id);
            msg.writer().writeUTF(text);
            msg.writer().writeShort(seconds);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void sendItemTime(Player player, int itemId, int time) {
        Message msg;
        try {
            msg = new Message(-106);
            msg.writer().writeShort(itemId);
            msg.writer().writeShort(time);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    public void removeItemTime(Player player, int itemTime) {
        sendItemTime(player, itemTime, 0);
    }

}
