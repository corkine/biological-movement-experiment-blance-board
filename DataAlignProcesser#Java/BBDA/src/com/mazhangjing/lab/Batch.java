package com.mazhangjing.lab;

import com.mazhangjing.lab.BBDA.DataProcess;
import org.junit.Test;
import sun.security.krb5.SCDynamicStoreConfig;

import java.io.InputStreamReader;
import java.io.StringReader;
import java.time.Duration;
import java.time.temporal.Temporal;
import java.util.*;
import java.util.stream.Collectors;

public class Batch {

    @Test
    public List<InputData> parseInputData() {

        String dta = " \"jiangwanyue\" \"01\" \"int\" \"2018-06-22 10:14:17.610380\" \"2018-06-22 10:14:21.760380\" \"2018-06-22 10:33:38.701380\" \"2018-06-22 10:33:40.801380\" \"01_jiangwanyue_int.csv\" \"01_jiangwanyue_int_201806221033.log\" \n" +
                " \"jiangwanyue\" \"01\" \"scr\" \"2018-06-22 10:35:20.868380\" \"2018-06-22 10:35:25.018380\" \"2018-06-22 10:53:39.280380\" \"2018-06-22 10:53:41.380380\" \"01_jiangwanyue_scr.csv\" \"01_jiangwanyue_scr_201806221153.log\" \n" +
                " \"wuhui\" \"02\" \"int\" \"2018-06-26 13:02:52.642453\" \"2018-06-26 13:02:56.792038\" \"2018-06-26 13:22:42.319474\" \"2018-06-26 13:22:44.419264\" \"02_wuhui_int.csv\" \"02_wuhui_201806261323\" \n" +
                " \"wuhui\" \"02\" \"scr\" \"2018-06-26 13:27:10.933610\" \"2018-06-26 13:27:15.083195\" \"2018-06-26 13:48:07.730355\" \"2018-06-26 13:48:09.830355\" \"02_wuhui_scr.csv\" \"02_wuhui_scr_201806261323\" \n" +
                " \"yangkaiyuan\" \"03\" \"int\" \"2018-06-26 14:42:39.282934\" \"2018-06-26 14:42:43.433934\" \"2018-06-26 15:02:37.640934\" \"2018-06-26 15:02:39.742934\" \"03_yangkaiyuan_int.csv\" \"03_yangkaiyuan_int_201806261502\" \n" +
                " \"yangkaiyuan\" \"03\" \"scr\" \"2018-06-26 14:21:53.985934\" \"2018-06-26 14:21:58.135934\" \"2018-06-26 14:41:53.478934\" \"2018-06-26 14:41:55.579934\" \"03_yangkaiyuan_scr.csv\" \"03_yangkaiyuan_scr_201806261442\" \n" +
                " \"liangyingyin\" \"04\" \"int\" \"2018-06-26 17:28:41.083950\" \"2018-06-26 17:28:45.235187\" \"2018-06-26 17:46:13.285132\" \"2018-06-26 17:46:15.385252\" \"04_liangyingyin_int.csv\" \"04_liangyingyin_int_201806261748\" \n" +
                " \"liangyingyin\" \"04\" \"scr\" \"2018-06-26 17:06:50.483083\" \"2018-06-26 17:06:54.633320\" \"2018-06-26 17:26:12.024519\" \"2018-06-26 17:26:14.125639\" \"04_liangyingyin_scr.csv\" \"04_liangyingyin_scr_201806261726\" \n" +
                " \"pengjing\" \"05\" \"int\" \"2018-06-26 18:25:28.944971\" \"2018-06-26 18:25:33.095208\" \"2018-06-26 18:43:12.719815\" \"2018-06-26 18:43:14.820936\" \"05_pengjing_int.csv\" \"05_pengjing_int_201806261843\" \n" +
                " \"pengjing\" \"05\" \"scr\" \"2018-06-26 18:44:07.537951\" \"2018-06-26 18:44:11.688188\" \"2018-06-26 19:00:14.319248\" \"2018-06-26 19:00:16.419368\" \"05_pengjing_scr.csv\" \"05_pengjing_scr_201806261901\" \n" +
                " \"zhouxinqi\" \"06\" \"int\" \"2018-06-28 10:09:56.342341\" \"2018-06-28 10:10:00.492756\" \"2018-06-28 10:26:44.908187\" \"2018-06-28 10:26:47.008398\" \"06_zhouxinqi_int.csv\" \"06_zhouxinqi_int_201906271027\" \n" +
                " \"zhouxinqi\" \"06\" \"scr\" \"2018-06-28 10:28:12.821978\" \"2018-06-28 10:28:16.972393\" \"2018-06-28 10:44:54.945180\" \"2018-06-28 10:44:57.045390\" \"06_zhouxinqi_scr.csv\" \"06_zhouxinqi_scr_201906271045\" \n" +
                " \"huangdongbin\" \"07\" \"int\" \"2018-06-28 17:48:13.634233\" \"2018-06-28 17:48:17.783818\" \"2018-06-28 18:15:12.331347\" \"2018-06-28 18:15:14.432137\" \"07_huangdongbin_int.csv\" \"07_huangdongbin_int_201806281815\" \n" +
                " \"huangdongbin\" \"07\" \"scr\" \"2018-06-28 17:26:08.201789\" \"2018-06-28 17:26:12.351374\" \"2018-06-28 17:46:33.231274\" \"2018-06-28 17:46:35.332064\" \"07_huangdongbin_scr.csv\" \"07_huangdongbin_scr_201806281747\" \n" +
                " \"qianyanchao\" \"08\" \"int\" \"2018-06-28 19:37:52.970998\" \"2018-06-28 19:37:57.120583\" \"2018-06-28 19:54:46.294656\" \"2018-06-28 19:54:48.395445\" \"08_qianchaoyan_int.csv\" \"08_qianchaoyan_int_201806271955.log\" \n" +
                " \"qianyanchao\" \"08\" \"scr\" \"2018-06-28 19:15:49.122396\" \"2018-06-28 19:15:53.273981\" \"2018-06-28 19:36:20.167279\" \"2018-06-28 19:36:22.267069\" \"08_qianchaoyan_scr.csv\" \"08_qianyanchao_scr_201806271936\" \n" +
                " \"2017110682\" \"09\" \"int\" \"2018-10-17 19:56:22.273027\" \"2018-10-17 19:56:26.423264\" \"2018-10-17 20:46:52.145326\" \"2018-10-17 20:46:54.245446\" \"09_2017110682_int.csv\" \"09_2017110682_int.log\" \n" +
                " \"xuxinyi\" \"10\" \"int\" \"2018-10-18 10:40:44.960583\" \"2018-10-18 10:40:49.110583\" \"2018-10-18 11:16:42.279583\" \"2018-10-18 11:16:44.379583\" \"10_xuxinyi_int.csv\" \"10_xuxinyi_all.log\" \n" +
                " \"xuxinyi\" \"10\" \"scr\" \"2018-10-18 10:40:44.960583\" \"2018-10-18 10:40:49.110583\" \"2018-10-18 11:16:42.279583\" \"2018-10-18 11:16:44.379583\" \"10_xuxinyi_scr.csv\" \"10_xuxinyi_all.log\" \n" +
                " \"liweijie\" \"11\" \"int\" \"2018-10-18 12:34:00.985583\" \"2018-10-18 12:34:05.135583\" \"2018-10-18 13:13:54.174583\" \"2018-10-18 13:13:56.274583\" \"11_int.csv\" \"11_liweijie_all.log\" \n" +
                " \"liweijie\" \"11\" \"scr\" \"2018-10-18 12:34:00.985583\" \"2018-10-18 12:34:05.135583\" \"2018-10-18 13:13:54.174583\" \"2018-10-18 13:13:56.274583\" \"11_scr.csv\" \"11_liweijie_all.log\" \n" +
                " \"huanghuayang\" \"12\" \"int\" \"2018-10-18 13:28:00.650583\" \"2018-10-18 13:28:04.800583\" \"2018-10-18 14:05:17.549583\" \"2018-10-18 14:05:19.649583\" \"12_int.csv\" \"12_huanghuayang_all.log\" \n" +
                " \"huanghuayang\" \"12\" \"scr\" \"2018-10-18 13:28:00.650583\" \"2018-10-18 13:28:04.800583\" \"2018-10-18 14:05:17.549583\" \"2018-10-18 14:05:19.649583\" \"12_scr.csv\" \"12_huanghuayang_all.log\" \n" +
                " \"tuzhiting\" \"13\" \"int\" \"2018-10-18 16:10:06.549583\" \"2018-10-18 16:10:10.701583\" \"2018-10-18 16:44:11.147583\" \"2018-10-18 16:44:13.248583\" \"13_int.csv\" \"13_tuzhiting_all.log\" \n" +
                " \"tuzhiting\" \"13\" \"scr\" \"2018-10-18 16:10:06.549583\" \"2018-10-18 16:10:10.701583\" \"2018-10-18 16:44:11.147583\" \"2018-10-18 16:44:13.248583\" \"13_scr.csv\" \"13_tuzhiting_all.log\" \n" +
                " \"wangxinyue\" \"14\" \"int\" \"2018-10-18 17:05:41.089583\" \"2018-10-18 17:05:45.241583\" \"2018-10-18 17:36:36.268584\" \"2018-10-18 17:36:38.368583\" \"14_int.csv\" \"14_wangxinyue_all.log\" \n" +
                " \"wangxinyue\" \"14\" \"scr\" \"2018-10-18 17:05:41.089583\" \"2018-10-18 17:05:45.241583\" \"2018-10-18 17:36:36.268584\" \"2018-10-18 17:36:38.368583\" \"14_scr.csv\" \"14_wangxinyue_all.log\" \n" +
                " \"yangyufei\" \"15\" \"int\" \"2018-10-18 18:02:03.353583\" \"2018-10-18 18:02:07.503583\" \"2018-10-18 18:35:45.530583\" \"2018-10-18 18:35:47.631583\" \"15_int.csv\" \"15_yangyufei_all.log\" \n" +
                " \"yangyufei\" \"15\" \"scr\" \"2018-10-18 18:02:03.353583\" \"2018-10-18 18:02:07.503583\" \"2018-10-18 18:35:45.530583\" \"2018-10-18 18:35:47.631583\" \"15_scr.csv\" \"15_yangyufei_all.log\" \n" +
                " \"chenlihua\" \"16\" \"int\" \"2018-10-18 18:41:22.693583\" \"2018-10-18 18:41:26.845583\" \"2018-10-18 19:16:18.811583\" \"2018-10-18 19:16:20.911583\" \"16_int.csv\" \"16_chenlihua_all.log\" \n" +
                " \"chenlihua\" \"16\" \"scr\" \"2018-10-18 18:41:22.693583\" \"2018-10-18 18:41:26.845583\" \"2018-10-18 19:16:18.811583\" \"2018-10-18 19:16:20.911583\" \"16_scr.csv\" \"16_chenlihua_all.log\" \n" +
                " \"yougaosheng\" \"17\" \"int\" \"2018-10-18 20:00:12.026583\" \"2018-10-18 20:00:16.179583\" \"2018-10-18 20:32:33.942583\" \"2018-10-18 20:32:36.042583\" \"17_int.csv\" \"17_yougaosheng_all.log\" \n" +
                " \"yougaosheng\" \"17\" \"scr\" \"2018-10-18 20:00:12.026583\" \"2018-10-18 20:00:16.179583\" \"2018-10-18 20:32:33.942583\" \"2018-10-18 20:32:36.042583\" \"17_scr.csv\" \"17_yougaosheng_all.log\" \n";

        ArrayList<String> inputData = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String data = scanner.nextLine();
            inputData.add(data);
        }

