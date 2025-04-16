package org.onlineshop.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

    @GetMapping("/")
    public ModelAndView index() {
        return new ModelAndView("index");
    }

    @GetMapping("/about-us")
    public ModelAndView about() {

        return new ModelAndView("about-us");
    }

    @GetMapping("/contacts")
    public ModelAndView contacts() {

        return new ModelAndView("contacts");
    }

    @GetMapping("/faq")
    public ModelAndView faq() {

        return new ModelAndView("faq");
    }

    @GetMapping("/privacy-policy")
    public ModelAndView privacyPolicy() {

        return new ModelAndView("privacy-policy");
    }

    @GetMapping("/general-conditions")
    public ModelAndView generalConditions() {

        return new ModelAndView("general-conditions");
    }

    @GetMapping("/delivery-and-payment")
    public ModelAndView deliveryAndPayment() {

        return new ModelAndView("delivery-and-payment");
    }

    @GetMapping("/maintenance-tips")
    public ModelAndView maintenanceTips() {

        return new ModelAndView("maintenance-tips");
    }

    @GetMapping("/loyalty-program")
    public ModelAndView loyaltyProgram() {

        return new ModelAndView("loyalty-program");
    }

    @GetMapping("/exchange-or-return")
    public ModelAndView exchangeOrReturn() {

        return new ModelAndView("exchange-or-return");
    }

    @GetMapping("/choose-size")
    public ModelAndView chooseSize() {

        return new ModelAndView("choose-size");
    }

    @GetMapping("/brands")
    public ModelAndView brands() {

        return new ModelAndView("brands");
    }
}