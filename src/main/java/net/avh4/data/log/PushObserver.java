package net.avh4.data.log;

public interface PushObserver {
    public void receive(Transaction txn);

    public void save();

    public void restore();
}
