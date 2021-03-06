package com.tuoming.common;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class FileDealUntil {
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
    private static SimpleDateFormat sdf4 = new SimpleDateFormat("yyyy-MM-dd");


    private static SimpleDateFormat sdf2 = new SimpleDateFormat("yyyyMMddHHmm00");
    private static SimpleDateFormat sdf3 = new SimpleDateFormat("yyyyMMddHHmmss");

    //判断文件是否存在
    public static boolean fileExist(String path) {
        File file = new File(path);
        return file.exists();
    }

    //删除文件
    public static boolean deleteFile(String path) {
        File file = new File(path);
        return file.delete();
    }

    public static void pathCheck(String file) {
        File f = new File(file);
        if (!f.exists()) {
            f.mkdir();
        }
    }


    //移动文件
    public static boolean moveFile(String sorFile, String disFile) {
        File file = new File(sorFile);
        File file2 = new File(disFile);
        if (file2.exists()) {
            file2.delete();
        }
        return file.renameTo(file2);
    }

    //扫描文件
    public static String[] scanFile(String path) {
        File file = new File(path);
        return file.list();
    }

    public static boolean renameFile(String path, String desPath) {
        File file = new File(path);
        File desFile = new File(desPath);
        return file.renameTo(desFile);
    }

    public static List<String> fileListAndSort(String path) {
//        String[] arr={"s1ap_20190620104256_2.csv","s1ap_20190620104257_2.csv","s1ap_20190620104255_2.csv"};
        String[] arr = scanFile(path);
        List<String> result = new ArrayList<>();
        if (arr.length > 0) {
            List<ClassName> list = new ArrayList<>();
            for (String file : arr) {
                String[] split = file.split("\\.");
                String[] spl = split[0].split("_");
                if ("csv".equals(split[1])) {
                    Long fs = null;
                    try {
                        fs = TypeChange.strToLong(spl[1]);
                    } catch (Exception e) {
                        fs = null;
                    }
                    if (fs != null) {
                        list.add(new ClassName(fs, file));
                    }
                }
            }
            Collections.sort(list);
            for (ClassName i : list) {
                result.add(i.name);
            }
        }
        return result;
    }

    //210000_1_LTE-S6a_20190610135320_00.csv   timeIndex=3,时间字符串的索引值（以_分隔后）
    public static List<String> fileListAndSort(String path, int fileNameTimeIndex) {
        String[] arr = scanFile(path);
        List<String> result = new ArrayList<>();
        if (arr.length > 0) {
            List<ClassName> list = new ArrayList<>();
            for (String file : arr) {
                if (file.endsWith("csv")) {
                    String[] split = file.split("_");
                    Long fs = null;
                    try {
                        fs = TypeChange.strToLong(split[fileNameTimeIndex]);
                    } catch (Exception e) {
                        fs = null;
                    }
                    if (fs != null) {
                        list.add(new ClassName(fs, file));
                    }
                }
            }
            Collections.sort(list);
            for (ClassName i : list) {
                result.add(i.name);
            }
        }
        return result;
    }

    static class ClassName implements Comparable {
        Long time;
        String name;

        ClassName(Long time, String name) {
            this.time = time;
            this.name = name;
        }

        @Override
        public int compareTo(Object o) {
            if (time - ((ClassName) o).time >= 0) {
                return 1;
            } else {
                return -1;
            }
        }
    }


    //时间转化，时间字符串，时间周期
    public static String timeTranform(String timeString, int cycleTime) {
        Date parse = null;
        Date date = new Date();
        try {
            if (cycleTime > 0 && cycleTime <= 60) {
                String min = timeString.substring(14, 16);
                parse = sdf.parse(timeString);
                Long str = parse.getTime() + Integer.parseInt(min) / cycleTime * cycleTime * 60 * 1000;
                date.setTime(str);
                return sdf2.format(date);
            } else if (cycleTime > 60 && cycleTime <= 1440) {
                String hour = timeString.substring(11, 13);
                parse = sdf4.parse(timeString);
                Long str = parse.getTime() + Integer.parseInt(hour) / (cycleTime / 60) * (cycleTime / 60) * 60 * 60 * 1000;
                date.setTime(str);
                return sdf2.format(date);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    //时间戳转化成分钟数
    public static Long longTimeToMin(String time) {
        Date parse = null;
        try {
            parse = sdf3.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parse.getTime() / (1000 * 60);
    }

}
