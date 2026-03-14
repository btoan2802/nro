package nro.services;

import java.util.ArrayList;
import java.util.List;
import nro.models.item.Item;
import nro.models.item.ItemOption;

/**
 *
 * @author Hoàng Việt - 0857853150
 *
 */
public class RuongSuuTamTemplate{

    public int id;
    public byte type;
    public int id_item;
    public List<ItemOption> optionRuong = new ArrayList<>();
}
