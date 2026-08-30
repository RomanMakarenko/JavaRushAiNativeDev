#!/bin/sh

file_path=$(jq -r '.tool_input.file_path // .tool_input.path // empty')

case "$file_path" in
  payments/*|*/payments/*|.env*|*/.env*)
    exit 2
    ;;
  *)
    exit 0
    ;;
esac
