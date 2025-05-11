package org.onlineshop.web;

import org.onlineshop.model.exportDTO.OpinionDTO;
import org.onlineshop.service.interfaces.OpinionService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
public class HomeController {

    private final OpinionService opinionService;

    public HomeController(OpinionService opinionService) {
        this.opinionService = opinionService;
    }

    @GetMapping("/")
    public ModelAndView index() {

        ModelAndView modelAndView = new ModelAndView("index");

        List<OpinionDTO> opinionsForIndexPage = this.opinionService.getOpinionsForIndexPage();

        modelAndView.addObject("opinions", opinionsForIndexPage);

        return modelAndView;
    }

    @GetMapping("/about-us")
    public ModelAndView about() {

        return new ModelAndView("about-us");
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

    @GetMapping("/choose-size")
    public ModelAndView chooseSize() {

        return new ModelAndView("choose-size");
    }

    @GetMapping("/brands")
    public ModelAndView brands() {

        return new ModelAndView("brands");
    }
}