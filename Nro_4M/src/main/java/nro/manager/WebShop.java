package nro.manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import nro.jdbc.DBService;
import nro.services.ItemService;
import nro.services.*;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.server.ServerManager;
import nro.models.player.Player;
import org.json.simple.JSONValue;
import org.json.simple.JSONArray;
import nro.server.Client;

public class WebShop implements Runnable {

    public static WebShop i;

    public long lastTimeShop;

    // public int timeSet = 300;
    public int timeSet = 5;

    public static WebShop gI() {
        if (i == null) {
            i = new WebShop();
        }
        return i;
    }

    @Override
    public void run() {
        while (ServerManager.isRunning) {
            if (lastTimeShop < (System.currentTimeMillis() - (timeSet * 1000))) {
                lastTimeShop = System.currentTimeMillis();
                try {
                    PreparedStatement ps = DBService.gI().getConnectionForGame().prepareStatement("SELECT * FROM `shops_web`");
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        int uid = rs.getInt("uid");
                        int id = rs.getInt("id");
                        int idItem = rs.getInt("items");
                        boolean status = (rs.getInt("status") == 0);
                        if (status) {
                            Player pl = Client.gI().getPlayer((long) uid);
                            if (pl != null) {
                                if (InventoryService.gI().getCountEmptyBag(pl) > 0) {
                                    PreparedStatement pss = DBService.gI().getConnectionForGame().prepareStatement("SELECT * FROM items_web where id = ?", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY);
                                    pss.setInt(1, idItem);
                                    ResultSet items = pss.executeQuery();
                                    if (items.first()) {
                                        int item_uid = items.getInt("items");
                                        int slot = items.getInt("slot");
                                        Item it = ItemService.gI().createNewItem((short) item_uid, slot);
                                        JSONValue jv = new JSONValue();
                                        JSONArray dataArray = null;
                                        dataArray = (JSONArray) jv.parse(items.getString("options"));
                                        for (int i = 0; i < dataArray.size(); i++) {
                                            JSONArray innerArray = (JSONArray) dataArray.get(i);
                                            int option = ((Long) innerArray.get(0)).intValue();
                                            int param = ((Long) innerArray.get(1)).intValue();
                                            it.itemOptions.add(new ItemOption(option, param));

                                        }
                                        InventoryService.gI().addItemBag(pl, it, 1);
                                        InventoryService.gI().sendItemBags(pl);
                                        Service.getInstance().sendThongBao(pl, "Bạn nhận được x " + slot + " item " + it.template.name + " từ Web");
                                        updateNhan(id);
                                    }
                                } else {
                                    Service.getInstance().sendThongBao(pl, "Vui lòng mở rộng hành trang");
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                }
            }
        }
    }

    public static void updateNhan(int id) {
        PreparedStatement psk = null;
        try (Connection con = DBService.gI().getConnectionForSaveData();) {
            psk = con.prepareStatement("update shops_web set status = ? where id = ?");
            psk.setInt(1, 1);
            psk.setInt(2, id);
            psk.executeUpdate();
            psk.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                psk.close();
            } catch (Exception e) {
            }
        }
    }
}
