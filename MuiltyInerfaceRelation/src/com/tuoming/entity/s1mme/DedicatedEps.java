package com.tuoming.entity.s1mme;

import com.tuoming.common.TypeChange;
import com.tuoming.entity.s1mme.method.MmeMapCommon;
import com.tuoming.tools.CommonUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashSet;
import java.util.Set;

public class DedicatedEps extends MmeCommon {

    public String enodeb_ip = "";         //eNodeB信令面IP
    public String enodeb_port = "";       //eNodeB端口
    public String mme_ip = "";            //MME IP地址
    public String mme_port = "";          //MME端口
    public String mme_ue_s1ap_id = "";    //MME UE S1AP ID
    public String enb_ue_s1ap_id = "";    //ENB UE S1AP ID
    public String mme_groupid = "";       //MME GroupID
    public String mme_code = "";          //MME Code
    public String m_tmsi = "";            //M-TMSI
    public String tac = "";               //TAC
    public String eci = "";               //ECI
    public String ebi = "";               //激活EBI
    public String pti = "";               //激活PTI
    public String tfa = "";               //Traffic flow aggregate
    public String lebi = "";              //Linked EPS bearer identity
    public String eps_qos = "";           //EPS QoS
    public String qos_negt = "";          //协商QoS
    public String ap_ambr = "";           //APN AMBR
    public String erab_num = "";          //ERAB数量
    public String erabid_req = "";        //请求消息的E-RAB ID
    public String rabqos_req = "";        //请求RAB中的QOS
    public String sgw_gtp_ip = "";        //SGW用户面IP
    public String sgw_gtp_teid = "";      //SGW用户面TEID
    public String erabid_rsp = "";        //响应消息的E-RAB ID
    public String rab_fillist = "";       //建立失败RAB的列表
    public String enb_gtp_ip = "";        //eNodeB用户面IP
    public String enb_gtp_teid = "";      //eNodeB用户面TEID
    public String req_time = "";          //请求消息请求时间
    public String rsp_time = "";          //响应消息时间
    public String deactive_cause = "";    //拒绝原因
    public String context_rel_time = "";  //UE context release request时间
    public String req_cause = "";         //请求原因
    public String contxt_rl_time = "";    //UE context release command时间
    public String release_cause = "";     //UE context release释放原因
    public String comp_time = "";         //UE context releasecomplete时间
    public String res_time = "";          //结束消息类型
    public String msg_id = "";            //结束消息
    public String cause_code = "";        //消息的结果码


    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private static Set<Integer> set = new HashSet<>();

    static {
        set.add(40);
    }

    @Override
    public String getKey() {
        return mme_ue_s1ap_id;
    }

    @Override
    public void decode(String[] str) {
        super.decode(str);
        this.enodeb_ip = str[DedicatedEpsIndex.enodeb_ip];
        this.enodeb_port = str[DedicatedEpsIndex.enodeb_port];
        this.mme_ip = str[DedicatedEpsIndex.mme_ip];
        this.mme_port = str[DedicatedEpsIndex.mme_port];
        this.mme_ue_s1ap_id = str[DedicatedEpsIndex.mme_ue_s1ap_id];
        this.enb_ue_s1ap_id = str[CommonUtils.convertIndex(str, AttachIndex.enb_ue_s1ap_id)];
        this.mme_groupid = str[DedicatedEpsIndex.mme_groupid];
        this.mme_code = str[DedicatedEpsIndex.mme_code];
        this.m_tmsi = str[DedicatedEpsIndex.m_tmsi];
        this.tac = str[DedicatedEpsIndex.tac];
        this.eci = str[DedicatedEpsIndex.eci];
        Integer ebi_index = CommonUtils.numIndex(str, DedicatedEpsIndex.ebi);
        if (ebi_index != null) {
            this.ebi = (str[DedicatedEpsIndex.erab_num] != null && CommonUtils.strToInteger(str[DedicatedEpsIndex.erab_num]) > 0) ? str[ebi_index] : "";
        }
        String ptinum = str[CommonUtils.convertIndex(str, DedicatedEpsIndex.pti)];
        this.pti = TypeChange.strToHex(ptinum);

        this.tfa = "";
        this.lebi = "";
        String epsnum = str[CommonUtils.convertIndex(str, DedicatedEpsIndex.eps_qos)];
        this.eps_qos = TypeChange.strToHex(epsnum);
        String qosn = str[CommonUtils.convertIndex(str, DedicatedEpsIndex.qos_negt)];
        this.qos_negt = TypeChange.strToHex(qosn);
        String ambr = str[CommonUtils.convertIndex(str, DedicatedEpsIndex.ap_ambr)];
        this.ap_ambr = TypeChange.strToHex(ambr);

        this.erab_num = str[DedicatedEpsIndex.erab_num];
        this.req_time = str[DedicatedEpsIndex.req_time];
        this.rsp_time = str[DedicatedEpsIndex.rsp_time];
        String deactive = (str[DedicatedEpsIndex.msg_id] != null && "1".equals(str[DedicatedEpsIndex.msg_id])) ? str[DedicatedEpsIndex.deactive_cause] : "";
        this.deactive_cause = TypeChange.strToOx7f(deactive);

        this.res_time = "0";
        if ("0".equals(str[DedicatedEpsIndex.msg_id])) {
            this.msg_id = "198";
        } else if ("1".equals(str[DedicatedEpsIndex.msg_id])) {
            this.msg_id = "199";
        } else if ("255".equals(str[DedicatedEpsIndex.msg_id])) {
            this.msg_id = "197";
        }
        String causecode = str[DedicatedEpsIndex.cause_code];
        this.cause_code = TypeChange.strToOx7f(causecode);


    }


