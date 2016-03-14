# jni_thread_pinning

 /proc/cpuinfo file contains info about the machine cpu architecture:

* Chip: The physical processor you can hold in your hands. Some people call it socket but it can generate confusion with network programming. Different chips usually share the same L3 cache.
* Core: A chip can have multiple cores (multi-core processor). Each core usually has its own L1 cache, which is the fastest in-chip cache. Different cores usually share the same L2 cache.
* Processor: A *logical* processor which is the smallest processing unit that can execute only one thread at a time. If the chip supports hyper-threading, then each core will have two logical processors. Otherwise each core will only be able to execute one thread through a single logical processor.
