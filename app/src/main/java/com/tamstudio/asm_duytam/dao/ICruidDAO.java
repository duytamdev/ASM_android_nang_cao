package com.tamstudio.asm_duytam.dao;

import java.util.List;

public interface ICruidDAO <T,E>{

    List<T>getAll();
    T getById(E id);
    boolean insert(T entity);
    boolean update(T entity);
    boolean delete(E id);

}
