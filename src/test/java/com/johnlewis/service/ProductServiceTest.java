package com.johnlewis.service;

import com.google.common.collect.Sets;
import com.johnlewis.domain.ColorSwatch;
import com.johnlewis.domain.Price;
import com.johnlewis.domain.Product;
import com.johnlewis.model.LabelType;
import com.johnlewis.repository.ProductRepository;
import com.johnlewis.transformer.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductServiceTest {

    @Mock private ProductRepository productRepository;
    private PriceTransformer priceTransformer = new PriceTransformer();
    private ShowWasNowTransformer showWasNowTransformer = new ShowWasNowTransformer(priceTransformer);
    private ShowWasThenNowTransformer showWasThenNowTransformer = new ShowWasThenNowTransformer(priceTransformer);
    private ShowPercentageDiscountTransformer showPercentageDiscountTransformer = new ShowPercentageDiscountTransformer(priceTransformer);
    private ColorSwatchTransformer colorSwatchTransformer = new ColorSwatchTransformer();

    @InjectMocks
    private ProductService productService = new ProductService(productRepository,
                                                                priceTransformer,
                                                                showWasNowTransformer,
                                                                showWasThenNowTransformer,
                                                                showPercentageDiscountTransformer,
                                                                colorSwatchTransformer);
    private static final ColorSwatch COLOR_SWATCH = ColorSwatch.builder().basicColor("Red").color("red").skuId("red1").build();
    private static final com.johnlewis.model.ColorSwatch COLOR_SWATCH_EXPECTED = com.johnlewis.model.ColorSwatch.builder().rgbColor("#FF0000").color("red").skuid("red1").build();


    @Test
    public void getProductsWhenEmptyNoProductsFromProductRepository() {
        when(productRepository.getProductsByCategoryId(anyInt())).thenReturn(Collections.emptySet());

        assertThat(productService.getProducts(1, LabelType.ShowWasNow)).isEmpty();
    }


    @Test
    public void getProductsHasProductAttributes() {
        Product prod1 = Product.builder().productId("productId1").title("title1").price(Price.builder().was("40").then2("30").now("10").build()).colorSwatches(Collections.singletonList(COLOR_SWATCH)).build();
        Product prod2 = Product.builder().productId("productId2").title("title2").price(Price.builder().was("50").then2("30").now("10").build()).colorSwatches(Collections.singletonList(COLOR_SWATCH)).build();
        when(productRepository.getProductsByCategoryId(anyInt())).thenReturn(Sets.newHashSet(prod1, prod2));

        List<com.johnlewis.model.Product> products = productService.getProducts(1, LabelType.ShowWasNow);
        assertThat(products)
                .hasSize(2)
                .flatExtracting(com.johnlewis.model.Product::getProductId, com.johnlewis.model.Product::getTitle, com.johnlewis.model.Product::getNowPrice, com.johnlewis.model.Product::getPriceLabel, com.johnlewis.model.Product::getColorSwatches)
                .contains("productId2", "title2", "£10", "Was £50, now £10", Collections.singletonList(COLOR_SWATCH_EXPECTED))
                .contains("productId1", "title1", "£10", "Was £40, now £10", Collections.singletonList(COLOR_SWATCH_EXPECTED));

        verify(productRepository).getProductsByCategoryId(eq(1));
    }

    @Test
    public void getProductsExcludesProductsWithoutPriceReduction() {
        Product prod1 = Product.builder().productId("productId1").title("title1").price(Price.builder().was("40").then2("30").now("10").build()).colorSwatches(Collections.singletonList(COLOR_SWATCH)).build();
        Product prod2 = Product.builder().productId("productId2").title("title2").price(Price.builder().was("50").then2("30").now("10").build()).colorSwatches(Collections.singletonList(COLOR_SWATCH)).build();
        Product prod3 = Product.builder().productId("productId3").title("title3").price(Price.builder().was("50").then2("30").now("50").build()).colorSwatches(Collections.singletonList(COLOR_SWATCH)).build();
        Product prod4 = Product.builder().productId("productId4").title("title4").price(Price.builder().now("50").build()).colorSwatches(Collections.singletonList(COLOR_SWATCH)).build();
        when(productRepository.getProductsByCategoryId(anyInt())).thenReturn(Sets.newHashSet(prod1, prod2, prod3, prod4));

        List<com.johnlewis.model.Product> products = productService.getProducts(1, LabelType.ShowWasNow);

        assertThat(products)
                .hasSize(2)
                .flatExtracting(com.johnlewis.model.Product::getProductId, com.johnlewis.model.Product::getTitle, com.johnlewis.model.Product::getNowPrice, com.johnlewis.model.Product::getPriceLabel, com.johnlewis.model.Product::getColorSwatches)
                .contains("productId2", "title2", "£10", "Was £50, now £10", Collections.singletonList(COLOR_SWATCH_EXPECTED))
                .contains("productId1", "title1", "£10", "Was £40, now £10", Collections.singletonList(COLOR_SWATCH_EXPECTED));

        verify(productRepository).getProductsByCategoryId(eq(1));
    }

    @Test
    public void getProductsSortByPriceReductionWithDescendingOrder() {
        Product prod1 = Product.builder().productId("productId1").title("title1").price(Price.builder().was("40").then2("30").now("10").build()).colorSwatches(Collections.singletonList(COLOR_SWATCH)).build();
        Product prod2 = Product.builder().productId("productId2").title("title2").price(Price.builder().was("50").then2("30").now("10").build()).colorSwatches(Collections.singletonList(COLOR_SWATCH)).build();
        Product prod3 = Product.builder().productId("productId3").title("title3").price(Price.builder().was("1000").then2("30").now("500").build()).colorSwatches(Collections.singletonList(COLOR_SWATCH)).build();
        Product prod4 = Product.builder().productId("productId4").title("title4").price(Price.builder().was("11").now("10").build()).colorSwatches(Collections.singletonList(COLOR_SWATCH)).build();
        when(productRepository.getProductsByCategoryId(anyInt())).thenReturn(Sets.newHashSet(prod1, prod2, prod3, prod4));

        List<com.johnlewis.model.Product> products = productService.getProducts(1, LabelType.ShowWasNow);

        List<com.johnlewis.model.Product> expectedProductsInOrder = Arrays.asList(
                com.johnlewis.model.Product.builder().productId("productId3").title("title3").nowPrice("£500").priceLabel("Was £1000, now £500").colorSwatches(Collections.singletonList(COLOR_SWATCH_EXPECTED)).build(),
                com.johnlewis.model.Product.builder().productId("productId2").title("title2").nowPrice("£10").priceLabel("Was £50, now £10").colorSwatches(Collections.singletonList(COLOR_SWATCH_EXPECTED)).build(),
                com.johnlewis.model.Product.builder().productId("productId1").title("title1").nowPrice("£10").priceLabel("Was £40, now £10").colorSwatches(Collections.singletonList(COLOR_SWATCH_EXPECTED)).build(),
                com.johnlewis.model.Product.builder().productId("productId4").title("title4").nowPrice("£10").priceLabel("Was £11, now £10").colorSwatches(Collections.singletonList(COLOR_SWATCH_EXPECTED)).build()
        );

        assertThat(products)
                .hasSize(4)
                .isEqualTo(expectedProductsInOrder);

        verify(productRepository).getProductsByCategoryId(eq(1));
    }

    @Test
    public void getProductsWhenLabelIsNotSetDefaultsToShowWaNow() {
        Product prod1 = Product.builder().productId("productId1").title("title1").price(Price.builder().was("20").now("10").build()).colorSwatches(Collections.singletonList(COLOR_SWATCH)).build();
        Product prod2 = Product.builder().productId("productId2").title("title2").price(Price.builder().was("50").now("10").build()).colorSwatches(Collections.singletonList(COLOR_SWATCH)).build();
        when(productRepository.getProductsByCategoryId(anyInt())).thenReturn(Sets.newHashSet(prod1, prod2));

        List<com.johnlewis.model.Product> products = productService.getProducts(1, LabelType.ShowWasNow);
        assertThat(products)
                .hasSize(2)
                .flatExtracting(com.johnlewis.model.Product::getProductId, com.johnlewis.model.Product::getTitle, com.johnlewis.model.Product::getNowPrice, com.johnlewis.model.Product::getPriceLabel, com.johnlewis.model.Product::getColorSwatches)
                .contains("productId2", "title2", "£10", "Was £50, now £10", Collections.singletonList(COLOR_SWATCH_EXPECTED))
                .contains("productId1", "title1", "£10", "Was £20, now £10", Collections.singletonList(COLOR_SWATCH_EXPECTED));

        verify(productRepository).getProductsByCategoryId(eq(1));
    }


    @Test
    public void getProductsWhenLabelIsShowWasThenNow() {
        Product prod1 = Product.builder().productId("productId1").title("title1").price(Price.builder().was("20").then1("30").now("10").build()).colorSwatches(Collections.singletonList(COLOR_SWATCH)).build();
        Product prod2 = Product.builder().productId("productId2").title("title2").price(Price.builder().was("50").then2("30").now("10").build()).colorSwatches(Collections.singletonList(COLOR_SWATCH)).build();
        when(productRepository.getProductsByCategoryId(anyInt())).thenReturn(Sets.newHashSet(prod1, prod2));

        List<com.johnlewis.model.Product> products = productService.getProducts(1, LabelType.ShowWasThenNow);
        assertThat(products)
                .hasSize(2)
                .flatExtracting(com.johnlewis.model.Product::getProductId, com.johnlewis.model.Product::getTitle, com.johnlewis.model.Product::getNowPrice, com.johnlewis.model.Product::getPriceLabel, com.johnlewis.model.Product::getColorSwatches)
                .contains("productId2", "title2", "£10", "Was £50, then £30, now £10", Collections.singletonList(COLOR_SWATCH_EXPECTED))
                .contains("productId1", "title1", "£10", "Was £20, then £30, now £10", Collections.singletonList(COLOR_SWATCH_EXPECTED));

        verify(productRepository).getProductsByCategoryId(eq(1));
    }


    @Test
    public void getProductsWhenLabelIsShowPercentageDiscount() {
        Product prod1 = Product.builder().productId("productId1").title("title1").price(Price.builder().was("50").then1("30").now("10").build()).colorSwatches(Collections.singletonList(COLOR_SWATCH)).build();
        Product prod2 = Product.builder().productId("productId2").title("title2").price(Price.builder().was("60").then2("30").now("10").build()).colorSwatches(Collections.singletonList(COLOR_SWATCH)).build();
        when(productRepository.getProductsByCategoryId(anyInt())).thenReturn(Sets.newHashSet(prod1, prod2));

        List<com.johnlewis.model.Product> products = productService.getProducts(1, LabelType.ShowPercentageDiscount);
        assertThat(products)
                .hasSize(2)
                .flatExtracting(com.johnlewis.model.Product::getProductId, com.johnlewis.model.Product::getTitle, com.johnlewis.model.Product::getNowPrice, com.johnlewis.model.Product::getPriceLabel, com.johnlewis.model.Product::getColorSwatches)
                .contains("productId2", "title2", "£10", "80% off - now £10", Collections.singletonList(COLOR_SWATCH_EXPECTED))
                .contains("productId1", "title1", "£10", "83% off - now £10", Collections.singletonList(COLOR_SWATCH_EXPECTED));

        verify(productRepository).getProductsByCategoryId(eq(1));
    }


}