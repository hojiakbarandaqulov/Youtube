package org.example.utils;


import org.example.config.CustomUserDetail;
import org.example.dto.JwtDTO;
import org.example.entity.profile.ProfileEntity;
import org.example.enums.ProfileRole;
import org.example.exp.AppForbiddenException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

public class SecurityUtil {
    public static JwtDTO getJwtDTO(String token) {
        String jwt = token.substring(7); // Bearer
        JwtDTO dto = JwtUtil.decode(jwt);
        return dto;
    }

    public static JwtDTO getJwtDTO(String token, ProfileRole requiredRole) {
        JwtDTO dto = getJwtDTO(token);
        if(!dto.getRole().equals(requiredRole)){
            throw new AppForbiddenException("Method not allowed");
        }
        return dto;
    }

    public static void getJwtDTO(String token, List<ProfileRole> requiredRole){
        JwtDTO dto = getJwtDTO(token);
        for (ProfileRole role:requiredRole){
            if(role==dto.getRole()){
                return;
            }
        }
        throw new AppForbiddenException("Method not allowed");

    }

    public static Long getProfileId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail user = (CustomUserDetail) authentication.getPrincipal();
        return user.getProfile().getId();
    }

    public static ProfileEntity getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetail user = (CustomUserDetail) authentication.getPrincipal();
        return user.getProfile();
    }
}
