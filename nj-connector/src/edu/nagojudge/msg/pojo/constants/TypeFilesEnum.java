package edu.nagojudge.msg.pojo.constants;

/**
 *
 * @author andresfelipegarciaduran
 */
public enum TypeFilesEnum {

    TYPE_FILE_IN("input"),
    TYPE_FILE_OUT("output"),
    TYPE_FILE_PROBLEM("problem"),
    PDF(".pdf"),
    IN(".in"),
    OUT(".out"),
    PNG(".png"),
    GIF(".gif");

    private String extension;

    private TypeFilesEnum(String extension) {
        this.extension = extension;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

}
