package datastructure1;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.function.IntConsumer;

class ZeroEvenOdd {
    private final int n;
    private volatile int count; // 新增volatile，确保所有线程看到最新的count值
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition zero= lock.newCondition();
    private final Condition even = lock.newCondition();
    private final Condition odd= lock.newCondition();
    private volatile boolean flag = false;
    public ZeroEvenOdd(int n) {
        this.n = n;
        this.count = 0;
    }



    // printNumber.accept(x) outputs "x", where x is an integer.
    public void zero(IntConsumer printNumber) throws InterruptedException {
        while (count < n) { // 需要打印n个0（对应1~n）
            lock.lock();
            try {
                while (flag) { // 等待奇/偶线程执行完
                    zero.await();
                }
                printNumber.accept(0);
                count++;
                flag = true; // 切换为奇/偶可执行
                // 唤醒对应线程
                if (count % 2 == 0) {
                    even.signal();
                } else {
                    odd.signal();
                }
            } finally {
                lock.unlock();
            }
        }
        // zero结束后，唤醒所有线程检查退出条件
        lock.lock();
        try {
            even.signal();
            odd.signal();
        } finally {
            lock.unlock();
        }
    }

    public void even(IntConsumer printNumber) throws InterruptedException {
        while (true) {
            lock.lock();
            try {
                // 先检查是否所有任务已完成（count > n 作为退出信号）
                if (count > n) {
                    break;
                }
                // 等待条件：flag为false（需先打印0）或当前count为奇数
                while (!flag || count % 2 != 0) {
                    if (count > n) {
                        break;
                    }
                    even.await();
                }
                if (count > n) {
                    break;
                }
                // 打印偶数
                if(count <= n) printNumber.accept(count);
                // 如果是最后一个数字，手动触发退出信号（count = n+1）
                if (count == n) {
                    count++; // 让count > n，触发所有线程退出
                    break;
                }
                // 切换为zero可执行
                flag = false;
                zero.signal();
            } finally {
                lock.unlock();
            }
        }
    }

    public void odd(IntConsumer printNumber) throws InterruptedException {
        while (true) {
            lock.lock();
            try {
                // 先检查是否所有任务已完成（count > n 作为退出信号）
                if (count > n) {
                    break;
                }
                // 等待条件：flag为false（需先打印0）或当前count为偶数
                while (!flag || count % 2 == 0) {
                    if (count > n) {
                        break;
                    }
                    odd.await();
                }
                if (count > n) {
                    break;
                }
                // 打印奇数
                if(count <= n) printNumber.accept(count);
                // 如果是最后一个数字，手动触发退出信号（count = n+1）
                if (count == n) {
                    count++; // 让count > n，触发所有线程退出
                    break;
                }
                // 切换为zero可执行
                flag = false;
                zero.signal();
            } finally {
                lock.unlock();
            }
        }
    }

    public static void main(String[] args) {
        ZeroEvenOdd zeroEvenOdd = new ZeroEvenOdd(5);
        new Thread(() -> {
            try {
                zeroEvenOdd.zero(System.out::println);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try {
                zeroEvenOdd.even(System.out::println);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
        new Thread(() -> {
            try {
                zeroEvenOdd.odd(System.out::println);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }
}