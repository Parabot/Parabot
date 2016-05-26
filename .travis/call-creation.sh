#! /bin/sh

curl -sS "http://v3.bdn.parabot.org/api/bot/create/client?build_id=$TRAVIS_BUILD_ID&version=2.5.1" >/dev/null