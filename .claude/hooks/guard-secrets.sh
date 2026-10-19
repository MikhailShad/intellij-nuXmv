#!/bin/bash
input=$(jq -c '.tool_input // {}')

deny() {
  jq -n --arg r "$1" '{hookSpecificOutput: {hookEventName: "PreToolUse", permissionDecision: "deny", permissionDecisionReason: $r}}'
  exit 0
}

if grep -qiE '(publishPlugin|signPlugin|(sign|publish) plugin)' <<<"$input"; then
  deny "Publishing and signing the plugin are done by the user only."
fi

if grep -qE '(^|[[:space:]/"'"'"'=<:])\.env([[:space:]"'"'"';|&)]|\\|$)' <<<"$input"; then
  deny ".env holds signing and publishing secrets and must not be read."
fi

exit 0
