package com.demo.ecommerce.service;

import com.demo.ecommerce.dao.RoleDao;
import com.demo.ecommerce.entity.Role;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private RoleDao roleDao;

    public Role createNewRole(Role role) {
        return roleDao.save(role);
    }
}
