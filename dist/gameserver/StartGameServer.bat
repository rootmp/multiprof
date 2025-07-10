@echo off
title L2Studio - Essence Vanguard (GameServer)

REM Настройки
set "MAX_USED_HEAP_IN_GB=6"
set "JAVA=C:\Program Files\Java\jdk-17.0.15\bin\java.exe"

:start
echo Starting GameServer.
echo.
"%JAVA%" -server -Dfile.encoding=UTF-8 -XX:+AlwaysActAsServerClassMachine -Xmx%MAX_USED_HEAP_IN_GB%g -cp config;../libs/* l2s.gameserver.GameServer

if ERRORLEVEL 2 goto restart
if ERRORLEVEL 1 goto error
goto end

:restart
echo.
echo Server restarted ...
echo.
goto start

:error
echo.
echo Server terminated abnormally ...
echo.

:end
echo.
echo Server terminated ...
echo.
pause
