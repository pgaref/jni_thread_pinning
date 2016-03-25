# jni_thread_pinning

 /proc/cpuinfo file contains info about the machine cpu architecture:

* Chip: The physical processor you can hold in your hands. Some people call it socket but it can generate confusion with network programming. Different chips usually share the same L3 cache.
* Core: A chip can have multiple cores (multi-core processor). Each core usually has its own L1 cache, which is the fastest in-chip cache. Different cores usually share the same L2 cache.
* Processor: A *logical* processor which is the smallest processing unit that can execute only one thread at a time. If the chip supports hyper-threading, then each core will have two logical processors. Otherwise each core will only be able to execute one thread through a single logical processor.

## Experimental results

Run on CPU:  Intel(R) Core(TM) i7-4770 CPU @ 3.40GHz with
* 4 physical cores
* 4 Hyper Threads
* 8 usable processors in total

### Small tasks
![Thead Affinity Small Tasks](https://github.com/pgaref/jni_thread_pinning/blob/master/graphs/thread-affinity-sTasks.jpg)

### Medium tasks
![Thead Affinity Medium Tasks](https://github.com/pgaref/jni_thread_pinning/blob/master/graphs/thread-affinity-mTasks.jpg)

### Large tasks
![Thead Affinity Large Tasks](https://github.com/pgaref/jni_thread_pinning/blob/master/graphs/thread-affinity-lTasks.jpg)


## References


* [Reducing system jitter-part1](http://epickrram.blogspot.co.uk/2015/09/reducing-system-jitter.html)
* [Reducing system jitter-part2](http://epickrram.blogspot.co.uk/2015/11/reducing-system-jitter-part-2.html)
* [HdrHistogram + jHiccup](http://psy-lob-saw.blogspot.com/2015/02/hdrhistogram-better-latency-capture.html)
