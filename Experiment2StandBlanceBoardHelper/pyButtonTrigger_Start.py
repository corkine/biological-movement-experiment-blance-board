# _*_ coding:UTF-8 _*_
import win32api
import win32con
import win32gui
from ctypes import *
import ctypes
import time

__version__ = "0.2.0"

class POINT(Structure):
  _fields_ = [("x", c_ulong),("y", c_ulong)]
def get_mouse_point():
    po = POINT()
    windll.user32.GetCursorPos(byref(po))
    return int(po.x), int(po.y)
def mouse_click(x=None,y=None):
    if not x is None and not y is None:
        mouse_move(x,y)
        time.sleep(0.05)
    win32api.mouse_event(win32con.MOUSEEVENTF_LEFTDOWN, 0, 0, 0, 0)
    win32api.mouse_event(win32con.MOUSEEVENTF_LEFTUP, 0, 0, 0, 0)
def mouse_dclick(x=None,y=None):
    if not x is None and not y is None:
        mouse_move(x,y)
        time.sleep(0.05)
    win32api.mouse_event(win32con.MOUSEEVENTF_LEFTDOWN, 0, 0, 0, 0)
    win32api.mouse_event(win32con.MOUSEEVENTF_LEFTUP, 0, 0, 0, 0)
    win32api.mouse_event(win32con.MOUSEEVENTF_LEFTDOWN, 0, 0, 0, 0)
    win32api.mouse_event(win32con.MOUSEEVENTF_LEFTUP, 0, 0, 0, 0)
def mouse_move(x,y):
    windll.user32.SetCursorPos(x, y)
def key_input(str=''):
    for c in str:
        win32api.keybd_event(VK_CODE[c],0,0,0)
        win32api.keybd_event(VK_CODE[c],0,win32con.KEYEVENTF_KEYUP,0)
        time.sleep(0.01)

if __name__ =="__main__":
    import datetime
    import sys
    name = input("Input name:   ____\b\b\b\b")
    numb = input("Input ID:  ____\b\b\b\b")
    cond = input("Input Condition:  ____\b\b\b\b")
    print("\nWait for 5 secs...\n")
    temp_out, temp_err = sys.stdout, sys.stderr
    stream = open("python_runtime_record.log","a+",encoding="utf8")
    sys.stdout = sys.stderr = stream
    print("%s\nSTART\n%s\n"%("="*30,"="*30))
    print("\nname: %s ; id: %s ; cond: %s\n\n"%(str(name),str(numb),str(cond)))
    time.sleep(5)
    print(datetime.datetime.fromtimestamp(time.time()),"\n")
    #参数根据 小新Pro13 100%缩放 1280*800 设置 - 使用 QQ/微信截图很容易定位到按钮，注意执行后迅速切换到数据搜集程序
    #zero_sensor
    mouse_click(1120,180)
    time.sleep(2)
    #clear
    mouse_click(1149,232)
    time.sleep(2)
    #record
    mouse_click(1088,209)
    print(datetime.datetime.fromtimestamp(time.time()))
    sys.stdout, sys.stderr = temp_out, temp_err
    
    
    