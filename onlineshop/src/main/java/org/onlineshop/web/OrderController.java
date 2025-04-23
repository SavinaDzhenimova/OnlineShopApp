package org.onlineshop.web;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.onlineshop.model.entity.Result;
import org.onlineshop.model.exportDTO.OrderRequestDTO;
import org.onlineshop.model.importDTO.AddOrderDTO;
import org.onlineshop.model.importDTO.AddOrderItemDTO;
import org.onlineshop.model.user.UserRegisterDTO;
import org.onlineshop.service.interfaces.OrderService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
@RequestMapping("/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping(value = "/create-order", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> createOrder(@RequestBody OrderRequestDTO request, HttpSession session) {

        OrderRequestDTO createdOrder = orderService.createOrder(
                request.getPromoCode(),
                request.getOrderItems()
        );

        session.setAttribute("createdOrder", createdOrder);

        return ResponseEntity.ok().build();
    }

    @GetMapping("/confirmation")
    public ModelAndView showConfirmation(Model model, HttpSession session) {
        Optional<AddOrderDTO> optionalAddOrderDTO = this.orderService.prepareAddOrderDTOFromSession(session);

        if (optionalAddOrderDTO.isEmpty()) {
            return new ModelAndView("redirect:/shopping-cart");
        }

        AddOrderDTO addOrderDTO = optionalAddOrderDTO.get();
        OrderRequestDTO createdOrder = (OrderRequestDTO) session.getAttribute("createdOrder");

        ModelAndView modelAndView = new ModelAndView("order-confirmation");

        if (!model.containsAttribute("addOrderDTO")) {
            model.addAttribute("addOrderDTO", addOrderDTO);
        }

        modelAndView.addObject("orderItems", createdOrder.getOrderItems());
        modelAndView.addObject("promoCode", createdOrder.getPromoCode());
        modelAndView.addObject("totalPrice", createdOrder.getTotalPrice());
        modelAndView.addObject("discount", createdOrder.getDiscount());
        modelAndView.addObject("discountPercent", createdOrder.getDiscountPercent());
        modelAndView.addObject("finalPrice", createdOrder.getFinalPrice());

        return modelAndView;
    }

    @PostMapping("/make-order")
    public ModelAndView makeOrder(@Valid @ModelAttribute("addOrderDTO") AddOrderDTO addOrderDTO,
                                  BindingResult bindingResult, RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("addOrderDTO", addOrderDTO)
                    .addFlashAttribute("org.springframework.validation.BindingResult.addOrderDTO",
                            bindingResult);

            return new ModelAndView("redirect:/orders/confirmation");
        }

        Result result = this.orderService.makeOrder(addOrderDTO);

        if (result.isSuccess()) {
            redirectAttributes.addFlashAttribute("successMessage", result.getMessage());
        } else {
            redirectAttributes.addFlashAttribute("failureMessage", result.getMessage());

            return new ModelAndView("redirect:/orders/confirmation");
        }

        return new ModelAndView("redirect:/orders/success");
    }
}