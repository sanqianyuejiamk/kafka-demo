# Apache Kafka 原理

### 基本角色
1. Broker - Kafka集群包含一个或多个服务器，这种服务器被称为broker
2. Producer - 消息生产者，负责发布消息到Kafka broker
3. Consumer - 消息消费者，向Kafka broker读取消息的客户端

### kafka工作原理
<img src="http://dl2.iteye.com/upload/attachment/0106/2420/7ace343e-3249-36b3-8579-1deb9fbb0809.png">

① 每个partition会创建3个备份replica,并分配到broker集群中； --replication-factor 3
② 用zookeeper来管理，consumer、producer、broker的活动状态；
③ 分配的每个备份replica的id和broker的id保持一致；
④ 对每个partition，会选择一个broker作为集群的leader； 
