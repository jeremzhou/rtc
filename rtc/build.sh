#!/bin/bash

function printLog
{
    echo -e ""`date +%Y%m%d-%H%M%S`" - [$1]: $2"
}

SHELLNAME=`basename $0`

if [ "${JAVA_HOME}" == "" ]; then
    printLog "$SHELLNAME" "please set the java environment variable: JAVA_HOME !"
    exit 2
else
    printLog "$SHELLNAME" "JAVA_HOME is : ${JAVA_HOME}"
    java -version
    if [ "$?" -ne "0" ]; then
        printLog "$SHELLNAME" "please add ${JAVA_HOME}/bin to system PATH !"
        exit 2
    fi
fi

if [ "${M2_HOME}" == "" ]; then
    printLog "$SHELLNAME" "please set the maven environment variable: M2_HOME !"
    exit 2
else
    printLog "$SHELLNAME" "maven M2_HOME is : ${M2_HOME}"
    mvn -version
    if [ "$?" -ne "0" ]; then
        printLog "$SHELLNAME" "please add ${M2_HOME}/bin to system PATH !"
        exit 2
    fi
fi

M2_CONF_FILE="${M2_HOME}"/conf/settings.xml
if [ ! -f ${M2_CONF_FILE} ]; then
    printLog "$SHELLNAME" "the maven config file: ${M2_CONF_FILE} don't exists!"
    exit 2
fi

chmod u+x src/main/assembly/copyReleaseNotes.sh
mvn -s ${M2_CONF_FILE} -P linux package
