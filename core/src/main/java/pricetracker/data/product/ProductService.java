package pricetracker.data.product;

import pricetracker.data.wishproduct.WishProduct;
import pricetracker.dto.product.ProductDto;
import pricetracker.dto.productprice.ProductPriceDto;
import pricetracker.dto.producttype.ProductTypeEnum;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

public interface ProductService {

    Optional<ProductDto> getOneById(Long id);

    long getProductsCountByType(ProductTypeEnum type);

    long getProductsByNameOrModelContainingPageable(String contains);

    List<ProductAndPricePresentation> getProductsByNameOrModelContainingPageable(String contains,
                                                                                 int pageNumber,
                                                                                 int rowsOnPageCount,
                                                                                 Principal principal);

    List<ProductAndPricePresentation> getProductsInfoPageableByType(ProductTypeEnum type,
                                                                    int pageNumber,
                                                                    int rowsOnPageCount,
                                                                    Principal principal);

    void addToWishList(Long productId, Long userId);

    void removeFromWishList(Long productId, Long userId);

    List<WishProduct> getWishProductsListForUserWishId(Long id);

    List<ProductPriceDto> getProductPrices(Long productId);
}
