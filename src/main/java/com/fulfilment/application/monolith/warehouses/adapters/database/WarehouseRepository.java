package com.fulfilment.application.monolith.warehouses.adapters.database;

import com.fulfilment.application.monolith.warehouses.domain.models.Warehouse;
import com.fulfilment.application.monolith.warehouses.domain.ports.WarehouseStore;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import io.quarkus.panache.common.page;
import io.quarkus.panache.common.sort;
import java.util.HashMap;
import java.util.Map;

@ApplicationScoped
public class WarehouseRepository implements WarehouseStore, PanacheRepository<DbWarehouse> {

  @Override
  public List<Warehouse> getAll() {
    return this.listAll().stream().map(DbWarehouse::toWarehouse).toList();
  }

  @Override
  public void create(Warehouse warehouse) {
    DbWarehouse dbWarehouse = new DbWarehouse();
    dbWarehouse.businessUnitCode = warehouse.businessUnitCode;
    dbWarehouse.location = warehouse.location;
    dbWarehouse.capacity = warehouse.capacity;
    dbWarehouse.stock = warehouse.stock;
    dbWarehouse.createdAt = warehouse.createdAt;
    dbWarehouse.archivedAt = warehouse.archivedAt;
    
    this.persist(dbWarehouse);
  }

  @Override
  public void update(Warehouse warehouse) {
    getEntityManager().createQuery(
      "UPDATE DbWarehouse w SET w.location = :loc, w.capacity = :cap, " +
      "w.stock = :stock, w.archivedAt = :archived WHERE w.businessUnitCode = :code")
      .setParameter("loc", warehouse.location)
      .setParameter("cap", warehouse.capacity)
      .setParameter("stock", warehouse.stock)
      .setParameter("archived", warehouse.archivedAt)
      .setParameter("code", warehouse.businessUnitCode)
      .executeUpdate();

    // Clear persistence context to see updates in subsequent queries
    getEntityManager().flush();
    getEntityManager().clear();
  }

  @Override
  public void remove(Warehouse warehouse) {
    getEntityManager().createQuery("DELETE FROM DbWarehouse w WHERE w.businessUnitCode = :code")
            .setParameter("code", warehouse.businessUnitCode).executeUpdate();
    // Clear persistence context to see updates in subsequent queries
    getEntityManager().flush();
    getEntityManager().clear();
  }

  @Override
  public Warehouse findByBusinessUnitCode(String buCode) {
    DbWarehouse dbWarehouse = find("businessUnitCode", buCode).firstResult();
    return dbWarehouse != null ? dbWarehouse.toWarehouse() : null;
  }

  @Override
  public List<Warehouse> search(String location, Integer mincapacity, Integer maxcapacity, String sortBy,
          String sortOrder, int page, int pageSize) {
    StringBuilder query = new StringBuilder("archived = false");  
    Map<String, Object> params = new HashMap<>();

    if (location != null) {
        query.append(" and location = :location");
        params.put("location", location);
    }

    if (minCapacity != null) {
        query.append(" and capacity >= :minCapacity");
        params.put("minCapacity", minCapacity);
    }

    if (maxCapacity != null) {
        query.append(" and capacity <= :maxCapacity");
        params.put("maxCapacity", maxCapacity);
    }

    String sortField = (sortBy != null) ? sortBy : "createdAt";
    Sort sort = Sort.by(sortField);

    if("desc".equalsIgnoreCase(sortOrder)) {
      sort = sort.descending();
    }

    List<DbWarehouse> dbWarehouse = find(query.toString(), sort, params).page(Page.of(page,Math.min(pageSize, 100))).list();
    return dbWarehouse.stream()
            .map(DbWarehouse::toWarehouse)
            .toList();
   }
}
