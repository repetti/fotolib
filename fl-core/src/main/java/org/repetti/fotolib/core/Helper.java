package org.repetti.fotolib.core;

import org.repetti.utils.SecurityHelper;
import org.repetti.utils.StringHelper;
import org.repetti.utils.UtilsException;

import java.io.File;

/**
 * Date: 24/07/15
 *
 * @author repetti
 */
public class Helper {
    public static Info getInfo(File f) throws UtilsException {
        String path = f.getAbsoluteFile().getParent();//.getAbsolutePath();
        String name = f.getName();
        long lastModified = f.lastModified();
        long [] size = new long[1];
        String sha = StringHelper.toHexString(SecurityHelper.sha512(f, size));
        return new Info(path, name, sha, lastModified, size[0]);

    }
}
