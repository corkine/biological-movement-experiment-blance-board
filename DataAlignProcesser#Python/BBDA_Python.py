
# coding: utf-8

# In[27]:

__version__ = "0.3.1"

import pandas as pd


# # 用户信息和实验开始时间处理

# In[53]:
print("\nYou are now using matlab balance_data_process tool by Corkine Ma\nVersion %s\n\n"%__version__)

try:
    user_d = {
        "name": "shenli",
        "id": "1234",
        "cond": "1",
        "s1":"2018-06-19 17:08:20.954925 ",
        "s2":"2018-06-19 17:08:21.004925",
        "s3":"2018-06-19 17:10:54.060925 ",
        "s4":"2018-06-19 17:10:54.110925",
    }


    # In[ ]:


    user_d["name"] = input("Input name: ____\b\b\b\b").strip()
    user_d["id"] = input("Input id: ____\b\b\b\b").strip()
    user_d["cond"] = input("Input condition: ____\b\b\b\b").strip()
    user_d["s1"] = input("Input start time I: ________\b\b\b\b").strip()
    user_d["s2"] = input("Input start time II: ________\b\b\b\b").strip()
    user_d["s3"] = input("Input end time I: ________\b\b\b\b").strip()
    user_d["s4"] = input("Input end time II: ________\b\b\b\b").strip()


    # In[54]:


    tim1 = user_d["s1"].split(" ")[1].split(":")
    tim2 = user_d["s2"].split(" ")[1].split(":")
    tim3 = user_d["s3"].split(" ")[1].split(":")
    tim4 = user_d["s4"].split(" ")[1].split(":")
    START_T = ((float(tim1[0])*60*60 + float(tim1[1])*60 + float(tim1[2]))+(float(tim2[0])*60*60 + float(tim2[1])*60 + float(tim2[2])))/2
    END_T = ((float(tim3[0])*60*60 + float(tim3[1])*60 + float(tim3[2]))+(float(tim4[0])*60*60 + float(tim4[1])*60 + float(tim4[2])))/2


    # In[58]:


    print("="*40)
    print("Confirm Input Data:\n")
    for k in user_d:
        print("%s - %s\n"%(k,user_d[k]))
    print("\nThe exp start at: ",START_T,"; End at: ",END_T,"\n")
    print("="*40)


    # # 导入传感器数据并换算

    # In[30]:


    print("\nImport the Sensor Data:\n")
    f_d = ["shenli_1234.log"]
    f_d[0] = input("Input the Sensor Data File like: shenli_1234.log\n (only filename.filetype)___________\b\b\b\b\b\b\b").strip()
    f = pd.read_table(f_d[0],sep=" ",header=None,names=["time","sensor1","sensor2","sensor3","sensor4","x","y","f"])
    f.time = f.time/1000
    sensor_start_local_time = f.iloc[0].time
    f["real_time"] = f.time - sensor_start_local_time + START_T


    # In[59]:


    of = f.reindex(columns="real_time sensor1 sensor2 sensor3 sensor4 x y f".split(" ")).rename(columns={"real_time":"time"})
    print("\nThis is the Sensor clear data:\n")
    print(of.head(),"\n")


    # # 导入实验数据并进行时间换算
    # 
    # s 表格是 matlab 计算的数据，我们需要将这些从计算机开机的时间转换成我们定义的时间，a1点的时间为a1点出现时计算机开机时间，start_secs为a1点的日期，经过换算后对应的我们定义的时间（当天的秒数）。

    # In[32]:


    print("\nDealing with MATLAB Data\n")
    s_d = ["shenli_1234_619.xlsx"]
    s_d[0] = input("Input the MATLAB Datafile (like shenli_1234_619.xlsx , only filename but not filepath)\n________\b\b\b\b\b\b\b")
    s = pd.read_excel(s_d[0],names=["year","month","day","hour","mint","secs","a1","a2","b1","b2","c1","c2","d1","d2","e1","e2"])
    s["start_secs"] = s["hour"]*60*60 + s["mint"]*60 + s["secs"]
    s = s.reindex(columns=["start_secs","a1","a2","b1","b2","c1","c2","d1","d2","e1","e2"])
    print("The file looks like:\n",s.head(3))


    # In[33]:


    os = pd.DataFrame()
    for col in list("a1 a2 b1 b2 c1 c2 d1 d2 e1 e2".split(" ")):
        os[col] = s[col] - s.a1 + s.start_secs


    # os 表格表示的就是所有使用我们计时单位（当天的秒数）的时间

    # In[34]:


    print("Clearning Data...\n")
    print("The Clear Data like this \n",os.head())


    # # 数据对齐

    # In[44]:


    print("="*40,"\nNow Dealing with Data Marge...\n")
    oos = os.copy()
    for current_alp in "a1 a2 b1 b2 c1 c2 d1 d2 e1 e2".split(" "):
        for current_lab in "similar_time sensor1 sensor2 sensor3 sensor4 x y f".split(" "):
            oos[current_alp+"_"+current_lab] = 0.00;
    #对于每一种条件进行遍历
    for current_alp in "a1 a2 b1 b2 c1 c2 d1 d2 e1 e2".split(" "):
        print("[PROCESSING] Now is %s , you may waiting for a long time...\n"%current_alp)
        #在每种条件的基础上，对每行数据进行遍历
        for r in os.index:
            #print("row now is %s\n"%r)
            os_time = os[current_alp][r]
            of_row = -1;
            of_row_temp = -1;
            #对于os的每一个单元格，遍历of查找最精确的数据
            for m in of.index:
                of_time = of["time"][m]
                if abs(of_time - os_time) < 0.1:
                    of_row_temp = m
                if abs(of_time - os_time) < 0.01:
                    of_row = m
                    break;
            if of_row == -1:
                if of_row_temp == -1:
                    print("=="*20 + "\n")
                    print("出现错误：对于MATLAB数据：%s - 索引位置 %s 无法匹配合适数据"%(os_time,r+1))
                    continue
                    #raise ValueError("没有匹配数据")
                else:
                    of_row = of_row_temp
            #print("find the of_row is %s\n"%of_row)
            for current_lab in "sensor1 sensor2 sensor3 sensor4 x y f".split(" "):
                oos[current_alp+"_"+current_lab][r] = of[current_lab][of_row]
            oos[current_alp+"_similar_time"][r] = of["time"][of_row]
        #break;#DEBUG


    # In[60]:


    print("\nThe data looks like this:\n",oos.head(20))


    # In[48]:


    start_time = user_d["s1"].strip().replace(" ","_").replace(".","_").replace("-","_").replace(":","_")
    oos.to_csv("%s_%s_%s_clear_data_%s.csv"%(user_d["name"],user_d["id"],user_d["cond"],start_time),encoding="utf8")
    print("\nFinished. Output to %s_%s_%s_clear_data_%s.csv\n\n"%(user_d["name"],user_d["id"],user_d["cond"],start_time))
    print("="*40+"\n")
    
except Exception as e:
    print("出现错误：",e)
input()

