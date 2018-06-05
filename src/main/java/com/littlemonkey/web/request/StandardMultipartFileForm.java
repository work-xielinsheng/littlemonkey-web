package com.littlemonkey.web.request;

import org.springframework.web.multipart.MultipartFile;

import java.io.*;

/**
 * @Author: xls
 * @Description:
 * @Date: Created in 0:31 2018/4/6
 * @Version: 1.0
 */
public class StandardMultipartFileForm implements MultipartFile, Serializable {

    private String paramName;

    private String fileName;

    private String contentType;

    private byte[] content;

    private long size;

    public StandardMultipartFileForm(String paramName, String fileName, String contentType, byte[] content, long size) {
        this.paramName = paramName;
        this.fileName = fileName;
        this.contentType = contentType;
        this.content = content;
        this.size = size;
    }

    public String getName() {
        return this.paramName;
    }

    public String getOriginalFilename() {
        return this.fileName;
    }

    public String getContentType() {
        return this.contentType;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public long getSize() {
        return this.size;
    }

    public byte[] getBytes() throws IOException {
        return this.content;
    }

    public InputStream getInputStream() throws IOException {
        return new ByteArrayInputStream(getBytes());
    }

    public void transferTo(File dest) throws IOException, IllegalStateException {
        if (dest.isAbsolute() && !dest.exists()) {
            BufferedOutputStream bufferedOutput = new BufferedOutputStream(new FileOutputStream(dest));
            bufferedOutput.write(this.getBytes());
            bufferedOutput.close();
        }
    }

}
