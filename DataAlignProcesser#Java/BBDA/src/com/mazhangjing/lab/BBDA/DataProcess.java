package com.mazhangjing.lab.BBDA;

import com.mazhangjing.lab.util.Soup;
import javafx.concurrent.Task;

import java.io.*;
import java.util.*;


public class DataProcess implements Soup {

    public final static String VERSION = "0.4.1";
    private double ExpStart;
    private double ExpEnd;

    public String getVersion() { return VERSION; }

    private void setData(String s1, String s2, String e1, String e2) {
        ExpStart = (parseData(s1) + parseData(s2))/2;
        ExpEnd = (parseData(e1) + parseData(e2))/2;
    }

    private Double parseData(String s) {
        String[] list = s.split(" ")[1].split(":");
        return Double.parseDouble(list[0])*60*60 + Double.parseDouble(list[1])*60 +
                Double.parseDouble(list[2]);
    }

    /**从日志文件解析平衡板数据，最大支持10万条数据*/
    List getExpData(String filename){
        try {
            List list = parseExpData(filename);
            return list;
        } catch (Exception e) { throw new RuntimeException(e); }
    }

    /**解析MATLAB数据，csv格式文件，必须符合要求的格式：不能有head，并且对应列含有对应元素*/
    Map[] getBlanceData(String filename) {
        try {
            Map[] list = parseBlanceData(filename);
            return list;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private List parseExpData(String filepath) throws Exception {
        List list = new ArrayList();
        Map data;
        FileReader fr = new FileReader(filepath);
        BufferedReader br = new BufferedReader(fr);
        String s;
        while ((s = br.readLine()) != null) {
            String[] slist = s.split(",");
            data = new LinkedHashMap();
            //得到MATLAB数据的开始时间
            String date = slist[0]+slist[1]+slist[2]+" "+slist[3]+ ":"+slist[4]+":"+slist[5];
            double startSecs = Double.parseDouble(parseData(date).toString());
            //System.out.print("Start Time is " + startSecs);
            double FixedTime = Double.parseDouble(slist[6]);
            //System.out.print("FixedTime(a1) is " + FixedTime);
            //data.put("time",startSecs);
            data.put("a1",Double.parseDouble(slist[6])-FixedTime+startSecs);
            data.put("a2",Double.parseDouble(slist[7])-FixedTime+startSecs);
            data.put("b1",Double.parseDouble(slist[8])-FixedTime+startSecs);
            data.put("b2",Double.parseDouble(slist[9])-FixedTime+startSecs);
            data.put("c1",Double.parseDouble(slist[10])-FixedTime+startSecs);
            data.put("c2",Double.parseDouble(slist[11])-FixedTime+startSecs);
            data.put("d1",Double.parseDouble(slist[12])-FixedTime+startSecs);
            data.put("d2",Double.parseDouble(slist[13])-FixedTime+startSecs);
            data.put("e1",Double.parseDouble(slist[14])-FixedTime+startSecs);
            data.put("e2",Double.parseDouble(slist[15])-FixedTime+startSecs);
            list.add(data);
        }
        return list;
    }

    private Map[] parseBlanceData(String filepath) throws Exception {
        Map[] list = new Map[400000];
        FileReader fr = new FileReader(filepath);
        BufferedReader br = new BufferedReader(fr);
        String s; Map map; int index = 0;
        while ((s = br.readLine()) != null) {
            String[] slist = s.split(" ");
            map = new LinkedHashMap();
            map.put("time",Double.parseDouble(slist[0]));
            map.put("sensor1",slist[1]);
            map.put("sensor2",slist[2]);
            map.put("sensor3",slist[3]);
            map.put("sensor4",slist[4]);
            map.put("x",slist[5]);
            map.put("y",slist[6]);
            map.put("f",slist[7]);
            list[index++] = map;
        }
        Double localTime = Double.parseDouble(list[0].get("time").toString());
        for (Map m : list) {
            if (m != null) {
                m.put("time", Double.parseDouble(m.get("time").toString())/1000-localTime/1000+ExpStart);
            } else continue;
        }
        return list;
    }

    /**对于MATLAB数据的每一行、每一列，查找平衡板上对应时间的表现情况。
     * Warning: 平衡板数据最大支持100_000条数据*/
    private List margeData(List<Map> expData, Map[] blanceData) {
        //对于实验数据中的每一列进行遍历
        for (String col : "a1 a2 b1 b2 c1 c2 d1 d2 e1 e2".split(" ")) {
            System.out.println("Processing " + col);
            //对于实验数据中的每一列的每一行进行遍历
            for (Map m : expData) {
                //System.out.print("\tProcessing " + m);
                double needTime = Double.parseDouble(m.get(col).toString());
                double nowGet;
                //对100000条数据进行遍历，寻找对应实验数据Cell的近似时间点
                int index = -1;
                for (Map bm : blanceData) {
                    index++;
                    if (bm == null) continue;
                    nowGet = (double)bm.get("time");
                    double diff = Math.abs(nowGet-needTime);
                    if (diff < 0.01) {
                        //System.out.System.out.println("Find!");
                        m.put(col +"_similar",bm);
                        m.put(col + "_path_index",index);
                        break;
                    } else if (diff >= 0.01 && diff < 0.1) {
                        //System.out.println("Find but not extract !");
                        m.put(col +"_similar",bm);
                        m.put(col + "_path_index",index);
                        break;
                    }
                }
                //如果找不到合适的数据，那么直接get就为null，不用设置 similar 的值为 null
            }
        }
        return expData;
    }

    /**用来计算 a-e 的 1-2 之间的数据平均偏移*/
    private List SetPathCost(List<Map> expData, Map[] blanceData) {
        //对于每一行数据分别计算
        for (Map m : expData) {
            //对于a-e每一列分别计算偏移量
            for (String col : "a b c d e".split(" ")) {
                double result = 0.0; double result2_x = 0.0; double result2_y = 0.0;
                //如果不存在path_index，则设置为一个很小的值并且退出循环
                if (m.get(col+"1_path_index") == null || m.get(col+"2_path_index") == null) {
                    m.put(col+"_distance",-9999);
                    m.put(col+"_offset_x",-9999);
                    m.put(col+"_offset_y",-9999);
                    continue;
                }
                int from = (int)m.get(col+"1_path_index");
                int to = (int)m.get(col+"2_path_index");
                //遍历这一列的不同偏移点，从后向前查找，计算两点间的欧式距离
                for (int i = to; i > from ; i--) {
                    double get_x = Double.parseDouble(blanceData[i].get("x").toString());
                    double get_y = Double.parseDouble(blanceData[i].get("y").toString());
                    double x_big = Double.parseDouble(blanceData[i].get("x").toString());
                    double y_big = Double.parseDouble(blanceData[i].get("y").toString());
                    double x_small = Double.parseDouble(blanceData[i-1].get("x").toString());
                    double y_small = Double.parseDouble(blanceData[i-1].get("y").toString());
                    result += Math.sqrt((x_big - x_small)*(x_big - x_small) +
                            (y_big - y_small)*(y_big - y_small));
                    result2_x += get_x;
                    result2_y += get_y;
                }
                //因为遍历时没有使用from处的数值，因此补上
                result2_x += Double.parseDouble(blanceData[from].get("x").toString());
                result2_y += Double.parseDouble(blanceData[from].get("y").toString());
                //print(result);
                m.put(col+"_distance",result);
                m.put(col+"_offset_x",result2_x/(to-from+1));
                m.put(col+"_offset_y",result2_y/(to-from+1));
            }
        }
        return expData;
    }

    /**删除多余的列*/
    private List pureOutData(List<Map> expData) {

        //生成新的列名，保存在colItem List中
        String realCol;
        for (Map m : expData){
            for (String col : "a1 a2 b1 b2 c1 c2 d1 d2 e1 e2".split(" ")) {
                realCol = col + "_similar";
                for (String item : "time sensor1 sensor2 sensor3 sensor4 x y f".split(" ")){
                    if (m.get(realCol) == null) {
                        m.put(col + "_" + item, "NULL");
                    } else {
                        m.put(col +  "_" + item,((Map)m.get(realCol)).get(item));
                    }
                }
                m.remove(realCol);
                m.remove(col + "_path_index");
            }
        }
        //System.out.print(expData.get(0).keySet());
        return expData;
    }

    /**输出数据到文件*/
    private void outputData(List<Map> expData, String filePath){
        try {
            PrintWriter pw = new PrintWriter(new BufferedWriter(
                    new FileWriter(filePath)));
            String header = Arrays.toString(expData.get(0).keySet().toArray()).replace("[","").replace("]","");
            pw.println(header);

            for (Map m : expData) {
                String line ="";
                for (String s : header.split(", ")) {
                    line += m.get(s).toString() + ", ";
                }
                line = line.substring(0,line.length()-2);
                pw.println(line);
            }
            pw.flush();
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    public static void main(String[] args) throws Exception{
        System.out.println("欢迎使用平衡板批处理程序，版本" + VERSION + "\nauthor: Corkine Ma\nusage: " +
                "传递9个参数，分别是姓名，ID，条件, 开始时间1、2，结束时间1、2，MATLAB数据地址，平衡板数据地址。如果有空格，使用双引号，各参数使用空格隔开。\n");
        if (args.length != 9) throw new RuntimeException("参数不足");
        System.out.println("读入参数信息");
        String ts = args[3].trim();
        String ts2 = args[4].trim();
        String te = args[5].trim();
        String te2 = args[6].trim();
        String mp = args[7].trim();
        String bp = args[8].trim();
        String name = args[0];
        String id = args[1];
        String cond = args[2];
        DataProcess d = new DataProcess();
        System.out.println("协调实验时间");
        d.setData(ts.trim(),ts2.trim(),te.trim(),te2.trim());
        System.out.println(d.ExpStart + " " + d.ExpEnd);
        System.out.println("读取并处理MATLAB数据");
        List e = d.getExpData(mp.trim()); System.out.println("MATLAB data simple: \n" + e.get(0));
        System.out.println("读取并处理平衡板数据");
        Map[] m = d.getBlanceData(bp.trim()); System.out.println("Blance data simple: \n" + m[0]);
        System.out.println("正在为MATLAB数据匹配平衡板数据");
        List eNew = d.margeData(e,m);
        System.out.println("计算平均距离和平均偏移");
        List out = d.SetPathCost(eNew,m);
        System.out.println("进行最后的清理工作");
        List finals = d.pureOutData(out);
        System.out.println("输出数据中");
        d.outputData(finals,name.trim().toLowerCase() + "_" + id.trim().toLowerCase() + "_" +
                cond.trim().toLowerCase() +"_output.csv");
        System.out.println("成功。数据输出到文件： "+name.trim().toLowerCase() +
                "_" + id.trim().toLowerCase() + "_" +
                cond.trim().toLowerCase() +"_output.csv");
        /*String ts = "2018-06-22 10:35:20.868380";
        String ts2 = "2018-06-22 10:35:25.018380";
        String te = "2018-06-22 10:53:39.280380";
        String te2 = "2018-06-22 10:53:41.380380";
        String mp = "01_jiangwanyue_scr.csv";
        String bp = "01_jiangwanyue_scr_201806221153.log";
        String name = "jiangwenyue";
        String id = "scr";
        String cond = "01";
        DataProcess d = new DataProcess();
        d.setData(ts.trim(),ts2.trim(),te.trim(),te2.trim());
        System.out.println(d.ExpStart + " " + d.ExpEnd);
        List e = d.getExpData(mp.trim()); System.out.println("MATLAB data simple: \n" + e.get(0));
        Map[] m = d.getBlanceData(bp.trim()); System.out.println("Blance data simple: \n" + m[0]);
        List eNew = d.margeData(e,m);
        List out = d.SetPathCost(eNew,m);
        List finals = d.pureOutData(out);
        d.outputData(finals,name.trim().toLowerCase() + "_" + id.trim().toLowerCase() + "_" +
                cond.trim().toLowerCase() +"_output.csv");*/
    }

    @Override
    public Task cook(String...args){
        return new Task() {
            @Override
            public Object call() {
                if (args.length != 9) throw new RuntimeException("参数不足");
                updateProgress(1,100); updateMessage("读入参数信息");
                String ts = args[0].trim();
                String ts2 = args[1].trim();
                String te = args[2].trim();
                String te2 = args[3].trim();
                String mp = args[4].trim();
                String bp = args[5].trim();
                String name = args[6];
                String id = args[7];
                String cond = args[8];
                DataProcess d = new DataProcess();
                updateProgress(5,100); updateMessage("协调实验时间");
                d.setData(ts.trim(),ts2.trim(),te.trim(),te2.trim());
                System.out.println(d.ExpStart + " " + d.ExpEnd);
                updateProgress(15,100); updateMessage("读取并处理MATLAB数据");
                List e = d.getExpData(mp.trim()); System.out.println("MATLAB data simple: \n" + e.get(0));
                updateProgress(30,100); updateMessage("读取并处理平衡板数据");
                Map[] m = d.getBlanceData(bp.trim()); System.out.println("Blance data simple: \n" + m[0]);
                updateProgress(70,100); updateMessage("正在为MATLAB数据匹配平衡板数据");
                List eNew = d.margeData(e,m);
                updateProgress(80,100); updateMessage("计算平均距离和平均偏移");
                List out = d.SetPathCost(eNew,m);
                updateProgress(85,100); updateMessage("进行最后的清理工作");
                List finals = d.pureOutData(out);
                updateProgress(90,100); updateMessage("输出数据中");
                d.outputData(finals,name.trim().toLowerCase() + "_" + id.trim().toLowerCase() + "_" +
                        cond.trim().toLowerCase() +"_output.csv");
                updateProgress(100,100); updateMessage("成功。数据输出到文件： "+name.trim().toLowerCase() +
                        "_" + id.trim().toLowerCase() + "_" +
                        cond.trim().toLowerCase() +"_output.csv");
                updateValue(1);
                return null;
            }
        };

    }
}