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

        if (request.getType() != null){
            product.setType(request.getType());
        }

        if (request.getPrice() != null){
            product.setPrice(request.getPrice());
        }

        if (request.getStock() != null){
            product.setStock(request.getStock());
        }

        productRepository.save(product);
        return new ResponseEntity<>("Successfully update", HttpStatus.OK);

    }

    public Optional<Product> findProductByName(String name) {
        return productRepository.findProductByName(name);
    }

    public ResponseEntity<String> addStock(ProductRequest request){

        Product product = productRepository.findProductByName(request.getName())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found"));

        product.setStock(product.getStock() + request.getStock());
        productRepository.save(product);
        return new ResponseEntity<>("Succcessfully add stock", HttpStatus.OK);
    }

    public Iterable<Product> findByType (int page, int size, String type, String sort){
        Pageable pageable = PageRequest.of(page,size, Sort.by("id"));
        if (sort.equalsIgnoreCase("desc")){
            pageable = PageRequest.of(page,size, Sort.by("id").descending());
        }
        return productRepository.findProductByType(type, pageable);
    }

    public ResponseEntity<String> remove(Long id) {
        productRepository.deleteById(id);
        return new ResponseEntity<>("Product Removed", HttpStatus.OK);
    }

}
