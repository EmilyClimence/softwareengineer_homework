package com.java1234.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.java1234.entity.User;

/*
 * 用户Repository接口
 * @author java1234 AT
 *
 */
public interface UserRepository extends JpaRepository<User, Integer>,JpaSpecificationExecutor<User>{

	/*
	 * 根据用户名查找用户实体
	 */
	@Query(value="select * from t_user where user_name=?1",nativeQuery=true)
    User findByUserName(String userName);
}