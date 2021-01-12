package com.sl.ms.inventorymanagement.inventory;

import org.springframework.data.jpa.repository.JpaRepository;



public interface InventoryRepository extends JpaRepository<Inventory, Long> {

}
