package viewUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/* loaded from: KaChat_Client.jar:cn/com/util/viewUtils.ResourcesUtils.class */
public class ResourcesUtils {
    public static File getResource(String path, String prefix, String suffix) {
        BufferedOutputStream bos = null;
        BufferedInputStream bis = null;
        try {
            try {
                File f = File.createTempFile(prefix, suffix);
                f.deleteOnExit();
                bos = new BufferedOutputStream(new FileOutputStream(f));
                byte[] bytes = new byte[1024];
                InputStream is = ResourcesUtils.class.getResourceAsStream(path);
                while (true) {
                    assert is != null;
                    int length = is.read(bytes);
                    if (length <= 0) {
                        break;
                    }
                    bos.write(bytes, 0, length);
                    bos.flush();
                }
                return f;
            } catch (Exception e) {
                e.printStackTrace();
                if (0 != 0) {
                    try {
                        bis.close();
                    } catch (IOException e2) {
                        e2.printStackTrace();
                    }
                }
                if (bos == null) {
                    return null;
                }
                try {
                    bos.close();
                    return null;
                } catch (IOException e3) {
                    e3.printStackTrace();
                    return null;
                }
            }
        } finally {
            if (0 != 0) {
                try {
                    bis.close();
                } catch (IOException e4) {
                    e4.printStackTrace();
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e5) {
                    e5.printStackTrace();
                }
            }
        }
    }
}
