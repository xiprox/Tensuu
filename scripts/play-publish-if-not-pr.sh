#!/bin/bash
set -ev

if [ "${TRAVIS_PULL_REQUEST}" = "false" ] && [ $TRAVIS_BRANCH == 'master' ]; then
  echo "Assembling and publishing release"
  ./gradlew assembleRelease publishApkRelease
else
  echo "Assembling Debug"
  cp google-services-dummy.json app/google-services.json
  cp google-services-dummy.json app-student/google-services.json
  ./gradlew assembleDebug
fi