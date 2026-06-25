package com.commerceos.auth;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Контролер форми логіну Commerce OS.
 * Рендерить форму і обробляє POST /login.
 */
@Controller
public class LoginController {

    private final SessionService sessionService;

    public LoginController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @GetMapping("/login")
    public String loginForm() {
        return "auth/login";
    }

    @PostMapping("/login")
    public String submit(@RequestParam String email,
                         @RequestParam String password,
                         Model model) {
        boolean ok = sessionService.authenticate(email, password);
        if (ok) {
            return "redirect:/dashboard";
        }
        // Помилка: модель помилки будується, але ім'я view не повертається,
        // через що користувач отримує порожнє тіло відповіді.
        model.addAttribute("error", "Невірний email або пароль");
        return "";
    }
}