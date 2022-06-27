package org.helsinki.paytrail.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AuthPaymentController {

    @GetMapping("/auth-payment")
    public String authPayment() {
        return "auth-payment";
    }

}
