package nro.models.player;

import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.map.ChungSinhBinhDang.ChungSinhBinhDangService;

public class NPointClone extends NPoint {

    public double buffDame = 0;
    public double buffHp = 0;
    public double buffMp = 0;

    public NPointClone(Player player) {
        super(player);
        //Set chỉ số cơ bản vào map
        this.hpMax = ChungSinhBinhDangService.HP;
        this.hp = this.hpMax;
        this.mpMax = ChungSinhBinhDangService.KI;
        this.mp = this.mpMax;
        this.dame = ChungSinhBinhDangService.SUC_DANH;
        this.def = ChungSinhBinhDangService.GIAP;
        this.crit = ChungSinhBinhDangService.CHI_MANG;
        //Set chỉ số cũ tránh lỗi
        this.limitPower = player.nPoint.limitPower;
        this.power = player.nPoint.power;
        this.tiemNang = player.nPoint.tiemNang;
        this.stamina = player.nPoint.stamina;
        this.maxStamina = player.nPoint.maxStamina;
        this.hpg = player.nPoint.hpg;
        this.mpg = player.nPoint.mpg;
        this.dameg = player.nPoint.dameg;
        this.defg = player.nPoint.defg;
        this.critg = player.nPoint.critg;
        this.speed = player.nPoint.speed;
    }

    @Override
    public void calPoint() {
        try {
            this.setPointWhenWearClothes();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void setPointWhenWearClothes() {
        resetPoint();
        for (Item item : this.player.inventory.itemsBodyTemp) {
            if (item.isNotNullItem()) {
                for (ItemOption io : item.itemOptions) {
                    setPoint(io);
                }
            }
        }
        setBasePoint();
    }

    @Override
    protected void setHpMax() {
        this.hpMax = ChungSinhBinhDangService.HP + this.buffHp;
        this.mpMax += this.mpAdd;
        //đồ
        for (Integer tl : this.tlHp) {
            this.hpMax += calPercent(this.hpMax, tl);
        }
    }

    @Override
    protected void setMpMax() {
        this.mpMax = ChungSinhBinhDangService.KI + this.buffMp;
        this.mpMax += this.mpAdd;
        //đồ
        for (Integer tl : this.tlMp) {
            this.mpMax += calPercent(this.mpMax, tl);
        }
    }

    @Override
    protected void setDame() {
        this.dame = ChungSinhBinhDangService.SUC_DANH + this.buffDame;
        this.dame += this.dameAdd;
        //đồ
        for (Integer tl : this.tlDame) {
            this.dame += calPercent(this.dame, tl);
        }
    }

    @Override
    protected void setDef() {
        this.def = ChungSinhBinhDangService.GIAP;
        this.def += this.defAdd;
        //đồ
        for (Integer tl : this.tlDef) {
            this.tlGiamst += tl;
        }
        if (tlGiamst > 90) {
            tlGiamst = 90;
        }
    }

    @Override
    protected void setCrit() {
        this.crit = ChungSinhBinhDangService.CHI_MANG;
        this.crit += this.critAdd;
    }

}
