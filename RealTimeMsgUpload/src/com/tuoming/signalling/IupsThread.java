package com.tuoming.signalling;

import com.tuoming.common.FileDealUntil;
import com.tuoming.readfile.SigReadFile;
import com.tuoming.signalling.iups.IupsInfo;
import com.tuoming.utils.UDMsgSendCount;
import org.apache.log4j.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.Queue;

public class IupsThread implements Runnable {
    private Logger logger = Logger.getLogger(IupsThread.class);
    private String path;
    private Queue<byte[]> realMsgList;
    private UDMsgSendCount count;


    public IupsThread(String path, Queue<byte[]> realMsgList, UDMsgSendCount count) {
        this.path = path;
        this.realMsgList = realMsgList;
        this.count = count;
    }

    @Override
    public void run() {
        Thread.currentThread().setName("IuPS文件处理线程");
        logger.info(Thread.currentThread().getName()+"启动!");
        String[] split = path.split("\\|");
        String inPath = split[0];
        String outPath = split[1];
        String rau = split[2];
        String reloc = split[3];
        while (true) {
            //F:\iups\mme|F:\iups\mme|PSRAU|PSReloc
            String[] pathArr = FileDealUntil.scanFile(inPath);
//            System.out.println("文件列表扫描" + Arrays.toString(pathArr));
            for (String path : pathArr) {
                logger.info("["+path+"]文件开始处理!");
                if (path.startsWith(rau)) {
                    SigReadFile sigReadFile = new SigReadFile();
                    sigReadFile.read(inPath + "/" + path);
                    List<String> list = sigReadFile.getFileBuffer();
                    for (String str : list) {
                        byte[] bytes = IupsInfo.iupsRauTobytes(str);
                        if (bytes != null) {
                            realMsgList.add(bytes);
                            count.iupsRauSum.getAndIncrement();
                        }
                    }

                } else if (path.startsWith(reloc)) {
                    SigReadFile sigReadFile = new SigReadFile();
                    sigReadFile.read(inPath + "/" + path);
                    List<String> list = sigReadFile.getFileBuffer();
                    for (String str : list) {
                        byte[] bytes = IupsInfo.iupsReLocationTobytes(str);
                        if (bytes != null) {
                            realMsgList.add(bytes);
                            count.iupsLocationSum.getAndIncrement();
                        }
                    }
                }
                logger.info("["+path+"]文件处理完成!准备转移目录……");
                FileDealUntil.moveFile(inPath + "/" + path, outPath + "/" + path);
                logger.info("["+path+"]文件转移完成!");
            }

            try {
                Thread.sleep(500L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }

    }
}
