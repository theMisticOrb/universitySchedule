package ua.com.foxminded.service;

public interface FullCrudService<E, D, I> extends ReadUpdateDeleteService<E, D, I> {

    E addOne(D dto);
}