        List<InputData> result = new ArrayList<>();
        inputData.stream()
                .map(s -> s.trim())
                .filter(s -> s.trim().split("\" \"").length == 9)
                .map(
                        s -> Arrays.stream(s.trim().split("\" \""))
                                .map(c -> c.replace("\"", ""))
                                .collect(Collectors.toList()))
                .filter(s -> {
                    for (String a : s) if (a.isEmpty()) return false;
                    return true; })
                .forEach(s -> result.add(new InputData(s.get(0),s.get(1),s.get(2),s.get(3),s.get(4),s.get(5),s.get(6),s.get(7),s.get(8))));
        System.out.println("Your input is" + result);
        return result;
    }

    class InputData {
        @Override
        public String toString() {
            return "InputData{" +
                    "name='" + name + '\'' +
                    ", id=" + id +
                    '}';
        }

        public InputData(String name, String id, String cond, String startTime1, String startTime2, String endTime1, String endTime2, String mPath, String bPath) {
            this.name = name;
            this.id = id;
            this.cond = cond;
            this.startTime1 = startTime1;
            this.startTime2 = startTime2;
            this.endTime1 = endTime1;
            this.endTime2 = endTime2;
            this.mPath = mPath;
            this.bPath = bPath;
        }

        String name;
        String id;
        String cond;
        String startTime1;
        String startTime2;
        String endTime1;
        String endTime2;
        String mPath;
        String bPath;
    }

    public void doCompute(List<InputData> data) {
        System.out.println("Computing now with cook...");
        data.stream()
                .parallel()
                .forEach(d -> { try {
                                DataProcess.main(new String[]{d.name,d.id,d.cond,
                                        d.startTime1,d.startTime2,d.endTime1,d.endTime2,
                                        d.mPath,d.bPath});
                                } catch (Exception e) { e.printStackTrace(); } }
                );
    }

    public static void main(String[] args) {
        System.out.println("Starting batch process with DataProcess v" + DataProcess.VERSION);
        System.out.println("You should put all BBDAManager meta info here...");
        Batch batch = new Batch();
        List<InputData> inputData = batch.parseInputData();
        batch.doCompute(inputData);
    }

}
