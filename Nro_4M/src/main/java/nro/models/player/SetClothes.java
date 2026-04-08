package nro.models.player;

import nro.models.item.Item;
import nro.models.item.ItemOption;

/**
 *
 * @author Hoàng Việt - 0857853150
 * @copyright 💖 GirlkuN 💖
 *
 */
public class SetClothes {

    private Player player;

    public SetClothes(Player player) {
        this.player = player;
    }

    public byte nhanhoangKM;
    public byte nhanhoangKOK;
    public byte mahoangLH;
//    public byte mahoangKI;
//    public byte thienhoangGL;
//    public byte thienhoangHP;

    public byte nhanhoangKM_1;
    public byte nhanhoangKOK_1;
    public byte mahoangLH_1;
    public byte mahoangKI_1;
    public byte thienhoangGL_1;
    public byte thienhoangHP_1;
    public byte nhanhoangdbKame = 0;
    public byte nhanhoangdbKaioken = 0;
    public byte mahoangdbLienHoan = 0;
    public byte mahoangdbKI = 0;
    public byte thienhoangdbGL = 0;
    public byte thienhoangdbHP = 0;

    public byte songoku;
    public byte thienXinHang;
    public byte kirin;

    public byte ocTieu;
    public byte pikkoroDaimao;
    public byte picolo;

    public byte kakarot;
    public byte cadic;
    public byte nappa;

    public byte tinhan;
    public byte nguyetan;
    public byte nhatan;

    public byte setDHD;
    public byte setDTS;
    public byte setDTL;
    public byte setDTT;

    public byte Setbilltocdanh;
    public byte SetbilltocChuong;
    public byte SetbillSdcm;

    public int ctHaiTac = -1;

    public void setup() {
        setDefault();
        setupSKT();
        setupSKThoangkim();
        setupAN();
        setupDTS();
        setupDHD();
        setupDTL();
        setupThanhTon();
        setupSKHBilltocdanh();
        setupSKHBillchuong();
        setupSKHBillsdcm();
        Item ct = this.player.inventory.itemsBody.get(5);
        if (ct.isNotNullItem()) {
            switch (ct.template.id) {
                case 618:
                case 619:
                case 620:
                case 621:
                case 622:
                case 623:
                case 624:
                case 626:
                case 627:
                    this.ctHaiTac = ct.template.id;
                    break;
            }
        }
    }

