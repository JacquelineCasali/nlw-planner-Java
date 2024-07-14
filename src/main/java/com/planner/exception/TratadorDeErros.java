package com.planner.exception;

import jakarta.persistence.EntityNotFoundException;
import org.aspectj.bridge.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TratadorDeErros {


    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity tratarError404(){
        return ResponseEntity.notFound().build();
    }


//    tratarError400
@ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity tratarError400(MethodArgumentNotValidException ex){
    var erros =ex.getFieldErrors();
    return  ResponseEntity.badRequest().body(erros.stream().map(DadosErrosValidacao::new).toList());
    }


    @ExceptionHandler(ValidacaoException.class)
    public ResponseEntity tratarErroRegrasDeNegocios(ValidacaoException ex){
        return  ResponseEntity.badRequest().body("Message: " + ex.getMessage());
    }
    @ExceptionHandler(Exception.class)
    public ResponseEntity tratarErro500(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro: " +ex.getLocalizedMessage());
    }
    private  record  DadosErrosValidacao(String campo, String mensagem){

        public  DadosErrosValidacao(FieldError error){
            this(error.getField(), error.getDefaultMessage());
        }
    }

}
