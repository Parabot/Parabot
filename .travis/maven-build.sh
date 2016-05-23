#!/bin/sh

if [ "$TRAVIS_BRANCH" == "master" ]; then
    mvn -U package
else
    mvn -Dversion="-RC-$TRAVIS_COMMIT" -U package
fi