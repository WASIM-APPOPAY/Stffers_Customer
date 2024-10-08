package com.stuffer.stuffers;

//import javax.jms.JMSException;
/*import com.amazon.sqs.javamessaging.SQSConnection;
import com.amazon.sqs.javamessaging.SQSMessageConsumerPrefetch;
import com.amazon.sqs.javamessaging.SQSMessagingClientConstants;
import com.amazon.sqs.javamessaging.SQSQueueDestination;
import com.amazon.sqs.javamessaging.SQSSession;
import com.amazon.sqs.javamessaging.SQSSessionCallbackScheduler;
import com.amazon.sqs.javamessaging.acknowledge.Acknowledger;
import com.amazon.sqs.javamessaging.acknowledge.NegativeAcknowledger;
import com.amazonaws.logging.Log;
import com.amazonaws.logging.LogFactory;

import java.util.Queue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.QueueReceiver;*/

public class SQSMessageConsumer{}
/*implements MessageConsumer, QueueReceiver {
    private static final Log LOG = LogFactory.getLog(SQSMessageConsumer.class);
    public static final int PREFETCH_EXECUTOR_GRACEFUL_SHUTDOWN_TIME = 30;

    protected volatile boolean closed = false;

    private final SQSQueueDestination sqsDestination;
    private final SQSSession parentSQSSession;

    private final SQSSessionCallbackScheduler sqsSessionRunnable;

    *//**
     * Executor for prefetch thread.
     *//*
    private final ExecutorService prefetchExecutor;

    *//**
     * Prefetch Runnable. This include keeping internal message buffer filled and call MessageListener if set.
     *//*
    private final SQSMessageConsumerPrefetch sqsMessageConsumerPrefetch;

    SQSMessageConsumer(SQSConnection parentSQSConnection, SQSSession parentSQSSession,
                       SQSSessionCallbackScheduler sqsSessionRunnable, SQSQueueDestination destination,
                       Acknowledger acknowledger, NegativeAcknowledger negativeAcknowledger, ThreadFactory threadFactory) {
        this(parentSQSConnection, parentSQSSession,
                sqsSessionRunnable, destination,
                acknowledger, negativeAcknowledger, threadFactory,
                new SQSMessageConsumerPrefetch(sqsSessionRunnable, acknowledger, negativeAcknowledger, destination,
                        parentSQSConnection.getWrappedAmazonSQSClient(),
                        parentSQSConnection.getNumberOfMessagesToPrefetch()));

    }

    SQSMessageConsumer(SQSConnection parentSQSConnection, SQSSession parentSQSSession,
                       SQSSessionCallbackScheduler sqsSessionRunnable, SQSQueueDestination destination,
                       Acknowledger acknowledger, NegativeAcknowledger negativeAcknowledger, ThreadFactory threadFactory,
                       SQSMessageConsumerPrefetch sqsMessageConsumerPrefetch) {
        this.parentSQSSession = parentSQSSession;
        this.sqsDestination = destination;
        this.sqsSessionRunnable = sqsSessionRunnable;
        this.sqsMessageConsumerPrefetch = sqsMessageConsumerPrefetch;
        this.sqsMessageConsumerPrefetch.setMessageConsumer(this);

        prefetchExecutor = Executors.newSingleThreadExecutor(threadFactory);
        prefetchExecutor.execute(sqsMessageConsumerPrefetch);
    }



    *//**
     * Gets the queue destination associated with this queue receiver, where the
     * messages are delivered from.
     *
     * @return a queue destination
     *//*
    @Override
    public Queue getQueue() throws JMSException {
        return (Queue) sqsDestination;
    }

    *//**
     * Gets the message consumer's MessageListener.
     *
     * @return a message listener
     *//*
    @Override
    public MessageListener getMessageListener() throws JMSException {
        return sqsMessageConsumerPrefetch.getMessageListener();
    }

    *//**
     * Sets the message consumer's MessageListener.
     *
     * @param listener
     *            a message listener to use for asynchronous message delivery
     * @throws JMSException
     *             If the message consumer is closed
     *//*
    @Override
    public void setMessageListener(MessageListener listener) throws JMSException {
        checkClosed();
        this.sqsMessageConsumerPrefetch.setMessageListener(listener);
    }

    *//**
     * This call blocks indefinitely until a message is produced or until this
     * message consumer is closed. When ConnectionState is stopped receive is
     * paused.
     *
     * @return the next message produced for this message consumer, or null if
     *         this message consumer is closed
     * @throws JMSException
     *             On internal error
     *//*
    @Override
    public Message receive() throws JMSException {
        return sqsMessageConsumerPrefetch.receive();
    }

    *//**
     * This call blocks until a message arrives, the timeout expires, or this
     * message consumer is closed. A timeout of zero never expires, and the call
     * blocks indefinitely.
     *
     * @param timeout
     *            the timeout value (in milliseconds)
     * @return the next message produced for this message consumer, or null if
     *         the timeout expires or this message consumer is closed
     * @throws JMSException
     *             On internal error
     *//*
    @Override
    public Message receive(long timeout) throws JMSException {
        return sqsMessageConsumerPrefetch.receive(timeout);
    }

    *//**
     * Receives the next message if one is immediately available.
     *
     * @return the next message produced for this message consumer, or null if
     *         no message is available
     * @throws JMSException
     *             On internal error
     *//*
    @Override
    public Message receiveNoWait() throws JMSException {
        return sqsMessageConsumerPrefetch.receiveNoWait();
    }

    *//**
     * Closes the message consumer.
     * </p><p>
     * This will not return until receives and/or message listeners in progress
     * have completed. A blocked message consumer receive call returns null when
     * this consumer is closed.
     * </p><p>
     * Since consumer prefetch threads use SQS long-poll feature with 20 seconds
     * timeout, closing each consumer prefetch thread can take up to 20 seconds,
     * which in-turn will impact the time on consumer close.
     * </p><p>
     * This method may be called from a message listener's onMessage method on
     * its own consumer. After this method returns the onMessage method will be
     * allowed to complete normally, and the callback scheduler thread will be
     * closing the message consumer.
     *
     * @throws JMSException
     *             On internal error.
     *//*
    @Override
    public void close() throws JMSException {
        if (closed) {
            return;
        }

        if (parentSQSSession.isActiveCallbackSessionThread()) {
            sqsSessionRunnable.setConsumerCloseAfterCallback(this);
            return;
        }

        doClose();
    }

    void doClose() {
        if (closed) {
            return;
        }

        sqsMessageConsumerPrefetch.close();

        parentSQSSession.removeConsumer(this);

        try {
            if (!prefetchExecutor.isShutdown()) {
                LOG.info("Shutting down " + SQSSession.CONSUMER_PREFETCH_EXECUTER_NAME + " executor");
                *//** Shut down executor. *//*
                prefetchExecutor.shutdown();
            }

            parentSQSSession.waitForConsumerCallbackToComplete(this);

            if (!prefetchExecutor.awaitTermination(PREFETCH_EXECUTOR_GRACEFUL_SHUTDOWN_TIME, TimeUnit.SECONDS)) {

                LOG.warn("Can't terminate executor service " + SQSSession.CONSUMER_PREFETCH_EXECUTER_NAME +
                        " after " + PREFETCH_EXECUTOR_GRACEFUL_SHUTDOWN_TIME +
                        " seconds, some running threads will be shutdown immediately");
                prefetchExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            LOG.error("Interrupted while closing the consumer.", e);
        }

        closed = true;
    }

    boolean isClosed() {
        return closed;
    }

    *//** This method is not supported. *//*
    @Override
    public String getMessageSelector() throws JMSException {
        throw new JMSException(SQSMessagingClientConstants.UNSUPPORTED_METHOD);
    }

    *//** This stops the prefetching *//*
    protected void stopPrefetch() {
        if (!closed) {
            sqsMessageConsumerPrefetch.stop();
        }
    }

    *//** This starts the prefetching *//*
    protected void startPrefetch() {
        if (!closed) {
            sqsMessageConsumerPrefetch.start();
        }
    }

    private void checkClosed() throws IllegalStateException {
        if (closed) {
            throw new IllegalStateException("Consumer is closed");
        }
    }

    List<sqsmessageidentifier> purgePrefetchedMessagesWithGroups(Set<string> affectedGroups) throws JMSException {
        return sqsMessageConsumerPrefetch.purgePrefetchedMessagesWithGroups(affectedGroups);
    }
}*/