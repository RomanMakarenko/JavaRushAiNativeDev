#!/usr/bin/env python3
"""Збірка машиночитуваного handoff зі stage-input.

ВІДОМІ ПРОБЛЕМИ (потрібно виправити):
- втрачається поле next_step під час формування handoff;
- результат завжди записується в загальний файл summary.txt,
  а не в шлях, переданий другим аргументом.
"""
import json
import sys


def build_handoff(stage_input):
    # next_step тут не переноситься — це і є баг
    return {
        "stage": stage_input.get("stage"),
        "input_artifact": stage_input.get("input_artifact"),
        "output_artifact": stage_input.get("output_artifact"),
        "open_questions": stage_input.get("open_questions", []),
    }


def main():
    input_path = sys.argv[1]

    with open(input_path, encoding="utf-8") as f:
        stage_input = json.load(f)

    handoff = build_handoff(stage_input)

    # шлях виводу ігнорується — завжди summary.txt
    with open("summary.txt", "w", encoding="utf-8") as f:
        json.dump(handoff, f, ensure_ascii=False, indent=2)

    print("handoff записано у summary.txt")


if __name__ == "__main__":
    main()