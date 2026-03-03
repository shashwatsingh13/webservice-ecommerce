package com.ecommerce.order_service.service;


import com.ecommerce.order_service.clients.InventoryOpenFeignClient;
import com.ecommerce.order_service.dto.OrderDTO;
import com.ecommerce.order_service.dto.OrderRequestDTO;
import com.ecommerce.order_service.dto.ResponseDTO;
import com.ecommerce.order_service.entity.OrderItem;
import com.ecommerce.order_service.entity.OrderStatus;
import com.ecommerce.order_service.entity.OrdersEntity;
import com.ecommerce.order_service.repository.OrdersRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.resilience.annotation.Retryable;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Slf4j
@Repository
public class OrderService {

    private OrdersRepository ordersRepository;
    private ModelMapper modelMapper;
    private InventoryOpenFeignClient inventoryOpenFeignClient;

    public ResponseDTO getAllOrders(){
        log.info("Start: OrderService getAllOrders()");
        ResponseDTO response = new ResponseDTO();
        try{
            List<OrdersEntity> orders = ordersRepository.findAll();
            if(orders.isEmpty()){
                response.setMessage("No record found");
                response.setStatus("Error");
                return response;
            }
            List<OrderDTO> ordersDto = orders.stream().map(item -> modelMapper.map(item, OrderDTO.class))
                    .toList();
             response.setData(ordersDto);
             response.setStatus("Success");
             response.setMessage("Records fetch successfully");
             log.info("End : orderService getAllOrders()");
             return response;
        } catch (Exception e) {
            response.setMessage("System error occurred");
            response.setStatus("Error");
            log.error("Error: OrderService getAllOrders()", e.getMessage());
            return response;
        }
    }


    public ResponseDTO getOrderById(Long id) {
        log.info("Start: OrderService getOrderById(Long id)");
        ResponseDTO response = new ResponseDTO();
        try {
            Optional<OrdersEntity> orderEntity = ordersRepository.findById(id);
            if(orderEntity.isEmpty()){
                response.setMessage("No record found");
                response.setStatus("Error");
                return response;
            }
            OrderDTO orderDto = new OrderDTO();
            BeanUtils.copyProperties(orderEntity,orderDto);
            response.setStatus("Success");
            response.setData(orderDto);
            response.setMessage("Records fetch successfully");
            return response;
        } catch (Exception e) {
            response.setMessage("System error occurred");
            response.setStatus("Error");
            log.error("Error: OrderService getOrderById()", e.getLocalizedMessage());
            return response;
        }
    }

//    @Retry(name = "inventoryRetry", fallbackMethod = "createOrderFallback")
//    @RateLimiter(name= "inventoryRateLimiter", fallbackMethod = "createOrderFallback")
    @CircuitBreaker(name = "inventoryCircuitBreaker", fallbackMethod = "createOrderFallback")
    public ResponseDTO createOrder(OrderRequestDTO orderRequestDTO){
        log.info("Start: OrderService - createOrder()");
        ResponseDTO response = new ResponseDTO();

            response = inventoryOpenFeignClient.reduceStocks(orderRequestDTO);
            OrdersEntity orders = modelMapper.map(orderRequestDTO, OrdersEntity.class);
            for(OrderItem orderItem : orders.getItems()){
                orderItem.setOrder(orders);
            }
            orders.setTotalPrice(Double.valueOf((response.getData()).toString()));
            orders.setOrderStatus(OrderStatus.CONFIRMED);
            ordersRepository.save(orders);

            response.setData(null);
            response.setStatus("Success");
            response.setMessage("Operation completed successfully");
            return response;

    }

    /**
     * Calls when parent api failed
     **/
    ResponseDTO createOrderFallback (OrderRequestDTO orderRequestDTO, Throwable throwable){
        log.error("Fallback occurred due to: {}", throwable.getMessage());
        ResponseDTO response = new ResponseDTO();
        response.setMessage("System error occurred");
        response.setStatus("error");
        return response;
    }
}
