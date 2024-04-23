package com.github.williamdlm.picpaysimplificado.services;

import com.github.williamdlm.picpaysimplificado.domain.user.User;
import com.github.williamdlm.picpaysimplificado.domain.user.UserType;
import com.github.williamdlm.picpaysimplificado.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void validateTransaction(User sender, BigDecimal amount) throws Exception {
        if(sender.getUserType() == UserType.MERCHANT){
            throw  new Exception("Usuario do tipo Lojista não está autorizado a realizar transações");
        }

        if(sender.getBalance().compareTo(amount) < 0){
            throw  new Exception("Saldo insuficiente");
        }
    }

    public  User findUserById(Long id) throws Exception {
        return this.userRepository.findById(id).orElseThrow(() -> new Exception(
                "Usuario não encontrado"));
    }

    public void saveUser(User user) {
        this.userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return this.userRepository.findAll();
    }
}
