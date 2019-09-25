package com.johnlewis.controller;

import com.johnlewis.model.LabelType;
import com.johnlewis.model.Product;
import com.johnlewis.service.ProductService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestClientException;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest
public class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @Test
    public void getProducts() throws Exception {
        Product product = Product.builder().productId("1").priceLabel("10").nowPrice("20").build();
        when(productService.getProducts(any(), any())).thenReturn(Collections.singletonList(product));

        this.mockMvc.perform(get("/products/1"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{'productId':'1','title':null,'nowPrice':'20','priceLabel':'10','colorSwatches':null}]"));

        verify(productService).getProducts(eq(1), eq(LabelType.ShowWasNow));
    }

    @Test
    public void getProductsWithQueryParam() throws Exception {
        Product product = Product.builder().productId("1").priceLabel("10").nowPrice("20").build();
        when(productService.getProducts(any(), any())).thenReturn(Collections.singletonList(product));

        this.mockMvc.perform(get("/products/2?labelType=ShowWasThenNow"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{'productId':'1','title':null,'nowPrice':'20','priceLabel':'10','colorSwatches':null}]"));

        verify(productService).getProducts(eq(2), eq(LabelType.ShowWasThenNow));
    }

    @Test
    public void exceptionNotFoundForUnknownCategoryId() throws Exception {
        doThrow(RestClientException.class).when(productService).getProducts(any(), any());

        this.mockMvc.perform(get("/products/2?labelType=ShowWasThenNow"))
                .andExpect(status().isNotFound())
                .andExpect(content().json("{'message':'Unable to get data'}"));

        verify(productService).getProducts(eq(2), eq(LabelType.ShowWasThenNow));
    }

    @Test
    public void exceptionInternalServerError() throws Exception {
        when(productService.getProducts(any(), any())).thenAnswer( invocation -> { throw new Exception("some"); });

        this.mockMvc.perform(get("/products/2?labelType=ShowWasThenNow"))
                .andExpect(status().isInternalServerError())
                .andExpect(content().json("{'message':'Something went wrong unable to get data now'}"));

        verify(productService).getProducts(eq(2), eq(LabelType.ShowWasThenNow));
    }


}