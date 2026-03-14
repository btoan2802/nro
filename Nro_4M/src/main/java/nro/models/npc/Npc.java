package nro.models.npc;

import nro.consts.ConstNpc;
import nro.models.map.Map;
import nro.models.map.Zone;
import nro.models.player.Player;
import nro.server.Client;
import nro.server.Manager;
import nro.server.io.Message;
import nro.services.MapService;
import nro.services.Service;
import nro.services.func.ShopService;
import nro.utils.Log;
import nro.utils.Util;

public abstract class Npc implements IAtionNpc {

    public int mapId;
    public Map map;

    public int status;

    public int cx;

    public int cy;

    public int tempId;

    public int avartar;

    public BaseMenu baseMenu;

    public long LastTimeAutoChatQuyLao = 0;

    public long LastTimeAutoChatCSBD = 0;

    public void AutoChat() {
        switch (tempId) {
            case 13: //id 13 là quy lão
                if (System.currentTimeMillis() - LastTimeAutoChatQuyLao >= 7000) {
                    LastTimeAutoChatQuyLao = System.currentTimeMillis();

                    for (Player pl : Client.gI().getPlayers()) {
                        if (pl.zone.map.mapId == mapId && pl.isPl()) {
                            npcChat(pl, getText(tempId));
                        }
                    }
                }
                break;
            case 102: //id 102 là chúng sinh bình đẳng
                if (System.currentTimeMillis() - LastTimeAutoChatCSBD >= 5000) {
                    LastTimeAutoChatCSBD = System.currentTimeMillis();

                    for (Player pl : Client.gI().getPlayers()) {
                        if (pl.zone.map.mapId == mapId && pl.isPl()) {
                            npcChat(pl, getText(tempId));
                        }
                    }
                }
                break;
            default:
                break;
        }
    }

    private String getText(int id) {
        return switch (id) {
            case 13 ->
                textQuyLao[Util.nextInt(0, textQuyLao.length - 1)];
            case 95 ->
                textLyTieuNuong[Util.nextInt(0, textLyTieuNuong.length - 1)];
            case 22 ->
                textLyTrongTai[Util.nextInt(0, textLyTrongTai.length - 1)];
            case 102 ->
                textChungSinhBinhDang[Util.nextInt(0, textChungSinhBinhDang.length - 1)];
            default ->
                textSanta[Util.nextInt(0, textSanta.length - 1)];
        };
    }

    private static final String[] textLyTieuNuong = new String[]{
        "Con số gì đây..",};

    private static final String[] textLyTrongTai = new String[]{
        "Đại Hội Võ Thuật lần thứ 23 đã chính thức khai mạc",
        "Còn chờ gì nữa mà không đăng kí tham gia để nhận nhiều phẩn quà hấp dẫn"
    };

    private static final String[] textSanta = new String[]{
        "Mọi sự cố gắng đều sẽ được đền đáp xứng đáng.",};

    private static final String[] textQuyLao = new String[]{
        "Lá Là La...",
        "Ngày tươi đẹp nhất là ngày được may mắn nhìn thấy em",
        "Tình yêu không cần phải hoàn hảo, chỉ cần sự chân thật. ",
        "Tôi là một ngọn lửa cháy lên, nhưng sẽ không thể bị lụi tàn. "
    };
    private static final String[] textChungSinhBinhDang = new String[]{
        "Chào mừng đến với đấu trường Chúng Sinh Bình Đẳng!",
        "Nơi đây không có kẻ mạnh hay yếu, chỉ có bản lĩnh và chiến thuật!",
        "Chỉ những chiến binh dũng cảm mới có thể đứng vững tại nơi này.",
        "Hãy cẩn thận, mọi đòn tấn công đều có thể là đòn kết liễu!",
        "Kẻ sống sót cuối cùng sẽ được vinh danh khắp thiên hạ!"
    };

    protected Npc(int mapId, int status, int cx, int cy, int tempId, int avartar) {
        this.map = MapService.gI().getMapById(mapId);
        this.mapId = mapId;
        this.status = status;
        this.cx = cx;
        this.cy = cy;
        this.tempId = tempId;
        this.avartar = avartar;
        Manager.NPCS.add(this);
    }

