package com.service.marketplace.persistence.repository;

import com.service.marketplace.persistence.entity.User;
import com.service.marketplace.persistence.entity.VipService;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VipServiceRepository extends JpaRepository<VipService, Integer> {
}