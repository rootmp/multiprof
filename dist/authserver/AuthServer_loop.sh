#!/bin/bash

while :;
do
	java -server -Dfile.encoding=UTF-8 -XX:+AlwaysActAsServerClassMachine -Xmx64m -cp config:../libs/* l2s.authserver.AuthServer > log/stdout.log 2>&1

	[ $? -ne 2 ] && break
	sleep 10;
done
