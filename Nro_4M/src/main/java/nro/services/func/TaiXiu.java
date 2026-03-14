/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nro.services.func;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import nro.models.item.Item;
import nro.models.player.Player;
import nro.server.Client;
import nro.services.InventoryService;
import nro.services.ItemService;
import nro.services.Service;
import nro.utils.Util;

/**
 *
 * @author DEV Ăn Trộm
 */
public class TaiXiu implements Runnable {

    public int goldTai;
    public int goldXiu;
    public boolean ketquaTai = false;
    public boolean ketquaXiu = false;
    public boolean ketquaTamhoa = false;

    public boolean baotri = false;
    public boolean dongtaixiu = false;
    public long lastTimeEnd;
    public List<Player> PlayersTai = new ArrayList<>();
    public List<Player> PlayersXiu = new ArrayList<>();
    private static TaiXiu instance;
    public int x, y, z;
    public String ketqua = "";
    public List<String> List_KetQua = new ArrayList<>();

    public static TaiXiu gI() {
        if (instance == null) {
            instance = new TaiXiu();
        }
        return instance;
    }

    public void addPlayerXiu(Player pl) {
        if (!PlayersXiu.equals(pl)) {
            PlayersXiu.add(pl);
        }
    }

    public void addPlayerTai(Player pl) {
        if (!PlayersTai.equals(pl)) {
            PlayersTai.add(pl);
        }
    }

    public void removePlayerXiu(Player pl) {
        if (PlayersXiu.equals(pl)) {
            PlayersXiu.remove(pl);
        }
    }

    public void removePlayerTai(Player pl) {
        if (PlayersTai.equals(pl)) {
            PlayersTai.remove(pl);
        }
    }

    public String InKetQua() {
        String listketqua = "";
        for (int i = 0; i < List_KetQua.size(); i++) {
            listketqua += List_KetQua.get(i);

            if (i < List_KetQua.size() - 1) {
                listketqua += " ";
            }
        }
        return listketqua;
    }

