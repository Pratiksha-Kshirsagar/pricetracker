package pricetracker.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import pricetracker.data.product.ProductService;
import pricetracker.data.user.UserService;
import pricetracker.dto.user.UserDto;

import static pricetracker.controller.ControllersConstants.IS_SUCCESSFULLY_REGISTERED_ATTR;
import static pricetracker.controller.ControllersConstants.IS_USED_EMAIL_ATTR;
import static pricetracker.controller.ControllersConstants.USER_ATTR;
import static pricetracker.controller.ControllersConstants.WISH_LIST_ATTR;
import static pricetracker.util.LayoutUtils.LOGIN_PAGE;
import static pricetracker.util.LayoutUtils.PROFILE_PAGE;

import java.security.Principal;
import java.util.Optional;

@Controller
public class UserManageController {

    private final UserService userService;
    private final ProductService productService;

    @Autowired
    public UserManageController(UserService userService, ProductService productService) {
        this.userService = userService;
        this.productService = productService;
    }

    @GetMapping("/login")
    public String viewLoginPage(Model model) {
        model.addAttribute(USER_ATTR, new UserDto());
        return LOGIN_PAGE;
    }

    @GetMapping("/profile")
    public String viewProfilePage(Principal principal, Model model) {
        UserDto user = userService.getUser(principal);
        model.addAttribute(WISH_LIST_ATTR, productService.getWishProductsListForUserWishId(user.getId()));
        model.addAttribute(USER_ATTR, user);
        return PROFILE_PAGE;
    }

    @PostMapping("/registration")
    public String registration(UserDto dto, Model model) {
        Optional<String> email = Optional.ofNullable(dto).map(UserDto::getEmail);

        if (email.isPresent() && !userService.isEmailUsed(email.get())) {
            userService.saveNewUser(dto);
            model.addAttribute(IS_SUCCESSFULLY_REGISTERED_ATTR, true);
        } else {
            model.addAttribute(IS_USED_EMAIL_ATTR, true);
        }

        model.addAttribute(USER_ATTR, new UserDto());
        return LOGIN_PAGE;
    }

    @PostMapping("/updateUserInfo")
    public String updateUserInfo(UserDto dto, Model model) {
        userService.updateUser(dto);
        model.addAttribute(USER_ATTR, userService.getUser(dto.getEmail()).orElseThrow(IllegalArgumentException::new));
        return PROFILE_PAGE;
    }

    @GetMapping("/addToWishList/{id}")
    public String addToWishList(@PathVariable Long id, Principal principal) {
        productService.addToWishList(id, userService.getUser(principal).getId());
        return "redirect:/product/" + id;
    }

    @GetMapping("/removeFromWishList/{id}")
    public String removeFromWishList(@PathVariable Long id, Principal principal) {
        productService.removeFromWishList(id, userService.getUser(principal).getId());
        return "redirect:/product/" + id;
    }
}
