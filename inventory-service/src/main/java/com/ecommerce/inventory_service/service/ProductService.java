package com.ecommerce.inventory_service.service;

import com.ecommerce.inventory_service.dto.OrderRequestDTO;
import com.ecommerce.inventory_service.dto.OrderRequestItemDTO;
import com.ecommerce.inventory_service.dto.ProductDTO;
import com.ecommerce.inventory_service.dto.ResponseDTO;
import com.ecommerce.inventory_service.entity.ProductEntity;
import com.ecommerce.inventory_service.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@Repository
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public List<ProductDTO> getAllInventory(){
        log.info("Start : ProductService getAllInventory()");
        try{
            List<ProductEntity> productList = productRepository.findAll();
            return productList.stream().map(product -> modelMapper.map(product,ProductDTO.class)).toList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @author shashwat.singh
     * */
    public ResponseDTO getProductById(Long id) {
        log.info("Start: ProductService - getProductById(Long id),id[{}]", id);
        ResponseDTO response = new ResponseDTO();
        try {
            Optional<ProductEntity> inventory = productRepository.findById(id);
            ProductDTO productDto = inventory
                    .map(product -> modelMapper.map(product, ProductDTO.class))
                    .orElse(null);

            response.setData(productDto);
            response.setStatus("Success");
            response.setMessage("Data fetched successfully");
            return response;
        } catch (Exception e) {
            response.setData(null);
            response.setStatus("Error");
            response.setMessage(e.getLocalizedMessage());
            return response;
        }
    }

    /**
     * @author shashwat.singh
     * */
    @Transactional
    public ResponseDTO reduceStocks(OrderRequestDTO orderRequestDTO){
        log.info("Start: ProductService - reduceStocks()");
        ResponseDTO response = new ResponseDTO();
        Double totalPrice = 0.0;
        try{
            for(OrderRequestItemDTO orderRequestItemDTO: orderRequestDTO.getItems()){
                Long productId = orderRequestItemDTO.getProductid();
                Integer quantity = orderRequestItemDTO.getQuantity();

                ProductEntity product =  productRepository.getReferenceById(productId);
                if(product == null) {
                    response.setData(null);
                    response.setStatus("Error");
                    response.setMessage("Product not found with id: "+ productId);
                    return response;

                } else if (product.getStock() < quantity) {
                    response.setData(null);
                    response.setStatus("Error");
                    response.setMessage("Product cannot fulfilled for given quantity");
                    return response;
                }
                product.setStock(product.getStock() - quantity);
                productRepository.save(product);
                totalPrice += quantity * product.getPrice();
            }
            response.setStatus("success");
            response.setMessage("Data fetch successfully");
            response.setData(totalPrice);
            return response;
        } catch (Exception e) {
            log.info("Error: ProductService - reduceStocks()", e.getLocalizedMessage());
            response.setData(null);
            response.setStatus("Error");
            response.setMessage(e.getLocalizedMessage());
            return response;
        }
    }
}
