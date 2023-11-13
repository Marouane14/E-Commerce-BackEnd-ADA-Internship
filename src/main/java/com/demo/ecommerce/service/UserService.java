package com.demo.ecommerce.service;

import com.demo.ecommerce.dao.RoleDao;
import com.demo.ecommerce.dao.UserDao;
import com.demo.ecommerce.entity.Role;
import com.demo.ecommerce.entity.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void initRoleAndUser() {

        Role adminRole = new Role();
        adminRole.setRoleName("Admin");
        adminRole.setRoleDescription("Admin role");
        roleDao.save(adminRole);

        Role userRole = new Role();
        userRole.setRoleName("User");
        userRole.setRoleDescription("Default role for newly created record");
        roleDao.save(userRole);

        User adminUser = new User();
        adminUser.setUserName("admin123");
        adminUser.setUserPassword(getEncodedPassword("admin@pass"));
        adminUser.setUserFirstName("admin");
        adminUser.setUserLastName("admin");
        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(adminRole);
        adminUser.setRole(adminRoles);
        userDao.save(adminUser);

        User user = new User();
        user.setUserName("raj123");
        user.setUserPassword(getEncodedPassword("raj@123"));
        user.setUserFirstName("raj");
        user.setUserLastName("sharma");
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(userRole);
        user.setRole(userRoles);
        userDao.save(user);
    }

    public User registerNewUser(User user) {
        Role role = roleDao.findById("User").get();
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(role);
        user.setRole(userRoles);
        user.setUserPassword(getEncodedPassword(user.getUserPassword()));

        return userDao.save(user);
    }
    public int getUsersWithAdminRole() {
        List<User> adminUsers = new ArrayList<>();
        List<User> users = (List<User>) userDao.findAll();
        for (User user : users) {
            Set<Role> roles = user.getRole();
            
            if (roles.size() == 1) {
                Role role = roles.iterator().next(); // Get the single role
                if (role.getRoleName().equals("Admin")) {
                    adminUsers.add(user);
                }
            }
        }

        int nbr=adminUsers.size();
        return nbr;
    }
    public int getUsersWithUserRole() {
        List<User> regularUsers = new ArrayList<>();
        List<User> users = (List<User>) userDao.findAll();
        for (User user : users) {
            Set<Role> roles = user.getRole();
            
            if (roles.size() == 1) {
                Role role = roles.iterator().next(); // Get the single role
                if (role.getRoleName().equals("User")) {
                    regularUsers.add(user);
                }
            }
        }
        int nbr=regularUsers.size();
        return nbr;
    }
    
    public int getUsersWithBothRoles() {
        List<User> bothRoleUsers = new ArrayList<>();
        List<User> users = (List<User>) userDao.findAll();
        for (User user : users) {
            Set<Role> roles = user.getRole();
            boolean isAdmin = false;
            boolean isUser = false;

            for (Role role : roles) {
                if (role.getRoleName().equals("Admin")) {
                    isAdmin = true;
                } else if (role.getRoleName().equals("User")) {
                    isUser = true;
                }
            }

            if (isAdmin && isUser) {
                bothRoleUsers.add(user);
            }
        }
        int nbr=bothRoleUsers.size();
        return nbr;
    }
//	public int numberOfAdmins() {
//		List<User> adminUsers = new ArrayList<>();
//		List<User> regularUsers = new ArrayList<>();
//		List<User> bothRoleUsers = new ArrayList<>();
//		
//		Iterable<User> tabUsers = userDao.findAll();
//		
//		for (User user : tabUsers) {
//			Set<Role> roles = user.getRole();
//			boolean isAdmin = false;
//			boolean isUser = false;
//
//			for (Role role : roles) {
//				if (role.getRoleName().equals("Admin")) {
//					isAdmin = true;
//				} else if (role.getRoleName().equals("User")) {
//					isUser = true;
//				}
//			}
//
//			if (isAdmin && isUser) {
//				bothRoleUsers.add(user);
//			} else if (isAdmin) {
//				adminUsers.add(user);
//			} else if (isUser) {
//				regularUsers.add(user);
//			}
//
//		}
//		
//		return 0;
//	}

    public String getEncodedPassword(String password) {
        return passwordEncoder.encode(password);
    }
}
