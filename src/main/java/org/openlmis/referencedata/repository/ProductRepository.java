package org.openlmis.referencedata.repository;

import org.openlmis.referencedata.domain.Product;
import org.openlmis.referencedata.domain.Program;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

public interface ProductRepository extends Repository<Product, Integer>
{
    Iterable<Product> findAll(Sort sort);
    Page<Product> findAll(Pageable pageable);

    //See UserRepository for examples of how we might implement additional endpoints after we know what they should be.
}
