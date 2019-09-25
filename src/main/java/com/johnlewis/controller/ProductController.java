package com.johnlewis.controller;

import com.johnlewis.model.LabelType;
import com.johnlewis.model.Product;
import com.johnlewis.service.ProductService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.List;

@RestController
@RequestMapping(value = "/products")
@Validated
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @ApiOperation(
            value = "Get products by categoryId.",
            notes = "The array of products should only contain products with a price reduction and should be sorted to show the highest price reduction first. Price reduction is calculated using price.was - price.now.",
            response = Product.class,
            responseContainer = "List"
    )
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully fetches product list"),
            @ApiResponse(code = 404, message = "Unable to get data"),
            @ApiResponse(code = 500, message = "Something went wrong unable to get data")
    }
    )
    @GetMapping(value = "/{categoryId}", produces = "application/json")
    public List<Product> getProducts(@PathVariable Integer categoryId,
                                     @RequestParam(value = "labelType", required = false, defaultValue = "ShowWasNow") LabelType labelType){
        return productService.getProducts(categoryId, labelType);
    }

}
