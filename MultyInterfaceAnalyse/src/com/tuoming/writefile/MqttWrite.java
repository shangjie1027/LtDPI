package com.tuoming.writefile;

public class MqttWrite extends Write {

    public MqttWrite(String time) {
        super(time);
    }

    // ZJ_HZ_MOBILE_CONS_Huawei_CXDR_RNC001_1440_20150818000000_Mqtt-00083_1_2.tar.gz
    public String getFile(int cycleTime, String networkElement) {
        String result = "ZJ_HZ_MOBILE_CONS_Huawei_CXDR_" + networkElement + "_" + cycleTime + "_" + time + "_" + "Mqtt" + "-" + getFileSign() + "_0_2.txt";
        tmpName = result + ".tmp";
        finlName = result;
        return tmpName;
    }

}
