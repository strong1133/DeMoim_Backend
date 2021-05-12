package com.demoim_backend.demoim_backend.config.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.demoim_backend.demoim_backend.config.auth.PrincipalDetails;
import com.demoim_backend.demoim_backend.dto.LoginRequestDto;
import com.demoim_backend.demoim_backend.model.ApplyInfo;
import com.demoim_backend.demoim_backend.model.Team;
import com.demoim_backend.demoim_backend.model.User;
import com.demoim_backend.demoim_backend.repository.ApplyInfoRepository;
import com.demoim_backend.demoim_backend.service.ApplyService;
import com.demoim_backend.demoim_backend.service.SignupService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

//스프링 시큐리에서 UsernamePasswordAuthenticationFilter라는 것을 제공해줌
//loginForm.disable 했기 때문에 /login 요청시 이걸 시큐리티 로그인으로 인터셉트 해줘야함.
// UsernamePasswordAuthenticationFilter라는 필터를 걸어서 username, password를 /login으로 Post 요청하면 해당 필터에 걸리게끔 설정.
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private final ApplyInfoRepository applyInfoRepository;
    private static final String SCERET_KEY = "AWDSDV+/asdwzwr3434@#$vvadflf00ood/[das";

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        System.out.println("JwtAuthenticationFilter : 진입");

        ObjectMapper om = new ObjectMapper();
        LoginRequestDto loginRequestDto = null;
        try {
            loginRequestDto = om.readValue(request.getInputStream(), LoginRequestDto.class);
            System.out.println("loginRequestDto :" + loginRequestDto);
        } catch (Exception e) {
            throw new IllegalArgumentException("로그인에 실패하였습니다") {
            };
        }

        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(
                        loginRequestDto.getUsername(),
                        loginRequestDto.getPassword() + SCERET_KEY
                );
        System.out.println("JwtAuthenticationFilter : 토큰생성완료");
        System.out.println("UsernamePasswordAuthenticationToken : " + authenticationToken);

        Authentication authentication = null;

        try {
            authentication = authenticationManager.authenticate(authenticationToken);
        } catch (AuthenticationException e) {
           throw new IllegalArgumentException("로그인이 정보가 잘못 되었습니다.");
        }
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        return authentication;

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authResult) throws IOException, ServletException {


        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        String jwtToken = JWT.create()
                .withSubject(principalDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + JwtProperties.EXPIRATION_TIME))
                .withClaim("id", principalDetails.getUser().getId())
                .withClaim("username", principalDetails.getUser().getUsername())
                .sign(Algorithm.HMAC512(JwtProperties.SECRET));
        System.out.println("jwtToken : " + jwtToken);
        ObjectMapper objectMapper = new ObjectMapper();
        User user = principalDetails.getUser();
        int memberCnt = applyInfoRepository.countByUserIdAndMembershipAndApplyStateAndTeam_ProjectState(user.getId(), ApplyInfo.Membership.MEMBER, ApplyInfo.ApplyState.ACCEPTED,
                Team.StateNow.ACTIVATED)+applyInfoRepository.countByUserIdAndMembershipAndApplyStateAndTeam_ProjectState(user.getId(), ApplyInfo.Membership.MEMBER, ApplyInfo.ApplyState.ACCEPTED,
                Team.StateNow.YET);
        int leadCnt = applyInfoRepository.countByUserIdAndMembershipAndTeam_ProjectState(user.getId(), ApplyInfo.Membership.LEADER, Team.StateNow.ACTIVATED)+
                applyInfoRepository.countByUserIdAndMembershipAndTeam_ProjectState(user.getId(), ApplyInfo.Membership.LEADER, Team.StateNow.YET);

        int nowTeamCnt = memberCnt + leadCnt;

        Map<String, String> users = new HashMap<>();
        users.put("Id", user.getId().toString());
        users.put("Username", user.getUsername());
        users.put("Nickname", user.getNickname());
        users.put("ProfileImage", user.getProfileImage());
        users.put("Description", user.getDescription());
        users.put("Position", user.getPosition());
        users.put("NowTeamCnt", String.valueOf(nowTeamCnt));
        Map<String, List<Long>> applyTeamId = new HashMap<>();

        List<ApplyInfo> applyInfos = applyInfoRepository.findTeamIdByUserIdAndMembershipAndApplyState(user.getId(), ApplyInfo.Membership.MEMBER, ApplyInfo.ApplyState.WAITING);
        List<Long> applyInfoList = new ArrayList<>();
        for(ApplyInfo applyInfo : applyInfos){
            applyInfoList.add(applyInfo.getTeam().getId());
        }
        applyTeamId.put("applyTeamId", applyInfoList );
        System.out.println("applyTeamId :" +applyTeamId );
//        Map<String, Map<String, String>> map = new HashMap<>();
//        Map<String, Map<String, String>> map = new HashMap<>();
        Map<String, Object> map = new HashMap<>();
//        Map<String, Map<String, List<Long>>> map2 = new HashMap<>();
        map.put("userInfo", users);
        map.put("applyTeamId", applyInfoList);
//

        response.addHeader(JwtProperties.HEADER_STRING, JwtProperties.TOKEN_PREFIX + jwtToken);
        response.setContentType("application/json");
//        response.getWriter().write(objectMapper.writeValueAsString(map));
//        response.getWriter().write(objectMapper.writeValueAsString(map2));
        response.getWriter().write(objectMapper.writeValueAsString(map));

    }

}
