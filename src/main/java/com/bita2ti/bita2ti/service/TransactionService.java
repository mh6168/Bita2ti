package com.bita2ti.bita2ti.service;

import com.bita2ti.bita2ti.model.Transaction;
import com.bita2ti.bita2ti.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    public void record(Long userId, String action) {
        if (action == null || action.isBlank()) {
            return;
        }

        Transaction t = new Transaction();
        t.setUserId(userId);
        t.setAction(action.trim());
        transactionRepository.save(t);
    }
}

