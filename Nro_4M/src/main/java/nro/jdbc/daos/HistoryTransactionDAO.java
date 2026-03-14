package nro.jdbc.daos;

import nro.jdbc.DBService;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.player.Player;
import nro.utils.TimeUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class HistoryTransactionDAO {

    public static void insert(Player pl1, Player pl2,
            int goldP1, int goldP2,
            List<Item> itemP1, List<Item> itemP2,
            List<Item> bag1Before, List<Item> bag2Before,
            List<Item> bag1After, List<Item> bag2After,
            long gold1Before, long gold2Before,
            long gold1After, long gold2After) {

        String player1 = pl1.name + " (" + pl1.id + ")";
        String player2 = pl2.name + " (" + pl2.id + ")";
        String itemPlayer1 = "Gold: " + goldP1;
        String itemPlayer2 = "Gold: " + goldP2;
        List<Item> doGD1 = new ArrayList<>();
        List<Item> doGD2 = new ArrayList<>();

        for (Item item : itemP1) {
            if (item.isNotNullItem() && doGD1.stream().noneMatch(i -> i.template.id == item.template.id)) {
                doGD1.add(item);
            } else if (item.isNotNullItem()) {
                doGD1.stream().filter(i -> i.template.id == item.template.id).findFirst().get().quantityGD += item.quantityGD;
            }
        }

        for (Item item : itemP2) {
            if (item.isNotNullItem() && doGD2.stream().noneMatch(i -> i.template.id == item.template.id)) {
                doGD2.add(item);
            } else if (item.isNotNullItem()) {
                doGD2.stream().filter(i -> i.template.id == item.template.id).findFirst().get().quantityGD += item.quantityGD;
            }
        }

        for (Item item : doGD1) {
            if (item.isNotNullItem()) {
                itemPlayer1 += ", " + buildItemLog(item, true);
            }
        }

        for (Item item : doGD2) {
            if (item.isNotNullItem()) {
                itemPlayer2 += ", " + buildItemLog(item, true);
            }
        }

        String beforeTran1 = "";
        for (Item item : bag1Before) {
            if (item.isNotNullItem()) {
                beforeTran1 += buildItemLog(item, false) + ", ";
            }
        }

        String beforeTran2 = "";
        for (Item item : bag2Before) {
            if (item.isNotNullItem()) {
                beforeTran2 += buildItemLog(item, false) + ", ";
            }
        }

        String afterTran1 = "";
        for (Item item : bag1After) {
            if (item.isNotNullItem()) {
                afterTran1 += buildItemLog(item, false) + ", ";
            }
        }

        String afterTran2 = "";
        for (Item item : bag2After) {
            if (item.isNotNullItem()) {
                afterTran2 += buildItemLog(item, false) + ", ";
            }
        }

        try (Connection con = DBService.gI().getConnectionForSaveHistory(); PreparedStatement ps = con.prepareStatement(
                "INSERT INTO history_transaction "
                + "(player_1, player_2, item_player_1, item_player_2, bag_1_before_tran, bag_2_before_tran, bag_1_after_tran, bag_2_after_tran, time_tran) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)")) {

            ps.setString(1, player1);
            ps.setString(2, player2);
            ps.setString(3, itemPlayer1);
            ps.setString(4, itemPlayer2);
            ps.setString(5, beforeTran1.trim());
            ps.setString(6, beforeTran2.trim());
            ps.setString(7, afterTran1.trim());
            ps.setString(8, afterTran2.trim());
            ps.setTimestamp(9, new Timestamp(System.currentTimeMillis()));
            ps.executeUpdate();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private static String buildItemLog(Item item, boolean isGD) {
        if (!item.isNotNullItem()) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("id:").append(item.template.id);
        sb.append(" (x").append(isGD ? item.quantityGD : item.quantity).append(")");
        if (item.itemOptions != null && !item.itemOptions.isEmpty()) {
            sb.append(" [");
            for (int i = 0; i < item.itemOptions.size(); i++) {
                ItemOption opt = item.itemOptions.get(i);
                sb.append(opt.optionTemplate.id).append(":").append(opt.param);
                if (i < item.itemOptions.size() - 1) {
                    sb.append(",");
                }
            }
            sb.append("]");
        }
        return sb.toString();
    }

    public static void deleteHistory() {
        PreparedStatement ps = null;
        try (Connection con = DBService.gI().getConnectionForAutoSave();) {
            ps = con.prepareStatement("delete from history_transaction where time_tran < '"
                    + TimeUtil.getTimeBeforeCurrent(3 * 24 * 60 * 60 * 1000, "yyyy-MM-dd") + "'");
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                ps.close();
            } catch (SQLException ex) {
                java.util.logging.Logger.getLogger(HistoryTransactionDAO.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
