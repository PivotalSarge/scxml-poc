#!/bin/sh

java -cp out/production/scxml:/usr/local/lib/commons-scxml2.jar:/usr/local/lib/commons-logging-1.2.jar io.pivotal.prototype.scxml.Main $*
