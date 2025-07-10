@echo off
title L2Studio - Essence Vanguard (GameServer)
SET MAX_USED_HEAP_IN_GB=6
SET path="C:\Program Files\BellSoft\LibericaJDK-17\bin"
:start
echo Starting GameServer.
if %MAX_USED_HEAP_IN_GB% gtr 2 java -server -Dfile.encoding=UTF-8 -Xmx%MAX_USED_HEAP_IN_GB%g -cp config;../libs/* l2s.gameserver.GameServer
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
echo Server terminated abnormaly ...
echo.
:end
echo.
echo Server terminated ...
echo.

pause
