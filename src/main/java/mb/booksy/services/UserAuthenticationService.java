package mb.booksy.services;


import mb.booksy.domain.user.Client;
import mb.booksy.domain.user.Person;
import mb.booksy.exceptions.UserAlreadyExistException;
import mb.booksy.repository.ClientRepository;
import mb.booksy.web.model.UserDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.security.auth.message.AuthException;

import static mb.booksy.security.ApplicationUserRole.CLIENT;

@Service
public class UserAuthenticationService implements UserDetailsService {

    private final PasswordEncoder passwordEncoder;
    private final ClientRepository clientRepository;

    public UserAuthenticationService(ClientRepository clientRepository, PasswordEncoder passwordEncoder) {
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Person loadUserByUsername(String username) throws UsernameNotFoundException {
        Client client = clientRepository.selectClientByUsername(username);
        if(client != null)
            return client;
        else
            throw new UsernameNotFoundException("Blad 0");
    }

    public Long getAuthenticatedClientId() throws AuthException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Client client = clientRepository.selectClientByUsername(authentication.getName());
        if(client == null)
            throw new AuthException("Blad 1");
        return client.getId();
    }

    public Person getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Client client = clientRepository.selectClientByUsername(authentication.getName());
        return client;
    }


    public void registerNewUserAccount(UserDto userDto) throws UserAlreadyExistException {
        if (!emailExist(userDto.getEmail())) {
           if(userDto.getRole().equals("CLIENT")) {
               Client newClient = Client.builder()
                       .name(userDto.getName())
                       .email(userDto.getEmail())
                       .password(passwordEncoder.encode(userDto.getPassword()))
                       .userRole(CLIENT.getUserRole()).build();
               clientRepository.save(newClient);
               //emailService.sendSimpleMessage(newClient.getEmail(), UserMessages.MAIL_WELCOME_TITLE, UserMessages.MAIL_WELCOME_MESSAGE);
           }
        }else
            throw new UserAlreadyExistException("Konto istnieje" + userDto.getEmail());
    }

    private boolean emailExist(String email) {
        return clientRepository.selectClientByUsername(email) != null;
    }

}
