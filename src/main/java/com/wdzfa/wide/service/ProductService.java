package com.wdzfa.wide.service;

import com.wdzfa.wide.dto.ProductRequest;
import com.wdzfa.wide.model.Product;
import com.wdzfa.wide.repository.ProductPagingAndSortingRepository;
import com.wdzfa.wide.repository.ProductRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    ProductPagingAndSortingRepository pagingAndSortingRepository;

    public Product create (Product request){
        return productRepository.save(request);
    }

    public Product findOne (Long id){
        Optional<Product> product = productRepository.findById(id);
        return product.get();
    }

    public Iterable<Product> findAll(){
        return productRepository.findAll();
    }

    public Iterable<Product> findAllProductPageAndSort(int page, int size, String sort){

        Pageable pageable = PageRequest.of(page,size, Sort.by("id"));
        if (sort.equalsIgnoreCase("desc")){
            pageable = PageRequest.of(page,size,Sort.by("id").descending());
        }
        return pagingAndSortingRepository.findAll(pageable);
    }

    public ResponseEntity<String> update(ProductRequest request){

        Product product = productRepository.findProductByName(request.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));

        product.setName(request.getName());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setType(request.getType());
        productRepository.save(product);
        return new ResponseEntity<>("Successfully update", HttpStatus.OK);

    }

    public Optional<Product> findProductByType(String type) {
        return productRepository.findProductByType(type);
    }

}
