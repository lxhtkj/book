package codec.probuf;

import com.google.protobuf.InvalidProtocolBufferException;

import java.util.ArrayList;
import java.util.List;

public class TestSubscribeReqProto {

    public static void main(String[] args) throws InvalidProtocolBufferException {
        SubscribeReqProto.SubscribeReq.Builder builderReq = SubscribeReqProto.SubscribeReq.newBuilder();
        builderReq.setSubReqID(1);
        builderReq.setUserName("ALX");
        builderReq.setProductName("HELLO");
        List<String> addList = new ArrayList<>();
        addList.add("AAA");
        addList.add("BBB");
        addList.add("CCC");
        builderReq.addAllAddress(addList);
        SubscribeReqProto.SubscribeReq req = builderReq.build();
        System.out.println("编解码之前结果：" + req.toString());
        SubscribeReqProto.SubscribeReq reqA = SubscribeReqProto.SubscribeReq.parseFrom(req.toByteArray());
        System.out.println("编解码之后结果：" + reqA);
        System.out.println("对比结果：" + req.equals(reqA));
        System.out.println("======================");
        SubscribeRespProto.SubscribeResp.Builder builderResp = SubscribeRespProto.SubscribeResp.newBuilder();
        builderResp.setSubReqID(222);
        builderResp.setRespCode(0);
        builderResp.setDesc("OK!");
        SubscribeRespProto.SubscribeResp resp = builderResp.build();
        System.out.println("编解码之前结果：" + resp.toString());
        SubscribeRespProto.SubscribeResp resp1 = SubscribeRespProto.SubscribeResp.parseFrom(resp.toByteArray());
        System.out.println("编解码之后结果：" + resp1);
        System.out.println("对比结果：" + resp.equals(resp1));
    }


}