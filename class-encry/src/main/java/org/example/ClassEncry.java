package org.example;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.io.FastByteArrayOutputStream;
import cn.hutool.core.io.IoUtil;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.asymmetric.KeyType;
import cn.hutool.crypto.asymmetric.RSA;

import java.io.*;
import java.nio.file.Files;
import java.security.KeyPair;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarOutputStream;

/**
 * Hello world!
 */
public class ClassEncry {

    private static String privateKey = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIAQg9QOQppogL4cctdeO+XEQ/R18Iy0FDX2nRLVwYlEUck90rekLXyMKSvdsrMISFxmIP4vuA7yE1Ty9fbEimtI0LF65wPqDjkLwntSPu/uVp9nDlKtijo0oV0vCM94scNjwI2Yvt8TCYE0Id2gQ+F+kbc7X/Bj1lK4DyLvuCftAgMBAAECgYB1Xi1jiFo0Jzhug6YgicW9c12QSxLV8ShguI8GNw9znUCTfeyDz5y8e7wz7rAa8qlWvWbZbqRyVhuvjCguK7xq/iNUJN1BK0edlICxnC1vIcIDQxpH4bmZVhp/kf+QTcBx4coSwgcbAnq9eTeJB/9VJd4HEZLS3KMn6FXN1SMDQQJBAM3o+pOBB+0dJP2LEdYCO9fe0DAUbTQ9OGy4eoaL0jTVV+r9uylCxnWDPqtVfj5HI02BRkDIGI3oCvlqGiUTJt0CQQCfN7bv6rNG7P3SzOL5sLXhntet0QfSo3gxwfblrhExmT2fUSa7kME85+lCOZTntBWxj12KbO30WIARRdIOiIxRAkAE+tp8kMVRTcAkRaXDyEAMQ35De2r8tXJU5s1Hzb/iuTosG8DYCZ5lHyx31lR8SxomeLbrQCuf2vnQ+GjzdpgdAkEAkPY0HCovQhNCYmxi931Zi+uIaVwY6EIni/4Sojmis+rWvTgwsLNyDNcFfGNa3L8MxVZHj2HwIPl22MMeJldSkQJAVJhGhHOdxOWnOsw5bZM4Io//xW2fLvaA1BnFd2PbEUZ+KCWlcfAsDZDDVh8Pqz8Conl+6YqFOrPCHZk6zn8bJg==";
    private static String publicKey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCAEIPUDkKaaIC+HHLXXjvlxEP0dfCMtBQ19p0S1cGJRFHJPdK3pC18jCkr3bKzCEhcZiD+L7gO8hNU8vX2xIprSNCxeucD6g45C8J7Uj7v7lafZw5SrYo6NKFdLwjPeLHDY8CNmL7fEwmBNCHdoEPhfpG3O1/wY9ZSuA8i77gn7QIDAQAB";

    public static void main(String[] args) throws Exception {
        File jarOutput = new File("/Users/hbj/Study/java-classloader-study/salary-system/target/salary-system-encry-1.0-SNAPSHOT.jar");
        try (
                JarFile jarFile = new JarFile(new File("/Users/hbj/Study/java-classloader-study/salary-system/target/salary-system-1.0-SNAPSHOT.jar"));
                JarOutputStream jos = new JarOutputStream(Files.newOutputStream(jarOutput.toPath()))
        ) {
            Enumeration<JarEntry> entries = jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry jarEntry = entries.nextElement();
                if (jarEntry.getName().endsWith(".class")) {
                    // 需要在这里加密
                    // 第一步修改class文件后缀
                    String jarEntryName = jarEntry.getName().replace(".class", ".security");
                    JarEntry newEntry = new JarEntry(jarEntryName);
                    jos.putNextEntry(newEntry);
                    // 第二步加密字节数组
                    InputStream is = jarFile.getInputStream(jarEntry);
                    RSA rsa = new RSA(privateKey, publicKey);
                    byte[] bytes = rsa.encrypt(is, KeyType.PublicKey);
                    jos.write(bytes);
                    // 加密
                } else {
                    InputStream is = jarFile.getInputStream(jarEntry);
                    FastByteArrayOutputStream bytes = IoUtil.read(is);
                    jos.putNextEntry(jarEntry);
                    jos.write(bytes.toByteArray());
                }
                jos.closeEntry();
            }
            jos.finish();
        }
    }

    public static void createKey() {
        KeyPair pair = SecureUtil.generateKeyPair("RSA");
        String privateKey = Base64.encode(pair.getPrivate().getEncoded());
        System.out.println("私钥\t" + privateKey);
        String publicKey = Base64.encode(pair.getPublic().getEncoded());
        System.out.println("公钥\t" + publicKey);
    }
}
