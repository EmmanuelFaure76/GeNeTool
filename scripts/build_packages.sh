#!/usr/bin/env bash

orig_pkg_dir=$1
jar_path=$2
work_dir=$3
# Store the absolute path of build script directory
build_script_dir="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"

cd $work_dir

platform="GeNeTool.windows32"
if [ -f $orig_pkg_dir/$platform.zip ]
then
  echo $platform
  unzip $orig_pkg_dir/$platform.zip $platform/*
  cp $jar_path $platform/lib/
  zip -r $platform.zip $platform/
fi
rm -rf $platform

platform="GeNeTool.windows64"
if [ -f $orig_pkg_dir/$platform.zip ]
then
  echo $platform
  unzip $orig_pkg_dir/$platform.zip $platform/*
  cp $jar_path $platform/lib/
  zip -r $platform.zip $platform/
fi
rm -rf $platform

platform="GeNeTool.linux32"
if [ -f $orig_pkg_dir/$platform.zip ]
then
  echo $platform
  unzip $orig_pkg_dir/$platform.zip $platform/*
  cp $jar_path $platform/lib/
  zip -r $platform.zip $platform/
fi
rm -rf $platform

platform="GeNeTool.linux64"
if [ -f $orig_pkg_dir/$platform.zip ]
then
  echo $platform
  unzip $orig_pkg_dir/$platform.zip $platform/*
  cp $jar_path $platform/lib/
  zip -r $platform.zip $platform/
fi
rm -rf $platform

platform="GeNeTool.macosx64"
if [ -f $orig_pkg_dir/$platform.zip ]
then
  echo $platform
  unzip $orig_pkg_dir/$platform.zip $platform/*
  cp $jar_path $platform/GeNeTool.app/Contents/Java/GeNeTool.jar
  zip -r $platform.zip $platform -x *.DS_Store*
fi
rm -rf $platform

# The no-JVM OSX build
platform="GeNeTool.macosx64"
target=$platform.nojvm
if [ -f $orig_pkg_dir/$platform.zip ]
then
  echo $target

  unzip $orig_pkg_dir/$platform.zip $platform/*
  # Script for launching the application with native JVM
  cp $build_script_dir/macosx/GeNeTool $platform/GeNeTool.app/Contents/MacOS/GeNeTool
  cp $jar_path $platform/GeNeTool.app/Contents/Java/GeNeTool.jar
  zip -r $target.zip $platform -x *.DS_Store* -x *PlugIns/jdk*
fi
rm -rf $platform
