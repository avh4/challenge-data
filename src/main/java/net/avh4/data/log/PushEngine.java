package net.avh4.data.log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PushEngine implements TransactionLogCommands {
    private final TransactionBuffer buffer;
    private final ArrayList<PushObserver> engines = new ArrayList<>();
    private boolean didFirstSync;

    public PushEngine(TransactionBuffer buffer) {
        this.buffer = buffer;
    }

    public void add(PushObserver... engines) {
        if (didFirstSync)
            throw new IllegalStateException("PushObservers must be added before the first call to sync()");
        Collections.addAll(this.engines, engines);
    }

    public void sync() {
        synchronized(this) {
            syncWrites();
            syncReads();
        }
        didFirstSync = true;
    }

    private void syncWrites() {
        buffer.flush();
    }

    private void syncReads() {
        List<Transaction> transactions = buffer.getCommitted(0);
        for (Transaction txn : transactions) {
            for (PushObserver engine : engines) {
                engine.receive(txn);
            }
        }
    }

    @Override
    public void add(String key, String value) {
        buffer.add(key, value);
    }
}
