#set JAVA_HOME
JAVA_HOME=/usr/local/jdk1.8.0_74
export JAVA_HOME
JAVA="$JAVA_HOME"/bin/java

#set MALLOC_ARENA_MAX
export MALLOC_ARENA_MAX=4

#resovle: java.io.IOException: Too many open files
ulimit -n 1048576

#heap dump
ulimit -c unlimited

#set java used memory size
totalMem=`awk '{if($1=="MemTotal:") {printf ("%.0f\n",$2/1024);exit 0} }' /proc/meminfo`

JAVAMEM=1024M
if [ $totalMem -le 16384 ]; then
    JAVAMEM=512M
elif [ $totalMem -gt 16384 ] && [ $totalMem -le 49152 ]; then
    JAVAMEM=1024M
else
    JAVAMEM=2048M
fi

#public function - printLog
#function printLog: print normal logs
#input: $1 - shell name
#       $2 - general comments
function printLog
{
    echo ""`date +%Y%m%d-%H%M%S`" - [$1]: $2"
}

#public function - printError
#function printLog: if recived error result, print logs
#input: $1 - shell name
#       $2 - error comments
function printError
{
    if [ $? -ne 0 ]; then
        echo ""`date +%Y%m%d-%H%M%S`" - [$1]: $2"
    fi
}
