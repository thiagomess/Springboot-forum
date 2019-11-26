package br.com.alura.forum.config.validacao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;


//Classe cria um inteceptador, para cada erro que acontecer com os BeanValidation, entrará nessa classe
//e retornará um json mais claro e simples do campo e o erro que aconteceu
@RestControllerAdvice
public class ErroDeValidacaoHandler {

    @Autowired
    private MessageSource messageSource; //Com o erro e o locale, retorna a mensagem no idioma solicitado

    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public List<ErroDeFormularioDto> handle(MethodArgumentNotValidException exception) {
        List<ErroDeFormularioDto> dto = new ArrayList<>();
        final List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();

        fieldErrors.forEach(e -> {
            final String message = messageSource.getMessage(e, LocaleContextHolder.getLocale());
            dto.add(new ErroDeFormularioDto(e.getField(), message));
        });
        return dto;
    }


    @ResponseStatus(code = HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException.class)
    public ErroNotFound notFound(EntityNotFoundException exception) {
        return new ErroNotFound("Nenhum dado encontrado para o ID informado");
    }
    
    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<StandardError> authorization(AuthorizationException e, HttpServletRequest request){
        StandardError err = new StandardError(HttpStatus.FORBIDDEN.value(), e.getMessage(), LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(err);
    }
    
    

}
