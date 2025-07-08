package com.gymmanagement.gymapp.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;

/**
 * Manejador global de excepciones para la aplicación.
 * Centraliza el manejo de errores y proporciona respuestas consistentes.
 * 
 * @author Gym Management System
 * @version 1.0
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);
    
    private static final String ERROR_VIEW = "error/error";
    private static final String ERROR_MESSAGE_ATTR = "errorMessage";
    private static final String ERROR_DETAILS_ATTR = "errorDetails";
    private static final String REQUEST_URL_ATTR = "requestUrl";

    /**
     * Maneja excepciones generales de runtime.
     * 
     * @param ex Excepción lanzada
     * @param request Solicitud HTTP
     * @return Vista de error con información relevante
     */
    @ExceptionHandler(RuntimeException.class)
    public ModelAndView handleRuntimeException(RuntimeException ex, HttpServletRequest request) {
        logger.error("Error de runtime en la aplicación: {}", ex.getMessage(), ex);
        
        ModelAndView modelAndView = new ModelAndView(ERROR_VIEW);
        modelAndView.addObject(ERROR_MESSAGE_ATTR, "Ha ocurrido un error en la aplicación");
        modelAndView.addObject(ERROR_DETAILS_ATTR, ex.getMessage());
        modelAndView.addObject(REQUEST_URL_ATTR, request.getRequestURL().toString());
        modelAndView.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        
        return modelAndView;
    }

    /**
     * Maneja excepciones de acceso no autorizado.
     * 
     * @param ex Excepción de acceso
     * @param request Solicitud HTTP
     * @return Vista de error de acceso
     */
    @ExceptionHandler(SecurityException.class)
    public ModelAndView handleSecurityException(SecurityException ex, HttpServletRequest request) {
        logger.warn("Intento de acceso no autorizado: {}", ex.getMessage());
        
        ModelAndView modelAndView = new ModelAndView(ERROR_VIEW);
        modelAndView.addObject(ERROR_MESSAGE_ATTR, "Acceso no autorizado");
        modelAndView.addObject(ERROR_DETAILS_ATTR, "No tiene permisos para acceder a este recurso");
        modelAndView.addObject(REQUEST_URL_ATTR, request.getRequestURL().toString());
        modelAndView.setStatus(HttpStatus.FORBIDDEN);
        
        return modelAndView;
    }

    /**
     * Maneja excepciones generales no capturadas.
     * 
     * @param ex Excepción general
     * @param request Solicitud HTTP
     * @return Vista de error general
     */
    @ExceptionHandler(Exception.class)
    public ModelAndView handleGeneralException(Exception ex, HttpServletRequest request) {
        logger.error("Error inesperado en la aplicación: {}", ex.getMessage(), ex);
        
        ModelAndView modelAndView = new ModelAndView(ERROR_VIEW);
        modelAndView.addObject(ERROR_MESSAGE_ATTR, "Ha ocurrido un error inesperado");
        modelAndView.addObject(ERROR_DETAILS_ATTR, "Por favor, contacte al administrador del sistema");
        modelAndView.addObject(REQUEST_URL_ATTR, request.getRequestURL().toString());
        modelAndView.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        
        return modelAndView;
    }
}