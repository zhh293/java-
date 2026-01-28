package 状态机模式;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class ExampleThree {
//    枚举轻量实现状态机 —— 企业开发最常用
//    状态模式的问题是：状态多了会产生大量的具体状态类（如 10 个状态就有 10 个类），代码量冗余。企业开发中最常用的是「枚举轻量实现」：把状态、事件、转移规则都用枚举管理，用Map维护「当前状态 + 事件→目标状态」的映射关系，结合函数式接口处理转移动作，兼顾「轻量简洁」和「可扩展」，是状态数 5-10 个场景的最优解。
//    核心亮点：
//    用Map<OrderEvent, StateAction>维护每个状态的「事件→（目标状态 + 动作）」映射；
//    用函数式接口StateAction封装转移动作，支持 Lambda 表达式，简化代码；
//    无多余类，所有逻辑集中在枚举中，代码量极少，易维护。


    // 1. 定义事件枚举（与之前一致）
    public enum OrderEvent {
        PAY, DELIVER, CONFIRM, CANCEL
    }

    // 2. 核心：订单状态枚举（封装所有转移规则和动作）
    public enum OrderState {
        // 2.1 定义状态，构造方法传入「事件→(目标状态+动作)」的映射
        UNPAID(
                new HashMap<OrderEvent, StateAction>() {{
                    // 待支付+支付→已支付，执行支付动作
                    put(OrderEvent.PAY, new StateAction(PAID, orderId ->
                            System.out.println("订单[" + orderId + "]执行支付：扣减库存，生成支付记录")));
                    // 待支付+取消→已取消，执行取消动作
                    put(OrderEvent.CANCEL, new StateAction(CANCELLED, orderId ->
                            System.out.println("订单[" + orderId + "]执行取消：释放库存")));
                }}
        ),
        PAID(
                new HashMap<OrderEvent, StateAction>() {{
                    put(OrderEvent.DELIVER, new StateAction(DELIVERED, orderId ->
                            System.out.println("订单[" + orderId + "]执行发货：生成物流单")));
                    put(OrderEvent.CANCEL, new StateAction(CANCELLED, orderId ->
                            System.out.println("订单[" + orderId + "]执行退款：退还金额，释放库存")));
                }}
        ),
        DELIVERED(
                new HashMap<OrderEvent, StateAction>() {{
                    put(OrderEvent.CONFIRM, new StateAction(COMPLETED, orderId ->
                            System.out.println("订单[" + orderId + "]执行确认收货：完成订单结算")));
                }}
        ),
        // 终态：空映射，无任何转移规则
        COMPLETED(new HashMap<>()),
        CANCELLED(new HashMap<>());

        // 每个状态持有：事件→(目标状态+动作)的映射（核心）
        private final Map<OrderEvent, StateAction> transitionMap;

        // 枚举构造方法
        OrderState(Map<OrderEvent, StateAction> transitionMap) {
            this.transitionMap = transitionMap;
        }

        // 对外提供的方法：根据事件获取转移动作（目标状态+业务动作）
        public StateAction getTransition(OrderEvent event) {
            return transitionMap.get(event);
        }
    }

    // 3. 状态动作封装类：存储「目标状态」+「转移时的业务动作」
    public static class StateAction {
        private final OrderState targetState; // 目标状态
        private final Consumer<String> action; // 业务动作（函数式接口，入参为订单ID）

        public StateAction(OrderState targetState, Consumer<String> action) {
            this.targetState = targetState;
            this.action = action;
        }

        // 执行动作
        public void execute(String orderId) {
            action.accept(orderId);
        }

        // getter
        public OrderState getTargetState() {
            return targetState;
        }
    }

    // 4. 上下文（Context）：订单对象（极简，核心是状态流转）
    static class Order {
        private String orderId;
        private OrderState currentState;

        public Order(String orderId) {
            this.orderId = orderId;
            this.currentState = OrderState.UNPAID; // 初始状态
        }

        // 对外统一入口：触发事件，执行状态转移
        public void triggerEvent(OrderEvent event) {
            // 1. 获取当前状态的转移动作
            StateAction stateAction = currentState.getTransition(event);
            if (stateAction == null) {
                System.out.println("订单[" + orderId + "]当前状态[" + currentState + "]，无法触发[" + event + "]事件！");
                return;
            }
            // 2. 执行业务动作
            stateAction.execute(orderId);
            // 3. 更新当前状态为目标状态
            this.currentState = stateAction.getTargetState();
            System.out.println("订单[" + orderId + "]状态更新为：" + currentState);
        }

        // 获取当前状态
        public OrderState getCurrentState() {
            return currentState;
        }
    }

    // 测试
    public static void main(String[] args) {
        Order order = new Order("O20260128003");
        System.out.println("初始状态：" + order.getCurrentState()); // UNPAID

        // 正常流转
        order.triggerEvent(OrderEvent.PAY);
        order.triggerEvent(OrderEvent.DELIVER);
        order.triggerEvent(OrderEvent.CONFIRM);
        // 终态触发非法事件
        order.triggerEvent(OrderEvent.CANCEL);

        System.out.println("---------------------");
        // 测试非法流转：待支付触发发货
        Order order2 = new Order("O20260128004");
        order2.triggerEvent(OrderEvent.DELIVER);
    }



}


//核心优势
//极致轻量：无多余类，所有状态、规则、动作都集中在枚举中，代码量只有状态模式的 1/3；
//易维护：转移规则以「键值对」形式定义，一目了然，修改 / 新增规则只需修改枚举的HashMap；
//灵活扩展：新增状态→新增枚举实例；新增事件→在对应状态的HashMap中添加映射；
//函数式编程：用Consumer函数式接口封装业务动作，支持 Lambda 表达式，简化代码；
//企业级实用：兼顾可读性、扩展性、简洁性，是阿里、美团等大厂订单 / 物流系统的主流实现方式。