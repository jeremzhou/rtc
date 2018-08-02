#!/bin/sh

MODULENAME="rtc"
version=`ls ../lib/${MODULENAME}* | awk -F/ '{ print $NF }' | sed -n '1p' | sed -e "s/${MODULENAME}.//g;s/.jar//g"`

echo "        Module: ${MODULENAME}"
echo "System Version: ${version}"