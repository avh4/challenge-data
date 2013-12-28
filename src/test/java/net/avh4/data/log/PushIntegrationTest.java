package net.avh4.data.log;

import org.junit.Test;

import java.util.List;

import static org.fest.assertions.Assertions.assertThat;

public class PushIntegrationTest {
    @Test
    public void test() {
        CountedTransientTransactionLog master = new CountedTransientTransactionLog();
        master.add("/list/LA/items/IA/name", "Get milk");
        master.add("/list/LA/items/IB/name", "Feed dog");
        master.add("/list/LA/order", "[\"/list/LA/items/IA\",\"/list/LA/items/IB\"]");


        TransactionBuffer buffer1 = new TransactionBuffer(master, master);
        PushFollowerEngine<TestFollower> mobile1Model = new PushFollowerEngine<>(new TestFollower());
        PushEngine mobile1 = new PushEngine(buffer1);
        mobile1.add(mobile1Model);
        mobile1.sync();

        TransactionBuffer buffer2 = new TransactionBuffer(master, master);
        PushFollowerEngine<TestFollower> mobile2Model = new PushFollowerEngine<>(new TestFollower());
        PushEngine mobile2 = new PushEngine(buffer2);
        mobile2.add(mobile2Model);
        mobile2.sync();

        // ============================================================
        mobile1.add("/list/LA/order", "[\"/list/LA/items/IB\",\"/list/LA/items/IA\"]");
        mobile1.add("/list/LA/items/IA/due", "Tuesday");

        mobile2.add("/list/LA/items/IB/name", "Feed the dog");

        assertThat(mobile2Model.result().transactions).containsExactly(
                new Transaction(0, "/list/LA/items/IA/name", "Get milk"),
                new Transaction(1, "/list/LA/items/IB/name", "Feed dog"),
                new Transaction(2, "/list/LA/order", "[\"/list/LA/items/IA\",\"/list/LA/items/IB\"]"),
                new Transaction(0, "/list/LA/items/IB/name", "Feed the dog")
        );

        // ============================================================
        mobile2.sync();
//        assertThat(mobile2.getAll()).containsExactly(
//                new Transaction("LS", 0, "/list/LA/items/IA/name", "Get milk"),
//                new Transaction("LS", 1, "/list/LA/items/IB/name", "Feed dog"),
//                new Transaction("LS", 2, "/list/LA/order", "[\"/list/LA/items/IA\",\"/list/LA/items/IB\"]"),
//                new Transaction("LS", 3, "/list/LA/items/IB/name", "Feed the dog")
//        );

        // ============================================================
        mobile2.add("/list/LA/items/IC/name", "Fill oil");
        mobile2.add("/list/LA/order", "[\"/list/LA/items/IC\",\"/list/LA/items/IB\"]");

//        assertThat(mobile1.getAll()).containsExactly(
//                new Transaction("LS", 0, "/list/LA/items/IA/name", "Get milk"),
//                new Transaction("LS", 1, "/list/LA/items/IB/name", "Feed dog"),
//                new Transaction("LS", 2, "/list/LA/order", "[\"/list/LA/items/IA\",\"/list/LA/items/IB\"]"),
//                new Transaction("LM1A", 0, "/list/LA/order", "[\"/list/LA/items/IB\",\"/list/LA/items/IA\"]"),
//                new Transaction("LM1A", 1, "/list/LA/items/IA/due", "Tuesday")
//        );
        // ============================================================
        mobile1.sync();
//        assertThat(mobile2.getAll()).containsExactly(
//                new Transaction("LS", 0, "/list/LA/items/IA/name", "Get milk"),
//                new Transaction("LS", 1, "/list/LA/items/IB/name", "Feed dog"),
//                new Transaction("LS", 2, "/list/LA/order", "[\"/list/LA/items/IA\",\"/list/LA/items/IB\"]"),
//                new Transaction("LS", 3, "/list/LA/items/IB/name", "Feed the dog"),
//                new Transaction("LS", 4, "/list/LA/order", "[\"/list/LA/items/IB\",\"/list/LA/items/IA\"]"),
//                new Transaction("LS", 5, "/list/LA/items/IA/due", "Tuesday"),
//                new Transaction("LM2B", 0, "/list/LA/items/IC/name", "Fill oil"),
//                new Transaction("LM2B", 1, "/list/LA/order", "[\"/list/LA/items/IC\",\"/list/LA/items/IB\"]")
//        );

        // ============================================================
        mobile2.sync();

        assertThat(master.count).isEqualTo(5);
        assertThat(master.get(0)).containsExactly(
                new Transaction(1, "/list/LA/items/IA/name", "Get milk"),
                new Transaction(2, "/list/LA/items/IB/name", "Feed dog"),
                new Transaction(3, "/list/LA/order", "[\"/list/LA/items/IA\",\"/list/LA/items/IB\"]"),
                new Transaction(4, "/list/LA/items/IB/name", "Feed the dog"),
                new Transaction(5, "/list/LA/order", "[\"/list/LA/items/IB\",\"/list/LA/items/IA\"]"),
                new Transaction(6, "/list/LA/items/IA/due", "Tuesday"),
                new Transaction(7, "/list/LA/items/IC/name", "Fill oil"),
                new Transaction(8, "/list/LA/order", "[\"/list/LA/items/IC\",\"/list/LA/items/IB\"]")
        );
//        assertThat(mobile1.getAll()).containsExactly(
//                new Transaction("LS", 0, "/list/LA/items/IA/name", "Get milk"),
//                new Transaction("LS", 1, "/list/LA/items/IB/name", "Feed dog"),
//                new Transaction("LS", 2, "/list/LA/order", "[\"/list/LA/items/IA\",\"/list/LA/items/IB\"]"),
//                new Transaction("LS", 3, "/list/LA/items/IB/name", "Feed the dog"),
//                new Transaction("LS", 4, "/list/LA/order", "[\"/list/LA/items/IB\",\"/list/LA/items/IA\"]"),
//                new Transaction("LS", 5, "/list/LA/items/IA/due", "Tuesday"),
//                new Transaction("LS", 6, "/list/LA/items/IC/name", "Fill oil"),
//                new Transaction("LS", 7, "/list/LA/order", "[\"/list/LA/items/IC\",\"/list/LA/items/IB\"]")
//        );
//        assertThat(mobile2.getAll()).containsExactly(
//                new Transaction("LS", 0, "/list/LA/items/IA/name", "Get milk"),
//                new Transaction("LS", 1, "/list/LA/items/IB/name", "Feed dog"),
//                new Transaction("LS", 2, "/list/LA/order", "[\"/list/LA/items/IA\",\"/list/LA/items/IB\"]"),
//                new Transaction("LS", 3, "/list/LA/items/IB/name", "Feed the dog"),
//                new Transaction("LS", 4, "/list/LA/order", "[\"/list/LA/items/IB\",\"/list/LA/items/IA\"]"),
//                new Transaction("LS", 5, "/list/LA/items/IA/due", "Tuesday"),
//                new Transaction("LS", 6, "/list/LA/items/IC/name", "Fill oil"),
//                new Transaction("LS", 7, "/list/LA/order", "[\"/list/LA/items/IC\",\"/list/LA/items/IB\"]")
//        );
    }

    private static class CountedTransientTransactionLog extends TransientTransactionLog {
        public int count;

        @Override
        public List<Transaction> get(int startingIndex) {
            count++;
            return super.get(startingIndex);
        }
    }
}
