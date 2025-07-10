#!/bin/bash
MAX_USED_HEAP_IN_GB=8
while :;
do
	java -server -Dfile.encoding=UTF-8 -XX:+AlwaysActAsServerClassMachine -Xmx"$MAX_USED_HEAP_IN_GB"g -cp config:../libs/* l2s.gameserver.GameServer > log/stdout.log 2>&1

	[ $? -ne 2 ] && break
	sleep 30;
done

