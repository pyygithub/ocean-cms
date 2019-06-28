package cn.com.thtf.vo;

/**
 * ========================
 * Created with IntelliJ IDEA.
 * User：pyy
 * Date：2019/6/28
 * Time：10:41
 * Version: v1.0
 * ========================
 */
public class UploadFileResult {
    private String fileNewName;  //文件新名称
    private String fileSavePath; //文件保持相对路径
    private String httpPath;     //文件http绝对路径

    public String getFileNewName() {
        return fileNewName;
    }

    public void setFileNewName(String fileNewName) {
        this.fileNewName = fileNewName;
    }

    public String getFileSavePath() {
        return fileSavePath;
    }

    public void setFileSavePath(String fileSavePath) {
        this.fileSavePath = fileSavePath;
    }

    public String getHttpPath() {
        return httpPath;
    }

    public void setHttpPath(String httpPath) {
        this.httpPath = httpPath;
    }
}
