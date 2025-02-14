package com.app.services;

import com.app.entites.Brand;
import com.app.entites.Product;

import java.util.List;

public interface BrandService {
    Brand createBrand(Long brandId, String brandName, List<Product> products);

    Brand getBrand(Long brandId);

    List<Brand> getAllBrands();

    Brand updateBrand(Long brandId, Brand brand);

    void deleteBrand(Long brandId);

    Brand addProductToBrand(Long brandId, Product product);

    Brand deleteProductFromBrand(Long brandId, Product product);
}
