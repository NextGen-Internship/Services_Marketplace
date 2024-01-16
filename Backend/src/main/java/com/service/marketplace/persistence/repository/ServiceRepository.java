package com.service.marketplace.persistence.repository;

import com.service.marketplace.persistence.entity.Category;
import com.service.marketplace.persistence.entity.City;
import com.service.marketplace.persistence.entity.Service;
import com.service.marketplace.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRepository extends JpaRepository<Service, Integer> {
    List<Service> findByCategory(Category category);
    List<Service> findByProvider(User user);

    @Query("SELECT s FROM Service s JOIN s.cities c WHERE c IN :cities")
    List<Service> findByCitiesUsingQuery(@Param("cities") List<City> cities);
}
