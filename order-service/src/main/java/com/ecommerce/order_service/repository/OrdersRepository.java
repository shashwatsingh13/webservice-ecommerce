package com.ecommerce.order_service.repository;

import com.ecommerce.order_service.entity.OrdersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
public interface OrdersRepository extends JpaRepository<OrdersEntity, Long> {
}
