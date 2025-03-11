package com.example.sangdaeng001sqlbank.dao.hyepin;

import com.example.sangdaeng001sqlbank.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserDao {
    public int updatePassword(UserDto userDto);
}
