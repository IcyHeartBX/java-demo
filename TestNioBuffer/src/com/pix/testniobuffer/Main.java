package com.pix.testniobuffer;

import java.nio.ByteBuffer;

public class Main {

    public static void main(String[] args) {
        testAllocate();
        testPut();
        testFlip();
        testGet();
        testClear();
    }

    public static void testByteBuffer() {
        ByteBuffer bb = ByteBuffer.allocate(100);
        System.out.println("testByteBuffer(),bb:" + bb.toString());
        bb.put((byte) 'a');
        System.out.println("testByteBuffer(),put a byte,bb:" + bb.toString());
        bb.flip();
        System.out.println("testByteBuffer(),flip(),bb:" + bb.toString());
        byte b = bb.get();
        System.out.println("testByteBuffer(),get a byte,b:" + b + ",bb:" + bb.toString());
        bb.clear();
        System.out.println("testByteBuffer(),clear(),bb:" + bb.toString());
        b = bb.get();
        System.out.println("testByteBuffer(),get a byte,b:" + b + ",bb:" + bb.toString());
        bb.rewind();
        System.out.println("testByteBuffer(),rewind(),bb:" + bb.toString());
        b = bb.get();
        System.out.println("testByteBuffer(),get a byte,b:" + b + ",bb:" + bb.toString());
        bb.compact();
        System.out.println("testByteBuffer(),compact()," + bb.toString());
        b = bb.get();
        System.out.println("testByteBuffer(),get a byte,b:" + b + ",bb:" + bb.toString());
    }

    public static  void testAllocate() {
        ByteBuffer bb = ByteBuffer.allocate(100);
        System.out.println("testAllocate(),bb:" + bb.toString());
    }

    public static void testPut() {
        ByteBuffer bb = ByteBuffer.allocate(100);
        System.out.println("testPut(),bb:" + bb.toString());
        bb.put((byte) 'a');
        System.out.println("testPut(),put a byte,bb:" + bb.toString());
    }

    public static void testFlip() {
        ByteBuffer bb = ByteBuffer.allocate(100);
        System.out.println("testFlip(),bb:" + bb.toString());
        bb.put((byte) 'a');
        System.out.println("testFlip(),put a byte,bb:" + bb.toString());
        bb.flip();
        System.out.println("testFlip(),flip(),bb:" + bb.toString());
    }

    public static void testGet() {
        ByteBuffer bb = ByteBuffer.allocate(100);
        System.out.println("testGet(),bb:" + bb.toString());
        bb.put((byte) 'a');
        System.out.println("testGet(),put a byte,bb:" + bb.toString());
        bb.flip();
        System.out.println("testGet(),flip(),bb:" + bb.toString());
        byte b = bb.get();
        System.out.println("testGet(),get a byte,b:" + b + ",bb:" + bb.toString());
    }

    public static void testClear() {
        ByteBuffer bb = ByteBuffer.allocate(100);
        System.out.println("testGet(),bb:" + bb.toString());
        bb.put((byte) 'a');
        bb.flip();
        bb.clear();
        System.out.println("testGet(),clear(),bb:" + bb.toString());
    }
}

