package com.stoptakip.dao.daoabstract;

import java.util.ArrayList;

public interface IExtendedGenericServices<T> extends IGenericServices<T> {

    ArrayList<T> getAllByName(String name);
}
