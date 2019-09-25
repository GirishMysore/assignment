package com.johnlewis.repository;

import com.google.common.collect.Sets;
import com.johnlewis.domain.Category;
import com.johnlewis.domain.Product;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductRepositoryTest {

    @Mock private RestTemplate restTemplate;

    @InjectMocks private ProductRepository productRepository;

    private static final String V1_CATEGORY_URL = "/v1/categories/{categoryId}/products?key=2ALHCAAs6ikGRBoy6eTHA58RaG097Fma";

    @Test
    public void getProductsByCategoryIdWillFetchCategoryOfProducts() {
        int categoryId = 1;
        Category category = Category.builder().products(Sets.newHashSet(Product.builder().build())).build();
        when(restTemplate.getForObject(anyString(), eq(Category.class), any(Integer.class))).thenReturn(category);

        assertThat(productRepository.getProductsByCategoryId(categoryId))
                .isEqualTo(category.getProducts());

        verify(restTemplate).getForObject(eq(V1_CATEGORY_URL), eq(Category.class), eq(1));
    }

    @Test
    public void getProductsByCategoryIdThrowsRestClientException() {
        int categoryId = 1;
        doThrow(RestClientException.class).when(restTemplate).getForObject(anyString(), eq(Category.class), any(Integer.class));

        assertThatThrownBy(() -> productRepository.getProductsByCategoryId(categoryId))
                .isExactlyInstanceOf(RestClientException.class);

        verify(restTemplate).getForObject(eq(V1_CATEGORY_URL), eq(Category.class), eq(1));
    }

    @Test
    public void getProductsByCategoryIdEmpty() {
        int categoryId = 1;
        when(restTemplate.getForObject(anyString(), eq(Category.class), any(Integer.class))).thenReturn(null);

        assertThat(productRepository.getProductsByCategoryId(categoryId))
                .isEmpty();

        verify(restTemplate).getForObject(eq(V1_CATEGORY_URL), eq(Category.class), eq(1));
    }

}