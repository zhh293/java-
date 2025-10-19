package datastructure1;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class FooBar {
    private int n;
    public FooBar(int n) {
        this.n = n;
    }
    private ReentrantLock lock = new ReentrantLock();
    private boolean flag = true;
    private Condition fooCondition = lock.newCondition();
    private Condition barCondition = lock.newCondition();
    public void foo(Runnable printFoo) throws InterruptedException {
          for (int i = 0; i < n; i++){
              lock.lock();
              try {
                  while (!flag){
                      fooCondition.await();
                  }
                  printFoo.run();
                  flag = false;
                  barCondition.signal();
              }catch (Exception e){
                  e.printStackTrace();
              }finally {
                  lock.unlock();
              }
          }

    }

    public void bar(Runnable printBar) throws InterruptedException {
           for (int i = 0; i < n; i++){
               lock.lock();
               try {
                   while (flag){
                       barCondition.await();
                   }
                   printBar.run();
                   flag = true;
                     fooCondition.signal();
               }catch (Exception e){
                   e.printStackTrace();
               }finally {
                   lock.unlock();
               }
           }
    }
}
