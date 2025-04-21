package com.example.ldaptest.service;

import com.example.ldaptest.common.LdapUtil;
import com.example.ldaptest.models.entity.LdapUserEntity;
import com.example.ldaptest.repository.ldap.LdapUserRepository;
import io.netty.util.internal.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.query.LdapQuery;
import org.springframework.ldap.query.LdapQueryBuilder;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Service;

import javax.naming.Name;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.ldap.LdapName;
import java.util.List;
import java.util.Optional;

@Service
public class LdapUserRepoService {

    @Autowired
    private LdapTemplate ldapTemplate;

    @Autowired
    private LdapUserRepository ldapUserRepository;

    public static final Logger log = LogManager.getLogger(LdapUserRepoService.class);

    public Optional<LdapUserEntity> getUser(String dn) {
        if(dn == null || dn.isEmpty()){
            return Optional.empty();
        }

        LdapName ldapName = LdapUtil.getLdapNameFromDnString(dn);

        Optional<LdapUserEntity> userEntity = ldapUserRepository.findById(ldapName);
        return userEntity;
    }

    public List<LdapUserEntity> getUsersByName(String userName) {
        String searchUserName = StringUtil.isNullOrEmpty(userName) ? "*" : "*" + userName + "*";

        LdapQuery ldapQuery = LdapQueryBuilder.query()
              //  .base("ou=people")
                .where("cn").like(searchUserName);
        List<LdapUserEntity> userList = ldapUserRepository.findAll(ldapQuery);
        return userList;
    }


    public boolean addUser(String uid, String cn, String sn, String ou, String email) {
        Name dn = LdapNameBuilder.newInstance()
                .add("ou", "people")
                .add("cn", cn)
                .build();

        BasicAttributes attrs = new BasicAttributes();
        attrs.put("uid", uid);
        attrs.put("cn", cn);    // fullName
        attrs.put("sn", sn);    // surname
        attrs.put("ou", ou);    // org name
        attrs.put("mail", email);

        // objectClass는 여러 값을 가질 수 있는 multi-valued attribute입니다.
        BasicAttribute ocattr = new BasicAttribute("objectClass");
        ocattr.add("inetOrgPerson");
        ocattr.add("organizationalPerson");
        ocattr.add("person");
        ocattr.add("top");

        // 추가
        attrs.put(ocattr);
        ldapTemplate.bind(dn, null, attrs);

        return true;
    }

    public boolean deleteUser(String dn) {
        ldapTemplate.unbind(dn);
        //Optional<LdapUserEntity> userEntity = getUser(dn);
        //ldapUserRepository.delete(userEntity.get());
        return true;
    }

}
