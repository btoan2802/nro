@echo off
title NRO Auto Update Restart

REM ===== FIX PATH =====
set PATH=C:\maven\bin;%PATH%

cd /d C:\Nro_4M

echo ===============================
echo Set UTF-8 Encoding
echo ===============================
chcp 65001 > nul
set JAVA_TOOL_OPTIONS=-Dfile.encoding=UTF-8

echo ===============================
echo Pull Source
echo ===============================
git pull

echo ===============================
echo Send BAOTRI message
echo ===============================

powershell -command "$wshell = New-Object -ComObject wscript.shell; $wshell.AppActivate('GameServer'); Start-Sleep -Milliseconds 200; $wshell.SendKeys('baotri{ENTER}')"
powershell -command "$wshell = New-Object -ComObject wscript.shell; $wshell.AppActivate('LoginServer'); Start-Sleep -Milliseconds 200; $wshell.SendKeys('baotri{ENTER}')"

echo Waiting 35 seconds before restart...
timeout /t 35 >nul

echo ===============================
echo Stop Server
echo ===============================

REM 🔥 KILL JAVA TRƯỚC
taskkill /F /IM java.exe >nul 2>&1

REM 🔥 KILL THEO WINDOW TITLE (CMD CHA)
taskkill /F /FI "WINDOWTITLE eq GameServer*" >nul 2>&1
taskkill /F /FI "WINDOWTITLE eq LoginServer*" >nul 2>&1

REM 🔥 KILL LUÔN CHILD PROCESS (QUAN TRỌNG)
for /f "tokens=2" %%a in ('tasklist ^| findstr java.exe') do (
    taskkill /F /PID %%a >nul 2>&1
)

REM 🔥 DỌN CMD TREO
taskkill /F /IM cmd.exe /FI "WINDOWTITLE eq GameServer*" >nul 2>&1
taskkill /F /IM cmd.exe /FI "WINDOWTITLE eq LoginServer*" >nul 2>&1

timeout /t 3 >nul

echo ===============================
echo Build Server (Maven)
echo ===============================

cd /d C:\Nro_4M\Nro_4M
call mvn package -DskipTests

if %errorlevel% neq 0 (
 echo BUILD FAILED
 exit /b 1
)

echo ===============================
echo Start Login Server
echo ===============================

cd /d C:\Nro_4M\ServerLogin
start "LoginServer" cmd /c bat.cmd

timeout /t 3 >nul

echo ===============================
echo Start Game Server
echo ===============================

cd /d C:\Nro_4M\Nro_4M
start "GameServer" cmd /c run.cmd

echo ===============================
echo Server Started
echo ===============================

exit