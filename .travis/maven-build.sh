#! /bin/bash

if [ "$TRAVIS_BRANCH" == "master" ]; then
    mvn -U package
    mkdir -p $TRAVIS_BUILD_DIR/target/apidocs/zips/
else
    mvn -Dbuild.version="-RC-$TRAVIS_BUILD_ID" -U package
fi