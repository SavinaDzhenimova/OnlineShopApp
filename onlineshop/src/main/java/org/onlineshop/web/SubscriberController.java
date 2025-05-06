package org.onlineshop.web;

import org.onlineshop.model.entity.Result;
import org.onlineshop.service.interfaces.SubscriberService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class SubscriberController {

    private final SubscriberService subscriberService;

    public SubscriberController(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @PostMapping("/subscribe")
    public ModelAndView subscribe(@RequestParam("email") String email, @RequestParam("source") String source,
                                  RedirectAttributes redirectAttributes) {

        Result result = this.subscriberService.subscribe(email);

        if (result.isSuccess()) {
            redirectAttributes.addFlashAttribute("successMessage", result.getMessage());
        } else {
            redirectAttributes.addFlashAttribute("failureMessage", result.getMessage());
        }

        String anchor = source.equals("footer") ? "#footer-subscribe" : "#main-subscribe";
        return new ModelAndView("redirect:/" + anchor);
    }
}