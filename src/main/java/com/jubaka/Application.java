package com.jubaka;

import com.jubaka.sicknet.packet.handler.SimplePacketHandler;
import com.jubaka.sicknet.protocol.tcp.TcpProtocolDecoder;
import com.jubaka.sicknet.protocol.tcp.model.payload.filesystem.SimpleHolderFactory;
import org.pcap4j.core.PcapHandle;
import org.pcap4j.core.Pcaps;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Application {

    ExecutorService singleThreadPool = Executors.newSingleThreadExecutor();
    SimplePacketHandler packetHandler = new SimplePacketHandler();

    public static void main(String[] args) throws Exception {
        Application app = new Application();
        app.testCapture();
    }

    public void testCapture() throws Exception {
        TcpProtocolDecoder tcpDecoder =
                new TcpProtocolDecoder(SimpleHolderFactory.builder().basePath("/Users/ozhupanov/sick_home").build());
        PcapHandle handle = Pcaps.openOffline("/Users/ozhupanov/Downloads/http_with_jpegs.cap");

        packetHandler.addDecoder(tcpDecoder);
        handle.loop(-1,packetHandler, singleThreadPool);
        singleThreadPool.shutdown();
        singleThreadPool.awaitTermination(1, TimeUnit.HOURS);
        tcpDecoder.printStat();
    }




}
