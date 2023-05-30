package api.mentalotus.Service;

import api.mentalotus.Domain.User;
import api.mentalotus.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {this.userRepository = userRepository;}

    public User Authenticate(String email) { return userRepository.findByEmail(email); }

    public void save(User user){userRepository.save(user);}

    public void delete(String userKey){userRepository.deleteById(userKey);}
}