    @Override
    public void run() {
        while (true) {
            try {
//                Calendar calendar = Calendar.getInstance();
//                int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
//                if (dongtaixiu == false) {
//                    if (!(dayOfWeek == Calendar.MONDAY
//                            || dayOfWeek == Calendar.WEDNESDAY
//                            || dayOfWeek == Calendar.FRIDAY)) {
//                        baotri = true;
//                    } else {
//                        baotri = false;
//                    }
//                }
                if (((TaiXiu.gI().lastTimeEnd - System.currentTimeMillis()) / 1000) <= 0) {
                    int x, y, z;
                    // Thực hiện các hành động sau khi chờ 10 giây
                    if (TaiXiu.gI().goldTai >= TaiXiu.gI().goldXiu) {
                        if (Util.isTrue(60, 100)) {
                            x = Util.nextInt(1, 4);
                            y = Util.nextInt(1, 3);
                            z = Util.nextInt(1, 3);
                            TaiXiu.gI().x = x;
                            TaiXiu.gI().y = y;
                            TaiXiu.gI().z = z;
                        } else {
                            x = Util.nextInt(4, 6);
                            y = Util.nextInt(4, 6);
                            z = Util.nextInt(3, 6);
                            TaiXiu.gI().x = x;
                            TaiXiu.gI().y = y;
                            TaiXiu.gI().z = z;
                        }
                    } else {
                        if (Util.isTrue(60, 100)) {
                            x = Util.nextInt(4, 6);
                            y = Util.nextInt(4, 6);
                            z = Util.nextInt(3, 6);
                            TaiXiu.gI().x = x;
                            TaiXiu.gI().y = y;
                            TaiXiu.gI().z = z;
                        } else {
                            x = Util.nextInt(1, 4);
                            y = Util.nextInt(1, 3);
                            z = Util.nextInt(1, 3);
                            TaiXiu.gI().x = x;
                            TaiXiu.gI().y = y;
                            TaiXiu.gI().z = z;
                        }
                    }
//                    x = Util.nextInt(1, 6);
//                    y = Util.nextInt(1, 6);
//                    z = Util.nextInt(1, 6);
//                    TaiXiu.gI().x = x;
//                    TaiXiu.gI().y = y;
//                    TaiXiu.gI().z = z;
                    int tong = (x + y + z);

                    if (tong >= 3 && tong < 11) {
                        ketquaTamhoa = false;
                        ketquaXiu = true;
                        ketquaTai = false;
                        ketqua = "(Xỉu)";
                        if (List_KetQua.size() >= 10) {
                            List_KetQua.remove(0);
                            List_KetQua.add("X");
                        } else {
                            List_KetQua.add("X");
                        }
                    }
                    if (tong > 10) {
                        ketquaTamhoa = false;
                        ketquaXiu = false;
                        ketquaTai = true;
                        ketqua = "(Tài)";
                        if (List_KetQua.size() >= 10) {
                            List_KetQua.remove(0);
                            List_KetQua.add("T");
                        } else {
                            List_KetQua.add("T");
                        }
                    }
//                    if (x==y && y==z && z==x){
//                        ketquaTamhoa = true;
//                        ketquaXiu = false;
//                        ketquaTai = false;
//                    }

                    if (ketquaTai == true) {
                        if (!TaiXiu.gI().PlayersTai.isEmpty()) {
                            for (int i = 0; i < PlayersTai.size(); i++) {
                                Player pl = this.PlayersTai.get(i);
                                if (pl != null && Client.gI().getPlayer(pl.name) != null) {
                                    int goldC = pl.goldTai + pl.goldTai * 90 / 100;
                                    Service.getInstance().sendThongBao(pl, "Số hệ thống quay ra\n" + x + " : "
                                            + y + " : " + z + "\n|5|Tổng là : " + tong + "\n(TÀI)\n\n|1|Bạn đã chiến thắng!!");
                                    Service.getInstance().sendThongBao(pl, "Chúc mừng bạn đã dành chiến thắng và nhận được " + Util.format(goldC) + " Thỏi vàng");
                                    Item thoivang = ItemService.gI().createNewItem((short) 457, goldC);
                                    InventoryService.gI().addItemBag(pl, thoivang, 1);
                                    Service.getInstance().sendMoney(pl);
                                    InventoryService.gI().sendItemBags(pl);
                                }
                            }
                        }
                        for (int i = 0; i < PlayersXiu.size(); i++) {
                            Player pl = this.PlayersXiu.get(i);
                            if (pl != null && Client.gI().getPlayer(pl.name) != null) {
                                Service.getInstance().sendThongBao(pl, "Số hệ thống quay ra\n" + x + " : "
                                        + y + " : " + z + "\n|5|Tổng là : " + tong + "\n(TÀI)\n\n|7|Trắng tay gòi, chơi lại đi!!!");
                            }
                        }
                    } else if (ketquaXiu == true) {
                        if (!TaiXiu.gI().PlayersXiu.isEmpty()) {
                            for (int i = 0; i < PlayersXiu.size(); i++) {
                                Player pl = this.PlayersXiu.get(i);
                                if (pl != null && Client.gI().getPlayer(pl.name) != null) {
                                    int goldC = pl.goldXiu + pl.goldXiu * 90 / 100;
                                    Service.getInstance().sendThongBao(pl, "Số hệ thống quay ra\n" + x + " : "
                                            + y + " : " + z + "\n|5|Tổng là : " + tong + "\n(XỈU)\n\n|1|Bạn đã chiến thắng!!");
                                    Service.getInstance().sendThongBao(pl, "Chúc mừng bạn đã dành chiến thắng và nhận được " + Util.format(goldC) + " Thỏi vàng");
                                    Item thoivang = ItemService.gI().createNewItem((short) 457, goldC);
                                    InventoryService.gI().addItemBag(pl, thoivang, 1);
                                    Service.getInstance().sendMoney(pl);
                                    InventoryService.gI().sendItemBags(pl);
                                }
                            }
                        }
                        for (int i = 0; i < PlayersTai.size(); i++) {
                            Player pl = this.PlayersTai.get(i);
                            if (pl != null && Client.gI().getPlayer(pl.name) != null) {
                                Service.getInstance().sendThongBao(pl, "Số hệ thống quay ra\n" + x + " : "
                                        + y + " : " + z + "\n|5|Tổng là : " + tong + "\n(XỈU)\n\n|7|Trắng tay gòi, chơi lại đi!!!");
                            }
                        }
                    } else {
                        for (int i = 0; i < PlayersTai.size(); i++) {
                            Player pl = this.PlayersTai.get(i);
                            if (pl != null && Client.gI().getPlayer(pl.name) != null) {
                                Service.getInstance().sendThongBao(pl, "Số hệ thống quay ra\n" + x + " : "
                                        + y + " : " + z + "\n|5|Tổng là : " + tong + "\n(TAM HOA)\n\n|7|Hahaha Nhà cái lụm hết nha!!!");
                            }
                        }
                        for (int i = 0; i < PlayersXiu.size(); i++) {
                            Player pl = this.PlayersXiu.get(i);
                            if (pl != null && Client.gI().getPlayer(pl.name) != null) {
                                Service.getInstance().sendThongBao(pl, "Số hệ thống quay ra\n" + x + " : "
                                        + y + " : " + z + "\n|5|Tổng là : " + tong + "\n(TAM HOA)\n\n|7|Hahaha Nhà cái lụm hết nha!!!");
                            }
                        }
                    }
                    for (int i = 0; i < TaiXiu.gI().PlayersTai.size(); i++) {
                        Player pl = TaiXiu.gI().PlayersTai.get(i);
                        if (pl != null) {
                            pl.goldTai = 0;
                        }
                    }
                    for (int i = 0; i < TaiXiu.gI().PlayersXiu.size(); i++) {
                        Player pl = TaiXiu.gI().PlayersXiu.get(i);
                        if (pl != null) {
                            pl.goldXiu = 0;
                        }
                    }
                    ketquaXiu = false;
                    ketquaTai = false;
                    ketquaTamhoa = false;
                    TaiXiu.gI().goldTai = 0;
                    TaiXiu.gI().goldXiu = 0;
                    TaiXiu.gI().PlayersTai.clear();
                    TaiXiu.gI().PlayersXiu.clear();
                    TaiXiu.gI().lastTimeEnd = System.currentTimeMillis() + 60000;
                }
                Thread.sleep(500);
            } catch (Exception e) {
            }
        }
    }
}
