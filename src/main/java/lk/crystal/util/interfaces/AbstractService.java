package lk.crystal.util.interfaces;

import java.util.List;

public interface AbstractService<E, I> {
    /**
     * Find All From The DB relevant to given Entity class
     */
    List<E> findAll();

    /**
     * Find one From The DB relevant to given Entity id
     */
    E findById(I id);

    /**
     * Save or Update to The DB given Entity details from frontend
     */
    E persist(E e);

    /**
     * Delete From The DB relevant to given Entity id
     */
    boolean delete(I id);

    /**
     * Find All From The DB relevant to given Entity one parameter or many parameter
     */
    List<E> search(E e);

}
