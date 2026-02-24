package com.ecommerce.inventory_service.controller;

import com.ecommerce.inventory_service.client.OrdersFeignClient;
import com.ecommerce.inventory_service.dto.OrderRequestDTO;
import com.ecommerce.inventory_service.dto.ProductDTO;
import com.ecommerce.inventory_service.dto.ResponseDTO;
import com.ecommerce.inventory_service.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;
    private final DiscoveryClient discoveryClient;
    private final RestClient restClient;

    private final OrdersFeignClient ordersFeignClient;

    @GetMapping("/fetchOrders")
    public String fetchFromOrderService(HttpServletRequest request){

        log.info(request.getHeader("x-custom-header")); // it used to pass important information from api gateway eg. token, userId etc.

        /**   below is the logic through restClient  **/
//        List<ServiceInstance> instances =
//                discoveryClient.getInstances("order-service");


//        if (instances == null || instances.isEmpty()) {
//            return "Order Service is not available";
//        }
//
//        ServiceInstance orderService = instances.get(0);
//
//        return restClient.get()
//                .uri(orderService.getUri() + "/orders/core/helloOrder")
//                .retrieve()
//                .body(String.class);

        /**   below is the logic through feignClient  **/
        return ordersFeignClient.helloOrders();
    }


    @GetMapping("/fetchlist")
    public ResponseEntity<List<ProductDTO>> getAllInventory(){
        log.info("Start: ProductController getAllInventory() ");
        return ResponseEntity.ok(productService.getAllInventory());
    }

    @GetMapping("/id")
    public ResponseEntity<ResponseDTO> getProductById(Long id){
        log.info("Start: ProductController - getProductById()");
        return ResponseEntity.ok(productService.getProductById(id));
        }

    @PutMapping("reduce-stocks")
    public ResponseEntity<?> reduceStocks(@RequestBody OrderRequestDTO orderRequestDTO){
            return ResponseEntity.ok(productService.reduceStocks(orderRequestDTO));
    }
}
