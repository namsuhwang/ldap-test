package com.example.ldaptest.common.constant;

import lombok.Getter;

public class IcaclsCodeConstant {

    @Getter
    public enum ACCESS_CONTROL_ENTRY implements EnumCode {

        OI("OBJECT_INHERIT"),             // 하위 파일에 상속함
        CONTAINER_INHERIT("CI"),          // 하위 폴더에 상속함
        INHERIT_ONLY("IO"),               // 상속 전용, 현재 객체에는 적용되지 않음
        DO_NOT_PROPAGATE_INHERIT("NP"),   // 하위 객체에는 적용하되 더 아래로는 상속하지 않음
        INHERIT_ACE("ID")                 // 상속된 ACE, 수동으로 설정한 것이 아님 (읽기 전용 표시)
        ;

        private String code;

        private ACCESS_CONTROL_ENTRY(String code) {
            this.code = code;
        }

        @Override
        public boolean equals(String code) {
            return this.code.equals(code);
        }
    }

    @Getter
    public enum DIR_AUTH implements EnumCode {

        FULL_CONTROLLER("F"),
        MODIFY("M"),
        READ_AND_EXECUTE("RX"),
        READ("R"),
        WRITE("W"),
        LIST_FOLDER_CONTENTS("L"),
        DELETE("D"),
        NO_ACCESS("N");

        private String code;

        private DIR_AUTH(String code) {
            this.code = code;
        }

        @Override
        public boolean equals(String code) {
            return this.code.equals(code);
        }
    }
}
