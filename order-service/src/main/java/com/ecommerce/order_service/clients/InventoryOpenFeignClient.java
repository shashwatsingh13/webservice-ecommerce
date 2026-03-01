package com.ecommerce.order_service.clients;


import com.ecommerce.order_service.dto.OrderRequestDTO;
import com.ecommerce.order_service.dto.ResponseDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "inventory-service", path = "/inventory")
public interface InventoryOpenFeignClient {

    @PutMapping("/products/reduce-stocks")
    Double  reduceStocks(@RequestBody OrderRequestDTO orderRequestDTO);

}
