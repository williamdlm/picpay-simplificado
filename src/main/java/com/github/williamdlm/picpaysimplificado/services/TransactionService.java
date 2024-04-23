package com.github.williamdlm.picpaysimplificado.services;

import com.github.williamdlm.picpaysimplificado.domain.transaction.Transaction;
import com.github.williamdlm.picpaysimplificado.domain.user.User;
import com.github.williamdlm.picpaysimplificado.dtos.TransactionDTO;
import com.github.williamdlm.picpaysimplificado.repositories.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private final UserService userService;
    private final TransactionRepository transactionRepository;
    private final RestTemplate restTemplate;
    private final NotificationService notificationService;

    public Transaction createTransaction(TransactionDTO transactionDTO) throws Exception {

        User sender = this.userService.findUserById(transactionDTO.senderId());
        User receiver = this.userService.findUserById(transactionDTO.receiverId());

        userService.validateTransaction(sender, transactionDTO.value());

        boolean isAuthorized = isAuthorizedTransaction(sender, transactionDTO.value());

        if(!isAuthorized) {
            throw new Exception("Transação não autorizada");
        }

        Transaction newTransaction = new Transaction();
        newTransaction.setAmount(transactionDTO.value());
        newTransaction.setSender(sender);
        newTransaction.setReceiver(receiver);
        newTransaction.setTimestamp(LocalDateTime.now());

        sender.setBalance(sender.getBalance().subtract(transactionDTO.value()));
        receiver.setBalance(receiver.getBalance().add(transactionDTO.value()));
        this.transactionRepository.save(newTransaction);
        this.userService.saveUser(sender);
        this.userService.saveUser(receiver);

        this.notificationService.sendNotification(sender, "Transação efetuada com sucesso");
        this.notificationService.sendNotification(receiver, "Transação recebida com sucesso");

        return newTransaction;
    }

    public boolean isAuthorizedTransaction(User sender, BigDecimal value) {
        ResponseEntity<Map> authorizationResponse = restTemplate.getForEntity(
                "https://run.mocky.io/v3/5794d450-d2e2-4412-8131" +
                        "-73d0293ac1cc", Map.class);

        if (authorizationResponse.getStatusCode() ==
                HttpStatus.OK) {
            String message = authorizationResponse.getBody().get("message").toString();
            return "Autorizado".equalsIgnoreCase(message);
        } else return false;
    }
}
