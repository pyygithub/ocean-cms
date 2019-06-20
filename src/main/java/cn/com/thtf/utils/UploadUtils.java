package cn.com.thtf.utils;

import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;

import java.io.File;
import java.util.UUID;

/**
 * ========================
 * Created with IntelliJ IDEA.
 * User：pyy
 * Date：2019/6/19
 * Time：13:47
 * Version: v1.0
 * ========================
 */
public class UploadUtils {
    /**
     * 得到真实文件名
     *
     * @param fileName
     * @return
     */
    public static String subFileName(String fileName) {
        // 查找最后一个 \ (文件分隔符)位置
        int index = fileName.lastIndexOf(File.separator);
        if (index == -1) {
            // 没有分隔符，说明是真实名称
            return fileName;
        } else {
            return fileName.substring(index + 1);
        }
    }

    /**
     * 获得随机UUID文件名
     *
     * @param fileName
     * @return
     */
    public static String generateRandonFileName(String fileName) {
        // 首相获得扩展名，然后生成一个UUID码作为名称，然后加上扩展名
        String ext = fileName.substring(fileName.lastIndexOf("."));
        return UUID.randomUUID().toString() + ext;
    }

    public static String generateRandonFileName() {
        return UUID.randomUUID().toString();
    }

    /**
     * 获得hashcode 生成二级目录
     *
     * @param uuidFileName
     * @return
     */
    public static String generateRandomDir(String uuidFileName) {
        int hashCode = uuidFileName.hashCode();// 得到它的hashcode编码
        // 一级目录
        int d1 = hashCode & 0xf;
        // 二级目录
        int d2 = (hashCode >> 4) & 0xf;
        return "/" + Integer.toHexString(d1) + "/" + Integer.toHexString(d2);
    }

    public static void main(String[] args) {
        System.out.println(generateRandonFileName());

        System.out.println(generateRandomDir("abc.txt"));
    }
}
