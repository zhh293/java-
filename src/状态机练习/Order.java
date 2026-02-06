package 状态机练习;

import java.util.Optional;

public class Order {
    private String orderId;
    private OrderState state;
    public Order(String orderId) {
        this.orderId = orderId;
        this.state = OrderState.UNPAID;
    }
    public void triggerEvent(OrderEvent event) {
        if(event==null){
            return;
        }
        Optional<OrderState> nextState = StateMachine.getNextState(state, event);
        if(nextState.isPresent()){
            this.state = nextState.get();
            System.out.println("订单["+orderId+"]状态更新为："+state);
            handleEvent(event);
        }else{
            System.out.println("订单["+orderId+"]当前状态["+state+"]，无法触发["+event+"]事件！");
            return;
        }
    }
    private void handleEvent(OrderEvent event) {
        switch (event){
            case PAY:
                System.out.println("订单["+orderId+"]执行支付：扣减库存，生成支付记录");
                break;
            case DELIVER:
                System.out.println("订单["+orderId+"]执行发货：生成物流单");
                break;
            case CONFIRM:
                System.out.println("订单["+orderId+"]执行确认收货：完成订单结算");
                break;
        }
    }
}
