package pricetracker.dto.productprice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pricetracker.data.productprice.ProductPrice;
import pricetracker.data.productprice.ProductPriceBuilder;
import pricetracker.dto.product.ProductDtoConverter;
import pricetracker.dto.store.StoreDtoConverter;

@Component
public class ProductPriceDtoConverterImpl implements ProductPriceDtoConverter {

    private final ProductDtoConverter productDtoConverter;
    private final StoreDtoConverter storeDtoConverter;

    @Autowired
    public ProductPriceDtoConverterImpl(ProductDtoConverter productDtoConverter, StoreDtoConverter storeDtoConverter) {
        this.productDtoConverter = productDtoConverter;
        this.storeDtoConverter = storeDtoConverter;
    }

    @Override
    public ProductPriceDto convertToDto(ProductPrice entity) {
        return ProductPriceDtoBuilder.aProductPriceDto()
                .withId(entity.getId())
                .withPrice(entity.getPrice())
                .withDate(entity.getDate())
                .withProduct(productDtoConverter.convertToDto(entity.getProduct()))
                .withStore(storeDtoConverter.convertToDto(entity.getStore()))
                .build();
    }

    @Override
    public ProductPrice convertToEntity(ProductPriceDto dto) {
        return ProductPriceBuilder.aProductPrice()
                .withId(dto.getId())
                .withPrice(dto.getPrice())
                .withDate(dto.getDate())
                .withProduct(productDtoConverter.convertToEntity(dto.getProduct()))
                .withStore(storeDtoConverter.convertToEntity(dto.getStore()))
                .build();
    }
}
