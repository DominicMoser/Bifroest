@echo off
setlocal enabledelayedexpansion

:: Execute this file after first cloning the repository.

cd /d "%~dp0"
echo Updating git submodules...
git submodule update --init --recursive
if %ERRORLEVEL% neq 0 (
    echo ❌ Failed to update submodules.
    exit /b %ERRORLEVEL%
)

call "%~dp0toolkit\scripts\initDevelopmentEnvironment.bat" "%~dp0"
