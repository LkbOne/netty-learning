package org.example.sample.multi;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

public class MultiThreadEchoServerReactor {

   final ServerSocketChannel serverSocket;

   AtomicInteger next = new AtomicInteger(0);

//   Selector[] workSelectors = new Selector[2];
   Selector workSelector;

   Selector bossSelector;

   Reactor[] reactors;

   Reactor bossReactor;

   Reactor workReactor;

   MultiThreadEchoServerReactor() throws IOException {
//      workSelectors[0] = Selector.open();
//      workSelectors[1] = Selector.open();
      bossSelector = Selector.open();
      workSelector = Selector.open();
      serverSocket = ServerSocketChannel.open();

      serverSocket.configureBlocking(false);
      InetSocketAddress address = new InetSocketAddress(InetAddress.getLocalHost(), 8100);
      serverSocket.socket().bind(address);
      SelectionKey sk = serverSocket.register(bossSelector, SelectionKey.OP_ACCEPT);
      sk.attach(new AcceptHandler());

      bossReactor = new Reactor(bossSelector);
      workReactor = new Reactor(workSelector);
   }


   public void startService() {
      new Thread(bossReactor).start();
      new Thread(workReactor).start();
//      new Thread(reactors[1]).start();
   }

   class AcceptHandler implements Runnable{

      public void run() {
         try {
            SocketChannel channel = serverSocket.accept();
            System.out.println("receive a new connection");
            if(channel != null) {
//               int index = next.get();
//               System.out.println("选择器的编号：" + index);
//               Selector selector = workSelectors[index];
//               new MultiThreadEchoHandler(selector, channel);
               new MultiThreadEchoHandler(workSelector, channel);
            }
         } catch (IOException e) {
            throw new RuntimeException(e);
         }
//         if (next.incrementAndGet() == workSelectors.length) {
//            next.set(0);
//         }
      }
   }

   public class Reactor implements Runnable {

      final Selector selector;

      public Reactor(Selector selector) {
         this.selector = selector;
      }

      public void run() {
         try{
            while(!Thread.interrupted()) {
               selector.select(1000);
               Set<SelectionKey> selectionKeySet = selector.selectedKeys();
               if(null == selectionKeySet || selectionKeySet.size() == 0){
                  continue;
               }
               for (SelectionKey sk : selectionKeySet) {
                  dispatch(sk);
               }
               selectionKeySet.clear();
            }
         } catch (IOException e) {
            throw new RuntimeException(e);
         }
      }

      private void dispatch(SelectionKey sk) {
         Runnable handler = (Runnable) sk.attachment();
         if(handler != null){
            handler.run();
         }
      }
   }

   public static void main(String[] args) throws IOException {
      new MultiThreadEchoServerReactor().startService();
   }
}
