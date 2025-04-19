package com.example.ldaptest.service;

import com.example.ldaptest.common.LdapUtil;
import com.example.ldaptest.models.dto.LdapGroup;
import com.example.ldaptest.models.dto.LdapGroupMember;
import com.example.ldaptest.models.dto.LdapUser;
import com.example.ldaptest.models.entity.LdapGroupEntity;
import com.example.ldaptest.models.entity.LdapUserEntity;
import com.example.ldaptest.repository.ldap.LdapGroupRepository;
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
import javax.naming.directory.Attribute;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.ModificationItem;
import javax.naming.ldap.LdapName;
import java.util.List;
import java.util.Optional;

import static javax.naming.directory.DirContext.ADD_ATTRIBUTE;
import static javax.naming.directory.DirContext.REMOVE_ATTRIBUTE;

@Service
public class LdapGroupRepoService {

    @Autowired
    private LdapTemplate ldapTemplate;

    @Autowired
    private LdapGroupRepository ldapGroupRepository;

    @Autowired
    private LdapUserRepoService ldapUserRepoService;

    public static final Logger log = LogManager.getLogger(LdapGroupRepoService.class);


    public Optional<LdapGroupEntity> getGroup(String dn) {
        if(dn == null || dn.isEmpty()){
            return Optional.empty();
        }

        LdapName ldapName = LdapUtil.getLdapNameFromDnString(dn);

        Optional<LdapGroupEntity> groupEntity = ldapGroupRepository.findById(ldapName);
        return groupEntity;
    }

    public List<LdapGroupEntity> getGroupsByName(String groupName) {
        String searchGroupName = StringUtil.isNullOrEmpty(groupName) ? "*" : "*" + groupName + "*";

        LdapQuery ldapQuery = LdapQueryBuilder.query()
              //  .base("ou=people")
                .where("cn").like(searchGroupName);
        List<LdapGroupEntity> groupList = ldapGroupRepository.findAll(ldapQuery);
        return groupList;
    }

    public boolean addGroup(String cn, String groupType) {
        Name dn = LdapNameBuilder.newInstance("ou=people")  // users
                .add("cn", cn)
                .build();

        BasicAttributes attrs = new BasicAttributes();
        // attrs.put("uid", uid);
        attrs.put("cn", cn);     // fullName
        // attrs.put("sn", sn);     // surname
        attrs.put("groupType", groupType);

        // objectClass는 여러 값을 가질 수 있는 multi-valued attribute입니다.
        BasicAttribute ocattr = new BasicAttribute("objectClass");
        // ocattr.add("groupOfUniqueNames");
        ocattr.add("Group");
        ocattr.add("top");

        // 추가
        attrs.put(ocattr);
        ldapTemplate.bind(dn, null, attrs);

        return true;
    }


    public boolean addUserForGroup( String groupId, String userId) {
        if(StringUtil.isNullOrEmpty(userId) || StringUtil.isNullOrEmpty(groupId)){
            return false;
        }

        Optional<LdapUserEntity> optionalLdapUserEntity = ldapUserRepoService.getUser(userId);
        if(optionalLdapUserEntity == null){
            return false;
        }

        LdapUserEntity userEntity = optionalLdapUserEntity.get();

        Optional<LdapGroupEntity> optionalLdapGroupEntity = getGroup(groupId);
        if(optionalLdapGroupEntity == null){
            return false;
        }

        LdapGroupEntity groupEntity = optionalLdapGroupEntity.get();

        if(groupEntity.getMembers() != null || groupEntity.getMembers().size() > 0){
            for(String memberDn : groupEntity.getMembers()){
                if(memberDn.equals(userId)){
                    log.info("addUserForGroup :: already exists :: userId = [" + userId + "]");
                    return true;
                }
            }
        }

         // uniqueMember 속성에 사용자 DN 추가
         Attribute attr = new BasicAttribute("member", userEntity.getDn());
         ModificationItem item = new ModificationItem(ADD_ATTRIBUTE, attr);

         ldapTemplate.modifyAttributes(groupEntity.getDn(), new ModificationItem[]{item});

         return true;
    }

    public boolean removeUserFromGroup( String groupId, String userId) {
        if(StringUtil.isNullOrEmpty(userId) || StringUtil.isNullOrEmpty(groupId)){
            return false;
        }

        Optional<LdapUserEntity> optionalLdapUserEntity = ldapUserRepoService.getUser(userId);
        if(optionalLdapUserEntity == null){
            return false;
        }

        LdapUserEntity userEntity = optionalLdapUserEntity.get();

        Optional<LdapGroupEntity> optionalLdapGroupEntity = getGroup(groupId);
        if(optionalLdapGroupEntity == null){
            return false;
        }

        LdapGroupEntity groupEntity = optionalLdapGroupEntity.get();

        int cnt = 0;
        if(groupEntity.getMembers() != null || groupEntity.getMembers().size() > 0){
            for(String memberDn : groupEntity.getMembers()){
                if(memberDn.equals(userId)){
                    cnt++;
                    log.info("delUserFromGroup :: exists :: userId = [" + userId + "]");
                    break;
                }
            }
        }

        if(cnt == 0){
            log.info("delUserFromGroup :: not exists :: userId = [" + userId + "]");
            return true;
        }

         // uniqueMember 속성에 사용자 DN 추가
         Attribute attr = new BasicAttribute("member", userEntity.getDn());
         ModificationItem item = new ModificationItem(REMOVE_ATTRIBUTE, attr);

         ldapTemplate.modifyAttributes(groupEntity.getDn(), new ModificationItem[]{item});

         return true;
    }



}
