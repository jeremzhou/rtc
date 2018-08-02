#!/bin/bash

source ./setEnv.sh
SHELLNAME=`basename $0`

printLog "$SHELLNAME" "start to stop rtc."
if [ `ps -ef | grep "rtc" | grep "cn.utstarcom.rtc.RtcApplication" | grep -v grep | wc -l` -gt "0" ] || [ `ps -ef | grep "rtc_monitor.py" | grep -v grep | wc -l` -gt "0" ]; then
    #dump rtc threads
    cTime=`date +%Y%m%d%H%M%S`
    jccPid=`ps -ef | grep "rtc" | grep "cn.utstarcom.rtc.RtcApplication" | grep -v grep | awk '{ print $2 }'`
    printLog "$SHELLNAME" "dump rtc threads. rtc pid: "$jccPid""
    if [ ! -z "$jccPid" ]; then
        $JAVA_HOME/bin/jstack -l "$jccPid" > ./jstack_"$jccPid"_"$cTime".txt 2>&1
        pstack "$jccPid" > ./pstack_"$jccPid"_"$cTime".txt 2>&1
    fi
    #delete expired file
    expiredDays=30
    printLog "$SHELLNAME" "delete expired rtc dumped threads file. expired days: "$expiredDays""
    find . -type f -name "jstack_*" -mtime +"$expiredDays" | xargs rm -f
    find . -type f -name "pstack_*" -mtime +"$expiredDays" | xargs rm -f
    
    #stop rtc
    ccPid=`ps -ef | grep "rtc" | grep "cn.utstarcom.rtc.RtcApplication" | grep -v grep | awk '{ print $2,$3 }'`
    ccPid=$ccPid" "`ps -ef | grep "rtc_monitor.py" | grep -v grep | awk '{ print $2 }' 2>/dev/null`
    if [ ! -z "$ccPid" ]; then
        printLog "$SHELLNAME" "the rtc pid is "$ccPid""
        printLog "$SHELLNAME" "run kill -9 "$ccPid""
        kill -9 $ccPid
        printLog "$SHELLNAME" "sleep 3 secconds...."
        sleep 3
        while [ `ps -ef | grep "rtc" | grep "cn.utstarcom.rtc.RtcApplication" | grep -v grep | wc -l` -gt "0" ] || [ `ps -ef | grep "rtc_monitor.py" | grep -v grep | wc -l` -gt "0" ]
        do
            printLog "$SHELLNAME" "kill rtc again!!!"
            printLog "$SHELLNAME" "kill -9 "$ccPid""
            kill -9 $ccPid
            printLog "$SHELLNAME" "sleep 1 secconds...."
            sleep 1
            if  [ `ps -ef | grep "rtc" | grep "cn.utstarcom.rtc.RtcApplication" | grep -v grep | wc -l` -eq "0" ] && [ `ps -ef | grep "rtc_monitor.py" | grep -v grep | wc -l` -eq "0" ]; then
                break
            fi
        done
        printLog "$SHELLNAME" "the rtc stop completly." 
    fi
else
    printLog "$SHELLNAME" "the rtc not run!"
fi
