package in.con.redhat.repositories;

import org.springframework.data.repository.CrudRepository;

import in.con.redhat.domain.Product;

public interface ProductRepository extends CrudRepository<Product, Integer>{
}