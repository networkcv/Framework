package com.lwj.netty._6_codec2;


import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Random;

public class NettyClientHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        //在向服务器端发送POJO, 随机生成
        int random=new Random().nextInt(3);
        MyDataInfo.MyMessage myMessage = null;
        if (0 == random) {

            myMessage = MyDataInfo.MyMessage.newBuilder().
                    setDataType(MyDataInfo.MyMessage.DataType.StudentType).
                    setStudent(MyDataInfo.Student.newBuilder().
                            setName("玉麒麟 卢俊义").setId(11).build())
                    .build();


        } else {
            myMessage = MyDataInfo.MyMessage.newBuilder().
                    setDataType(MyDataInfo.MyMessage.DataType.WorkerType).
                    setWorker(MyDataInfo.Worker.newBuilder().
                            setName("李师傅").setAge(19).build())
                    .build();
        }

        ctx.writeAndFlush(myMessage);
    }

}

