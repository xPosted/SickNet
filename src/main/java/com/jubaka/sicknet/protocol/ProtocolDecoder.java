package com.jubaka.sicknet.protocol;

import org.pcap4j.core.PcapPacket;

public interface ProtocolDecoder {
    public void handlePacket(PcapPacket pcapPacket);
}
