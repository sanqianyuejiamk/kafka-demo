# Apache Kafka

### 基本角色
1. Broker - Kafka集群包含一个或多个服务器，这种服务器被称为broker
2. Producer - 消息生产者，负责发布消息到Kafka broker
3. Consumer - 消息消费者，向Kafka broker读取消息的客户端

### kafka专用术语
1. Topic - 每条发布到Kafka集群的消息都有一个类别，这个类别被称为Topic
2. Partition - Partition是物理上的概念，每个Topic包含一个或多个Partition
3. Consumer Group - 每个Consumer属于一个特定的Consumer Group（可为每个Consumer指定group name，若不指定group name则属于默认的group）

### kafka工作原理
<img src="http://dl2.iteye.com/upload/attachment/0106/2420/7ace343e-3249-36b3-8579-1deb9fbb0809.png">

① 每个partition会创建3个备份replica,并分配到broker集群中； --replication-factor 3<br>
② 用zookeeper来管理，consumer、producer、broker的活动状态；<br>
③ 分配的每个备份replica的id和broker的id保持一致；<br>
④ 对每个partition，会选择一个broker作为集群的leader；<br> 

### kafka-manager控制台
<img src="http://imglf5.nosdn0.126.net/img/dFJTKzkyUDNhSjg0ZDc5aWk5RHB5UGFpU1dYVEZJWlA2dHBrM2dPcDkydGxodEU5bk44QkJnPT0.png">

### 参考
```
https://www.ibm.com/developerworks/cn/opensource/os-cn-kafka/index.html
```
