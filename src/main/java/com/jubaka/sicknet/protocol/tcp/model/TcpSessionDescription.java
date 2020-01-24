package com.jubaka.sicknet.protocol.tcp.model;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.net.InetAddress;
import java.time.LocalDateTime;

@Builder
@Getter
@EqualsAndHashCode
public class TcpSessionDescription {

     int srcPort;
     int dstPort;

     InetAddress srcAddr;
     InetAddress dstAddr;
     LocalDateTime timestamp;

}
