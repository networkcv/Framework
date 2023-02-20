package com.lwj._11_netty_protobuf;

import com.google.common.collect.Lists;
import com.google.protobuf.InvalidProtocolBufferException;

/**
 * Date: 2022/1/20
 * <p>
 * Description:
 *
 * @author liuWangjie
 */
public class SubscribeProtoTest {
    private static byte[] encode(SubscribeProto.SubscribeReq req) {
        return req.toByteArray();
    }

    private static SubscribeProto.SubscribeReq decode(byte[] body) throws InvalidProtocolBufferException {
        return SubscribeProto.SubscribeReq.parseFrom(body);
    }

    private static SubscribeProto.SubscribeReq createReq() {
        return SubscribeProto.SubscribeReq.newBuilder()
                .setId(1)
                .setName("jack")
                .setProductName("iphone13")
                .addAllAddress(Lists.newArrayList("XiAN", "HangZhou"))
                .build();
    }

    public static void main(String[] args) throws InvalidProtocolBufferException {
        SubscribeProto.SubscribeReq req = createReq();
        System.out.println("before encode " + req.toString());
        SubscribeProto.SubscribeReq req2 = decode(encode(req));
        System.out.println("after decode " + req2.toString());
        System.out.println("equals " + req.equals(req2));
    }

}