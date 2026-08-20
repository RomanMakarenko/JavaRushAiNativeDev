#!/usr/bin/env bash
# package-local.sh — локальний цикл «build → run → smoke → cleanup» для order-service.
#
# Послідовність кроків:
#   1. build    — Gradle збирає fat jar (build/libs/order-service-0.0.1.jar),
#                 після чого Docker будує локальний образ order-service:local.
#   2. run      — контейнер order-service стартує у фоновому режимі на порту 8080.
#   3. smoke    — curl періодично опитує /actuator/health, поки сервіс не стане UP.
#   4. cleanup  — контейнер і локальний образ видаляються; фаза навішана на trap
#                 EXIT, тому прибирає за собою навіть якщо якась фаза впала.
#
# Межі безпеки: скрипт НЕ виконує git commit, git push і НЕ публікує образ
# (немає docker push до registry) — образ лишається лише локальним.
#
# Вимоги:
#   - запущений Docker-демон;
#   - сумісний Gradle 8.x: канонічний ./gradlew, або дистрибутив 8.x з кешу
#     GRADLE_USER_HOME (~/.gradle/wrapper/dists), або глобальний gradle;
#   - вихідний код Spring Boot застосунку з головним класом, щоб bootJar
#     зібрав jar (зараз у репозиторії його немає — build падатиме).

set -euo pipefail

# Корінь проєкту: директорія вище від scripts/.
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT"

IMAGE="order-service:local"
CONTAINER="order-service"
PORT="8080"
HEALTH_URL="http://localhost:${PORT}/actuator/health"

# --- Визначення команд -----------------------------------------------

# Шукаємо gradle для bootJar: спершу канонічний wrapper (якщо є і виконуваний),
# далі локальний дистрибутив 8.x із кешу GRADLE_USER_HOME, наостанок — глобальний.
find_gradle() {
    if [ -x "$ROOT/gradlew" ]; then
        echo "$ROOT/gradlew"
        return
    fi
    local dist
    for dist in "$HOME"/.gradle/wrapper/dists/gradle-8.*/*/gradle-8.*/bin/gradle; do
        if [ -f "$dist" ]; then
            echo "$dist"
            return
        fi
    done
    if command -v gradle >/dev/null 2>&1; then
        echo "gradle"
        return
    fi
    echo "Помилка: не знайдено gradle (ні ./gradlew, ні 8.x у кеші, ні глобального)." >&2
    return 1
}

# --- Фази -------------------------------------------------------------

build() {
    echo "==> build: bootJar ($GRADLE)"
    "$GRADLE" clean bootJar --console=plain
    echo "==> build: docker image $IMAGE"
    docker build -t "$IMAGE" .
}

run() {
    echo "==> run: контейнер $CONTAINER на порту $PORT"
    docker run -d --name "$CONTAINER" -p "$PORT:8080" "$IMAGE"
}

smoke() {
    echo "==> smoke: $HEALTH_URL"
    local deadline=$((SECONDS + 30))
    until curl -fsS "$HEALTH_URL" >/dev/null 2>&1; do
        if [ "$SECONDS" -ge "$deadline" ]; then
            echo "Помилка: сервіс не відповів на $HEALTH_URL протягом 30 с." >&2
            docker logs "$CONTAINER" 2>&1 | tail -20 || true
            return 1
        fi
        sleep 1
    done
    echo "health: $(curl -fsS "$HEALTH_URL")"
}

cleanup() {
    echo "==> cleanup: контейнер $CONTAINER і образ $IMAGE"
    docker rm -f "$CONTAINER" >/dev/null 2>&1 || true
    docker rmi "$IMAGE" >/dev/null 2>&1 || true
    echo "cleanup: готово"
}

# --- Перевірка передумов ----------------------------------------------

if ! command -v docker >/dev/null 2>&1; then
    echo "Помилка: команда docker не знайдена — запустіть Docker Desktop." >&2
    exit 1
fi

# cleanup гарантовано виконується наприкінці — навіть якщо якась фаза впала.
trap cleanup EXIT

# --- Послідовність ----------------------------------------------------

GRADLE="$(find_gradle)"

build
run
smoke

echo "==> OK: локальний цикл завершено (без commit/push/publish)."