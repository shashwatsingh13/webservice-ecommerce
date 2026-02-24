package com.ecommerce.order_service.controller;

import com.ecommerce.order_service.dto.OrderDTO;
import com.ecommerce.order_service.dto.ResponseDTO;
import com.ecommerce.order_service.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/core")
public class OrderController {


    private final OrderService orderService;

    @GetMapping("/helloOrder")
    public String helloOrder(){
        return "hello from Orders Service";
    }

    @GetMapping("/fetchlist")
    public ResponseEntity<ResponseDTO> getAllInventory(){
        log.info("Start: ProductController getAllInventory() ");
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/id")
    public ResponseEntity<ResponseDTO> getProductById(Long id){
        log.info("Start: ProductController - getProductById()");
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @PostMapping("/create-order")
    public ResponseEntity<Object> createorder(@RequestBody OrderDTO orderRequestDto){

    }
}
