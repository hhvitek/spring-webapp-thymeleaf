package app.spring_webapp_thymeleaf.controllers;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**<pre>
 * this is application default /error endpoint controller.
 * If for example an unknown url endpoint is requested, this default error page is served instead.
 *
 * Request are redirected using web.xml configuration
 *     <error-page>
 *         <location>/error</location>
 *     </error-page>
 *</pre>
 */
@Controller
@RequestMapping("/error")
public class ErrorController{

    @GetMapping
    public String showErrorPage(HttpServletRequest request, Model model) {
        model.addAttribute("originalUri", getOriginalRequestUriFromRequest(request));
        model.addAttribute("message", getErrorMessageFromRequest(request));
        model.addAttribute("exception", getErrorExceptionFromRequest(request));

        return "pages/error";
    }

    private String getOriginalRequestUriFromRequest(HttpServletRequest request) {
        String originalUri = String.valueOf(request.getAttribute(RequestDispatcher.FORWARD_REQUEST_URI));

        if (originalUri.equals("null")) {
            originalUri = request.getRequestURI();
        }

        return originalUri;
    }

    private String getErrorMessageFromRequest(HttpServletRequest request) {
        Object exceptionNullable = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        if (exceptionNullable == null) {
            return null;
        } else {
            return exceptionNullable.toString();
        }
    }

    private String getErrorExceptionFromRequest(HttpServletRequest request) {
        Object exceptionNullable = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);
        return getRootCauseMessageFromExceptionIfExists(exceptionNullable)
                .orElse(null);
    }

    private Optional<String> getRootCauseMessageFromExceptionIfExists(Object exceptionNullable) {
        if (exceptionNullable != null) {
            Exception exception =  (Exception) exceptionNullable;
            return Optional.of(ExceptionUtils.getRootCauseMessage(exception));
        } else
            return Optional.empty();
    }
}