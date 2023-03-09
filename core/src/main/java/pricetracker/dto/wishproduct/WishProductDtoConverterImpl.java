package pricetracker.dto.wishproduct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pricetracker.data.wishproduct.WishProduct;
import pricetracker.data.wishproduct.WishProductBuilder;
import pricetracker.dto.product.ProductDtoConverter;
import pricetracker.dto.user.UserDtoConverter;

@Component
public class WishProductDtoConverterImpl implements WishProductDtoConverter {

    private UserDtoConverter userDtoConverter;
    private ProductDtoConverter productDtoConverter;

    @Autowired
    public WishProductDtoConverterImpl(UserDtoConverter userDtoConverter, ProductDtoConverter productDtoConverter) {
        this.userDtoConverter = userDtoConverter;
        this.productDtoConverter = productDtoConverter;
    }

    @Override
    public WishProductDto convertToDto(WishProduct entity) {
        return WishProductDtoBuilder.aWishProductDto()
                .withId(entity.getId())
                .withDateAdded(entity.getDateAdded())
                .withVersion(entity.getVersion())
                .withUser(userDtoConverter.convertToDto(entity.getUser()))
                .withProduct(productDtoConverter.convertToDto(entity.getProduct()))
                .build();
    }

    @Override
    public WishProduct convertToEntity(WishProductDto dto) {
        return WishProductBuilder.aWishProduct()
                .withId(dto.getId())
                .withDateAdded(dto.getDateAdded())
                .withVersion(dto.getVersion())
                .withUser(userDtoConverter.convertToEntity(dto.getUser()))
                .withProduct(productDtoConverter.convertToEntity(dto.getProduct()))
                .build();
    }
}