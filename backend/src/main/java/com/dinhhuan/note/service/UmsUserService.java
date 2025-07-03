package com.dinhhuan.note.service;

import com.dinhhuan.note.dto.UmsUserLoginParam;
import com.dinhhuan.note.dto.UmsUserParam;
import com.dinhhuan.note.dto.UpdatePasswordParam;
import com.dinhhuan.note.model.UmsUser;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;


public interface UmsUserService {
    UmsUser getUserByUsername(String username);
    UmsUser register(UmsUserParam umsUserParam);
    String login(UmsUserLoginParam umsUserLoginParam);
    UmsUser getItem(Long id);
    List<UmsUser> list(String keyword, Integer pageSize, Integer pageNum);
    int update (Long id, UmsUser user);
    int delete(Long id);
    int updatePassword(UpdatePasswordParam updatePasswordParam);
    UserDetails loadUserByUsername(String username);
    UmsUserCacheService getCacheService();
    void logout(String username);
    UmsUser getCurrentUser();
    UmsUser getUserByEmail(String email);
    int authenticateEmail(String email);
    boolean checkOtp(String otp, String email);
}
