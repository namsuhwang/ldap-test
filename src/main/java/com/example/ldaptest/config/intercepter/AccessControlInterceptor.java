package com.example.ldaptest.config.intercepter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


@RequiredArgsConstructor
@Component
public class AccessControlInterceptor implements HandlerInterceptor {

    public static final Logger log = LogManager.getLogger(AccessControlInterceptor.class);


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//
//        String inputJsonString = new String(IOUtils.toByteArray(request.getInputStream()));
//        log.info("inputJsonString=[" + inputJsonString + "]");
//        String ipAddr = WebUtil.getClientIp(request);    // 추가
//
//        String url = request.getRequestURI() == null ? null : request.getRequestURI().substring(4, request.getRequestURI().length() - 1);
//        if (VdUtil.isNotEmpty(request.getRequestURI())
//                && url.length() >= 9
//                && (url.substring(0, 8).equals("/swagger")
//                || url.substring(0, 9).equals("/api-docs")
//                || url.substring(0, 9).equals("/v3/api-d"))) {
//            return true;
//        }
//
//        UriMstSearch uriMstSearch = new UriMstSearch(request.getRequestURI());
//        changeMemberLastContactDt(inputJsonString);
//
//
//        // uriMstSearch.setTrGbCd(EnumFilter.UriTrGb.MGMT);
//        uriMstSearch.setUseYn("Y");
//        UriMstEntity uriMstEntity = uriMstService.getUriMst(uriMstSearch);
//
//        // 등록된 uri 가 없거나 사용여부가 Y 가 아니면 false
//        VdUtil.ec((VdUtil.isEmpty(uriMstEntity)
//                || !uriMstEntity.getUseYn().equalsIgnoreCase("Y")), BIZ_ERR_001130);
//
//        // 접근정책이 ALL 이면 무조건 통과
//        if (VdUtil.isEqual(uriMstEntity.getAcsPolicyCd(), EnumFilter.AcsPolicy.ALL)) {
//            return true;
//        }
//
//        // log.info("IpAddressAccessControlInterceptor.preHandle :: client-ip = [" + ipAddr + "]");
//
//        // 접근정책이 화이트리스트 이면 등록된 IP 만 통과
//        if (VdUtil.isEqual(uriMstEntity.getAcsPolicyCd(), EnumFilter.AcsPolicy.WHITE)
//                && VdUtil.isEmpty(ipFilterService.getIpFilter(new IpFilterSearch(ipAddr)))) {
//            // return false;
//            return true;  // 일단 무조건 통과
//        }

        return true;
    }
}