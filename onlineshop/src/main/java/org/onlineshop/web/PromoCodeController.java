package org.onlineshop.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.onlineshop.model.entity.Result;
import org.onlineshop.model.exportDTO.PromoCodesListDTO;
import org.onlineshop.model.importDTO.AddPromoCodeDTO;
import org.onlineshop.model.importDTO.CartItemRequestDTO;
import org.onlineshop.service.interfaces.PromoCodeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

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

        return new ModelAndView("redirect:/promo-codes");
    }

    @DeleteMapping("/delete-promo-code/{id}")
    public ModelAndView deletePromoCode(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {

        Result result = this.promoCodeService.deletePromoCode(id);

        if (result.isSuccess()) {
            redirectAttributes.addFlashAttribute("successMessage", result.getMessage());
        } else {
            redirectAttributes.addFlashAttribute("failureMessage", result.getMessage());
        }

        return new ModelAndView("redirect:/promo-codes");
    }

    @PostMapping("/apply-promo")
    @ResponseBody
    public Map<String, Object> applyPromo(@RequestPart("promoCode") String promoCode,
                                          @RequestPart("cartData") String cartDataJson) throws JsonProcessingException {

        ObjectMapper mapper = new ObjectMapper();
        List<CartItemRequestDTO> cartItems = Arrays.asList(
                mapper.readValue(cartDataJson, CartItemRequestDTO[].class)
        );

        return promoCodeService.applyPromoCode(promoCode, cartItems);
    }
}