#!/bin/bash

source ./setEnv.sh
SHELLNAME=`basename $0`

printLog "$SHELLNAME" "stop rtc ..."
./stop_rtc.sh
printLog "$SHELLNAME" "stop rtc success! start the rtc ..."
nohup ./start_rtc.sh &
printLog "$SHELLNAME" "start rtc completed, please use 'tail -f nohup.out' to check start result."
