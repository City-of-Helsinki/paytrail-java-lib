package org.helsinki.paytrail.example.controller;

import org.helsinki.paytrail.VismaPayClient;
import org.helsinki.paytrail.example.Constants;
import org.helsinki.paytrail.example.service.GetVismaPayTokenCommand;
import org.helsinki.paytrail.example.util.Strings;
import org.helsinki.paytrail.model.payment.PaymentMethod;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AuthPaymentController {

	@Autowired
	private GetVismaPayTokenCommand getVismaPayTokenCommand;

	@GetMapping("/auth-payment")
	public String authPayment(HttpServletRequest request, final RedirectAttributes redirectAttributes) {
		String returnUrl = resolveReturnUrl(request);
		String method = request.getParameter("method");
		String selected = request.getParameter("selected");

		if (Strings.isNullOrEmpty(method)) {
			method = PaymentMethod.TYPE_EPAYMENT;
		}

		try {
			String token = getVismaPayTokenCommand.getToken(returnUrl, method, selected);
			return "redirect:" + VismaPayClient.API_URL + "/token/" + token;
		} catch (Exception e) {
			redirectAttributes.addFlashAttribute(Constants.KEY_ERROR, e);
			return "redirect:/error";
		}
	}

	private String resolveReturnUrl(HttpServletRequest request) {
		String serverPort = (request.getServerPort() != 80 && request.getServerPort()!= 433) ? ":" + request.getServerPort() : "";
		return (request.isSecure() ? "https" : "http") + "://" + request.getServerName() + serverPort + "/return-from-pay-page";
	}
}
