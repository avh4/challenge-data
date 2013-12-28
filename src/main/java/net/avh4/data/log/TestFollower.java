package net.avh4.data.log;

import java.util.ArrayList;

/**
 * A TransactionFollower that simply records all the transactions it receives.  This will generally only be useful for
 * testing new FollowerEngines as you will normally want to write TransactionFollowers that maintain internal state
 * that is specific to your domain.
 */
public class TestFollower implements TransactionFollower<TestFollower> {
    public final ArrayList<Transaction> transactions;

    public TestFollower() {
        this(new ArrayList<Transaction>());
    }

    private TestFollower(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public void process(Transaction txn) {
        transactions.add(txn);
    }

    @Override
    public TestFollower fork() {
        return new TestFollower(new ArrayList<>(transactions));
    }
}
