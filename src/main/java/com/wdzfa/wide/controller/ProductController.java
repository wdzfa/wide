package com.wdzfa.wide.controller;

import com.wdzfa.wide.model.Product;
import com.wdzfa.wide.dto.ProductRequest;
import com.wdzfa.wide.dto.ResponseData;
import com.wdzfa.wide.service.ProductService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    ModelMapper modelMapper;

    @PostMapping("/create")
    public ResponseEntity<ResponseData<Product>> create (@RequestBody ProductRequest request, Errors error){

        ResponseData<Product> response = new ResponseData<>();
        if (error.hasErrors()){
            for (Object obj: error.getAllErrors()){
                response.setStatus(false);
                response.setPayload(null);
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
        }

        Product product = modelMapper.map(request, Product.class);
        response.setStatus(true);
        response.setPayload(productService.create(product));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public Product findOne (@PathVariable("id") Long id){
        return productService.findOne(id);
    }

    @GetMapping("/all")
    public Iterable<Product> findAll(){
        return productService.findAll();
    }

    @GetMapping("/search/{size}/{page}/{sort}")
    public Iterable<Product> findAllPagedAndSorted(@PathVariable("page") int page,
                                                   @PathVariable("size") int size,
                                                   @PathVariable("sort") String sort){
        return productService.findAllProductPageAndSort(page,size,sort);
    }

    @PutMapping("/update")
    public ResponseEntity<String> update(@RequestBody ProductRequest request){
        return productService.update(request);
    }

    @PutMapping("/add-stock")
    public ResponseEntity<String> addStock(@RequestBody ProductRequest request){
        return productService.addStock(request);
    }

    @GetMapping("/search/{size}/{page}/{type}/{sort}")
    public Iterable<Product> searchCategory(@PathVariable("page") int page,
                                            @PathVariable("size") int size,
                                            @PathVariable("type") String type,
                                            @PathVariable("sort") String sort){
        return productService.findByType(page,size,type,sort);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable("id") Long id){
        return productService.remove(id);
    }

}
