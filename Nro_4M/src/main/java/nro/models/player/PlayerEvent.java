package nro.models.player;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PlayerEvent {

    private Player player;
    private boolean isUseQuanHoa;
    private boolean isUseBonTam;
    private boolean isUseCaiTrangQuy;
    private boolean cookingChungCake;
    private boolean cookingTetCake;
    private boolean receivedLuckyMoney;

    private int diemTichLuy;
    private int mocNapDaNhan;
    private int eventPoint;
    private int timeCookTetCake;
    private int timeCookChungCake;
    public int Diem_Su_Kien;
    public int Tieu_Phi_Cloud_Sky;

    public PlayerEvent(Player player) {
        this.player = player;
    }

    public void setTimeCookChungCake(int sec) {
        timeCookChungCake += sec;
    }

    public void setTimeCookTetCake(int sec) {
        timeCookTetCake += sec;
    }

    public void addEventPoint(int num) {
        eventPoint += num;
    }

    public void subEventPoint(int num) {
        eventPoint -= num;
    }

    public void addEventPointSK(int num) {
        Diem_Su_Kien += num;
    }

    public void addEventPointTP(int num) {
        Tieu_Phi_Cloud_Sky += num;
    }

    public void update() {
        if (timeCookChungCake > 0) {
            timeCookChungCake--;
        }
        if (timeCookTetCake > 0) {
            timeCookTetCake--;
        }
    }

}
