package net.avh4.data.log;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.fest.assertions.Assertions.assertThat;

public class PushFollowerEngineTest {
    private PushFollowerEngine<TestFollower> subject;
    @Mock
    private Transaction t1;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        subject = new PushFollowerEngine<>(new TestFollower());
    }

    @Test
    public void result_withNoData_shouldEqualInitialFollower() throws Exception {
        assertThat(subject.result().transactions).isEmpty();
    }

    @Test
    public void receive_shouldSendTransactionToTheFollower() throws Exception {
        subject.receive(t1);
        assertThat(subject.result().transactions).containsExactly(t1);
    }
}
