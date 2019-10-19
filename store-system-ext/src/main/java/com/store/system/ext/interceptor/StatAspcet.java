package com.store.system.ext.interceptor;


import com.quakoo.baseFramework.json.JsonUtils;
import com.quakoo.webframework.MappingResponseJsonView;
import com.store.system.model.User;
import com.store.system.util.UserUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Aspect
@Component("jobStatAspcet")
public class StatAspcet {

    private Logger statLogger = LoggerFactory.getLogger("statistic");
    private Logger logger = LoggerFactory.getLogger(StatAspcet.class);

    ThreadLocal<String> requestArgs = new ThreadLocal<String>();
    ThreadLocal<String> requestFunction = new ThreadLocal<String>();
    ThreadLocal<Long> requestTime = new ThreadLocal<Long>();

//    @Resource
//    private OptLogService optLogService;


    private void initValues() {
        requestFunction.set("unknown");
        requestTime.set(System.currentTimeMillis());
        if (requestArgs.get() == null) {
            requestArgs.set("");
        }
    }

    public void setRequestValue(String requestArgs){
        this.requestArgs.set(requestArgs);
    }

    @Around("execution(public * com.store.system.ext.*Controller.*(..))")
    public Object logStat(ProceedingJoinPoint joinPoint) throws Throwable {
        initValues();
        try {
            Object[] arg = joinPoint.getArgs();
            HttpServletResponse response = null;
            HttpServletRequest request = null;
            StringBuilder reqStr = new StringBuilder("");
            for (Object o : arg) {
                if (o instanceof HttpServletResponse) {
                    response = (HttpServletResponse) o;
                } else if (o instanceof HttpServletRequest) {
                    request = (HttpServletRequest) o;
                } else if (o instanceof Model) {
                    //no
                } else if (o instanceof byte[]) {
                    //no
                } else if (o instanceof MultipartFile) {
                    //no
                } else {
                    reqStr.append(",");
                    reqStr.append(toJsonString(o));
                }
            }

            requestArgs.set(reqStr.toString());
            if (request == null) {
                request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            }
            if (joinPoint.getSignature().getName() != null) {
                String classType = joinPoint.getTarget().getClass().getName();
                Class<?> clazz = Class.forName(classType);
                String clazzSimpleName = clazz.getSimpleName();
                requestFunction.set(clazzSimpleName+"."+joinPoint.getSignature().getName());
            }
            Object result = joinPoint.proceed();
            String logResult = getResultFromResult(result);
            String stat = getStatFromResponseAndException(response, null);
            logMessage(logResult, stat);

            User user = UserUtils.getUser(request);
            if(user!=null){
//                saveOptLog(requestFunction.get(), user.getId(), reqStr.toString());
            }

            return result;
        } catch (Throwable throwable) {
            //logger.error("[StatAspcet.logStat]" + ExceptionUtils.getFullStackTrace(throwable));
            throw throwable;
        }

    }


    @Around("execution(public * com.quakoo.webframework.BaseController.exp(..))")
    public Object logStatWithExp(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            HttpServletResponse response = null;
            HttpServletRequest request = null;
            Exception exception = null;
            Object[] arg = joinPoint.getArgs();
            for (Object o : arg) {
                if (o instanceof HttpServletResponse) {
                    response = (HttpServletResponse) o;
                } else if (o instanceof HttpServletRequest) {
                    request = (HttpServletRequest) o;
                } else if (o instanceof Exception) {
                    exception = (Exception) o;
                }
            }
            Object result = joinPoint.proceed();
            String logResult = getResultFromResult(result);
            String stat = getStatFromResponseAndException(response, exception);
            logMessage(logResult, stat);
            return result;

        } catch (Throwable throwable) {
            logger.error("[StatAspcet.logStatWithExp]" + ExceptionUtils.getFullStackTrace(throwable));
            return null;
        }

    }


    private void logMessage(String logResult, String stat) throws IOException {
        long time=0;
        if(requestTime.get()!=null){
            time=System.currentTimeMillis() - requestTime.get();
        }
        statLogger.info("func:{},stat:{},time:{},req:{},res:{}", new Object[]{requestFunction.get(), stat,
                time, requestArgs.get(), logResult});


    }


    private String getStatFromResponseAndException(HttpServletResponse response, Exception exception) {
        String stat = "";
        if (response != null) {
            stat = Integer.toString(response.getStatus());
            if (exception != null) {
                stat = stat + "," + exception.getClass().getSimpleName();
            }
        }
        return stat;
    }

    private String getResultFromResult(Object object) throws IOException {
        String result = "";
        if (object != null) {
            if (object instanceof ModelAndView) {
                result = getResultFromModelAndView((ModelAndView) object);
            } else if (object instanceof String) {
                result = (String) object;
            } else {
                result = JsonUtils.format(object);
            }
        }
        return result;
    }

    private String getResultFromModelAndView(ModelAndView modelAndView) throws IOException {
        String result = "";
        if (modelAndView.getView() instanceof MappingResponseJsonView) {
            Object jsonResult = modelAndView.getModel().get(MappingResponseJsonView.resultKey);
            if (jsonResult != null) {
                result = JsonUtils.format(jsonResult);
            }
        } else {
            //TODO 支持其他非json视图类型
            //取出model
            //过滤掉非结果参数
        }
        return result;
    }

    private String toJsonString(Object o) throws IOException {
        if (o == null) {
            return "";
        }
        if (o instanceof String) {
            String result=(String) o;
            if(result.length()>200){
                return result.substring(0,200);
            }
            return (String) o;
        }
        return JsonUtils.format(o);
    }


    /**
     * 记录后台操作日志
     * @param action
     * @param uid
     * @param args
     * @throws Exception
     */
//    private void saveOptLog(String action, long uid, String args) throws Exception {
//        OptLog optLog = new OptLog();
//        optLog.setAction(action);
//        optLog.setUid(uid);
//        optLog.setArgs(args);
//        long time = System.currentTimeMillis();
//        optLog.setTime(time);
//        optLogService.insert(optLog);
//    }



}