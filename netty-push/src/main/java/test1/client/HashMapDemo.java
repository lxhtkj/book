package test1.client;

import java.util.concurrent.locks.LockSupport;

public class HashMapDemo {

    public static void main(String[] args) {

        LockSupport.unpark(Thread.currentThread());

        LockSupport.park();

        System.out.println("hello");

        LockSupport.unpark(Thread.currentThread());

        LockSupport.unpark(Thread.currentThread());

        LockSupport.park();

        LockSupport.park();

        System.out.println("hello");

    }

}