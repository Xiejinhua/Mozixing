package com.gz.mozixing.utils;

import android.os.Environment;

import java.io.File;

/**
 * @author Alex
 * @since 2019/2/10
 */
public class AppFolderUtil {
    /**
     * 获取app在sd卡上的主目录
     *
     * @return 成功则返回目录，失败则返回null
     */
    public static File getAppFolder() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

            File appFolder = new File(Environment.getExternalStorageDirectory(), "mcash");
            return createOnNotFound(appFolder);

        } else {
            return null;
        }
    }
    /**
     * 创建目录
     *
     * @param folder
     * @return 创建成功则返回目录，失败则返回null
     */
    private static File createOnNotFound(File folder) {
        if (folder == null) {
            return null;
        }

        if (!folder.exists()) {
            folder.mkdirs();
        }

        if (folder.exists()) {
            folder.mkdirs();
            return folder;
        } else {
            return folder;
        }
    }

    /**
     * 清除文件夹
     * @param file
     */
    public static void clearFolder(File file){
        File files[] = file.listFiles();
        if (files != null)
            for (File f : files) {
                if (f.isDirectory()) { // 判断是否为文件夹

                    try {
                        f.delete();
                    } catch (Exception e) {
                    }
                } else {
                    if (f.exists()) { // 判断是否存在

                        try {
                            f.delete();
                        } catch (Exception e) {
                        }
                    }
                }
            }
    }
}
