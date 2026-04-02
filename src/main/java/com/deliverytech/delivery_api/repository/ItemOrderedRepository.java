package com.deliverytech.delivery_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery_api.dto.ItemOrderedDTO;
import com.deliverytech.delivery_api.model.ItemOrdered;

@Repository
public interface ItemOrderedRepository extends JpaRepository<ItemOrdered, Long> {
    @Query(
        """
            SELECT
                p.name AS productName,
                i.quantity AS quantity,
                i.subtotal AS subtotal
            FROM ItemOrdered i
            JOIN i.product p
            WHERE i.order.id = :orderId
        """
    )
    List<ItemOrderedDTO> searchItemsWithProducts(@Param ("orderId") Long orderId);
}
