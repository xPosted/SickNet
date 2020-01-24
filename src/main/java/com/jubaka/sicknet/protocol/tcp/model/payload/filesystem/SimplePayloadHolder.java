package com.jubaka.sicknet.protocol.tcp.model.payload.filesystem;

import com.jubaka.sicknet.protocol.tcp.model.IpTcpEntity;
import com.jubaka.sicknet.protocol.tcp.model.TcpSessionDescription;
import com.jubaka.sicknet.protocol.tcp.model.payload.PayloadHolder;

import java.io.*;
import java.nio.file.*;
import java.time.format.DateTimeFormatter;

public class SimplePayloadHolder implements PayloadHolder {

    private TcpSessionDescription description;
    private File sourcePayloadFile;
    private File destinationPayloadFile;

    private FileOutputStream sourcePayload;
    private FileOutputStream destinationPayload;

    private DateTimeFormatter simpleTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss:SSS");

    public SimplePayloadHolder(String path, TcpSessionDescription description) {
        try {
            String suffix =  description.getSrcAddr().getHostName().replace(".","-")
                    +description.getDstAddr().getHostName().replace(".","-")
                    +":"
                    +description.getDstPort()
                    +"_"
                    +description.getTimestamp().format(simpleTimeFormatter);
            sourcePayloadFile = new File(path+File.separator+"source-"+suffix);
            destinationPayloadFile = new File(path+File.separator+"destination-"+suffix);
            sourcePayloadFile.createNewFile();
            destinationPayloadFile.createNewFile();
            sourcePayload = new FileOutputStream(sourcePayloadFile);
            destinationPayload = new FileOutputStream(destinationPayloadFile);
            this.description = description;
        } catch (IOException io) {
            throw new RuntimeException(io);
        }

    }

    @Override
    public void put(IpTcpEntity ipTcpEntity, byte[] payload) {
        try {
            if (ipTcpEntity.getSrcAddr().equals(description.getSrcAddr())) {
                sourcePayload.write(payload);
            }
        } catch (IOException io) {
            throw new RuntimeException(io);
        }

        try {
            if (ipTcpEntity.getSrcAddr().equals(description.getDstAddr())) {
                destinationPayload.write(payload);
            }
        } catch (IOException io) {
            throw new RuntimeException(io);
        }

    }

    @Override
    public void onSessionClosed() {
        try {
            sourcePayload.flush();
            sourcePayload.close();
            destinationPayload.flush();
            destinationPayload.close();
        } catch (IOException io) {
            throw new RuntimeException(io);
        }

    }

    @Override
    public byte[] getRawSourceData() {
        try {
           return Files.readAllBytes(Paths.get(sourcePayloadFile.toURI()));
        } catch (IOException io) {
            throw new RuntimeException(io);
        }
    }

    @Override
    public byte[] getRawDestinationData() {
        try {
            return Files.readAllBytes(Paths.get(destinationPayloadFile.toURI()));
        } catch (IOException io) {
            throw new RuntimeException(io);
        }
    }

    @Override
    public InputStream getSourceDataStream() {
        try {
            return Files.newInputStream(Paths.get(sourcePayloadFile.toURI()), StandardOpenOption.READ);
        } catch (IOException io) {
            throw new RuntimeException(io);
        }
    }

    @Override
    public InputStream getDestinationDataStream() {
        try {
            return Files.newInputStream(Paths.get(destinationPayloadFile.toURI()), StandardOpenOption.READ);
        } catch (IOException io) {
            throw new RuntimeException(io);
        }
    }
}
