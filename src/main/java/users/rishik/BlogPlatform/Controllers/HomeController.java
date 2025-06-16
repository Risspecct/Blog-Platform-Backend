package users.rishik.BlogPlatform.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {

    @GetMapping("/docs")
    public String swingUi(){
        return "redirect:/swagger-ui/index.html";
    }
}
