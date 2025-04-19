package com.example.ldaptest.service;

import com.example.ldaptest.models.dto.*;
import com.example.ldaptest.models.mapper.LdapGroupContextMapper;
import com.example.ldaptest.models.mapper.LdapUserContextMapper;
import io.netty.util.internal.StringUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.ldap.support.LdapNameBuilder;
import org.springframework.stereotype.Service;

import javax.naming.InvalidNameException;
import javax.naming.Name;
import javax.naming.directory.*;
import javax.naming.ldap.LdapName;
import java.util.ArrayList;
import java.util.List;

import static javax.naming.directory.DirContext.ADD_ATTRIBUTE;
import static javax.naming.directory.DirContext.REMOVE_ATTRIBUTE;

@Service
public class LdapTemplateService {

    @Autowired
    private LdapTemplate ldapTemplate;

    @Value("${spring.ldap.base}")
    private String ldapBase;

    public static final Logger log = LogManager.getLogger(LdapTemplateService.class);

    public LdapUser getAdUser(String userId) {
        if(StringUtil.isNullOrEmpty(userId)){
            return null;
        }

        Name dn = LdapNameBuilder.newInstance(userId).build();
        String filter = "(objectClass=person)";
        List<LdapUser> userList = ldapTemplate.search(
                dn,
                filter,
            new LdapUserContextMapper()
        );

        if(userList == null || userList.size() == 0){
            return null;
        }

        LdapUser user = userList.get(0);
        user.setBase(ldapBase);
        return user;
    }

    public List<LdapUser> getAdUsers(String userName) {
        String searchUser = StringUtil.isNullOrEmpty(userName) ? "*" : "*" + userName + "*";
        String base = "ou=people";
        String filter = "(&(objectClass=person)(cn=" + searchUser + "))";

        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        List<LdapUser> userList = ldapTemplate.search(
            base,
            filter,
            searchControls,
            new LdapUserContextMapper()
        );

        if(userList == null || userList.size() == 0){
            return null;
        }

        for(LdapUser user : userList){
            user.setBase(ldapBase);
        }

        return userList;
    }

    public LdapGroup getAdGroup(String groupId) {
        if(StringUtil.isNullOrEmpty(groupId)){
            return null;
        }

        Name dn = LdapNameBuilder.newInstance(groupId).build();
        String filter = "(objectClass=group)";
        List<LdapGroup> groupList = ldapTemplate.search(
                dn,
                filter,
            new LdapGroupContextMapper()
        );

        if(groupList == null || groupList.size() == 0){
            return null;
        }

        LdapGroup group = groupList.get(0);
        return group;
    }

    public List<LdapGroup> getAdGroups(String groupName) {
        String searchGroup = StringUtil.isNullOrEmpty(groupName) ? "*" : "*" + groupName + "*";
        String base = "ou=people";
        String filter = "(&(objectClass=group)(cn=" + searchGroup + "))";

        SearchControls searchControls = new SearchControls();
        searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);

        List<LdapGroup> groupList = ldapTemplate.search(
                base,
                filter,
                searchControls,
                new LdapGroupContextMapper()
        );

        if(groupList == null || groupList.size() == 0){
            return null;
        }

        return groupList;
    }


    public List<String> getAdGroupMembers(String groupId, String userName) {
        if(StringUtil.isNullOrEmpty(groupId)){
            return null;
        }

        LdapGroup group = getAdGroup(groupId);
        if(group == null){
            return null;
        }

        List<String> userIdList = new ArrayList<>();

        if(group.getMembers() == null || group.getMembers().size() == 0){
            return null;
        }

        for(LdapGroupMember member : group.getMembers()){
            if(!StringUtil.isNullOrEmpty(userName)){
                if(member.getCn().contains(userName)){
                    userIdList.add(member.getInfo());
                }
            }else{
                userIdList.add(member.getInfo());
            }
        }

        return userIdList;
    }


    public boolean addAdUser(String uid, String cn, String sn, String ou, String email) {
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

    public boolean deleteAdUser(String userId) {
        ldapTemplate.unbind(userId);
        //Optional<LdapUserEntity> userEntity = getUser(dn);
        //ldapUserRepository.delete(userEntity.get());
        return true;
    }

    public boolean addUserForGroup( String groupId, String userId) {
        if(StringUtil.isNullOrEmpty(userId) || StringUtil.isNullOrEmpty(groupId)){
            return false;
        }

        LdapUser user = getAdUser(userId);
        if(user == null){
            return false;
        }

        LdapGroup group = getAdGroup(groupId);
        if(group == null){
            return false;
        }

        if(group.getMembers() != null || group.getMembers().size() > 0){
            for(LdapGroupMember member : group.getMembers()){
                if(member.getCn().equals(userId)){
                    log.info("addUserForGroup :: already exists :: userId = [" + userId + "]");
                    return true;
                }
            }
        }

         // uniqueMember 속성에 사용자 DN 추가
         Attribute attr = new BasicAttribute("member", user.getDn());
         ModificationItem item = new ModificationItem(ADD_ATTRIBUTE, attr);

         ldapTemplate.modifyAttributes(group.getDn(), new ModificationItem[]{item});

         return true;
    }

    public boolean delUserFromGroup( String groupId, String userId) {
        if(StringUtil.isNullOrEmpty(userId) || StringUtil.isNullOrEmpty(groupId)){
            return false;
        }

        LdapUser user = getAdUser(userId);
        if(user == null){
            return false;
        }

        LdapGroup group = getAdGroup(groupId);
        if(group == null){
            return false;
        }

        int cnt = 0;
        if(group.getMembers() != null || group.getMembers().size() > 0){
            for(LdapGroupMember member : group.getMembers()){
                if(member.getCn().equals(userId)){
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
         Attribute attr = new BasicAttribute("member", user.getDn());
         ModificationItem item = new ModificationItem(REMOVE_ATTRIBUTE, attr);

         ldapTemplate.modifyAttributes(group.getDn(), new ModificationItem[]{item});

         return true;
    }

    public boolean addObjectClassForGroup(String groupId, String objectClassName) {
        if(StringUtil.isNullOrEmpty(groupId) || StringUtil.isNullOrEmpty(objectClassName)){
            return false;
        }

        List<LdapGroup> groupList = getAdGroups(groupId);
        if(groupList == null || groupList.size() == 0){
            return false;
        }

        LdapGroup group = groupList.get(0);

        Attribute attr = new BasicAttribute("objectClass", objectClassName);
        ModificationItem item = new ModificationItem(ADD_ATTRIBUTE, attr);

        ldapTemplate.modifyAttributes(group.getDn(), new ModificationItem[]{item});

        return true;
    }
}
