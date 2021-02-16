package com.tabnine.binary;

import com.tabnine.binary.exceptions.TabNineDeadException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;

public class BinaryProcessGateway {
    private Process process = null;
    private BufferedReader reader = null;

    public void init(List<String> command) throws IOException {
        Process createdProcess = new ProcessBuilder(command).start();

        process = createdProcess;
        reader = new BufferedReader(new InputStreamReader(createdProcess.getInputStream(), StandardCharsets.UTF_8));
    }

    public String readRawResponse() throws IOException, TabNineDeadException {
        return Optional.ofNullable(reader.readLine())
                .orElseThrow(() -> new TabNineDeadException("End of stream reached"));
    }

    public void writeRequest(String request) throws IOException {
        OutputStream outputStream = process.getOutputStream();

        outputStream.write(request.getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
    }

    public boolean isDead() {
        return process == null || !process.isAlive();
    }

    public void destroy() {
        process.destroy();
    }
}