    //40
    private void createType40(String[] str) {
        Integer erabid_req_index = CommonUtils.numIndex(str, DedicatedEpsIndex.erabid_req);
        if (erabid_req_index != null) {
            this.erabid_req = str[erabid_req_index];
        }
        String rabqos = str[CommonUtils.convertIndex(str, DedicatedEpsIndex.rabqos_req)];
        this.rabqos_req = TypeChange.strToHex(rabqos);

        this.sgw_gtp_ip = str[CommonUtils.convertIndex(str, DedicatedEpsIndex.sgw_gtp_ip)];
        this.sgw_gtp_teid = str[CommonUtils.convertIndex(str, DedicatedEpsIndex.sgw_gtp_teid)];
        Integer erabid_rsp_index = CommonUtils.numIndex(str, DedicatedEpsIndex.erabid_rsp);
        if (erabid_req_index != null) {
            this.erabid_rsp = str[erabid_rsp_index];
        }
        String rabfill = str[CommonUtils.convertIndex(str, DedicatedEpsIndex.rab_fillist)];
        this.rab_fillist = TypeChange.strToHex(rabfill);
        this.enb_gtp_ip = str[CommonUtils.convertIndex(str, DedicatedEpsIndex.enb_gtp_ip)];
        Integer enb_gtp_teid_index = CommonUtils.numIndex(str, DedicatedEpsIndex.enb_gtp_teid);
        if (enb_gtp_teid_index != null) {
            this.enb_gtp_teid = str[enb_gtp_teid_index];
        }

    }

    //20
    private void createType20(String[] str) {
        this.context_rel_time = str[CommonUtils.convertIndex(str, DedicatedEpsIndex.context_rel_time)];
        String reqcause = str[CommonUtils.convertIndex(str, DedicatedEpsIndex.req_cause)];
        this.req_cause = TypeChange.strToOx7f(reqcause);
        this.contxt_rl_time = str[DedicatedEpsIndex.contxt_rl_time];
        String releasecause = str[DedicatedEpsIndex.release_cause];
        this.release_cause = TypeChange.strToOx7f(releasecause);
        this.comp_time = str[DedicatedEpsIndex.comp_time];

    }

    @Override
    public boolean getMiddleProcedure(int produceType) {
        if (produceType == 40) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Integer getEndProcedure() {
        return null;
    }

    @Override
    public Integer relation(MmeMapCommon mmeMapCommon, int type, String[] arr) {
        long startTime = mmeMapCommon.getStartTime();
        long endTime = mmeMapCommon.getEndTime();
        Long start;
        Long end;
        try {
            start = sdf.parse(arr[MmeIndex.startTime].substring(0, 23)).getTime();
            end = sdf.parse(arr[MmeIndex.endTime].substring(0, 23)).getTime();
        } catch (ParseException e) {
            return 3;
        }
        if (getMiddleProcedure(type)) {
            if (start >= startTime && endTime >= end) {
                switch (type) {
                    case 40:
                        createType40(arr);
                        break;
                }
            }
            return 1;
        } else if (type == getEndProcedure()) {
            createType20(arr);
            return 2;
        } else {
            return 0;
        }
    }


    @Override
    public String toString() {
        String common = super.toString();
        StringBuilder sb = new StringBuilder();
        sb.append(enodeb_ip).append("|").append(enodeb_port).append("|").append(mme_ip).append("|").append(mme_port).append("|").append(mme_ue_s1ap_id).append("|").append(enb_ue_s1ap_id).append("|")
                .append(mme_groupid).append("|").append(mme_code).append("|").append(m_tmsi).append("|").append(tac).append("|").append(eci).append("|").append(ebi).append("|")
                .append(pti).append("|").append(tfa).append("|").append(lebi).append("|").append(eps_qos).append("|").append(qos_negt).append("|").append(ap_ambr).append("|")
                .append(erab_num).append("|").append(erabid_req).append("|").append(rabqos_req).append("|").append(sgw_gtp_ip).append("|").append(sgw_gtp_teid).append("|")
                .append(erabid_rsp).append("|").append(rab_fillist).append("|").append(enb_gtp_ip).append("|").append(enb_gtp_teid).append("|").append(req_time).append("|")
                .append(rsp_time).append("|").append(deactive_cause).append("|").append(context_rel_time).append("|").append(req_cause).append("|").append(contxt_rl_time).append("|")
                .append(release_cause).append("|").append(comp_time).append("|").append(res_time).append("|").append(msg_id).append("|").append(cause_code);
        return common + sb.toString();
    }
}
