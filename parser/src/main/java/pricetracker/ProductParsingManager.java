package pricetracker;

import pricetracker.data.manufacturer.ManufacturerEnum;
import pricetracker.data.product.Product;
import pricetracker.dto.producttype.ProductTypeEnum;

import java.util.List;

public interface ProductParsingManager {

    void loadProducts(List<Product> products,
                      ManufacturerEnum manufacturer,
                      ProductTypeEnum productType);

    void run();
}
