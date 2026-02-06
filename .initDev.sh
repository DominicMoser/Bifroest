#!/usr/bin/env sh

# Execute this file after first cloning the repository.

set -e

SCRIPT_DIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd) || exit 1
cd "$SCRIPT_DIR"

echo "Updating git submodules..."
git submodule update --init --recursive

sh "$SCRIPT_DIR/toolkit/scripts/initDevelopmentEnvironment.sh" "$SCRIPT_DIR"
