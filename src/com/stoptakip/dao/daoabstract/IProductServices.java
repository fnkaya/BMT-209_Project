package com.stoptakip.dao.daoabstract;

import com.stoptakip.dto.models.Category;
import com.stoptakip.dto.models.Product;

import java.util.ArrayList;

public interface IProductServices extends IExtendedGenericServices<Product> {

    int getCategoryId(Product entity);
    ArrayList<Product> getAllByCategoryId();
    boolean insertRelation(Product productEntity, Category categoryEntity);
    boolean deleteRelation(Product entity);
    boolean updateRelation(Product productEntity, Category categoryEntity);
}
