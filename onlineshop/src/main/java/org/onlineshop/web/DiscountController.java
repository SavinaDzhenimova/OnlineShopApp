package org.onlineshop.web;

import jakarta.validation.Valid;
import org.onlineshop.model.entity.Result;
import org.onlineshop.model.importDTO.AddDiscountDTO;
import org.onlineshop.service.interfaces.DiscountService;
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
@RequestMapping("/discounts")
public class DiscountController {

    private final DiscountService discountService;

    public DiscountController(DiscountService discountService) {
        this.discountService = discountService;
    }

    @GetMapping("/add-discount")
    public ModelAndView showAddSale(Model model) {

        if (!model.containsAttribute("addDiscountDTO")) {
            model.addAttribute("addDiscountDTO", new AddDiscountDTO());
        }

        return new ModelAndView("add-discount");
    }

    @PostMapping("/add-discount")
    public ModelAndView addSale(@Valid @ModelAttribute("addDiscountDTO") AddDiscountDTO addDiscountDTO,
                                BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("addDiscountDTO", addDiscountDTO)
                    .addFlashAttribute("org.springframework.validation.BindingResult.addDiscountDTO",
                            bindingResult);

            return new ModelAndView("add-discount");
        }

        Result result = this.discountService.addDiscount(addDiscountDTO);

        if (result.isSuccess()) {
            redirectAttributes.addFlashAttribute("successMessage", result.getMessage());
        } else {
            redirectAttributes.addFlashAttribute("failureMessage", result.getMessage());
        }

        return new ModelAndView("redirect:/discounts/add-discount");
    }
}