package com.example.lelik.rp5;

class Stopwatch {
    private long startTime;

    static Stopwatch startNew() {
        Stopwatch result = new Stopwatch();
        result.start();

        return result;
    }

    private void start() {
        startTime = System.currentTimeMillis();
    }

    String getElapsed() {
        return Long.toString(System.currentTimeMillis() - startTime);
    }

    String getElapsedAndRestart() {
        String result = Long.toString(System.currentTimeMillis() - startTime);
        start();

        return result;
    }
}