    public void initBaseMenu(String text) {
        text = text.substring(1);
        String[] data = text.split("\\|");
        baseMenu = new BaseMenu();
        baseMenu.npcId = tempId;
        baseMenu.npcSay = data[0].replaceAll("<>", "\n");
        baseMenu.menuSelect = new String[data.length - 1];
        for (int i = 0; i < baseMenu.menuSelect.length; i++) {
            baseMenu.menuSelect[i] = data[i + 1].replaceAll("<>", "\n");
        }
    }

    public void createOtherMenu(Player player, int indexMenu, String npcSay, String... menuSelect) {
        Message msg;
        try {
            player.iDMark.setIndexMenu(indexMenu);
            msg = new Message(32);
            msg.writer().writeShort(tempId);
            msg.writer().writeUTF(npcSay);
            msg.writer().writeByte(menuSelect.length);
            for (String menu : menuSelect) {
                msg.writer().writeUTF(menu);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void createOtherMenu(Player player, int indexMenu, String npcSay, String[] menuSelect, Object object) {
        NpcFactory.PLAYERID_OBJECT.put(player.id, object);
        Message msg;
        try {
            player.iDMark.setIndexMenu(indexMenu);
            msg = new Message(32);
            msg.writer().writeShort(tempId);
            msg.writer().writeUTF(npcSay);
            msg.writer().writeByte(menuSelect.length);
            for (String menu : menuSelect) {
                msg.writer().writeUTF(menu);
            }
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void openBaseMenu(Player player) {
        if (canOpenNpc(player)) {
            player.iDMark.setIndexMenu(ConstNpc.BASE_MENU);
            try {
                if (baseMenu != null) {
                    baseMenu.openMenu(player);
                } else {
                    Message msg;
                    msg = new Message(32);
                    msg.writer().writeShort(tempId);
                    msg.writer().writeUTF("NPC " + this.tempId + " map " + this.mapId + " chưa làm!");
                    msg.writer().writeByte(1);
                    msg.writer().writeUTF("Đóng");
                    player.sendMessage(msg);
                    msg.cleanup();
                }
            } catch (Exception e) {
                Log.error(Npc.class, e);
            }
        }
    }

    public void npcChat(Player player, String text) {
        Message msg;
        try {
            msg = new Message(124);
            msg.writer().writeShort(tempId);
            msg.writer().writeUTF(text);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception e) {
            Log.error(Service.class, e);
        }
    }

    public void npcChat(Zone zone, String text) {
        Message msg;
        try {
            msg = new Message(124);
            msg.writer().writeShort(tempId);
            msg.writer().writeUTF(text);
            Service.getInstance().sendMessAllPlayerInMap(zone, msg);
            msg.cleanup();
        } catch (Exception e) {
            Log.error(Service.class, e);
        }
    }

    public void npcChat(String text) {
        Message msg;
        try {
            msg = new Message(124);
            msg.writer().writeShort(tempId);
            msg.writer().writeUTF(text);
            for (Zone zone : map.zones) {
                Service.getInstance().sendMessAllPlayerInMap(zone, msg);
            }
            msg.cleanup();
        } catch (Exception e) {
            Log.error(Service.class, e);
        }
    }

    public void openShopWithGender(Player player, int shopId, int order) {
        ShopService.gI().openShopNormal(player, this, shopId, order, player.gender);
    }

    public void openShop(Player player, int shopId, int order) {
        ShopService.gI().openShopNormal(player, this, shopId, order, -1);
    }

    public boolean canOpenNpc(Player player) {
        if (this.tempId == ConstNpc.DAU_THAN) {
            if (player.zone.map.mapId == 21
                    || player.zone.map.mapId == 22
                    || player.zone.map.mapId == 23) {
                return true;
            } else {
                Service.getInstance().hideWaitDialog(player);
                Service.getInstance().sendThongBao(player, "Không thể thực hiện");
                return false;
            }
        }
        if (player.zone.map.mapId == this.mapId
                && Util.getDistance(this.cx, this.cy, player.location.x, player.location.y) <= 60) {
            return true;
        } else {
            Service.getInstance().hideWaitDialog(player);
            Service.getInstance().sendThongBao(player, "Không thể thực hiện khi đứng quá xa");
            return false;
        }
    }
    
    public void hide_npc(Player player, int idnpc, int action) {
        Message msg;
        try {
            msg = new Message(-73);
            msg.writer().writeByte(idnpc);
            msg.writer().writeByte(action);
            player.sendMessage(msg);
            msg.cleanup();
        } catch (Exception ex) {

        }
    }

}
