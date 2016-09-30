#!/bin/bash

JAVA=java

dir=`dirname $0`

CLASSPATH=~/.m2/repository/org/bouncycastle/bcprov-jdk15on/1.47/bcprov-jdk15on-1.47.jar:~/.m2/repository/org/apache/httpcomponents/httpclient/4.5.2/httpclient-4.5.2.jar:~/.m2/repository/org/json/json/20140107/json-20140107.jar:~/.m2/repository/log4j/log4j/1.2.6/log4j-1.2.6.jar:~/.m2/repository/org/apache/httpcomponents/httpcore/4.4.4/httpcore-4.4.4.jar:~/.m2/repository/commons-logging/commons-logging/1.1.1/commons-logging-1.1.1.jar:./target/ess-sdk.jar:./target/ess-sdk-tests.jar

$JAVA -Xmx512m -classpath $CLASSPATH ee.signwise.sdk.tests.TestContainerExtractFile  ./conf/SignWise.cfg "$@"
