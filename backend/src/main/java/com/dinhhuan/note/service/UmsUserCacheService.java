package com.dinhhuan.note.service;

import com.dinhhuan.note.model.UmsUser;


public interface UmsUserCacheService {
    void deleteUser(Long id);
    UmsUser getUser(String username);
    void setUser(UmsUser user);

    void setOtp(String otp, String email);
    String getOtp(String email);
}
