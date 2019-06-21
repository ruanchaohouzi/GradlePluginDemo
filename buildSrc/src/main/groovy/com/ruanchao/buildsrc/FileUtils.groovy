package com.ruanchao.buildsrc

class FileUtils {
    public static boolean copyFile(File src, File dist) {
        boolean isSuccess = false;
        if (src.exists()) {
            if (src.isFile()) {
                isSuccess = copyFile(src, dist);
            } else {
                if (dist.getPath().indexOf(src.getPath()) != -1) {
                    return false;
                }
                dist.mkdirs();
                File[] fs = src.listFiles();
                for (File f : fs) {
                    copy(f, new File(dist.getPath() + File.separator + f.getName()));
                }
                isSuccess = true;
            }
        }
        return isSuccess;
    }
}