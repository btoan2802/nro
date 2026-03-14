package nro.manager;

/**
 * @author Hoàng Việt - 0857853150
 */

public interface IManager <E> {

    void add(E e);

    void remove(E e);

    E findByID(int id);
}
