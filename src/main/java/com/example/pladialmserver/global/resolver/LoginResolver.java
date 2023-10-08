package com.example.pladialmserver.global.resolver;

import com.example.pladialmserver.global.Constants;
import com.example.pladialmserver.global.exception.BaseException;
import com.example.pladialmserver.global.exception.BaseResponseCode;
import com.example.pladialmserver.global.utils.JwtUtil;
import com.example.pladialmserver.user.entity.User;
import com.example.pladialmserver.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
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
        token = token.replace(Constants.JWT.BEARER_PREFIX, "");
        // 유효성 검사
        jwtUtil.validateToken(token);
        // todo : redis blacklist 토큰 확인 필요
        return userRepository.findById(jwtUtil.getUserIdFromJWT(token)).orElseThrow(() -> new BaseException(BaseResponseCode.USER_NOT_FOUND));
    }
}
