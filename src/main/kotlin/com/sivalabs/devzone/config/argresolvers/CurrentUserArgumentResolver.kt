package com.sivalabs.devzone.config.argresolvers

import com.sivalabs.devzone.common.annotations.CurrentUser
import com.sivalabs.devzone.users.services.SecurityService
import org.springframework.core.MethodParameter
import org.springframework.core.annotation.AnnotationUtils
import org.springframework.lang.Nullable
import org.springframework.stereotype.Component
import org.springframework.web.bind.support.WebDataBinderFactory
import org.springframework.web.context.request.NativeWebRequest
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.method.support.ModelAndViewContainer

@Component
class CurrentUserArgumentResolver(private val securityService: SecurityService) : HandlerMethodArgumentResolver {
    override fun supportsParameter(methodParameter: MethodParameter): Boolean {
        return findMethodAnnotation(CurrentUser::class.java, methodParameter) != null
    }

    override fun resolveArgument(
        methodParameter: MethodParameter,
        @Nullable mavContainer: ModelAndViewContainer?,
        nativeWebRequest: NativeWebRequest,
        @Nullable binderFactory: WebDataBinderFactory?,
    ): Any? {
        return securityService.loginUser()
    }

    private fun <T : Annotation?> findMethodAnnotation(
        annotationClass: Class<T>,
        parameter: MethodParameter,
    ): T? {
        var annotation = parameter.getParameterAnnotation(annotationClass)
        if (annotation != null) {
            return annotation
        }
        val annotationsToSearch: Array<Annotation> = parameter.parameterAnnotations

        for (toSearch in annotationsToSearch) {
            annotation = AnnotationUtils.findAnnotation(toSearch.annotationClass.java, annotationClass)
            if (annotation != null) {
                return annotation
            }
        }
        return null
    }
}
