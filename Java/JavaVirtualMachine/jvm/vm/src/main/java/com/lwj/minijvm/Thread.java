package com.lwj.minijvm;

import tech.medivh.classpy.classfile.ClassFile;
import tech.medivh.classpy.classfile.MethodInfo;
import tech.medivh.classpy.classfile.bytecode.*;
import tech.medivh.classpy.classfile.constant.ConstantMethodrefInfo;
import tech.medivh.classpy.classfile.constant.ConstantPool;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Date: 2025/5/7
 * <p>
 * Description: 线程
 *
 * @author 乌柏
 */
public class Thread {
    private final String name;
    private final JvmStack jvmStack;
    private PcRegister pcRegister;
    BootstrapClassLoader classLoader;


    public Thread(String name, StackFrame stackFrame, BootstrapClassLoader classLoader) {
        this.name = name;
        this.jvmStack = new JvmStack();
        this.jvmStack.push(stackFrame);
        this.pcRegister = new PcRegister(jvmStack);
        this.classLoader = classLoader;
    }

    public void start() throws Exception {
        for (Instruction instruction : pcRegister) {
            System.out.println(instruction);
            StackFrame stackFrame = this.jvmStack.peek();
            ConstantPool constantPool = stackFrame.constantPool;
            switch (instruction.getOpcode()) {
                case getstatic -> {
                    GetStatic getStatic = (GetStatic) instruction;
                    String className = getStatic.getClassName(constantPool);
                    String fieldName = getStatic.getFieldName(constantPool);
                    Object staticField;
                    if (className.contains("java")) {
                        Class<?> aClass = Class.forName(className);
                        Field declaredField = aClass.getDeclaredField(fieldName);
                        staticField = declaredField.get(null);
                        stackFrame.pushObjectToOperandStack(staticField);
                    }
                }
                case iconst_1 -> stackFrame.pushObjectToOperandStack(1);
                case iconst_2 -> stackFrame.pushObjectToOperandStack(2);
                case iconst_3 -> stackFrame.pushObjectToOperandStack(3);
                case iconst_4 -> stackFrame.pushObjectToOperandStack(4);
                case iload_0 -> jvmStack.peek().pushObjectToOperandStack(stackFrame.localVariables[0]);
                case iload_1 -> jvmStack.peek().pushObjectToOperandStack(stackFrame.localVariables[1]);
                case ifne -> {
                    int value1 = (int) stackFrame.popOperandStack();
                    if (value1 != 0) {
                        Branch branch = (Branch) instruction;
                        stackFrame.jumpTo(branch.getJumpTo());
                    }
                }
                case isub -> {
                    int value2 = (int) stackFrame.popOperandStack();
                    int value1 = (int) stackFrame.popOperandStack();
                    int result = value1 - value2;
                    stackFrame.pushObjectToOperandStack(result);
                }
                case iadd -> {
                    int value2 = (int) stackFrame.popOperandStack();
                    int value1 = (int) stackFrame.popOperandStack();
                    int result = value1 + value2;
                    stackFrame.pushObjectToOperandStack(result);
                }
                case bipush -> {
                    Bipush bipush = (Bipush) instruction;
                    stackFrame.pushObjectToOperandStack((int) bipush.getPushByte());
                }
                case invokevirtual -> {
                    InvokeVirtual invokeVirtual = (InvokeVirtual) instruction;
                    ConstantMethodrefInfo methodInfo = invokeVirtual.getMethodInfo(constantPool);
                    String className = methodInfo.className(constantPool);
                    String methodName = methodInfo.methodName(constantPool);
                    List<String> paramClassName = methodInfo.paramClassName(constantPool);
                    if (className.contains("java")) {
                        //由于自定义的classLoader只去加载指定的classpath，所以java包下的类需要特殊处理
                        //没法按照方法封装栈帧然后压栈的方式进行方法调用，所以将java包下的方法直接反射获取调用结果，然后存入操作数栈顶
                        Class<?> aClass = Class.forName(className);
                        Method declaredMethod = aClass.getDeclaredMethod(methodName, paramClassName.stream().map(this::nameToClass).toArray(Class[]::new));
                        Object[] args = new Object[paramClassName.size()];
                        for (int i = args.length - 1; i >= 0; i--) {
                            args[i] = jvmStack.peek().operandStack.pop();
                        }
                        Object result = declaredMethod.invoke(jvmStack.peek().operandStack.pop(), args);
                        if (!methodInfo.isVoid(constantPool)) {
                            jvmStack.peek().operandStack.push(result);
                        }
                        break;
                    }
                    ClassFile classFile = classLoader.loadClass(className);
                    MethodInfo finalMethodInfo = classFile.getMethods(methodName).get(0);
                    //这里调用的是virtual方法需要对象实例，数组索引0的位置存放的就是调用函数的对象
                    Object[] args = new Object[paramClassName.size() + 1];
                    for (int index = args.length - 1; index >= 0; index--) {
                        args[index] = stackFrame.popOperandStack();
                    }
                    StackFrame invokeStackFrame = new StackFrame(finalMethodInfo, constantPool, args);
                    jvmStack.push(invokeStackFrame);
                }
                case invokestatic -> {
                    InvokeStatic invokeVirtual = (InvokeStatic) instruction;
                    ConstantMethodrefInfo methodInfo = invokeVirtual.getMethodInfo(constantPool);
                    String className = methodInfo.className(constantPool);
                    String methodName = methodInfo.methodName(constantPool);
                    List<String> paramClassName = methodInfo.paramClassName(constantPool);
                    if (className.contains("java")) {
                        //由于自定义的classLoader只去加载指定的classpath，所以java包下的类需要特殊处理
                        //没法按照方法封装栈帧然后压栈的方式进行方法调用，所以将java包下的方法直接反射获取调用结果，然后存入操作数栈顶
                        Class<?> aClass = Class.forName(className);
                        Method declaredMethod = aClass.getDeclaredMethod(methodName, paramClassName.stream().map(this::nameToClass).toArray(Class[]::new));
                        Object[] args = new Object[paramClassName.size()];
                        for (int i = args.length - 1; i >= 0; i--) {
                            args[i] = jvmStack.peek().operandStack.pop();
                        }
                        Object result = declaredMethod.invoke(null, args);
                        if (!methodInfo.isVoid(constantPool)) {
                            jvmStack.peek().operandStack.push(result);
                        }
                        break;
                    }
                    ClassFile classFile = classLoader.loadClass(className);
                    MethodInfo finalMethodInfo = classFile.getMethods(methodName).get(0);
                    //静态函数不需要对象来调用
                    Object[] args = new Object[paramClassName.size()];
                    for (int index = args.length - 1; index >= 0; index--) {
                        args[index] = stackFrame.popOperandStack();
                    }
                    StackFrame invokeStackFrame = new StackFrame(finalMethodInfo, constantPool, args);
                    jvmStack.push(invokeStackFrame);

                }
                case _return -> {
                    this.jvmStack.pop();
                }
                case ireturn -> {
                    //当前函数要返回一个int值，这个值是当前栈帧操作数栈的栈顶元素，取得返回值后还要移除当前函数栈帧，
                    //并将返回值放到下一个栈帧操作数栈的栈顶
                    int result = (int) stackFrame.popOperandStack();
                    this.jvmStack.pop();
                    jvmStack.peek().pushObjectToOperandStack(result);
                }
                //if_icmple 7 比较操作数栈顶的两个元素进行比较，如果比较成功就跳转到第七行指令，如果不成功就继续向下走
                /*
                 * 0 iload_0
                 * 1 iload_1
                 * 2 if_icmple 7 (+5)
                 * 5 iload_0
                 * 6 ireturn
                 * 7 iload_1
                 * 8 ireturn
                 */
                case if_icmple -> {
                    int value2 = (int) stackFrame.popOperandStack();
                    int value1 = (int) stackFrame.popOperandStack();
                    if (value1 <= value2) {
                        Branch branch = (Branch) instruction;
                        stackFrame.jumpTo(branch.getJumpTo());
                    }

                }
                default -> throw new IllegalArgumentException("这个指令还未实现" + instruction);

            }
        }
    }

    private Class<?> nameToClass(String className) {
        if (className.equals("int")) {
            return int.class;
        }
        try {
            return Class.forName(className);
        } catch (Exception e) {
            return null;
        }

    }
}

