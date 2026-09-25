package ua.com.foxminded.service;

import java.util.List;

public interface ReadUpdateDeleteService<E, D, I>  {
    
    E findById(I id);
    List<E> findAll();
    E fullUpdate(D dto, I id);
    void deleteById(I id);
}
