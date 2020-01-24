package com.jubaka.sicknet.protocol.tcp.model;

import lombok.Builder;

import java.net.InetAddress;
import java.util.function.Predicate;

@Builder
public class TcpSessionSearchPredicate implements Predicate<TcpSession> {


    private InetAddress srcAddr;
    private InetAddress dstAddr;
    private int srcPort = -1;
    private int dstPort = -1;

    private long payloadMin;
    private long payloadMax;

    @Override
    public boolean test(TcpSession tcpSession) {
        if (srcAddr != null && !tcpSession.getDescription().srcAddr.equals(srcAddr))
            return false;
        if (dstAddr != null && !tcpSession.getDescription().dstAddr.equals(dstAddr))
            return false;
        if (srcPort != -1 && tcpSession.getDescription().srcPort != srcPort )
            return false;
        if (dstPort != -1 && tcpSession.getDescription().dstPort != dstPort )
            return false;
        // TODO: payload comparasion

        return true;
    }
}
