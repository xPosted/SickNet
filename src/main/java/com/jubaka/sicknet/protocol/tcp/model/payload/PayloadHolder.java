package com.jubaka.sicknet.protocol.tcp.model.payload;

import com.jubaka.sicknet.protocol.tcp.model.IpTcpEntity;

import java.io.InputStream;

public interface PayloadHolder {

    public void put(IpTcpEntity ipTcpEntity, byte[] payload);
    public void onSessionClosed();
    public byte[] getRawSourceData();
    public byte[] getRawDestinationData();

    public InputStream getSourceDataStream();
    public InputStream getDestinationDataStream();
}
