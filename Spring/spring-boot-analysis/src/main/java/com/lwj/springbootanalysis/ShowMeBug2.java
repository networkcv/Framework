package com.lwj.springbootanalysis;

/**
 * Date: 2022/3/31
 * <p>
 * Description:
 *
 * @author liuWangjie
 */

import java.lang.reflect.Proxy;

interface IA {
    String getHelloName();
}

public class ShowMeBug2 {
    public static void main(String[] arges) throws Exception {
        IA ia = (IA) createObject(IA.class.getName() + "$getHelloName=Abc");
        System.out.println(ia.getHelloName()); //方法名匹配的时候，输出“Abc”
        ia = (IA) createObject(IA.class.getName() + "$getTest=Bcd");
        System.out.println(ia.getHelloName()); //方法名不匹配的时候，输出null
    }

    //请实现方法createObject，接口中"getName()"方法名仅仅是个示例，不能写死判断

    public static Object createObject(String str) throws ClassNotFoundException {
        InputInfo inputInfo = parseStr(str);
        return Proxy.newProxyInstance(inputInfo.getClazz().getClassLoader(), new Class[]{inputInfo.getClazz()}, (proxy, method, args1) -> {
            if (inputInfo.getMethodName().equals(method.getName())) {
                return inputInfo.getExpectRes();
            }
            return null;
        });
    }

    static class InputInfo {
        private Class<?> clazz;
        private String methodName;
        private String expectRes;

        public InputInfo() {
        }

        public Class<?> getClazz() {
            return clazz;
        }

        public void setClazz(Class<?> clazz) {
            this.clazz = clazz;
        }

        public String getMethodName() {
            return methodName;
        }

        public void setMethodName(String methodName) {
            this.methodName = methodName;
        }

        public String getExpectRes() {
            return expectRes;
        }

        public void setExpectRes(String expectRes) {
            this.expectRes = expectRes;
        }
    }

    public static InputInfo parseStr(String str) throws ClassNotFoundException {
        InputInfo inputInfo = new InputInfo();
        String[] paraArr = str.split("\\$");
        inputInfo.setClazz(Class.forName(paraArr[0]));
        String[] methodAndParaArr = paraArr[1].split("=");
        inputInfo.setMethodName(methodAndParaArr[0]);
        inputInfo.setExpectRes(methodAndParaArr[1]);
        return inputInfo;
    }


}


