package in.con.redhat.repositories;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import in.con.redhat.CustomException;
import in.con.redhat.configuration.RepositoryConfiguration;
import in.con.redhat.domain.Product;

import java.math.BigDecimal;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = {RepositoryConfiguration.class})
public class ProductRepositoryTest {

    private ProductRepository productRepository;

    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @SuppressWarnings("unused")
	@Test
    public void testSaveProduct() throws CustomException{
        
        Product product = new Product();
        product.setDescription("Shirt");
        product.setPrice(new BigDecimal("20.00"));
        product.setProductId("1234");
        
        assertNull(product.getId());
        productRepository.save(product);
        assertNotNull(product.getId());
        
        Product prod = productRepository.findById(product.getId()).orElse(null);
        
        if(null == prod) {
        	throw new CustomException("Product shouldn't be null or empty.");
        }

        assertNotNull(prod);
        assertEquals(product.getId(), prod.getId());
        assertEquals(product.getDescription(), prod.getDescription());

        //update the product description and save
        prod.setDescription("New Description");
        productRepository.save(prod);

        //fetch the updated product from database
        Product updatedProd = productRepository.findById(prod.getId()).orElse(null);
        assertEquals(prod.getDescription(), updatedProd.getDescription());

        long productCount = productRepository.count();
        assertEquals(productCount, 1);

        //list of all products
        Iterable<Product> products = productRepository.findAll();

        int count = 0;

        for(Product p : products){
            count++;
        }

        assertEquals(count, 1);
    }
}