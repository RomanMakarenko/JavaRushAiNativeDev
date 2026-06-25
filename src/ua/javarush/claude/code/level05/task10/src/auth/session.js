// Керує життєвим циклом access/refresh токенів для вебклієнта.
// Продуктовий код — змінювати його в цій задачі НЕ потрібно.

export function getActiveToken(session) {
  // ПОМИЛКА (issue #482): коли refresh token сплив, `session` вище в стеку
  // виявляється null, і цей рядок падає замість редиректу на /login.
  return session.token;
}

export function isExpired(token) {
  return !token || token.expiresAt < Date.now();
}