package com.fypembeddingapplication.embeddingapplication.Service;

import com.fypembeddingapplication.embeddingapplication.Encryption.ASEEncryption;
import com.fypembeddingapplication.embeddingapplication.database.ConfirmationTokenRepository;
import com.fypembeddingapplication.embeddingapplication.database.UserRepository;
import com.fypembeddingapplication.embeddingapplication.model.ConfirmationToken;
import com.fypembeddingapplication.embeddingapplication.model.User;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.internet.MimeMessage;
import java.text.MessageFormat;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService implements UserDetailsService{
    @Autowired
    private UserRepository userRepository;
    private ConfirmationTokenService confirmationTokenService ;
    private EmailSenderService emailSenderService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        Optional<User> optionalUser = userRepository.findByEmail(email)    ;
        if (optionalUser.isPresent()){
            return optionalUser.get();
        } else {
                throw new UsernameNotFoundException(MessageFormat.format("User with email {0} cannot be found.",email)) ;
        }
    }
    
    public int signUpUser (String email,String username, String password){
        String encyptedPassword = bCryptPasswordEncoder.encode(password);
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()){
            if (optionalUser.get().getEnabled()){
                return 1;
            }else {
                return 2;
            }
        }else {
           User user = new User(email,username,encyptedPassword,username);
           userRepository.save(user);
           ConfirmationToken confirmationToken = new ConfirmationToken(user);
           confirmationTokenService.saveConfirmationToken(confirmationToken);
           sendConfirmationMail(email,confirmationToken.getConfirmatinToken());
           return 3;
         }

    }
   public void sendConfirmationMail(String userMail, String token){
        emailSenderService .sendEmail(userMail,token);
    }
   public void confirmUser (ConfirmationToken confirmationToken){
        User user = confirmationToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);
        confirmationTokenService.deleteConfirmationToken(confirmationToken.getId());
    }




}
