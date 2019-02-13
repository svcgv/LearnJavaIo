package com.charjay;

import com.charjay.rpc.registry.RpcRegistry;

public class App {
    public static void main(String[] args) {
        new RpcRegistry().start();
    }
}
