package horizon.aether.gaeserver.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class PageController {
    
    @RequestMapping(value = "/main", method = RequestMethod.GET)
    public ModelAndView telephonyState() {
        return new ModelAndView("page/main");
    }

}
