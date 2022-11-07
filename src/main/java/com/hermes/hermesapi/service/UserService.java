package com.hermes.hermesapi.service;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.hermes.hermesapi.entity.ConfirmationToken;
import com.hermes.hermesapi.entity.User;
import com.hermes.hermesapi.models.WhatsAppMessageModel;
import com.hermes.hermesapi.repository.UserRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final ConfirmationTokenService confirmationTokenService;

    private final MessageSenderService messageSenderService;

    void sendTokenWhatsApp(String name, String phone, String token) {

        final WhatsAppMessageModel whatsAppMessageModel = new WhatsAppMessageModel();

        whatsAppMessageModel
                .setMessage(MessageFormat.format("Olá {0}, seu token de confirmação é: *{1}*", name, token));
        whatsAppMessageModel.setNumber(phone);

        messageSenderService.sendWhatsAppMessage(whatsAppMessageModel);
    }

    public User signUpUser(User user) {

        final String encryptedPassword = bCryptPasswordEncoder.encode(user.getPassword());

        user.setPassword(encryptedPassword);

        List<User> userExists = userRepository.findByEmail(user.getEmail());

        if (userExists.isEmpty()) {

            User savedUser = userRepository.save(user);

            final ConfirmationToken confirmationToken = new ConfirmationToken(user);

            confirmationTokenService.saveConfirmationToken(confirmationToken);

            sendTokenWhatsApp(savedUser.getName(), savedUser.getPhone(), confirmationToken.getToken());

            return savedUser;
        }
        else{
            return null;
        }

    }

    public void confirmUser(ConfirmationToken confirmationToken) {

        final User user = confirmationToken.getUser();

        user.setEnabled(true);

        userRepository.save(user);

        confirmationTokenService.deleteConfirmationToken(confirmationToken.getId());

    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(email);
        if (!user.isEmpty()) {
            return new org.springframework.security.core.userdetails.User(user.get(0).getEmail(),
                    user.get(0).getPassword(), new ArrayList<>());
        } else {
            throw new UsernameNotFoundException("User not found with email: " + email);
        }
    }
}
