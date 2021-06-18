package in.con.redhat.service;

import in.con.redhat.domain.Product;

public interface ProductService {
    
	Iterable<Product> listAllProducts();

    Product getProductById(Integer id);

    Product saveProduct(Product product);
}