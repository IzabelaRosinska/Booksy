package mb.booksy.web.controller;

import lombok.extern.slf4j.Slf4j;;
import mb.booksy.exceptions.AuthException;
import mb.booksy.exceptions.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@Slf4j
@ControllerAdvice
public class ErrorHandlerController {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public ModelAndView handleNotFoundException(Exception exception) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("errors/404error");
        modelAndView.addObject("exception", exception);
        return modelAndView;
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(AuthException.class)
    public ModelAndView handleAuthException(Exception exception) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("errors/401error");
        modelAndView.addObject("exception", exception);
        return modelAndView;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(NumberFormatException.class)
    public ModelAndView handleNumberFormatException(Exception exception) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("errors/400error");
        modelAndView.addObject("exception", exception);
        return modelAndView;
    }
}
