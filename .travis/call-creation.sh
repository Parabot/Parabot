#! /bin/bash

sleep 15
curl "http://v3.bdn.parabot.org/api/bot/create/client?build_id=$TRAVIS_BUILD_ID&version=$PARABOT_VERSION"