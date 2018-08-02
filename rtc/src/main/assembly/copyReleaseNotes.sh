#!/bin/bash

function printLog
{
    echo ""`date +%Y%m%d-%H%M%S`" - [$1]: $2"
}

SHELLNAME=`basename $0`

srcfile="`echo $1| sed 's/"//g'`"
dstfile="`echo $2| sed 's/"//g'`"
printLog "$SHELLNAME" "start copy source file: ${srcfile} to releasenotes: ${dstfile}"
cp -p "${srcfile}" "${dstfile}"
if [ "$?" -eq "0" ]; then
    printLog "$SHELLNAME" "copy releasenotes success!"
else
    printLog "$SHELLNAME" "copy releasenotes failed!"
fi
