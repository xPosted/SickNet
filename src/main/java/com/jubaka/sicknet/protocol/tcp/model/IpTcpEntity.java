package com.jubaka.sicknet.protocol.tcp.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.pcap4j.packet.IpPacket;
import org.pcap4j.packet.TcpPacket;

import java.net.InetAddress;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.TemporalAccessor;
import java.util.Objects;

@Builder
@Getter
@AllArgsConstructor
public class IpTcpEntity {
    private InetAddress srcAddr;
    private InetAddress dstAddr;
    private int srcPort;
    private int dstPort;
    private int seq;
    private int ack;

    private boolean SYN;
    private boolean ACK0;
    private boolean URG;
    private boolean PSH;
    private boolean FIN;
    private boolean RST;

    private LocalDateTime timestamp;

    private int payloadLen;


    public IpTcpEntity() {

    }

    public IpTcpEntity(IpPacket ipPacket, TcpPacket tcpPacket, Instant timestamp, byte[] payload) {
        this.srcAddr = ipPacket.getHeader().getSrcAddr();
        this.dstAddr = ipPacket.getHeader().getDstAddr();
        this.seq = tcpPacket.getHeader().getSequenceNumber();
        this.ack = tcpPacket.getHeader().getAcknowledgmentNumber();
        this.srcPort = tcpPacket.getHeader().getSrcPort().valueAsInt();
        this.dstPort = tcpPacket.getHeader().getDstPort().valueAsInt();
        this.SYN = tcpPacket.getHeader().getSyn();
        this.ACK0 = tcpPacket.getHeader().getAck();
        this.FIN = tcpPacket.getHeader().getFin();
        this.URG = tcpPacket.getHeader().getUrg();
        this.PSH = tcpPacket.getHeader().getPsh();
        this.RST = tcpPacket.getHeader().getRst();
        this.timestamp = LocalDateTime.ofInstant(timestamp, Clock.systemDefaultZone().getZone());
        this.payloadLen = payload.length;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IpTcpEntity)) return false;
        IpTcpEntity that = (IpTcpEntity) o;
        return getSrcPort() == that.getSrcPort() &&
                getDstPort() == that.getDstPort() &&
                getSeq() == that.getSeq() &&
                getAck() == that.getAck() &&
                isSYN() == that.isSYN() &&
                isACK0() == that.isACK0() &&
                isURG() == that.isURG() &&
                isPSH() == that.isPSH() &&
                isFIN() == that.isFIN() &&
                isRST() == that.isRST() &&
                Objects.equals(getSrcAddr(), that.getSrcAddr()) &&
                Objects.equals(getDstAddr(), that.getDstAddr());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSrcAddr(), getDstAddr(), getSrcPort(), getDstPort(), getSeq(), getAck(), isSYN(), isACK0(), isURG(), isPSH(), isFIN(), isRST());
    }
}
