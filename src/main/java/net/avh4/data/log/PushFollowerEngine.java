package net.avh4.data.log;

public class PushFollowerEngine<F extends TransactionFollower<F>> implements FollowerEngine<F>, PushObserver {
    private final F follower;

    public PushFollowerEngine(F follower) {
        this.follower = follower;
    }

    @Override
    public F result() {
        return follower;
    }

    @Override
    public void receive(Transaction txn) {
        follower.process(txn);
    }

    @Override
    public void save() {

    }

    @Override
    public void restore() {

    }
}
