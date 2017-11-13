#!/bin/bash

path=$1
target=$2

pushd $path
res=$target
res+=".zip"
zip -r $res $target
popd
