package com.bita2ti.bita2ti.controller;

import com.bita2ti.bita2ti.model.Transaction;
import com.bita2ti.bita2ti.model.User;
import com.bita2ti.bita2ti.repository.TransactionRepository;
import com.bita2ti.bita2ti.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminTransactionController {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/transactions/latest")
    public String latestTransactions(Model model, jakarta.servlet.http.HttpSession session) {

        // Restrict access to the global admin
        Object userObj = session.getAttribute("user");
        if (!(userObj instanceof User user) || !"admin@admin.com".equalsIgnoreCase(user.getEmail())) {
            return "redirect:/login";
        }

        // Fetch newest-to-oldest
        List<Transaction> transactions = transactionRepository.findAll();
        // In case repository doesn't provide global sort, sort in memory.
        transactions = transactions.stream()
                .sorted((a, b) -> b.getCreatedAt().compareTo(a.getCreatedAt()))
                .collect(Collectors.toList());

        List<Long> userIds = transactions.stream().map(Transaction::getUserId).distinct().collect(Collectors.toList());
        Map<Long, User> userById = userRepository.findAllById(userIds)
                .stream()
                .collect(Collectors.toMap(User::getId, u -> u));

        model.addAttribute("transactions", transactions);
        model.addAttribute("userById", userById);
        return "admin-latest-transactions";
    }
}

