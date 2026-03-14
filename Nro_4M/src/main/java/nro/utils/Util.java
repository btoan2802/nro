package nro.utils;

import nro.models.mob.Mob;
import nro.models.npc.Npc;
import nro.models.player.Player;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import nro.models.boss.BossManager;
import nro.models.item.Item;
import nro.models.item.ItemOption;
import nro.models.map.ItemMap;
import nro.models.map.Zone;
import nro.services.ItemService;

public class Util {

    private static final Random rand;
    private static final SimpleDateFormat dateFormat;
    private static SimpleDateFormat dateFormatDay = new SimpleDateFormat("yyyy-MM-dd");
    private static final Locale locale = new Locale("vi", "VN");
    private static final NumberFormat num = NumberFormat.getInstance(locale);

    static {
        rand = new Random();
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    }

    public static int randomBossId() {
        int bossId = Util.nextInt(10000);
        while (BossManager.gI().getBossByIdRandom(bossId) != null) {
            bossId = Util.nextInt(10000);
        }
        return bossId;
    }

    public static int highlightsItem(boolean highlights, int value) {
        double highlightsNumber = 1.1;
        return highlights ? (int) (value * highlightsNumber) : value;
    }

    public static String numberToMoney(long power) {
        Locale locale = new Locale("vi", "VN");
        NumberFormat num = NumberFormat.getInstance(locale);
        num.setMaximumFractionDigits(1);
        if (power >= 1000000000) {
            return num.format((double) power / 1000000000) + " Tỷ";
        } else if (power >= 1000000) {
            return num.format((double) power / 1000000) + " Tr";
        } else if (power >= 1000) {
            return num.format((double) power / 1000) + " k";
        } else {
            return num.format(power);
        }
    }
    
    public static String powerToStringnew(double power) {
        Locale localee = new Locale("vi", "VN");
        NumberFormat number = NumberFormat.getInstance(localee);
        number.setMaximumFractionDigits(1);
        if (power >= 1_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000D) {
            return number.format((double) power / 1_000_000_000_000_000_000_000_000_000_000_000_000_000_000_000D) + " Tỷ Tỷ Tỷ Tỷ Tỷ";
        } else if (power >= 1_000_000_000_000_000_000_000_000_000_000_000_000_000_000D) {
            return number.format((double) power / 1_000_000_000_000_000_000_000_000_000_000_000_000_000_000D) + " Triệu Tỷ Tỷ Tỷ Tỷ";
        } else if (power >= 1_000_000_000_000_000_000_000_000_000_000_000_000_000D) {
            return number.format((double) power / 1_000_000_000_000_000_000_000_000_000_000_000_000_000D) + " Nghìn Tỷ Tỷ Tỷ Tỷ";
        } else if (power >= 1_000_000_000_000_000_000_000_000_000_000_000_000D) {
            return number.format((double) power / 1_000_000_000_000_000_000_000_000_000_000_000_000D) + " Tỷ Tỷ Tỷ Tỷ";
        } else if (power >= 1000000000000000000000000000000000D) {
            return number.format((double) power / 1_000_000_000_000_000_000_000_000_000_000_000D) + " Triệu Tỷ Tỷ Tỷ";
        } else if (power >= 1_000_000_000_000_000_000_000_000_000_000D) {
            return number.format((double) power / 1_000_000_000_000_000_000_000_000_000_000D) + " Nghìn Tỷ Tỷ Tỷ";
        } else if (power >= 1000000000000000000000000000D) {
            return number.format((double) power / 1000000000000000000000000000D) + " Tỷ Tỷ Tỷ";
        } else if (power >= 1000000000000000000000000D) {
            return number.format((double) power / 1000000000000000000000000D) + " Triệu Tỷ Tỷ";
        } else if (power >= 1000000000000000000000D) {
            return number.format((double) power / 1000000000000000000000D) + " Nghìn Tỷ Tỷ";
        } else if (power >= 1000000000000000000L) {
            return number.format((double) power / 1000000000000000000L) + " Tỷ Tỷ";
        } else if (power >= 1000000000000000L) {
            return number.format((double) power / 1000000000000000L) + " Triệu Tỷ";
        } else if (power >= 1000000000000L) {
            return number.format((double) power / 1000000000000L) + " Nghìn Tỷ";
        } else if (power >= 1000000000) {
            return number.format((double) power / 1000000000) + " Tỷ";
        } else if (power >= 1000000) {
            return number.format((double) power / 1000000) + " Tr";
        } else if (power >= 1000) {
            return number.format((double) power / 1000) + " k";
        } else {
            return number.format(power);
        }
    }

