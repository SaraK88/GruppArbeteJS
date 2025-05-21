package se.systementor.supershoppen1.shop.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import se.systementor.supershoppen1.shop.model.Product;
import se.systementor.supershoppen1.shop.services.ProductService;
import se.systementor.supershoppen1.shop.services.SubscriberService;

@Controller
public class HomeController {
    private  ProductService productService;
    private SubscriberService subscriberService;
    @Autowired
    public HomeController(ProductService productService, SubscriberService subscriberService) {
        this.productService = productService;
        this.subscriberService = subscriberService;
    }    

    @GetMapping(path="/")
    public String empty(Model model, Authentication authentication)
    {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Object ud = auth.getPrincipal();
        if(ud instanceof UserDetails)
        {
            String user = ((UserDetails)ud).getUsername();
            var va2 = subscriberService.isSubscriber(user);
            model.addAttribute("hideSubscription", va2);
        }
        else{
            model.addAttribute("hideSubscription", false);

        }

        if (authentication != null && authentication.isAuthenticated()
                && authentication.getPrincipal() instanceof OAuth2User user) {


            String username = null;

            // För GitHub
            if (user.getAttribute("login") != null) {
                username = user.getAttribute("login");
            }
            // För Discord
            else if (user.getAttribute("username") != null) {
                username = user.getAttribute("username");
            }
            // För Google
            else if (user.getAttribute("name") != null) {
                username = user.getAttribute("name");
            }

            if (username == null) {
                username = "Guest";
            }

            model.addAttribute("username", username);

        }



        return "home";
    }

    @GetMapping(path="/test2")
    List<Product> getAll(){
        return productService.getAll();
    }

    @GetMapping("/privacy")
    public String privacy() {
        return "privacy";
    }

}
