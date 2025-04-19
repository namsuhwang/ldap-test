package com.example.ldaptest.repository.ldap;

import com.example.ldaptest.models.entity.LdapGroupEntity;
import org.springframework.data.ldap.repository.LdapRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface LdapGroupRepository extends LdapRepository<LdapGroupEntity> {

    Optional<LdapGroupEntity> findByCn(String cn);

    Optional<LdapGroupEntity> findByDn(String dn);
}
