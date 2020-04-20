package NIOtest;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NioSocketClient2 {
    @Test
    public void run() throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("localhost",8196));
        socketChannel.configureBlocking(false);

        ByteBuffer readByteBuffer = ByteBuffer.allocate(1024);
        ByteBuffer writeByteBuffer = ByteBuffer.allocate(1024);
        //向服务端发送消息
        writeByteBuffer.put("hello i am client2".getBytes("UTF-8"));
        writeByteBuffer.flip();
        socketChannel.write(writeByteBuffer);
        //客户端保持接收消息
        while (true) {
            readByteBuffer.clear();
            int legth = socketChannel.read(readByteBuffer);
            //如果收到服务端的消息输出
            if(legth > 0){
                System.out.println("recive: "+ new String(readByteBuffer.array(),0,legth,"UTF-8"));
            }
        }
    }
}

