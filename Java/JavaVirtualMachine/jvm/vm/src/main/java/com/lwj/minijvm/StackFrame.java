package com.lwj.minijvm;

import tech.medivh.classpy.classfile.MethodInfo;
import tech.medivh.classpy.classfile.bytecode.Instruction;
import tech.medivh.classpy.classfile.constant.ConstantPool;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

/**
 * Date: 2025/5/7
 * <p>
 * Description: 栈帧
 * <p></p>
 * 每个方法都会被封装为栈帧压入线程的jvm虚拟机栈中
 *
 * @author 乌柏
 */
public class StackFrame {
    /**
     * 栈帧中的关联方法
     */
    final MethodInfo methodInfo;

    /**
     * 局部变量表
     */
    final Object[] localVariables;

    /**
     * 操作数栈
     */
    final Deque<Object> operandStack;

    /**
     * 字节码指令
     */
    final List<Instruction> codes;

    /**
     * 字符串常量池
     */
    ConstantPool constantPool;

    /**
     * 当前执行的指令索引
     */
    int currentIndex;

    public StackFrame(MethodInfo methodInfo, ConstantPool constantPool, Object... args) {
        this.methodInfo = methodInfo;
        this.localVariables = new Object[methodInfo.getMaxLocals()];
        this.operandStack = new ArrayDeque<>();
        this.codes = methodInfo.getCodes();
        this.constantPool = constantPool;
        System.arraycopy(args, 0, this.localVariables, 0, args.length);
    }


    public Instruction getNextInstruction() {
        return codes.get(currentIndex++);
    }

    public void pushObjectToOperandStack(Object object) {
        operandStack.push(object);
    }

    public Object popOperandStack() {
        return operandStack.pop();
    }

    public void jumpTo(int index) {
        for (int i = 0; i < codes.size(); i++) {
            Instruction instruction = codes.get(i);
            if (instruction.getPc() == index) {
                this.currentIndex = i;
                return;
            }
        }
    }
}
