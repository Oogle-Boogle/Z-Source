@echo off
title Running Zamron 317!
:Start
java -server -Xmx4000m -XX:+UnlockExperimentalVMOptions -XX:+UseZGC -cp bin;lib/* com.zamron.GameServer
goto:Start
