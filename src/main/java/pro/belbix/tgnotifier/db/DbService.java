package pro.belbix.tgnotifier.db;

import static pro.belbix.tgnotifier.tg.Commands.fillFieldForCommand;

import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import pro.belbix.tgnotifier.db.entity.UserEntity;
import pro.belbix.tgnotifier.db.entity.TokenWatchEntity;
import pro.belbix.tgnotifier.db.repositories.UserRepository;
import pro.belbix.tgnotifier.db.repositories.TokenWatchRepository;

@Service
@Log4j2
public class DbService {

    private final UserRepository userRepository;
    private final TokenWatchRepository tokenWatchRepository;

    public DbService(UserRepository userRepository, TokenWatchRepository tokenWatchRepository) {
        this.userRepository = userRepository;
        this.tokenWatchRepository = tokenWatchRepository;
    }

    public void save(UserEntity userEntity) {
        userRepository.save(userEntity);
    }

    public void save(TokenWatchEntity tokenWatchEntity) {
        tokenWatchRepository.save(tokenWatchEntity);
    }

    public void saveNewUser(UserEntity userEntity) {
        userRepository.save(userEntity);
    }

    public boolean isKnownChatId(long id) {
        return userRepository.existsById(id);
    }

    public List<UserEntity> findAllChats() {
        return userRepository.findAll();
    }

    public String updateValueForLastCommand(long id, String text) {

        UserEntity userEntity = userRepository.findById(id).orElse(null);
        if (userEntity == null) {
            return "User not found";
        }

        String lastCommand = userEntity.getLastCommand();

        if (lastCommand == null) {
            return "Please, use command before setup value";
        }

        try {
            if (!fillFieldForCommand(lastCommand, userEntity, text)) {
                return "Command not found";
            }
        } catch (IllegalStateException e) {
            return e.getMessage();
        }
        userEntity.setLastCommand(null);
        userRepository.save(userEntity);
        return "Value successfully updated to " + text;
    }

    public void updateLastCommand(long id, String command) {
        userRepository.findById(id)
            .ifPresent(u -> {
                u.setLastCommand(command);
                userRepository.save(u);
            });
    }

    public UserEntity findById(long chatId) {
        return userRepository.findById(chatId)
            .orElseThrow(() -> new IllegalStateException("Not found user with id " + chatId));
    }
}
