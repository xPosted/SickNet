package com.jubaka.sicknet.protocol.tcp.model.payload;

import com.jubaka.sicknet.protocol.tcp.model.TcpSessionDescription;

public interface HolderFactory {
    public PayloadHolder getHolder(TcpSessionDescription description);
}
