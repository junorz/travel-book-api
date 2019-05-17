package com.junorz.travelbook.context.orm;

import java.util.function.Supplier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * The transaction manager for handling transaction.
 */
public class TxManager {

    private static final Logger logger = LoggerFactory.getLogger(TxManager.class);
    private final TransactionTemplate txTemplate;

    public TxManager(PlatformTransactionManager txm) {
        txTemplate = new TransactionTemplate(txm);
    }

    public static TxManager of(PlatformTransactionManager txm) {
        return new TxManager(txm);
    }

    public <T> T tx(Supplier<T> supplier) {
        return txTemplate.execute(status -> {
            T result = null;
            try {
                return supplier.get();
            } catch (Exception e) {
                status.setRollbackOnly();
                logger.error("The transaction has been rolled back due to an exception occurred.", e);
            }
            return result;
        });
    }

    public void tx(Runnable runnable) {
        txTemplate.execute(status -> {
            try {
                runnable.run();
            } catch (Exception e) {
                status.setRollbackOnly();
                logger.error("The transaction has been rolled back due to an exception occurred.", e);
            }
            return null;
        });
    }

}
