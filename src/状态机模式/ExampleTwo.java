package 状态机模式;

public class ExampleTwo {


    // 1. 定义状态和事件（与示例1一致）
    public enum OrderStateEnum {
        UNPAID, PAID, DELIVERED, COMPLETED, CANCELLED
    }
    public enum OrderEventEnum {
        PAY, DELIVER, CONFIRM, CANCEL
    }

    // 2. 上下文（Context）：订单对象（核心载体）
    static class OrderContext {
        // 持有当前状态的引用（核心）
        private OrderState currentState;
        // 订单ID（辅助标识）
        private String orderId;

        public OrderContext(String orderId) {
            this.orderId = orderId;
            // 初始状态：待支付
            this.currentState = new UnPaidState(this);
        }

        // 对外提供的统一入口：触发事件
        public void triggerEvent(OrderEventEnum event) {
            currentState.handleEvent(event);
        }

        // 状态回写：由具体状态类调用，修改上下文的当前状态
        public void setCurrentState(OrderState currentState) {
            this.currentState = currentState;
            System.out.println("订单[" + orderId + "]状态更新为：" + currentState.getState());
        }

        // 获取当前状态
        public OrderStateEnum getCurrentStateEnum() {
            return currentState.getState();
        }

        public String getOrderId() {
            return orderId;
        }
    }

    // 3. 抽象状态（State）：定义所有状态的通用方法
    interface OrderState {
        // 获取当前状态枚举
        OrderStateEnum getState();
        // 处理事件（核心方法：每个具体状态实现自己的转移逻辑）
        void handleEvent(OrderEventEnum event);
    }

    // 4. 具体状态类：待支付状态（UnPaidState）
    static class UnPaidState implements OrderState {
        // 持有上下文引用，用于回写状态
        private OrderContext orderContext;

        public UnPaidState(OrderContext orderContext) {
            this.orderContext = orderContext;
        }

        @Override
        public OrderStateEnum getState() {
            return OrderStateEnum.UNPAID;
        }

        @Override
        public void handleEvent(OrderEventEnum event) {
            switch (event) {
                case PAY:
                    // 1. 执行支付业务动作
                    System.out.println("订单[" + orderContext.getOrderId() + "]执行支付：扣减库存，生成支付记录");
                    // 2. 状态转移：待支付→已支付，回写上下文状态
                    orderContext.setCurrentState(new PaidState(orderContext));
                    break;
                case CANCEL:
                    System.out.println("订单[" + orderContext.getOrderId() + "]执行取消：释放库存");
                    orderContext.setCurrentState(new CancelledState(orderContext));
                    break;
                default:
                    System.out.println("待支付订单[" + orderContext.getOrderId() + "]无法触发[" + event + "]事件！");
            }
        }
    }

    // 5. 具体状态类：已支付状态（PaidState）
    static class PaidState implements OrderState {
        private OrderContext orderContext;

        public PaidState(OrderContext orderContext) {
            this.orderContext = orderContext;
        }

        @Override
        public OrderStateEnum getState() {
            return OrderStateEnum.PAID;
        }

        @Override
        public void handleEvent(OrderEventEnum event) {
            switch (event) {
                case DELIVER:
                    System.out.println("订单[" + orderContext.getOrderId() + "]执行发货：生成物流单");
                    orderContext.setCurrentState(new DeliveredState(orderContext));
                    break;
                case CANCEL:
                    System.out.println("订单[" + orderContext.getOrderId() + "]执行退款：退还金额，释放库存");
                    orderContext.setCurrentState(new CancelledState(orderContext));
                    break;
                default:
                    System.out.println("已支付订单[" + orderContext.getOrderId() + "]无法触发[" + event + "]事件！");
            }
        }
    }

    // 6. 具体状态类：已发货状态（DeliveredState）
    static class DeliveredState implements OrderState {
        private OrderContext orderContext;

        public DeliveredState(OrderContext orderContext) {
            this.orderContext = orderContext;
        }

        @Override
        public OrderStateEnum getState() {
            return OrderStateEnum.DELIVERED;
        }

        @Override
        public void handleEvent(OrderEventEnum event) {
            if (event == OrderEventEnum.CONFIRM) {
                System.out.println("订单[" + orderContext.getOrderId() + "]执行确认收货：完成订单结算");
                orderContext.setCurrentState(new CompletedState(orderContext));
            } else {
                System.out.println("已发货订单[" + orderContext.getOrderId() + "]无法触发[" + event + "]事件！");
            }
        }
    }

    // 7. 具体状态类：已完成状态（CompletedState）- 终态
    static class CompletedState implements OrderState {
        private OrderContext orderContext;

        public CompletedState(OrderContext orderContext) {
            this.orderContext = orderContext;
        }

        @Override
        public OrderStateEnum getState() {
            return OrderStateEnum.COMPLETED;
        }

        @Override
        public void handleEvent(OrderEventEnum event) {
            System.out.println("已完成订单[" + orderContext.getOrderId() + "]是终态，无法触发[" + event + "]事件！");
        }
    }

    // 8. 具体状态类：已取消状态（CancelledState）- 终态
    static class CancelledState implements OrderState {
        private OrderContext orderContext;

        public CancelledState(OrderContext orderContext) {
            this.orderContext = orderContext;
        }

        @Override
        public OrderStateEnum getState() {
            return OrderStateEnum.CANCELLED;
        }

        @Override
        public void handleEvent(OrderEventEnum event) {
            System.out.println("已取消订单[" + orderContext.getOrderId() + "]是终态，无法触发[" + event + "]事件！");
        }
    }

    // 测试：模拟订单状态流转
    public static void main(String[] args) {
        // 创建订单（上下文），初始状态：待支付
        OrderContext order = new OrderContext("O20260128001");
        System.out.println("初始状态：" + order.getCurrentStateEnum()); // UNPAID

        // 1. 触发支付事件
        order.triggerEvent(OrderEventEnum.PAY);
        // 2. 触发发货事件
        order.triggerEvent(OrderEventEnum.DELIVER);
        // 3. 触发确认收货事件
        order.triggerEvent(OrderEventEnum.CONFIRM);
        // 4. 终态触发取消事件
        order.triggerEvent(OrderEventEnum.CANCEL);

        System.out.println("---------------------");
        // 测试另一个订单：待支付直接取消
        OrderContext order2 = new OrderContext("O20260128002");
        order2.triggerEvent(OrderEventEnum.CANCEL);
        // 已取消订单触发支付
        order2.triggerEvent(OrderEventEnum.PAY);
    }


}


//状态机的纯面向对象落地方式，对应你提到的「设计模式中的状态机」。适用场景：状态较多（5+）、状态转移逻辑复杂、需要频繁扩展状态 / 事件的场景；追求代码高内聚、低耦合的企业开发。核心思想：把每个状态的流转逻辑封装到独立的具体状态类中，上下文（Order）不再管理状态转移，而是委托给当前的状态对象，彻底消除if-else，符合开闭原则（新增状态只需新增具体状态类，无需修改原有代码）。
//状态模式的核心角色
//抽象状态（State）：定义所有状态的通用方法（触发事件的方法），持有上下文引用（用于状态回写）；
//具体状态（ConcreteState）：如待支付状态、已支付状态，实现抽象状态的方法，管理当前状态的所有转移规则；
//上下文（Context）：拥有状态的核心对象，持有当前状态的引用，对外提供统一入口，委托具体状态对象处理事件。
