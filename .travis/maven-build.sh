#! /bin/bash

if [ "$TRAVIS_BRANCH" == "master" ]; then
    mvn -U package
    mkdir -p $TRAVIS_BUILD_DIR/target/apidocs/zips/
    zip -r $TRAVIS_BUILD_DIR/target/apidocs/zips/$PARABOT_VERSION.zip $TRAVIS_BUILD_DIR/target/apidocs/$PARABOT_VERSION
else
    mvn -Dbuild.version="-RC-$TRAVIS_BUILD_ID" -U package
fi