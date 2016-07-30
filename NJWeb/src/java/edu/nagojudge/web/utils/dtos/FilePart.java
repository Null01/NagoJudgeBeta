package edu.nagojudge.web.utils.dtos;

import java.io.Serializable;

/**
 *
 * @author andresfelipegarciaduran
 */
public class FilePart implements Serializable {

    private String name;
    private String contentType;
    private long size;
    private byte[] content;

    public FilePart(String name, String contentType, long size, byte[] content) {
        this.name = name;
        this.contentType = contentType;
        this.size = size;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public byte[] getContent() {
        return content;
    }

    public void setContent(byte[] content) {
        this.content = content;
    }

}
