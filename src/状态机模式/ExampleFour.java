package 状态机模式;

public class ExampleFour {
//    示例 4：工业级框架实现（Spring Statemachine）—— 分布式系统进阶
//    前面的示例都是单机内存状态机，而你提到的「分布式系统中的状态机」，需要解决状态持久化、分布式一致性、复杂状态流转（如子状态、并行状态）、状态监听等问题，此时无需重复造轮子，直接使用Spring Statemachine（Spring 官方提供的工业级状态机框架）。
//    Spring Statemachine 是状态机思想的工程化落地，支持：
//    状态持久化（将状态保存到 DB/Redis，适配分布式系统）；
//    复杂状态流转（嵌套状态、并行状态、历史状态）；
//    状态监听（监听状态转移的前后事件，做日志 / 告警）；
//    分布式锁（保证分布式环境下状态流转的原子性）；
//    与 Spring 生态无缝集成（Spring Boot/Spring Cloud）。
//    适用场景：分布式系统（如分布式订单、分布式事务、服务治理）、状态流转极复杂（10 + 状态）、需要状态持久化和分布式一致性的工业级场景。
//    步骤 1：引入依赖（Spring Boot 项目）
//    xml
//            <!-- Spring Statemachine核心依赖 -->
//<dependency>
//    <groupId>org.springframework.statemachine</groupId>
//    <artifactId>spring-statemachine-core</artifactId>
//    <version>3.2.0</version>
//</dependency>
//<!-- 可选：Spring Boot自动配置 -->
//<dependency>
//    <groupId>org.springframework.statemachine</groupId>
//    <artifactId>spring-statemachine-boot</artifactId>
//    <version>3.2.0</version>
//</dependency>
//    import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.Bean;
//import org.springframework.statemachine.StateContext;
//import org.springframework.statemachine.StateMachine;
//import org.springframework.statemachine.config.EnableStateMachine;
//import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
//import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
//import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
//import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
//import org.springframework.statemachine.listener.StateMachineListenerAdapter;
//import org.springframework.statemachine.state.State;
//
//import java.util.EnumSet;
//
//    /**
//     * Spring Statemachine实现分布式状态机 - 订单示例
//     * 核心：框架封装了状态机的核心逻辑，只需配置「状态、事件、转移规则」，支持持久化/监听/分布式
//     */
//    @SpringBootApplication
//    @EnableStateMachine // 开启状态机
//    public class SpringStateMachineDemo {
//        // 1. 定义状态和事件（与之前一致）
//        public enum OrderState {
//            UNPAID, PAID, DELIVERED, COMPLETED, CANCELLED
//        }
//        public enum OrderEvent {
//            PAY, DELIVER, CONFIRM, CANCEL
//        }
//
//        // 2. 状态机核心配置：配置状态、事件、转移规则
//        @Bean
//        public StateMachineConfigurerAdapter<OrderState, OrderEvent> stateMachineConfig() {
//            return new StateMachineConfigurerAdapter<OrderState, OrderEvent>() {
//                // 配置状态
//                @Override
//                public void configure(StateMachineStateConfigurer<OrderState, OrderEvent> states) throws Exception {
//                    states
//                            .withStates()
//                            .initial(OrderState.UNPAID) // 初始状态
//                            .states(EnumSet.allOf(OrderState.class)); // 所有状态
//                }
//
//                // 配置转移规则（核心：当前状态 + 事件 → 目标状态）
//                @Override
//                public void configure(StateMachineTransitionConfigurer<OrderState, OrderEvent> transitions) throws Exception {
//                    transitions
//                            // 待支付→已支付（支付事件）
//                            .withExternal().source(OrderState.UNPAID).target(OrderState.PAID).event(OrderEvent.PAY)
//                            .and()
//                            // 待支付→已取消（取消事件）
//                            .withExternal().source(OrderState.UNPAID).target(OrderState.CANCELLED).event(OrderEvent.CANCEL)
//                            .and()
//                            // 已支付→已发货（发货事件）
//                            .withExternal().source(OrderState.PAID).target(OrderState.DELIVERED).event(OrderEvent.DELIVER)
//                            .and()
//                            // 已支付→已取消（取消事件）
//                            .withExternal().source(OrderState.PAID).target(OrderState.CANCELLED).event(OrderEvent.CANCEL)
//                            .and()
//                            // 已发货→已完成（确认收货事件）
//                            .withExternal().source(OrderState.DELIVERED).target(OrderState.COMPLETED).event(OrderEvent.CONFIRM);
//                    // 终态（COMPLETED/CANCELLED）无需配置转移规则
//                }
//
//                // 配置监听器：监听状态转移事件
//                @Override
//                public void configure(StateMachineConfigurationConfigurer<OrderState, OrderEvent> config) throws Exception {
//                    config
//                            .withConfiguration()
//                            .listener(new StateMachineListenerAdapter<OrderState, OrderEvent>() {
//                                // 监听状态改变
//                                @Override
//                                public void stateChanged(State<OrderState, OrderEvent> from, State<OrderState, OrderEvent> to) {
//                                    if (from == null) {
//                                        System.out.println("状态机初始化，初始状态：" + to.getId());
//                                    } else {
//                                        System.out.println("状态转移：" + from.getId() + " → " + to.getId());
//                                        // 执行状态转移的业务动作（如扣库存、退款、生成物流单）
//                                        doBusinessAction(from.getId(), to.getId());
//                                    }
//                                }
//
//                                // 监听状态转移失败（非法事件）
//                                @Override
//                                public void stateMachineError(StateContext<OrderState, OrderEvent> context) {
//                                    OrderState currentState = context.getStateMachine().getState().getId();
//                                    OrderEvent event = context.getEvent();
//                                    System.out.println("状态转移失败：当前状态[" + currentState + "]，触发事件[" + event + "]，非法流转！");
//                                }
//                            });
//                }
//            };
//        }
//
//        // 3. 状态转移时的业务动作（可根据实际业务扩展）
//        private static void doBusinessAction(OrderState from, OrderState to) {
//            if (from == OrderState.UNPAID && to == OrderState.PAID) {
//                System.out.println("执行支付动作：扣减库存，生成支付记录");
//            } else if (from == OrderState.UNPAID && to == OrderState.CANCELLED) {
//                System.out.println("执行取消动作：释放库存");
//            } else if (from == OrderState.PAID && to == OrderState.DELIVERED) {
//                System.out.println("执行发货动作：生成物流单");
//            } else if (from == OrderState.PAID && to == OrderState.CANCELLED) {
//                System.out.println("执行退款动作：退还金额，释放库存");
//            } else if (from == OrderState.DELIVERED && to == OrderState.COMPLETED) {
//                System.out.println("执行确认收货动作：完成订单结算");
//            }
//        }
//
//        // 4. 测试：启动Spring Boot，获取状态机并触发事件
//        public static void main(String[] args) {
//            // 启动Spring Boot容器
//            var context = SpringApplication.run(SpringStateMachineDemo.class, args);
//            // 获取状态机实例
//            StateMachine<OrderState, OrderEvent> stateMachine = context.getBean(StateMachine.class);
//
//            // 启动状态机
//            stateMachine.start();
//            System.out.println("当前状态：" + stateMachine.getState().getId());
//
//            // 触发事件：支付→发货→确认收货
//            stateMachine.sendEvent(OrderEvent.PAY);
//            System.out.println("当前状态：" + stateMachine.getState().getId());
//            stateMachine.sendEvent(OrderEvent.DELIVER);
//            System.out.println("当前状态：" + stateMachine.getState().getId());
//            stateMachine.sendEvent(OrderEvent.CONFIRM);
//            System.out.println("当前状态：" + stateMachine.getState().getId());
//
//            // 触发非法事件：已完成订单取消
//            stateMachine.sendEvent(OrderEvent.CANCEL);
//
//            // 重置状态机，测试另一种流转：待支付→取消
//            stateMachine.stop();
//            stateMachine.start();
//            stateMachine.sendEvent(OrderEvent.CANCEL);
//            System.out.println("当前状态：" + stateMachine.getState().getId());
//        }
//    }
}







