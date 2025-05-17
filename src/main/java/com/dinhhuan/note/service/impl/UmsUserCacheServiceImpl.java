package com.dinhhuan.note.service.impl;

import com.dinhhuan.note.common.service.RedisService;
import com.dinhhuan.note.model.UmsUser;
import com.dinhhuan.note.service.UmsUserCacheService;
import com.dinhhuan.note.service.UmsUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class UmsUserCacheServiceImpl implements UmsUserCacheService {
    @Autowired
    private UmsUserService userService;
    @Autowired
    private RedisService redisService;
    @Value("${redis.database}")
    private String REDIS_DATABASE;
    @Value("${redis.expire.common}")
    private Long REDIS_EXPIRE;
    @Value("${redis.key.user}")
    private String REDIS_KEY_USER;
    @Override
    public void deleteUser(Long id) {
        UmsUser user = userService.getItem(id);
        if (user != null) {
            String key = REDIS_DATABASE + ":" + REDIS_KEY_USER + ":" + user.getUsername();
            redisService.del(key);
        }
    }
    @Override
    public UmsUser getUser(String username) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_USER + ":" + username;
        return (UmsUser) redisService.get(key);
    }

    @Override
    public void setUser(UmsUser user) {
        String key = REDIS_DATABASE + ":" + REDIS_KEY_USER + ":" + user.getUsername();
        redisService.set(key, user, REDIS_EXPIRE);
    }
}
