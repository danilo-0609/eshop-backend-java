package com.eshop.backend.modules.catalog.domain.products;

//Estado del inventario de un producto. El estado puede ser "Sin inventario" (OUT_OF_STOCK) o "Con inventario" (WITH_STOCK)
public enum StockStatus {
    OUT_OF_STOCK,
    WITH_STOCK
}
