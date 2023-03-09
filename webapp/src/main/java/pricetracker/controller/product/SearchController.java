package pricetracker.controller.product;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pricetracker.data.product.ProductAndPricePresentation;
import pricetracker.data.product.ProductService;
import pricetracker.util.LayoutUtils;

import static pricetracker.controller.ControllersConstants.PRODUCT_AND_PRICE_ATTR;
import static pricetracker.controller.ControllersConstants.QUERY_ATTR;
import static pricetracker.controller.ControllersConstants.SINGLE_PRODUCT_LIST_ATTR;
import static pricetracker.util.LayoutUtils.NUMBER_OF_PRODUCTS_PER_PAGE_AT_A_TIME;
import static pricetracker.util.LayoutUtils.SEARCH_RESULT_LIST_PAGE;

import java.security.Principal;
import java.util.List;

@Controller
public class SearchController {

    private final ProductService productService;

    @Autowired
    public SearchController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/search")
    public String viewSearchResult(@RequestParam(value = "query", required = false) String query,
                                   @RequestParam(value = "page", required = false) Integer selectedPage,
                                   Model model,
                                   Principal principal) {
        if (selectedPage == null || selectedPage < 1) {
            selectedPage = 1;
        }
        if (query == null || query.isBlank()) {

            model.addAttribute(QUERY_ATTR, "");
        } else {
            prepareModel(query.trim(), model, selectedPage, principal);
        }

        return SEARCH_RESULT_LIST_PAGE;
    }

    private void prepareModel(String query, Model model, Integer selectedPage, Principal principal) {
        long countOfProducts = productService.getProductsByNameOrModelContainingPageable(query);
        model.addAttribute(SINGLE_PRODUCT_LIST_ATTR, countOfProducts == 1);
        int countOfPages = (int) Math.ceil(countOfProducts / (float) NUMBER_OF_PRODUCTS_PER_PAGE_AT_A_TIME);

        if (countOfPages > 0) {
            LayoutUtils.preparePagination(model, selectedPage, countOfPages);
            List<ProductAndPricePresentation> productsAndPrices =
                    productService.getProductsByNameOrModelContainingPageable(
                            query, selectedPage - 1, NUMBER_OF_PRODUCTS_PER_PAGE_AT_A_TIME, principal);
            model.addAttribute(PRODUCT_AND_PRICE_ATTR, productsAndPrices);
        }

        model.addAttribute(QUERY_ATTR, query);
    }
}
