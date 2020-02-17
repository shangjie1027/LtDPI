package com.tuoming.signalling.mme;

import com.tuoming.common.TypeChange;
import com.tuoming.signalling.SignallingCommon;
import com.tuoming.utils.CommonUtils;
import com.tuoming.utils.MD5Util;
import com.tuoming.utils.ParseEvery;
import com.tuoming.utils.TBCDUtil;

public class MmeAttachDetachInfo extends SignallingCommon {

    //3字节
    protected final String version = "412";
    //8字节,毫秒数，即将标准unix时间转成毫秒，网络字节序
    protected Long time;
    //16/20字节,TBCD编码
    protected String msisdn;
    //16/20字节,TBCD编码
    protected String imei;
    //16/20字节,TBCD编码
    protected String imsi;
    //1字节,承载网络：1=3G, 2=2G,6=4G,9=5G
    protected Integer rat;
    //2字节,2/3G对应lac，4G对应Tac，网络字节序
    protected Integer lacTac;
    //4字节,2/3G对应ci/sac，4G对应eci，网络字节序
    protected Long ciEci;
    //1个字节 0：开机 1：关机
    protected Integer serviceType;
    //3个字节
    protected final String provinceId = "130";
    //1字节，默认全F
    protected Integer uenc;
    //gNBIP 4字节,16进制IP地址
    protected String gnbIp;

    @Override
    public boolean init(String[] str) {
        return false;
    }

