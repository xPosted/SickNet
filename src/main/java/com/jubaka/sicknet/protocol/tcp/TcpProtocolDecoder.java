package com.jubaka.sicknet.protocol.tcp;

import com.jubaka.sicknet.protocol.ProtocolDecoder;
import com.jubaka.sicknet.protocol.tcp.model.*;
import com.jubaka.sicknet.protocol.tcp.model.payload.HolderFactory;
import lombok.extern.slf4j.Slf4j;
import org.pcap4j.core.PcapPacket;
import org.pcap4j.packet.IpPacket;
import org.pcap4j.packet.Packet;
import org.pcap4j.packet.TcpPacket;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class TcpProtocolDecoder implements ProtocolDecoder {

    public HolderFactory holderFactory;
    public final Set<IpTcpEntity> packetMap = new HashSet<>();
    public final Map<TcpSessionKey, TcpSession> openedSessionMap = new HashMap<>();
    public final Map<TcpSessionKey, TcpSession> closedSessionMap = new HashMap<>();


    public TcpProtocolDecoder(HolderFactory holderFactory) {
        this.holderFactory = holderFactory;
    }

    public void printStat() {
        log.info("====== TCP protocol decoder stat =======");
        log.info("Active sessions: "+openedSessionMap.size());
        log.info("Closed sessions: "+closedSessionMap.size());
        log.info("====== Active sessions desc");
        openedSessionMap.entrySet().stream().forEach(entry-> {
            TcpSessionKey key = entry.getKey();
            TcpSession session = entry.getValue();
            log.info(session.getDescription().getSrcAddr().toString() + " -> " + session.getDescription().getDstAddr() + ":"+session.getDescription().getDstPort()+" | "+session.getRawData().length);
        });

        log.info("====== Closed sessions desc");
        closedSessionMap.entrySet().stream().forEach(entry-> {
            TcpSessionKey key = entry.getKey();
            TcpSession session = entry.getValue();
            log.info(session.getDescription().getSrcAddr().toString() + " -> " + session.getDescription().getDstAddr() + ":"+session.getDescription().getDstPort()+" | "+session.getRawData().length);
         //   log.info( "SOURCE>> "+new String (session.getRawSourceData()));
          //  log.info( "DESTINATION>> "+new String (session.getRawDestinationData()));
            log.info(new String(session.getRawData()));
        });
    }

    @Override
    public void handlePacket(PcapPacket packet) {
        if ( !packet.contains(IpPacket.class) || !packet.contains(TcpPacket.class)) return;
        IpPacket ipPacket = packet.get(IpPacket.class);
        TcpPacket tcpPacket = packet.get(TcpPacket.class);
        Optional<Packet> payloadOptional = Optional.ofNullable(tcpPacket.getPayload());
        byte[] payload = payloadOptional.map(p -> p.getRawData()).orElse(new byte[0]);

        IpTcpEntity key = new IpTcpEntity(ipPacket,tcpPacket, packet.getTimestamp(), payload);
        packetMap.add(key);
        checkForNewSession(key);
        checkForExistedSession(key,payload);

    }

    public void checkForExistedSession(IpTcpEntity ipTcpEntity,byte[] payload) {
        if (ipTcpEntity.isSYN()) return;
        TcpSessionDescription sessionDesc = TcpSessionDescription.builder()
                .srcPort(ipTcpEntity.getSrcPort())
                .dstPort(ipTcpEntity.getDstPort())
                .srcAddr(ipTcpEntity.getSrcAddr())
                .dstAddr(ipTcpEntity.getDstAddr())
                .build();
        TcpSessionKey sessionKey = new TcpSessionKey(sessionDesc);
        if (openedSessionMap.containsKey(sessionKey)) {
            openedSessionMap.get(sessionKey).put(ipTcpEntity,payload);
            if (ipTcpEntity.isFIN()) {
                TcpSession session = openedSessionMap.get(sessionKey);
                session.closeSession();
                closedSessionMap.put(sessionKey,session);
                openedSessionMap.remove(sessionKey);
            }
        }
    }


    private void checkForNewSession(IpTcpEntity ipTcpEntity) {

        if (ipTcpEntity.isSYN() && ipTcpEntity.isACK0()) {
            IpTcpEntity previousSyn = IpTcpEntity.builder()
                    .ACK0(false)
                    .SYN(true)
                    .dstPort(ipTcpEntity.getSrcPort())
                    .srcPort(ipTcpEntity.getDstPort())
                    .dstAddr(ipTcpEntity.getSrcAddr())
                    .srcAddr(ipTcpEntity.getDstAddr())
                    .seq(ipTcpEntity.getAck()-1)
                    .timestamp(ipTcpEntity.getTimestamp())
                    .build();
            if (packetMap.contains(previousSyn)) {
                TcpSessionDescription sessionDescription =TcpSessionDescription.builder()
                        .srcAddr(previousSyn.getSrcAddr())
                        .dstAddr(previousSyn.getDstAddr())
                        .srcPort(previousSyn.getSrcPort())
                        .dstPort(previousSyn.getDstPort())
                        .timestamp(previousSyn.getTimestamp()).build();
                TcpSession newTcpSession = new TcpSession(holderFactory.getHolder(sessionDescription), sessionDescription);

                openedSessionMap.put(new TcpSessionKey(sessionDescription),newTcpSession);
                packetMap.remove(previousSyn);
                packetMap.remove(ipTcpEntity);
            }
        }
    }

    public List<TcpSession> find(TcpSessionSearchPredicate predicate) {
       return openedSessionMap.values().stream().filter(predicate).collect(Collectors.toList());
    }

}
