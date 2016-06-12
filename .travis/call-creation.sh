#! /bin/bash

sleep 15
curl -sS "http://v3.bdn.parabot.org/api/bot/create/client?build_id=$TRAVIS_BUILD_ID&version=$PARABOT_VERSION" >/dev/null