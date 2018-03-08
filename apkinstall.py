#!/usr/bin/python
# -*- coding: UTF-8 -*-

import os

def getApkFileName(path):
    fileList = os.listdir(path)
    for file in fileList:
        if os.path.splitext(file)[1] == '.apk':
            return file

apk_path = '/Users/all/AndroidStudioProjects/CandyTool/app/build/outputs/apk/release/'
apkName = getApkFileName(apk_path)
cmd_install = r'adb install ' + apk_path + apkName
cmd_unInstall = r'adb uninstall com.candy.tool'
os.system(cmd_unInstall)
os.system(cmd_install)