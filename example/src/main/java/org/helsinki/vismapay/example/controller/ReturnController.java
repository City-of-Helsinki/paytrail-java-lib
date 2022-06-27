package org.helsinki.vismapay.example.controller;

import org.helsinki.vismapay.example.Constants;
import org.helsinki.vismapay.example.service.ReturnHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ReturnController {

	@Autowired
	private ReturnHandler returnHandler;

	@GetMapping("/return-from-pay-page")
	public ModelAndView authPayment(HttpServletRequest request, final RedirectAttributes redirectAttributes) {
		ReturnHandler.Result result = returnHandler.handle(request);

		if (result.isValid()) {
			redirectAttributes.addFlashAttribute(Constants.KEY_MESSAGE, "Payment succeeded");
			return new ModelAndView("redirect:/");
		} else {
			redirectAttributes.addFlashAttribute(Constants.KEY_ERROR, result.getMessage());
			return new ModelAndView("redirect:/error");
		}
	}
}
