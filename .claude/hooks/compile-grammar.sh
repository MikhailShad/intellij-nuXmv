#!/bin/bash
path=$(jq -r '.tool_input | (.file_path, .pathInProject, .input, .patch) // empty' | grep -oE '[^[:space:]]*nuXmv\.(bnf|flex)' | head -1)

[[ -z "$path" ]] && exit 0

cd "${CLAUDE_PROJECT_DIR:-.}" || exit 0

if ! out=$(./gradlew compileKotlin -q 2>&1); then
  {
    echo "Grammar regeneration/compilation failed after editing $(basename "$path"):"
    grep -E '^e: |error:|ERROR|FAILED|\.(bnf|flex|java|kt):[0-9]+' <<<"$out" | head -40
    echo "--- tail ---"
    tail -15 <<<"$out"
  } >&2
  exit 2
fi

exit 0
