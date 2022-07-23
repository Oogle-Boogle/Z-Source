@echo off
title Running Zamron 317!
java -server -Xmx3600m -XX:+UnlockExperimentalVMOptions -XX:+UseZGC -cp bin;lib/* com.zamron.GameServer
pause