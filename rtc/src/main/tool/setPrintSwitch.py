#!/usr/bin/python

import logging
import os
import sys
import threading
import time
import urllib2

logging.basicConfig(level=logging.DEBUG,
                    format='%(asctime)s %(threadName)-8s %(levelname)-5s %(message)s')

HTTP_POST_HEADERS = {'Content-Type': 'text/xml'}


def showHelp():
    logging.info("please input the correct parameters:\n \
                              isPrintUri    - whether is print requested uri? true or false\n \
                              isPrintData   - whether is print collected data? true or false")


def getIpPort(confFile):
    ip = '127.0.0.1'
    port = '9999'
    count = 0
    file = open(confFile)
    line = file.readline()
    while line:
        arrs = line.replace("\r\n", "").replace("\n", "").split(":")
        if (arrs[0] == "      address"):
            ip = arrs[1].strip()
            if ip == '0.0.0.0':
                ip = '127.0.0.1'
            count += 1
        if (arrs[0] == "      port"):
            port = arrs[1].strip()
            count += 1
        if count == 2:
            break
        line = file.readline()
    file.close()
    return ip, port


class HttpPostClient:
    def __init__(self, url, headers, data, timeOutSeconds):
        self.__url = url
        self.__headers = headers
        self.__data = data
        self.__timeOutSeconds = timeOutSeconds

    def doPost(self):
        postReq = urllib2.Request(url=self.__url, headers=self.__headers, data=self.__data)
        try:
            response = urllib2.urlopen(postReq, timeout=self.__timeOutSeconds)
            if (response.getcode() != 200):
                return -1
            else:
                logging.debug("response content:\r\n %s" % (response.read()))
                return 0
        except Exception, e:
            logging.debug("response exception: %s" % (e))
            return -1


class RtcHttpPostTest(threading.Thread):
    def __init__(self, ip, port, content, uri, timeOutSeconds, threadName):
        threading.Thread.__init__(self, name=threadName)
        self.__timeOutSeconds = timeOutSeconds
        self.__postUrl = "http://" + ip + ":" + port + "/" + uri
        self.__content = content

    def run(self):
        logging.info("httpPost start ...")
        logging.info("postUrl: %s content:\n%s" % (self.__postUrl, self.__content))

        startTime = int(round(time.time() * 1000))
        httpClient = HttpPostClient(self.__postUrl, HTTP_POST_HEADERS, self.__content,
                                    self.__timeOutSeconds)
        result = httpClient.doPost()
        usedTime = int(round(time.time() * 1000)) - startTime
        logging.info("httpPost end. result: %s usedTime: %s" % (result, usedTime))


class SetPrintSwitch(RtcHttpPostTest):
    def __init__(self, serverIp, serverPort, content, uri):
        super(SetPrintSwitch, self).__init__(serverIp, serverPort, content, uri, 10, 'setPrintSwitch')

if __name__ == "__main__":

    logging.info("setPrintSwitch start to run ...")

    confFile = None
    if len(sys.argv) == 3:
        isPrintUri = sys.argv[1].lower()
        isPrintData = sys.argv[2].lower()
    else:
        showHelp()
        sys.exit(2)

    flagPool = ('true', 'false')
    if isPrintUri not in flagPool or isPrintData not in flagPool:
        showHelp()
        sys.exit(1)

    uri = 'print-switch?isPrintUri=%s&isPrintData=%s' % (isPrintUri, isPrintData)
    confFile = "../config/rtc.yml"
    (ip, port) = getIpPort(confFile)
    logging.info("setPrintSwitch the ip: %s port: %s uri: %s" % (ip, port, uri))

    setPrintSwitch = SetPrintSwitch(ip, port, None, uri)
    setPrintSwitch.start()

    logging.info("setPrintSwitch end to run.")
