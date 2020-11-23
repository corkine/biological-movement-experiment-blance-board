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
    print("Wait for 5 secs to stop..")
    temp_out, temp_err = sys.stdout, sys.stderr
    stream = open("python_runtime_record.log","a+",encoding="utf8")
    sys.stdout = sys.stderr = stream
    time.sleep(5)
    print(datetime.datetime.fromtimestamp(time.time()),"\n")
    #end_record
    mouse_click(1085,209)
    time.sleep(2)
    #savefile
    mouse_click(1205,210)
    print(datetime.datetime.fromtimestamp(time.time()))
    print("%s\nEND\n%s\n"%("="*30,"="*30))
    sys.stdout, sys.stderr = temp_out, temp_err
    
    
    