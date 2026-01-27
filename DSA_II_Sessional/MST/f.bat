@echo off
set SRC=2305008.cpp
set EXE=a

g++ -std=c++17 -O2 -Wno-unused-result -Wno-sign-conversion "%SRC%" -o "%EXE%"
if errorlevel 1 (
    echo Compilation failed!
    exit /b
)

echo.
echo Input:
type "%1"
echo.
echo Output:
"%EXE%" < "%1"
echo.
