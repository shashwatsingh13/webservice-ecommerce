package com.ecommerce.order_service.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class OrderRequestDTO {
     Long id;
     List<OrderRequestItemDTO> items;
     BigDecimal totalPrice;
}
