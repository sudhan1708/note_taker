package com.hackathon.note_taker.services.proto;

import io.grpc.MethodDescriptor;

import java.io.*;

class ProtoMarshaller<T> implements MethodDescriptor.Marshaller<T> {
    private Class<T> clazz;

    ProtoMarshaller(Class<T> clazz) {
        this.clazz = clazz;
    }

    @Override
    public InputStream stream(T value) {
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(baos);
            oos.writeObject(value);
            return new ByteArrayInputStream(baos.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public T parse(InputStream stream) {
        try {
            ObjectInputStream ois = new ObjectInputStream(stream);
            return clazz.cast(ois.readObject());
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
