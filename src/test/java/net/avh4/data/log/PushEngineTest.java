package net.avh4.data.log;

import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class PushEngineTest {
    private PushEngine subject;
    @Mock
    private TransactionBuffer buffer;
    @Mock
    private PushObserver engine1;
    @Mock
    private PushObserver engine2;
    @Mock
    private Transaction t1;
    @Mock
    private Transaction t2;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        stub(buffer.getCommitted(0)).toReturn(ImmutableList.of(t1, t2));
        subject = new PushEngine(buffer);
    }

    @Test
    public void sync_fetchesTransactionsFromServer() throws Exception {
        subject.sync();
        verify(buffer).getCommitted(0);
    }

    @Test
    public void sync_sendsNewTransactionsToObservers() throws Exception {
        subject.add(engine1, engine2);
        subject.sync();
        InOrder order1 = inOrder(engine1);
        order1.verify(engine1).receive(t1);
        order1.verify(engine1).receive(t2);
        InOrder order2 = inOrder(engine2);
        order2.verify(engine2).receive(t1);
        order2.verify(engine2).receive(t2);
    }

    @Test(expected = IllegalStateException.class)
    public void addObserver_afterTransactionsHaveBeenSent_throws() throws Exception {
        subject.add(engine1);
        subject.sync();
        subject.add(engine2);
    }

    @Test
    public void sync_withPendingTransactions_sendThemToServer() throws Exception {
        subject.add("k1", "v1");
        subject.add("k2", "v2");
        subject.sync();
        InOrder inOrder = inOrder(buffer);
        inOrder.verify(buffer).add("k1", "v1");
        inOrder.verify(buffer).add("k2", "v2");
    }

    @Test
    public void sync_clearsPendingTransactions() throws Exception {
        subject.add("k1", "v1");
        subject.add("k2", "v2");
        subject.sync();
        reset(buffer);
        subject.sync();
        verify(buffer, never()).add(anyString(), anyString());
    }
}
