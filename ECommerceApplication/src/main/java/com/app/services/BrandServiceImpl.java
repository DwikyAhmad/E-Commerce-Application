package com.app.services;

import com.app.entites.Brand;
import com.app.entites.Product;
import com.app.exceptions.APIException;
import com.app.exceptions.ResourceNotFoundException;
import com.app.repositories.BrandRepo;
import com.app.repositories.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
public class BrandServiceImpl implements BrandService{
    @Autowired
    private BrandRepo brandRepo;
    @Autowired
    private ProductRepo productRepo;

    @Override
    public Brand createBrand(Long brandId, String brandName, List<Product> products) {
        if (brandRepo.findByBrandId(brandId).isPresent()) {
            throw new APIException("Brand already exists with brandId: " + brandId + " !!!");
        }
        if (products != null) {
            products = new ArrayList<>();
        }

        Brand brand = new Brand(brandId, brandName, products);

        return brandRepo.save(brand);
    }

    @Override
    public Brand getBrand(Long brandId) {
        return brandRepo.findByBrandId(brandId).orElseThrow(
                () -> new ResourceNotFoundException("Brand", "brandId", brandId));
    }

    @Override
    public List<Brand> getAllBrands() {
        return brandRepo.findAll();
    }

    @Override
    public Brand updateBrand(Long brandId, Brand brand) {
        Brand brandToUpdate = brandRepo.findByBrandId(brandId).orElseThrow(
                () -> new ResourceNotFoundException("Brand", "brandId", brandId));

        brandToUpdate.setBrandName(brand.getBrandName());
        return brandRepo.save(brandToUpdate);
    }

    @Override
    public void deleteBrand(Long brandId) {
        Brand brandToDelete = brandRepo.findByBrandId(brandId).orElseThrow(
                () -> new ResourceNotFoundException("Brand", "brandId", brandId));

        List<Product> products = brandToDelete.getProducts();

        for (Product product : products) {
            product.setBrand(null);
            productRepo.save(product);
        }

        brandRepo.delete(brandToDelete);
    }

    @Override
    public Brand addProductToBrand(Long brandId, Product product) {
        Long productId = product.getProductId();
        product = productRepo.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product", "productId", productId));

        Brand brand = brandRepo.findByBrandId(brandId).orElseThrow(
                () -> new ResourceNotFoundException("Brand", "brandId", brandId));

        boolean isProductNotPresent = true;
        List<Product> products = brand.getProducts();

        for (Product listProduct : products) {
            if (listProduct.getProductName().equals(product.getProductName())
                    && listProduct.getDescription().equals(product.getDescription())) {

                isProductNotPresent = false;
                break;
            }
        }

        if (isProductNotPresent) {
            products.add(product);
            brand.setProducts(products);

            product.setBrand(brand);
            productRepo.save(product);

            return brandRepo.save(brand);
        } else {
            throw new APIException("Product already exists !!!");
        }
    }

    @Override
    public List<Product> getBrandProducts(Long brandId) {
        Brand brand = brandRepo.findByBrandId(brandId).orElseThrow(
                () -> new ResourceNotFoundException("Brand", "brandId", brandId));

        return brand.getProducts();
    }

    @Override
    public Brand deleteProductFromBrand(Long brandId, Product product) {
        Long productId = product.getProductId();
        product = productRepo.findById(productId).orElseThrow(
                () -> new ResourceNotFoundException("Product", "productId", productId));

        Brand brand = brandRepo.findByBrandId(brandId).orElseThrow(
                () -> new ResourceNotFoundException("Brand", "brandId", brandId));

        boolean isProductNotPresent = true;
        List<Product> products = brand.getProducts();

        for (Product listProduct : products) {
            if (listProduct.getProductName().equals(product.getProductName())
                    && listProduct.getDescription().equals(product.getDescription())) {

                isProductNotPresent = false;
                break;
            }
        }

        if (!isProductNotPresent) {
            products.remove(product);
            brand.setProducts(products);

            product.setBrand(null);
            productRepo.save(product);

            return brandRepo.save(brand);
        } else {
            throw new APIException("Product already exists !!!");
        }
    }
}
