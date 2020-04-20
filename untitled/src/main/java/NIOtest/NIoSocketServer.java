package NIOtest;

import org.junit.Test;

import java.io.IOException;

import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class NIoSocketServer {
    @Test
    public void  run() throws IOException {

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.socket().bind( new InetSocketAddress("localhost",8196));
        serverSocketChannel.configureBlocking(false);

        Selector selector = Selector.open();
        serverSocketChannel.register(selector,SelectionKey.OP_ACCEPT);

        ByteBuffer readBuffer = ByteBuffer.allocate(1024);
        ByteBuffer writeBuffer = ByteBuffer.allocate(1024);

        writeBuffer.put("helllo i am server".getBytes("UTF-8"));
        writeBuffer.flip();

        while (true) {
            //
            int nReady = selector.select();
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keys.iterator();

            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                iterator.remove();

                if(selectionKey.isAcceptable()) {
                    SocketChannel socketChannel = serverSocketChannel.accept();
                    socketChannel.configureBlocking(false);
                    socketChannel.register(selector,SelectionKey.OP_READ);
                }
                else if(selectionKey.isReadable()) {
                     SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                     readBuffer.clear();
                      int legth = socketChannel.read(readBuffer);

                     readBuffer.flip();
                     System.out.println("recive " + new String(readBuffer.array(),0,legth,"UTF-8"));
                     selectionKey.interestOps(SelectionKey.OP_WRITE);
                }
                else if(selectionKey.isWritable()) {
                     SocketChannel socketChannel = (SocketChannel) selectionKey.channel();
                     writeBuffer.clear();
                     socketChannel.write(writeBuffer);
                     selectionKey.interestOps(SelectionKey.OP_READ);
                }
            }
        }
    }
}
