package pro.belbix.tgnotifier.db;

import static pro.belbix.tgnotifier.tg.Commands.fillFieldForCommand;
import static pro.belbix.tgnotifier.tg.Commands.nextCommand;
import static pro.belbix.tgnotifier.tg.Commands.responseForCommand;

import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import pro.belbix.tgnotifier.db.entity.UserEntity;
import pro.belbix.tgnotifier.db.entity.TokenWatchEntity;
import pro.belbix.tgnotifier.db.repositories.UserRepository;
import pro.belbix.tgnotifier.db.repositories.TokenWatchRepository;
import pro.belbix.tgnotifier.tg.UserResponse;

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

    public UserResponse updateValueForLastCommand(long id, String text) {

        UserEntity userEntity = userRepository.findById(id).orElse(null);
        if (userEntity == null) {
            return new UserResponse("User not found", null, false);
        }

        String lastCommand = userEntity.getLastCommand();

        if (lastCommand == null) {
            return new UserResponse("Please, use command before setup value", null, false);
        }

        try {
            if (!fillFieldForCommand(lastCommand, userEntity, text)) {
                return new UserResponse("Command not found", null, false);
            }
        } catch (IllegalStateException e) {
            return new UserResponse(e.getMessage(), null, false);
        }
        
        String nextCommandID = nextCommand(lastCommand);

        log.info("next command: " + nextCommandID);

        userEntity.setLastCommand(nextCommandID);
        userRepository.save(userEntity);
        
        if (nextCommandID!=null){
            return responseForCommand(nextCommandID);
        }
        else {
            return new UserResponse("Value successfully updated to " + text, null, false);
        }
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
