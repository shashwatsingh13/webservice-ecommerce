package com.ecommerce.order_service.clients;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;

@FeignClient(name = "inventory-service", path = "/inventory")
public interface InventoryOpenFeignClient {

   // @PutMapping("/products/reduce-stocks")

}
