package in.con.redhat.bootstrap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import in.con.redhat.domain.Product;
import in.con.redhat.repositories.ProductRepository;

import java.math.BigDecimal;

@Component
public class ProductLoader implements ApplicationListener<ContextRefreshedEvent> {

    private ProductRepository productRepository;

    private Logger log = LogManager.getLogger(ProductLoader.class);

    @Autowired
    public void setProductRepository(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {

        Product shirt = new Product();
        shirt.setDescription("Shirt");
        shirt.setPrice(new BigDecimal("20.00"));
        shirt.setImageUrl("/webapp/images/rh_guru_shirt.jpg");
        shirt.setProductId("1234");
        productRepository.save(shirt);

        log.info("Shirt Id: " + shirt.getId());

        Product mug = new Product();
        mug.setDescription("Mug");
        mug.setImageUrl("/webapp/images/rh_guru_mug.jpg");
        mug.setProductId("1235");
        mug.setPrice(new BigDecimal("15.00"));
        productRepository.save(mug);

        log.info("Mug Id: " + mug.getId());
    }
}
