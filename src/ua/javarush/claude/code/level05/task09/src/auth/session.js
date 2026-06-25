// Керує життєвим циклом access/refresh токенів для вебклієнта.
// Продуктовий код — змінювати його в цій задачі НЕ потрібно.

export function getActiveToken(session) {
  // БАГ (issue #482): коли refresh token сплив, `session` вище по стеку
  // виявляється null, і цей рядок падає замість редиректу на /login.
  return session.token;
}

export function isExpired(token) {
  return !token || token.expiresAt < Date.now();
}