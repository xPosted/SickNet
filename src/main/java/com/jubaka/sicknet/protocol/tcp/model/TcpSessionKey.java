package com.jubaka.sicknet.protocol.tcp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.net.InetAddress;
import java.util.Objects;

public class TcpSessionKey {

    int srcPort;
    int dstPort;

    InetAddress srcAddr;
    InetAddress dstAddr;

    public TcpSessionKey(TcpSessionDescription sessionDescription) {
        srcPort = sessionDescription.srcPort;
        srcAddr = sessionDescription.srcAddr;
        dstPort = sessionDescription.dstPort;
        dstAddr = sessionDescription.dstAddr;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TcpSessionKey)) return false;
        TcpSessionKey that = (TcpSessionKey) o;
        return ((srcPort == that.srcPort || srcPort == that.dstPort) &&
                (dstPort == that.srcPort || dstPort == that.dstPort)) &&
                ((Objects.equals(srcAddr, that.srcAddr) || Objects.equals(srcAddr, that.dstAddr)) &&
                (Objects.equals(dstAddr, that.srcAddr) || Objects.equals(dstAddr, that.dstAddr)));
    }

    @Override
    public int hashCode() {
        return Objects.hash(srcPort+dstPort, srcAddr.hashCode()+dstAddr.hashCode());
    }
}
