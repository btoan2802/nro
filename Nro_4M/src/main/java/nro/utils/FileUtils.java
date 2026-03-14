package nro.utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.player.Player;

public class FileUtils {

    public static void writeLogToFile(String fileName, String line) {
        try {
            File folder = new File("log");
            if (!folder.exists()) {
                folder.mkdirs();
            }

            BufferedWriter bw = new BufferedWriter(new FileWriter("log/" + fileName + ".txt", true));
            bw.write(line);
            bw.newLine();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static long getFolderSize(File folder) {
        long length = 0;
        File[] files = folder.listFiles();
        int count = files.length;
        for (int i = 0; i < count; i++) {
            if (files[i].isFile()) {
                length += files[i].length();
            } else {
                length += getFolderSize(files[i]);
            }
        }
        return length;
    }

    public static String cutPng(String str) {
        String result = str;
        if (str.contains(".png")) {
            result = str.replace(".png", "");
        }
        return result;
    }

    public static void addPath(ArrayList<File> list, File file) {
        if (file.isFile()) {
            list.add(file);
        } else {
            for (File f : file.listFiles()) {
                addPath(list, f);
            }
        }
    }

    public static void LogItemRemoved(Player player, Item item, String reason) {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date())).append("] ");
        sb.append("Player: ").append(player.name).append(" (ID: ").append(player.id).append(") - ");
        sb.append("Removed item: ").append(item.template.name).append(" (ID: ").append(item.template.id).append(") - ");

        if (item.itemOptions != null && !item.itemOptions.isEmpty()) {
            sb.append("Options: ");
            for (ItemOption io : item.itemOptions) {
                sb.append(io.optionTemplate.name)
                        .append(" (ID: ").append(io.optionTemplate.id)
                        .append(", Param: ").append(io.param)
                        .append("); ");
            }
        } else {
            sb.append("Options: none");
        }

        sb.append("- Lý do: ").append(reason);

        writeLogToFile("npc_item_remove", sb.toString());
    }

    public static void logItemThrow(Player player, Item item, int where, int index) {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(TimeUtil.getTimeNow("dd/MM/yyyy HH:mm:ss")).append("]\n");
        sb.append("+ Player: ").append(player.name).append("\n");
        sb.append("+ Account: ").append(player.session != null ? player.session.uu : "null").append("\n");
        sb.append("+ Map ID: ").append(player.zone.map.mapId).append("\n");
        sb.append("+ Vị trí: ").append(where == 0 ? "Body (trang bị)" : "Bag (hành trang)")
                .append(" | Slot: ").append(index).append("\n");
        sb.append("+ Item ID: ").append(item.template.id).append(" | Tên: ").append(item.template.name).append("\n");
        sb.append("+ Thuộc tính:\n");

        if (item.itemOptions != null && !item.itemOptions.isEmpty()) {
            for (ItemOption io : item.itemOptions) {
                String name = io.optionTemplate.name.replaceAll("#", String.valueOf(io.param));
                sb.append(String.format("   - %s [ID: %d, Param: %d]%n", name, io.optionTemplate.id, io.param));
            }
        } else {
            sb.append("   - (Không có option)\n");
        }

        sb.append("--------------------------------------------------\n");

        writeLogToFile("log_throw_item", sb.toString());
    }

    public static void writeGoSaoTrangBiLog(Player player, Item itemTruoc, Item itemSau) {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(TimeUtil.getTimeNow("dd/MM/yyyy HH:mm:ss")).append("]").append("\n")
                .append("Player: ").append(player.name).append("\n")
                .append("Account: ").append(player.session != null ? player.session.uu : "null").append("\n")
                .append("=> Trước khi gỡ SPL:\n")
                .append(buildItemInfo(itemTruoc)).append("\n")
                .append("=> Sau khi gỡ SPL:\n")
                .append(buildItemInfo(itemSau)).append("\n")
                .append("------------------------------------------------------\n");

        writeLogToFile("go_sao_trang_bi", sb.toString());
    }

    public static String buildItemInfo(Item item) {
        StringBuilder sb = new StringBuilder();
        sb.append("  [ID: ").append(item.template.id).append("] ").append(item.template.name).append("\n");
        sb.append("  Options:\n");
        for (ItemOption io : item.itemOptions) {
            sb.append("    - ID: ").append(io.optionTemplate.id)
                    .append(", Param: ").append(io.param).append("\n");
        }
        return sb.toString();
    }

    public static void logMergeItem(Player player, int itemId, long totalBefore, long totalAfter) {
        String log = "[" + TimeUtil.getTimeNow("dd/MM/yyyy HH:mm:ss") + "] "
                + "Player: " + player.name + " (ID: " + player.id + ") gộp item ID: " + itemId
                + " | Trước: " + totalBefore + " | Sau: " + totalAfter;

        writeLogToFile("gop_item_log", log);
    }

    public static void writeTachItemLog(Player player, int itemId, int soLuongConLai, int quantityPerStack, int totalNeeded, int numStacks, String itemName) {
        String log = "[" + TimeUtil.getTimeNow("dd/MM/yyyy HH:mm:ss") + "] "
                + "Player: " + player.name
                + " | Account: " + (player.session != null ? player.session.uu : "null")
                + " | ItemID: " + itemId
                + " | Item: " + itemName
                + " | Số lượng mỗi stack: " + quantityPerStack
                + " | Stack muốn tách: " + numStacks
                + " | Tổng cần: " + totalNeeded
                + " | Còn lại: " + soLuongConLai;

        writeLogToFile("tach_item_log", log);
    }

    public static void logThuHoiNgocRong(Player player, List<Integer> idsNgoc, int soLuongTra) {
        String log = "[" + TimeUtil.getTimeNow("dd/MM/yyyy HH:mm:ss") + "] "
                + "Player: " + player.name
                + " | Account: " + (player.session != null ? player.session.uu : "null")
                + " | ID: " + player.id
                + " | Đã thu hồi ngọc rồng ID: " + idsNgoc
                + " => Trả lại item ID: 1015 x" + soLuongTra;
        writeLogToFile("thu_hoi_ngoc_rong", log);
    }

    public static void logRewardItems(Player player, List<Item> items, String reason) {
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(TimeUtil.getTimeNow("yyyy-MM-dd HH:mm:ss")).append("] ");
        sb.append("Player: ").append(player.name)
                .append(" | ID: ").append(player.id)
                .append(" | Account: ").append(player.session != null ? player.session.uu : "null").append("\n");

        sb.append("Lý do: ").append(reason).append("\n");

        for (Item item : items) {
            sb.append("- Item: ").append(item.template.name)
                    .append(" (ID: ").append(item.template.id)
                    .append("), Quantity: ").append(item.quantity).append("\n");

            if (item.itemOptions != null && !item.itemOptions.isEmpty()) {
                sb.append("  Options:\n");
                for (ItemOption io : item.itemOptions) {
                    sb.append(String.format("     + ID: %d, Param: %d%n", io.optionTemplate.id, io.param));
                }
            } else {
                sb.append("  Options: none\n");
            }
        }
        sb.append("--------------------------------------------------\n");
        writeLogToFile("reward_mailbox", sb.toString());
    }

}
