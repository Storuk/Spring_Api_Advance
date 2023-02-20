package com.epam.esm.orders;

import com.epam.esm.utils.SqlQueries;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * @author Vlad Storoshchuk
 * */
@Repository
public interface OrderRepo extends JpaRepository<Order, Long> {
    /**
     * A repository method for getting user orders
     * calling a query from {@link SqlQueries} class
     * @return Page of Orders
     * */
    Page<Order> getOrdersByUserId(@Param("user_id") long user_id, Pageable pageable);
}