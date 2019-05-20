package com.junorz.travelbook.context.security;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * Wrap HttpServletRequest to read InputStream multiple times.
 */
public class MultipleReadHttpRequest extends HttpServletRequestWrapper {

    private ByteArrayOutputStream cachedContent = new ByteArrayOutputStream();

    public MultipleReadHttpRequest(HttpServletRequest request) {
        super(request);

        try (InputStream is = request.getInputStream()) {
            byte[] buffer = new byte[16384];
            int bytesReaded = 0;
            while ((bytesReaded = is.read(buffer, 0, buffer.length)) != -1) {
                cachedContent.write(buffer, 0, bytesReaded);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(cachedContent.toByteArray());

        ServletInputStream servletInputStream = new ServletInputStream() {

            @Override
            public int read() throws IOException {
                return bis.read();
            }

            @Override
            public void setReadListener(ReadListener listener) {
                // Nothing
            }

            @Override
            public boolean isReady() {
                return true;
            }

            @Override
            public boolean isFinished() {
                return bis.available() == 0;
            }
        };

        return servletInputStream;
    }

    @Override
    public BufferedReader getReader() throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(cachedContent.toByteArray());
        return new BufferedReader(new InputStreamReader(bis, StandardCharsets.UTF_8));
    }

}
