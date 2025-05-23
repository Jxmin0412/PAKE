package com.pake.pake.Repository;


import com.pake.pake.Entities.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    // Basic CRUD operations are automatically implemented by JpaRepository
    // You can add custom query methods here if needed in the future
}
