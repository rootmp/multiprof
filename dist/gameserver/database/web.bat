@echo off
title L2Studio - Essence Vanguard (WebServer Install)
set PATH=%PATH%;%ProgramFiles%\MySQL\MySQL Server 8.0\bin

if exist db_settings.conf goto settings

echo Can't find db_settings.conf file!
goto end

:settings

for /r web %%f in (*.sql) do ( 
                echo Loading %%~nf ...
		mysql --defaults-extra-file=db_settings.conf < %%f
	)
:end

pause
