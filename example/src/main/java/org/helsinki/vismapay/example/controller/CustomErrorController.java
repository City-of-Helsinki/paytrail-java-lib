package org.helsinki.vismapay.example.controller;

import org.helsinki.vismapay.example.Constants;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CustomErrorController implements ErrorController {

	@RequestMapping("/error")
	public String handleError(@ModelAttribute(Constants.KEY_ERROR) Object error, Model model) {
		addErrorMessageForRender(error, model);
		return "error";
	}

	public String getErrorPath() {
		return null;
	}

	private void addErrorMessageForRender(Object error, Model model) {
		String attributeKey = Constants.KEY_ERROR;
		if (error instanceof Exception) {
			model.addAttribute(attributeKey, ((Exception) error).getMessage());
		} else if(error instanceof String) {
			model.addAttribute(attributeKey, error);
		} else {
			model.addAttribute(attributeKey, null);
		}
	}
}
