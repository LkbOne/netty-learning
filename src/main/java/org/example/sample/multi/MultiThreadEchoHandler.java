package org.example.sample.multi;

import java.io.IOException;
import java.net.StandardSocketOptions;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadEchoHandler implements Runnable {
  final SocketChannel channel;
  final SelectionKey sk;
  final ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
  static final int RECIEVING = 0, SENDING = 1;
  int state = RECIEVING;
  static ExecutorService pool = Executors.newFixedThreadPool(4);

  MultiThreadEchoHandler(Selector selector, SocketChannel c) throws
      IOException {
    channel = c;
    channel.configureBlocking(false);
    channel.setOption(StandardSocketOptions.TCP_NODELAY, true);
    sk = channel.register(selector, 0);
    sk.attach(this);
    sk.interestOps(SelectionKey.OP_READ);
    selector.wakeup();
    System.out.println("new connection registered");
  }

  public void run() {
    pool.execute(new AsyncTask());
  }

  class AsyncTask implements Runnable {
    public void run() {
      MultiThreadEchoHandler.this.asyncRun();
    }
  }

  public synchronized void asyncRun() {
    try {
      if (state == SENDING) {
//写入通道
        channel.write(byteBuffer);
//写完后,准备开始从通道读,byteBuffer 切换成写模式
        byteBuffer.clear();
//写完后,注册 read 就绪事件
        sk.interestOps(SelectionKey.OP_READ);
//写完后,进入接收的状态
        state = RECIEVING;
      } else if (state == RECIEVING) {
//从通道读
        int length = 0;
        while ((length = channel.read(byteBuffer)) > 0) {
          System.out.println(new String(byteBuffer.array(), 0, length));
        }
//读完后，准备开始写入通道,byteBuffer 切换成读模式
        byteBuffer.flip();
//读完后，注册 write 就绪事件
        sk.interestOps(SelectionKey.OP_WRITE);
//读完后,进入发送的状态
        state = SENDING;
      }
//处理结束了, 这里不能关闭 select key，需要重复使用
//sk.cancel();
    } catch (IOException ex) {
      ex.printStackTrace();
    }
  }
}
