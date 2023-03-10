package pricetracker.data.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import pricetracker.data.productprice.ProductPriceRepository;
import pricetracker.data.productprice.ShortProductPriceProjection;
import pricetracker.data.user.User;
import pricetracker.data.user.UserRepository;
import pricetracker.data.wishproduct.WishProduct;
import pricetracker.data.wishproduct.WishProductBuilder;
import pricetracker.data.wishproduct.WishProductRepository;
import pricetracker.dto.product.ProductDto;
import pricetracker.dto.product.ProductDtoConverter;
import pricetracker.dto.productprice.ProductPriceDto;
import pricetracker.dto.productprice.ProductPriceDtoConverter;
import pricetracker.dto.producttype.ProductTypeEnum;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductDtoConverter productConverter;
    private final ProductPriceRepository priceRepository;
    private final WishProductRepository wishProductRepository;
    private final UserRepository userRepository;
    private final ProductPriceDtoConverter priceConverter;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository,
                              ProductDtoConverter productConverter,
                              ProductPriceRepository priceRepository,
                              WishProductRepository wishProductRepository,
                              UserRepository userRepository,
                              ProductPriceDtoConverter priceConverter) {
        this.productRepository = productRepository;
        this.productConverter = productConverter;
        this.priceRepository = priceRepository;
        this.wishProductRepository = wishProductRepository;
        this.userRepository = userRepository;
        this.priceConverter = priceConverter;
    }

    @Override
    public Optional<ProductDto> getOneById(Long id) {
        if (id == null || id < 1) {
            throw new IllegalArgumentException("Id can't be null or less than 1!");
        }

        Optional<ProductDto> product = productRepository.findById(id).map(productConverter::convertToDto);
        product.ifPresent(this::prepareImageUrl);
        return product;
    }

    @Override
    public long getProductsCountByType(ProductTypeEnum type) {
        if (type == null) {
            throw new IllegalArgumentException("Type can't be null!");
        }

        return productRepository.countAllByProductTypeId(type.getId());
    }

    @Override
    public long getProductsByNameOrModelContainingPageable(String contains) {
        if (contains == null || contains.isBlank()) {
            throw new IllegalArgumentException("String with contains can't be null or blank!");
        }

        return productRepository.countAllByNameContainingOrModelContaining(contains, contains);
    }

    @Override
    public List<ProductAndPricePresentation> getProductsByNameOrModelContainingPageable(String contains,
                                                                                        int pageNumber,
                                                                                        int rowsOnPageCount,
                                                                                        Principal principal) {
        if (pageNumber < 0 || rowsOnPageCount < 1) {
            throw new IllegalArgumentException(
                    String.format("Invalid page number or rows on page count. Page number = %d, rowsOnPageCount = %d",
                            pageNumber, rowsOnPageCount)
            );
        } else if (contains == null || contains.isBlank()) {
            throw new IllegalArgumentException("String with contains can't be null or blank!");
        }

        List<ProductDto> products = productRepository.findAllByNameContainingOrModelContaining(
                contains,
                contains,
                PageRequest.of(pageNumber, rowsOnPageCount)
        ).stream().map(productConverter::convertToDto).collect(Collectors.toList());
        prepareImageUrl(products);
        return getProductAndPricePresentationList(products, principal);
    }

    @Override
    public List<ProductAndPricePresentation> getProductsInfoPageableByType(ProductTypeEnum type,
                                                                           int pageNumber,
                                                                           int rowsOnPageCount,
                                                                           Principal principal) {
        if (pageNumber < 0 || rowsOnPageCount < 1) {
            throw new IllegalArgumentException(
                    String.format("Invalid page number or rows on page count. Page number = %d, rowsOnPageCount = %d",
                            pageNumber, rowsOnPageCount)
            );
        } else if (type == null) {
            throw new IllegalArgumentException("Type can't be null!");
        }

        List<ProductDto> products = productRepository.findAllByProductTypeId(type.getId(),
                PageRequest.of(pageNumber, rowsOnPageCount)).stream()
                .map(productConverter::convertToDto)
                .collect(Collectors.toList());
        prepareImageUrl(products);
        return getProductAndPricePresentationList(products, principal);
    }

    @Override
    @Transactional
    public void addToWishList(Long productId, Long userId) {
        wishProductRepository.save(WishProductBuilder.aWishProduct()
                .withDateAdded(LocalDate.now())
                .withUser(userRepository.findById(userId).orElseThrow(IllegalArgumentException::new))
                .withProduct(productRepository.findById(productId).orElseThrow(IllegalArgumentException::new))
                .build());
    }

    @Override
    @Transactional
    public void removeFromWishList(Long productId, Long userId) {
        wishProductRepository.deleteFirstByUserIdAndProductId(userId, productId);
    }

    @Override
    public List<WishProduct> getWishProductsListForUserWishId(Long id) {
        return wishProductRepository.findAllByUserId(id);
    }

    @Override
    public List<ProductPriceDto> getProductPrices(Long productId) {
        return priceRepository.getPriceForProductWithId(productId).stream()
                .map(priceConverter::convertToDto)
                .collect(Collectors.toList());
    }

    private void prepareImageUrl(List<ProductDto> products) {
        products.forEach(this::prepareImageUrl);
    }

    private void prepareImageUrl(ProductDto p) {
        String image = p.getImage();
        if (StringUtils.isEmpty(image)) {
            return;
        }

        p.setImage(image.replace("webapp/src/main/resources/static", ""));
    }

    private ProductAndPricePresentation getPresentation(ProductDto product, List<ShortProductPriceProjection> prices, List<WishProduct> wishProducts) {
        ProductAndPricePresentation presentation = new ProductAndPricePresentation(product, prices.stream()
                .filter(pr -> Objects.equals(pr.getProductId(), product.getId()))
                .findFirst()
                .orElse(null));
        presentation.setHasProductInWishList(wishProducts.stream()
                .anyMatch(wp -> wp.getProduct().getId().equals(presentation.getProduct().getId())));
        return presentation;
    }

    private List<ProductAndPricePresentation> getProductAndPricePresentationList(List<ProductDto> products, Principal principal) {
        List<Long> ids = products.stream()
                .map(ProductDto::getId)
                .collect(Collectors.toList());

        List<WishProduct> wishProducts = Collections.emptyList();
        if (principal != null && principal.getName() != null && !principal.getName().isBlank()) {
            Optional<User> user = userRepository.findByEmail(principal.getName());
            if (user.isPresent()) {
                wishProducts = wishProductRepository.findAllByUserIdAndProductIdIn(user.get().getId(), ids);
            }
        }

        List<ShortProductPriceProjection> prices = priceRepository.findFreshPricesForProductsWithIds(ids);
        List<WishProduct> finalWishProducts = wishProducts;
        return products.stream().map(p -> getPresentation(p, prices, finalWishProducts)).collect(Collectors.toList());
    }
}
