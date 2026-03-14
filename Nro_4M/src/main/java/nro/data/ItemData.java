package nro.data;

import java.util.Arrays;
import java.util.List;
import nro.models.item.ItemOptionTemplate;
import nro.models.item.ItemTemplate;
import nro.server.Manager;
import nro.server.io.Message;
import nro.server.io.Session;

public class ItemData {

    public static List<Integer> list_Do_tl = Arrays.asList(555, 557, 559, 556, 558, 560, 562, 564, 566, 563, 565, 567, 561);
    public static List<Integer> list_Do_hd = Arrays.asList(650, 651, 652, 653, 654, 655, 656, 657, 658, 659, 660, 661, 662);
    public static List<Integer> list_Do_thiensu = Arrays.asList(1048, 1051, 1054, 1057, 1060, 1049, 1052, 1055, 1058, 1061, 1050, 1053, 1056, 1059, 1062);
    public static List<Integer> list_thuc_an = Arrays.asList(663, 664, 665, 666, 667);

    public static List<Integer> list_dapdo = Arrays.asList(1196, 1197, 1198, 1221,
            1222, 1223, 1229, 1230, 1326, 1462, 1415, 1524, 1502, 1345, 1297, 1298, 1200, 1606,
            1607, 1608, 1619, 1641, 1642, 1644, 1668, 1669, 1670, 1676, 1677, 1678, 1679, 1680, 1681, 1682, 1683,
            1661, 1247, 1665, 1664, 1718, 1719, 1391, 1497, 1498, 1142, 1657, 2023, 1650, 1653, 1651, 2020, 2021,
            2022, 1325, 1544, 1545, 1546, 1983, 1663, 1984, 2073, 2074, 2075, 2076, 2077, 1170, 1171, 1172, 1211,
            1212, 1213, 1221, 1222, 1223, 1784, 1785, 1786, 734, 1663, 2015, 1973, 1974, 1975, 1976, 1977, 1785, 1786,
            1789, 1790, 2013, 2014);//pháp sư hóa

    public static List<Integer> phieu = Arrays.asList(459);

    public static List<Integer> IdMiniPet = Arrays.asList(936, 892, 893, 942, 943, 944, 967, 1039,
            1040, 1046, 916, 917, 918, 919, 1188, 1202, 1203, 1207, 1213, 1243, 1244,
            1196, 1197, 1198, 1221, 1222, 1223, 1229, 1230, 1415, 1462, 1497, 1498, 1524, 2017,
            2018, 1606, 1607, 1608, 1619, 1641, 1642, 1644, 1668, 1669, 1670, 1676, 1677, 1678, 1679,
            1680, 1681, 1682, 1683, 1718, 1719, 1391, 1140, 1983, 2073, 2074, 2075, 2076, 2077);

    public static List<Integer> itemDacBiet = Arrays.asList(1416);//bán vpham hoàn trả

    public static List<Integer> caitrang_update = Arrays.asList(2035, 2036, 1998, 1997, 2062,2058,1744,1858,
    1884,1883,1885,1879,1894,1887,1838,1834,1844,1880); // ct nâng cấp

    //------------------------------------------------------ start update client
    public static void updateItem(Session session) {
        updateItemOptionItemplate(session);
        updateTocBay(session);
        updateItemTemplate(session, 930);
        updateItemTemplate(session, 930, Manager.ITEM_TEMPLATES.size());
    }

    private static final short[][] head3Htinh = {
        {1804, 1803, 1800}, {1822, 1823, 1824}, {1827, 1828, 1829}, {1847, 1848, 1849},
        {1837, 1838, 1839}, {1832, 1833, 1834}, {1844, 1843, 1842}, {1880, 1881, 1882},
        {1883, 1884, 1885}, {1886, 1887, 1888}, {1889, 1890, 1891}, {1892, 1893, 1894},
        {1897, 1898, 1899}, {1900, 1901, 1902}, {1903, 1904, 1905}, {1906, 1907, 1908},
        {1909, 1910, 1911}, {1912, 1913, 1914}, {1917, 1918, 1919}, {1920, 1921, 1922},
        {1923, 1924, 1925}, {1926, 1927, 1928}, {1929, 1930, 1931}, {1853, 1854, 1855},
        {1858, 1859, 1860}, {1864, 1865, 1866}, {1789, 1799, 1809}, {1586, 1587, 1588},
        {1640, 1641, 1642}, {1649, 1650, 1651}, {1550, 1551, 1552}, {1556, 1557, 1558},
        {1598, 1599, 1600}, {1571, 1572, 1573}, {2051, 2052, 2053}, {2054, 2055, 2056},
        {2060, 2061, 2062}
    };

