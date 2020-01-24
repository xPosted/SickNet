package com.jubaka.sicknet.protocol.tcp.model;

import com.jubaka.sicknet.protocol.tcp.model.payload.PayloadHolder;

import java.io.*;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class TcpSession {


    private PayloadHolder payloadHolder;
    private TcpSessionDescription description;
    private List<IpTcpEntity> packetQueue = new ArrayList<>();

    public TcpSession(PayloadHolder payloadHolder, TcpSessionDescription description) {
        this.payloadHolder = payloadHolder;
        this.description = description;
    }

    public void put(IpTcpEntity nextPacket, byte[] payload) {

        if (packetQueue.size() == 0) {
            addPacket(nextPacket, payload);
            return;
        }
        IpTcpEntity previousPacket = packetQueue.get(packetQueue.size() - 1);
        if ((previousPacket.getSrcPort() == nextPacket.getSrcPort()) && (previousPacket.getSrcAddr().equals(nextPacket.getSrcAddr()))) {
            if (previousPacket.getAck() == nextPacket.getAck()) {
                addPacket(nextPacket, payload);
                return;
            }
        }

        if ((previousPacket.getSrcPort() == nextPacket.getDstPort()) && (previousPacket.getSrcAddr().equals(nextPacket.getDstAddr()))) {
            if (previousPacket.getAck() == nextPacket.getSeq()) {
                addPacket(nextPacket, payload);
                return;
            }
        }
    }

    private void addPacket(IpTcpEntity nextPacket, byte[] payload) {
        packetQueue.add(nextPacket);
        if (payload != null) {
            payloadHolder.put(nextPacket,payload);
        }
    }


    public byte[] getRawData() {
        InputStream sourcePayloadStream = payloadHolder.getSourceDataStream();
        InputStream destinationPayloadStream = payloadHolder.getDestinationDataStream();
        ByteArrayOutputStream allDataStream = new ByteArrayOutputStream();
        byte[] buf = new byte[65535];
        try {
            packetQueue.forEach(ipTcpEntity -> {
                try {
                    if (ipTcpEntity.getSrcAddr().equals(description.srcAddr) && ipTcpEntity.getPayloadLen() >0) {
                        sourcePayloadStream.read(buf,0,ipTcpEntity.getPayloadLen());
                        allDataStream.write(buf,0,ipTcpEntity.getPayloadLen());
                    }
                    if (ipTcpEntity.getSrcAddr().equals(description.dstAddr) && ipTcpEntity.getPayloadLen() >0) {
                        destinationPayloadStream.read(buf,0,ipTcpEntity.getPayloadLen());
                        allDataStream.write(buf,0,ipTcpEntity.getPayloadLen());
                    }
                } catch (IOException io) {
                    throw new RuntimeException(io);
                }
            });
            allDataStream.flush();
            allDataStream.close();
            sourcePayloadStream.close();
            destinationPayloadStream.close();
        } catch (IOException io) {
            throw new RuntimeException(io);
        }
       return allDataStream.toByteArray();
    }

    public TcpSessionDescription getDescription() {
        return description;
    }
/*
    public byte[] getSessionDataByAddr(InetAddress addr) throws IOException  {
        ByteArrayInputStream payload = new ByteArrayInputStream(baos.toByteArray());
        ByteArrayOutputStream sourcePayload = new ByteArrayOutputStream();
        byte[] buf = new byte[65535];
        packetQueue.stream().forEach(ipTcpEntity -> {
                if (ipTcpEntity.getSrcAddr().equals(addr) && ipTcpEntity.getPayloadLen() > 0 ) {
                    payload.read(buf,0,ipTcpEntity.getPayloadLen());
                    sourcePayload.write(buf,0,ipTcpEntity.getPayloadLen());
                } else payload.skip(ipTcpEntity.getPayloadLen());
        });
        sourcePayload.close();
        return sourcePayload.toByteArray();
    }

    public byte[] getSessionDestinationData() {
        try {
            return getSessionDataByAddr(description.dstAddr);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public byte[] getSessionSourceData() {
        try {
            return getSessionDataByAddr(description.srcAddr);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    */

public byte[] getRawSourceData() {
    return payloadHolder.getRawSourceData();
}

public byte[] getRawDestinationData() {
    return payloadHolder.getRawDestinationData();
}

public byte[] getSessionDataByAddr(InetAddress address) {
    if (address.equals(description.srcAddr))
        return payloadHolder.getRawSourceData();
    if (address.equals(description.dstAddr))
        return payloadHolder.getRawDestinationData();
    return null;
}


    public void closeSession() {
        payloadHolder.onSessionClosed();
    }

}
