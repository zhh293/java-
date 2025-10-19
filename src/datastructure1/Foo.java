package datastructure1;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

class Foo {
    private ReentrantLock lock = new ReentrantLock();
    private int state = 1;
    private Condition first = lock.newCondition();
    private Condition second = lock.newCondition();
    private Condition third = lock.newCondition();

    public Foo() {

    }

    public void first(Runnable printFirst) throws InterruptedException {
        //这都是有套路的孩子
        lock.lock();
        try {
            while (state != 1){
                first.await();
            }
            printFirst.run();
            state = 2;
            second.signal();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            lock.unlock();
        }
    }

    public void second(Runnable printSecond) throws InterruptedException {
        lock.lock();
        try {
            while (state != 2){
                second.await();
            }
            printSecond.run();
            state = 3;
            third.signal();
        }catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            lock.unlock();
        }
    }

    public void third(Runnable printThird) throws InterruptedException {
        lock.lock();
        try {
            while (state != 3){
                third.await();
            }
            printThird.run();
            state = 1;
            first.signal();
        }catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            lock.unlock();
        }
    }
}