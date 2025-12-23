#!/usr/bin/env sh

# Execute this file after first cloning the repository.

set -e

SCRIPT_DIR=$(CDPATH= cd -- "$(dirname -- "$0")" && pwd) || exit 1
cd "$SCRIPT_DIR" || exit 1

git submodule update --init --recursive
echo "🔍 Checking for npx..."

if ! command -v npx >/dev/null 2>&1; then
  echo "❌ npx not found. Installing Node.js..."

  if command -v brew >/dev/null 2>&1; then
    # macOS (Homebrew)
    brew install node
  elif command -v apt-get >/dev/null 2>&1; then
    # Ubuntu / Debian
    sudo apt-get update
    sudo apt-get install -y nodejs npm
  elif command -v dnf >/dev/null 2>&1; then
    # Fedora
    sudo dnf install -y nodejs npm
  elif command -v pacman >/dev/null 2>&1; then
    # Arch Linux
    sudo pacman -Sy --noconfirm nodejs npm
  elif command -v apk >/dev/null 2>&1; then
    # Alpine Linux
    sudo apk add --no-cache nodejs npm
  else
    echo "⚠️  Package manager not supported."
    echo "Please install Node.js manually: https://nodejs.org"
    exit 1
  fi
else
  echo "✅ npx already installed"
fi

echo "Setting git hooks"
git config --local core.hooksPath ./toolkit/hooks
git config --local commit.template ./toolkit/resources/git-commit-msg-template.txt
# Move to repo root (safe for hooks & scripts)
REPO_ROOT=$(git rev-parse --show-toplevel 2>/dev/null || pwd)
cd "$REPO_ROOT" || exit 1

# Detect existing Gradle build
if [ -f build.gradle ] || \
   [ -f build.gradle.kts ] || \
   [ -f settings.gradle ] || \
   [ -f settings.gradle.kts ] || \
   [ -f gradlew ]; then
  echo "✅ Gradle project already exists — skipping init"
  exit 0
fi

if ! command -v gradle >/dev/null 2>&1; then
  echo "🔧 Installing Gradle..."
  if command -v brew >/dev/null 2>&1; then
    # macOS
    brew install gradle
  elif command -v apt-get >/dev/null 2>&1; then
    # Debian / Ubuntu
    sudo apt-get update
    sudo apt-get install -y gradle
  elif command -v pacman >/dev/null 2>&1; then
    # Arch Linux
    sudo pacman -Sy --noconfirm gradle
  elif command -v apk >/dev/null 2>&1; then
    # Alpine Linux
    sudo apk add --no-cache gradle
  else
    echo "❌ Unsupported platform. Install Gradle manually:"
    echo "https://gradle.org/install/"
    exit 1
  fi
fi
# Ensure Gradle is available
if ! command -v gradle >/dev/null 2>&1; then
  echo "❌ Gradle not found. Please install Gradle first."
  exit 1
fi

echo "🚀 Initializing Gradle project..."
gradle init --project-name "$(basename "$REPO_ROOT")"

echo "✅ Gradle initialized"