    public static String powerToString(double power) {
        Locale locale = new Locale("vi", "VN");
        NumberFormat num = NumberFormat.getInstance(locale);
        num.setMaximumFractionDigits(1);
        if (power >= 1000000000) {
            return num.format((double) power / 1000000000) + " Tỷ";
        } else if (power >= 1000000) {
            return num.format((double) power / 1000000) + " Tr";
        } else if (power >= 1000) {
            return num.format((double) power / 1000) + " k";
        } else {
            return num.format(power);
        }
    }

    public static String format(double power) {
        return num.format(power);
    }

    public static String formatNew(double power) {
        return num.format(power);
    }

    public static void setTimeout(Runnable runnable, int delay) {
        new Thread(() -> {
            try {
                Thread.sleep(delay);
                runnable.run();
            } catch (Exception e) {
                System.err.println(e);
            }
        }).start();
    }
    
    public static int createIdRandomBoss(int idPlayer) {
        return -251003 - idPlayer;
    }

    public static String convertMillisecondsToSeconds(long milliseconds) {
        double seconds = (double) milliseconds / 1000;
        return String.format("%.2f", seconds).replace('.', ',');
    }
    
    public static String convertSecondsToTime2(long totalSeconds) {
        if (totalSeconds < 60) {
            return totalSeconds + " giây trước";
        } else if (totalSeconds < 3600) {
            long minutes = totalSeconds / 60;
            return minutes + " phút" + " trước";
        } else if (totalSeconds < 86400) {
            long hours = totalSeconds / 3600;
            return hours + " giờ" + " trước";
        } else {
            long days = totalSeconds / 86400;
            return days + " ngày" + " trước";
        }
    }

