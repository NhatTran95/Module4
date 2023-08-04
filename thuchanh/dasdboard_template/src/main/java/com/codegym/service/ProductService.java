package com.codegym.service;

import com.codegym.model.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductService implements IProductService{
    private static final Map<Integer, Product> products;
    static {
        products = new HashMap<Integer, Product>();
        products.put(1, new Product(1, "iphone 8", "beautiful",5000));
        products.put(2, new Product(2, "iphone 11", "beautiful",10000));
        products.put(3, new Product(3, "iphone 12", "beautiful",12000));
        products.put(4, new Product(4, "iphone 13", "beautiful",14000));
    }
//    static {
//        products = new HashMap<Integer, Product>();
//        products.put(1, new Product(1, "iphone 8", "beautiful",5000, "2023-02-20"));
//        products.put(2, new Product(2, "iphone 11", "beautiful",10000, "2023-02-20"));
//        products.put(3, new Product(3, "iphone 12", "beautiful",12000, "2023-02-20"));
//        products.put(4, new Product(4, "iphone 13", "beautiful",14000, "2023-02-20"));
//    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<Product>(products.values());
    }

    @Override
    public void save(Product product) {
        products.put(product.getId(), product);
    }

    @Override
    public Product findById(int id) {
        return products.get(id);
    }

    @Override
    public void update(int id, Product product) {
        products.put(id, product);
    }

    @Override
    public void remove(int id) {
        products.remove(id);
    }
}
