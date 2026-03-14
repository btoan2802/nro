package nro.dialog;

import lombok.Getter;
import lombok.Setter;

/**
 * @author Hoàng Việt - 0857853150
 */
@Setter
@Getter
public abstract class MenuRunable implements Runnable {
    private int indexSelected;
}
