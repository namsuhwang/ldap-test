package com.example.ldaptest.models.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.Data;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import javax.naming.Name;
import java.util.List;

@Data
@Entry(base = "ou=groups", objectClasses = { "Group", "top"}) // "groupOfUniqueNames",
public class LdapGroupEntity {
    @Id
    private Name dn;

    // fullName
    @Attribute(name = "cn")
    private String cn;

    // surName
    @Attribute(name = "sn")
    private String sn;

    // surName
    @Attribute(name = "groupType")
    private String groupType;

    @Attribute(name = "objectclass")
    private List<String> objectClassList;

    // 멤버의 DN　목록
    @Attribute(name = "member")
    private List<String> members;

    // Getter for dn (Serialized as String)
    @JsonGetter("dn")
    public String getDnAsString() {
        return dn != null ? dn.toString() : null;
    }

    public void setDn(Name dn) {
        this.dn = dn;
    }

}
