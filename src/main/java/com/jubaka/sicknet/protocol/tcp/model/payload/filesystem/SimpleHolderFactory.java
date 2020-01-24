package com.jubaka.sicknet.protocol.tcp.model.payload.filesystem;

import com.jubaka.sicknet.protocol.tcp.model.TcpSessionDescription;
import com.jubaka.sicknet.protocol.tcp.model.payload.HolderFactory;
import com.jubaka.sicknet.protocol.tcp.model.payload.PayloadHolder;
import com.jubaka.sicknet.protocol.tcp.model.payload.filesystem.SimplePayloadHolder;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class SimpleHolderFactory implements HolderFactory {

    private String basePath;

    public PayloadHolder getHolder(TcpSessionDescription description) {
        return new SimplePayloadHolder(basePath,description);
    }

}
