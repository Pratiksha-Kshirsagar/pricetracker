package pricetracker.dto.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pricetracker.data.product.Product;
import pricetracker.data.productproperties.ProductProperties;
import pricetracker.dto.manufacturer.ManufacturerDtoConverter;
import pricetracker.dto.productproperties.ProductPropertiesDtoConverter;
import pricetracker.dto.producttype.ProductTypeDtoConverter;

import static org.springframework.beans.BeanUtils.copyProperties;

@Component
public class ProductDtoConverterImpl implements ProductDtoConverter {
    /**
     * Names of the field to ignore by org.springframework.beans.BeanUtils#copyProperties(...).
     * These field will be copied in specific way.
     */
    private static final String PRODUCT_PROPERTIES_FIELD = "productProperties";
    private static final String PRODUCT_TYPE_FIELD = "productType";
    private static final String MANUFACTURER_FIELD = "manufacturer";
    private static final String VERSION_FIELD = "version";

    private final ProductPropertiesDtoConverter productPropertiesDtoConverter;
    private final ProductTypeDtoConverter productTypeDtoConverter;
    private final ManufacturerDtoConverter manufacturerDtoConverter;

    @Autowired
    public ProductDtoConverterImpl(ProductPropertiesDtoConverter productPropertiesDtoConverter,
                                   ProductTypeDtoConverter productTypeDtoConverter,
                                   ManufacturerDtoConverter manufacturerDtoConverter) {
        this.productPropertiesDtoConverter = productPropertiesDtoConverter;
        this.productTypeDtoConverter = productTypeDtoConverter;
        this.manufacturerDtoConverter = manufacturerDtoConverter;
    }

    @Override
    public ProductDto convertToDto(Product entity) {
        ProductDto dto = new ProductDto();
        if (entity.getVersion() == null) {
            copyProperties(entity, dto, PRODUCT_PROPERTIES_FIELD, PRODUCT_TYPE_FIELD, MANUFACTURER_FIELD, VERSION_FIELD);
        } else {
            copyProperties(entity, dto, PRODUCT_PROPERTIES_FIELD, PRODUCT_TYPE_FIELD, MANUFACTURER_FIELD);
        }

        ProductProperties productProperties = entity.getProductProperties();
        if (productProperties != null) {
            dto.setProductProperties(productPropertiesDtoConverter.convertToDto(entity.getProductProperties()));
        }
        dto.setProductType(productTypeDtoConverter.convertToDto(entity.getProductType()));
        dto.setManufacturer(manufacturerDtoConverter.convertToDto(entity.getManufacturer()));

        return dto;
    }

    @Override
    public Product convertToEntity(ProductDto dto) {
        Product entity = new Product();
        if (dto.getVersion() == null) {
            copyProperties(dto, entity, PRODUCT_PROPERTIES_FIELD, PRODUCT_TYPE_FIELD, MANUFACTURER_FIELD, VERSION_FIELD);
        } else {
            copyProperties(dto, entity, PRODUCT_PROPERTIES_FIELD, PRODUCT_TYPE_FIELD, MANUFACTURER_FIELD);
        }

        entity.setProductProperties(productPropertiesDtoConverter.convertToEntity(dto.getProductProperties()));
        entity.setProductType(productTypeDtoConverter.convertToEntity(dto.getProductType()));
        entity.setManufacturer(manufacturerDtoConverter.convertToEntity(dto.getManufacturer()));

        return entity;
    }
}
