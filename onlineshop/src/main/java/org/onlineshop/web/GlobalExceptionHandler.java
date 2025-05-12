package org.onlineshop.web;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessDeniedException.class)
    public ModelAndView handleAccessDenied(AccessDeniedException ex) {
        return new ModelAndView("error/403");
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ModelAndView handleResourceNotFound(NoSuchElementException ex) {
        return new ModelAndView("error/404");
    }
}