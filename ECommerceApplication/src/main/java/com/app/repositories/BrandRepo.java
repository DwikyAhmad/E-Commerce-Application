package com.app.repositories;

import com.app.entites.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BrandRepo extends JpaRepository<Brand, Long> {
    Optional<Brand> findByBrandId(Long brandId);
}
