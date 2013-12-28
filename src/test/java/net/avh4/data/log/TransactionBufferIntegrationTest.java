package net.avh4.data.log;

import org.junit.Test;

import static org.fest.assertions.Assertions.assertThat;

public class TransactionBufferIntegrationTest {
    @Test
    public void slaveShouldHaveMastersInitialContents() {
        TransientTransactionLog master = new TransientTransactionLog();
        master.add("/list/LA/items/IA/name", "Get milk");
        master.add("/list/LA/items/IB/name", "Feed dog");
        master.add("/list/LA/order", "[\"/list/LA/items/IA\",\"/list/LA/items/IB\"]");

        TransactionBuffer mobile1 = new TransactionBuffer(master, master);
        TransactionBuffer mobile2 = new TransactionBuffer(master, master);

        mobile1.add("/list/LA/order", "[\"/list/LA/items/IB\",\"/list/LA/items/IA\"]");
        mobile1.add("/list/LA/items/IA/due", "Tuesday");

        mobile2.add("/list/LA/items/IB/name", "Feed the dog");

//        assertThat(mobile2.getAll()).containsExactly(
//                new Transaction("LS", 0, "/list/LA/items/IA/name", "Get milk"),
//                new Transaction("LS", 1, "/list/LA/items/IB/name", "Feed dog"),
//                new Transaction("LS", 2, "/list/LA/order", "[\"/list/LA/items/IA\",\"/list/LA/items/IB\"]"),
//                new Transaction("LM2A", 0, "/list/LA/items/IB/name", "Feed the dog")
//        );
        mobile2.flush();
//        assertThat(mobile2.getAll()).containsExactly(
//                new Transaction("LS", 0, "/list/LA/items/IA/name", "Get milk"),
//                new Transaction("LS", 1, "/list/LA/items/IB/name", "Feed dog"),
//                new Transaction("LS", 2, "/list/LA/order", "[\"/list/LA/items/IA\",\"/list/LA/items/IB\"]"),
//                new Transaction("LS", 3, "/list/LA/items/IB/name", "Feed the dog")
//        );

        mobile2.add("/list/LA/items/IC/name", "Fill oil");
        mobile2.add("/list/LA/order", "[\"/list/LA/items/IC\",\"/list/LA/items/IB\"]");

//        assertThat(mobile1.getAll()).containsExactly(
//                new Transaction("LS", 0, "/list/LA/items/IA/name", "Get milk"),
//                new Transaction("LS", 1, "/list/LA/items/IB/name", "Feed dog"),
//                new Transaction("LS", 2, "/list/LA/order", "[\"/list/LA/items/IA\",\"/list/LA/items/IB\"]"),
//                new Transaction("LM1A", 0, "/list/LA/order", "[\"/list/LA/items/IB\",\"/list/LA/items/IA\"]"),
//                new Transaction("LM1A", 1, "/list/LA/items/IA/due", "Tuesday")
//        );
        mobile1.flush();
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

        mobile2.flush();

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
}
