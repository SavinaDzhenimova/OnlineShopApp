package org.onlineshop.web;

import jakarta.validation.Valid;
import org.onlineshop.model.entity.Result;
import org.onlineshop.model.exportDTO.PromoCodesListDTO;
import org.onlineshop.model.importDTO.AddPromoCodeDTO;
import org.onlineshop.service.interfaces.PromoCodeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/promo-codes")
public class PromoCodeController {

    private final PromoCodeService promoCodeService;

    public PromoCodeController(PromoCodeService promoCodeService) {
        this.promoCodeService = promoCodeService;
    }

    @GetMapping
    public ModelAndView showPromoCodes() {

        ModelAndView modelAndView = new ModelAndView("promo-codes");

        PromoCodesListDTO promoCodesList = this.promoCodeService.getAllPromoCodes();

        modelAndView.addObject("promoCodes", promoCodesList);

        return modelAndView;
    }

    @GetMapping("/add-promo-code")
    public ModelAndView showAddPromoCode(Model model) {

        if (!model.containsAttribute("addPromoCodeDTO")) {
            model.addAttribute("addPromoCodeDTO", new AddPromoCodeDTO());
        }

        return new ModelAndView("add-promo-code");
    }

    @PostMapping("/add-promo-code")
    public ModelAndView addPromoCode(@Valid @ModelAttribute("addPromoCodeDTO") AddPromoCodeDTO addPromoCodeDTO,
                                     BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("addPromoCodeDTO", addPromoCodeDTO)
                    .addFlashAttribute("org.springframework.validation.BindingResult.addPromoCodeDTO",
                            bindingResult);

            return new ModelAndView("add-promo-code");
        }

        Result result = this.promoCodeService.addPromoCode(addPromoCodeDTO);

        if (result.isSuccess()) {
            redirectAttributes.addFlashAttribute("successMessage", result.getMessage());
        } else {
            redirectAttributes.addFlashAttribute("failureMessage", result.getMessage());

            return new ModelAndView("redirect:/promo-codes/add-promo-code");
        }

        return new ModelAndView("redirect:/users/profile");
    }
}