package com.rush.commerce.auth;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Контролер входу користувача.
 * У разі неправильного пароля зараз повертається порожнє тіло відповіді,
 * через що фронтенд рендерить порожній екран.
 */
@RestController
public class LoginController {

    private final SessionService sessionService;

    public LoginController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest request) {
        // Перевірка credentials і створення session
        if (!sessionService.verify(request.username(), request.password())) {
            // TODO: повернути зрозуміле повідомлення про помилку замість порожньої відповіді
            return new LoginResponse(null, null);
        }
        String token = sessionService.start(request.username());
        return new LoginResponse(token, "ok");
    }
}