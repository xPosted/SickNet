package com.jubaka.sicknet.protocol.tcp.model.payload.inmemory;

import com.jubaka.sicknet.protocol.tcp.model.IpTcpEntity;
import com.jubaka.sicknet.protocol.tcp.model.payload.PayloadHolder;

import java.io.InputStream;

public class InMemoryPayloadHolder implements PayloadHolder {

    @Override
    public void put(IpTcpEntity ipTcpEntity, byte[] payload) {

    }

    @Override
    public void onSessionClosed() {

    }

    @Override
    public byte[] getRawSourceData() {
        return new byte[0];
    }

    @Override
    public byte[] getRawDestinationData() {
        return new byte[0];
    }

    @Override
    public InputStream getSourceDataStream() {
        return null;
    }

    @Override
    public InputStream getDestinationDataStream() {
        return null;
    }
}
