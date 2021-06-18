package in.con.redhat.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.con.redhat.domain.Product;
import in.con.redhat.repositories.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {
    
	private ProductRepository productRepository;

    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Iterable<Product> listAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Integer id) {
        return productRepository.findById(id).orElse(null);
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }
}
