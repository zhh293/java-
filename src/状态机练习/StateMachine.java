package 状态机练习;

import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class StateMachine {
    public static ConcurrentHashMap<OrderState, ConcurrentHashMap<OrderEvent, OrderState>> stateMachine = new ConcurrentHashMap<>();
    static {
        stateMachine.put(OrderState.UNPAID, new ConcurrentHashMap<OrderEvent, OrderState>() {{
            put(OrderEvent.PAY, OrderState.PAID);
            put(OrderEvent.CANCEL, OrderState.CANCELLED);
        }});
        stateMachine.put(OrderState.PAID, new ConcurrentHashMap<OrderEvent, OrderState>() {{
            put(OrderEvent.DELIVER, OrderState.DELIVERED);
            put(OrderEvent.CANCEL, OrderState.CANCELLED);
        }});
        stateMachine.put(OrderState.DELIVERED, new ConcurrentHashMap<OrderEvent, OrderState>() {{
            put(OrderEvent.CONFIRM, OrderState.COMPLETED);
        }});
        stateMachine.put(OrderState.COMPLETED, new ConcurrentHashMap<OrderEvent, OrderState>());
    }
    public static Optional<OrderState> getNextState(OrderState state, OrderEvent event) {
        return Optional.ofNullable(stateMachine.get(state).get(event));
    }
}
