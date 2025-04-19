package com.example.ldaptest.models.entity;

import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.Data;
import org.springframework.ldap.odm.annotations.Attribute;
import org.springframework.ldap.odm.annotations.Entry;
import org.springframework.ldap.odm.annotations.Id;

import javax.naming.Name;
import javax.naming.ldap.LdapName;
import java.util.List;

@Data
@Entry(base = "ou=people", objectClasses = {"inetOrgPerson", "organizationalPerson", "person", "top"})
public class LdapUserEntity {

    @Id
    private Name dn;

    // Full Name
    @Attribute(name = "cn")
    private String cn;

    // First Name
    @Attribute(name = "uid")
    private String uid;

    // Sur Name
    @Attribute(name = "sn")
    private String sn;

    // Organizational Unit
    @Attribute(name = "ou")
    private String ou;

    // Domain Component
    @Attribute(name = "dc")
    private String dc;

    @Attribute(name = "mail")
    private String mail;

//    @Attribute(name = "objectclass")
//    private List<String> objectClassList;

    // Getter for dn (Serialized as String)
    @JsonGetter("dn")
    public String getDnAsString() {
        return dn != null ? dn.toString() : null;
    }

    public void setDn(Name dn) {
        this.dn = dn;
    }

}

