package com.mystore.orders.service;

import com.mystore.orders.dto.OrderRequest;
import com.mystore.orders.dto.OrderResponse;
import com.mystore.orders.dto.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Random;

@Service
public class OrderService {

    String GET_PROD_URL  = "http://product-service/products/{id}";


    @Autowired
    private DiscoveryClient discoveryClient ;

    public OrderResponse placeOrder(OrderRequest orderRequest) {

        // 1. Retrieve the product details from the product-service
        RestTemplate restTemplate = new RestTemplate();
        Product product = restTemplate.getForObject(GET_PROD_URL, Product.class, orderRequest.getProductId());

        // 2. Process the order (total price = quantity ordered * product price)
        double totalPrice = orderRequest.getQuantity() * product.getPrice();

        // 3. Return the response
        OrderResponse orderResponse = new OrderResponse();
        orderResponse.setOrderId(new Random().nextInt(1000)); // Generate a random order ID
        orderResponse.setProductId(orderRequest.getProductId());
        orderResponse.setQuantity(orderRequest.getQuantity());
        orderResponse.setTotalPrice(totalPrice);
        orderResponse.setStatus("Order placed successfully");

        return orderResponse;

    }

}
