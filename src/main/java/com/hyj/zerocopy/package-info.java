package com.hyj.zerocopy;
/**
 * 零拷贝
 * 针对linux和Unix系统，在网络IO中，传统的socket编程会进行两次拷贝，
 * 例如读取数据：
 * 第一次是从硬盘读取到统核心kernel的内存缓冲区
 * 第二次将数据从系统核心kernel的内存缓冲区copy到用户空间userSpace的内存缓冲区
 * 而NIO则不同：
 * 1、从硬盘读取到统核心kernel的内存缓冲区后，会将该缓冲区数据直接写出到客户端的socket
 * 2、或者直接从硬盘读取数据到客户端的socket
 *
 *
 * */
