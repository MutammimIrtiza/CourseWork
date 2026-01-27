@echo off
set SRC=2305008.cpp
set EXE=a
g++ -std=c++17 -DLOCAL -Wshadow -Wall -g -D_GLIBCXX_DEBUG "%SRC%" -o "%EXE%" -fmax-errors=2
if errorlevel 1 (
    echo Compilation failed!
    exit /b
)
echo.
echo Input:
type "%1"
echo.
echo.
echo Output:
"%EXE%" < "%1"
echo.
