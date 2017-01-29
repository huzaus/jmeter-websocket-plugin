#!/usr/bin/env bash
if [ ! -d "$JMETER_HOME" ]; then
    echo "Installing jmeter..."
    cd $HOME
    echo "Downloading jmeter..."
    wget -c http://ftp.ps.pl/pub/apache/jmeter/binaries/apache-jmeter-3.1.tgz # download jmeter
    echo "Extracting jmeter..."
    tar -xf apache-jmeter-3.1.tgz # unpack jmeter
fi


