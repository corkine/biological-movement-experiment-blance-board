#/usr/bin/env python3
import sys, os

def renameIt():
    try:
        os.mkdir("output")
    except: pass
    for _, _, files in os.walk("."):
        for file in files:
            if "output.csv" in file:
                pics = str(file).split("_")
                if pics[1].isdigit(): newName = "%s_%s_%s_%s"%(pics[1],pics[0],pics[2],pics[3])
                else: newName = file
                try:
                    os.rename(file, "output/"+newName )
                except: pass

def replaceXlsx():
    try:
        os.mkdir("xlsx")
    except: pass
    for _, _, files in os.walk("."):
        for file in files:
            if ".xlsx" in file:
                try:
                    os.rename(file, "xlsx/"+file )
                except: pass

def replaceLog():
    try:
        os.mkdir("log")
    except: pass
    for _, _, files in os.walk("."):
        for file in files:
            if ".log" in file:
                try:
                    os.rename(file, "log/"+file )
                except: pass

def replacCsv():
    try:
        os.mkdir("csv")
    except: pass
    for _, _, files in os.walk("."):
        for file in files:
            if ".csv" in file:
                try:
                    os.rename(file, "csv/"+file )
                except: pass

def rollBack():
    try:
        for _, _, files in os.walk("csv"):
            for file in files:
                try:
                    os.rename("csv/" + file, file )
                except: pass
    except: pass

    try:
        for _, _, files in os.walk("log"):
            for file in files:
                try:
                    os.rename("log/" + file, file )
                except: pass
    except: pass

    try:
        for _, _, files in os.walk("xlsx"):
            for file in files:
                try:
                    os.rename("xlsx/" + file, file )
                except: pass
    except: pass

if __name__ == "__main__":
    result = input("====="*10 +
          "\n此脚本将本文件夹所有以 output.csv 结尾的文件移动到 output 目录下\n"
          "将本文件夹所有以 .csv 结尾的文件移动到 csv 目录下\n"
          "将本文件夹所有以 .log 结尾的文件移动到 log 目录下\n"
          "将本文件夹所有以 .xlsx 结尾的文件移动到 xlsx 目录下\n"
          "并且修正文件名顺序，请按下 y 确认, 按下 r 恢复\n" +
          "====="*10 + "\nYour input is >> ")

    if result == "y":
        renameIt()
        replaceXlsx()
        replacCsv()
        replaceLog()
    elif result == "r":
        rollBack()

