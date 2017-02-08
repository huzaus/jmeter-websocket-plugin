# Jmeter Websocket Plugin

## Purpose

The Jmeter Websocket Plugin is a tool for testing websocket based application in asynchronous way.

## Installation

1. Install Jmeter 3.0 or above  
2. Copy required dependencies to JMeter/lib
  * jetty-http-9.1.1.v20140108.jar
  * jetty-io-9.1.1.v20140108.jar
  * jetty-util-9.1.1.v20140108.jar
  * websocket-api-9.1.1.v20140108.jar
  * websocket-client-9.1.1.v20140108.jar
  * websocket-common-9.1.1.v20140108.jar
  * guava-19.0.jar
3. Copy plugin jar from [releases](/releases/latest) to JMeter/leb/ext


## Jmeter Websocket Plugin Elements

### Websocket Session Manager

Websocket test plan should have one Websocket Session Manager.  It is used to handle websocket connections (sessions). 
Additionally it can be used to dump all websocket messages to a file.

![Websocket Session Manager GUI] (/raw/master/docs/websocket-session-manager.png)


### Websocket Open Session Sampler

Websocket Open Session Sampler is used to open new websocket connection for further usage.

![Websocket Open Session Sampler GUI] (/raw/master/docs/websocket-open-session-sampler.png)

### Websocket Message Sampler

Websocket Message Sampler is used to send message to websocket server endpoint using session opened by [Websocket Open Session Sampler].

![Websocket Message Sampler GUI] (/raw/master/docs/websocket-message-sampler.png)

### Websocket Response Regex Assertion

Websocket Response Regex Assertion is used to add expectation of websocket server response to any JMeter Sapmler.

![Websocket Response Regex Assertion GUI] (/raw/master/docs/websocket-message-sampler.png)

## Building

Gradle is used as build tool

Gradle build is required predefined values. Please update local gradle.properties with:
jmeterHome=$JMETER_HOME
jmeterProperties=$JMETER_HOME/bin/jmeter.properties
 
To update JMeter with fresh plugin jar execute:

    gradle copySample
    
To update JMeter with fresh plugin jar and open Jmeter with demo test plan on OS X execute:

    gradle openSampleMac