//
//分布式系统关键扩展（核心）
//Spring Statemachine 默认是内存状态机，要适配分布式系统，只需做 2 个关键扩展：
//状态持久化：实现StateMachinePersister接口，将状态机的状态保存到数据库（MySQL） 或分布式缓存（Redis），每次触发事件前从持久化介质加载状态，触发后更新状态；
//分布式原子性：在触发事件的方法上添加分布式锁（如 Redisson），防止分布式环境下多个线程同时修改同一个订单的状态，导致状态乱转；
//状态机工厂：使用StateMachineFactory为每个订单创建独立的状态机实例，避免状态共享。
//这部分是分布式系统的工程化细节，核心思想还是状态机的 5 大要素，只是增加了「持久化」和「分布式一致性」的保障。
//三、状态机的选型建议（根据场景选择）
//学习状态机的关键不是死记某一种实现，而是根据业务场景选择最合适的方式，以下是大厂主流的选型建议：
//简单场景（状态≤3，无扩展）：基础硬编码（快速实现，无需抽象）；
//常规业务（状态 3-10，需扩展）：枚举轻量实现（企业开发最优解，兼顾简洁和扩展）；
//复杂业务（状态≥10，转移逻辑极复杂）：状态模式（纯面向对象，高内聚低耦合）；
//分布式系统（需要持久化 / 分布式一致性 / 复杂状态）：Spring Statemachine（工业级框架，避免重复造轮子）；
//超高性能场景（如秒杀订单）：自定义轻量状态机（枚举 + Map）+ 分布式锁 + 数据库行锁（比框架更轻量，性能更高）。
//四、状态机在分布式系统中的核心落地思路
//你关注到状态机在分布式系统中的应用，核心是将「内存状态机」升级为「持久化状态机」，关键思路如下：
//状态持久化：将对象的当前状态保存到数据库 / Redis（如订单表增加order_state字段），所有状态流转都基于持久化状态，避免单机内存丢失；
//转移规则中心化：将状态转移规则在代码中硬编码（或配置化），所有节点使用相同的规则，保证分布式环境下状态流转的一致性；
//原子性保障：通过数据库行锁（for update） 或分布式锁（Redisson/ZooKeeper），保证「读取状态→判断规则→更新状态」的原子性，防止并发导致的非法状态；
//状态审计：记录每一次状态转移的日志（如订单状态流转表：order_id、from_state、to_state、event、operator、create_time），支持问题追溯和分布式事务回查；
//异常处理：状态转移失败时（如数据库异常），执行回滚或重试，保证状态的一致性（如支付成功但订单状态更新失败，需重试更新状态）。
//典型分布式场景落地：
//分布式订单：订单状态保存在 MySQL，状态流转用「枚举轻量状态机」+ 数据库行锁 + 状态流转日志；
//分布式事务（Seata）：全局事务状态（Begin/Commit/Rollback）和分支事务状态（Registered/Prepared/Committed/RolledBack）用状态机管理，状态保存在 Seata Server 的 DB 中；
//消息队列（RocketMQ）：消息状态（Prepared/Committed/Sent/Consumed）用状态机管理，状态保存在 Broker 的 DB 中，保证消息的可靠投递；
//服务治理（Nacos）：服务实例状态（UP/DOWN/STARTING/OUT_OF_SERVICE）用状态机管理，状态保存在 Nacos 集群，保证服务发现的一致性。
//五、总结
//状态机（有限状态机 FSM）的核心是一种管理「有状态对象状态流转」的通用思想，而非具体技术，关键记住 5 大要素：上下文、状态、事件、转移规则、动作。
//设计模式中的状态机：本质是状态模式，将每个状态的流转逻辑封装到独立类中，是状态机的纯面向对象实现；
//分布式系统中的状态机：是状态机思想的工程化落地，核心是「持久化状态 + 原子性流转 + 一致性保障」，解决分布式环境下的状态管理问题；
//Java 实现选型：企业开发优先用枚举 + Map + 函数式接口的轻量实现，分布式系统优先用Spring Statemachine（或自定义轻量状态机 + 持久化）；
//核心价值：消除if-else、统一状态管理、防止非法状态、提升代码可维护性和扩展性，是处理「状态流转类业务」的银弹。
