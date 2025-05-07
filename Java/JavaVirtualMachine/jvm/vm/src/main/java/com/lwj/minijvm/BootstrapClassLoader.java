package com.lwj.minijvm;

import tech.medivh.classpy.classfile.ClassFile;
import tech.medivh.classpy.classfile.ClassFileParser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.Objects;

/**
 * Date: 2025/5/7
 * <p>
 * Description:
 *
 * @author 乌柏
 */
public class BootstrapClassLoader {

    private List<String> classPaths;

    public BootstrapClassLoader(List<String> classPaths) {
        this.classPaths = classPaths;
    }

    /**
     * 加载指定类
     *
     * @param className 全限定类名
     * @return 目标类的字节码
     */
    public ClassFile loadClass(String className) throws ClassNotFoundException {
        return classPaths.stream()
                .map(path -> tryLoad(path, className))
                .filter(Objects::nonNull)
                .findAny()
                .orElseThrow(() -> new ClassNotFoundException(className + "找不到"));

    }

    /**
     * 尝试在path下加载 mainClass
     *
     * @param path      类路径
     * @param mainClass 主类字节码的全限定名
     * @return 主类字节码文件
     */
    private ClassFile tryLoad(String path, String mainClass) {
        File classFilePath = new File(path, mainClass.replace(".", File.separator) + ".class");
        if (!classFilePath.exists()) {
            return null;
        }
        byte[] bytes;
        try {
            bytes = Files.readAllBytes(classFilePath.toPath());
        } catch (IOException e) {
            return null;
        }
        return new ClassFileParser().parse(bytes);
    }


}
