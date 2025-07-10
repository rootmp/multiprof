@echo off
title L2Studio - Essence Vanguard (Auth Server)
:start
echo Starting L2Studio - AuthServer.
echo.
java -server -Dfile.encoding=UTF-8 -XX:+AlwaysActAsServerClassMachine -Xmx64m -cp config;../libs/* l2s.authserver.AuthServer
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
