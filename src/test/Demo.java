package test;

public class Demo {
    public static void main(String[] args) {
        new C();
    }
}
class A{
    public A(){
        System.out.println("A");
    }
}
abstract class B extends A{
    public B(){
        System.out.println("B");
    }
}
class C extends B{
    public C(){
        super();
    }
}