    public boolean init(String[] split, Integer produceType) {
        try {
            Integer n = TypeChange.strToInteger(split[MmeIndex.mmeBearerN]);
            Integer keyword = TypeChange.strToInteger(split[MmeIndex.keyword]);
            if (produceType == 1) {
                serviceType = 0;
            } else if (produceType == 6 && keyword != null && "1".equals(TypeChange.add0(Integer.toBinaryString(keyword)).indexOf(3))) {
                serviceType = 1;
            } else {
                return false;
            }
            time = TypeChange.timeChange(split[MmeIndex.getMmeIndex(MmeAttachDetachIndex.time, n)]);
            msisdn = split[MmeIndex.getMmeIndex(MmeAttachDetachIndex.msisdn, n)];
            if (msisdn.length() >= 3) {
                msisdn = msisdn.startsWith("86") ? msisdn.substring(2) : msisdn;
            } else {
                msisdn = "";
            }
            imei = split[MmeIndex.getMmeIndex(MmeAttachDetachIndex.imei, n)];
            imsi = split[MmeIndex.getMmeIndex(MmeAttachDetachIndex.imsi, n)];
            rat = TypeChange.strToInteger(split[MmeIndex.getMmeIndex(MmeAttachDetachIndex.rat, n)]);
            if (!(rat == 1 || rat == 2 || rat == 6)) {
                rat = null;
            }
            lacTac = TypeChange.strToInteger(split[MmeIndex.getMmeIndex(MmeAttachDetachIndex.lacTac, n)]);
            ciEci = TypeChange.strToLong(split[MmeIndex.getMmeIndex(MmeAttachDetachIndex.ciEci, n)]);
            uenc = TypeChange.strToInteger(split[MmeIndex.getMmeIndex(MmeAttachDetachIndex.uenc, n)]);
            gnbIp = split[MmeIndex.getMmeIndex(MmeAttachDetachIndex.gnbIp, n)];
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public byte[] toBytes() {
        byte[] bytes = new byte[87];
        byte[] bytes1 = version.getBytes();
        int index = 0;
        for (int i = 0; i < 3; i++) {
            bytes[index + i] = bytes1[i];
        }
        index += 3;

        if (time == null) {
            for (int i = 0; i < 8; i++) {
                bytes[index + i] = -1;
            }
            index += 8;
        } else {
            byte[] bytes2 = ParseEvery.longToBytes2(time);
            for (int i = 0; i < 8; i++) {
                bytes[index + i] = bytes2[i];
            }
            index += 8;
        }

        if (msisdn.length() >= 8) {
            String msisdnPre = msisdn.substring(0, 7);
            byte[] bytes3 = TBCDUtil.parseTBCD(msisdnPre);
            for (int i = 0; i < 4; i++) {
                bytes[index + i] = bytes3[i];
            }
            index += 4;
            String msisdnTail = msisdn.substring(7);
            byte[] md5_msisdnByte = MD5Util.getMD5_Byte(msisdnTail);
            for (int i = 0; i < 16; i++) {
                bytes[i + index] = md5_msisdnByte[i];
            }
            index += 16;
        } else {
            for (int i = 0; i < 20; i++) {
                bytes[i + index] = -1;
            }
            index += 20;
        }

        if (imei.length() >= 9) {
            String imeiPre = imei.substring(0, 8);
            byte[] bytes4 = TBCDUtil.parseTBCD(imeiPre);
            for (int i = 0; i < 4; i++) {
                bytes[i + index] = bytes4[i];
            }
            index += 4;
            String imeiTail = imei.substring(8);
            byte[] md5_ImeiByte = MD5Util.getMD5_Byte(imeiTail);
            for (int i = 0; i < 16; i++) {
                bytes[i + index] = md5_ImeiByte[i];
            }
            index += 16;
        } else {
            for (int i = 0; i < 20; i++) {
                bytes[i + index] = -1;
            }
            index += 20;
        }

        if (imei.length() >= 9) {
            String imsiPre = imsi.substring(0, 8);
            byte[] bytes5 = TBCDUtil.parseTBCD(imsiPre);
            for (int i = 0; i < 4; i++) {
                bytes[i + index] = bytes5[i];
            }
            index += 4;
            String imsiTail = imsi.substring(8);
            byte[] md5_imsiByte = MD5Util.getMD5_Byte(imsiTail);
            for (int i = 0; i < 16; i++) {
                bytes[i + index] = md5_imsiByte[i];
            }
            index += 16;
        } else {
            for (int i = 0; i < 20; i++) {
                bytes[i + index] = -1;
            }
            index += 20;
        }

        bytes[index++] = ParseEvery.intToByte(rat);

        if (lacTac == null) {
            bytes[index++] = -1;
            bytes[index++] = -1;
        } else {
            byte[] bytes6 = ParseEvery.shortToByteArr(lacTac.shortValue());
            bytes[index++] = bytes6[0];
            bytes[index++] = bytes6[1];
        }

        if (ciEci == null) {
            for (int i = 0; i < 4; i++) {
                bytes[i + index] = -1;
            }
            index += 4;
        } else {
            byte[] bytes7 = ParseEvery.intToByte4Array(ciEci.intValue());
            for (int i = 0; i < 4; i++) {
                bytes[index + i] = bytes7[i];
            }
            index += 4;
        }

        bytes[index++] = ParseEvery.intToByte(serviceType);

        byte[] bytes11 = provinceId.getBytes();
        for (int i = 0; i < 3; i++) {
            bytes[index + i] = bytes11[i];
        }
        index += 3;
        if (uenc == null) {
            bytes[index++] = -1;
        } else {
            bytes[index++] = ParseEvery.intToByte(uenc);
        }


        String[] spliteIp = gnbIp.split("\\.");
        if (spliteIp.length == 4) {
            for (int i = 0; i < 4; i++) {
                bytes[i + index] = ParseEvery.intToByte(CommonUtils.strToInteger(spliteIp[i]));
            }
            index += 4;
        } else {
            for (int i = 0; i < 4; i++) {
                bytes[i + index] = -1;
            }
            index += 4;
        }
        return bytes;
    }

    @Override
    public String toString() {
        return "version:" + version + " " +
                "time:" + time + " " +
                "msisdn:" + msisdn + " " +
                "imei:" + imei + " " +
                "imsi:" + imsi + " " +
                "rat:" + rat + " " +
                "lacTac:" + lacTac + " " +
                "ciEci:" + ciEci + " " +
                "serviceType:" + serviceType + " " +
                "provinceId:" + provinceId + " " +
                "uenc:" + uenc + " " +
                "gnbIp:" + gnbIp + " ";
    }
}
