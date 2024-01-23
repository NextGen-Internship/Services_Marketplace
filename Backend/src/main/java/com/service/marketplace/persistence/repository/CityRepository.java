package com.service.marketplace.persistence.repository;

import com.service.marketplace.persistence.entity.City;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CityRepository extends JpaRepository<City, Integer> {
}
