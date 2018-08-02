#!/bin/bash

source ./setEnv.sh
SHELLNAME=`basename $0`

printLog "$SHELLNAME" "start to run rtc."

if [ `ps -ef | grep "rtc" | grep "cn.utstarcom.rtc.RtcApplication" | grep -v grep | wc -l` -eq "1" ]; then
    printLog "$SHELLNAME" "the rtc has been running. exit!"
    exit 0
fi

cd ..
RTC_HOME=`pwd`
export RTC_HOME

if [ `ps -ef | grep "rtc_monitor.py" | grep -v grep | wc -l` -eq "0" ]; then
    printLog "$SHELLNAME" "run rtc_monitor.py script."
    cd ./bin
    nohup ./rtc_monitor.py &
    cd ..
fi

CLASSPATH="$RTC_HOME"/lib/*

[ -d ${RTC_HOME}/logs/gc ] || mkdir -p ${RTC_HOME}/logs/gc
if [ $? -ne "0" ]; then
        printLog "$SHELLNAME" "create gc log dir : ${RTC_HOME}/logs/gc failed! exit!"
        exit 2
fi

printLog "$SHELLNAME" "run rtc main program."
printLog "$SHELLNAME" "rtc used memory size: ${JAVAMEM}"
CURRENT_DATE=`date '+%Y%m%d-%H.%M.%S'`
GC_OPTS="-Xms"${JAVAMEM}" -Xmx"${JAVAMEM}" -d64 -server -XX:+AggressiveOpts -XX:MaxDirectMemorySize=128M -XX:+UseG1GC -XX:MaxGCPauseMillis=400 -XX:G1ReservePercent=15 -XX:InitiatingHeapOccupancyPercent=30 -XX:ParallelGCThreads=16 -XX:ConcGCThreads=4 -XX:+UnlockExperimentalVMOptions -XX:+UnlockDiagnosticVMOptions -XX:G1NewSizePercent=20 -XX:+G1SummarizeConcMark -XX:G1LogLevel=finest -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintGCDateStamps -XX:+PrintAdaptiveSizePolicy -Xloggc:${RTC_HOME}/logs/gc/gc-$CURRENT_DATE.txt -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=10 -XX:GCLogFileSize=10M -XX:+HeapDumpOnOutOfMemoryError"
#performance stable, startup slowly for -XX:+AlwaysPreTouch
#GC_OPTS="-Xms"${JAVAMEM}" -Xmx"${JAVAMEM}" -d64 -server -XX:+AggressiveOpts -XX:+AlwaysPreTouch -XX:MaxDirectMemorySize=1024M -XX:+UseG1GC -XX:MaxGCPauseMillis=400 -XX:G1ReservePercent=15 -XX:InitiatingHeapOccupancyPercent=30 -XX:ParallelGCThreads=16 -XX:ConcGCThreads=4 -XX:+UnlockExperimentalVMOptions -XX:+UnlockDiagnosticVMOptions -XX:G1NewSizePercent=20 -XX:+G1SummarizeConcMark -XX:G1LogLevel=finest -XX:+PrintGCDetails -XX:+PrintGCTimeStamps -XX:+PrintGCDateStamps -XX:+PrintAdaptiveSizePolicy -Xloggc:${RTC_HOME}/logs/gc/gc-$CURRENT_DATE.txt -XX:+UseGCLogFileRotation -XX:NumberOfGCLogFiles=10 -XX:GCLogFileSize=10M -XX:+HeapDumpOnOutOfMemoryError"

$JAVA -server $GC_OPTS -classpath "$CLASSPATH" -Djava.net.preferIPv4Stack=true -Dspring.property.path="${RTC_HOME}" -Dlogging.config=file:"${RTC_HOME}"/config/logback.xml cn.utstarcom.rtc.RtcApplication
