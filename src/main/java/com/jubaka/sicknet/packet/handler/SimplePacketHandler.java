package com.jubaka.sicknet.packet.handler;

import com.jubaka.sicknet.protocol.ProtocolDecoder;
import org.pcap4j.core.PacketListener;
import org.pcap4j.core.PcapPacket;

import java.util.HashSet;
import java.util.Set;

public class SimplePacketHandler implements PacketListener {

    private Set<ProtocolDecoder> decodersSet = new HashSet<>();

    public void addDecoder(ProtocolDecoder decoder) {
        decodersSet.add(decoder);
    }

    public boolean containDecoder(ProtocolDecoder decoder) {
        return decodersSet.contains(decoder);
    }

    public void removeDecoder(ProtocolDecoder decoder) {
        decodersSet.remove(decoder);
    }

    public void gotPacket(PcapPacket pcapPacket) {

        decodersSet.forEach(decoder -> decoder.handlePacket(pcapPacket.getBuilder().build()));

    }
}