    public static int getDistance(int x1, int y1, int x2, int y2) {
        return (int) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    public static int getDistance(Player pl1, Player pl2) {
        return getDistance(pl1.location.x, pl1.location.y, pl2.location.x, pl2.location.y);
    }

    public static int getDistance(Player pl, Npc npc) {
        return getDistance(pl.location.x, pl.location.y, npc.cx, npc.cy);
    }

    public static int getDistance(Player pl, Mob mob) {
        return getDistance(pl.location.x, pl.location.y, mob.location.x, mob.location.y);
    }

    public static int getDistance(Mob mob1, Mob mob2) {
        return getDistance(mob1.location.x, mob1.location.y, mob2.location.x, mob2.location.y);
    }

    public static String msToTime(long ms) {
        ms = ms - System.currentTimeMillis();
        if (ms < 0) {
            ms = 0;
        }
        long giay = 0;
        long phut = 0;
        long gio = 0;
        long ngay = 0;
        giay = ms / 1000;
        phut = giay / 60;
        giay = giay % 60;
        gio = phut / 60;
        phut = phut % 60;
        ngay = gio / 24;
        gio = gio % 24;
        String giayString = String.valueOf(giay);
        String phutString = String.valueOf(phut);
        String gioString = String.valueOf(gio);
        String ngayString = String.valueOf(ngay);
        String time = null;
        if (ngay != 0) {
            time = ngayString + " Ngày, " + gioString + " giờ, " + phutString + "phút, " + giayString + "giây";
        } else if (gio != 0) {
            time = gioString + " giờ, " + phutString + "phút, " + giayString + "giây";
        } else if (phut != 0) {
            time = phutString + "phút, " + giayString + "giây";
        } else if (giay != 0) {
            time = giayString + "giây";
        } else {
            time = "Hết hạn";
        }
        return time;
    }

    public static String formatTimeBinh(long ms) {
        long giay = 0;
        long phut = 0;
        long gio = 0;
        long ngay = 0;
        giay = ms / 1000;
        phut = giay / 60;
        giay = giay % 60;
        gio = phut / 60;
        phut = phut % 60;
        ngay = gio / 24;
        gio = gio % 24;
        String giayString = String.valueOf(giay);
        String phutString = String.valueOf(phut);
        String gioString = String.valueOf(gio);
        String ngayString = String.valueOf(ngay);
        String time = null;
        if (ngay != 0) {
            time = ngayString + "d " + gioString + "h " + phutString + "p " + giayString + "s";
        } else if (gio != 0) {
            time = gioString + "h " + phutString + "p " + giayString + "s";
        } else if (phut != 0) {
            time = phutString + "p " + giayString + "s";
        } else if (giay != 0) {
            time = giayString + "s";
        } else {
            time = "";
        }
        return time;
    }

    public static String formatTimeString2(long ms) {
        long giay = 0;
        long phut = 0;
        long gio = 0;
        long ngay = 0;
        giay = ms / 1000;
        phut = giay / 60;
        gio = phut / 60;
        phut = phut % 60;
        ngay = gio / 24;
        gio = gio % 24;
        String phutString = String.valueOf(phut);
        String gioString = String.valueOf(gio);
        String ngayString = String.valueOf(ngay);
        String time = null;
        if (ngay != 0) {
            time = ngayString + " Ngày " + gioString + "giờ " + phutString + "phút";
        } else if (gio != 0) {
            time = gioString + "giờ " + phutString + "phút";
        } else if (phut != 0) {
            time = phutString + "phút";
        } else {
            time = "";
        }
        return time;
    }

    public static int getDistanceByDir(int x, int x1, int dir) {
        if (dir == -1) {
            return x + x1;
        }
        return x - x1;
    }

    public static int nextInt(int from, int to) {
        return from + rand.nextInt(to - from + 1);
    }

    public static long nextLong(long min, long max) {
        if (min > max) {
            throw new IllegalArgumentException("min > max");
        }
        return ThreadLocalRandom.current().nextLong(min, max + 1);
    }

    public static int nextInt(int max) {
        return rand.nextInt(max);
    }

    public static int nextInt(int[] percen) {
        int next = nextInt(1000), i;
        for (i = 0; i < percen.length; i++) {
            if (next < percen[i]) {
                return i;
            }
            next -= percen[i];
        }
        return i;
    }

    public static int getOne(int n1, int n2) {
        return rand.nextInt() % 2 == 0 ? n1 : n2;
    }

    public static int currentTimeSec() {
        return (int) System.currentTimeMillis() / 1000;
    }

    public static String replace(String text, String regex, String replacement) {
        return text.replace(regex, replacement);
    }

    public static void debug(String message) {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
        String strDate = formatter.format(date);
        try {
            System.err.println(message);
        } catch (Exception e) {
            System.out.println(message);
        }
    }

    public static void main(String[] args) {
        for (int i = 0; i < 1008; i++) {
            if (!isTrue(104, 100)) {
                System.out.println("xxx");
            }
        }
    }

    public static int DoubleGioihan(double a) {
        if (a > 2123456789) {
            a = 2123456789;
        }
        return (int) a;
    }

    public static int DoubleToInter(double a) {
        return (int) Math.floor(a);
    }

    public static long DoubleToDouble(double a) {
        // Dùng Math.floor để làm tròn xuống và giữ giá trị ở dạng double
        return (long) a;
    }

    public static long LongGioihan(double a) {
        if (a > 9000000000000000000L) {
            a = 9000000000000000000L;
        }
        return (long) a;
    }

    public static long GioiHannextdame(double from, double to) {
        //code by Việt
        return (long) from + rand.nextInt((int) (to - from + 1));
    }

    public static double nextdame(double from, double to) {
        //code by Việt
        return (double) from + rand.nextDouble((double) (to - from + 1));
    }

    public static boolean isTrue(int ratio, int typeRatio) {
        int num = Util.nextInt(typeRatio);
        if (num < ratio) {
            return true;
        }
        return false;
    }

    public static boolean isTrue(float ratio, int typeRatio) {
        if (ratio < 1) {
            ratio *= 10;
            typeRatio *= 10;
        }
        int num = Util.nextInt(typeRatio);
        if (num < ratio) {
            return true;
        }
        return false;
    }

    public static boolean haveSpecialCharacter(String text) {
        Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(text);
        boolean b = m.find();
        return b || text.contains(" ");
    }

    public static boolean canDoWithTime(long lastTime, long miniTimeTarget) {
        return System.currentTimeMillis() - lastTime > miniTimeTarget;
    }

    private static final char[] SOURCE_CHARACTERS = {'À', 'Á', 'Â', 'Ã', 'È', 'É',
        'Ê', 'Ì', 'Í', 'Ò', 'Ó', 'Ô', 'Õ', 'Ù', 'Ú', 'Ý', 'à', 'á', 'â',
        'ã', 'è', 'é', 'ê', 'ì', 'í', 'ò', 'ó', 'ô', 'õ', 'ù', 'ú', 'ý',
        'Ă', 'ă', 'Đ', 'đ', 'Ĩ', 'ĩ', 'Ũ', 'ũ', 'Ơ', 'ơ', 'Ư', 'ư', 'Ạ',
        'ạ', 'Ả', 'ả', 'Ấ', 'ấ', 'Ầ', 'ầ', 'Ẩ', 'ẩ', 'Ẫ', 'ẫ', 'Ậ', 'ậ',
        'Ắ', 'ắ', 'Ằ', 'ằ', 'Ẳ', 'ẳ', 'Ẵ', 'ẵ', 'Ặ', 'ặ', 'Ẹ', 'ẹ', 'Ẻ',
        'ẻ', 'Ẽ', 'ẽ', 'Ế', 'ế', 'Ề', 'ề', 'Ể', 'ể', 'Ễ', 'ễ', 'Ệ', 'ệ',
        'Ỉ', 'ỉ', 'Ị', 'ị', 'Ọ', 'ọ', 'Ỏ', 'ỏ', 'Ố', 'ố', 'Ồ', 'ồ', 'Ổ',
        'ổ', 'Ỗ', 'ỗ', 'Ộ', 'ộ', 'Ớ', 'ớ', 'Ờ', 'ờ', 'Ở', 'ở', 'Ỡ', 'ỡ',
        'Ợ', 'ợ', 'Ụ', 'ụ', 'Ủ', 'ủ', 'Ứ', 'ứ', 'Ừ', 'ừ', 'Ử', 'ử', 'Ữ',
        'ữ', 'Ự', 'ự',};

    private static final char[] DESTINATION_CHARACTERS = {'A', 'A', 'A', 'A', 'E',
        'E', 'E', 'I', 'I', 'O', 'O', 'O', 'O', 'U', 'U', 'Y', 'a', 'a',
        'a', 'a', 'e', 'e', 'e', 'i', 'i', 'o', 'o', 'o', 'o', 'u', 'u',
        'y', 'A', 'a', 'D', 'd', 'I', 'i', 'U', 'u', 'O', 'o', 'U', 'u',
        'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A',
        'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'A', 'a', 'E', 'e',
        'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E', 'e', 'E',
        'e', 'I', 'i', 'I', 'i', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o',
        'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O', 'o', 'O',
        'o', 'O', 'o', 'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u', 'U', 'u',
        'U', 'u', 'U', 'u',};

    public static char removeAccent(char ch) {
        int index = Arrays.binarySearch(SOURCE_CHARACTERS, ch);
        if (index >= 0) {
            ch = DESTINATION_CHARACTERS[index];
        }
        return ch;
    }

    public static String removeAccent(String str) {
        StringBuilder sb = new StringBuilder(str);
        for (int i = 0; i < sb.length(); i++) {
            sb.setCharAt(i, removeAccent(sb.charAt(i)));
        }
        return sb.toString();
    }

    public static int timeToInt(int d, int h, int m) {
        int result = 0;
        try {
            if (d > 0) {
                result += (60 * 60 * 24 * d);
            }
            if (h > 0) {
                result += 60 * 60 * h;
            }
            if (m > 0) {
                result += 60 * m;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static String convertSeconds(int sec) {
        int seconds = sec % 60;
        int minutes = sec / 60;
        if (minutes >= 60) {
            int hours = minutes / 60;
            minutes %= 60;
            if (hours >= 24) {
                int days = hours / 24;
                return String.format("%dd%02dh%02d'", days, hours % 24, minutes);
            }
            return String.format("%02dh%02d'", hours, minutes);
        }
        return String.format("%02d'", minutes);
    }

    public static String formatTime(long time) {
        try {
            SimpleDateFormat sdm = new SimpleDateFormat("H%1 m%2");
            String done = sdm.format(new java.util.Date(time));
            done = done.replaceAll("%1", "giờ").replaceAll("%2", "phút");
            return done;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static short getTimeCanMove(byte speed) {
        switch (speed) {
            case 1:
                return 2000;
            case 2:
                return 1000;
            case 3:
                return 500;
            case 5:
                return 400;
            default:
                return 0;
        }
    }

    public static synchronized boolean compareDay(Date now, Date when) {
        try {
            Date date1 = Util.dateFormatDay.parse(Util.dateFormatDay.format(now));
            Date date2 = Util.dateFormatDay.parse(Util.dateFormatDay.format(when));
            return !date1.equals(date2) && !date1.before(date2);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Date getDate(String str) {
        try {
            return dateFormat.parse(str);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String toDateString(Date date) {
        try {
            String a = Util.dateFormat.format(date);
            return a;
        } catch (Exception e) {
            Date now = new Date();
            return dateFormat.format(now);
        }
    }

    public static int[] generateArrRandNumber(int from, int to, int size) {
        return rand.ints(from, to).distinct().limit(size).toArray();
    }

    public static int[] pickNRandInArr(int[] array, int n) {
        List<Integer> list = new ArrayList<Integer>(array.length);
        for (int i : array) {
            list.add(i);
        }
        Collections.shuffle(list);
        int[] answer = new int[n];
        for (int i = 0; i < n; i++) {
            answer[i] = list.get(i);
        }
        Arrays.sort(answer);
        return answer;
    }

    public static Item ratiItemTL(int tempId) {
        Item it = ItemService.gI().createItemSetKichHoat(tempId, 1);
        List<Integer> ao = Arrays.asList(555, 557, 559);
        List<Integer> quan = Arrays.asList(556, 558, 560);
        List<Integer> gang = Arrays.asList(562, 564, 566);
        List<Integer> giay = Arrays.asList(563, 565, 567);
        int ntl = 561;
        if (ao.contains(tempId)) {
            it.itemOptions.add(new ItemOption(47, highlightsItem(it.template.gender == 2, new Random().nextInt(501) + 1000)));
        }
        if (quan.contains(tempId)) {
            it.itemOptions.add(new ItemOption(22, highlightsItem(it.template.gender == 0, new Random().nextInt(11) + 45)));
        }
        if (gang.contains(tempId)) {
            it.itemOptions.add(new ItemOption(0, highlightsItem(it.template.gender == 2, new Random().nextInt(1001) + 3500)));
        }
        if (giay.contains(tempId)) {
            it.itemOptions.add(new ItemOption(23, highlightsItem(it.template.gender == 1, new Random().nextInt(11) + 35)));
        }
        if (ntl == tempId) {
            it.itemOptions.add(new ItemOption(14, new Random().nextInt(3) + 15));
        }
        it.itemOptions.add(new ItemOption(21, 15));
        return it;
    }

    public static Item ratiItemHuyDiet(int tempId) {
        Item it = ItemService.gI().createItemSetKichHoat(tempId, 1);
        List<Integer> ao = Arrays.asList(650, 652, 654);
        List<Integer> quan = Arrays.asList(651, 653, 655);
        List<Integer> gang = Arrays.asList(657, 659, 661);
        List<Integer> giay = Arrays.asList(658, 660, 662);
        int nhd = 656;
        if (ao.contains(tempId)) {
            it.itemOptions.add(new ItemOption(47, Util.highlightsItem(it.template.gender == 2, new Random().nextInt(1001) + 1800))); // áo từ 1800-2800 giáp
        }
        if (quan.contains(tempId)) {
            it.itemOptions.add(new ItemOption(22, Util.highlightsItem(it.template.gender == 0, new Random().nextInt(16) + 85))); // hp 85-100k
        }
        if (gang.contains(tempId)) {
            it.itemOptions.add(new ItemOption(0, Util.highlightsItem(it.template.gender == 2, new Random().nextInt(1500) + 8500))); // 8500-10000
        }
        if (giay.contains(tempId)) {
            it.itemOptions.add(new ItemOption(23, Util.highlightsItem(it.template.gender == 1, new Random().nextInt(11) + 80))); // ki 80-90k
        }
        if (nhd == tempId) {
            it.itemOptions.add(new ItemOption(14, new Random().nextInt(3) + 17));
        }
        it.itemOptions.add(new ItemOption(21, 80));
        return it;
    }

    public static Item ratiItemThienSu(int tempId) {
        Item dots = ItemService.gI().createItemSetKichHoat(tempId, 1);
        List<Integer> ao = Arrays.asList(1048, 1049, 1050);
        List<Integer> quan = Arrays.asList(1051, 1052, 1053);
        List<Integer> gang = Arrays.asList(1054, 1055, 1056);
        List<Integer> giay = Arrays.asList(1057, 1058, 1059);
        List<Integer> nhan = Arrays.asList(1060, 1061, 1062);
        //áo
        if (ao.contains(tempId)) {
            dots.itemOptions.add(new ItemOption(47, Util.highlightsItem(dots.template.gender == 2, new Random().nextInt(1201) + 2800))); // áo từ 2800-4000 giáp
        }
        //quần
        if (Util.isTrue(60, 100)) {
            if (quan.contains(tempId)) {
                dots.itemOptions.add(new ItemOption(22, Util.highlightsItem(dots.template.gender == 0, new Random().nextInt(11) + 120))); // hp 120k-130k
            }
        } else {
            if (quan.contains(tempId)) {
                dots.itemOptions.add(new ItemOption(22, Util.highlightsItem(dots.template.gender == 0, new Random().nextInt(51) + 130))); // hp 130-180k 15%
            }
        }
        //găng
        if (Util.isTrue(60, 100)) {
            if (gang.contains(tempId)) {
                dots.itemOptions.add(new ItemOption(0, Util.highlightsItem(dots.template.gender == 2, new Random().nextInt(7651) + 11000))); // 11000-18600
            }
        } else {
            if (gang.contains(tempId)) {
                dots.itemOptions.add(new ItemOption(0, Util.highlightsItem(dots.template.gender == 2, new Random().nextInt(7001) + 12000))); // gang 15% 12-19k -xayda 12k1
            }
        }
        //giày
        if (Util.isTrue(60, 100)) {
            if (giay.contains(tempId)) {
                dots.itemOptions.add(new ItemOption(23, Util.highlightsItem(dots.template.gender == 1, new Random().nextInt(21) + 120))); // ki 90-110k
            }
        } else {
            if (giay.contains(tempId)) {
                dots.itemOptions.add(new ItemOption(23, Util.highlightsItem(dots.template.gender == 1, new Random().nextInt(21) + 130))); // ki 110-130k
            }
        }

        if (nhan.contains(tempId)) {
            dots.itemOptions.add(new ItemOption(14, Util.highlightsItem(dots.template.gender == 1, new Random().nextInt(3) + 18))); // nhẫn 18-20%
        }
        dots.itemOptions.add(new ItemOption(21, 120));
        return dots;
    }

    public static Item ratiItemThanhTon(int tempId) {
        Item it = ItemService.gI().createItemSetKichHoat(tempId, 1);
        int ao = 1401;
        int quan = 1402;
        int giay = 1403;
        int gang = 1404;
        int nhan = 1405;
        if (ao == tempId) {
            it.itemOptions.add(new ItemOption(47, highlightsItem(it.template.gender == 2, new Random().nextInt(4501) + 5000)));
        }
        if (quan == tempId) {
            it.itemOptions.add(new ItemOption(22, highlightsItem(it.template.gender == 0, new Random().nextInt(200) + 200)));
        }
        if (gang == tempId) {
            it.itemOptions.add(new ItemOption(0, highlightsItem(it.template.gender == 2, new Random().nextInt(20000) + 20000)));
        }
        if (giay == tempId) {
            it.itemOptions.add(new ItemOption(23, highlightsItem(it.template.gender == 1, new Random().nextInt(190) + 200)));
        }
        if (nhan == tempId) {
            it.itemOptions.add(new ItemOption(14, new Random().nextInt(25) + 20));
        }
        it.itemOptions.add(new ItemOption(21, 200));
        return it;
    }

    public static Item ratiItemDacBiet(int tempId) {
        Item it = ItemService.gI().createItemSetKichHoat(tempId, 1);
        int ao = 1973;
        int quan = 1974;
        int giay = 1975;
        int gang = 1976;
        int nhan = 1977;
        if (ao == tempId) {
            it.itemOptions.add(new ItemOption(47, highlightsItem(it.template.gender == 2, new Random().nextInt(10000) + 10000)));
        }
        if (quan == tempId) {
            it.itemOptions.add(new ItemOption(22, highlightsItem(it.template.gender == 0, new Random().nextInt(500) + 500)));
        }
        if (gang == tempId) {
            it.itemOptions.add(new ItemOption(0, highlightsItem(it.template.gender == 2, new Random().nextInt(50000) + 50000)));
        }
        if (giay == tempId) {
            it.itemOptions.add(new ItemOption(23, highlightsItem(it.template.gender == 1, new Random().nextInt(500) + 500)));
        }
        if (nhan == tempId) {
            it.itemOptions.add(new ItemOption(14, new Random().nextInt(20) + 50));
        }
        it.itemOptions.add(new ItemOption(21, 200));
        return it;
    }

    public static Item ratiItemDacBietDeTu(int tempId) {
        Item it = ItemService.gI().createItemSetKichHoat(tempId, 1);
        int ao = 1973;
        int quan = 1974;
        int giay = 1975;
        int gang = 1976;
        int nhan = 1977;
        it.itemOptions.add(new ItemOption(215, 1));
        if (ao == tempId) {
            it.itemOptions.add(new ItemOption(47, 20000));
        }
        if (quan == tempId) {
            it.itemOptions.add(new ItemOption(6, 1000000));
        }
        if (gang == tempId) {
            it.itemOptions.add(new ItemOption(0, 100000));
        }
        if (giay == tempId) {
            it.itemOptions.add(new ItemOption(7, 1000000));
        }
        if (nhan == tempId) {
            it.itemOptions.add(new ItemOption(14, 70));
        }
        it.itemOptions.add(new ItemOption(50, 200));
        it.itemOptions.add(new ItemOption(77, 200));
        it.itemOptions.add(new ItemOption(103, 200));
        it.itemOptions.add(new ItemOption(101, 500));
        it.itemOptions.add(new ItemOption(197, 1));
        return it;
    }

    public static Item ratiItemDacBietFull(int tempId) {
        Item it = ItemService.gI().createItemSetKichHoat(tempId, 1);
        int ao = 1973;
        int quan = 1974;
        int giay = 1975;
        int gang = 1976;
        int nhan = 1977;
        if (ao == tempId) {
            it.itemOptions.add(new ItemOption(47, highlightsItem(it.template.gender == 2, 10000 + 10000)));
        }
        if (quan == tempId) {
            it.itemOptions.add(new ItemOption(22, highlightsItem(it.template.gender == 0, 500 + 500)));
        }
        if (gang == tempId) {
            it.itemOptions.add(new ItemOption(0, highlightsItem(it.template.gender == 2, 50000 + 50000)));
        }
        if (giay == tempId) {
            it.itemOptions.add(new ItemOption(23, highlightsItem(it.template.gender == 1, 500 + 500)));
        }
        if (nhan == tempId) {
            it.itemOptions.add(new ItemOption(14, 20 + 50));
        }
        it.itemOptions.add(new ItemOption(21, 200));
        return it;
    }

    public static Item ratiItemDeVuong(int tempId) {
        Item it = ItemService.gI().createItemSetKichHoat(tempId, 1);
        int ao = 2086;
        int quan = 2087;
        int giay = 2088;
        int gang = 2089;
        int nhan = 2090;
        if (ao == tempId) {
            it.itemOptions.add(new ItemOption(47, highlightsItem(it.template.gender == 2, new Random().nextInt(4501) + 7000)));
        }
        if (quan == tempId) {
            it.itemOptions.add(new ItemOption(22, highlightsItem(it.template.gender == 0, new Random().nextInt(300) + 300)));
        }
        if (gang == tempId) {
            it.itemOptions.add(new ItemOption(0, highlightsItem(it.template.gender == 2, new Random().nextInt(30000) + 30000)));
        }
        if (giay == tempId) {
            it.itemOptions.add(new ItemOption(23, highlightsItem(it.template.gender == 1, new Random().nextInt(290) + 300)));
        }
        if (nhan == tempId) {
            it.itemOptions.add(new ItemOption(14, new Random().nextInt(25) + 30));
        }
        it.itemOptions.add(new ItemOption(21, 200));
        return it;
    }

    public static Item ratiItemVuongThan(int tempId) {
        Item it = ItemService.gI().createItemSetKichHoat(tempId, 1);
        int ao = 1711;
        int quan = 1712;
        int giay = 1713;
        int gang = 1714;
        int nhan = 1715;
        if (ao == tempId) {
            it.itemOptions.add(new ItemOption(47, highlightsItem(it.template.gender == 2, new Random().nextInt(3501) + 5000)));
        }
        if (quan == tempId) {
            it.itemOptions.add(new ItemOption(22, highlightsItem(it.template.gender == 0, new Random().nextInt(150) + 150)));
        }
        if (gang == tempId) {
            it.itemOptions.add(new ItemOption(0, highlightsItem(it.template.gender == 2, new Random().nextInt(15000) + 15000)));
        }
        if (giay == tempId) {
            it.itemOptions.add(new ItemOption(23, highlightsItem(it.template.gender == 1, new Random().nextInt(140) + 150)));
        }
        if (nhan == tempId) {
            it.itemOptions.add(new ItemOption(14, new Random().nextInt(20) + 20));
        }
        it.itemOptions.add(new ItemOption(21, 200));
        return it;
    }

    private static final int TI_LE_OPTION_HIEM = 20; // Tỷ lệ % xuất hiện option hiếm

    public static ItemMap Randomchisochungsinhbinhdang(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, y, playerId);

        List<ItemOption> optionPhoBien = new ArrayList<>();
        optionPhoBien.add(new ItemOption(50, Util.nextInt(100, 250)));   // Sức đánh %
        optionPhoBien.add(new ItemOption(77, Util.nextInt(100, 250)));   // HP %
        optionPhoBien.add(new ItemOption(103, Util.nextInt(100, 250)));  // KI %
        optionPhoBien.add(new ItemOption(22, Util.nextInt(500, 1000))); // +k HP
        optionPhoBien.add(new ItemOption(23, Util.nextInt(500, 1000))); // +k KI

        List<ItemOption> optionHiem = new ArrayList<>();
        optionHiem.add(new ItemOption(95, Util.nextInt(1, 20)));   // Hút HP
        optionHiem.add(new ItemOption(96, Util.nextInt(1, 20)));   // Hút KI
        optionHiem.add(new ItemOption(97, Util.nextInt(1, 20)));   // Phản sát thương
        optionHiem.add(new ItemOption(108, Util.nextInt(1, 20)));  // Né đòn
        optionHiem.add(new ItemOption(98, Util.nextInt(1, 20)));   // Xuyên giáp
        optionHiem.add(new ItemOption(94, Util.nextInt(1, 20)));   // Giáp %

        Collections.shuffle(optionPhoBien);
        Collections.shuffle(optionHiem);
        int soOption = Util.nextInt(4, 8); // option tối đa mỗi trang bị
        while (it.options.size() < soOption) {
            if (Util.nextInt(100) < TI_LE_OPTION_HIEM && !optionHiem.isEmpty()) {
                it.options.add(optionHiem.remove(0));
            } else if (!optionPhoBien.isEmpty()) {
                it.options.add(optionPhoBien.remove(0));
            } else if (!optionHiem.isEmpty()) {
                it.options.add(optionHiem.remove(0));
            } else {
                break;
            }
        }
        it.options.add(new ItemOption(196, 1)); // Item ảo
        it.options.add(new ItemOption(30, 1));  // Khóa giao dịch
        return it;
    }

    public static ItemMap khongthegiaodichchungsinhbinhdang(Zone zone, int tempId, int quantity, int x, int y, long playerId) {
        ItemMap it = new ItemMap(zone, tempId, quantity, x, y, playerId);
        it.options.add(new ItemOption(196, 1)); // Item ảo
        it.options.add(new ItemOption(30, 1));  // Khóa giao dịch
        return it;
    }
}
