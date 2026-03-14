package nro.models.player;

import nro.services.Service;

public class Charms {

    private Player player;

    public Charms(Player player) {
        this.player = player;
    }

    public long tdTriTue;
    public long tdManhMe;
    public long tdDaTrau;
    public long tdOaiHung;
    public long tdBatTu;
    public long tdDeoDai;
    public long tdThuHut;
    public long tdDeTu;
    public long tdDeTuMabu;
    public long tdTriTue3;
    public long tdTriTue4;

    public long lastTimeSubMinTriTueX4;

    public void addTimeCharms(int itemId, int min) {
        switch (itemId) {
            case 213:
                if (tdTriTue < System.currentTimeMillis()) {
                    tdTriTue = System.currentTimeMillis();
                }
                tdTriTue += min * 60 * 1000L;
                break;
            case 214:
                if (tdManhMe < System.currentTimeMillis()) {
                    tdManhMe = System.currentTimeMillis();
                }
                tdManhMe += min * 60 * 1000L;
                break;
            case 215:
                if (tdDaTrau < System.currentTimeMillis()) {
                    tdDaTrau = System.currentTimeMillis();
                }
                tdDaTrau += min * 60 * 1000L;
                break;
            case 216:
                if (tdOaiHung < System.currentTimeMillis()) {
                    tdOaiHung = System.currentTimeMillis();
                }
                tdOaiHung += min * 60 * 1000L;
                break;
            case 217:
                if (tdBatTu < System.currentTimeMillis()) {
                    tdBatTu = System.currentTimeMillis();
                }
                tdBatTu += min * 60 * 1000L;
                break;
            case 218:
                if (tdDeoDai < System.currentTimeMillis()) {
                    tdDeoDai = System.currentTimeMillis();
                }
                tdDeoDai += min * 60 * 1000L;
                break;
            case 219:
                if (tdThuHut < System.currentTimeMillis()) {
                    tdThuHut = System.currentTimeMillis();
                }
                tdThuHut += min * 60 * 1000L;
                break;
            case 522:
                if (tdDeTu < System.currentTimeMillis()) {
                    tdDeTu = System.currentTimeMillis();
                }
                tdDeTu += min * 60 * 1000L;
                break;
            case 671:
                if (tdTriTue3 < System.currentTimeMillis()) {
                    tdTriTue3 = System.currentTimeMillis();
                }
                tdTriTue3 += min * 60 * 1000L;
                break;
            case 672:
                if (tdTriTue4 < System.currentTimeMillis()) {
                    tdTriTue4 = System.currentTimeMillis();
                }
                tdTriTue4 += min * 60 * 1000L;
                break;
            case 2025:
                if (tdDeTuMabu < System.currentTimeMillis()) {
                    tdDeTuMabu = System.currentTimeMillis();
                }
                tdDeTuMabu += min * 60 * 1000L;
                break;
        }
    }

    public void removeCharmBySelect(int select) {
        switch (select) {
            case 0 -> {
                tdTriTue = 0;
                Service.getInstance().sendThongBao(player, "Đã xóa hiệu ứng Bùa Trí Tuệ thành công!");
            }
            case 1 -> {
                tdManhMe = 0;
                Service.getInstance().sendThongBao(player, "Đã xóa hiệu ứng Bùa Mạnh Mẽ thành công!");
            }
            case 2 -> {
                tdDaTrau = 0;
                Service.getInstance().sendThongBao(player, "Đã xóa hiệu ứng Bùa Da Trâu thành công!");
            }
            case 3 -> {
                tdOaiHung = 0;
                Service.getInstance().sendThongBao(player, "Đã xóa hiệu ứng Bùa Oai Hùng thành công!");
            }
            case 4 -> {
                tdBatTu = 0;
                Service.getInstance().sendThongBao(player, "Đã xóa hiệu ứng Bùa Bất Tử thành công!");
            }
            case 5 -> {
                tdDeoDai = 0;
                Service.getInstance().sendThongBao(player, "Đã xóa hiệu ứng Bùa Dẻo Dai thành công!");
            }
            case 6 -> {
                tdThuHut = 0;
                Service.getInstance().sendThongBao(player, "Đã xóa hiệu ứng Bùa Thu Hút thành công!");
            }
            case 7 -> {
                tdDeTu = 0;
                Service.getInstance().sendThongBao(player, "Đã xóa hiệu ứng Bùa Đệ Tử thành công!");
            }
            case 8 -> {
                tdTriTue3 = 0;
                Service.getInstance().sendThongBao(player, "Đã xóa hiệu ứng Bùa Trí Tuệ x3 thành công!");
            }
            case 9 -> {
                tdTriTue4 = 0;
                Service.getInstance().sendThongBao(player, "Đã xóa hiệu ứng Bùa Trí Tuệ x4 thành công!");
            }
            case 10 -> {
                tdDeTuMabu = 0;
                Service.getInstance().sendThongBao(player, "Đã xóa hiệu ứng Bùa Đệ Tử (Mabu/Berus/Zeno) thành công!");
            }
            case 11 -> {
                clearAllCharms();
                Service.getInstance().sendThongBao(player, "Đã xóa tất cả các hiệu ứng bùa thành công!");
            }
            default ->
                Service.getInstance().sendThongBao(player, "Không xác định được loại bùa để xóa!");
        }
    }

    private void clearAllCharms() {
        tdTriTue = 0;
        tdManhMe = 0;
        tdDaTrau = 0;
        tdOaiHung = 0;
        tdBatTu = 0;
        tdDeoDai = 0;
        tdThuHut = 0;
        tdDeTu = 0;
        tdTriTue3 = 0;
        tdTriTue4 = 0;
        tdDeTuMabu = 0;
    }

    public void dispose() {
        this.player = null;
    }
}
