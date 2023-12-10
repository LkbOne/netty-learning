package org.example.sample;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

public class EchoHandler implements Runnable {

  final SelectionKey sk;
  final SocketChannel channel;

  private String state = "RECEIVE";

  // 这个设置过小，会爆炸
  final ByteBuffer byteBuffer = ByteBuffer.allocate(1000);
  public EchoHandler(Selector selector, SocketChannel sc) throws IOException {
    channel = sc;
    // 设置为非阻塞
    channel.configureBlocking(false);
    sk = channel.register(selector, 0);
    sk.attach(this);
    sk.interestOps(SelectionKey.OP_READ);
    selector.wakeup();
  }
  public void run() {
      try {
        if("SENDING".equals(state)) {
          channel.write(byteBuffer);
          byteBuffer.clear();
          sk.interestOps(SelectionKey.OP_READ);
          state = "RECEIVE";
        }else if("RECEIVE".equals(state)){
          int length = 0;
          while((length = channel.read(byteBuffer)) > 0) {
            System.out.println(new String(byteBuffer.array(), 0, length));
          }
          byteBuffer.flip();
          sk.interestOps(SelectionKey.OP_WRITE);
          state = "SENDING";
        }
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
  }
}
