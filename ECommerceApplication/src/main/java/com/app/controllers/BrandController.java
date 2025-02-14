package com.app.controllers;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.payloads.BrandDTO;
import com.app.payloads.ResponseDTO;
import com.app.services.BrandService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.http.ResponseEntity;
import com.app.entites.Brand;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/api")
@SecurityRequirement(name = "E-Commerce Application")
public class BrandController {
    
    @Autowired
    private BrandService brandService;

    @PostMapping("/admin/brand")
    public ResponseEntity<?> createBrand(@RequestBody BrandDTO brandDTO) {
        String brandName = brandDTO.getBrandName();

        Brand brand = brandService.createBrand(brandName);
        String responseMsg = "Brand created successfully";
        ResponseDTO response = new ResponseDTO(responseMsg, brand, 201);

        return new ResponseEntity<ResponseDTO>(response, HttpStatus.CREATED);
    }
    
    @GetMapping("/public/brands")
    public ResponseEntity<?> getBrands() {
        List<Brand> brands = brandService.getAllBrands();
        String message = "All brands fetched successfully";
        ResponseDTO response = new ResponseDTO(message, brands, 200);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/public/brand/{id}")
    public ResponseEntity<?> getBrand(@PathVariable long id) {
        Brand brand = brandService.getBrand(id);
        String message = "Brand fetched successfully";
        ResponseDTO response = new ResponseDTO(message, brand, 200);

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @PutMapping("/admin/brand/{id}")
    public ResponseEntity<?> updateBrand(@PathVariable long id, @RequestBody BrandDTO brandDTO) {
        Brand brand = brandService.getBrand(id);
        brand.setBrandName(brandDTO.getBrandName());
        Brand updatedBrand = brandService.updateBrand(id, brand);

        ResponseDTO response = new ResponseDTO("Brand updated successfully", updatedBrand, 200);
        return new ResponseEntity<ResponseDTO>(response, HttpStatus.OK);
    }

    @DeleteMapping("/admin/brand/{id}")
    public ResponseEntity<?> deleteBrand(@PathVariable long id) {
        brandService.deleteBrand(id);
        ResponseDTO response = new ResponseDTO("Brand deleted successfully", null, 200);
        return new ResponseEntity<ResponseDTO>(response, HttpStatus.OK);
    }

}
