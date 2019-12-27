package com.stoptakip.dao.daoabstract;

import java.util.ArrayList;

public interface IGenericServices<T> {

    boolean insert(T entity);
    boolean delete(T entity);
    boolean update(T entity);
    boolean control(T entity);
    T get(int id);
    ArrayList<T> getAll();
}
