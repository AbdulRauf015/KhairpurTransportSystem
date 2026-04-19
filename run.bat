@echo off
title Khairpur Transport - Easy Setup
color 1F
echo.
echo  ============================================
echo   KHAIRPUR TRANSPORT MANAGEMENT SYSTEM
echo   Developer: Abdul Rauf
echo  ============================================
echo.

:: AUTO path - works anywhere
set "BASEDIR=%~dp0"
if "%BASEDIR:~-1%"=="\" set "BASEDIR=%BASEDIR:~0,-1%"
echo  Folder: %BASEDIR%
echo.

:: Step 1: Java
echo [1/4] Java check...
set "JAVAC="
set "JAVA="
for /d %%d in ("C:\Program Files\Java\jdk-25" "C:\Program Files\Java\jdk-21" "C:\Program Files\Java\jdk-17" "C:\Program Files\Java\jdk*") do (
    if exist "%%~d\bin\javac.exe" if not defined JAVAC (
        set "JAVAC=%%~d\bin\javac.exe"
        set "JAVA=%%~d\bin\java.exe"
    )
)
if not defined JAVAC ( color 4F & echo ERROR: JDK not found! & pause & exit /b 1 )
echo  Java: %JAVA%

:: Step 2: JAR - search in lib folder
echo.
echo [2/4] JDBC Driver check...
set "JAR="
for %%f in ("%BASEDIR%\lib\*.jar") do set "JAR=%%~f"
if not defined JAR (
    color 4F
    echo.
    echo  ERROR: No .jar file found in lib folder!
    echo  lib folder path: %BASEDIR%\lib\
    echo.
    echo  Contents of lib folder:
    dir "%BASEDIR%\lib\" /b 2>nul || echo  EMPTY or NOT FOUND
    echo.
    echo  ACTION: Put mysql-connector-j-9.6.0.jar in lib folder!
    pause & exit /b 1
)
echo  JAR: %JAR%

:: Step 3: Compile
echo.
echo [3/4] Compiling...
if exist "%BASEDIR%\out" rmdir /s /q "%BASEDIR%\out"
mkdir "%BASEDIR%\out"
set "TMP_SRC=%TEMP%\ktsrc%RANDOM%.txt"
set "TMP_ERR=%TEMP%\kterr%RANDOM%.txt"
dir /s /b "%BASEDIR%\src\*.java" > "%TMP_SRC%" 2>nul
for /f %%c in ('type "%TMP_SRC%" ^| find /c /v ""') do echo  Compiling %%c files...
"%JAVAC%" -encoding UTF-8 -cp "%BASEDIR%\src;%JAR%" -d "%BASEDIR%\out" @"%TMP_SRC%" 2>"%TMP_ERR%"
set CERR=%errorlevel%
del "%TMP_SRC%" >nul 2>&1
if %CERR% NEQ 0 (
    color 4F & echo. & echo  COMPILE ERROR:
    type "%TMP_ERR%"
    del "%TMP_ERR%" >nul 2>&1
    pause & exit /b 1
)
del "%TMP_ERR%" >nul 2>&1
echo  Compiled OK!

:: Step 4: Run
echo.
echo [4/4] Starting app...
echo  Admin: admin/admin123    User: user1/user123
echo.
"%JAVA%" -cp "%BASEDIR%\out;%JAR%" Main
if %errorlevel% NEQ 0 ( echo. & echo  DB Error: Start MySQL and run transport.sql )
pause
