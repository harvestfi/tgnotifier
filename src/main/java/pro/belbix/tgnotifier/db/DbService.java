package pro.belbix.tgnotifier.db;

import static pro.belbix.tgnotifier.tg.Commands.fillFieldForCommand;

import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import pro.belbix.tgnotifier.db.entity.UserEntity;
import pro.belbix.tgnotifier.db.repositories.UserRepository;

@Service
@Log4j2
public class DbService {

    private final UserRepository userRepository;

    public DbService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void save(UserEntity userEntity) {
        userRepository.save(userEntity);
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

        double value;
        try {
            value = Double.parseDouble(textToValue(text));
        } catch (Exception e) {
            log.error("Error parse value from " + text);
            return "Incorrect value";
        }

        if (!fillFieldForCommand(lastCommand, userEntity, value)) {
            return "Command not found";
        }
        userEntity.setLastCommand(null);
        userRepository.save(userEntity);
        return "Value successful updated to " + value;
    }

    private String textToValue(String text) {
        return text.replaceAll(",", ".")
            .replaceAll("[^0-9.]+", "").trim();
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
