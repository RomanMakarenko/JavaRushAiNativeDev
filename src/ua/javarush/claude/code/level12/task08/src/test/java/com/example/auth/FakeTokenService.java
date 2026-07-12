package com.example.auth;

/** Тестова заглушка TokenService. */
class FakeTokenService implements TokenService {

    private final boolean valid;

    FakeTokenService(boolean valid) {
        this.valid = valid;
    }

    @Override
    public boolean verify(String user, String password) {
        return valid;
    }

    @Override
    public String issueAccessToken(String user) {
        return "access-token-for-" + user;
    }

    @Override
    public String resolveUser(String refreshToken) {
        return "alice";
    }
}