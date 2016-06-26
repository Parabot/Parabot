#! /bin/bash

if [ "$TRAVIS_BRANCH" == "master" ]; then
    mvn -U package
else
    mvn -Dbuild.version="-RC-$TRAVIS_BUILD_ID" -U package
fi