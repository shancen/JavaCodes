#### GC日志分析总结

##### 1.jvm参数（GC相关）

```JAVA
1.-XX:+PrintGCDetails：发生垃圾收集时打印详细回收日志

2.-XX:+PrintGCDateStamps：输出GC时的时间戳

3.-Xloggc：日志文件的输出路径 例 -Xloggc:./logs/gc.log

4.-XX:+UseSerialGC：开启Serial收集器

5.-XX:+UseParNewGC： 开启ParNew收集器

6.-XX:+UseParallelGC：Parallel Scavenge收集器

7.-XX:+UseSerialOldGC：Serial Old收集器

8.-XX:+UseConcMarkSweepGC：CMS收集器

9.-XX:+UseG1GC：G1 收集器
    
10.-XX:UseAdaptiveSizePolicy:自适应参数
```

##### 2.UseSerialGC（串行GC）

##### 	java -XX:+PrintGCDetails -XX:+PrintGCDateStamps  -Xmx1g   -Xms1g -XX:+UseSerialGC com.lzx.jvm.gc.GCLogAnalysis

```java
  相同代码,相同堆内存设置情况下，年轻代GC平均STW时长为0.04s,未触发fullGC
```

#####     java -XX:+PrintGCDetails -XX:+PrintGCDateStamps  -Xmx512M   -Xms512M -XX:+UseSerialGC com.lzx.jvm.gc.GCLogAnalysis

```java
   将堆内存调小，年轻代GC平均STW时长为0.02s，但GC频率更高了，fullGC平均STW时长为0.04s，到后面堆内存一直不够，一直触发fullGC
```

##### 3.UseParallelGC 并行GC（ Parallel Scavenge + Parallel Old  JDK 默认）

​    之前有Parallel Scavenge +Serial Old（吞吐量低）

##### 	java -XX:+PrintGCDetails -XX:+PrintGCDateStamps  -Xmx1g   -Xms1g -XX:+UseParallelGC com.lzx.jvm.gc.GCLogAnalysis

```JAVA
  PSYoungGen 年轻代GC几次后，老年代容量快满了，下一次年轻代GC后，老年代容量不够，分配内存失败是，触发fullGC
  相同代码,相同堆内存设置情况下，年轻代GC平均时长为0.1s，fullGC平均时长为0.04s 
```

##### 4.UseConcMarkSweepGC（CMS GC  并发）( ParNew  + CMS)(会产生内存碎片)

##### 		java -XX:+PrintGCDetails -XX:+PrintGCDateStamps  -Xmx1g   -Xms1g -XX:+UseConcMarkSweepGC com.lzx.jvm.gc.GCLogAnalysis

```JAVA
	老年代只有2阶段会触发STW，STW时间为0.001s
    开启CMS收集器,老年代收集多线程,采用标记清除算法，优点是可以并行并发处理，注重停顿时间，用户体验更快，缺点是产出内存碎片，吞吐量会下降
```

##### 5.UseG1GC(G1 GC **JDK9以及以上**)

##### 	java -XX:+PrintGCDetails -XX:+PrintGCDateStamps  -Xmx1g   -Xms1g -XX:+UseG1GC com.lzx.jvm.gc.GCLogAnalysis

```java
	1.将 STW 停顿的时间和分布，变成可预期且可配置的
    2.每次回收，回收一部分，增量回收垃圾
    3.重要的是不在划分分代了，也可以说所有区域都能变成年轻代或者老年代，通常分为2048个region区
    4.可以通过JVM启动参数来设置STW暂停时间
```

##### 6.总结

​	1.多核情况下，UseParallelGC相比于UseSerialGC，STW时间更短,UseConcMarkSweepGC 相比于前2种延迟也是更短，并且清除垃圾也会有业务线程进行工作,但是CMS会产出碎片垃圾,而G1GC停顿时间更短，并且可以通过参数控制停顿时间,注意G1特殊情况下会退化为串行GC(如果有时候，垃圾回收停顿时间突然变长了，可以从这方面考虑)

​	2.堆内存设置大小问题，明显可以看出,当设置1g比设置512m时，触发的fullGC更少了，并且GC频率也更低一些,所以堆内存大小设置合理能够有效提升系统性能

​	3.Xmx与Xms设置不相等会怎样？

​		当不设置xms时，明显发现fullGC提前了，并且触发fullGC频率变高了，因为初始堆内存太小，一会就满了，导致fullGC频率变高，堆内存开始动态扩容，随着堆内存慢慢扩容到最大堆内存是，fullGC收集频率慢慢趋于稳定，所以一般建议将生产Xms(初始堆内存)与Xmx（最大堆内存）设置一样大

​	4.GC如何选择

1. 如果系统考虑吞吐优先，CPU 资源都用来最大程度处理业务，用 Parallel GC（一般使用这个）； 

2. 如果系统考虑低延迟有限，每次 GC 时间尽量短，用 CMS GC；

3. 如果系统内存堆较大，同时希望整体来看平均 GC 时间可控，使用 G1 GC

4. 对于内存大小的考量： 

   1. 一般 4G 以上，算是比较大，用 G1 的性价比较高。
   2.  一般超过 8G，比如 16G-64G 内存，非常推荐使用 G1 GC。

   

