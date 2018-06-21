package ru.geekbrains.server.repository;

import org.springframework.data.repository.CrudRepository;
import ru.geekbrains.server.entity.UserInfo;

public interface UserInfoRepository extends CrudRepository<UserInfo, Long> {
    UserInfo findUserInfoByUserId(Long userId);

    UserInfo findUserInfoByToken(String token);


}
