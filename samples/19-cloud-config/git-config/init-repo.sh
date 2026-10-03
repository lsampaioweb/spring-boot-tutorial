#!/usr/bin/env bash
# Initialize the local Config Server Git backend (safe to re-run).
set -euo pipefail

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
cd "${SCRIPT_DIR}"

if [[ ! -d .git ]]; then
  git init
fi

git add cloud-config-client
if git diff --cached --quiet; then
  echo "git-config already committed; nothing to do."
  exit 0
fi

git -c user.email="tutorial@localhost" -c user.name="Spring Boot Tutorial" commit -m "Initial cloud-config-client configuration."
echo "Initialized Config Server Git repository at ${SCRIPT_DIR}"