    private static void updateTocBay(Session session) {
        Message msg;
        try {
            msg = new Message(-28);
            msg.writer().writeByte(8);
            msg.writer().writeByte(DataGame.vsItem);//vcitem
            msg.writer().writeByte(100); //type NroItem100
            msg.writer().writeShort(head3Htinh.length);//tổng list lặp head
            for (int i = 0; i < head3Htinh.length; i++) {
                msg.writer().writeByte(head3Htinh[i].length);//chiều dài 1 chuỗi
                for (int j = 0; j < head3Htinh[i].length; j++) {
                    msg.writer().writeShort(head3Htinh[i][j]);
                }
            }
            session.doSendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
        }
    }

    private static void updateItemOptionItemplate(Session session) {
        Message msg;
        try {
            msg = new Message(-28);
            msg.writer().writeByte(8);
            msg.writer().writeByte(DataGame.vsItem); //vcitem
            msg.writer().writeByte(0); //update option
            msg.writer().writeByte(Manager.ITEM_OPTION_TEMPLATES.size());
            for (ItemOptionTemplate io : Manager.ITEM_OPTION_TEMPLATES) {
                msg.writer().writeUTF(io.name);
                msg.writer().writeByte(io.type);
            }
            session.doSendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {

        }
    }

    private static void updateItemTemplate(Session session, int count) {
        Message msg;
        try {
            msg = new Message(-28);
            msg.writer().writeByte(8);

            msg.writer().writeByte(DataGame.vsItem); //vcitem
            msg.writer().writeByte(1); //reload itemtemplate
            msg.writer().writeShort(count);
            for (int i = 0; i < count; i++) {
                ItemTemplate itemTemplate = Manager.ITEM_TEMPLATES.get(i);
                msg.writer().writeByte(itemTemplate.type);
                msg.writer().writeByte(itemTemplate.gender);
                msg.writer().writeUTF(itemTemplate.name);
                msg.writer().writeUTF(itemTemplate.description);
                msg.writer().writeByte(itemTemplate.level);
                msg.writer().writeInt(itemTemplate.strRequire);
                msg.writer().writeShort(itemTemplate.iconID);
                msg.writer().writeShort(itemTemplate.part);
                msg.writer().writeBoolean(itemTemplate.isUpToUp);
            }
            session.doSendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void updateItemTemplate(Session session, int start, int end) {
        Message msg;
        try {
            msg = new Message(-28);
            msg.writer().writeByte(8);

            msg.writer().writeByte(DataGame.vsItem); //vcitem
            msg.writer().writeByte(2); //add itemtemplate
            msg.writer().writeShort(start);
            msg.writer().writeShort(end);
            for (int i = start; i < end; i++) {
//                System.out.println("start: " + start + " -> " + end + " id " + Manager.ITEM_TEMPLATES.get(i).id);
                msg.writer().writeByte(Manager.ITEM_TEMPLATES.get(i).type);
                msg.writer().writeByte(Manager.ITEM_TEMPLATES.get(i).gender);
                msg.writer().writeUTF(Manager.ITEM_TEMPLATES.get(i).name);
                msg.writer().writeUTF(Manager.ITEM_TEMPLATES.get(i).description);
                msg.writer().writeByte(Manager.ITEM_TEMPLATES.get(i).level);
                msg.writer().writeInt(Manager.ITEM_TEMPLATES.get(i).strRequire);
                msg.writer().writeShort(Manager.ITEM_TEMPLATES.get(i).iconID);
                msg.writer().writeShort(Manager.ITEM_TEMPLATES.get(i).part);
                msg.writer().writeBoolean(Manager.ITEM_TEMPLATES.get(i).isUpToUp);
            }
            session.doSendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //-------------------------------------------------------- end update client
}
