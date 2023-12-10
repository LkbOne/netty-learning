package org.example.sample;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class EchoServerReactor implements Runnable {
  Selector selector;
  ServerSocketChannel serverSocket;

  public EchoServerReactor() throws IOException {
    selector = Selector.open();
    serverSocket = ServerSocketChannel.open();
    serverSocket.socket().bind(new InetSocketAddress(8000));
    serverSocket.configureBlocking(false);
    SelectionKey sk = serverSocket.register(selector, SelectionKey.OP_ACCEPT);
    sk.attach(new AcceptorHandler());
  }


  public void run() {
    try{
      while(!Thread.interrupted()) {
        selector.select(1000);
        Set<SelectionKey> selected = selector.selectedKeys();
        Iterator<SelectionKey> it = selected.iterator();
        while(it.hasNext()) {
          SelectionKey sk = it.next();
          dispatch(sk);
        }
        selected.clear();
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void dispatch(SelectionKey sk){
    Runnable handler = (Runnable) (sk.attachment());
    if(handler != null){
      handler.run();
    }
  }


  public class AcceptorHandler implements Runnable {

    public void run() {
      try {
        SocketChannel channel = serverSocket.accept();
        if(channel != null){
          new EchoHandler(selector, channel);
        }
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  public static void main(String[] args) throws IOException {
    new Thread(new EchoServerReactor()).start();
  }
}