    private void setupSKT() {
        for (int i = 0; i < 5; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                boolean isActSet = false;
                for (ItemOption io : item.itemOptions) {
                    switch (io.optionTemplate.id) {
                        case 129:
                        case 141:
                            isActSet = true;
                            songoku++;
                            break;
                        case 127:
                        case 139:
                            isActSet = true;
                            thienXinHang++;
                            break;
                        case 128:
                        case 140:
                            isActSet = true;
                            kirin++;
                            break;
                        case 131:
                        case 143:
                            isActSet = true;
                            ocTieu++;
                            break;
                        case 132:
                        case 144:
                            isActSet = true;
                            pikkoroDaimao++;
                            break;
                        case 130:
                        case 142:
                            isActSet = true;
                            picolo++;
                            break;
                        case 135:
                        case 138:
                            isActSet = true;
                            nappa++;
                            break;
                        case 133:
                        case 136:
                            isActSet = true;
                            kakarot++;
                            break;
                        case 134:
                        case 137:
                            isActSet = true;
                            cadic++;
                            break;
                    }
                    if (isActSet) {
                        break;
                    }
                }
            } else {
                break;
            }
        }
    }

    private void setupSKThoangkim() {
        for (int i = 0; i < 5; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                boolean isActSet = false;
                for (ItemOption io : item.itemOptions) {
                    switch (io.optionTemplate.id) {
                        // Set 200%
                        case 223:
                            isActSet = true;
                            nhanhoangKM_1++;
                            break;
                        case 224:
                            isActSet = true;
                            nhanhoangKOK_1++;
                            break;
                        case 225:
                            isActSet = true;
                            mahoangLH_1++;
                            break;
                        case 226:
                            isActSet = true;
                            mahoangKI_1++;
                            break;
                        case 227:
                            isActSet = true;
                            thienhoangGL_1++;
                            break;
                        case 228:
                            isActSet = true;
                            thienhoangHP_1++;
                            break;

                        // Set 700% đặc biệt
                        case 41: // (5 món tăng 700% Sát thương Kamejoko)
                            isActSet = true;
                            nhanhoangdbKame++;
                            break;
                        case 42: // (5 món tăng 700% Sát thương Kaioken)
                            isActSet = true;
                            nhanhoangdbKaioken++;
                            break;
                        case 43: // (5 món tăng 700% Sát thương Liên hoàn)
                            isActSet = true;
                            mahoangdbLienHoan++;
                            break;
                        case 44: // (5 món tăng 700% KI)
                            isActSet = true;
                            mahoangdbKI++;
                            break;
                        case 45: // (5 món tăng 700% Sát thương Galick)
                            isActSet = true;
                            thienhoangdbGL++;
                            break;
                        case 46: // (5 món tăng 700% HP)
                            isActSet = true;
                            thienhoangdbHP++;
                            break;
                    }
                    if (isActSet) {
                        break;
                    }
                }
            }
        }
    }

    private void setupAN() {
        for (int i = 0; i < 5; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                boolean isActSett = false;
                for (ItemOption io : item.itemOptions) {
                    switch (io.optionTemplate.id) {
                        case 34:
                            isActSett = true;
                            tinhan++;
                            break;
                        case 35:
                            isActSett = true;
                            nguyetan++;
                            break;
                        case 36:
                            isActSett = true;
                            nhatan++;
                            break;
                    }
                    if (isActSett) {
                        break;
                    }

                }
            } else {
                break;
            }
        }
    }

    private void setupDTS() {
        for (int i = 0; i < 5; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                boolean isActSet = false;
                if (item.template.id >= 1048 && item.template.id <= 1062) {
                    setDTS++;
                }
                if (isActSet) {
                    break;
                }
            } else {
                break;
            }
        }
    }

    private void setupSKHBilltocdanh() {
        for (int i = 0; i < 5; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                boolean isActSet = false;
                for (ItemOption io : item.itemOptions) {
                    switch (io.optionTemplate.id) {
                        case 214:
                            if (io.param == 1) {
                                Setbilltocdanh++;
                            }
                            break;
                    }
                    if (isActSet) {
                        break;
                    }

                }
            } else {
                break;
            }
        }
    }

    private void setupSKHBillchuong() {
        for (int i = 0; i < 5; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                boolean isActSet = false;
                for (ItemOption io : item.itemOptions) {
                    switch (io.optionTemplate.id) {
                        case 216:
                            if (io.param == 1) {
                                SetbilltocChuong++;
                            }
                            break;
                    }
                    if (isActSet) {
                        break;
                    }

                }
            } else {
                break;
            }
        }
    }

    private void setupSKHBillsdcm() {
        for (int i = 0; i < 5; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                boolean isActSet = false;
                for (ItemOption io : item.itemOptions) {
                    switch (io.optionTemplate.id) {
                        case 215:
                            if (io.param == 1) {
                                SetbillSdcm++;
                            }
                            break;
                    }
                    if (isActSet) {
                        break;
                    }

                }
            } else {
                break;
            }
        }
    }

    private void setupThanhTon() {
        for (int i = 0; i < 5; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                boolean isActSet = false;
                switch (item.template.id) {
                    case 1401:
                    case 1402:
                    case 1403:
                    case 1404:
                    case 1405:
                        setDTT++;
                        break;
                }
                if (isActSet) {
                    break;
                }
            } else {
                break;
            }
        }
    }

    private void setupDHD() {
        for (int i = 0; i < 5; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                boolean isActSet = false;
                for (ItemOption io : item.itemOptions) {
                    switch (io.optionTemplate.id) {
                        case 21:
                            if (io.param == 80) {
                                setDHD++;
                            }
                            break;
                    }
                    if (isActSet) {
                        break;
                    }

                }
            } else {
                break;
            }
        }
    }

    private void setupDTL() {
        for (int i = 0; i < 5; i++) {
            Item item = this.player.inventory.itemsBody.get(i);
            if (item.isNotNullItem()) {
                boolean isActSet = false;
                switch (item.template.id) {
                    case 555:
                    case 556:
                    case 557:
                    case 558:
                    case 559:
                    case 560:
                    case 561:
                    case 562:
                    case 563:
                    case 564:
                    case 565:
                    case 566:
                    case 567:
                        setDTL++;
                        break;
                }
                if (isActSet) {
                    break;
                }
            } else {
                break;
            }
        }
    }

    private void setDefault() {
        this.songoku = 0;
        this.thienXinHang = 0;
        this.kirin = 0;
        this.ocTieu = 0;
        this.pikkoroDaimao = 0;
        this.picolo = 0;
        this.kakarot = 0;
        this.cadic = 0;
        this.nappa = 0;
        this.ctHaiTac = -1;

        this.tinhan = 0;
        this.nhatan = 0;
        this.nguyetan = 0;

        this.setDHD = 0;
        this.setDTS = 0;
        this.setDTL = 0;
        this.setDTT = 0;

        this.nhanhoangKM = 0;
        this.nhanhoangKOK = 0;
        this.mahoangLH = 0;

        this.nhanhoangKM_1 = 0;
        this.nhanhoangKOK_1 = 0;
        this.mahoangLH_1 = 0;
        this.mahoangKI_1 = 0;
        this.thienhoangGL_1 = 0;
        this.thienhoangHP_1 = 0;
        this.nhanhoangdbKame = 0;
        this.nhanhoangdbKaioken = 0;
        this.mahoangdbLienHoan = 0;
        this.mahoangdbKI = 0;
        this.thienhoangdbGL = 0;
        this.thienhoangdbHP = 0;

        this.Setbilltocdanh = 0;
        this.SetbilltocChuong = 0;
        this.SetbillSdcm = 0;
    }

    public void dispose() {
        this.player = null;
    }
}
