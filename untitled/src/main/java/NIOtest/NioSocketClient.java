package NIOtest;

import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class NioSocketClient {
    @Test
    public void run() throws IOException {
        SocketChannel socketChannel = SocketChannel.open();
        socketChannel.connect(new InetSocketAddress("127.0.0.1",8196));

        ByteBuffer readByteBuffer = ByteBuffer.allocate(1024);
        ByteBuffer writeByteBuffer = ByteBuffer.allocate(1024);

        writeByteBuffer.put("hello".getBytes("UTF-8"));
        writeByteBuffer.flip();

        while (true) {
            writeByteBuffer.rewind();
            socketChannel.write(writeByteBuffer);
            readByteBuffer.clear();
            int legth = socketChannel.read(readByteBuffer);
            System.out.println("recive "+ new String(readByteBuffer.array(),0,legth,"UTF-8"));
        }
    }
}
