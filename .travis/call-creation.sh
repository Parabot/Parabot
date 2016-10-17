#! /bin/bash

sleep 1
curl --data "build_id=$TRAVIS_BUILD_ID&version=$PARABOT_VERSION" "http://v3.bdn.parabot.org/api/bot/create/client"