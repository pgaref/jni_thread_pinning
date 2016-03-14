CLASS_PATH = ../target/classes
vpath %.class $(CLASS_PATH)

# GCC Compiler
CC = gcc
# GCC flags
CFL_BASE = -W -Wall -DWARNING -fpic
CFL_COMMON = $(CFL_BASE) -g
CFLAGS = $(CFL_COMMON)

# Try and guess Find Java path
JAVA = $(shell which java|xargs readlink -f|xargs dirname|xargs dirname|xargs dirname)
$(info Discovered JAVA_PATH="$(JAVA)")
# Java Dependency - MacOS and Linux 
ifeq (Darwin,$(shell uname -s))
	CFLAGS += -I/Library/Java/JavaVirtualMachines/jdk1.8.0_45.jdk/Contents/Home/include
	CFLAGS += -I/Library/Java/JavaVirtualMachines/jdk1.8.0_45.jdk/Contents/Home/include/darwin
else
	CFLAGS += -I$(JAVA)/include
	CFLAGS += -I$(JAVA)/include/linux
endif

CFLAGS += -I/usr/include -D_GNU_SOURCE


# Targets

all: libCPU.so

libCPU.so: CPU.o
	$(CC) -shared -o libCPU.so CPU.o

CPU.o: CPU.c uk_ac_imperial_lsds_thread_affinity_TheCPU.h
	$(CC) $(CFLAGS) -c $< -o $@

uk_ac_imperial_lsds_seep_thread_affinity_TheCPU.h:
	javah -classpath $(CLASS_PATH) uk.ac.imperial.lsds.thread.affinity.TheCPU

clean:
	rm -f *.o *.so
# TODO: Test CPU
test: libCPU.so
	$(CC) $(CFLAGS) test.c -o test.exe $(LIBS) $(CLIB)
