#!/usr/bin/env bash
# Локальний симулятор hook lifecycle.
#
# Використання:
#   bash scripts/simulate_hook.sh <event> <file> [tool]
#
# Приклад:
#   bash scripts/simulate_hook.sh beforeToolUse .env.local Write
#
# Симулятор знаходить hook-конфіг з відповідним event, грубо перевіряє matcher
# за шляхом і інструментом і при збігу запускає handler. Якщо handler
# завершився ненульовим exit code, друкує "blocked result".
set -uo pipefail

EVENT="${1:-}"
FILE="${2:-}"
TOOL="${3:-}"

if [ -z "$EVENT" ] || [ -z "$FILE" ]; then
  echo "usage: simulate_hook.sh <event> <file> [tool]" >&2
  exit 2
fi

HOOKS_DIR=".claude/hooks"
matched_config=""

for cfg in "$HOOKS_DIR"/*.json; do
  [ -f "$cfg" ] || continue
  cfg_event="$(python3 -c "import json,sys;print(json.load(open(sys.argv[1])).get('event',''))" "$cfg" 2>/dev/null)"
  [ "$cfg_event" = "$EVENT" ] || continue

  # Перевірка matcher за tool (якщо вказано в конфігу).
  cfg_tool="$(python3 -c "import json,sys;print(json.load(open(sys.argv[1])).get('matcher',{}).get('tool',''))" "$cfg" 2>/dev/null)"
  if [ -n "$cfg_tool" ] && [ -n "$TOOL" ] && [ "$cfg_tool" != "$TOOL" ]; then
    continue
  fi

  # Груба перевірка matcher за .env-шляхами.
  if python3 -c "import json,sys;m=json.load(open(sys.argv[1])).get('matcher',{});p=m.get('paths',[]);import fnmatch;sys.exit(0 if any(fnmatch.fnmatch(sys.argv[2], pat) or fnmatch.fnmatch(sys.argv[2], pat.replace('**/','')) for pat in p) else 1)" "$cfg" "$FILE" 2>/dev/null; then
    matched_config="$cfg"
    break
  fi
done

if [ -z "$matched_config" ]; then
  echo "no matching hook for event=$EVENT file=$FILE tool=$TOOL"
  exit 0
fi

cmd="$(python3 -c "import json,sys;print(json.load(open(sys.argv[1])).get('handler',{}).get('command',''))" "$matched_config")"
echo "matched hook: $matched_config"
echo "running handler: $cmd $FILE"

# Команду з конфига запускаємо через bash, щоб не залежати від біта +x.
bash $cmd "$FILE"
handler_rc=$?

if [ "$handler_rc" -ne 0 ]; then
  echo "blocked result (exit code $handler_rc)"
  exit 0
fi

echo "allowed result (exit code 0)"
exit 0