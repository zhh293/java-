package 状态机模式;

public class ExampleOne {
//    通用约定：订单的状态与事件
//    状态（State）	说明	事件（Event）	说明
//    UNPAID	待支付	PAY	支付订单
//    PAID	已支付	DELIVER	商家发货
//    DELIVERED	已发货	CONFIRM	买家确认收货
//    COMPLETED	已完成	CANCEL	取消订单
//    CANCELLED	已取消	-	-

//
//    核心转移规则（实际业务可扩展）：
//    UNPAID（待支付）：PAY→PAID；CANCEL→CANCELLED
//    PAID（已支付）：DELIVER→DELIVERED；CANCEL→CANCELLED（未发货可取消）
//    DELIVERED（已发货）：CONFIRM→COMPLETED（仅能确认收货）
//    COMPLETED/CANCELLED（终态）：无任何转移规则（状态不再变化）
    public enum OrderState {
        UNPAID, PAID, DELIVERED, COMPLETED, CANCELLED
    }

    public  enum OrderEvent {
        PAY, DELIVER, CONFIRM, CANCEL
    }
    public static class Order {
        public OrderState state;
        public Order(OrderState state) {
            this.state = state;
        }

        public void changeState(OrderEvent event) {
            if(state == OrderState.UNPAID){
                handleUnpaid(event);
            }else if(state == OrderState.PAID){
                handlePaid(event);
            }else if (state == OrderState.DELIVERED){
                handleDelivered(event);
            }else if(state == OrderState.COMPLETED){
                handleCompleted(event);
            }else if(state == OrderState.CANCELLED){
                handleCancelled(event);
            }
        }
        public void handleUnpaid(OrderEvent event) {
            switch (event){
                case PAY:
                    state = OrderState.PAID;
                    System.out.println("订单已支付");
                    break;
                case CANCEL:
                    state = OrderState.CANCELLED;
                    System.out.println("订单已取消");
                    break;
                default:
                    System.out.println("无效操作");
                    break;
            }
        }

        private void handlePaid(OrderEvent event) {
            switch (event){
                case DELIVER:
                    state = OrderState.DELIVERED;
                    System.out.println("订单已发货");
                    break;
                case CANCEL:
                    state = OrderState.CANCELLED;
                    System.out.println("订单已取消");
                    break;
                default:
                    System.out.println("无效操作");
                    break;
            }
        }
        private void handleDelivered(OrderEvent event) {
            switch (event){
                case CONFIRM:
                    state = OrderState.COMPLETED;
                    System.out.println("订单已完成");
                    break;
                default:
                    System.out.println("无效操作");
                    break;
            }
        }
        private void handleCompleted(OrderEvent event) {
            System.out.println("订单已完成");
        }
        private void handleCancelled(OrderEvent event) {
            System.out.println("订单已取消");
        }



    }

//
//    这个例子虽然用了if-else，但已经体现了状态机的核心逻辑：
//    上下文（Order）持有当前状态，对外提供triggerEvent统一入口；
//    严格遵循「当前状态 + 事件→目标状态」的转移规则；
//    状态转移与业务动作（如扣库存、退款）绑定；
//    严格禁止非法状态流转（如待支付订单不能发货）。


}
