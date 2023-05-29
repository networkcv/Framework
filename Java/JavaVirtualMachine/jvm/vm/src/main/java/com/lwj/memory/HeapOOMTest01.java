package com.lwj.memory;


import java.util.ArrayList;
import java.util.List;

/**
* VM Args： -Xms20m -Xmx20m
*/
public class HeapOOMTest01 {
    static class OOMObject {
    }
    public static void main(String[] args) {
        List<OOMObject> oomObjectList = new ArrayList<OOMObject>();
        while (true) {
            oomObjectList.add(new OOMObject());

        }
    }
}