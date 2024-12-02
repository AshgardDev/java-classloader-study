package org.example;

import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.SecureClassLoader;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class SecurityClassLoader extends SecureClassLoader {

    // 这里公钥私钥密码对就简单写这里了，实际上应该要放个地方存储起来，别被发现
    private static String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIAQg9QOQppogL4cctdeO+XEQ/R18Iy0FDX2nRLVwYlEUck90rekLXyMKSvdsrMISFxmIP4vuA7yE1Ty9fbEimtI0LF65wPqDjkLwntSPu/uVp9nDlKtijo0oV0vCM94scNjwI2Yvt8TCYE0Id2gQ+F+kbc7X/Bj1lK4DyLvuCftAgMBAAECgYB1Xi1jiFo0Jzhug6YgicW9c12QSxLV8ShguI8GNw9znUCTfeyDz5y8e7wz7rAa8qlWvWbZbqRyVhuvjCguK7xq/iNUJN1BK0edlICxnC1vIcIDQxpH4bmZVhp/kf+QTcBx4coSwgcbAnq9eTeJB/9VJd4HEZLS3KMn6FXN1SMDQQJBAM3o+pOBB+0dJP2LEdYCO9fe0DAUbTQ9OGy4eoaL0jTVV+r9uylCxnWDPqtVfj5HI02BRkDIGI3oCvlqGiUTJt0CQQCfN7bv6rNG7P3SzOL5sLXhntet0QfSo3gxwfblrhExmT2fUSa7kME85+lCOZTntBWxj12KbO30WIARRdIOiIxRAkAE+tp8kMVRTcAkRaXDyEAMQ35De2r8tXJU5s1Hzb/iuTosG8DYCZ5lHyx31lR8SxomeLbrQCuf2vnQ+GjzdpgdAkEAkPY0HCovQhNCYmxi931Zi+uIaVwY6EIni/4Sojmis+rWvTgwsLNyDNcFfGNa3L8MxVZHj2HwIPl22MMeJldSkQJAVJhGhHOdxOWnOsw5bZM4Io//xW2fLvaA1BnFd2PbEUZ+KCWlcfAsDZDDVh8Pqz8Conl+6YqFOrPCHZk6zn8bJg==";

    private static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCAEIPUDkKaaIC+HHLXXjvlxEP0dfCMtBQ19p0S1cGJRFHJPdK3pC18jCkr3bKzCEhcZiD+L7gO8hNU8vX2xIprSNCxeucD6g45C8J7Uj7v7lafZw5SrYo6NKFdLwjPeLHDY8CNmL7fEwmBNCHdoEPhfpG3O1/wY9ZSuA8i77gn7QIDAQAB";

    private final JarFile jar;

    public SecurityClassLoader(File jarFile) throws IOException {
        this.jar = new JarFile(jarFile);
    }

    // 重载该方法，实现解密逻辑
    @Override
    public Class<?> loadClass(String name) throws ClassNotFoundException {
        /*
        try {
            return super.loadClass(name);
        } catch (ClassNotFoundException exception) {
            String jarEntryName = name.replace('.', '/') + ".security";
            JarEntry jarEntry = jar.getJarEntry(jarEntryName);
            try (InputStream is = jar.getInputStream(jarEntry)) {
                RSA rsa = new RSA(privateKey, publicKey);
                byte[] bytes = rsa.decrypt(is, KeyType.PrivateKey);
                return this.defineClass(name, bytes, 0, bytes.length);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        */
        synchronized (getClassLoadingLock(name)) {
            if (name.startsWith("org.example.SalaryCalc")) {
                String jarEntryName = name.replace('.', '/') + ".security";
                JarEntry jarEntry = jar.getJarEntry(jarEntryName);
                try (InputStream is = jar.getInputStream(jarEntry)) {
                    RSA rsa = new RSA(privateKey, publicKey);
                    byte[] bytes = rsa.decrypt(is, KeyType.PrivateKey);
                    return this.defineClass(name, bytes, 0, bytes.length);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            } else {
                return super.loadClass(name);
            }
        }
    }
}
