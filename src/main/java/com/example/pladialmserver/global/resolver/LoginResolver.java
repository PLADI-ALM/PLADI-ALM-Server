package com.example.pladialmserver.global.resolver;

import com.example.pladialmserver.global.Constants;
import com.example.pladialmserver.global.exception.BaseException;
import com.example.pladialmserver.global.exception.BaseResponseCode;
import com.example.pladialmserver.global.utils.JwtUtil;
import com.example.pladialmserver.user.entity.User;
import com.example.pladialmserver.user.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
@Component
public class LoginResolver implements HandlerMethodArgumentResolver {
    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(Account.class) && User.class.equals(parameter.getParameterType());
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {

        // header 에 값이 있는지 확인
        String token = webRequest.getHeader(Constants.JWT.AUTHORIZATION_HEADER);
        if(!StringUtils.hasText(token)) throw new BaseException(BaseResponseCode.NULL_TOKEN);
        // 추출
        token = token.substring(Constants.JWT.BEARER_PREFIX.length());
        // 유효성 검사
        jwtUtil.validateToken(token);
        // 이미 로그아웃 & 회원 탈퇴가 된 토큰인지 확인
        if(!ObjectUtils.isEmpty(jwtUtil.getTokenInRedis(token))) throw new BaseException(BaseResponseCode.BLACKLIST_TOKEN);
        return userRepository.findById(jwtUtil.getUserIdFromJWT(token)).orElseThrow(() -> new BaseException(BaseResponseCode.USER_NOT_FOUND));
    }
}
