package pricetracker.controller;

import static pricetracker.controller.ControllersConstants.INDEX_PAGE;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping("/")
    public String viewIndexPage() {
        return INDEX_PAGE;
    }
}
