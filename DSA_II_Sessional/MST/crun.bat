@echo off
setlocal enabledelayedexpansion
g++ -std=c++17 2305008.cpp -o a.exe || exit /b

for %%f in (input\test*.txt) do (
    set "n=%%~nf"
    set "id=!n:test=!"
    a.exe < "%%f" > tmp.txt
    fc /w tmp.txt "%~1\out!id!.txt" >nul && echo Passed test !id! || echo Failed test !id!
    echo.
)

del tmp.txt
pause