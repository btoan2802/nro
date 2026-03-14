package nro.models.player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerManager {
    private static PlayerManager instance;
    private final Map<Long, Map<Integer, Integer>> playerRewardStates = new ConcurrentHashMap<>();
    private final Map<Long, List<Integer>> playerDailyRewardLists = new ConcurrentHashMap<>();
    private final Map<Long, List<Integer>> playerPermanentRewardLists = new ConcurrentHashMap<>();

    private PlayerManager() {
    }

    public static PlayerManager getInstance() {
        if (instance == null) {
            instance = new PlayerManager();
        }
        return instance;
    }

    public Map<Integer, Integer> getRewardState(Player player) {
        return playerRewardStates.computeIfAbsent(player.id, k -> new HashMap<>());
    }

    public List<Integer> getDailyRewardList(Player player) {
        return playerDailyRewardLists.computeIfAbsent(player.id, k -> new ArrayList<>());
    }

    public List<Integer> getPermanentRewardList(Player player) {
        return playerPermanentRewardLists.computeIfAbsent(player.id, k -> new ArrayList<>());
    }

    public void clearRewardState(long playerId) {
        playerRewardStates.remove(playerId);
    }

    public void clearDailyRewardList(long playerId) {
        playerDailyRewardLists.remove(playerId);
    }

    public void resetAllRewardStates() {
        playerRewardStates.clear();
        playerDailyRewardLists.clear();
    }
}