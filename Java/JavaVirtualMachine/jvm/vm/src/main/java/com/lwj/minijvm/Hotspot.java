package com.lwj.minijvm;

import tech.medivh.classpy.classfile.ClassFile;

import java.io.File;
import java.util.Arrays;

/**
 * Date: 2025/5/7
 * <p>
 * Description:
 *
 * @author 乌柏
 */
public class Hotspot {

    private final String mainClass;
    private final BootstrapClassLoader classLoader;

    public Hotspot(String mainClass, String classPathStr) {
        this.mainClass = mainClass;
        this.classLoader = new BootstrapClassLoader(Arrays.asList(classPathStr.split(File.pathSeparator)));
    }


    public void start() throws Exception {
        ClassFile classFile = classLoader.loadClass(mainClass);
        StackFrame mainMethodStackFrame = new StackFrame(classFile.getMainMethod(), classFile.getConstantPool());
        Thread main = new Thread("main", mainMethodStackFrame, classLoader);
        main.start();
    }
}
