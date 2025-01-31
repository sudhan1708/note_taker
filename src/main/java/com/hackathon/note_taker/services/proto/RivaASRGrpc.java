package com.hackathon.note_taker.services.proto;

import io.grpc.*;
import io.grpc.stub.AbstractStub;
import io.grpc.stub.ClientCalls;
import io.grpc.stub.StreamObserver;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.concurrent.TimeUnit;

public final class RivaASRGrpc {
    private RivaASRGrpc() {}

    public static final String SERVICE_NAME = "nvidia.riva.asr.RivaASR";

    private static final MethodDescriptor<RecognizeRequest, RecognizeResponse> METHOD_RECOGNIZE =
            MethodDescriptor.<RecognizeRequest, RecognizeResponse>newBuilder()
                    .setType(MethodDescriptor.MethodType.UNARY)
                    .setFullMethodName(generateFullMethodName(SERVICE_NAME, "Recognize"))
                    .setSampledToLocalTracing(true)
                    .setRequestMarshaller(new ProtoMarshaller<>(RecognizeRequest.class))
                    .setResponseMarshaller(new ProtoMarshaller<>(RecognizeResponse.class))
                    .build();

    private static String generateFullMethodName(String serviceName, String methodName) {
        return serviceName + "/" + methodName;
    }

    // Blocking stub
    public static final class RivaASRBlockingStub extends AbstractStub<RivaASRBlockingStub> {
        private RivaASRBlockingStub(Channel channel) {
            super(channel);
        }

        private RivaASRBlockingStub(Channel channel, CallOptions callOptions) {
            super(channel, callOptions);
        }

        @Override
        protected RivaASRBlockingStub build(Channel channel, CallOptions callOptions) {
            return new RivaASRBlockingStub(channel, callOptions);
        }

        public RecognizeResponse recognize(RecognizeRequest request) {
            return ClientCalls.blockingUnaryCall(
                    getChannel(), METHOD_RECOGNIZE, getCallOptions(), request);
        }

        // Changed method name to avoid final method conflict
        public RivaASRBlockingStub withTimeout(long timeout, TimeUnit unit) {
            return build(getChannel(), getCallOptions().withDeadlineAfter(timeout, unit));
        }
    }

    // Future stub
    public static final class RivaASRFutureStub extends AbstractStub<RivaASRFutureStub> {
        private RivaASRFutureStub(Channel channel) {
            super(channel);
        }

        private RivaASRFutureStub(Channel channel, CallOptions callOptions) {
            super(channel, callOptions);
        }

        @Override
        protected RivaASRFutureStub build(Channel channel, CallOptions callOptions) {
            return new RivaASRFutureStub(channel, callOptions);
        }

        public ListenableFuture<RecognizeResponse> recognize(RecognizeRequest request) {
            return ClientCalls.futureUnaryCall(
                    getChannel().newCall(METHOD_RECOGNIZE, getCallOptions()), request);
        }
    }

    // Async stub
    public static final class RivaASRStub extends AbstractStub<RivaASRStub> {
        private RivaASRStub(Channel channel) {
            super(channel);
        }

        private RivaASRStub(Channel channel, CallOptions callOptions) {
            super(channel, callOptions);
        }

        @Override
        protected RivaASRStub build(Channel channel, CallOptions callOptions) {
            return new RivaASRStub(channel, callOptions);
        }

        public void recognize(RecognizeRequest request, StreamObserver<RecognizeResponse> responseObserver) {
            ClientCalls.asyncUnaryCall(
                    getChannel().newCall(METHOD_RECOGNIZE, getCallOptions()),
                    request,
                    responseObserver);
        }
    }

    // Factory methods for stubs
    public static RivaASRBlockingStub newBlockingStub(Channel channel) {
        return new RivaASRBlockingStub(channel);
    }

    public static RivaASRFutureStub newFutureStub(Channel channel) {
        return new RivaASRFutureStub(channel);
    }

    public static RivaASRStub newStub(Channel channel) {
        return new RivaASRStub(channel);
    }
}