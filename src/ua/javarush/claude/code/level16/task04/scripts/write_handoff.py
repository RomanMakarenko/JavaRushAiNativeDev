#!/usr/bin/env python3
"""Збірка машиночитуваного handoff зі stage-input.

Читає inputs/stage-input.json, створює submissions/handoff.json
з полями stage, input_artifact, output_artifact, open_questions, next_step.
"""
import json
import sys
from pathlib import Path


def build_handoff(stage_input):
    return {
        "stage": stage_input.get("stage"),
        "input_artifact": stage_input.get("input_artifact"),
        "output_artifact": stage_input.get("output_artifact"),
        "open_questions": stage_input.get("open_questions", []),
        "next_step": stage_input.get("next_step"),
    }


def main():
    script_dir = Path(__file__).resolve().parent
    project_dir = script_dir.parent  # task04

    input_path = project_dir / "inputs" / "stage-input.json"
    output_path = project_dir / "submissions" / "handoff.json"

    # Дозволити перевизначення через аргументи командного рядка
    if len(sys.argv) > 1:
        input_path = Path(sys.argv[1])
    if len(sys.argv) > 2:
        output_path = Path(sys.argv[2])

    with open(input_path, encoding="utf-8") as f:
        stage_input = json.load(f)

    handoff = build_handoff(stage_input)

    output_path.parent.mkdir(parents=True, exist_ok=True)
    with open(output_path, "w", encoding="utf-8") as f:
        json.dump(handoff, f, ensure_ascii=False, indent=2)

    print(f"handoff записано у {output_path}")


if __name__ == "__main__":
    main()