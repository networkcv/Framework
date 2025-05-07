package com.lwj.minijvm;

import tech.medivh.classpy.classfile.bytecode.Instruction;

import java.util.Iterator;

/**
 * Date: 2025/5/7
 * <p>
 * Description: PC寄存器
 *
 * @author 乌柏
 */
public class PcRegister implements Iterable<Instruction> {

    private JvmStack jvmStack;

    public PcRegister(JvmStack jvmStack) {
        this.jvmStack = jvmStack;
    }

    @Override
    public Iterator<Instruction> iterator() {
        return new Itr();
    }

    class Itr implements Iterator<Instruction> {

        @Override
        public boolean hasNext() {
            return !jvmStack.isEmpty();
        }

        @Override
        public Instruction next() {
            StackFrame stackFrame = jvmStack.peek();
            return stackFrame.getNextInstruction();
        }
    }
}
