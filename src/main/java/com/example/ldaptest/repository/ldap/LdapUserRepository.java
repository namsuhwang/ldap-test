package com.example.ldaptest.repository.ldap;

import com.example.ldaptest.models.entity.LdapUserEntity;
import org.springframework.data.ldap.repository.LdapRepository;
import org.springframework.stereotype.Repository;

import javax.naming.ldap.LdapName;
import java.util.Optional;

@Repository
public interface LdapUserRepository  extends LdapRepository<LdapUserEntity> {
    Optional<LdapUserEntity> findByCn(String cn);

    Optional<Object> findByDn(String dn);
}

